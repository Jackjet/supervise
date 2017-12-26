package gov.df.fap.api.gl.core.daosupport;

import java.sql.PreparedStatement;

/**
 *
 * PreparedStatement parameter setter for batch update.
 * @author 
 * @version 
 */
public interface BatchPreparedStatementParamSetter {

	public void setter(PreparedStatement ps, int paramIndex, int batchIndex);
	
	public int paramCount();
	
	public int batchCount();
	
}
