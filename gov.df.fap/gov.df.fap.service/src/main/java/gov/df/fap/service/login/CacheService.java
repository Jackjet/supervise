package gov.df.fap.service.login;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import gov.df.fap.api.login.ICacheService;

@Component("CacheService")
public class CacheService implements ICacheService {

  // 读个数
  private final AtomicInteger readNodeCount;
  
  // 写个数
  private final AtomicInteger writeNodeCount;

  public CacheService() {
    this.readNodeCount = new AtomicInteger();
    this.writeNodeCount = new AtomicInteger();
  }
  
//  public CacheService(String name, String type, long maxsize, boolean todisk, boolean cluster, String cacheeventlistener, boolean platform, boolean globle){
//    this.hitNodeCount = new AtomicInteger();
//    this.updateNodeCount = new AtomicInteger();
//  }

  @Override
  public Object get(String key) {
    this.readNodeCount.getAndIncrement();
    return this.get(key);
  }

  @Override
  public void put(String key, Object value) throws RuntimeException {
    this.writeNodeCount.getAndIncrement();
    this.put(key, value);
  }

}
