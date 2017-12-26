package gov.df.fap.api.attach;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public interface IAttachManage {

  /**
   * 附件上传
   * 
   * @param fileList
   *            文件对象(二进制字节码)
   * @param app_id
   *            子系统编码
   * @param orgcode
   *            用户所属（单位或处室编码）
   * @param extpath
   *            自定义的扩展路径
   * @param categoryId
   *            附件分类号
   * @return Map “attachID”:”001”, “attachName”:”xxx.doc”
   * @throws Exception
   */
  public List<Map<String, Object>> uploadattach(List<Map<String, Object>> fileList, String app_id, String orgcode,
    String extpath, String categoryId) throws Exception;

  /**
   * 消息服务
   * 传送即时消息文件
   * 
   * @param fileList
   *            文件对象(二进制字节码)
   * @return Map “attachID”:”001”, “attachName”:”xxx.doc”
   * @throws Exception
   */
  public List<Map<String, Object>> uploadIMattach(List<Map<String, Object>> fileList) throws Exception;

  /**
   * 根据附件ID查询附件
   * 
   * @param attachId
   *            附件id
   * @return
   * @throws Exception
   */
  public Map getAttach(String attachId) throws Exception;

  /**
   * 批量查询附件
   * 
   * @param attachIds
   *            附件id集合
   * @return
   * @throws Exception
   */
  public List getAttachList(List attachIds) throws Exception;

  /**
   * 根据附件ID删除附件
   * 
   * @param attachIds
   *            附件id（可以删除一个或多个）
   * @return
   * @throws Exception
   */
  public boolean deleteAttach(List attachIds) throws Exception;

  /**
   * 根据附件ID修改附件备注信息（一个或多个）
   * 
   * @param list
   *            Map: attachId：附件id remark：修改的备注信息
   * @return
   * @throws Exception
   */
  public boolean updateRemark(List<Map> list) throws Exception;

  /**
   * 根据附件ID修改附件附件名称（一个或多个）
   * 
   * @param list
   *            Map: attachId：附件id attachName：修改的附件名称
   * @return
   * @throws Exception
   */
  public boolean updateAttachName(List<Map> list) throws Exception;

  /**
  * 获取分页请求
  * @param pageNumber
  * @param pageSize
  * @return
  */
  public PageRequest buildPageRequest(int pageNumber, int pageSize);

  /**
   * 获取分页表
   * @param map_list
   * @return
   */
  public List<Map<String, String>> changePageFormat(List<Map<String, Object>> map_list);

  /**
   * 获取分页数据
   * @param sql
   * @param pageNumber2
   * @param pageSize
   * @return
   */
  public PageImpl getPageData(String sql, int pageNumber2, int pageSize);

  /**
    * 查询到的数据总数
    * @param tableName 对应表名 
    * @return
    * Integer
    * @throws Exception
    */
  public Integer getDataCount(String tableName) throws Exception;

  /**
   * 根据key获取附件配置文件value
   * @param filePath
   * @param key
   * @return
   */
  public String getPropertiesValueByKey(String filePath, String key);

  /**
   * 获取配置文件的所有属性
   * @param filePath
   * @return
   * @throws IOException
   */
  public Map<String, Object> getAllProperties(String filePath) throws IOException;

  /**
   * 更新附件信息
   * @param attachInfo
   * @return
   */
  public boolean updateAttachInfo(Map attachInfo);

  /**
   * 获取sys_app
   * @return
   */
  public List getSysApp();

  /**
   * 获取上传类型
   * @return
   */
  public String getUploadMode();

  /**
   * 获取上传路径
   * @return
   */
  public String getUploadRootPath();

  public String getRgCode();

  public Map<String, Object> checkDiskSize(String uploadPath, long attachSize, long limitSize);

  public Map getAttach(String attachId, boolean isAll) throws Exception;

  /**
   * 附件预览
   * @param filePath
   *         附件路径
   * @param fileType
   *         附件原类型
   * @param rootPath
   *         项目根路径
   * @param response
   * @return
   *         Map :已转成可以预览的html文件字符串， 文件夹存放路径
   * @throws Exception
   */
  public Map<String, String> previewFile(String filePath, String fileType, String rootPath, HttpServletResponse response)
    throws Exception;

  /**
   * 删除预览产生的所有文件
   * @param srcPath
   *          文件存放目录
   * @return 删除标识: true 删除, false 删除失败
   * @throws Exception
   */
  public boolean deletePreviewFile(String srcPath) throws Exception;
}
