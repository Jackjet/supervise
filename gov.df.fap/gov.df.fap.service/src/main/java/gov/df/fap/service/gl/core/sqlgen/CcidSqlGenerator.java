package gov.df.fap.service.gl.core.sqlgen;

import gov.df.fap.api.gl.coa.ibs.ICoa;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.core.sqlgen.SqlGenerator;
import gov.df.fap.bean.gl.dto.ConditionObj;
import gov.df.fap.bean.gl.dto.IConditionItem;

import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author 
 * @verison 2017-04-30
 *
 */
@Service
public class CcidSqlGenerator implements SqlGenerator {

  private static final long serialVersionUID = 1L;

  @Autowired
  private ICoa coa = null;

  public void setCoa(ICoa coa) {
    this.coa = coa;
  }

  public String generateStatement(IConditionItem conditionItem, BusVouAccount sumType) {
    String opera = conditionItem.getOperation();
    StringBuffer strSQL = new StringBuffer();
    if (!opera.equalsIgnoreCase(ConditionObj.NOTIN) && !opera.equalsIgnoreCase(ConditionObj.IN))
      throw new RuntimeException("逻辑判断符" + opera + "非法,ccid逻辑判断符支持或者not in!");
    strSQL.append(" and b.ccid " + opera + " (");
    StringTokenizer sz = new StringTokenizer(conditionItem.getValue(), ",");
    while (sz.hasMoreTokens()) {
      String ccid = null;
      try {
        ccid = coa.preCCIDTrans(sumType.getCoaId(), sz.nextToken(), false);
      } catch (Exception e) {
        throw new RuntimeException("CCID转换时异常, CCID条件生成失败!", e);
      }
      strSQL.append("'").append(ccid).append("',");
    }
    strSQL.deleteCharAt(strSQL.length() - 1);
    strSQL.append(")");

    return strSQL.toString();
  }

}
