package gov.df.fap.api.messageplatform;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 消息发送接口
 * @author yanyga
 * 2017-07-27
 */
public interface IMessageSend {
  
  /**
   * 根据用户id，获取系统消息
   * @param userId 用户id
   * @return json消息
   */
  public JSONObject getSystemMessageByUserId(String userId);
  
  /**
   * 发送消息接口
   * @param messageTempleteId 消息模板ID
   * @param dataList 业务数据List
   * @param receiverList 接收人List
   * @return 发送成功(true)/发送失败(false)
   */
  public boolean sendMessage(String messageTempleteId,List dataList,List receiverList);
  
  /**
   * 获取消息的配置内容
   * @param msgRuleCode 消息模板编码，
   * @return 配置信息，包括配置的角色信息，url
   */
  public String getContentByMessageTempleteCode(String msgRuleCode);
  
  /**
   * 获取消息的配置内容
   * @param msgRuleId 消息模板编码，
   * @return 配置信息，包括配置的角色信息，url
   */
  public String getContentByMessageTempleteId(String msgRuleId);
  
  /**
   * 查询接收人信息
   * @param belong_org 单位ID/处室ID
   * @param roleId 角色ID
   * @return 接收人ID
   */
  public List getReveiverList(String belong_org,String roleId);
  
  /**
   * 获取消息配置
   */
  public Map<String,Object> getImParam();
  
  /**
   * 获取用户的Token
   * @param username 用户名
   * @return 
   */
  public Map<String,Object> getUserToken(String username);
  
}
