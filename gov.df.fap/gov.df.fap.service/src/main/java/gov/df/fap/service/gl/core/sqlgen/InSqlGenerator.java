package gov.df.fap.service.gl.core.sqlgen;

import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.core.sqlgen.SqlGenerator;
import gov.df.fap.bean.gl.dto.IConditionItem;

import java.util.StringTokenizer;

public class InSqlGenerator implements SqlGenerator {

  private static final long serialVersionUID = 1L;
  
  public boolean isNot = false;

  public InSqlGenerator(boolean isNotOper) {
    this.isNot = isNotOper;
  }

  public String generateStatement(IConditionItem item, BusVouAccount sumType) {
    StringBuffer strSQL = new StringBuffer();
    strSQL.append(" " + item.getConnectOper() + " (").append(item.getField())
      .append(" " + (isNot ? "not" : "") + " in ");

    strSQL.append("(");
    StringTokenizer sz = new StringTokenizer(item.getValue(), ",");
    int count1000 = 0;
    while (sz.hasMoreTokens()) {
      if (++count1000 >= 1000) {
        strSQL.deleteCharAt(strSQL.length() - 1);
        if (isNot)
          strSQL.append(") and ");
        else
          strSQL.append(") or ");
        strSQL.append(item.getField()).append(" " + (isNot ? "not" : "") + " in (");
        count1000 = 0;
      }
      strSQL.append("'").append(sz.nextToken()).append("',");
    }
    strSQL.deleteCharAt(strSQL.length() - 1);
    strSQL.append("))");

    return strSQL.toString();
  }
}
