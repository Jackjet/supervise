package gov.df.supervise.bean.csofacc;

import java.io.Serializable;

public class csofAccSupOfficeEntity implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String id;

  private int is_input;

  private String work_content;

  private String set_id;

  public String getSet_id() {
    return set_id;
  }

  public void setSet_id(String set_id) {
    this.set_id = set_id;
  }

  public String getWork_content() {
    return work_content;
  }

  public void setWork_content(String work_content) {
    this.work_content = work_content;
  }

  private int sup_num;

  public int getSup_num() {
    return sup_num;
  }

  public void setSup_num(int sup_num) {
    this.sup_num = sup_num;
  }

  public int getIs_input() {
    return is_input;
  }

  public void setIs_input(int is_input) {
    this.is_input = is_input;
  }

  private String sid;

  private String sup_no;

  private String sup_name;

  private String set_month;

  private String year;

  private String acc_id;

  public String getAcc_id() {
    return acc_id;
  }

  public void setAcc_id(String acc_id) {
    this.acc_id = acc_id;
  }

  public String getSet_month() {
    return set_month;
  }

  public void setSet_month(String set_month) {
    this.set_month = set_month;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getSid() {
    return sid;
  }

  public void setSid(String sid) {
    this.sid = sid;
  }

  public String getSup_no() {
    return sup_no;
  }

  public void setSup_no(String sup_no) {
    this.sup_no = sup_no;
  }

  public String getSup_name() {
    return sup_name;
  }

  public void setSup_name(String sup_name) {
    this.sup_name = sup_name;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getIs_valid() {
    return is_valid;
  }

  public void setIs_valid(int is_valid) {
    this.is_valid = is_valid;
  }

  public int getIs_end() {
    return is_end;
  }

  public void setIs_end(int is_end) {
    this.is_end = is_end;
  }

  public String getOid() {
    return oid;
  }

  public void setOid(String oid) {
    this.oid = oid;
  }

  public String getSup_type_id() {
    return sup_type_id;
  }

  public void setSup_type_id(String sup_type_id) {
    this.sup_type_id = sup_type_id;
  }

  public String getSup_type_code() {
    return sup_type_code;
  }

  public void setSup_type_code(String sup_type_code) {
    this.sup_type_code = sup_type_code;
  }

  public String getSup_type_name() {
    return sup_type_name;
  }

  public void setSup_type_name(String sup_type_name) {
    this.sup_type_name = sup_type_name;
  }

  public String getObj_type_id() {
    return obj_type_id;
  }

  public void setObj_type_id(String obj_type_id) {
    this.obj_type_id = obj_type_id;
  }

  public String getObj_type_code() {
    return obj_type_code;
  }

  public void setObj_type_code(String obj_type_code) {
    this.obj_type_code = obj_type_code;
  }

  public String getObj_type_name() {
    return obj_type_name;
  }

  public void setObj_type_name(String obj_type_name) {
    this.obj_type_name = obj_type_name;
  }

  public String getWork_type() {
    return work_type;
  }

  public void setWork_type(String work_type) {
    this.work_type = work_type;
  }

  public String getSup_content() {
    return sup_content;
  }

  public void setSup_content(String sup_content) {
    this.sup_content = sup_content;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getCreate_user() {
    return create_user;
  }

  public void setCreate_user(String create_user) {
    this.create_user = create_user;
  }

  public String getCreate_date() {
    return create_date;
  }

  public void setCreate_date(String create_date) {
    this.create_date = create_date;
  }

  public String getLatest_op_user() {
    return latest_op_user;
  }

  public void setLatest_op_user(String latest_op_user) {
    this.latest_op_user = latest_op_user;
  }

  public String getLatest_op_date() {
    return latest_op_date;
  }

  public void setLatest_op_date(String latest_op_date) {
    this.latest_op_date = latest_op_date;
  }

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

  private String status;

  private int is_valid;

  private int is_end;

  private String oid;

  private String sup_type_id;

  private String sup_type_code;

  private String sup_type_name;

  private String obj_type_id;

  private String obj_type_code;

  private String obj_type_name;

  private String work_type;

  private String regulatory_area;

  private String time_requirement;

  private String regulatory_results;

  private String feedback;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRegulatory_area() {
    return regulatory_area;
  }

  public void setRegulatory_area(String regulatory_area) {
    this.regulatory_area = regulatory_area;
  }

  public String getTime_requirement() {
    return time_requirement;
  }

  public void setTime_requirement(String time_requirement) {
    this.time_requirement = time_requirement;
  }

  public String getRegulatory_results() {
    return regulatory_results;
  }

  public void setRegulatory_results(String regulatory_results) {
    this.regulatory_results = regulatory_results;
  }

  public String getFeedback() {
    return feedback;
  }

  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }

  private String sup_content;

  private String remark;

  private String create_user;

  private String create_date;

  private String latest_op_user;

  private String latest_op_date;

  private String set_year;

  private String rg_code;

  private String book_id;

  private String sup_object;

  private String sup_money;

  private String sup_cost;

  public String getBook_id() {
    return book_id;
  }

  public void setBook_id(String book_id) {
    this.book_id = book_id;
  }

  public String getSup_object() {
    return sup_object;
  }

  public void setSup_object(String sup_object) {
    this.sup_object = sup_object;
  }

  public String getSup_money() {
    return sup_money;
  }

  public void setSup_money(String sup_money) {
    this.sup_money = sup_money;
  }

  public String getSup_cost() {
    return sup_cost;
  }

  public void setSup_cost(String sup_cost) {
    this.sup_cost = sup_cost;
  }

}
