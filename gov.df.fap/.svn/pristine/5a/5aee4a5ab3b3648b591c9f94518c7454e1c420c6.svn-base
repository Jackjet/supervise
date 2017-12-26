/*
 * <p>Copyright 2000 by Spring 1st Software corporation,
 * <p>All rights reserved.
 * <p>版权所有：用友政务软件有限公司
 * <p>未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 * <p>侵权者将受到法律追究。
 */
package gov.df.fap.service.dictionary.elecache;

import gov.df.fap.service.util.dictionary.interfaces.CachedObject;

import java.util.List;

/**
 * 默认缓存实现
 * @author 
 * @version 2017-03-06
 */
public class DefaultCache extends AbstractCache{

	protected DefaultCache(){}
	
	public CachedObject buildCacheObject(Object key, Object userObject) {
		return new DefaultCachedObject(key, userObject, System.currentTimeMillis());
	}

	public List getCacheObjectByIndex(Object indexKey) {
		throw new UnsupportedOperationException();
	}
}
