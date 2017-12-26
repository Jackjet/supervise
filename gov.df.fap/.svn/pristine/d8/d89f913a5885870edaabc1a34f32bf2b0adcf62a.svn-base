package gov.df.fap.service.gl.core.sqlgen;

import gov.df.fap.api.dictionary.ElementOperation;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.coa.FCoaDetail;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.core.sqlgen.SqlGenerator;
import gov.df.fap.bean.gl.dto.Condition;
import gov.df.fap.bean.gl.dto.IConditionItem;
import gov.df.fap.bean.gl.dto.Order;
import gov.df.fap.service.gl.coa.impl.CoaService;
import gov.df.fap.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * plus sql,SQL生成器 
 *
 */
@Service
public class PlusSql {

  protected static Map sqlGeneratorCache = new HashMap();

  private static Map customerSqlGenCache;

  @Autowired
  @Qualifier("ccidSqlGenerator")
  private SqlGenerator ccidsqlgen;

  @Autowired
  @Qualifier("elementSqlGenerator")
  private SqlGenerator elementSqlGenerator;

  @Autowired
  @Qualifier("fromctrlidSqlGenerator")
  private SqlGenerator fromctrlidSqlGenerator;

  @Autowired
  @Qualifier("setMonthSqlGenerator")
  private SqlGenerator setMonthSqlGenerator;

  @Autowired
  @Qualifier("elementOperationWrapper")
  private ElementOperation eleOp = null;

  private static final String ELEMENT_SQL_GENERATOR_KEY = "sys.element";

  static {
    sqlGeneratorCache.put(Condition.EQUAL, new LogicSqlGenerator("="));
    sqlGeneratorCache.put(Condition.LT, new LogicSqlGenerator("<"));
    sqlGeneratorCache.put(Condition.GT, new LogicSqlGenerator(">"));
    sqlGeneratorCache.put(Condition.LTEQUAL, new LogicSqlGenerator("<="));
    sqlGeneratorCache.put(Condition.GTEQUAL, new LogicSqlGenerator(">="));
    sqlGeneratorCache.put(Condition.NOTEQUAL, new LogicSqlGenerator("<>"));

    sqlGeneratorCache.put(Condition.LIKE, new LikeSqlGenerator(false));
    sqlGeneratorCache.put(Condition.NOTLIKE, new LikeSqlGenerator(true));
    sqlGeneratorCache.put(Condition.IN, new InSqlGenerator(false));
    sqlGeneratorCache.put(Condition.NOTIN, new InSqlGenerator(true));
    sqlGeneratorCache.put(Condition.NULL, new NullSqlGenerator());
  }

  public void setCustomerSqlGenCache(Map cache) {
    customerSqlGenCache = cache;
  }

  public Map getCustomerSqlGenCache() {
    if (customerSqlGenCache == null) {
      customerSqlGenCache = new HashMap<String, SqlGenerator>();
      customerSqlGenCache.put("sys.element", elementSqlGenerator);
      customerSqlGenCache.put("fromctrlid", fromctrlidSqlGenerator);
      customerSqlGenCache.put("ccid", ccidsqlgen);
      customerSqlGenCache.put("set_month", setMonthSqlGenerator);

    }
    return customerSqlGenCache;
  }

  public void setEleOp(ElementOperation eleOp) {
    this.eleOp = eleOp;
  }

  /**
   * 
   * @param billType
   * @param sumType
   * @param obj
   * @return
   * @throws Exception
   */
  public String getConditionSql(BusVouAccount sumType, Condition condition) {

    return getConditionSql(sumType, condition, new GeneratorDelegate() {
      public String generatorItemSql(IConditionItem conditionItem, BusVouAccount sumType) {
        if (StringUtil.isEmpty(conditionItem.getValue())) {
          //特殊处理一此为空的情况,历史原因,这里留下这个判断
          return getDefaultSqlGenrator(Condition.NULL).generateStatement(conditionItem, sumType);
        } else
          return generatorConditionSql(conditionItem, sumType);
      }

      public boolean isItemAppend(IConditionItem conditionItem) {
        return !isEleField(conditionItem.getField());
      }
    });
  }

  public String getEleConditionSql(BusVouAccount sumType, Condition condition) {
    return getConditionSql(sumType, condition, new GeneratorDelegate() {
      public String generatorItemSql(IConditionItem conditionItem, BusVouAccount sumType) {
        return getFieldSqlGenerator(ELEMENT_SQL_GENERATOR_KEY).generateStatement(conditionItem, sumType);
      }

      public boolean isItemAppend(IConditionItem conditionItem) {
        return isEleField(conditionItem.getField());
      }
    });
  }

  private String getConditionSql(BusVouAccount sumType, Condition condition, GeneratorDelegate delegate) {
    if (condition == null)
      return StringUtil.EMPTY;

    StringBuffer conditionSql = new StringBuffer();
    for (int i = 0; i < condition.size(); i++) {
      IConditionItem conditionItem = condition.get(i);
      if (delegate.isItemAppend(conditionItem))
        conditionSql.append(delegate.doGenerateSql(conditionItem, sumType));
    }

    return conditionSql.toString();
  }

  /**
   * 生成COA查询的字段名
   * @param coa
   * @param alias
   * @return
   */
  public String generateCcidSelectFields(FCoaDTO coa, String alias) {
    List coaDetails = coa.getCoaDetail();
    StringBuffer sb = new StringBuffer(alias + ".coa_id coa_id," + alias + ".ccid ccid,");
    FCoaDetail detail = null;
    String eleCodeLower = null;
    for (int i = 0; i < coaDetails.size(); i++) {
      detail = (FCoaDetail) coaDetails.get(i);
      eleCodeLower = detail.getEleCode();
      sb.append(alias + "." + eleCodeLower + "_id " + eleCodeLower + "_id,")
        .append(alias + "." + eleCodeLower + "_code " + eleCodeLower + "_code,")
        .append(alias + "." + eleCodeLower + "_name " + eleCodeLower + "_name,");
      if (CoaService.isAccount(eleCodeLower))
        sb.append(CoaService.getAccountAddtionalField(eleCodeLower) + ",");
    }

    return sb.substring(0, sb.length() - 1);
  }

  /**
   * 取字段的
   * @param fieldName
   * @return
   */
  private boolean isEleField(String field) {
    int charIndex = -1;
    if ((charIndex = field.lastIndexOf("_id")) >= 0 || (charIndex = field.lastIndexOf("_code")) >= 0
      || (charIndex = field.lastIndexOf("_name")) >= 0) {

      String eleCode = field.substring(0, charIndex);
      return eleOp.getEleSetByCode(eleCode) != null;
    } else
      return false;
  }

  public String getOrderStatement(Condition orderCondtion) {
    if (orderCondtion == null || orderCondtion.orderSize() == 0)
      return StringUtil.EMPTY;

    StringBuffer orderByStatement = new StringBuffer(" order by ");
    for (int i = 0; i < orderCondtion.orderSize(); i++) {
      Order order = orderCondtion.getOrder(i);
      orderByStatement.append(order.getStatement());
      //			if(isEleField(order.getField()))
      //				orderByStatement.append(order.getStatement("c"));
      //			else
      //				orderByStatement.append(order.getStatement("bal"));

      if (i < orderCondtion.orderSize() - 1) {
        orderByStatement.append(",");
      }
    }

    return orderByStatement.toString();
  }

  /**
   * 读取通过字段名特殊处理的SQL生成器，读取不出特殊处理生成器不抛异常
   * @param fieldName
   * @return
   */
  private SqlGenerator getFieldSqlGenerator(String fieldName) {
    return (SqlGenerator) getCustomerSqlGenCache().get(fieldName);
  }

  /**
   * 读取默认的SQL生成器
   * @param operName
   * @return
   */
  protected static SqlGenerator getDefaultSqlGenrator(String operName) {
    return (SqlGenerator) sqlGeneratorCache.get(operName);
  }

  /**
   * 取得SQL生成器,包括默认的特殊处理字段,但不包括要素条件.
   * @param conditionItem
   * @param sumType
   * @return
   */
  private SqlGenerator getSqlGenerator(IConditionItem conditionItem) {
    //优先读取字段的SQL生成器, 也就是特殊的字段生成器
    SqlGenerator sqlGen = getFieldSqlGenerator(conditionItem.getField());
    if (sqlGen == null)
      sqlGen = getDefaultSqlGenrator(conditionItem.getOperation());

    return sqlGen;
  }

  /**
   * 生成SQL,包括默认的特殊处理字段,但不包括要素条件.
   * @param item
   */
  private String generatorConditionSql(IConditionItem item, BusVouAccount sumType) {
    SqlGenerator sqlGen = getSqlGenerator(item);
    if (sqlGen == null)
      throw new RuntimeException("根据字段名:" + item.getField() + "和操作符:" + item.getOperation()
        + "找不到对应SQL条件生成对象, 生成条件SQL失败!");

    return sqlGen.generateStatement(item, sumType);
  }

  /**
   * 注册字段生成器
   * @param fieldName
   * @param sqlGen
   */
  public void registerFieldSqlGenerator(String fieldName, SqlGenerator sqlGen) {
    if (getFieldSqlGenerator(fieldName) != null)
      throw new RuntimeException(fieldName + "字段已经存在SQL生成器!");
    else
      getCustomerSqlGenCache().put(fieldName, sqlGen);
  }

  /**
   * SQL生成代理
   * @author
   * @version 2017-03-24
   */
  abstract class GeneratorDelegate {

    public abstract String generatorItemSql(IConditionItem item, BusVouAccount sumType);

    public String doGenerateSql(IConditionItem item, BusVouAccount sumType) {
      if (item.getCustomerSqlGen() != null) {
        return item.getCustomerSqlGen().generateStatement(item, sumType);
      } else if (item.isStrictGenerate())
        return getDefaultSqlGenrator(item.getOperation()).generateStatement(item, sumType);
      else
        return generatorItemSql(item, sumType);
    }

    public abstract boolean isItemAppend(IConditionItem item);
  }
}
