package gov.df.fap.service.gl.core.daosupport;

import gov.df.fap.service.gl.core.interfaces.FieldMapper;
import gov.df.fap.service.util.gl.core.AbstractBeanMapper;
import gov.df.fap.util.Properties.ObjectTransferWrapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 数据库字段与bean属性的映射
 * @author 
 *
 */
public class BeanFieldMapper implements FieldMapper {

  AbstractBeanMapper owner = null;

  String dbField = null;

  String propertyName = null;

  Class propertyType = null;

  Method getterMethod = null;

  Method setterMethod = null;

  static ObjectTransferWrapper objectTransfer = new ObjectTransferWrapper();

  public BeanFieldMapper(String dbField, String propertyName, Class propertyType, Method getterMethod,
    Method setterMethod) {
    this.dbField = dbField;
    this.propertyName = propertyName;
    this.propertyType = propertyType;
    this.getterMethod = getterMethod;
    this.setterMethod = setterMethod;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.dao.support.FieldMapper#getDbField()
   */
  public String getDbField() {
    return dbField;
  }

  public void setDbField(String dbField) {
    this.dbField = dbField;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.dao.support.FieldMapper#getPropertyType()
   */
  public Class getPropertyType() {
    return propertyType;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.dao.support.FieldMapper#getGetterMethod()
   */
  public Method getGetterMethod() {
    return getterMethod;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.dao.support.FieldMapper#getSetterMethod()
   */
  public Method getSetterMethod() {
    return setterMethod;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.dao.support.FieldMapper#getOwner()
   */
  public AbstractBeanMapper getOwner() {
    return owner;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.dao.support.FieldMapper#getPropertyName()
   */
  public String getPropertyName() {
    return this.propertyName;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.dao.support.FieldMapper#setPropertyValue(java.lang.Object, java.lang.Object)
   */
  public void setPropertyValue(Object bean, Object value) throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException {
    if (value == null)
      return;
    if (getPropertyType() == value.getClass())
      setterMethod.invoke(bean, new Object[] { value });
    else
      setterMethod.invoke(bean, new Object[] { objectTransfer.converteValue(getPropertyType(), value) });
  }

  public void setOwner(AbstractBeanMapper owner) {
    this.owner = owner;
  }
}
