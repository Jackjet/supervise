/*
 * @(#)FBillTypeDTO	1.0 09/06/06
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.bean.workflow;

import gov.df.fap.bean.util.FBaseDTO;
import gov.df.fap.util.xml.XMLData;

/**
 * 交易凭证数据传输DTO对象
 * 
 * @version 1.0
 * @author 
 * @version 2017-03-06r
 * @since 
 */
public class FBillTypeDTO extends FBaseDTO {
  public static final long serialVersionUID = -1L;

  // 交易凭证ID
  private String billtype_id = "";

  // 交易凭证编码
  private String billtype_code = "";

  // 交易凭证名称
  private String billtype_name = "";

  // 交易凭证业务增减标志
  private int is_busincrease = -9;

  // 交易令ID
  private String busvou_type_id = "";

  // 编码规则ID
  private String billnorule_id = "";

  // 交易凭证类型
  private int billtype_class = -9;

  // 交易凭证启用标示
  private int enabled = -9;

  // 最后操作日期
  private String latest_op_date = "";

  // 最后操作人
  private String latest_op_user = "";

  // 年度
  private int set_year = 2006;

  // 交易凭证对应表
  private String table_name = "";

  private String is_needchecknobudget = "";

  private String nobudgetbusvou_type_id = "";

  /** 2007年8月3日 add by 黄节 start* */
  private String sys_id;

  private String field_name;

  private String ui_id;

  private String from_billtype_id;

  private String from_ui_id;

  private String to_billtype_id;

  private String to_ui_id;

  private String vou_control_id;

  private String rg_code;

  /** 2007年8月3日 add by 黄节 end* */
  // coa明细信息
  private XMLData coaMap = null;

  public String getIs_needchecknobudget() {
    return is_needchecknobudget;
  }

  public void setIs_needchecknobudget(String is_needchecknobudget) {
    this.is_needchecknobudget = is_needchecknobudget;
  }

  public String getNobudgetbusvou_type_id() {
    return nobudgetbusvou_type_id;
  }

  public void setNobudgetbusvou_type_id(String nobudgetbusvou_type_id) {
    this.nobudgetbusvou_type_id = nobudgetbusvou_type_id;
  }

  public String getTable_name() {
    return table_name;
  }

  public void setTable_name(String table_name) {
    this.table_name = table_name;
  }

  public String getBillnorule_id() {
    return billnorule_id;
  }

  public void setBillnorule_id(String billnorule_id) {
    this.billnorule_id = billnorule_id;
  }

  public int getBilltype_class() {
    return billtype_class;
  }

  public void setBilltype_class(int billtype_class) {
    this.billtype_class = billtype_class;
  }

  public String getBilltype_code() {
    return billtype_code;
  }

  public void setBilltype_code(String billtype_code) {
    this.billtype_code = billtype_code;
  }

  public String getBilltype_id() {
    return billtype_id;
  }

  public void setBilltype_id(String billtype_id) {
    this.billtype_id = billtype_id;
  }

  public String getBilltype_name() {
    return billtype_name;
  }

  public void setBilltype_name(String billtype_name) {
    this.billtype_name = billtype_name;
  }

  public String getBusvou_type_id() {
    return busvou_type_id;
  }

  public void setBusvou_type_id(String busvou_type_id) {
    this.busvou_type_id = busvou_type_id;
  }

  public int getEnabled() {
    return enabled;
  }

  public void setEnabled(int enabled) {
    this.enabled = enabled;
  }

  public int getIs_busincrease() {
    return is_busincrease;
  }

  public void setIs_busincrease(int is_busincrease) {
    this.is_busincrease = is_busincrease;
  }

  public String getLatest_op_date() {
    return latest_op_date;
  }

  public void setLatest_op_date(String latest_op_date) {
    this.latest_op_date = latest_op_date;
  }

  public String getLatest_op_user() {
    return latest_op_user;
  }

  public void setLatest_op_user(String latest_op_user) {
    this.latest_op_user = latest_op_user;
  }

  public int getSet_year() {
    return set_year;
  }

  public void setSet_year(int set_year) {
    this.set_year = set_year;
  }

  public XMLData getCoaMap() {
    return coaMap;
  }

  public void setCoaMap(XMLData coaMap) {
    this.coaMap = coaMap;
  }

  /**
       * @return field_name
       */
  public String getField_name() {
    return field_name;
  }

  /**
       * @param field_name
       *                要设置的 field_name
       */
  public void setField_name(String field_name) {
    this.field_name = field_name;
  }

  /**
       * @return from_billtype_id
       */
  public String getFrom_billtype_id() {
    return from_billtype_id;
  }

  /**
       * @param from_billtype_id
       *                要设置的 from_billtype_id
       */
  public void setFrom_billtype_id(String from_billtype_id) {
    this.from_billtype_id = from_billtype_id;
  }

  /**
       * @return from_ui_id
       */
  public String getFrom_ui_id() {
    return from_ui_id;
  }

  /**
       * @param from_ui_id
       *                要设置的 from_ui_id
       */
  public void setFrom_ui_id(String from_ui_id) {
    this.from_ui_id = from_ui_id;
  }

  /**
       * @return sys_id
       */
  public String getSys_id() {
    return sys_id;
  }

  /**
       * @param sys_id
       *                要设置的 sys_id
       */
  public void setSys_id(String sys_id) {
    this.sys_id = sys_id;
  }

  /**
       * @return to_billtype_id
       */
  public String getTo_billtype_id() {
    return to_billtype_id;
  }

  /**
       * @param to_billtype_id
       *                要设置的 to_billtype_id
       */
  public void setTo_billtype_id(String to_billtype_id) {
    this.to_billtype_id = to_billtype_id;
  }

  /**
       * @return to_ui_id
       */
  public String getTo_ui_id() {
    return to_ui_id;
  }

  /**
       * @param to_ui_id
       *                要设置的 to_ui_id
       */
  public void setTo_ui_id(String to_ui_id) {
    this.to_ui_id = to_ui_id;
  }

  /**
       * @return ui_id
       */
  public String getUi_id() {
    return ui_id;
  }

  /**
       * @param ui_id
       *                要设置的 ui_id
       */
  public void setUi_id(String ui_id) {
    this.ui_id = ui_id;
  }

  /**
       * @return vou_control_id
       */
  public String getVou_control_id() {
    return vou_control_id;
  }

  /**
       * @param vou_control_id
       *                要设置的 vou_control_id
       */
  public void setVou_control_id(String vou_control_id) {
    this.vou_control_id = vou_control_id;
  }

  //add by liuzw 20120327
  public String getRg_code() {
    return rg_code;
  }

  public void setRg_code(String rg_code) {
    this.rg_code = rg_code;
  }
  // public String getCoa_id() {
  // return coa_id;
  // }
  //
  // public void setCoa_id(String coa_id) {
  // this.coa_id = coa_id;
  // }
}
