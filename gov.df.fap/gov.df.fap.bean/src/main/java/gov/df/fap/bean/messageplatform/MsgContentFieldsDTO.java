package gov.df.fap.bean.messageplatform;

import gov.df.fap.bean.util.DTO;

import java.io.Serializable;


/**
 * <p>
 * 
 * Title:消息规则 内容模板按钮字段DTO
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
 * @author chenyy
 * @see 
 * @CreateDate 2014-7-15
 * @version 1.0
 */
public class MsgContentFieldsDTO extends DTO implements Serializable {
  
  private static final long serialVersionUID = 4322894448278975364L;
  /**
   * ID流水号
   */
  private Long id;
  /**
   * 更新id
   */
  private Long upId;
  /**
   * 是否有效
   * 0:无效
   * 1:有效
   */
  private String isValid;
  /**
   * 区划
   */
  private String rgCode;

  /**
   * 年度
   */
  private String setYear;
  /**
   * 消息规则ID
   */
  private Long msgRuleId;
  /**
   * 占位符变量名
   */
  private String keyName;
  /**
   * 业务要素变量名
   */
  private String valueName;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUpId() {
    return upId;
  }

  public void setUpId(Long upId) {
    this.upId = upId;
  }

  public String getIsValid() {
    return isValid;
  }

  public void setIsValid(String isValid) {
    this.isValid = isValid;
  }

  public String getRgCode() {
    return rgCode;
  }

  public void setRgCode(String rgCode) {
    this.rgCode = rgCode;
  }

  public String getSetYear() {
    return setYear;
  }

  public void setSetYear(String setYear) {
    this.setYear = setYear;
  }

  public String getKeyName() {
    return keyName;
  }
  
  public void setKeyName(String keyName) {
    this.keyName = keyName;
  }
  
  public Long getMsgRuleId() {
    return msgRuleId;
  }
  
  public void setMsgRuleId(Long msgRuleId) {
    this.msgRuleId = msgRuleId;
  }
  
  public String getValueName() {
    return valueName;
  }
  
  public void setValueName(String valueName) {
    this.valueName = valueName;
  }

}
