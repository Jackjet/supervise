package gov.df.supervise.service.Tree;

import gov.df.supervise.service.common.persistence.annotation.MyBatisDao;

import java.util.List;
import java.util.Map;

/**
 *  列表树
 * @author tongya
 *
 */
@MyBatisDao
public interface elementTreeDao {
  //获取要素视图
  public String findEleSource(String ele_code);

  //获取列表树
  public List findTree(Map<String, Object> param);

  //获取要素表的数据
  public Map<String, Object> getElementByCode(String ele_code);

  //获取某个要素的详细信息,加权限
  public List getElementData(Map<String, Object> param);

  //获取某个要素的详细信息,加权限
  public List getElementDataNo(Map<String, Object> param);

  public List getElement(Map<String, Object> param);

}
