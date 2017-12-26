package gov.df.fap.util.portal;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil {

  private static MessageDigest digest = null;

  public static synchronized String hash(String data) {
    if (org.apache.commons.lang.StringUtils.isEmpty(data)) {
      return null;
    }
    if (digest == null)
      try {
        digest = MessageDigest.getInstance("MD5");
      } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
        throw new RuntimeException("不支持该加密算法:MD5");
      }
    try {
      digest.update(data.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e.getMessage());
    }
    return encodeHex(digest.digest());
  }

  private static String encodeHex(byte[] bytes) {
    StringBuffer hex = new StringBuffer(bytes.length * 2);
    for (int i = 0; i < bytes.length; ++i) {
      if ((bytes[i] & 0xFF) < 16) {
        hex.append("0");
      }
      hex.append(Integer.toString(bytes[i] & 0xFF, 16));
    }
    return hex.toString();
  }

}
