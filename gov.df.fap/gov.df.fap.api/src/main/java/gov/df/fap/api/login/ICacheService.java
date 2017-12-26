package gov.df.fap.api.login;

public interface ICacheService {

  public Object get(String key);
  
  public void put(String key, Object value);
  
}
