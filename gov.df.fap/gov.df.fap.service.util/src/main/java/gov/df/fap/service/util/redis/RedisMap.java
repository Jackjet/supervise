package gov.df.fap.service.util.redis;

import gov.df.fap.api.redis.CacheUtil;
import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.service.util.sessionmanager.SessionUtil;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RedisMap extends AbstractMap implements Map, Serializable {

  @Autowired
  @Qualifier("df.cacheUtil")
  private CacheUtil cacheUtil;

  private String preKey = "";// 前缀

  private static String isRedisCache = "1";

  @Override
  public Set entrySet() {
    // TODO Auto-generated method stub
    return null;
  }

  private RedisMap() {
    super();
    //    isRedisCache = cacheUtil.redisflag();
  }

  public static Map getCacheMap(Class classz) {
    return getCacheMap(classz, "");
  }

  public static boolean existRedis() {
    return true;
  }

  /**
   * @param classz
   * @param specialKey  特殊标识
   * @return
   */
  public static Map getCacheMap(Class classz, String specialKey) {
    if (isRedisCatche()) {
      return createRedisMap(classz.getName() + specialKey);
    } else {
      return new Hashtable();
    }
  }

  /**
   * @param key
   * @return RedisMap
   */
  private static RedisMap createRedisMap(String key) {
    String tempKey = key + CommonUtil.getIntSetYear() + SessionUtil.getRgCode();
    return null;
  }

  public static boolean isRedisCatche() {
    boolean isUse = "1".equals(isRedisCache);
    return isUse;
  }

  public Object put(Object key, Object value) {
    cacheUtil.put(this.preKey + key.toString(), value);
    Set keySet = getPreKeySet();
    keySet.add(key.toString());
    cacheUtil.put(this.preKey, keySet);
    return null;
  }

  public Object get(Object key) {
    if (key == null) {
      return null;
    }
    String tKey = this.preKey + key.toString();
    Object res = cacheUtil.get(tKey);
    return res;
  }

  // 取所有以this.preKey为前缀的key
  private Set getPreKeySet() {
    Set set = cacheUtil.get(this.preKey, Set.class);
    return set == null ? new HashSet() : set;
  }

}
