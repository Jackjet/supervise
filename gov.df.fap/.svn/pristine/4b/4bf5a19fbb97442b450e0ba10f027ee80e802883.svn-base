package gov.df.fap.service.gl.core.sqlgen;


import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.core.sqlgen.SqlGenerator;
import gov.df.fap.bean.gl.dto.IConditionItem;

/**
 * 
 * @author Administrator
 *
 */
public class LikeSqlGenerator implements SqlGenerator {

  private static final long serialVersionUID = 1L;
  
  public boolean isNot = false;
	
	public LikeSqlGenerator(boolean isNotOper){
		this.isNot = isNotOper;
	}
	
	public String generateStatement(IConditionItem item, BusVouAccount sumType) {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append(" "+item.getConnectOper()+" ").append(item.getField()).append(" "+(isNot?"not":"")+" like '%"+item.getValue()+"%'");
		return strSQL.toString();
	}

}
