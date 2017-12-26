package gov.df.fap.service.gl.balance.saver;

import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.service.gl.balance.IBalanceDao;

import java.util.List;

/**
 * 累计月保存
 * @author 
 *
 */
public class SumMonthBalanceSaver extends AbstractPeriodBalanceSaver {

  public SumMonthBalanceSaver(ICoaService coaService, BusVouAccount account, IBalanceDao dao) {
    super(coaService, account, dao);
  }

  /**
   * 新增零额度,新增零明细额度
   */
  public void doSupplementZeroBalance() {
    dao.saveSumMonthBalanceWithCache(account);
    if (log.isDebugEnabled())
      log.debug("新增0明细额度,明细额度表:" + account.getMonthDetailTableName());
    dao.saveSumMonthBalanceDetailWithCache(account);
  }

  /**
   * 校验额度合法性,对于累计月额度,分两部分校验,一是校验以前月额度的合法性,一是
   * 校验未来月的合法性.
   */
  public void doValidBalance(List balances) {
    List resultList = null;
    //关联额度表与临时额度表校验额度,需要汇总
    resultList = dao.loadCalculateBeforeMonthBalance(account);
    if (resultList.size() > 0)
      throwsIllegalBalanceException(balances, resultList);

    resultList = dao.loadCalculateAfterMonthBalance(account);
    if (resultList.size() > 0)
      throwsIllegalBalanceException(balances, resultList);

  }

  /**
   * 更新额度,更新明细额度
   */
  public void doUpdateBalanceWithCache() {
    dao.updateSumMonthBalanceMoneyWithCache(account);
    if (log.isDebugEnabled())
      log.debug("关联临时额度表更新明细额度金额");
    dao.updateSumMonthBalanceDetailMoneyWithCache(account);
  }

  /**
   * 执行月结,结束某月,将下月的额度明细加到正式额度表中.
   * @param month 被结束的月份.
   */
  public void closeMonth(int setYear, int month){
    dao.insertBalanceNotExists(account, month + 1);
    dao.clearBalanceMoney(account);
    dao.updateBalanceByDetail(account, setYear, month + 1);
    dao.updateBalanceMonth(account, month + 1);
  }
}
