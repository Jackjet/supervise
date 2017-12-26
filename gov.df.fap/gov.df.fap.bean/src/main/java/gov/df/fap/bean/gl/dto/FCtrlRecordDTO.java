/*
 * @(#)FCtrlRecordDTO	1.0 09/06/06
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.bean.gl.dto;

import gov.df.fap.bean.util.FBaseDTO;

/**
 * 控制表(额度表)数据传输DTO对象
 * @version 1.0
 * @author 
 * @since java 1.6
 */
public class FCtrlRecordDTO extends FBaseDTO
{
	public static final long serialVersionUID = -1L;
	
	private String balance_id = "";
	//控制额度ID
    private String sum_id = "";
    //控制类型ID
    private String account_id = "";
    //控制类型code
    private String account_code = "";
    //创建时间
    private String create_date = "";
    //创建用户
    private String create_user = "";
    //最后修改时间
    private String latest_op_date = "";
    //最后修改用户
    private String latest_op_user = "";
    //月份
    private int set_month = 0;
    //生效金额=originalbal
    private String avi_money = "0";   
    //已申请金额 =originalbal-curbal
    private String use_money = "0";  
    //在途调减金额 =endbal-curbal
    private String minus_money = "0";
    //在途调增金额＝0
    private String aving_money = "0"; 
    //可用金额 =curbal;
    private String canuse_money = "0" ;
    //最后版本
    private String last_ver = "";
    //来源额度
    private String fromctrlid = "";
    //额度版本号
    private int bal_version = 1;
    /*区域码*/
    private String rg_code;
    
    private String remark;
    
    public String getRg_code() {
		return rg_code;
	}

	public void setRg_code(String rg_code) {
		this.rg_code = rg_code;
	}

	public FCtrlRecordDTO(){
    }

	public String getAvi_money() {
		return avi_money;
	}

	public void setAvi_money(String avi_money) {
		this.avi_money = avi_money;
	}

	public String getAving_money() {
		return aving_money;
	}

	public void setAving_money(String aving_money) {
		this.aving_money = aving_money;
	}

	public int getBal_version() {
		return bal_version;
	}

	public void setBal_version(int bal_version) {
		this.bal_version = bal_version;
	}

	public String getCanuse_money() {
		return canuse_money;
	}

	public void setCanuse_money(String canuse_money) {
		this.canuse_money = canuse_money;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getFromctrlid() {
		return fromctrlid;
	}

	public void setFromctrlid(String fromctrlid) {
		this.fromctrlid = fromctrlid;
	}

	public String getLast_ver() {
		return last_ver;
	}

	public void setLast_ver(String last_ver) {
		this.last_ver = last_ver;
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

	public String getMinus_money() {
		return minus_money;
	}

	public void setMinus_money(String minus_money) {
		this.minus_money = minus_money;
	}

	public int getSet_month() {
		return set_month;
	}

	public void setSet_month(int set_month) {
		this.set_month = set_month;
	}

	public String getSum_id() {
		return sum_id;
	}

	public void setSum_id(String sum_id) {
		this.sum_id = sum_id;
	}

	public String getUse_money() {
		return use_money;
	}

	public void setUse_money(String use_money) {
		this.use_money = use_money;
	}

	public String getBalance_id() {
		return balance_id;
	}

	public void setBalance_id(String balance_id) {
		this.balance_id = balance_id;
	}

	public String getAccount_code() {
		return account_code;
	}

	public void setAccount_code(String account_code) {
		this.account_code = account_code;
	}

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
    

}
