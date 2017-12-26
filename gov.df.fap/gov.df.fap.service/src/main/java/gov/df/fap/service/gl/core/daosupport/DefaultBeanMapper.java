package gov.df.fap.service.gl.core.daosupport;

import gov.df.fap.service.dictionary.elecache.DefaultCacheFactory;
import gov.df.fap.service.gl.core.interfaces.FieldMapper;
import gov.df.fap.service.util.dictionary.interfaces.Cache;
import gov.df.fap.service.util.gl.core.AbstractBeanMapper;
import gov.df.fap.util.DbUtil;
import gov.df.fap.util.Properties.ClassInfo;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 默认对象映射
 * @author 
 *
 */
public class DefaultBeanMapper extends AbstractBeanMapper{
	
	protected static Cache beanMapperCache = DefaultCacheFactory.getInstance().getCacheInstance();
	static{
		beanMapperCache.setCacheCapability(50);
	}
	
	
	List fieldMappers = new ArrayList();
	Class mappingBeanType = null;
	
	/**
	 * init BeanMapper through list of FieldMapper objects.
	 * @param mappingClass
	 * @param fieldMappers
	 */
	private DefaultBeanMapper(Class mappingClass){
		this.mappingBeanType = mappingClass;
	}
	
	public DefaultBeanMapper(){}
	
	/**
	 * Factory method of Class BeanMapper
	 * @param sqlStatement 主要用于在线程中缓存BeanMapper,无实际用途
	 * @param rsmd
	 * @param clazz
	 * @return
	 */
	public static DefaultBeanMapper beanMapperFactory(String sqlStatement, ResultSetMetaData rsmd, Class clazz){
		String key = clazz.getName()+"."+sqlStatement;
		DefaultBeanMapper beanMapper = (DefaultBeanMapper) beanMapperCache.getCacheObject(key);
		try{
			if (beanMapper == null || rsmd.getColumnCount() != beanMapper.size()){//简单判断表结构是否有变化
				beanMapper = new DefaultBeanMapper(clazz);
				beanMapper.init(rsmd, clazz);
				beanMapperCache.addCacheObject(key, beanMapper);
			}
		}catch(SQLException sqlEx){
			throw new RuntimeException(sqlEx);
		}
		return beanMapper;
	}
	
	/**
	 * 初始化FieldMapper列表.
	 * @param rsmd
	 * @param clazz
	 */
	private void init(ResultSetMetaData rsmd, Class clazz){
		try{
			//如果是MAP类型
			if (Map.class.isAssignableFrom(clazz)){
				initMap(rsmd);
			}else{//如果是JAVABEAN
				initJavaBean(rsmd, clazz);
			}
		}catch(Exception ex){
			throw new RuntimeException("exception when initialized bean mapper", ex);
		}
	}
	
	/**
	 * 初始化MAP类型
	 * @param rsmd
	 * @throws SQLException
	 */
	public void initMap(ResultSetMetaData rsmd) throws SQLException{
		int columnCount = rsmd.getColumnCount();
		for (int i = 1; i <= columnCount; i++)
			addMapper(new MapFieldMapper(rsmd.getColumnName(i)));
	}
	
	/**
	 * 初始化标准JavaBean
	 * @param rsmd
	 * @param clazz
	 * @throws SQLException
	 */
	public void initJavaBean(ResultSetMetaData rsmd, Class clazz) throws SQLException{
		
		int columnCount = rsmd.getColumnCount();
		
		Map propMap = new HashMap();
		ClassInfo classInfo = ClassInfo.classInfoFactory(clazz);
		String[] writeablePropName = classInfo.getWriteableProps();
		for (int i = 0; i < writeablePropName.length; i++)
			propMap.put(writeablePropName[i].toUpperCase(), writeablePropName[i]);
		
		for (int i = 1; i <= columnCount; i++){
			
			String dbField = rsmd.getColumnName(i);
			String propertyName = (String)propMap.get(dbField.toUpperCase());
			if (propertyName != null){
				addMapper(new BeanFieldMapper(
						dbField, 
						propertyName, 
						classInfo.getSetterType(propertyName), 
						classInfo.getGetterMethod(propertyName), 
						classInfo.getSetterMethod(propertyName))
						);
			}
		}
	}
	
	/**
	 * add FieldMapper item;
	 * @param mapper
	 */
	public void addMapper(FieldMapper mapper){
		this.fieldMappers.add(mapper);
		mapper.setOwner(this);
	}
	
	/**
	 * add a list of FieldMapper Object
	 * @param fieldMappers
	 */
	protected void addMappers(List fieldMappers){
		if (fieldMappers == null || fieldMappers.size() == 0)
			return;
			
		for (int i = 0; i < fieldMappers.size(); i++){
			BeanFieldMapper fm = (BeanFieldMapper)fieldMappers.get(i);
			this.addMapper(fm);
		}
	}
	
	int size(){
		return fieldMappers.size();
	}
	
	protected void remove(FieldMapper fm){
		fieldMappers.remove(fm);
	}
	
	FieldMapper get(int i){
		return (FieldMapper)fieldMappers.get(i);
	}
	
	/**
	 * Factory method of the mapped bean.
	 * @param rs db result set
	 * @return
	 */
	public Object resultSetToObject(ResultSet rs){
		try {
			Object beanObject = newClassInstant(rs);
			for (int i = 0; i < size(); i++){
				FieldMapper fieldMapper = get(i);
				//目前只取String
				Object propertyValue = DbUtil.getResultValue(rs, fieldMapper.getDbField(), String.class);
				fieldMapper.setPropertyValue(beanObject, propertyValue);
			}
			return beanObject;
		} catch (Exception e) {
			throw new RuntimeException("exception when transfer ResutlSet to Object!",e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see gov.gfmis.fap.gl.core.daosupport.AbstractBeanMapper#newClassInstant(java.sql.ResultSet)
	 */
	public Object newClassInstant(ResultSet rs) {
		try {
			return mappingBeanType.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Exception when get instance of Class:"+mappingBeanType.getName(), e);
		}
	}
}
