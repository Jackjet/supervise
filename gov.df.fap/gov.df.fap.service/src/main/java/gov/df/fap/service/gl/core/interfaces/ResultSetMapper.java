package gov.df.fap.service.gl.core.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * ResultSet mapping to a List.
 * @author
 * @version 
 */
public abstract class ResultSetMapper {

	public List handlerResultSet(ResultSet rs){
		List resultList = new LinkedList();
		try {
			while(rs.next()){
				resultList.add(handleRow(rs));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return resultList;
	}
	
	public abstract Object handleRow(ResultSet rs) throws SQLException;
	
	public void init(ResultSet rs){}
}
