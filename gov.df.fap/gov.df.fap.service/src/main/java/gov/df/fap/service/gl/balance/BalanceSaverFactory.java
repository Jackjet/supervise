package gov.df.fap.service.gl.balance;

import gov.df.fap.api.gl.balance.PeriodBalanceSaver;
import gov.df.fap.bean.gl.configure.BusVouAccount;


/**
 * 额度保存方式工厂
 * @author
 *
 */
public interface BalanceSaverFactory {

	/**
	 * get the balance saver implemence;
	 *
	 */
	public PeriodBalanceSaver newSaverInstance(BusVouAccount account);
	
}
