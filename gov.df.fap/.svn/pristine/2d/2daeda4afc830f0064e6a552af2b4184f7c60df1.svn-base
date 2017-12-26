package gov.df.fap.api.attach;

import gov.df.fap.bean.attach.AttachCategoryForm;

import java.util.List;

public interface IAttachCategory {
  /**
   * 根据附件分类号删除分类
   * @param categoryId 
   *        分类id
   * @return
   * @throws Exception
   */
  public boolean deleteAttachCategory(String categoryId, String uploadMode) throws Exception;

  /**
  * 
  * @param attachCategory
  *        附件分类实体类
  * @return
  * @throws Exception
  */
  public boolean updateAttachCategory(AttachCategoryForm attachCategory) throws Exception;

  /**
   * 
   * @param attachCategory
   *        附件分类实体类 
   * @return
   * @throws Exception
   */
  public boolean saveAttachCategory(AttachCategoryForm attachCategory) throws Exception;

  /**
   * 根据附件分类号查询附件
   * @param categoryId
   * 
   * @return
   * 
   */
  public List findAttachByCategoryId(String categoryId, String uploadMode);

  /**
   * 获取分类信息
   * @return
   */
  public List getAllAttachCategory(String sysId);

}
