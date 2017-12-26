package interf;

import gov.mof.fasp2.fw.log.server.dto.ServerStatusLogDTO;
import gov.mof.fasp2.fw.log.server.service.IServerLogService;
import gov.mof.fasp2.sec.syncuserinfo.manager.LogThread;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.longtu.framework.exception.AppException;
import com.longtu.framework.heartbeat.IHearbeatService;
import com.longtu.framework.log.enums.LogServiceEnum;
import com.longtu.framework.springexp.LoadAppidFactory;
import com.longtu.framework.util.ServiceFactory;

public class HearbeatManager implements IHearbeatService {
  public static final List<IHearbeatListener> listener = new ArrayList();

  public static Map<String, String> services = new HashMap();

  public static Map<String, Collection<String>> dbServices = new HashMap();

  private IServerLogService serverservice = (IServerLogService) ServiceFactory.getBean("fasp2.server.log");

  private Map map = new HashMap();

  public static String mainguid = null;

  public String reg(String serviceGuid, String dbGuid) {
    services.put(serviceGuid, dbGuid);
    if (dbServices.get(dbGuid) == null) {
      dbServices.put(dbGuid, new HashSet());
    }
    ((Collection) dbServices.get(dbGuid)).add(serviceGuid);
    for (IHearbeatListener l : listener) {
      l.start(serviceGuid, dbGuid);
    }
    logServerInfo(serviceGuid, Integer.valueOf(0));
    return LoadAppidFactory.newInstance().getServerGuid();
  }

  public String getService2Db(String serviceGuid) {
    return ((String) services.get(serviceGuid));
  }

  public Collection<String> getServersByDb(String dbguid) {
    return ((Collection) dbServices.get(dbguid));
  }

  public void destroy(String guid) {
    logServerInfo(guid, Integer.valueOf(1));
    String dbGuid = (String) services.remove(guid);
    ((Collection) dbServices.get(dbGuid)).remove(guid);
    if (((Collection) dbServices.get(dbGuid)).size() == 0) {
      dbServices.remove(dbGuid);
    }
    for (IHearbeatListener l : listener)
      l.stop(guid, dbGuid);
  }

  public Serializable send(String serverGuid, String bean, String method, Serializable[] objs) {
    if (LoadAppidFactory.newInstance().getServerGuid().equals(serverGuid)) {
      return send(bean, method, objs);
    }
    IHearbeatService hrs = (IHearbeatService) ServiceFactory.getBean("remote.fasp2.hearbeat.service", serverGuid);
    return hrs.send(bean, method, objs);
  }

  public Serializable send(String bean, String method, Serializable[] objs) {
    Object obj = ServiceFactory.getBean(bean);
    if ((objs == null) || (objs.length == 0)) {
      try {
        return ((Serializable) obj.getClass().getMethod(method, new Class[0]).invoke(obj, null));
      } catch (Exception e) {
        return e;
      }
    }
    Class[] objstype = new Class[objs.length];
    for (int i = 0; i < objs.length; ++i)
      objstype[i] = objs[i].getClass();
    try {
      return ((Serializable) obj.getClass().getMethod(method, objstype).invoke(obj, objs));
    } catch (Exception e) {
    }
    return null;
  }

  public boolean hearbeat(String serviceguid) {
    return serviceguid.equals(LoadAppidFactory.newInstance().getServerGuid());
  }

  public Collection<String> getServiceAll() {
    return ((Collection) services.keySet());
  }

  public Collection<String> getDbAll() {
    return ((Collection) dbServices.keySet());
  }

  public void regServerInfo(Map map) {
    this.map = map;
  }

  public void logServerInfo(String serviceGuid, Integer status) {
    ServerStatusLogDTO s;
    if ((this.map != null) && (this.map.size() > 0)) {
      ServerStatusLogDTO serverdto = new ServerStatusLogDTO();
      serverdto.setIdentify(serviceGuid);
      String serverip = (this.map.get("serverip") != null) ? this.map.get("serverip").toString() : null;
      serverdto.setServerip(serverip);
      serverdto.setServerport((this.map.get("serverport") != null) ? this.map.get("serverport").toString() : null);
      serverdto.setDomainname((this.map.get("domainname") != null) ? this.map.get("domainname").toString() : null);
      serverdto.setServerstatus(status);
      serverdto.setAppid((this.map.get("appid") != null) ? this.map.get("appid").toString() : null);
      serverdto.setCurrentdate((this.map.get("currentdate") != null) ? this.map.get("currentdate").toString() : null);
      serverdto.setIszkserverstarted((this.map.get("iszkserverstarted") != null) ? Integer.valueOf(this.map.get(
        "iszkserverstarted").toString()) : null);
      serverdto.setZkserverip((this.map.get("zkserverip") != null) ? this.map.get("zkserverip").toString() : null);
      serverdto
        .setZkserverport((this.map.get("zkserverport") != null) ? this.map.get("zkserverport").toString() : null);
      serverdto.setDubboport((this.map.get("dubboport") != null) ? this.map.get("dubboport").toString() : null);
      serverdto.setNestversion((this.map.get("nestversion") != null) ? this.map.get("nestversion").toString() : null);
      serverdto.setFaspversion((this.map.get("faspversion") != null) ? this.map.get("faspversion").toString() : null);
      serverdto.setLogenum(LogServiceEnum.SERVER);
      List logs = new ArrayList();
      if (status.intValue() == 0)
        if ("0".equals((this.map.get("iszkserverstarted") != null) ? this.map.get("iszkserverstarted").toString()
          : null)) {
          Collection colls = null;
          try {
            colls = this.serverservice.queryServer(null, null, null);
          } catch (AppException localAppException1) {
          }
          if ((colls != null) && (colls.size() > 0)) {
            for (Iterator localIterator = colls.iterator(); localIterator.hasNext();) {
              s = (ServerStatusLogDTO) localIterator.next();
              ServerStatusLogDTO sdto = new ServerStatusLogDTO();
              sdto.setIdentify(s.getIdentify());
              sdto.setServerip(s.getServerip());
              sdto.setServerport(s.getServerport());
              sdto.setDomainname(s.getDomainname());
              sdto.setServerstatus(Integer.valueOf(1));
              sdto.setAppid(s.getAppid());
              sdto.setCurrentdate(getcurrentdate());
              sdto.setIszkserverstarted(s.getIszkserverstarted());
              sdto.setZkserverip(s.getZkserverip());
              sdto.setZkserverport(s.getZkserverport());
              sdto.setDubboport(s.getDubboport());
              sdto.setNestversion(s.getNestversion());
              sdto.setFaspversion(s.getFaspversion());
              sdto.setLogenum(LogServiceEnum.SERVER);
              logs.add(sdto);
            }
          }
        }
      logs.add(serverdto);
      LogThread.getInstance().add(logs);
    } else {
      Collection<ServerStatusLogDTO> colls = null;
      Integer zkStatus = Integer.valueOf(1);
      try {
        colls = this.serverservice.queryServer(null, zkStatus, serviceGuid);
      } catch (AppException localAppException2) {
      }
      List logs = new ArrayList();
      if ((colls != null) && (colls.size() > 0)) {
        for (ServerStatusLogDTO se : colls) {
          ServerStatusLogDTO sdto = new ServerStatusLogDTO();
          sdto.setIdentify(se.getIdentify());
          sdto.setServerip(se.getServerip());
          sdto.setServerport(se.getServerport());
          sdto.setDomainname(se.getDomainname());
          sdto.setServerstatus(Integer.valueOf(1));
          sdto.setAppid(se.getAppid());
          sdto.setCurrentdate(getcurrentdate());
          sdto.setIszkserverstarted(se.getIszkserverstarted());
          sdto.setZkserverip(se.getZkserverip());
          sdto.setZkserverport(se.getZkserverport());
          sdto.setDubboport(se.getDubboport());
          sdto.setNestversion(se.getNestversion());
          sdto.setFaspversion(se.getFaspversion());
          sdto.setLogenum(LogServiceEnum.SERVER);
          logs.add(sdto);
        }
      }
      LogThread.getInstance().add(logs);
    }
  }

  public String getcurrentdate() {
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    String time = df.format(new Date());
    return time;
  }
}
