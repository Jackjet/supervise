package gov.df.fap.api.workflow;

import java.util.List;
import java.util.Map;

/**
 * 业务单据类型节点接口
 * @author liuwj3
 *
 */
public interface IBusinessBillNode {
  /**
   * 查询所有业务单据类型节点
   * @return
   */
  List<Map<String, Object>> getBusinessBillCode() throws Exception;

  /**
   * 保存业务单据类型节点
   * @param map
   */
  void saveBusinessBillNode(Map<String, Object> map) throws Exception;

  /**
   * 更新业务单据类型节点
   * @param map
   */
  void updateBusinessBillNode(Map<String, Object> map) throws Exception;

  /**
   * 删除业务单据类型节点
   * @param id
   */
  void deleteBusinessBillNode(String id) throws Exception;
}
