package gov.df.fap.bean.gl.configure;

import gov.df.fap.util.StringUtil;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;



public class AccountEntry implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6890269993113268299L;

	private String entryCode = StringUtil.EMPTY;
	
	private String entryName = StringUtil.EMPTY;
	
	private List acctmdlList = new LinkedList();

	public List getAcctmdlList() {
		return acctmdlList;
	}

	/**
	 * 设置分录和acctmdl关联关系,并设置acctmdl中accountEntry属性
	 * @param acctmdlList
	 */
	public void setAcctmdlList(List acctmdlList) {
		BusVouAcctmdl acctmdl = null;
		this.acctmdlList = acctmdlList;	
		//重新设置acctmdl集合的entry信息
		Iterator iterator = acctmdlList.iterator();
		while (iterator.hasNext()) {
			acctmdl = (BusVouAcctmdl) iterator.next();
			acctmdl.setAccountEntry(this);
		}
			
	}
	
	/**
	 * 删除分录和acctmdl关联关系
	 */
	public void clearRelation() {
		Iterator iterator = null;
		BusVouAcctmdl acctmdl = null;
		iterator = this.getAcctmdlList().iterator();
		//将原来会计分录中acctmdl的entry信息清空
		while (iterator.hasNext()) {
			acctmdl = (BusVouAcctmdl) iterator.next();
			acctmdl.setAccountEntry(null);
		}
		this.getAcctmdlList().clear();
	}

	public String getEntryCode() {
		return entryCode;
	}

	public void setEntryCode(String entryCode) {
		this.entryCode = entryCode;
	}

	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}
}
