/*
 * @(#)FVoucherDTO	1.0 09/06/06
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.bean.rule.dto;

import gov.df.fap.bean.util.DTO;

/**
 * 工作流日志数据传输DTO对象
 * 
 * @version 1.0
 * @author 
 * @since 
 * @de
 */
public class FRightSqlDTO extends DTO {
	public static final long serialVersionUID = -1L;

	// 任务ID
	private String table_clause = "";

	// 流程ID
	private String where_clause = "";
	
	private String user_id_by_org = "";
	
	private boolean org_right_flag = false;
	
	private boolean data_right_flag = false;
	
	private String user_id_by_right = "";
	
	private String role_id_by_right = "";


	public String getTable_clause() {
		return table_clause;
	}

	public void setTable_clause(String table_clause) {
		this.table_clause = table_clause;
	}

	public String getWhere_clause() {
		return where_clause;
	}

	public void setWhere_clause(String where_clause) {
		this.where_clause = where_clause;
	}

	public void addWhere_clause(String where_clause) {
		this.where_clause =this.where_clause+ where_clause;
	}	
	public void addTable_clause(String table_clause) {
		this.table_clause = this.table_clause+table_clause;
	}

	public String getRole_id_by_right() {
		return role_id_by_right;
	}

	public void setRole_id_by_right(String role_id_by_right) {
		this.role_id_by_right = role_id_by_right;
	}

	public String getUser_id_by_org() {
		return user_id_by_org;
	}

	public void setUser_id_by_org(String user_id_by_org) {
		this.user_id_by_org = user_id_by_org;
	}

	public String getUser_id_by_right() {
		return user_id_by_right;
	}

	public void setUser_id_by_right(String user_id_by_right) {
		this.user_id_by_right = user_id_by_right;
	}

	public boolean isData_right_flag() {
		return data_right_flag;
	}

	public void setData_right_flag(boolean data_right_flag) {
		this.data_right_flag = data_right_flag;
	}

	public boolean isOrg_right_flag() {
		return org_right_flag;
	}

	public void setOrg_right_flag(boolean org_right_flag) {
		this.org_right_flag = org_right_flag;
	}
}
