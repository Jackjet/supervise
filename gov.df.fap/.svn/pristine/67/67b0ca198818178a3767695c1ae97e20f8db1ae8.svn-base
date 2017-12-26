package gov.df.fap.api.gl.core.daosupport;

/**
 * 类定位器,根据数据库某字段,决定查询出来的一行数据是何种对象.
 * @author 
 * @version 
 */
public interface ClassLocator {

	/**
	 * 决定类型的字段名.
	 * @return
	 */
	public String getFieldName();
	
	/**
	 * 决定类型字段的index.
	 * @return
	 */
	public int getFieldIndex();
	
	/**
	 * 所以有效的值.
	 * @return
	 */
	public Object[] fieldValues();
	
	/**
	 * 字段值决定返回的类.
	 * @return 行映射出来的类
	 */
	public Class mappingClass(Object fieldValue);
}
