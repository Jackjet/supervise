package gov.df.fap.service.messageplatform;


import gov.df.fap.api.messageplatform.IMsgSetting;
import gov.df.fap.bean.messageplatform.MsgContentFieldsDTO;
import gov.df.fap.bean.messageplatform.MsgRuleDTO;
import gov.df.fap.service.messageplatform.dao.MsgSettingDao;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 
 * Title:消息平台设置实现
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
@Service
public class MsgSettingImpl implements IMsgSetting {

  @Autowired
  private MsgSettingDao msgSettingDao;

  /**
   * 保存内容模板新添加的按钮
   * @param rule_code
   * @param showName
   * @param paramName
   * @return
   */
  public void saveMsgContentFields(MsgContentFieldsDTO dto) {
    dto.setId(Long.valueOf(msgSettingDao.generateMsgRuleIdBySeq()));
    msgSettingDao.saveMsgContentFields(dto);
  }

  /**
   * 删除内容模板所有按钮
   * @param msgRuleId 消息规则Id
   */
  public void deleteAllMsgContentFields(String msgRuleId) {
    msgSettingDao.deleteAllMsgContentFields(msgRuleId);
  }

  /**
   * 删除内容模板按钮
   * @param key_name
   */
  public void deleteMsgContentField(String msgRuleId, String key_name) {
    msgSettingDao.deleteMsgContentField(msgRuleId, key_name);
  }

  /**
   * 获取消息规则
   * @param msgRuleCode
   */
  public List getMsgRuleData(String msgRuleCode) {
    return msgSettingDao.getMsgRuleData(msgRuleCode);
  }

  /**
   * 获取消息规则(启用状态的规则)
   * @param msgRuleCode
   */
  public List getMsgRuleDataEnabled(String msgRuleCode) {
    return msgSettingDao.getMsgRuleDataEnabled(msgRuleCode);
  }

  /**
   * 获取内容模板按钮字段
   * @param msgRuleId
   * @return
   */
  public List getMsgContentFields(String msgRuleId) {
    return msgSettingDao.getMsgContentFields(msgRuleId);
  }

  /**
   * 查询所有工作流
   * 
   * @return 
   */
  public List queryFlowName() {
    return msgSettingDao.queryFlowName();
  }

  /**
   * 查询工作流节点
   * @param flowId
   * @return ID,CODE,NAME
   */
  public List queryNodeName(String flowId) {
    return msgSettingDao.queryNodeName(flowId);
  }

  /**
   * 获取用户Tree(可选接收人)
   * @param msgRuleId
   */
  public List getChoosingUserTreeByRuleId(String msgRuleId) {
    return msgSettingDao.getChoosingUserTreeByRuleId(msgRuleId);
  }

  /**
   * 获取用户群Tree(可选接收人)
   * @param msgRuleId
   */
  public List getChoosingUserGroupTreeByRuleId(String msgRuleId) {
    return msgSettingDao.getChoosingUserGroupTreeByRuleId(msgRuleId);
  }

  /**
   * 获取用户和用户群Tree(已选接收人)
   * @param msgRuleId
   */
  public List getChoosedUserAndGroupTreeByRuleId(String msgRuleId) {
    return msgSettingDao.getChoosedUserAndGroupTreeByRuleId(msgRuleId);
  }

  /**
   * 获取用户Tree(可选接收人)
   * @param choosedUserId
   */
  public List getChoosingUserTreeByChoosedId(String choosedUserId) {
    return msgSettingDao.getChoosingUserTreeByChoosedId(choosedUserId);
  }

  /**
   * 获取用户群Tree(可选接收人)
   * @param choosedUserGroupId
   */
  public List getChoosingUserGroupTreeByChoosedId(String choosedUserGroupId) {
    return msgSettingDao.getChoosingUserGroupTreeByChoosedId(choosedUserGroupId);
  }

  /**
   * 获取用户群Tree(可选接收人)
   * @param choosedUserGroupId
   */
  public List getChoosedUserAndGroupTreeByChoosedId(String choosedUserId, String choosedUserGroupId) {
    return msgSettingDao.getChoosedUserAndGroupTreeByChoosedId(choosedUserId, choosedUserGroupId);
  }

  /**
   * 获取用户群Tree(所有)
   */
  public List getUserGroupTree() {
    return msgSettingDao.getUserGroupTree();
  }

  /**
   * 获取某个用户群下的用户Tree(可选用户)
   * @param userGroupId:  允许为null
   */
  public List getChoosingUserTreeByGroupId(String userGroupId) {
    if (userGroupId == null || "".equals(userGroupId.trim())) {
      return msgSettingDao.getUserTree();
    }
    return msgSettingDao.getChoosingUserTreeByGroupId(userGroupId);
  }

  /**
   * 获取某个用户群下的用户Tree(已选用户)
   * @param userGroupId
   */
  public List getChoosedUserTreeByGroupId(String userGroupId) {
    return msgSettingDao.getChoosedUserTreeByGroupId(userGroupId);
  }

  /**
   * 保存或更新用户群
   * @param name: 允许为null
   */
  public String saveOrUpdateUserGroup(String name, String userGroupId) {
    if (userGroupId == null || "".equals(userGroupId)) {//保存
      userGroupId = msgSettingDao.generateMsgUserGroupIdBySeq();
      msgSettingDao.saveUserGroup(name, userGroupId);
    } else {//更新
      msgSettingDao.updateUserGroup(name, userGroupId);
    }
    return userGroupId;
  }

  /**
   * 保存或更新用户群所选用户
   * @param userIdList
   * @param userGroupId: 允许为null
   */
  public void saveOrUpdateUserGroupRelation(List choosedUserList, String userGroupId) {
    if (userGroupId == null || "".equals(userGroupId)) {//保存
      userGroupId = msgSettingDao.generateMsgUserGroupIdBySeq();
    }
    List dtoOrMapList = new ArrayList();
    String[] fields = { "id", "rg_code", "set_year", "user_id", "user_group_id", "upid" };
    //查找是否在数据库中存在
    List oldUserIdList = msgSettingDao.getUserIdFromUserGroupRelatioin(userGroupId);
    int size = choosedUserList.size();
    int oldSize = oldUserIdList.size();
    for (int i = 0; i < size; i++) {
      String userId = (String) choosedUserList.get(i);
      boolean isExist = false;
      for (int j = 0; j < oldSize; j++) {
        if (userId.equals(((Map) oldUserIdList.get(j)).get("user_id"))) {
          isExist = true;
          break;
        }
      }
      if (!isExist) {
        Map map = new HashMap();
        map.put("id", msgSettingDao.generateMsgUserGroupIdBySeq());
        map.put("rg_code", SessionUtil.getRgCode());
        map.put("set_year", SessionUtil.getLoginYear());
        map.put("user_id", userId);
        map.put("user_group_id", userGroupId);
        map.put("upid", "0");
        dtoOrMapList.add(map);
      }
    }
    //批量保存
    msgSettingDao.batchSaveUserGroupRelation("sys_user_group_relation", fields, dtoOrMapList);
  }

  /**
   * 删除用户群
   * @param userGroupId
   */
  public void deleteUserGroup(String userGroupId) {
    msgSettingDao.deleteUserGroup(userGroupId);
  }

  /**
   * 删除用户群表
   * @param userGroupId
   */
  public void deleteUserGroupRelation(String userGroupId) {
    msgSettingDao.deleteUserGroupRelation(userGroupId);
  }

  /**
   * 保存消息规则
   * @param dto
   */
  public void saveMsgRule(MsgRuleDTO dto) {
    msgSettingDao.saveMsgRule(dto);
  }

  /**
   * 更新消息规则
   * @param dto
   */
  public void updateMsgRule(MsgRuleDTO dto) {
    msgSettingDao.updateMsgRule(dto);
  }

  /**
   * 保存或更新消息接收人
   * @param msgRuleId
   * @param isUser
   * @param id
   */
  public void saveOrUpdateMsgRuleReceiver(String msgRuleId, List dtoOrMapList) {
    msgSettingDao.deleteMsgRuleReceiver(msgRuleId);
    for (int i = 0, size = dtoOrMapList.size(); i < size; i++) {
      XMLData data = (XMLData) dtoOrMapList.get(i);
      data.put("id", msgSettingDao.generateMsgRuleIdBySeq());
      data.put("rg_code", SessionUtil.getRgCode());
      data.put("set_year", SessionUtil.getLoginYear());
    }
    String[] fields = { "id", "msg_rule_id", "rg_code", "set_year", "is_user", "user_id", "user_group_id",
      "upid" };
    msgSettingDao.batchSaveMsgRuleReceiver("MSG_RULE_RECEIVER", fields, dtoOrMapList);
  }

  /**
   * 获取所有规则
   */
  public List getAllMsgRules() {
    return msgSettingDao.getAllMsgRules();
  }

  /**
   * 删除消息规则
   * @param msgRuleId
   */
  public void deleteMsgRule(String msgRuleId) {
    msgSettingDao.deleteMsgRule(msgRuleId);
  }

  /**
   * 删除消息规则接收人
   * @param msgRuleId
   */
  public void deleteMsgRuleReceiver(String msgRuleId) {
    msgSettingDao.deleteMsgRuleReceiver(msgRuleId);
  }

  /**
   * 生成规则Id
   */
  public String generateMsgRuleIdBySeq() {
    return msgSettingDao.generateMsgRuleIdBySeq();
  }

  /**
   * 根据工作流节点和操作类型查找规则
   * @param currentNodeId
   * @param actionType
   * @return
   */
  public List getMsgRulesByWorkFlowInfo(Long currentNodeId, String actionType) {
    return msgSettingDao.getMsgRulesByWorkFlowInfo(currentNodeId, actionType);
  }

  /**
   * 根据单据类型编码获取单据类型名称
   * @param billtypeCode
   * @return
   */
  public String getBilltypeNameByCode(String billtypeCode) {
    return msgSettingDao.getBilltypeNameByCode(billtypeCode);
  }

  /**
   * 得到规则和机构映射关系
   * @return
   */
  public List getRuleToOrg() {
    return msgSettingDao.getRuleToOrg();
  }

  @Override
  public List getMsgRuleDataById(String msgRuleId) {
    // TODO Auto-generated method stub
    return msgSettingDao.getMsgRuleDataByMsgID(msgRuleId);
  }
}
