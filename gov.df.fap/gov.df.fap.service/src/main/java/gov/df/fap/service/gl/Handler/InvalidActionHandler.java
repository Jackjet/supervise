package gov.df.fap.service.gl.Handler;

import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.gl.dto.JournalDTO;
import gov.df.fap.service.gl.balance.IBalanceService;
import gov.df.fap.service.gl.configure.impl.EngineConfiguration;
import gov.df.fap.service.gl.je.IJournalService;
import gov.df.fap.service.util.exceptions.gl.VoucherNotFoundException;
import gov.df.fap.util.number.NumberUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * 作废操作处理器
 * @author 
 * @version 2017-03-06
 */
public class InvalidActionHandler extends AbstractAccountHandler {

  public InvalidActionHandler(IBalanceService balanceService, IJournalService journalService, ICoaService coaDao,
    EngineConfiguration engineConfiguration) {
    super(balanceService, journalService, coaDao, engineConfiguration);
  }

  public void tracerBalance(List balanceTracers) {
  }

  public void checkJournal(JournalDTO existsJournal, JournalDTO inputJournal) {
    if (existsJournal.getIs_valid() == 0)
      throw new RuntimeException("业务明细" + existsJournal.getVou_id() + "已作废, 不允许执行作废操作!");
  }

  public void checkJournalWithCache(List existsJournals, List inputJournals) {
    //有数据未入账
    if (inputJournals.size() > existsJournals.size()) {
      List notExistsJournals = journalService.findJournalWithCache(false);
      throw new VoucherNotFoundException(notExistsJournals);
    }
  }

  public int getIsEnd() {
    return 0;
  }

  public long getActionId() {
    return ActionDefinitions.INVALID_ACTION;
  }

  public void storeJournalWithCache() {
    journalService.updateJournalWithCache();
  }

  public int getIsValid() {
    return 0;
  }

  /**
   * 肯定会生成临时额度
   */
  protected boolean isGenerateBalance(int changeStatus) {
    return true;
  }

  /**
   * 肯定冲销额度
   */
  protected boolean generateNegative() {
    return true;
  }

  /**
   * 不重新生成额度
   */
  protected boolean generatePositive() {
    return false;
  }

  /**
   * 不重新追溯
   */
  protected boolean isReTracer(int changeStatus) {
    return false;
  }

  /**
   * 作废情况金额返回0
   */
  public BigDecimal journalMoney(String money) {
    return NumberUtil.BIG_DECIMAL_ZERO;
  }
}
