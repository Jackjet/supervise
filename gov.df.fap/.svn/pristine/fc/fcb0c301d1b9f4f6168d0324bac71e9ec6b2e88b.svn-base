package gov.df.fap.service.gl.Handler;

import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.gl.GlConstant;
import gov.df.fap.bean.gl.configure.BusVouAcctmdl;
import gov.df.fap.bean.gl.configure.BusVouType;
import gov.df.fap.bean.gl.dto.CtrlRecordDTO;
import gov.df.fap.bean.gl.dto.JournalDTO;
import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.service.gl.balance.BalanceGenResult;
import gov.df.fap.service.gl.balance.BalanceTracer;
import gov.df.fap.service.gl.balance.IBalanceService;
import gov.df.fap.service.gl.balance.matcher.AbstractBalanceMatcher;
import gov.df.fap.service.gl.coa.impl.BatchCcidTransProcesser;
import gov.df.fap.service.gl.coa.impl.CoaService.RuleMatchKey;
import gov.df.fap.service.gl.configure.impl.EngineConfiguration;
import gov.df.fap.service.gl.je.IJournalService;
import gov.df.fap.service.util.exceptions.gl.VoucherNotFoundException;
import gov.df.fap.util.StringUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 对象记账操作处理过程的抽象,主要是抽象是否生成冲销额度、是否生成额度以及对象传入日志与
 * 历史日志的校验。这几种操作对于每种记账操作来说都是不一样的，但他们的大体逻辑却很相似，
 * 所以经过抽象后，额度生成的方法很统一，代码重用度较高。
 * @author 
 * @version   2017-03-06
 */
public abstract class AbstractAccountHandler implements AccountHandler {

  protected static final Log logger = LogFactory.getLog(AbstractAccountHandler.class);

  static final int NO_CHANGE = -1;

  static final int CCID_CHANGE = 0;

  static final int MONTH_CHANGE = 1;

  static final int MONEY_CHANGE = 2;

  static final int ISEND_CHANGE = 3;

  //针对 resetFromctrlidAudit 接口 重定来源
  static final int FROMCTRLID_CHANGE = 4;

  protected IBalanceService balanceService = null;

  protected IJournalService journalService = null;

  protected EngineConfiguration engineConfiguration;

  protected ICoaService coaService = null;

  private Map ruleMatchCache = new HashMap();

  public AbstractAccountHandler(IBalanceService balanceService, IJournalService journalService, ICoaService coaService,
    EngineConfiguration engineConfiguration) {
    this.balanceService = balanceService;
    this.journalService = journalService;
    this.coaService = coaService;
    this.engineConfiguration = engineConfiguration;
  }

  public BalanceGenResult generateBalance(List existsJournals, List inputJournalList,
    AbstractBalanceMatcher balanceMatcher) {
    if (inputJournalList == null)
      throw new NullPointerException("传入日志为空,生成临时额度失败!");

    if (logger.isDebugEnabled())
      logger.debug("记账处理器:" + getClass().getName() + " 开始处理余额生成逻辑!");
    //原始额度、录入额度
    BalanceGenResult result = new BalanceGenResult();
    //计算需更新额度
    Iterator journalIt = inputJournalList.iterator();
    int offset = 0;
    JournalDTO existsJournal = null;
    //原始COA、CCID、额度； 录入COA、CCID、额度
    BatchCcidTransProcesser processer = new BatchCcidTransProcesser(coaService);
    while (journalIt.hasNext()) {
      final JournalDTO inputJournal = (JournalDTO) journalIt.next();
      if (existsJournals == null || offset >= existsJournals.size())
        existsJournal = null;
      else {
        existsJournal = (JournalDTO) existsJournals.get(offset);//基于两个列表都已经按vou_id排序，按相同顺序读取原凭证
        if (!StringUtil.equals(existsJournal.getVou_id(), inputJournal.getVou_id())) {
          offset--;
          existsJournal = null;
        }
      }
      //existsJournals 存在并且is_valid为0，提示己作废
      checkJournal(existsJournal, inputJournal);

      int changeStatus = compareJournal(existsJournal, inputJournal);//比较有没有改变
      if (isGenerateBalance(changeStatus)) {
        final BusVouType bvType = getBvTypeByJournal(existsJournal, inputJournal);
        //记账模板
        List acctmdlList = bvType.getBusVouAcctmdl();
        if (acctmdlList == null)
          continue;
        for (int i = 0; i < acctmdlList.size(); i++) {
          final BusVouAcctmdl acctmdl = (BusVouAcctmdl) acctmdlList.get(i);
          if (acctmdl.getBusVouAccount() == null)
            throw new NullPointerException("ID 为：" + acctmdl.getVou_type_id() + "的交易令上科目为空！");
          if (acctmdl.getBusVouAccount().getCoaDto() != null && existsJournal != null && generateNegative()
            && isJournalMatchRule(acctmdl, existsJournal)) {
            //存在数据的临时额度，冲销科目余额不产生追溯对象
            final CtrlRecordDTO negativeBalance = balanceMatcher.matchBalance(existsJournal, acctmdl, processer);
            negativeBalance.negative();
            result.addBalance(negativeBalance);
            if (logger.isDebugEnabled())
              logger.debug("生成冲销用临时额度不生额度追溯对象, 额度信息:" + negativeBalance);
          }

          if (acctmdl.getBusVouAccount().getCoaDto() != null && generatePositive()
            && isJournalMatchRule(acctmdl, inputJournal)) {
            //录入数据临时额度
            final CtrlRecordDTO tempPositiveBalance = balanceMatcher.matchBalance(inputJournal, acctmdl, processer);
            //录入数据明细额度
            final CtrlRecordDTO detailBalance = result.addBalance(tempPositiveBalance);
            //ccid及来源没有发生变化 ，不用追溯
            if (isReTracer(changeStatus))
              result.addTracer(new BalanceTracer(inputJournal, detailBalance, acctmdl));

            if (logger.isDebugEnabled())
              logger.debug("生成临时额度与额度追溯对象,额度信息:" + tempPositiveBalance);
          }
        }
      }
      offset++;
    }
    if (logger.isDebugEnabled())
      logger.debug("临时额度生成结束,利用临时额度中的ccid(其实是转换的源CCID)进行要素匹配,生成科目COA指定的CCID!, 批量数据大小" + processer.size());
    coaService.preCcidTransBatch(processer);
    return result;
  }

  /**
   * 是否生成临时额度
   * @param changeStatus
   * @return
   */
  protected abstract boolean isGenerateBalance(int changeStatus);

  /**
   * 是否冲销额度
   * @return
   */
  protected abstract boolean generateNegative();

  /**
   * 是否生成额度
   * @return
   */
  protected abstract boolean generatePositive();

  /**
   * 是否重新生成追溯记录
   * @param changeStatus
   * @return
   */
  protected abstract boolean isReTracer(int changeStatus);

  /**
   * 根据billtype_code取记账模板
   * @param existsJournal
   * @param inputJournal
   * @return
   */
  protected BusVouType getBvTypeByJournal(JournalDTO existsJournal, JournalDTO inputJournal) {
    return engineConfiguration.getBvTypeByBill(existsJournal.getBilltype_code());
  }

  /**
   * 判断是否生成科目余额
   * @param acctmdl
   * @param journal
   * @return
   */
  private boolean isJournalMatchRule(BusVouAcctmdl acctmdl, JournalDTO journal) {
    if (StringUtil.isEmpty(acctmdl.getRule_id()) || StringUtil.ZERO.equals(acctmdl.getRule_id()))
      return true;
    RuleMatchKey key = new RuleMatchKey(acctmdl.getRule_id(), journal.getCcid());
    Boolean ruleMatch = (Boolean) ruleMatchCache.get(key);
    if (ruleMatch == null) {
      boolean ccidMatchRule = coaService.ccidMatchRule(acctmdl.getRule_id(), journal.getCcid());
      ruleMatchCache.put(key, ccidMatchRule ? Boolean.TRUE : Boolean.FALSE);
      return ccidMatchRule;
    } else
      return ruleMatch.booleanValue();
  }

  public void checkJournalWithCache(List existsJournals, List inputJournals) {
    //有数据未入账
    if (inputJournals.size() > existsJournals.size()) {
      List notExistsJournals = journalService.findJournalWithCache(false);
      throw new VoucherNotFoundException(notExistsJournals);
    }
  }

  public void tracerBalance(List balanceTracers) {
    if (balanceTracers.size() > 0)
      balanceService.reTraceCcidChangeBalance(balanceTracers);
  }

  public List findExistsJournalsWithCache() {
    return journalService.findJournalWithCache(true);
  }

  /**
   * 比较两个日志是改变状态
   * @param exists
   * @param input
   * @return
   */
  protected int compareJournal(JournalDTO exists, JournalDTO input) {
    if (exists == null || input == null)
      return CCID_CHANGE;

    BigDecimal priVouMoney = exists.getVou_money().setScale(6, BigDecimal.ROUND_HALF_DOWN);
    BigDecimal inputVouMoney = input.getVou_money().setScale(6, BigDecimal.ROUND_HALF_DOWN);
    if (input.getCtrlLevel() == GlConstant.CTRLLEVEL_ALWAYS) {
      return CCID_CHANGE;//如果总是计算额度，则当作CCID变化处理
    } else if (exists.getCcid() != input.getCcid())
      return CCID_CHANGE;
    else if (isChangedFromctrlid(exists.getPrimarySourceId(), input.getPrimarySourceId()))
      return FROMCTRLID_CHANGE;
    else if (exists.getSet_month() != input.getSet_month())
      return MONTH_CHANGE;
    else if (!priVouMoney.equals(inputVouMoney))
      return MONEY_CHANGE;
    else if (exists.getIs_end() != input.getIs_end())
      return ISEND_CHANGE;

    else
      return NO_CHANGE;
  }

  /**
   * 判断fromctrlid 是否发生变化
   * @param primarySourceId1
   * @param primarySourceId2
   * @return
   */
  protected boolean isChangedFromctrlid(String primarySourceId1, String primarySourceId2) {
    if (StringUtil.isEmpty(primarySourceId1) && StringUtil.isEmpty(primarySourceId2))
      return false;
    else if (StringUtil.equals(primarySourceId1, primarySourceId2))
      return false;
    return true;
  }

  public void checkJournal(JournalDTO existsjournal, JournalDTO inputJournal) {
    //空实现
  }

  /**
   * 一般情况按原金额返回
   */
  public BigDecimal journalMoney(String money) {
    return new BigDecimal(money);
  }

  /**
   * 返回记账类型
   * @return
   */
  protected boolean isAudit() {
    return false;
  }

  /**
   * 记账成功后，回写记账dto数据 特殊属性
   * @param inputList
   */
  public void handleAfterProperties(List inputList) {
    for (int i = 0; i < inputList.size(); i++)
      ((FVoucherDTO) inputList.get(i)).setIs_end(isAudit() ? 1 : 0);
  }
}
