package gov.df.fap.util.Properties;



import gov.df.fap.util.xml.XMLData;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * 
 * @author LittleTree
 * @version 1.1
 *
 */
public class PropertiesUtil extends PropertyUtils{

	protected static final ThreadLocal classFieldsCache = new ThreadLocal();
	
	protected static final ObjectTransferWrapper transferWrapper = new ObjectTransferWrapper();
	
	/**
	 * VO转为<code>XMLData</code>,目前转换后属性均为<code>String</code>类型.
	 * 
	 * @param voObject
	 * @return
	 * @throws IntrospectionException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static XMLData getData(Object object) throws Exception{
		
		if (object == null)
			return null;
		
		XMLData data = new XMLData();
		Class clazz = object.getClass();
		Field[] fields = null;
		
		while(clazz != Object.class){
			fields = clazz.getDeclaredFields();
			propertyGetter(object, data, fields, clazz);
			clazz = clazz.getSuperclass();
		}
		
		return data;
	}
	
	/**
	 * 支持对父类属性的复制到一个Bean中
	 * @param object
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static Object getData(XMLData object, Class clazz) throws Exception{
		
		if (object == null)
			return null;
		Object vo = clazz.newInstance();
		
		if (clazz.isAssignableFrom(Map.class)){
			((Map) vo).putAll(object);
			return vo;
		}
		
		//增加对父类的属性复制.
		
		ClassInfo clzInfo = ClassInfo.classInfoFactory(clazz);
		String[] propertys = clzInfo.getWriteableProps();
		if (propertys == null)
			return null;
		for (int i = 0; i < propertys.length; i++){
			final String propertyName=  propertys[i];
			final Object value = object.get(propertyName);
			final Class setterType = clzInfo.getSetterType(propertyName);
			if (value != null){
				clzInfo.getSetterMethod(propertyName).invoke(vo, new Object[]{transferWrapper.converteValue(setterType, value)});
			}
		}
		
		return vo;
	}
	
	/**
	 * 属性读取到XMLData中
	 * @param object
	 * @param data
	 * @param fields
	 * @param clazz
	 * @throws Exception
	 */
	private static void propertyGetter(Object object, XMLData data, Field[] fields, Class clazz) throws Exception{
//		PropertyDescriptor pd = null;
		String propertyName = null;
		
		for (int i = 0; i < fields.length; i++){
			propertyName = fields[i].getName();
//			pd = new PropertyDescriptor(propertyName, clazz);
			data.put(toDataBaseStyle(propertyName), getProperty(object, propertyName).toString());
		}
	}
	
	/**
	 * 单个属性setter
	 * @param target  目标对象
	 * @param property 属性名
	 * @param value   setter值
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setProperty(Object target, String property, Object value) {
		try {
			PropertyUtils.setProperty(target, property, value);
		}catch (Exception e) {
			throw new RuntimeException("exception when set property:"+property+" to Class:"+target.getClass().getName(), e);
		} 
	}
	
	/**
	 * 单个属性setter
	 * @param target
	 * @param property
	 * @param value
	 */
	public static void setPropertyIgnoreFit(Object target, String property, Object value){
		try {
			if (target instanceof Map){
				PropertyUtils.setProperty(target, property, value);
			}else
				BeanUtils.setProperty(target, property, value);
		}catch (Exception e) {
			throw new RuntimeException("exception when set property:"+property+" to Class:"+target.getClass().getName(), e);
		} 
	}
	
	/**
	 * 单个属性getter
	 * @param target  getter的对象
	 * @param property  属性名
	 * @return
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object getProperty(Object target, String property) {
		try{
			return PropertyUtils.getProperty(target, property);
		}catch(NestedNullException nullEx){
			return null;
		}catch(IllegalArgumentException ex1){
			if (ex1.getMessage().indexOf("Null") > -1)
				return null;
			else
				throw ex1;
		}catch(Exception ex){
			throw new RuntimeException("exception when get property:"+property+" from Class:"+target.getClass().getName(), ex);
		}
	}
	
	/**
	 * 是否基本属性
	 * @param field
	 * @return
	 */
	public static boolean isPrimitive(Field field){
		return field.getClass().isPrimitive();
	}
	
	/**
	 * 变成数据库风格的变量名
	 * @param str
	 * @return
	 */
	public static String toDataBaseStyle(String str){
		
		char temp = 0;
		StringBuffer tmp = new StringBuffer();
		for (int i = 0; i < str.length(); i++){
			temp = str.charAt(i);
			if (temp > 64 && temp < 91){
				tmp.append("_"+Character.toLowerCase(temp));
			}else
				tmp.append(temp);
		}
		return tmp.toString();
	}
	
	/**
	 * 变成JavaBean风格的属性名
	 * @param str
	 * @return
	 */
	public static String toBeanStyle(String str){
		
		char downLine = '_';
		str = str.toLowerCase();
		char temp = 0;
		StringBuffer tmp = new StringBuffer();
		for (int i = 0; i < str.length(); i++){
			temp = str.charAt(i);
			if (temp == downLine){
				tmp.append(Character.toUpperCase(str.charAt(++i)));
			}else
				tmp.append(temp);
		}
		return tmp.toString();
	}
	
	/**
	 * 取得类的所有属性，包括父类属性
	 * @param clazz
	 * @return
	 */
	public static Field[] getAllFields(Class clazz){
		Field[] field = clazz.getDeclaredFields();
		Class superClass = clazz.getSuperclass();
		while(superClass != Object.class){
			Field[] newField = superClass.getDeclaredFields();
			Field[] all = new Field[field.length+newField.length];
			System.arraycopy(field, 0, all, 0, field.length);
			System.arraycopy(newField, 0, all, field.length, newField.length);
			field = all;
			superClass = superClass.getSuperclass();
		}
		return field;
	}
	
	/**
	 * 取得类属性
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Field getField(Class clazz, String fieldName){
		//从缓存读取
		Field clazzField = getCacheField(clazz, fieldName);
		if (clazzField != null)
			return clazzField;
		
		clazzField = getFieldFromClass(clazz, fieldName);
		if (clazzField != null){
			//缓存起属性
			setCacheField(clazz, clazzField);
		}
		
		return clazzField;
		//throw new GlException("no field named "+ fieldName+" in Class "+clazz.getName());
	}
	
	/**
	 * 读取类属性对象
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Field getFieldFromClass(Class clazz, String fieldName){
		Field[] fields = PropertiesUtil.getAllFields(clazz);
		Field field = null;
		for (int i = 0; i < fields.length; i++){
			field = fields[i];
			//做大小写敏感的匹配,这个地方,以后再做考虑.
			if (field.getName().equals(fieldName)){
				return field;
			}
		}
		return null;
	}
	
	/**
	 * 缓存类属性
	 * @param clazz
	 * @param field
	 */
	private static void setCacheField(Class clazz, Field field){
		Map cache = (Map) classFieldsCache.get();
		if (cache == null){
			cache = new HashMap();
			classFieldsCache.set(cache);
		}
		
		cache.put(clazz.getName()+"."+field.getName(), field);
	}
	
	/**
	 * 读取缓存属性
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	private static Field getCacheField(Class clazz, String fieldName){
		Map cache = (Map) classFieldsCache.get();
		if (cache == null)
			return null;
		
		return (Field) cache.get(clazz.getName()+"."+fieldName);
	}
}
