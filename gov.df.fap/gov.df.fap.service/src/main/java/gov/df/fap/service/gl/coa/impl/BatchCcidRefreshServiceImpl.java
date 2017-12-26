package gov.df.fap.service.gl.coa.impl;

import gov.df.fap.api.gl.coa.IBatchCcidRefreshService;
import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.service.gl.core.DaoSupport;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量刷新CCID功能服务类
 * @author LiuYan
 * @version 2017-03-06
 */
public class BatchCcidRefreshServiceImpl implements IBatchCcidRefreshService {

  private static final String COA_ID = "coa_id";

  private static final String NEW_CCID = "new_ccid";

  private static final String OLD_CCID = "old_ccid";

  private static final String QUERY_CCID_TABLE_SQL = "select * from gl_batch_ccid_table";

  private static final String INSERT_CCID_TABLE_SQL = "insert into gl_batch_ccid_table(table_name,remark) values(#table_name#, #remark#)";

  private static final String DELETE_CCID_TABLE_SQL = "delete gl_batch_ccid_table";

  private static final String QUERY_SINGLE_CCID_SQL = "select * from gl_ccids where ccid=?";

  private static final String INSERT_BATCH_CCID_HIS_SQL = "insert into gl_batch_ccid_his(old_ccid,new_ccid) values (#old_ccid#, #new_ccid#)";

  private static final String QUERY_CCID_TABLE_COUNT_SQL = "select count(1) num from ";

  private static final String UPDATE_CCID_SQL = " a set ccid=(select new_ccid from gl_batch_ccid_his b where a.ccid=b.old_ccid) "
    + "where exists (select 1 from gl_batch_ccid_his b where a.ccid=b.old_ccid)";

  private static final String DELTE_CCID_HIS_SQL = "delete gl_batch_ccid_his";

  private static final String QUERY_FIELD_SQL = "select cname from sys.col where tname=?";

  private static final String QUERY_COA_SQL = "select coa_id from gl_ccids where ccid=?";

  private static final int CALCULATE_COUNT = 5000;

  private List tableList = null;

  private DaoSupport daoSupport = null;

  private ICoaService coaService = null;

  public void setCoaService(ICoaService coaService) {
    this.coaService = coaService;
  }

  public void setDaoSupport(DaoSupport daoSupport) {
    this.daoSupport = daoSupport;
  }

  public List getRefreshCcidTables() {
    if (tableList == null)
      tableList = daoSupport.genericQuery(QUERY_CCID_TABLE_SQL, XMLData.class);
    return tableList;
  }

  public void saveRefreshCcidTables(List tableList) {
    daoSupport.execute(DELETE_CCID_TABLE_SQL);
    daoSupport.batchExcute(INSERT_CCID_TABLE_SQL, tableList);
    this.tableList = tableList;
  }

  public void calculateCcid(String calculateModel, Object calParam, boolean isResetCcid) throws Exception {
    daoSupport.execute(DELTE_CCID_HIS_SQL);
    if (calculateModel.equals(CALCULATE_SINGLE_CCID_MODEL))
      calculateSingleCcid(calParam.toString(), isResetCcid);
    else if (calculateModel.equals(CALCULATE_TABLE_MODEL))
      calculateTableCcid(calParam.toString(), isResetCcid);
    else if (calculateModel.equals(CALCULATE_GL_CCID_MODEL))
      calculateCcid(isResetCcid);
    else if (calculateModel.equals(CALCULATE_BUSINESS_MODEL))
      calculateCcid((List) calParam, isResetCcid);
  }

  /**
   * 计算单条CCID
   * @param ccid
   * @param isResetCcid
   * @throws Exception
   */
  private void calculateSingleCcid(String ccid, boolean isResetCcid) throws Exception {
    final Map tmpMap = new HashMap();
    List ccidList = daoSupport.genericQuery(QUERY_SINGLE_CCID_SQL, new Object[] { ccid }, FVoucherDTO.class);
    if (ccidList.isEmpty())
      throw new Exception("在gl_ccids表中没有ccid是" + ccid + "的记录，请重试!");
    tmpMap.put(OLD_CCID, ccid);
    coaService.getCcidBatch(ccidList);
    tmpMap.put(NEW_CCID, ((FVoucherDTO) ccidList.get(0)).getCcid());
    List paramList = new ArrayList(1);
    paramList.add(tmpMap);
    daoSupport.batchExcute(INSERT_BATCH_CCID_HIS_SQL, paramList);
    if (isResetCcid)
      resetCcid();
  }

  /**
   * 按表计算CCID
   * @param tableName
   * @param isResetCcid
   * @throws Exception
   */
  private void calculateTableCcid(String tableName, boolean isResetCcid) throws Exception {
    checkTable(tableName);
    int count = Integer.parseInt(((Map) daoSupport.genericQuery(QUERY_CCID_TABLE_COUNT_SQL + tableName, HashMap.class)
      .get(0)).get("num").toString());
    int sec = count / CALCULATE_COUNT + ((count % CALCULATE_COUNT == 0) ? 0 : 1);

    StringBuffer querySql = new StringBuffer();
    List genList = null;
    Map tmpMap = null;
    List ccidList = new ArrayList();
    try {
      for (int i = 0; i < sec; i++) {
        querySql.append("select * from (select row_.*,rownum num from ");
        querySql.append("(select * from ").append(tableName).append(") row_");
        querySql.append(" where rownum<=?) as a").append(" where num>?");
        genList = daoSupport.genericQuery(querySql.toString(),
          new Object[] { String.valueOf(CALCULATE_COUNT), String.valueOf(i * CALCULATE_COUNT) }, FVoucherDTO.class);
        for (int j = 0; j < genList.size(); j++)
          ((FVoucherDTO) genList.get(j)).setRemark(((FVoucherDTO) genList.get(j)).getCcid());
        coaService.getCcidBatch(genList);
        for (int j = 0; j < genList.size(); j++) {
          tmpMap = new HashMap();
          tmpMap.put(OLD_CCID, ((FVoucherDTO) genList.get(j)).getRemark());
          tmpMap.put(NEW_CCID, ((FVoucherDTO) genList.get(j)).getCcid());
          ccidList.add(tmpMap);
        }
        daoSupport.batchExcute(INSERT_BATCH_CCID_HIS_SQL, ccidList);
        genList.clear();
        ccidList.clear();
      }
      if (isResetCcid)
        resetCcid();
    } catch (Exception ex) {
      throw ex;
    }
  }

  /**
   * 校验表名是否存在
   * @param tableName
   */
  private void checkTable(String tableName) {
    daoSupport.execute("select 1 from " + tableName);
  }

  /**
   * 重新计算CCID表所有CCID
   * @param isResetCcid
   * @throws Exception
   */
  private void calculateCcid(boolean isResetCcid) throws Exception {
    calculateTableCcid("gl_ccids", isResetCcid);
  }

  /**
   * 重新计算CCID
   * @param dto
   * @param isResetCcid
   * @throws Exception
   */
  private void calculateCcid(List dataList, boolean isResetCcid) throws Exception {
    List ccidList = new ArrayList(dataList.size());
    XMLData tmpData = null;
    //补coa_id信息
    for (int i = 0; i < dataList.size(); i++) {
      tmpData = (XMLData) dataList.get(i);
      tmpData.put(OLD_CCID, tmpData.get("ccid"));
      String coaId = ((XMLData) daoSupport.genericQuery(QUERY_COA_SQL, new Object[] { tmpData.get("ccid") },
        XMLData.class).get(0)).get(COA_ID).toString();
      tmpData.put(COA_ID, coaId);
      ccidList.add(tmpData);
    }
    coaService.getCcidBatch(ccidList);
    for (int i = 0; i < dataList.size(); i++)
      ((XMLData) ccidList.get(i)).put(NEW_CCID, ((XMLData) ccidList.get(i)).get("ccid"));
    daoSupport.batchExcute(INSERT_BATCH_CCID_HIS_SQL, ccidList);
    if (isResetCcid)
      resetCcid();
  }

  private void resetCcid() {
    XMLData tmpData = null;
    for (int i = 0; i < tableList.size(); i++) {
      String sql = "update ";
      tmpData = (XMLData) tableList.get(i);
      sql += tmpData.get("table_name").toString();
      daoSupport.execute(sql + UPDATE_CCID_SQL);
    }
  }

  /**
   * 获取列名
   */
  public List getTableColumn(String tableName) {
    return daoSupport.genericQuery(QUERY_FIELD_SQL, new Object[] { tableName }, XMLData.class);
  }

  /**
   * 查询
   */
  public List getBusnessData(String tableName, String condition) {
    String querySql = "select * from vw_" + tableName
      + (StringUtil.isEmpty(condition) ? StringUtil.EMPTY : (" where " + condition));
    return daoSupport.genericQuery(querySql, XMLData.class);
  }
}
