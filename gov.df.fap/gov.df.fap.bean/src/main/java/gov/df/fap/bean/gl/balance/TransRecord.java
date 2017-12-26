package gov.df.fap.bean.gl.balance;

import gov.df.fap.bean.gl.dto.CtrlRecordDTO;

/**
 * 转换记录
 * @author lwq
 *
 */
public class TransRecord {

	public CtrlRecordDTO positiveBalance = null;
	public CtrlRecordDTO negativeBalance = null;
	
	/**
	 * 构造方法,生成转换记录
	 * @param positiveBalance
	 * @param negativeBalance
	 */
	public TransRecord(CtrlRecordDTO positiveBalance, CtrlRecordDTO negativeBalance){
		this.positiveBalance = positiveBalance;
		this.negativeBalance = negativeBalance;
	}

	public CtrlRecordDTO getNegativeBalance() {
		return negativeBalance;
	}

	public void setNegativeBalance(CtrlRecordDTO negativeBalance) {
		this.negativeBalance = negativeBalance;
	}

	public CtrlRecordDTO getPositiveBalance() {
		return positiveBalance;
	}

	public void setPositiveBalance(CtrlRecordDTO positiveBalance) {
		this.positiveBalance = positiveBalance;
	}
	
}
