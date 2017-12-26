package gov.df.fap.service.gl.core.sqlgen;

import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.core.sqlgen.SqlGenerator;
import gov.df.fap.bean.gl.dto.IConditionItem;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.date.DateHandler;

import org.springframework.stereotype.Service;

/**
 * 月份转化器
 * @author 
 *
 */
@Service
public class SetMonthSqlGenerator implements SqlGenerator {

  private static final long serialVersionUID = 1L;

  public String generateStatement(IConditionItem conditionItem, BusVouAccount sumType) {
    String value = conditionItem.getValue();
    if (sumType.getBalancePeriodType() == BusVouAccount.BALANCE_PERIOD_TYPE_SUM_MONTH) {//累积月控制
      if (!value.equals("0"))
        return " and set_month <=" + value;
      else
        //截至当前月份
        return " and set_month <=" + DateHandler.getCurrentMonth();
    } else if (sumType.getBalancePeriodType() == BusVouAccount.BALANCE_PERIOD_TYPE_MONTH) //当前月控制
      return " and set_month = " + (value == null ? StringUtil.ZERO : value);
    else
      //全年控制
      return " and set_month = 0";
  }

}
