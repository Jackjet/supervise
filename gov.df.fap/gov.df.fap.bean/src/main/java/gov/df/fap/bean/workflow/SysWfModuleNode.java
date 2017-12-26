package gov.df.fap.bean.workflow;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysWfModuleNode implements Serializable {

  /**
  * 
  */
  private static final long serialVersionUID = 7566229575308676280L;

  /** identifier field */
  private String NODE_ID;

  /** identifier field */
  private String MODULE_ID;

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
  public SysWfModuleNode(String NODE_ID, String MODULE_ID, String RG_CODE, int SET_YEAR, String LAST_VER) {
    this.NODE_ID = NODE_ID;
    this.MODULE_ID = MODULE_ID;
    this.RG_CODE = RG_CODE;
    this.SET_YEAR = SET_YEAR;
    this.LAST_VER = LAST_VER;
  }

  /** default constructor */
  public SysWfModuleNode() {
  }

  public String getNODE_ID() {
    return this.NODE_ID;
  }

  public void setNODE_ID(String NODE_ID) {
    this.NODE_ID = NODE_ID;
  }

  public String getMODULE_ID() {
    return this.MODULE_ID;
  }

  public void setMODULE_ID(String MODULE_ID) {
    this.MODULE_ID = MODULE_ID;
  }

  public String toString() {
    return new ToStringBuilder(this).append("NODE_ID", getNODE_ID()).append("MODULE_ID", getMODULE_ID())
      .toString();
  }

}
