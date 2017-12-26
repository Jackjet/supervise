package gov.df.fap.api.messageplatform;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 消息平台配置
 * @author Administrator
 *
 */
public interface IMessageConfigOperation {

  /**
   * 获取所有工作流名称
   * @param request
   * @param response
   * @return
   */
  Map<String,Object> getWFList(HttpServletRequest request,HttpServletResponse response);
  
  
  /**
   * 获取所有规则
   * @param request
   * @param response
   * @return
   */
  Map<String,Object> getAllMsgRules(HttpServletRequest request,HttpServletResponse response);
  
  
  /**
   * 获取某个工作流下的节点
   * @param request
   * @param response
   * @return
   */
  Map<String,Object> getWFNodesById(HttpServletRequest request,HttpServletResponse response);
  
  
  /**
   * 保存消息规则
   * @param request
   * @param response
   * @return
   */
  Map<String,Object> saveMsgRule(HttpServletRequest request,HttpServletResponse response);
  
  /**
   * 删除消息规则
   * @param request
   * @param response
   * @return
   */
  Map<String,Object> deleteMsgRuleById(HttpServletRequest request,HttpServletResponse response);
  
  /**
   * 删除用户群
   * @param request
   * @param response
   * @return
   */
  Map<String,Object> deleteUserGroup(HttpServletRequest request,HttpServletResponse response);
  
  
  /**
   * 根据ID查询消息规则
   * @param request
   * @param response
   * @return
   */
  Map<String,Object> getMsgRuleById(HttpServletRequest request,HttpServletResponse response);
  
  
  /**
   * 保存或者更新用户信息
   * @param request
   * @param response
   * @return
   */
  Map<String,Object> saveOrUpdateGroup(HttpServletRequest request,HttpServletResponse response);
  
  /**
   * 获取用户树
   * @param request
   * @param response
   * @return
   */
  Map<String,Object> getUserTree(HttpServletRequest request,HttpServletResponse response);
  
  
  /**
   * 获取用户群里的用户
   * @param request
   * @param response
   * @return
   */
  Map<String,Object> getUserByGroupId(HttpServletRequest request,HttpServletResponse response);
  
  /**
   * 消息面板---查找用户
   * @param request
   * @param response
   * @return
   */
  Map<String,Object> findSysUser(HttpServletRequest request,HttpServletResponse response);
  
  
}
