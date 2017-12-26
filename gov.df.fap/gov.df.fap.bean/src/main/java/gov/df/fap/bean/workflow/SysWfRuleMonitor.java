package gov.df.fap.bean.workflow;

import java.io.Serializable;

public class SysWfRuleMonitor implements Serializable {

	private static final long serialVersionUID = 3589564170486269777L;
	
	private String LINE_RULE_ID;
	
	private String CLASS_NAME;
	
	private String PARA_NAME;
	
	private String LAST_VER;
	
	private int SET_YEAR;
	
	private String RG_CODE;
	

	
	public String getCLASS_NAME() {
		return CLASS_NAME;
	}

	public void setCLASS_NAME(String cLASS_NAME) {
		CLASS_NAME = cLASS_NAME;
	}

	public String getPARA_NAME() {
		return PARA_NAME;
	}

	public void setPARA_NAME(String pARA_NAME) {
		PARA_NAME = pARA_NAME;
	}

	public String getLINE_RULE_ID() {
		return LINE_RULE_ID;
	}

	public void setLINE_RULE_ID(String lINE_RULE_ID) {
		LINE_RULE_ID = lINE_RULE_ID;
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

	public SysWfRuleMonitor() {
	}

	@Override
	public String toString() {
		return "SysWfRuleMonitor [LINE_RULE_ID=" + LINE_RULE_ID
				+ ", CLASS_NAME=" + CLASS_NAME + ", PARA_NAME=" + PARA_NAME
				+ ", LAST_VER=" + LAST_VER + ", SET_YEAR=" + SET_YEAR
				+ ", RG_CODE=" + RG_CODE + "]";
	}

}
