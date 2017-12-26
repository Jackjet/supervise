package gov.df.fap.api.messageplatform;


import gov.df.fap.bean.messageplatform.MsgContentFieldsDTO;
import gov.df.fap.bean.messageplatform.MsgRuleDTO;

import java.util.List;

/**
 * <p>
 * 
 * Title:消息平台设置接口
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: 北京用友政务软件有限公司
 * </p>
 * 
 * @author yanyga
 * @see 
 * @CreateDate 2017-8-2
 * @version 1.0
 */
public interface IMsgSetting {

  /**
   * 保存内容模板新添加的按钮
   * @param rule_code
   * @param showName
   * @param paramName
   * @return
   */
  public void saveMsgContentFields(MsgContentFieldsDTO dto);

  /**
   * 删除内容模板单个按钮
   * @param key_name 按钮名称
   */
  public void deleteMsgContentField(String msgRuleId, String key_name);

  /**
   * 删除内容模板所有按钮
   * @param msgRuleId 消息规则Id
   */
  public void deleteAllMsgContentFields(String msgRuleId);
  
  
  /**
   * (通过ID获取)获取消息规则
   * @param msgRuleCode
   */
  public List getMsgRuleDataById(String msgRuleId);

  /**
   * 获取消息规则
   * @param msgRuleCode
   */
  public List getMsgRuleData(String msgRuleCode);

  /**
   * 获取消息规则(启用状态的规则)
   * @param msgRuleCode
   */
  public List getMsgRuleDataEnabled(String msgRuleCode);

  /**
   * 获取内容模板按钮字段
   * @param msgRuleId
   * @return
   */
  public List getMsgContentFields(String msgRuleId);

  /**
   * 查询所有工作流
   * @return ID,CODE,NAME
   */
  public List queryFlowName();

  /**
   * 查询所有工作流节点
   * @param flowId
   * @return ID,CODE,NAME
   */
  public List queryNodeName(String flowId);

  /**
   * 获取用户Tree(可选接收人)
   * @param msgRuleId
   */
  public List getChoosingUserTreeByRuleId(String msgRuleId);

  /**
   * 获取用户Tree(可选接收人)
   * @param choosedUserId
   */
  public List getChoosingUserTreeByChoosedId(String choosedUserId);

  /**
   * 获取用户群Tree(可选接收人)
   * @param msgRuleId
   */
  public List getChoosingUserGroupTreeByRuleId(String msgRuleId);

  /**
   * 获取用户群Tree(可选接收人)
   * @param choosedUserGroupId
   */
  public List getChoosingUserGroupTreeByChoosedId(String choosedUserGroupId);

  /**
   * 获取用户和用户群Tree(已选接收人)
   * @param msgRuleId
   */
  public List getChoosedUserAndGroupTreeByRuleId(String msgRuleId);

  /**
   * 获取用户群Tree(可选接收人)
   * @param choosedUserGroupId
   */
  public List getChoosedUserAndGroupTreeByChoosedId(String choosedUserId, String choosedUserGroupId);

  /**
   * 获取用户群Tree(所有)
   */
  public List getUserGroupTree();

  /**
   * 获取某个用户群下的用户Tree(可选用户)
   * @param userGroupId： 可以为 NULL
   */
  public List getChoosingUserTreeByGroupId(String userGroupId);

  /**
   * 获取某个用户群下的用户Tree(已选用户)
   * @param userGroupId
   */
  public List getChoosedUserTreeByGroupId(String userGroupId);

  /**
   * 保存或更新用户群
   * @param name
   * @param userGroupId： 可以为 NULL
   */
  public String saveOrUpdateUserGroup(String name, String userGroupId);

  /**
   * 保存或更新用户群所选用户
   * @param userIdList
   * @param userGroupId： 可以为 NULL
   */
  public void saveOrUpdateUserGroupRelation(List userIdList, String userGroupId);

  /**
   * 删除用户群
   * @param userGroupId
   */
  public void deleteUserGroup(String userGroupId);

  /**
   * 删除用户群表
   * @param userGroupId
   */
  public void deleteUserGroupRelation(String userGroupId);

  /**
   * 保存消息规则
   * @param dto
   */
  public void saveMsgRule(MsgRuleDTO dto);

  /**
   * 删除消息规则
   * @param msgRuleId
   */
  public void deleteMsgRule(String msgRuleId);

  /**
   * 删除消息规则接收人
   * @param msgRuleId
   */
  public void deleteMsgRuleReceiver(String msgRuleId);

  /**
   * 更新消息规则
   * @param dto
   */
  public void updateMsgRule(MsgRuleDTO dto);

  /**
   * 保存或更新消息接收人
   * @param msgRuleId
   * @param isUser
   * @param id
   */
  public void saveOrUpdateMsgRuleReceiver(String msgRuleId, List dtoOrMapList);

  /**
   * 获取所有规则
   */
  public List getAllMsgRules();

  /**
   * 生成规则Id
   */
  public String generateMsgRuleIdBySeq();

  /**
   * 根据工作流节点和操作类型查找规则
   * @param currentNodeId
   * @param actionType
   * @return
   */
  public List getMsgRulesByWorkFlowInfo(Long currentNodeId, String actionType);

  /**
   * 根据单据类型编码获取单据类型名称
   * @param billtypeCode
   * @return
   */
  public String getBilltypeNameByCode(String billtypeCode);

  /**
   * 得到规则和机构映射关系
   * @return
   */
  public List getRuleToOrg();
}
