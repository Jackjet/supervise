package gov.df.fap.service.gl.balance.saver;

import java.util.List;

/**
 * rewrite the doUpdateBalanceWithCache method,特殊的额度保存类，更新fromctrlid
 * @author ydz
 *
 */
public class ResetFromctrlidSaver extends AbstractPeriodBalanceSaver {

	private AbstractPeriodBalanceSaver delegate = null;
	
	public ResetFromctrlidSaver(){}
	
	public ResetFromctrlidSaver(AbstractPeriodBalanceSaver saver) {
		super(saver.getCoaService(), saver.getAccount(), saver.getDao());
		this.delegate = saver;
	}

	public void closeMonth(int setYear, int month){
		delegate.closeMonth(setYear, month);
	}

	public void doSupplementZeroBalance() {
		delegate.doSupplementZeroBalance();
	}

	public void doUpdateBalanceWithCache() {
		delegate.doUpdateBalanceWithCache();
		dao.updateFromctrlidWithCache(account);
	}

	public void doValidBalance(List balances) {
		delegate.doValidBalance(balances);
	}	
	
}

