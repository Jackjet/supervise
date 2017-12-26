package gov.df.fap.service.dictionary.elecache;

import gov.df.fap.service.util.dictionary.interfaces.Cache;
import gov.df.fap.service.util.dictionary.interfaces.CacheFactory;


/**
 * 默认缓存的工厂
 * 
 * @author justin
 * @version 2017-03-06
 */
public class DefaultCacheFactory implements CacheFactory {

	private static DefaultCacheFactory defaultCacheFactoryInstance = null;

	static {
		defaultCacheFactoryInstance = new DefaultCacheFactory();
	}

	private DefaultCacheFactory() {
		super();
	}

	public static CacheFactory getInstance() {
		return defaultCacheFactoryInstance;
	}

	public Cache getCacheInstance() {
		return new DefaultCache();
	}

}
