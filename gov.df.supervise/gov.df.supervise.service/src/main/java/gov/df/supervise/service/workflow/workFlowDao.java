package gov.df.supervise.service.workflow;

import gov.df.supervise.service.common.persistence.annotation.MyBatisDao;

import java.util.Map;

@MyBatisDao
public interface workFlowDao {
  public Map<String, Object> SelectMenuNode(Map<String, Object> param);

  // public String SelectNodeName(String node_id);

  public void InsertWorkFlow(Map<String, Object> param);

  public void InsertWorkFlowEnd(Map<String, Object> param);

  public void InsertWorkTrace(Map<String, Object> param);

  public void updateWorkFlow(Map<String, Object> param);

  public void updateWorkFlowTrace(Map<String, Object> param);

  //public Map<String, Object> SelectAll(String entity_id);

  public void updateStatus1(Map<String, Object> param);

  public void updateStatus2(Map<String, Object> param);

  public Map<String, Object> selectTableName(String billtype_code);

  public Map<String, Object> getWFCurItem(String entity_id);

  //public int getCount(String entity_id);

  public void deleteWFCurItem(String entity_id);

  public void updateIsEnd(Map<String, Object> param);

  public Map<String, Object> SelectNode(String cur_node_id);

  public Map<String, Object> getWFEndItem(String entity_id);

  public void deleteWFEndItem(String entity_id);

  public int getCount(String entity_id);

  public int getCountEnd(String entity_id);

  public void InsertWorkFlowDel(Map<String, Object> param);

}
