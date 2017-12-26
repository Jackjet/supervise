import gov.mof.fasp2.dic.table.ddl.dao.AbstractPartition;
import gov.mof.fasp2.dic.table.ddl.dao.DMPartition;
import gov.mof.fasp2.dic.table.ddl.dao.OraclePartition;

import javax.sql.DataSource;

import server.ServerDetector;

import com.longtu.framework.exception.AppException;

public class LoadAppidFactory {
  public static ILoadAppid ils = null;

  private static boolean develop = false;

  public static void setDevelop(boolean b) {
    develop = b;
  }

  public static ILoadAppid newInstance() {
    return newInstance(null);
  }

  public static boolean isDevelop() {
    return develop;
  }

  public static ILoadAppid newInstance(DataSource ds) {
    if (ils == null) {
      getDBType();
      //暂时屏蔽，因为读了develop文件
      ils = getLoadAppid(ds);
    }
    return ils;
  }

  private static ILoadAppid getLoadAppid(DataSource ds) {
    AbstractLoadAppid loadAppid = null;
    String webServer = null;
    try {
      webServer = ServerDetector.getServerId();
    } catch (Exception localException) {
    }
    if ("weblogic".equals(webServer))
      loadAppid = new WeblogicLoadAppid();
    else if ("jboss".equals(webServer))
      //      loadAppid = new JbossLoadAppid();
      ;
    else if ("apusic".equals(webServer))
      //      loadAppid = new ApusicLoadAppid();
      ;
    else if ("tomcat".equals(webServer))
      //      loadAppid = new TomcatAppid();
      ;
    else if ("tongWeb".equals(webServer))
      //      loadAppid = new TongWebAppid();
      ;
    else {
      //      loadAppid = new DefLoadAppid();
      ;
    }
    if (develop) {
      DevelopLoadAppid dlp = new DevelopLoadAppid();
      dlp.setLoadAppid(loadAppid);
      loadAppid = dlp;
    }
    loadAppid.init(ds);
    return (ILoadAppid) loadAppid;
  }

  public static DBEnum getDBType() {
    return DBDetector.getDBId();
  }

  public static AbstractPartition getPartitionFactory() throws AppException {
    DBEnum dbtype = getDBType();
    if (DBEnum.DM == dbtype)
      return new DMPartition();
    if (DBEnum.ORACLE == dbtype) {
      return new OraclePartition();
    }
    throw new AppException("", "不支持的数据库类型", "不支持的数据库类型");
  }
}