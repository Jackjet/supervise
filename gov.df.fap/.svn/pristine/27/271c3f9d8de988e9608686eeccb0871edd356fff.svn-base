package gov.df.fap.service.gl.coa.impl;

import gov.df.fap.util.StringUtil;

/**
 * 已修复的CCID冲突
 * @author 
 *
 */
public class FixedConflicts {

	/**CCID冲突标志*/
	public static final int CONFLICT_TYPE_CCID = 1;
	
	/**MD5冲突标志*/
	public static final int CONFLICT_TYPE_MD5 = 0;
	
	private long conflictCcid = 0;
	
	private String conflictMd5 = StringUtil.EMPTY;
	
	private int conflictType = 0;
	
	private String fixedValue = StringUtil.EMPTY;

	public long getConflictCcid() {
		return conflictCcid;
	}

	public void setConflictCcid(long conflictCcid) {
		this.conflictCcid = conflictCcid;
	}

	public String getConflictMd5() {
		return conflictMd5;
	}

	public void setConflictMd5(String conflictMd5) {
		this.conflictMd5 = conflictMd5;
	}

	public int getConflictType() {
		return conflictType;
	}

	public void setConflictType(int conflictType) {
		this.conflictType = conflictType;
	}

	public String getFixedValue() {
		return fixedValue;
	}

	public void setFixedValue(String fixedValue) {
		this.fixedValue = fixedValue;
	}
	
	public boolean equals(Object o){
		if (o == null || !(o instanceof FixedConflicts))
			return false;
		FixedConflicts f = (FixedConflicts)o;
		return f.conflictCcid == this.conflictCcid 
			  && StringUtil.equals(f.conflictMd5, this.conflictMd5)
			  && f.conflictType == this.conflictType;
	}
	
	public int hashCode(){
		return (int) (conflictType ^ conflictCcid ^ conflictMd5.hashCode());
	}
}
