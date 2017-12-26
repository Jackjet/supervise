package gov.df.fap.service.gl.core;

import gov.df.fap.api.gl.core.daosupport.BatchPreparedStatementParamSetter;
import gov.df.fap.api.gl.core.daosupport.CallableStatementHandler;
import gov.df.fap.api.gl.core.daosupport.ClassLocator;
import gov.df.fap.api.gl.core.daosupport.ClassLocatorAdapter;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.service.gl.core.daosupport.DefaultBeanMapper;
import gov.df.fap.service.gl.core.daosupport.MultipleBeanMapper;
import gov.df.fap.service.gl.core.daosupport.PreparedForBeanSql;
import gov.df.fap.service.gl.core.interfaces.ConnectionProvider;
import gov.df.fap.service.gl.core.interfaces.PreparedStatementHandler;
import gov.df.fap.service.gl.core.interfaces.PreparedStatementParamSetter;
import gov.df.fap.service.gl.core.interfaces.ResultSetMapper;
import gov.df.fap.service.gl.core.interfaces.StatementHandler;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.gl.core.AbstractBeanMapper;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.DbUtil;
import gov.df.fap.util.Properties.ClassInfo;
import gov.df.fap.util.Properties.PropertiesUtil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * DAO对于数据库操作的一个辅助类，可以使用继承的方法使用，也可以包含使用.
 * 方便简单的ORM小组件.
 * <p>
 * 对于查询操作:<p>
 * 只要写出来的SQL语句字段名与需要转换的类属性名一致便可,对于不一致
 * 的字段名可用"AS"等关键字进行转换.例如:<p>
 * 		select account_id accountId, account_code as accountCode
 * 			from gl_account;<p>
 * 语句,调用DaoSupport.genericQuery(sql, FAccount.class)方法,便可将将
 * gl_account表中的account_id, account_code字映射到FAccount对象的accountId
 * accountCode属性中.
 * <p>
 * 对于更新操作:<p>
 * 可使用"#"符号指定属性名,将对象映射到数据库表中.例如:<p>
 *      update gl_account set account_id=#accountId#, accountCode="acctmdl.code";
 *      where account_name=#acctmdlName#;<p>
 * 调用daoSupport.execute(sql, Object value)便可将对象更新到数据库表中.
 * @author 
 * @version 2017-03-16
 */
@Component("df.fap.daoSupport")
public class DaoSupport implements DaoOperation {
  @Autowired
  private ConnectionProvider connectionProvider;
  
  /**出于性能考虑，当不需要查询最大页时，默认返回的最大记录数*/
  private static final int DEFAULT_MAX_ROW_COUNT = 100000;
  
  private static final Logger logger = LoggerFactory.getLogger(DaoSupport.class);

  public DaoSupport() {

  }

  public DaoSupport(ConnectionProvider cp) {
    this.connectionProvider = cp;
  }

  public void setConnectionProvider(ConnectionProvider cp) {
    this.connectionProvider = cp;
  }

  public Object genericQueryForObject(String sqlStatement, Class resultClass) {
    return genericQueryForObject(sqlStatement, null, resultClass);
  }

  public Object genericQueryForObject(String sqlStatement, Object[] args, Class resultClass) {
    List list = genericQuery(sqlStatement, args, resultClass);
    if (list.size() > 0)
      return list.get(0);
    else
      return null;
  }

  public List genericQuery(String sqlStatement, ResultSetMapper mapper) {
    Statement st = null;
    ResultSet rs = null;
    Connection conn = null;
    try {
      conn = connectionProvider.getConnection();
      st = DbUtil.createStatement(conn);
      rs = st.executeQuery(sqlStatement);
      mapper.init(rs);
      return mapper.handlerResultSet(rs);
    } catch (SQLException e) {
      throw new RuntimeException("SQL exception :" + sqlStatement, e);
    } finally {
      DbUtil.closeResultSet(rs);
      DbUtil.closeStatement(st);
      DbUtil.closeConnection(conn);
    }
  }

  /**
   *  只做处理，不关闭数据库连接
   */
  public List genericQuery(String sqlStatement, ResultSetMapper mapper, Connection conn) {
    Statement st = null;
    ResultSet rs = null;
    try {
      st = DbUtil.createStatement(conn);
      rs = st.executeQuery(sqlStatement);
      mapper.init(rs);
      return mapper.handlerResultSet(rs);
    } catch (SQLException e) {
      throw new RuntimeException("SQL exception :" + sqlStatement, e);
    } finally {
      DbUtil.closeResultSet(rs);
      DbUtil.closeStatement(st);
    }
  }

  /**
   * 通用查询方法，传入"符合约定SQL!"的SQL语句，返回查询的对象列表
   * @param sqlStatement 符合约定的SQL语句!
   * @param resultClass 结果对象的类
   * @return
   * @see DbUtil
   */
  public List genericQuery(String sqlStatement, Class resultClass) {
    return genericQuery(sqlStatement, new BeanMapperResultSetHandler(sqlStatement, new SingleClassLocator(resultClass)));
  }

  /**
   * 通用查询方法，传入"符合约定SQL!"的SQL语句，以及数据库链接,返回查询的对象列表
   * @param sqlStatement 符合约定的SQL语句!
   * @param resultClass 结果对象的类
   * @param Connection 数据库连接
   * @return
   * @see DbUtil
   */
  public List genericQuery(String sqlStatement, Class resultClass, Connection con) {
    if (null == con) {
      return genericQuery(sqlStatement, new BeanMapperResultSetHandler(sqlStatement,
        new SingleClassLocator(resultClass)));
    } else {
      return genericQuery(sqlStatement, new BeanMapperResultSetHandler(sqlStatement,
        new SingleClassLocator(resultClass)), con);
    }
  }

  /**
   * 通用查询方法，传入"符合约定SQL!"的SQL语句，返回查询的对象列表
   * @param sqlStatement 符合约定的SQL语句!
   * @param resultClass 结果对象的类
   * @return
   * @see DbUtil
   */
  public Object execute(String sqlStatement, PreparedStatementHandler psHandler, PreparedStatementParamSetter psps) {
    PreparedStatement st = null;
    Connection conn = null;
    try {
      conn = connectionProvider.getConnection();
      st = DbUtil.prepareStatement(conn, sqlStatement);
      for (int i = 0; i < psps.size(); i++) {
        psps.setter(st, i + 1);
      }
      return psHandler.handler(st);
    } catch (SQLException e) {
      throw new RuntimeException("SQL exception :" + sqlStatement, e);
    } finally {
      DbUtil.closeStatement(st);
      DbUtil.closeConnection(conn);
    }
  }

  public List genericQuery(String sqlStatement, final Object[] args, ClassLocator classLocator) {
    return (List) execute(sqlStatement, new BeanMapperPreparedStatementHandler(sqlStatement, classLocator),
      new PreparedStatementParamSetter() {
        public void setter(PreparedStatement ps, int i) throws SQLException {
          DbUtil.setParamValue(ps, i, args[i - 1]);
        }

        public int size() {
          if (args == null)
            return 0;
          return args.length;
        }

      });
  }

  /**
   * 根据用户所属类型，获取
   * @param user_id
   * @param rg_code
   * @param set_year
   * @return
   */
  public List findUserEnOrgByUserId(String user_id, String rg_code, int set_year) {
    Connection conn = connectionProvider.getConnection();
    ResultSet rs = null;
    PreparedStatement ps = null;
    final int maxCount = 20;//用户最多可以执行执行的单位数，少于定值 使用 en_id in () 多的使用原有的exists 方法
    try {
      //查询是否是单位类型的用户 查到数据就是单位用户
      ps = conn
        .prepareStatement("select a.user_id  from sys_user_orgtype a where a.USER_ID = ?  and a.org_type = '002' ");
      ps.setString(1, user_id);
      rs = ps.executeQuery();
      if (!rs.next()) {
        return null;
      }
      rs.close();
      ps.close();
      //TODO sys_user_org 添加年度过滤
      ps = conn.prepareStatement("select org_id from sys_user_org where user_id =? and set_year=? ");
      ps.setString(1, user_id);
      ps.setString(2, SessionUtil.getLoginYear());
      rs = ps.executeQuery();
      ArrayList list = new ArrayList();
      int n = 0;
      while (rs.next()) {
        list.add(rs.getString(1));
        n++;
        if (n > maxCount) {
          return null;
        }
      }

      return list;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    } finally {
      DbUtil.closeResultSet(rs);
      DbUtil.closeStatement(ps);
      DbUtil.closeConnection(conn);
    }
  }

  /**
   * 通用查询方法，传入"符合约定SQL!"的SQL语句，返回查询的对象列表
   * @param sqlStatement 符合约定的SQL语句!
   * @param args 参数
   * @param resultClass 结果对象的类
   * @return
   * @see DbUtil
   */
  public List genericQuery(String sqlStatement, final Object[] args, Class resultClass) {
    return genericQuery(sqlStatement, args, new SingleClassLocator(resultClass));
  }

  /**
   * 通用查询方法,传入"符合约定SQL!"的SQL语句，返回查询的对象列表
   * @param sqlStatement 特定SQL语句
   * @param condition 条件对象
   * @param resultClass 返回对象
   * @return
   */
  public List genericQuery(String sqlStatement, final Object condition, final Class resultClass) {
    final PreparedForBeanSql sqlObject = new PreparedForBeanSql(sqlStatement);
    return (List) execute(sqlObject.getPreparedSql(), new BeanMapperPreparedStatementHandler(
      sqlObject.getPreparedSql(), new SingleClassLocator(resultClass)), new BeanMapperPreparedStatementParamSetter(
      sqlObject, condition));
  }

  /**
   * 仅仅查询一个整数
   * @param sql
   * @return
   */
  public int queryForInt(String sql) {
    Connection conn = connectionProvider.getConnection();
    ResultSet rs = null;
    Statement st = null;
    try {
      st = DbUtil.createStatement(conn);
      rs = st.executeQuery(sql);
      if (rs.next()) {
        Integer intValue = (Integer) DbUtil.getResultValue(rs, 1, int.class);
        return intValue == null ? 0 : intValue.intValue();
      } else
        return 0;
    } catch (Exception ex) {
      throw new RuntimeException("Exception when get result set value of sql:" + sql, ex);
    } finally {
      DbUtil.closeResultSet(rs);
      DbUtil.closeStatement(st);
      DbUtil.closeConnection(conn);
    }
  }

  /**
   * 仅仅查询一个整数
   * @param sql
   * @return
   */
  public int queryForInt(String sql, final Object[] params) {
    Connection conn = connectionProvider.getConnection();
    ResultSet rs = null;
    PreparedStatement ps = null;
    try {
      ps = DbUtil.prepareStatement(conn, sql);
      if (params != null) {
        for (int i = 0; i < params.length; i++) {
          ps.setObject(i + 1, params[i]);
        }
      }
      rs = ps.executeQuery();
      if (rs.next()) {
        Integer intValue = (Integer) DbUtil.getResultValue(rs, 1, int.class);
        return intValue == null ? 0 : intValue.intValue();
      } else
        return 0;
    } catch (Exception ex) {
      throw new RuntimeException("Exception when get result set value of sql:" + sql, ex);
    } finally {
      DbUtil.closeResultSet(rs);
      DbUtil.closeStatement(ps);
      DbUtil.closeConnection(conn);
    }
  }

  /**
   * 仅仅查询一个字串
   * @param sql
   * @return
   */
  public String queryForString(String sql) {
    Connection conn = connectionProvider.getConnection();
    ResultSet rs = null;
    Statement st = null;
    try {
      st = DbUtil.createStatement(conn);
      rs = st.executeQuery(sql);
      if (rs.next()) {
        return (String) DbUtil.getResultValue(rs, 1, String.class);
      } else
        return null;
    } catch (Exception ex) {
      throw new RuntimeException("Exception when get result set value of sql:" + sql, ex);
    } finally {
      DbUtil.closeResultSet(rs);
      DbUtil.closeStatement(st);
      DbUtil.closeConnection(conn);
    }
  }

  /**
   * 仅仅查询一个字串
   * @param sql
   * @return
   */
  public String queryForString(String sql, final Object[] params) {
    Connection conn = connectionProvider.getConnection();
    ResultSet rs = null;
    PreparedStatement ps = null;
    try {
      ps = DbUtil.prepareStatement(conn, sql);
      if (params != null) {
        for (int i = 0; i < params.length; i++) {
          ps.setObject(i + 1, params[i]);
        }
      }
      rs = ps.executeQuery();
      if (rs.next()) {
        return (String) DbUtil.getResultValue(rs, 1, String.class);
      } else
        return null;
    } catch (Exception ex) {
      throw new RuntimeException("Exception when get result set value of sql:" + sql, ex);
    } finally {
      DbUtil.closeResultSet(rs);
      DbUtil.closeStatement(ps);
      DbUtil.closeConnection(conn);
    }
  }

  /**
   * 查询一个字符串数组
   * @param sql
   * @return
   */
  public List queryForStrings(String sql) {
    Connection conn = connectionProvider.getConnection();
    ResultSet rs = null;
    Statement st = null;
    List strList = new LinkedList();
    try {
      st = DbUtil.createStatement(conn);
      rs = st.executeQuery(sql);
      while (rs.next()) {
        strList.add(DbUtil.getResultValue(rs, 1, String.class));
      }
    } catch (Exception ex) {
      throw new RuntimeException("Exception when get result set value of sql:" + sql, ex);
    } finally {
      DbUtil.closeResultSet(rs);
      DbUtil.closeStatement(st);
      DbUtil.closeConnection(conn);
    }
    return strList;
  }

  /**
   * 分面查询,SQL约定和查询详细见genericQuery(String, Class)方法
   * @param sql
   * @param clazz
   * @param page
   * @return
 * @throws SQLException 
   * @see DaoSupport#genericQuery(String, Class)
   */
  public List genericQueryByPage(String sql, Class clazz, FPaginationDTO page, boolean queryWithMaxCount) {
    return genericQueryByPage(sql, null, clazz, page, queryWithMaxCount);
  }

  /**
   * 分面查询,SQL约定和查询详细见genericQuery(String, Class)方法
   * @param sql
   * @param args
   * @param clazz
   * @param page
   * @return
 * @throws SQLException 
   * @see DaoSupport#genericQuery(String, Class)
   */
  public List genericQueryByPage(String sql, Object[] args, Class clazz, FPaginationDTO page, boolean queryWithMaxCount) {
    List data = null;//new ArrayList();

    if (page == null)
      return genericQuery(sql, args, clazz);

    if (queryWithMaxCount)
      page.setTotalrows(getRowCount(sql, args));
    else
      page.setTotalrows(DEFAULT_MAX_ROW_COUNT);

    if (page.getCurrpage() >= 1) {
    	if(TypeOfDB.isOracle()) {
    		sql = "select subdata.* from (select rownum r,data.* from(" + sql + ") data where rownum <= "
    				+ (page.getCurrpage() * page.getPagecount()) + ")subdata where subdata.r>" + (page.getCurrpage() - 1)
    				* page.getPagecount();
    	} else if(TypeOfDB.isMySQL()) {
    		sql = "select subdata.* from (select data.* from(" + sql + ") data limit "
    				+ (page.getCurrpage() * page.getPagecount()) + ")subdata limit " + (page.getCurrpage() - 1)
    				* page.getPagecount() + ", " + page.getPagecount();
    	}    	
      data = genericQuery(sql, args, clazz);
    } else
      data = genericQuery(sql, args, clazz);

    //将page_index,page_count还原,方便下一次调用
    return data;
  }

  /**
   * 执行SQL语句
   * @param ddl
   */
  public boolean execute(final String ddl) {
    return ((Boolean) execute(new StatementHandler() {
      public Object statementHandle(Statement st) throws SQLException {
        return Boolean.valueOf(st.execute(ddl));
      }
    })).booleanValue();
  }

  public Object execute(StatementHandler stHandler) {
    Connection conn = connectionProvider.getConnection();
    Statement st = null;
    try {
      st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
      return stHandler.statementHandle(st);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    } finally {
      DbUtil.closeStatement(st);
      DbUtil.closeConnection(conn);
    }
  }

  /**
   * 执行一个更新或者删除SQL语句,返回更新条数.
   * @param sql
   * @return
   */
  public int executeUpdate(final String sql) {
    Integer updateCount = (Integer) execute(new StatementHandler() {
      public Object statementHandle(Statement st) throws SQLException {
        return new Integer(st.executeUpdate(sql));
      }
    });
    return updateCount.intValue();
  }

  /**
   * 执行一个更新的SQL语句,值从BEAN中取,字段与BEAN属性的映射在SQL语句中体现.
   * @param sql
   * @param bean
   * @return
   */
  public int executeUpdate(final String sql, Object bean) {
    List list = new ArrayList();
    list.add(bean);
    return batchExcute(sql, list)[0];
  }

  /**
   * 
   * @param sql
   * @param callableHandler
   * @return
   */
  public Object executeCall(String sql, CallableStatementHandler callableHandler) {
    Connection con = connectionProvider.getConnection();
    CallableStatement cs = null;
    try {
      cs = con.prepareCall(sql);
      return callableHandler.handleCallStatement(cs);
    } catch (SQLException e) {
      throw new RuntimeException("Exception when execute:" + sql, e);
    } finally {
      DbUtil.closeStatement(cs);
      DbUtil.closeConnection(con);
    }
  }

  //	public int execute(String sql, Object values){
  //		return DbUtil.executeUpdate(connectionProvider.getConnection(), sql);
  //	}

  public int[] batchExcute(PreparedForBeanSql sqlObject, BatchPreparedStatementParamSetter psps) {
    PreparedStatement st = null;
    Connection conn = null;
    try {
      conn = connectionProvider.getConnection();
      st = DbUtil.prepareStatement(conn, sqlObject.getPreparedSql());
      for (int i = 0; i < psps.batchCount(); i++) {
        for (int j = 0; j < psps.paramCount(); j++) {
          psps.setter(st, j + 1, i);
        }
        st.addBatch();
        //st.clearParameters();
      }
      return st.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException("SQL Exception:" + sqlObject.getPreparedSql(), e);
    } finally {
      DbUtil.closeStatement(st);
      DbUtil.closeConnection(conn);
    }
  }

  public int[] batchExcute(final String sql, final List values) {

    if (values == null || values.size() == 0) {
      return new int[0];
    }
    final PreparedForBeanSql sqlObject = new PreparedForBeanSql(sql);
    return batchExcute(sqlObject, new BatchPreparedStatementParamSetter() {

      int index = 0;//for replace condition object at first parameter setting.

      BeanMapperPreparedStatementParamSetter setter = new BeanMapperPreparedStatementParamSetter(sqlObject, values
        .get(0));

      public void setter(PreparedStatement ps, int paramIndex, int batchIndex) {
        if (batchIndex != index) {
          index = batchIndex;
          setter.replaceCondition(values.get(batchIndex));
        }
        setter.setter(ps, paramIndex);
      }

      public int paramCount() {
        return setter.size();
      }

      public int batchCount() {
        return values.size();
      }

    });
  }

  /**
     * 查询结果总记录数
     * @param sql 查询SQL语句
     * @return 记录总数
     */
  public int getRowCount(String sql) {
    return queryForInt("select count(1) as total from (" + sql + ") a");
  }

  /**
   * 查询结果总记录数
   * @param sql 查询SQL语句
   * @return 记录总数
   */
  public int getRowCount(String sql, Object[] args) {
    return queryForInt("select count(1) as total from (" + sql + ") a", args);
  }

  /**
   * 类restult set映射处理器,负责转换result set到对象.
   */
  class BeanMapperResultSetHandler extends ResultSetMapper {
    AbstractBeanMapper beanMapper = null;

    String sqlStatement = null;

    ClassLocator resultClass = null;

    BeanMapperResultSetHandler(String sqlStatement, ClassLocator resultClass) {
      this.sqlStatement = sqlStatement;
      this.resultClass = resultClass;
    }

    public Object handleRow(ResultSet rs) {
      try {
        return beanMapper.resultSetToObject(rs);
      } catch (SQLException e) {
        throw new RuntimeException("exception when handler row!", e);
      }
    }

    public void init(ResultSet rs) {
      if (beanMapper == null) {
        try {
          if (resultClass instanceof SingleClassLocator)
            beanMapper = DefaultBeanMapper.beanMapperFactory(sqlStatement, rs.getMetaData(),
              resultClass.mappingClass(null));
          else
            beanMapper = new MultipleBeanMapper(sqlStatement, rs.getMetaData(), resultClass);
        } catch (SQLException e) {
          throw new RuntimeException("exception when transfer ResutlSet to Class", e);
        }
      }
    }
  }

  /**
   * Statement 处理器
   */
  class BeanMapperPreparedStatementHandler implements PreparedStatementHandler {
    BeanMapperResultSetHandler rsHandler = null;

    BeanMapperPreparedStatementHandler(String sqlStatement, ClassLocator resultClass) {
      rsHandler = new BeanMapperResultSetHandler(sqlStatement, resultClass);
    }

    public Object handler(PreparedStatement ps) {
      ResultSet rs = null;
      try {
        rs = ps.executeQuery();
        rsHandler.init(rs);
        return rsHandler.handlerResultSet(rs);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      } finally {
        DbUtil.closeResultSet(rs);
      }
    }
  }

  /**
   * 参数插入器
   * @author 
   *
   */
  class BeanMapperPreparedStatementParamSetter implements PreparedStatementParamSetter {
    ClassInfo classInfo = null;

    String[] propertyNames = null;

    int size = 0;

    Object condition = null;

    public BeanMapperPreparedStatementParamSetter(PreparedForBeanSql sqlObject, Object condition) {
      this.classInfo = ClassInfo.classInfoFactory(condition.getClass());
      propertyNames = sqlObject.getPropertyNames();
      size = sqlObject.propertyCount();
      this.condition = condition;
    }

    public void replaceCondition(Object condition) {
      this.condition = condition;
    }

    public void setter(PreparedStatement ps, int i) {
      try {
        //支持取属性值比如#account.hello#,意思是取account对象中的hello属性.
        DbUtil.setParamValue(ps, i, PropertiesUtil.getProperty(condition, propertyNames[i - 1]));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    public int size() {
      return size;
    }
  }

  /**
   * 单一类的类定位器
   * @author 
   *
   */
  class SingleClassLocator extends ClassLocatorAdapter {

    Class resultClass;

    public SingleClassLocator(Class resultClass) {
      this.resultClass = resultClass;
    }

    public Class mappingClass(Object fieldValue) {
      return resultClass;
    }
  }
}
