package gov.df.fap.bean.workflow;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysWfNodeTollyActionType implements Serializable {

  /**
  * 
  */
  private static final long serialVersionUID = 5920759326250923107L;

  /** identifier field */
  private String NODE_ID;

  /** identifier field */
  private String ACTION_TYPE_CODE;

  /** persistent field */
  private int TOLLY_FLAG;

  //add by liuzw 20120420 增加对多财政多年度的支持
  private String RG_CODE;

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

  //add by liuzw 20120514 添加LAST_VER字段
  private String LAST_VER;

  public String getLAST_VER() {
    return LAST_VER;
  }

  public void setLAST_VER(String LAST_VER) {
    this.LAST_VER = LAST_VER;
  }

  /** full constructor */
  public SysWfNodeTollyActionType(String NODE_ID, String ACTION_TYPE_CODE, int TOLLY_FLAG, String RG_CODE,
    int SET_YEAR, String LAST_VER) {
    this.NODE_ID = NODE_ID;
    this.ACTION_TYPE_CODE = ACTION_TYPE_CODE;
    this.TOLLY_FLAG = TOLLY_FLAG;
    this.RG_CODE = RG_CODE;
    this.SET_YEAR = SET_YEAR;
    this.LAST_VER = LAST_VER;
  }

  /** default constructor */
  public SysWfNodeTollyActionType() {
  }

  public String getNODE_ID() {
    return this.NODE_ID;
  }

  public void setNODE_ID(String NODE_ID) {
    this.NODE_ID = NODE_ID;
  }

  public String getACTION_TYPE_CODE() {
    return this.ACTION_TYPE_CODE;
  }

  public void setACTION_TYPE_CODE(String ACTION_TYPE_CODE) {
    this.ACTION_TYPE_CODE = ACTION_TYPE_CODE;
  }

  public int getTOLLY_FLAG() {
    return this.TOLLY_FLAG;
  }

  public void setTOLLY_FLAG(int TOLLY_FLAG) {
    this.TOLLY_FLAG = TOLLY_FLAG;
  }

  public String toString() {
    return new ToStringBuilder(this).append("NODE_ID", getNODE_ID()).append("ACTION_TYPE_CODE", getACTION_TYPE_CODE())
      .toString();
  }

}
