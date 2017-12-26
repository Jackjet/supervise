package gov.df.fap.bean.gl.dto;

import gov.df.fap.bean.gl.core.sqlgen.SqlGenerator;

/**
 * 
 * @author Administrator
 *
 */
public interface IConditionItem {

	/**
	 * 取连接符
	 * @return
	 */
	public String getConnectOper();
	
	/**
	 * 取条件字段
	 * @return
	 */
	public String getField();
	
	/**
	 * 取条件过滤值
	 * @return
	 */
	public String getValue();
	
	/**
	 * 取逻辑操作符
	 * @return
	 */
	public String getOperation();
	
	/**
	 * 自定义SQL生成
	 * @return
	 */
	public SqlGenerator getCustomerSqlGen();
	
	/**
	 * 严格条件,总账引擎不自动解析条件
	 * @return
	 */
	public boolean isStrictGenerate();
	
	/**
	 * 严格条件
	 * @param strictGenerate
	 */
	public void setStrictGenerate(boolean strictGenerate);
}
