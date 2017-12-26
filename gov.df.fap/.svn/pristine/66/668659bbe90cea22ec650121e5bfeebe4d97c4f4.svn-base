package gov.df.fap.util.Properties;

import gov.df.fap.util.number.NumberUtil;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.collections.FastHashMap;

/**
 * Supplies all generaic type transfer operations.
 * @author 
 * @version 
 */
public class ObjectTransferWrapper {

  public static final Map converterCache = new FastHashMap();

  static {
    converterCache.put(Integer.class, new ToIntConverter());
    converterCache.put(int.class, new ToIntConverter());
    converterCache.put(Double.class, new ToDoubleConverter());
    converterCache.put(double.class, new ToDoubleConverter());
    converterCache.put(Long.class, new ToLongConverter());
    converterCache.put(long.class, new ToLongConverter());
    converterCache.put(String.class, new ToStringConverter());
    converterCache.put(Character.class, new ToCharConverter());
    converterCache.put(char.class, new ToCharConverter());
    converterCache.put(Short.class, new ToShortConverter());
    converterCache.put(short.class, new ToShortConverter());
    converterCache.put(Float.class, new ToFloatConverter());
    converterCache.put(float.class, new ToFloatConverter());
    converterCache.put(BigDecimal.class, new ToBigDecimalConverter());
  }

  /**
   * 取数据转换器
   * @param clazz
   * @return
   */
  private ObjectTransferer getTransferer(Class clazz) {
    if (converterCache.get(clazz) == null)
      throw new IllegalArgumentException("did not have the converter of Class:" + clazz.getName());

    return (ObjectTransferer) converterCache.get(clazz);
  }

  /**
   * 转换数据类型
   * @param clazz
   * @param value
   * @return
   */
  public Object converteValue(Class clazz, Object value) {
    return getTransferer(clazz).converteObject(value);
  }

  /**
   * 整数转换
   * @author 
   *
   */
  static final class ToIntConverter implements ObjectTransferer {

    public Object converteObject(Object srcObject) {

      if (srcObject == null)
        return new Integer(0);
      else
        return new Integer(srcObject.toString().trim());
    }

  }

  static final class ToBigDecimalConverter implements ObjectTransferer {

    public Object converteObject(Object value) {
      if (value == null)
        return new BigDecimal(0);
      else
        return new BigDecimal(value.toString());
    }

  }

  static final class ToDoubleConverter implements ObjectTransferer {

    public Object converteObject(Object value) {

      if (value == null)
        return new Double(0D);
      else
        return new Double(value.toString().trim());
    }

  }

  /**
   * 字串转换
   * @author 
   * @version 2017-03-24
   */
  static final class ToStringConverter implements ObjectTransferer {

    public Object converteObject(Object srcObject) {
      return srcObject == null ? null : srcObject.toString();
    }

  }

  static final class ToLongConverter implements ObjectTransferer {

    public Object converteObject(Object value) {
      return value == null ? NumberUtil.LONG_ZERO : new Long(value.toString());
    }

  }

  static final class ToCharConverter implements ObjectTransferer {

    public Object converteObject(Object value) {
      if (value == null)
        return null;
      else if (value.toString().length() > 1)
        throw new RuntimeException("Exception when result value formated, value too much large:" + value
          + " as character");
      return value == null ? null : new Character(value.toString().charAt(0));
    }
  }

  static final class ToShortConverter implements ObjectTransferer {

    public Object converteObject(Object value) {
      return value == null ? NumberUtil.SHORT_ZERO : new Short(value.toString());
    }
  }

  static final class ToFloatConverter implements ObjectTransferer {

    public Object converteObject(Object value) {
      return value == null ? NumberUtil.FLOAT_ZERO : new Float(value.toString());
    }
  }

}
