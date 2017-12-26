package gov.df.fap.service.gl.core.interfaces;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Statement handler for call back in Class DaoSupport.
 * @author 
 * @version
 */
public interface StatementHandler {

	public Object statementHandle(Statement st) throws SQLException;
	
}
