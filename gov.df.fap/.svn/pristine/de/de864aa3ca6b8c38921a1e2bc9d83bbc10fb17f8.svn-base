package gov.df.fap.service.workflow;

import gov.df.fap.api.dictionary.interfaces.IDictionary;
import gov.df.fap.api.gl.coa.ibs.ICoa;
import gov.df.fap.api.rule.ISysBillNoRule;
import gov.df.fap.api.sysbilltype.ISysBillType;
import gov.df.fap.bean.system.billtype.BillTypeForm;
import gov.df.fap.bean.user.UserInfoContext;
import gov.df.fap.service.util.DatabaseAccess;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.log.Log;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.xml.XMLData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 交易凭证类型管理服务端接口实现类
 * 
 * @author a
 * @author 2017年7月11日修改，具体的修改参考《单据类型设计.doc》
 */
@Service
public class SysBillTypeBO implements ISysBillType {

  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao;

  @Autowired
  TypeOfDB typeOfDB;

  @Override
  public List getFieldNameByFieldCode(String fieldCode) throws Exception {
    StringBuffer strSQL = new StringBuffer("select field_name from sys_metadata  where field_code=?");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString(), new Object[] { fieldCode });
      strSQL = null;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  @Autowired
  ISysBillNoRule iSysBillNoRule;

  @Autowired
  private IDictionary dictionary;

  /**
   * 得到所有交易凭证类型数据
   */
  public List findAllSysBillTypes() throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();

    StringBuffer strSQL = new StringBuffer("select * from sys_billtype").append(Tools.addDbLink())
      .append(" where rg_code=?  and set_year=?").append("  order by billtype_code");

    List list = null;
    try {

      list = dao.findBySql(strSQL.toString(), new Object[] { rg_code, setYear });
      strSQL = null;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 得到所有sysId的交易凭证类型数据
   */
  public List findBillTypesBySysId(String sysId) throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    String strSQL = "select * from sys_billtype where rg_code=?  and set_year=? and sys_id=? order by billtype_code";
    List list = null;
    try {
      list = dao.findBySql(strSQL, new Object[] { rg_code, setYear, sysId });
      strSQL = null;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return list;
  }

  /**
   * 根据单据类型得到所有交易凭证类型数据
   */
  public List findSysBillTypesBySysName(String billtypeClass) throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();

    StringBuffer strSQL = new StringBuffer();
    strSQL.append(" select t.*, tt.sys_name ")
      .append(" from sys_billtype t left join sys_app tt on t.sys_id = tt.sys_id ")
      .append("where set_year = ? and rg_code = ?");
    if (typeOfDB.isOracle()) {
      strSQL.append(" order by t.billtype_class,  decode(tt.sys_name, null, '999', t.sys_id), t.billtype_code");
    } else if (typeOfDB.isMySQL()) {
      strSQL
        .append("order by t.billtype_class,  case tt.sys_name when null then '999' else t.sys_id end, t.billtype_code");
    }
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString(), new Object[] { setYear, rg_code });
      strSQL = null;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 根据单据类型得到所有交易凭证类型数据
   */
  public List findSysBillTypesByBilltypeClass(String billtypeClass) throws Exception {
    StringBuffer strSQL = new StringBuffer();
    strSQL.append(" select t.*, tt.sys_name ")
      .append(" from sys_billtype t left join sys_app tt on t.sys_id = tt.sys_id ")
      .append("where set_year = ? and rg_code = ? and billtype_class= ? ").append(" order by t.billtype_code");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString(), new Object[] { getSetYear(), getRgCode(), billtypeClass });
      strSQL = null;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 查找所有的COA
   */
  public List findAllGlCoas() throws Exception {
    StringBuffer strSQL = new StringBuffer("select coa_id,coa_name,coa_code from gl_coa").append(Tools.addDbLink())
      .append(" where enabled='1' and rg_code=?").append(" and set_year=? ").append(" order by COA_CODE");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString(), new Object[] { getRgCode(), getSetYear() });//modify by justin at 2012-05-22 18:33:32
      strSQL = null;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 查找所有交易类型
   */
  public List findGlBusvouTypes() throws Exception {
    StringBuffer strSQL = new StringBuffer("select vou_type_id,vou_type_name,vou_type_code from gl_busvou_type ")
      .append(Tools.addDbLink()).append(" where  rg_code=? and set_year=? order by VOU_TYPE_CODE");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString(), new Object[] { getRgCode(), getSetYear() });
      strSQL = null;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 查找表类型为201，202的物理表名
   */
  public List findSysTableManagers() throws Exception {
    StringBuffer strSQL = new StringBuffer("select * from sys_tablemanager").append(Tools.addDbLink()).append(
      " where TABLE_TYPE in ('201', '202') order by TABLE_TYPE, TABLE_CODE ");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString());
      strSQL = null;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 查找所有辅助要素
   */
  public List findAssistSysElements() throws Exception {
    StringBuffer strSQL = new StringBuffer(
      "select chr_id,ele_code,rg_code,max_level,ele_name,set_year from Sys_Element").append(Tools.addDbLink()).append(
      " where is_system=0 and enabled=1 and rg_code=? and set_year=? order by ELE_CODE ");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString(), new String[] { getRgCode(), getSetYear() });
      strSQL = null;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return list;
  }

  /**
   * 取得基础数据表eleSource中的最大level_num
   * 
   * @param eleSource
   * @return
   */
  public Integer getMaxLevelNumByEleSource(String eleSource) {
    StringBuffer strSQL = new StringBuffer("select MAX(level_num) max_level_num from ").append(eleSource).append(
      Tools.addDbLink());
    Integer maxLevelNum = null;
    try {
      List ls = dao.findBySql(strSQL.toString());
      if (ls != null && ls.size() > 0) {
        if (((XMLData) ls.get(0)).get("max_level_num") != "" && (((XMLData) ls.get(0)).get("max_level_num") != null))
          return maxLevelNum = new Integer((String) ((XMLData) ls.get(0)).get("max_level_num"));
      }
      strSQL = null;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return null;
  }

  /**
   * 通过BILLTYPE_ID查找辅助要素
   */
  public List findSysBilltypeAsselesByBillTypeId(String billTypeId) throws Exception {
    StringBuffer strSQL = new StringBuffer("select assele.*,  ele.ele_name from sys_billtype_assele")
      .append(Tools.addDbLink())
      .append(" assele, sys_element")
      .append(Tools.addDbLink())
      .append(" ele where assele.BILLTYPE_ID = '")
      .append(billTypeId)
      .append(
        "' and assele.ele_code = ele.ele_code and assele.rg_code=? and assele.set_year=? order by assele.ele_code ");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString(), new Object[] { getRgCode(), getSetYear() });
      strSQL = null;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return list;
  }

  /**
   * 通过billtype_id，ele_code查找sys_billtype_assele
   * 
   * @param billtypeId
   * @param eleCode
   * @return
   * @throws Exception
   */
  public Integer getSysBilltypeAsselesLevelNumByBillTypeIdEleCode(String billtypeId, String eleCode) throws Exception {
    StringBuffer strSQL = new StringBuffer("select level_num from sys_billtype_assele").append(Tools.addDbLink())
      .append(" where billtype_id =? and ele_code =? and rg_code=? and set_year=? ");
    try {
      List list = dao.findBySql(strSQL.toString(), new Object[] { billtypeId, eleCode, getRgCode(), getSetYear() });
      if (list != null && list.size() > 0) {
        if (((XMLData) list.get(0)).get("level_num") != null)
          return new Integer((String) ((XMLData) list.get(0)).get("level_num"));
      }
      strSQL = null;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return null;
  }

  /**
   * 保存交易凭证类型数据
   */
  public void saveorUpdateSysBillType(XMLData xmlData) throws Exception {
    try {
      xmlData.put("set_year", getSetYear());
      xmlData.put("rg_code", getRgCode());

      dao.deleteDataBySql("sys_billtype", xmlData, new String[] { "billtype_id" });
      dao.saveDataBySql("sys_billtype", xmlData);
      /** modify eleRuleMap无值时，不保存定值规则，解决单据类型管理保存时，清除定值规则  */
      if (!((List) xmlData.get("eleRuleMap") == null)) {
        this.saveValueSet((List) xmlData.get("eleRuleMap"), xmlData.get("billtype_id").toString());
      }
      /** modify eleRuleMap无值时，不保存定值规则，解决单据类型管理保存时，清除定值规则  */

    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * 删除交易凭证类型数据
   */
  public void deleteSysBillType(XMLData xmlData) throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();

    try {
      xmlData.put("set_year", setYear);
      xmlData.put("rg_code", rg_code);

      dao.deleteDataBySql("sys_billtype", xmlData, new String[] { "billtype_id" });

    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * 级联删除相关联的交易凭证类型数据
   * 
   * @param userId
   */
  public void deleteSysBillTypeByBillTypeId(String billtypeId) throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();

    StringBuffer delSysBilltypeAsseleSql = new StringBuffer("delete from sys_billtype_assele")
      .append(Tools.addDbLink()).append(" where billtype_id = '").append(billtypeId)
      .append("' and rg_code=? and set_year=?");
    StringBuffer delSysBilltypeSql = new StringBuffer("delete from sys_billtype").append(Tools.addDbLink())
      .append(" where billtype_id = '").append(billtypeId).append("' and rg_code=? and set_year=?");

    try {
      int count = 0;
      count = dao.executeBySql(delSysBilltypeAsseleSql.toString(), new Object[] { rg_code, setYear });
      count = dao.executeBySql(delSysBilltypeSql.toString(), new Object[] { rg_code, setYear });

      delSysBilltypeAsseleSql = null;
      delSysBilltypeSql = null;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * 更新与billtypeId相关联的sys_billtype_assele
   * 
   * @param billtypeId
   * @param newBilltypeAsseles
   * @throws Exception
   */
  public void updateSysBilltypeAssele(String billtypeId, List newBilltypeAsseles) throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();

    StringBuffer delSysBilltypeAsseleSql = new StringBuffer("delete from sys_billtype_assele")
      .append(Tools.addDbLink()).append(" where billtype_id = '").append(billtypeId)
      .append("' and rg_code=? and set_year=?");

    try {
      int count = 0;
      // 删除相关联的 sys_billtype_assele
      count = dao.executeBySql(delSysBilltypeAsseleSql.toString(), new Object[] { rg_code, setYear });
      for (int i = 0; newBilltypeAsseles != null && i < newBilltypeAsseles.size(); i++) {
        XMLData assele = (XMLData) newBilltypeAsseles.get(i);
        StringBuffer insertsql = new StringBuffer("insert into sys_billtype_assele")
          .append(Tools.addDbLink())
          .append(
            " (ASSELE_ID, BILLTYPE_ID, ELE_CODE, LATEST_OP_USER, LATEST_OP_DATE, SET_YEAR, LEVEL_NUM, LAST_VER,RG_CODE) ")
          .append("values ('").append(UUIDRandom.generate()).append("', '").append(billtypeId).append("', '")
          .append((String) assele.get("ele_code")).append("', '")
          .append((String) SessionUtil.getUserInfoContext().getUserID()).append("', '")
          .append(DateHandler.getLastVerTime()).append("', ").append(getSetYear()).append(", ")
          .append(assele.get("level_num")).append(", '").append(Tools.getCurrDate()).append("', '").append(rg_code)
          .append("')");
        dao.executeBySql(insertsql.toString());
        insertsql = null;
      }
      delSysBilltypeAsseleSql = null;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * 得到系统业务年度
   * 
   * @return
   */
  public String getSetYear() {
    String set_year = SessionUtil.getLoginYear();
    if (set_year == null || set_year.equalsIgnoreCase("")) {
      set_year = String.valueOf(DateHandler.getCurrentYear());
    }
    return set_year;
  }

  /**
   * 通过交易凭证类型得到字段
   * 
   * @author xunyuqing
   * @param billType_id
   *            交易凭证类型ID
   */
  public List getEleByBillType(String billType_id) throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();

    StringBuffer strSQL = new StringBuffer("select coa_id,table_name from sys_billtype").append(Tools.addDbLink())
      .append(" where billtype_id=? and rg_code=? and set_year=?");
    List billTypeList = dao.findBySql(strSQL.toString(), new String[] { billType_id, rg_code, setYear });
    List eleList = new ArrayList();
    // 通过coaid得到字段
    String coaID = "";
    if (((XMLData) billTypeList.get(0)).get("coa_id") != null) {
      coaID = ((XMLData) billTypeList.get(0)).get("coa_id").toString();
      ICoa coa = (ICoa) SessionUtil.getServerBean("sys.coaService");
      XMLData coaInfo = coa.getCoabyID(coaID);
      List fieldListByCoa = (List) coaInfo.get("row");
      for (int i = 0; i < fieldListByCoa.size(); i++) {
        Map map = (Map) fieldListByCoa.get(i);
        Map mapField = new HashMap();
        mapField.put("field_code", map.get("ele_code").toString() + "_id");
        mapField.put("ele_code", map.get("ele_code"));
        eleList.add(mapField);
      }
    }
    strSQL = null;
    return eleList;
  }

  /**
   * 通过交易凭证类型得到字段
   * 
   * @param billType_id
   *            交易凭证类型ID
   */
  public List getFieldByBillType(String billType_id) throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();

    StringBuffer strSQL = new StringBuffer("select coa_id,table_name from sys_billtype").append(Tools.addDbLink())
      .append(" where billtype_id=? and rg_code=? and set_year=?");
    List billTypeList = dao.findBySql(strSQL.toString(), new String[] { billType_id, rg_code, setYear });
    List fieldList = new ArrayList();
    // 通过coaid得到字段
    ICoa coa = (ICoa) SessionUtil.getServerBean("sys.coaService");
    String coaID = "";
    if (((XMLData) billTypeList.get(0)).get("coa_id") != null) {
      coaID = ((XMLData) billTypeList.get(0)).get("coa_id").toString();
      XMLData coaInfo = coa.getCoabyID(coaID);
      List fieldListByCoa = null;
      if (coaInfo != null && coaInfo.get("row") != null) {
        fieldListByCoa = (List) coaInfo.get("row");
      }
      for (int i = 0; fieldListByCoa != null && i < fieldListByCoa.size(); i++) {
        XMLData map = (XMLData) fieldListByCoa.get(i);
        XMLData mapField = new XMLData();
        mapField.put("field_code", map.get("ele_code").toString().toLowerCase() + "_id");
        mapField.put("ele_code", map.get("ele_code").toString().toLowerCase());
        fieldList.add(mapField);
      }
    }
    // 通过源表得到字段
    if (((XMLData) billTypeList.get(0)).get("table_name") != null) {
      String sourceTab = ((XMLData) billTypeList.get(0)).get("table_name").toString();
      String fieldStr = DatabaseAccess.getFieldString(sourceTab);
      StringTokenizer st = new StringTokenizer(fieldStr, ",");
      while (st.hasMoreTokens()) {
        String fieldCode = st.nextToken();
        if (fieldCode.equalsIgnoreCase("ccid") || fieldCode.equalsIgnoreCase("rcid")) {
          continue;
        }
        XMLData map = new XMLData();
        map.put("field_code", fieldCode.toLowerCase());
        map.put("ele_code", fieldCode.toLowerCase().replaceAll("_id", ""));
        fieldList.add(map);
      }
    }
    return getFieldByBillTypeHelper(fieldList);
  }

  /**
   * 通过交易凭证类型得到字段
   * 
   * @param billType_id
   *            交易凭证类型ID
   * @param coaID
   * @param sourceName
   */
  public List getFieldByCoaAndSource(String coaID, String sourceName) throws Exception {
    List fieldList = new ArrayList();
    // 通过coaid得到字段
    ICoa coa = (ICoa) SessionUtil.getServerBean("sys.coaService");
    XMLData coaInfo = coa.getCoabyID(coaID);
    List fieldListByCoa = null;
    if (coaInfo != null && coaInfo.get("row") != null) {
      fieldListByCoa = (List) coaInfo.get("row");
    }
    for (int i = 0; fieldListByCoa != null && i < fieldListByCoa.size(); i++) {
      XMLData map = (XMLData) fieldListByCoa.get(i);
      XMLData mapField = new XMLData();
      mapField.put("field_code", map.get("ele_code").toString().toLowerCase() + "_id");
      mapField.put("ele_code", map.get("ele_code").toString().toLowerCase());
      fieldList.add(mapField);
    }
    // 通过源表得到字段
    String fieldStr = DatabaseAccess.getFieldString(sourceName);
    StringTokenizer st = new StringTokenizer(fieldStr, ",");
    while (st.hasMoreTokens()) {
      String fieldCode = st.nextToken();
      if (fieldCode.equalsIgnoreCase("ccid") || fieldCode.equalsIgnoreCase("rcid")) {
        continue;
      }
      XMLData map = new XMLData();
      map.put("field_code", fieldCode.toLowerCase());
      map.put("ele_code", fieldCode.toLowerCase().replaceAll("_id", ""));
      fieldList.add(map);
    }
    return fieldList;
  }

  private List getFieldByBillTypeHelper(List fieldList) {
    if (fieldList.size() == 0)
      return fieldList;
    StringBuffer sb = new StringBuffer();
    XMLData xd = null;
    String field_code = null;
    for (int i = 0; i < fieldList.size(); ++i) {
      xd = (XMLData) fieldList.get(i);
      field_code = xd.get("field_code") == null ? "" : xd.get("field_code").toString();
      if (!field_code.equals(""))
        sb.append(",'").append(field_code.toUpperCase()).append("'");
    }

    if (sb.length() == 0)
      return fieldList;

    StringBuffer sql = new StringBuffer();
    sql.append(" select t.field_code, t.field_name from sys_metadata t ").append(" where t.is_deleted = 0")
      .append(" and t.field_code in (").append(sb.subSequence(1, sb.length())).append(")");
    List ls = dao.findBySql(sql.toString());

    Map map = new HashMap();
    for (int i = 0; i < ls.size(); ++i) {
      xd = (XMLData) ls.get(i);
      map.put(xd.get("field_code"), xd.get("field_name"));
    }
    String field_name = null;
    for (int i = 0; i < fieldList.size(); ++i) {
      xd = (XMLData) fieldList.get(i);
      field_code = xd.get("field_code") == null ? "" : xd.get("field_code").toString();
      if (!field_code.equals("")) {
        field_name = map.get(field_code.toUpperCase()) == null ? "" : map.get(field_code.toUpperCase()).toString();
        xd.put("field_name", field_name);
      }
    }
    return fieldList;
  }

  /**
   * 通过交易凭证类型得到字段
   * 
   * @author xunyuqing
   * @param condition
   *            查询条件 默认为:and + 条件
   */
  public List getBillTypeData(String condition) {
    String rg_code = getRgCode();
    String setYear = getSetYear();

    StringBuffer strSQL = new StringBuffer("select * from sys_billtype").append(Tools.addDbLink())
      .append(" where 1=1 and rg_code='").append(rg_code).append("' and set_year='").append(setYear).append("'")
      .append(condition);
    List billTypeList = dao.findBySql(strSQL.toString());
    strSQL = null;
    return billTypeList;
  }

  /**
   * 通过要素得到要素规则
   * 
   * @author xunyuqing
   * @param condStr
   *            要素条件 固定格式：and ele_code=?
   * @param field
   *            要素列
   *            
   * 增加年度区划的过滤
   */
  public List getEleRuleWithField(String field, String condStr) {
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    StringBuffer strSQL = new StringBuffer("select  ").append(field).append("  from  sys_ele_rule")
      .append(Tools.addDbLink()).append("  where 1=1 ").append(" and set_year='").append(set_year).append("'")
      .append(" and rg_code='").append(rg_code).append("'").append(condStr);
    List result = dao.findBySql(strSQL.toString());
    strSQL = null;
    return result;
  }

  /**
   * 通过要素得到要素规则
   * 
   * @author xunyuqing
   * @param condStr
   *            要素条件 固定格式：and ele_code=?
   * update by justin at 2017-03-22 18:05:34 增加年度区划的过滤
   */
  public List getEleRule(String condStr) {
    UserInfoContext userInfo = SessionUtil.getUserInfoContext();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    StringBuffer strSQL = new StringBuffer("select  rule.*  from  sys_ele_rule").append(Tools.addDbLink())
      .append(" rule where 1=1 ").append(" and set_year='").append(set_year).append("'").append(condStr);
    List result = dao.findBySql(strSQL.toString());
    strSQL = null;
    return result;
  }

  /**
   * 通过字段得到
   * 
   *  
   */
  public List getEleRuleAndEleValueSet(String fieldCode, String billType) {
    StringBuffer strSQL = new StringBuffer();
    if (!fieldCode.equals("")) {
      if (typeOfDB.isOracle()) {
        strSQL
          .append("select distinct sbvs.field_code,decode(sbvs.valueset_type,1,rule.ele_code,0,'') ele_code,")
          .append(
            "decode(sbvs.valueset_type,1,rule.ele_rule_id,0,'') ele_rule_id,decode(sbvs.valueset_type,1,ele_rule_name,0,'') ele_rule_name, ");
      } else if (typeOfDB.isMySQL()) {
        strSQL
          .append(
            "select distinct sbvs.field_code,case sbvs.valueset_type when 1 then rule.ele_code when 0 then '' end ele_code,")
          .append(
            "case sbvs.valueset_type when 1 then rule.ele_rule_id when 0 then '' end ele_rule_id,"
              + "case sbvs.valueset_type when 1 then ele_rule_name when 0 then '' end ele_rule_name, ");
      }
      strSQL
        .append("sbvs.billtype_id, sbvs.valueset_type, default_value from sys_ele_rule")
        .append(Tools.addDbLink())
        .append(" rule,sys_billtype_valueset")
        .append(Tools.addDbLink())
        .append(
          " sbvs where 1=1 and rule.rg_code = sbvs.rg_code and rule.set_year=sbvs.set_year and sbvs.billtype_id='")
        .append(billType).append("' and ((field_code='").append(fieldCode)
        .append("' and rule.ele_rule_id = sbvs.ele_rule_id and sbvs.valueset_type=1").append(" ) or (")
        .append("field_code='").append(fieldCode).append("' and sbvs.valueset_type='0')) ");
    } else {
      strSQL
        .append(
          "select sbvs.billtype_id,sbvs.field_code, sbvs.valueset_type, sbvs.default_value, ser.ele_rule_code, ser.ele_rule_name ")
        .append(" from sys_billtype_valueset")
        .append(Tools.addDbLink())
        .append(
          " sbvs left join sys_ele_rule ser on ser.ele_rule_id = sbvs.ele_rule_id where 1=1 and sbvs.billtype_id='")
        .append(billType).append("' ");
    }

    List result = dao.findBySql(strSQL.toString());
    strSQL = null;
    return result;
  }

  public List getSourceDataBySourceID(String source, String sourceID) {

    StringBuffer strSQL = new StringBuffer("select * from ").append(source).append(Tools.addDbLink())
      .append(" where chr_id='").append(sourceID).append("'");
    List result = dao.findBySql(strSQL.toString());
    strSQL = null;
    return result;
  }

  /**
   * Map存储如下值对： ele_code ele_rule_id ele_rule_code ele_rule_name row
   * （row中存在XMLData列表，其中XMLData保存已经设置上规则的要素值）
   * 
   * update by justin at 2017-03-22 14:47:12 sys_ele_rule增加rg_code字段
   */
  public void insertOrUpdateEleRule(Map value) throws Exception {
    try {
      String eleRuleID = "";
      String eleRuleCode = "";
      String eleRuleName = "";
      String eleCode = "";
      StringBuffer strSQL = new StringBuffer();
      // ele_rule_id不存在 则为新增数据
      if ((value.get("ele_rule_id") != null && value.get("ele_rule_id").equals("")) || value.get("ele_rule_id") == null) {
        eleRuleID = UUIDRandom.generate();
        eleRuleName = value.get("ele_rule_name").toString();
        eleRuleCode = value.get("ele_rule_code").toString();
        eleCode = value.get("ele_code").toString();
        strSQL
          .append("INSERT  INTO  SYS_ELE_RULE")
          .append(Tools.addDbLink())
          .append(
            " (ELE_RULE_ID,ELE_RULE_CODE,ELE_RULE_NAME,ELE_CODE,create_user,create_date,LATEST_OP_USER, LATEST_OP_DATE, SET_YEAR, LAST_VER,RG_CODE) ")
          .append(" VALUES ('").append(eleRuleID).append("','").append(eleRuleCode).append("','").append(eleRuleName)
          .append("','").append(eleCode.toUpperCase()).append("' ,'")
          .append((String) SessionUtil.getUserInfoContext().getUserID()).append("', '")
          .append(DateHandler.getLastVerTime()).append("','")
          .append((String) SessionUtil.getUserInfoContext().getUserID()).append("', '")
          .append(DateHandler.getLastVerTime()).append("','").append(getSetYear()).append("', '")
          .append(Tools.getCurrDate()).append("', '").append(SessionUtil.getUserInfoContext().getRgCode()).append("')");
        dao.executeBySql(strSQL.toString());
      }
      // 如果存在则为修改状态
      else {
        eleRuleID = value.get("ele_rule_id").toString();
        strSQL.append("update  SYS_ELE_RULE").append(Tools.addDbLink()).append(" set ").append("ELE_RULE_ID='")
          .append(eleRuleID).append("',ELE_RULE_CODE='").append(eleRuleCode).append("',ELE_CODE='")
          .append(eleCode.toUpperCase()).append("',create_user='")
          .append((String) SessionUtil.getUserInfoContext().getUserID()).append("',create_date='")
          .append(DateHandler.getLastVerTime()).append("',LATEST_OP_USER='")
          .append((String) SessionUtil.getUserInfoContext().getUserID()).append("',LATEST_OP_DATE='")
          .append(DateHandler.getLastVerTime()).append("',SET_YEAR='").append(getSetYear()).append("'")
          .append(",LAST_VER='").append(Tools.getCurrDate()).append("'")
          //dingyy20120601 加上rg_code,ELE_RULE_ID过滤，不然把所有数据都更新了
          .append(" where rg_code='").append(getRgCode()).append("' and ELE_RULE_ID='").append(eleRuleID).append("'");
        dao.executeBySql(strSQL.toString());
      }

      strSQL = null;
    } catch (Exception e) {
      throw e;
    }
  }

  /**
     * 新增时获取rule_id
     * @param seq
     * @return rule_id
     */
  public String generateNumberBySeq(String seq) {
    String sql = "select " + seq + ".NEXTVAL as id from dual";
    String sql2 = "select nextval('" + seq + "') from dual";
    List list = null;
    try {
      if (typeOfDB.isOracle()) {
        list = dao.findBySql(sql);
      } else if (typeOfDB.isMySQL()) {
        list = dao.findBySql(sql2);
      }

      if (((XMLData) list.get(0)).get("id").toString() != null) {
        return ((XMLData) list.get(0)).get("id").toString();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @author XUN
   * @param value
   * @param billTypeID
   * @return
   */
  public boolean saveValueSet(List value, String billTypeID) throws Exception {
    PreparedStatement ps = null;
    Session session = null;
    try {
      StringBuffer strSQL = new StringBuffer("delete from SYS_BILLTYPE_VALUESET where billtype_id='")
        .append(billTypeID).append("'");
      dao.executeBySql(strSQL.toString());
      //现在设置size为0时的情况：用户删除全部定值规则
      String rg_code = getRgCode();
      if (value != null && value.size() > 0) {
        strSQL.delete(0, strSQL.length());
        strSQL.append(" INSERT  INTO  SYS_BILLTYPE_VALUESET").append(Tools.addDbLink())
          .append("(valueset_id,FIELD_CODE,valueset_type,billtype_id,default_value,ele_rule_id,")
          .append("last_ver,set_year,create_user,create_date,LATEST_OP_USER, LATEST_OP_DATE,RG_CODE)  ")
          .append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
        Connection conn = null;
        session = dao.getSession();
        conn = session.connection();
        ps = conn.prepareStatement(strSQL.toString());
        for (int i = 0; value != null && i < value.size(); i++) {
          Map map = (Map) value.get(i);
          ps.setString(1, UUIDRandom.generate());
          ps.setString(2, map.get("field_code").toString().toUpperCase());
          ps.setString(3, map.get("valueset_type").toString());
          ps.setString(4, billTypeID);
          if (map.get("valueset_type").toString().equals("0")) {
            if (map.get("default_value") != null) {
              ps.setString(5, map.get("default_value").toString());
            } else {
              ps.setString(5, "");
            }
            ps.setString(6, "");
          } else {
            ps.setString(5, "");
            ps.setString(6, map.get("ele_rule_id").toString());
          }
          ps.setString(7, Tools.getCurrDate());
          ps.setString(8, getSetYear());
          ps.setString(9, (String) SessionUtil.getUserInfoContext().getUserID());
          ps.setString(10, DateHandler.getLastVerTime());
          ps.setString(11, (String) SessionUtil.getUserInfoContext().getUserID());
          ps.setString(12, DateHandler.getLastVerTime());
          ps.setString(13, rg_code);
          ps.addBatch();
        }
        ps.executeBatch();
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      try {
        if (ps != null) {
          ps.close();
        }
        if (session != null) {
          dao.closeSession(session);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  /**
   * 
   * @param ele_rule_id
   * @return
   */
  public List getRuleInfo(String eleRuleID, int pageIndex, int pageRows) {
    String rg_code = SessionUtil.getRgCode();
    String set_year = SessionUtil.getLoginYear();
    StringBuffer strSQL = new StringBuffer();
    try {
      List result = new ArrayList();
      strSQL = strSQL.append("select ele_code from sys_ele_rule where ele_rule_id='").append(eleRuleID).append("'");
      List list = dao.findBySql(strSQL.toString());
      if (list != null && list.size() > 0 && ((Map) list.get(0)).get("ele_code") != null) {
        XMLData sourceTableMap = dictionary.getElementSetByCode(((Map) list.get(0)).get("ele_code").toString());
        String eleTable = sourceTableMap.get("ele_source").toString();
        strSQL.delete(0, strSQL.length());

        strSQL.append("select eleTable.chr_code,eleTable.chr_id,eleTable.chr_name,")
          .append("ruleDetail.rule_id,ruleDetail.ele_rule_detail_id,ruleDetail.ele_rule_id,")
          .append("(case when ruleDetail.rule_id is not null then ")
          .append("case when rule.remark is not null then rule.remark else '已设定规则' end ")
          .append("else rule.remark end) remark,rule.rule_code,rule.rule_name from ").append(eleTable)
          .append(Tools.addDbLink()).append(" eleTable").append(" left join sys_ele_rule_detail")
          .append(Tools.addDbLink()).append(" ruleDetail on eleTable.chr_id =")
          .append("ruleDetail.Ele_Value and ruleDetail.Ele_Rule_Id ='").append(eleRuleID)
          .append("' left join sys_rule").append(Tools.addDbLink()).append(" rule on ruleDetail.rule_id =")
          .append("rule.rule_id ").append("where eleTable.rg_code='").append(rg_code)
          .append("' and eleTable.set_year=").append(set_year).append(" order by eleTable.chr_code");

        if (typeOfDB.isOracle()) {
          strSQL.insert(0, "select subdata.* from (select rownum r,data.* from(");
          strSQL.append(") data where rownum <= ").append((pageIndex * pageRows)).append(")subdata where subdata.r>")
            .append((pageIndex - 1) * pageRows);

        } else if (typeOfDB.isMySQL()) {
          strSQL.insert(0, "select data.* from(");
          strSQL.append(") as data limit ").append((pageIndex - 1) * pageRows).append(",").append(pageRows);

        }
        result = dao.findBySql(strSQL.toString());
        strSQL = null;
      }
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      return (List) null;
    }
  }

  /**
   * 
   * @param ele_rule_id
   * @return
   */
  public int getRuleInfoTotalCount(String eleRuleID) {
    StringBuffer strSQL = new StringBuffer();
    try {
      String rg_code = SessionUtil.getRgCode();
      String set_year = SessionUtil.getLoginYear();
      IDictionary dictionary = (IDictionary) SessionUtil.getServerBean("sys.dictionaryService");
      strSQL = strSQL.append("select ele_code from sys_ele_rule").append(Tools.addDbLink())
        .append(" where ele_rule_id='").append(eleRuleID).append("'");
      List list = dao.findBySql(strSQL.toString());
      XMLData sourceTableMap = dictionary.getElementSetByCode(((Map) list.get(0)).get("ele_code").toString());
      String eleTable = sourceTableMap.get("ele_source").toString();
      strSQL.delete(0, strSQL.length());
      strSQL
        .append("Select count(1) from ")
        .append(eleTable)
        .append(Tools.addDbLink())
        .append(
          "eleTable LEFT JOIN (ruleDetail LEFT JOIN sys_rule rule ON ( ruleDetail.rule_id = rule.rule_id)) ON (eleTable.chr_code = ruleDetail.ele_value)")
        .append("where eleTable.rg_code='").append(rg_code).append("' and eleTable.set_year=").append(set_year);
      List tempList = dao.findBySql(strSQL.toString());
      strSQL = null;
      return Integer.parseInt(((Map) tempList.get(0)).get("count(1)").toString());
    } catch (Exception e) {
      System.out.println("执行sql出错：\n" + e.getMessage() + "\nSQL开始\n" + strSQL + "\nSQL结束");
      return 0;
    }
  }

  /**
   * update by justin SYS_ELE_RULE增加rg_code字段
   */
  public void insertOrUpdateDetailRule(String ele_value, String ele_code, String ele_name, String rule_id,
    String ele_rule_id) throws Exception {
    PreparedStatement ps = null;
    Session session = null;
    try {
      // 删除重复数据
      StringBuffer strSQL = new StringBuffer();
      strSQL.append("select (1) from sys_ele_rule_detail");
      strSQL.append(" where ele_value='");
      strSQL.append(ele_value);
      strSQL.append("' and ele_rule_id='");
      strSQL.append(ele_rule_id);
      strSQL.append("' ");
      List detailList = dao.findBySql(strSQL.toString());
      if (detailList.size() > 0) {
        strSQL.delete(0, strSQL.length());
        strSQL.append("delete from sys_ele_rule_detail");
        strSQL.append(" where ele_value='");
        strSQL.append(ele_value);
        strSQL.append("' and ele_rule_id='");
        strSQL.append(ele_rule_id);
        strSQL.append("' ");
        dao.executeBySql(strSQL.toString());
      }
      // 插入新数据
      strSQL.delete(0, strSQL.length());
      strSQL.append("insert into sys_ele_rule_detail").append(Tools.addDbLink())
        .append(" (ELE_RULE_DETAIL_ID,ELE_RULE_ID,ELE_VALUE,RULE_ID,create_user,create_date,LATEST_OP_USER,")
        .append("LATEST_OP_DATE,LAST_VER,SET_YEAR,ele_code,ele_name,rg_code) values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
      Connection conn = null;
      session = dao.getSession();
      conn = session.connection();
      ps = conn.prepareStatement(strSQL.toString());
      ps.setString(1, UUIDRandom.generate());
      ps.setString(2, ele_rule_id);
      ps.setString(3, ele_value);
      ps.setString(4, rule_id);
      ps.setString(5, (String) SessionUtil.getUserInfoContext().getUserID());
      ps.setString(6, DateHandler.getLastVerTime());
      ps.setString(7, (String) SessionUtil.getUserInfoContext().getUserID());
      ps.setString(8, DateHandler.getLastVerTime());
      ps.setString(9, Tools.getCurrDate());
      ps.setString(10, getSetYear());
      ps.setString(11, ele_code);
      ps.setString(12, ele_name);
      ps.setString(13, SessionUtil.getRgCode());

      ps.execute();
      strSQL = null;
    } catch (Exception e) {
      throw e;
    } finally {
      try {
        if (ps != null) {
          ps.close();
        }
        if (session != null) {
          dao.closeSession(session);
        }
      } catch (Exception ex) {
      }
    }

  }

  public void deleteEleRule(String eleRuleID) throws Exception {
    String rg_code = SessionUtil.getRgCode();
    String setYear = SessionUtil.getLoginYear();

    StringBuffer strSQL = new StringBuffer("select 1 from sys_billtype_valueset").append(Tools.addDbLink()).append(
      " a where a.ele_rule_id = '");
    strSQL.append(eleRuleID).append("' and rg_code='").append(rg_code).append("' and set_year='").append(setYear)
      .append("'");
    List list = dao.findBySql(strSQL.toString());
    if (list.size() > 0) {
      throw new Exception("本要素定值规则已经使用,无法删除!");
    }

    Session session = dao.getSession();
    try {
      Connection conn = session.connection();
      Statement delStatement = conn.createStatement();

      strSQL.delete(0, strSQL.length());
      strSQL.append("delete from sys_ele_rule").append(Tools.addDbLink()).append(" where ele_rule_id='")
        .append(eleRuleID).append("'");
      delStatement.addBatch(strSQL.toString());

      strSQL.delete(0, strSQL.length());//sys_rule表
      strSQL
        .append(
          "delete from sys_rule where rule_id in (select distinct rule_id from sys_ele_rule_detail where ele_rule_id= '")
        .append(eleRuleID).append("')");
      delStatement.addBatch(strSQL.toString());

      strSQL.delete(0, strSQL.length());//sys_rule_cross_join_add表
      strSQL
        .append(
          "delete from sys_rule_cross_join_add where rule_id in (select distinct rule_id from sys_ele_rule_detail where ele_rule_id= '")
        .append(eleRuleID).append("')");
      delStatement.addBatch(strSQL.toString());

      strSQL.delete(0, strSQL.length());//sys_rule_cross_join_del表
      strSQL
        .append(
          "delete from sys_rule_cross_join_del where rule_id in (select distinct rule_id from sys_ele_rule_detail where ele_rule_id= '")
        .append(eleRuleID).append("')");
      delStatement.addBatch(strSQL.toString());

      strSQL.delete(0, strSQL.length());//sys_right_group_type表
      strSQL
        .append(
          "delete from sys_right_group_type where right_group_id in (select distinct rg.right_group_id from sys_right_group rg where rg.rule_id in (select distinct rule_id from sys_ele_rule_detail rd where rd.ele_rule_id= '")
        .append(eleRuleID).append("'))");
      delStatement.addBatch(strSQL.toString());

      strSQL.delete(0, strSQL.length());//sys_right_group_detail表
      strSQL
        .append(
          "delete from sys_right_group_detail where right_group_id in (select distinct rg.right_group_id from sys_right_group rg where rg.rule_id in (select distinct rule_id from sys_ele_rule_detail rd where rd.ele_rule_id= '")
        .append(eleRuleID).append("'))");
      delStatement.addBatch(strSQL.toString());

      strSQL.delete(0, strSQL.length());//sys_right_group表
      strSQL
        .append(
          "delete from sys_right_group where rule_id in (select distinct rule_id from sys_ele_rule_detail where ele_rule_id= '")
        .append(eleRuleID).append("')");
      delStatement.addBatch(strSQL.toString());

      strSQL.delete(0, strSQL.length());
      strSQL.append("delete from sys_ele_rule_detail").append(Tools.addDbLink()).append(" where ele_rule_id='")
        .append(eleRuleID).append("'");
      delStatement.addBatch(strSQL.toString());
      delStatement.executeBatch();
      //     保存操作日志
      strSQL = null;
    } catch (Exception e) {
      e.printStackTrace();
      Log.error("执行sql出错：\n" + e.getMessage() + "\nSQL开始\n" + strSQL + "\nSQL结束");
      throw e;
    } finally {
      dao.closeSession(session);
    }
  }

  public void deleteEleRuleDetail(String eleRuleID, String eleValue) throws Exception {
    StringBuffer strSQL = new StringBuffer("select 1 from sys_ele_rule_detail").append(Tools.addDbLink())
      .append(" a where a.ele_rule_id = '").append(eleRuleID).append("' and ele_value='").append(eleValue).append("'");
    List list = dao.findBySql(strSQL.toString());
    if (list.size() < 1) {
      throw new Exception("本要素定值规则已经为空!");
    }
    strSQL.delete(0, strSQL.length());
    strSQL.append("delete from sys_ele_rule_detail").append(Tools.addDbLink()).append(" where ele_rule_id='")
      .append(eleRuleID).append("' and ele_value='").append(eleValue).append("'");
    dao.executeBySql(strSQL.toString());
    strSQL = null;
  }

  public List getInfoByTableName(String fields, String tableName) {
    List result = dao.findBySql(new StringBuffer("select ").append(fields).append(" from ").append(tableName)
      .toString());
    return result;
  };

  /**
   * 获取子系统信息
   * 
   * @return List（XMLData参考sys_app表中的sys_id和sys_name字段
   * @author justin 2017年7月12日添加
   * 
   */
  public List getApp_id() {
    // 获取子系统名字和id
    String sql = "select sa.sys_id,sa.sys_name from sys_app sa order by sa.sys_id";
    List app_id_List = dao.findBySql(sql);
    return app_id_List;
  }

  /**
   * 获取所有业务口径
   * 
   * @return List（XMLData参考GL_COA表中的coa_id、coa_code和coa_name字段）
   * @author justin 2017年7月12日添加
   * 
   */
  public List getCoa() {
    // 获取业务口径的id、编码和名字
    String sql = "select gc.coa_id,gc.coa_code,gc.coa_name from gl_coa gc where rg_code =? and set_year=? order by gc.coa_code";
    List coa_List = dao.findBySql(sql, new String[] { getRgCode(), getSetYear() });
    return coa_List;
  }

  /**
   * 获取所有单号规则
   * 
   * @return List（XMLData参考Sys_Billnorule表中的billnorule_id、billnorule_code和billnorule_name字段）
   * @author justin 2017年7月12日添加
   * 
   */
  public List getBillnorule() {

    String rg_code = getRgCode();
    String setYear = getSetYear();

    // 获取所有单号
    StringBuffer sql = new StringBuffer(
      "select sb.billnorule_id,sb.billnorule_code,sb.billnorule_name from sys_billnorule sb ")
      .append("where rg_code=? and set_year=? order by sb.billnorule_code");

    List coa_List = dao.findBySql(sql.toString(), new Object[] { rg_code, setYear });
    return coa_List;
  }

  /**
   * 获取录入视图
   * 
   * @return List（XMLData参考SYS_UIMANAGER表中的ui_id、ui_code和ui_name字段）
   * @author justin 2017年7月12日添加
   * 
   */
  public List getUI() {
    String rg_code = getRgCode();
    String sql = "select su.ui_id,su.ui_code,su.ui_name from sys_uimanager su where su.ui_type=? and su.rg_code = ? and su.set_year=? order by su.ui_code";
    List coa_List = dao.findBySql(sql, new Object[] { "001", rg_code, getSetYear() });
    return coa_List;
  }

  /**
   * 获取单据主/明细表
   * 
   * @param table_type
   *            int 202-主表 201－表示明细表
   * @return List （XMLData参考sys_tablemanager表中的chr_id、table_code和table_name字段）
   * @author justin 2017年7月12日添加
   * 
   */
  public List geTable_name(int table_type) {
    // 获取主/明细表的id、编码和名字
    String sql = "select st.chr_id,st.table_code,st.table_name from sys_tablemanager st where st.table_type="
      + table_type;
    List coa_List = dao.findBySql(sql);
    return coa_List;
  }

  /**
   * 获取“来源单据明细类型”和“去向单据明细类型”
   * 
   * @param sys_id
   *            String 要获取的子系统的id
   * @return List
   *         (XMLData参考SYS_BILLTYPE表中的billtype_id、billtype_code和billtype_name)
   * @author justin 2017年7月12日添加
   * 
   */
  public List getBilltype(String sys_id) {
    String rg_code = getRgCode();
    String setYear = getSetYear();

    String sql = "select billtype_name,billtype_code,billtype_id,billtype_class from sys_billtype sb where sb.billtype_class=1 and sb.enabled=1 and sb.sys_id=? and sb.rg_code=? and sb.set_year=?";

    List coa_List = dao.findBySql(sql, new Object[] { sys_id, rg_code, setYear });
    return coa_List;
  }

  /**
   * 获取所有“来源单据明细类型”和“去向单据明细类型”
   * 
   * @return List
   *         (XMLData参考SYS_BILLTYPE表中的billtype_id、billtype_code和billtype_name)
   * @author justin 2017年7月12日添加
   * 
   */
  public List getBilltype() {
    String rg_code = getRgCode();
    String setYear = getSetYear();

    String sql = "select * from sys_billtype sb where sb.billtype_class=1 and sb.enabled=1  and sb.rg_code=? and sb.set_year=?";

    List coa_List = dao.findBySql(sql, new Object[] { rg_code, setYear });
    return coa_List;
  }

  /**
   * 获取记帐凭证类型
   * 
   * @return List
   *         (XMLData参考GL_BUSVOU_TYPE表中的vou_type_id、vou_type_code和vou_type_name)
   * @author justin 2017年7月12日添加
   * 
   */
  public List getBusvou_type() {
    // 获取单据明细类型的id、编码和名字
    String sql = "select gbt.vou_type_id,gbt.vou_type_code,gbt.vou_type_name from gl_busvou_type gbt where rg_code=? and set_year=? order by gbt.vou_type_code";
    List coa_List = dao.findBySql(sql, new Object[] { SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
    return coa_List;
  }

  /**
   * 获取录入额度控制类型
   * 
   * @return List（XMLData参考Gl_Sum_Type表中的sum_type_id、sum_type_code和sum_type_name字段）
   * @author justin 2017年7月12日添加
   */
  public List getVou_control() {
    // 获取单据明细类型的id、编码和名字
    String rg_code = SessionUtil.getRgCode();//add by  justin at 2012-04-24 10:37:54
    String sql = "select gst.sum_type_id,gst.sum_type_code,gst.sum_type_name from gl_sum_type gst where gst.rg_code=? and set_year=? order by gst.sum_type_code";//modify by  justin at 2012-04-24 10:37:54
    List coa_List = dao.findBySql(sql, new Object[] { rg_code, SessionUtil.getLoginYear() });
    return coa_List;
  }

  public List getReport_id_ss() {
    StringBuffer sql = new StringBuffer();
    List report_id_List = new ArrayList();
    if (typeOfDB.isOracle()) {
      sql
        .append("select distinct rm.report_type chr_id, ")
        .append(
          "decode(rm.report_type, 'FIX_PRINT', '固定套打', 'GROUP_REPORT_BOTH', '简单明细报表', 'FUNC_FILL','函数填充报表') chr_code, ")
        .append("' ' chr_name, null parent_id ")
        .append(
          "from reportcy_manager rm where upper(rm.report_type) <> 'RSTATIC' and rm.rg_code='"
            + SessionUtil.getRgCode() + "'")
        .append("union all ")
        .append(
          "select distinct rm.report_type || '_' || rm.report_sort chr_id, rm.report_sort chr_code, ' ' chr_name, ")
        .append("rm.report_type parent_id ")
        .append(
          "from reportcy_manager rm  where upper(rm.report_type) <> 'RSTATIC' and rm.rg_code='"
            + SessionUtil.getRgCode() + "'")
        .append("union all ")
        .append(
          "select rm.report_id chr_id, rm.report_id chr_code, rm.report_name chr_name, rm.report_type || '_' || rm.report_sort parent_id ")
        .append(
          "from reportcy_manager rm where upper(rm.report_type) <> 'RSTATIC' and rm.rg_code='"
            + SessionUtil.getRgCode() + "'");
    } else if (typeOfDB.isMySQL()) {
      sql
        .append("select distinct rm.report_type chr_id, ")
        .append(
          "CASE rm.report_type WHEN 'FIX_PRINT' then  '固定套打'  WHEN 'GROUP_REPORT_BOTH' then  '简单明细报表' WHEN 'FUNC_FILL' then  '函数填充报表'END chr_code, ")
        .append("' ' chr_name, null parent_id ")
        .append(
          "from reportcy_manager rm where upper(rm.report_type) <> 'RSTATIC' and rm.rg_code='"
            + SessionUtil.getRgCode() + "'")
        .append("union all ")
        .append(
          "select distinct CONCAT(rm.report_type,'_',rm.report_sort) chr_id, rm.report_sort chr_code, ' ' chr_name, ")
        .append("rm.report_type parent_id ")
        .append(
          "from reportcy_manager rm  where upper(rm.report_type) <> 'RSTATIC' and rm.rg_code='"
            + SessionUtil.getRgCode() + "'")
        .append("union all ")
        .append(
          "select rm.report_id chr_id, rm.report_id chr_code, rm.report_name chr_name, CONCAT(rm.report_type,'_',rm.report_sort) parent_id ")
        .append(
          "from reportcy_manager rm where upper(rm.report_type) <> 'RSTATIC' and rm.rg_code='"
            + SessionUtil.getRgCode() + "'");
    }
    report_id_List = dao.findBySql(sql.toString());
    return report_id_List;
  }

  public List getReport() {
    StringBuffer sql = new StringBuffer();
    List report_id_List = new ArrayList();
    if (typeOfDB.isOracle()) {
      sql
        .append("select distinct rm.report_type report_id,' ' chr_code,")
        .append(
          "decode(rm.report_type, 'FIX_PRINT', '固定套打', 'GROUP_REPORT_BOTH', '简单明细报表', 'FUNC_FILL','函数填充报表') report_name, ")
        .append(" null parent_id ")
        .append(
          "from reportcy_manager rm where upper(rm.report_type) <> 'RSTATIC' and rm.rg_code='"
            + SessionUtil.getRgCode() + "'")
        .append(" union all ")
        .append(
          "select distinct rm.report_type || '_' || rm.report_sort report_id, ' ' chr_code, rm.report_sort report_name, ")
        .append("rm.report_type parent_id ")
        .append(
          "from reportcy_manager rm  where upper(rm.report_type) <> 'RSTATIC' and rm.rg_code='"
            + SessionUtil.getRgCode() + "'")
        .append(" union all ")
        .append(
          "select rm.report_id report_id, rm.report_id chr_code, rm.report_name report_name, rm.report_type || '_' || rm.report_sort parent_id ")
        .append(
          "from reportcy_manager rm where upper(rm.report_type) <> 'RSTATIC' and rm.rg_code='"
            + SessionUtil.getRgCode() + "'");
    } else if (typeOfDB.isMySQL()) {
      sql
        .append("select distinct rm.report_type report_id,' ' chr_code,")
        .append(
          "CASE rm.report_type WHEN 'FIX_PRINT' then  '固定套打'  WHEN 'GROUP_REPORT_BOTH' then  '简单明细报表'WHEN 'FUNC_FILL' then  '函数填充报表'END report_name, ")
        .append(" null parent_id ")
        .append(
          "from reportcy_manager rm where upper(rm.report_type) <> 'RSTATIC' and rm.rg_code='"
            + SessionUtil.getRgCode() + "'")
        .append(" union all ")
        .append(
          "select distinct CONCAT(rm.report_type,'_',rm.report_sort)  report_id, ' ' chr_code, rm.report_sort report_name, ")
        .append("rm.report_type parent_id ")
        .append(
          "from reportcy_manager rm  where upper(rm.report_type) <> 'RSTATIC' and rm.rg_code='"
            + SessionUtil.getRgCode() + "'")
        .append(" union all ")
        .append(
          "select rm.report_id report_id, rm.report_id chr_code, rm.report_name report_name, CONCAT(rm.report_type,'_',rm.report_sort) parent_id ")
        .append(
          "from reportcy_manager rm where upper(rm.report_type) <> 'RSTATIC' and rm.rg_code='"
            + SessionUtil.getRgCode() + "'");
    }

    report_id_List = dao.findBySql(sql.toString());
    return report_id_List;

  }

  /**
   * 获取打印格式
   * 
   * @return List（XMLData参考reportcy_manager表中的report_id、report_code和report_name字段）
   * @author 
   */
  public List getReport_id() {
    String sql = "select rm.report_id,rm.report_code,rm.report_name from reportcy_manager rm where rm.rg_code='"
      + SessionUtil.getRgCode() + "'";
    List report_id_List = dao.findBySql(sql);
    return report_id_List;
  }

  public List findPrintFormat(String flag, String name) {

    StringBuffer sb = new StringBuffer();

    sb.append("select rm.report_id,rm.report_code,rm.report_name from reportcy_manager rm where rm.rg_code='")
      .append(SessionUtil.getRgCode()).append("' and rm.report_name ");
    if ("0".equals(flag)) {
      sb.append("like '%").append(name).append("%'");
    } else if ("1".equals(flag)) {
      sb.append("like '").append(name).append("%'");
    } else if ("2".equals(flag)) {
      sb.append("like '%").append(name).append("'");
    } else {
      sb.append("= '").append(name).append("'");
    }

    List report_id_List = dao.findBySql(sb.toString());
    return report_id_List;

  }

  /**
   * 根据单据id获取“打印模版”的数据
   * @param billtype_id
   *            String 单据类型的id
   * @return List（XMLData,字段参考表sys_print_models）
   * @author justin 2017年7月20日添加
   */
  public List getPrintModeData(String billtype_id) {
    // 获取所有的“打印模版”的数据
    String sql = "select sbr.*,rm.report_name from sys_print_models sbr,reportcy_manager rm where rm.report_id = sbr.report_id and sbr.billtype_id  = ? and rm.rg_code=? and sbr.set_year=? order by display_order";
    List tmp_report = dao.findBySql(sql,
      new Object[] { billtype_id, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });

    //打印模板中选择多个报表时，report_id以分号分隔，下面程序处理通过上面查询语句查询不到的打印模板信息
    sql = "select * from sys_print_models where instr(report_id,';')>0 and billtype_id=? ";
    List tmp_report_2 = dao.findBySql(sql, new Object[] { billtype_id });
    //如果存在配置多个报表的情况，执行下面程序，获取报表名称，名称以同样用分号分隔
    if (tmp_report_2 != null && tmp_report_2.size() > 0) {
      Iterator it = tmp_report_2.iterator();
      String[] reportIdArr = null;
      while (it.hasNext()) {
        // 拼接查询报表名称的sql
        String reportNameSql = "select report_id,report_name from reportcy_manager where rg_code='" + getRgCode()
          + "' and report_id in(";
        Map tmpMap = (Map) it.next();
        // 分隔报表ID
        reportIdArr = tmpMap.get("report_id").toString().split(";");
        // 根据ID拼接sql
        for (int i = 0; i < reportIdArr.length; i++) {
          reportNameSql = reportNameSql + "'" + reportIdArr[i] + "',";
        }
        // 去掉字符串第一个字母“；”
        reportNameSql = reportNameSql.substring(0, reportNameSql.length() - 1) + ")";
        // 执行查询
        List reportNameList = dao.findBySql(reportNameSql);
        // 拼接report_name
        if (reportNameList != null) {
          Map reportNameMap = new HashMap();
          // 为了是报表名称与id顺序一致，先把报表名称放到map中，便于查找
          for (int i = 0; i < reportNameList.size(); i++) {
            Map map = (Map) reportNameList.get(i);
            reportNameMap.put(map.get("report_id"), map.get("report_name"));
          }
          // 拼接报表名称
          String reportName = "";
          for (int i = 0; i < reportIdArr.length; i++) {
            reportName = reportName + ";" + reportNameMap.get(reportIdArr[i]);
          }
          tmpMap.put("report_name", reportName.substring(1));
        }
        // 加到报表主列表中
        tmp_report.add(tmpMap);
      }
    }

    XMLData tmp;
    // 将数据库中的enabled和is_default转换成Boolean
    Boolean enabled;
    Boolean is_default;
    int tmp_int;
    Iterator report = tmp_report.iterator();
    List report_id_List = new ArrayList();
    while (report.hasNext()) {
      tmp = (XMLData) report.next();
      // 整理字段“enabled”
      tmp_int = Integer.parseInt((String) tmp.get("enabled"));
      if (tmp_int == 1) {
        enabled = Boolean.TRUE;
        tmp.remove("enabled");
        tmp.put("enabled", enabled);
      } else {
        // enabled = Boolean.FALSE;
        tmp.remove("enabled");
      }
      // 整理字段“is_default”
      tmp_int = Integer.parseInt((String) tmp.get("is_default"));
      if (tmp_int == 1) {
        is_default = Boolean.TRUE;
        tmp.remove("is_default");
        tmp.put("is_default", is_default);
      } else {
        is_default = Boolean.FALSE;
        tmp.remove("is_default");
      }
      report_id_List.add(tmp);
    }
    return report_id_List;
  }

  /**
   * 保存“打印模版”的数据
   * 
   * @param printModeData
   *            （XMLData,字段参考表sys_print_models）
   * @param billtype_id
   *            String 对应的单据类型id
   * @throws Exception
   *             向外抛出sql错误
   * @author justin 2017年7月13日添加, modify by wanghongtao 保存时，先根据billtype_id删除，再添加。
   */
  public void savePrintModeData(List printModeData, String billtype_id) throws Exception {

    //add by wanghongtao 删除billtype_id相关联的打印模板
    deletePrintModeData(billtype_id);

    if (printModeData != null) {
      Iterator printModeDataitr = printModeData.iterator();
      // 年份
      //      String set_year = (String) SessionUtil.getUserInfoContext().getAttribute("set_year");
      String set_year = SessionUtil.getLoginYear();
      // 现在的时间
      String latest_op_date = null;
      // 最后操作的用户

      String latest_op_user = (String) SessionUtil.getUserInfoContext().getAttribute("user_id");
      //测试数据

      String report_id = null;
      String enabled;
      String is_default;
      String sql = null;
      String id = null;
      Boolean tmp_Boolean;
      String arg_list = null;
      String para_list = null;
      String display_order = null;
      try {
        while (printModeDataitr.hasNext()) {
          XMLData tmp = (XMLData) printModeDataitr.next();
          //          // 此条数据创建的时间，如果为新增数据，此条数据为null，否则有数据，（原来就存在）
          report_id = (String) tmp.get("report_id");
          arg_list = tmp.get("arg_list") == null ? "" : tmp.get("arg_list").toString();
          para_list = tmp.get("para_list") == null ? "" : tmp.get("para_list").toString();
          display_order = tmp.get("display_order") == null ? "" : tmp.get("display_order").toString();

          // 获取是否默认
          if ("Y".equals(tmp.get("enabled"))) {
            enabled = "1";
          } else {
            enabled = "0";
          }
          // 获取是否默认
          if ("Y".equals(tmp.get("is_default"))) {
            is_default = "1";
          } else {
            is_default = "0";
          }
          latest_op_date = DateHandler.getToday();
          id = UUIDRandom.generate();
          sql = "insert into sys_print_models (BILLTYPE_REPORT_ID,BILLTYPE_ID, REPORT_ID, ENABLED, IS_DEFAULT, CREATE_DATE, LATEST_OP_DATE, LATEST_OP_USER, SET_YEAR, ARG_LIST, PARA_LIST, DISPLAY_ORDER,RG_CODE) values (?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";
          dao.executeBySql(sql, new Object[] { id, billtype_id, report_id, enabled, is_default, latest_op_date,
            latest_op_date, latest_op_user, set_year, arg_list, para_list, display_order, SessionUtil.getRgCode() });
        }
      } catch (Exception ex) {
        throw ex;
      }
    }
  }

  /**
   * 删除“打印模版”的数据
   * 
   * @param billtype_id
   *            String 对应的单据类型id
   * @throws Exception
   *             向外抛出sql错误
   * @author justin  2017年7月13日添加
   */
  public void deletePrintModeData(String billtype_id) throws Exception {
    String sql = "delete from sys_print_models where billtype_id=?";
    try {
      dao.executeBySql(sql, new Object[] { billtype_id });
    } catch (Exception ex) {
      throw ex;
    }
  }

  /**
   * 根据单据类型id获取单据类型所有信息
   * 
   * @param billtype_id
   *            String 对应的单据类型id
   * @throws Exception
   *             向外抛出sql错误
   * @return XMLData (数据参考： sys_billtype表字段 ＋ assele（List
   *         XMLData－sys_billtype_assele表字段） ＋ valueset（List
   *         XMLData－sys_print_models表字段） ＋ report（List
   *         XMLData－sys_billtype_valueset表字段）)， 没有数据返回null
   */
  public XMLData getSysBilltypeById(String billtype_id) throws Exception {
    // 获取单据类型基本信息
    XMLData sysBilltype = this.getBasicSysBilltypeById(billtype_id);
    if (sysBilltype != null) {
      // 获取单据类型辅助要素信息
      sysBilltype.put("assele", this.getSysBilltypeAsseleById(billtype_id));

      // 获取单据类型打印模版信息
      sysBilltype.put("valueset", this.getSysBilltypeValuesetById(billtype_id));

      // 获取单据类型定值规则信息
      sysBilltype.put("report", this.getSysBilltypeReportById(billtype_id));
    }
    return sysBilltype;
  }

  /**
   * 根据单据类型id获取单据类型所有信息
   * 
   * @param billtype_id
   *            String 对应的单据类型id
   * @throws Exception
   *             向外抛出sql错误
   * @return XMLData (数据参考： sys_billtype表字段 ＋ assele（List
   *         XMLData－sys_billtype_assele表字段） ＋ valueset（List
   *         XMLData－sys_print_models表字段） ＋ report（List
   *         XMLData－sys_billtype_valueset表字段）)， 没有数据返回null
   */
  public XMLData getOneSysBilltypeById(String billtype_id) throws Exception {
    // 获取单据类型基本信息
    XMLData sysBilltype = this.getBasicSysBilltypeById(billtype_id);
    if (sysBilltype != null) {
      // 获取单据类型辅助要素信息
      sysBilltype.put("assele", this.findSysBilltypeAsselesByBillTypeId(billtype_id));
      // 获取单据类型打印模版信息
      sysBilltype.put("report", this.getPrintModeData(billtype_id));
      // 获取单据类型定值规则信息
      sysBilltype.put("valueset", this.getSysBilltypeValuesetBybilltypeId(billtype_id));//可能有问题
      // 获取单据类型单号规则信息
      sysBilltype.put("billnoruleline",
        iSysBillNoRule.getSysBillNoruleBybillnorulelineId((String) sysBilltype.get("billnorule_id")));
    }
    return sysBilltype;
  }

  /**
   * 根据billtype_id查询定值规则信息
   * @param billtype_id
   * @return
   * @throws Exception
   */
  public List getSysBilltypeValuesetBybilltypeId(String billtype_id) throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();

    String sql = " select a.*, b.field_code,b.field_name, c.ele_rule_id, c.ele_rule_name from "
      + " sys_billtype_valueset a left join sys_metadata b on a.field_code = b.field_code "
      + " left join sys_ele_rule c on a.ele_rule_id = c.ele_rule_id "
      + "where a.billtype_id =? and a.rg_code =? and a.set_year =? ";
    try {
      List sysBilltypeList = dao.findBySql(sql, new Object[] { billtype_id, rg_code, setYear });
      return sysBilltypeList;
    } catch (Exception ex) {
      throw ex;
    }
  }

  /**
   * 查询新增修改定值规则初始化信息
   * @param billtype_id
   * @return
   * @throws Exception
   */
  public Map<String, Object> getsBilltypeAsselesInit() throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    Map<String, Object> map = new HashMap<String, Object>();
    String sql1 = "select valueset_id,field_code,valueset_type from sys_billtype_valueset where rg_code=? and set_year=?";
    String sql2 = "select ele_rule_id,ele_rule_name,ele_rule_code,ele_code from sys_ele_rule where rg_code=? and set_year=?";

    try {
      List sysValuesetTypeList = dao.findBySql(sql1, new Object[] { rg_code, setYear });
      List sysValuesetNameList = dao.findBySql(sql2, new Object[] { rg_code, setYear });
      map.put("sysValuesetTypeList", sysValuesetTypeList);
      map.put("sysValuesetNameList", sysValuesetNameList);
      return map;
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
  }

  /**
   * 根据单据类型id获取单据类型基本信息
   * 
   * @param billtype_id
   *            String 对应的单据类型id
   * @throws Exception
   *             向外抛出sql错误
   * @return XMLData (数据参考：sys_billtype表字段)，没有数据返回null
   */
  public XMLData getBasicSysBilltypeById(String billtype_id) throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();

    String sql = "select * from sys_billtype sb where sb.billtype_id=? and sb.rg_code=? and set_year=?";
    XMLData sysBilltype = null;
    try {
      List sysBilltypeList = dao.findBySql(sql, new Object[] { billtype_id, rg_code, setYear });
      if (sysBilltypeList.size() > 0) {
        sysBilltype = (XMLData) sysBilltypeList.get(0);
      }
    } catch (Exception ex) {
      throw ex;
    }
    return sysBilltype;
  }

  /**
   * 根据单据类型id获取单据类型辅助要素信息
   * 
   * @param billtype_id
   *            String 对应的单据类型id
   * @throws Exception
   *             向外抛出sql错误
   * @return List（XMLData 参考sys_billtype_assele表字段）
   */
  public List getSysBilltypeAsseleById(String billtype_id) throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();

    String sql = "select * from sys_billtype_assele sba where sba.billtype_id=? and sb.rg_code=? and sb.set_year=?";
    try {
      List sysBilltypeList = dao.findBySql(sql, new Object[] { billtype_id, rg_code, setYear });
      return sysBilltypeList;
    } catch (Exception ex) {
      throw ex;
    }
  }

  /**
   * 根据单据类型id获取单据类型打印模版信息
   * 
   * @param billtype_id
   *            String 对应的单据类型id
   * @throws Exception
   *             向外抛出sql错误
   * @return List（XMLData 参考sys_billtype_valueset表字段）
   */
  public List getSysBilltypeValuesetById(String billtype_id) throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();

    String sql = "select * from sys_billtype_valueset sbv where sbv.billtype_id=? and sbv.rg_code=? and sbv.set_year=?";
    try {
      List sysBilltypeList = dao.findBySql(sql, new Object[] { billtype_id, rg_code, setYear });
      return sysBilltypeList;
    } catch (Exception ex) {
      throw ex;
    }
  }

  /**
   * 根据单据类型id获取单据类型定值规则信息
   * 
   * @param billtype_id
   *            String 对应的单据类型id
   * @throws Exception
   *             向外抛出sql错误
   * @return List（XMLData 参考sys_print_models表字段）
   */
  public List getSysBilltypeReportById(String billtype_id) throws Exception {
    String sql = "select * from sys_print_models sbr where sbr.billtype_id=?";
    try {
      List sysBilltypeList = dao.findBySql(sql, new Object[] { billtype_id });
      return sysBilltypeList;
    } catch (Exception ex) {
      throw ex;
    }
  }

  public List findGLCoaDetails(String coa_id) throws Exception {
    StringBuffer sb = new StringBuffer();
    if (typeOfDB.isOracle()) {
      sb.append(
        "select b1.*, decode(b1.level_num, -2, '任意级次', -1, '底级', 0, '自定义级次', 1, '一级', 2, '二级', 3, '三级', 4, '四级') level_num_name, b2.ele_name from gl_coa_detail b1, sys_element b2 ")
        .append(
          " where b1.ele_code = b2.ele_code AND b1.set_year = b2.set_year AND b1.rg_code = b2.rg_code and coa_id = ?");
    } else if (typeOfDB.isMySQL()) {
      sb.append(
        "select b1.*, case b1.level_num when -2 then '任意级次' when -1 then '底级' when 0 then '自定义级次'  when 1 then '一级' when 2 then '二级' when 3 then '三级' when 4 then '四级' end level_num_name, b2.ele_name from gl_coa_detail b1, sys_element b2 ")
        .append(
          " where b1.ele_code = b2.ele_code AND b1.set_year = b2.set_year AND b1.rg_code = b2.rg_code and coa_id = ?");
    }
    return dao.findBySql(sb.toString(), new Object[] { coa_id });
  }

  public List findGLCoaEleValues(String ele, String coa_id) throws Exception {
    IDictionary dic = (IDictionary) SessionUtil.getServerBean("sys.dictionaryService");
    return dic.findEleValues(ele, null, null, false, coa_id, null, " order by chr_code");
  }

  /*
   * update sys_rule,sys_ele_rule_detail 增加区划字段
   * */
  public void createNewEleRuleViaExistsEleRule(String old_rule_id, String ele_rule_id, String ele_value,
    String ele_code, String ele_name) throws Exception {
    String set_year = SessionUtil.getLoginYear();
    String rule_code = UUIDRandom.generate();
    String right_group_id = UUIDRandom.generate();
    String ele_rule_detail_id = UUIDRandom.generate();
    String user_id = SessionUtil.getUserInfoContext().getUserID();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String date = dateFormat.format(new Date());
    String rule_id = "";
    String rg_code = SessionUtil.getRgCode();

    StringBuffer sb = new StringBuffer();
    //在sys_rule表中插入数据
    if (typeOfDB.isOracle()) {
      sb.append("insert into sys_rule(rule_id, rule_code, set_year,rg_code, enabled, rule_classify, right_type)")
        .append(" values(seq_other_id.nextval, '").append(rule_code).append("', '").append(set_year).append("', '")
        .append(rg_code).append("', 1, '005', '0') ");
    } else if (typeOfDB.isMySQL()) {
      sb.append("insert into sys_rule(rule_id, rule_code, set_year,rg_code, enabled, rule_classify, right_type)")
        .append(" values(nextval('seq_other_id'), '").append(rule_code).append("', '").append(set_year).append("', '")
        .append(rg_code).append("', 1, '005', '0') ");
    }
    dao.executeBySql(sb.toString());

    sb = new StringBuffer();
    sb.append("select rule_id from sys_rule where rule_code = ? and rg_code= ? and set_year = ? ");
    List ls = dao.findBySql(sb.toString(), new Object[] { rule_code, rg_code, set_year });
    rule_id = ((XMLData) (ls.get(0))).get("rule_id").toString();

    //在sys_right_group表中插入数据
    sb = new StringBuffer();
    sb.append(
      "insert into sys_right_group(right_group_id, right_group_describe, right_type, rule_id, rg_code, set_year) ")
      .append(" values(?, ?, ?, ?, ?, ?)");
    dao.executeBySql(sb.toString(), new Object[] { right_group_id, "001", "0", rule_id, rg_code, set_year });

    //在sys_right_group_type表重插入数据
    sb = new StringBuffer();
    sb.append("insert into sys_right_group_type(ele_code, right_group_id, right_type, set_year,rg_code) ")
      .append(" select  distinct a1.ele_code, '").append(right_group_id).append("', '0', '").append(set_year)
      .append("','").append(rg_code).append("' from sys_right_group_detail a1, sys_right_group a2 ")
      .append(" where a1.right_group_id = a2.right_group_id and a1.rg_code = a2.rg_code").append(" and a2.rule_id = '")
      .append(old_rule_id).append("' and a2.rg_code = '").append(rg_code).append("' and a2.set_year = '")
      .append(set_year).append("'");
    dao.executeBySql(sb.toString());

    sb = new StringBuffer();
    sb.append("insert into sys_right_group_detail(ele_code, right_group_id, ele_value, set_year,rg_code)")
      .append(" select distinct a1.ele_code, '").append(right_group_id).append("', '#', a1.set_year,a1.rg_code ")
      .append(" from sys_right_group_detail a1, sys_right_group a2 ")
      .append(" where a1.right_group_id = a2.right_group_id and a1.rg_code = a2.rg_code").append(" and a1.rg_code= '")
      .append(rg_code).append("' and a2.rule_id = '").append(old_rule_id).append("' and a1.set_year = '")
      .append(set_year).append("'");
    dao.executeBySql(sb.toString());

    sb = new StringBuffer();
    sb.append("insert into sys_ele_rule_detail(ele_rule_detail_id, ele_value, ele_rule_id, rule_id, ")
      .append(" create_date, create_user, latest_op_date, latest_op_user, last_ver, set_year,rg_code, ")
      .append(" ele_code, ele_name) ").append(" values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
    Object pas[] = new Object[] { ele_rule_detail_id, ele_value, ele_rule_id, rule_id, date, user_id, date, user_id,
      date, set_year, rg_code, ele_code, ele_name };
    dao.executeBySql(sb.toString(), pas);
  }

  public List getRelatedBillType(String billTypeId, String ele_rule_id) throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();

    StringBuffer sb = new StringBuffer();
    sb.append("select a1.*, a2.field_code from sys_billtype a1, sys_billtype_valueset a2 ")
      .append(" where a1.billtype_id = a2.billtype_id ").append(" and a2.ele_rule_id = ? and a1.billtype_id <> ? ")
      .append(" and a1.set_year = a2.set_year and a1.rg_code = a2.rg_code")
      .append(" and a1.set_year=? and a1.rg_code=? ");

    return dao.findBySql(sb.toString(), new Object[] { ele_rule_id, billTypeId, setYear, rg_code });
  }

  public List getFieldByBillTypeViaCoaID(String billType_id, String coa_id) throws Exception {

    String rg_code = getRgCode();
    String setYear = getSetYear();

    StringBuffer strSQL = new StringBuffer("select coa_id,table_name from sys_billtype").append(Tools.addDbLink())
      .append(" where billtype_id='").append(billType_id).append("' and rg_code=? and set_year=?");
    List billTypeList = dao.findBySql(strSQL.toString(), new String[] { rg_code, setYear });
    List fieldList = new ArrayList();
    ICoa coa = (ICoa) SessionUtil.getServerBean("sys.coaService");
    if (coa_id != null && !coa_id.equals("")) {
      XMLData coaInfo = coa.getCoabyID(coa_id);
      List fieldListByCoa = null;
      if (coaInfo != null && coaInfo.get("row") != null) {
        fieldListByCoa = (List) coaInfo.get("row");
      }
      for (int i = 0; fieldListByCoa != null && i < fieldListByCoa.size(); i++) {
        XMLData map = (XMLData) fieldListByCoa.get(i);
        XMLData mapField = new XMLData();
        mapField.put("field_code", map.get("ele_code").toString().toLowerCase() + "_id");
        mapField.put("ele_code", map.get("ele_code").toString().toLowerCase());
        fieldList.add(mapField);
      }
    }
    // 通过源表得到字段
    if (((XMLData) billTypeList.get(0)).get("table_name") != null) {
      String sourceTab = ((XMLData) billTypeList.get(0)).get("table_name").toString();
      String fieldStr = DatabaseAccess.getFieldString(sourceTab);
      StringTokenizer st = new StringTokenizer(fieldStr, ",");
      while (st.hasMoreTokens()) {
        String fieldCode = st.nextToken();
        if (fieldCode.equalsIgnoreCase("ccid") || fieldCode.equalsIgnoreCase("rcid")) {
          continue;
        }
        XMLData map = new XMLData();
        map.put("field_code", fieldCode.toLowerCase());
        map.put("ele_code", fieldCode.toLowerCase().replaceAll("_id", ""));
        fieldList.add(map);
      }
    }
    return getFieldByBillTypeHelper(fieldList);
  }

  /**
   * 得到行政区划代码
   * 
   * @return
   * 
   * 
   */
  private String getRgCode() {
    return SessionUtil.getRgCode();
  }

  /**
  * 根据要素定值规则id查询要素定值规则详细表
  * @param eleRuleDetailId
  * @return XMLData
  * @throws Exception
  */
  @Override
  public XMLData selectByDetailId(String eleRuleDetailId) throws Exception {
    StringBuffer strSQL = new StringBuffer();
    try {
      strSQL.append("select * from sys_ele_rule_detail").append(Tools.addDbLink())
        .append(" where ele_rule_detail_id = '").append(eleRuleDetailId).append("'");
      List<?> list = dao.findBySql(strSQL.toString());
      return (XMLData) list.get(0);
    } catch (Exception e) {
      Log.error(e.getMessage() + org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
      System.out.println("执行sql出错：\n" + e.getMessage() + "\nSQL开始\n" + strSQL + "\nSQL结束");
      throw e;
    }

  }

  /**
   * 编辑要素定值规则主表
   */
  @Override
  public Map<String, Object> updateEleRule(Map value) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    StringBuffer strSQL = new StringBuffer();
    try {
      String eleRuleID = "";
      String eleRuleCode = "";
      String eleRuleName = "";
      String eleCode = "";
      eleRuleID = value.get("ele_rule_id").toString();
      eleRuleCode = value.get("ele_rule_code").toString();
      eleRuleName = value.get("ele_rule_name").toString();
      eleCode = value.get("ele_code").toString();
      strSQL.append("update  SYS_ELE_RULE").append(Tools.addDbLink()).append(" set ").append("ELE_RULE_NAME='")
        .append(eleRuleName).append("',LATEST_OP_USER='").append((String) SessionUtil.getUserInfoContext().getUserID())
        .append("',LATEST_OP_DATE='").append(DateHandler.getLastVerTime()).append("',SET_YEAR='")
        .append(SessionUtil.getLoginYear()).append("'").append(",LAST_VER='").append(Tools.getCurrDate()).append("'")
        // dingyy20120601 加上rg_code,ELE_RULE_ID过滤，不然把所有数据都更新了
        .append(" where rg_code='").append(SessionUtil.getRgCode()).append("' and ELE_RULE_ID='").append(eleRuleID)
        .append("'");
      dao.executeBySql(strSQL.toString());
      result.put("ele_rule_id", eleRuleID);
      result.put("ele_rule_code", eleRuleCode);
      result.put("ele_rule_name", eleRuleName);
      result.put("ele_code", eleCode);
      result.put("set_year", SessionUtil.getLoginYear());
      strSQL = null;
      return result;
    } catch (Exception e) {
      Log.error(e.getMessage() + org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
      Log.debug("执行sql出错：\n" + e.getMessage() + "\nSQL开始\n" + strSQL + "\nSQL结束");
      System.out.println("执行sql出错：\n" + e.getMessage() + "\nSQL开始\n" + strSQL + "\nSQL结束");
      throw new Exception(e.getMessage() + "sql语句:" + strSQL);
    }
  }

  /**
   * 新增要素定值规则主表
   */
  @Override
  public Map<String, Object> insertEleRule(Map value) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    StringBuffer strSQL = new StringBuffer();
    try {
      String eleRuleID = "";
      String eleRuleCode = "";
      String eleRuleName = "";
      String eleCode = "";
      // ele_rule_id不存在 则为新增数据
      if ((value.get("ele_rule_id") != null && value.get("ele_rule_id").equals("")) || value.get("ele_rule_id") == null) {
        eleRuleID = UUIDRandom.generate();
        eleRuleName = value.get("ele_rule_name").toString();
        eleRuleCode = value.get("ele_rule_code").toString();
        eleCode = value.get("ele_code").toString();
        strSQL
          .append("INSERT  INTO  SYS_ELE_RULE")
          .append(Tools.addDbLink())
          .append(
            " (ELE_RULE_ID,ELE_RULE_CODE,ELE_RULE_NAME,ELE_CODE,create_user,create_date,LATEST_OP_USER, LATEST_OP_DATE, SET_YEAR, LAST_VER,RG_CODE) ")
          .append(" VALUES ('").append(eleRuleID).append("','").append(eleRuleCode).append("','").append(eleRuleName)
          .append("','").append(eleCode.toUpperCase()).append("' ,'")
          .append((String) SessionUtil.getUserInfoContext().getUserID()).append("', '")
          .append(DateHandler.getLastVerTime()).append("','")
          .append((String) SessionUtil.getUserInfoContext().getUserID()).append("', '")
          .append(DateHandler.getLastVerTime()).append("','").append(SessionUtil.getLoginYear()).append("', '")
          .append(Tools.getCurrDate()).append("', '").append(SessionUtil.getRgCode()).append("')");
        dao.executeBySql(strSQL.toString());
        result.put("ele_rule_id", eleRuleID);
        result.put("ele_rule_code", eleRuleCode);
        result.put("ele_rule_name", eleRuleName);
        result.put("ele_code", eleCode);
        result.put("set_year", SessionUtil.getLoginYear());
      }
      strSQL = null;
      return result;
    } catch (Exception e) {
      Log.error(e.getMessage() + org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
      Log.debug("执行sql出错：\n" + e.getMessage() + "\nSQL开始\n" + strSQL + "\nSQL结束");
      System.out.println("执行sql出错：\n" + e.getMessage() + "\nSQL开始\n" + strSQL + "\nSQL结束");
      throw new Exception(e.getMessage() + "sql语句:" + strSQL);
    }
  }

  /**
  * 根据eleRuleId查询要素定值规则详细信息列表
  * @param ele_rule_id
  * @return
  */
  public List getRuleInfo(String eleRuleID) throws Exception {
    String rg_code = SessionUtil.getRgCode();
    String set_year = SessionUtil.getLoginYear();
    StringBuffer strSQL = new StringBuffer();
    try {
      List result = new ArrayList();
      strSQL = strSQL.append("select ele_code from sys_ele_rule where ele_rule_id='").append(eleRuleID).append("'");
      List list = dao.findBySql(strSQL.toString());
      if (list != null && list.size() > 0 && ((Map) list.get(0)).get("ele_code") != null) {
        String a = ((Map) list.get(0)).get("ele_code").toString();
        XMLData sourceTableMap = dictionary.getElementSetByCode(((Map) list.get(0)).get("ele_code").toString());
        String eleTable = sourceTableMap.get("ele_source").toString();
        strSQL.delete(0, strSQL.length());
        strSQL.append("select eleTable.chr_code,eleTable.chr_id,eleTable.chr_name,")
          .append("ruleDetail.rule_id,ruleDetail.ele_rule_detail_id,ruleDetail.ele_rule_id,")
          .append("ruleDetail.ele_value,ruleDetail.set_year,").append("rule.rule_code,rule.rule_name from ")
          .append(eleTable).append(Tools.addDbLink()).append(" eleTable").append(" left join sys_ele_rule_detail")
          .append(Tools.addDbLink()).append(" ruleDetail on eleTable.chr_id =")
          .append("ruleDetail.Ele_Value and ruleDetail.Ele_Rule_Id ='").append(eleRuleID)
          .append("' left join sys_rule").append(Tools.addDbLink()).append(" rule on ruleDetail.rule_id =")
          .append("rule.rule_id ").append("where eleTable.rg_code='").append(rg_code)
          .append("' and eleTable.set_year=").append(set_year).append(" order by eleTable.chr_code");
        result = dao.findBySql(strSQL.toString());
        strSQL = null;
      }
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      Log.error("执行sql出错：\n" + e.getMessage() + "\nSQL开始\n" + strSQL + "\nSQL结束");
      throw e;
    }
  }

  /**
   * 新增
   */
  @Override
  public void insertBillType(BillTypeForm form, String userCode) throws Exception {
    try {

      // 保存billType和保存定值规则
      if ("".equals(form.getBillTypeId()) || form.getBillTypeId() == null) {
        //新增自动生成billtype_id
        form.setBillTypeId(UUIDRandom.generate());
      }

      XMLData xmlData = getXmlData(form);
      saveorUpdateSysBillType(xmlData);

      // 保存“打印模版”的数据
      if (form.getPrintData() != null && !"".equals(form.getPrintData())) {
        savePrintModeData(parseList(form.getPrintData()), form.getBillTypeId(), userCode);
      }

      // 更新与billtypeId相关联的sys_billtype_assele
      if (form.getAssistData() != null && !"".equals(form.getAssistData())) {
        updateSysBilltypeAssele(form.getBillTypeId(), parseList(form.getAssistData()));
      }

    } catch (Exception e) {
      e.printStackTrace();
      Log.error("执行insertBillType方法出错：\n");
      throw e;
    }
  }

  /**
   * 删除
   */
  public void delete(BillTypeForm form) throws Exception {
    try {// 判断是否要级联删除，如果有辅助要素，则需要级联删除
      if (findSysBilltypeAsselesByBillTypeId(form.getBillTypeId()) != null) {
        // 删除单据类型和辅助要素
        deleteSysBillTypeByBillTypeId(form.getBillTypeId());
      } else {
        XMLData xmlData = getXmlData(form);
        // 只删除单据类型
        deleteSysBillType(xmlData);
      }
      // 删除“打印模版”关联数据
      deletePrintModeData(form.getBillTypeId());
    } catch (Exception e) {
      e.printStackTrace();
      Log.error("执行delete方法出错：\n");
      throw e;
    }
  }

  /**
   * 取消规则(删除定值规则详细表和规则相关数据)
   */
  @Override
  public void deleteEleRuleDetailAndRule(String eleRuleID, String eleValue, String ruleId) throws Exception {
    //1.删除要素定值规则详细表
    StringBuffer strSQL = new StringBuffer("select 1 from sys_ele_rule_detail").append(Tools.addDbLink())
      .append(" a where a.ele_rule_id = '").append(eleRuleID).append("' and ele_value='").append(eleValue).append("'");
    List list = dao.findBySql(strSQL.toString());
    if (list.size() < 1) {
      throw new Exception("本要素定值规则已经为空!");
    }
    strSQL.delete(0, strSQL.length());
    strSQL.append("delete from sys_ele_rule_detail").append(Tools.addDbLink()).append(" where ele_rule_id='")
      .append(eleRuleID).append("' and ele_value='").append(eleValue).append("'");
    dao.executeBySql(strSQL.toString());
    strSQL = null;
    //2.删除规则相关数据
    if (ruleId != null && !"".equals(ruleId)) {
      String delete_sql = "";
      try {

        String rg_code = SessionUtil.getRgCode();
        String set_year = SessionUtil.getLoginYear();
        // 删除权限组类型表
        if (typeOfDB.isOracle()) {
          delete_sql = "delete from  sys_right_group_type" + Tools.addDbLink()
            + " b where b.right_group_id in (select right_group_id from sys_right_group" + Tools.addDbLink()
            + " where rule_id=?) ";

          delete_sql += " and b.set_year= " + set_year + " and b.rg_code='" + rg_code + "'";
        }
        if (typeOfDB.isMySQL()) {
          delete_sql = "delete from  sys_right_group_type" + Tools.addDbLink()
            + "  where right_group_id in (select right_group_id from sys_right_group" + Tools.addDbLink()
            + " where rule_id=?) ";

          delete_sql += " and set_year= " + set_year + " and rg_code='" + rg_code + "'";
        }
        dao.executeBySql(delete_sql, new Object[] { ruleId });

        // 删除权限明细表
        if (typeOfDB.isOracle()) {
          delete_sql = "delete from  sys_right_group_detail" + Tools.addDbLink()
            + " b where b.right_group_id in (select right_group_id from sys_right_group" + Tools.addDbLink()
            + " where rule_id=?) ";

          delete_sql += " and b.set_year= " + set_year + " and b.rg_code='" + rg_code + "'";
        } else if (typeOfDB.isMySQL()) {
          delete_sql = "delete from  sys_right_group_detail" + Tools.addDbLink()
            + "  where right_group_id in (select right_group_id from sys_right_group" + Tools.addDbLink()
            + " where rule_id=?) ";

          delete_sql += " and set_year= " + set_year + " and rg_code='" + rg_code + "'";
        }
        dao.executeBySql(delete_sql, new Object[] { ruleId });
        // 删除主范围和追加范围
        delete_sql = "delete from sys_rule_cross_join_add" + Tools.addDbLink() + " b where b.rule_id = ?";
        dao.executeBySql(delete_sql, new Object[] { ruleId });

        // 删除追减范围
        delete_sql = "delete from sys_rule_cross_join_del" + Tools.addDbLink() + " b where b.rule_id = ?";
        dao.executeBySql(delete_sql, new Object[] { ruleId });

        // 删除权限组主表
        delete_sql = "delete from  sys_right_group" + Tools.addDbLink() + " b where rule_id=?";

        delete_sql += " and b.set_year= " + set_year + " and b.rg_code='" + rg_code + "'";

        dao.executeBySql(delete_sql, new Object[] { ruleId });

        // 删除规则主表
        delete_sql = "delete from  sys_rule" + Tools.addDbLink() + " b where b.rule_id=? ";
        dao.executeBySql(delete_sql, new Object[] { ruleId });
      } catch (Exception e) {
        e.printStackTrace();
        Log.error("执行sql出错：\n" + e.getMessage() + "\nSQL开始\n" + delete_sql + "\nSQL结束");
        throw e;
      }
    }
  }

  /**
  * json字符串转换为list对象
  * @param jsonStr
  * @return
  */
  public List<XMLData> parseList(String jsonStr) {

    List<XMLData> xmlDataList = new ArrayList<XMLData>();
    if (null != jsonStr) {
      xmlDataList = JSON.parseObject(jsonStr, new TypeReference<List<XMLData>>() {
      }.getType());
    }

    return xmlDataList;
  }

  /**
  * 将form数据装成XMLData格式
  *
  * @param form
  * @return
  */
  private XMLData getXmlData(BillTypeForm form) {

    XMLData xmlData = new XMLData();

    xmlData.put("billtype_id", form.getBillTypeId());
    xmlData.put("billtype_code", form.getBillTypeCode());
    xmlData.put("billtype_name", form.getBillTypeName());
    xmlData.put("is_busincrease", form.getIsBusIncrease());
    xmlData.put("busvou_type_id", form.getBusVouTypeId());
    xmlData.put("coa_id", form.getCoaId());
    xmlData.put("billnorule_id", form.getBillNoRuleId());
    xmlData.put("table_name", form.getTableName());
    xmlData.put("billtype_class", form.getBillTypeClass());
    xmlData.put("enabled", form.getEnabled());
    xmlData.put("latest_op_date", form.getLatestOpDate());
    xmlData.put("latest_op_user", form.getLatestOpUser());
    xmlData.put("last_ver", form.getLastVer());
    xmlData.put("nobudgetnusvou_type_id", form.getNoBudgetNusVouTypeId());
    xmlData.put("is_needchecknobudget", form.getIsNeedCheckNoBudget());
    xmlData.put("sys_id", form.getSysId());
    xmlData.put("field_name", form.getFieldName());
    xmlData.put("ui_id", form.getUiId());
    xmlData.put("from_billtype_id", form.getFromBillTypeId());
    xmlData.put("from_ui_id", form.getFromUiId());
    xmlData.put("to_billtype_id", form.getToBillTypeId());
    xmlData.put("to_ui_id", form.getToUiId());
    xmlData.put("vou_control_id", form.getVouControlId());
    xmlData.put("eleRuleMap", parseList(form.getCusRoleData()));
    return xmlData;

  }

  /**
   * 保存“打印模版”的数据 (修改)
   * 
   * @param printModeData
   *            （XMLData,字段参考表sys_print_models）
   * @param billtype_id
   *            String 对应的单据类型id
   * @throws Exception
   * 
   */
  public void savePrintModeData(List printModeData, String billtype_id, String userCode) throws Exception {

    //add by wanghongtao 删除billtype_id相关联的打印模板
    deletePrintModeData(billtype_id);

    if (printModeData != null) {
      Iterator printModeDataitr = printModeData.iterator();
      // 年份
      //      String set_year = (String) SessionUtil.getUserInfoContext().getAttribute("set_year");
      String set_year = SessionUtil.getLoginYear();
      // 现在的时间
      String latest_op_date = null;
      // 最后操作的用户
      String latest_op_user = userCode;
      //String latest_op_user = (String) SessionUtil.getUserInfoContext().getAttribute("user_id");
      //测试数据

      String report_id = null;
      String enabled;
      String is_default;
      String sql = null;
      String id = null;
      Boolean tmp_Boolean;
      String arg_list = null;
      String para_list = null;
      String display_order = null;
      try {
        while (printModeDataitr.hasNext()) {
          XMLData tmp = (XMLData) printModeDataitr.next();
          //          // 此条数据创建的时间，如果为新增数据，此条数据为null，否则有数据，（原来就存在）
          report_id = (String) tmp.get("report_id");
          arg_list = tmp.get("arg_list") == null ? "" : tmp.get("arg_list").toString();
          para_list = tmp.get("para_list") == null ? "" : tmp.get("para_list").toString();
          display_order = tmp.get("display_order") == null ? "" : tmp.get("display_order").toString();

          // 获取是否默认
          if ("Y".equals(tmp.get("enabled"))) {
            enabled = "1";
          } else {
            enabled = "0";
          }
          // 获取是否默认
          if ("Y".equals(tmp.get("is_default"))) {
            is_default = "1";
          } else {
            is_default = "0";
          }
          latest_op_date = DateHandler.getToday();
          id = UUIDRandom.generate();
          sql = "insert into sys_print_models (BILLTYPE_REPORT_ID,BILLTYPE_ID, REPORT_ID, ENABLED, IS_DEFAULT, CREATE_DATE, LATEST_OP_DATE, LATEST_OP_USER, SET_YEAR, ARG_LIST, PARA_LIST, DISPLAY_ORDER,RG_CODE) values (?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";
          dao.executeBySql(sql, new Object[] { id, billtype_id, report_id, enabled, is_default, latest_op_date,
            latest_op_date, latest_op_user, set_year, arg_list, para_list, display_order, SessionUtil.getRgCode() });
        }
      } catch (Exception ex) {
        throw ex;
      }
    }
  }

}
