package gov.df.fap.service.dictionary.element;

import gov.df.fap.api.dictionary.ElementConfigurationService;

import java.util.ArrayList;
import java.util.List;

/**
 * 引擎配置接口,从该接口可以读取记账模板,COA,科目等配置信息,该接口只提供读方法
 * 
 * 在版本将基础数据缓存的配置放到本类中，包括设置要素缓存、基础数据缓存、基础数据
 * 结果缓存.<p>
 * 
 * @author 
 * @version 
 */
public class ElementConfiguration implements ElementConfigurationService {

  private static boolean isStaticInit = false;

  private static ElementConfiguration configInstance = null;

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

  public ElementConfiguration() {
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
  public static ElementConfiguration getConfig() {
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
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#isEleCached(java.lang.String)
   */
  public boolean isEleCached(String eleCode) {
    if (cachedEleList == null || cachedEleList.isEmpty())//默认缓存所有要素
      return true;
    else
      return cachedEleList.contains(eleCode) || cachedEleList.contains(eleCode.toUpperCase())
        || cachedEleList.contains(eleCode.toLowerCase());
  }

  //	public void clearElementSourceCache(){
  //		eleOpWrapper.clearElementSrcCache();
  //	}

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.configure.EngineConfigurationService#afterPropertiesSet()
   */
  //	public void afterPropertiesSet() throws Exception {
  //		if (!isLazyLoadCache()){
  //			logger.info("采用服务启动加载基础数据缓存模式, 开始加载基础数据缓存");
  //			if (cachedEleList == null)
  //				return;
  //			for (int i = 0; i < cachedEleList.size(); i++){
  //				final String upperEleCode = ((String) cachedEleList.get(i)).toUpperCase();
  //				eleOpWrapper.loadELementSourceCache(upperEleCode);
  //				logger.info("加载要素"+upperEleCode+"缓存");
  //			}
  //			logger.info("基础数据缓存加载结束");
  //		}
  //	}

}
