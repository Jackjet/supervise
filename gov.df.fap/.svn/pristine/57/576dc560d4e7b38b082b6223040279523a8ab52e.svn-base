package gov.df.fap.service.messageplatform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.df.fap.api.messageplatform.IMessageConfigOperation;
import gov.df.fap.api.messageplatform.IMsgSetting;
import gov.df.fap.bean.messageplatform.MsgContentFieldsDTO;
import gov.df.fap.bean.messageplatform.MsgRuleDTO;
import gov.df.fap.service.messageplatform.dao.MsgSettingDao;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.xml.XMLData;

@Service
public class MessageConfigOperationBO implements IMessageConfigOperation {

  @Autowired
  private IMsgSetting msgSetting;
  
  @Autowired
  private MsgSettingDao msgSettingDao;
  
  /**
   * 获取所有工作流信息
   */
  public Map<String, Object> getWFList(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> result=new HashMap<String, Object>();
    result.put("result", "success");
    List wfList=msgSetting.queryFlowName();
    result.put("wfList", wfList);
    return result;
  }

  /**
   * 获取所有规则
   */
  public Map<String, Object> getAllMsgRules(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> result=new HashMap<String, Object>();
    result.put("result", "success");
    List msgRules=msgSetting.getAllMsgRules();
    result.put("msgRules", msgRules);
    return result;
  }

  /**
   * 获取工作流中的节点
   */
  public Map<String, Object> getWFNodesById(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> result=new HashMap<String, Object>();
    result.put("result", "success");
    String flowId=request.getParameter("flowId");
    List wfNodes=msgSetting.queryNodeName(flowId);
    result.put("wfNodes", wfNodes);
    return result;
  }
  
  
  /**
   * 删除消息规则
   */
  public Map<String, Object> deleteMsgRuleById(HttpServletRequest request, HttpServletResponse response) {
    // TODO Auto-generated method stub
    Map<String, Object> result=new HashMap<String, Object>();
    result.put("result", "success");
    String msgRuleId=request.getParameter("msgRuleId");
    msgSetting.deleteMsgRule(msgRuleId); //删除消息规则
    msgSetting.deleteMsgRuleReceiver(msgRuleId); //删除消息规则接收人
    msgSetting.deleteAllMsgContentFields(msgRuleId);
    return result;
  }

  @Override
  public Map<String, Object> deleteUserGroup(HttpServletRequest request, HttpServletResponse response) {
    // TODO Auto-generated method stub
    Map<String, Object> result=new HashMap<String, Object>();
    result.put("result", "success");
    String userGroupId=request.getParameter("userGroupId");
    msgSetting.deleteUserGroup(userGroupId);
    msgSetting.deleteUserGroupRelation(userGroupId);
    return result;
  }

  @Override
  public Map<String, Object> saveMsgRule(HttpServletRequest request, HttpServletResponse response) {
    // TODO Auto-generated method stub
    Map<String, Object> result=new HashMap<String, Object>();
    result.put("result", "success");
    Object msgRuleDtoObj=request.getParameter("msgRuleDTO");
    Object msgContentFieldsDTOs=request.getParameter("msgContentFieldsDTOs");
   
    String msgType=request.getParameter("msgType");
    String receiver = request.getParameter("receiver");
    
    JSONObject jsObj=JSONObject.fromObject(msgRuleDtoObj);
    MsgRuleDTO msgRuleDTO=transMsgRuleDTO(msgType,jsObj);
    
    //保存消息模板
    if(msgContentFieldsDTOs != null)
    {
      //先删除原来保存的发送字段
      msgSetting.deleteAllMsgContentFields(msgRuleDTO.getId()+"");
      saveMsgContentFields(msgRuleDTO.getId(),msgContentFieldsDTOs);
    }
  
    if (receiver == null || "".equals(receiver)) {
      msgSetting.deleteMsgRuleReceiver(msgRuleDTO.getId().toString());
    }else{
      msgSetting.saveOrUpdateMsgRuleReceiver(msgRuleDTO.getId().toString(), parseReceiverStringToList(msgRuleDTO.getId().toString(),receiver));
    }
    if("new".equals(msgType)){
      msgSetting.saveMsgRule(msgRuleDTO);
    }
    else{
      msgSetting.updateMsgRule(msgRuleDTO);
    }
    return result;
  }
  
  /**
   * 保存消息模板
   * @param msgRuleId
   */
  public void saveMsgContentFields(Long msgRuleId,Object objMsgContentFields){
    JSONArray jsArr=JSONArray.fromObject(objMsgContentFields);
    for(int i=0,length=jsArr.size();i<length;i++){
      //内容模板
      MsgContentFieldsDTO dto = new MsgContentFieldsDTO();
      dto.setMsgRuleId(msgRuleId);
      JSONObject jsObj=(JSONObject)jsArr.get(0);
      dto.setKeyName(jsObj.getString("key_name"));
      dto.setValueName(jsObj.getString("value_name"));
      msgSetting.saveMsgContentFields(dto);
    }
  }
  
  /**
   * JSONObject 转   MsgRuleDTO
   * @param jsonMsgRule
   * @return
   */
  private MsgRuleDTO transMsgRuleDTO(String msgType,JSONObject jsonMsgRule){
    MsgRuleDTO msgRuleDTO=new MsgRuleDTO();
    Long msg_rule_id=0L;
    Long upId=0L;
    //新增
    if("new".equals(msgType)){
      msg_rule_id=Long.parseLong(msgSetting.generateMsgRuleIdBySeq());
      upId=0L;
    }
    else if("modify".equals(msgType)){
      msg_rule_id=jsonMsgRule.getLong("msg_rule_id");
      upId=jsonMsgRule.getLong("upId");
    }
    msgRuleDTO.setId(msg_rule_id);
    msgRuleDTO.setMsg_rule_code(jsonMsgRule.getString("msg_rule_code"));
    msgRuleDTO.setMsg_rule_name(jsonMsgRule.getString("msg_rule_name"));
    String invoketype=jsonMsgRule.getString("invoketype");
    msgRuleDTO.setInvoketype(invoketype);
    msgRuleDTO.setIs_valid("1");
    
    if("0".equals(invoketype)){
      //业务系统调用
      msgRuleDTO.setWf_flow_id(null);
      msgRuleDTO.setWf_flow_name(null);
      msgRuleDTO.setWf_flow_code(null);
      msgRuleDTO.setWf_node_id(null);
      msgRuleDTO.setWf_node_name(null);
      msgRuleDTO.setWf_node_code(null);
      msgRuleDTO.setWf_action_id(Long.valueOf("0"));
      msgRuleDTO.setWf_action_name(null);
      msgRuleDTO.setWf_action_code(null);
    }
    else{
      msgRuleDTO.setWf_flow_id(jsonMsgRule.getLong("wf_flow_id"));
      msgRuleDTO.setWf_flow_code(jsonMsgRule.getString("wf_flow_code"));
      msgRuleDTO.setWf_flow_name(jsonMsgRule.getString("wf_flow_name"));
      msgRuleDTO.setWf_node_id(jsonMsgRule.getLong("wf_node_id"));
      msgRuleDTO.setWf_node_code(jsonMsgRule.getString("wf_node_code"));
      msgRuleDTO.setWf_node_name(jsonMsgRule.getString("wf_node_name"));
      msgRuleDTO.setWf_action_id(jsonMsgRule.getLong("wf_action_id"));
      msgRuleDTO.setWf_action_code(jsonMsgRule.getString("wf_action_code"));
      msgRuleDTO.setWf_action_name(jsonMsgRule.getString("wf_action_name"));
      msgRuleDTO.setWf_condition(jsonMsgRule.getString("wf_condition"));
    }
    msgRuleDTO.setSend_type(jsonMsgRule.getString("send_type"));
    msgRuleDTO.setEnabled(jsonMsgRule.getString("enabled"));
    msgRuleDTO.setContent_model(jsonMsgRule.getString("content_model"));
    //保存时候已经修改 upId 
    msgRuleDTO.setUpid(upId);
    msgRuleDTO.setContent_title(jsonMsgRule.getString("content_title"));
    msgRuleDTO.setRg_code(SessionUtil.getRgCode());
    msgRuleDTO.setSet_year(Long.parseLong(SessionUtil.getLoginYear()));
    return msgRuleDTO;
  }
  
  
  /**
   * 将接收人由String转换成可用于保存的List
   * @param receiver
   * @return
   */
  public List parseReceiverStringToList(String msgRuleId,String receiver){
    List receiverList = new ArrayList();
    int userIndex = receiver.indexOf("用户-");
    int groupIndex = receiver.indexOf("用户群-");

    if (userIndex < 0) {//仅有用户群
      String[] userGroupReceiver = receiver.split("用户群-")[1].split(",");
      for (int i = 0, n = userGroupReceiver.length; i < n; i++) {
        XMLData data = new XMLData();
        data.put("msg_rule_id", msgRuleId);
        data.put("is_user", "0");
        data.put("user_group_id", userGroupReceiver[i].split(" ")[0]);
        data.put("user_id", "");
        data.put("upid", "0");
        receiverList.add(data);
      }
    } else if (groupIndex < 0) {//仅有用户
      String[] userReceiver = receiver.split("用户-")[1].split(",");
      for (int i = 0, n = userReceiver.length; i < n; i++) {
        XMLData data = new XMLData();
        data.put("msg_rule_id", msgRuleId);
        data.put("is_user", "1");
        data.put("user_id", userReceiver[i].split(" ")[0]);
        data.put("user_group_id", "");
        data.put("upid", "0");
        receiverList.add(data);
      }
    } else { //用户和用户群均有
      String[] userReceiver = receiver.split(";用户-")[1].split(",");
      for (int i = 0, n = userReceiver.length; i < n; i++) {
        XMLData data = new XMLData();
        data.put("msg_rule_id", msgRuleId);
        data.put("is_user", "1");
        data.put("user_id", userReceiver[i].split(" ")[0]);
        data.put("user_group_id", "");
        data.put("upid", "0");
        receiverList.add(data);
      }
      String[] userGroupReceiver = receiver.split(";用户-")[0].split("用户群-")[1].split(",");
      for (int i = 0, n = userGroupReceiver.length; i < n; i++) {
        XMLData data = new XMLData();
        data.put("msg_rule_id", msgRuleId);
        data.put("is_user", "0");
        data.put("user_group_id", userGroupReceiver[i].split(" ")[0]);
        data.put("user_id", "");
        data.put("upid", "0");
        receiverList.add(data);
      }
    }
    return receiverList;
  }

  /**
   * 通过ID获取消息规则
   */
  public Map<String, Object> getMsgRuleById(HttpServletRequest request, HttpServletResponse response) {
    // TODO Auto-generated method stub
    Map<String, Object> result=new HashMap<String, Object>();
    result.put("result", "success");
    String msgRuleId=request.getParameter("msg_rule_id");
    List msgRule=msgSettingDao.getMsgRuleDataByMsgID(msgRuleId);
    List msgContentFields=msgSetting.getMsgContentFields(msgRuleId);
    result.put("msgRule", msgRule);
    result.put("msgContentFields", msgContentFields);
    return result;
  }

  @Override
  public Map<String, Object> saveOrUpdateGroup(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> result=new HashMap<String, Object>();
    result.put("result", "success");
    String groupName=request.getParameter("groupName");
    String groupId=request.getParameter("groupId");
    
    String userIdStr=request.getParameter("userIdStr");
    List choosedUserList=Arrays.asList(userIdStr.split("#"));
    
    //保存
    String userGroupId = msgSetting.saveOrUpdateUserGroup(groupName, groupId);
    msgSetting.saveOrUpdateUserGroupRelation(choosedUserList, userGroupId);
    
    return result;
    
  }

  @Override
  public Map<String, Object> getUserTree(HttpServletRequest request, HttpServletResponse response) {
    // TODO Auto-generated method stub
    Map<String, Object> result=new HashMap<String, Object>();
    result.put("result", "success");
    String msgRuleId=request.getParameter("msgRuleId");
    //当为新增时，特殊处理 获取全部的用户
    if("".equals(msgRuleId)||msgRuleId == null){
      msgRuleId="0000";
    }
    List canSelectUsers=msgSetting.getChoosingUserTreeByRuleId(msgRuleId);
    List canSelectUserGroups=msgSetting.getChoosingUserGroupTreeByRuleId(msgRuleId);
    List hasSelectedUsers=msgSettingDao.getChoosedUserByRuleId(msgRuleId);
    List hasSelectedGroup=msgSettingDao.getChoosedGroupTreeByRuleId(msgRuleId);
    result.put("canSelectUsers", canSelectUsers);
    result.put("canSelectUserGroups", canSelectUserGroups);
    result.put("hasSelectedUsers", hasSelectedUsers);
    result.put("hasSelectedGroup", hasSelectedGroup);
    
    return result;
  }

  @Override
  public Map<String, Object> findSysUser(HttpServletRequest request, HttpServletResponse response) {
    // TODO Auto-generated method stub
    Map<String, Object> result=new HashMap<String, Object>();
    result.put("result", "success");
    String u_code=request.getParameter("u_code");
    String u_name=request.getParameter("u_name");
    List userList=new ArrayList();
    //根据code和name查找用户
    if(!("".equals(u_code) || u_code == null )&& !("".equals(u_name) || u_name == null )){
      userList=msgSettingDao.querySysUserByUsercodeOrName(u_code, u_name);
    }
    else if(u_code != null){
      userList=msgSettingDao.querySysUserBycode(u_code);
    }
    else if(u_name != null){
      userList=msgSettingDao.querySysUserByName(u_name);
    }
    else{
      result.put("result", "fail");
      result.put("reason", "请输入查找条件！");
      return result;
    }
    result.put("userList", userList);
    return result;
  }

  @Override
  public Map<String, Object> getUserByGroupId(HttpServletRequest request, HttpServletResponse response) {
    // TODO Auto-generated method stub
    Map<String, Object> result=new HashMap<String, Object>();
    result.put("result", "success");
    String group_id=request.getParameter("group_id");
    List groupUsers = msgSetting.getChoosedUserTreeByGroupId(group_id);
    result.put("groupUsers", groupUsers);
    return result;
  }

}
