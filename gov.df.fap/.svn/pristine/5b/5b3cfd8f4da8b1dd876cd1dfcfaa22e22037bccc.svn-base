/**
 * 
 */
package gov.df.fap.service.util.wf.activiti.json.converter;

import gov.df.fap.api.workflow.activiti.ReferenceJsonConstants;

import java.util.List;

import org.activiti.bpmn.model.StringDataObject;
import org.activiti.bpmn.model.ValuedDataObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author chouhl
 *
 */
public class ReferenceJsonConverterUtil implements ReferenceJsonConstants{

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static ObjectNode createRootNode() {
		ObjectNode node = objectMapper.createObjectNode();
		
		ArrayNode nodes = objectMapper.createArrayNode();
		
		node.put(REFERENCE_RESULTS, nodes);
		
	    return node;
	}
	
	public static ObjectNode createNode() {
		ObjectNode node = objectMapper.createObjectNode();
		
	    return node;
	}
	
	public static void convertDataToJson(List<ValuedDataObject> dataObjects, ObjectNode node) {
		ArrayNode nodes = (ArrayNode)node.get(REFERENCE_RESULTS);
		if(nodes == null){
			nodes = objectMapper.createArrayNode();
		}
		
		ObjectNode dataNode = objectMapper.createObjectNode();
	    
	    for (ValuedDataObject dObj : dataObjects) {
	    	if(dObj instanceof StringDataObject){
	    		if(dObj.getValue() == null){
	    			dataNode.put(dObj.getId(), "");
	    		}else{
	    			dataNode.put(dObj.getId(), (String)dObj.getValue());
	    		}
	    		//日期类型格式化
	    		
	    		dataNode.put(REFERENCE_DATA_TYPE, StringDataObject.class.getSimpleName());
	    	}
	    }
	    
	    nodes.add(dataNode);
	    
	    node.put(REFERENCE_RESULTS, nodes);;
	  }
	
}
