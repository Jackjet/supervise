package server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerDetector {
  public static final String GERONIMO_CLASS = "/org/apache/geronimo/system/main/Daemon.class";

  public static final String JBOSS_CLASS = "/org/jboss/Main.class";

  public static final String JETTY_CLASS = "/org/mortbay/jetty/Server.class";

  public static final String JONAS_CLASS = "/org/objectweb/jonas/server/Server.class";

  public static final String OC4J_CLASS = "/oracle/jsp/oc4jutil/Oc4jUtil.class";

  public static final String ORION_CLASS = "/com/evermind/server/ApplicationServer.class";

  public static final String PRAMATI_CLASS = "/com/pramati/Server.class";

  public static final String RESIN_CLASS = "/com/caucho/server/resin/Resin.class";

  public static final String REXIP_CLASS = "/com/tcc/Main.class";

  public static final String SUN7_CLASS = "/com/iplanet/ias/tools/cli/IasAdminMain.class";

  public static final String SUN8_CLASS = "/com/sun/enterprise/cli/framework/CLIMain.class";

  public static final String TOMCAT_CLASS = "/org/apache/catalina/startup/Bootstrap.class";

  public static final String WEBLOGIC_CLASS = "/weblogic/Server.class";

  public static final String WEBSPHERE_CLASS = "/com/ibm/websphere/product/VersionInfo.class";

  public static final String TONGWEB_CLASS = "/com/tongweb/config/serverbeans/Server.class";

  private static Log _log = LogFactory.getLog(ServerDetector.class);

  private static ServerDetector _instance = new ServerDetector();

  private String _serverId;

  private Boolean _geronimo;

  private Boolean _jBoss;

  private Boolean _jetty;

  private Boolean _jonas;

  private Boolean _oc4j;

  private Boolean _orion;

  private Boolean _pramati;

  private Boolean _resin;

  private Boolean _rexIP;

  private Boolean _sun7;

  private Boolean _sun8;

  private Boolean _tomcat;

  private Boolean _webLogic;

  private Boolean _webSphere;

  private Boolean _apusic;

  private Boolean _tongWeb;

  public static String getServerId() {
    ServerDetector sd = _instance;

    if (sd._serverId == null) {
      if (isApusic())
        sd._serverId = "apusic";
      else if (isWebLogic())
        sd._serverId = "weblogic";
      else if (isJBoss())
        sd._serverId = "jboss";
      else if (isJOnAS())
        sd._serverId = "jonas";
      else if (isOC4J())
        sd._serverId = "oc4j";
      else if (isOrion())
        sd._serverId = "orion";
      else if (isResin())
        sd._serverId = "resin";
      else if (isGeronimo())
        sd._serverId = "geronimo";
      else if (isWebSphere())
        sd._serverId = "websphere";
      else if (isTongWeb()) {
        sd._serverId = "tongWeb";
      }

      if (isJetty()) {
        if (sd._serverId == null)
          sd._serverId = "jetty";
        else
          sd._serverId += "-jetty";
      } else if (isTomcat()) {
        if (sd._serverId == null)
          sd._serverId = "tomcat";
        else {
          sd._serverId += "-tomcat";
        }
      }

      if (_log.isInfoEnabled()) {
        _log.info("Detected server " + sd._serverId);
      }

      if (sd._serverId == null) {
        throw new RuntimeException("Server is not supported");
      }
    }

    return sd._serverId;
  }

  public static boolean isGeronimo() {
    ServerDetector sd = _instance;

    if (sd._geronimo == null) {
      Class c = sd.getClass();

      if (c.getResource("/org/apache/geronimo/system/main/Daemon.class") != null)
        sd._geronimo = Boolean.TRUE;
      else {
        sd._geronimo = Boolean.FALSE;
      }
    }

    return sd._geronimo.booleanValue();
  }

  public static boolean isJBoss() {
    ServerDetector sd = _instance;

    if (sd._jBoss == null) {
      Class c = sd.getClass();

      if (c.getResource("/org/jboss/Main.class") != null)
        sd._jBoss = Boolean.TRUE;
      else {
        sd._jBoss = Boolean.FALSE;
      }
    }

    return sd._jBoss.booleanValue();
  }

  public static boolean isJetty() {
    ServerDetector sd = _instance;

    if (sd._jetty == null) {
      Class c = sd.getClass();

      if (c.getResource("/org/mortbay/jetty/Server.class") != null)
        sd._jetty = Boolean.TRUE;
      else {
        sd._jetty = Boolean.FALSE;
      }
    }

    return sd._jetty.booleanValue();
  }

  public static boolean isJOnAS() {
    ServerDetector sd = _instance;

    if (sd._jonas == null) {
      Class c = sd.getClass();

      if (c.getResource("/org/objectweb/jonas/server/Server.class") != null)
        sd._jonas = Boolean.TRUE;
      else {
        sd._jonas = Boolean.FALSE;
      }
    }

    return sd._jonas.booleanValue();
  }

  public static boolean isOC4J() {
    ServerDetector sd = _instance;

    if (sd._oc4j == null) {
      Class c = sd.getClass();

      if (c.getResource("/oracle/jsp/oc4jutil/Oc4jUtil.class") != null)
        sd._oc4j = Boolean.TRUE;
      else {
        sd._oc4j = Boolean.FALSE;
      }
    }

    return sd._oc4j.booleanValue();
  }

  public static boolean isOrion() {
    ServerDetector sd = _instance;

    if (sd._orion == null) {
      Class c = sd.getClass();

      if (c.getResource("/com/evermind/server/ApplicationServer.class") != null)
        sd._orion = Boolean.TRUE;
      else {
        sd._orion = Boolean.FALSE;
      }
    }

    return sd._orion.booleanValue();
  }

  public static boolean isPramati() {
    ServerDetector sd = _instance;

    if (sd._pramati == null) {
      Class c = sd.getClass();

      if (c.getResource("/com/pramati/Server.class") != null)
        sd._pramati = Boolean.TRUE;
      else {
        sd._pramati = Boolean.FALSE;
      }
    }

    return sd._pramati.booleanValue();
  }

  public static boolean isResin() {
    ServerDetector sd = _instance;

    if (sd._resin == null) {
      Class c = sd.getClass();

      if (c.getResource("/com/caucho/server/resin/Resin.class") != null)
        sd._resin = Boolean.TRUE;
      else {
        sd._resin = Boolean.FALSE;
      }
    }

    return sd._resin.booleanValue();
  }

  public static boolean isRexIP() {
    ServerDetector sd = _instance;

    if (sd._rexIP == null) {
      Class c = sd.getClass();

      if (c.getResource("/com/tcc/Main.class") != null)
        sd._rexIP = Boolean.TRUE;
      else {
        sd._rexIP = Boolean.FALSE;
      }
    }

    return sd._rexIP.booleanValue();
  }

  public static boolean isSun() {
    return ((isSun7()) || (isSun8()));
  }

  public static boolean isSun7() {
    ServerDetector sd = _instance;

    if (sd._sun7 == null) {
      Class c = sd.getClass();

      if (c.getResource("/com/iplanet/ias/tools/cli/IasAdminMain.class") != null)
        sd._sun7 = Boolean.TRUE;
      else {
        sd._sun7 = Boolean.FALSE;
      }
    }

    return sd._sun7.booleanValue();
  }

  public static boolean isSun8() {
    ServerDetector sd = _instance;

    if (sd._sun8 == null) {
      Class c = sd.getClass();

      if (c.getResource("/com/sun/enterprise/cli/framework/CLIMain.class") != null)
        sd._sun8 = Boolean.TRUE;
      else {
        sd._sun8 = Boolean.FALSE;
      }
    }

    return sd._sun8.booleanValue();
  }

  public static boolean isTomcat() {
    ServerDetector sd = _instance;

    if (sd._tomcat == null) {
      Class c = sd.getClass();

      if (c.getResource("/org/apache/catalina/startup/Bootstrap.class") != null)
        sd._tomcat = Boolean.TRUE;
      else {
        sd._tomcat = Boolean.FALSE;
      }
    }

    return sd._tomcat.booleanValue();
  }

  public static boolean isWebLogic() {
    ServerDetector sd = _instance;

    if (sd._webLogic == null) {
      Class c = sd.getClass();

      if (c.getResource("/weblogic/Server.class") != null)
        sd._webLogic = Boolean.TRUE;
      else {
        sd._webLogic = Boolean.FALSE;
      }
    }

    return sd._webLogic.booleanValue();
  }

  public static boolean isTongWeb() {
    ServerDetector sd = _instance;

    if (sd._tongWeb == null) {
      Class c = sd.getClass();

      if (c.getResource("/com/tongweb/config/serverbeans/Server.class") != null)
        sd._tongWeb = Boolean.TRUE;
      else {
        sd._tongWeb = Boolean.FALSE;
      }
    }

    return sd._tongWeb.booleanValue();
  }

  public static boolean isApusic() {
    ServerDetector sd = _instance;
    if (sd._apusic == null) {
      if (System.getProperty("com.apusic.domain.home") != null)
        sd._apusic = Boolean.TRUE;
      else {
        sd._apusic = Boolean.FALSE;
      }
    }

    return sd._apusic.booleanValue();
  }

  public static boolean isWebSphere() {
    ServerDetector sd = _instance;

    if (sd._webSphere == null) {
      Class c = sd.getClass();

      if (c.getResource("/com/ibm/websphere/product/VersionInfo.class") != null)
        sd._webSphere = Boolean.TRUE;
      else {
        sd._webSphere = Boolean.FALSE;
      }
    }

    return sd._webSphere.booleanValue();
  }
}
