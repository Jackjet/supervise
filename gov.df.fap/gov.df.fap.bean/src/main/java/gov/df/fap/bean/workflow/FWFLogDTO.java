package gov.df.fap.bean.workflow;

import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.bean.util.DTO;
import gov.df.fap.util.xml.ParseXML;

/**
 * 工作流日志数据传输DTO对象
 * 
 * @version 1.0
 * @author zhadaopeng
 * @since java 1.4.2
 * @de
 */
public class FWFLogDTO extends DTO {
  public static final long serialVersionUID = -1L;

  // 任务ID
  private String task_id = "";

  // 流程ID
  private String wf_id = "";

  // 流程名称
  private String wf_name = "";

  // 流程表名
  private String wf_table_name = "";

  // 业务ID
  private String entity_id = "";

  // 当前节点ID
  private String node_id = "";

  // 当前节点名称
  private String node_name = "";
  
  // 当前节点编码
  private String node_code = "";
  

  // 操作类型ID
  private String action_type_id = "";

  // 操作类型名称
  private String action_type_name = "";

  // 是否被撤消标志
  private String is_undo = "";

  // 创建人
  private String create_user = "";

  // 创建时间
  private String create_date = "";

  // 撤消人
  private String undo_user = "";

  // 撤消时间
  private String undo_date = "";

  // 操作名称
  private String operation_name = "";

  // 操作时间
  private String operation_date = "";

  // 审核意见
  private String operation_remark = "";

  // 初始金额
  private String init_money = "";

  // 修改后金额
  private String result_money = "";

  // DTO
  private FVoucherDTO voucher_dto = new FVoucherDTO();

  // DTO的字符串形式
  private String remark = "";

  // 状态CODE
  private String status_code = "";

  // 状态名称
  private String status_name = "";

  // 记录条数
  private String num = "0";

  //ymj增加 明细记录条数
  private String detailNum = "0";

  // 功能ID
  private String module_id = "";

  // 功能名称
  private String module_name = "";

  // 菜单ID
  private String menu_id = "";

  // 菜单名称
  private String menu_name = "";

  private String URL = "";

  // 结点类型
  private String node_type = "";

  //子系统ID
  private String sys_id = "";

  private String user_code;

  private String user_name;

  private String telephone;

  private String mobile;

  public String getSys_id() {
    return sys_id;
  }

  public void setSys_id(String sys_id) {
    this.sys_id = sys_id;
  }

  public String getTask_id() {
    return task_id;
  }

  public void setTask_id(String task_id) {
    this.task_id = task_id;
  }

  public String getWf_id() {
    return wf_id;
  }

  public void setWf_id(String wf_id) {
    this.wf_id = wf_id;
  }

  public String getWf_name() {
    return wf_name;
  }

  public void setWf_name(String wf_name) {
    this.wf_name = wf_name;
  }

  public String getWf_table_name() {
    return wf_table_name;
  }

  public void setWf_table_name(String wf_table_name) {
    this.wf_table_name = wf_table_name;
  }

  public String getEntity_id() {
    return entity_id;
  }

  public void setEntity_id(String entity_id) {
    this.entity_id = entity_id;
  }

  public String getAction_type_id() {
    return action_type_id;
  }

  public void setAction_type_id(String action_type_id) {
    this.action_type_id = action_type_id;
  }

  public String getAction_type_name() {
    return action_type_name;
  }

  public void setAction_type_name(String action_type_name) {
    this.action_type_name = action_type_name;
  }

  public String getIs_undo() {
    return is_undo;
  }

  public void setIs_undo(String is_undo) {
    this.is_undo = is_undo;
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

  public String getInit_money() {
    return init_money;
  }

  public void setInit_money(String init_money) {
    this.init_money = init_money;
  }

  public String getOperation_date() {
    return operation_date;
  }

  public void setOperation_date(String operation_date) {
    this.operation_date = operation_date;
  }

  public String getOperation_name() {
    return operation_name;
  }

  public void setOperation_name(String operation_name) {
    this.operation_name = operation_name;
  }

  public String getOperation_remark() {
    return operation_remark;
  }

  public void setOperation_remark(String operation_remark) {
    this.operation_remark = operation_remark;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
    if (remark != null && !remark.toString().equals("")) {
      try {
        this.voucher_dto.copy(ParseXML.convertXmlToObj(remark.toString()));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public String getResult_money() {
    return result_money;
  }

  public void setResult_money(String result_money) {
    this.result_money = result_money;
  }

  public String getUndo_date() {
    return undo_date;
  }

  public void setUndo_date(String undo_date) {
    this.undo_date = undo_date;
  }

  public String getUndo_user() {
    return undo_user;
  }

  public void setUndo_user(String undo_user) {
    this.undo_user = undo_user;
  }

  public FVoucherDTO getVoucher_dto() {
    return voucher_dto;
  }

  public void setVoucher_dto(FVoucherDTO voucher_dto) {
    this.voucher_dto = voucher_dto;
  }

  public String getStatus_code() {
    return status_code;
  }

  public void setStatus_code(String status_code) {
    this.status_code = status_code;
  }

  public String getStatus_name() {
    return status_name;
  }

  public void setStatus_name(String status_name) {
    this.status_name = status_name;
  }

  public String getModule_id() {
    return module_id;
  }

  public void setModule_id(String module_id) {
    this.module_id = module_id;
  }

  public String getModule_name() {
    return module_name;
  }

  public void setModule_name(String module_name) {
    this.module_name = module_name;
  }

  public String getNum() {
    return num;
  }

  public void setNum(String num) {
    this.num = num;
  }

  public String getNode_type() {
    return node_type;
  }

  public void setNode_type(String node_type) {
    this.node_type = node_type;
  }

  public String getNode_id() {
    return node_id;
  }

  public void setNode_id(String node_id) {
    this.node_id = node_id;
  }

  public String getNode_name() {
    return node_name;
  }

  public void setNode_name(String node_name) {
    this.node_name = node_name;
  }
  
  

  public String getNode_code() {
    return node_code;
  }

  public void setNode_code(String node_code) {
    this.node_code = node_code;
  }

  public String getMenu_id() {
    return menu_id;
  }

  public void setMenu_id(String menu_id) {
    this.menu_id = menu_id;
  }

  public String getMenu_name() {
    return menu_name;
  }

  public void setMenu_name(String menu_name) {
    this.menu_name = menu_name;
  }

  public String getDetailNum() {
    return detailNum;
  }

  public void setDetailNum(String detailNum) {
    this.detailNum = detailNum;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getUser_code() {
    return user_code;
  }

  public void setUser_code(String user_code) {
    this.user_code = user_code;
  }

  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }

  public String getURL() {
    return URL;
  }

  public void setURL(String uRL) {
    URL = uRL;
  }

}
