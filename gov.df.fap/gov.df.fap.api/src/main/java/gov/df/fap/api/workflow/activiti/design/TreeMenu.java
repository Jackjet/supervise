package gov.df.fap.api.workflow.activiti.design;

import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface TreeMenu {
	
	public Map<String,String> leadin(byte[] modelEditorSource,RepositoryService repositoryService,String leadIn);
	
	public byte[] leadout(String code,RepositoryService repositoryService);

	public void addProcess(String code, String name, String ptname, String field,RepositoryService repositoryService);

	public void delProcess(String code,RepositoryService repositoryService);

	public List queryPrimaryName();

	public Map getZtree();
}
