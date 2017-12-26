package gov.df.fap.bean.gl.dto;

import gov.df.fap.bean.gl.core.sqlgen.SqlGenerator;
import gov.df.fap.bean.gl.core.sqlgen.StatementSqlGenerator;
import gov.df.fap.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 额度查询条件对象,以后应该考虑将表名等条件放在里面,这样便可以提供给外部别名等信息,而不是等外部传入别名.
 * @author Administrator
 *
 */
public class ConditionObj implements Condition,Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static Map operaMap = new HashMap();
	protected List itemList = new ArrayList();
	private List orders = new ArrayList();
	
	private boolean allowGroup = false;
	private boolean queryWithMaxCount = true;
	private boolean allowRight = true;
	private boolean balanceQueryByDetailtable=false;
	static
	{
		operaMap.put("=","=");
		operaMap.put(">",">");
		operaMap.put(">=",">=");
		operaMap.put("<","<");
		operaMap.put("<=","<=");
		operaMap.put("<>","<>");
		operaMap.put("in","in");
		operaMap.put("not in","not in");
		operaMap.put("like","like");
		operaMap.put("not like","not like");
	}
	
	public List getCondition()
	{
		return itemList;
	}
	public boolean isBalanceQueryByDetailtable(){
		return balanceQueryByDetailtable;
	}
	public void setBalaceQueryByDetailtable(boolean balanceQueryByDetailtable){
		this.balanceQueryByDetailtable=balanceQueryByDetailtable;
	}
	public void addItem(IConditionItem item){
		itemList.add(item);
	}
	/* (non-Javadoc)
	 * @see gov.gfmis.fap.gl.dao.Condition#add(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void add(String field,String operation,String value)
	{
		ConditionItem item = generateItem(null, field, operation, value, false);
		if (item != null)
			itemList.add(item);
	}
	/* (non-Javadoc)
	 * @see gov.gfmis.fap.gl.dao.Condition#add(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void add(String andOR,String field,String operation,String value)
	{
		ConditionItem item = generateItem(andOR, field, operation, value, false);
		if (item != null)
			itemList.add(item);
	}
	
	public void add(String andOR, String field, SqlGenerator sqlGen){
		if (!StringUtil.isEmpty(andOR) && !StringUtil.isEmpty(field) && sqlGen != null){
			ConditionItem item = new ConditionItem();
			item.setAndOR(andOR);
			item.setField(field);
			item.setCustomerSqlGen(sqlGen);
			itemList.add(item);
		}
	}
	
	public void addSqlCondition(String sql){
		add(" ", INNER_SQL_FLAG, new StatementSqlGenerator(sql));
	}
	
	public void addSqlCondition(String andOR, String sql){
		add(andOR, INNER_SQL_FLAG, new StatementSqlGenerator(sql));
	}
	
	/**
	 * 
	 * @param opConn
	 * @param field
	 * @param customSql
	 */
	public void addSqlCondition(String andOR, String field, String customSql) {
		add(andOR, field, new StatementSqlGenerator(customSql));
	}
	
	/* (non-Javadoc)
	 * @see gov.gfmis.fap.gl.dao.Condition#isItemExists(java.lang.String)
	 */
	public boolean isItemExists(String field)
	{
		for(int i=0;i<itemList.size();i++)
		{
			ConditionItem item = (ConditionItem)itemList.get(i);
			if(item.getField().equals(field))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * 判断某一字段在过滤条件列表中的索引位置
	 * @param field 字段名 
	 * @return int -1表示不存在
	 */
	public int getIndex(String field)
	{
		for(int i=0;i<itemList.size();i++)
		{
			ConditionItem item = (ConditionItem)itemList.get(i);
			if(item.getField().equals(field))
			{
				return i;
			}
		}
		return -1;
	}
	/**
	 * 根据过滤条件字段名获取过滤条件名
	 * @param field 字段名
	 * @return 条件颗粒
	 */
	public ConditionItem getItem(String field)
	{
		ConditionItem item = null;
		int index = getIndex(field);
		if(index != -1)
		{
			item = (ConditionItem)itemList.get(index);
		}
		return item;
	}
	/* (non-Javadoc)
	 * @see gov.gfmis.fap.gl.dao.Condition#size()
	 */
	public int size()
	{
		return itemList.size();
	}
	/* (non-Javadoc)
	 * @see gov.gfmis.fap.gl.dao.Condition#get(int)
	 */
	public ConditionItem get(int index)
	{
		if(index < 0 && index >= itemList.size())
		{
		    throw new IndexOutOfBoundsException(
		    		"Index: "+index+", Size: "+itemList.size());
		}
		return (ConditionItem)itemList.get(index);
	}
	/* (non-Javadoc)
	 * @see gov.gfmis.fap.gl.dao.Condition#remove(int)
	 */
	public void remove(int index)
	{
		if(index < 0 && index >= itemList.size())
		{
		    throw new IndexOutOfBoundsException(
		    		"Index: "+index+", Size: "+itemList.size());
		}
		itemList.remove(index);
	}
	
	public void addOrder(Order order) {
		this.orders.add(order);
	}
	
	public void addOrder(boolean ascending, String field){
		orders.add(new Order(field,ascending));
	}
	
	public Order getOrder(int i){
		return (Order) orders.get(i);
	}
	
	public int orderSize(){
		return orders.size();
	}
	public boolean isQueryWithMaxCount() {
		return queryWithMaxCount;
	}
	public void setQueryWithMaxCount(boolean queryWithMaxCount) {
		this.queryWithMaxCount = queryWithMaxCount;
	}
	public void add(String field, String operation, String value, boolean strict) {
		ConditionItem item = this.generateItem(Condition.AND, field, operation, value, strict);
		if (item != null)
			itemList.add(item);
	}
	
	protected ConditionItem generateItem(String connOp, String field, String operation, String value, boolean strict){
		if(field != null && !field.equals(""))
		{
			if(connOp != null && !connOp.equalsIgnoreCase("and") 
					&& !connOp.equalsIgnoreCase("or") && !connOp.equals("")){
				throw new RuntimeException("SQL连接符不支持"+connOp+",仅支持and,or和空!");
			}
			if(!operaMap.containsKey(operation) && !NULL.equals(operation)){
				throw new RuntimeException("查询类型不支持"+operation+",仅支持=,>,>=,<,<=,<>,in,not in,like,\"\"!");
			}
			ConditionItem item = new ConditionItem();
			item.setOperation(operation);
			item.setField(field);
			item.setValue(value);
			item.setAndOR(StringUtil.isEmpty(connOp) ? AND : connOp);
			item.setStrictGenerate(strict);
			return item;
		}
		return null;
	}
	
	public void setAllowGroup(boolean allowGroup){
		this.allowGroup = allowGroup;
	}
	
	public boolean allowGroup() {
		return allowGroup;
	}
	
	public boolean isAllowRight() {
		return allowRight;
	}
	
	public void setAllowRight(boolean allowRight) {
		this.allowRight = allowRight;
	}
	
	
}
