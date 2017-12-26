package gov.df.fap.service.util.gl.configure;

import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.api.gl.configure.AccountService;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.configure.BusVouType;
import gov.df.fap.service.util.exceptions.gl.AlreadCloseMonthEndException;
import gov.df.fap.service.util.exceptions.gl.ExecutingMonthlyBalanceException;

import java.util.List;

public interface EngineConfigurationService {

  //  public abstract void setBusVouService(IBusVouTypeService busVouService);

  public abstract void setAccountService(AccountService accountService);

  /**
   * 读取所有科目
   * @return 科目对象
   */
  public abstract List getAllAccount();

  /**
   * 根据科目ID读取科目对象
   * @param accountId 科目ID
   * @return 科目对象
   */
  public abstract BusVouAccount getAccount(long accountId);

  /**
   * 读取所有记账模板
   * @return 记账模板对象
   */
  public abstract List getAllBusVouType();

  /**
   * 根据记账模板ID读取记账模板
   * @param busVouTypeId
   * @return
   */
  public abstract BusVouType getBusVouType(long busVouTypeId);

  /**
   * 读取所有COA
   * @return COA对象
   */
  public abstract List getAllCoa();

  /**
   * 根据COA ID读取COA对象
   * @param coaId COA ID
   * @return COA对象
   */
  public abstract FCoaDTO getCoa(long coaId);

  /**
   * 配置是否启用要素缓存
   * @param cacheEleSet
   */
  public abstract void setElementSetCache(boolean cacheEleSet);

  /**
   * 配置是否使用结果缓存
   * @param resultCache
   */
  public abstract void setNeedResultCache(boolean resultCache);

  /**
   * 是否启用了要素缓存
   * @return
   */
  public abstract boolean isElementSetCache();

  /**
   * 是否启用了基础数据缓存
   * @return
   */
  public abstract boolean isElementSourceCache();

  /**
   * 配置是否使用结果缓存
   * @return
   */
  public abstract boolean isNeedResultCache();

  /**
   * 配置是否启用要素缓存
   * @param elementSourceCache
   */
  public abstract void setElementSourceCache(boolean elementSourceCache);

  /**
   * 指定要缓存的要素
   * @param cachedList
   */
  public abstract void setCachedEleList(List cachedList);

  /**
   * 是否开户CCID生成缓存
   * @return
   */
  public abstract boolean isEnableCcidGenCache();

  /**
   * 设置是否开户CCID生成缓存
   * @param enableCcidGenCache
   */
  public abstract void setEnableCcidGenCache(boolean enableCcidGenCache);

  /**
   * 是否开户CCID转换缓存
   * @return
   */
  public abstract boolean isEnableCcidTransCache();

  /**
   * 设置是否开始CCID转换缓存
   * @param enableCcidTransCache
   */
  public abstract void setEnableCcidTransCache(boolean enableCcidTransCache);

  /**
   * 是否延迟加载基础数据缓存
   * @return
   */
  public abstract boolean isLazyLoadCache();

  /**
   * 设置是否延迟加载基础数据缓存
   * @param lazyLoadCache
   */
  public abstract void setLazyLoadCache(boolean lazyLoadCache);

  /**
   * 允许CCID生成的模糊匹配
   * @return
   */
  public abstract boolean isAllowElementFuzzyMatch();

  /**
   * 设置CCID生成的模糊匹配
   * @param allowElementFuzzyMatch
   */
  public abstract void setAllowElementFuzzyMatch(boolean allowElementFuzzyMatch);

  /**
   * 交易凭证与科目直接关联,就是使用交易凭证表上的控制类型字段
   * @return
   */
  public abstract boolean isBillTypeAccountAssociate();

  /**
   * 交易凭证与科目直接关联,就是使用交易凭证表上的控制类型字段
   * @param billTypeAccountAssociate
   */
  public abstract void setBillTypeAccountAssociate(boolean billTypeAccountAssociate);

  /**
   * 是否备份日志表
   * @return
   */
  public boolean isBackupJournal();

  /**
   * 设置备份日志表
   * @param isBackup
   */
  public void setBackupJournal(boolean isBackup);

  /**
   * 是否使用ccid_trans表
   * @return
   */
  public boolean isUseCcidTransTable();

  /**
   * 设置是否使用ccid_trans表
   * @param useCcidTransTable
   */
  public void setUseCcidTransTable(boolean useCcidTransTable);

  /**
   * 要素是否已经配置缓存
   * @param eleCode
   * @return
   */
  public abstract boolean isEleCached(String eleCode);

  /**清除科目缓存*/
  public abstract void clearAccountCache();

  public abstract void afterPropertiesSet() throws Exception;

  /**
   * 保存总账引擎整体配置
   * @param graph 图形
   * @param bvTypeList 记账模板
   * @param accountList 科目
   * @param coaList COA
   * @throws Exception
   * @author huanglifeng
   * @since 6.2.61.07
   */
  public abstract void reinstallAllConfiguration(byte[] graph, final List bvTypeList, final List accountList,
    final List coaList) throws Exception;

  public abstract void setCoaService(ICoaService coaService);

  /**
   * 取缓存要素列表
   * @return
   */
  public List getCachedEleList();

  /**
   * 清空基础数据缓存
   *
   */
  public void clearElementSourceCache();

  /**
   * 开始月结逻辑
   * @throws ExecutingMonthlyBalanceException 正在执行月结逻辑异常
   * @throws AlreadCloseMonthEndException 
   * @throws Exception 
   *
   */
  public void closeMonth() throws ExecutingMonthlyBalanceException, AlreadCloseMonthEndException, Exception;
}