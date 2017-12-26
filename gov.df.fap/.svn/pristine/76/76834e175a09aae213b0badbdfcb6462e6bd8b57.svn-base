package gov.df.fap.util.Properties;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存类信息.
 * @author 
 *
 */
public class ClassInfo {
	
	Class clazz = null;
	
	String[] writeableProps = null;
	String[] readableProps = null;
	
	Map getterMethod = new HashMap();
	Map setterMethod = new HashMap();
	
	Map setterType = new HashMap();
	
	private static final Map classInfoMap = new HashMap();
	
	public ClassInfo(Class clazz){
		initialized(clazz);
	}
	
	private void initialized(Class clazz){
		this.clazz = clazz;
		initMehtods(clazz);
		writeableProps = (String[]) setterMethod.keySet().toArray(new String[setterMethod.size()]);
		readableProps = (String[]) getterMethod.keySet().toArray(new String[getterMethod.size()]);
	}
	
	/**
	 * initialized class methods.
	 * @param clazz
	 */
	private void initMehtods(Class clazz){
		Method[] methods = clazz.getMethods();
		
		for (int i = 0; i < methods.length; i++){
			addMethod(methods[i]);
		}
	}
	
	/**
	 * Add method.
	 * @param method method to add.
	 */
	public void addMethod(Method method){
		String methodName = method.getName();
		if (methodName.startsWith("set") && method.getParameterTypes().length == 1 && methodName.length() > 3){
			addSetterMethod(method);
		}else if (methodName.startsWith("get") && method.getParameterTypes().length == 0 && methodName.length() > 3){
			addGetterMethod(method);
		}else if (methodName.startsWith("is") && method.getParameterTypes().length == 0 && methodName.length() > 2){
			addGetterMethod(method);
		}
	}
	
	/**
	 * Get property name with Java Specification.
	 * @param propMethodName property setter/getter method name.
	 * @return property name
	 */
	private String getCasePropName(String propMethodName){
		String propName = null;
		if (propMethodName.startsWith("is")){
			propName = propMethodName.substring(2);
		}else if (propMethodName.startsWith("get") || propMethodName.startsWith("set")){
			propName = propMethodName.substring(3);
		}else{
			throw new RuntimeException("Exception when parsing property "+propMethodName+", must starts with is/get/set");
		}
		
		if (propName.length() == 1 || (propName.length() > 1 && !Character.isUpperCase(propName.charAt(1)))) {
			propName = propName.substring(0, 1).toLowerCase() + propName.substring(1);
		}
		
		return propName;
	}
	
	/**
	 * 
	 * @param method
	 */
	private void addGetterMethod(Method method) {
		String prop = getCasePropName(method.getName());
		if (!getterMethod.containsKey(prop)){
			getterMethod.put(prop, method);
		}
	}
	
	/**
	 * 
	 * @param method
	 */
	private void addSetterMethod(Method method) {
		String prop = getCasePropName(method.getName());
		if (!setterMethod.containsKey(prop)){
			setterMethod.put(prop, method);
			setterType.put(prop, method.getParameterTypes()[0]);
		}else{
			throw new RuntimeException("Illegal overload setter method for property:"+prop+" in "+method.getDeclaringClass());
		}
	}
	
	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	public Method getSetterMethod(String propertyName){
		Method setter = (Method) setterMethod.get(propertyName);
		
		if (setter == null)
			throw new RuntimeException("There is no setter method of Property "+propertyName+" in "+clazz);
		return setter;
	}
	
	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	public Method getGetterMethod(String propertyName){
		Method getter = (Method) getterMethod.get(propertyName);
		
		if (getter == null)
			throw new RuntimeException("There is no getter method of Property "+propertyName+" in "+clazz);
		return getter;
	}
	
	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	public Class getSetterType(String propertyName){
		Class type = (Class) setterType.get(propertyName);
		if (type == null)
			throw new RuntimeException("There is no not has setter type of Property "+propertyName+" in "+clazz);
		
		return type;
	}
	
	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	public Class getReturnType(String propertyName){
		Method type = (Method) getterMethod.get(propertyName);
		if (type == null)
			throw new RuntimeException("There is no not has getter type of Property "+propertyName+" in "+clazz);
		
		return type.getReturnType();
	}
	
	/**
	 * Object factory.
	 * @param clazz
	 * @return
	 */
	public static ClassInfo classInfoFactory(Class clazz){
		
		ClassInfo classInfo = (ClassInfo)classInfoMap.get(clazz);
		if (classInfo == null)
			classInfo = new ClassInfo(clazz);
		classInfoMap.put(clazz, classInfo);
		return classInfo;
	} 
	
	public String[] getWriteableProps(){
		return writeableProps;
	}
	
	public String[] getReadableProps(){
		return readableProps;
	}
	
	public String getClassName(){
		return clazz.getName();
	}
	
	public Class getInfoClass(){
		return clazz;
	}
}
