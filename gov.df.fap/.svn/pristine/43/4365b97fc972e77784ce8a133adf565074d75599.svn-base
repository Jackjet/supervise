package gov.df.fap.service.util.dao.ufgovdao;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: ufgov</p>
 * @author not attributable
 * @version 1.0
 */

public class UfConnection implements Connection, UfLogQueue {

	private Connection con;//代理连接

	public static int longsqltime = 0;

	//是否将出错的sql输出
	public static boolean isErr = "true".equalsIgnoreCase(System
			.getProperty("cyerr"));

	private static Vector logSql = new Vector();

	static {
		try {
			//取要重点关注的时间，执行超过多少时间时毫秒，将纪录log
			String stime = System.getProperty("sqltime");
			if (stime != null) {
				longsqltime = Integer.parseInt(stime);
			}
		} catch (Exception ex) {

		}
	}

	/**
	 * 纪录日志
	 * @param startTime 开始运行时间
	 * @param endTime 结束运行时间
	 * @param sql 执行的sql语句
	 */
	public void log(long startTime, long endTime, String methodName, String sql) {

		if (sql.length() < 20 && sql.indexOf("DUAL") > 0)
			return;
		if (sql.indexOf("FROM QRTZ_TRIGGERS WHERE") > 0)
			return;

		StringBuffer strBuf = new StringBuffer(200);
		strBuf.append(getDateFromNow(0)).append("执行").append(methodName)
				.append(":\r\n").append(sql).append("\r\n").append("耗时")
				.append(endTime - startTime).append("毫秒\r\n");
		//保证sql语句不至于太多
		if (logSql.size() < 500)
			logSql.add(strBuf.toString());

	}

	/**
	 * 记录日志
	 * @param sqlex sql执行出现异常
	 * @param sql 执行的sql语句
	 */
	public void err(SQLException sqlex, String sql) {
		StringBuffer strBuf = new StringBuffer(200);
		
		strBuf.append(getDateFromNow(0)).append("数据库连接").append(hashCode())
				.append("执行有误:\r\n").append(sql).append("\r\n");
		StackTraceElement[] trace = sqlex.getStackTrace();
		for (int i=0; i < trace.length; i++)
			strBuf.append("\r\n\tat ").append(trace[i]);
//		保证sql语句不至于太多
		if (logSql.size() < 500)
			logSql.add(strBuf.toString());

	}

	public UfConnection(Connection con) throws SQLException {
		this.con = con;
	}

	
	public CallableStatement prepareCall(String sql) throws SQLException {
		return new UfCallableStatement(this, con.prepareCall(sql), sql);
	}

	public String nativeSQL(String sql) throws SQLException {
		return con.nativeSQL(sql);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		con.setAutoCommit(autoCommit);
	}

	public boolean getAutoCommit() throws SQLException {
		return con.getAutoCommit();
	}

	public void commit() throws SQLException {
		con.commit();
	}

	public void rollback() throws SQLException {
		con.rollback();
	}

	/**
	 * 数据库连接关闭前将打开的游标statement关闭
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		con.close();

	}

	public boolean isClosed() throws SQLException {

		return con.isClosed();
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return con.getMetaData();
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		con.setReadOnly(readOnly);
	}

	public boolean isReadOnly() throws SQLException {
		return con.isReadOnly();
	}

	public void setCatalog(String catalog) throws SQLException {
		con.setCatalog(catalog);
	}

	public String getCatalog() throws SQLException {
		return con.getCatalog();
	}

	public void setTransactionIsolation(int level) throws SQLException {
		con.setTransactionIsolation(level);
	}

	public int getTransactionIsolation() throws SQLException {
		return con.getTransactionIsolation();
	}

	public SQLWarning getWarnings() throws SQLException {
		return con.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		con.clearWarnings();
		;
	}


	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return new UfCallableStatement(this, con.prepareCall(sql,
				resultSetType, resultSetConcurrency), sql);
	}

	public Map getTypeMap() throws SQLException {
		return con.getTypeMap();
	}



	public void setHoldability(int holdability) throws SQLException {
		con.setHoldability(holdability);
	}

	public int getHoldability() throws SQLException {
		return con.getHoldability();
	}

	public Savepoint setSavepoint() throws SQLException {
		return con.setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return con.setSavepoint(name);
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		con.rollback(savepoint);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		con.releaseSavepoint(savepoint);
	}

	
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return new UfCallableStatement(this, con.prepareCall(sql,
				resultSetType, resultSetConcurrency, resultSetHoldability), sql);
	}

	

	public int hashCode() {
		return con.hashCode();
	}

	/**
	 * 计算从现在开始几天后的时间
	 * @param afterDay 天数
	 * @return 从现在开始afterDay天后的时间
	 * @author cc
	 */
	public static String getDateFromNow(int afterDay) {
		GregorianCalendar calendar = new GregorianCalendar();
		Date date = calendar.getTime();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + afterDay);
		date = calendar.getTime();

		return df.format(date);
	}

	/**
	 * 将保存的sql脚本取出
	 * @return
	 */
	public static List pushOutSQL() {
		ArrayList sqlList = new ArrayList();
		synchronized (logSql) {
			sqlList.addAll(logSql);
			logSql.clear();
		}
		return sqlList;
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

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createStatement() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}