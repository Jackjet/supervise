package gov.df.fap.service.workflow.activiti;

import gov.df.fap.bean.workflow.activiti.ApproveUserTask;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.engine.impl.history.parse.UserTaskHistoryParseHandler;


public class ApproveUserTaskHistoryParseHandler extends UserTaskHistoryParseHandler{
	
	 protected Class< ? extends BaseElement> getHandledType() {
		    return ApproveUserTask.class;
		  }

}
