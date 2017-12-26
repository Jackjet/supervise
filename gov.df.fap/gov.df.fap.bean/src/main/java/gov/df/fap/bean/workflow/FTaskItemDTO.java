package gov.df.fap.bean.workflow;

import gov.df.fap.bean.util.DTO;

/**
 * 事务提醒数据传输DTO对象
 * 
 * @version 1.0
 * @author 
 * @since java 1.6
 * @de
 */
public class FTaskItemDTO extends DTO {

  public static final long serialVersionUID = -1L;

  // 任务ID
  private String task_id = "";

  // 类型编码
  private String msg_type_code = "";

  // 类型名称
  private String msg_type_name = "";

  // 内部分类中文名
  private String msg_type_name_local = "";

  // 任务描述
  private String task_content = "";

  // 功能ID
  private String module_id = "";

  // 角色ID
  private String role_id = "";

  // 进入功能的参数串
  private String para_string = "";

  //发送类别
  private String send_type = "0";

  //是否临时消息
  private String is_temp = "0";

  //菜单ID
  private String menu_id = "";

  //系统app
  private String sysapp = "";

  //菜单名
  private String menu_name = "";

  //ymj 增加 操作时间
  private String operation_date = "";

  private String menu_url = "";

  //ymj 增加 操作角色
  private String role_name = "";

  public String getRole_name() {
    return role_name;
  }

  public void setRole_name(String role_name) {
    this.role_name = role_name;
  }

  public String getOperation_date() {
    return operation_date;
  }

  public void setOperation_date(String operation_date) {
    this.operation_date = operation_date;
  }

  public String getMenu_name() {
    return menu_name;
  }

  public void setMenu_name(String menu_name) {
    this.menu_name = menu_name;
  }

  public String getSysapp() {
    return sysapp;
  }

  public void setSysapp(String sysapp) {
    this.sysapp = sysapp;
  }

  public String getTask_id() {
    return task_id;
  }

  public void setTask_id(String task_id) {
    this.task_id = task_id;
  }

  public String getModule_id() {
    return module_id;
  }

  public void setModule_id(String module_id) {
    this.module_id = module_id;
  }

  public String getMsg_type_code() {
    return msg_type_code;
  }

  public void setMsg_type_code(String msg_type_code) {
    this.msg_type_code = msg_type_code;
  }

  public String getMsg_type_name() {
    return msg_type_name;
  }

  public void setMsg_type_name(String msg_type_name) {
    this.msg_type_name = msg_type_name;
  }

  public String getRole_id() {
    return role_id;
  }

  public void setRole_id(String role_id) {
    this.role_id = role_id;
  }

  public String getIs_temp() {
    return is_temp;
  }

  public void setIs_temp(String is_temp) {
    this.is_temp = is_temp;
  }

  public String getMsg_type_name_local() {
    return msg_type_name_local;
  }

  public void setMsg_type_name_local(String msg_type_name_local) {
    this.msg_type_name_local = msg_type_name_local;
  }

  public String getPara_string() {
    return para_string;
  }

  public void setPara_string(String para_string) {
    this.para_string = para_string;
  }

  public String getSend_type() {
    return send_type;
  }

  public void setSend_type(String send_type) {
    this.send_type = send_type;
  }

  public String getTask_content() {
    return task_content;
  }

  public void setTask_content(String task_content) {
    this.task_content = task_content;
  }

  public String getMenu_id() {
    return menu_id;
  }

  public void setMenu_id(String menu_id) {
    this.menu_id = menu_id;
  }

  public String getMenu_url() {
    return menu_url;
  }

  public void setMenu_url(String menu_url) {
    this.menu_url = menu_url;
  }

}
