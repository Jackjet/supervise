package gov.df.fap.api.dictionary;

import java.util.List;

public interface ElementConfigurationService {

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
	 * 要素是否已经配置缓存
	 * @param eleCode
	 * @return
	 */
	public abstract boolean isEleCached(String eleCode);

	/**
	 * 取缓存要素列表
	 * @return
	 */
	public List getCachedEleList();
	
	/**
	 * 清空基础数据缓存
	 *
	 */
//	public void clearElementSourceCache();
	
}