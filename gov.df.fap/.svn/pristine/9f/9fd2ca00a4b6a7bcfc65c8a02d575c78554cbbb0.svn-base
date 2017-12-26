package gov.df.fap.service.redis.pool;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

public class RedisConnect {

  private static String redisflag = "0";

  public synchronized void initialize() {
    InputStream in = null;
    try {
      in = this.getClass().getResourceAsStream("/redis.properties");
      Properties prop = new Properties();
      prop.load(in);
      redisflag = prop.getProperty("useable").trim();
    } catch (Exception e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
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
    if ("0".equals(redisflag)) {
      return;
    }
    String param1 = System.getProperty("user.dir");
    String OS = System.getProperty("os.name").toLowerCase();
    if (OS.indexOf("windows") > -1) {
      try {
        param1 = param1 + File.separator + "redis-windows" + File.separator + "redis-start.bat";
        String[] cmd = { "cmd", "/C", "start /b \"\" \"" + param1 + "\"" };
        Process ps = Runtime.getRuntime().exec(cmd);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } else if (OS.indexOf("linux") > -1) {
      try {
        String command = param1 + File.separator + "redis-3.2.8/src/redis-server";
        Runtime.getRuntime().exec(command);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } else {

    }
  }

  public synchronized void shutDown() {
    if ("0".equals(redisflag)) {
      return;
    }
    String param1 = System.getProperty("user.dir");
    String OS = System.getProperty("os.name").toLowerCase();
    if (OS.indexOf("windows") > -1) {
      try {
        param1 = param1 + File.separator + "redis-windows" + File.separator + "redis-stop.bat";
        String[] cmd = { "cmd", "/C", "start \"\" \"" + param1 + "\"" };
        Runtime.getRuntime().exec(cmd);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } else if (OS.indexOf("linux") > -1) {
      try {
        String command = param1 + File.separator + "redis-3.2.8/src/redis-cli -p 6379 shutdown";
        Runtime.getRuntime().exec(command);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    } else {

    }
  }
}
