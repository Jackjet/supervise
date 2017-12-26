package gov.df.fap.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * <p>
 * Title:配置文件访问类
 * </p>
 * <p>
 * Description:封装配置文件，提供配置信息的存取。
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: FounderCY
 * </p>
 * z
 * 
 * @author jusitn
 * @version 1.0
 */

public class PropertiesFile {
  private String fileName;

  private boolean readOnly;

  /**
   * 构建方法 只读属性用来确定属性文件存在的位置，如果只读，保存在java包中，如果不是只读，保存在用户目录下边
   * 
   * @param fileName
   *            文件名称
   * @param readOnly
   *            只读属性
   */
  public PropertiesFile(String fileName, boolean readOnly) {
    this.readOnly = readOnly;
    this.fileName = fileName;
  }

  // 获取配置
  public String getProperty(String key, String defaultValue) {
    try {
      Properties property = new Properties();
      if (readOnly == true) {
        property.load(this.getClass().getResourceAsStream("/" + fileName));
        return property.getProperty(key, defaultValue);
      } else {
        // by swj identified os name windows or unix
        //dingyy20120424 修改在其他系统上不能保存本地的问题
        String userHome = System.getProperties().getProperty("user.home");
        String separator = System.getProperties().getProperty("file.separator");
        String pathName = userHome + separator + fileName;

        //System.out.println(pathName);
        File file = new File(pathName);
        if (!file.exists()) {
          file.createNewFile();
        }
        property.load(new FileInputStream(pathName));
        return property.getProperty(key, defaultValue);
      }
    } catch (FileNotFoundException fe) {
      System.out.println(fe.getMessage());
    } catch (IOException ioe) {
      System.out.println(ioe.getMessage());
    }
    return "";
  }

  public String getProperty(String key) {
    return getProperty(key, "");
  }

  // 设置配置
  public void setProperty(String key, String value) {
    try {
      if (readOnly == true)
        return;
      //String pathname = System.getProperties().getProperty("user.home") + "/" + fileName;

      String userHome = System.getProperties().getProperty("user.home");
      String separator = System.getProperties().getProperty("file.separator");
      String pathName = userHome + separator + fileName;

      File file = new File(pathName);
      if (!file.exists()) {
        file.createNewFile();
      }
      if (file.canRead() && file.canWrite()) {
        Properties property = new Properties();
        FileInputStream in = new FileInputStream(file);
        property.load(in);
        in.close();
        //edited by zhadaopeng
        if (value == null) {
          property.setProperty(key, "");
        } else {
          property.setProperty(key, value);
        }
        FileOutputStream out = new FileOutputStream(file);
        property.store(out, null);
        out.close();
      }
    } catch (FileNotFoundException fe) {
      System.out.println(fe.getMessage());
    } catch (IOException ioe) {
      System.out.println(ioe.getMessage());
    }
  }

  public static void main(String[] args) {
    PropertiesFile f = new PropertiesFile("clientconf.properties", true);
    System.out.println(f.getProperty("USERCODE"));
  }
}