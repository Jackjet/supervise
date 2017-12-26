package gov.df.fap.service.gl.coa.impl;

import gov.df.fap.api.gl.coa.CodeCombination;
import gov.df.fap.api.gl.coa.CodeCombinationDefinition;
import gov.df.fap.bean.gl.GlConstant;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.coa.FCoaDetail;
import gov.df.fap.service.gl.core.DaoSupport;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.datasource.SQLUtil;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.gl.coa.SimpleCodeCombination;
import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.Properties.PropertiesUtil;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.number.NumberUtil;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * CoaDao coa配置管理服务数据库访问层
 * @version 1.0
 * @author lwq
 * @since java 1.6
 */
@Component
public class CoaDaoService implements CoaDao {
  @Autowired
  @Qualifier("df.fap.daoSupport")
  private DaoSupport daoSupport = null;

  protected final static String CHECK_COA_STATEMENT = "update gl_coa set coa_code=coa_code where coa_code=#coa_code# and set_year=#set_year# and coa_id <>#coa_id#";

  protected final static String DELETE_COA_DETAIL_CODE_STATEMENT = "delete from gl_coa_detail_code  where exists (select 1 from gl_coa_detail b where set_year = b.set_year and coa_detail_id = b.coa_detail_id and b.coa_id = #coa_id#) and set_year =#set_year#";

  protected final static String DELETE_COA_DETAIL_CODE_STATEMENT_FORBEAN = "delete from gl_coa_detail_code  where exists (select 1 from gl_coa_detail b where set_year = b.set_year and coa_detail_id = b.coa_detail_id and b.coa_id = #coaId#) and set_year =#setYear#";

  protected final static String DELETE_COA_DETAIL_STATEMENT = "delete from gl_coa_detail where coa_id = #coa_id# and set_year = #set_year#";

  protected final static String DELETE_COA_DETAIL_STATEMENT_FORBEAN = "delete from gl_coa_detail where coa_id = #coaId# and set_year = #setYear#";

  protected final static String INSERT_COA_DETAIL_STATEMENT = "insert into gl_coa_detail(coa_id,coa_detail_id,ele_code,disp_order,level_num,is_mustinput,last_ver,set_year, default_value ,rg_code) values (#coa_id#,#coa_detail_id#,#ele_code#,#disp_order#,#level_num#,#is_mustinput#,#last_ver#,#set_year#,#default_value#,#rg_code#)";

  protected final static String INSERT_COA_DETAIL_CODE_STATEMENT = "insert into gl_coa_detail_code(coa_code_id,coa_detail_id,level_code,last_ver,set_year) values(#coa_code_id#,#coa_detail_id#,#level_code#,#last_ver#,#set_year#)";

  protected final static String UPDATE_COA_STATEMENT = "update gl_coa set coa_code=#coa_code#,coa_name=#coa_name#,coa_desc=#coa_desc#,ccids_table=#ccids_table#,enabled=#enabled#,latest_op_date=#latest_op_date#,latest_op_user=#latest_op_user#,last_ver=#last_ver#,sys_app_name=#sysAppName# where coa_id=#coa_id# and set_year=#set_year#";

  protected final static String UPDATE_COA_STATEMENT_FORBEAN = "update gl_coa set coa_code=#coaCode#,coa_name=#coaName#,coa_desc=#coaDesc#,ccids_table=#ccidsTable#,enabled=#enabled#,latest_op_date=#latestOpDate#,latest_op_user=#latestOpUser#,last_ver=#lastVer#,sys_app_name=#sysAppName# where coa_id=#coaId# and set_year=#setYear#";

  protected final static String FIND_COA_BY_ID_SIMPLE = "select a.coa_id,a.coa_code,a.coa_name,a.set_year,a.coa_desc, a.ccids_table from gl_coa a where a.set_year=? and a.coa_id = ?";

  //  protected final static String FIND_COA_DETAIL_SIMPLE = "select b.coa_detail_id,b.ele_code,b.default_value,b.is_mustinput, (select ele_name from sys_element where ele_code = upper(b.ele_code) and set_year = b.set_year and b.rg_code=rg_code) as ele_name,b.level_num from gl_coa_detail b where b.set_year=? and b.coa_id = ?";
  //modify by yanyga 优化查询语句 增加code_name字段
  protected final static String FIND_COA_DETAIL_SIMPLE = "select b.coa_detail_id, b.ele_code, b.default_value,b.is_mustinput,a.ele_name as ele_name,a.ele_code || ' ' || a.ele_name as code_name,b.level_num from gl_coa_detail b, sys_element a where b.set_year = ? and b.coa_id = ? and a.ele_code = upper(b.ele_code) and a.set_year = b.set_year and b.rg_code = a.rg_code";

  //兼容MySQL修改
  protected final static String FIND_COA_DETAIL_SIMPLE_MYSQL = "select b.coa_detail_id, b.ele_code, b.default_value,b.is_mustinput,a.ele_name as ele_name,concat(a.ele_code, ' ',a.ele_name) as code_name,b.level_num from gl_coa_detail b, sys_element a where b.set_year = ? and b.coa_id = ? and a.ele_code = upper(b.ele_code) and a.set_year = b.set_year and b.rg_code = a.rg_code";

  protected final static String FIND_LEVEL_CODE_STATEMENT = "select level_code from gl_coa_detail_code where coa_detail_id=? and set_year=? ";

  protected final static String LIST_COA = "select a.coa_id,a.coa_code,a.coa_name,a.set_year,a.coa_desc,a.ccids_table,a.enabled,'('||b.user_code||')'||b.user_name as create_user,a.create_date, '('||c.user_code||')'||c.user_name as latest_op_user,a.latest_op_date from gl_coa a left join sys_user b on a.create_user = b.user_id and b.set_year = a.set_year left join sys_user c on a.latest_op_user = c.user_id and c.set_year = a.set_year where a.set_year=? and a.rg_code=? #condition# order by coa_code";

  //兼容MySQL修改
  protected final static String LIST_COA_MYSQL = "SELECT a.coa_id, a.coa_code, a.coa_name, a.set_year, a.coa_desc, a.ccids_table, a.enabled, CONCAT('(',b.USER_CODE,')', b.USER_NAME) as create_user, a.create_date, CONCAT('(',c.USER_CODE,')', c.USER_NAME) as latest_op_user, a.latest_op_date FROM gl_coa a LEFT JOIN sys_user b ON a.create_user = b.user_id AND b.set_year = a.set_year LEFT JOIN sys_user c ON a.latest_op_user = c.user_id AND c.set_year = a.set_year WHERE a.set_year =? AND a.rg_code =? #condition# order by coa_code";

  private final static String CONDITION_KEY = "#condition#";

  protected final static String DEFAULT_SET_YEAR = "2006";

  /**查询COA对象语句*/
  protected final static String FIND_COA_BY_ID_SIMPLE_FOR_BEAN = "select a.coa_id coaId,a.coa_code coaCode,a.coa_name coaName,a.set_year setYear,a.coa_desc coaDesc,a.enabled enabled,a.create_date createDate,a.create_user createUser,a.latest_op_date latestOpDate,a.latest_op_user latestOpUser,a.last_ver lastVer,a.rg_code rgCode, a.ccids_table ccidsTable,a.sys_app_name sysAppName from gl_coa a where a.set_year=? and a.rg_code=? and a.coa_id = ?";

  /**查询COA明细对象语句*/
  protected final static String FIND_COA_DETAIL_SIMPLE_FOR_BEAN = "select b.coa_id coaId, b.coa_detail_id coaDetailId,b.ele_code eleCode,b.default_value defaultValue,(select ele_name from sys_element where ele_code = upper(b.ele_code) and set_year = b.set_year and b.rg_code=rg_code) as eleName,b.level_num levelNum, b.is_mustinput isMustInput from gl_coa_detail b where b.set_year=? and b.rg_code=? and b.coa_id = ? order by b.ele_code";

  /**查询所有COA对象的SQL，排除900开关的，即财政总账的机制凭证COA*/
  protected final static String FIND_ALL_COA_FOR_BEAN = "select a.coa_id coaId,a.coa_code coaCode,a.coa_name coaName,a.set_year setYear,a.coa_desc coaDesc,a.enabled enabled,a.create_date createDate,a.create_user createUser,a.latest_op_date latestOpDate,a.latest_op_user latestOpUser,a.last_ver lastVer,a.rg_code rgCode, a.ccids_table ccidsTable,a.sys_app_name sysAppName from gl_coa a where a.set_year=? and a.rg_code=?";

  //  private final static String ID_SUBFIX = "_id";

  protected final static String FIND_ALL_COA_CASCADE_SQL = "select t.coa_id,t.coa_name,t.relation_coa_id,t.is_up_stream,t.is_branch from gl_coa_cascade t where rg_code=?";

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CoaDaoI#saveCoa(gov.gfmis.fap.util.XMLData)
   */
  public XMLData saveCoa(XMLData coaData) throws Exception {
    String coa_code = (String) coaData.get("coa_code");
    List eleList = coaData.getRecordListByTag("row");
    if (eleList == null) {
      throw new Exception("未正确传入对应参数,无法修改对应coa配置!");
    }
    StringBuffer strSQL = new StringBuffer();
    String set_year = "2006";

    String coa_id = (String) coaData.get("coa_id");
    if (StringUtil.isEmpty(coa_id))
      coa_id = StringUtil.toStr(CommonUtil.generateSequence(GlConstant.SEQ_COA_KEY));

    coaData.put("coa_id", coa_id);
    set_year = SessionUtil.getLoginYear();
    String user_code = CommonUtil.getUserCode();
    String user_name = CommonUtil.getUserName();
    //验证code是否重复
    strSQL.append("update gl_coa").append(Tools.addDbLink()).append(" set coa_code=coa_code where coa_code='")
      .append(coa_code).append("' and set_year=").append(set_year).append(" and rg_code='")
      .append(SessionUtil.getRgCode()).append("'");
    int updateCount = daoSupport.executeUpdate(strSQL.toString());
    if (updateCount != 0) {
      throw new Exception("编码为" + coa_code + "的coa已存在!");
    }
    //创建新CCIDS 表
    String table = (String) coaData.get("ccids_table");
    if (table == null || table.trim().equals("")) {
      coaData.put("ccids_table", "gl_ccids");
    } else if (!isExistCcidsTable(table)) {
      CcidsTable ccidsTable = new CcidsTable(table);
      ccidsTable.setDaoSupport(daoSupport);
      ccidsTable.create();
    }
    //插入coa主表
    //添加部分系统信息
    String lastVerTime = DateHandler.getLastVerTime();
    coaData.put("set_year", set_year);
    coaData.put("create_user", user_code + " " + user_name);
    coaData.put("create_date", lastVerTime);
    coaData.put("latest_op_user", user_code + " " + user_name);
    coaData.put("latest_op_date", lastVerTime);
    coaData.put("last_ver", lastVerTime);
    coaData.put("rg_code", SessionUtil.getRgCode());
    strSQL.delete(0, strSQL.length());
    strSQL
      .append("insert into gl_coa")
      .append(Tools.addDbLink())
      .append("(coa_id,coa_code,coa_name,coa_desc,ccids_table,")
      .append("enabled,create_date,create_user,latest_op_date,latest_op_user,")
      .append(
        "last_ver,set_year,sys_app_name, rg_code) values (#coa_id#,#coa_code#,#coa_name#,#coa_desc#,#ccids_table#,#enabled#,#create_date#,#create_user#,#latest_op_date#,#latest_op_user#,#last_ver#,#set_year#,#sys_app_name#, #rg_code#)");
    if (daoSupport.executeUpdate(strSQL.toString(), coaData) == 0) {
      throw new Exception("无法正常插入coa配置!");
    }

    //循环插入要素配置
    strSQL.delete(0, strSQL.length());
    insertCoaDetail(coa_id, lastVerTime, set_year, eleList);
    return coaData;
  }

  //判断 CCIDS 表是否已存在
  public boolean isExistCcidsTable(String ccids_table) {
    try {
      daoSupport.queryForInt("select 1 from " + ccids_table);
      return true;
    } catch (Exception e) {
      return false;
    }

  }

  /**
   * 
   * @param coa_id
   * @param lastVerTime
   * @param set_year
   * @param eleList
   */
  private void insertCoaDetail(String coa_id, String lastVerTime, String set_year, List eleList) {
    List coaDetailCodeList = new LinkedList();
    for (int i = 0; i < eleList.size(); i++) {
      XMLData eleData = (XMLData) eleList.get(i);
      String coa_detail_id = UUIDRandom.generate();
      eleData.put("coa_id", coa_id);
      eleData.put("coa_detail_id", coa_detail_id);
      eleData.put("last_ver", lastVerTime);
      eleData.put("set_year", set_year);
      eleData.put("rg_code", SessionUtil.getRgCode());

      if ("0".equals(eleData.get("level_num"))) {
        List eleCode = eleData.getRecordListByTag("coa_detail_code");
        for (int j = 0; j < eleCode.size(); j++) {
          XMLData code = (XMLData) eleCode.get(j);
          code.put("ele_code_id", UUIDRandom.generate());
          code.put("coa_deatil_id", coa_detail_id);
          code.put("last_ver", lastVerTime);
          code.put("set_year", set_year);
          coaDetailCodeList.add(code);
        }
      }
    }
    daoSupport.batchExcute(INSERT_COA_DETAIL_STATEMENT, eleList);
    if (coaDetailCodeList.size() != 0)
      daoSupport.batchExcute(INSERT_COA_DETAIL_CODE_STATEMENT, coaDetailCodeList);
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CoaDaoI#updateCoa(gov.gfmis.fap.util.XMLData)
   */
  public void updateCoa(XMLData coaData) throws Exception {
    String coa_id = (String) coaData.get("coa_id");
    String coa_code = (String) coaData.get("coa_code");
    String ccids_table = (String) coaData.get("ccids_table");
    List eleList = coaData.getRecordListByTag("row");
    if (coa_id == null || eleList == null) {
      throw new Exception("未正确传入对应参数,无法修改对应coa配置!");
    }

    String set_year = "2006";
    String lastVerTime = DateHandler.getLastVerTime();
    set_year = set_year = SessionUtil.getLoginYear();
    String user_code = CommonUtil.getUserCode();
    String user_name = CommonUtil.getUserName();

    coaData.put("set_year", set_year);
    //验证code是否重复 .executeUpdate(CHECK_COA_STATEMENT, coaData)
    String coaIdByCode = daoSupport.queryForString(
      "select coa_id from gl_coa where coa_code=? and set_year=? and rg_code =?", new Object[] { coa_code, set_year,
        SessionUtil.getRgCode() });

    if (coaIdByCode != null && !StringUtil.equalsIgnoreCase(coaIdByCode, coa_id)) {
      throw new Exception("编码为" + coa_code + "的coa已存在!");
    }

    //删除明细code表
    daoSupport.executeUpdate(DELETE_COA_DETAIL_CODE_STATEMENT, coaData);
    //删除明细detail表
    daoSupport.executeUpdate(DELETE_COA_DETAIL_STATEMENT, coaData);

    insertCoaDetail(coa_id, lastVerTime, set_year, eleList);

    //添加部分系统信息
    coaData.put("latest_op_user", user_code + " " + user_name);
    coaData.put("latest_op_date", lastVerTime);
    coaData.put("last_ver", lastVerTime);
    if (daoSupport.executeUpdate(UPDATE_COA_STATEMENT, coaData) == 0) {
      throw new Exception("无法正常修改coa配置!");
    }
    //验证ccids_table是否存在 不存在创新表
    if (ccids_table == null || ccids_table.trim().equals(""))
      coaData.put("ccids_table", "gl_ccids");
    else if (!isExistCcidsTable(ccids_table)) {
      CcidsTable ccidsTable = new CcidsTable(ccids_table);
      ccidsTable.setDaoSupport(daoSupport);
      ccidsTable.create();
    }
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CoaDaoI#deleteCoa(java.lang.String)
   */
  public void deleteCoa(String coa_id) throws Exception {
    if (coa_id == null) {
      throw new Exception("未正确传入对应参数,无法删除对应coa配置!");
    }
    StringBuffer strSQL = new StringBuffer();
    String set_year = "2006";
    FCoaDTO coa = this.loadCoa(NumberUtil.toLong(coa_id));
    set_year = SessionUtil.getLoginYear();
    //查询定位当前coa是否已经被采用

    strSQL.append("select 1 from ").append(coa.getCcidsTable()).append(" where rownum=1 and coa_id = ?")
      .append(" and set_year=?");
    if (StringUtil.isNotEmpty(daoSupport.queryForString(strSQL.toString(), new Object[] { coa_id, set_year })))
      throw new Exception("coa_id为" + coa_id + "配置已被业务数据应用,无法删除!");

    strSQL.delete(0, strSQL.length());
    strSQL.append("select 1 from ele_accountant_subject ").append(Tools.addDbLink())
      .append(" where rownum=1 and coa_id=?").append(" and set_year=?");
    if (StringUtil.isNotEmpty(daoSupport.queryForString(strSQL.toString(), new Object[] { coa_id, set_year })))
      throw new Exception("coa_id为" + coa_id + "配置已被科目引用,无法删除!");

    strSQL.delete(0, strSQL.length());

    XMLData coaData = new XMLData();
    coaData.put("coa_id", coa_id);
    coaData.put("set_year", set_year);

    //删除明细code表
    daoSupport.executeUpdate(DELETE_COA_DETAIL_CODE_STATEMENT, coaData);
    //删除明细detail表
    daoSupport.executeUpdate(DELETE_COA_DETAIL_STATEMENT, coaData);
    //删除coa表
    coaData.put("coa_id", coa_id);
    coaData.put("set_year", set_year);
    strSQL.delete(0, strSQL.length());
    strSQL.append("delete from gl_coa where coa_id = #coa_id# and set_year = #set_year#");

    if (daoSupport.executeUpdate(strSQL.toString(), coaData) == 0)
      throw new Exception("无法正常删除coa配置!");
  }

  public void deleteAllCoa() {
    String sql = "delete from gl_coa_detail_code";
    daoSupport.execute(sql);

    //edit by fengdz 2013年09月30日，根据年度区划删除COA以及COA明细，否则数据库中只剩下本级COA beigin
    String set_year = SessionUtil.getLoginYear();
    String rgCode = SessionUtil.getRgCode();
    sql = "delete from gl_coa_detail where  set_year=" + set_year + " and  rg_code='" + rgCode + "'";
    daoSupport.execute(sql);
    sql = "delete from gl_coa  where  set_year=" + set_year + " and  rg_code='" + rgCode + "'";
    //edit by fengdz 2013年09月30日，根据年度区划删除COA以及COA明细，否则数据库中只剩下本级COA   end
    daoSupport.execute(sql);
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CoaDaoI#getCoabyID(java.lang.String)
   */
  public XMLData getCoabyID(String coa_id) {
    try {
      String set_year = DEFAULT_SET_YEAR;
      set_year = SessionUtil.getLoginYear();

      XMLData data = (XMLData) daoSupport.genericQueryForObject(FIND_COA_BY_ID_SIMPLE,
        new Object[] { set_year, coa_id }, XMLData.class);
      if (data == null)
        return data;
      List subData = null;
      if (TypeOfDB.isOracle()) {
        subData = daoSupport.genericQuery(FIND_COA_DETAIL_SIMPLE, new Object[] { set_year, coa_id }, XMLData.class);
      } else if (TypeOfDB.isMySQL()) {
        subData = daoSupport.genericQuery(FIND_COA_DETAIL_SIMPLE_MYSQL, new Object[] { set_year, coa_id },
          XMLData.class);
      }
      data.put("row", subData);
      //补自定义要素
      for (int i = 0; i < subData.size(); i++) {
        XMLData element = (XMLData) subData.get(i);
        String level_num = (String) element.get(CodeCombinationDefinition.LEVEL_NUM);
        if (level_num != null && level_num.equalsIgnoreCase("0")) {
          XMLData grandSubData = (XMLData) daoSupport.genericQueryForObject(FIND_LEVEL_CODE_STATEMENT, new Object[] {
            element.get(CodeCombinationDefinition.COA_DETAIL_ID), set_year }, XMLData.class);
          element.put(CodeCombinationDefinition.COA_DETAIL_CODE, grandSubData);
        }
      }
      return data;
    } catch (Exception e) {
      throw new RuntimeException("查询COA失败！", e);
    }
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CoaDaoI#loadCoa(long)
   */
  public FCoaDTO loadCoa(long coaId) {

    String set_year = SessionUtil.getLoginYear();
    ;
    String rg_code = SessionUtil.getRgCode();

    FCoaDTO resultObject = (FCoaDTO) daoSupport.genericQueryForObject(FIND_COA_BY_ID_SIMPLE_FOR_BEAN, new Object[] {
      set_year, rg_code, new Long(coaId) }, FCoaDTO.class);
    if (resultObject != null) {
      resultObject.setCoaDetailList(daoSupport.genericQuery(FIND_COA_DETAIL_SIMPLE_FOR_BEAN, new Object[] { set_year,
        rg_code, new Long(coaId) }, FCoaDetail.class));
      //补自定义要素
      for (int i = 0; i < resultObject.size(); i++) {
        final FCoaDetail element = (FCoaDetail) resultObject.get(i);
        final String level_num = StringUtil.toStr(element.getLevelNum());
        if (level_num != null && level_num.equalsIgnoreCase("0")) {
          List grandSubData = daoSupport.genericQuery(
            "select level_code from gl_coa_detail_code where coa_detail_id = ? and set_year= ? and rg_code=?",
            new Object[] { element.getCoaDetailId(), set_year, rg_code }, XMLData.class);
          if (grandSubData.size() > 0)
            element.setCoaDetailCode(grandSubData);
        }
      }
    }
    return resultObject;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CoaDaoI#getCoaList(java.lang.String)
   */
  public List getCoaList(String condition) {
    String set_year = SessionUtil.getLoginYear();
    String rgCode = SessionUtil.getRgCode();
    List result = null;
    if (TypeOfDB.isOracle()) {
      result = daoSupport.genericQuery(
        LIST_COA.replaceAll(CONDITION_KEY, condition == null ? StringUtil.EMPTY : condition), new Object[] { set_year,
          rgCode }, XMLData.class);
    } else if (TypeOfDB.isMySQL()) {
      result = daoSupport.genericQuery(
        LIST_COA_MYSQL.replaceAll(CONDITION_KEY, condition == null ? StringUtil.EMPTY : condition), new Object[] {
          set_year, rgCode }, XMLData.class);
    }
    return result;
  }

  public List getCoaDTOList() {
    String setYear = SessionUtil.getLoginYear();
    String rgCode = SessionUtil.getRgCode();
    return daoSupport.genericQuery(FIND_ALL_COA_FOR_BEAN, new String[] { setYear, rgCode }, FCoaDTO.class);
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CoaDaoI#findCcid(gov.gfmis.fap.dictionary.coa.FCoaDTO, long)
   */
  //hult 2011-07-13修改查询成预处理方式
  public Map findCcid(FCoaDTO coa, long ccid) {
    int setYear = NumberUtil.toInt(SessionUtil.getLoginYear());
    return (Map) daoSupport.genericQueryForObject("select * from " + coa.getCcidsTable()
      + " where ccid=? and set_year=?", new Object[] { new Long(ccid), new Integer(setYear) }, HashMap.class);
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CoaDaoI#findCcid(gov.gfmis.fap.dictionary.coa.FCoaDTO, long)
   */
  public Map findCcid(FCoaDTO coa, CodeCombination ccid) {
    int setYear = NumberUtil.toInt(SessionUtil.getLoginYear());
    return (Map) daoSupport.genericQueryForObject("select * from " + coa.getCcidsTable()
      + " where ccid=? and md5=? and set_year=?", new Object[] { new Long(ccid.getCcid()), ccid.getMd5(),
      new Integer(setYear) }, HashMap.class);
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CoaDaoI#findCcid(gov.gfmis.fap.dictionary.coa.FCoaDTO, long)
   */
  public Map findCcidFuzzy(FCoaDTO coa, CodeCombination ccid) {
    int setYear = NumberUtil.toInt(SessionUtil.getLoginYear());
    return (Map) daoSupport.genericQueryForObject("select * from " + coa.getCcidsTable()
      + " where (ccid=? or md5=?) and set_year=?", new Object[] { new Long(ccid.getCcid()), ccid.getMd5(),
      new Integer(setYear) }, HashMap.class);
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CoaDaoI#findCcid(gov.gfmis.fap.dictionary.coa.FCoaDTO, java.util.List, boolean)
   */
  public List fuzzyQueryCcid(FCoaDTO coa, List ccidObjects) {
    StringBuffer querySql = new StringBuffer();
    StringBuffer orderbySql = new StringBuffer();
    querySql.append("select coa_id as coaId, ccid, md5 from ").append(coa.getCcidsTable()).append(" where ccid in (");
    orderbySql.append(" order by ");
    boolean orderby = false;
    for (int i = 0; i < coa.size(); i++) {
      final FCoaDetail coaDetail = coa.get(i);
      final String eleLower = coaDetail.getEleCode().toLowerCase();

      if (coaDetail.getLevelNum() == -2) {
        orderbySql.append(eleLower).append(GlConstant.CODE_SUBFIX).append(GlConstant.COMMA);
        orderby = true;
      }
    }
    for (int i = 0; i < ccidObjects.size(); i++) {
      querySql.append(PropertiesUtil.getProperty(ccidObjects.get(i), GlConstant.CCID_KEY));
      if (i < ccidObjects.size() - 1)
        querySql.append(GlConstant.COMMA);
      else {
        querySql.append(") ");
      }
    }
    if (orderby) {
      orderbySql.delete(orderbySql.length() - 1, orderbySql.length());
      querySql.append(orderbySql);
    }

    return daoSupport.genericQuery(querySql.toString(), SimpleCodeCombination.class);
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CoaDaoI#insertCcid(gov.gfmis.fap.dictionary.coa.FCoaDTO, java.lang.Object, int)
   */
  public void insertCcid(FCoaDTO coa, Object elementContainer, int setYear) throws CodeCombinationConflictException {
    StringBuffer fieldSql = new StringBuffer();
    StringBuffer valueSql = new StringBuffer();
    StringBuffer insertCcidSql = new StringBuffer();
    //    判断 是否在库中已存在 此CCID 和MD5
    StringBuffer isExist = new StringBuffer();

    Iterator it = coa.getCoaDetail().iterator();
    while (it.hasNext()) {
      final FCoaDetail coaDetail = (FCoaDetail) it.next();
      appendElementInsertStatement(fieldSql, valueSql, coaDetail.getEleCode());
    }
    insertCcidSql.append("insert into ").append(coa.getCcidsTable())
      .append("(ccid,md5,latest_op_date,coa_id,set_year,rg_code").append(fieldSql).append(") values (#")
      .append("ccid#,#md5#,'").append(DateHandler.getToday()).append("',#coa_id#,").append(setYear).append(",'")
      .append(SessionUtil.getRgCode()).append("'").append(valueSql).append(")");

    isExist.append("select 1 from ").append(coa.getCcidsTable()).append(" where ccid = ? and md5 = ?");

    //如果冲突处理后的CCID 已在库中存在 就不再插入
    if (daoSupport.queryForInt(isExist.toString(),
      new Object[] { new Long((String) ((HashMap) elementContainer).get(GlConstant.CCID_KEY)),
        (String) ((HashMap) elementContainer).get(GlConstant.MD5_KEY) }) == 0)
      try {
        daoSupport.executeUpdate(insertCcidSql.toString(), elementContainer);
      } catch (Exception ex) {
        //判断是否冲突,如果冲突,则抛出冲突异常.
        String ccid = (String) PropertiesUtil.getProperty(elementContainer, GlConstant.CCID_KEY);
        String md5 = (String) PropertiesUtil.getProperty(elementContainer, GlConstant.MD5_KEY);
        String coaId = (String) PropertiesUtil.getProperty(elementContainer, GlConstant.COA_ID_KEY);
        if (ccid != null && md5 != null) {
          long longCcid = NumberUtil.toLong(ccid);
          CodeCombination conflict = new SimpleCodeCombination(longCcid, coaId, md5);
          Map map = findCcidFuzzy(coa, conflict);

          String loadedCcid = (String) map.get(GlConstant.CCID_KEY);
          String loadedMd5 = (String) map.get(GlConstant.MD5_KEY);

          if (!StringUtil.equals(ccid, loadedCcid) || !StringUtil.equals(md5, loadedMd5)) {
            CodeCombinationConflictException beThrown = new CodeCombinationConflictException();
            beThrown.addConflict(conflict);
            throw beThrown;
          }
        }
        //其它情况,将异常抛出
        throwsNormalException(ex);
      }

  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CoaDaoI#insertCcidBatch(java.util.Collection, java.util.List, java.util.List)
   */
  public void insertCcidBatch(List coas, List newCcids) throws CodeCombinationConflictException {
    try {
      int setYear = NumberUtil.toInt(SessionUtil.getLoginYear());
      StringBuffer fieldSql = new StringBuffer();
      StringBuffer valueSql = new StringBuffer();
      StringBuffer insertCcidSql = new StringBuffer();
      Iterator it = makeUpElements(coas).iterator();
      while (it.hasNext()) {
        final String eleCode = (String) it.next();
        appendElementInsertStatement(fieldSql, valueSql, eleCode);
      }
      insertCcidSql.append("insert into GL_CCIDS_CACHE (ccid,md5,latest_op_date,coa_id,set_year,rg_code")
        .append(fieldSql).append(") values (#").append("ccid#,#md5#," + SQLUtil.getSysdateToCharSql() + ",#coa_id#,")
        .append(setYear).append(",'").append(SessionUtil.getRgCode()).append("'").append(valueSql).append(")");
      daoSupport.batchExcute(insertCcidSql.toString(), newCcids);
      Map ccidTableMap = new HashMap();
      for (int i = 0; i < coas.size(); i++) {
        final FCoaDTO coa = (FCoaDTO) coas.get(i);
        final String ccidsTable = coa.getCcidsTable();
        if (ccidTableMap.containsKey(ccidsTable)) {
          if (ccidTableMap.get(ccidsTable).toString().indexOf(coa.getCoaId() + GlConstant.COMMA) < 0)
            ccidTableMap.put(ccidsTable, ccidTableMap.get(ccidsTable) + GlConstant.COMMA + coa.getCoaId());
        } else {
          ccidTableMap.put(ccidsTable, coa.getCoaId());
        }
      }

      for (it = ccidTableMap.keySet().iterator(); it.hasNext();) {
        final String ccidsTable = (String) it.next();
        try {
          daoSupport.executeUpdate("insert into " + ccidsTable + " select * from gl_ccids_cache t where t.coa_id in ("
            + ccidTableMap.get(ccidsTable) + ") and not exists(select 1 from " + ccidsTable + " c where c.coa_id in ("
            + ccidTableMap.get(ccidsTable) + ") and t.ccid=c.ccid and t.md5=c.md5)");
        } catch (Exception ex) {

          /*
           * 处理冲突异常. 判断是否在批量插入的数据中存在 冲突数据，如果有 选择一条先插入 现只支持 在临时表中
           *
           */
          boolean isExistCcidConflictInCache = false; // CCID 冲突
          boolean isExistMD5ConflictInCache = false; // //MD5 冲突
          List conflictInCache = new ArrayList();
          StringBuffer ccidConflictInCacheSql = new StringBuffer();
          StringBuffer md5ConflictInCacheSql = new StringBuffer();

          isExistCcidConflictInCache = daoSupport
            .queryForInt(" select 1 from gl_ccids_cache t where not exists(select 1 from gl_ccids c where c.ccid=t.ccid ) group by ccid having count(1)>1") > 0;
          isExistMD5ConflictInCache = daoSupport
            .queryForInt("select 1 from gl_ccids_cache t where not exists (select 1 from gl_ccids c where c.md5=t.md5) group by md5 having count(1)>1") > 0;

          // Status 为 3 表示临时表中 CCID 冲突
          ccidConflictInCacheSql
            .append("select ccid, coa_id as coaId, md5,3 as Status from gl_ccids_cache")
            .append(
              " where md5 in (select min(md5) ccid from gl_ccids_cache t where not exists (select 1 from gl_ccids c where t.ccid=c.ccid ) group by ccid having count(1)>1)");

          // Status 为 2 表示临时表中 MD5 冲突
          md5ConflictInCacheSql
            .append("select ccid, coa_id as coaId, md5,2 as Status from gl_ccids_cache")
            .append(
              " where ccid in (select min(ccid) ccid from gl_ccids_cache t where not exists (select 1 from gl_ccids c where t.md5=c.md5) group by md5 having count(1)>1)");

          if (isExistCcidConflictInCache) {
            conflictInCache.addAll(daoSupport.genericQuery(ccidConflictInCacheSql.toString(),
              SimpleCodeCombination.class));
          }
          if (isExistMD5ConflictInCache) {
            conflictInCache.addAll(daoSupport.genericQuery(md5ConflictInCacheSql.toString(),
              SimpleCodeCombination.class));
          }

          List conflict = daoSupport.genericQuery(
            "select ccid, coa_id as coaId, md5 from gl_ccids_cache t where exists(select 1 from " + ccidsTable
              + " c where (t.ccid=c.ccid and t.md5<>c.md5) or (t.md5=c.md5 and t.ccid<>c.ccid))",
            SimpleCodeCombination.class);
          conflict.addAll(conflictInCache);
          daoSupport.executeUpdate("delete from GL_CCIDS_CACHE");
          if (conflict != null && conflict.size() > 0)
            throw new CodeCombinationConflictException(conflict);
          else
            throwsNormalException(ex);
        }
      }
    } finally {
      daoSupport.executeUpdate("delete from GL_CCIDS_CACHE");
    }
  }

  /**
   * 处理普通异常
   * @param ex
   */
  private void throwsNormalException(Exception ex) {
    if (ex instanceof RuntimeException)
      throw (RuntimeException) ex;
    else
      throw new RuntimeException(ex);
  }

  /**
   * 
   * @param coa
   * @return
   */
  private List makeUpElements(List coas) {
    if (coas == null || coas.isEmpty())
      return Collections.EMPTY_LIST;
    List elements = new ArrayList();
    for (int i = 0; i < coas.size(); i++) {
      final FCoaDTO coa = (FCoaDTO) coas.get(i);
      for (int j = 0; j < coa.size(); j++) {
        if (!elements.contains(coa.get(j).getEleCode()))
          elements.add(coa.get(j).getEleCode());
      }
    }

    return elements;
  }

  /**
   * 拼INSERT语句所需要的内容.
   * @param fieldSql
   * @param valueSql
   * @param eleCode
   * 
   */
  private void appendElementInsertStatement(StringBuffer fieldSql, StringBuffer valueSql, String eleCode) {
    String eleCodeLower = eleCode.toLowerCase();
    fieldSql.append(GlConstant.COMMA).append(eleCodeLower).append(GlConstant.ID_SUBFIX).append(GlConstant.COMMA)
      .append(eleCodeLower).append(GlConstant.NAME_SUBFIX).append(GlConstant.COMMA).append(eleCodeLower)
      .append(GlConstant.CODE_SUBFIX);

    valueSql.append(GlConstant.COMMA).append("#").append(eleCode).append(".chrId#").append(GlConstant.COMMA)
      .append("#").append(eleCode).append(".chrName#").append(GlConstant.COMMA).append("#").append(eleCode)
      .append(".chrCode#");

    if (CoaService.isAccount(eleCode)) {
      final String prefix = (String) GlConstant.accountPrefixMapping.get(eleCode);

      fieldSql.append(",").append(prefix).append("_account_no,").append(prefix).append("_account_name,").append(prefix)
        .append("_account_bank");

      valueSql.append(",#").append(eleCode).append(".accountNo#,#").append(eleCode).append(".accountName#,#")
        .append(eleCode).append(".bankName#");
    }
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CoaDaoI#quickQueryCcid(gov.gfmis.fap.dictionary.coa.FCoaDTO, java.lang.String)
   */
  public long quickQueryCcid(FCoaDTO coa, long ccid) {
    int setYear = NumberUtil.toInt(SessionUtil.getLoginYear());
    String strCcid = daoSupport.queryForString(
      "select a.t_ccid from GL_CCID_Trans a where a.s_ccid =? and a.set_year=? and a.t_coa_id =?", new Object[] {
        String.valueOf(ccid), new Integer(setYear), coa.getCoaId() });
    if (StringUtil.isEmpty(strCcid))
      return CodeCombination.CCID_NULL;
    else
      return NumberUtil.toLong(strCcid);
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CoaDaoI#saveCcidTransCache(java.util.List)
   */
  public void saveCcidTransCache(List objects) {
    int setYear = NumberUtil.toInt(SessionUtil.getLoginYear());
    daoSupport.batchExcute(
      "insert into gl_ccid_trans(t_coa_id, s_ccid, t_ccid, last_ver, set_year) values(#coa_id#, #source_ccid#,#target_ccid#,"
        + SQLUtil.getSysdateToCharSql() + "," + setYear + ")", objects);
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CoaDaoI#isCustomerElementValid(gov.gfmis.fap.dictionary.coa.FCoaDetail, java.lang.String)
   */
  public boolean isCustomerElementValid(FCoaDetail coaDetail, String eleValueCode) {
    return daoSupport.genericQuery("select 1 from gl_coa_detail_code where coa_detail_id = ? and level_code = ?",
      new Object[] { coaDetail.getCoaDetailId(), eleValueCode }, XMLData.class).size() > 0;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CoaDaoI#setDaoSupport(gov.gfmis.fap.gl.core.DaoSupport)
   */
  public void setDaoSupport(DaoSupport daoSupport) {
    this.daoSupport = daoSupport;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CoaDaoI#saveCoa(gov.gfmis.fap.dictionary.coa.FCoaDTO)
   */
  public void saveCoa(FCoaDTO coaData) throws Exception {
    String coa_code = coaData.getCoaCode();
    List eleList = coaData.getCoaDetailList();
    if (eleList == null) {
      throw new Exception("未正确传入对应参数,无法修改对应coa配置!");
    }
    StringBuffer strSQL = new StringBuffer();
    String set_year = "2006";
    String coa_id = coaData.getCoaId();
    if (StringUtil.isEmpty(coa_id) || StringUtil.ZERO.equals(coa_id))
      coa_id = StringUtil.toStr(CommonUtil.generateSequence(GlConstant.SEQ_COA_KEY));
    coaData.setCoaId(coa_id);
    set_year = SessionUtil.getLoginYear();
    String user_code = CommonUtil.getUserCode();
    String rgCode = SessionUtil.getRgCode();
    //验证code是否重复
    strSQL.append("update gl_coa").append(Tools.addDbLink()).append(" set coa_code=coa_code where coa_code='")
      .append(coa_code).append("' and set_year=").append(set_year).append(" and rg_code='")
      .append(SessionUtil.getRgCode()).append("'");
    int updateCount = daoSupport.executeUpdate(strSQL.toString());
    if (updateCount != 0) {
      throw new Exception("编码为" + coa_code + "的coa已存在!");
    }
    //创建新CCIDS 表
    String table = coaData.getCcidsTable();
    if (!isExistCcidsTable(table)) {
      CcidsTable ccidsTable = new CcidsTable(table);
      ccidsTable.setDaoSupport(daoSupport);
      ccidsTable.create();
    }
    //插入coa主表
    //添加部分系统信息
    String lastVerTime = DateHandler.getLastVerTime();
    coaData.setSetYear(Integer.parseInt(set_year));
    coaData.setRgCode(rgCode);
    coaData.setCreateUser(user_code);
    coaData.setCreateDate(lastVerTime);
    coaData.setLatestOpUser(user_code);
    coaData.setLatestOpDate(lastVerTime);
    coaData.setLastVer(lastVerTime);
    strSQL.delete(0, strSQL.length());
    strSQL
      .append("insert into gl_coa")
      .append(Tools.addDbLink())
      .append("(coa_id,coa_code,coa_name,coa_desc,ccids_table,")
      .append("enabled,create_date,create_user,latest_op_date,latest_op_user,")
      .append(
        "last_ver,set_year,rg_code,sys_app_name) values (#coaId#,#coaCode#,#coaName#,#coaDesc#,#ccidsTable#,#enabled#,#createDate#,#createUser#,#latestOpDate#,#latestOpUser#,#lastVer#,#setYear#,#rgCode#,#sysAppName#)");
    if (daoSupport.executeUpdate(strSQL.toString(), coaData) == 0) {
      throw new Exception("无法正常插入coa配置!");
    }

    //循环插入要素配置
    final Iterator iterator = coaData.getCoaDetail().iterator();
    while (iterator.hasNext()) {
      FCoaDetail coaDetail = (FCoaDetail) iterator.next();
      coaDetail.setCoaId(coa_id);
      coaDetail.setSetYear(Integer.parseInt(set_year));
      coaDetail.setRgCode(rgCode);
      saveCoaDetail(coaDetail);
    }
  }

  /**
   * 插入FCoaDetail格式coa明细
   * @param coaDetailDto
   * @throws Exception
   */
  protected void saveCoaDetail(FCoaDetail coaDetailDto) throws Exception {
    String coaDetailId = coaDetailDto.getCoaDetailId();
    StringBuffer strSQL = new StringBuffer();
    if (StringUtil.isEmpty(coaDetailId)) {
      coaDetailId = UUIDRandom.generate();
      coaDetailDto.setCoaDetailId(coaDetailId);
    }
    strSQL
      .append(
        "insert into gl_coa_detail(coa_id,coa_detail_id,ele_code, disp_order, level_num, set_year, default_value, rg_code, is_mustinput) values(")
      .append("#coaId#,").append("#coaDetailId#,").append("#eleCode#,").append("#dispOrder#,").append("#levelNum#,")
      .append("#setYear#,").append("#defaultValue#,").append("#rgCode#,").append("#isMustInput#)");
    //    System.out.println(coaDetailDto.getCoaId() + ":" + coaDetailDto.getEleCode() +":" + coaDetailDto.getCoaDetailId());
    daoSupport.executeUpdate(strSQL.toString(), coaDetailDto);
    List detailCodeList = coaDetailDto.getCoaDetailCode();
    if (detailCodeList != null)
      saveCoaDetailCode(detailCodeList, coaDetailId);
  }

  protected void saveCoaDetailCode(final List detailCodeList, final String detailId) throws Exception {
    final Iterator iterator = detailCodeList.iterator();
    final List codeList = new LinkedList();
    XMLData data = null;
    while (iterator.hasNext()) {
      data = new XMLData();
      data.put("coa_code_id", UUIDRandom.generate());
      data.put("coa_detail_id", detailId);
      data.put("level_code", (String) iterator.next());
      data.put("last_ver", DateHandler.getDateFromNow(0));
      data.put("set_year", SessionUtil.getLoginYear());
      codeList.add(data);
    }
    StringBuffer buffer = new StringBuffer();
    buffer.append("insert into gl_coa_detail_code(coa_code_id,coa_detail_id,level_code,last_ver,set_year) values (")
      .append("#coa_code_id#,#coa_detail_id#,#level_code#,#last_ver#,#set_year#)");
    daoSupport.batchExcute(buffer.toString(), codeList);
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CoaDaoI#updateCoa(gov.gfmis.fap.dictionary.coa.FCoaDTO)
   */
  public void updateCoa(FCoaDTO coaData) throws Exception {
    String coa_id = coaData.getCoaId();
    String coa_code = coaData.getCoaCode();
    String ccids_table = coaData.getCcidsTable();
    List eleList = coaData.getCoaDetailList();
    if (coa_id == null || eleList == null) {
      throw new Exception("未正确传入对应参数,无法修改对应coa配置!");
    }

    String set_year = "2006";
    String lastVerTime = DateHandler.getLastVerTime();
    set_year = SessionUtil.getLoginYear();
    String user_code = CommonUtil.getUserCode();
    coaData.setSetYear(Integer.parseInt(set_year));
    //验证code是否重复 .executeUpdate(CHECK_COA_STATEMENT, coaData)
    String coaIdByCode = daoSupport.queryForString(
      "select coa_id from gl_coa where coa_code=? and set_year=? and rg_code=?", new Object[] { coaData.getCoaCode(),
        new Integer(coaData.getSetYear()), coaData.getRgCode() });
    if (coaIdByCode != null && !StringUtil.equalsIgnoreCase(coaIdByCode, coa_id)) {
      throw new Exception("编码为" + coa_code + "的coa已存在!");
    }
    //添加部分系统信息
    coaData.setCreateUser(user_code);
    String currentTime = DateHandler.getDateFromNow(0);
    coaData.setLatestOpDate(currentTime);
    coaData.setLastVer(lastVerTime);
    if (daoSupport.executeUpdate(UPDATE_COA_STATEMENT_FORBEAN, coaData) == 0) {
      throw new Exception("无法正常修改coa配置!");
    }
    //删除明细code表
    daoSupport.executeUpdate(DELETE_COA_DETAIL_CODE_STATEMENT_FORBEAN, coaData);
    //删除明细detail表
    daoSupport.executeUpdate(DELETE_COA_DETAIL_STATEMENT_FORBEAN, coaData);

    final Iterator iterator = coaData.getCoaDetailList().iterator();
    FCoaDetail detailDto = null;
    while (iterator.hasNext()) {
      detailDto = (FCoaDetail) iterator.next();
      detailDto.setCoaId(coaData.getCoaId());
      detailDto.setSetYear(coaData.getSetYear());
      detailDto.setRgCode(coaData.getRgCode());
      saveCoaDetail(detailDto);
    }
    //验证ccids_table是否存在 不存在创新表
    if (!isExistCcidsTable(ccids_table)) {
      CcidsTable ccidsTable = new CcidsTable(ccids_table);
      ccidsTable.setDaoSupport(daoSupport);
      ccidsTable.create();
    }
  }

  public CodeCombination findCcidKey(FCoaDTO coa, long ccid) {
    return (CodeCombination) daoSupport.genericQueryForObject(
      "select ccid, coa_id as coaId, md5 from " + coa.getCcidsTable() + " where ccid=?",
      new Object[] { String.valueOf(ccid) }, SimpleCodeCombination.class);
  }

  /**
   * 保存冲突
   * @return
   */
  public void saveConflict(FixedConflicts fixConflict) {
    //1CCID冲突,0MD5冲突
    daoSupport
      .executeUpdate(
        "insert into gl_ccid_conflict(ccid, md5, conflict_type, fixed_value) values(#conflictCcid#, #conflictMd5#, #conflictType#, #fixedValue#)",
        fixConflict);
  }

  /**
   * 
   */
  public List findFixedConflict() {
    return daoSupport
      .genericQuery(
        "select ccid as conflictCcid, md5 as conflictMd5, conflict_type as conflictType, fixed_value as fixedValue from gl_ccid_conflict",
        FixedConflicts.class);
  }

  /**
   * 从ccid表中查询是否该COA已经生成CCID
   */
  public boolean checkCcidIsExists(FCoaDTO coa) {
    String str = daoSupport.queryForString("select distinct 1 from " + coa.getCcidsTable() + " c where c.coa_id=?",
      new Object[] { coa.getCoaId() });
    return str != null;
  }

  /**
   * 查询coa的级联设置
   */
  public List loadCoaCascade() {
    return daoSupport.genericQuery(FIND_ALL_COA_CASCADE_SQL, new String[] { SessionUtil.getRgCode() }, HashMap.class);
  }

  /**
   * 判断传入coa是否生成了ccid
   * @param coaDto
   * @return
   */
  public boolean isGenCcid(FCoaDTO coaDto) {
    int rows = daoSupport.getRowCount("select 1 from gl_ccids where coa_id=?", new Object[] { coaDto.getCoaId() });
    return rows > 0;
  }
}
