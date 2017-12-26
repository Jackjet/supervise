import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.longtu.framework.util.ServiceFactory;

public class SystemDataSourceManager {
  private static Logger logger = Logger.getLogger(SystemDataSourceManager.class);

  public static DataSource getDataSource() {
    DataSource ds = null;
    try {
      ds = (DataSource) ServiceFactory.getBean("fasp2datasource");
    } catch (Exception localException) {
      ds = DFInitClasspathXmlApplicationContext.getThis().getDataSource();
    }
    return ds;
  }

  public static DataSource getDataSource(String id) {
    try {
      return ((DataSource) ServiceFactory.getBean(id));
    } catch (Exception e) {
      logger.error("初始化数据源 " + id + " 出错！", e);
    }
    return null;
  }

  public static boolean testDataSource(DataSource ds) {
    try {
      Connection conn = ds.getConnection();
      conn.close();
    } catch (SQLException localSQLException) {
      return false;
    }

    return true;
  }
}
