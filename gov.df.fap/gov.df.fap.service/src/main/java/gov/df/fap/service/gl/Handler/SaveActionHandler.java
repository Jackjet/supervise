package gov.df.fap.service.gl.Handler;

import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.gl.configure.BusVouAcctmdl;
import gov.df.fap.bean.gl.configure.BusVouType;
import gov.df.fap.bean.gl.dto.CtrlRecordDTO;
import gov.df.fap.bean.gl.dto.JournalDTO;
import gov.df.fap.service.gl.balance.IBalanceService;
import gov.df.fap.service.gl.coa.impl.CcidAccelerator;
import gov.df.fap.service.gl.configure.impl.EngineConfiguration;
import gov.df.fap.service.gl.je.IJournalService;
import gov.df.fap.service.util.exceptions.gl.GlException;

import java.util.List;

/**
 * 保存操作处理器
 * @author lwq
 * @version  2017-03-06
 */

public class SaveActionHandler extends AbstractAccountHandler {

  public SaveActionHandler(IBalanceService balanceService, IJournalService journalService, ICoaService coaDao,
    EngineConfiguration engineConfiguration) {
    super(balanceService, journalService, coaDao, engineConfiguration);
  }

  public void checkJournalWithCache(List alreadyList, List inputJournals) {
    if (alreadyList != null && alreadyList.size() > 0) {
      StringBuffer errorNos = new StringBuffer();
      for (int i = 0; i < alreadyList.size(); i++) {
        errorNos.append("'").append(((JournalDTO) alreadyList.get(i)).getVou_id()).append("',");
      }
      throw new GlException(errorNos.substring(0, errorNos.length() - 1) + "已录入，请不要重复录入!");
    }
  }

  public void tracerBalance(List balanceTracers) {
    balanceService.insertBalanceTrace(balanceTracers);
  }

  public void storeJournalWithCache() {
    //更新日志表及日志历史表
    journalService.insertJournalByCache();
  }

  public int getIsEnd() {
    return 0;
  }

  public long getActionId() {
    return ActionDefinitions.SAVE_ACTION;
  }

  public int getIsValid() {
    return 1;
  }

  /*
   * (non-Javadoc)
   * @see gov.gfmis.fap.gl.service.AccountHandler#doGenNegativeBalance(gov.gfmis.fap.gl.je.JournalDTO, gov.gfmis.fap.gl.configure.BusVouAcctmdl, int, gov.gfmis.fap.dictionary.coa.CcidAccelerator)
   */
  public CtrlRecordDTO doGenNegativeBalance(JournalDTO existsJournal, BusVouAcctmdl acctmdl, CcidAccelerator accelerator) {
    //不冲销额度
    return null;
  }

  /**
   * 录入的情况下,无论如何都会生成额度
   */
  protected boolean isGenerateBalance(int changeStatus) {
    return true;
  }

  /**
   * 不冲销额度
   */
  protected boolean generateNegative() {
    return false;
  }

  /**
   * 生成额度
   */
  protected boolean generatePositive() {
    return true;
  }

  /**
   * 录入的情况全部追溯信息都是新增
   */
  protected boolean isReTracer(int changeStatus) {
    return true;
  }

  protected BusVouType getBvTypeByJournal(JournalDTO existsJournal, JournalDTO inputJournal) {
    return engineConfiguration.getBvTypeByBill(inputJournal.getBilltype_code());
  }
}
