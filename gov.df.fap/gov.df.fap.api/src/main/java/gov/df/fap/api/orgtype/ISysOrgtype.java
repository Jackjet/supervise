/*
 * $Id: ISysOrgtype.java 198545 2012-01-12 03:33:57Z zhangwh1 $
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.api.orgtype;

import gov.df.fap.util.xml.XMLData;

import java.util.List;

/**
 * 机构类型管理服务端接口
 * @author a
 *
 */
public interface ISysOrgtype {
  public List findAllSysOrgtypes() throws Exception;

  public void saveorUpdateSysOrgtype(XMLData xmlData) throws Exception;

  public void deleteSysOrgtype(XMLData xmlData) throws Exception;

  public void deleteSysOrgtypeByOrgtypeCode(String orgtypeCode) throws Exception;

  public List findSysUserManageByOrgtypeCode(String orgtypeCode) throws Exception;

  public String findEleCodeByOrgtypeCode(String orgtypeCode) throws Exception;

  public void updateUserView() throws Exception;

  public boolean checkEleIsExist(String ele) throws Exception;

  //add by gaoqb
  public List findSelectedOrgtypesByUserId(String userId) throws Exception;

  public String getOrgTypeByUserId(String userId) throws Exception;

}
