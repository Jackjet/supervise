package gov.df.fap.service.gl.core.interfaces;

import java.sql.Connection;

/**
 * 引擎的数据底层连接提供器，用户从平台取连接，而不管是怎样的数据源。
 * @author 
 *
 */
public interface ConnectionProvider {

	/**
	 * 取连接，实现上可以是从SssionFactory中取连接或者从DataSource里面取。
	 * @return 连接
	 */
	public Connection getConnection();
	
}
