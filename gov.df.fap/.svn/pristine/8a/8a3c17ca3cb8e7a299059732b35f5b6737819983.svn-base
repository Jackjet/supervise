package gov.df.fap.service.redis;

import gov.df.fap.api.redis.CacheUtil;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component("df.cacheUtil")
public class CacheUtilImpl implements CacheUtil {

  @Autowired
  private StringRedisTemplate redisTemplate;//redis操作模板  

  private static Logger log = Logger.getLogger(CacheUtilImpl.class.getName());

  private static String cacheflag = "0";

  public CacheUtilImpl() {
    InputStream in = null;
    try {
      in = this.getClass().getResourceAsStream("/redis.properties");
      Properties prop = new Properties();
      prop.load(in);
      cacheflag = prop.getProperty("useable").trim();
    } catch (Exception e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
      cacheflag = "0";
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }

  }

  public void put(String key, String value) {
    if (key == null || "".equals(key) || "0".equals(cacheflag)) {
      return;
    }
    redisTemplate.opsForHash().put(key, key, value);

  }

  public void put(String key, Object value) {
    if (key == null || "".equals(key) || "0".equals(cacheflag)) {
      return;
    }
    redisTemplate.opsForHash().put(key, key, new Gson().toJson(value));

  }

  public <T> T get(String key, Class<T> className) {
    if (key == null || "".equals(key) || "0".equals(cacheflag)) {
      return null;
    }
    Object obj = redisTemplate.opsForHash().get(key, key);
    if (obj == null) {
      return null;
    }
    return new Gson().fromJson("" + obj, className);
  }

  public Object get(String key) {
    if (StringUtil.isNull(key) || "0".equals(cacheflag)) {
      return null;
    }
    Object obj = redisTemplate.opsForHash().get(key, key);
    return obj;
  }

  public boolean exist(String key) {
    if (key == null || "".equals(key) || "0".equals(cacheflag)) {
      return false;
    }
    if (key == null) {
      log.error("key is null for exist()");
      return false;
    }
    try {
      if (get(key) == null) {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean delete(String key) {
    if (key == null || "".equals(key) || "0".equals(cacheflag)) {
      return false;
    }
    if (key == null) {
      log.error("key is null for delete()");
      return false;
    }
    redisTemplate.delete(key);
    return true;
  }

  public String redisflag() {
    return cacheflag;
  }

  /**
   * 
   * @param element  要素简称
   * @param isNeedRight  权限
   * @param coa_id  
   * @param ctrlElementValues 编码
   * @param sPlusSQL  条件
   * @return
   */
  public String getCacheKey(String element, boolean isNeedRight, String coa_id, String ctrlElementValues,
    String sPlusSQL) {
    String cacheKey = "";
    if (!StringUtil.isNull(element)) {
      cacheKey = element.toLowerCase();
    }
    if (isNeedRight) {
      //权限和角色是一对一关系，因此不再使用权限rule_id，而用角色代替
      if (!StringUtil.isNull(SessionUtil.getUserInfoContext().getRoleID())) {
        cacheKey = cacheKey + "#" + SessionUtil.getUserInfoContext().getRoleID();
      }
    }
    if (!StringUtil.isNull(coa_id)) {
      cacheKey = cacheKey + "#" + coa_id;
    }
    if (!StringUtil.isNull(ctrlElementValues)) {
      cacheKey = cacheKey + "#" + ctrlElementValues;
    }
    if (!StringUtil.isNull(sPlusSQL)) {
      if (sPlusSQL.length() <= 1000) {
        cacheKey = cacheKey + "#" + sPlusSQL.trim();
      }
    }
    return cacheKey;
  }
}
