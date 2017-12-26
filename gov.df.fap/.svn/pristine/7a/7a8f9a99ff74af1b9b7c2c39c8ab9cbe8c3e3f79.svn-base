package gov.df.fap.bean.gl.core.sqlgen;


import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.dto.IConditionItem;

/**
 * 该类不能用作singleton,注意到它的内部有一个变量sqlStatement,该变更保存了一个sql语句.
 * @author 
 *
 */
public class StatementSqlGenerator implements SqlGenerator{

  private static final long serialVersionUID = 1L;
  
  /**sql查询条件*/
	private String sqlStatement = null;
	
	public StatementSqlGenerator(String sql){
		this.sqlStatement = sql;
	}
	
	public String generateStatement(IConditionItem conditionItem, BusVouAccount sumType) {
		StringBuffer sql = new StringBuffer();
		sql.append(" "+conditionItem.getConnectOper()+" "+sqlStatement);
		return sql.toString();
	}

}
