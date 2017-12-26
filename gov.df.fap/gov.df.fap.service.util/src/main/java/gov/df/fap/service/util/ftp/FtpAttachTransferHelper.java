package gov.df.fap.service.util.ftp;

import gov.df.fap.service.util.sessionmanager.SessionUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * ftp上传
 * @author Administrator
 *
 */
public class FtpAttachTransferHelper extends DefaultAttachTransferHelper {
  /**
   * 文件名称原编码
   */
  public static final String FILE_NAME_ENCODING = "UTF-8";

  /**
   * 文件名称需要转换的目标编码
   */
  public static final String FILE_NAME_TARGET_ENCODING = "ISO-8859-1";

  public FtpAttachTransferHelper() {
    super();
  }

  private FTPClient ftpClient = null;

  private String ip = null;

  private String port = null;

  private String user = null;

  private String password = null;

  protected void makeDir(String path) {
    if (StringUtils.isEmpty(path)) {
      return;
    }
    //创建路径，感觉不是很好，先这样吧
    try {
      ftpClient.changeWorkingDirectory(File.separator);
    } catch (IOException e1) {
    }
    StringTokenizer s = new StringTokenizer(path, File.separator); //sign
    while (s.hasMoreElements()) {
      String dir = s.nextElement().toString();
      try {
        ftpClient.makeDirectory(dir);
        ftpClient.changeWorkingDirectory(dir);
      } catch (IOException e) {
      }
    }
  }

  protected void upload(String path, String fileName, byte[] content) {
    InputStream is = null;
    try {
      is = new ByteArrayInputStream(content);
      ftpClient.storeFile(new String(fileName.getBytes(FILE_NAME_ENCODING), FILE_NAME_TARGET_ENCODING), is);
      if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
        throw new RuntimeException("文件上传出错错误!");
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      if (is != null)
        try {
          is.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
  }

  protected byte[] download(String path, String fileName) {
    ByteArrayOutputStream bos = null;
    try {
      bos = new ByteArrayOutputStream();
      ftpClient.retrieveFile(path + new String(fileName.getBytes(FILE_NAME_ENCODING), FILE_NAME_TARGET_ENCODING), bos);
      return bos.toByteArray();
    } catch (FileNotFoundException e) {
    } catch (IOException e) {
    } finally {
      if (bos != null)
        try {
          bos.close();
        } catch (IOException e) {
        }
    }
    return null;
  }

  /**
   * 登录验证用户方法，默认返回true.ftp模式重写此方法
   * @return
   */
  public boolean login() {//UPLOAD_FTP_PORT
    String ip = getIp();
    String port = getPort();
    String user = getUser();
    String password = getPassword();
    if (StringUtils.isEmpty(ip) || StringUtils.isEmpty(ip) || StringUtils.isEmpty(user)
      || StringUtils.isEmpty(password))
      throw new RuntimeException("预算执行系统附件上传功能使用了ftp上传模式，请管理员设置相关ftp参数!");

    ftpClient = new FTPClient();
    try {
      ftpClient.connect(ip, Integer.parseInt(port));
      ftpClient.login(user, password);
      if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
        throw new RuntimeException("预算执行系统附件上传功能使用了ftp上传模式,用户名或密码错误!");
      }
      ftpClient.enterLocalPassiveMode();
      ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    } catch (NumberFormatException e) {
    } catch (SocketException e) {
      throw new RuntimeException("预算执行系统附件上传功能使用了ftp上传模式，ftp服务器ip是" + ip + "无法连通，请确认ftp服务器是否启动!");
    } catch (IOException e) {
      throw new RuntimeException("预算执行系统附件上传功能使用了ftp上传模式，ftp服务器port是" + port + "无法连通，请确认ftp服务器是否启动!");
    }

    return true;
  }

  public void loginOut() {
    if (ftpClient != null)
      try {
        ftpClient.logout();
        ftpClient.disconnect();
      } catch (IOException e) {
        e.printStackTrace();
      }
  }

  protected void delete(String path, String fileName) {
    try {
      ftpClient.deleteFile(path + fileName);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getIp() {
    ip = getDefaultFromSessionUtilWhenEmpty(ip, "UPLOAD_FTP_IP");
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getPassword() {
    password = getDefaultFromSessionUtilWhenEmpty(password, "UPLOAD_FTP_PASSWORD");
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPort() {
    port = getDefaultFromSessionUtilWhenEmpty(port, "UPLOAD_FTP_PORT");
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getUser() {
    user = getDefaultFromSessionUtilWhenEmpty(user, "UPLOAD_FTP_USER");
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  /**
   * 当目标值为空时，从SessionUtil中获取相应值
   * 注：不保证一定能取到值，有可能未设置该值
   * @param source
   * @param key
   * @return
   */
  public String getDefaultFromSessionUtilWhenEmpty(String source, String key) {
    if (StringUtils.isEmpty(source) && !StringUtils.isEmpty(key)) {
      //			Object obj = Global.clientContext.getAttribute(key);
      Object obj = SessionUtil.getParaMap().get(key);
      return obj == null ? null : obj.toString().trim();
    }
    return source;
  }

}
