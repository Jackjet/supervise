package gov.df.fap.service.util.ftp;

import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.xml.XMLData;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

/**
 * 默认上传模式，只上传到服务器
 * @author Administrator
 *
 */
public class DefaultAttachTransferHelper extends AbstractAttachTransferHelper {

  public DefaultAttachTransferHelper() {
    super();
  }

  /**
   * 根路径
   */
  private String rootPath = null;

  public String createFilePath(XMLData data) {
    //默认为消息的附件上传
    //路径形式：根路径/userid/roleid/消息ID/
    //注：要先将消息插入数据库才能上传附件，保证消息ID存在
    //这样，消息ID路径唯一，能单消息存储多附件，至于是否需要单消息多附件，以后再考虑
    if (data != null && !StringUtils.isEmpty(getRootPath())
      && !ObjectIsEmpty(data.get(AbstractAttachTransferHelper.FILE_PATH_KEY))) {
      StringBuffer sb = new StringBuffer(getRootPath());
      String path = data.get(AbstractAttachTransferHelper.FILE_PATH_KEY).toString().trim();
      sb.append(path);
      if (!path.endsWith("/") || !path.endsWith("\\")) {
        sb.append(File.separator);
      }
      return sb.toString();
    }
    return null;
  }

  protected void makeDir(String path) {
    if (StringUtils.isEmpty(path)) {
      return;
    }
    File folder = new File(path);
    if (!folder.exists())
      folder.mkdirs();
  }

  protected void upload(String path, String fileName, byte[] content) {
    BufferedOutputStream stream = null;
    FileOutputStream fstream = null;
    try {
      fstream = new FileOutputStream(new File(path + fileName));
      stream = new BufferedOutputStream(fstream);
      if (content != null) {
        stream.write(content);
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException("目录不存在，请管理员检查附件上传目录是否有读写权限");
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (stream != null)
        try {
          stream.close();
        } catch (IOException e) {
        }
      if (fstream != null)
        try {
          fstream.close();
        } catch (IOException e) {
        }
    }
  }

  protected byte[] download(String path, String fileName) {
    BufferedInputStream in = null;
    ByteArrayOutputStream out = null;
    InputStream is = null;
    try {
      is = new FileInputStream(path + fileName);
      in = new BufferedInputStream(is);
      out = new ByteArrayOutputStream(1024);

      byte[] temp = new byte[1024];
      int size = 0;
      while ((size = in.read(temp)) != -1) {
        out.write(temp, 0, size);
      }
      byte[] content = out.toByteArray();
      return content;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (in != null)
        try {
          in.close();
        } catch (IOException e) {
        }
      if (is != null)
        try {
          is.close();
        } catch (IOException e) {
        }
      if (out != null)
        try {
          out.close();
        } catch (IOException e) {
        }
    }
    return null;
  }

  /**
   * 获取根路径，当根路径为空时，该方法会设置操作系统用户目录为根路径
   * @return
   */
  public String getRootPath() {
    if (StringUtils.isEmpty(rootPath)) {
      //先到参数里获取
      rootPath = (String) SessionUtil.getParaMap().get("UPLOAD_ROOT_PATH");
    }
    if (StringUtils.isEmpty(rootPath)) {
      //如果未设置根目录，则根目录为操作系统用户目录
      rootPath = System.getProperty("user.home");
    }
    //处理根目录尾部带路径分隔符
    if (!StringUtils.isEmpty(rootPath)) {
      rootPath = rootPath.trim();
      if (!rootPath.endsWith("/") && !rootPath.endsWith("\\")) {
        rootPath = rootPath + File.separator;
      }
    }
    return rootPath;
  }

  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  /**
   * 判断字符串在Object形态时，是否为 null 或 trim之后为""
   * @param obj
   * @return
   */
  public boolean ObjectIsEmpty(Object obj) {
    if (obj != null) {
      return StringUtils.isEmpty(obj.toString().trim());
    }
    return false;
  }

  protected void delete(String path, String fileName) {
    File file = new File(path + fileName);
    if (file.exists()) {
      file.delete();
    }
  }

}
