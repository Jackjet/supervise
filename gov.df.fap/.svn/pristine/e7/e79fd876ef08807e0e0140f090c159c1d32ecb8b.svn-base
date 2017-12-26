/*
 * $Id: SysUserBO.java 564364 2015-04-02 13:47:09Z zhaoqiang1 $
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.service.user;

import gov.df.fap.api.user.ISysUser;
import gov.df.fap.service.rule.DataRightBO;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.MultiDataSource;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.xml.XMLData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 用户管理服务端接口实现类
 * 
 * @author a
 * 
 */
@Service
public class SysUserBO implements ISysUser {
  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao;

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  private final String SYS_USERMANAGE_TABLE = "mappingfiles.sysdb.SysUsermanage";

  /**
   * 得到所有用户数据
   */
  public List getAllSysUsers() throws Exception {
    // String sql = "select * from sys_user" + Tools.addDbLink() + "";
    StringBuffer sb = new StringBuffer();
    sb.append("SELECT * FROM SYS_USER SU,SYS_USER_REGION SUR ");
    sb.append(" WHERE SU.USER_ID=SUR.USER_ID AND SUR.RG_CODE=? and and su.set_year=? ");
    String rg_code = SessionUtil.getRgCode();
    String set_year = SessionUtil.getLoginYear();
    List list = null;
    try {
      List userList = dao.findBySql(sb.toString(), new Object[] { rg_code, set_year });
      // 并入sys_user_org数据
      if (userList != null && userList.size() > 0) {
        list = new ArrayList();
        for (int i = 0; i < userList.size(); i++) {
          XMLData userData = (XMLData) userList.get(i);
          String userId = (String) userData.get("user_id");
          userData.put("org_id", this.findSysUserOrgIdsByUserId(userId));
          list.add(userData);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 通过user_id查找SYS_USERMANAGE
   * 
   * @param userId
   * @return
   * @throws Exception
   */
  public XMLData findSysUserByUserId(String userId) throws Exception {
    // add by wl 20120425sys_user_role_rule区分年度和区划
    String setYear = SessionUtil.getLoginYear();
    String rgCode = SessionUtil.getRgCode();
    //add by YennieChen 2014.9.4 添加移动IMSI、IMEI、黑名单信息 Begin
    //String sql = "select * from sys_user" + Tools.addDbLink() + " where user_id = '" + userId + "'";
    String sql = "select u.*, m.imsi, m.imei, m.is_blacklist from sys_user u, sys_usermanage m where u.user_id = '"
      + userId + "' and u.user_id = m.user_id";
    //add by YennieChen 2014.9.4 添加移动IMSI、IMEI、黑名单信息 End
    String sql1 = "select a.IS_DEFINED,a.role_id,b.role_code,b.role_name,a.rule_id,c.rule_code,c.rule_name from SYS_USER_ROLE_RULE a,sys_role b,sys_rule c where a.role_id=b.role_id and a.rule_id=c.rule_id and a.user_id = '"
      + userId + "' and a.set_year='" + setYear + "' and a.rg_code='" + rgCode + "'";
    List userList1 = dao.findBySql(sql1);
    XMLData is_defin = null;
    if (userList1.size() > 0) {
      is_defin = (XMLData) userList1.get(0);
    }

    XMLData userData = null;
    try {
      List userList = dao.findBySql(sql);
      // 并入sys_user_org数据
      if (userList != null && userList.size() > 0) {
        userData = (XMLData) userList.get(0);
        userData.put("org_id", this.findSysUserOrgIdsByUserId(userId));
        userData.put("rule_mess", userList1);
        if (userList1.size() > 0) {
          userData.put("is_defined", (String) is_defin.get("is_defined"));
          // if(((String) is_defin.get("is_defined")).equals("")
          // ||
          // ((String) is_defin.get("is_defined"))==null){
          // userData.put("is_defined","0");
          // }
          userData.put("defaultdatarole",
            (String) is_defin.get("role_code") + "  " + (String) is_defin.get("role_name"));
          userData.put("datarule", (String) is_defin.get("rule_code") + "   " + (String) is_defin.get("rule_name"));

          userData.put("rule_id", is_defin.get("rule_id"));
        } else {
          userData.put("is_defined", "0");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return userData;
  }

  /**
   * 根据USER_ID查找对应的SYS_USER_ORG.ORG_ID
   * 
   * @return
   */
  public Object[] findSysUserOrgIdsByUserId(String userId) throws Exception {
    String sql = "select org_id from sys_user_org" + Tools.addDbLink() + " where user_id = '" + userId + "'";
    try {
      List list = dao.findBySql(sql);
      //
      if (list != null && list.size() > 0) {
        Object[] orgIds = new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
          XMLData orgData = (XMLData) list.get(i);
          orgIds[i] = ((String) orgData.get("org_id"));
        }
        if (orgIds.length > 0)
          return orgIds;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return null;
  }

  /**
   * 根据审核类型查找用户
   * 
   * @param isAudit
   *            , 1--已审核；0--未审核
   * @return
   * @throws Exception
   */
  public List findSysUserByIsAudit(int isAudit) throws Exception {
    // String sql = "select * from sys_user" + Tools.addDbLink()
    // + " where is_audit = " + isAudit;
    // add by wl 20110322 加入区划查询条件
    StringBuffer sb = new StringBuffer();
    sb.append("SELECT * FROM SYS_USER SU,SYS_USER_REGION SUR ");
    sb.append(" WHERE SU.USER_ID=SUR.USER_ID AND SU.IS_AUDIT=? AND SUR.RG_CODE=? and su.set_year=?");
    String rg_code = SessionUtil.getRgCode();
    String set_year = SessionUtil.getLoginYear();
    List list = null;
    try {
      List userList = dao.findBySql(sb.toString(), new Object[] { new Integer(isAudit), rg_code, set_year });
      // 并入sys_user_org数据
      if (userList != null && userList.size() > 0) {
        list = new ArrayList();
        for (int i = 0; i < userList.size(); i++) {
          XMLData userData = (XMLData) userList.get(i);
          String userId = (String) userData.get("user_id");
          userData.put("org_id", this.findSysUserOrgIdsByUserId(userId));
          list.add(userData);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 根据user_id修改is_audit
   * 
   * @param toBeAuditedUserIds
   * @param isAudit
   */
  public void updateSysUserByIsAudit(String[] toBeAuditedUserIds, int isAudit) throws Exception {
    String updateSql = "update sys_usermanage set is_audit = " + isAudit + ", audit_date = '"
      + DateHandler.getLastVerTime() + "' " + ", audit_user = '"
      + (String) SessionUtil.getUserInfoContext().getUserID() + "' " + " where user_id = ? ";
    try {
      for (int i = 0; toBeAuditedUserIds != null && i < toBeAuditedUserIds.length; i++) {
        String userId = toBeAuditedUserIds[i];
        dao.executeBySql(updateSql, new Object[] { userId });
      }

    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  public List getOrgById(String id, String type) {
    String sql = "select * from sys_orgtype where orgtype_code=?";
    List temp = dao.findBySql(sql, new Object[] { type });
    XMLData data = (XMLData) temp.get(0);
    String eleCode = data.get("ele_code").toString();
    sql = "select * from sys_element where ele_code=?";
    temp = dao.findBySql(sql, new Object[] { eleCode });
    data = (XMLData) temp.get(0);
    sql = "select * from " + data.get("ele_source").toString() + " where chr_id=?";
    return dao.findBySql(sql, new Object[] { id });
  }

  /**
   * 保存用户数据
   */
  public void saveorUpdateSysUser(List userInfo) throws Exception {
    XMLData xmlData = (XMLData) userInfo.get(0);
    String userId = (String) xmlData.get("user_id");
    //    String userCode = (String) xmlData.get("user_code");
    String setYear = (String) xmlData.get("set_year");
    String rgCode = (String) xmlData.get("rg_code");
    if (rgCode == null) {
      rgCode = SessionUtil.getRgCode();
      xmlData.put("rg_code", rgCode);
    }
    if (setYear == null) {
      setYear = SessionUtil.getLoginYear();
      xmlData.put("set_year", setYear);
    }
    //保存机构权限
    reBuildSysUserOrgByUserId(xmlData);

    if (userInfo != null && userInfo.size() > 1) {
      // 角色授权信息
      XMLData ruleInfo = (XMLData) userInfo.get(1);
      // 所选角色信息
      List roleInfo = (ArrayList) userInfo.get(2);
      // 用户权限
      String ruleId = (String) xmlData.get("rule_id");

      // 保存用户权限
      this.deleteSysUserRuleByUserId(userId);
      if (ruleId != null && !ruleId.equals("")) {
        XMLData ruleData = new XMLData();
        // addby wl 20120323加入区划信息
        ruleData.put("rg_code", rgCode);
        ruleData.put("user_id", userId);
        ruleData.put("rule_id", ruleId);
        ruleData.put("set_year", setYear);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        ruleData.put("last_ver", dateFormat.format(date));
        dao.saveDataBySql("sys_user_rule", ruleData);
      }

      // 保存角色权限
      this.deleteSysUserRoleRuleByUserId(userId);
      if (roleInfo != null && roleInfo.size() > 0) {
        for (int i = 0; i < roleInfo.size(); i++) {
          XMLData roleData = (XMLData) roleInfo.get(i);
          XMLData roleRuleData = new XMLData();
          roleRuleData.put("table_name", "mappingfiles.sysdb.SysUserRoleRule");
          // 20120323add by wl 加入区划信息
          roleRuleData.put("rg_code", rgCode);
          roleRuleData.put("user_id", userId);
          roleRuleData.put("role_id", roleData.get("role_id"));
          if (ruleInfo.containsKey(roleData.get("role_id"))) {
            // 自定义权限
            roleRuleData.put("rule_id", ruleInfo.get(roleData.get("role_id")));
            roleRuleData.put("is_defined", "1");
          } else {
            // 默认用户权限
            roleRuleData.put("rule_id", ruleId);
            roleRuleData.put("is_defined", "0");
          }
          roleRuleData.put("set_year", setYear);
          roleRuleData.put("role_name", roleData.get("role_name"));
          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          Date date = new Date();
          roleRuleData.put("last_ver", dateFormat.format(date));
          dao.saveDataBySql("Sys_User_Role_Rule", roleRuleData);
        }
      }
    }
  }

  /**
   * 判断是否为xxxxxx999不允许添加的用户
   * 
   * @param user_code
   * @return
   */
  public boolean isUserCodeNotInsert(String user_code) {

    boolean flag = false;

    if (user_code.length() == 9 && user_code.substring(6, 9).equals("999")) {
      flag = true;
    }

    return flag;
  }

  /**
   * 重新生成sys_user_org相关信息
   * 
   * @param xmlData
   *            用户信息xmlData
   * @throws Exception
   * @author zhupeidong at 2008-11-10下午04:30:58
   */
  private void reBuildSysUserOrgByUserId(XMLData xmlData) throws Exception {
    // 处理sys_user_org
    // 删除相关sys_user_org
    String userId = (String) xmlData.get("user_id");
    //add by gaoqb
    String org_type = (String) xmlData.get("org_type");
    Map orgmap = new HashMap();
    orgmap.put("user_id", userId);
    orgmap.put("org_type", org_type);
    orgmap.put("set_year", SessionUtil.getLoginYear());
    orgmap.put("last_ver", DateHandler.getLastVerTime());
    this.deleteSysUserOrgTypeByUserId(userId);
    dao.saveDataBySql("SYS_USER_ORGTYPE", orgmap);
    this.deleteSysUserOrgByUserId(userId);
    //modified by zhaoqiang 重新生成用户权限时，清空用户权限缓存 2015年4月2日22:01:10
    DataRightBO.removeRoleMenu();
    // modify by wanghongtao
    if (xmlData.get("org_id") != null) {
      Session session = dao.getSession();
      Connection conn = session.connection();

      // 批量插入相关sys_user_org
      PreparedStatement psmt = conn.prepareStatement("insert into sys_user_org" + Tools.addDbLink() + " ("
        + "USER_ID, ORG_ID, LAST_VER, SET_YEAR) " + "values (?, ?, ?, ?)");
      try {
        List list = (ArrayList) xmlData.get("org_id");
        for (int i = 0; i < list.size(); i++) {
          //
          if (!(list.get(i) instanceof XMLData))
            continue;

          String userOrg = (String) ((XMLData) list.get(i)).get("chr_id");
          psmt.setString(1, userId);
          psmt.setString(2, userOrg);
          psmt.setString(3, DateHandler.getLastVerTime());
          psmt.setString(4, SessionUtil.getLoginYear());
          psmt.addBatch();
        }
        psmt.executeBatch();
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      } finally {
        dao.closeSession(session);
      }
    }
  }

  /**
   * 判断是否已经存在相同userId的用户
   * 
   * @param userId
   * @return
   */
  private boolean isUserExist(String userId) {
    String sql = "select count(1) user_count from sys_usermanage where user_id = '" + userId + "'";
    List list = dao.findBySql(sql);
    if (list != null && list.size() > 0) {
      XMLData data = (XMLData) list.get(0);
      int userCount = Integer.parseInt(String.valueOf(data.get("user_count")));
      if (userCount > 0)
        return true;
    }
    return false;
  }

  /**
   * 判断是否已经存在相同userId的用户
   * 
   * @param userId
   * @return
   */
  private boolean isUserCodeExist(String userCode, String setYear) {
    // add by wl 20120410 在不同区划内用户编码可以相同
    String rg_code = SessionUtil.getRgCode();
    String sql = "select count(1) user_count from sys_usermanage su,sys_user_region sur where su.user_id=sur.user_id and su.user_code = ? and sur.rg_code=? and su.set_year = ? ";
    List list = dao.findBySql(sql, new Object[] { userCode, rg_code, setYear });
    if (list != null && list.size() > 0) {
      XMLData data = (XMLData) list.get(0);
      int userCount = Integer.parseInt(String.valueOf(data.get("user_count")));
      if (userCount > 0)
        return true;
    }
    return false;
  }

  /**
   * 根据USER_ID删除sys_user_role_rule
   */
  private void deleteSysUserRoleRuleByUserId(String userId) {
    // add by wl 20120323加入区划条件
    String rg_code = SessionUtil.getRgCode();
    String loginYear = SessionUtil.getLoginYear();
    String delSysUserOrgSql = "delete from sys_user_role_rule" + Tools.addDbLink() + " where user_id = '" + userId
      + "' and rg_code='" + rg_code + "' and set_year='" + loginYear + "'";
    // 删除sys_user_rule
    dao.executeBySql(delSysUserOrgSql);
  }

  /**
   * 根据USER_ID删除SYS_USER_RULE
   */
  private void deleteSysUserRuleByUserId(String userId) {

    // add by wl 20120323加入区划条件
    String rg_code = SessionUtil.getRgCode();
    String loginYear = SessionUtil.getLoginYear();
    String delSysUserOrgSql = "delete from sys_user_rule" + Tools.addDbLink() + " where user_id = '" + userId
      + "' and rg_code='" + rg_code + "' and set_year='" + loginYear + "'";
    // 删除sys_user_rule
    dao.executeBySql(delSysUserOrgSql);
  }

  /**
   * 根据USER_ID删除SYS_USER_ORG
   * 
   * @param userId
   */
  private void deleteSysUserOrgByUserId(String userId) {
    String delSysUserOrgSql = "delete from sys_user_org" + Tools.addDbLink() + " where user_id = ?  ";
    // 删除sys_user_org
    dao.executeBySql(delSysUserOrgSql, new Object[] { userId });
  }

  /**
   * 删除用户数据
   */
  public void deleteSysUser(XMLData xmlData) throws Exception {

    try {
      dao.deleteDataBySql("sys_usermanage", xmlData, new String[] { "user_id" });
      this.deleteSysUserRegionByUserId((String) xmlData.get("user_id"));

    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * add by wl 20110322删除用户时同时把用户和区划关联关系删掉 根据user_id删除sys_user_region表中的数据
   * 
   * @param userid
   */
  public void deleteSysUserRegionByUserId(String userId) {

    String rg_code = SessionUtil.getRgCode();
    String sql = " delete from sys_user_region where user_id=? and rg_code=?";
    dao.executeBySql(sql, new Object[] { userId, rg_code });
  }

  /**
   * 级联删除相关联的用户数据
   * 
   * @param userId
   */
  public void deleteSysUserByUserId(String userId) throws Exception {
    String rg_code = SessionUtil.getRgCode();
    String loginYear = SessionUtil.getLoginYear();
    String delSysUserRoleSql = "delete from sys_user_role_rule" + Tools.addDbLink() + " where user_id = '" + userId
      + "' and rg_code='" + rg_code + "' and set_year=" + loginYear;
    String delSysUserSql = "delete from sys_usermanage" + Tools.addDbLink() + " where user_id = '" + userId + "'";
    // lgc add
    String delSysUserRuleSql = "delete from sys_user_rule where user_id = '" + userId + "' and rg_code='" + rg_code
      + "' and set_year=" + loginYear;
    String delSysUserRoleRule = "delete from sys_user_role_rule where user_id ='" + userId + "' and rg_code='"
      + rg_code + "' and set_year=" + loginYear;
    String user_code = null;
    // lgc end
    try {
      /** add by 黄节 2007年7月16日 start* */
      // 先获取用的code
      String sql = "select su.user_code from sys_usermanage su where su.user_id=?";
      List user_codeList = dao.findBySql(sql, new Object[] { userId });

      if (user_codeList.size() > 0) {
        user_code = (String) ((XMLData) user_codeList.get(0)).get("user_code");
      } else {
        throw new Exception("根据传进的用户id找不到对应的用户信息！");
      }
      // 删除相关联的 sys_user_role
      dao.executeBySql(delSysUserRoleSql);
      // Log.debug("删除 user_id = " + userId + " 相关联sys_user_role_rule记录: "
      // + count + " 条.");
      // 删除相关sys_user_org
      this.deleteSysUserOrgByUserId(userId);
      // 删除sys_user
      dao.executeBySql(delSysUserSql);
      // Log.debug("删除 user_id = " + userId + " sys_usermanage: " + count
      // + " 条.");

      // 删除sys_user_rule
      dao.executeBySql(delSysUserRuleSql);
      // Log.debug("删除 user_id = " + userId + " 相关联sys_user_rule记录: "
      // + count + " 条.");

      // 删除sys_user_role_rule
      dao.executeBySql(delSysUserRoleRule);

      // 删除sys_user_region
      this.deleteSysUserRegionByUserId(userId);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * 通过USER_ID查找SYS_USER_ROLE
   * 
   * @param userId
   * @return lgc update
   */
  public List findSysUserRoleByUserId(String userId) throws Exception {
    // add by wl 20110323加入区划、年份查询条件
    String rg_code = SessionUtil.getRgCode();
    String loginYear = SessionUtil.getLoginYear();
    String sql = "select * from SYS_USER_ROLE_RULE" + Tools.addDbLink()
      + " where user_id = ? and set_year=? and rg_code=?";
    List result = null;
    try {
      result = dao.findBySql(sql, new Object[] { userId, loginYear, rg_code });
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return result;
  }

  /**
   * 通过USER_ID和年度查找SYS_USER_ROLE
   * 
   * @param userId
   * @return lgc update
   */
  public List findSysUserRoleByUserIdAndYear(String userId, String year) throws Exception {
    // add by wl 20110323加入区划、年份查询条件
    String rg_code = SessionUtil.getRgCode();
    //    String loginYear = SessionUtil.getLoginYear();
    String loginYear = year;
    String sql = "select * from SYS_USER_ROLE_RULE" + Tools.addDbLink()
      + " where user_id = ? and set_year=? and rg_code=?";
    List result = null;
    try {
      result = dao.findBySql(sql, new Object[] { userId, loginYear, rg_code });
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return result;
  }

  /**
   * 更新用户对角色数据
   * 
   * @param userId
   * @param newUserRoles
   */
  public void updateSysUserRole(String userId, List newUserRoles) throws Exception {
    Session session = dao.getSession();
    Connection conn = session.connection();
    // add by wl 20110323加入区划、年份条件
    String rg_code = (String) SessionUtil.getRgCode();
    String loginYear = SessionUtil.getLoginYear();
    try {
      // 删除相关联的 sys_user_role
      // 删除相关联的 sys_user_role
      String delSysUserRoleSql = "delete from sys_user_role_rule" + Tools.addDbLink() + " where user_id = '" + userId
        + "' and set_year=" + loginYear + " and rg_code='" + rg_code + "'";
      dao.executeBySql(delSysUserRoleSql);
      // 批量插入sys_user_role
      PreparedStatement psmt = conn.prepareStatement("insert into sys_user_role_rule" + Tools.addDbLink() + " ("
        + "USER_ID, ROLE_ID, SET_YEAR, LAST_VER, IS_DEFINED,rg_code) " + "values (?, ?, ?, ?, 1,?)");
      for (int i = 0; newUserRoles != null && i < newUserRoles.size(); i++) {
        XMLData userRole = (XMLData) newUserRoles.get(i);
        psmt.setString(1, userId);
        psmt.setString(2, (String) userRole.get("role_id"));
        // psmt.setString(3, (String) userRole.get("set_year"));
        psmt.setString(3, loginYear);
        psmt.setString(4, DateHandler.getLastVerTime());
        psmt.setString(5, rg_code);
        psmt.addBatch();
      }
      psmt.executeBatch();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      dao.closeSession(session);
    }
  }

  /**
   * 更新用户对角色数据
   * 
   * @param userId
   * @param newUserRoles
   */
  public void batchUpdateSysUserRole(String userId, List newUserRoles, XMLData right) throws Exception {
    Session session = dao.getSession();
    Connection conn = session.connection();
    // add by wl 20110323加入区划、年份条件
    String rg_code = (String) SessionUtil.getRgCode();
    String loginYear = SessionUtil.getLoginYear();
    try {
      String sql = "delete from SYS_USER_ROLE_RULE" + Tools.addDbLink() + " where user_id = '" + userId
        + "' and set_year=" + loginYear + " and rg_code='" + rg_code + "'";
      dao.executeBySql(sql);
      // 批量插入sys_user_role
      PreparedStatement psmt = conn.prepareStatement("insert into sys_user_role_rule" + Tools.addDbLink() + " ("
        + "USER_ID, ROLE_ID, RULE_ID, SET_YEAR, LAST_VER, IS_DEFINED,rg_code) " + "values (?, ?, ?, ?, ?, 1,?)");
      for (int i = 0; newUserRoles != null && i < newUserRoles.size(); i++) {
        XMLData userRole = (XMLData) newUserRoles.get(i);
        // 相关联的 sys_user_role
        // String findSysUserRoleSql =
        // "select * from sys_user_role_rule"
        // + Tools.addDbLink() + " where user_id = '" + userId +
        // "' and role_id='"+ userRole.get("role_id").toString() +
        // "' and set_year='"+userRole.get("set_year").toString()+"'";
        String findSysUserRoleSql = "select * from sys_user_role_rule" + Tools.addDbLink() + " where user_id = '"
          + userId + "' and role_id='" + userRole.get("role_id").toString() + "' and set_year='" + loginYear + "'"
          + " AND RG_CODE='" + rg_code + "'";// add by wl 2012
        // userRole.get("set_year")为空
        // 改为登陆年份
        List result = dao.findBySql(findSysUserRoleSql);
        String rule_id = null;
        if (null == right) {
          String findRuleSql = "select * from sys_user_role_rule where user_id = '" + userId + "' and set_year="
            + loginYear + " and rg_code='" + rg_code + "'";
          List result1 = dao.findBySql(findRuleSql);
          if (null != result1)
            rule_id = ((XMLData) result1.get(0)).get("rule_id").toString();
        } else {
          rule_id = (String) right.get("rule_id");
        }
        if (result.size() <= 0) {
          psmt.setString(1, userId);
          psmt.setString(2, (String) userRole.get("role_id"));
          psmt.setString(3, rule_id);
          // psmt.setString(4, (String) userRole.get("set_year"));
          psmt.setString(4, (String) loginYear);// add by wl 20120328
          // userRole.get("set_year")为空
          // 改为登陆年份
          psmt.setString(5, DateHandler.getLastVerTime());
          psmt.setString(6, rg_code);
          psmt.addBatch();
        }
      }
      psmt.executeBatch();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      dao.closeSession(session);
    }
  }

  /**
   * 取得某一用户的数据授权信息
   * 
   * @param userId
   * @return
   * @throws Exception
   */
  public List getRightInfo(String userId) throws Exception {
    List userInfoArr = new ArrayList();
    // add by wl 20110323加入区划、年份条件
    String rg_code = SessionUtil.getRgCode();
    String loginYear = SessionUtil.getLoginYear();

    try {
      // 用户授权信息
      String sql = "select s.*,r.rule_name from sys_user_rule s,sys_rule r where r.rule_id=s.rule_id and user_id=?"
        + " and s.set_year=? and s.rg_code=?";
      List userRight = dao.findBySql(sql, new Object[] { userId, loginYear, rg_code });
      userInfoArr.add(userRight);
      // 用户对应角色授权信息
      sql = "select * from sys_user_role_rule where user_id=? and set_year=? and rg_code=?";
      List roleRight = dao.findBySql(sql, new Object[] { userId, loginYear, rg_code });
      userInfoArr.add(roleRight);
    } catch (Exception e) {
      throw e;
    }
    return userInfoArr;
  }

  /**
   * 根据用户编码获取用户安全级别
   * 
   * @param user_code
   *            String 用户编码
   * @return int 0-密码认证 1-CA认证
   * @throws AppException
   *             如果传入的用户编码找不到用户信息，抛出编码错误
   * @author 黄节 2007年7月16日添加
   */
  public int getSecurity_level(String user_code) throws Exception {
    int security_level = 0;
    // 根据用户code取安全级别信息
    String sql = "select sy.security_level from sys_usermanage sy where sy.user_code=?";
    List security_levelList = dao.findBySql(sql, new Object[] { user_code });
    if (security_levelList.size() <= 0) {
      throw new Exception("用户编码有误，请检查。");
    } else {
      XMLData security_levelData = (XMLData) security_levelList.get(0);
      security_level = Integer.parseInt(((String) security_levelData.get("security_level")));
    }
    return security_level;
  }

  /**
   * 得到所有的 sys_user_sys
   * 
   * @date 2008-01-25
   * @author lwq
   * @return
   */
  public List getAllSysUserSys() {
    List subSysUser = null;
    String sql = "select user_sys_id,user_sys_name from sys_user_sys";
    subSysUser = dao.findBySql(sql);
    return subSysUser;
  }

  /**
   * 在用户管理的修改或者新增用户时,将用户信息同步更新到配置的所有数据库
   * 
   * @param userData
   *            用户信息XMLData
   * @throws Exception
   * @author zhupeidong at 2008-11-10上午09:31:12
   */
  private void updateUserInformationToMultiDataSource(XMLData userData) throws Exception {

    String userId = "";
    if (null == userData.get("user_id")) { // 如果user_id为空,直接返回
      return;
    } else {
      userId = userData.get("user_id").toString();
    }
    String userCode = "";
    if (null == userData.get("user_code")) { // 如果user_Code为空,直接返回
      return;
    } else {
      userCode = userData.get("user_code").toString();
    }
    if (null == SessionUtil.getServerBean("multiDataSource")) {// 如果没有配置多数据源,则直接退出
      return;
    }
    MultiDataSource multiDataSource = (MultiDataSource) SessionUtil.getServerBean("multiDataSource");
    //Map insertUpdate = getUserSaveOrUpdateSql(userData);
    StringBuffer findSql = new StringBuffer("select user_id from sys_usermanage where user_id<>'");
    findSql.append(userId).append("' ");
    findSql.append(" and user_code='");
    findSql.append(userCode).append("' ");
    Map mulMap = multiDataSource.getDataSources();
    Object[] keys = mulMap.keySet().toArray();
    //DataSource ds = null;
    List existList = null;
    String sysSetYear = SessionUtil.getLoginYear();
    int intSetYear = Integer.parseInt(sysSetYear);
    for (int i = 0; i < keys.length; ++i) {// 循环所有数据库数据源
      if (null != mulMap.get(keys[i])) {
        //ds = (DataSource) mulMap.get(keys[i]);
        int year = -1;

        /**
         * add by fengdz 20130704
         * 从数据源中找出当前区划下所有年份。然后获取通过当前区划的年份获取数据库连接，进行用户数据的维护
         * ，之前的处理是找出所有区划的年份，导致新年度初始化后， 新启用的区划进入系统添加用户找不到数据源问题
         */
        if (SessionUtil.getRgCode().equals(String.valueOf(keys[i].toString().split("#")[0]))) {
          // add by wl 20120326 现在keys:000000#2011 截取年份
          String keyYear = keys[i].toString().split("#")[1];
          year = Integer.parseInt(keyYear);

          if (intSetYear != year) {// 如果该数据源不是当前连接使用的数据源,则要同步更新该数据源数据
            // 从查询要更新的数据源sys_usermanager表中是否已经存在该数据
            existList = dao.findBySqlWithYear(year, findSql.toString());
            if (null == existList || existList.size() == 0) {// 如果不存在该数据,则执行插入语句
              // dao.executeBySqlWithYear(year,
              // insertUpdate.get("insert").toString());
              // 修改此数据上面的节点信息，改为非叶节点 add by beaf
              // String isLeaf = (String) userData.get("is_leaf");
              // int is_leaf = Integer.parseInt(isLeaf);
              // String userCode = (String)
              // userData.get("user_code");
              // if (userCode.length() > 3 && is_leaf == 1) {
              // userCode = userCode.substring(0,
              // (userCode.length() - 3));
              // String updateIsLeafsql =
              // "update sys_usermanage su set su.is_leaf=0 where su.user_code='"
              // + userCode + "'";
              // dao.executeBySqlWithYear(year, updateIsLeafsql);
              // }
            } else {// 如果存在该数据,则执行更新语句
              StringBuffer updateSb = new StringBuffer();
              updateSb.append(" update sys_usermanage set password = '" + userData.get("password")
                + "' where user_id in (''");
              for (int index = 0; index < existList.size(); index++) {
                XMLData map = (XMLData) existList.get(index);
                updateSb.append(",'" + map.get("user_id") + "'");
              }
              updateSb.append(")");

              dao.executeBySqlWithYear(year, updateSb.toString());
              // 修改此数据上面的节点信息，改为非叶节点 add by beaf
              // String isLeaf = (String) userData.get("is_leaf");
              // int is_leaf = Integer.parseInt(isLeaf);
              // String userCode = (String)
              // userData.get("user_code");
              // if (userCode.length() > 3 && is_leaf == 1) {
              // userCode = userCode.substring(0,
              // (userCode.length() - 3));
              // String updateIsLeafsql =
              // "update sys_usermanage su set su.is_leaf=0 where su.user_code='"
              // + userCode + "'";
              // dao.executeBySqlWithYear(year, updateIsLeafsql);
              // }
            }
          }
        }
      }
    }

  }

  /**
   * 查询当前连接的数据库的sys_usermanager表结构,拼凑insert和update该表记录的sql
   * 
   * @param userData
   *            用户信息XMLData
   * @return
   * @author zhupeidong at 2008-11-10上午09:33:11
   */
  /*private Map getUserSaveOrUpdateSql(XMLData userData) {
    // sys_usermanager表字段及类型信息
    String fieldsSql = "select lower(column_name) column_name,lower(data_type) data_type,lower(nullable) nullable from user_tab_cols where table_name='SYS_USERMANAGE'";
    List fieldsList = dao.findBySql(fieldsSql);
    if (null == fieldsList)
      return null;
    int fieldLength = fieldsList.size();
    String name = "";
    String type = "";
    String nullable = "";
    // sys_usermanager更新语句
    StringBuffer updateSb = new StringBuffer(" update sys_usermanage set ");
    // sys_usermanager插入语句
    StringBuffer insertFiled = new StringBuffer("insert into sys_usermanage(");
    StringBuffer insertValue = new StringBuffer(" values (");
    Object value = null;
    Map map = new HashMap();
    for (int i = 0; i < fieldLength; i++) {// 循环遍历所有字段,拼凑插入和更新sql语句
      Map fieldMap = (Map) fieldsList.get(i);
      name = fieldMap.get("column_name").toString();
      type = fieldMap.get("data_type").toString();
      nullable = fieldMap.get("nullable").toString();
      value = userData.get(name);
      if (null != value && !("").equals(value) && !("null").equals(value)) {// 如果该字段值不为空,拼凑插入和更新sql语句
        insertFiled.append(name);
        if (type.startsWith("number")) {// 如果类型是number,则不要加''
          insertValue.append(value);
          updateSb.append(name).append("=").append(value).append("");
        } else { // 如果类型是其它类型,则加''
          insertValue.append("'").append(value).append("'");
          updateSb.append(name).append("='").append(value).append("'");
        }
        // if (i != fieldLength - 1) {// 最后一个循环后不要加,
        insertFiled.append(",");
        insertValue.append(",");
        updateSb.append(",");

      } else {// 如果该字段值为空
        if (nullable.equalsIgnoreCase("n")) {// 但是该字段是不允许为空的,则加上默认值
          insertFiled.append(name);
          if (type.startsWith("number")) {// 如果该字段是number,则默认为0
            insertValue.append(0);
          } else { // 如果该字段是其它类型,则默认为''
            insertValue.append("''");
          }
          // if (i != fieldLength - 1) {// 最后一个循环后不要加,
          insertFiled.append(",");
          insertValue.append(",");

        }
      }

    }
    // 去掉最后一个逗号，add by beaf
    insertFiled.deleteCharAt(insertFiled.length() - 1);
    insertValue.deleteCharAt(insertValue.length() - 1);
    updateSb.deleteCharAt(updateSb.length() - 1);

    insertFiled.append(") ").append(insertValue).append(")");
    updateSb.append(" where user_id='").append(userData.get("user_id")).append("'");
    map.put("insert", insertFiled);
    map.put("update", updateSb);
    return map;
  }*/

  /**
   * 多数据源更新密码
   */
  public void updatePasswordToMultiDataSource(String password, String userid) throws Exception {

    // lindx edit 2008-12-17
    StringBuffer sql = new StringBuffer();
    sql.append("update sys_usermanage set password=?");
    sql.append(",init_password=1");

    /**
     * add by fengdz 2013.03.07 用户登录三级改造 Begin
     */
    sql.append(",is_edit_pwd=1");
    /**
     * add by fengdz 2013.03.07 用户登录三级改造 End
     */
    sql.append(" where user_id=?");

    try {
      MultiDataSource multiDataSource = (MultiDataSource) SessionUtil.getServerBean("multiDataSource");
      Map multiDS = multiDataSource.getDataSources();
      Iterator setYearIt = multiDS.keySet().iterator();
      while (setYearIt.hasNext()) {
        String strYear = (String) setYearIt.next();
        this.dao.executeBySqlWithYear(Integer.parseInt(strYear), sql.toString(), new Object[] { password, userid });

      }
    } catch (Exception e) {

      this.dao.executeBySql(sql.toString(), new Object[] { password, userid });
    }

  }

  /**
   * 通过条件找到用户
   * 
   * @param sql
   *            条件表达式
   * @return 用户
   * @throws Exception
   */
  public List findUserByCondition(String sSql) throws Exception {
    String rg_code = (String) SessionUtil.getUserInfoContext().getContext().get("rg_code");
    String sql = "select * from sys_user" + Tools.addDbLink() + " where " + sSql
      + " and exists (select 1 from sys_user_region sur where su.USER_ID=sur.user_id and sur.rg_code=?)";
    List list = null;
    try {
      // List userList = dao.findBySql(sql);
      List userList = dao.findBySql(sql, new Object[] { rg_code });
      // 并入sys_user_org数据
      if (userList != null && userList.size() > 0) {
        list = new ArrayList();
        for (int i = 0; i < userList.size(); i++) {
          XMLData userData = (XMLData) userList.get(i);
          String userId = (String) userData.get("user_id");
          userData.put("org_id", this.findSysUserOrgIdsByUserId(userId));
          list.add(userData);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 得到所有用户数据
   */
  public List getSysUsersByRoleId(String roleId) throws Exception {
    // add by wl 20110323 加入年度区划条件
    String loginYear = SessionUtil.getLoginYear();
    String rg_code = (String) SessionUtil.getUserInfoContext().getContext().get("rg_code");
    String sql = "select * from sys_user a,SYS_USER_ROLE_RULE b where a.user_id=b.user_id and b.role_id='" + roleId
      + "'" + " and b.set_year='" + loginYear + "' and b.rg_code='" + rg_code + "'";
    List list = null;
    try {
      List userList = dao.findBySql(sql);
      // 并入sys_user_org数据
      if (userList != null && userList.size() > 0) {
        list = new ArrayList();
        for (int i = 0; i < userList.size(); i++) {
          XMLData userData = (XMLData) userList.get(i);
          String userId = (String) userData.get("user_id");
          userData.put("org_id", this.findSysUserOrgIdsByUserId(userId));
          list.add(userData);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 保存用户的机构权限
   * 
   * @param userId
   *            用户ID
   * @param orgid
   *            机构ID数组
   * @throws Exception
   */
  public void saveOrgForUser(String userId, String[] orgid) throws Exception {
    String sql = "delete from sys_user_org where user_id='" + userId + "'";
    try {
      dao.executeBySql(sql);
      for (int i = 0; i < orgid.length; i++) {
        sql = "insert into sys_user_org (user_id,org_id,last_ver,set_year) values(?,?,?,?)";
        dao.executeBySql(sql,
          new Object[] { userId, orgid[i], DateHandler.getLastVerTime(), SessionUtil.getLoginYear() });
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * 获取构建用户树的数据
   * 
   * @return UserTreeList
   * @throws Exception
   */
  public List getUserTree() throws Exception {
    StringBuffer sql = new StringBuffer();
    sql
      .append("SELECT a.*,user_id, user_code, user_name, password, org_type, org_code, level_num, is_leaf, gender, telephone, mobile, enabled, headship_code, birthday, address, email, user_type, is_audit, audit_date, audit_user, nickname, last_ver, mb_id, belong_org, belong_type, security_level, init_password, ca_serial, identity_card, gp_org, title_tech, rtxuin, photo, photo_blobid, co_name, emp_index ");
    //add by fengdz 2013.03.01 用户登录三级改造 Begin
    sql.append(" ,is_ever_locked FROM sys_usertree a, sys_usermanage b ");
    //add by fengdz 2013.03.01 用户登录三级改造 End
    sql.append(" WHERE a.chr_id = b.user_id(+) ");
    sql.append(" and (a.set_year is null or a.set_year =? ) ");
    sql.append(" and (a.rg_code is null or a.rg_code =? ) ");
    sql.append(" order by CHR_CODE");
    String rg_code = SessionUtil.getRgCode();
    String setYear = SessionUtil.getLoginYear();
    List list = null;
    try {
      List userList = dao.findBySql(sql.toString(), new Object[] { setYear, rg_code });
      list = userList;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 根据条件找到userTree
   * 
   * @param sSql
   *            条件表达式
   * @return userTree构建List
   * @throws Exception
   */
  public List findUserTreeByCondition(String sSql) throws Exception {
    StringBuffer sql = new StringBuffer();
    sql
      .append("SELECT a.*,user_id, user_code, user_name, password, org_type, org_code, level_num, is_leaf, gender, telephone, mobile, enabled, headship_code, birthday, address, email, user_type, is_audit, audit_date, audit_user, nickname, last_ver, mb_id, belong_org, belong_type, security_level, init_password, ca_serial, identity_card, gp_org, title_tech, rtxuin, photo, photo_blobid, co_name, emp_index ");
    sql.append(" FROM sys_usertree a, sys_usermanage b ");
    sql.append(" WHERE a.chr_id = b.user_id(+) ");
    sql.append(sSql);
    sql.append(" and (a.set_year is null or a.set_year =? ) ");
    sql.append(" and (a.rg_code is null or a.rg_code =? ) ").append(" order by CHR_CODE");

    String rg_code = SessionUtil.getRgCode();
    String setYear = SessionUtil.getLoginYear();

    List list = null;
    try {
      List userList = dao.findBySql(sql.toString(), new Object[] { setYear, rg_code });
      list = userList;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 根据条件找到userTree
   * 
   * @param sSql
   *            条件表达式
   * @param enabled
   *            是否可用
   * @return userTree构建List
   * @throws Exception
   */
  public List findUserTreeByConditionAndEnabled(String sSql, boolean enabled) throws Exception {
    StringBuffer sql = new StringBuffer();
    // add by wl 20110323查询用户树的时候加入区划条件
    String rg_code = (String) SessionUtil.getUserInfoContext().getContext().get("rg_code");
    String setYear = SessionUtil.getLoginYear();
    if (enabled) {
      sql
        .append("SELECT a.*,user_id, user_code, user_name, password, org_type, org_code, level_num, is_leaf, gender, telephone, mobile, enabled, headship_code, birthday, address, email, user_type, is_audit, audit_date, audit_user, nickname, last_ver, mb_id, belong_org, belong_type, security_level, init_password, ca_serial, identity_card, gp_org, title_tech, rtxuin, photo, photo_blobid, co_name, emp_index ");
      sql.append(" FROM sys_usertree_enable a, sys_usermanage b ");
      sql.append(" WHERE a.chr_id = b.user_id(+) ");
      sql.append(sSql);
      sql.append(" and (a.set_year is null or a.set_year =? ) ");
      sql.append(" and (a.rg_code is null or a.rg_code =? ) ");
      sql.append(" order by CHR_CODE");
    } else {
      sql
        .append("SELECT a.*,user_id, user_code, user_name, password, org_type, org_code, level_num, is_leaf, gender, telephone, mobile, enabled, headship_code, birthday, address, email, user_type, is_audit, audit_date, audit_user, nickname, last_ver, mb_id, belong_org, belong_type, security_level, init_password, ca_serial, identity_card, gp_org, title_tech, rtxuin, photo, photo_blobid, co_name, emp_index ");
      sql.append(" FROM sys_usertree_disable a, sys_usermanage b ");
      sql.append(" WHERE a.chr_id = b.user_id(+) ");
      sql.append(sSql);
      sql.append(" and (a.set_year is null or a.set_year =? ) ");
      sql.append(" and (a.rg_code is null or a.rg_code =? ) ");
      sql.append(" order by CHR_CODE");
    }
    List list = null;
    try {
      List userList = dao.findBySql(sql.toString(), new Object[] { setYear, rg_code });
      list = userList;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 将选中的用户同步到财政信息系统中
   * 
   * @param userList
   * @throws Exception
   * @author zhupeidong at 2009-1-7上午09:41:19
   */
  public void syncWebServiceSysUser(List userList) throws Exception {
    if (null == userList || userList.size() < 1) {
      return;
    }
    int length = userList.size();
    List fieldsList = getUsermanageFieldsList();
    Map insertUpdate = null;
    XMLData xmlData = null;
    String userId = null;
    MultiDataSource multiDataSource = null;
    StringBuffer findSql = null;
    if (null != SessionUtil.getServerBean("multiDataSource")) {// 如果没有配置多数据源,则直接退出
      multiDataSource = (MultiDataSource) SessionUtil.getServerBean("multiDataSource");
    }
    for (int i = 0; i < length; i++) {
      findSql = new StringBuffer("select user_id from sys_usermanage where user_id='");
      xmlData = (XMLData) userList.get(i);
      if (null == xmlData.get("user_id")) { // 如果user_id为空,直接返回
        return;
      } else {
        userId = xmlData.get("user_id").toString();
      }
      findSql.append(userId).append("'");
      insertUpdate = getUserSaveOrUpdateSql(xmlData, fieldsList);
      syncSysUserToMultiDataSource(multiDataSource, findSql.toString(), insertUpdate, xmlData);
    }
  }

  /**
   * 获取sys_usermanage表的字段信息
   * 
   * @return
   * @author zhupeidong at 2009-1-7上午10:11:20
   */
  private List getUsermanageFieldsList() {
    String fieldsSql = "select lower(column_name) column_name,lower(data_type) data_type,lower(nullable) nullable from user_tab_cols where table_name='SYS_USERMANAGE'";
    List fieldsList = dao.findBySql(fieldsSql);
    return fieldsList;
  }

  /**
   * 同步IBM用户到财政系统数据库中
   * 
   * @param multiDataSource
   *            如果为空,表明是单数据源连接;如果不为空则配置了多数据源连接
   * @param findSql
   *            插入之前先判断,该user_id是否已经存在,然后再根据是否存在执行插入还是更新操作
   * @param insertUpdate
   *            insert和update语句的Map集合
   * @param userInfo
   *            一条IBM用户信息
   * @throws Exception
   * @author zhupeidong at 2009-1-7上午10:36:13
   */
  private void syncSysUserToMultiDataSource(MultiDataSource multiDataSource, String findSql, Map insertUpdate,
    XMLData userInfo) throws Exception {
    List existList = null;
    if (null == multiDataSource) {
      existList = dao.findBySql(findSql);
      if (null == existList || existList.size() == 0) {// 如果不存在该数据,则执行插入语句
        dao.executeBySql(insertUpdate.get("insert").toString());
      } else {// 如果存在该数据,则执行更新语句
        // dao.executeBySql(insertUpdate.get("update").toString());
      }
    } else {
      Map mulMap = multiDataSource.getDataSources();
      Object[] keys = mulMap.keySet().toArray();
      for (int i = 0; i < keys.length; ++i) {// 循环所有数据库数据源
        if (null != mulMap.get(keys[i])) {
          int year = Integer.parseInt(keys[i].toString());
          // 从查询要更新的数据源sys_usermanager表中是否已经存在该数据
          existList = dao.findBySqlWithYear(year, findSql.toString());
          if (null == existList || existList.size() == 0) {// 如果不存在该数据,则执行插入语句
            dao.executeBySqlWithYear(year, insertUpdate.get("insert").toString());
          } else {// 如果存在该数据,则执行更新语句
            // dao.executeBySqlWithYear(year, insertUpdate.get(
            // "update").toString());
          }

        }
      }
    }
  }

  /**
   * 查询当前连接的数据库的sys_usermanager表结构,拼凑insert和update该表记录的sql
   * 
   * @param userData
   *            用户信息XMLData
   * @return
   * @author zhupeidong at 2008-11-10上午09:33:11
   */
  private Map getUserSaveOrUpdateSql(XMLData userData, List fieldsList) {
    if (null == fieldsList)
      return null;
    int fieldLength = fieldsList.size();
    String name = "";
    String type = "";
    String nullable = "";
    // sys_usermanager更新语句
    StringBuffer updateSb = new StringBuffer(" update sys_usermanage set ");
    // sys_usermanager插入语句
    StringBuffer insertFiled = new StringBuffer("insert into sys_usermanage(");
    StringBuffer insertValue = new StringBuffer(" values (");
    Object value = null;
    Map map = new HashMap();
    for (int i = 0; i < fieldLength; i++) {// 循环遍历所有字段,拼凑插入和更新sql语句
      Map fieldMap = (Map) fieldsList.get(i);
      name = fieldMap.get("column_name").toString();
      type = fieldMap.get("data_type").toString();
      nullable = fieldMap.get("nullable").toString();
      value = userData.get(name);
      if (null != value && !("").equals(value) && !("null").equals(value)) {// 如果该字段值不为空,拼凑插入和更新sql语句
        insertFiled.append(name);
        if (type.startsWith("number")) {// 如果类型是number,则不要加''
          insertValue.append(value);
          updateSb.append(name).append("=").append(value).append("");
        } else { // 如果类型是其它类型,则加''
          insertValue.append("'").append(value).append("'");
          updateSb.append(name).append("='").append(value).append("'");
        }
        if (i != fieldLength - 1) {// 最后一个循环后不要加,
          insertFiled.append(",");
          insertValue.append(",");
          updateSb.append(",");
        }
      } else {// 如果该字段值为空
        if (nullable.equalsIgnoreCase("n")) {// 但是该字段是不允许为空的,则加上默认值
          insertFiled.append(name);
          if (type.startsWith("number")) {// 如果该字段是number,则默认为0
            insertValue.append(0);
          } else { // 如果该字段是其它类型,则默认为''
            insertValue.append("''");
          }
          if (i != fieldLength - 1) {// 最后一个循环后不要加,
            insertFiled.append(",");
            insertValue.append(",");
          }
        }
      }
    }
    if (insertFiled.toString().endsWith(",")) {
      insertFiled.delete(insertFiled.length() - 1, insertFiled.length());
    }
    if (insertValue.toString().endsWith(",")) {
      insertValue.delete(insertValue.length() - 1, insertValue.length());
    }
    if (updateSb.toString().endsWith(",")) {
      updateSb.delete(updateSb.length() - 1, updateSb.length());
    }

    insertFiled.append(") ").append(insertValue).append(")");
    updateSb.append(" where user_id='").append(userData.get("user_id")).append("'");
    map.put("insert", insertFiled);
    map.put("update", updateSb);
    return map;
  }

  /**
   * 按条件获取用户，并根据用户所属类型分类，主要用于用户辅助录入树中树形结构的显示
   * 
   * @return 用户列表
   * @throws Exception
   */
  public List findAllSysUserGroupWithOrgType(String searchCondition) throws Exception {
    StringBuffer sqlBuffer = new StringBuffer();
    sqlBuffer
      .append("select sut.*,")
      .append(
        " (case when exists(select 1 from sys_usermanage su where su.user_id = sut.chr_id) then 1 else 0 end) is_leaf")
      .append(
        " from sys_usertree sut where 1 = 1 and (sut.rg_code is null or sut.rg_code=? ) and (sut.set_year is null or sut.set_year=? )");
    if (searchCondition != null && !"".equals(searchCondition)) {
      sqlBuffer.append(searchCondition);
    }
    String rg_code = SessionUtil.getRgCode();
    String set_year = SessionUtil.getLoginYear();//add by lirf 增加年度过滤  20131028
    List list = dao.findBySql(sqlBuffer.toString(), new Object[] { rg_code, set_year });
    return list;
  }

  /**
   * <strong>Description:</strong><br>
   * 永久锁定<br>
   * 
   * @author fengdz
   * @date Mar 8, 2013 2:25:06 PM
   * 
   */
  public void lockedUser(String userId, String user_name) throws Exception {
    String sql = "update sys_usermanage set is_ever_locked=1 ,locked_date='' where user_id='" + userId + "'";
    dao.executeBySql(sql);
  }

  /**
   * <strong>Description:</strong><br>
   * 解锁包含接触永久锁定和用户密码输入三次的锁定<br>
   * 
   * @author fengdz
   * @date Mar 8, 2013 2:25:06 PM
   * 
   */
  public void noLockedUser(String userId, String user_name) throws Exception {
    String sql = "update sys_usermanage set is_ever_locked=0 ,locked_date='' where user_id='" + userId + "'";
    dao.executeBySql(sql);
  }

  @Override
  public void deleteSysUserOrgTypeByUserId(String userId) throws Exception {
    String delSysUserOrgtypeSql = "delete from sys_user_orgtype" + Tools.addDbLink() + " where user_id = ?  ";
    // 删除sys_user_org
    dao.executeBySql(delSysUserOrgtypeSql, new Object[] { userId });

  }

}
