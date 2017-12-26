package gov.df.fap.util.portal;

import java.io.IOException;

import sun.misc.BASE64Decoder;

/**
 * base64 加解密
 */
public class Base64 {

  public static String decode(String base64CodedString) {
    String decodeStr = "";
    try {
      BASE64Decoder dec = new BASE64Decoder();
      byte[] decodeBuffer = dec.decodeBuffer(base64CodedString);
      decodeStr = new String(decodeBuffer);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return decodeStr;
  }

}
