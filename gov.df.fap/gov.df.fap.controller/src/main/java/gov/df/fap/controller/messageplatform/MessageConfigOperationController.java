package gov.df.fap.controller.messageplatform;

import gov.df.fap.api.messageplatform.IMessageConfigOperation;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/messageconfig")
public class MessageConfigOperationController {
  
  @Autowired
  private IMessageConfigOperation messageConfigOperation;

  /**
   * 查询所有工作流
   */
  @RequestMapping(method = RequestMethod.GET,value="/getWFList.do")
  public  @ResponseBody Map<String,Object> getWFList(HttpServletRequest request,HttpServletResponse response){
    Map<String,Object> wfList = new HashMap<String, Object>();
    wfList=messageConfigOperation.getWFList(request, response);
    return wfList;
  }
  
  /**
   * 获取所有规则
   */
  @RequestMapping(method = RequestMethod.GET,value="/getAllMsgRules.do")
  public  @ResponseBody Map<String,Object> getAllMsgRules(HttpServletRequest request,HttpServletResponse response){
    Map<String,Object> msgRuleList = new HashMap<String, Object>();
    msgRuleList=messageConfigOperation.getAllMsgRules(request, response);
    return msgRuleList;
  }
  
  /**
   * 根据规则ID获取消息规则
   */
  @RequestMapping(method = RequestMethod.GET,value="/getMsgRuleById.do")
  public  @ResponseBody Map<String,Object> getMsgRuleById(HttpServletRequest request,HttpServletResponse response){
    Map<String,Object> msgRuleList = new HashMap<String, Object>();
    msgRuleList=messageConfigOperation.getMsgRuleById(request, response);
    return msgRuleList;
  }
  
  
  
  /**
   * 获取工作流中的节点
   */
  @RequestMapping(method = RequestMethod.GET,value="/getWFNodesById.do")
  public  @ResponseBody Map<String,Object> getWFNodesById(HttpServletRequest request,HttpServletResponse response){
    Map<String,Object> msgRuleList = new HashMap<String, Object>();
    msgRuleList=messageConfigOperation.getWFNodesById(request, response);
    return msgRuleList;
  }
  
  
  /**
   * 保存消息规则
   */
  @RequestMapping(method = RequestMethod.POST,value="/saveMsgRule.do")
  public  @ResponseBody Map<String,Object> saveMsgRule(HttpServletRequest request,HttpServletResponse response){
    Map<String,Object> result = new HashMap<String, Object>();
    result=messageConfigOperation.saveMsgRule(request, response);
    return result;
  }
  
  /**
   * 删除消息规则
   */
  @RequestMapping(method = RequestMethod.POST,value="/deleteMsgRuleById.do")
  public  @ResponseBody Map<String,Object> deleteMsgRuleById(HttpServletRequest request,HttpServletResponse response){
    Map<String,Object> result = new HashMap<String, Object>();
    result=messageConfigOperation.deleteMsgRuleById(request, response);
    return result;
  }
  
  /**
   * 删除用户群
   */
  @RequestMapping(method = RequestMethod.POST,value="/deleteUserGroup.do")
  public  @ResponseBody Map<String,Object> deleteUserGroup(HttpServletRequest request,HttpServletResponse response){
    Map<String,Object> result = new HashMap<String, Object>();
    result=messageConfigOperation.deleteUserGroup(request, response);
    return result;
  }
  
  /**
   * 保存或者更新用户群
   */
  @RequestMapping(method = RequestMethod.POST,value="/saveOrUpdateGroup.do")
  public  @ResponseBody Map<String,Object> saveOrUpdateGroup(HttpServletRequest request,HttpServletResponse response){
    Map<String,Object> result = new HashMap<String, Object>();
    result=messageConfigOperation.saveOrUpdateGroup(request, response);
    return result;
  }
  
  
  /**
   * 获取用户（已选择用户/未选择用户）
   */
  @RequestMapping(method = RequestMethod.GET,value="/getUserTree.do")
  public  @ResponseBody Map<String,Object> getUserTree(HttpServletRequest request,HttpServletResponse response){
    Map<String,Object> result = new HashMap<String, Object>();
    result=messageConfigOperation.getUserTree(request, response);
    return result;
  }
  
  /**
   * 获取用户群中的用户
   */
  @RequestMapping(method = RequestMethod.GET,value="/getUserByGroupId.do")
  public  @ResponseBody Map<String,Object> getUserByGroupId(HttpServletRequest request,HttpServletResponse response){
    Map<String,Object> result = new HashMap<String, Object>();
    result=messageConfigOperation.getUserByGroupId(request, response);
    return result;
  }
  
  
  /**
   * 消息面板-查找用户）
   */
  @RequestMapping(method = RequestMethod.GET,value="/findSysUser.do")
  public  @ResponseBody Map<String,Object> findSysUser(HttpServletRequest request,HttpServletResponse response){
    Map<String,Object> result = new HashMap<String, Object>();
    result=messageConfigOperation.findSysUser(request, response);
    return result;
  }
  
  
  
}
