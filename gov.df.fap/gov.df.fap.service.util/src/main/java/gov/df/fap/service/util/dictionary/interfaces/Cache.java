package gov.df.fap.service.util.dictionary.interfaces;

import java.util.List;

/**
 * 缓存基本操作.
 * @author 
 * @version 2017-03-24
 *
 */
public interface Cache {

  /**
   * 设置缓存容量
   * @param capability
   */
  public long getCacheCapability();

  /**
   * 设置缓存容量
   * @param capability
   */
  public void setCacheCapability(long capability);

  /**
   * 设置是否需要同步
   * @param needSynchronized
   */
  public boolean isNeedSynchronized();

  /**
   * 取得缓存用户对象
   * @param key
   * @return
   */
  public Object getCacheObject(Object key);

  /**
   * 取得缓存用户对象
   * @param key
   * @return
   */
  public List getCacheObjectsByKeys(List keys);

  /**
   * 取得缓存载体对象
   * @return
   */
  public CachedObject getCacheItem(Object key);

  /**
   * 
   * @param keys
   * @return
   */
  public List getCacheItemsByKeys(List keys);

  /**
   * 缓存一个对象
   * @param key
   * @param obj
   */
  public CachedObject addCacheObject(Object key, Object obj);

  /**
   * 删除一个缓存对象
   * @param key
   * @return
   */
  public Object removeCacheObject(Object key);

  /**
   * 返回缓存的大小
   * @return
   */
  public long cacheSize();

  /**
   * 缓存对象数目
   */
  public long objectCount();

  /**
   * 清空缓存
   *
   */
  public void clear();

  /**
   * 清空超时缓存对象
   *
   */
  public void clearTimeOut();

  /**
   * get total hit count.
   * @return hit count
   */
  public long getHitCount();

  /**
   * get last hit time.
   * @return last hit time of long.
   */
  public long getLastHitTime();

  /**
   * if the cache contains the speified key.
   * @param key
   * @return
   */
  public boolean containsKey(Object key);

  /**
   * get all cached Object
   * @return
   */
  public List allObjects();

  /**
   * get all cached Object
   * @return
   */
  public List allObjects(CacheObjectCloner cloner);
}
