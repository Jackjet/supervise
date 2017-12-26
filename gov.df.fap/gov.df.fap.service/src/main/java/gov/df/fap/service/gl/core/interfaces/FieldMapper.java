package gov.df.fap.service.gl.core.interfaces;

import gov.df.fap.service.util.gl.core.AbstractBeanMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Mapper for database table field to Object property.
 * @author 
 * @version 
 */

public interface FieldMapper {

	public abstract String getDbField();

	public abstract Class getPropertyType();

	public abstract Method getGetterMethod();

	public abstract Method getSetterMethod();

	public abstract AbstractBeanMapper getOwner();

	public void setOwner(AbstractBeanMapper beanMapper);
	
	public abstract String getPropertyName();

	public abstract void setPropertyValue(Object bean, Object value)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException;
}