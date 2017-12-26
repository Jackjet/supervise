package gov.df.fap.service.gl.balance.saver;

import gov.df.fap.api.gl.balance.PeriodBalanceSaver;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.service.gl.balance.BalanceSaverFactory;

/**
 * for the reset fromctrlid saver
 * @author lwq
 *
 */
public class ResetFromctrlidSaverFactory implements BalanceSaverFactory {

	private BalanceSaverFactory delegate = null;
	
	public ResetFromctrlidSaverFactory(BalanceSaverFactory balanceFactory){
		this.delegate = balanceFactory;
	}
	
	public PeriodBalanceSaver newSaverInstance(BusVouAccount account) {
		PeriodBalanceSaver saver = delegate.newSaverInstance(account);
		if (saver instanceof AbstractPeriodBalanceSaver)
			return new ResetFromctrlidSaver((AbstractPeriodBalanceSaver)saver);
		else
			throw new RuntimeException("BalanceSaverFactory can not get instance of AbstractBalanceSaver!");
	}

}
