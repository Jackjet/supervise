package gov.df.fap.api.redis;

public interface CacheUtil {

  public void put(String key, String value);

  public void put(String key, Object value);

  public <T> T get(String key, Class<T> className);

  public Object get(String key);

  public boolean exist(String key);

  public boolean delete(String key);

  public String redisflag();

  public String getCacheKey(String element, boolean isNeedRight, String coa_id, String ctrlElementValues,
    String sPlusSQL);

}
