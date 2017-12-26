package gov.df.fap.service.gl.configure.impl;

import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.api.gl.configure.AccountService;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.service.util.gl.configure.AccountIllegalException;
import gov.df.fap.service.util.gl.configure.IAccountDao;
import gov.df.fap.util.StringUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 科目服务
 * @author 
 * @version 2008-4-10
 */
@Component("accountServiceImpl")
public class AccountServiceImpl implements AccountService {

  @Autowired
  IAccountDao accDao = null;

  @Autowired
  ICoaService coaService = null;

  public void setAccDao(IAccountDao daoSupport) {
    this.accDao = daoSupport;
  }

  public void setCoaService(ICoaService coa) {
    this.coaService = coa;
  }

  public BusVouAccount loadBusVouAccount(String accountId) {
    BusVouAccount account = accDao.loadAccount(accountId);
    if (account == null)
      return null;
    account.setCoaDto(coaService.getCoa(account.getCoaId()));
    return account;
  }

  public BusVouAccount loadBusVouAccountByCode(String accountCode) {
    BusVouAccount account = accDao.loadAccountByCode(accountCode);
    if (account != null)
      account.setCoaDto(coaService.getCoa(account.getCoaId()));
    return account;
  }

  public List allBusVouAccount() {
    Map cache = new HashMap();//dingyy20120530 防止循环查询数据库
    List allAccounts = accDao.allAccount();
    for (int i = 0; i < allAccounts.size(); i++) {
      final BusVouAccount account = (BusVouAccount) allAccounts.get(i);
      Long coaId = new Long(account.getCoaId());
      if (cache.containsKey(coaId)) {
        account.setCoaDto((FCoaDTO) cache.get(coaId));
      } else {
        FCoaDTO coa = coaService.getCoa(coaId.longValue());
        account.setCoaDto(coa);
        cache.put(coaId, coa);
      }
    }
    return allAccounts;
  }

  public List allBusVouAccountSimple() {
    return accDao.allAccount();
  }

  public void updateBusVouAccount(BusVouAccount oldAccount, BusVouAccount newAccount) {
    this.checkAccount(newAccount);
    accDao.updateAccount(newAccount);
  }

  public void saveBusVouAccount(BusVouAccount bvAccount) {
    this.checkAccount(bvAccount);
    accDao.saveAccount(bvAccount);
  }

  public void deleteBusVouAccount(BusVouAccount bvAccount) {
    accDao.deleteAccount(bvAccount);
  }

  public void checkAccount(BusVouAccount account) {
    if (account.getBalancePeriodType() == BusVouAccount.BALANCE_PERIOD_TYPE_SUM_MONTH
      && StringUtil.equalsIgnoreCase(account.getTableName(), account.getMonthDetailTableName()))
      throw new AccountIllegalException("累计月科目的余额表不能与余额明细表相同!");
  }

  public void reinstallAccount(List accountList) {
    accDao.deleteAllVwGlAccount();
    batchSaveBusVouAccount(accountList);
  }

  /**
   * 批量保存科目
   * @param accountList
   */
  public void batchSaveBusVouAccount(List accountList) {
    final Iterator iterator = accountList.iterator();
    while (iterator.hasNext()) {
      accDao.saveAccountByOrigin((BusVouAccount) iterator.next());
    }
  }

  /**
   * 批量更新科目
   * @param accountList
   */
  public void batchUpdateBusVouAccount(List accountList) {
    final Iterator iterator = accountList.iterator();
    while (iterator.hasNext()) {
      updateBusVouAccount(null, (BusVouAccount) iterator.next());
    }
  }

  /**
   * 批量删除科目
   * @param accountList
   */
  public void batchDeleteBusVouAccount(List accountList) {
    final Iterator iterator = accountList.iterator();
    while (iterator.hasNext()) {
      deleteBusVouAccount((BusVouAccount) iterator.next());
    }
  }

  public String getId(String accountCode, String rg_code, String set_year) {
    // TODO Auto-generated method stub
    BusVouAccount account = accDao.loadAccountByCode(accountCode);
    if (null != account) {
      return account.getAccountCode();
    }
    return null;
  }

  @Override
  public void clearCache() {
    // TODO Auto-generated method stub

  }

}
