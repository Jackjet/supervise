package gov.df.fap.service.gl.balance.matcher;

import gov.df.fap.bean.gl.configure.BusVouAcctmdl;
import gov.df.fap.bean.gl.dto.Condition;
import gov.df.fap.bean.gl.dto.ConditionObj;
import gov.df.fap.bean.gl.dto.JournalDTO;
import gov.df.fap.service.gl.balance.IBalanceDao;
import gov.df.fap.service.gl.core.DaoSupport;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.number.NumberUtil;

/**
 * 主去向额度匹配器,同时包括了主来源额度匹配功能
 * @author 
 */
public class TargetBalanceMatcher extends AbstractBalanceMatcher {

  public TargetBalanceMatcher(IBalanceDao dao) {
    super(dao);
  }

  public TargetBalanceMatcher(DaoSupport dao) {
    super(dao);
  }

  /**
   * 是否使用要素匹配,判断准则:
   * 1、primaryTargetId为空,使用要素匹配.
   * 2、科目非主去向 且 传入的主去向类型与科目不同.
   * 3、科目不是去向.
   */
  public boolean isMatchByElement(JournalDTO journal, BusVouAcctmdl account) {
    return !account.isPlusAccountBalance() ? (StringUtil.isEmpty(journal.getPrimarySourceId()) || (account
      .getIs_primary_source() != NumberUtil.INT_TRUE && (StringUtil.isEmpty(journal.getPrimarySourceAccountId()) || !StringUtil
      .equals(journal.getPrimarySourceAccountId(), account.getBusVouAccount().getAccountCode()))))
      : (StringUtil.isEmpty(journal.getPrimaryTargetId()) || (account.getIs_primary_target() != NumberUtil.INT_TRUE && (StringUtil
        .isEmpty(journal.getPrimaryTargetAccountId()) || !StringUtil.equals(journal.getPrimaryTargetAccountId(),
        account.getBusVouAccount().getAccountCode()))));
  }

  public ConditionObj getMatchConditonObj(JournalDTO journal, BusVouAcctmdl account) {
    final ConditionObj condition = new ConditionObj();
    condition.setQueryWithMaxCount(false);
    condition.setAllowRight(false);
    if (!account.isPlusAccountBalance())
      condition.add("sum_id", Condition.EQUAL, journal.getPrimarySourceId());
    else
      condition.add("sum_id", Condition.EQUAL, journal.getPrimaryTargetId());
    return condition;
  }
}
