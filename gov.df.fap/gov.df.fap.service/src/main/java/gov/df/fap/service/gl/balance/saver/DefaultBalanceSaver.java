package gov.df.fap.service.gl.balance.saver;

import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.service.gl.balance.IBalanceDao;

import java.sql.SQLException;
import java.util.List;

/**
 * 默认科目余额保存
 * @author 
 *
 */
public class DefaultBalanceSaver extends AbstractPeriodBalanceSaver {

  public DefaultBalanceSaver(ICoaService coaService, BusVouAccount account, IBalanceDao dao) {
    super(coaService, account, dao);
  }

  /**
   * 保存额度
   */
  public void doSupplementZeroBalance() {
    dao.saveCommonBalanceWithCache(account);
  }

  /**
   * 关联额度表与临时额度表校验额度.
 * @throws SQLException 
   */
  public void doValidBalance(List balances) {
    // 关联额度表与临时额度表校验额度.
    List resultList = dao.loadMarkingMonthBalance(account);
    if (resultList.size() > 0)
      throwsIllegalBalanceException(balances, resultList);
  }

  /**
   * 更新额度
 * @throws SQLException 
   */
  public void doUpdateBalanceWithCache() {
    dao.updateCommonBalanceMoneyWithCache(account);
  }

  public void closeMonth(int setYear, int month) {
    //do nothing, 普通科目不进行月结.
  }
}
