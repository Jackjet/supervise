package gov.df.fap.bean.sysdb;

import java.io.Serializable;

public class SysRegion implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public SysRegion() {

  }

  public SysRegion(String rgCode, String rgName) {
    this.rg_code = rgCode;
    this.rg_name = rgName;
  }

  public String rg_code;

  public String rg_name;

  public String is_top;

  public String chr_code1;

  public String getChr_code1() {
    return chr_code1;
  }

  public void setChr_code1(String chr_code1) {
    this.chr_code1 = chr_code1;
  }

  public String getIs_top() {
    return is_top;
  }

  public void setIs_top(String is_top) {
    this.is_top = is_top;
  }

  public String toString() {
    return this.rg_code + " " + this.rg_name;
  }

  public String getRg_code() {
    return rg_code;
  }

  public void setRg_code(String rg_code) {
    this.rg_code = rg_code;
  }

  public String getRg_name() {
    return rg_name;
  }

  public void setRg_name(String rg_name) {
    this.rg_name = rg_name;
  }

  public boolean isTopRegion() {
    if (this.is_top != null && this.is_top.equals("1")) {
      return true;
    }
    return false;
  }
}
