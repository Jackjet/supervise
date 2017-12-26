package gov.df.fap.util.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class FileUtil {

  /**
   * 在ClassPath中查找配置文件，并返回文件的绝对路径
   * 
   * @param fileName
   *            配置文件的名字
   * @return
   * @throws MalformedURLException
   */
  public static String getFilePath(String fileName) {
    if (fileName == null)
      return null;
    URL url = FileUtil.class.getClassLoader().getResource(fileName);
    if (url == null) {
      try {
        url = new URL("file", "localhost", fileName);
      } catch (MalformedURLException e) {
        return null;
      }
    }
    return url.getFile();
  }

  /**
   * 将文件转为String
   * 
   * @param fileName
   *            可以相对路径，也可以是绝对路径。
   * @return 文件内容字符串。
   * @throws IOException
   */
  public static String file2String(String fileName) throws IOException {

    if (fileName == null)
      return null;
    byte[] data = getData(fileName);
    if (data == null)
      return null;

    return new String(data, "GBK");
  }

  /**
   * 将文件转为String 最大200,000,000bytes
   * 
   * @param fileName
   *            可以相对路径，也可以是绝对路径。
   * @return 文件内容字符串。
   * @throws IOException
   */
  public static byte[] file2Bytes(String fileName) throws IOException {

    if (fileName == null) {
      throw new IOException("未设定对应的文件路径!");
    }
    return getData(fileName);
  }

  private static byte[] getData(String fileName) throws IOException {

    //创建该文件的内存实例
    File file = null;
    try {
      file = new File(fileName);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    //从绝对路径取得文件。
    InputStream in = null;
    ByteArrayOutputStream data = new ByteArrayOutputStream();
    if (file != null && file.exists()) {
      in = new java.io.FileInputStream(file);
      if (in.available() > 0) {
        int bit;
        while ((bit = in.read()) != -1) {
          if (bit == 10 || bit == 13)
            continue;
          data.write(bit);
        }
      }
      in.close();
      //从ClassPath中取得文件实例。
    } else {
      //察看本地是否存在该文件
      in = FileUtil.class.getClassLoader().getParent().getResourceAsStream(fileName);

      if (in == null) {
        in = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
        if (in == null) {
          throw new IOException("Can not read the file in classpath--" + fileName);
        } else {
          if (in.available() > 0) {
            int bit;
            while ((bit = in.read()) != -1) {
              if (bit == 10 || bit == 13)
                continue;
              data.write(bit);
            }
          }
          in.close();
        }
      } else {
        if (in.available() > 0) {
          int bit;
          while ((bit = in.read()) != -1) {
            if (bit == 10 || bit == 13)
              continue;
            data.write(bit);
          }
        }
        in.close();
      }
    }
    return data.toByteArray();
  }

  public static boolean createDir(String path) {
    File file = new File(path);
    boolean flag = false;
    if (!file.exists() && !file.isDirectory()) {
      flag = file.mkdir();
    }
    return flag;
  }

  public static void createDirs(String[] path) throws Exception {
    try {
      for (int i = 0; i < path.length; i++) {
        createDir(path[i]);
      }
    } catch (Exception e) {
      throw new Exception("目录创建失败！");
    }
  }

  public static void cutFiles(File srcDir, File destDir) throws IOException {
    FileUtils.copyDirectoryToDirectory(srcDir, destDir);
    File[] srcFiles = srcDir.listFiles();
    if (srcFiles != null) {
      for (int i = 0; i < srcFiles.length; i++) {
        srcFiles[i].delete();
      }
    }
  }
}