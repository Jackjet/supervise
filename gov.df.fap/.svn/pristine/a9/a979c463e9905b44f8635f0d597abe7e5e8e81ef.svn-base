package gov.df.fap.service.dictionary.elecache;

import gov.df.fap.service.util.dictionary.interfaces.CachedObject;



/**
 *
 * @author 
 * @version 2017-03-06
 */
public class DefaultCachedObject implements CachedObject {

	private long createTime = -1;
	
	private Object key = null;
	
	private Object userObject = null;
	
	/**最近命中时间*/
	long lastHitTime = 0;
	/***/
	long hitCount = 0;
	
	DefaultCachedObject(Object key, Object userObject, long createTime){
		this.key = key;
		this.userObject = userObject;
		this.createTime = createTime;
	}
	
	public long getCreateTime() {
		return createTime;
	}

	public Object getObjectCached() {
		return userObject;
	}

	public int size() {
		return 1;
	}
	
	public void setLastHitTime(long time){
		this.lastHitTime = time;
	}
	
	public long getLastHitTime(){
		return lastHitTime;
	}

	public void setHitCount(long count){
		this.hitCount = count;
	}
	
	public long getHitCount() {
		return hitCount;
	}
	
	public Object getKey(){
		return key;
	}
}
