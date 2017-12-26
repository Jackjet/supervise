package gov.df.fap.service.util.ftp;

import gov.df.fap.util.xml.XMLData;

import java.util.List;

public abstract class AbstractAttachTransferHelper {

  /**
   * XMLData中存放文件名称的Key
   */
  public static final String FILE_NAME_KEY = "ob_name";

  /**
   * XMLData中存放文件类型的Key
   */
  public static final String FILE_TYPE_KEY = "ob_type";

  /**
   * XMLData中存放文件的Key
   */
  public static final String FILE_BYTES_KEY = "ob";

  /**
   * XMLData中存放文件路径的Key（不包括根路径部分）
   */
  public static final String FILE_PATH_KEY = "ob_path";

  public AbstractAttachTransferHelper() {
    super();
  }

  /**
   * 上传文件
   * @param fileList
   */
  public void uploadFiles(List fileList) {
    if (!login())
      return;

    for (int i = 0; i < fileList.size(); i++) {
      XMLData data = (XMLData) fileList.get(i);
      if (data != null) {
        String path = createFilePath(data);
        String fileName = createUpLoadFileName(data);
        byte[] blobByte = (byte[]) data.get(FILE_BYTES_KEY);
        makeDir(path);
        upload(path, fileName, blobByte);
      }
    }

    loginOut();
  }

  public byte[] downloadFiles(XMLData targetFile) {
    if (!login())
      return null;
    byte[] content = download(createFilePath(targetFile), createDownLoadFileName(targetFile));
    loginOut();
    return content;
  }

  /**
   * 删除，list里面是XMLData
   * @param datas
   */
  public void deleteFiles(List datas) {
    if (datas == null || datas.size() == 0 || !login())
      return;
    for (int i = 0; i < datas.size(); i++) {
      XMLData data = (XMLData) datas.get(i);
      delete(createFilePath(data), createDownLoadFileName(data));
    }
    loginOut();
  }

  /**
   * 登录验证用户方法，默认返回true.ftp模式重写此方法
   * @return
   */
  protected boolean login() {
    return true;
  }

  /**
   * 注销登录
   */
  public void loginOut() {
  }

  /**
   * 构建文件路径的方法，如:C:\Documents and Settings\
   * 实现时，注意文件路径后加分隔符File.separator
   * @param data 
   * @return
   */
  public abstract String createFilePath(XMLData data);

  /**
   * 生成上传文件名称的方法，如test.txt
   * @param data 
   * @return
   */
  public String createUpLoadFileName(XMLData data) {
    if (data != null) {
      if (data.get(FILE_NAME_KEY) != null && !("".equals(data.get(FILE_NAME_KEY)))) {
        if (data.get(FILE_TYPE_KEY) != null && !("".equals(data.get(FILE_TYPE_KEY)))) {
          return data.get(FILE_NAME_KEY) + "." + data.get(FILE_TYPE_KEY);
        } else {
          return (String) data.get(FILE_NAME_KEY);
        }
      } else {
        if (data.get(FILE_TYPE_KEY) != null && !("".equals(data.get(FILE_TYPE_KEY)))) {
          return (String) data.get(FILE_TYPE_KEY);
        }
      }
    }
    return null;
  }

  /**
   * 生成下载文件名称的方法，如test.txt
   * 默认实现是调用createUpLoadFileName，
   * 如果方法要求实现不一致，需重写该方法
   * @param data
   * @return
   */
  public String createDownLoadFileName(XMLData data) {
    return createUpLoadFileName(data);
  }

  /**
   * 上传文件方法
   * @param path
   * @param fileName
   */
  protected abstract void makeDir(String path);

  /**
   * 上传文件方法
   * @param path
   * @param fileName
   */
  protected abstract void upload(String path, String fileName, byte[] content);

  /**
   * 下载文件方法
   * @param path
   * @param fileName
   */
  protected abstract byte[] download(String path, String fileName);

  /**
   * 删除文件
   * @param path
   * @param fileName
   */
  protected abstract void delete(String path, String fileName);

}
