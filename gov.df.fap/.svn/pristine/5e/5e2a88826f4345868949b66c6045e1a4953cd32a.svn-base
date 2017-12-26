package gov.df.fap.service.dictionary.elecache;

import gov.df.fap.service.util.dictionary.interfaces.Cache;
import gov.df.fap.service.util.dictionary.interfaces.CacheFactory;
import gov.df.fap.service.util.memcache.MemCacheMap;
import gov.df.fap.util.StringUtil;

/**
 * 要素缓存工厂
 * @author 
 * @version 
 */
public class ElementCacheFactory implements CacheFactory {

	private static ElementCacheFactory elementCacheFactoryInstance = null;

	static {
		elementCacheFactoryInstance = new ElementCacheFactory();
	}

	private ElementCacheFactory() {
	}

	public static CacheFactory getInstance() {
		return elementCacheFactoryInstance;
	}

	public Cache getCacheInstance() {
		ElementDataCache cache = null;
		if (MemCacheMap.isUseMemCache()) {
			cache = new ElementDataMemCache(StringUtil.EMPTY);// 分布式缓存
		} else {
			cache = new ElementDataCache(StringUtil.EMPTY);
		}
		cache.addCacheListener(cache);
		return cache;
	}

}
