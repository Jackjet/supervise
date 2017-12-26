import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class WeblogicLoadAppid extends AbstractLoadAppid {
  private static final Logger log = Logger.getLogger(WeblogicLoadAppid.class);

  private static final String DOMAINMARK = "domainmark";

  private static String domainName = null;

  private static Map<String, String> serverInfo = null;

  private static String arguments = null;

  public String getDomainName() {
    InitialContext ctx = null;
    if (domainName != null) {
      return domainName;
    }
    domainName = "DomainName";
    try {
      ObjectName service = new ObjectName(
        "com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
      ctx = new InitialContext();

      MBeanServer server = (MBeanServer) ctx.lookup("java:comp/env/jmx/runtime");

      ObjectName drt = (ObjectName) server.getAttribute(service, "DomainConfiguration");
      domainName = (String) server.getAttribute(drt, "Name");
      log.info("PfConfiguration Override Domain Name: " + domainName);
    } catch (Exception e) {
      log.error("Error fetching Weblogic Server Info", e);
    } finally {
      if (ctx != null) {
        try {
          ctx.close();
        } catch (NamingException e) {
          log.error("Error closing InitialContext :" + e.toString(), e);
        }
      }

    }

    return domainName;
  }

  public Map<String, String> getServerInfo() {
    if (serverInfo != null) {
      return serverInfo;
    }
    InitialContext ctx = null;
    serverInfo = new HashMap();
    try {
      ObjectName service = new ObjectName(
        "com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
      ctx = new InitialContext();

      MBeanServer mBeanserver = (MBeanServer) ctx.lookup("java:comp/env/jmx/runtime");

      ObjectName serverRT = (ObjectName) mBeanserver.getAttribute(service, "ServerRuntime");
      String name = (String) mBeanserver.getAttribute(serverRT, "Name");
      String listenAddress = null;
      String slistenAddress = (String) mBeanserver.getAttribute(serverRT, "ListenAddress");
      if (slistenAddress.indexOf("/") != -1) {
        listenAddress = slistenAddress.split("/")[1];
        Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");
        if (!(IP_PATTERN.matcher(listenAddress).matches()))
          listenAddress = slistenAddress.split("/")[0];
      } else {
        listenAddress = slistenAddress;
      }
      String listenPort = String.valueOf(mBeanserver.getAttribute(serverRT, "ListenPort"));
      String clusterName = null;
      String state = (String) mBeanserver.getAttribute(serverRT, "State");
      ObjectName clusterRuntime = (ObjectName) mBeanserver.getAttribute(serverRT, "ClusterRuntime");
      if (clusterRuntime != null) {
        clusterName = (String) mBeanserver.getAttribute(clusterRuntime, "Name");
        if (log.isDebugEnabled()) {
          log.debug("\n\t clusterRuntime.Name                       : " + clusterName);
        }
      }

      if (log.isDebugEnabled()) {
        log.debug("Server name: " + name + ".Server listenAddress: " + listenAddress + ".Server listenPort: "
          + listenPort + ".Server clusterName: " + clusterName + ".Server state: " + state);
      }
      serverInfo.put("Name", name);
      serverInfo.put("ListenAddress", listenAddress);
      serverInfo.put("ListenPort", listenPort);
      serverInfo.put("State", state);
      serverInfo.put("ClusterName", clusterName);
    } catch (Exception e) {
      log.error("Error fetching Weblogic Server Info", e);
    } finally {
      if (ctx != null) {
        try {
          ctx.close();
        } catch (NamingException e) {
          log.error("Error closing InitialContext :" + e.toString(), e);
        }
      }
    }

    return serverInfo;
  }

  public String getServerArguments() {
    if (arguments != null) {
      return arguments;
    }
    InitialContext ctx = null;
    arguments = "";
    try {
      ObjectName service = new ObjectName(
        "com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
      ctx = new InitialContext();

      MBeanServer server = (MBeanServer) ctx.lookup("java:comp/env/jmx/runtime");

      ObjectName rt = (ObjectName) server.getAttribute(service, "ServerConfiguration");

      ObjectName serverStartMBean = (ObjectName) server.getAttribute(rt, "ServerStart");

      arguments = (String) server.getAttribute(serverStartMBean, "Arguments");
    } catch (Exception e) {
      logger.error("Error fetching Weblogic Server Info", e);
    } finally {
      if (ctx != null) {
        try {
          ctx.close();
        } catch (NamingException e) {
          logger.error("Error closing InitialContext :" + e.toString(), e);
        }
      }
    }

    return arguments;
  }
}
