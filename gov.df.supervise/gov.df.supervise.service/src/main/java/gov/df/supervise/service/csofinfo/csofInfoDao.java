package gov.df.supervise.service.csofinfo;

import gov.df.supervise.service.common.persistence.annotation.MyBatisDao;

import java.util.List;
import java.util.Map;

/**
 * 信息档案库
 * @author tongya
 *
 */
@MyBatisDao
public interface csofInfoDao {
  /**
   * 获取动态页签
   * @param chr_id
   * @return 
   */
  public List getDisplayTitle(String chr_id);

  /**
   * 获取动态要素视图
   * @param ele_code
   * @return
   */
  public String findEleSource(String ele_code);

  /**
   * 获取期间
   * @param param
   * @return
   */
  public List findYear(Map<String, String> param);

  /**
   * 获取列表树
   * @param param
   * @return
   */
  public List findDataByuser(Map<String, String> param);

  /**
   * 获取通用要素信息
   * @param param
   * @return
   */
  public List findData(Map<String, String> param);

}
