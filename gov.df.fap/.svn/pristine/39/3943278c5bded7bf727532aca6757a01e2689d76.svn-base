package gov.df.fap.service.rule;

import gov.df.fap.api.rule.IDataRight;
import gov.df.fap.api.rule.IRuleConfigure;
import gov.df.fap.api.rule.ISysRight;
import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.bean.rule.dto.FRightSqlDTO;
import gov.df.fap.bean.rule.dto.RuleDTO;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.UtilCache;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.xml.XMLData;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 数据权限接口实现
 * 
 * @version 1.0
 * @author 
 * @since java 1.6.0.34
 * 
 */
@Service
public class DataRightBO implements IDataRight {

  /* 查询用户的SQL */
  private static final String QUERY_USERS_SQL = "SELECT * FROM SYS_USERMANAGE";

  private static final String INSERT_USER_ORG_SQL = "INSERT INTO SYS_USER_ORG VALUES(?,?,to_char(sysdate,'yyyy-mm-dd hh:mm:ss'),?)";

  private static final String INSERT_USER_ORG_SQL_ForMySQL = "INSERT INTO SYS_USER_ORG VALUES(?,?,date_format(now(),'%Y-%m-%d %H:%i:%S'), ?)";

  /* */
  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao;

  /**
   * 用户和机构类型缓存
   */
  private static final Map userOrgTypeCache = new HashMap();

  /**
   * 用户和相关机构缓存
   */
  public static final Map userOrgCache = new HashMap();

  /**
   *  实现返回业务表的数据权限sql语句
   *  返回类似display.mb_id in(..) 或者 display.en_id in(..)的语句
   *  使用范围为走工作流时的数据权限查询
   * @param userid-------------用户id
   * @param roleid-------------角色id
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */
  public String getSqlBusiRight(String userid, String roleid, String tablealias) throws Exception {
    return getSqlBusiRightByUser(userid, tablealias) + getSqlBusiRightByUserAndRole(userid, roleid, tablealias);
  }

  /**
   * 实现返回业务表的数据权限sql语句
   * 返回类似and exists(select 1 from gl_ccids where ..)的语句
   * 使用范围:走工作流或者不走工作流时都可以使用，走工作流时最好使用getSqlBusiRight方法
   * 本方法为兼容旧的权限查询而保留,只能用于不走工作流时的权限查询
   * @param userid-------------用户id
   * @param roleid-------------角色id
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */
  public String getSqlBusiRightForOld(String userid, String roleid, String tablealias) throws Exception {
    return getSqlBusiRightByUserForOld(userid, tablealias) + getSqlBusiRightByUserAndRole(userid, roleid, tablealias);
  }

  @Autowired
  private IRuleConfigure iRuleConfigure;

  @Autowired
  private ISysRight iSysRight;

  private String rule_id;

  private RuleDTO returnRuleDto = new RuleDTO();

  private UIRuleOperation smop = new UIRuleOperation();

  /**
  * 保存或者新增权限组
  */
  public Map<String, Object> saveOrUpdate(HttpServletRequest request, HttpServletResponse response) {
    // iRuleConfigure.saveOrUpdateRule(xmlData);
    Map<String, Object> map = new HashMap<String, Object>();
    String rule_code = request.getParameter("rule_code");
    String rule_name = request.getParameter("rule_name");
    String remark = request.getParameter("remark");

    returnRuleDto.setRULE_CODE(rule_code);
    returnRuleDto.setRULE_NAME(rule_name);
    returnRuleDto.setREMARK(remark);
    returnRuleDto.setENABLED(1);
    returnRuleDto.setRULE_CLASSIFY("006");
    returnRuleDto.setRIGHT_TYPE(1);
    returnRuleDto.setRG_CODE(SessionUtil.getRgCode());
    returnRuleDto.setSET_YEAR(SessionUtil.getLoginYear());

    try {
      if (!isExist(rule_code)) {
        smop.saveOrUpdateRule(this.returnRuleDto);
      } else {
        map.put("result", "fail");
        map.put("msg", "权限编码已存在！");
      }

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    this.rule_id = this.returnRuleDto.getRULE_ID().toString();

    return map;
  }

  /**
   * 判断权限组是否存在
   * @param ruleCode 权限编码
   * @return 
   * @throws Exception
   */
  private boolean isExist(String ruleCode) throws Exception {
    return smop.isExist(ruleCode);
  }

  /**
   * 获取所有权限组集合
   * @return map
   */
  public Map<String, Object> showAllGROUPlist() {
    Map<String, Object> map = new HashMap<String, Object>();
    List groupList = iSysRight.getAllSysRightGroup();
    map.put("groupList", groupList);
    return map;
  }

  /**
   * 实现返回业务表的RCID
   * 
   * @param inObjXml-----------各要素值
   * @return RCID
   * @throws Exception---------错误信息
  	ydz 2011/05/30 优化
   */
  public String getRcid(XMLData inObjXml) throws Exception {
    String set_Year = "";
    if (inObjXml.containsKey("set_year")) {
      set_Year = (String) inObjXml.get("set_year");
    }

    if (set_Year == null || set_Year.equals("")) {
      set_Year = SessionUtil.getUserInfoContext().getSetYear();
    }
    String rg_code = "";
    if (inObjXml.containsKey("rg_code")) {
      rg_code = (String) inObjXml.get("rg_code");
    }

    if (rg_code == null || rg_code.equals("")) {
      rg_code = SessionUtil.getUserInfoContext().getRgCode();
    }
    StringBuffer insertrightsql = new StringBuffer();
    insertrightsql.append(" insert into sys_right_combination(");
    String ele_code = "";
    String ele_value = "";
    StringBuffer findRcidSql = new StringBuffer();
    String insertRoleRcidSql2 = "";
    String guid = "";
    String sql = "select * from Sys_Element where IS_RIGHTFILTER=1 and SET_YEAR= ? and RG_CODE = ?";

    // 读取缓存中的权限要素列表
    List lstDataType = UtilCache.getRightEleList(set_Year + "#" + rg_code);
    if (lstDataType == null) {
      String setYear = set_Year;
      lstDataType = dao.findBySql(sql.replaceAll(Tools.addDbLink(), ""), new Object[] { setYear, rg_code });
      if (lstDataType == null || lstDataType.size() == 0) {
        setYear = SessionUtil.getLoginYear();
        lstDataType = dao.findBySql(sql.replaceAll(Tools.addDbLink(), ""), new Object[] { setYear, rg_code });
      }
      UtilCache.putRightEleList(setYear + "#" + rg_code, lstDataType);
    }

    if (lstDataType == null || lstDataType.size() == 0)
      throw new Exception("没有找到系统权限要素设置！");
    Iterator it = lstDataType.iterator();
    String[] eleValueString = new String[2 * lstDataType.size()];
    //插入权限表值集合
    String[] insertRightData = new String[lstDataType.size() + 4];
    String[] findSqlData = new String[lstDataType.size() + 2];

    StringBuffer inserRightValueStr = new StringBuffer();
    int w = 0;
    while (it.hasNext()) {
      XMLData rightSet = (XMLData) it.next();
      ele_code = rightSet.get("ele_code").toString();
      ele_value = "";// 清空历史值

      insertrightsql.append(ele_code + "_ID,");
      inserRightValueStr.append("?,");

      findRcidSql.append(" and ").append(ele_code).append("_ID = ?");

      if (inObjXml.get(ele_code.toLowerCase() + "_id") != null
        && !inObjXml.get(ele_code.toLowerCase() + "_id").toString().equals("")) {
        ele_value = inObjXml.get(ele_code.toLowerCase() + "_id").toString();
        insertRightData[w] = ele_value;
        findSqlData[w] = ele_value;
        eleValueString[w] = ele_value;
        w = w + 1;

        insertRoleRcidSql2 = insertRoleRcidSql2 + " and (a." + ele_code + "_id is null or a." + ele_code + "_id= ?) ";
      } else {
        //如果要素的id为null，则在sys_right_combination表中查询和插入时，使用
        //“#”代替，这样可以保证相关的索引生效
        insertRightData[w] = "#";
        findSqlData[w] = "#";
        w = w + 1;

      }
    }

    findSqlData[w] = set_Year;
    findSqlData[w + 1] = rg_code;//modify by kongcy  add rg_code at 2012-03-30 08:30:43 

    sql = "select * from Sys_Right_Combination com where 1=1 " + findRcidSql + " and com.SET_YEAR=? and com.RG_CODE =?";

    lstDataType = dao.findBySql(sql, findSqlData);

    Iterator it1 = lstDataType.iterator();
    if (it1.hasNext()) {
      XMLData sysRightCombination = (XMLData) it1.next();
      return sysRightCombination.get("rcid").toString();
    }

    insertrightsql.append("rcid,set_year,update_flag,rg_code) values(").append(inserRightValueStr.toString())
      .append("?,?,?,?)");

    List elevaluesList = new ArrayList();

    guid = UUIDRandom.generateNumberBySeqServer("SEQ_RCID");
    elevaluesList.add(guid);
    for (int j = 0; j < 2; j++) {
      for (int i = 0; i < eleValueString.length; i++) {
        if (eleValueString[i] != null)
          elevaluesList.add(eleValueString[i]);
      }
    }

    String[] elevalues = (String[]) elevaluesList.toArray(new String[elevaluesList.size()]);

    insertRightData[w] = guid;

    String insert_sql = "insert into sys_rule_rcid"
      + "(set_year,rule_id,rcid,last_ver,rg_code) (select "
      + set_Year
      + ",b.rule_id,?,"
      + (TypeOfDB.isOracle() ? "sysdate" : "sysdate()")
      + ","
      + rg_code
      + " from sys_rule b where b.rule_classify = '006' and exists(select 1 from sys_rule_cross_join_add a where a.rule_id=b.rule_id "
      + insertRoleRcidSql2 + " ) and not exists(select 1 from sys_rule_cross_join_del a where a.rule_id=b.rule_id "
      + insertRoleRcidSql2 + "))";

    dao.executeBySql(insert_sql, elevalues);
    insertRightData[w + 1] = set_Year;
    insertRightData[w + 2] = "1";
    insertRightData[w + 3] = rg_code;
    dao.executeBySql(insertrightsql.toString(), insertRightData);
    return guid;
  }

  /**
   * 实现返回业务表的数据权限sql语句，并返回带?
   * 
   * @param userid-------------用户id
   * @param roleid-------------角色id
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */
  public FRightSqlDTO getSqlBusiRightByJoinWithQry(String userid, String roleid, String tablealias) throws Exception {

    FRightSqlDTO return_dto = new FRightSqlDTO();
    if (userid == null || roleid == null || tablealias == null) {
      return_dto.addWhere_clause(" and 1=0 ");
      return return_dto;
    }
    if ((userid != null) && !userid.toString().equals("") && (tablealias != null) && !tablealias.toString().equals("")) {
      // 查询该用户是否有机构类型,机构权限已经缓存
      String sql = "select b.* from Sys_User a,sys_orgtype b" + " where a.USER_ID=? and a.org_type=b.orgtype_code";
      List lstDataType = null;
      String elecode = "";
      lstDataType = dao.findBySql(sql, new String[] { userid });
      if (lstDataType != null && lstDataType.size() > 0) {
        XMLData m = new XMLData();
        m = (XMLData) lstDataType.get(0);
        elecode = (String) m.get("ele_code");

        if (elecode == null || elecode.equals("")) {
          // return " and 1=1";
        } else {
          // 由于机构权限使用的是IS_NULL，所以机构权限还使用EXISTS这种形式
          // return_dto.addTable_clause(",gl_ccids cc,sys_user_org org
          // ");
          // return_dto.addWhere_clause(" and "+tablealias+ ".ccid =
          // cc.ccid and org.user_id = '"+
          // userid+"'and (org.org_id = cc." + elecode
          // + "_id or cc." + elecode
          // + "_id is null)");
          return_dto.addWhere_clause(" and exists(select 1 from gl_ccids" + " cc where " + tablealias
            + ".ccid = cc.ccid and (cc." + elecode + "_id is null or exists (select org_id from sys_user_org"
            + " org where org.user_id = '" + userid + "' and org.org_id = cc." + elecode
            + "_id and org.set_year=cc.set_year)))");
          // return_dto.setUser_id_by_org(userid);
          // return_dto.setOrg_right_flag(true);
        }
      }
      // }
    }

    // 通过用户和角色选出对应权限的类型 ０为全部权限，１为部分权限，２为没有权限
    String sql = "select r.right_type from sys_rule r,sys_user_role_rule s"
      + " where r.rule_id=s.rule_id and s.user_id=? and s.role_id=?";
    List rs = null;
    rs = dao.findBySql(sql, new Object[] { userid, roleid });
    if (rs.size() == 0) {
      sql = "select r.right_type from sys_rule r, sys_user_role_rule s, Sys_Useraccredit ua "
        + "where r.rule_id = s.rule_id and ua.accredit_to = ? and s.role_id = ? and s.user_id = ua.accredit_from "
        + "and ua.start_time < (select "
        + (TypeOfDB.isOracle() ? " to_char(sysdate,'YYYY-MM-DD') from dual" : " date_format(sysdate(), '%Y-%m-%d') ")
        + " ) " + "and ua.end_time > (select "
        + (TypeOfDB.isOracle() ? " to_char(sysdate,'YYYY-MM-DD') from dual" : " date_format(sysdate(), '%Y-%m-%d') ")
        + " )";
      rs = dao.findBySql(sql, new Object[] { userid, roleid });
      if (rs.size() == 0) {
        return_dto.addWhere_clause(" and 1=0 ");
      } else {
        Map map = (Map) rs.get(0);
        if (Integer.parseInt((String) map.get("right_type")) == 0) {
          // return " and 1=1";
        }
        if (Integer.parseInt((String) map.get("right_type")) == 1) {
          return_dto.addTable_clause(",sys_rule_rcid rr, sys_user_role_rule urr ");
          return_dto.addWhere_clause(" and urr.user_id= '" + userid + "'and urr.role_id= '" + roleid
            + "' and urr.rule_id=rr.rule_id and rr.rcid =" + tablealias + ".rcid");
          return_dto.setUser_id_by_right(userid);
          return_dto.setRole_id_by_right(roleid);
          return_dto.setData_right_flag(true);

        }
        if (Integer.parseInt((String) map.get("right_type")) == 2) {
          return_dto.addWhere_clause(" and 1=0 ");
          // return return_dto;
        }
      }
      // return return_dto;
    } else {
      Map map = (Map) rs.get(0);
      if (Integer.parseInt((String) map.get("right_type")) == 0) {
        // return " and 1=1";
      }
      if (Integer.parseInt((String) map.get("right_type")) == 1) {
        return_dto.addTable_clause(",sys_rule_rcid rr, sys_user_role_rule urr ");
        return_dto.addWhere_clause(" and urr.user_id= '" + userid + "'and urr.role_id= '" + roleid
          + "' and urr.rule_id=rr.rule_id and rr.rcid =" + tablealias + ".rcid");
        return_dto.setUser_id_by_right(userid);
        return_dto.setRole_id_by_right(roleid);
        return_dto.setData_right_flag(true);

      }
      if (Integer.parseInt((String) map.get("right_type")) == 2) {
        return_dto.addWhere_clause(" and 1=0 ");
        // return return_dto;
      }
    }

    return return_dto;
  }

  /**
   * 数据权限新增时候需要标签列
   */
  public Map<String, Object> initEnabledElement(HttpServletRequest request, HttpServletResponse response)
    throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();

    String sql = "select * from sys_element where rg_code =? and set_year=? and is_rightfilter=1";
    List rs = null;
    List result = new ArrayList();

    rs = dao.findBySql(sql, new Object[] { (String) SessionUtil.getRgCode(), (String) SessionUtil.getLoginYear() });
    for (int i = 0; i < rs.size(); i++) {
      Map tempmap = (Map) rs.get(i);
      String ele_source = (String) tempmap.get("ele_source");
      String selectSql = "select "
        + (TypeOfDB.isOracle() ? " chr_code|| ' ' ||chr_name " : " concat(chr_code, ' ' ,chr_name) ")
        + " as chr_name,t.chr_id as chr_id,t.parent_id as parent_id from " + ele_source
        + " t where rg_code =? and set_year=?";
      List tmprs = null;
      tmprs = dao.findBySql(selectSql,
        new Object[] { (String) SessionUtil.getRgCode(), (String) SessionUtil.getLoginYear() });
      tempmap.put("element_list", tmprs);
      result.add(tempmap);
    }
    map.put("enable_elements", result);
    return map;

  }

  /**
   * 实现返回业务表的RCID
   * 
   * @param inputFVoucherDto---DTO
   * @return RCID
   * @throws Exception---------错误信息
   */
  public String getRcidByDto(FVoucherDTO inputFVoucherDto) throws Exception {
    if (inputFVoucherDto != null) {
      return this.getRcid(inputFVoucherDto.toXMLData());
    } else {
      return null;
    }

  }

  /**
   * 实现返回业务表的数据权限sql语句（通过用户、角色s）
   * 
   * @param roleid-------------角色id
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */
  public String getSqlBusiRightByUserAndRole(String user_id, String role_id, String tablealias) throws Exception {
    if (user_id == null || role_id == null || tablealias == null) {
      return "";
    }

    String rule_id = "";
    // 通过用户和角色选出对应权限的类型 ０为全部权限，１为部分权限，２为没有权限
    String sql = "select r.right_type,s.rule_id from sys_rule r,sys_user_role_rule s"
      + " where r.rule_id=s.rule_id and s.user_id=? and s.role_id=?";
    List rs = null;
    rs = dao.findBySql(sql, new Object[] { user_id, role_id });
    if (rs.size() == 0) {
      return " and 1=0";
    }
    if (!tablealias.equals("")) {
      tablealias = tablealias + ".";
    }
    Map map = (Map) rs.get(0);
    rule_id = (String) map.get("rule_id");
    if (Integer.parseInt((String) map.get("right_type")) == 0) {
      return " and 1=1";
    }
    if (Integer.parseInt((String) map.get("right_type")) == 1) {
      return " and exists(select 1 from sys_rule_rcid r where r.rule_id = '" + rule_id + "' and r.rcid =" + tablealias
        + "rcid) ";
    }
    if (Integer.parseInt((String) map.get("right_type")) == 2) {
      return " and 1=0";
    }
    return "";
  }

  /**
   * 实现返回业务表的数据权限sql语句（通过用户）
   * 
   * @param userid-------------用户id
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */
  public String getSqlBusiRightByUser(String userid, String tablealias) throws Exception {
    if ((userid != null) && !userid.toString().equals("") && (tablealias != null) && !tablealias.toString().equals("")) {
      String sql = "select b.* from Sys_User a,sys_orgtype b where a.USER_ID=? and a.org_type=b.orgtype_code";
      List lstDataType = null;
      String elecode = "";
      lstDataType = dao.findBySql(sql, new String[] { userid });
      if (lstDataType != null && lstDataType.size() > 0) {
        XMLData m = new XMLData();
        m = (XMLData) lstDataType.get(0);
        elecode = (String) m.get("ele_code");

        List orgList = getOrgByUserId(userid);

        int orgSize = orgList.size();
        List paraList = new ArrayList();
        for (int i = 0; i < orgSize; i++) {
          paraList.add(((Map) orgList.get(i)).get("org_id"));
        }
        if (elecode == null || elecode.equals("")) {
          return " and 1=1";
        } else {
          String whereSql = Tools.getInSql(tablealias, elecode + "_id", paraList, "2");
          return " and ( " + (TypeOfDB.isOracle() ? "nvl(" : "ifnull(") + tablealias + "." + elecode
            + "_id,'#') = '#' or (" + whereSql + " )) ";
          //          return " and exists(select 1 from gl_ccids" + " cc where " + tablealias
          //            + ".ccid = cc.ccid and (cc." + elecode + "_id is null or exists (select org_id from sys_user_org"
          //            + " org where org.user_id = '" + userid + "' and org.org_id = cc." + elecode
          //            + "_id and cc.set_year=org.set_year)))";
        }
      } else {
        return " and 1=1";
      }
      // }
    }

    return " and 1=0 ";
  }

  /**
   * 实现返回业务表的数据权限sql语句（通过用户）
   * 
   * @param userid-------------用户id
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */
  public String getSqlBusiRightByUserForOld(String userid, String tablealias) throws Exception {
    if ((userid != null) && !userid.toString().equals("") && (tablealias != null) && !tablealias.toString().equals("")) {
      String sql = "select b.* from Sys_User a,sys_orgtype b where a.USER_ID=? and a.org_type=b.orgtype_code";
      List lstDataType = null;
      String elecode = "";
      lstDataType = dao.findBySql(sql, new String[] { userid });
      if (lstDataType != null && lstDataType.size() > 0) {
        XMLData m = new XMLData();
        m = (XMLData) lstDataType.get(0);
        elecode = (String) m.get("ele_code");

        if (elecode == null || elecode.equals("")) {
          return " and 1=1";
        } else {
          return " and exists(select 1 from gl_ccids" + " cc where " + tablealias + ".ccid = cc.ccid and (cc."
            + elecode + "_id is null or exists (select org_id from sys_user_org" + " org where org.user_id = '"
            + userid + "' and org.org_id = cc." + elecode + "_id and cc.set_year=org.set_year)))";
        }
      } else {
        return " and 1=1";
      }
    }

    return " and 1=0 ";
  }

  /**
   * 实现返回业务表的数据权限sql语句（通过用户,没有CCID）
   * 
   * @param userid-------------用户id
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */
  public String getSqlBusiRightByUserNoCCID(String userid, String tablealias) throws Exception {

    if ((userid != null) && !userid.toString().equals("") && (tablealias != null) && !tablealias.toString().equals("")) {
      // 查询该用户是否有机构类型
      String sql = "select b.* from Sys_User a,sys_orgtype b where a.USER_ID= ? and a.org_type=b.orgtype_code";
      List lstDataType = null;
      String elecode = "";
      lstDataType = dao.findBySql(sql, new String[] { userid });
      if (lstDataType != null && lstDataType.size() > 0) {
        XMLData m = new XMLData();
        m = (XMLData) lstDataType.get(0);
        elecode = (String) m.get("ele_code");

        if (elecode == null || elecode.equals("")) {
          return " and 1=1";
        } else {
          return " and exists(select 1 from sys_user_org" + Tools.addDbLink() + " org where" + " org.user_id = '"
            + userid + "' and org.org_id = " + tablealias + "." + elecode + "_id and org.set_year=" + tablealias
            + ".set_year)";
        }
      } else {
        return " and 1=1";
      }
    }

    return " and 1=0 ";

  }

  /**
   * 实现返回业务表的数据权限sql语句（没有CCID）
   * 
   * @param userid-------------用户id
   * @param roleid-------------角色id
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */
  public String getSqlBusiRightNoCCID(String userid, String roleid, String tablealias) throws Exception {
    return getSqlBusiRightByUserNoCCID(userid, tablealias)
    // + getSqlBusiRightByRole(roleid, tablealias);
      + getSqlBusiRightByUserAndRole(userid, roleid, tablealias);
  }

  /**
   * 实现返回业务表的数据权限sql语句
   * 
   * @param userid-------------用户id
   * @param roleid-------------角色id
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */
  public FRightSqlDTO getSqlBusiRightByJoin(String userid, String roleid, String tablealias) throws Exception {
    FRightSqlDTO return_dto = new FRightSqlDTO();
    if (userid == null || roleid == null || tablealias == null) {
      return_dto.addWhere_clause(" and 1=0 ");
      return return_dto;
    }
    if ((userid != null) && !userid.toString().equals("") && (tablealias != null) && !tablealias.toString().equals("")) {
      String sql = "select b.* from sys_user_orgtype a,sys_orgtype b where a.USER_ID=? and a.org_type=b.orgtype_code";
      List lstDataType = null;
      String elecode = "";
      lstDataType = dao.findBySql(sql, new String[] { userid });
      if (lstDataType != null && lstDataType.size() > 0) {
        XMLData m = new XMLData();
        m = (XMLData) lstDataType.get(0);
        elecode = (String) m.get("ele_code");

        if (elecode == null || elecode.equals("")) {

        } else {
          List orgList = getOrgByUserId(userid);
          //平台拼的数据权限sql（en_id in）in列表超过1000报错
          int orgSize = orgList.size();
          List paraList = new ArrayList();
          for (int i = 0; i < orgSize; i++) {
            paraList.add(((Map) orgList.get(i)).get("org_id"));
          }
          String whereSql = Tools.getInSql(tablealias, elecode + "_id", paraList, "2");
          return_dto.addWhere_clause(" and ( " + (TypeOfDB.isOracle() ? "nvl(" : "ifnull(") + tablealias + "."
            + elecode + "_id,'#') = '#' or  (" + whereSql.toString() + " )) ");
        }
      }
    }

    // 通过用户和角色选出对应权限的类型 ０为全部权限，１为部分权限，２为没有权限
    String sql = "select r.right_type from sys_rule r,sys_user_role_rule s where r.rule_id=s.rule_id and s.user_id=? and s.role_id=?";
    List rs = null;
    rs = dao.findBySql(sql, new Object[] { userid, roleid });
    if (rs.size() == 0) {
      return_dto.addWhere_clause(" and 1=0 ");
      // return return_dto;
    } else {
      Map map = (Map) rs.get(0);
      if (Integer.parseInt((String) map.get("right_type")) == 0) {
        // return " and 1=1";
      }
      if (Integer.parseInt((String) map.get("right_type")) == 1) {
        return_dto.addTable_clause(",sys_rule_rcid rr, sys_user_role_rule urr ");
        return_dto.addWhere_clause(" and urr.user_id= ? and urr.role_id= ? and urr.rule_id=rr.rule_id and rr.rcid ="
          + tablealias + ".rcid");
        return_dto.setUser_id_by_right(userid);
        return_dto.setRole_id_by_right(roleid);
        return_dto.setData_right_flag(true);

      }
      if (Integer.parseInt((String) map.get("right_type")) == 2) {
        return_dto.addWhere_clause(" and 1=0 ");
        // return return_dto;
      }
    }

    return return_dto;
  }

  /**
   * 提取当前系统所有的用户列表
   * 
   * @return List对象（内部含XMLDATA)
   * @throws Exception---------错误信息
   */
  public List getAllUser() throws Exception {
    List userList = new ArrayList();
    try {
      userList = dao.findBySql(QUERY_USERS_SQL);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return userList;
  }

  /**
   * 根据预算单位和所选的用户，保存到对应的权限设置中
   * 
   * @return List对象（内部含XMLDATA,user_id,en_id)
   * @throws Exception---------错误信息
   */
  public void saveUserRight(List userRightList) throws Exception {
    try {
      // 将原数据中添加物理表名更新版本等信息
      /*
       * 
       * for(int i=0; i<userRightList.size(); i++) { //表名
       * ((XMLData)userRightList.get(i)).put("table_name", TABLE_NAME);
       * //版本 ((XMLData)userRightList.get(i)).put("last_ver",
       * DateHandler.getToday()); } dao.saveOrUpdateAll(userRightList);
       */
      for (int i = 0; i < userRightList.size(); i++) {
        Object[] obj = new Object[] { ((XMLData) userRightList.get(i)).get("user_id"),
          ((XMLData) userRightList.get(i)).get("en_id"), SessionUtil.getLoginYear() };
        dao.executeBySql((TypeOfDB.isOracle() ? INSERT_USER_ORG_SQL : INSERT_USER_ORG_SQL_ForMySQL), obj);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  /**
   * 大批量的rcid生成
   * 
   * @param tableName
   *            调用该接口的传入的表名
   * @param whereSql
   *            在实现类中的拼sql语句中的附加的过滤条件
   * @throws Exception
   */

  public void getBatchRCID(String tableName, String whereSql) throws Exception {
    createBatchRCID(tableName, whereSql);
  }

  public void createBatchRCID(String tableName, String whereSql) throws Exception {
    Session session = dao.getSession();
    Connection conn = session.connection();
    CallableStatement cs = conn.prepareCall("{call create_batch_rcid(?, ? ,?,?)}");

    cs.setString(1, tableName);
    cs.setString(2, whereSql);
    cs.setString(3, SessionUtil.getRgCode());
    cs.setString(4, SessionUtil.getLoginYear());
    try {
      cs.execute();
    } finally {
      if (cs != null)
        cs.close();
      if (session != null)
        dao.closeSession(session);
    }
  }

  /**
   * 根据用户ID取出所属机构类型
   * @param userId
   * @return
   */
  public String getOrgTypeByUserId(String userId) {
    if (!userOrgTypeCache.containsKey(userId)) {
      String sql = "select b.ele_code from Sys_User a,sys_orgtype b where a.USER_ID=? and a.org_type=b.orgtype_code";
      List userOrgTypeList = dao.findBySql(sql, new String[] { userId });
      for (int i = 0; i < userOrgTypeList.size(); i++) {
        XMLData m = (XMLData) userOrgTypeList.get(i);
        userOrgTypeCache.put(userId,
          StringUtils.isEmpty((String) m.get("ele_code")) ? StringUtils.EMPTY : m.get("ele_code").toString()
            .toLowerCase()
            + "_id");
      }
    }
    return userOrgTypeCache.get(userId).toString();
  }

  /**
   * 根据用户ID取出所属机构类型
   * @param userId
   * @return
   */
  public List getOrgByUserId(String userId) {
    if (!userOrgCache.containsKey(userId)) {
      String sql = "select a.org_id from sys_user_org a where a.USER_ID=? and a.set_year=?";
      List userOrgList = dao.findBySql(sql, new String[] { userId, SessionUtil.getLoginYear() });
      userOrgCache.put(userId, userOrgList);
    }
    return (List) userOrgCache.get(userId);
  }

  /**
   * 清空用户机构权限缓存
   */
  public static void removeRoleMenu() {
    if (null != userOrgCache && userOrgCache.size() > 0) {
      userOrgCache.clear();
    }
  }

  @Override
  public FRightSqlDTO getSqlBusiRightNoRcid(String userid, String roleid, String tablealias) throws Exception {
    // TODO Auto-generated method stub
    FRightSqlDTO return_dto = new FRightSqlDTO();
    if (userid == null || roleid == null || tablealias == null) {
      return_dto.addWhere_clause(" and 1=0 ");
      return return_dto;
    }
    if ((userid != null) && !userid.toString().equals("") && (tablealias != null) && !tablealias.toString().equals("")) {
      // 查询该用户是否有机构类型,机构权限已经缓存
      String sql = "select b.* from Sys_User a,sys_orgtype b" + " where a.USER_ID=? and a.org_type=b.orgtype_code";
      List lstDataType = null;
      String elecode = "";
      lstDataType = dao.findBySql(sql, new String[] { userid });
      if (lstDataType != null && lstDataType.size() > 0) {
        XMLData m = new XMLData();
        m = (XMLData) lstDataType.get(0);
        elecode = (String) m.get("ele_code");

        if (elecode == null || elecode.equals("")) {
          // return " and 1=1";
        } else {
          return_dto.addWhere_clause(" and exists (select org_id from sys_user_org" + " org where org.user_id = '"
            + userid + "' and org.org_id = " + tablealias + "." + elecode + "_id and org.set_year=" + tablealias
            + ".set_year)");
        }
      }
      // }
    }

    // 通过用户和角色选出对应权限的类型 ０为全部权限，１为部分权限，２为没有权限
    String sql = "select r.right_type from sys_rule r,sys_user_role_rule s"
      + " where r.rule_id=s.rule_id and s.user_id=? and s.role_id=?";
    List rs = null;
    rs = dao.findBySql(sql, new Object[] { userid, roleid });
    if (rs.size() == 0) {
      sql = "select r.right_type from sys_rule r, sys_user_role_rule s, Sys_Useraccredit ua "
        + "where r.rule_id = s.rule_id and ua.accredit_to = ? and s.role_id = ? and s.user_id = ua.accredit_from "
        + "and ua.start_time < (select "
        + (TypeOfDB.isOracle() ? " to_char(sysdate,'YYYY-MM-DD') from dual" : " date_format(sysdate(), '%Y-%m-%d') ")
        + " ) " + "and ua.end_time > (select "
        + (TypeOfDB.isOracle() ? " to_char(sysdate,'YYYY-MM-DD') from dual" : " date_format(sysdate(), '%Y-%m-%d') ")
        + " )";
      rs = dao.findBySql(sql, new Object[] { userid, roleid });
      if (rs.size() == 0) {
        return_dto.addWhere_clause(" and 1=0 ");
      } else {
        Map map = (Map) rs.get(0);
        if (Integer.parseInt((String) map.get("right_type")) == 0) {
        }
        if (Integer.parseInt((String) map.get("right_type")) == 1) {

          String selectElesql = "select distinct t.ele_code,t.right_group_id from sys_right_group_detail t where t.ele_value !='#'  and t.right_group_id in ("
            + " select srp.right_group_id from sys_right_group srp where srp.rule_id in ("
            + " select urr.rule_id from sys_user_role_rule urr where urr.user_id=? and urr.role_id=?))";

          List selectElesqlResult = null;
          selectElesqlResult = dao.findBySql(selectElesql, new Object[] { userid, roleid });
          if (selectElesqlResult.size() == 0) {

          } else {

            String insql = "(";
            //循环拼出rightgroupid
            for (int i = 0; i < selectElesqlResult.size(); i++) {

              Map tempMap = (Map) selectElesqlResult.get(i);
              String tempEleCode = (String) tempMap.get("right_group_id");
              if (i != selectElesqlResult.size()) {
                insql = insql + "'" + tempEleCode + "','";
              } else {
                insql = insql + "'" + tempEleCode + "'";
              }

            }
            insql = insql + ")";

            //循环每个ele_code值
            for (int i = 0; i < selectElesqlResult.size(); i++) {
              Map tempMap = (Map) selectElesqlResult.get(i);
              String tempEleCode = (String) tempMap.get("ele_code");
              return_dto.addWhere_clause("and " + tablealias + "." + tempEleCode
                + "_code in (select srpd.ele_value from sys_right_group_detail srpd  where srpd.ele_code=" + "'"
                + tempEleCode + "'" + " and srpd.right_group_id in " + insql + ")");

            }
          }
        }
        if (Integer.parseInt((String) map.get("right_type")) == 2) {
          return_dto.addWhere_clause(" and 1=0 ");
        }
      }
    } else {
      Map map = (Map) rs.get(0);
      if (Integer.parseInt((String) map.get("right_type")) == 0) {
        // return " and 1=1";
      } else if (Integer.parseInt((String) map.get("right_type")) == 1) {

        String selectElesql = "select distinct t.ele_code,t.right_group_id from sys_right_group_detail t where t.ele_value !='#'  and t.right_group_id in ("
          + " select srp.right_group_id from sys_right_group srp where srp.rule_id in ("
          + " select urr.rule_id from sys_user_role_rule urr where urr.user_id=? and urr.role_id=?))";

        List selectElesqlResult = null;
        selectElesqlResult = dao.findBySql(selectElesql, new Object[] { userid, roleid });
        if (selectElesqlResult.size() == 0) {

        } else {

          String insql = "(";
          //循环拼出rightgroupid
          for (int i = 0; i < selectElesqlResult.size(); i++) {

            Map tempMap = (Map) selectElesqlResult.get(i);
            String tempEleCode = (String) tempMap.get("right_group_id");
            if (i != selectElesqlResult.size() - 1) {
              insql = insql + "'" + tempEleCode + "'',";
            } else {
              insql = insql + "'" + tempEleCode + "'";
            }

          }
          insql = insql + ")";

          //循环每个ele_code值
          for (int i = 0; i < selectElesqlResult.size(); i++) {
            Map tempMap = (Map) selectElesqlResult.get(i);
            String tempEleCode = (String) tempMap.get("ele_code");
            return_dto.addWhere_clause(" and " + tablealias + "." + tempEleCode
              + "_code in (select srpd.ele_value from sys_right_group_detail srpd  where srpd.ele_code=" + "'"
              + tempEleCode + "'" + " and srpd.right_group_id in " + insql + ")");
          }

          return_dto.setUser_id_by_right(userid);
          return_dto.setRole_id_by_right(roleid);
          return_dto.setData_right_flag(true);

        }

      } else if (Integer.parseInt((String) map.get("right_type")) == 2) {
        return_dto.addWhere_clause(" and 1=0 ");
      }
    }

    return return_dto;
  }

  @Override
  public String getSqlBusiRightByUserAndRoleNoRcid(String user_id, String role_id, String tablealias) throws Exception {
    // TODO Auto-generated method stub

    String returnSql = "";

    if (user_id == null || role_id == null || tablealias == null) {
      return "and 1=0";
    }

    String rule_id = "";
    // 通过用户和角色选出对应权限的类型 ０为全部权限，１为部分权限，２为没有权限
    String sql = "select r.right_type,s.rule_id from sys_rule r,sys_user_role_rule s"
      + " where r.rule_id=s.rule_id and s.user_id=? and s.role_id=?";
    List rs = null;
    rs = dao.findBySql(sql, new Object[] { user_id, role_id });
    if (rs.size() == 0) {
      return " and 1=0";
    }
    Map map = (Map) rs.get(0);
    rule_id = (String) map.get("rule_id");
    if (Integer.parseInt((String) map.get("right_type")) == 0) {
      return " and 1=1";
    } else if (Integer.parseInt((String) map.get("right_type")) == 1) {

      String selectElesql = "select distinct t.ele_code,t.right_group_id from sys_right_group_detail t where t.ele_value !='#'  and t.right_group_id in ("
        + " select srp.right_group_id from sys_right_group srp where srp.rule_id in ("
        + " select urr.rule_id from sys_user_role_rule urr where urr.user_id=? and urr.role_id=?))";

      List selectElesqlResult = null;
      selectElesqlResult = dao.findBySql(selectElesql, new Object[] { user_id, role_id });
      if (selectElesqlResult.size() == 0) {
        return "and 1=1";
      } else {
        String insql = "(";
        //循环拼出rightgroupid
        for (int i = 0; i < selectElesqlResult.size(); i++) {

          Map tempMap = (Map) selectElesqlResult.get(i);
          String tempEleCode = (String) tempMap.get("right_group_id");
          if (i != selectElesqlResult.size() - 1) {
            insql = insql + "'" + tempEleCode + "'',";
          } else {
            insql = insql + "'" + tempEleCode + "'";
          }
        }
        insql = insql + ")";

        //循环每个ele_code值
        for (int i = 0; i < selectElesqlResult.size(); i++) {
          Map tempMap = (Map) selectElesqlResult.get(i);
          String tempEleCode = (String) tempMap.get("ele_code");
          returnSql += " and " + tablealias + "." + tempEleCode
            + "_code in (select srpd.ele_value from sys_right_group_detail srpd  where srpd.ele_code=" + "'"
            + tempEleCode + "'" + " and srpd.right_group_id in " + insql + ")";
        }
        return returnSql;
      }
    } else if (Integer.parseInt((String) map.get("right_type")) == 2) {
      return " and 1=0";
    }
    return returnSql;
  }
}
