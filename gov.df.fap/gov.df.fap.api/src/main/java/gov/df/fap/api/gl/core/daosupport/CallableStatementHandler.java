package gov.df.fap.api.gl.core.daosupport;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * Handle CallableStatement
 * @author 
 * @version
 */
public interface CallableStatementHandler {

	/**
	 * Handle CallStatement
	 * @param callSt CallableStatment fetched from Connection
	 * @return the result of 
	 * @throws SQLException 
	 */
	public Object handleCallStatement(CallableStatement callSt) throws SQLException;
	
}
