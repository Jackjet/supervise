package gov.df.fap.bean.workflow;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysWfNodeCondition implements Serializable {

  /**
  * 
  */
  private static final long serialVersionUID = 1446287468673945154L;

  /** identifier field */
  private String WF_ID;

  /** identifier field */
  private String NODE_ID;

  /** identifier field */
  private String NEXT_NODE_ID;

  /** identifier field */
  private String CONDITION_ID;

  /** identifier field */
  private String ROUTING_TYPE;

  //add by liuzw 20120420 增加对多财政多年度的支持
  private String RG_CODE;
  
  //add by zhaoxda 20170614 增加流转线上挂规则
  private String LINE_RULE_ID;


  public String getLINE_RULE_ID() {
	return LINE_RULE_ID;
  }

  public void setLINE_RULE_ID(String lINE_RULE_ID) {
	LINE_RULE_ID = lINE_RULE_ID;
  }

  public String getRG_CODE() {
    return RG_CODE;
  }

  public void setRG_CODE(String rG_CODE) {
    RG_CODE = rG_CODE;
  }

  private int SET_YEAR;

  public int getSET_YEAR() {
    return this.SET_YEAR;
  }

  public void setSET_YEAR(int SET_YEAR) {
    this.SET_YEAR = SET_YEAR;
  }

  //add by liuzw 20120413 添加LAST_VER字段
  private String LAST_VER;

  public String getLAST_VER() {
    return LAST_VER;
  }

  public void setLAST_VER(String LAST_VER) {
    this.LAST_VER = LAST_VER;
  }

  /** full constructor */
  public SysWfNodeCondition(String WF_ID, String NODE_ID, String NEXT_NODE_ID, String CONDITION_ID,
    String ROUTING_TYPE, String LAST_VER, String RG_CODE, int SET_YEAR) {
    this.WF_ID = WF_ID;
    this.NODE_ID = NODE_ID;
    this.NEXT_NODE_ID = NEXT_NODE_ID;
    this.CONDITION_ID = CONDITION_ID;
    this.ROUTING_TYPE = ROUTING_TYPE;
    this.LAST_VER = LAST_VER;
    this.RG_CODE = RG_CODE;
    this.SET_YEAR = SET_YEAR;
  }

  /** default constructor */
  public SysWfNodeCondition() {
  }

  public String getWF_ID() {
    return this.WF_ID;
  }

  public void setWF_ID(String WF_ID) {
    this.WF_ID = WF_ID;
  }

  public String getNODE_ID() {
    return this.NODE_ID;
  }

  public void setNODE_ID(String NODE_ID) {
    this.NODE_ID = NODE_ID;
  }

  public String getNEXT_NODE_ID() {
    return this.NEXT_NODE_ID;
  }

  public void setNEXT_NODE_ID(String NEXT_NODE_ID) {
    this.NEXT_NODE_ID = NEXT_NODE_ID;
  }

  public String getCONDITION_ID() {
    return this.CONDITION_ID;
  }

  public void setCONDITION_ID(String CONDITION_ID) {
    this.CONDITION_ID = CONDITION_ID;
  }

  public String getROUTING_TYPE() {
    return this.ROUTING_TYPE;
  }

  public void setROUTING_TYPE(String ROUTING_TYPE) {
    this.ROUTING_TYPE = ROUTING_TYPE;
  }

  public String toString() {
    return new ToStringBuilder(this).append("WF_ID", getWF_ID()).append("NODE_ID", getNODE_ID())
      .append("NEXT_NODE_ID", getNEXT_NODE_ID()).append("CONDITION_ID", getCONDITION_ID())
      .append("ROUTING_TYPE", getROUTING_TYPE()).toString();
  }

}
