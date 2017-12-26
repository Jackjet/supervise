import interf.HearbeatManager;
import interf.IHearbeatListener;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.longtu.framework.distributed.util.NetUtils;

public class HearbeatStartListener extends FaspDefaultReaderEventListener {
  private static Logger logger = Logger.getLogger(HearbeatStartListener.class);

  public Object createBean(String beanName, Object o) {
    if ((IHearbeatListener.class.isAssignableFrom(o.getClass())) && (LoadAppidFactory.newInstance().isZkStrat())) {
      HearbeatManager.listener.add((IHearbeatListener) o);
    }

    return o;
  }

  public void afertReflash() {
    //    IHearbeatService hrs = null;
    //    if (LoadAppidFactory.newInstance().isZkStrat()) {
    //      hrs = (IHearbeatService) ServiceFactory.getBean("fasp2.hearbeat.service");
    //      new HearbearThread().start();
    //    } else {
    //      hrs = (IHearbeatService) ServiceFactory.getBean("remote.fasp2.hearbeat.service", "fasp");
    //    }
    //    Map map = getServerInfo();
    //    hrs.regServerInfo(map);
    //    hrs.reg(LoadAppidFactory.newInstance().getServerGuid(), LoadAppidFactory.newInstance().getDbGuid());
  }

  public Map getServerInfo() {
    String serverip = NetUtils.getLocalServerIP();
    String serverport = NetUtils.getLocalServerPort();
    Collection<String> appids = LoadAppidFactory.newInstance().getAppid();
    String domainname = LoadAppidFactory.newInstance().getDomainName();
    String zkip = LoadAppidFactory.newInstance().getZkAddress();
    boolean iszkstarted = LoadAppidFactory.newInstance().isZkStrat();
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    String currentdate = df.format(new Date());
    String appidstr = null;
    StringBuilder sb = new StringBuilder();
    if ((appids != null) && (appids.size() > 0)) {
      for (String appid : appids) {
        sb.append(appid).append(",");
      }
      appidstr = sb.substring(0, sb.length() - 1);
    }
    Integer iszk = Integer.valueOf(1);
    if (iszkstarted) {
      iszk = Integer.valueOf(0);
    }
    String[] split = (String[]) null;
    String zkserverip = null;
    String zkserverport = null;
    if ((zkip.length() > 0) && (zkip.contains(":"))) {
      split = zkip.split(":");
      if (split.length > 1) {
        zkserverip = split[0];
        zkserverport = split[1];
      }
    }

    String dubboport = System.getProperty("fasp2.zkserver.dubboport");
    String nestversion = null;
    String faspversion = null;
    try {
      Properties version = new Properties();
      version.load(super.getClass().getResourceAsStream("/NEST.VERSION"));
      nestversion = version.get("VERSION").toString();
    } catch (Exception e) {
      logger.error("获取NEST版本信息异常" + e);
    }
    try {
      Properties version = new Properties();
      version.load(super.getClass().getResourceAsStream("/FASP.VERSION"));
      faspversion = version.get("VERSION").toString();
    } catch (Exception e) {
      logger.error("获取平台版本信息异常" + e);
    }
    Map map = new HashMap();
    map.put("serverip", serverip);
    map.put("serverport", serverport);
    map.put("domainname", domainname);
    map.put("currentdate", currentdate);
    map.put("appid", appidstr);
    map.put("iszkserverstarted", iszk);
    map.put("zkserverip", zkserverip);
    map.put("zkserverport", zkserverport);
    map.put("dubboport", dubboport);
    map.put("nestversion", nestversion);
    map.put("faspversion", faspversion);
    return map;
  }
}