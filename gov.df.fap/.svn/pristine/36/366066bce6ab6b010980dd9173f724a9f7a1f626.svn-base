
package gov.df.fap.api.portal;

import gov.df.fap.util.factory.ServiceFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/** 
 * 
 * JDBC数据库操作相关的一些辅助方法
 * 所处理对象将不会缓存
 *  
 */
@Service
public class DBHelper  {
	private static final Logger logger = Logger.getLogger(DBHelper.class);
	private static JdbcTemplate jdbc =(JdbcTemplate) ServiceFactory.getBean("jdbcTemplate"); 
	
	
	/**
	 * 关闭数据库连接和语句
   * @param conn
   * @param stmt
   * @param rs
   * @return
	 */
	public static void closeConnection(Connection conn, Statement stmt,
			ResultSet rs) {
		try {
			if (null != rs) {
				rs.close();
			}
		} catch (SQLException e) {
      logger.error(e);
		}
		try {
			if (null != stmt) {
				stmt.close();
			}
		} catch (SQLException e) {
      logger.error(e);
		}
		try {
			if (null != conn) {
				if (!conn.isClosed()) {
					conn.close();
				}
			}
		} catch (SQLException e) {
      logger.error(e);
		}
	}

	/**
	 * 设置参数：用于变量绑定中参数的设置
	 * @param pst
	 * @param params
	 * @throws SQLException
	 */
	public static void setStatementParameters(PreparedStatement pst,
			Object[] params) throws SQLException {
		for (int i = 0; i < params.length; ++i) {
			Object obj = params[i];
			if (null == obj) {
				pst.setNull(i + 1, Types.CHAR);
			} else if (obj instanceof java.sql.Date) {
				pst.setDate(i + 1, (java.sql.Date) obj);
			} else if (obj instanceof java.sql.Timestamp) {
				pst.setTimestamp(i + 1, (java.sql.Timestamp) obj);
			} else {
				pst.setObject(i + 1, obj);
			}
		}
	}
  /**
   * 执行指定的查询并返回结果的第一个字段值
   * @param sql
   * @param params
   * @return
   */  
  public static Object queryOneValue(String sql, Object[] params) {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = jdbc.getDataSource().getConnection(); 
      stmt = conn.prepareStatement(sql);
      if (null != params && params.length > 0) {
        setStatementParameters(stmt, params);
      }
      rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getObject(1);
      }
    } catch (SQLException e){
      logger.error(e);
      throw new RuntimeException(e);
    } finally {
      closeConnection(conn, stmt, rs);
    }
    return null;
  }

  /**
   * 执行sql语句，用来进行插入、删除和更新操作
   * @param sql
   * @param params
   * @return
   */
  public static int executeUpdate(String sql, Object[] params){
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = jdbc.getDataSource().getConnection(); 
      stmt = conn.prepareStatement(sql);
      if (null != params && params.length > 0) {
        setStatementParameters(stmt, params);
      }
      return stmt.executeUpdate();
    } catch(SQLException e){
      logger.error(e);
      throw new RuntimeException(e);
    } finally {
      DBHelper.closeConnection(conn, stmt, null);
    }
  }
  public static boolean executeCreateSql(String sql){
    Connection conn = null;
    Statement stmt = null;
    try {
      conn = jdbc.getDataSource().getConnection(); 
      stmt = conn.createStatement();
      return stmt.execute(sql);
    } catch(SQLException e){
      logger.error(e);
      throw new RuntimeException(e);
    } finally {
      DBHelper.closeConnection(conn, stmt, null);
    }
  }
  
  /**
   * 查询操作，返回结果为List，list中的元素为Map
   * @param sql
   * @param params
   * @return
   */
  public static List queryForList(String sql, Object[] params){
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = jdbc.getDataSource().getConnection(); 
      stmt = conn.prepareStatement(sql);
      if (null != params && params.length > 0) {
        setStatementParameters(stmt, params);
      }
      rs = stmt.executeQuery();
      return resultSetToList(rs);
    } catch(SQLException e){
      logger.error(e);
      throw new RuntimeException(e);
    } finally {
      DBHelper.closeConnection(conn, stmt, rs);
    }  
  }
  
  /**
   * 查询操作，返回结果为Map
   * @param sql
   * @param params
   * @return
   */
  public static Map queryForMap(String sql, Object[] params){
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Map result = null;
    try {
      conn = jdbc.getDataSource().getConnection(); 
      stmt = conn.prepareStatement(sql);
      if (null != params && params.length > 0) {
        setStatementParameters(stmt, params);
      }
      rs = stmt.executeQuery();
      
      List temp = resultSetToList(rs);
      if(temp.isEmpty()){
        result = new HashMap();
      }else{
        result = (Map)temp.get(0);
      }
    } catch(SQLException e){
      logger.error(e);
      throw new RuntimeException(e);
    } finally {
      DBHelper.closeConnection(conn, stmt, rs);
    }
    return result;
  }
  
  public  long getNextSequence(String sequenceName) {
    String sql = "select " + sequenceName + ".nextval from dual";
    String[] params = new String[] {};
    Object lastNumber = queryOneValue(sql, params);
    if (null != lastNumber) {
      return Long.parseLong(lastNumber.toString(), 10);
    }
    throw new RuntimeException("Error041491: 找不到 " + sequenceName + " 的ID");
  }
  /**
   * 结果集转换为数组List，List中包含Map
   * @param rs
   * @return
   * @throws SQLException
   */
  private static List resultSetToList(ResultSet rs) throws SQLException{
    List result = new ArrayList();
    ResultSetMetaData metaData = rs.getMetaData();
    int count = metaData.getColumnCount();
    while(rs.next()){
      Map resultMap = new HashMap();
      for(int i = 0; i < count; i++){
        String columnName = metaData.getColumnName(i + 1);
        resultMap.put(columnName, rs.getObject(i + 1));
      }
      result.add(resultMap);
    }
    return result;
  }
  
  /**
   * 简单处理参数，参数通过";"隔开
   * @param condition
   * @return
   */
  public static Map parseParamsSimpleForSql(String condition){
    Map map = new HashMap();
    parseParamsSimpleForSql(condition, map);
    return map;
  }
  
  /**
   * 简单处理参数，参数通过";"隔开
   * 
   * @param condition
   * @return
   */
  public static void parseParamsSimpleForSql(String condition, Map map) {
    if (map == null) {
      map = new HashMap();
    }
    if (condition != null && condition.length() != 0) {
      String[] conds = condition.split(";");
      for (int i = 0; i < conds.length; i++) {
        if (conds[i].trim().length() == 0) {
          continue;
        }
        String[] entry = conds[i].split("=");
        if (entry.length > 1) {
          if (entry[1].indexOf("|") > 0) {// 处理数组
            String[] multiValue = entry[1].split("\\|");
            List valueList = new ArrayList();
            for (int j = 0; j < multiValue.length; j++) {
              if (multiValue[j] != null
                  && multiValue[j].trim().length() > 0)
                valueList.add(multiValue[j].trim());
            }
            map.put(entry[0].trim(), valueList);
          } else {
            map.put(entry[0].trim(), entry[1].trim());
          }
        } else {
          map.remove(entry[0]);
        }
      }
    }
  }

}
