package gov.df.fap.util.where;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title:构造单表的where子句
 * </p>
 * <p>
 * Description:多表嵌套别找我，只提供specialSql，爱怎么搞怎么搞。 生成的where子句以操作符开始，例如 and xxx='xxx'
 * ...... 所以不能直接跟在where的后面。
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: 
 * </p>
 * <p>
 * CreateData 2006-6-9
 * </p>
 * 
 * @author justin
 * @version 1.0
 */
public class WhereObject implements Serializable, Cloneable {

  /**
   * 
   */
  private static final long serialVersionUID = -1556853982065483221L;

  public static final String NOT_EQUAL = "<>";

  public static final String EQUAL = "=";

  public static final String LIKE = "like";

  public static final String NOT_LIKE = "not like";

  public static final String GREATER = ">";

  public static final String LESS = "<";

  public static final String IN = "in";

  public static final String BETWEEN = "between";

  /**
   * 存储条件的集合
   */
  private List whereList;

  /**
   * 存储orderby的集合，每个元素均为String
   */
  private List orderByList = new ArrayList();

  /**
   * 存储特殊的sql语句
   */
  private String specialSql = "";

  /**
   * 存储查询视图返回的sql
   */
  private String queryPanelSql = "";

  private String alias = "alias";

  private String detailAlias = "detail";

  // relatingAlias 出现的第3个表的表名 ＋ 别名 clarky.gao 2007.04.19 13:59
  private String relatingAlias = " ";

  private Object object;

  public Object getObject() {
    return object;
  }

  public void setObject(Object object) {
    this.object = object;
  }

  public static void main(String[] args) {
    WhereObject o = new WhereObject();
    // and en='001001' and (mk like '001%' or mk like '002%')
    // 设置where子句
    o.and("en_code", WhereObject.EQUAL, "'001001'").andPlusBracket("mk_code", WhereObject.LIKE, "'001%'")
      .or("mk_code", WhereObject.LIKE, "'002%'").addBracketBack();
    o.and("mk", WhereObject.NOT_LIKE, "'004%'").and("mk", WhereObject.GREATER + WhereObject.EQUAL, "'002002'");
    o.and("en", WhereObject.BETWEEN, "('001','002')");
    o.and("en", WhereObject.IN, "('001','002','003')");
    o.addOrderBy("en_code");
    o.addOrderBy("mk_code");

    // 设置特殊的where子句或sql语句
    o.setSpecialSql("and exists(select 1 from bb where ....)");

    o.toString();
    o.getSpecialSql();

    System.out.println(o.toString() + o.getOrderBy());
    // 设置别名
    o.setAlias("tim");
    System.out.println(o.toString() + o.getOrderBy());
  }

  public WhereObject() {
    super();
  }

  public WhereObject(String sql) {
    super();
    this.specialSql = sql;
  }

  /**
   * 添加到whereList
   * @param bean
   */
  private void addWhereList(WhereBean bean) {
    if (whereList == null) {
      whereList = new ArrayList();
    }
    whereList.add(bean);
  }

  public String getRelatingAlias() {
    return relatingAlias;
  }

  public void setRelatingAlias(String relatingAlias) {
    this.relatingAlias = relatingAlias;
  }

  /**
   * 拼装and条件
   * @param key
   * @param operation
   * @param value
   * @return
   */
  public WhereObject and(String key, String operation, String value) {
    WhereBean bean = new WhereBean();
    bean.setRelation("and");
    bean.setBracketFront(false);
    bean.setKey(key);
    bean.setOperation(operation);
    bean.setValue(value);
    bean.setBracketBack(false);
    addWhereList(bean);
    return this;
  }

  public WhereObject andPlusBracket(String key, String operation, String value) {
    WhereBean bean = new WhereBean();
    bean.setRelation("and");
    bean.setBracketFront(true);
    bean.setKey(key);
    bean.setOperation(operation);
    bean.setValue(value);
    bean.setBracketBack(false);
    addWhereList(bean);
    return this;
  }

  /**
   * 拼装or条件
   * @param key
   * @param operation
   * @param value
   * @return
   */
  public WhereObject or(String key, String operation, String value) {
    WhereBean bean = new WhereBean();
    bean.setRelation("or");
    bean.setBracketFront(false);
    bean.setKey(key);
    bean.setOperation(operation);
    bean.setValue(value);
    bean.setBracketBack(false);
    addWhereList(bean);
    return this;
  }

  public WhereObject orPlusBracket(String key, String operation, String value) {
    WhereBean bean = new WhereBean();
    bean.setRelation("or");
    bean.setBracketFront(true);
    bean.setKey(key);
    bean.setOperation(operation);
    bean.setValue(value);
    bean.setBracketBack(false);
    addWhereList(bean);
    return this;
  }

  public WhereObject addBracketFront() {
    WhereBean bean = new WhereBean();
    bean.setBracketFront(true);
    bean.setOnlyBracket(true);
    addWhereList(bean);
    return this;
  }

  public WhereObject addBracketBack() {
    WhereBean bean = new WhereBean();
    bean.setBracketBack(true);
    bean.setOnlyBracket(true);
    addWhereList(bean);
    return this;
  }

  public WhereObject addOrderBy(String sOrderBy) {
    if (sOrderBy != null && sOrderBy.length() > 0) {
      orderByList.add(sOrderBy);
    }
    return this;
  }

  /**
   * 把对象转换成sql语句
   */
  public String toString() {
    if (whereList == null || whereList.size() == 0) {
      return "";
    }

    StringBuffer sb = new StringBuffer();
    for (Iterator it = whereList.iterator(); it.hasNext();) {
      WhereBean bean = (WhereBean) it.next();
      if (bean.isOnlyBracket()) {
        sb.append(" ");
        sb.append(bean.isBracketFront() ? "(" : "");
        sb.append(bean.isBracketBack() ? ")" : "");
        sb.append(" ");
      } else if (alias == null || alias.equals("")) {
        sb.append(" ");
        sb.append(bean.getRelation());
        sb.append(" ");
        sb.append(bean.isBracketFront() ? "(" : "");
        sb.append(" ");
        sb.append(bean.getKey());
        sb.append(" ");
        sb.append(bean.getOperation());
        sb.append(" ");
        sb.append(bean.getValue());
        sb.append(bean.isBracketBack() ? ")" : "");
        sb.append(" ");
      } else {
        sb.append(" ");
        sb.append(bean.getRelation());
        sb.append(" ");
        sb.append(bean.isBracketFront() ? "(" : "");
        sb.append(" ");
        sb.append(getAlias() + "." + bean.getKey());
        sb.append(" ");
        sb.append(bean.getOperation());
        sb.append(" ");
        sb.append(bean.getValue());
        sb.append(bean.isBracketBack() ? ")" : "");
        sb.append(" ");
      }
    }
    sb.append(" " + queryPanelSql);
    return sb.toString();
  }

  /**
   * 构建orderby子句
   * 
   * @return
   */
  public String getOrderBy() {
    StringBuffer sb = new StringBuffer();
    if (orderByList.size() > 0) {
      sb.append(" order by ");
      for (int i = 0, n = orderByList.size(); i < n; i++) {
        sb.append(this.getAlias());
        sb.append(".");
        sb.append((String) orderByList.get(i));
        sb.append(",");
      }
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }

  /**
   * @param whereList
   *            The whereList to set.
   */
  public void setWhereList(List whereList) {
    if (this.whereList == null) {
      this.whereList = new ArrayList();
    }
    this.whereList = whereList;
  }

  /**
   * @return Returns the whereList.
   */
  public List getWhereList() {
    return whereList;
  }

  /**
   * @param specialSql
   *            The specialSql to set.
   */
  public void setSpecialSql(String specialSql) {
    if (this.specialSql == null) {
      this.specialSql = new String();
    }
    this.specialSql = specialSql;
  }

  /**
   * @return Returns the specialSql.
   */
  public String getSpecialSql() {
    if (specialSql == null) {
      specialSql = "";
    }
    return specialSql;
  }

  /**
   * @param alias
   *            The alias to set.
   */
  public WhereObject setAlias(String alias) {
    this.alias = alias;
    return this;
  }

  /**
   * @return Returns the alias.
   */
  public String getAlias() {
    return alias;
  }

  /**
   * @return Returns the queryPanelSql.
   */
  public String getQueryPanelSql() {
    return queryPanelSql;
  }

  /**
   * @param queryPanelSql
   *            The queryPanelSql to set.
   */
  public void setQueryPanelSql(String queryPanelSql) {
    this.queryPanelSql = queryPanelSql;
  }

  public void addOraderByList(List orderByList) {
    if (orderByList != null) {
      this.setOrderByList(orderByList);
    }
  }

  private void setOrderByList(List orderByList) {
    this.orderByList = orderByList;
  }

  public String getDetailAlias() {
    return detailAlias;
  }

  public void setDetailAlias(String billAlias) {
    this.detailAlias = billAlias;
  }

  //	河南版本合并 2007年7月17号 小雷  河南工资统发
  public Object clone() {
    WhereObject whereObj = null;
    try {
      whereObj = (WhereObject) super.clone();
      // modified by zhoulingling 2010-06-23
      // 判断list是否为空，否则容易造成NullPointerException
      if (whereList != null && whereList.size() > 0) {
        List whereCopy = new ArrayList(whereList.size());
        whereCopy.addAll(whereList);
        whereObj.setWhereList(whereCopy);
      }
      if (orderByList != null && orderByList.size() > 0) {
        List orderByCopy = new ArrayList(orderByList.size());
        orderByCopy.addAll(orderByList);
        whereObj.setOrderByList(orderByCopy);
      }
      // modified end zhoulingling 2010-06-23
    } catch (CloneNotSupportedException ex) {
      ex.printStackTrace();
    }
    return whereObj;
  }

  /**
   * 根据主键和数据列表得到 in组合的条件sql
   * @param keyValues
   * @param keyField
   * @author hlf
   * @return
   */
  public void in(String keyField, List keyValues) {
    this.and("", "", inSql(keyField, keyValues));
  }

  /**
   * 根据主键和数据列表得到 in组合的条件sql
   * @param keyField
   * @param keyValues
   * @author hulitian
   * @return
   */
  public static String inSql(String keyField, List keyValues) {
    StringBuffer sb = new StringBuffer();
    sb.append("(  ").append(keyField).append(" in ( ");
    for (int i = 0; i < keyValues.size(); i++) {
      if (i % 1000 == 0 && i != 0) {
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") or ").append(keyField).append(" in ( ");
      }
      sb.append("'").append(keyValues.get(i)).append("',");

    }
    sb.deleteCharAt(sb.length() - 1);
    sb.append(") )");

    return sb.toString();
  }

  /**
   * 根据主键和数据列表得到 in组合的条件sql
   * @param keyField
   * @param Map keyValues 
   * @author hulitian
   * @return
   */
  public static String inSqlForObj(String keyField, String keyValue, List keyValues) {
    StringBuffer sb = new StringBuffer();
    sb.append("(  ").append(keyField).append(" in ( ");
    for (int i = 0; i < keyValues.size(); i++) {
      if (i % 1000 == 0 && i != 0) {
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") or ").append(keyField).append(" in ( ");
      }
      sb.append("'").append(((Map) keyValues.get(i)).get(keyValue)).append("',");
    }
    sb.deleteCharAt(sb.length() - 1);
    sb.append(") )");

    return sb.toString();
  }

  /**
   * 根据主键和数据列表得到 in组合的条件sql
   * @param keyField
   * @param keyValues
   * @author hulitian
   * @return
   */
  public static String inSql(String keyField, String alias, List keyValues) {
    StringBuffer sb = new StringBuffer();
    sb.append("(  ").append(alias).append(".").append(keyField).append(" in ( ");
    for (int i = 0; i < keyValues.size(); i++) {
      if (i % 1000 == 0 && i != 0) {
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") or ").append(alias).append(".").append(keyField).append(" in ( ");
      }
      sb.append("'").append(keyValues.get(i)).append("',");

    }
    sb.deleteCharAt(sb.length() - 1);
    sb.append(") )");

    return sb.toString();
  }
}
