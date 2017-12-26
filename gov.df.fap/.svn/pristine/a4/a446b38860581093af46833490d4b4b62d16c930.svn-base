package gov.df.fap.service.util.dao;

import gov.df.fap.util.factory.ServiceFactory;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

@Component("fasp2.dao.df")
public class Fasp2DaoSupport implements DataSource {
  static DataSource yogdatasource = null;

  public static DataSource instanceDataSource() {
    if (yogdatasource == null)
      new Fasp2DaoSupport();
    return yogdatasource;
  }

  public Fasp2DaoSupport() {
    if (yogdatasource == null) {
      try {
        //yogdatasource = (DataSource) ServiceFactory.getBean("fasp2datasource");
      } catch (Exception _ex) {
        _ex.printStackTrace();
      }
    }
  }

  public Connection getConnection() throws SQLException {
    return yogdatasource.getConnection();
  }

  public Connection getConnection(String arg0, String arg1) throws SQLException {
    return yogdatasource.getConnection(arg0, arg1);
  }

  public PrintWriter getLogWriter() throws SQLException {
    return yogdatasource.getLogWriter();
  }

  public int getLoginTimeout() throws SQLException {
    return yogdatasource.getLoginTimeout();
  }

  public void setLogWriter(PrintWriter arg0) throws SQLException {
    yogdatasource.setLogWriter(arg0);
  }

  public void setLoginTimeout(int arg0) throws SQLException {
    yogdatasource.setLoginTimeout(arg0);
  }

  public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

}
