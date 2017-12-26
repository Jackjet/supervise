package gov.df.fap.service.gl.balance.saver;

import gov.df.fap.api.gl.balance.PeriodBalanceSaver;
import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.service.gl.balance.BalanceSaverFactory;
import gov.df.fap.service.gl.balance.IBalanceDao;

/**
 * The default implemence of interface <code>BalanceSaverFactory</code>
 * @author 
 *
 */
public class DefaultBalanceSaverFactory implements BalanceSaverFactory {

  /**Singlton*/
  protected static BalanceSaverFactory instance = null;

  /**dependency*/
  private ICoaService coaService = null;

  private IBalanceDao balanceDao = null;

  public static BalanceSaverFactory newInstance(ICoaService coa, IBalanceDao dao) {
    if (instance == null) {
      instance = new DefaultBalanceSaverFactory(coa, dao);
    }

    return instance;
  }

  protected DefaultBalanceSaverFactory(ICoaService coa, IBalanceDao dao) {
    this.coaService = coa;
    this.balanceDao = dao;
  }

  public PeriodBalanceSaver newSaverInstance(BusVouAccount account) {
    if (account.getBalancePeriodType() == BusVouAccount.BALANCE_PERIOD_TYPE_SUM_MONTH) {
      return new SumMonthBalanceSaver(coaService, account, balanceDao);
    } else {
      return new DefaultBalanceSaver(coaService, account, balanceDao);
    }
  }

}
