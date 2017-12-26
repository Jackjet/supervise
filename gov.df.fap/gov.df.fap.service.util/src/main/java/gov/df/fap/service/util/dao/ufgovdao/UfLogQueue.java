package gov.df.fap.service.util.dao.ufgovdao;

import java.sql.SQLException;

public interface  UfLogQueue {
	  /**
	   * 根据sql运行的前后时间确定运行时间
	   * @return
	   */
	  public void log(long startTime, long endTime,String methodName, String sql);
	  /**
	   * 执行出错的sql语句
	   * @param startTime
	   * @param endTime
	   * @param sql
	   */
	  public void err(SQLException sqle,String sql);
}
