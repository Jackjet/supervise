package gov.df.fap.service.util.dao.ufgovdao; 

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class UfStatement implements Statement
{
    private Statement stmt = null;
    private UfLogQueue queue=null;
    private ResultSet rs = null;
    private List m_batch = new ArrayList();
    public UfStatement(UfLogQueue queue,Statement stmt)
    {
        this.stmt = stmt;
        this.queue = queue;
    }
    public ResultSet executeQuery(String sql) throws SQLException
    {
      long startQuery = System.currentTimeMillis();
      try
      {
        rs = stmt.executeQuery(sql);
      }
      catch (SQLException ex)
      {
        this.queue.err(ex,ex.getMessage()+sql);
        throw ex;
      }
      long endQuery = System.currentTimeMillis();
      this.queue.log(startQuery,endQuery,"Statement.executeQuery(sql)",sql);
      return rs;
    }
    public int executeUpdate(String sql) throws SQLException
    {
      long startUpdate = System.currentTimeMillis();
      try
      {
        int iRet = stmt.executeUpdate(sql);
        long endUpdate = System.currentTimeMillis();
        this.queue.log(startUpdate, endUpdate,"Statement.executeUpdate(sql)", sql);
        return iRet;
      }
      catch (SQLException ex)
      {
        this.queue.err(ex,ex.getMessage()+sql);
        throw ex;
      }

    }
    public void close() throws SQLException
    {
        stmt.close();
    }
    public int getMaxFieldSize() throws SQLException
    {
        return stmt.getMaxFieldSize();
    }
    public void setMaxFieldSize(int max) throws SQLException
    {
        stmt.setMaxFieldSize(max);
    }
    public int getMaxRows() throws SQLException
    {
        return stmt.getMaxRows();
    }
    public void setMaxRows(int max) throws SQLException
    {
        stmt.setMaxRows(max);
    }
    public void setEscapeProcessing(boolean enable) throws SQLException
    {
        stmt.setEscapeProcessing(enable);
    }
    public int getQueryTimeout() throws SQLException
    {
        return stmt.getQueryTimeout();
    }
    public void setQueryTimeout(int seconds) throws SQLException
    {
        stmt.setQueryTimeout(seconds);
    }
    public void cancel() throws SQLException
    {
        stmt.cancel();
    }
    public SQLWarning getWarnings() throws SQLException
    {
        return stmt.getWarnings();
    }
    public void clearWarnings() throws SQLException
    {
        stmt.clearWarnings();
    }
    public void setCursorName(String name) throws SQLException
    {
        stmt.setCursorName(name);
    }
    public boolean execute(String sql) throws SQLException
    {
        return stmt.execute(sql);
    }
    public ResultSet getResultSet() throws SQLException
    {
        return stmt.getResultSet();
    }
    public int getUpdateCount() throws SQLException
    {
        return stmt.getUpdateCount();
    }
    public boolean getMoreResults() throws SQLException
    {
        return stmt.getMoreResults();
    }
    public void setFetchDirection(int direction) throws SQLException
    {
        stmt.setFetchDirection(direction);
    }
    public int getFetchDirection() throws SQLException
    {
        return stmt.getFetchDirection();
    }
    public void setFetchSize(int rows) throws SQLException
    {
        stmt.setFetchSize(rows);
    }
    public int getFetchSize() throws SQLException
    {
        return stmt.getFetchSize();
    }
    public int getResultSetConcurrency() throws SQLException
    {
        return stmt.getResultSetConcurrency();
    }
    public int getResultSetType() throws SQLException
    {
        return stmt.getResultSetType();
    }
    public void addBatch(String sql) throws SQLException
    
    {
    	m_batch.add(sql);
        stmt.addBatch(sql);
    }
    public void clearBatch() throws SQLException
    {
        stmt.clearBatch();
        m_batch.clear();
    }
    public int[] executeBatch() throws SQLException
    {
    	 long startUpdate = System.currentTimeMillis();
         try
         {           
    		StringBuffer sb = new StringBuffer("批量执行\n");
    		for(int i=0;i<m_batch.size();i++)
    		{
    			sb.append(i).append(":").append(m_batch.get(i)).append("\n");
    			
    		}    		    	
    		int []iRet= stmt.executeBatch();
    		long endUpdate = System.currentTimeMillis();
    		this.queue.log(startUpdate, endUpdate,"Statement.executeBatch()",sb.toString());
    		return iRet;        
    	}catch(SQLException sqle){
    		StringBuffer sb = new StringBuffer("批量执行\n");
    		for(int i=0;i<m_batch.size();i++)
    		{
    			sb.append(i).append(":").append(m_batch.get(i)).append("\n");
    			
    		}
    		this.queue.err(sqle,sb.toString());
    		throw sqle;
    	}
    }
    public Connection getConnection() throws SQLException
    {
        return stmt.getConnection();
    }
    public boolean getMoreResults(int current) throws SQLException
    {
        return stmt.getMoreResults(current);
    }
    public ResultSet getGeneratedKeys() throws SQLException
    {
        return stmt.getGeneratedKeys();
    }
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException
    {
        return stmt.executeUpdate(sql,autoGeneratedKeys);
    }
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException
    {
        return stmt.executeUpdate(sql,columnIndexes);
    }
    public int executeUpdate(String sql, String[] columnNames) throws SQLException
    {
        return stmt.executeUpdate(sql,columnNames);
    }
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException
    {
        return stmt.execute(sql,autoGeneratedKeys);
    }
    public boolean execute(String sql, int[] columnIndexes) throws SQLException
    {
        return stmt.execute(sql,columnIndexes);
    }
    public boolean execute(String sql, String[] columnNames) throws SQLException
    {
        return stmt.execute(sql,columnNames);
    }
    public int getResultSetHoldability() throws SQLException
    {
        return stmt.getResultSetHoldability();
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
}