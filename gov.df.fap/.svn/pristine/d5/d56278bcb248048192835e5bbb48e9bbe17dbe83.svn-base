package factory;

import java.util.Map;

public class SqlFactory {
  public static String getSQL(String appid, String key) {
    return ParseXmlCache.getSql(appid, key);
  }

  public static String getSQL(String appid, String key, Map<String, Object> param) {
    String sql = ParseXmlCache.getSql(appid, key);
    StringBuilder sb = new StringBuilder();
    char[] cs = sql.toCharArray();
    boolean iscontinue = false;
    boolean isAT = false;
    StringBuilder parakey = null;
    for (char c : cs)
      if (c == '@') {
        parakey = new StringBuilder();
        iscontinue = true;
        isAT = true;
      } else {
        if (isAT) {
          isAT = false;
          if (c == '{') {
            continue;
          }
          sb.append('@');
          iscontinue = false;
        }

        if ((iscontinue) && (c == '}')) {
          sb.append(param.get(parakey.toString()));
          iscontinue = false;
          parakey = new StringBuilder();
        } else if (iscontinue) {
          parakey.append(c);
        } else {
          sb.append(c);
        }
      }
    return sb.toString();
  }
}
