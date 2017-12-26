package gov.df.fap.service.gl.core.interfaces;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Normal PreparedStatement parameter setter.
 * <p>
 * The implemence just need to set parameter, like:
 * <p>ps.setString(i, new Object());
 * Do not make other PreparedStatement operations in implemence. 
 * @author 
 * @version 
 */
public interface PreparedStatementParamSetter {

	public void setter(PreparedStatement ps, int i) throws SQLException;
	
	public int size();
}
