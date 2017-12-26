package gov.df.fap.service.util.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TypeOfDB {

  @Autowired
  private MultiDataSource multiDS;

  /**
   * 系统MultiDataSource数据源连接的数据库是否为Oracle
   * @return true: Oracle数据库, false: 非Oracle 
   * @throws SQLException
   */
  public static boolean isOracle() {
	  return "Oracle".equals(MultiDataSource.dbType);
  }

  /**
   * 系统MultiDataSource数据源连接的数据库是否为MySQL
   * @return true: MySQL数据库, false: 非MySQL 
   * @throws SQLException
   */
  public static boolean isMySQL() {
	  return "MySQL".equals(MultiDataSource.dbType);
  }

  /**
   * 系统MultiDataSource数据源连接的数据库软件名称
   * @return 数据库软件名称,Oracle\MySQL\SQL Server\DB2等 
   * @throws SQLException
   */
  public String getDBName() throws SQLException {
    return getDBName(multiDS.getConnection());
  }

  /**
   * 系统MultiDataSource数据源连接的数据库软件版本
   * @return 数据库软件版本 
   * @throws SQLException
   */
  public String getDBVersion() throws SQLException {
    return getDBVersion(multiDS.getConnection());
  }

  /**
   * 参数conn连接的数据库是否为Oracle
   * @return true: Oracle数据库, false: 非Oracle 
   * @throws SQLException
   */
  public static boolean isOracle(Connection conn) throws SQLException {
    return "Oracle".equals(getDBName(conn));
  }

  /**
   * 参数conn连接的数据库是否为MySQL
   * @return true: MySQL数据库, false: 非MySQL 
   * @throws SQLException
   */
  public static boolean isMySQL(Connection conn) throws SQLException {
    return "MySQL".equals(getDBName(conn));
  }

  /**
   * 参数conn连接的数据库软件名称
   * @return 数据库类型名称,Oracle\MySQL\SQL Server\DB2等 
   * @throws SQLException
   */
  public static String getDBName(Connection conn) throws SQLException {
    if (conn != null) {
      return conn.getMetaData().getDatabaseProductName();
    } else {
      return "";
    }
  }

  /**
   * 参数conn连接的数据库软件版本
   * @return 数据库软件版本 
   * @throws SQLException
   */
  public static String getDBVersion(Connection conn) throws SQLException {
    if (conn != null) {
      return conn.getMetaData().getDatabaseProductVersion();
    } else {
      return "";
    }
  }
}
