package gov.df.fap.bean.rule;

public class SysRuleFunctionParas implements java.io.Serializable {

  /**
  * 
  */
  private static final long serialVersionUID = -4428414957024400059L;

  private String fun_id;

  private Long funParaid;

  private SysRuleFunction sysWfFunction;

  private String funParaname;

  private String funParatype;

  private Long funParasort;

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
  public SysRuleFunctionParas() {
  }

  /** full constructor */
  public SysRuleFunctionParas(SysRuleFunction sysWfFunction, String funParaname, String funParatype, Long funParasort,
    String RG_CODE, int SET_YEAR) {
    this.sysWfFunction = sysWfFunction;
    this.funParaname = funParaname;
    this.funParatype = funParatype;
    this.funParasort = funParasort;
    this.RG_CODE = RG_CODE;
    this.SET_YEAR = SET_YEAR;
  }

  // Property accessors

  public Long getFunParaid() {
    return this.funParaid;
  }

  public void setFunParaid(Long funParaid) {
    this.funParaid = funParaid;
  }

  public SysRuleFunction getSysWfFunction() {
    return this.sysWfFunction;
  }

  public void setSysWfFunction(SysRuleFunction sysWfFunction) {
    this.sysWfFunction = sysWfFunction;
  }

  public String getFunParaname() {
    return this.funParaname;
  }

  public void setFunParaname(String funParaname) {
    this.funParaname = funParaname;
  }

  public String getFunParatype() {
    return this.funParatype;
  }

  public void setFunParatype(String funParatype) {
    this.funParatype = funParatype;
  }

  public Long getFunParasort() {
    return this.funParasort;
  }

  public void setFunParasort(Long funParasort) {
    this.funParasort = funParasort;
  }

  public String getFun_id() {
    return fun_id;
  }

  public void setFun_id(String fun_id) {
    this.fun_id = fun_id;
  }

}