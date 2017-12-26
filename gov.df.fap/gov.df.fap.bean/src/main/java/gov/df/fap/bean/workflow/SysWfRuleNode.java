package gov.df.fap.bean.workflow;

import java.io.Serializable;

public class SysWfRuleNode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3581566793487572887L;
	
	private String NODE_ID;
	
	private String RULE_ID;
	
	private String LAST_VER;
	
	private int SET_YEAR;
	
	private String RG_CODE;

	public String getNODE_ID() {
		return NODE_ID;
	}

	public void setNODE_ID(String nODE_ID) {
		NODE_ID = nODE_ID;
	}

	public String getRULE_ID() {
		return RULE_ID;
	}

	public void setRULE_ID(String rULE_ID) {
		RULE_ID = rULE_ID;
	}

	public String getLAST_VER() {
		return LAST_VER;
	}

	public void setLAST_VER(String lAST_VER) {
		LAST_VER = lAST_VER;
	}

	public int getSET_YEAR() {
		return SET_YEAR;
	}

	public void setSET_YEAR(int sET_YEAR) {
		SET_YEAR = sET_YEAR;
	}

	public String getRG_CODE() {
		return RG_CODE;
	}

	public void setRG_CODE(String rG_CODE) {
		RG_CODE = rG_CODE;
	}

	@Override
	public String toString() {
		return "SysWfRuleNode [NODE_ID=" + NODE_ID + ", RULE_ID=" + RULE_ID
				+ ", LAST_VER=" + LAST_VER + ", SET_YEAR=" + SET_YEAR
				+ ", RG_CODE=" + RG_CODE + "]";
	}
	
	public SysWfRuleNode() {
	}

}
