package gov.df.fap.bean.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysRightGroup implements Serializable {

  private static final long serialVersionUID = 1L;

  /** identifier field */
  private String RIGHT_GROUP_ID;

  /** nullable persistent field */
  private String RIGHT_GROUP_DESCRIBE;

  /** nullable persistent field */
  private Integer RIGHT_TYPE;

  /** nullable persistent field */
  private String RULE_ID;

  /** not nullable persistent field ADD BY KONGCY AT 2012-04-01*/
  private String SET_YEAR;

  public String getSET_YEAR() {
    return SET_YEAR;
  }

  public void setSET_YEAR(String sET_YEAR) {
    SET_YEAR = sET_YEAR;
  }

  public String getRG_CODE() {
    return RG_CODE;
  }

  public void setRG_CODE(String rG_CODE) {
    RG_CODE = rG_CODE;
  }

  /** not nullable persistent field  ADD BY KONGCY AT 2012-04-01*/
  private String RG_CODE;

  /** full constructor */
  public SysRightGroup(String RIGHT_GROUP_ID, String RIGHT_GROUP_DESCRIBE, Integer RIGHT_TYPE, String RULE_ID) {
    this.RIGHT_GROUP_ID = RIGHT_GROUP_ID;
    this.RIGHT_GROUP_DESCRIBE = RIGHT_GROUP_DESCRIBE;
    this.RIGHT_TYPE = RIGHT_TYPE;
    this.RULE_ID = RULE_ID;
  }

  /** default constructor */
  public SysRightGroup() {
  }

  /** minimal constructor */
  public SysRightGroup(String RIGHT_GROUP_ID) {
    this.RIGHT_GROUP_ID = RIGHT_GROUP_ID;
  }

  public String getRIGHT_GROUP_ID() {
    return this.RIGHT_GROUP_ID;
  }

  public void setRIGHT_GROUP_ID(String RIGHT_GROUP_ID) {
    this.RIGHT_GROUP_ID = RIGHT_GROUP_ID;
  }

  public String getRIGHT_GROUP_DESCRIBE() {
    return this.RIGHT_GROUP_DESCRIBE;
  }

  public void setRIGHT_GROUP_DESCRIBE(String RIGHT_GROUP_DESCRIBE) {
    this.RIGHT_GROUP_DESCRIBE = RIGHT_GROUP_DESCRIBE;
  }

  public Integer getRIGHT_TYPE() {
    return this.RIGHT_TYPE;
  }

  public void setRIGHT_TYPE(Integer RIGHT_TYPE) {
    this.RIGHT_TYPE = RIGHT_TYPE;
  }

  public String getRULE_ID() {
    return this.RULE_ID;
  }

  public void setRULE_ID(String RULE_ID) {
    this.RULE_ID = RULE_ID;
  }

  public String toString() {
    return new ToStringBuilder(this).append("RIGHT_GROUP_ID", getRIGHT_GROUP_ID()).toString();
  }

}
