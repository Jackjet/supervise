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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.activiti.bpmn.converter.UserTaskXMLConverter;
import org.activiti.bpmn.converter.XMLStreamReaderUtil;
import org.activiti.bpmn.converter.child.BaseChildElementParser;
import org.activiti.bpmn.converter.util.BpmnXMLUtil;
import org.activiti.bpmn.converter.util.CommaSplitter;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ExtensionAttribute;
import org.activiti.bpmn.model.UserTask;

import com.alibaba.dubbo.common.utils.StringUtils;

/**
 * @author Tijs Rademakers, Saeid Mirzaei
 */
public class ApproveUserTaskXMLConverter extends UserTaskXMLConverter {
  
  protected Map<String, BaseChildElementParser> childParserMap = new HashMap<String, BaseChildElementParser>();

  /** default attributes taken from bpmn spec and from activiti extension */
  protected static final List<ExtensionAttribute> defaultUserTaskAttributes = Arrays.asList(
      new ExtensionAttribute(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_FORM_FORMKEY), 
      new ExtensionAttribute(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_USER_DUEDATE), 
      new ExtensionAttribute(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_USER_ASSIGNEE), 
      new ExtensionAttribute(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_USER_PRIORITY), 
      new ExtensionAttribute(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_USER_CANDIDATEUSERS), 
      new ExtensionAttribute(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_USER_CANDIDATEGROUPS)
  );

 /* public ApproveUserTaskXMLConverter() {
    HumanPerformerParser humanPerformerParser = new HumanPerformerParser();
    childParserMap.put(humanPerformerParser.getElementName(), humanPerformerParser);
    PotentialOwnerParser potentialOwnerParser = new PotentialOwnerParser();
    childParserMap.put(potentialOwnerParser.getElementName(), potentialOwnerParser);
    CustomIdentityLinkParser customIdentityLinkParser = new CustomIdentityLinkParser();
    childParserMap.put(customIdentityLinkParser.getElementName(), customIdentityLinkParser);
  }*/
  
  public Class<? extends BaseElement> getBpmnElementType() {
    return ApproveUserTask.class;
  }
  
  @Override
  protected String getXMLElementName() {
    return "approveUserTask";
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected BaseElement convertXMLToElement(XMLStreamReader xtr, BpmnModel model) throws Exception {
    String formKey = xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_FORM_FORMKEY);
    ApproveUserTask userTask = null;
    if (userTask == null) {
      userTask = new ApproveUserTask();
    }
    BpmnXMLUtil.addXMLLocation(userTask, xtr);
    userTask.setDueDate(xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_USER_DUEDATE));
    userTask.setPriority(xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_USER_PRIORITY));
    userTask.setCategory(xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_USER_CATEGORY));
    userTask.setFormKey(formKey);
    if("true".equals(xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE,"assignAble")))
    	userTask.setAssignAble(true);
    if (StringUtils.isNotEmpty(xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_USER_CANDIDATEGROUPS))) {
      String expression = xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_USER_CANDIDATEGROUPS);
      userTask.getCandidateGroups().addAll(parseDelimitedList(expression));
    }
    /*begin_审批任务测试数据_加属性*/
	    userTask.setMultiinstance_handletype(xtr.getAttributeValue("http://activiti.org/bpmn","multiinstance_handletype"));
	    
	    userTask.setMultiinstance_outtertrantablename(xtr.getAttributeValue("http://activiti.org/bpmn","multiinstance_outtertrantablename"));
	    
	    userTask.setRemark(xtr.getAttributeValue("http://activiti.org/bpmn","remark"));
	    
	    userTask.setIdfield(xtr.getAttributeValue("http://activiti.org/bpmn","idfield"));
	    
	    userTask.setMultiinstance_maintablename(xtr.getAttributeValue("http://activiti.org/bpmn","multiinstance_maintablename"));//路程图扩展属性
	    
	    userTask.setProcessname(xtr.getAttributeValue("http://activiti.org/bpmn","processname"));
	    
	    userTask.setStartnodetype(xtr.getAttributeValue("http://activiti.org/bpmn","startnodetype"));
	    
	    userTask.setEndnodetype(xtr.getAttributeValue("http://activiti.org/bpmn","endnodetype"));
	    
	    
   /*end_审批任务测试数据_加属性*/
    
    // TODO
    userTask.setNodenumber(xtr.getAttributeValue("http://activiti.org/bpmn","nodenumber"));
    userTask.setNodename(xtr.getAttributeValue("http://activiti.org/bpmn","nodename"));
    userTask.setNodetype(xtr.getAttributeValue("http://activiti.org/bpmn","nodetype"));
    userTask.setMultiinstance_person(xtr.getAttributeValue("http://activiti.org/bpmn","multiinstance_person"));
    userTask.setItmti(xtr.getAttributeValue("http://activiti.org/bpmn","itmti"));
    userTask.setOtmti(xtr.getAttributeValue("http://activiti.org/bpmn","otmti"));
    userTask.setEtreid(xtr.getAttributeValue("http://activiti.org/bpmn","etreid"));
    userTask.setFunctionauth(xtr.getAttributeValue("http://activiti.org/bpmn","functionauth"));

    BpmnXMLUtil.addCustomAttributes(xtr, userTask, defaultElementAttributes, 
        defaultActivityAttributes, defaultUserTaskAttributes);
    parseChildElements(getXMLElementName(), userTask, childParserMap, model, xtr);
    
    userTask.setAssignee("${assignee}");
    return userTask;
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected void writeAdditionalAttributes(BaseElement element, BpmnModel model, XMLStreamWriter xtw) throws Exception {
    ApproveUserTask userTask = (ApproveUserTask) element;
    writeQualifiedAttribute(ATTRIBUTE_TASK_USER_ASSIGNEE, userTask.getAssignee(), xtw);
    writeQualifiedAttribute(ATTRIBUTE_TASK_USER_OWNER, userTask.getOwner(), xtw);
    writeQualifiedAttribute("roles", convertToDelimitedString(userTask.getRoles()), xtw);
    writeQualifiedAttribute("orgs", convertToDelimitedString(userTask.getOrgs()), xtw);
    if(userTask.getAssignAble()!=null)
    	writeQualifiedAttribute("assignAble",userTask.getAssignAble().toString(), xtw);
    writeQualifiedAttribute("usergroups", convertToDelimitedString(userTask.getUserGroups()), xtw);
    writeQualifiedAttribute(ATTRIBUTE_TASK_USER_CANDIDATEUSERS, convertToDelimitedString(userTask.getCandidateUsers()), xtw);
    writeQualifiedAttribute(ATTRIBUTE_TASK_USER_CANDIDATEGROUPS, convertToDelimitedString(userTask.getCandidateGroups()), xtw);
    writeQualifiedAttribute(ATTRIBUTE_TASK_USER_DUEDATE, userTask.getDueDate(), xtw);
    writeQualifiedAttribute(ATTRIBUTE_TASK_USER_CATEGORY, userTask.getCategory(), xtw);
    writeQualifiedAttribute(ATTRIBUTE_FORM_FORMKEY, userTask.getFormKey(), xtw);
    if (userTask.getPriority() != null) {
      writeQualifiedAttribute(ATTRIBUTE_TASK_USER_PRIORITY, userTask.getPriority().toString(), xtw);
    }
    
    /*begin_审批任务测试数据_加属性*/
	    writeQualifiedAttribute("multiinstance_handletype", userTask.getMultiinstance_handletype(), xtw);
	    
	    writeQualifiedAttribute("multiinstance_outtertrantablename", userTask.getMultiinstance_outtertrantablename(), xtw);
	    
	    writeQualifiedAttribute("remark", userTask.getRemark(), xtw);
	    
	    writeQualifiedAttribute("processname", userTask.getProcessname(), xtw);
	    
	    writeQualifiedAttribute("idfield", userTask.getIdfield(), xtw);
	    
	    writeQualifiedAttribute("multiinstance_maintablename", userTask.getMultiinstance_maintablename(), xtw);//流程图扩展属性
	    
	    writeQualifiedAttribute("startnodetype", userTask.getStartnodetype(), xtw);
	    
	    writeQualifiedAttribute("endnodetype", userTask.getEndnodetype(), xtw);
	    
   /*end_审批任务测试数据_加属性*/
    
    //TODO
    writeQualifiedAttribute("nodenumber", userTask.getNodenumber(), xtw);
    writeQualifiedAttribute("nodename", userTask.getNodename(), xtw);
    writeQualifiedAttribute("nodetype", userTask.getNodetype(), xtw);
    writeQualifiedAttribute("multiinstance_person", userTask.getMultiinstance_person(), xtw);
    writeQualifiedAttribute("itmti", userTask.getItmti(), xtw);
    writeQualifiedAttribute("otmti", userTask.getOtmti(), xtw);
    writeQualifiedAttribute("etreid", userTask.getEtreid(), xtw);
    writeQualifiedAttribute("functionauth", userTask.getFunctionauth(), xtw);
    
    // write custom attributes
    BpmnXMLUtil.writeCustomAttributes(userTask.getAttributes().values(), xtw, defaultElementAttributes, 
        defaultActivityAttributes, defaultUserTaskAttributes);
  }
  
  /*@Override
  protected boolean writeExtensionChildElements(BaseElement element, boolean didWriteExtensionStartElement, XMLStreamWriter xtw) throws Exception {
    ApproveUserTask userTask = (ApproveUserTask) element;
    didWriteExtensionStartElement = writeFormProperties(userTask, didWriteExtensionStartElement, xtw);
    didWriteExtensionStartElement = writeCustomIdentities(element, didWriteExtensionStartElement, xtw);
    return didWriteExtensionStartElement;
  }*/
  
  /*protected boolean writeCustomIdentities(BaseElement element, boolean didWriteExtensionStartElement, XMLStreamWriter xtw) throws Exception {
	  ApproveUserTask userTask = (ApproveUserTask) element;
	  if(userTask.getCustomUserIdentityLinks().isEmpty() && userTask.getCustomGroupIdentityLinks().isEmpty())
		  return didWriteExtensionStartElement;
	    
	    
	    	if (didWriteExtensionStartElement == false) { 
	            xtw.writeStartElement(ELEMENT_EXTENSIONS);
	            didWriteExtensionStartElement = true;
	          }
	    	Set<String> identityLinkTypes = new HashSet<String>();
	    	identityLinkTypes.addAll(userTask.getCustomUserIdentityLinks().keySet());
	    	identityLinkTypes.addAll(userTask.getCustomGroupIdentityLinks().keySet());
	    	for (String identityType : identityLinkTypes) {
	    		writeCustomIdentities(userTask, identityType, userTask.getCustomUserIdentityLinks().get(identityType), userTask.getCustomGroupIdentityLinks().get(identityType), xtw);
	    	}
	    
	    return didWriteExtensionStartElement;
	  }*/

  protected void writeCustomIdentities(UserTask userTask,String identityType, Set<String> users, Set<String> groups, XMLStreamWriter xtw) throws Exception {
	  xtw.writeStartElement(ACTIVITI_EXTENSIONS_PREFIX, ELEMENT_CUSTOM_RESOURCE, ACTIVITI_EXTENSIONS_NAMESPACE);
	  xtw.writeAttribute(ACTIVITI_EXTENSIONS_PREFIX, ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_NAME, identityType);
      
    List<String> identityList = new ArrayList<String>();
    
    if (users!=null) {
      for (String userId: users) {
        identityList.add("user("+userId+")");
      }
    }
    
    if (groups!=null) {
      for (String groupId: groups){
    	  identityList.add("group("+groupId+")");
      }
    }
    
    String delimitedString = convertToDelimitedString(identityList);
    
    xtw.writeStartElement(ELEMENT_RESOURCE_ASSIGNMENT);
    xtw.writeStartElement(ELEMENT_FORMAL_EXPRESSION);
    xtw.writeCharacters(delimitedString);
    xtw.writeEndElement(); // End ELEMENT_FORMAL_EXPRESSION
    xtw.writeEndElement(); // End ELEMENT_RESOURCE_ASSIGNMENT
    
    xtw.writeEndElement(); // End ELEMENT_CUSTOM_RESOURCE
  }
  
  @Override
  protected void writeAdditionalChildElements(BaseElement element, BpmnModel model, XMLStreamWriter xtw) throws Exception {
  }
  
  public class HumanPerformerParser extends BaseChildElementParser {

    public String getElementName() {
      return "humanPerformer";
    }

    public void parseChildElement(XMLStreamReader xtr, BaseElement parentElement, BpmnModel model) throws Exception {
      String resourceElement = XMLStreamReaderUtil.moveDown(xtr);
      if (StringUtils.isNotEmpty(resourceElement) && ELEMENT_RESOURCE_ASSIGNMENT.equals(resourceElement)) {
        String expression = XMLStreamReaderUtil.moveDown(xtr);
        if (StringUtils.isNotEmpty(expression) && ELEMENT_FORMAL_EXPRESSION.equals(expression)) {
          ((UserTask) parentElement).setAssignee(xtr.getElementText());
        }
      }
    }
  }

 

  public class PotentialOwnerParser extends BaseChildElementParser {

    public String getElementName() {
      return "potentialOwner";
    }
    
   

    public void parseChildElement(XMLStreamReader xtr, BaseElement parentElement, BpmnModel model) throws Exception {
      String resourceElement = XMLStreamReaderUtil.moveDown(xtr);
      if (StringUtils.isNotEmpty(resourceElement) && ELEMENT_RESOURCE_ASSIGNMENT.equals(resourceElement)) {
        String expression = XMLStreamReaderUtil.moveDown(xtr);
        if (StringUtils.isNotEmpty(expression) && ELEMENT_FORMAL_EXPRESSION.equals(expression)) {
          
          List<String> assignmentList = CommaSplitter.splitCommas(xtr.getElementText());
          
          for (String assignmentValue : assignmentList) {
            if (assignmentValue == null)
              continue;
            assignmentValue = assignmentValue.trim();
            if (assignmentValue.length() == 0)
              continue;

            String userPrefix = "user(";
            String groupPrefix = "group(";
            if (assignmentValue.startsWith(userPrefix)) {
              assignmentValue = assignmentValue.substring(userPrefix.length(), assignmentValue.length() - 1).trim();
              ((UserTask) parentElement).getCandidateUsers().add(assignmentValue);
            } else if (assignmentValue.startsWith(groupPrefix)) {
              assignmentValue = assignmentValue.substring(groupPrefix.length(), assignmentValue.length() - 1).trim();
              ((UserTask) parentElement).getCandidateGroups().add(assignmentValue);
            } else {
              ((UserTask) parentElement).getCandidateGroups().add(assignmentValue);
            }
          }
        }
      }
    }
  }
  
  /*public class CustomIdentityLinkParser extends BaseChildElementParser {

    public String getElementName() {
	  return ELEMENT_CUSTOM_RESOURCE;
	}*/
	    
    /*public void parseChildElement(XMLStreamReader xtr, BaseElement parentElement, BpmnModel model) throws Exception {
	    String identityLinkType = xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_NAME);
	    
	    if(identityLinkType == null) return;
	    
	    String resourceElement = XMLStreamReaderUtil.moveDown(xtr);
	      if (StringUtils.isNotEmpty(resourceElement) && ELEMENT_RESOURCE_ASSIGNMENT.equals(resourceElement)) {
	        String expression = XMLStreamReaderUtil.moveDown(xtr);
	        if (StringUtils.isNotEmpty(expression) && ELEMENT_FORMAL_EXPRESSION.equals(expression)) {
	          
	          List<String> assignmentList = CommaSplitter.splitCommas(xtr.getElementText());
	          
	          for (String assignmentValue : assignmentList) {
	            if (assignmentValue == null)
	              continue;
	            assignmentValue = assignmentValue.trim();
	            if (assignmentValue.length() == 0)
	              continue;

	            String userPrefix = "user(";
	            String groupPrefix = "group(";
	            if (assignmentValue.startsWith(userPrefix)) {
	              assignmentValue = assignmentValue.substring(userPrefix.length(), assignmentValue.length() - 1).trim();
	              ((UserTask) parentElement).addCustomUserIdentityLink(assignmentValue, identityLinkType);
	            } else if (assignmentValue.startsWith(groupPrefix)) {
	              assignmentValue = assignmentValue.substring(groupPrefix.length(), assignmentValue.length() - 1).trim();
	              ((UserTask) parentElement).addCustomGroupIdentityLink(assignmentValue, identityLinkType);
	            } else {
	              ((UserTask) parentElement).addCustomGroupIdentityLink(assignmentValue, identityLinkType);
	            }
	          }
	        }
	      }
	    }*/
	  }

