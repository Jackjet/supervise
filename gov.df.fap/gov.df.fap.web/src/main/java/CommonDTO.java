import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.longtu.framework.dto.ICommonDTO;
import com.longtu.framework.exception.AppException;
import com.longtu.framework.util.CRC16;
import com.longtu.framework.util.StringUtil;

public class CommonDTO extends HashMap implements Serializable, Cloneable, ICommonDTO {
  private static final long serialVersionUID = 1L;

  protected static final String GUID = "guid";

  protected static final String ORDERNO = "orderno";

  protected static final String CREATETIME = "createtime";

  private boolean isReadOnly = false;

  private static String TRUE = "true";

  private static String TRUEINT = "1";

  private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");

  void isReadOnly(boolean readOnly) {
    this.isReadOnly = readOnly;
  }

  public CommonDTO() {
  }

  public CommonDTO(Map map) {
    putAll(map);
  }

  public String loadSysGuid() {
    String guid = StringUtil.createUUID();
    put("guid", guid);
    return guid;
  }

  public String loadSysGuid8() throws AppException {
    String guid = StringUtil.createUUID8();
    put("guid", guid);
    return guid;
  }

  public String loadSysGuidByCRC() throws AppException {
    String guid = StringUtil.createUUID();
    put("guid", Long.valueOf(new CRC16(guid.getBytes()).getValue()));
    return getGuid();
  }

  public String getCreatetime() {
    return getString("createtime");
  }

  public String getGuid() {
    return getString("guid");
  }

  public int getOrderno() {
    return getInt("orderno").intValue();
  }

  public void setCreatetime(String createtime) {
    put("createtime", createtime);
  }

  public void setCreatetime() {
    put("createtime", StringUtil.getSysDBDate());
  }

  public void setGuid(String guid) {
    put("guid", guid);
  }

  public void setOrderno(int orderno) {
    put("orderno", Integer.valueOf(orderno));
  }

  public Boolean getBoolean(String key) {
    Object o = get(key);
    if (o instanceof Boolean)
      return ((Boolean) o);
    if (o instanceof Integer) {
      if (((Integer) o).intValue() == 1) {
        return Boolean.TRUE;
      }
      return Boolean.FALSE;
    }
    if (o instanceof String) {
      String so = (String) o;
      if ((TRUE.equalsIgnoreCase(so)) || (TRUEINT.equals(so))) {
        return Boolean.TRUE;
      }
      return Boolean.FALSE;
    }
    if (o instanceof BigDecimal) {
      if (((BigDecimal) o).intValue() == 1) {
        return Boolean.TRUE;
      }
      return Boolean.FALSE;
    }

    return Boolean.FALSE;
  }

  public void setBoolean(String key, Object value) {
    if (value == null) {
      put(key, Integer.valueOf(0));
      return;
    }
    if (value.toString().equals("1")) {
      put(key, Integer.valueOf(1));
      return;
    }
    if (value.toString().toLowerCase().equals("true")) {
      put(key, Integer.valueOf(1));
      return;
    }
    put(key, Integer.valueOf(0));
  }

  public Double getDouble(String key) {
    Object v = get(key);
    if (v == null)
      return null;
    if (v instanceof Number)
      return Double.valueOf(((Number) v).doubleValue());
    if (v instanceof Character)
      return Double.valueOf(((Character) v).charValue());
    if (v instanceof String) {
      return Double.valueOf((String) v);
    }
    return null;
  }

  public Integer getInt(String key) {
    Object v = get(key);
    if (v == null)
      return Integer.valueOf(0);
    if (v instanceof Number)
      return new Integer(((Number) v).intValue());
    if (v instanceof Character)
      return new Integer(((Character) v).charValue());
    if (v instanceof String) {
      return Integer.valueOf((String) v);
    }
    return Integer.valueOf(0);
  }

  public Long getLong(String key) {
    Object v = get(key);
    if (v == null)
      return Long.valueOf(0L);
    if (v instanceof Number)
      return Long.valueOf(((Number) v).longValue());
    if (v instanceof Character)
      return Long.valueOf(((Character) v).charValue());
    if (v instanceof String) {
      return Long.valueOf((String) v);
    }
    return Long.valueOf(0L);
  }

  public String getString(String key) {
    return getString(key, null);
  }

  public String getString(String key, String value) {
    Object v = get(key);
    if (v == null) {
      return value;
    }
    return v.toString();
  }

  public Date getDate(String key) {
    String date = (String) get(key);
    if (date == null)
      return null;
    try {
      return DATEFORMAT.parse(date);
    } catch (ParseException localParseException) {
    }
    return null;
  }

  public Object get(String key) {
    return super.get(key.toLowerCase());
  }

  public Object put(String key, Object value) {
    if (value instanceof Date) {
      value = DATEFORMAT.format(value);
    }

    return super.put(key.toLowerCase(), value);
  }

  public ICommonDTO parseDTO(Class<? extends ICommonDTO> clazz) throws AppException {
    try {
      ICommonDTO dto = (ICommonDTO) clazz.newInstance();
      dto.putAll(this);
      return dto;
    } catch (Exception e) {
      throw new AppException("", e, "");
    }
  }

  public void loadSysCreatetime() {
    setCreatetime(DATEFORMAT.format(new Date()));
  }

  public void clear() {
    super.clear();
  }

  public Object put(Object key, Object value) {
    return super.put(key, value);
  }

  public void putAll(Map m) {
    Set key = m.keySet();
    for (Iterator localIterator = key.iterator(); localIterator.hasNext();) {
      Object k = localIterator.next();
      put(k, m.get(k));
    }
  }

  public Object remove(Object key) {
    return super.remove(key);
  }

  public BigDecimal getBigDecimal(String key) {
    Object o = super.get(key.toLowerCase());
    if (o == null) {
      return null;
    }
    if (o instanceof BigDecimal) {
      return ((BigDecimal) o);
    }
    return new BigDecimal(o.toString());
  }
}