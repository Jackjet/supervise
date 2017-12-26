package gov.df.supervise.service.document;

import gov.df.supervise.service.common.persistence.annotation.MyBatisDao;

import java.util.List;
import java.util.Map;

/**
 * 政策法规库
 * @author tongya
 *
 */
@MyBatisDao
public interface csofDocumentDao {
  /**
   * 获取法规类型
   * @param param
   * @return
   */
  public List getTreeByOid(Map<String, Object> param);

  /**
   * 新增政策法规
   */
  public void saveDocument(Map<String, Object> param);

  /**
   * 通过法规类型查询政策法规
   */
  public List getDocumentById(Map<String, Object> param);

  /**
   * 通过法规类型查询政策法规数量
   */
  public int getDocumentCountById(Map<String, Object> param);

  /**
   * 编辑政策法规
   */
  public void updateDocument(Map<String, Object> param);

  /**
   * 删除政策法规
   */
  public void deleteDocument(String id);

  /**
   * 查询政策法规
   */
  public List getDocument(Map<String, Object> param);

  /**
   * 查询政策法规
   */
  public List getHomeDocument(Map<String, Object> param);

  /**
   * 查询政策法规数量
   */
  public int getDocumentCount(String oid);

  /**
   * 查询政策法规数量
   */
  public int getDocumentCount();

  /**
   * 
   *查询政策法规的附件id
   */
  public String getFileId(String id);

  /**
   * 通过政策法规类型id查询政策法规类型名称
   */
  public String getDoctypeName(String doctype_id);
}
