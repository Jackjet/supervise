package gov.df.fap.service.util.dao.ufgovdao;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;


public class UfPreparedStatement
    implements PreparedStatement 
{
  private PreparedStatement ps;
  private UfLogQueue queue;
  private String sql = null;
  private StringBuffer parameter = new StringBuffer();
  private ResultSet rs = null;
  public UfPreparedStatement(UfLogQueue queue, PreparedStatement ps,
                             String sql)
  {
    this.ps = ps;
    this.queue = queue;
    this.sql = sql;
  }

  public ResultSet executeQuery()
      throws SQLException
  {
    
    try
    {
      long startQuery = System.currentTimeMillis();
      rs = ps.executeQuery();
      long endQuery = System.currentTimeMillis();
      this.queue.log(startQuery, endQuery,"PreparedStatement.executeQuery", sql);
    }
    catch (SQLException ex)
    {
      this.queue.err(ex,sql);
      throw ex;
    }
    return rs;
  }

  public int executeUpdate()
      throws SQLException
  {
    try
    {
      long startUpdate = System.currentTimeMillis();
      int iRet = ps.executeUpdate();
      long endUpdate = System.currentTimeMillis();
      this.queue.log(startUpdate, endUpdate,"PreparedStatement.executeUpdate", sql);
      return iRet;
    }
    catch (SQLException ex)
    {
      this.queue.err(ex,sql);
      throw ex;
    }
  }

  public void setNull(int parameterIndex, int sqlType)
      throws SQLException
  {
    ps.setNull(parameterIndex, sqlType);
  }

  public void setBoolean(int parameterIndex, boolean x)
      throws SQLException
  {
    ps.setBoolean(parameterIndex, x);
    parameter.append("\n第"+parameterIndex+"参数为"+x);
  }

  public void setByte(int parameterIndex, byte x)
      throws SQLException
  {
    ps.setByte(parameterIndex, x);
    parameter.append("\n第"+parameterIndex+"参数为"+x);
  }

  public void setShort(int parameterIndex, short x)
      throws SQLException
  {
    ps.setShort(parameterIndex, x);
    parameter.append("\n第"+parameterIndex+"参数为"+x);
  }

  public void setInt(int parameterIndex, int x)
      throws SQLException
  {
    ps.setInt(parameterIndex, x);
    parameter.append("\n第"+parameterIndex+"参数为"+x);
  }

  public void setLong(int parameterIndex, long x)
      throws SQLException
  {
    ps.setLong(parameterIndex, x);
    parameter.append("\n第"+parameterIndex+"参数为"+x);
  }

  public void setFloat(int parameterIndex, float x)
      throws SQLException
  {
    ps.setFloat(parameterIndex, x);
    parameter.append("\n第"+parameterIndex+"参数为"+x);
  }

  public void setDouble(int parameterIndex, double x)
      throws SQLException
  {
    ps.setDouble(parameterIndex, x);
    parameter.append("\n第"+parameterIndex+"参数为"+x);
  }

  public void setBigDecimal(int parameterIndex, BigDecimal x)
      throws
      SQLException
  {
    ps.setBigDecimal(parameterIndex, x);
    parameter.append("\n第"+parameterIndex+"参数为"+x);
  }

  public void setString(int parameterIndex, String x)
      throws SQLException
  {
    ps.setString(parameterIndex, x);
    parameter.append("\n第"+parameterIndex+"参数为"+x);
  }

  public void setBytes(int parameterIndex, byte[] x)
      throws SQLException
  {
    ps.setBytes(parameterIndex, x);
  }

  public void setDate(int parameterIndex, Date x)
      throws SQLException
  {
    ps.setDate(parameterIndex, x);
    parameter.append("\n第"+parameterIndex+"参数为"+x);
  }

  public void setTime(int parameterIndex, Time x)
      throws SQLException
  {
    ps.setTime(parameterIndex, x);
    parameter.append("\n第"+parameterIndex+"参数为"+x);
  }

  public void setTimestamp(int parameterIndex, Timestamp x)
      throws
      SQLException
  {
    ps.setTimestamp(parameterIndex, x);
    parameter.append("\n第"+parameterIndex+"参数为"+x);
  }

  public void setAsciiStream(int parameterIndex, InputStream x, int length)
      throws
      SQLException
  {
    ps.setAsciiStream(parameterIndex, x, length);
  }

  public void setUnicodeStream(int parameterIndex, InputStream x, int length)
      throws
      SQLException
  {
    ps.setUnicodeStream(parameterIndex, x, length);
  }

  public void setBinaryStream(int parameterIndex, InputStream x, int length)
      throws
      SQLException
  {
    ps.setBinaryStream(parameterIndex, x, length);
  }

  public void clearParameters()
      throws SQLException
  {
    ps.clearParameters();
  }

  public void setObject(int parameterIndex, Object x, int targetSqlType,
                        int scale)
      throws SQLException
  {
    ps.setObject(parameterIndex, x, targetSqlType, scale);
  }

  public void setObject(int parameterIndex, Object x, int targetSqlType)
      throws
      SQLException
  {
    ps.setObject(parameterIndex, x, targetSqlType);
  }

  public void setObject(int parameterIndex, Object x)
      throws SQLException
  {
    ps.setObject(parameterIndex, x);
    this.sql+="\n第"+parameterIndex+"参数为"+x;
  }

  public boolean execute()
      throws SQLException
  {
    try
    {
      long startUpdate = System.currentTimeMillis();
      boolean bRet = ps.execute();
      long endUpdate = System.currentTimeMillis();
      this.queue.log(startUpdate, endUpdate,"PreparedStatement.execute", sql);
      return bRet;
    }
    catch (SQLException ex)
    {
      this.queue.err(ex,sql);
      throw ex;
    }
  }

  public void addBatch()
      throws SQLException
  {
    ps.addBatch();
    parameter.append("增加一次批处理\n");
  }

  public void setCharacterStream(int parameterIndex, Reader reader,
                                 int length)
      throws SQLException
  {
    ps.setCharacterStream(parameterIndex, reader, length);
  }

  public void setRef(int i, Ref x)
      throws SQLException
  {
    ps.setRef(i, x);
  }

  public void setBlob(int i, Blob x)
      throws SQLException
  {
    ps.setBlob(i, x);
  }

  public void setClob(int i, Clob x)
      throws SQLException
  {
    ps.setClob(i, x);
  }

  public void setArray(int i, Array x)
      throws SQLException
  {
    ps.setArray(i, x);
  }

  public ResultSetMetaData getMetaData()
      throws SQLException
  {
    return ps.getMetaData();
  }

  public void setDate(int parameterIndex, Date x, Calendar cal)
      throws
      SQLException
  {
    ps.setDate(parameterIndex, x, cal);
  }

  public void setTime(int parameterIndex, Time x, Calendar cal)
      throws
      SQLException
  {
    ps.setTime(parameterIndex, x, cal);
  }

  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
      throws
      SQLException
  {
    ps.setTimestamp(parameterIndex, x, cal);
  }

  public void setNull(int paramIndex, int sqlType, String typeName)
      throws
      SQLException
  {
    ps.setNull(paramIndex, sqlType, typeName);
  }

  public void setURL(int parameterIndex, URL x)
      throws SQLException
  {
    ps.setURL(parameterIndex, x);
  }

  public ParameterMetaData getParameterMetaData()
      throws SQLException
  {
    return ps.getParameterMetaData();
  }

  public ResultSet executeQuery(String sql)
      throws SQLException
  {
    long startQuery = System.currentTimeMillis();
    ResultSet rs = ps.executeQuery(sql);
    long endQuery = System.currentTimeMillis();
    this.queue.log(startQuery, endQuery, "PreparedStatement.executeQuery", sql);
    return rs;
  }

  public int executeUpdate(String sql)
      throws SQLException
  {
    return ps.executeUpdate(sql);
  }

  public void close()
      throws SQLException
  {
      ps.close();
  }

  public int getMaxFieldSize()
      throws SQLException
  {
    return ps.getMaxFieldSize();
  }

  public void setMaxFieldSize(int max)
      throws SQLException
  {
    ps.setMaxFieldSize(max);
  }

  public int getMaxRows()
      throws SQLException
  {
    return ps.getMaxRows();
  }

  public void setMaxRows(int max)
      throws SQLException
  {
    ps.setMaxRows(max);
  }

  public void setEscapeProcessing(boolean enable)
      throws SQLException
  {
    ps.setEscapeProcessing(enable);
  }

  public int getQueryTimeout()
      throws SQLException
  {
    return ps.getQueryTimeout();
  }

  public void setQueryTimeout(int seconds)
      throws SQLException
  {
    ps.setQueryTimeout(seconds);
  }

  public void cancel()
      throws SQLException
  {
    ps.cancel();
  }

  public SQLWarning getWarnings()
      throws SQLException
  {
    return ps.getWarnings();
  }

  public void clearWarnings()
      throws SQLException
  {
    ps.clearWarnings();
  }

  public void setCursorName(String name)
      throws SQLException
  {
    ps.setCursorName(name);
  }

  public boolean execute(String sql)
      throws SQLException
  {
    return ps.execute(sql);
  }

  public ResultSet getResultSet()
      throws SQLException
  {
    return ps.getResultSet();
  }

  public int getUpdateCount()
      throws SQLException
  {
    return ps.getUpdateCount();
  }

  public boolean getMoreResults()
      throws SQLException
  {
    return ps.getMoreResults();
  }

  public void setFetchDirection(int direction)
      throws SQLException
  {
    ps.setFetchDirection(direction);
  }

  public int getFetchDirection()
      throws SQLException
  {
    return ps.getFetchDirection();
  }

  public void setFetchSize(int rows)
      throws SQLException
  {
    ps.setFetchSize(rows);
  }

  public int getFetchSize()
      throws SQLException
  {
    return ps.getFetchSize();
  }

  public int getResultSetConcurrency()
      throws SQLException
  {
    return ps.getResultSetConcurrency();
  }

  public int getResultSetType()
      throws SQLException
  {
    return ps.getResultSetType();
  }

  public void addBatch(String sql)
      throws SQLException
  {
    ps.addBatch(sql);
  }

  public void clearBatch()
      throws SQLException
  {
    ps.clearBatch();
    this.parameter.setLength(0);
  }

  public int[] executeBatch()
      throws SQLException
  {
 	 long startUpdate = System.currentTimeMillis();
     try
     {           
		StringBuffer sb = new StringBuffer("批量执行\n").append(sql);
		sb.append(this.parameter);
		int []iRet= ps.executeBatch();
		long endUpdate = System.currentTimeMillis();
		this.queue.log(startUpdate, endUpdate,"PreStatement.executeBatch()",sb.toString());
		return iRet;        
	}catch(SQLException sqle){
		StringBuffer sb = new StringBuffer("批量执行\n").append(sql);
		sb.append(this.parameter);
		this.queue.err(sqle,sb.toString());
		throw sqle;
	}
  }

  public Connection getConnection()
      throws SQLException
  {
    return ps.getConnection();
  }

  public boolean getMoreResults(int current)
      throws SQLException
  {
    return ps.getMoreResults(current);
  }

  public ResultSet getGeneratedKeys()
      throws SQLException
  {
    return ps.getGeneratedKeys();
  }

  public int executeUpdate(String sql, int autoGeneratedKeys)
      throws
      SQLException
  {
    return ps.executeUpdate(sql, autoGeneratedKeys);
  }

  public int executeUpdate(String sql, int[] columnIndexes)
      throws
      SQLException
  {
    return ps.executeUpdate(sql, columnIndexes);
  }

  public int executeUpdate(String sql, String[] columnNames)
      throws
      SQLException
  {
    return ps.executeUpdate(sql, columnNames);
  }

  public boolean execute(String sql, int autoGeneratedKeys)
      throws
      SQLException
  {
    return ps.execute(sql, autoGeneratedKeys);
  }

  public boolean execute(String sql, int[] columnIndexes)
      throws SQLException
  {
    return ps.execute(sql, columnIndexes);
  }

  public boolean execute(String sql, String[] columnNames)
      throws
      SQLException
  {
    return ps.execute(sql, columnNames);
  }

  public int getResultSetHoldability()
      throws SQLException
  {
    return ps.getResultSetHoldability();
  }
  public String getCloseSql(){
	  return sql;
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

@Override
public boolean isWrapperFor(Class<?> iface) throws SQLException {
	// TODO Auto-generated method stub
	return false;
}

@Override
public void setRowId(int parameterIndex, RowId x) throws SQLException {
	// TODO Auto-generated method stub
	
}

@Override
public void setNString(int parameterIndex, String value) throws SQLException {
	// TODO Auto-generated method stub
	
}

@Override
public void setNCharacterStream(int parameterIndex, Reader value, long length)
		throws SQLException {
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
public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
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
public void setCharacterStream(int parameterIndex, Reader reader, long length)
		throws SQLException {
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
}