package gov.df.fap.api.rule;

import gov.df.fap.bean.rule.dto.FRightSqlDTO;
import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Title: 数据权限接口
 * </p>
 * <p>
 * Description:数据权限接口
 * 
 * @version 1.0
 * @author justin
 * @author  2017年03月06日整理
 * @since java 1.6
 */
public interface IDataRight {

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
  public String getSqlBusiRightForOld(String userid, String roleid, String tablealias) throws Exception;

  /**
   * 实现返回业务表的数据权限sql语句（通过用户）
   * 
   * @param userid-------------用户id
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */
  public String getSqlBusiRightByUserForOld(String userid, String tablealias) throws Exception;

  /**
  * 得到所有启用权限的要素
  * yanyga 20170324
  */
  public Map<String, Object> initEnabledElement(HttpServletRequest request, HttpServletResponse response)
    throws Exception;

  /**
  * 保存或者新增权限组
  */
  public Map<String, Object> saveOrUpdate(HttpServletRequest request, HttpServletResponse response);

  /**
   * 获取所有权限组集合
   * @return
   */
  public Map<String, Object> showAllGROUPlist();

  /**
   * 实现返回业务表的RCID
   * 
   * @param inObjXml
   *            各要素值
   * @return RCID
   * @throws Exception
   *             错误信息
   */
  public String getRcid(XMLData inObjXml) throws Exception;

  /**
   * 实现返回业务表的数据权限sql语句
   * 
   * @param userid
   *            用户id
   * @param roleid
   *            角色id
   * @param tablealias
   *            基础表别名
   * @return sql语句
   * @throws Exception
   *             错误信息
   */
  public FRightSqlDTO getSqlBusiRightByJoin(String userid, String roleid, String tablealias) throws Exception;

  /**
   * 实现返回业务表的数据权限sql语句
   * 
   * @param userid
   *            用户id
   * @param roleid
   *            角色id
   * @param tablealias
   *            基础表别名
   * @return sql语句
   * @throws Exception
   *             错误信息
   */
  public String getSqlBusiRight(String userid, String roleid, String tablealias) throws Exception;

  /**
   * 实现返回业务表的数据权限sql语句（没有CCID）
   * 
   * @param userid
   *            用户id
   * @param roleid
   *            角色id
   * @param tablealias
   *            基础表别名
   * @return sql语句
   * @throws Exception
   *             错误信息
   */
  public String getSqlBusiRightNoCCID(String userid, String roleid, String tablealias) throws Exception;

  /**
   * 实现返回业务表的数据权限sql语句
   * 
   * @param userid
   *            用户id
   * @param roleid
   *            角色id
   * @param tablealias
   *            基础表别名
   * @return sql语句
   * @throws Exception
   *             错误信息
   */
  public FRightSqlDTO getSqlBusiRightByJoinWithQry(String userid, String roleid, String tablealias) throws Exception;

  /**
   * 实现返回业务表的数据权限sql语句
   * 
   * @param userid
   *            用户id
   * @param roleid
   *            角色id
   * @param tablealias
   *            基础表别名
   * @return sql语句
   * @throws Exception
   *             错误信息
   */
  public FRightSqlDTO getSqlBusiRightNoRcid(String userid, String roleid, String tablealias) throws Exception;
 

  /**
   * 实现返回业务表的数据权限sql语句（通过用户）
   * 
   * @param userid
   *            用户id
   * @param tablealias
   *            基础表别名
   * @return sql语句
   * @throws Exception
   *             错误信息
   */
  public String getSqlBusiRightByUser(String userid, String tablealias) throws Exception;

  /**
   * 实现返回业务表的数据权限sql语句（通过用户,没有CCID）
   * 
   * @param userid
   *            用户id
   * @param tablealias
   *            基础表别名
   * @return sql语句
   * @throws Exception
   *             错误信息
   */
  public String getSqlBusiRightByUserNoCCID(String userid, String tablealias) throws Exception;

  /**
   * 提取当前系统所有的用户列表
   * 
   * @return List对象（内部含XMLDATA)
   * @throws Exception
   *             错误信息
   */
  public List getAllUser() throws Exception;

  /**
   * 根据预算单位和所选的用户，保存到对应的权限设置中
   * 
   * @param userRightList
   *            List对象（内部含XMLDATA,user_id,en_id)
   * @throws Exception
   *             错误信息
   */
  public void saveUserRight(List userRightList) throws Exception;

  /**
   * 大批量的rcid生成
   * 
   * @param tableName
   *            调用该接口的传入的表名
   * @param whereSql
   *            在实现类中的拼sql语句中的附加的过滤条件
   * @throws Exception
   * @deprecated 使用createBatchRCID 代替
   */

  public void getBatchRCID(String tableName, String whereSql) throws Exception;

  /**
   * 大批量的rcid生成,调用存储过程
   * 
   * @param tableName
   *            调用该接口的传入的表名
   * @param whereSql
   *            在实现类中的拼sql语句中的附加的过滤条件
   * @throws Exception
   */
  public void createBatchRCID(String tableName, String whereSql) throws Exception;

  /**
   * 实现返回业务表的数据权限sql语句（通过用户、角色s）
   * 
   * @param roleid-------------角色id
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */

  public String getSqlBusiRightByUserAndRole(String user_id, String role_id, String tablealias) throws Exception;
  /**
   * 实现返回业务表的数据权限sql语句（通过用户、角色s）
   * 
   * @param roleid-------------角色id
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */

  public String getSqlBusiRightByUserAndRoleNoRcid(String user_id, String role_id, String tablealias) throws Exception;

}
