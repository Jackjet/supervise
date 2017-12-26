package gov.df.fap.service.util.gl.balance;

import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.configure.BusVouAcctmdl;
import gov.df.fap.bean.gl.dto.CtrlRecordDTO;
import gov.df.fap.bean.gl.dto.FCtrlRecordDTO;
import gov.df.fap.bean.gl.dto.JournalDTO;
import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.number.NumberUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 额度工具
 * @author 
 * @versio
 */
public class BalanceUtil {

  public static final String BALANCE_KEYS_SEPERATOR = ",";

  /**
   * 将balance2的金额加到balance上,返回balance.
   * @param balance
   * @param balance2
   * @return
   */
  public static CtrlRecordDTO plus(CtrlRecordDTO balance1, CtrlRecordDTO balance2) {
    balance1.setAvi_money(balance1.getAvi_money().add(balance2.getAvi_money()));
    balance1.setAving_money(balance1.getAving_money().add(balance2.getAving_money()));
    balance1.setUse_money(balance1.getUse_money().add(balance2.getUse_money()));
    balance1.setMinus_money(balance1.getMinus_money().add(balance2.getMinus_money()));
    if (balance2.getBalanceType() == CtrlRecordDTO.BALANCE_TYPE_POSITIVE) {
      balance1.setPrimarySourceId(balance2.getPrimarySourceId());
    }
    if (balance2.getIs_enforce() == NumberUtil.INT_TRUE) {
      balance1.setIs_enforce(NumberUtil.INT_TRUE);
    }
    balance1.setBalanceType(CtrlRecordDTO.BALANCE_TYPE_SUM);
    return balance1;
  }

  /**
   * 计算出临时额度对象,金额也只是临时对额度的影响数,并不是指该额度的真实数.
   * @param journal 日志对象
   * @param account 科目对象
   * @return 临时额度对象
   */
  public static CtrlRecordDTO caculateBalance(JournalDTO journal, BusVouAcctmdl account) {
    CtrlRecordDTO positiveBalance = new CtrlRecordDTO();
    boolean end = journal.getIs_end() == 1;
    boolean plusAccountBalance = account.isPlusAccountBalance();//科目余额
    boolean moneyPositive = journal.getVou_money().signum() > 0;

    if (plusAccountBalance && end)//生成余额(无论正负)终审后把金额加到avi_money上
      positiveBalance.setAvi_money(journal.getVou_money());
    if (plusAccountBalance && !end && moneyPositive)//生成正的余额在途是把金额加到aving_money上
      positiveBalance.setAving_money(journal.getVou_money());
    if (plusAccountBalance && !end && !moneyPositive)//生成负的余额在途把金额加到minus_money上
      positiveBalance.setMinus_money(journal.getVou_money().compareTo(new BigDecimal(0)) == 1 ? journal.getVou_money()
        : journal.getVou_money().negate());
    if (!plusAccountBalance && (moneyPositive || (!moneyPositive && end)))//扣减余额，正金额时在途就加use_money, 负金额时终审才加use_money
      positiveBalance.setUse_money(journal.getVou_money());

    if (journal.getCtrlLevel() == 2 && account.getCtrl_level() == 2)
      positiveBalance.setIs_enforce(0);//非强制
    else if (journal.getCtrlLevel() == 3 && account.getCtrl_level() == 2)
      positiveBalance.setIs_enforce(3);//对于重新计算额度的特殊处理
    else
      positiveBalance.setIs_enforce(1);//强制

    //将数据其他信息写入临时额度中
    if (!StringUtil.isEmpty(journal.getPrimarySourceId()) && plusAccountBalance)
      positiveBalance.setPrimarySourceId(journal.getPrimarySourceId());
    positiveBalance.setAccount_id(account.getBusVouAccount().getAccountId());
    positiveBalance.setAccount_code(account.getBusVouAccount().getAccountCode());
    positiveBalance.setCtrl_side(NumberUtil.xor(account.getEntry_side(), account.getBusVouAccount().getBalanceSide()));
    //positiveBalance.setIs_primary(account.getEntry_side() == 1 ? account.getIs_primary_source() : account.getIs_primary_target());
    positiveBalance.setRg_code(journal.getRg_code());
    positiveBalance.setBal_version(journal.getBal_version());
    positiveBalance.setCreate_date(journal.getCreate_date());
    positiveBalance.setLatest_op_date(journal.getLatest_op_date());
    positiveBalance.setLast_ver(journal.getLast_ver());
    positiveBalance.setRemark(journal.getRemark());
    positiveBalance.setSet_year(journal.getSet_year());
    positiveBalance.setSet_month(journal.getSet_month());
    positiveBalance.setMb_id(journal.getMb_id());
    positiveBalance.setAgency_id(journal.getAgency_id());

    //处理月份
    int balancePriodType = account.getBusVouAccount().getBalancePeriodType();
    if (balancePriodType == BusVouAccount.BALANCE_PERIOD_TYPE_YEAR) {
      positiveBalance.setSet_month(0);
    } else if (journal.getSet_month() < 0 || journal.getSet_month() > 12) {
      positiveBalance.setSet_month(DateHandler.getCurrentMonth());
    } else
      positiveBalance.setSet_month(journal.getSet_month());

    return positiveBalance;
  }

  /**
   * add by ydz 添加对缺省时默认月份的设置
   * @return
   */
  public static int getDefaultMonth() {
    final int currentYear = CommonUtil.getIntSetYear();
    final int realYear = DateHandler.getCurrentYear();
    int currentMonth = 0;
    if (currentYear > realYear) {// 操作下年，默认1月份
      currentMonth = 1;
    } else if (currentYear < realYear) {// 操作上年，默认12月
      currentMonth = 12;
    } else {
      currentMonth = DateHandler.getCurrentMonth();
    }
    return currentMonth;
  }

  public static boolean checkBalanceEquals(CtrlRecordDTO record1, CtrlRecordDTO record2) {
    return record1.getAccount_id().equals(record2.getAccount_id()) && record1.getCcid() == record2.getCcid()
      && record1.getSet_year() == record2.getSet_year() && record1.getRg_code().equals(record2.getRg_code());
  }

  public static void clearMoney(FCtrlRecordDTO recordDto) {
    recordDto.setAvi_money("0");
    recordDto.setAving_money("0");
    recordDto.setMinus_money("0");
    recordDto.setUse_money("0");
  }

  public static void clearMoney(CtrlRecordDTO recordDto) {
    recordDto.setAvi_money(new BigDecimal(0));
    recordDto.setAving_money(new BigDecimal(0));
    recordDto.setMinus_money(new BigDecimal(0));
    recordDto.setUse_money(new BigDecimal(0));
  }

  /**
   * 根据balanceList回填transList中生成额度的sum_id
   * @param transList
   * @param balanceList
   */
  public static void resetSumId(List transList, List balanceList) {
    final Map map = new HashMap();
    CtrlRecordDTO recordDto = null;
    //将已有的额度缓存
    Iterator iterator = balanceList.iterator();
    while (iterator.hasNext()) {
      recordDto = (CtrlRecordDTO) iterator.next();
      map.put(recordDto, recordDto);
    }
    iterator = transList.iterator();
    while (iterator.hasNext()) {
      recordDto = (CtrlRecordDTO) iterator.next();
      recordDto.setSum_id(((CtrlRecordDTO) map.get(recordDto)).getSum_id());
    }
  }
}
