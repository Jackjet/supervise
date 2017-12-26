/*
 * $Id: ISysUser.java 505558 2014-06-23 07:48:35Z zhaoqiang1 $
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.api.user;

import gov.df.fap.util.xml.XMLData;

import java.util.List;

/**
 * 功能管理服务端接口
 * 
 * @author a
 * 
 */
public interface ISysUser {
  public List getAllSysUsers() throws Exception;

  public XMLData findSysUserByUserId(String userId) throws Exception;

  public Object[] findSysUserOrgIdsByUserId(String userId) throws Exception;

  public List findSysUserByIsAudit(int isAudit) throws Exception;

  public List findSysUserRoleByUserId(String userId) throws Exception;

  public List findSysUserRoleByUserIdAndYear(String userId, String year) throws Exception;

  public void saveorUpdateSysUser(List userInfo) throws Exception;

  public void deleteSysUser(XMLData xmlData) throws Exception;

  public void deleteSysUserByUserId(String userId) throws Exception;

  public void deleteSysUserOrgTypeByUserId(String userId) throws Exception;

  public void lockedUser(String userId, String user_name) throws Exception;

  public void noLockedUser(String userId, String user_name) throws Exception;

  public void updateSysUserRole(String userId, List newUserRoles) throws Exception;

  /**
   * 更新用户对角色数据
   * 
   * @param userId
   * @param newUserRoles
   * @param right
   */
  public void batchUpdateSysUserRole(String userId, List newUserRoles, XMLData right) throws Exception;

  public void updateSysUserByIsAudit(String[] toBeAuditedUserIds, int isAudit) throws Exception;

  /**
   * 取得某一用户的数据授权信息
   * 
   * @param userId
   * @return
   * @throws Exception
   */
  public List getRightInfo(String userId) throws Exception;

  /**
   * 根据用户编码获取用户安全级别
   * 
   * @param user_code
   *            String 用户编码
   * @return int 0-密码认证 1-CA认证
   * @throws AppException
   *             如果传入的用户编码找不到用户信息，抛出编码错误
   * @author 2007年7月16日添加
   */
  public int getSecurity_level(String user_code) throws Exception;

  /**
   * 得到所有的 sys_user_sys
   * 
   * @date 2008-01-25
   * @author lwq
   * @return
   */
  public List getAllSysUserSys();

  /**
   * 更新系统中配置的所有数据源的该用户的密码
   * 
   * @param password
   * @param userid
   * @throws Exception
   */
  public void updatePasswordToMultiDataSource(String password, String userid) throws Exception;

  /**
   * 通过条件找到用户
   * 
   * @param sql
   *            条件表达式
   * @return 用户
   * @throws Exception
   */
  public List findUserByCondition(String sql) throws Exception;

  /**
   * 根据角色获取用户
   * 
   * @param roleId
   *            角色ID
   * @return 用户
   * @throws Exception
   */
  public List getSysUsersByRoleId(String roleId) throws Exception;

  /**
   * 保存用户的机构权限
   * 
   * @param userId
   *            用户ID
   * @param orgid
   *            机构ID数组
   * @throws Exception
   */
  public void saveOrgForUser(String userId, String[] orgid) throws Exception;

  /**
   * 获取构建用户树的数据
   * 
   * @return UserTreeList
   * @throws Exception
   */
  public List getUserTree() throws Exception;

  /**
   * 根据条件找到userTree
   * 
   * @param sSql
   *            条件表达式
   * @return userTree构建List
   * @throws Exception
   */
  public List findUserTreeByCondition(String sSql) throws Exception;

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
  public List findUserTreeByConditionAndEnabled(String sSql, boolean enabled) throws Exception;

  /**
   * 通过ID找单位
   * 
   * @param id
   *            单位ID
   * @param type
   *            单位类型
   * @return
   */
  public List getOrgById(String id, String type);

  /**
   * 将选中的用户同步到财政信息系统中
   * 
   * @param userList
   * @throws Exception
   * @author zhupeidong at 2009-1-7上午09:41:19
   */
  public void syncWebServiceSysUser(List userList) throws Exception;

  /**
   * 获取所有系统用户，并根据用户所属类型分类，主要用于用户辅助录入树中树形结构的显示
   * @return 用户列表
   * @throws Exception
   */
  public List findAllSysUserGroupWithOrgType(String searchCondition) throws Exception;
}
