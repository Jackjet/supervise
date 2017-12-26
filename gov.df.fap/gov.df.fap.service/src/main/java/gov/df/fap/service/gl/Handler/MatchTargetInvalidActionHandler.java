package gov.df.fap.service.gl.Handler;

import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.gl.dto.JournalDTO;
import gov.df.fap.service.gl.balance.IBalanceService;
import gov.df.fap.service.gl.configure.impl.EngineConfiguration;
import gov.df.fap.service.gl.je.IJournalService;

/**
 * 作废操作处理器
 * 针对下游额度作废特殊接口使用
 * @author 
 */
public class MatchTargetInvalidActionHandler extends InvalidActionHandler {

  public MatchTargetInvalidActionHandler(IBalanceService balanceService, IJournalService journalService,
    ICoaService coaDao, EngineConfiguration engineConfiguration) {
    super(balanceService, journalService, coaDao, engineConfiguration);
  }

  public void checkJournal(JournalDTO existsJournal, JournalDTO inputJournal) {
    //此特殊处理时为了 已经查询出的日志主去向与传入数据中toctrlid不符,而要作废的是传入的toctrlid
    existsJournal.setPrimaryTargetId(inputJournal.getPrimaryTargetId());
  }

}
