package gov.df.fap.service.gl.core.daosupport;

import gov.df.fap.service.gl.core.interfaces.FieldMapper;
import gov.df.fap.service.util.gl.core.AbstractBeanMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Mapper between db field and Map property.
 * @author 
 * @version
 */
public class MapFieldMapper implements FieldMapper {

	String dbField = null;
	AbstractBeanMapper owner = null;
	
	public MapFieldMapper(String dbField){
		this.dbField = dbField;
	}
	
	public String getDbField() {
		return dbField;
	}

	public Class getPropertyType() {
		return String.class;
	}

	public Method getGetterMethod() {
		return null;
	}

	public Method getSetterMethod() {
		return null;
	}

	public AbstractBeanMapper getOwner() {
		return owner;
	}

	public void setOwner(AbstractBeanMapper owner){
		this.owner = owner;
	}
	
	public String getPropertyName() {
		return dbField.toLowerCase();
	}

	public void setPropertyValue(Object bean, Object value)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		if (value != null)
			((Map)bean).put(getPropertyName(), value.toString());
	}
}
