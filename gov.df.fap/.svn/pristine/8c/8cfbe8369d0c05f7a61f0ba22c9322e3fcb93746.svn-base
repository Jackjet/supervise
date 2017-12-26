package gov.df.fap.bean.gl.dto;

import gov.df.fap.util.StringUtil;



public class Order {

	public static final String asc = "asc";
	public static final String desc = "desc";
	
	String field = null;
	boolean ascending = true;
	
	public Order(String field, boolean ascending){
		this.ascending = ascending;
		this.field = field;
	}

	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	public String getStatement(){
		return getStatement(null);
	}
	
	public String getStatement(String alias){
		if (StringUtil.isEmpty(alias))
			return field+(ascending?" asc ":" desc ");
		else 
			return alias+"."+field+(ascending?" asc ":" desc ");
	}
}
