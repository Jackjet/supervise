package gov.df.fap.service.dictionary.elecache;

import gov.df.fap.service.util.memcache.MemCacheMap;

import java.io.Serializable;
import java.util.Map;

/**
 * ElementDataMemCache缓存实现
 * 
 * @author 
 * @version 
 */
public class ElementDataMemCache extends ElementDataCache implements
		Serializable {

	private static final long serialVersionUID = 1L;

	protected Map cache = MemCacheMap.getCacheMap(ElementDataMemCache.class);

	protected ElementDataMemCache(String code) {
		super(code);
	}
}
