/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gov.df.fap.service.util.wf.activiti.json.converter;

import gov.df.fap.service.util.wf.activiti.BaseBpmnJsonConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.UserTask;
import org.apache.commons.collections.CollectionUtils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Tijs Rademakers
 */
public class UserTaskJsonConverter extends BaseBpmnJsonConverter {

    public static void fillTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap,
            Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
    
        fillJsonTypes(convertersToBpmnMap);
        fillBpmnTypes(convertersToJsonMap);
    }
  
    public static void fillJsonTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap) {
        convertersToBpmnMap.put(STENCIL_TASK_USER, UserTaskJsonConverter.class);
    }
  
    public static void fillBpmnTypes(Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
        convertersToJsonMap.put(UserTask.class, UserTaskJsonConverter.class);
    }
  
    @Override
    protected String getStencilId(BaseElement baseElement) {
        return STENCIL_TASK_USER;
    }
  
    @Override
    protected void convertElementToJson(ObjectNode propertiesNode, BaseElement baseElement) {
    	UserTask userTask = (UserTask) baseElement;
        String assignee = userTask.getAssignee();
        //String assigneeName = userTask.getAssigneeName();
        String owner = userTask.getOwner();
    
        if (StringUtils.isNotEmpty(assignee) || StringUtils.isNotEmpty(owner) || CollectionUtils.isNotEmpty(userTask.getCandidateUsers()) || 
                CollectionUtils.isNotEmpty(userTask.getCandidateGroups())) {
          
            ObjectNode assignmentNode = objectMapper.createObjectNode();
            ObjectNode assignmentValuesNode = objectMapper.createObjectNode();
          
            if (StringUtils.isNotEmpty(assignee)) {
              assignmentValuesNode.put(PROPERTY_USERTASK_ASSIGNEE, assignee);
            }
            
            if (StringUtils.isNotEmpty(owner)) {
              assignmentValuesNode.put(PROPERTY_USERTASK_OWNER, owner);
            }
          
            if (CollectionUtils.isNotEmpty(userTask.getCandidateUsers())) {
            	ObjectNode candidateNode=BpmnJsonConverterUtil.convertRefValue(userTask.getCandidateUsers());
            	propertiesNode.put("candidateusers", candidateNode);
            }
          
            if (CollectionUtils.isNotEmpty(userTask.getCandidateGroups())) {
                ArrayNode candidateArrayNode = objectMapper.createArrayNode();
                for (String candidateGroup : userTask.getCandidateGroups()) {
                    ObjectNode candidateNode = objectMapper.createObjectNode();
                    candidateNode.put("value", candidateGroup);
                    candidateArrayNode.add(candidateNode);
                }
                assignmentValuesNode.put(PROPERTY_USERTASK_CANDIDATE_GROUPS, candidateArrayNode);
            }
          
            assignmentNode.put("assignment", assignmentValuesNode);
            propertiesNode.put(PROPERTY_USERTASK_ASSIGNMENT, assignmentNode);
        }
        if(userTask.isForCompensation())
        	setPropertyValue(PROPERTY_COMPEMSATE, "true", propertiesNode);
        else
        	setPropertyValue(PROPERTY_COMPEMSATE, "false", propertiesNode);	
        
        if (userTask.getPriority() != null) {
            setPropertyValue(PROPERTY_USERTASK_PRIORITY, userTask.getPriority().toString(), propertiesNode);
        }
        if (StringUtils.isNotEmpty(userTask.getFormKey())) {
            setPropertyValue(PROPERTY_FORMKEY, userTask.getFormKey(), propertiesNode);
        }
        
        setPropertyValue(PROPERTY_USERTASK_DUEDATE, userTask.getDueDate(), propertiesNode);
        setPropertyValue(PROPERTY_USERTASK_CATEGORY, userTask.getCategory(), propertiesNode);
        if(userTask.getAssignee()!=null)
        {
        	ObjectNode userNode=objectMapper.createObjectNode();
        	/*if(userTask.getAssignee().indexOf("{")!=-1)
        	{*/
        		userNode=BpmnJsonConverterUtil.convertRefValue(userTask.getAssignee());
        	/*}
        	else
        		userNode.put("showValue", userTask.getAssignee());*/
        	propertiesNode.put("assignment", userNode);
	        
        }
        
        addFormProperties(userTask.getFormProperties(), propertiesNode);
    }
  
    @Override
    protected FlowElement convertJsonToElement(JsonNode elementNode, JsonNode modelNode, Map<String, JsonNode> shapeMap) {
    	UserTask task = new UserTask();
        task.setPriority(getPropertyValueAsString(PROPERTY_USERTASK_PRIORITY, elementNode));
        String formKey = getPropertyValueAsString(PROPERTY_FORMKEY, elementNode);
        if (StringUtils.isNotEmpty(formKey)) {
            task.setFormKey(formKey);
        }
        String duedate=getPropertyValueAsString(PROPERTY_USERTASK_PARAM_DUEDATE, elementNode);
        if(duedate!=null&&StringUtils.isNotEmpty(duedate))
        	task.setDueDate(duedate);
        else
        	task.setDueDate(getPropertyValueAsString(PROPERTY_USERTASK_DUEDATE, elementNode));
        task.setCategory(getPropertyValueAsString(PROPERTY_USERTASK_CATEGORY, elementNode));
        JsonNode assignmentNode = getProperty("assignment", elementNode);
        if (assignmentNode != null) {
            JsonNode assignmentDefNode = assignmentNode.get("refResultData");
            if (assignmentDefNode != null) {
                JsonNode assigneeNode = assignmentDefNode.get("pk");
                if(assigneeNode==null&&assignmentDefNode.get(0)!=null)
                	assigneeNode=assignmentDefNode.get(0).get("pk");
                if (assigneeNode != null && assigneeNode.isNull() == false) {
                    task.setAssignee(assigneeNode.asText());
                }
                
            }
        }
        JsonNode candidateusersNode = getProperty("candidateusers", elementNode);
        if (candidateusersNode != null) {
            JsonNode candidateusersDefNode = candidateusersNode.get("refResultData");
            if (candidateusersDefNode != null) {
                List<String> candidateUsersList = new ArrayList<String>();
                List<String> candidateUsersNameList = new ArrayList<String>();
                for(int i=0;i<candidateusersDefNode.size();i++)
                {
                	JsonNode assigneeNode = candidateusersDefNode.get(i).get("pk");
                    if (assigneeNode != null && assigneeNode.isNull() == false) {
                        candidateUsersList.add(assigneeNode.asText());
                    
	                    assigneeNode = candidateusersDefNode.get(i).get("name");
	                    if(assigneeNode != null && assigneeNode.isNull() == false)
	                    	candidateUsersNameList.add(candidateusersDefNode.get(i).get("name").asText());
	                    else
	                    	candidateUsersNameList.add(candidateusersDefNode.get(i).get("pk").asText());
                    }
                }
                
                task.setCandidateUsers(candidateUsersList);
            }
        }
        convertJsonToFormProperties(elementNode, task);
//中建任务监听器
        
         ActivitiListener litener=new ActivitiListener();
         litener.setId("taskeventDefaultDel");
         litener.setEvent("delete");
         litener.setImplementation("com.yonyou.bpm.engine.listener.BpmTaskActionListener");
         litener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
         task.getTaskListeners().add(litener);
         
         litener=new ActivitiListener();
         litener.setId("taskeventDefaultWithdraw");
         litener.setEvent("withdraw");
         litener.setImplementation("com.yonyou.bpm.engine.listener.BpmTaskActionListener");
         litener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
         task.getTaskListeners().add(litener);
         
         litener=new ActivitiListener();
         litener.setId("taskeventDefaultCreate");
         litener.setEvent("create");
         litener.setImplementation("com.yonyou.bpm.engine.listener.BpmTaskActionListener");
         litener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
         task.getTaskListeners().add(litener);
         
         litener=new ActivitiListener();
         litener.setId("taskeventDefaultJump");
         litener.setEvent("jump");
         litener.setImplementation("com.yonyou.bpm.engine.listener.BpmTaskActionListener");
         litener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
         task.getTaskListeners().add(litener);
         
        return task;
    }
}
