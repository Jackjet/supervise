package gov.df.fap.service.gl.balance;

import gov.df.fap.bean.gl.configure.BusVouAcctmdl;
import gov.df.fap.bean.gl.dto.CtrlRecordDTO;
import gov.df.fap.bean.gl.dto.JournalDTO;
import gov.df.fap.service.gl.coa.impl.BatchCcidTransProcesser;

/**
 * 额度匹配器,通过日志与记账模板配置匹配出临时额度对象,主要是匹配出额度所使用的
 * ccid,目前只支持批量匹配,所以要传入CcidGenProcess对象.
 * @author 
 *
 */
public interface BalanceMatcher {

	/**
	 * 匹配出临时额度对象.
	 * @param journal
	 * @param account
	 * @param processer
	 * @return
	 */
	public CtrlRecordDTO matchBalance(JournalDTO journal ,BusVouAcctmdl account, BatchCcidTransProcesser processer);
	
}
