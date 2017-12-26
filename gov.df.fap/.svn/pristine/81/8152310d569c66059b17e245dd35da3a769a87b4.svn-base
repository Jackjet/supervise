package gov.df.fap.service.gl.balance.matcher;

import gov.df.fap.bean.gl.configure.BusVouAcctmdl;
import gov.df.fap.bean.gl.dto.ConditionObj;
import gov.df.fap.bean.gl.dto.CtrlRecordDTO;
import gov.df.fap.bean.gl.dto.FCtrlRecordDTO;
import gov.df.fap.bean.gl.dto.JournalDTO;
import gov.df.fap.service.gl.balance.BalanceMatcher;
import gov.df.fap.service.gl.balance.IBalanceDao;
import gov.df.fap.service.gl.coa.impl.BatchCcidTransProcesser;
import gov.df.fap.service.gl.core.DaoSupport;
import gov.df.fap.service.util.gl.balance.BalanceUtil;
import gov.df.fap.util.number.NumberUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 抽象额度匹配器
 * @author
 */
public abstract class AbstractBalanceMatcher implements BalanceMatcher {

  @Autowired
  private IBalanceDao dao;

  private DaoSupport daoSupport;

  public AbstractBalanceMatcher(IBalanceDao dao) {
    this.dao = dao;
  }

  public AbstractBalanceMatcher(DaoSupport daoSupport) {
    this.daoSupport = daoSupport;
  }

  /**
   * 额度匹配方法
   * @param journal 日志
   * @param account 科目信息
   * @param processer ccid批量转换处理器
   * @return
   */
  public CtrlRecordDTO matchBalance(JournalDTO journal, BusVouAcctmdl account, BatchCcidTransProcesser processer) {
    boolean ccidTrans = true;
    //生成额度临时对象.
    CtrlRecordDTO recordDto = BalanceUtil.caculateBalance(journal, account);
    if (!isMatchByElement(journal, account)) {
      final ConditionObj condition = getMatchConditonObj(journal, account);
      //修改 将原有的额度获取改成简单的sql 只查询需要的字段
      List dtoList = daoSupport
        .genericQuery(
          "select sum_id,ccid,rcid,fromctrlid from gl_balance where set_year=? and rg_code=? and account_id=? and sum_id=? ",
          new Object[] { new Integer(journal.getSet_year()), journal.getRg_code(), account.getAccount_id(),
            condition.get(0).getValue() }, FCtrlRecordDTO.class);
      FCtrlRecordDTO balance = null;
      if (dtoList != null && dtoList.size() > 0) {
        balance = (FCtrlRecordDTO) dtoList.get(0);
      }
      if (balance != null) {
        recordDto.setSum_id(balance.getSum_id());
        recordDto.setCcid(NumberUtil.toLong(balance.getCcid()));
        recordDto.setRcid(balance.getRcid());

        //解决录入支付数据时，分月额度表存在fromctrlid为空的情况
        //在生成临时额度时，补充上游额度的fromctrlid
        if (!account.isPlusAccountBalance())
          recordDto.setPrimarySourceId(balance.getFromctrlid());
        ccidTrans = false;
      }
    }
    if (ccidTrans) {
      processer.addCtrlRecordNeedTrans(account.getBusVouAccount().getCoaDto(), journal.getCcid(), recordDto,
        !account.isPlusAccountBalance());
    }

    recordDto.setBalanceType(CtrlRecordDTO.BALANCE_TYPE_POSITIVE);
    return recordDto;
  }

  public IBalanceDao getDao() {
    return dao;
  }

  public void setDao(IBalanceDao dao) {
    this.dao = dao;
  }

  /**
   * 是否使用要素匹配
   * @param journal 日志对象
   * @param account 记账模板科目关联关系
   * @return 使用要素匹配
   */
  public abstract boolean isMatchByElement(JournalDTO journal, BusVouAcctmdl account);

  /**
   * 获取额度查询条件
   * @param journal
   * @param account
   * @return
   */
  public abstract ConditionObj getMatchConditonObj(JournalDTO journal, BusVouAcctmdl account);
}
