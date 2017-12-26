package gov.df.fap.api.workflow.activiti.saveModel;

import org.activiti.engine.repository.Model;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface IGetModelBaseData {

  public ObjectNode getModelBaseData(ObjectNode editorJsonNode, Model model,String expresonFull);
  
  
}
