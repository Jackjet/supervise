package gov.df.fap.util;

import gov.df.fap.util.Properties.ClassInfo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/**
 * 数据库操作封装，这个类很薄的做了一层ORM，而且前提是基于几个约定：
 * <p>1.类属性都是小写或者是标准的JAVA命名.
 * <p>2.字段名或者SQL查询时的别名跟JAVA命名一致
 * 目前仅仅支持简单JAVA属性。
 * @author 
 *
 */
public class DbUtil {

	public static final int UNKOWN_FIELD_TYPE = Integer.MIN_VALUE;

	
	public static PreparedStatement prepareStatement(Connection conn, String sqlStatement) throws SQLException{
		return conn.prepareStatement(sqlStatement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}
	
	public static Statement createStatement(Connection conn) throws SQLException{
		return conn.createStatement();
	}
	
	/**
	 * 设置PreparedStatement参数值.
	 * @param ps
	 * @param args
	 * @throws SQLException
	 */
	public static void setParamValue(PreparedStatement ps, Object[] args) throws SQLException{
		for (int i = 0; i < args.length; i++){
			setParamValue(ps, i+1, args[i], UNKOWN_FIELD_TYPE);
		}
	}
	
	/**
	 * 设置PreparedStatement参数值.
	 * @param ps
	 * @param args
	 * @throws SQLException
	 */
	public static void setParamValue(PreparedStatement ps, String[] propertyNames, Object condition) throws SQLException{
		try {
			ClassInfo classInfo = ClassInfo.classInfoFactory(condition.getClass());
			for (int i = 0; i < propertyNames.length; i++){
				try {
					setParamValue(ps, i+1, classInfo.getGetterMethod(propertyNames[i]).invoke(condition, null), UNKOWN_FIELD_TYPE);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 设置PreparedStatement参数值
	 * @param ps
	 * @param paramIndex
	 * @param value
	 * @param valueType
	 * @throws SQLException 
	 */
	public static void setParamValue(PreparedStatement ps, int paramIndex, Object value) throws SQLException{
		setParamValue(ps, paramIndex, value, UNKOWN_FIELD_TYPE);
	}
	
	/**
	 * 设置PreparedStatement参数值
	 * @param ps
	 * @param paramIndex
	 * @param value
	 * @param valueType
	 * @throws SQLException 
	 */
	public static void setParamValue(PreparedStatement ps, int paramIndex, Object value, int valueType) throws SQLException{
		if (value == null){
			ps.setNull(paramIndex, Types.VARCHAR);
		}else if (valueType == UNKOWN_FIELD_TYPE){
			ps.setObject(paramIndex, value);
		}else{
			ps.setObject(paramIndex, value);
		}
	}
	
	/**
	 * 从ResutlSet中取结果值。
	 * @param rs
	 * @param valueIndex
	 * @param valueType
	 * @return
	 */
	public static Object getResultValue(ResultSet rs, int valueIndex, Class valueType){
		try {
			if (valueType == int.class || valueType == Integer.class)
				return new Integer(rs.getInt(valueIndex));
			else if (valueType == String.class){
				return rs.getString(valueIndex);
			}else
				throw new IllegalArgumentException("param valueType illegal, it supportes integer and String only!");
		} catch (SQLException e) {
			throw new RuntimeException("Exception when get ResutlSet value by index:"+valueIndex, e);
		}
	}
	
	/**
	 * 
	 * @param rs
	 * @param valueIndex
	 * @param valueType
	 * @return
	 */
	public static Object getResultValue(ResultSet rs, String columnName, Class valueType){
		try {
			if (valueType == int.class || valueType == Integer.class)
				return new Integer(rs.getInt(columnName));
			else if (valueType == String.class){
				return rs.getString(columnName);
			}else
				throw new IllegalArgumentException("param valueType illegal, it supportes integer and String only!");
		} catch (SQLException e) {
			throw new RuntimeException("Exception when get ResutlSet value by column name:"+columnName, e);
		}
	}
	
	/**
	 * 解释预编译SQL语句.
	 * @param sqlStatement
	 * @param args
	 * @return
	 */
	public static String parsePreparedSql(String sqlStatement, Object[] args){
		String errorSql = new String(sqlStatement);
		if (args != null){
			int argsIndex = 0;
			while(errorSql.indexOf("?") > -1 && argsIndex < args.length){
				Object param = args[argsIndex];
				if (param == null)
					param = "null";
				if (param instanceof String || param instanceof Character)
					param = "'"+param+"'";
				errorSql = errorSql.replaceFirst("\\?", param.toString());
				argsIndex++;
			}
		}
		return errorSql;
	}
	
	/**
	 * 关闭ResultSet
	 * @param rs
	 */
	public static void closeResultSet(ResultSet rs){
		try{
			if (rs != null)
				rs.close();
		}catch(SQLException ex){
			throw new RuntimeException("Exception when close ResultSet.");
		}
	}
	
	/**
	 * 关闭Statement
	 * @param st
	 */
	public static void closeStatement(Statement st){
		try{
			if (st != null)
				st.close();
		}catch(SQLException ex){
			throw new RuntimeException("Exception when close Statement.");
		}
	}
	/**
	 * 关闭Statement
	 * @param st
	 */
	public static void closeStatement(CallableStatement st){
		try{
			if (st != null)
				st.close();
		}catch(SQLException ex){
			throw new RuntimeException("Exception when close Statement.");
		}
	}
	
	public static void closeConnection(Connection conn){
		try{
			if (conn != null)
				conn.close();
		}catch(SQLException ex){
			throw new RuntimeException("Exception when close Connection.");
		}
	}
}


