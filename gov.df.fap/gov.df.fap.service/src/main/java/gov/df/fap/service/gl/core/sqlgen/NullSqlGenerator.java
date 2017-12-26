package gov.df.fap.service.gl.core.sqlgen;


import org.springframework.stereotype.Component;

import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.core.sqlgen.SqlGenerator;
import gov.df.fap.bean.gl.dto.Condition;
import gov.df.fap.bean.gl.dto.IConditionItem;
import gov.df.fap.service.util.datasource.TypeOfDB;

/**
 * 为空的情况
 * @author 
 * @versoin 
 *
 */
@Component
public class NullSqlGenerator implements SqlGenerator {

  private static final long serialVersionUID = 1L;

  public String generateStatement(IConditionItem item, BusVouAccount sumType) {
		String opera = item.getOperation();
		if(opera.equalsIgnoreCase(Condition.NULL)) {
			return " "+item.getConnectOper()+" "+item.getField()+" ";
		} else {
			if(TypeOfDB.isOracle()) {
				return " "+item.getConnectOper()+" "+item.getField()+" is null ";
			} else if(TypeOfDB.isMySQL()){
				return " "+item.getConnectOper()+" isnull("+item.getField()+")"; 
			} else {
				return null;
			}
		}
	}

}
