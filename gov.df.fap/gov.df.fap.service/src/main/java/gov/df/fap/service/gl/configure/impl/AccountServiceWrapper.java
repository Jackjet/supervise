package gov.df.fap.service.gl.configure.impl;

import gov.df.fap.api.gl.configure.AccountService;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.configure.CommonKey;
import gov.df.fap.service.dictionary.elecache.DefaultCacheFactory;
import gov.df.fap.service.util.dictionary.interfaces.Cache;
import gov.df.fap.service.util.dictionary.interfaces.CachedObject;
import gov.df.fap.service.util.gl.configure.EngineCache;
import gov.df.fap.service.util.gl.core.CommonUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 科目维护缓存,由于与记账模板、COA缓存存在关联关系,所以这两个缓存处理要十分小心.
 * 对象的级联处理是比较重量级的处理,所以在这个小缓存组件中,不引入.
 * 在搭建数据库时,要建立好account的外键,用于约束account与busvou_type,这样
 * 缓存中也无法删除被BusVouType缓存所引用的account.
 * @author 
 * @version 
 */
@Component("accountServiceWrapper")
public class AccountServiceWrapper implements AccountService, EngineCache {

  @Autowired
  @Qualifier("accountServiceImpl")
  private AccountService accountServiceImpl = null;

  private Cache idCache = DefaultCacheFactory.getInstance().getCacheInstance();

  private Cache codeCache = DefaultCacheFactory.getInstance().getCacheInstance();

  public void setAccountServiceImpl(AccountServiceImpl service) {
    this.accountServiceImpl = service;
  }

  public BusVouAccount loadBusVouAccount(String accountId) {
    final Object idKey = new CommonKey(accountId, CommonUtil.getSetYear(), CommonUtil.getRgCode());
    BusVouAccount returnOne = (BusVouAccount) idCache.getCacheObject(idKey);
    if (returnOne == null) {
      BusVouAccount loadedAccount = accountServiceImpl.loadBusVouAccount(accountId);
      if (loadedAccount != null) {
        final Object codeKey = new CommonKey(loadedAccount.getAccountCode(), CommonUtil.getSetYear(),
          CommonUtil.getRgCode());
        idCache.addCacheObject(idKey, loadedAccount);
        codeCache.addCacheObject(codeKey, loadedAccount);
        returnOne = loadedAccount;
      }
    }
    return returnOne;
  }

  public BusVouAccount loadBusVouAccountByCode(String accountCode) {
    final Object codeKey = new CommonKey(accountCode, CommonUtil.getSetYear(), CommonUtil.getRgCode());
    BusVouAccount returnOne = (BusVouAccount) codeCache.getCacheObject(codeKey);
    if (returnOne == null) {
      BusVouAccount loadedAccount = accountServiceImpl.loadBusVouAccountByCode(accountCode);
      if (loadedAccount != null) {
        final Object idKey = new CommonKey(loadedAccount.getAccountId(), CommonUtil.getSetYear(),
          CommonUtil.getRgCode());
        idCache.addCacheObject(idKey, loadedAccount);
        codeCache.addCacheObject(codeKey, loadedAccount);
        returnOne = loadBusVouAccount(loadedAccount.getAccountId());
      }
    }
    return returnOne;
  }

  public List allBusVouAccount() {

    clearCache();

    List allAcc = accountServiceImpl.allBusVouAccount();
    List returnList = new LinkedList();

    for (int i = 0; i < allAcc.size(); i++) {
      final BusVouAccount account = (BusVouAccount) allAcc.get(i);
      returnList.add(loadBusVouAccount(account.getAccountId()));
    }
    Collections.sort(returnList, new Comparator() {
      public int compare(Object o1, Object o2) {
        final BusVouAccount as1 = (BusVouAccount) o1;
        final BusVouAccount as2 = (BusVouAccount) o2;
        return as1.getAccountCode().compareTo(as2.getAccountCode());
      }
    });
    return returnList;
  }

  public void initalie() {
    allBusVouAccount();
  }

  public void updateBusVouAccount(BusVouAccount oldAccount, BusVouAccount newAccount) {
    accountServiceImpl.updateBusVouAccount(oldAccount, newAccount);
    clearCache();
  }

  public void saveBusVouAccount(BusVouAccount bvAccount) {
    accountServiceImpl.saveBusVouAccount(bvAccount);
    idCache.addCacheObject(bvAccount.getKey(), bvAccount);
    codeCache.addCacheObject(bvAccount.getCodeKey(), bvAccount);
  }

  public void deleteBusVouAccount(BusVouAccount bvAccount) {
    accountServiceImpl.deleteBusVouAccount(bvAccount);
    idCache.removeCacheObject(bvAccount.getKey());
    codeCache.removeCacheObject(bvAccount.getCodeKey());
    clearCache();
  }

  public void reinstallAccount(List accountList) {
    final Iterator iterator = accountList.iterator();
    BusVouAccount account = null;
    BusVouAccount tempAccount = null;
    CommonKey key = null;
    while (iterator.hasNext()) {
      account = (BusVouAccount) iterator.next();
      key = new CommonKey(account.getAccountCode(), String.valueOf(account.getSetYear()), account.getRgCode());
      if (codeCache.getCacheItem(key) == null)
        continue;
      //此处是为新导入数据重新赋值科目ID。
      tempAccount = (BusVouAccount) ((CachedObject) codeCache.getCacheItem(key)).getObjectCached();
      if (tempAccount != null)
        account.setAccountId(tempAccount.getAccountId());
    }

    accountServiceImpl.reinstallAccount(accountList);
    initalie();
  }

  public void clearCache() {
    this.idCache.clear();
    this.codeCache.clear();
    EngineConfiguration.getConfig().clearBvTypeCache();
  }

  /**
   * 存缓中取科目编码对应的科目id
   */
  public String getId(String accountCode, String rg_code, String set_year) {
    // TODO Auto-generated method stub
    CommonKey key = new CommonKey(accountCode, set_year, rg_code);
    if (codeCache.getCacheItem(key) == null)
      return null;
    else
      return ((BusVouAccount) ((CachedObject) codeCache.getCacheItem(key)).getObjectCached()).getAccountId();
  }
}
