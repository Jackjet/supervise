package gov.df.fap.service.gl.configure.impl;

import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.api.gl.configure.AccountService;
import gov.df.fap.api.rule.IRuleConfigure;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.configure.BusVouAcctmdl;
import gov.df.fap.bean.gl.configure.BusVouType;
import gov.df.fap.bean.rule.dto.RightGroupDTO;
import gov.df.fap.bean.rule.dto.RuleDTO;
import gov.df.fap.service.dictionary.element.ElementOperationWrapperBO;
import gov.df.fap.service.gl.balance.impl.BalanceService;
import gov.df.fap.service.util.exceptions.gl.AlreadCloseMonthEndException;
import gov.df.fap.service.util.exceptions.gl.ExecutingMonthlyBalanceException;
import gov.df.fap.service.util.gl.configure.IBusVouTypeService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 引擎配置接口,从该接口可以读取记账模板,COA,科目等配置信息,该接口只提供读方法.<p>
 * 
 * 在版本将基础数据缓存的配置放到本类中，包括设置要素缓存、基础数据缓存、基础数据
 * 结果缓存.<p>
 * 
 * @author justin
 * @version 
 */
@Component
public class EngineConfiguration {

  private static Log logger = LogFactory.getLog(EngineConfiguration.class);

  private static boolean isStaticInit = false;

  @Autowired
  private static EngineConfiguration configInstance = null;

  @Autowired
  @Qualifier("busVouTypeServiceWrapper")
  private IBusVouTypeService busVouService;

  @Autowired
  @Qualifier("accountServiceWrapper")
  private AccountService accountService = null;

  @Autowired
  private static ICoaService coaService = null;

  @Autowired
  private static BalanceService balanceService = null;

  @Autowired
  private static IRuleConfigure ruleConfigureService = null;

  /**基础数据缓存封装*/
  private ElementOperationWrapperBO eleOpWrapper = null;

  /**是否缓存要素信息*/
  private boolean elementSetCache = false;

  /**是否缓存要素数据源内数据*/
  private boolean elementSourceCache = false;

  /**要素缓存*/
  private List cachedEleList = null;

  /**是否需要基础数据查询结果缓存*/
  private boolean needResultCache = false;

  /**延迟加载基础数据缓存*/
  private boolean lazyLoadCache = true;

  /**是否启用CCID生成缓存*/
  private boolean enableCcidGenCache = true;

  /**是否启用CCID转换缓存*/
  private boolean enableCcidTransCache = true;

  /**允许进行模糊匹配*/
  private boolean allowElementFuzzyMatch = true;

  /**使用交易凭证表上的科目类型进行额度查询*/
  private boolean billTypeAccountAssociate = false;

  /**是否备份日志*/
  private boolean backupJournal = true;

  /**使用ccid_trans表加速*/
  private boolean useCcidTransTable = true;

  private List budgetAssociateStream = null;

  private List traceBalanceAssociateStream = null;

  private List traceSurfaceAssociateStream = null;

  //  /* (non-Javadoc)
  //   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#setBusVouService(gov.gfmis.fap.gl.configure.impl.BusVouTypeServiceWrapper)
  //   */
  //  public void setBusVouService(IBusVouTypeService busVouService) {
  //    busVouService = (BusVouTypeServiceWrapper) busVouService;
  //  }
  //
  //  public void setBusVouService(BusVouTypeServiceWrapper busVouService) {
  //    busVouService = busVouService;
  //  }

  public void setRuleConfigureService(IRuleConfigure ruleConfigureService) {
    EngineConfiguration.ruleConfigureService = ruleConfigureService;
  }

  /* (non-Javadoc)
  * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#setAccountService(gov.gfmis.fap.gl.configure.impl.AccountServiceWrapper)
  */
  public void setAccountService(AccountService accountService) {
    accountService = accountService;
  }

  public void setEleOpWrapper(ElementOperationWrapperBO w) {
    this.eleOpWrapper = w;
  }

  public void setBalanceService(BalanceService balanceService) {
    EngineConfiguration.balanceService = balanceService;
  }

  public EngineConfiguration() {
    if (isStaticInit) {
      throw new RuntimeException("EngineConfiguration could not be initialized twist");
    } else {
      configInstance = this;
      isStaticInit = true;
    }
  }

  /**
   * 取出配置实例
   * @return 总账引擎配置信息
   */
  public static EngineConfiguration getConfig() {
    if (configInstance == null)
      throw new NullPointerException("gl engine configuration need initialized!!");
    return configInstance;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#getAllAccount()
   */
  public List getAllAccount() {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#getAccount(long)
   */
  public BusVouAccount getAccount(long accountId) {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#getAllBusVouType()
   */
  public List getAllBusVouType() {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#getBusVouType(long)
   */
  public BusVouType getBusVouType(long busVouTypeId) {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#getAllCoa()
   */
  public List getAllCoa() {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#getCoa(long)
   */
  public FCoaDTO getCoa(long coaId) {
    throw new UnsupportedOperationException();
  }

  public boolean isUseCcidTransTable() {
    return useCcidTransTable;
  }

  public void setUseCcidTransTable(boolean useCcidTransTable) {
    this.useCcidTransTable = useCcidTransTable;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#setElementSetCache(boolean)
   */
  public void setElementSetCache(boolean cacheEleSet) {
    this.elementSetCache = cacheEleSet;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#setNeedResultCache(boolean)
   */
  public void setNeedResultCache(boolean resultCache) {
    this.needResultCache = resultCache;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#isElementSetCache()
   */
  public boolean isElementSetCache() {
    return elementSetCache;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#isElementSourceCache()
   */
  public boolean isElementSourceCache() {
    return elementSourceCache;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#isNeedResultCache()
   */
  public boolean isNeedResultCache() {
    return needResultCache;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#setElementSourceCache(boolean)
   */
  public void setElementSourceCache(boolean elementSourceCache) {
    this.elementSourceCache = elementSourceCache;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#setCachedEleList(java.util.List)
   */
  public void setCachedEleList(List cachedList) {
    this.cachedEleList = cachedList;
  }

  public List getCachedEleList() {
    List list = new ArrayList();
    list.addAll(cachedEleList);
    return cachedEleList;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#isEnableCcidGenCache()
   */
  public boolean isEnableCcidGenCache() {
    return enableCcidGenCache;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#setEnableCcidGenCache(boolean)
   */
  public void setEnableCcidGenCache(boolean enableCcidGenCache) {
    this.enableCcidGenCache = enableCcidGenCache;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#isEnableCcidTransCache()
   */
  public boolean isEnableCcidTransCache() {
    return enableCcidTransCache;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#setEnableCcidTransCache(boolean)
   */
  public void setEnableCcidTransCache(boolean enableCcidTransCache) {
    this.enableCcidTransCache = enableCcidTransCache;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#isLazyLoadCache()
   */
  public boolean isLazyLoadCache() {
    return lazyLoadCache;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#setLazyLoadCache(boolean)
   */
  public void setLazyLoadCache(boolean lazyLoadCache) {
    this.lazyLoadCache = lazyLoadCache;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#isAllowElementFuzzyMatch()
   */
  public boolean isAllowElementFuzzyMatch() {
    return allowElementFuzzyMatch;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#setAllowElementFuzzyMatch(boolean)
   */
  public void setAllowElementFuzzyMatch(boolean allowElementFuzzyMatch) {
    this.allowElementFuzzyMatch = allowElementFuzzyMatch;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#isBillTypeAccountAssociate()
   */
  public boolean isBillTypeAccountAssociate() {
    return billTypeAccountAssociate;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#setBillTypeAccountAssociate(boolean)
   */
  public void setBillTypeAccountAssociate(boolean billTypeAccountAssociate) {
    this.billTypeAccountAssociate = billTypeAccountAssociate;
  }

  public boolean isBackupJournal() {
    return backupJournal;
  }

  public void setBackupJournal(boolean isBackupJournal) {
    this.backupJournal = isBackupJournal;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.co
   * nfigure.EngineConfigurationService#isEleCached(java.lang.String)
   */
  public boolean isEleCached(String eleCode) {
    if (cachedEleList == null || cachedEleList.isEmpty())//默认缓存所有要素
      return true;
    else
      return cachedEleList.contains(eleCode) || cachedEleList.contains(eleCode.toUpperCase())
        || cachedEleList.contains(eleCode.toLowerCase());
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#clearAccountCache()
   */
  public void clearAccountCache() {
    accountService = new AccountServiceWrapper();
    accountService.clearCache();
    clearBvTypeCache();
  }

  public void clearBvTypeCache() {
    busVouService.clearCache();
  }

  public void clearElementSourceCache() {
    eleOpWrapper.clearElementSrcCache();
  }

  /**
   * 读取记账模板类型
   * @param billTypeCode
   * @return
   */
  public BusVouType getBvTypeByBill(String billTypeCode) {
    return busVouService.loadVouTypeByBill(billTypeCode);
  }

  public List allBvType() {
    return busVouService.allBusVouType();
  }

  public List allAccount() {
    return accountService.allBusVouAccount();
  }

  /**
   * 读取记账模板类型
   * @param vouTypeId 记账模板ID
   * @return
   */
  public BusVouType getBvType(long vouTypeId) {
    return busVouService.loadBusVouType(vouTypeId);
  }

  public BusVouAccount getAccountByCode(String accountCode) {
    return accountService.loadBusVouAccountByCode(accountCode);
  }

  public BusVouAccount getAccount(String accountId) {
    return accountService.loadBusVouAccount(accountId);
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#afterPropertiesSet()
   */
  public void afterPropertiesSet() throws Exception {
    if (!isLazyLoadCache()) {
      logger.info("采用服务启动加载基础数据缓存模式, 开始加载基础数据缓存");
      if (cachedEleList == null)
        return;
      for (int i = 0; i < cachedEleList.size(); i++) {
        final String upperEleCode = ((String) cachedEleList.get(i)).toUpperCase();
        eleOpWrapper.loadELementSourceCache(upperEleCode);
        logger.info("加载要素" + upperEleCode + "缓存");
      }
      logger.info("基础数据缓存加载结束");
    }
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#reinstallAllConfiguration(java.util.List, java.util.List, java.util.List)
   */
  public void reinstallAllConfiguration(byte[] graphByte, final List bvTypeList, final List accountList,
    final List coaList) throws Exception {
    busVouService.removeAllBvType();
    busVouService.saveGraphConfig(bvTypeList, accountList, graphByte);//保存的时候也会删除原有科目并插入新的科目
    coaService.reinstallCoa(coaList);
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#setCoaService(gov.gfmis.fap.dictionary.coa.CoaService)
   */
  public void setCoaService(ICoaService coaService) {
    EngineConfiguration.coaService = coaService;
  }

  public List getBudgetAssociateStream() {
    return budgetAssociateStream;
  }

  public void setBudgetAssociateStream(List budgetAssociateStream) {
    this.budgetAssociateStream = budgetAssociateStream;
  }

  public void closeMonth() throws ExecutingMonthlyBalanceException, AlreadCloseMonthEndException, Exception {
    balanceService.closeMonthEnd();
  }

  public List getTraceBalanceAssociateStream() {
    return traceBalanceAssociateStream;
  }

  public void setTraceBalanceAssociateStream(List traceBalanceAssociateStream) {
    this.traceBalanceAssociateStream = traceBalanceAssociateStream;
  }

  public List getTraceSurfaceAssociateStream() {
    return traceSurfaceAssociateStream;
  }

  public void setTraceSurfaceAssociateStream(List traceSurfaceAssociateStream) {
    this.traceSurfaceAssociateStream = traceSurfaceAssociateStream;
  }

  public static BusVouType configureEleCode(final BusVouType bvType) throws Exception {
    //新交易令对象.建立对应关系
    final BusVouType returnType = new BusVouType();
    returnType.setVou_type_id(bvType.getVou_type_id());
    returnType.setVou_type_code(bvType.getVou_type_code());
    returnType.setVou_type_name(bvType.getVou_type_name());
    returnType.setRg_code(bvType.getRg_code());
    returnType.setSet_year(bvType.getSet_year());

    final List list = bvType.getBusVouAcctmdl();
    BusVouAcctmdl acctmdl = null;
    BusVouAcctmdl tmpAcctmdl = null;
    for (int i = 0; i < list.size(); i++) {
      acctmdl = (BusVouAcctmdl) list.get(i);
      tmpAcctmdl = (BusVouAcctmdl) acctmdl.clone();
      tmpAcctmdl.setBvType(returnType);
      returnType.addAcctmdl(tmpAcctmdl);
    }

    final Iterator iterator = bvType.getBusVouAcctmdl().iterator();
    RuleDTO ruleDto = null;
    RightGroupDTO groupDto = null;
    while (iterator.hasNext()) {
      acctmdl = (BusVouAcctmdl) iterator.next();
      ruleDto = ruleConfigureService.getRuleDto(acctmdl.getRule_id());
      final List rightGroupList = ruleDto.right_group_list;
      final List rightTypeList = acctmdl.getRuleEleConfigure();
      for (int i = 0; i < rightGroupList.size(); i++) {
        groupDto = (RightGroupDTO) rightGroupList.get(i);
        final List detailList = groupDto.type_list;
        for (int j = 0; j < detailList.size(); j++)
          if (!rightTypeList.contains(detailList.get(j)))
            rightTypeList.add(detailList.get(j));
      }
    }

    return returnType;
  }

}
