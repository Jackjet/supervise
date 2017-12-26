package gov.df.fap.service.gl.core.sqlgen;

import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.core.sqlgen.SqlGenerator;
import gov.df.fap.bean.gl.dto.IConditionItem;
import gov.df.fap.util.Tools;

import org.springframework.stereotype.Service;

/**
 * 
 * @author justin
 *
 */
@Service
public class FromctrlidSqlGenerator implements SqlGenerator {

  private static final long serialVersionUID = 1L;

  public String generateStatement(IConditionItem item, BusVouAccount sumType) {
    if (sumType.getBalancePeriodType() == 1) {
      StringBuffer strSQL = new StringBuffer();
      strSQL.append(" and fromctrlid  in (select sum_id from ").append(sumType.getTableName())
        .append("" + Tools.addDbLink() + " where exists (select 1 from ").append(sumType.getTableName())
        .append("" + Tools.addDbLink() + " where ").append("b.ccid=ccid and sum_id = '").append(item.getValue())
        .append("'))");
      return strSQL.toString();
    } else {
      return new LogicSqlGenerator("=").generateStatement(item, sumType);
    }
  }

}
