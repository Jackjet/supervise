package gov.df.fap.service.gl.Handler;

import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.gl.dto.JournalDTO;
import gov.df.fap.service.gl.balance.IBalanceService;
import gov.df.fap.service.gl.configure.impl.EngineConfiguration;
import gov.df.fap.service.gl.je.IJournalService;
import gov.df.fap.service.util.exceptions.gl.VoucherNotFoundException;

import java.util.List;

/**
 * 取消终审操作处理器,处理取乐终审操作.
 * @author 
 * @version 2017-03-24
 */
public class CancelAuditActionHandler extends AbstractAccountHandler {

  public CancelAuditActionHandler(IBalanceService balanceService, IJournalService journalService, ICoaService coaDao,
    EngineConfiguration engineConfiguration) {
    super(balanceService, journalService, coaDao, engineConfiguration);
  }

  public void checkJournal(JournalDTO existsJournal, JournalDTO inputJournal) {

    if (existsJournal.getIs_valid() == 0)
      throw new RuntimeException("业务明细" + existsJournal.getVou_id() + "已作废, 不允许执行撤销终审操作!");
  }

  public List findExistsJournalsWithCache() {
    return journalService.findJournalWithCache(true);
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
    return ActionDefinitions.CANCEL_AUDIT_ACTION;
  }

  public void storeJournalWithCache() {
    journalService.updateJournalWithCache();
  }

  public int getIsValid() {
    return 1;
  }

  /**
   * 撤销终审一定生成临时额度
   */
  protected boolean isGenerateBalance(int changeStatus) {
    return true;
  }

  /**
   * 冲销已终审的科目余额
   */
  protected boolean generateNegative() {
    return true;
  }

  /**
   * 生成在途的科目余额
   */
  protected boolean generatePositive() {
    return true;
  }

  /**
   * CCID改变则重新形成追溯记录
   */
  protected boolean isReTracer(int changeStatus) {
    return changeStatus == CCID_CHANGE;
  }

}
