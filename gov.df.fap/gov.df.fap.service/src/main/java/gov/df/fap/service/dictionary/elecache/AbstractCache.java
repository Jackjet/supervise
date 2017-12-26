package gov.df.fap.service.dictionary.elecache;

import gov.df.fap.service.util.dictionary.interfaces.Cache;
import gov.df.fap.service.util.dictionary.interfaces.CacheListener;
import gov.df.fap.service.util.dictionary.interfaces.CacheObjectCloner;
import gov.df.fap.service.util.dictionary.interfaces.CacheObjectLoader;
import gov.df.fap.service.util.dictionary.interfaces.CachedObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.FastHashMap;

/**
 *
 * @author 
 * @version 
 */
public abstract class AbstractCache implements Cache {
	
	/**要素缓存*/
  protected FastHashMap cache = new FastHashMap();
	
	/**默认缓存超时时间为10分钟*/
	protected static final long timeoutLimit = 10*60*1000; //毫秒
	
	/**是否清除超时缓存，默认为是*/
	private boolean clearTimeOut = true;
	
	/**是否清除最少访问对象，默认为否*/
	//private boolean clearLessAccess = true;
	
	/**缓存容积,默认为2000个单位, 为0表示无限大*/
	private long cacheCapability = 2000;
	
	/**缓存大小，不能超过最大容量*/
	private long cacheSize = 0;
	
	/**缓存命中次数，用于评估缓存存在意义*/
	private long hitCount = 0;
	
	/**缓存非命中数*/
	private long missCount = 0;
	
	/**是否需要同步，设置同步性能会有所下降*/
	private boolean needSynchronized = false;
	
	/**最近命中时间*/
	private long lastHitTime = 0;
	
	/**缓存侦听器的列表*/
	private List cacheListener = null;
	
	/**数据加载器*/
	private CacheObjectLoader cacheObjectLoader = null;
	
	public void setCacheCapability(int capability) {
		this.cacheCapability = capability;
	}

	/**
	 * 设置是否须要同步，在要素值变更不大的情况建议设置为false,可以提高性能。
	 * @param needSynchronized
	 */
	public void setNeedSynchronized(boolean needSynchronized) {
		this.needSynchronized = needSynchronized;
	}
	
	public boolean getClearTimeOut(){
    return this.clearTimeOut;
  }
	
	public void setClearTimeOut(boolean clear){
		this.clearTimeOut = clear;
	}
	
	/**
	 * 取要素值
	 * @param key 要素的id/code
	 * @return
	 */
	public Object getCacheObject(Object key){
		return getCacheObject(key, new CacheObjectCloner(){
			public Object clone(Object beCloned) {
				return beCloned;
			}
		});
	}
	
	/**
	 * 取要素值
	 * @param key 要素的id/code
	 * @param cloner 克隆器
	 * @return
	 */
	public Object getCacheObject(Object key, CacheObjectCloner cloner) {
		CachedObject item = getCacheItem(key);
		return item == null ? null : cloner.clone(item.getObjectCached());
	}

	public List getCacheObjectsByKeys(List keys){
		return getCacheObjectsByKeys(keys, new CacheObjectCloner(){
			public Object clone(Object beCloned) {
				return beCloned;
			}
		});
	}
	
	/**
	 * 
	 * @param keys
	 * @param cloner
	 * @return
	 */
	public List getCacheObjectsByKeys(List keys, CacheObjectCloner cloner){
		if (keys == null)
			return null;
		Iterator it = keys.iterator();
		List cacheItems = new LinkedList();
		while(it.hasNext()){
			final Object key = it.next();
			try{
				cacheItems.add(cloner.clone(getCacheObject(key)));
			}catch(Exception ex){
				ex.printStackTrace();
			}

		}
		return cacheItems;
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.gfmis.fap.dictionary.service.elecache.Cache#addCacheObject(java.lang.Object, java.lang.Object)
	 */
	public CachedObject addCacheObject(Object key, Object userObject) {
		final CachedObject originalCachedItem = (CachedObject)cache.get(key);
		if (originalCachedItem != null)
			removeCacheObject(originalCachedItem);
		
		if (userObject == null)
			return null;
		
		CachedObject cachedItem = buildCacheObject(key, userObject);
		//while (cacheCapability > 0 && cacheSize()+cachedItem.size()>cacheCapability){
			//if (clearTimeOut) clearTimeOut();
			//if (clearLessAccess) clearLessAccess();
		//}
		
		storeCache(key, cachedItem);
		setCacheSize(cacheSize+cachedItem.size());
		notifyAddListener(cachedItem);
		return cachedItem;
	}
	
	/**
	 * 储存要素
	 * @param cachedItem
	 */
	private void storeCache(Object key, CachedObject cachedItem){
		//缓存要素值by id&&code
		cache.put(key, cachedItem);
		//不再通过code缓存，没有必要
		//cache.put(cachedItem.getChrCode(), cachedItem);
		
//		//来得晚的放在链表后面
//		ageList.addLast(cachedItem);
//		//用得多的放丰链表后面
//		accessList.addLast(cachedItem);
	}
	
	/**
	 * remove cached Object by cache key
	 * @param key
	 * @return
	 */
	public Object removeCacheObject(Object key){
		CachedObject removedObject = (CachedObject) cache.remove(key);
		return removeCacheObject(removedObject);
	}
	
	/**
	 * remove specified Cached Object.CachedObject must override equals() method,
	 * or the <code>removeObject</code> is the instance in cache container.
	 * @param removedObject
	 * @return
	 */
	protected Object removeCacheObject(CachedObject removedObject){
		
		if (removedObject == null)
			return null;
//		System.out.println("删除第一个对象＝"+removedObject);
//		ageList.remove(removedObject);
//		System.out.println("删除第二个对象＝"+removedObject);
//		accessList.remove(removedObject);
		
		setCacheSize(cacheSize - removedObject.size());
		this.notifyReomveUpdate(removedObject);
		return removedObject.getObjectCached();
	}
	
	public long cacheSize() {
		return cacheSize;
	}

	public long objectCount() {
		return cache.size();
	}

	public void clear() {
		cache.clear();
//		accessList.clear();
//		ageList.clear();
	}

	public void clearTimeOut() {
		throw new UnsupportedOperationException();
	}

//	private void clearLessAccess() {
//		//清除10%使用最少的对象
//		int clearSize = accessList.size()/10;
//		while((clearSize--)>0){
//			final CachedObject removedElement = (CachedObject) accessList.getFirst();
//			removeCacheObject(removedElement);
//		}
//	}
	
	public long getHitCount() {
		return hitCount;
	}

	public long getMissCount(){
		return missCount;
	}
	
	protected void setCacheSize(long size){
		cacheSize = size;
	}

	public boolean isNeedSynchronized() {
		return needSynchronized;
	}

	public long getCacheCapability(){
		return cacheCapability;
	}
	
	public void setCacheCapability(long size){
		this.cacheCapability = size;
	}
	
	public  CachedObject getCacheItem(Object key){
		CachedObject element = (CachedObject) cache.get(key);
		if (element == null && cacheObjectLoader != null){
			Object loadedObject = cacheObjectLoader.loaderElement(key);
			CachedObject co = addCacheObject(key, loadedObject);
			return co;
		}
		if (element != null){
			hitCount++;
			element.setHitCount(element.getHitCount()+1);
			lastHitTime = System.currentTimeMillis();
			element.setLastHitTime(lastHitTime);

//			accessList.remove(element);
//			accessList.addLast(element);
			
			return element;
		}else{
			missCount++;
			return null;
		}
	}
	
	public List getCacheItemsByKeys(List keys){
		if (keys == null)
			return null;
		Iterator it = keys.iterator();
		List cacheItems = new LinkedList();
		while(it.hasNext()){
			cacheItems.add(getCacheItem(it.next()));
		}
		return cacheItems;
	}
	
	public void setCacheObjectLoader(CacheObjectLoader loader){
		if (loader == null)
			return;
		this.cacheObjectLoader = loader;
	}
	
	public long getLastHitTime() {
		return lastHitTime;
	}	

	protected void addCacheListener(CacheListener listener){
		if (cacheListener == null)
			cacheListener = new ArrayList();
		
		if (!cacheListener.contains(listener))
			cacheListener.add(listener);
	}
	
	protected void removeCacheListener(CacheListener listener){
		if (cacheListener == null)
			return;
		
			boolean isRemove = cacheListener.remove(listener);
			if (!isRemove)
				throw new RuntimeException("There is no listener removed!"); 
		
	}
	
	protected void notifyAddListener(CachedObject actionItem){
		if (cacheListener == null)
			return;
		
		for (int i = 0; i < cacheListener.size(); i++){
			final CacheListener listener = (CacheListener)cacheListener.get(i);
			listener.addCacheAction(actionItem);
		}
	}
	
	/**
	 * notify all the listeners that one cache item has been removed from the cache container.
	 * @param removedItem
	 */
	protected void notifyReomveUpdate(CachedObject removedItem){
		if (cacheListener == null)
			return;
		
		for (int i = 0; i < cacheListener.size(); i++){
			final CacheListener listener = (CacheListener)cacheListener.get(i);
			listener.removeCacheAction(removedItem);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.gfmis.fap.dictionary.service.elecache.Cache#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key){
		return cache.containsKey(key);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.gfmis.fap.dictionary.service.elecache.Cache#allObjects()
	 */
	public List allObjects(){
		List allCachedObjects = new LinkedList();
		Iterator it = cache.values().iterator();
		while(it.hasNext()){
			final CachedObject item = (CachedObject)it.next();
			allCachedObjects.add(item.getObjectCached());
		}
		return allCachedObjects;
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.gfmis.fap.dictionary.service.elecache.Cache#allObjects()
	 */
	public List allObjects(CacheObjectCloner cloner){
		List allCachedObjects = new LinkedList();
		Iterator it = cache.values().iterator();
		while(it.hasNext()){
			final CachedObject item = (CachedObject)it.next();
			allCachedObjects.add(cloner.clone(item.getObjectCached()));
		}
		return allCachedObjects;
	}
	
	/**
	 * 创建相应缓存的CacheObject缓存对象.
	 * @param key 缓存标识
	 * @param userObject 用户缓存的对象
	 * @return 缓存载体.
	 */
	public abstract CachedObject buildCacheObject(Object key, Object userObject);
}
