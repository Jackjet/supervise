package gov.df.fap.bean.log;

import java.io.Serializable;
import java.math.BigDecimal;

/** @author Hibernate CodeGenerator */
public class LogDTO implements Serializable {

  /**
  * 
  */
  private static final long serialVersionUID = 1L;

  /** identifier field */
  private String log_id;

  /** nullable persistent field */
  private String user_id;

  private String user_name;

  /** nullable persistent field */
  private String user_ip;

  /** nullable persistent field */
  private String log_type;

  /** nullable persistent field */
  private Integer log_level;

  /** nullable persistent field */
  private String menu_id;

  /** nullable persistent field */
  private String module_id;

  /** nullable persistent field */
  private String action_type_code;

  /** nullable persistent field */
  private String action_name;

  /** nullable persistent field */
  private String sys_id;

  /** nullable persistent field */
  private String inspect_info;

  /** nullable persistent field */
  private String remark;

  /** nullable persistent field */
  private String wf_name;

  /** nullable persistent field */
  private String cur_node_name;

  /** nullable persistent field */
  private BigDecimal money;

  /** nullable persistent field */
  private String vou_id;

  /** nullable persistent field */
  private String oper_time;

  /** nullable persistent field */
  private String last_ver;

  /** nullable persistent field */
  private String set_year;

  /** nullable persistent field */
  private String rg_code;

  public String getSet_year() {
    return set_year;
  }

  public void setSet_year(String set_year) {
    this.set_year = set_year;
  }

  public String getRg_code() {
    return rg_code;
  }

  public void setRg_code(String rg_code) {
    this.rg_code = rg_code;
  }

  public LogDTO() {

  }

  public String getAction_name() {
    return action_name;
  }

  public void setAction_name(String action_name) {
    this.action_name = action_name;
  }

  public String getAction_type_code() {
    return action_type_code;
  }

  public void setAction_type_code(String action_type_code) {
    this.action_type_code = action_type_code;
  }

  public String getCur_node_name() {
    return cur_node_name;
  }

  public void setCur_node_name(String cur_node_name) {
    this.cur_node_name = cur_node_name;
  }

  public String getInspect_info() {
    return inspect_info;
  }

  public void setInspect_info(String inspect_info) {
    this.inspect_info = inspect_info;
  }

  public String getLast_ver() {
    return last_ver;
  }

  public void setLast_ver(String last_ver) {
    this.last_ver = last_ver;
  }

  public String getLog_id() {
    return log_id;
  }

  public void setLog_id(String log_id) {
    this.log_id = log_id;
  }

  public Integer getLog_level() {
    return log_level;
  }

  public void setLog_level(Integer log_level) {
    this.log_level = log_level;
  }

  public String getLog_type() {
    return log_type;
  }

  public void setLog_type(String log_type) {
    this.log_type = log_type;
  }

  public String getMenu_id() {
    return menu_id;
  }

  public void setMenu_id(String menu_id) {
    this.menu_id = menu_id;
  }

  public String getModule_id() {
    return module_id;
  }

  public void setModule_id(String module_id) {
    this.module_id = module_id;
  }

  public BigDecimal getMoney() {
    return money;
  }

  public void setMoney(BigDecimal money) {
    this.money = money;
  }

  public String getOper_time() {
    return oper_time;
  }

  public void setOper_time(String oper_time) {
    this.oper_time = oper_time;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getSys_id() {
    return sys_id;
  }

  public void setSys_id(String sys_id) {
    this.sys_id = sys_id;
  }

  public String getUser_id() {
    return user_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }

  public String getUser_ip() {
    return user_ip;
  }

  public void setUser_ip(String user_ip) {
    this.user_ip = user_ip;
  }

  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }

  public String getVou_id() {
    return vou_id;
  }

  public void setVou_id(String vou_id) {
    this.vou_id = vou_id;
  }

  public String getWf_name() {
    return wf_name;
  }

  public void setWf_name(String wf_name) {
    this.wf_name = wf_name;
  }
}
