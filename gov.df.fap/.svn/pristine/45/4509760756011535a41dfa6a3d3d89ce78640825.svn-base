package gov.df.fap.bean.message;

import gov.df.fap.bean.workflow.FAttachmentDTO;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Title:消息公共服务参数</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: 北京用友政务软件有限公司</p>
 * @author Gavin.Guo
 * @see 
 * @CreateDate Jul 16, 2014
 * @version 1.0
 */
public class MsgServiceParam implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 父消息id
   */
  private Long msgParentId;

  /**
   * 发送人，纯消息和手工发送模式使用
   */
  private String sendUserId;

  /**
   * 接收人，纯消息模式使用
   */
  private List receiveUserIdList;

  /**
   * 发送类型，纯消息模式使用
   */
  private String sendType;

  /**
   * 标题，纯消息模式使用
   */
  private String msgTitle;

  /**
   * 内容，纯消息模式使用
   */
  private String msgContent;

  /**
   * 消息规则编码，手工发送模式使用
   */
  private String msgRuleCode;

  /**
   * 业务数据列表，手工发送模式和工作流发送模式使用
   */
  private List businessDtoList;

  /**
   * 工作流模版，工作流发送模式使用
   */
  private Long wfFlowId;

  /**
   * 工作流节点，工作流发送模式使用
   */
  private Long wfNodeId;

  /**
   * 操作类型，工作流发送模式使用
   */
  private String wfActionType;

  /**
   * 备注字段
   */
  private String remark;
  
  /**
   * 附件
   */
  private FAttachmentDTO attachment;
  
  /**
   * 系统内消息_角色关联标识
   */
  private Integer is_relaterole;
  
  /**
   * 系统内消息_一次核销消息标识
   */
  private int canceltype;

  public Long getMsgParentId() {
    return msgParentId;
  }

  public void setMsgParentId(Long msgParentId) {
    this.msgParentId = msgParentId;
  }

  public String getSendUserId() {
    return sendUserId;
  }

  public void setSendUserId(String sendUserId) {
    this.sendUserId = sendUserId;
  }

  public List getReceiveUserIdList() {
    return receiveUserIdList;
  }

  public void setReceiveUserIdList(List receiveUserIdList) {
    this.receiveUserIdList = receiveUserIdList;
  }

  public String getSendType() {
    return sendType;
  }

  public void setSendType(String sendType) {
    this.sendType = sendType;
  }

  public String getMsgTitle() {
    return msgTitle;
  }

  public void setMsgTitle(String msgTitle) {
    this.msgTitle = msgTitle;
  }

  public String getMsgContent() {
    return msgContent;
  }

  public void setMsgContent(String msgContent) {
    this.msgContent = msgContent;
  }

  public String getMsgRuleCode() {
    return msgRuleCode;
  }

  public void setMsgRuleCode(String msgRuleCode) {
    this.msgRuleCode = msgRuleCode;
  }

  public List getBusinessDtoList() {
    return businessDtoList;
  }

  public void setBusinessDtoList(List businessDtoList) {
    this.businessDtoList = businessDtoList;
  }

  public Long getWfFlowId() {
    return wfFlowId;
  }

  public void setWfFlowId(Long wfFlowId) {
    this.wfFlowId = wfFlowId;
  }

  public Long getWfNodeId() {
    return wfNodeId;
  }

  public void setWfNodeId(Long wfNodeId) {
    this.wfNodeId = wfNodeId;
  }

  public String getWfActionType() {
    return wfActionType;
  }

  public void setWfActionType(String wfActionType) {
    this.wfActionType = wfActionType;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public FAttachmentDTO getAttachment() {
    return attachment;
  }

  public void setAttachment(FAttachmentDTO attachment) {
    this.attachment = attachment;
  }

  public Integer getIs_relaterole() {
    return is_relaterole;
  }

  public void setIs_relaterole(Integer is_relaterole) {
    this.is_relaterole = is_relaterole;
  }

  public int getCanceltype() {
    return canceltype;
  }

  public void setCanceltype(int canceltype) {
    this.canceltype = canceltype;
  }

}
