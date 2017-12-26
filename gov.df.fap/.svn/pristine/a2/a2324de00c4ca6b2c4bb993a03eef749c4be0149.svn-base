/*
 * $Id: SysOrgtypeBO.java 519759 2014-08-14 06:30:32Z guoyc $
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.service.orgtype;

import gov.df.fap.api.orgtype.ISysOrgtype;
import gov.df.fap.bean.log.LogDTO;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.log.Log;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.xml.XMLData;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 机构类型管理服务端接口实现类
 * @author a
 *
 */
@Service
public class SysOrgtypeBO implements ISysOrgtype {
  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao;

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  //private final String SYS_ORGTYPE_TABLE = "mappingfiles.sysdb.SysOrgtype";

  /**
   * 得到所有机构类型数据
   */
  public List findAllSysOrgtypes() throws Exception {
    String sql = "select * from sys_orgtype" + Tools.addDbLink() + " order by orgtype_code ";
    List list = null;
    try {
      list = dao.findBySql(sql);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 保存机构类型数据
   */
  public void saveorUpdateSysOrgtype(XMLData xmlData) throws Exception {

    try {

      dao.deleteDataBySql("sys_orgtype", xmlData, new String[] { "orgtype_code" });
      dao.saveDataBySql("sys_orgtype", xmlData);
      //			 保存操作日志
      LogDTO logDto = new LogDTO();
      logDto.setLog_type("1");
      logDto.setLog_level(new Integer(0));
      logDto.setRemark(SessionUtil.getOnlineUser().getUser_name() + "新增或修改了orgtype_code=" + xmlData.get("orgtype_code")
        + "的机构类型数据信息");
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  public boolean checkEleIsExist(String ele) throws Exception {
    try {
      List orgs = dao.findBySql("select 1 from SYS_ORGTYPE" + Tools.addDbLink() + " where ele_code=?",
        new Object[] { ele });

      if (orgs.size() > 0) {
        return false;
      } else {
        return true;
      }

    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * 删除机构类型数据
   */
  public void deleteSysOrgtype(XMLData xmlData) throws Exception {

    try {
      dao.deleteDataBySql("sys_orgtype", xmlData, new String[] { "orgtype_code" });
      //			 保存操作日志
      LogDTO logDto = new LogDTO();
      logDto.setLog_type("1");
      logDto.setLog_level(new Integer(0));
      logDto.setRemark(SessionUtil.getOnlineUser().getUser_name() + "删除了orgtype_code=" + xmlData.get("orgtype_code")
        + "的机构类型数据信息");
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * 级联删除相关联的机构类型数据
   * @param orgtypeCode
   */
  public void deleteSysOrgtypeByOrgtypeCode(String orgtypeCode) throws Exception {
    //ymj 判断情况分开处理
    String switch01 = (String) SessionUtil.getParaMap().get("switch01");
    switch01 = switch01 == null ? "0" : switch01;
    String delSysUserManageSql = "";
    if (switch01.equals("1")) {
      //			delSysUserManageSql = "delete from t_causer"+Tools.addDbLink()+" where orgtype_code = '" + orgtypeCode +"'";
    }
    delSysUserManageSql = "delete from sys_usermanage" + Tools.addDbLink() + " where org_type = '" + orgtypeCode + "'";

    //		String delSysUserManageSql = "delete from sys_usermanage"+Tools.addDbLink()+" where orgtype_code = '" + orgtypeCode +"'";
    String delSysOrgtypeSql = "delete from sys_orgtype" + Tools.addDbLink() + " where orgtype_code = '" + orgtypeCode
      + "'";
    try {
      int count = 0;

      //删除相关联的	sys_usermanage
      count = dao.executeBySql(delSysUserManageSql);
      Log.debug("删除 orgtype_code = " + orgtypeCode + " 相关联 sys_usermanage 记录: " + count + " 条.");

      //删除sys_orgtype
      count = dao.executeBySql(delSysOrgtypeSql);
      Log.debug("删除 orgtype_code = " + orgtypeCode + " sys_orgtype: " + count + " 条.");

      //			 保存操作日志
      LogDTO logDto = new LogDTO();
      logDto.setLog_type("1");
      logDto.setLog_level(new Integer(0));
      logDto.setRemark(SessionUtil.getOnlineUser().getUser_name() + "删除了 orgtype_code = " + orgtypeCode + "的机构类型数据信息");

    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    Log.debug("成功删除 orgtype_code = " + orgtypeCode + "  sys_orgtype !");

  }

  /**
   * 通过ORGTYPE_CODE查找SYS_USERMANAGE
   * @param orgtypeCode
   * @return
   */
  public List findSysUserManageByOrgtypeCode(String orgtypeCode) throws Exception {
    //ymj 查询改为试图
    String sql = "select * from sys_user" + Tools.addDbLink() + " where BELONG_TYPE = ? ";
    //		String sql = "select * from sys_usermanage"+Tools.addDbLink()+" where orgtype_code = ? ";
    List result = null;
    try {
      result = dao.findBySql(sql, new Object[] { orgtypeCode });
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return result;
  }

  /**
   * 通过机构类型码查找对应的要素简称
   * @param orgtypeCode
   * @return
   * @throws Exception
   */
  public String findEleCodeByOrgtypeCode(String orgtypeCode) throws Exception {
    String sql = "select ele_code from sys_orgtype" + Tools.addDbLink() + " where orgtype_code = ? ";
    try {
      List list = dao.findBySql(sql, new Object[] { orgtypeCode });
      if (list != null && list.size() > 0) {
        return (String) ((XMLData) list.get(0)).get("ele_code");
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return null;
  }

  public void updateUserView() throws Exception {
    try {
      dao.executeBySql("{call pf_build_vw_usertree}");
      //			 保存操作日志
      LogDTO logDto = new LogDTO();
      logDto.setLog_type("1");
      logDto.setLog_level(new Integer(0));
      logDto.setRemark(SessionUtil.getOnlineUser().getUser_name() + "更新了用户视图");

    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public List findSelectedOrgtypesByUserId(String userId) throws Exception {
    String sql = "select * from sys_user_org where user_id=?";
    List list = null;
    try {
      list = dao.findBySql(sql, new Object[] { userId });
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  @Override
  public String getOrgTypeByUserId(String userId) throws Exception {
    String sql = "select * from sys_user_orgtype where user_id=?";
    List list = null;
    String orgType = null;
    try {
      list = dao.findBySql(sql, new Object[] { userId });
      if (list != null && list.size() > 0) {
        orgType = (String) ((XMLData) list.get(0)).get("org_type");
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return orgType;
  }

}
