package gov.df.fap.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class BeanUtils {

  public static Object populate(Map newData) throws Exception {
    String table = (String) newData.get("table");
    String tableClassName = null;
    boolean isRemoveTableName = true;
    if (table == null || table.equals("") || !table.startsWith("mappingfiles")) {
      tableClassName = (String) newData.get("table_name");
    } else {
      tableClassName = table;
      isRemoveTableName = false;
    }

    Class className = Class.forName(tableClassName);
    Method methods[] = className.getDeclaredMethods();
    Object obj = (Object) Class.forName(tableClassName).newInstance();
    if (isRemoveTableName == true)
      newData.remove("table_name");
    Method setMethod = null;
    Iterator it = newData.keySet().iterator();
    while (it.hasNext()) {
      String key = (String) it.next();
      Object value = (Object) newData.get(key);
      for (int i = 0; i < methods.length; i++) {
        if (methods[i].getName().equalsIgnoreCase("set" + key)) {
          Object[] param = new Object[1];
          Class[] paraClasses = methods[i].getParameterTypes();
          if (paraClasses[0].getName().equals("java.lang.Float") || paraClasses[0].getName().equals("float")) {
            if (value == null || value.toString().equals("") || value.toString().equals("null"))
              param[0] = null;
            else {
              Float f = new Float((String) value.toString());
              param[0] = f;
            }
          } else if (paraClasses[0].getName().equals("java.lang.Double") || paraClasses[0].getName().equals("double")) {
            if (value == null || value.toString().equals("") || value.toString().equals("null"))
              param[0] = null;
            else {
              Double d = new Double((String) value.toString());
              param[0] = d;
            }

          } else if (paraClasses[0].getName().equals("java.lang.Boolean") || paraClasses[0].getName().equals("boolean")) {
            if (value == null || value.toString().equals("") || value.toString().equals("null"))
              param[0] = null;
            else {
              Boolean b = new Boolean((String) value.toString());
              param[0] = b;
            }

          } else if (paraClasses[0].getName().equals("java.lang.Byte") || paraClasses[0].getName().equals("byte")) {
            if (value == null || value.toString().equals("") || value.toString().equals("null"))
              param[0] = null;
            else {
              Byte b = new Byte((String) value.toString());
              param[0] = b;
            }

          } else if (paraClasses[0].getName().equals("java.lang.Short") || paraClasses[0].getName().equals("short")) {
            if (value == null || value.toString().equals("") || value.toString().equals("null"))
              param[0] = null;
            else {
              Short s = new Short((String) value.toString());
              param[0] = s;
            }

          } else if (paraClasses[0].getName().equals("java.lang.Integer") || paraClasses[0].getName().equals("int")) {
            if (value == null || value.toString().equals("") || value.toString().equals("null"))
              param[0] = null;
            else {
              Integer in = new Integer((String) value.toString());
              param[0] = in;
            }

          } else if (paraClasses[0].getName().equals("java.lang.Long") || paraClasses[0].getName().equals("long")) {
            if (value == null || value.toString().equals("") || value.toString().equals("null"))
              param[0] = null;
            else {
              Long l = new Long((String) value.toString());
              param[0] = l;
            }

          } else if (paraClasses[0].getName().equals("java.math.BigDecimal")
            || paraClasses[0].getName().equals("BigDecimal")) {
            if (value == null || value.toString().equals("") || value.toString().equals("null"))
              param[0] = null;
            else {
              BigDecimal bd = new BigDecimal((String) value.toString());
              param[0] = bd;
            }

          } else if (paraClasses[0].getName().equals("java.math.BigInteger")
            || paraClasses[0].getName().equals("BigInteger")) {
            if (value == null || value.toString().equals("") || value.toString().equals("null"))
              param[0] = null;
            else {
              BigInteger bi = new BigInteger((String) value.toString());
              param[0] = bi;
            }

          } else if (paraClasses[0].getName().equals("java.util.Date")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            if (value == null)
              param[0] = new Date();
            else {
              param[0] = sdf.parse(value.toString().substring(0, value.toString().indexOf(".")));
            }

          } else {
            param[0] = value;
          }
          setMethod = methods[i];
          setMethod.invoke(obj, param);
          break;
        }
      }
    }
    return obj;
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO 自动生成方法存根

  }

}
