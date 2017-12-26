package gov.df.fap.service.util.datasource;

import gov.df.fap.service.util.dao.ufgovdao.UfConnection;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jndi.JndiObjectFactoryBean;

/**
 * <p>
 * Title:数据库支持多数据库类
 * </p>
 * <p>
 * Description:提供根据年度取连接和根据登陆年份取连接
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: 北京方正春元软件有限公司
 * </p>
 * <p>
 * CreateData 2007-8-21
 * </p>
 *<p> 此类方法平台多次调用的方法有getDataSources()；getDataSource()调用次数1次    以及getDataSourceWithYear()add by fengdz</p>
 * @author 黄节
 * @version 1.0
 */
public class MultiDataSource implements DataSource, ApplicationContextAware {
	
	//数据库类型，MySQL、Oracle，区分大小写，默认为Oracle
	public static String dbType = "Oracle";

  private Map rgCodes;

  public Map getRgCodes() {
    return rgCodes;
  }

  public void setRgCodes(Map rgCodes) {
    this.rgCodes = rgCodes;
  }

  private DataSource defaultDataSource;

  private Map dataSources;

  // 服务初始化时加载的默认数据库年度
  private String defaultYear = null;

  public static boolean isDebugSql = false;

  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
  }

  /**
   * 获取一个默认年度的连接
   * 
   * @return Connection 返回一个连接
   * 
   * @throws SQLException
   *             取连接时发生错误，向外抛出sql错误
   * 
   */
  public Connection getConnection() throws SQLException {
    if (!isDebugSql) {
      return getDataSource().getConnection();
    } else {
      return new UfConnection(getDataSource().getConnection());
    }
  }

  /**
   * 
   * 根据当前用户的会计年份取得对应的数据源bean
   * 
   * @return DataSource 
   *         数据源的key变更为key=rg+#year
   */
  public DataSource getDataSource() {
    String year = (String) SessionUtil.getUserInfoContext().getAttribute("set_year");
    String rgCode = (String) SessionUtil.getUserInfoContext().getRgCode();
    //查询库对应 在datasource中配置对应的查询库对应的datasource
    String read = (String) SessionUtil.getUserInfoContext().getAttribute("read");
    String rgAndYear = rgCode + "#" + year;
    if (null == defaultYear && null == year) { // 如果用户还没有登录,并且没有获取过默认年度
      initiaDefaultDBYear();
    }
    if (SessionUtil.getUserInfoContext() == null) {
      return this.getDefaultDataSource();
    }
    if (null != year && !"".equals(year)) {
      if(read!=null && !"".equals(read))
      {
    	  if (dataSources.containsKey(rgAndYear+"#"+read)) {
    	        return (DataSource) dataSources.get(rgAndYear+"#"+read);
    	  }
      }
      if (dataSources.containsKey(rgAndYear)) {
        return (DataSource) dataSources.get(rgAndYear);
      } else {
        return this.getDefaultDataSource();

      }
    }
    return this.getDefaultDataSource();
  }

  /**
   * 根据指定年份取连接
   * 
   * @param year
   *            int 指定的年份
   * 
   * @return DataSource返回一个DataSource
   * 
   *       
   */
  public DataSource getDataSourceWithYear(int year) {
    String rgCode = SessionUtil.getRgCode();
    String key = rgCode + "#" + year;
    if (dataSources.containsKey(key.trim())) {
      return (DataSource) dataSources.get(key);
    } else {
      /*如果不存在该数据源  抛出运行时异常 add by fengdz*/
      throw new RuntimeException(" 没有找到对应的数据源，请检查public-context.xml文件! 数据源名称为【" + key + "】");
    }
  }

  /**
   * 通过年度和区划查找是否存在这个数据源
   * 返回true表示存在数据源
   * 返回false表示不存在这个数据源
   * @time 2013-03-29
   * @author fengdz
   * @param year
   * @param rg
   * @return
   */
  public boolean isExistsSourceByRgAndYear(String year, String rgCode) {
    String key = rgCode + "#" + year;
    if (dataSources.containsKey(key.trim()))
      return true;
    return false;
  }

  /**
   * @author 根据指定的年度，区划获取连接 特别为报表的延时查询设置
   * @param year
   * @param rgCode
   * @return
   */
  public DataSource getDataSourceWithRgYear(int year, String rgCode) {
    String key = rgCode + "#" + year;
    if (key == null || "".equals(key.trim()) || !dataSources.containsKey(key)) {
      //*如果不存在该数据源  抛出运行时异常 
      throw new RuntimeException(" 没有找到对应的数据源，请检查public-context.xml文件! 数据源名称为【" + key + "】");
    }
    return (DataSource) dataSources.get(key);
  }

  public DataSource getDataSourceWithKey(String key) {
    if (key == null || "".equals(key.trim()) || !dataSources.containsKey(key)) {
      //如果不存在该数据源  抛出运行时异常 
      throw new RuntimeException(" 没有找到对应的数据源，请检查public-context.xml文件! 数据源名称为【" + key + "】");

    }
    return (DataSource) dataSources.get(key);
  }

  public Connection getConnection(String username, String password) throws SQLException {
    return null;
  }

  public PrintWriter getLogWriter() throws SQLException {

    return null;
  }

  public int getLoginTimeout() throws SQLException {

    return 0;
  }

  public void setLogWriter(PrintWriter out) throws SQLException {

  }

  public void setLoginTimeout(int seconds) throws SQLException {

  }

  /**
   * @return defaultDataSource
   */
  public DataSource getDefaultDataSource() {
    return defaultDataSource;
  }

  /**
   * @param defaultDataSource
   *            要设置的 defaultDataSource update by kongcy at 2012-03-27
   *            把license校验去掉
   */
  public void setDefaultDataSource(DataSource defaultDataSource) {
    // 如果是生产部署环境，启动服务时校验License的有效性（通过Mac地址验证），如果是开发环境则不校验。
    // 通过数据源类型判断是生产部署环境还是开发环境：
    String className = defaultDataSource.getClass().toString();
    if (defaultDataSource instanceof JndiObjectFactoryBean || className.indexOf("RmiDataSource") >= 0) {
      try {
      } catch (Exception e) {
        System.exit(0);
      }
    }
    this.defaultDataSource = defaultDataSource;
  }

  /**
   * @return dataSources
   */
  public Map getDataSources() {
    return dataSources;
  }

  /**
   * @param dataSources
   *            要设置的 dataSources
   */
  public void setDataSources(Map dataSources) {
    this.dataSources = dataSources;
  }

  public static boolean testConnection(String url, String driver, String user, String password) throws Exception {
    Class.forName(driver);
    DriverManager.getConnection(url, user, password);
    return true;
  }

  /**
   * 根据defaultDataSource获取服务初始化加载的数据库年度
   * 
   * @author zhupeidong at 2008-11-24上午11:36:02 update by kongcy at 2012-03-20
   *         because 数据源格式变更为rg(标准6位)+#+year
   */
  private void initiaDefaultDBYear() {
    /**
     * add by fengdz  2013.04.10 修改从数据源取年度的方式  保持与SessionUtil.getRgCode() 
     * 取区划代码逻辑一致 原始代码取的结果和现在的取的结果一致 为了保险起见  两边保持一致Begin
     */

    String defaultYear = StringUtil.EMPTY;
    if (getDataSources() != null && !getDataSources().isEmpty() && getDataSources().size() > 0) {
      Set keySet = getDataSources().keySet();
      Iterator it = keySet.iterator();
      while (it.hasNext()) {
        String rgAndyear = it.next().toString();
        defaultYear = String.valueOf(rgAndyear.split("#")[1]);
        if (!(defaultYear == null) || !"".equals(defaultYear))
          break;

      }
    }
    SessionUtil.setDefaultYear(defaultYear);
  }

  public boolean isWrapperFor(Class arg0) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  public Object unwrap(Class arg0) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }
  
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
}
