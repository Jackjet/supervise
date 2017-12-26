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
package gov.df.fap.service.workflow.activiti;

import gov.df.fap.bean.workflow.activiti.ApproveUserTask;

import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.AbstractActivityBpmnParseHandler;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.form.DefaultTaskFormHandler;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;

import com.alibaba.dubbo.common.utils.StringUtils;




/**
 * @author Joram Barrez
 */
public class ApproveUserTaskParseHandler extends AbstractActivityBpmnParseHandler<ApproveUserTask> {
  
  public static final String PROPERTY_TASK_DEFINITION = "taskDefinition";
  
  public Class< ? extends BaseElement> getHandledType() {
    return ApproveUserTask.class;
  }
  
  protected void executeParse(BpmnParse bpmnParse, ApproveUserTask userTask) {
    ActivityImpl activity = createActivityOnCurrentScope(bpmnParse, userTask, BpmnXMLConstants.ELEMENT_TASK_USER);
    
    activity.setAsync(userTask.isAsynchronous());
    activity.setExclusive(!userTask.isNotExclusive()); 
    
    TaskDefinition taskDefinition = parseTaskDefinition(bpmnParse, userTask, userTask.getId(), (ProcessDefinitionEntity) bpmnParse.getCurrentScope().getProcessDefinition());
    activity.setProperty(PROPERTY_TASK_DEFINITION, taskDefinition);
    //任务创建
    ActivitiListener activitiListener=new ActivitiListener();
	activitiListener.setEvent(TaskListener.EVENTNAME_CREATE);
	activitiListener.setId("defaultUbpmTaskCreate");
	//activitiListener.setImplementation(DefaultTaskCreateListener.class.getName());
	activitiListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);

    taskDefinition.addTaskListener(TaskListener.EVENTNAME_CREATE,  bpmnParse.getListenerFactory()
            .createClassDelegateTaskListener(activitiListener));
    
    if(userTask.getAssignAble()!=null)
    	activity.setProperty("assignAble", userTask.getAssignAble().toString());
		    /* begin_审批任务测试数据_加属性 */
			if (userTask.getMultiinstance_handletype() != null)
				activity.setProperty("multiinstance_handletype", userTask
						.getMultiinstance_handletype().toString());
		
			if (userTask.getMultiinstance_outtertrantablename() != null)
				activity.setProperty("multiinstance_outtertrantablename", userTask
						.getMultiinstance_outtertrantablename().toString());
		
			if (userTask.getRemark() != null)
				activity.setProperty("remark", userTask.getRemark().toString());
			
			if (userTask.getIdfield() != null)
				activity.setProperty("idfield", userTask.getIdfield().toString());
			
			if (userTask.getStartnodetype() != null)
        activity.setProperty("startnodetype", userTask.getStartnodetype().toString());
			
			if (userTask.getEndnodetype() != null)
        activity.setProperty("endnodetype", userTask.getEndnodetype().toString());
			
			
			  //扩展流程图属性
			if (userTask.getMultiinstance_maintablename() != null)
				activity.setProperty("multiinstance_maintablename", userTask
						.getMultiinstance_maintablename().toString());
			
			if (userTask.getProcessname() != null)
				activity.setProperty("processname", userTask.getProcessname().toString());
			
			/* end_审批任务测试数据_加属性 */
			// TODO
			if (userTask.getNodenumber() != null)
				activity.setProperty("nodenumber", userTask.getNodenumber()
						.toString());
			if (userTask.getNodename() != null)
				activity.setProperty("nodename", userTask.getNodename().toString());
			if (userTask.getNodetype() != null)
				activity.setProperty("nodetype", userTask.getNodetype().toString());
			if (userTask.getMultiinstance_person() != null)
				activity.setProperty("multiinstance_person", userTask
						.getMultiinstance_person().toString());
			if (userTask.getItmti() != null)
				activity.setProperty("itmti", userTask.getItmti().toString());
			if (userTask.getOtmti() != null)
				activity.setProperty("otmti", userTask.getOtmti().toString());
			if (userTask.getEtreid() != null)
				activity.setProperty("etreid", userTask.getEtreid().toString());
			if (userTask.getFunctionauth() != null)
				activity.setProperty("functionauth", userTask.getFunctionauth().toString());
    activity.setActivityBehavior(bpmnParse.getActivityBehaviorFactory().createUserTaskActivityBehavior(userTask, taskDefinition));
  }
  
  public TaskDefinition parseTaskDefinition(BpmnParse bpmnParse, UserTask userTask, String taskDefinitionKey, ProcessDefinitionEntity processDefinition) {
    TaskFormHandler taskFormHandler = new DefaultTaskFormHandler();
    taskFormHandler.parseConfiguration(userTask.getFormProperties(), userTask.getFormKey(), bpmnParse.getDeployment(), processDefinition);

    TaskDefinition taskDefinition = new TaskDefinition(taskFormHandler);

    taskDefinition.setKey(taskDefinitionKey);
    processDefinition.getTaskDefinitions().put(taskDefinitionKey, taskDefinition);
    ExpressionManager expressionManager = bpmnParse.getExpressionManager();

    if (StringUtils.isNotEmpty(userTask.getName())) {
      taskDefinition.setNameExpression(expressionManager.createExpression(userTask.getName()));
    }

    if (StringUtils.isNotEmpty(userTask.getDocumentation())) {
      taskDefinition.setDescriptionExpression(expressionManager.createExpression(userTask.getDocumentation()));
    }

    if (StringUtils.isNotEmpty(userTask.getAssignee())) {
      taskDefinition.setAssigneeExpression(expressionManager.createExpression(userTask.getAssignee()));
    }
    if (StringUtils.isNotEmpty(userTask.getOwner())) {
      taskDefinition.setOwnerExpression(expressionManager.createExpression(userTask.getOwner()));
    }
    for (String candidateUser : userTask.getCandidateUsers()) {
      taskDefinition.addCandidateUserIdExpression(expressionManager.createExpression(candidateUser));
    }
    for (String candidateGroup : userTask.getCandidateGroups()) {
      taskDefinition.addCandidateGroupIdExpression(expressionManager.createExpression(candidateGroup));
    }
    
    // Activiti custom extension
    
    // Task listeners
    for (ActivitiListener taskListener : userTask.getTaskListeners()) {
      taskDefinition.addTaskListener(taskListener.getEvent(), createTaskListener(bpmnParse, taskListener, userTask.getId()));
    }

    // Due date
    if (StringUtils.isNotEmpty(userTask.getDueDate())) {
      taskDefinition.setDueDateExpression(expressionManager.createExpression(userTask.getDueDate()));
    }
    
    // Category
    if (StringUtils.isNotEmpty(userTask.getCategory())) {
    	taskDefinition.setCategoryExpression(expressionManager.createExpression(userTask.getCategory()));
    }
    
    // Priority
    if (StringUtils.isNotEmpty(userTask.getPriority())) {
      taskDefinition.setPriorityExpression(expressionManager.createExpression(userTask.getPriority()));
    }
    
    if (StringUtils.isNotEmpty(userTask.getFormKey())) {
    	taskDefinition.setFormKeyExpression(expressionManager.createExpression(userTask.getFormKey()));
    }

    // CustomUserIdentityLinks
   /* for (String customUserIdentityLinkType : userTask.getCustomUserIdentityLinks().keySet()) {
    	Set<Expression> userIdentityLinkExpression = new HashSet<Expression>();
    	for (String userIdentityLink : userTask.getCustomUserIdentityLinks().get(customUserIdentityLinkType)) {
    		userIdentityLinkExpression.add(expressionManager.createExpression(userIdentityLink));
    	}
    	taskDefinition.addCustomUserIdentityLinkExpression(customUserIdentityLinkType, userIdentityLinkExpression);
      }
    */
    // CustomGroupIdentityLinks
   /* for (String customGroupIdentityLinkType : userTask.getCustomGroupIdentityLinks().keySet()) {
    	Set<Expression> groupIdentityLinkExpression = new HashSet<Expression>();
    	for (String groupIdentityLink : userTask.getCustomGroupIdentityLinks().get(customGroupIdentityLinkType)) {
    		groupIdentityLinkExpression.add(expressionManager.createExpression(groupIdentityLink));
    	}
    	taskDefinition.addCustomGroupIdentityLinkExpression(customGroupIdentityLinkType, groupIdentityLinkExpression);
      }*/

    return taskDefinition;
  }
  
  protected TaskListener createTaskListener(BpmnParse bpmnParse, ActivitiListener activitiListener, String taskId) {
    TaskListener taskListener = null;

    if (ImplementationType.IMPLEMENTATION_TYPE_CLASS.equalsIgnoreCase(activitiListener.getImplementationType())) {
      taskListener = bpmnParse.getListenerFactory().createClassDelegateTaskListener(activitiListener); 
    } else if (ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION.equalsIgnoreCase(activitiListener.getImplementationType())) {
      taskListener = bpmnParse.getListenerFactory().createExpressionTaskListener(activitiListener);
    } else if (ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION.equalsIgnoreCase(activitiListener.getImplementationType())) {
      taskListener = bpmnParse.getListenerFactory().createDelegateExpressionTaskListener(activitiListener);
    }
    return taskListener;
  }
}
