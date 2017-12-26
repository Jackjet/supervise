package gov.df.fap.api.workflow.activiti.design;

import gov.df.fap.bean.workflow.FWFLogDTO;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface IGetBpmJson {

  public ObjectNode getBpmJsonData(String modelId);
  
  public ObjectNode setBpmJsonData(ObjectNode bpmJsonData,String id,String deploymentId, List<FWFLogDTO> logList, List<Map> currentNodeNameAndId);
  
  public  String getSysWfFlowsCode(String wfid);
  
  public ObjectNode getHighlightsProcessInstance(String id,String deploymentId, List<FWFLogDTO> logList,List<Map> nodeList,List<Map> queryFlowDataTask);
  
  public List queryFlowDataTask(String wfId, String entityId);
  
  public List<Map> getCurrentNodeName(String entityId);
  
  
  
  
  
  
  
  
}
