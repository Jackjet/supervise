package gov.df.fap.service.gl.balance.saver;

import gov.df.fap.api.gl.balance.PeriodBalanceSaver;
import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.dto.CtrlRecordDTO;
import gov.df.fap.bean.util.FBaseDTO;
import gov.df.fap.service.gl.balance.IBalanceDao;
import gov.df.fap.service.util.exceptions.gl.IllegalBalanceException;
import gov.df.fap.service.util.gl.balance.BalanceUtil;
import gov.df.fap.util.StringUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 抽象的额度保存方式
 * @author
 *
 */
public abstract class AbstractPeriodBalanceSaver implements PeriodBalanceSaver {

  protected static Log log = LogFactory.getLog(AbstractPeriodBalanceSaver.class);

  protected ICoaService coaService = null;

  protected BusVouAccount account = null;

  protected IBalanceDao dao = null;

  /**
   * get the balance dao for sub class
   * @return
   */
  protected IBalanceDao getDao() {
    return dao;
  }

  /**
   * get the account for sub class.
   * @return
   */
  protected BusVouAccount getAccount() {
    return account;
  }

  /**
   * 
   * @return
   */
  protected ICoaService getCoaService() {
    return coaService;
  }

  public AbstractPeriodBalanceSaver() {
  }

  public AbstractPeriodBalanceSaver(ICoaService coaService, BusVouAccount account, IBalanceDao dao) {
    this.coaService = coaService;
    this.account = account;
    this.dao = dao;
  }

  public void saveBalance(List balances) {
    //保存额度
    doSupplementZeroBalance();
    //更新先更新额度 再检查额度是否足够
    doUpdateBalanceWithCache();
    //检查额度
    doValidBalance(balances);
  }

  /**
   * 抛出非法额度异常.
   * @param account
   * @param tempCtrlList
   * @param invalidList
   * @param propertyName
   */
  protected void throwsIllegalBalanceException(List tempCtrlList, List invalidList) {
    Iterator it = tempCtrlList.iterator();
    while (it.hasNext()) {
      final CtrlRecordDTO tempBalance = (CtrlRecordDTO) it.next();
      for (int j = 0; j < invalidList.size(); j++) {
        final CtrlRecordDTO existsBalance = (CtrlRecordDTO) invalidList.get(j);

        if (BalanceUtil.checkBalanceEquals(tempBalance, existsBalance)) {
          if (tempBalance.getIs_enforce() != 1) {
            if (tempBalance.isBalanceWillDecrease()) {
              throwBalanceException(existsBalance, tempBalance);
            } else if (tempBalance.getIs_enforce() == 3) {
              try {
                CtrlRecordDTO newDto = (CtrlRecordDTO) existsBalance.clone();

                BalanceUtil.plus(newDto, tempBalance);
                if (newDto.isBalanceWillDecrease())
                  throwBalanceException(existsBalance, tempBalance);
              } catch (CloneNotSupportedException ex) {
                throw new RuntimeException(ex);
              }
            }
            //此处校验应为求和后的值
            BalanceUtil.plus(tempBalance, existsBalance);
            if (tempBalance.checkMoneyValid() > -1) {
              throw new IllegalBalanceException("额度金额出现负数，数据异常！");
            }

            invalidList.remove(j);
            break;
          } else {
            invalidList.remove(j);
            break;
          }
        }
      }
    }
    if (invalidList.size() > 0)
      throw new RuntimeException("额度保存异常，记账失败！");
  }

  private void throwBalanceException(CtrlRecordDTO existsBalance, CtrlRecordDTO tempBalance) {
    FBaseDTO ccidInfo = coaService.getCCIDInfo(account.getCoaDto(), existsBalance.getCcid());
    StringBuffer sb = new StringBuffer();
    sb.append("预算单位为");

    if (!StringUtil.isEmpty(ccidInfo.getAgency_id()) && !StringUtil.isEmpty(ccidInfo.getAgency_name()))
      sb.append(ccidInfo.getAgency_code()).append(ccidInfo.getAgency_name());
    else
      sb.append("空");

    sb.append(",功能分类为");

    if (!StringUtil.isEmpty(ccidInfo.getExpfunc_code()) && !StringUtil.isEmpty(ccidInfo.getExpfunc_name()))
      sb.append(ccidInfo.getExpfunc_code()).append(ccidInfo.getExpfunc_name());
    else
      sb.append("空");
    sb.append("的").append(account).append("科目余额可用数:");

    BigDecimal existsCanUse = existsBalance.getAvi_money().add(existsBalance.getUse_money().negate())
      .add(existsBalance.getMinus_money().negate());
    BigDecimal tempCanUse = tempBalance.getAvi_money().add(tempBalance.getUse_money().negate())
      .add(tempBalance.getMinus_money().negate());
    sb.append(existsCanUse).append("+(").append(tempCanUse).append(")=").append(existsCanUse.add(tempCanUse))
      .append("，额度保存失败！");

    throw new IllegalBalanceException(sb.toString());
  }

  /**
   * 补充零额度
   * @param account
   */
  public abstract void doSupplementZeroBalance();

  /**
   * 校验额度有效性
   * @param account 科目
   * @param balances 临时额度
 * @throws SQLException 
   */
  public abstract void doValidBalance(List balances);

  /**
   * 通过临时额度表更新额度
   * @param account 科目
 * @throws SQLException 
   */
  public abstract void doUpdateBalanceWithCache();
}
