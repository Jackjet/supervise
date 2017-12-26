package gov.df.fap.service.util.gl.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 抽象BeanMapper类
 * @author 
 * @version 2017-03-24
 */
public abstract class AbstractBeanMapper {
  /**
   * Factory method of the mapped bean.
   * @param rs db result set
   * @return
   * @throws SQLException 
   */
  public abstract Object resultSetToObject(ResultSet rs) throws SQLException;

}
