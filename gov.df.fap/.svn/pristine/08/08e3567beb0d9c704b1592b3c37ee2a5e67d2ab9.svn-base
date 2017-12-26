package gov.df.fap.service.gl.core.sqlgen;

import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.core.sqlgen.SqlGenerator;
import gov.df.fap.bean.gl.dto.IConditionItem;

/**
 * 普通的查询条件.
 * @author 
 * @version 2007-11-09
 *
 */
public class LogicSqlGenerator implements SqlGenerator {

  private static final long serialVersionUID = 1L;

  private String logicOper = "=";//默认等于

  public LogicSqlGenerator(String logicOp) {
    this.logicOper = logicOp;
  }

  public String generateStatement(IConditionItem item, BusVouAccount sumType) {
    StringBuffer strSQL = new StringBuffer();
    strSQL.append(" " + item.getConnectOper() + " ").append(item.getField())
      .append(" " + logicOper + " '" + item.getValue() + "'");
    return strSQL.toString();
  }

}
