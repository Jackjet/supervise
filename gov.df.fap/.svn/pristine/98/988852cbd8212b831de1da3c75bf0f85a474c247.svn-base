package gov.df.fap.bean.gl.dto;

import java.util.List;

import gov.df.fap.bean.gl.core.sqlgen.SqlGenerator;

/**
 * 条件规格接口
 * @author 
 *
 */
public interface Condition {

	//枚举各种操作值
	public static final String	EQUAL		= "=";
	public static final String	GT			= ">";
	public static final String	GTEQUAL		= ">=";
	public static final String	LT			= "<";
	public static final String	LTEQUAL		= "<=";
	public static final String	NOTEQUAL	= "<>";
	public static final String	IN			= "in";
	public static final String	NOTIN		= "not in";
	public static final String	LIKE		= "like";
	public static final String	NOTLIKE		= "not like";
	//条件连接符
	public static final String	AND			= "and";
	public static final String	OR			= "or";
	//操作类型和连接符共用,用来设定某些特殊情况
	public static final String	NULL		= "";
	
	public static final String  OUTTER_SQL_FLAG = "agency_id";
	public static final String  INNER_SQL_FLAG = "inner";  
	
	/**
	 * 设置查询条件(默认and)
	 * @param field 字段名
	 * @param operation 查询条件类型
	 * @param value 字符值
	 */
	public abstract void add(String field, String operation, String value);
	
	/**
	 * 设置查询条件(默认and)
	 * @param field 字段名
	 * @param operation 查询条件类型
	 * @param value 字符值
	 */
	public abstract void add(String field, String operation, String value, boolean strict);
	
	/**
	 * 设置查询条件
	 * @param andOR 连接符
	 * @param field 字段名
	 * @param operation 查询条件类型
	 * @param value 字符值
	 */
	public abstract void add(String andOR, String field, String operation,
			String value);

	/**
	 * 设置自定义条件SQL
	 * @param andOr
	 * @param field
	 * @param sqlGen
	 */
	public void add(String andOr, String field, SqlGenerator sqlGen);
	
	/**
	 * 
	 * @param sql
	 */
	public void addSqlCondition(String sql);
	
	/**
	 * 
	 * @param sql
	 */
	public void addSqlCondition(String field, String sql);
	
	/**
	 * 设置排序
	 * @param order
	 */
	public void addOrder(Order order);
	
	/**
	 * 
	 * @param i
	 * @return
	 */
	public Order getOrder(int i);
	
	/**
	 * 
	 * @return
	 */
	public int orderSize();
	
	/**
	 * 
	 * @param ascending
	 * @param field
	 */
	public void addOrder(boolean ascending, String field);
	
	/**
	 * 判断某一字段是否在过滤条件列表中存在
	 * @param field 字段名
	 * @return true or false
	 */
	public abstract boolean isItemExists(String field);

	/**
	 * 返回条件列表长度
	 * @return int
	 */
	public abstract int size();

	/**
	 * 根据索引读取条件项
	 * @param index 索引
	 * @return index;
	 */
	public abstract ConditionItem get(int index);

	/**
	 * 根据索引删除条件项
	 * @param index 索引
	 */
	public abstract void remove(int index);
	
	/**
	 * 是否查询最大页数
	 * @return
	 */
	public boolean isQueryWithMaxCount();
	
	/**
	 * 允许汇总
	 * @return
	 */
	public boolean allowGroup();
	
	/**
	 * 设置允许汇总
	 * @param allowGroup
	 */
	public void setAllowGroup(boolean allowGroup);
	
	/**
	 * 取出条件明细
	 * @return
	 */
	public List getCondition();
	
	/**
	 * 是否需要权限
	 * @return
	 */
	public boolean isAllowRight();
	/**
	 * 是否从明细表中查额度
	 */
	public boolean isBalanceQueryByDetailtable();
}