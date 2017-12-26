package gov.df.fap.api.dictionary;

import java.util.Iterator;
import java.util.List;

/**
 * 缓存索引
 * @author 
 * @version
 */
public class Index {

	/**索引的唯一标识*/
	private Object indexKey = null;

	/**维护着缓存key*/
	private List cacheIndex = null; 
	
	public Index(Object indexKey, List cacheIndex){
		
		if (indexKey == null)
			throw new NullPointerException("cache index identity can not be null!");
		reIndex(cacheIndex);
		
		this.indexKey = indexKey;
		this.cacheIndex = cacheIndex;
	}
	
	public Object getIndexKey(){
		return indexKey;
	}
	
	public int size(){
		return cacheIndex.size();
	}
	
	public Object getCacheItem(int keysIndex){
		return cacheIndex;
	}
	
	public void reIndex(List newIndex){
		if (newIndex == null)
			throw new NullPointerException("cacheKeys can not be null!");
		this.cacheIndex = newIndex;
	}
	
	public Iterator iterator(){
		return cacheIndex.iterator();
	}
}
