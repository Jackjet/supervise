package gov.df.fap.service.util.dictionary.interfaces;

/**
 * 在缓存容器中所缓存对象的一个载体
 * @author 
 * @version 
 */
public interface CachedObject {

	/**
	 * get the created time.
	 * @return create time of long.
	 */
	public long getCreateTime();
	
	/**
	 * get the cache key.
	 * @return
	 */
	public Object getKey();
	
	/**
	 * get the last hit time
	 * @return
	 */
	public long getLastHitTime();
	
	/**
	 * 
	 *
	 */
	public void setLastHitTime(long lastHitTime);
	
	/**
	 * get hit count
	 * @return
	 */
	public long getHitCount();
	
	/**
	 * set hit count
	 * @param hitCount
	 */
	public void setHitCount(long hitCount);
	
	/**
	 * get the actually cache object.
	 * @return object was cached.
	 */
	public Object getObjectCached();

	/**
	 * 该对象的大小
	 * @return
	 */
	public int size();
}
