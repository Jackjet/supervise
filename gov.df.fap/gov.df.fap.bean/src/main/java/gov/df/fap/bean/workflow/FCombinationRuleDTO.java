/*
 * @(#)FVoucherDTO	1.0 09/06/06
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.bean.workflow;

import gov.df.fap.bean.util.DTO;

import java.util.List;


/**
 *合单规则数据传输DTO对象
 * @version 1.0
 * @author 
 * @since 
 */
public class FCombinationRuleDTO extends DTO
{	
    public static final long serialVersionUID = -1L;
    //交易凭证类型ID
	private String billtype_id = "";
	//交易凭证类型Code
	private String billtype_code = "";
	//交易凭证类型名称
	private String billtype_name = "";
	//编号规则
	private List ruleMap = null;
	public List getRuleMap()
	{
		return ruleMap;
	}
	public void setRuleMap(List ruleMap)
	{
		this.ruleMap = ruleMap;
	}
	public String getBilltype_code()
	{
		return billtype_code;
	}
	public void setBilltype_code(String billtype_code)
	{
		this.billtype_code = billtype_code;
	}
	public String getBilltype_id()
	{
		return billtype_id;
	}
	public void setBilltype_id(String billtype_id)
	{
		this.billtype_id = billtype_id;
	}
	public String getBilltype_name()
	{
		return billtype_name;
	}
	public void setBilltype_name(String billtype_name)
	{
		this.billtype_name = billtype_name;
	}
	
}
