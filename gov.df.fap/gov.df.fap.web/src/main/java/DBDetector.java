import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import com.longtu.framework.springexp.ServerDetector;

public class DBDetector {
  private static final String ORACLE_SQL = "select * from v$version";

  private static final String DM_SQL = "select * from v$version";

  private static final String DB2_SQL = "";

  private static final String MYSQL_SQL = "select version()";

  private static final String SQLSERVER_SQL = "";

  private static final CommonJdbcDaoSupport dao = CommonJdbcDaoSupport.instanceDao();

  private static Log _log = LogFactory.getLog(DBDetector.class);

  private static DBDetector _instance = new DBDetector();

  private DBEnum _dbId;

  private static Map<String, String> JNDIINFO = new HashMap();

  private static boolean isOracle() {
    try {
      List list = dao.queryForList("select * from v$version");
      if ((list.size() > 0) && (!(list.isEmpty()))) {
        Map map = (Map) list.get(0);
        String sys = (String) map.get("banner");
        if (sys.indexOf("Oracle") != -1) {
          return true;
        }
      }
      return false;
    } catch (Exception localException) {
    }
    return false;
  }

  private static boolean isDm() {
    try {
      List list = dao.queryForList("select * from v$version");
      if ((list.size() > 0) && (!(list.isEmpty()))) {
        Map map = (Map) list.get(0);
        String sys = (String) map.get("banner");
        if (sys.indexOf("Oracle") != -1) {
          return false;
        }
      }
      return true;
    } catch (Exception localException) {
    }
    return false;
  }

  private static boolean isMySql() {
    try {
      dao.queryForList("select version()");
      return true;
    } catch (Exception localException) {
    }
    return false;
  }

  private static boolean isDB2() {
    return false;
  }

  private static boolean isSqlServer() {
    return false;
  }

  public static DBEnum getDBId() {
    DBDetector sd = _instance;

    if (sd._dbId == null) {
      if (isOracle())
        sd._dbId = DBEnum.ORACLE;
      else if (isDm())
        sd._dbId = DBEnum.DM;
      else if (isMySql())
        sd._dbId = DBEnum.MYSQL;
      else if (isSqlServer())
        sd._dbId = DBEnum.SQLSERVER;
      else if (isDB2()) {
        sd._dbId = DBEnum.DB2;
      }

    }

    return sd._dbId;
  }

  public static Map<String, String> getJNDIInfo() {
    if (JNDIINFO.size() == 3)
      return JNDIINFO;
    try {
      Connection conn = dao.getDataSource().getConnection();
      if (conn.getClass().getName().equals("org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper")) {
        setPoolableConnection(conn);
      }
      if (ServerDetector.isTongWeb()) {
        setTongWebJNDI(conn);
      }
      setWeblogicJNDI(conn);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return JNDIINFO;
  }

  private static void setPoolableConnection(Connection conn) throws Exception {
    Field[] fields = conn.getClass().getDeclaredFields();
    for (Field f : fields)
      if ("delegate".equals(f.getName())) {
        f.setAccessible(true);
        Object c = f.get(conn);
        Object delegate = c.getClass().getMethod("getDelegate", null).invoke(c, new Object[0]);
        setJNDIInfo(delegate);
        return;
      }
  }

  private static String getTongWebDataSourcePassword(String filePath, String connectionURL, String user) {
    String password = null;
    try {
      File f = new File(filePath);
      SAXReader reader = new SAXReader();

      reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      Document doc = reader.read(f);
      Element root = doc.getRootElement();

      Iterator pools = root.elementIterator("jdbc-connection-pool");
      while (pools.hasNext()) {
        Element pool = (Element) pools.next();
        List poolElements = pool.elements("property");

        if (poolElements.size() >= 3) {
          Iterator propertys = poolElements.iterator();
          HashMap connectionInfo = new HashMap();

          while (propertys.hasNext()) {
            Element property = (Element) propertys.next();
            if (property != null) {
              Attribute nameNode = property.attribute("name");
              String name = nameNode.getText().trim();

              Attribute valueNode = property.attribute("value");
              String value = valueNode.getText().trim();

              connectionInfo.put(name, value);
            }
          }
          String url = (String) connectionInfo.get("connectionURL");
          String userName = (String) connectionInfo.get("user");

          if ((url != null) && (userName != null) && (url.equalsIgnoreCase(connectionURL))
            && (userName.equalsIgnoreCase(user))) {
            password = (String) connectionInfo.get("password");
          }
        }
      }
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (DocumentException e) {
      e.printStackTrace();
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return password;
  }

  private static void setTongWebJNDI(Connection conn) throws Exception {
    DatabaseMetaData mtdt = conn.getMetaData();
    try {
      String userDir = System.getProperty("fasp2.tongweb.domain.home");
      if (userDir.charAt(userDir.length() - 1) == '/')
        userDir = userDir + "config/twns.xml";
      else {
        userDir = userDir + "/config/twns.xml";
      }
      String connectionUrl = mtdt.getURL();
      String userName = mtdt.getUserName();
      String password = getTongWebDataSourcePassword(userDir, connectionUrl, userName);
      if (password.contains("{RSA}")) {
        password = password.substring(5);
      }
      Class decryptionClass = Class.forName("com.tongweb.util.Decryption");
      Object invokertester = decryptionClass.newInstance();
      Method decryptStrMehtod = decryptionClass.getDeclaredMethod("decryptStr", new Class[] { String.class });
      password = (String) decryptStrMehtod.invoke(invokertester, new Object[] { password });

      JNDIINFO.put("url", connectionUrl);
      JNDIINFO.put("userName", userName);
      JNDIINFO.put("password", password);
    } catch (Exception es) {
      es.printStackTrace();
    }
  }

  private static void setWeblogicJNDI(Connection conn) throws Exception {
    Method[] m = conn.getClass().getMethods();
    for (Method ms : m)
      if ("getVendorObj".equals(ms.getName())) {
        Object o = ms.invoke(conn, new Object[0]);
        setJNDIInfo(o);
        return;
      }
  }

  private static void setJNDIInfo(Object o) {
    try {
      Field pwdfield = o.getClass().getDeclaredField("password");
      pwdfield.setAccessible(true);
      JNDIINFO.put("password", (String) pwdfield.get(o));
      Field urlfield = o.getClass().getSuperclass().getDeclaredField("url");
      urlfield.setAccessible(true);
      Object url = urlfield.get(o);
      JNDIINFO.put("url", (String) url);
      Field unfield = null;
      try {
        unfield = o.getClass().getSuperclass().getDeclaredField("userName");
      } catch (NoSuchFieldException localNoSuchFieldException) {
        unfield = o.getClass().getSuperclass().getDeclaredField("user");
      }
      unfield.setAccessible(true);
      Object userName = unfield.get(o);
      JNDIINFO.put("userName", (String) userName);
    } catch (Exception es) {
      es.printStackTrace();
    }
  }
}