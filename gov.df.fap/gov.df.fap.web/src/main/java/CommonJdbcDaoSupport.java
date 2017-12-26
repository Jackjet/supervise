import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.sql.DataSource;

import oracle.sql.TIMESTAMP;

import org.apache.log4j.Logger;
import org.springframework.core.CollectionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.longtu.framework.daosupport.DaoMonitor;
import com.longtu.framework.exception.AppRuntimeException;

public class CommonJdbcDaoSupport extends JdbcDaoSupport {
  public static final int commit_size = 100;

  protected static Logger logger = Logger.getLogger(CommonJdbcDaoSupport.class);

  protected DaoMonitor monitor;

  public static CommonJdbcDaoSupport instanceDao() {
    DataSource ds = null;
    return instanceDao(ds);
  }

  public static CommonJdbcDaoSupport instanceDao(String datasourceId) {
    DataSource ds = SystemDataSourceManager.getDataSource(datasourceId);
    if (ds == null) {
      ds = SystemDataSourceManager.getDataSource();
    }

    return instanceDao(ds);
  }

  public static CommonJdbcDaoSupport instanceDao(DataSource dataSource) {
    if (dataSource == null) {
      dataSource = SystemDataSourceManager.getDataSource();
      if (dataSource == null) {
        throw new AppRuntimeException("没有找到数据源！");
      }
    }

    Object dao = new CommonJdbcDaoSupport(dataSource);
    return ((CommonJdbcDaoSupport) dao);
  }

  protected CommonJdbcDaoSupport() {
    this(SystemDataSourceManager.getDataSource());
  }

  protected CommonJdbcDaoSupport(DataSource datasource) {
    this.monitor = DaoMonitor.getInstance();

    setDataSource(datasource);
  }

  public List query(String sql, Object[] args, RowMapper rowMapper) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().query(sql, args, rowMapper);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, args);
    }
  }

  public List query(String sql, List argList, RowMapper rowMapper) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().query(sql, argList.toArray(), rowMapper);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, argList.toArray());
    }
  }

  public List query(String sql, RowMapper rowMapper) throws DataAccessException {
    long l = System.currentTimeMillis();
    try {
      getJdbcTemplate().setFetchSize(100);
      return getJdbcTemplate().query(sql, rowMapper);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, null);
    }
  }

  public List query(String sql, Object arg, RowMapper rowMapper) throws DataAccessException {
    long l = System.currentTimeMillis();
    try {
      List localList = getJdbcTemplate().query(sql, new Object[] { arg }, rowMapper);

      return localList;
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, new Object[] { arg });
    }
  }

  public int queryForInt(String sql) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForInt(sql);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, null);
    }
  }

  public int queryForInt(String sql, Object arg) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForInt(sql, new Object[] { arg });
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, new Object[] { arg });
    }
  }

  public int queryForInt(String sql, Object[] args) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForInt(sql, args);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, args);
    }
  }

  public int queryForInt(String sql, List argList) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForInt(sql, argList.toArray());
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, argList.toArray());
    }
  }

  public List queryForList(String sql) {
    long l = System.currentTimeMillis();
    try {
      getJdbcTemplate().setFetchSize(100);

      return getJdbcTemplate().query(sql, new RowMapper() {
        private String[] rsNames;

        private int colsize;

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
          System.currentTimeMillis();
          if (this.rsNames == null) {
            ResultSetMetaData rsMeta = rs.getMetaData();
            this.colsize = rsMeta.getColumnCount();
            this.rsNames = new String[this.colsize + 1];
            for (int i = 0; i < this.colsize; ++i) {
              this.rsNames[(i + 1)] = rsMeta.getColumnName(i + 1).toLowerCase();
            }
            this.colsize += 1;
          }
          Map hm = CollectionFactory.createLinkedCaseInsensitiveMapIfPossible(this.colsize - 1);
          for (int i = 1; i < this.colsize; ++i) {
            hm.put(this.rsNames[i], getResultSetValue(rs, i));
          }
          return hm;
        }

        public Object getResultSetValue(ResultSet rs, int index) throws SQLException {
          Object obj = rs.getObject(index);
          if (obj instanceof Blob) {
            obj = rs.getBytes(index);
          } else if (obj instanceof Clob) {
            obj = rs.getString(index);
          } else if ((obj != null) && (obj.getClass().getName().startsWith("oracle.sql.TIMESTAMP"))) {
            obj = rs.getTimestamp(index);
          } else if ((obj != null) && (obj.getClass().getName().startsWith("oracle.sql.DATE"))) {
            String metaDataClassName = rs.getMetaData().getColumnClassName(index);
            if (("java.sql.Timestamp".equals(metaDataClassName)) || ("oracle.sql.TIMESTAMP".equals(metaDataClassName)))
              obj = rs.getTimestamp(index);
            else
              obj = rs.getDate(index);
          } else if ((obj != null) && (obj instanceof Date)
            && ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index)))) {
            obj = rs.getTimestamp(index);
          }

          return obj;
        }
      });
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, null);
    }
  }

  public List queryForList(String sql, Class elementType) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForList(sql, elementType);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, null);
    }
  }

  public List queryForList(String sql, int maxRows) {
    long l = System.currentTimeMillis();
    try {
      getJdbcTemplate().setMaxRows(maxRows);
      return getJdbcTemplate().queryForList(sql);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, null);
    }
  }

  public List queryForList(String sql, List argList) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForList(sql, argList.toArray());
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, argList.toArray());
    }
  }

  public List queryForList(String sql, List argList, Class elementType) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForList(sql, argList.toArray(), elementType);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, argList.toArray());
    }
  }

  public List queryForList(String sql, Object arg) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForList(sql, new Object[] { arg });
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, new Object[] { arg });
    }
  }

  public List queryForList(String sql, Object arg, Class elementType) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForList(sql, new Object[] { arg }, elementType);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, new Object[] { arg });
    }
  }

  public List queryForList(String sql, Object[] args) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForList(sql, args);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, args);
    }
  }

  public List queryForList(String sql, Object[] args, Class elementType) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForList(sql, args, elementType);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, args);
    }
  }

  public long queryForLong(String sql) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForLong(sql);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, null);
    }
  }

  public long queryForLong(String sql, List argList) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForLong(sql, argList.toArray());
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, argList.toArray());
    }
  }

  public long queryForLong(String sql, Object arg) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForLong(sql, new Object[] { arg });
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, new Object[] { arg });
    }
  }

  public long queryForLong(String sql, Object[] args) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForLong(sql, args);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, args);
    }
  }

  public Map queryForMap(String sql) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForMap(sql);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, null);
    }
  }

  public Map queryForMap(String sql, List argList) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForMap(sql, argList.toArray());
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, argList.toArray());
    }
  }

  public Map queryForMap(String sql, Object arg) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForMap(sql, new Object[] { arg });
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, new Object[] { arg });
    }
  }

  public Map queryForMap(String sql, Object[] args) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForMap(sql, args);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, args);
    }
  }

  public Object queryForObject(String sql, Class requiredType) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForObject(sql, requiredType);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, null);
    }
  }

  public Object queryForObject(String sql, List argList, Class requiredType) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForObject(sql, argList.toArray(), requiredType);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, argList.toArray());
    }
  }

  public Object queryForObject(String sql, List argList, RowMapper rowMapper) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForObject(sql, argList.toArray(), rowMapper);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, argList.toArray());
    }
  }

  public Object queryForObject(String sql, Object arg, Class requiredType) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForObject(sql, new Object[] { arg }, requiredType);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, new Object[] { arg });
    }
  }

  public Object queryForObject(String paramString, Object paramObject, RowMapper paramRowMapper) {
    throw new Error("Unresolved compilation problem: \n\tUnreachable code\n");
  }

  public Object queryForObject(String sql, Object[] args, Class requiredType) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForObject(sql, args, requiredType);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, args);
    }
  }

  public Object queryForObject(String sql, Object[] args, RowMapper rowMapper) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForObject(sql, args, rowMapper);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, args);
    }
  }

  public Object queryForObject(String sql, RowMapper rowMapper) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().queryForObject(sql, rowMapper);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, null);
    }
  }

  public void execute(String sql) {
    long l = System.currentTimeMillis();
    try {
      getJdbcTemplate().execute(sql);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, null);
    }
  }

  public Object execute(String callString, CallableStatementCallback action) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().execute(callString, action);
    } finally {
      this.monitor.logger(this, callString, System.currentTimeMillis() - l, null);
    }
  }

  public Object execute(String sql, PreparedStatementCallback action) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().execute(sql, action);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, null);
    }
  }

  public int update(String sql) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().update(sql);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, null);
    }
  }

  public int update(String sql, List argList) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().update(sql, argList.toArray());
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, argList.toArray());
    }
  }

  public int update(String sql, Object arg) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().update(sql, new Object[] { arg });
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, new Object[] { arg });
    }
  }

  public int update(String sql, Object[] args) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().update(sql, args);
    } finally {
      this.monitor.logger(this, sql, System.currentTimeMillis() - l, args);
    }
  }

  public void batchUpdate(List sqls) {
    if (sqls.size() > 100)
      batchMultiUpdate(sqls);
    else
      batchSingleUpdate(sqls);
  }

  public int[] batchUpdate(String[] sql) {
    long l = System.currentTimeMillis();
    try {
      return getJdbcTemplate().batchUpdate(sql);
    } finally {
      this.monitor.logger(this, sql.toString(), System.currentTimeMillis() - l, null);
    }
  }

  private void batchMultiUpdate(List paramList) {
    throw new Error(
      "Unresolved compilation problem: \n\tType mismatch: cannot convert from element type Object to Future\n");
  }

  public void batchSingleUpdate(List sqls) {
    int loop = sqls.size();
    if (loop == 0) {
      return;
    }

    String[] sql = (String[]) null;
    sql = new String[(loop > 100) ? 100 : loop];

    for (int i = 0; i < loop; ++i) {
      if ((i % 100 == 0) && (i > 0)) {
        batchUpdate(sql);
        sql = new String[(loop - i > 100) ? 100 : loop - i];
      }
      sql[(i % 100)] = sqls.get(i).toString();
    }

    batchUpdate(sql);
  }

  public void batchSingnalUpdate(CommonJdbcDaoSupport paramCommonJdbcDaoSupport, String paramString, List paramList) {
    throw new Error(
      "Unresolved compilation problems: \n\tThe constructor Object(List) is undefined\n\tval$SQLBuffer cannot be resolved or is not a field\n\tval$SQLBuffer cannot be resolved or is not a field\n");
  }

  private void setValues2Type(PreparedStatement ps, Object o, int index) throws SQLException {
    if (o == null)
      ps.setNull(index + 1, 0);
    else if (o instanceof String)
      ps.setString(index + 1, (o == null) ? null : String.valueOf(o));
    else if (o instanceof Date)
      ps.setDate(index + 1, (Date) o);
    else if (o instanceof Integer)
      ps.setInt(index + 1, ((Integer) o).intValue());
    else if (o instanceof BigDecimal)
      ps.setBigDecimal(index + 1, (BigDecimal) o);
    else if (o instanceof Number)
      ps.setBigDecimal(index + 1, new BigDecimal(o.toString()));
    else if (o instanceof Clob)
      ps.setClob(index + 1, (Clob) o);
    else if (o instanceof Blob)
      ps.setBlob(index + 1, (Blob) o);
    else if (o instanceof Timestamp)
      ps.setTimestamp(index + 1, (Timestamp) o);
    else if (o instanceof TIMESTAMP)
      ps.setTimestamp(index + 1, (Timestamp) o);
    else
      throw new RuntimeException("参数" + index + "类型未知：" + o.getClass());
  }

  public void batchUpdate(String paramString, List paramList) {
    throw new Error(
      "Unresolved compilation problems: \n\tThe constructor CommonJdbcDaoSupport.MultiUpdateParamThread(Connection, String, List) is undefined\n\tThe constructor CommonJdbcDaoSupport.MultiUpdateParamThread(Connection, String, List) is undefined\n\tType mismatch: cannot convert from element type Object to Future\n");
  }

  class MultiUpdateParamThread implements Callable {
    String sql;

    Connection conn;

    List<List> paramlist;

    public Object call() throws Exception {
      throw new Error("Unresolved compilation problem: \n\tt cannot be resolved to a variable\n");
    }
  }

  class MultiUpdateThread implements Callable {
    String[] sqls = null;

    Connection conn = null;

    public MultiUpdateThread(Connection paramConnection, String[] paramArrayOfString) {
      this.sqls = this.sqls;
      this.conn = paramConnection;
    }

    public Object call() throws Exception {
      throw new Error("Unresolved compilation problem: \n\tt cannot be resolved to a variable\n");
    }
  }
}