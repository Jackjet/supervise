package factory;

import java.util.HashMap;
import java.util.Map;

public class ParseXmlCache {
  private static Map<String, Map<String, String>> cache = new HashMap();

  public static void putSql(String appid, String sqlkey, String sql) {
    if (cache.get(appid) == null) {
      cache.put(appid, new HashMap());
    }
    Map map = (Map) cache.get(appid);
    map.get(sqlkey);

    map.put(sqlkey, sql);
  }

  public static String getSql(String appid, String key) {
    Map sqls = (Map) cache.get(appid);
    if (sqls == null) {
      return null;
    }
    return ((String) sqls.get(key));
  }
}