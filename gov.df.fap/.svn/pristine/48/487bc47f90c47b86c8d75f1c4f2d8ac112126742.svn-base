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
 * 主来源额度配器
 * @author 
 */
public class DefaultBalanceMatcher extends AbstractBalanceMatcher {

  public DefaultBalanceMatcher(IBalanceDao dao) {
    super(dao);
  }

  public DefaultBalanceMatcher(DaoSupport dao) {
    super(dao);
  }

  /**
   * 是否使用要素匹配,判断准则:
   * 1、primarySourceId为空,使用要素匹配.
   * 2、科目非主来源 且 传入的主来源类型与科目不同.
   * 3、科目不是来源.
   */
  public boolean isMatchByElement(JournalDTO journal, BusVouAcctmdl account) {
    return StringUtil.isEmpty(journal.getPrimarySourceId())
      || (account.getIs_primary_source() != NumberUtil.INT_TRUE && (StringUtil.isEmpty(journal
        .getPrimarySourceAccountId()) || !StringUtil.equals(journal.getPrimarySourceAccountId(), account
        .getBusVouAccount().getAccountCode())))
      || account.getEntry_side() == account.getBusVouAccount().getBalanceSide();
  }

  public ConditionObj getMatchConditonObj(JournalDTO journal, BusVouAcctmdl account) {
    final ConditionObj condition = new ConditionObj();
    condition.setQueryWithMaxCount(false);
    condition.setAllowRight(false);
    condition.add("sum_id", Condition.EQUAL, journal.getPrimarySourceId());
    return condition;
  }
}
