package gov.df.fap.bean.rule;

import java.util.HashSet;
import java.util.Set;

/**
 * AbstractSysRuleFunction generated 
 */

public class SysRuleFunction implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  private String funId;

  private String funClassname;

  private String funMethod;

  private String funName;

  private String funRemark;

  private String funValuetype;

  private String sysId;

  private Set sysWfFunctionParases = new HashSet(0);

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

  // Constructors

  /** default constructor */
  public SysRuleFunction() {
  }

  /** full constructor */
  public SysRuleFunction(String funClassname, String funMethod, String funName, String funRemark, String funValuetype,
    String sysId, Set sysWfFunctionParases, String RG_CODE, int SET_YEAR) {
    this.funClassname = funClassname;
    this.funMethod = funMethod;
    this.funName = funName;
    this.funRemark = funRemark;
    this.funValuetype = funValuetype;
    this.sysId = sysId;
    this.sysWfFunctionParases = sysWfFunctionParases;
    this.RG_CODE = RG_CODE;
    this.SET_YEAR = SET_YEAR;
  }

  // Property accessors

  public String getFunId() {
    return this.funId;
  }

  public void setFunId(String funId) {
    this.funId = funId;
  }

  public String getFunClassname() {
    return this.funClassname;
  }

  public void setFunClassname(String funClassname) {
    this.funClassname = funClassname;
  }

  public String getFunMethod() {
    return this.funMethod;
  }

  public void setFunMethod(String funMethod) {
    this.funMethod = funMethod;
  }

  public String getFunName() {
    return this.funName;
  }

  public void setFunName(String funName) {
    this.funName = funName;
  }

  public String getFunRemark() {
    return this.funRemark;
  }

  public void setFunRemark(String funRemark) {
    this.funRemark = funRemark;
  }

  public String getFunValuetype() {
    return this.funValuetype;
  }

  public void setFunValuetype(String funValuetype) {
    this.funValuetype = funValuetype;
  }

  public String getSysId() {
    return this.sysId;
  }

  public void setSysId(String sysId) {
    this.sysId = sysId;
  }

  public Set getSysWfFunctionParases() {
    return this.sysWfFunctionParases;
  }

  public void setSysWfFunctionParases(Set sysWfFunctionParases) {
    this.sysWfFunctionParases = sysWfFunctionParases;
  }

}