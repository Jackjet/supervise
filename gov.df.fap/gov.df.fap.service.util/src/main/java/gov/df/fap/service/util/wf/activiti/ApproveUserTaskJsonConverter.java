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
package gov.df.fap.service.util.wf.activiti;

import gov.df.fap.bean.workflow.activiti.ApproveUserTask;
import gov.df.fap.bean.workflow.activiti.ProcessParticipant;
import gov.df.fap.bean.workflow.activiti.ProcessParticipantItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Tijs Rademakers
 */
public class ApproveUserTaskJsonConverter extends BaseBpmnJsonConverter {

    public static void fillTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap,
            Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
    
        fillJsonTypes(convertersToBpmnMap);
        fillBpmnTypes(convertersToJsonMap);
    }
  
    public static void fillJsonTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap) {
    	/*FlowNodeHistoryParseHandler flowNodeHistoryParseHandler=new FlowNodeHistoryParseHandler();
    	flowNodeHistoryParseHandler.getHandledTypes().add(ApproveUserTask.class);*/
        convertersToBpmnMap.put("ApproveUserTask", ApproveUserTaskJsonConverter.class);
    }
  
    public static void fillBpmnTypes(Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
        convertersToJsonMap.put(ApproveUserTask.class, ApproveUserTaskJsonConverter.class);
    }
  
    @Override
    protected String getStencilId(BaseElement baseElement) {
        return "ApproveUserTask";
    }
  
    @Override
    protected void convertElementToJson(ObjectNode propertiesNode, BaseElement baseElement) {
    	ApproveUserTask userTask = (ApproveUserTask) baseElement;
        String currentUser=userTask.getLoopCharacteristics().getInputDataItem();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode otherObject;
      	JsonNode allNode=null;
    	if(currentUser!=null)
        {
	   	  	currentUser=currentUser.substring(currentUser.indexOf("${bpmBean.getUser")+"${bpmBean.getUser(\"".length(), currentUser.length()-3);
	   	  	currentUser=currentUser.replaceAll("'", "\"");
	   	  	if(currentUser!=null&&!currentUser.isEmpty()&&currentUser.length()>0)
	   	  	{
		   	  	try {
					allNode=objectMapper.readTree(currentUser);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		   	  	/*for(int i=0;i<allNode.get("processParticipantItems").size();i++)
		   	  	{
		   	  		if(allNode.get("processParticipantItems").get(i)!=null&&!allNode.get("processParticipantItems").get(i).isNull())
		   	  		{
			   	  		 Map<String, ParticipantConfig> userServiceImpl=BpmServiceUtils.getBpmEngineConfiguration().getParticipantService().getParticipantConfigs();
			   	  		 Collection<ParticipantConfig> c = userServiceImpl.values();
			   	  		 Iterator it = c.iterator();
			   	  		 for (; it.hasNext();) {
			   	  			 ParticipantConfig tempConfig=(ParticipantConfig) it.next();
			   	  			 if(allNode.get("processParticipantItems").get(i).get("type").asText().equals(tempConfig.getCode()))
			   	  			 {
				   	  			List<String> jsonArray=new ArrayList<String>();
			   		  			otherObject=allNode.get("processParticipantItems").get(i).get("details");
			   		  			for(int j=0;j<otherObject.size();j++)
			   		  			{
			   		  				jsonArray.add(otherObject.get(j).get("others").toString());
			   		  			}
			   		  			if(jsonArray!=null&&jsonArray.size()>0)
			   		  			{
			   		  				ObjectNode participantNode=BpmnJsonConverterUtil.convertRefValue(jsonArray);
			   		  				propertiesNode.put(tempConfig.getCode().toLowerCase(), participantNode);
			   		  			}
			   		  			else
			   	  				 propertiesNode.put(tempConfig.getCode().toLowerCase(), true);
			   	  				 
			   	  			 }
			   	  		 }
		   	  		}
		   	  	}*/
	   	  	}
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
        if(userTask.getAssignAble()!=null&&userTask.getAssignAble())
        	setPropertyValue(PROPERTY_USERTASK_ASSIGNABLE, userTask.getAssignAble().toString(), propertiesNode);
        setPropertyValue(PROPERTY_USERTASK_DUEDATE, userTask.getDueDate(), propertiesNode);
        setPropertyValue(PROPERTY_USERTASK_CATEGORY, userTask.getCategory(), propertiesNode);
        
        /*if (CollectionUtils.isNotEmpty(userTask.getUserGroups())&&userTask.getUserGroups().size()!=0) {
        	ObjectNode userGroupNode=BpmnJsonConverterUtil.convertRefValue(userTask.getUserGroups());
        	propertiesNode.put("usergroups", userGroupNode);
        }
        if (CollectionUtils.isNotEmpty(userTask.getDepts())&&userTask.getDepts().size()!=0) {
        	ObjectNode deptsNode=BpmnJsonConverterUtil.convertRefValue(userTask.getDepts());
        	propertiesNode.put("depts", deptsNode);
        }*/
        
        /* begin_审批任务测试数据_加属性 */
		if ((userTask.getMultiinstance_handletype() != null))
			setPropertyValue("multiinstance_handletype",
					userTask.getMultiinstance_handletype(), propertiesNode);

		if ((userTask.getMultiinstance_outtertrantablename() != null))
			setPropertyValue("multiinstance_otmtname",
					userTask.getMultiinstance_outtertrantablename(),
					propertiesNode);

		if ((userTask.getRemark() != null))
			setPropertyValue("remark", userTask.getRemark(), propertiesNode);
		
		if ((userTask.getProcessname() != null))
			setPropertyValue("processname", userTask.getProcessname(), propertiesNode);
		
		
		if ((userTask.getMultiinstance_maintablename() != null))
			setPropertyValue("multiinstance_maintablename",
					userTask.getMultiinstance_maintablename(),
					propertiesNode);//扩展流程图属性_主表名称
		
		if ((userTask.getIdfield() != null))
			setPropertyValue("idfield", userTask.getIdfield(), propertiesNode);
		
		if ((userTask.getStartnodetype() != null))
      setPropertyValue("startnodetype", userTask.getStartnodetype(), propertiesNode);
		
		if ((userTask.getEndnodetype() != null))
      setPropertyValue("endnodetype", userTask.getEndnodetype(), propertiesNode);
		

		/* end_审批任务测试数据_加属性 */
		// TODO
		if ((userTask.getNodenumber() != null))
			setPropertyValue("nodenumber", userTask.getNodenumber(),
					propertiesNode);
		if ((userTask.getNodename() != null))
			setPropertyValue("nodename", userTask.getNodename(), propertiesNode);
		if ((userTask.getNodetype() != null))
			setPropertyValue("nodetype", userTask.getNodetype(), propertiesNode);
		if ((userTask.getMultiinstance_person() != null))
			setPropertyValue("multiinstance_person",
					userTask.getMultiinstance_person(), propertiesNode);
		if ((userTask.getItmti() != null))
			setPropertyValue("itmti", userTask.getItmti(), propertiesNode);
		if ((userTask.getOtmti() != null))
			setPropertyValue("otmti", userTask.getOtmti(), propertiesNode);
		if ((userTask.getEtreid() != null))
			setPropertyValue("etreid", userTask.getEtreid(), propertiesNode);
		
		if ((userTask.getFunctionauth() != null))
			setPropertyValue("functionauth", userTask.getFunctionauth(), propertiesNode);
        
        if (userTask.getLoopCharacteristics() != null) {
            MultiInstanceLoopCharacteristics loopDef = userTask.getLoopCharacteristics();
            if (StringUtils.isNotEmpty(loopDef.getLoopCardinality()) || StringUtils.isNotEmpty(loopDef.getInputDataItem()) || StringUtils.isNotEmpty(loopDef.getCompletionCondition())) {

                if (loopDef.isSequential() == true) {
                    propertiesNode.put("multiinstance_model", "Sequential");
                } else if(loopDef.getCompletionCondition()!=null&&loopDef.getCompletionCondition().equals("${nrOfCompletedInstances==1}")){
                    propertiesNode.put("multiinstance_model", "Grab");
                } else if(loopDef.getCompletionCondition()!=null&&loopDef.getCompletionCondition().equals("${nrOfCompletedInstances/nrOfInstances==1}")){
                    propertiesNode.put("multiinstance_model", "Sign");
                } else {
                	propertiesNode.put("multiinstance_model", "Parallel");
                }

                if (StringUtils.isNotEmpty(loopDef.getLoopCardinality())) {
                    propertiesNode.put(PROPERTY_MULTIINSTANCE_CARDINALITY, loopDef.getLoopCardinality());
                }
                if (StringUtils.isNotEmpty(loopDef.getInputDataItem())) {
                    propertiesNode.put(PROPERTY_MULTIINSTANCE_COLLECTION, loopDef.getInputDataItem());
                }
                if (StringUtils.isNotEmpty(loopDef.getElementVariable())) {
                    propertiesNode.put(PROPERTY_MULTIINSTANCE_VARIABLE, loopDef.getElementVariable());
                }
                if (StringUtils.isNotEmpty(loopDef.getCompletionCondition())) {
                    propertiesNode.put(PROPERTY_MULTIINSTANCE_CONDITION, loopDef.getCompletionCondition());
                }
            }
        }
        addFormProperties(userTask.getFormProperties(), propertiesNode);
    }
  
    @Override
    protected FlowElement convertJsonToElement(JsonNode elementNode, JsonNode modelNode, Map<String, JsonNode> shapeMap) {
    	ApproveUserTask task = new ApproveUserTask();
        task.setPriority(getPropertyValueAsString(PROPERTY_USERTASK_PRIORITY, elementNode));
        String formKey = getPropertyValueAsString(PROPERTY_FORMKEY, elementNode);
        if (StringUtils.isNotEmpty(formKey)) {
            task.setFormKey(formKey);
        }
        task.setDueDate(getPropertyValueAsString(PROPERTY_USERTASK_DUEDATE, elementNode));
        task.setCategory(getPropertyValueAsString(PROPERTY_USERTASK_CATEGORY, elementNode));
        ProcessParticipantItem participantXmlNode=null;
        ProcessParticipant processParticipant=new ProcessParticipant();
        List <ProcessParticipantItem> processParticipantItemList=new ArrayList<ProcessParticipantItem>();
        /* Map<String, ParticipantConfig> userServiceImpl=BpmServiceUtils.getBpmEngineConfiguration().getParticipantService().getParticipantConfigs();
  		Collection<ParticipantConfig> c = userServiceImpl.values();
  		Iterator it = c.iterator();
  		for (; it.hasNext();) {
  			ParticipantConfig tempConfig=(ParticipantConfig) it.next();
  			JsonNode participantNode = getProperty(tempConfig.getCode().toLowerCase(), elementNode);
  	        if (participantNode != null) {
  	            JsonNode participantDefNode = participantNode.get("refResultData");
  	            if (participantDefNode != null) {
  	            	participantXmlNode=BpmnJsonConverterUtil.convertRefDataToXmlData(participantDefNode);
  	            	participantXmlNode.setType(tempConfig.getCode());  
  	            	processParticipantItemList.add(participantXmlNode);
  	            }else if(participantNode.asBoolean())
  	            {
  	            	participantXmlNode=new ProcessParticipantItem();
  	            	participantXmlNode.setType(tempConfig.getCode());  
  	            	processParticipantItemList.add(participantXmlNode);
  	            }
  	        }
  		}*/
        processParticipant.setProcessParticipantItems(processParticipantItemList.toArray(new ProcessParticipantItem[processParticipantItemList.size()]));
        String json=processParticipant.toJSONStr().replaceAll("\"", "'");
        String allUser="${bpmBean.getUser(\""+json+"\")}";
        String multiInstanceType = getPropertyValueAsString("multiinstance_model", elementNode);
        String multiInstanceCardinality = getPropertyValueAsString(PROPERTY_MULTIINSTANCE_CARDINALITY, elementNode);
        String multiInstanceCondition = getPropertyValueAsString(PROPERTY_MULTIINSTANCE_CONDITION, elementNode);

       // if (StringUtils.isNotEmpty(multiInstanceType) && "none".equalsIgnoreCase(multiInstanceType) == false) {

            String multiInstanceVariable = getPropertyValueAsString(PROPERTY_MULTIINSTANCE_VARIABLE, elementNode);

            MultiInstanceLoopCharacteristics multiInstanceObject = new MultiInstanceLoopCharacteristics();
            if ("Grab".equalsIgnoreCase(multiInstanceType)) {
                multiInstanceObject.setSequential(false);
                multiInstanceCondition="${nrOfCompletedInstances==1}";
            } else if("Sign".equalsIgnoreCase(multiInstanceType)){
                multiInstanceObject.setSequential(false);
                multiInstanceCondition="${nrOfCompletedInstances/nrOfInstances==1}";
            } else if("Sequential".equalsIgnoreCase(multiInstanceType)){
                multiInstanceObject.setSequential(true);
            } else if("Parallel".equalsIgnoreCase(multiInstanceType)){
                multiInstanceObject.setSequential(false);
            }
            multiInstanceObject.setLoopCardinality(multiInstanceCardinality);
            multiInstanceObject.setInputDataItem(allUser);
            multiInstanceObject.setElementVariable("assignee");
            multiInstanceObject.setCompletionCondition(multiInstanceCondition);
            task.setLoopCharacteristics(multiInstanceObject);
            task.setAssignee("${assignee}");
       // }
        JsonNode  jsonNode =getProperty("assignable", elementNode);
        
        Boolean assignAble =null;
        if(jsonNode!=null){
        	assignAble=jsonNode.asBoolean();
        }
        if(assignAble!=null&&assignAble)
        	task.setAssignAble(assignAble);
        else
        	task.setAssignAble(false);
        
        /* begin_审批任务测试数据_加属性 */
		jsonNode = getProperty("multiinstance_handletype", elementNode);
		String multiinstance_handletype = null;
		if (jsonNode != null) {
			multiinstance_handletype = String.valueOf(jsonNode.asText());
		}
		if ((multiinstance_handletype != null))
			task.setMultiinstance_handletype(multiinstance_handletype);
		else {
			task.setMultiinstance_handletype("");
		}

		jsonNode = getProperty("multiinstance_outtertrantablename", elementNode);
		String multiinstance_outtertrantablename = null;
		if (jsonNode != null) {
			multiinstance_outtertrantablename = String.valueOf(jsonNode
					.asText());
		}
		if ((multiinstance_outtertrantablename != null))
			task.setMultiinstance_outtertrantablename(multiinstance_outtertrantablename);
		else {
			task.setMultiinstance_outtertrantablename("");
		}

		jsonNode = getProperty("remark", elementNode);
		String remark = null;
		if (jsonNode != null) {
			remark = String.valueOf(jsonNode.asText());
		}
		if ((remark != null))
			task.setRemark(remark);
		else {
			task.setRemark("");
		}
		
		
		jsonNode = getProperty("processname", elementNode);
		String processname = null;
		if (jsonNode != null) {
			processname = String.valueOf(jsonNode.asText());
		}
		if ((processname != null))
			task.setProcessname(processname);
		else {
			task.setProcessname("");
		}
		
		jsonNode = getProperty("idfield", elementNode);
		String idfield = null;
		if (jsonNode != null) {
			idfield = String.valueOf(jsonNode.asText());
		}
		if ((idfield != null))
			task.setIdfield(idfield);
		else {
			task.setIdfield("");
		}
		
		//begin_扩展流程图属性_主表名称_2017_04_04
			jsonNode = getProperty("multiinstance_maintablename", elementNode);
			String multiinstance_maintablename = null;
			if (jsonNode != null) {
				multiinstance_maintablename = String.valueOf(jsonNode
						.asText());
			}
			if ((multiinstance_maintablename != null))
				task.setMultiinstance_maintablename(multiinstance_maintablename);
			else {
				task.setMultiinstance_maintablename("");
			}
		//end_扩展流程图属性_主表名称_2017_04_04
			
			jsonNode = getProperty("startnodetype", elementNode);
	    String startnodetype = null;
	    if (jsonNode != null) {
	      startnodetype = String.valueOf(jsonNode.asText());
	    }
	    if ((startnodetype != null))
	      task.setStartnodetype(startnodetype);
	    else {
	      task.setStartnodetype("");
	    }
	    
	    jsonNode = getProperty("endnodetype", elementNode);
      String endnodetype = null;
      if (jsonNode != null) {
        endnodetype = String.valueOf(jsonNode.asText());
      }
      if ((endnodetype != null))
        task.setEndnodetype(endnodetype);
      else {
        task.setEndnodetype("");
      }
			
			

		/* end_审批任务测试数据_加属性 */
		// TODO
		jsonNode = getProperty("nodenumber", elementNode);
		String nodenumber = null;
		if (jsonNode != null) {
			nodenumber = String.valueOf(jsonNode.asText());
		}
		if ((nodenumber != null))
			task.setNodenumber(nodenumber);
		else {
			task.setNodenumber(nodenumber);
		}

		jsonNode = getProperty("nodename", elementNode);
		String nodename = null;
		if (jsonNode != null) {
			nodename = String.valueOf(jsonNode.asText());
		}
		if ((nodename != null))
			task.setNodename(nodename);
		else {
			task.setNodename(nodename);
		}

		jsonNode = getProperty("nodetype", elementNode);
		String nodetype = null;
		if (jsonNode != null) {
			nodetype = String.valueOf(jsonNode.asText());
		}
		if ((nodetype != null))
			task.setNodetype(nodetype);
		else {
			task.setNodetype(nodetype);
		}

		jsonNode = getProperty("multiinstance_person", elementNode);
		String multiinstance_person = null;
		if (jsonNode != null) {
			multiinstance_person = String.valueOf(jsonNode.asText());
		}
		if ((multiinstance_person != null))
			task.setMultiinstance_person(multiinstance_person);
		else {
			task.setMultiinstance_person(multiinstance_person);
		}

		jsonNode = getProperty("itmti", elementNode);
		String itmti = null;
		if (jsonNode != null) {
			itmti = String.valueOf(jsonNode.asText());
		}
		if ((itmti != null))
			task.setItmti(itmti);
		else {
			task.setMultiinstance_person(itmti);
		}

		jsonNode = getProperty("otmti", elementNode);
		String otmti = null;
		if (jsonNode != null) {
			otmti = String.valueOf(jsonNode.asText());
		}
		if ((otmti != null))
			task.setOtmti(otmti);
		else {
			task.setOtmti(otmti);
		}

		jsonNode = getProperty("etreid", elementNode);
		String etreid = null;
		if (jsonNode != null) {
			etreid = String.valueOf(jsonNode.asText());
		}
		if ((etreid != null))
			task.setEtreid(etreid);
		else {
			task.setEtreid(etreid);
		}
		
		jsonNode = getProperty("functionauth", elementNode);
		String functionauth = null;
		if (jsonNode != null) {
			functionauth = String.valueOf(jsonNode.asText());
		}
		if ((functionauth != null))
			task.setFunctionauth(functionauth);
		else {
			task.setFunctionauth(functionauth);
		}
        
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
