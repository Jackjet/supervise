package gov.df.fap.util;

import gov.df.fap.util.xml.XMLData;

import java.io.File;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通用工具包
 * 
 * @version 1.0
 * @author justin
 * @since java 1.6
 */
public class Tools {

  public static boolean SYSTEM_OUT_ENABLED = true;

  public Tools() {
  }

  /**
   * 将数组转换为字符串,eg: 1,2,3
   * 
   * @param strArr
   *            数祖对象
   * @return String 转换后的字符串
   */
  public static String arrToString(String[] strArr) {
    if (strArr == null || strArr.length == 0)
      return "";
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < strArr.length; i++) {
      buffer.append(strArr[i]);
      if (i != strArr.length - 1)
        buffer.append(",");
    }
    return buffer.toString();
  }

  /**
   * 将数组转换为字符串,eg: '1','2','3'
   * 
   * @param strArr
   *            数祖对象
   * @return String 转换后的字符串
   */
  public static String arrToSqlString(String[] strArr) {
    if (strArr == null || strArr.length == 0)
      return "";
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < strArr.length; i++) {
      buffer.append("'");
      buffer.append(strArr[i]);
      buffer.append("'");
      if (i != strArr.length - 1)
        buffer.append(",");
    }
    return buffer.toString();
  }

  /**
   * 将数组转换为字符串,eg: 1,2,3
   * 
   * @param strArr
   *            数祖对象
   * @return String 转换后的字符串
   */
  public static String arrToSqllong(long[] strArr) {
    if (strArr == null || strArr.length == 0)
      return "";
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < strArr.length; i++) {
      buffer.append(strArr[i]);
      if (i != strArr.length - 1)
        buffer.append(",");
    }
    return buffer.toString();
  }

  /**
   * 将字符串1;2;3转换为字符串,eg: '1','2','3'
   * 
   * @param strArr
   *            字符串对象
   * @return String 转换后的字符串
   */
  public static String strToSqlString(String str) {
    if (str == null || str.equals(""))
      return "";
    String[] strArr = str.split(";");
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < strArr.length; i++) {
      buffer.append("'");
      buffer.append(strArr[i]);
      buffer.append("'");
      if (i != strArr.length - 1)
        buffer.append(",");
    }
    return buffer.toString();
  }

  /**
   * num1除以num2,显示小数点后4位,逢.5进一
   * 
   * @param num1
   *            除数
   * @param num2
   *            被除数
   * @return String 结果
   */
  public static String DivToPercent(BigDecimal num1, BigDecimal num2) {
    String strTmp = "";
    strTmp = String.valueOf(100 * (num1.divide(num2, 4, BigDecimal.ROUND_HALF_UP).doubleValue()));
    if (strTmp.length() > 5) {
      strTmp = strTmp.substring(0, 5);
    }
    strTmp = strTmp + "%";
    return strTmp;
  }

  /**
   * 从大写转换到小写
   * 
   * @param name
   *            字符串
   * @return String 转换小写后的字符串
   */
  public static String sql2javaName(String name) {
    String column = "";
    for (int i = 0; i < name.length(); i++) {
      column += String.valueOf(name.charAt(i)).toLowerCase();
    }
    return column;
  }

  /**
   * md5加密
   * 
   * @param s
   *            传入需要加密的字符串
   * @return 加密后字符串
   */
  public static String turn2MD5(String s) {
    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(s.trim().getBytes());
      byte[] b = md5.digest();
      return byte2hex(b);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return "";
  }

  /**
   * 字节转换成十六进制字符串
   * 
   * @param b
   *            字节数组
   * @return String 转换后的字符串
   */
  private static String byte2hex(byte[] b) {
    String hs = "";
    String stmp = "";
    for (int n = 0; n < b.length; n++) {
      stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
      if (stmp.length() == 1) {
        hs = hs + "0" + stmp;
      } else {
        hs = hs + stmp;
      }
    }
    return hs;
  }

  /**
   * 用strTo替换strSource中的第一个strForm字段
   * 
   * @param strSource
   *            源字符串
   * @param strFrom
   *            需要替换的子字符串
   * @param strTo
   *            替换字符串
   * @return String 替换后的字符串
   */
  public static String replaceFirst(String strSource, String strFrom, String strTo) {
    if (strSource == null) {
      return null;
    }
    int i = 0;
    if ((i = strSource.indexOf(strFrom, i)) >= 0) {
      char[] cSrc = strSource.toCharArray();
      char[] cTo = strTo.toCharArray();
      int len = strFrom.length();
      StringBuffer buf = new StringBuffer(cSrc.length);
      buf.append(cSrc, 0, i).append(cTo);
      i += len;
      int j = i;
      while ((i = strSource.indexOf(strFrom, i)) > 0) {
        buf.append(cSrc, j, i - j).append(cTo);
        i += len;
        j = i;
      }
      buf.append(cSrc, j, cSrc.length - j);
      return buf.toString();
    }
    return strSource;
  }

  /**
   * Compares the two specified <code>double</code> values. The sign of the
   * integer value returned is the same as that of the integer that would be
   * returned by the call:
   * 
   * @param d1
   *            the first <code>double</code> to compare
   * @param d2
   *            the second <code>double</code> to compare
   * @return the value <code>0</code> if <code>d1</code> is numerically equal
   *         to <code>d2</code>; a value less than <code>0</code> if
   *         <code>d1</code> is numerically less than <code>d2</code>; and a
   *         value greater than <code>0</code> if <code>d1</code> is
   *         numerically greater than <code>d2</code>.
   * @since 1.4
   */

  public static int compareDouble(double d1, double d2) {
    if (d1 > d2 + 0.00001) {
      return 1;
    }
    if (d1 + 0.00001 < d2) {
      return -1;
    }
    return 0;
  }

  /**
   * 三个数相减，把double转换成BigDecimal进行计算，提高精确度
   * 
   * @param numOne
   *            减数
   * @param numTwo
   *            被减数
   * @param numThree
   *            被减数
   * @return 相减的结果
   */
  public static BigDecimal doubleThreeSwitchBigDecimal(double numOne, double numTwo, double numThree) {
    BigDecimal bone = new BigDecimal(String.valueOf(numOne));
    BigDecimal btwo = new BigDecimal(String.valueOf(numTwo));
    BigDecimal bthree = new BigDecimal(String.valueOf(numThree));
    return bone.add(btwo.negate()).add(bthree.negate());
  }

  /**
   * 两个数相减。把double转换成BigDecimal进行计算，提高精确度
   * 
   * @param numOne
   *            减数
   * @param numTwo
   *            被减数
   * @return 相减的结果
   */
  public static BigDecimal doubleTwoSwitchBigDecimal(double numOne, double numTwo) {
    BigDecimal bone = new BigDecimal(String.valueOf(numOne));
    BigDecimal btwo = new BigDecimal(String.valueOf(numTwo));
    return bone.add(btwo.negate());
  }

  /**
   * 四个数相减，把double转换成BigDecimal进行计算，提高精确度
   * 
   * @param numOne
   *            减数
   * @param numTwo
   *            被减数
   * @param numThree
   *            被减数
   * @param numFour
   *            被减数
   * @return 相减的结果
   */
  public static BigDecimal doubleFourSwitchBigDecimal(double numOne, double numTwo, double numThree, double numFour) {
    BigDecimal bone = new BigDecimal(String.valueOf(numOne));
    BigDecimal btwo = new BigDecimal(String.valueOf(numTwo));
    BigDecimal bthree = new BigDecimal(String.valueOf(numThree));
    BigDecimal bfour = new BigDecimal(String.valueOf(numFour));
    return bone.add(btwo.negate()).add(bthree.negate()).add(bfour.negate());
  }

  /**
   * 将Map转换为XMLData,仅适用于单条记录数据转换
   * 
   * @param m
   * @return
   */
  public static XMLData parseMap2XMLData(Map m) {
    if (m != null) {
      XMLData xmlData = new XMLData();
      Object[] objs = m.keySet().toArray();
      for (int j = 0; j < objs.length; j++) {
        xmlData.put(objs[j], m.get(objs[j]));
      }
      return xmlData;
    }
    return null;
  }

  public static String addDbLink() {
    return "";

  }

  /**
   * 取本地日期时间.
   * 
   * @return 格式化后的日期时间字符
   */
  public static String getCurrDate() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    return dateFormat.format(date);
  }

  /**
   * 比较两个时间大小
   * 
   * @param fromTime
   *            yyyy-mm-dd HH:mm:ss
   * @param toTime
   *            yyyy-mm-dd HH:mm:ss
   * @return 后者大于等于返回1，前者大返回-1
   */
  public static int compareDateTime(String fromTime, String toTime) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
    try {
      long from = sdf.parse(fromTime).getTime();
      long to = sdf.parse(toTime).getTime();
      long res = to - from;
      if (res >= 0)
        return 1;
      else
        return -1;
    } catch (Exception ex) {
      ex.printStackTrace();
      return -1;
    }
  }

  public static String TimeAfterNonWorkDays(String curTime, int span, int intervalType) throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date curDate = sdf.parse(curTime);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(curDate);
    if (intervalType == 0)
      calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + span);
    if (intervalType == 1)
      calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + span);
    if (intervalType == 2)
      calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + span);
    if (intervalType == 3)
      calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + span);
    String nextTime = sdf.format(calendar.getTime());
    return nextTime;
  }

  public static void main(String[] args) throws Exception {

  }

  /**
   * @author lwq
   * @date Mar 8, 2013 12:33:27 PM
   * 
   */
  public static String getCurrYMD() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();
    return dateFormat.format(date);
  }

  /**
   * 计算间隔某段时间后的时间 <strong>Description:</strong><br>
   * span为天<br>
   * <br>
   * 
   * @author fengdz
   * @date Mar 8, 2013 12:33:27 PM
   * 
   */
  public static String timeSpanAfterDays(String startTime, int span) throws Exception {

    if (startTime == null || startTime.equals(""))
      return "bad starttime";
    else {

      Calendar calendar = Calendar.getInstance();
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

      calendar.setTime(df.parse(startTime));

      String year = startTime.substring(0, 4);
      String month = startTime.substring(5, 7);
      String day = startTime.substring(8, 10);
      calendar.set(Calendar.YEAR, Integer.parseInt(year));
      calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
      calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day) + span);
      return df.format(calendar.getTime());

    }

  }

  /**
   * 根据两个日期查看相差几天 "2013-03-10 16:26:10", "2013-03-10 18:26:10", "yyyy-MM-dd"
   * 只返回大于等于1天或者0天，不考虑小时、分、秒 author fengdz
   * 
   * @param startTime
   * @param endTime
   * @param format
   */
  public static long spanDaysByDate(String startTime, String endTime, String format) {
    long days = 0;
    SimpleDateFormat sd = new SimpleDateFormat(format);
    long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
    long diff = 0;
    // 获得两个时间的毫秒时间差异
    try {
      diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    days = diff / nd;// 计算差多少天

    return days;
  }

  /**
   * 
   * <strong>Description:</strong><br>
   * <br>
   * 
   * @author fengdz
   * @date Mar 11, 2013 9:23:21 AM
   * 
   */
  public static int spanDaysByDateTime(String startTime, String endTime) {
    long startT = fromDateStringToLong(startTime);
    long endT = fromDateStringToLong(endTime);
    long ss = (endT - startT) / (1000); // 共计秒数
     int MM = (int) ss / 60; // 共计分钟数
    //int hh = (int) ss / 3600; // 共计小时数
    //int dd = (int) hh / 24; // 共计天数
    // System.out.println("共" + dd + "天 准确时间是：" + hh + " 小时 " + MM + " 分钟"
    // + ss + " 秒 共计：" + ss * 1000 + " 毫秒");
    return MM;
  }

  /**
   * 
   * <strong>Description:</strong><br>
   * <br>
   * 
   * @author fengdz
   * @date Mar 11, 2013 9:23:21 AM
   * 
   */

  private static long fromDateStringToLong(String inVal) { // 此方法计算时间毫秒
    Date date = null; // 定义时间类型
    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    try {
      date = inputFormat.parse(inVal); // 将字符型转换成日期型
    } catch (Exception e) {
      e.printStackTrace();
    }
    return date.getTime(); // 返回毫秒数
  }

  /**
   * 根据参数个数，构建inSql，解决in中最大1000条问题  add by lirf at 2013.8.16
   * @param String tableName （表名）
   * @param String colName （字段名）
   * @param List list（参数集合<String>）
   * @param String type （构建类型 1：预编译型， 2：非预编译型）
   * @return String
   */
  public static String getInSql(String tableName, String colName, List list, String type) {
    String aliases = tableName + "." + colName;
    int limit = 1000;//in 中最个数
    if (list == null || list.size() == 0)
      return aliases + " in('')";
    int size = list.size();
    StringBuffer inSql = new StringBuffer();
    int flag = 0;//一千个标识
    int maxIndex = size - 1;
    inSql.append(aliases).append(" in (");
    if ("1".equals(type)) {//预编译型
      for (int i = 0; i < size; i++) {
        flag++;
        if (flag < limit && i != maxIndex) {
          inSql.append("?,");
        } else if (flag == limit && i != maxIndex) {
          flag = 0;
          inSql.append("?) or ");
          inSql.append(aliases);
          inSql.append(" in (");
        } else {
          inSql.append("?");
        }
      }
      inSql.append(") ");
    } else {//非预编译型
      for (int i = 0; i < size; i++) {
        flag++;
        if (flag < limit && i != maxIndex) {
          inSql.append("'");
          inSql.append((String) list.get(i));
          inSql.append("',");
        } else if (flag == limit && i != maxIndex) {
          flag = 0;
          inSql.append("'");
          inSql.append((String) list.get(i));
          inSql.append("'");
          inSql.append(") or ");
          inSql.append(aliases);
          inSql.append(" in (");
        } else {
          inSql.append("'");
          inSql.append((String) list.get(i));
          inSql.append("'");
        }
      }
      inSql.append(") ");
    }
    return inSql.toString();
  }

  /**
   * 根据字符串位数，前面补零
   * @return
   * @author liwenquan
   */
  public static String getStrByLen(int len, String var) {
    String str = "";
    if (len == 0 || var == null || var.equals(""))
      return "0";
    if (var.length() > len)
      return var;
    int m = len - var.length();
    str = var;
    for (int k = 0; k < m; k++) {
      str = "0" + str;
    }
    return str;
  }

  /**
   * 提供域目录的获取方法 
   * @return
   */
  public static String getDomainUrl() {
    String s = System.getProperty("user.dir");
    if (!(s.endsWith("/") || s.endsWith("\\")))
      s = s + File.separator;
    return s;
  }

  /**
   * 提供NULL转换成空字符串，不为NULL则去掉两边空字符串
   * @param str
   * @return
   */
  public static String convertStrNullToEmpty(Object str) {
    return (str == null ? "" : str.toString().trim());
  }

}