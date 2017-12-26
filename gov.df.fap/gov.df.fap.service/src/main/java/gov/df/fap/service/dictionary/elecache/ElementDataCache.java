package gov.df.fap.service.dictionary.elecache;

import gov.df.fap.api.dictionary.ElementCacheKey;
import gov.df.fap.api.dictionary.ElementInfo;
import gov.df.fap.api.dictionary.Index;
import gov.df.fap.api.dictionary.Indexable;
import gov.df.fap.service.util.dictionary.interfaces.Cache;
import gov.df.fap.service.util.dictionary.interfaces.CacheFactory;
import gov.df.fap.service.util.dictionary.interfaces.CacheListener;
import gov.df.fap.service.util.dictionary.interfaces.CacheObjectCloner;
import gov.df.fap.service.util.dictionary.interfaces.CachedObject;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.xml.XMLData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;



/**
 * 要素数据缓存,建议请本类作为全局sinleton处理,因为每次新建本类,都会重新建立缓存,极大降低效率.
 * 同样，如果要素值不频繁变更，可以设置不同步，可以提高性能。
 * 
 * <p>支持缓存Listener,在加入和删除缓存数据时会触发事件.
 * 
 * <p>支持索引,与数据库索引不同,缓存的索引不是针对单个列的,而是针对整个对象,每个索引都指向一个记录集.
 * 类似翻书的时候把关心的页码记录下来,而不是看书的目录.
 * 
 * <p>支持缓存的插入式克隆,因为缓存对象在取出后会有被修改的风险,所以在取出缓存时必须保证缓存不会被修改,最
 * 简单的做法就是克隆出一个缓存对象,但有时可能也会需要修改该对象或者在取出对象的时候进行一些特殊的操作,所以
 * 加入接口<code>CacheObjectCloner</code>,在取出缓存对象同时缓存会回调该接口实现对缓存对象的一些处理.
 * 
 * 
 * @author hlf
 * @version 2007-03-04
 */
public class ElementDataCache extends AbstractCache implements Cache, Indexable, CacheListener, Serializable {
	
	/**
   * 
   */
  private static final long serialVersionUID = 7296160834901570591L;

  private String eleCode = null;
	
	/**要素树的根，在要素树开始构造时，会有很多要素放在根部（因为有可能先把孩子找出来）
	 * 第加一个节点，就在根部找有没有自己的孩子。如果有，就将孩子加入到本节点之下。
	 * 然后再找自己的父节点，如果没有就加入到根节点*/
	protected CachedElement elementTreeRoot = new CachedElement(CachedElement.ROOT_OBJECT); 
	
	protected Cache indexs = null;
	
	protected ElementDataCache(String code){
		this.eleCode = code;
		initIndex();
	}
	
	private void initIndex(){
		CacheFactory cacheFactory = DefaultCacheFactory.getInstance();
		indexs = cacheFactory.getCacheInstance();
	}
	
	/**
	 * 从树上删除节点.
	 * @param cachedItem
	 */
	protected void removeElementFromTree(ElementInfo cachedItem){
		//从父节点中移走
		if (cachedItem.getParent() != null)
			cachedItem.getParent().removeChild(cachedItem);
		//将被删除节点的孩子挂回父节点等待更新.
		if (cachedItem.getChildren() != null
				&& cachedItem.getChildren().size() > 0){
			List children = cachedItem.getChildren();
			for(int i = 0; i < children.size(); i++){
				//将被删除节点的孩子挂回父节点等待更新.
				this.elementTreeRoot.addChild((CachedElement)children.get(i));
			}
			
			ElementInfo parent = cachedItem.getParent(); 
			if (parent != null){
				parent.getChildren().remove(cachedItem);
			}
		}
	}
	
	/**
	 * 将要素添加到要素树上，如果找不到父节点，就先放在根上。
	 * @param cachedItem
	 */
	protected void addToElementTree(CachedElement cachedItem){
		CachedElement parent = null;
		if (cachedItem.getParentId() != null)
			parent = (CachedElement) cache.get(cachedItem.getParentKey());
		if (parent != null)
			parent.addChild(cachedItem);
		else
			elementTreeRoot.addChild(cachedItem);
		
		//找孩子节点
		List rootChildren = elementTreeRoot.getChildren();
		for (int i = 0; i < rootChildren.size(); ){
			CachedElement rootChild = (CachedElement) rootChildren.get(i);
			if (!StringUtil.isEmpty(rootChild.getParentId())
					&& cachedItem.getKey().equals(rootChild.getParentKey())){
				cachedItem.addChild(rootChild);
				continue;
			}
			i++;
		}
	}

	public ElementInfo addCacheElement(XMLData element){
		return (ElementInfo) addCacheObject(new ElementCacheKey( (String)element.get(CachedElement.CHR_ID), (String)element.get(CachedElement.SET_YEAR), (String)element.get(CachedElement.RG_CODE)), element);
	}

	public CachedObject buildCacheObject(Object key, Object userObject) {
		return new CachedElement((XMLData)userObject);
	}
	
	public String getEleCode(){
		return eleCode;
	}

	/**
	 * 通过keys生成索引
	 * @param indexKey 索引唯一标识
	 * @param keysList 索引缓存key
	 */
	public void generateIndexByKeys(Object indexKey, List keysList){
		if (keysList == null)
			return;
		
		List index = new ArrayList();
		Iterator keyIt = keysList.iterator();
		while(keyIt.hasNext()){
			index.add(getCacheItem(keyIt.next()));
		}
		
		indexs.addCacheObject(indexKey, new Index(indexKey, index));
	}
	
	public List getCacheObjectByIndex(Object indexKey, CacheObjectCloner cloner){
		Index cacheIndex = (Index)indexs.getCacheObject(indexKey);
		if (cacheIndex == null)
			return null;
		
		List userObjects = new LinkedList();
		Iterator indexCachesIt = cacheIndex.iterator();
		while(indexCachesIt.hasNext()){
			final Object cacheObject = ((CachedObject)indexCachesIt.next()).getObjectCached();
			userObjects.add(cloner.clone(cacheObject));
		}
		return userObjects;
	}
	
	/**
	 * 通过缓存读取索引
	 */
	public List getCacheObjectByIndex(Object indexKey) {
		return getCacheObjectByIndex(indexKey, new CacheObjectCloner(){
			public Object clone(Object beCloned) {
				return beCloned;
			}
		});
	}
	
	/**
	 * 删除索引
	 * @param indexKey
	 */
	public void removeIndex(Object indexKey){
		indexs.removeCacheObject(indexKey);
	}
	
	/**
	 * 清空索引
	 */
	public void clearIndex(){
		indexs.clear();
	}
	
	/*
	 * Implements CacheListner's methods 
	 */
	
	public void addCacheAction(CachedObject actionItem) {
		Object element = actionItem.getObjectCached();
		if (element == null)
			throw new RuntimeException("Can not add a Null element to cache!");
		if (!element.getClass().isAssignableFrom(XMLData.class))
			throw new RuntimeException("Element cache stores Object of Class XMLData only!");
		
		addToElementTree((CachedElement) actionItem);
	}
	
	public void removeCacheAction(CachedObject actionItem){
		removeElementFromTree((ElementInfo)actionItem);
	}

	public Object getCacheAction(CachedObject toGetItem) {
		return null;
	}
}
