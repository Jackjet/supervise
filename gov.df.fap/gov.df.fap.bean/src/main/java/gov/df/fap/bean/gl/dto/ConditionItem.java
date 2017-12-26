package gov.df.fap.bean.gl.dto;

import gov.df.fap.bean.gl.core.sqlgen.SqlGenerator;

import java.io.Serializable;


public class ConditionItem implements IConditionItem,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String andOR;
	private String operation;
	private String field;
	private String value;

	/**是否严格按照条件生成SQL,默认否,true的话会*/
	private boolean strictGenerate = false;
	
	private SqlGenerator customerSqlGen = null;
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getAndOR() {
		return andOR;
	}
	public void setAndOR(String andOR) {
		this.andOR = andOR;
	}
	public String getConnectOper() {
		return getAndOR();
	}
	public SqlGenerator getCustomerSqlGen() {
		return customerSqlGen;
	}
	public void setCustomerSqlGen(SqlGenerator customerSqlGen) {
		this.customerSqlGen = customerSqlGen;
	}
	public boolean isStrictGenerate() {
		return strictGenerate;
	}
	public void setStrictGenerate(boolean strictGenerate) {
		this.strictGenerate = strictGenerate;
	}
}
