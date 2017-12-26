package gov.df.fap.service.gl.balance.impl;

import gov.df.fap.api.gl.balance.RefreshBalanceHandler;
import gov.df.fap.bean.gl.balance.TransRecord;
import gov.df.fap.bean.gl.dto.CtrlRecordDTO;
import gov.df.fap.bean.gl.dto.FCtrlRecordDTO;
import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.bean.util.FBaseDTO;
import gov.df.fap.service.gl.balance.IBalanceDao;
import gov.df.fap.service.gl.configure.impl.EngineConfiguration;
import gov.df.fap.service.gl.core.DaoSupport;
import gov.df.fap.service.gl.core.daosupport.DefaultBeanMapper;
import gov.df.fap.service.gl.core.interfaces.ResultSetMapper;
import gov.df.fap.service.util.datasource.SQLUtil;
import gov.df.fap.service.util.gl.balance.BalanceUtil;
import gov.df.fap.service.util.gl.core.AbstractBeanMapper;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.Properties.PropertiesUtil;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 默认额度刷新辅助类 - 指标文号替换
 * @author LiuYan
 */
public class DefaultRefreshBalanceHandler implements RefreshBalanceHandler {

  private List refreshBudgetData = null;

  private List refreshPayData = null;

  private List refreshBalanceData = null;

  private IBalanceDao balanceDao = null;

  private DaoSupport daoSupport = null;

  private final String transTableName = "GL_BALANCE_BUDGET_FILE_TRANS";

  private String tableName;

  private String whereSql;

  public static final String tempTraceTable = "gl_balance_year_begin_tmp";

  public String[] refreshElementFields = { "file_id", "budget_vou_id" };

  public static final String FRESH_MONEY = "use_money";

  private List refreshBudgetPositiveData = null;

  private List refreshBudgetNegativeData = null;

  final String QUERY_POSITIVE_BALANCE = "select gb.* from budget_control_replace replace, gl_balance gb, gl_ccids cc"
    + " where replace.control_id = gb.sum_id and gb.ccid = cc.ccid";

  public DefaultRefreshBalanceHandler(IBalanceDao balanceDao, DaoSupport daoSupport, String tableName, String whereSql) {
    this.balanceDao = balanceDao;
    this.daoSupport = daoSupport;
    this.tableName = tableName;
    this.whereSql = whereSql;
  }

  public void initRefreshData() {
    initRefreshFields();
    initBudgetData();
    initTraceTmpData();
    initPayData();
    initBalanceData();
  }

  private void initRefreshFields() {
    String refreshElements = (String) SessionUtil.getParaMap().get("refreshElements");
    if (!StringUtil.isEmpty(refreshElements)) {
      String[] eleArray = refreshElements.split(",");
      for (int i = 0; i < eleArray.length; i++) {
        eleArray[i] = eleArray[i].toLowerCase() + "_id";
      }
      refreshElementFields = eleArray;
    }
  }

  private void initBudgetData() {
    /**
     * 上面注掉的代码 是老的年初数替换模式
     * 原来的模式: 通过sys_userpara表refreshElements刷新字段,算出正式指标额度的ccid,并生成额度
     * 改后的模式: 通过关联budget_control_replace表中budget_sum_id(指标已给出正式额度的id)关联;
     * 			 并且为了兼容旧的指标系统提供的budget_sum_id为空情况，则使用budget_id作为正式指标额度id,不再重新计算
     */
    final StringBuffer buffer = new StringBuffer();//daoSupport.genericQuery("select * from budget_control_replace", HashMap.class)
    buffer.append("select gb.* from ").append(tableName).append(" replace, gl_balance gb, gl_ccids cc")
      .append(" where replace.control_id = gb.sum_id and gb.ccid = cc.ccid").append(whereSql)
      .append(" order by replace.sum_id");
    refreshBudgetNegativeData = daoSupport.genericQuery(buffer.toString(), CtrlRecordDTO.class);
    buffer.delete(0, buffer.length());
    buffer
      .append("select gb.* from ")
      .append(tableName)
      .append(" replace, gl_balance gb, gl_ccids cc")
      .append(
        " where " + SQLUtil.replaceNvl("nvl(replace.budget_sum_id,replace.budget_id)")
          + "= gb.sum_id and gb.ccid = cc.ccid").append(whereSql).append(" order by replace.sum_id");
    refreshBudgetPositiveData = daoSupport.genericQuery(buffer.toString(), CtrlRecordDTO.class);
  }

  /**
   * 获取刷新额度的所有下游追溯对象
   * @return
   */
  public List getRefreshPayList() {
    return refreshPayData;
  }

  //供后面刷新表层ccid
  private void initPayData() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("select m.*,cc.* from (select distinct j.vou_id, j.ccid");
    for (int i = 0; i < refreshElementFields.length; i++) {
      buffer.append(",tmp").append(".").append(refreshElementFields[i]).append(" as new_")
        .append(refreshElementFields[i]);
    }
    buffer
      .append(" from gl_journal j, gl_balance_trace t, ")
      .append(tempTraceTable)
      .append(
        " tmp  where j.vou_id = t.vou_id and j.vou_type_id = t.vou_type_id and j.set_year=t.set_year and j.rg_code=t.rg_code and tmp.ctrlid = t.ctrlid")
      .append(" and t.ctrl_side = 1 and j.is_valid=1) m inner join gl_ccids cc on cc.ccid=m.ccid");
    refreshPayData = daoSupport.genericQuery(buffer.toString(), new ResultSetMapper() {
      AbstractBeanMapper bm = null;

      public Object handleRow(ResultSet rs) throws SQLException {
        if (bm == null)
          bm = DefaultBeanMapper.beanMapperFactory(buffer.toString(), rs.getMetaData(), FVoucherDTO.class);
        final FVoucherDTO returnOne = (FVoucherDTO) bm.resultSetToObject(rs);
        doRefreshElement(returnOne, rs);
        returnOne.setRemark(rs.getString("ccid"));
        return returnOne;
      }
    });
  }

  /**
   * 供后面刷新额度表ccid
   */
  private void initBalanceData() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("select * from (select tmp.ctrlid sum_id ");
    for (int i = 0; i < refreshElementFields.length; i++) {
      buffer.append(",tmp").append(".").append(refreshElementFields[i]).append(" as ").append("new_")
        .append(refreshElementFields[i]);
    }
    buffer.append(",(select distinct ccid ").append(" from gl_balance where sum_id = tmp.ctrlid union")
      .append(" select ccid from gl_balance_month_detail where sum_id = tmp.ctrlid) ccid from ").append(tempTraceTable)
      .append(" tmp where tmp.batch>1) rst inner join gl_ccids cc on cc.ccid = rst.ccid");
    refreshBalanceData = daoSupport.genericQuery(buffer.toString(), new ResultSetMapper() {
      AbstractBeanMapper bm = null;

      public Object handleRow(ResultSet rs) throws SQLException {
        if (bm == null)
          bm = DefaultBeanMapper.beanMapperFactory(buffer.toString(), rs.getMetaData(), FCtrlRecordDTO.class);
        final FCtrlRecordDTO returnOne = (FCtrlRecordDTO) bm.resultSetToObject(rs);
        doRefreshElement(returnOne, rs);
        return returnOne;
      }
    });
  }

  /**
   * 初始化追溯临时表,将指标替换数额度id作为条件在追溯表中查询出使用该指标的下游数据对象
   * @return
   */
  private void initTraceTmpData() {
    StringBuffer bf = new StringBuffer();
    bf.append("insert into ").append(tempTraceTable).append("(ctrlid, batch");
    for (int i = 0; i < refreshElementFields.length; i++) {
      bf.append(",").append(refreshElementFields[i]);
    }
    bf.append(") select rep.sum_id, 1 ");
    for (int i = 0; i < refreshElementFields.length; i++) {
      bf.append(",rep").append(".").append(refreshElementFields[i]);
    }
    bf.append(" from budget_control_replace rep ").append("where 1 = 1 ").append(whereSql);
    daoSupport.execute(bf.toString());
    int index = 1;
    int result = 0;
    do {
      bf = new StringBuffer();
      bf.append("insert into ").append(tempTraceTable).append("(ctrlid,batch");
      for (int i = 0; i < refreshElementFields.length; i++) {
        bf.append(",").append(refreshElementFields[i]);
      }
      bf.append(") select distinct t.ctrlid, ").append(++index);
      for (int i = 0; i < refreshElementFields.length; i++) {
        bf.append(",r.").append(refreshElementFields[i]);
      }

      bf.append(" from gl_balance_trace t,")
        .append(tempTraceTable)
        .append(" r where r.batch=")
        .append(index - 1)
        .append(" and t.ctrl_side=0 and exists(select 1 ")
        .append(
          "from gl_balance_trace t1 where t1.vou_id=t.vou_id and t1.vou_type_id=t.vou_type_id and r.ctrlid=t1.ctrlid ")
        .append("and t1.ctrl_side=1)");
      result = daoSupport.executeUpdate(bf.toString());
    } while (result > 0);
  }

  public FCtrlRecordDTO getFCtrlRecordDTO(int index) {
    return (FCtrlRecordDTO) refreshBudgetData.get(index);
  }

  public void afterRefreshBalance(List transList) {
    //更新fromctrlid
    List streamList = EngineConfiguration.getConfig().getBudgetAssociateStream();
    Iterator iterator = streamList.iterator();
    StreamAssociate associate;
    while (iterator.hasNext()) {
      associate = (StreamAssociate) iterator.next();
      associate.updateSurfaceData(transList);
    }

    //更新底层ccid
    streamList = EngineConfiguration.getConfig().getTraceBalanceAssociateStream();
    iterator = streamList.iterator();
    while (iterator.hasNext()) {
      associate = (StreamAssociate) iterator.next();
      associate.updateTraceBalanceCcid(refreshBalanceData);
    }
    //更新表层ccid
    streamList = EngineConfiguration.getConfig().getTraceSurfaceAssociateStream();
    iterator = streamList.iterator();
    while (iterator.hasNext()) {
      associate = (StreamAssociate) iterator.next();
      associate.updateTraceSurfaceCcid(getRefreshPayList());
    }

    //更新追溯表 上游额度
    StreamAssociate tracerUpdate = new StreamAssociate();
    tracerUpdate.setFieldName("ctrlid");
    tracerUpdate.setStreamTableName("gl_balance_trace");
    tracerUpdate.setDaoSupport(daoSupport);
    tracerUpdate.updateSurfaceData(transList);
    //更新日志
    tracerUpdate.updateJournalCcid(getRefreshPayList());
    //保存转换结果
    balanceDao.saveTransData(transTableName, transList);
    //删除额度追溯临时表
    daoSupport.execute("delete from " + tempTraceTable);

    //2009-3-25 删除旧的支付核销中间表数据
    balanceDao.deletePayClearTmpData("plan_detail_detail", transTableName);
    balanceDao.deletePayClearTmpData("pay_detail", transTableName);
  }

  public int getRefreshDataSize() {
    return refreshBudgetNegativeData.size();
  }

  /**
   * 根据结果集中的数据刷新对象中的数据.
   * 增加处理，如果new_刷新要素为空，则不使用其值，使用原值
   * @param f
   * @param rs
   * @throws SQLException
   * @author huanglifeng
   * @since 6.2.61.09
   */
  public void doRefreshElement(FBaseDTO f, ResultSet rs) throws SQLException {
    for (int i = 0; i < refreshElementFields.length; i++) {
      Object targetValue = rs.getObject("new_" + refreshElementFields[i]);
      if (StringUtils.isEmpty(targetValue.toString()))
        continue;
      PropertiesUtil.setProperty(f, refreshElementFields[i], targetValue);
    }
  }

  public TransRecord getTransRecord(int index) {
    try {
      //			final FCtrlRecordDTO fctrlDto = getFCtrlRecordDTO(index);
      ////			final Map tempMap = getRefreshElements(index);
      ////			final BigDecimal refershMoney = new BigDecimal((String)tempMap.get(FRESH_MONEY));
      //			//生成冲销额度
      //			final CtrlRecordDTO oldCtrlDto = new CtrlRecordDTO();
      //			
      //			BeanUtils.copyProperties(oldCtrlDto, fctrlDto);
      //			//生成额度
      //			final CtrlRecordDTO newCtrlDto = (CtrlRecordDTO) oldCtrlDto.clone();
      //			newCtrlDto.setCcid(Long.valueOf(fctrlDto.getCcid()).longValue());
      //			//将冲销额度的use_money制成负
      //			oldCtrlDto.setUse_money(new BigDecimal(fctrlDto.getUse_money()).negate());
      //			//设置生成额度的use_money
      //			newCtrlDto.setUse_money(new BigDecimal(fctrlDto.getUse_money()));
      //			newCtrlDto.setSum_id("");
      //			newCtrlDto.setIs_enforce(1);
      //			newCtrlDto.setBalance_id("");
      /**
       * 上面注掉的代码 是老的年初数替换模式
       * 原来的模式: 通过sys_userpara表refreshElements刷新字段,算出正式指标额度的ccid,并生成额度
       * 改后的模式: 通过关联budget_control_replace表中budget_sum_id(指标已给出正式额度的id)关联;
       * 			 并且为了兼容旧的指标系统提供的budget_sum_id为空情况，则使用budget_id作为正式指标额度id,不再重新计算
       */
      final CtrlRecordDTO negativeRecord = (CtrlRecordDTO) refreshBudgetNegativeData.get(index);
      BigDecimal useMoney = negativeRecord.getUse_money();
      BalanceUtil.clearMoney(negativeRecord);
      negativeRecord.setUse_money(useMoney.negate());
      final CtrlRecordDTO positiveRecord = (CtrlRecordDTO) refreshBudgetPositiveData.get(index);
      BalanceUtil.clearMoney(positiveRecord);
      positiveRecord.setUse_money(useMoney);
      positiveRecord.setIs_enforce(1);
      return new TransRecord(positiveRecord, negativeRecord);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public List getRefreshBalanceData() {
    return refreshBalanceData;
  }

  public List getRefreshPayData() {
    return refreshPayData;
  }

  public String getTransTableName() {
    return transTableName;
  }

}
