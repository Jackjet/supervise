package gov.df.fap.service.util.memcache;

import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.service.util.sessionmanager.SessionUtil;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MemCacheMap extends AbstractMap implements Map, Serializable {
  private static final long serialVersionUID = 1L;

  private static MemCachedClient memcache = null;

  private static Map dataCacheMap = new HashMap();

  private String preKey = "";// 前缀

  private MemCacheMap() {
    super();
  }

  //  private static String isUseMemcache = (String) SessionUtil.getParaMap().get("IS_USE_MEM_CACHE");
  private static String isUseMemcache = "1";

  // SYS_USERPARA中配置 + MemCache服务正常启动
  public static boolean isUseMemCache() {
    boolean isUse = "1".equals(isUseMemcache);
    return isUse && memCacheIsNormalStart();
  }

  private static SockIOPool pool = (SockIOPool) SessionUtil.getServerBean("memcachedPool");

  // MemCache服务是否正常启动
  private static boolean memCacheIsNormalStart() {
    return pool.memCacheIsNormalStart();
    //return false;
  }

  /**
   * @param classz  当前调用类的class 如：ElementOperationWrapper.class
   * @return 返回缓存Map
   */

  public static Map getCacheMap(Class classz) {
    return getCacheMap(classz, "");
  }

  /**
   * @param classz
   * @param specialKey  特殊标识
   * @return
   */
  public static Map getCacheMap(Class classz, String specialKey) {
    if (isUseMemCache()) {
      return createMemCacheMap(classz.getName() + specialKey);
    } else {
      return new Hashtable();
    }
  }

  /**
   * @param key
   * @return MemCacheMap
   */
  private static MemCacheMap createMemCacheMap(String key) {
    String tempKey = key + CommonUtil.getIntSetYear() + SessionUtil.getRgCode();
    MemCacheMap cacheMap = (MemCacheMap) dataCacheMap.get(tempKey);
    if (cacheMap == null) {
      cacheMap = new MemCacheMap();
      cacheMap.init(key, CommonUtil.getIntSetYear());
      dataCacheMap.put(tempKey, cacheMap);
    }
    return cacheMap;
  }

  /**
   * @param key
   * @param setYear
   */
  private void init(String key, int setYear) {
    this.preKey = key + setYear + SessionUtil.getRgCode();
    if (memcache == null) {
      memcache = (MemCachedClient) SessionUtil.getServerBean("memcachedClient");
    }
    memcache.add(this.preKey, new HashSet());// 用于记录preKey前缀下所有的缓存的key
  }

  public boolean containsKey(Object key) {
    return memcache.keyExists(this.preKey + key.toString());
  }

  public boolean containsValue(Object value) {
    return getAllDataToMap().containsValue(value);
  }

  public Set entrySet() {
    return getAllDataToMap().entrySet();
  }

  public Object get(Object key) {
    return memcache.get(this.preKey + key.toString());
  }

  public boolean isEmpty() {
    return getPreKeySet().isEmpty();
  }

  // 取所有以this.preKey为前缀的key
  private Set getPreKeySet() {
    Set set = (Set) memcache.get(this.preKey);
    return set == null ? new HashSet() : set;
  }

  public Set keySet() {
    return getPreKeySet();
  }

  public Object put(Object key, Object value) {
    memcache.add(this.preKey + key.toString(), value);
    Set keySet = getPreKeySet();
    keySet.add(key.toString());
    if (memcache.keyExists(this.preKey)) {
      memcache.replace(this.preKey, keySet);
    } else {
      memcache.add(this.preKey, keySet);
    }
    return null;
  }

  public void putAll(Map t) {
    Set keySet = getPreKeySet();
    Iterator it = t.keySet().iterator();
    while (it.hasNext()) {
      Object key = it.next();
      Object value = t.get(key);
      keySet.add(key.toString());
      memcache.add(this.preKey + key.toString(), value);
    }
    if (keySet.size() > 0) {
      if (memcache.keyExists(this.preKey)) {
        memcache.replace(this.preKey, keySet);
      } else {
        memcache.add(this.preKey, keySet);
      }
    }
  }

  public Object remove(Object key) {
    memcache.delete(this.preKey + key.toString());
    if (memcache.keyExists(this.preKey)) {
      Set keySet = getPreKeySet();
      keySet.remove(key.toString());
      if (keySet.size() > 0) {
        memcache.replace(this.preKey, keySet);
      }
    }
    return null;
  }

  public int size() {
    return getPreKeySet().size();
  }

  public Collection values() {
    return getAllDataToMap().values();
  }

  public Object[] getMultiArray(Object[] keys) {
    String[] tempKeys = new String[keys.length];
    for (int i = 0; i < keys.length; i++) {
      tempKeys[i] = this.preKey + keys[i].toString();
    }
    return keys.length == 0 ? tempKeys : memcache.getMultiArray(tempKeys);
  }

  public Map getMulti(Object[] keys) {
    String[] tempKeys = new String[keys.length];
    for (int i = 0; i < keys.length; i++) {
      tempKeys[i] = this.preKey + keys[i].toString();
    }
    return keys.length == 0 ? new HashMap() : memcache.getMulti(tempKeys);
  }

  public Object[] getAllDataToArray() {
    Object[] keys = getPreKeySet().toArray();
    return getMultiArray(keys);
  }

  public Map getAllDataToMap() {
    Object[] keys = getPreKeySet().toArray();
    return getMulti(keys);
  }

  public boolean replace(String key, Object value) {
    memcache.replace(this.preKey + key.toString(), value);
    return true;
  }

  public void clear() {
    Set keySet = getPreKeySet();
    Object[] keys = keySet.toArray();
    if (keys.length > 0) {
      memcache.delete(keys);
      keySet.clear();
      memcache.replace(this.preKey, keySet);
    }
  }

}
