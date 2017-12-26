package gov.df.fap.api.workflow.activiti.saveModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ICreateModel2 {

	/**
	 * 创建设计器模型视图
	 * @param modelId
	 * @param values
	 * @return
	 */
	
	public HashMap createModelView(boolean isFirstCreate, String model,JsonNode editorNode,RepositoryService repositoryService,ObjectMapper objectMapper);
	
	public List<Map> getWfIdAndConditionId(String wf_code) throws Exception;
	
}
