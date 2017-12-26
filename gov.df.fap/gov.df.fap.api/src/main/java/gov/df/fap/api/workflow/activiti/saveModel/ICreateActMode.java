package gov.df.fap.api.workflow.activiti.saveModel;

import org.activiti.engine.RepositoryService;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface ICreateActMode {

  public void saveActModel(String modelId, MultiValueMap<String, String> values,RepositoryService repositoryService,ObjectMapper objectMapper,String object,boolean isFirstCreate);
  
  
  
}
