package gov.df.fap.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * 带有一些反射相关的方法的帮助类
 * @author Su Xianglin
 */
public class ReflectionUtil {

  /**
   * 根据传入的对象和参数反射进行调用
   * @param invoker
   * @param methodName
   * @param parameter
   * @param obj
   * @param args
   * @return
   * @throws NoSuchMethodException 
   * @throws SecurityException 
   * @throws InvocationTargetException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   * @throws Exception 
   */
  public static Object invokeMethod(Object invoker, String methodName, Class[] parameter, Object[] args)
    throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
    InvocationTargetException {
    Method method = invoker.getClass().getDeclaredMethod(methodName, parameter);
    return invokeMethod(invoker, method, args);
  }

  /**
   * 根据传入的对象和参数反射进行调用
   * @param invoker
   * @param methodName
   * @param parameter
   * @param obj
   * @param args
   * @return
   * @throws InvocationTargetException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   */
  public static Object invokeMethod(Object invoker, Method method, Object[] args) throws IllegalArgumentException,
    IllegalAccessException, InvocationTargetException {
    return method.invoke(invoker, args);
  }

  /**
   * 根据传入对象、属性名、属性值，调用传入对象的setXX方法把属性值赋值到属性去。支持父类属性的赋值
   * 例如对象user有个name的属性，有个setName的方法，要复制Lisa到name中，则调用invokeSetMethod(user,name,new String[]{Lisa})
   * @param src 
   * @param fieldName
   * @param params
   * @throws IntrospectionException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws NoSuchFieldException 
   * @throws SecurityException 
   */
  public static void invokeSetMethod(Object src, String fieldName, Object[] params) throws IntrospectionException,
    IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException,
    NoSuchFieldException {
    PropertyDescriptor pd = new PropertyDescriptor(fieldName, src.getClass());
    if (params != null)
      for (int i = 0; i < params.length; i++) {
        params[i] = getMatchObject(params[i], pd.getPropertyType());
      }
    pd.getWriteMethod().invoke(src, params);
  }

  /**
   * 根据类型转换对象
   * @param src 需转换的对象
   * @param classType 类型
   * @return
   */
  private static Object getMatchObject(Object src, Class classType) {
    if (classType == int.class || classType == Integer.class) {
      if (src == null) {
        return Integer.decode("0");
      }
      return Integer.decode(String.valueOf(src));
    } else if (classType == double.class || classType == Double.class) {
      return Double.valueOf(String.valueOf(src));
    } else if (classType == boolean.class || classType == Boolean.class) {
      return Boolean.valueOf(String.valueOf(src));
    } else if (classType == BigDecimal.class) {
      return new BigDecimal(String.valueOf(src));
    } else {

      return src;
    }
  }

  /**
   * 根据传入对象、属性名、参数值，调用传入对象的getXX方法获取属性名的值。支持父类属性的获取。如果getXX方法没有参数，则params赋值为空即可。
   * @param src
   * @param fieldName
   * @param params
   * @return
   * @throws IntrospectionException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  public static Object invokeGetMethod(Object src, String fieldName, Object[] params) throws IntrospectionException,
    IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    PropertyDescriptor pd = new PropertyDescriptor(fieldName, src.getClass());
    return pd.getReadMethod().invoke(src, params);
  }

}
