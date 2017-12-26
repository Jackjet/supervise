package gov.df.fap.service.gl.core.daosupport;

import gov.df.fap.api.gl.core.daosupport.ClassLocator;
import gov.df.fap.service.util.gl.core.AbstractBeanMapper;
import gov.df.fap.util.StringUtil;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 针对同一个表,根据字段值不同而得出不同对象的情况,使用这个类.这可以很好解决对象多态性问题.
 * @author 
 * @version 
 */
public class MultipleBeanMapper extends AbstractBeanMapper {
	
	/**对象类型映射所使用字段名*/
	private String fieldName = StringUtil.EMPTY;
	
	/**BeanMapper对象容器*/
	public Map beanMappers = new HashMap();
	
	/**
	 * 构造多类型返回值形式的BeanMapper.
	 * @param sql SQL语句
	 * @param rsmd 查询的结果元数据
	 * @param locator 类型定位器
	 */
	public MultipleBeanMapper(String sql, ResultSetMetaData rsmd, ClassLocator locator){
		Object[] value = locator.fieldValues();
		fieldName = locator.getFieldName();
		if (value.length == 0)
			throw new IllegalArgumentException("Field value array can not be null!");
		if (fieldName == null)
			throw new IllegalArgumentException("Field name can not be null!");
		for (int i = 0; i < value.length; i++){
			beanMappers.put(value[i], DefaultBeanMapper.beanMapperFactory(sql, rsmd, locator.mappingClass(value[i])));
		}
	}

	public AbstractBeanMapper getBeanMapper(String fieldValue){
		return (AbstractBeanMapper) beanMappers.get(fieldValue);
	}

	/**
	 * 根据类定位器去确定返回何种类型的对象,这种处理利于多态的对象返回.
	 */
	public Object resultSetToObject(ResultSet rs) throws SQLException {
		
		/*该方法会被频繁调用,如果出现性能问题,可以将value与mapper设置为全局变量(类变量),
		 * 因为BeanMapper对象是只在线程中使用,不会出现多线程问题.
		 */
		String value = rs.getString(fieldName);
		AbstractBeanMapper mapper = getBeanMapper(value);
		
		if (mapper == null)
			throw new RuntimeException("Can not locat mapping class by field '"+fieldName+"' with value '"+rs.getString(value)+"'");
			
		return mapper.resultSetToObject(rs);
	}

}
