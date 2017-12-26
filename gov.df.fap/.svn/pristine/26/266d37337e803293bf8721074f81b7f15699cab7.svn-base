package gov.df.fap.util;

import gov.df.fap.util.md5.MD5;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.longtu.framework.util.RandomGUID;

/**
 * 字串处理工具
 * @author lwq
 *
 */
public class StringUtil extends StringUtils {

  public static final String ZERO = "0";

  public static final String ONE = "1";

  public static final String TWO = "2";

  public static final String BOOL_TRUE = "true";

  public static final String BOOL_FALSE = "false";

  protected static Random random = new Random();

  public static MD5 md5 = new MD5();

  protected static MessageDigest digest = null;
  
  public static String[] chars;

  static {
    try {
      digest = java.security.MessageDigest.getInstance("MD5");
      chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", 
        "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", 
        "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", 
        "U", "V", "W", "X", "Y", "Z" };
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  /**
   * 判断字符串是否为空
   * 
   * @param str 待判断字符串
   * @return true：空；false：非空
   */
  public static boolean isNull(String str) {
    return (str == null || str.trim().equalsIgnoreCase("") || str.trim().equalsIgnoreCase("null"));
  }

  /**
   * 判断字符串是否为空
   * @param obj 待判断对象
   * @return true：空；false：非空
   */
  public static boolean isNull(Object obj) {
    return (obj == null) || isNull(obj.toString());
  }

  public static String toString(Object value) {
    if (value != null) {
      return value.toString().trim();
    } else {
      return "";
    }
  }

  /**
    * s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
    * </pre></blockquote>
    * using <code>int</code> arithmetic, where <code>s[i]</code> is the
    * <i>i</i>th character of the string, <code>n</code> is the length of
    * the string, and <code>^</code> indicates exponentiation.
    * (The hash value of the empty string is zero.)
    * @param beHashed
    */
  public static long stringHash(String beHashed) {
    if (StringUtil.isEmpty(beHashed))
      return 0;
    else {
      int hash = 0;
      char[] strValue = beHashed.toCharArray();
      int len = strValue.length;
      for (int i = 0; i < len; i++)
        hash = hash * 31 + strValue[i];
      //a supplemental hash function
      hash += ~(hash << 9);
      hash ^= (hash >>> 14);
      hash += (hash << 4);
      hash ^= (hash >>> 10);
      return hash;
    }
  }

  public static String toStr(int intValue) {
    return String.valueOf(intValue);
  }

  public static String toStr(double d) {
    return String.valueOf(d);
  }

  public static String toStr(long d) {
    return String.valueOf(d);
  }

  /**
   * 判断是否字符串等于给定字符数组中的一个.
   * @param str
   * @param arg
   * @return
   */
  public static boolean equals(String str, String[] arg) {
    if (str == null || arg == null)
      return false;

    for (int i = 0; i < arg.length; i++) {
      if (str.equals(arg[i]))
        return true;
    }
    return false;
  }

  /**
   * 判断是否字符串等于给定字符数组中的一个,忽略大小写.
   * @param str
   * @param arg
   * @return
   */
  public static boolean equalsIgnoreCase(String str, String[] arg) {
    if (str == null || arg == null)
      return false;

    for (int i = 0; i < arg.length; i++) {
      if (str.equalsIgnoreCase(arg[i]))
        return true;
    }
    return false;
  }

  public static void main(String[] args) {
    System.out.println(Integer.toString(18, 9));
    System.out.println(new BigDecimal("1111111111111144444"));
    System.out.println(3 ^ 1);
    System.out.println(StringUtil.stringHash("hr3k4jrhk3j4lr-kj3jkrh3kjrl"));
    System.out.println(StringUtil.stringHash("hr3k4jrhk3j4lr"));
    System.out.println(StringUtil.stringHash("kj3jkrh3kjrl"));
    System.out.println("hr3k4jrhk3j4lr".hashCode());
    System.out.println("kj3jkrh3kjrl".hashCode());
    System.out.println(StringUtil.stringHash("hr3k4jrhk3j4lr") ^ StringUtil.stringHash("kj3jkrh3kjrl"));
    System.out.println("0-42L".hashCode());
    System.out.println("0-43-".hashCode());
  }

  /**
   * 生成MD5
   * @param originMsg
   * @return
   */
  public synchronized static String genMD5(String originMsg) {
    md5.Init();
    md5.Update(originMsg);
    return md5.asHex();
  }

  public static String createUUID() {
    return RandomGUID.geneGuid().toUpperCase();
  }
  
  /**
   * 生成uuid
   */
  public static String createUUID8() {
    StringBuffer shortBuffer = new StringBuffer();
    String uuid = UUID.randomUUID().toString().replace("-", "");
    for (int i = 0; i < 8; ++i) {
      String str = uuid.substring(i * 4, i * 4 + 4);
      int x = Integer.parseInt(str, 16);
      shortBuffer.append(chars[(x % 62)]);
    }
    return shortBuffer.toString();
  }
  
}
