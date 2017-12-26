package gov.df.fap.api.portal;

import java.sql.SQLException;
import java.util.List;

public  interface IBaseDao
{

  public  Object insert(String paramString, Object paramObject)
    throws SQLException;

  public  Object insert(String paramString)
    throws SQLException;

  public  int update(String paramString, Object paramObject)
    throws SQLException;

  public  int update(String paramString)
    throws SQLException;

  public  int delete(String paramString, Object paramObject)
    throws SQLException;

  public  int delete(String paramString)
    throws SQLException;

  public  Object queryForObject(String paramString, Object paramObject)
    throws SQLException;

  public  Object queryForObject(String paramString)
    throws SQLException;

  public  List queryForList(String paramString, Object paramObject)
    throws SQLException;

  public  List queryForList(String paramString)
    throws SQLException;

  public  List queryForList(String paramString, Object paramObject, int paramInt1, int paramInt2)
    throws SQLException;

  public  int queryForCount(String paramString, Object paramObject)
    throws SQLException;

}