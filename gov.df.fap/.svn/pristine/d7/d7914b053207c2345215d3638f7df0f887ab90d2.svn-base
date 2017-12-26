package gov.df.fap.service.util.dao.portaldao;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

public interface _BaseDao {

  public DataSource getMyDataSource();

  public Object insert(String id, Object parameterObject) throws SQLException;

  public Object insert(String id) throws SQLException;

  public int update(String id, Object parameterObject) throws SQLException;

  public int update(String id) throws SQLException;

  public int delete(String id, Object parameterObject) throws SQLException;

  public int delete(String id) throws SQLException;

  public Object queryForObject(String id, Object parameterObject)
    throws SQLException;

  public Object queryForObject(String id) throws SQLException;

  public Object queryForObject(String id, Object parameterObject,
    Object resultObject) throws SQLException;

  public List queryForList(String id, Object parameterObject) throws SQLException;

  public List queryForList(String id) throws SQLException;

  //public void queryWithRowHandler(String id, Object parameterObject, RowHandler rowHandler) throws SQLException;

  //public void queryWithRowHandler(String id, RowHandler rowHandler) throws SQLException;

  public void startBatch() throws SQLException;

  public int executeBatch() throws SQLException;

  //public List executeBatchDetailed() throws SQLException, BatchException;

  public List queryForList(final String id, final Object parameterObject,
    final int skipResults, final int maxResults) throws SQLException;

  public int queryForCount(final String id, final Object parameterObject)
    throws SQLException;

  /**
   * 获取sqlmap配置中的sql文本
   * @param id
   * @return
   */
  public String getSql(String id, Object parameterObject);

  /**
   * 获取sqlmap配置中的sql文本，并形成新参数
   * @param id
   * @param parameterObject
   * @param newParams
   * @return
   */
  public String getSql(String id, Object parameterObject, List newParams);

  public Class getResultClass(String id);

  public Class getParameterClass(String id);

  public String getParameterField(String id, String property);

  public List queryBySql(String sql, Object[] params) throws SQLException;

  public int updateBySql(String sql, Object[] params) throws SQLException;

}
