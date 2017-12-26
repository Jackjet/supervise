package gov.df.fap.service.redis.pool;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisPool {

  private Jedis jedis;//非切片额客户端连接

  private static JedisPool jedisPool = null;//非切片连接池

  /**
   * 初始化非切片池
   */
  private static JedisPool getPool() {
    // 池基本配置 
    if (jedisPool == null) {
      JedisPoolConfig config = new JedisPoolConfig();
      //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；  
      //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。 
      config.setMaxActive(200);
      //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。 
      config.setMaxIdle(5);
      //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；  
      config.setMaxWait(1000l);
      //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
      config.setMinEvictableIdleTimeMillis(1800000);
      config.setTestOnBorrow(false);

      jedisPool = new JedisPool(config, "127.0.0.1", 6379);
    }
    return jedisPool;
  }

  /** 
   * 返还到连接池 
   *  
   * @param pool  
   * @param redis 
   */
  public static void returnResource(JedisPool pool, Jedis redis) {
    if (redis != null) {
      pool.returnResource(redis);
    }
  }

  /** 
   * 获取数据 
   *  
   * @param key 
   * @return 
   */
  public Object get(String key) {
    String value = null;

    JedisPool pool = null;
    Jedis jedis = null;
    try {
      pool = getPool();
      jedis = pool.getResource();
      value = jedis.get(key);
    } catch (Exception e) {
      //释放redis对象  
      pool.returnBrokenResource(jedis);
      e.printStackTrace();
    } finally {
      //返还到连接池  
      returnResource(pool, jedis);
    }

    return value;
  }

  /** 
   *放入数据
   *  
   * @param key 
   * @return 
   */
  public String set(String key, Object obj) {
    String value = null;
    String flag = "0";
    JedisPool pool = null;
    Jedis jedis = null;
    try {
      pool = getPool();
      jedis = pool.getResource();
      jedis.set(key, obj.toString());
      flag = "1";
    } catch (Exception e) {
      //释放redis对象  
      pool.returnBrokenResource(jedis);
      e.printStackTrace();
    } finally {
      //返还到连接池  
      returnResource(pool, jedis);
    }

    return flag;
  }

  public boolean del(String key) {
    JedisPool pool = null;
    Jedis jedis = null;
    try {
      pool = getPool();
      jedis = pool.getResource();
      jedis.del(key);
    } catch (Exception e) {
      //释放redis对象  
      pool.returnBrokenResource(jedis);
      e.printStackTrace();
    } finally {
      //返还到连接池  
      returnResource(pool, jedis);
    }
    return false;
  }

  public boolean exist(String key) {
    JedisPool pool = null;
    Jedis jedis = null;
    try {
      pool = getPool();
      jedis = pool.getResource();
      boolean a = jedis.exists(key);
      return a;
    } catch (Exception e) {
      //释放redis对象  
      pool.returnBrokenResource(jedis);
      e.printStackTrace();
    } finally {
      //返还到连接池  
      returnResource(pool, jedis);
    }
    return false;
  }
}
