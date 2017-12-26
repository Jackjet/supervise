package gov.df.fap.service.gl.Handler;

import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.gl.configure.BusVouType;
import gov.df.fap.bean.gl.dto.JournalDTO;
import gov.df.fap.service.gl.balance.IBalanceService;
import gov.df.fap.service.gl.configure.impl.EngineConfiguration;
import gov.df.fap.service.gl.je.IJournalService;

import java.util.List;

public class AuditActionHandler extends AbstractAccountHandler {

  public AuditActionHandler(IBalanceService balanceService, IJournalService journalService, ICoaService coaDao,
    EngineConfiguration engineConfiguration) {
    super(balanceService, journalService, coaDao, engineConfiguration);
    this.engineConfiguration = engineConfiguration;
  }

  private EngineConfiguration engineConfiguration;

  public void checkJournalWithCache(List existsJournals, List inputJournals) {
    //可直接终审,可终审后再终审,所以此处不作校验
  }

  public int getIsEnd() {
    return 1;
  }

  public long getActionId() {
    return ActionDefinitions.AUDIT_ACTION;
  }

  public void storeJournalWithCache() {
    //先更新已存在的,再插入不存在的.
    journalService.updateJournalWithCache();
    journalService.insertJournalByCache();
  }

  public int getIsValid() {
    return 1;
  }

  /**
   * 针对直接终审数据existsJournal可能为空,所以得从inputJournal中取数.
   */
  protected BusVouType getBvTypeByJournal(JournalDTO existsJournal, JournalDTO inputJournal) {
    return engineConfiguration.getBvTypeByBill(inputJournal.getBilltype_code());
  }

  /**
   * 终审情况下肯定生成临时额度
   */
  protected boolean isGenerateBalance(int changeStatus) {
    return true;
  }

  /**
   * 冲销历史额度
   */
  protected boolean generateNegative() {
    return true;
  }

  /**
   * 生成额度
   */
  protected boolean generatePositive() {
    return true;
  }

  /**
   * 如果CCID改变,重新形成额度追溯对象
   */
  protected boolean isReTracer(int changeStatus) {
    return changeStatus == CCID_CHANGE || changeStatus == FROMCTRLID_CHANGE;
  }

  public void checkJournal(JournalDTO existsJournal, JournalDTO inputJournal) {
    if (existsJournal != null && existsJournal.getIs_valid() == 0)//支持直接终审
      throw new RuntimeException("业务明细" + existsJournal.getVou_id() + "已作废, 不允许执行终审操作!");
  }

  /**
   * 返回记账类型
   * @return
   */
  protected boolean isAudit() {
    return true;
  }

}
