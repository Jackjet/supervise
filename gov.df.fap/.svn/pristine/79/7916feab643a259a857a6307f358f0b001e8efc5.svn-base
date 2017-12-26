/**
 * <p>Copyright 2007 by Digiark Software corporation,
 * All rights reserved.
 * <p>版权所有：用友政务软件有限公司
 * <p>未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 * <p>侵权者将受到法律追究。
 */
package gov.df.fap.bean.gl.dto;

import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.date.DateHandler;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 
 * @version 
 */
public class JournalDTO implements Serializable {

  /*日志ID*/
  private String journal_id;

  /*业务年度*/
  private int set_year;

  /*区域码*/
  private String rg_code;

  /*业务明细ID*/
  private String vou_id;

  /**业务明细code*/
  private String vou_no;

  /*记账模板类型ID*/
  private long vou_type_id;

  /*交易凭证类型ID*/
  private String billtype_code;

  /*金额*/
  private BigDecimal vou_money;

  /*业务月份*/
  private int set_month;

  /*要素组合*/
  private long ccid;

  /*权限组合*/
  private String rcid;

  /*状态*/
  private int is_end;

  /*作废标识*/
  private int is_valid;

  /*操作ID*/
  private long action_id;

  /*业务发生日期*/
  private String bill_date;

  /*备注*/
  private String remark;

  /*指标版本*/
  private String bal_version;

  /*创建日期*/
  private String create_date;

  /*最后修改日期*/
  private String latest_op_date;

  /*最后版本*/
  private String last_ver;

  /*权限ID*/
  private String rule_id;

  /*索引值*/
  private int index_;

  /*主来源额度ID*/
  private String primarySourceId;

  /*主来源科目类型*/
  private String primarySourceAccountId;

  /*主去向额度ID*/
  private String primaryTargetId;

  /*去向科目类型*/
  private String primaryTargetAccountId;

  /**控制级别*/
  private int ctrlLevel;

  private String agency_id = "";

  private String mb_id = "";

  public int getCtrlLevel() {
    return ctrlLevel;
  }

  public void setCtrlLevel(int ctrlLevel) {
    this.ctrlLevel = ctrlLevel;
  }

  public JournalDTO() {

  }

  public JournalDTO(FVoucherDTO voucherDTO) {
    init(voucherDTO);
  }

  public void init() {
    journal_id = null;
    set_year = 0;
    rg_code = null;
    vou_id = null;
    vou_no = null;
    vou_type_id = 0;
    billtype_code = null;
    vou_money = null;
    set_month = 0;
    ccid = 0;
    rcid = null;
    is_end = 0;
    is_valid = 0;
    action_id = 0;
    bill_date = null;
    remark = null;
    bal_version = null;
    create_date = null;
    latest_op_date = null;
    last_ver = null;
    rule_id = null;
    index_ = 0;
    primarySourceId = null;
    primarySourceAccountId = null;
    primaryTargetId = null;
    primaryTargetAccountId = null;
    ctrlLevel = 0;
  }

  public void init(FVoucherDTO voucherDTO) {
    this.set_year = voucherDTO.getSet_year();
    this.vou_id = voucherDTO.getVou_id();
    this.vou_type_id = 0;
    this.billtype_code = voucherDTO.getBilltype_code();
    this.vou_money = new BigDecimal(voucherDTO.getVou_money());
    this.set_month = voucherDTO.getSet_month();
    if (set_month < 0 || set_month > 12)
      set_month = DateHandler.getCurrentMonth();
    this.ccid = Long.valueOf(voucherDTO.getCcid()).longValue();
    this.rcid = voucherDTO.getRcid();
    this.is_end = voucherDTO.getIs_end();
    this.is_valid = voucherDTO.getIs_valid();
    this.bill_date = voucherDTO.getBill_date();
    this.remark = voucherDTO.getRemark() == null ? StringUtil.EMPTY : voucherDTO.getRemark();
    this.bal_version = String.valueOf(voucherDTO.getBal_version());
    this.create_date = DateHandler.getDateFromNow(0);
    this.latest_op_date = DateHandler.getDateFromNow(0);
    this.last_ver = DateHandler.getLastVerTime();
    this.primarySourceId = voucherDTO.getFromctrlid();
    this.primarySourceAccountId = voucherDTO.getFromctrltype();
    if (StringUtil.isEmpty(voucherDTO.getCtrllevel()))
      this.ctrlLevel = 2;
    else
      this.ctrlLevel = Integer.parseInt(voucherDTO.getCtrllevel());
    this.primaryTargetId = voucherDTO.getToctrlid();
    this.primaryTargetAccountId = voucherDTO.getToctrltype();
    this.agency_id = voucherDTO.getAgency_id();
    this.mb_id = voucherDTO.getMb_id();
  }

  public String getJournal_id() {
    return this.journal_id;
  }

  public void setJournal_id(String journal_id) {
    this.journal_id = journal_id;
  }

  public int getSet_year() {
    return this.set_year;
  }

  public void setSet_year(int set_year) {
    this.set_year = set_year;
  }

  public String getRg_code() {
    return this.rg_code;
  }

  public void setRg_code(String rg_code) {
    this.rg_code = rg_code;
  }

  public String getVou_id() {
    return this.vou_id;
  }

  public void setVou_id(String vou_id) {
    this.vou_id = vou_id;
  }

  public long getVou_type_id() {
    return this.vou_type_id;
  }

  public void setVou_type_id(long vou_type_id) {
    this.vou_type_id = vou_type_id;
  }

  public BigDecimal getVou_money() {
    return this.vou_money;
  }

  public void setVou_money(BigDecimal vou_money) {
    this.vou_money = vou_money;
  }

  public int getSet_month() {
    return this.set_month;
  }

  public void setSet_month(int set_month) {
    this.set_month = set_month;
  }

  public long getCcid() {
    return ccid;
  }

  public void setCcid(long ccid) {
    this.ccid = ccid;
  }

  public String getRcid() {
    return this.rcid;
  }

  public void setRcid(String rcid) {
    this.rcid = rcid;
  }

  public int getIs_end() {
    return this.is_end;
  }

  public void setIs_end(int is_end) {
    this.is_end = is_end;
  }

  public int getIs_valid() {
    return this.is_valid;
  }

  public void setIs_valid(int is_valid) {
    this.is_valid = is_valid;
  }

  public long getAction_id() {
    return this.action_id;
  }

  public void setAction_id(long action_id) {
    this.action_id = action_id;
  }

  public String getBill_date() {
    return this.bill_date;
  }

  public void setBill_date(String bill_date) {
    this.bill_date = bill_date;
  }

  public String getRemark() {
    return this.remark;
  }

  public void setRemark(String remark) {
    if (remark == null)
      this.remark = "";
    else
      this.remark = remark;
  }

  public String getBal_version() {
    return this.bal_version;
  }

  public void setBal_version(String bal_version) {
    this.bal_version = bal_version;
  }

  public String getCreate_date() {
    return this.create_date;
  }

  public void setRCreate_date(String create_date) {
    this.create_date = create_date;
  }

  public String getLatest_op_date() {
    return this.latest_op_date;
  }

  public void setLatest_op_date(String latest_op_date) {
    this.latest_op_date = latest_op_date;
  }

  public String getLast_ver() {
    return this.last_ver;
  }

  public void setLast_ver(String last_ver) {
    this.last_ver = last_ver;
  }

  public String getRule_id() {
    return this.rule_id;
  }

  public void setRule_id(String rule_id) {
    this.rule_id = rule_id;
  }

  public int getIndex_() {
    return this.index_;
  }

  public void setIndex_(int index_) {
    this.index_ = index_;
  }

  public String getVou_no() {
    return vou_no;
  }

  public void setVou_no(String vou_no) {
    this.vou_no = vou_no;
  }

  public String getBilltype_code() {
    return billtype_code;
  }

  public void setBilltype_code(String billtype_id) {
    this.billtype_code = billtype_id;
  }

  public String getPrimarySourceAccountId() {
    return primarySourceAccountId;
  }

  public void setPrimarySourceAccountId(String primarySourceAccountId) {
    this.primarySourceAccountId = primarySourceAccountId;
  }

  public String getPrimaryTargetAccountId() {
    return primaryTargetAccountId;
  }

  public void setPrimaryTargetAccountId(String primaryTargetAccountId) {
    this.primaryTargetAccountId = primaryTargetAccountId;
  }

  public String getPrimarySourceId() {
    return primarySourceId;
  }

  public void setPrimarySourceId(String primarySourceId) {
    this.primarySourceId = primarySourceId;
  }

  public String getPrimaryTargetId() {
    return primaryTargetId;
  }

  public void setPrimaryTargetId(String primaryTargetId) {
    this.primaryTargetId = primaryTargetId;
  }

  public String getAgency_id() {
    return agency_id;
  }

  public void setAgency_id(String agency_id) {
    this.agency_id = agency_id;
  }

  public String getMb_id() {
    return mb_id;
  }

  public void setMb_id(String mb_id) {
    this.mb_id = mb_id;
  }

}
