package gov.df.fap.service.globalConfigDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>Title: IO工具</p>
 * <p>Description: IO工具</p>
 * <p>Copyright: Copyright (c) 2006 </p>
 * <p>Company:  北京用友政务软件有限公司</p>
 * @Created on 2006-4-30
 * @authory huanglifeng
 * @version 1.0
 */
public class IOUtil {

  public static final int READ_BUFFER_SIZE = 1024;

  /**
   * 从输入流读取二进制数组。
   * @param is
   * @return
   * @throws IOException
   */
  public static byte[] readIOByteArray(InputStream is) throws IOException {
    if (is == null)
      return null;

    ByteArrayOutputStream fout = new ByteArrayOutputStream(READ_BUFFER_SIZE * 3);
    byte[] buffer = new byte[READ_BUFFER_SIZE];
    int bytesRead = -1;
    while ((bytesRead = is.read(buffer, 0, READ_BUFFER_SIZE)) > -1) {
      fout.write(buffer, 0, bytesRead);
    }
    return fout.toByteArray();
  }

  /**
   * 从输入流读取字符字符串。
   * @param is
   * @return
   * @throws IOException
   */
  public static String readIOString(InputStream is) throws IOException {
    byte[] ret = readIOByteArray(is);
    if (ret == null)
      return null;

    return new String(ret);
  }

  /**
   * 从输入流读取字符串
   * @param is
   * @return
   * @throws IOException
   */
  public static StringBuffer readIOStringBuffer(InputStream is) throws IOException {
    if (is == null)
      return null;
    byte[] buffer = new byte[READ_BUFFER_SIZE];
    int bytesRead = -1;
    StringBuffer sbRet = new StringBuffer();
    while ((bytesRead = is.read(buffer, 0, READ_BUFFER_SIZE)) > -1) {
      sbRet.append(new String(buffer, 0, bytesRead));
    }

    return sbRet;
  }

  /**
   * 关闭输入流
   * @param is
   */
  public static void closeStream(InputStream is) {
    if (is != null)
      try {
        is.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
  }

  /**
   * 关闭输出流
   * @param os
   */
  public static void closeStream(OutputStream os) {
    if (os != null)
      try {
        os.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
  }

  public static void main(String[] args) {
    ByteArrayInputStream bais = new ByteArrayInputStream("1231231231231232".getBytes());
    try {
      System.out.println(readIOStringBuffer(bais));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
