package gov.df.fap.service.gl.Handler;

import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.gl.dto.JournalDTO;
import gov.df.fap.service.gl.balance.IBalanceService;
import gov.df.fap.service.gl.configure.impl.EngineConfiguration;
import gov.df.fap.service.gl.je.IJournalService;
import gov.df.fap.service.util.exceptions.gl.GlException;
import gov.df.fap.service.util.exceptions.gl.VoucherNotFoundException;

import java.util.List;

/**
 * 更新操作处理器
 * @author 
 * @version  2017-03-06
 */
public class UpdateActionHandler extends AbstractAccountHandler {

  public UpdateActionHandler(IBalanceService balanceService, IJournalService journalService, ICoaService coaDao,
    EngineConfiguration engineConfiguration) {
    super(balanceService, journalService, coaDao, engineConfiguration);
  }

  public void checkJournal(JournalDTO existsJournal, JournalDTO inputJournal) {
    if (existsJournal.getIs_end() == 1)
      throw new GlException("业务明细" + existsJournal.getVou_id() + "已终审, 不允许执行更新操作!");

    if (existsJournal.getIs_valid() == 0)
      throw new RuntimeException("业务明细" + existsJournal.getVou_id() + "已作废, 不允许执行更新操作!");
  }

  public void checkJournalWithCache(List existsJournals, List inputJournals) {
    //有数据未入账
    if (inputJournals.size() > existsJournals.size()) {
      List notExistsJournals = journalService.findJournalWithCache(false);
      throw new VoucherNotFoundException("凭证未录入请不要执行更新操作!", notExistsJournals);
    }
  }

  public void tracerBalance(List balanceTracers) {
    if (balanceTracers.size() > 0)
      balanceService.reTraceCcidChangeBalance(balanceTracers);
  }

  public void storeJournalWithCache() {
    journalService.updateJournalWithCache();
  }

  public List findExistsJournalsWithCache() {
    return journalService.findJournalWithCache(true);
  }

  public int getIsEnd() {
    return 0;
  }

  public long getActionId() {
    return ActionDefinitions.UPDATE_ACTION;
  }

  public int getIsValid() {
    return 1;
  }

  /**
   * CCID、金额、月份改变的情况下重新生成科目余额
   */
  protected boolean isGenerateBalance(int changeStatus) {
    return changeStatus == CCID_CHANGE || changeStatus == MONEY_CHANGE || changeStatus == MONTH_CHANGE
      || changeStatus == FROMCTRLID_CHANGE;
  }

  /**
   * 如果重新生成科目余额，则冲销旧科目余额
   */
  protected boolean generateNegative() {
    return true;
  }

  /**
   * 如果重新生成科目余额，则生成新的科目余额
   */
  protected boolean generatePositive() {
    return true;
  }

  /**
   * CCID改变的情况下重新生成追溯记录
   */
  protected boolean isReTracer(int changeStatus) {
    return changeStatus == CCID_CHANGE;
  }

}
