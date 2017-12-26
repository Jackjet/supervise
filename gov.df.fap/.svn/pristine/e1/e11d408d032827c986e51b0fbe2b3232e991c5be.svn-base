package gov.df.fap.service.util.dao.ufgovdao;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;
import java.sql.Ref;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Array;
import java.util.Calendar;
import java.net.URL;
import java.io.InputStream;
import java.io.Reader;
import java.sql.NClob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.ParameterMetaData;
import java.sql.RowId;
import java.sql.SQLWarning;
import java.sql.Connection;
import java.sql.SQLXML;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: </p>
 * @author hult
 * @version 1.0
 */

public class UfCallableStatement implements CallableStatement {
	private CallableStatement cs = null;

	private UfLogQueue queue = null;

	private String sql = null;

	public UfCallableStatement(UfLogQueue queue,CallableStatement cs, String sql) {
		this.queue = queue;
		this.cs = cs;
		this.sql = sql;
	}

	public void registerOutParameter(int parameterIndex, int sqlType)
			throws SQLException {
		cs.registerOutParameter(parameterIndex, sqlType);
	}

	public void registerOutParameter(int parameterIndex, int sqlType, int scale)
			throws SQLException {
		cs.registerOutParameter(parameterIndex, sqlType, scale);
	}

	public boolean wasNull() throws SQLException {
		return cs.wasNull();
	}

	public String getString(int parameterIndex) throws SQLException {
		return cs.getString(parameterIndex);
	}

	public boolean getBoolean(int parameterIndex) throws SQLException {
		return cs.getBoolean(parameterIndex);
	}

	public byte getByte(int parameterIndex) throws SQLException {
		return cs.getByte(parameterIndex);
	}

	public short getShort(int parameterIndex) throws SQLException {
		return cs.getShort(parameterIndex);
	}

	public int getInt(int parameterIndex) throws SQLException {
		return cs.getInt(parameterIndex);
	}

	public long getLong(int parameterIndex) throws SQLException {
		return cs.getLong(parameterIndex);
	}

	public float getFloat(int parameterIndex) throws SQLException {
		return cs.getFloat(parameterIndex);
	}

	public double getDouble(int parameterIndex) throws SQLException {
		return cs.getDouble(parameterIndex);
	}

	public BigDecimal getBigDecimal(int parameterIndex, int scale)
			throws SQLException {
		return cs.getBigDecimal(parameterIndex, scale);
	}

	public byte[] getBytes(int parameterIndex) throws SQLException {
		return cs.getBytes(parameterIndex);
	}

	public Date getDate(int parameterIndex) throws SQLException {
		return cs.getDate(parameterIndex);
	}

	public Time getTime(int parameterIndex) throws SQLException {
		return cs.getTime(parameterIndex);
	}

	public Timestamp getTimestamp(int parameterIndex) throws SQLException {
		return cs.getTimestamp(parameterIndex);
	}

	public Object getObject(int parameterIndex) throws SQLException {
		return cs.getObject(parameterIndex);
	}

	public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
		return cs.getBigDecimal(parameterIndex);
	}

	public Object getObject(int i, Map map) throws SQLException {
		return cs.getObject(i, map);
	}

	public Ref getRef(int i) throws SQLException {
		return cs.getRef(i);
	}

	public Blob getBlob(int i) throws SQLException {
		return cs.getBlob(i);
	}

	public Clob getClob(int i) throws SQLException {
		return cs.getClob(i);
	}

	public Array getArray(int i) throws SQLException {
		return cs.getArray(i);
	}

	public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
		return cs.getDate(parameterIndex, cal);
	}

	public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
		return cs.getTime(parameterIndex, cal);
	}

	public Timestamp getTimestamp(int parameterIndex, Calendar cal)
			throws SQLException {
		return cs.getTimestamp(parameterIndex, cal);
	}

	public void registerOutParameter(int paramIndex, int sqlType,
			String typeName) throws SQLException {
		cs.registerOutParameter(paramIndex, sqlType, typeName);
	}

	public void registerOutParameter(String parameterName, int sqlType)
			throws SQLException {
		cs.registerOutParameter(parameterName, sqlType);
	}

	public void registerOutParameter(String parameterName, int sqlType,
			int scale) throws SQLException {
		cs.registerOutParameter(parameterName, sqlType, scale);

	}

	public void registerOutParameter(String parameterName, int sqlType,
			String typeName) throws SQLException {
		cs.registerOutParameter(parameterName, sqlType, typeName);
	}

	public URL getURL(int parameterIndex) throws SQLException {
		return cs.getURL(parameterIndex);
	}

	public void setURL(String parameterName, URL val) throws SQLException {
		cs.setURL(parameterName, val);
	}

	public void setNull(String parameterName, int sqlType) throws SQLException {
		cs.setNull(parameterName, sqlType);
	}

	public void setBoolean(String parameterName, boolean x) throws SQLException {
		cs.setBoolean(parameterName, x);
	}

	public void setByte(String parameterName, byte x) throws SQLException {
		cs.setByte(parameterName, x);
	}

	public void setShort(String parameterName, short x) throws SQLException {
		cs.setShort(parameterName, x);
	}

	public void setInt(String parameterName, int x) throws SQLException {
		cs.setInt(parameterName, x);
	}

	public void setLong(String parameterName, long x) throws SQLException {
		cs.setLong(parameterName, x);
	}

	public void setFloat(String parameterName, float x) throws SQLException {
		cs.setFloat(parameterName, x);
	}

	public void setDouble(String parameterName, double x) throws SQLException {
		cs.setDouble(parameterName, x);
	}

	public void setBigDecimal(String parameterName, BigDecimal x)
			throws SQLException {
		cs.setBigDecimal(parameterName, x);
	}

	public void setString(String parameterName, String x) throws SQLException {
		cs.setString(parameterName, x);
	}

	public void setBytes(String parameterName, byte[] x) throws SQLException {
		cs.setBytes(parameterName, x);
	}

	public void setDate(String parameterName, Date x) throws SQLException {
		cs.setDate(parameterName, x);
	}

	public void setTime(String parameterName, Time x) throws SQLException {
		cs.setTime(parameterName, x);
	}

	public void setTimestamp(String parameterName, Timestamp x)
			throws SQLException {
		cs.setTimestamp(parameterName, x);
	}

	public void setAsciiStream(String parameterName, InputStream x, int length)
			throws SQLException {
		cs.setAsciiStream(parameterName, x, length);
	}

	public void setBinaryStream(String parameterName, InputStream x, int length)
			throws SQLException {
		cs.setBinaryStream(parameterName, x, length);
	}

	public void setObject(String parameterName, Object x, int targetSqlType,
			int scale) throws SQLException {
		cs.setObject(parameterName, x, targetSqlType, scale);
	}

	public void setObject(String parameterName, Object x, int targetSqlType)
			throws SQLException {
		cs.setObject(parameterName, x, targetSqlType);
	}

	public void setObject(String parameterName, Object x) throws SQLException {
		cs.setObject(parameterName, x);
	}

	public void setCharacterStream(String parameterName, Reader reader,
			int length) throws SQLException {
		cs.setCharacterStream(parameterName, reader, length);
	}

	public void setDate(String parameterName, Date x, Calendar cal)
			throws SQLException {
		cs.setDate(parameterName, x, cal);
	}

	public void setTime(String parameterName, Time x, Calendar cal)
			throws SQLException {
		cs.setTime(parameterName, x, cal);
	}

	public void setTimestamp(String parameterName, Timestamp x, Calendar cal)
			throws SQLException {
		cs.setTimestamp(parameterName, x, cal);
	}

	public void setNull(String parameterName, int sqlType, String typeName)
			throws SQLException {
		cs.setNull(parameterName, sqlType, typeName);
	}

	public String getString(String parameterName) throws SQLException {
		return cs.getString(parameterName);
	}

	public boolean getBoolean(String parameterName) throws SQLException {
		return cs.getBoolean(parameterName);
	}

	public byte getByte(String parameterName) throws SQLException {
		return cs.getByte(parameterName);
	}

	public short getShort(String parameterName) throws SQLException {
		return cs.getShort(parameterName);
	}

	public int getInt(String parameterName) throws SQLException {
		return cs.getInt(parameterName);
	}

	public long getLong(String parameterName) throws SQLException {
		return cs.getLong(parameterName);
	}

	public float getFloat(String parameterName) throws SQLException {
		return cs.getFloat(parameterName);
	}

	public double getDouble(String parameterName) throws SQLException {
		return cs.getDouble(parameterName);
	}

	public byte[] getBytes(String parameterName) throws SQLException {
		return cs.getBytes(parameterName);
	}

	public Date getDate(String parameterName) throws SQLException {
		return cs.getDate(parameterName);
	}

	public Time getTime(String parameterName) throws SQLException {
		return cs.getTime(parameterName);
	}

	public Timestamp getTimestamp(String parameterName) throws SQLException {
		return cs.getTimestamp(parameterName);
	}

	public Object getObject(String parameterName) throws SQLException {
		return cs.getObject(parameterName);
	}

	public BigDecimal getBigDecimal(String parameterName) throws SQLException {
		return cs.getBigDecimal(parameterName);
	}

	public Object getObject(String parameterName, Map map) throws SQLException {
		return cs.getObject(parameterName, map);
	}

	public Ref getRef(String parameterName) throws SQLException {
		return cs.getRef(parameterName);
	}

	public Blob getBlob(String parameterName) throws SQLException {
		return cs.getBlob(parameterName);
	}

	public Clob getClob(String parameterName) throws SQLException {
		return cs.getClob(parameterName);
	}

	public Array getArray(String parameterName) throws SQLException {
		return cs.getArray(parameterName);
	}

	public Date getDate(String parameterName, Calendar cal) throws SQLException {
		return cs.getDate(parameterName, cal);
	}

	public Time getTime(String parameterName, Calendar cal) throws SQLException {
		return cs.getTime(parameterName, cal);
	}

	public Timestamp getTimestamp(String parameterName, Calendar cal)
			throws SQLException {
		return cs.getTimestamp(parameterName, cal);
	}

	public URL getURL(String parameterName) throws SQLException {
		return cs.getURL(parameterName);
	}

	public ResultSet executeQuery() throws SQLException {
		return cs.executeQuery();
	}

	public int executeUpdate() throws SQLException {
		long startQuery = System.currentTimeMillis();
		int iRen = cs.executeUpdate();
		long endQuery = System.currentTimeMillis();
		this.queue.log(startQuery, endQuery, "CallableStatement.executeUpdate",
				sql);
		return iRen;

	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		cs.setNull(parameterIndex, sqlType);
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		cs.setBoolean(parameterIndex, x);
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		cs.setByte(parameterIndex, x);
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		cs.setShort(parameterIndex, x);
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		cs.setInt(parameterIndex, x);
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		cs.setLong(parameterIndex, x);
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		cs.setFloat(parameterIndex, x);
	}

	public void setDouble(int parameterIndex, double x) throws SQLException {
		cs.setDouble(parameterIndex, x);
	}

	public void setBigDecimal(int parameterIndex, BigDecimal x)
			throws SQLException {
		cs.setBigDecimal(parameterIndex, x);
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		cs.setString(parameterIndex, x);
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		cs.setBytes(parameterIndex, x);
	}

	public void setDate(int parameterIndex, Date x) throws SQLException {
		cs.setDate(parameterIndex, x);
	}

	public void setTime(int parameterIndex, Time x) throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setTime() not yet implemented.");
	}

	public void setTimestamp(int parameterIndex, Timestamp x)
			throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setTimestamp() not yet implemented.");
	}

	public void setAsciiStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setAsciiStream() not yet implemented.");
	}

	public void setUnicodeStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setUnicodeStream() not yet implemented.");
	}

	public void setBinaryStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setBinaryStream() not yet implemented.");
	}

	public void clearParameters() throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method clearParameters() not yet implemented.");
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType,
			int scale) throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setObject() not yet implemented.");
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType)
			throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setObject() not yet implemented.");
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setObject() not yet implemented.");
	}

	public boolean execute() throws SQLException {
		long startQuery = System.currentTimeMillis();
		boolean rb = cs.execute();
		long endQuery = System.currentTimeMillis();
		this.queue.log(startQuery, endQuery, "CallableStatement.execute", sql);
		return rb;
	}

	public void addBatch() throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method addBatch() not yet implemented.");
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length)
			throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setCharacterStream() not yet implemented.");
	}

	public void setRef(int i, Ref x) throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setRef() not yet implemented.");
	}

	public void setBlob(int i, Blob x) throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setBlob() not yet implemented.");
	}

	public void setClob(int i, Clob x) throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setClob() not yet implemented.");
	}

	public void setArray(int i, Array x) throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setArray() not yet implemented.");
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method getMetaData() not yet implemented.");
	}

	public void setDate(int parameterIndex, Date x, Calendar cal)
			throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setDate() not yet implemented.");
	}

	public void setTime(int parameterIndex, Time x, Calendar cal)
			throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setTime() not yet implemented.");
	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
			throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setTimestamp() not yet implemented.");
	}

	public void setNull(int paramIndex, int sqlType, String typeName)
			throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setNull() not yet implemented.");
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setURL() not yet implemented.");
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		/**@todo Implement this java.sql.PreparedStatement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method getParameterMetaData() not yet implemented.");
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method executeQuery() not yet implemented.");
	}

	public int executeUpdate(String sql) throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method executeUpdate() not yet implemented.");
	}

	public void close() throws SQLException {
		cs.close();
	}

	public int getMaxFieldSize() throws SQLException {
		return cs.getMaxFieldSize();
	}

	public void setMaxFieldSize(int max) throws SQLException {
		cs.setMaxFieldSize(max);
	}

	public int getMaxRows() throws SQLException {
		return cs.getMaxRows();
	}

	public void setMaxRows(int max) throws SQLException {
		cs.setMaxRows(max);
	}

	public void setEscapeProcessing(boolean enable) throws SQLException {
		cs.setEscapeProcessing(enable);
	}

	public int getQueryTimeout() throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method getQueryTimeout() not yet implemented.");
	}

	public void setQueryTimeout(int seconds) throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setQueryTimeout() not yet implemented.");
	}

	public void cancel() throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method cancel() not yet implemented.");
	}

	public SQLWarning getWarnings() throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method getWarnings() not yet implemented.");
	}

	public void clearWarnings() throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method clearWarnings() not yet implemented.");
	}

	public void setCursorName(String name) throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setCursorName() not yet implemented.");
	}

	public boolean execute(String sql) throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method execute() not yet implemented.");
	}

	public ResultSet getResultSet() throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method getResultSet() not yet implemented.");
	}

	public int getUpdateCount() throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method getUpdateCount() not yet implemented.");
	}

	public boolean getMoreResults() throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method getMoreResults() not yet implemented.");
	}

	public void setFetchDirection(int direction) throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setFetchDirection() not yet implemented.");
	}

	public int getFetchDirection() throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method getFetchDirection() not yet implemented.");
	}

	public void setFetchSize(int rows) throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method setFetchSize() not yet implemented.");
	}

	public int getFetchSize() throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method getFetchSize() not yet implemented.");
	}

	public int getResultSetConcurrency() throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method getResultSetConcurrency() not yet implemented.");
	}

	public int getResultSetType() throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method getResultSetType() not yet implemented.");
	}

	public void addBatch(String sql) throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method addBatch() not yet implemented.");
	}

	public void clearBatch() throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		throw new java.lang.UnsupportedOperationException(
				"Method clearBatch() not yet implemented.");
	}

	public int[] executeBatch() throws SQLException {
		return cs.executeBatch();
	}

	public Connection getConnection() throws SQLException {
		return cs.getConnection();
	}

	public boolean getMoreResults(int current) throws SQLException {
		return cs.getMoreResults(current);
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		return cs.getGeneratedKeys();
	}

	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		return cs.executeUpdate(sql, autoGeneratedKeys);
	}

	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		return cs.executeUpdate(sql, columnIndexes);
	}

	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		return cs.executeUpdate(sql, columnNames);
	}

	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		return cs.execute(sql, autoGeneratedKeys);
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		return cs.execute(sql, columnIndexes);
	}

	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		/**@todo Implement this java.sql.Statement method*/
		return cs.execute(sql, columnNames);
	}

	public int getResultSetHoldability() throws SQLException {
		return cs.getResultSetHoldability();
	}

	@Override
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNString(int parameterIndex, String value)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RowId getRowId(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowId getRowId(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRowId(String parameterName, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNString(String parameterName, String value)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNCharacterStream(String parameterName, Reader value,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNClob(String parameterName, NClob value) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClob(String parameterName, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBlob(String parameterName, InputStream inputStream,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNClob(String parameterName, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NClob getNClob(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSQLXML(String parameterName, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SQLXML getSQLXML(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getNCharacterStream(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getNCharacterStream(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBlob(String parameterName, Blob x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClob(String parameterName, Clob x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAsciiStream(String parameterName, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBinaryStream(String parameterName, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCharacterStream(String parameterName, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAsciiStream(String parameterName, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBinaryStream(String parameterName, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCharacterStream(String parameterName, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNCharacterStream(String parameterName, Reader value)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClob(String parameterName, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBlob(String parameterName, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNClob(String parameterName, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}
}