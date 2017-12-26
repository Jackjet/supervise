package gov.df.fap.util;

import gov.df.fap.util.xml.XMLData;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 
 * <p>
 * Title: Map/DTO对象读写类
 * </p>
 * <p>
 * Description:读写Map/DTO对象的键值/属性值
 * </p>
 * 
 * @since java 1.4.2
 * 
 */
public class MapBeanWrapper {

  /**
   * 根据键值取得对应的从Map取得对应的键值 根据属性名取得对应DTO对象的属性值
   * 
   * @param obj
   *            Map/DTO对象
   * @param propertyName
   *            键值/属性名
   * @return 键值/属性值
   */
  public static Object getPropertyValue(Object obj, String propertyName) {
    Object objResult = null;
    if (obj instanceof Map) {

      Map objMap = (Map) obj;
      objResult = objMap.get(propertyName);
    } else {
      try {
        objResult = PropertyUtils.getProperty(obj, propertyName);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return objResult;
  }

  /**
   * 根据键值取得对应的从Map取得对应的键值，并转化为字符串 根据属性名取得对应DTO对象的属性值，并转化为字符串
   * 
   * @param obj
   *            Map/DTO对象
   * @param propertyName
   *            键值/属性名
   * @return 键值/属性值（字符串类型）
   */
  public static String getPropertyValueAsString(Object obj, String propertyName) {
    String ret = "";
    Object objResult = getPropertyValue(obj, propertyName);
    if (objResult != null) {
      ret = objResult.toString();
    }
    return ret;
  }

  /**
   * 设置Map/DTO对应的键值
   * 
   * @param obj
   *            Map/DTO对象
   * @param propertyName
   *            键值/属性名
   * @param value
   *            键值/属性值
   */
  public static void setPropertyValue(Object obj, String propertyName, Object value) {
    if (obj instanceof Map) {
      Map objMap = (Map) obj;
      objMap.put(propertyName, value);
    } else {
      try {
        PropertyUtils.setProperty(obj, propertyName, value);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }

  /**
   * 将DTO对象转换为对应的XMLData
   * 
   * @param obj
   *            DTO对象
   * @return XMLData对象
   */
  public static XMLData changeObj2XMLData(Object obj) {

    XMLData xmlData = new XMLData();
    if (obj == null)
      return null;
    if (obj instanceof XMLData) {
      xmlData = (XMLData) obj;
    } else if (obj instanceof Map) {
      xmlData.putAll((Map) obj);
    } else {
      try {
        xmlData.putAll(PropertyUtils.describe(obj));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return xmlData;

  }

}
