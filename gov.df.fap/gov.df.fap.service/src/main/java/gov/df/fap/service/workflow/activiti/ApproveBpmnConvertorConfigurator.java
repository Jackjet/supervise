package gov.df.fap.service.workflow.activiti;

import gov.df.fap.bean.workflow.activiti.ApproveUserTask;
import gov.df.fap.service.util.wf.activiti.ApproveUserTaskJsonConverter;
import gov.df.fap.service.util.wf.activiti.ApproveUserTaskXMLConverter;
import gov.df.fap.service.util.wf.activiti.BpmnJsonConverter;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.engine.cfg.ProcessEngineConfigurator;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.history.parse.FlowNodeHistoryParseHandler;
import org.springframework.stereotype.Component;


@Component
public class ApproveBpmnConvertorConfigurator implements ProcessEngineConfigurator {

	@Override
	public void beforeInit(
			ProcessEngineConfigurationImpl processEngineConfiguration) {
		BpmnXMLConverter.addConverter(new ApproveUserTaskXMLConverter());
		FlowNodeHistoryParseHandler flowNodeHistoryParseHandler=new FlowNodeHistoryParseHandler();
		processEngineConfiguration.getPreBpmnParseHandlers().add(
				new ApproveUserTaskParseHandler());
		processEngineConfiguration.getPreBpmnParseHandlers().add(
				new ApproveUserTaskHistoryParseHandler());
		//processEngineConfiguration.getPreBpmnParseHandlers().add(flowNodeHistoryParseHandler);
		flowNodeHistoryParseHandler.getHandledTypes().add(ApproveUserTask.class);
		BpmnJsonConverter.getConvertersToJsonMap().put(ApproveUserTask.class, ApproveUserTaskJsonConverter.class);
		BpmnJsonConverter.getConvertersToBpmnMap().put("ApproveUserTask",ApproveUserTaskJsonConverter.class );
		BpmnJsonConverter.getDiRectangles().add("ApproveUserTask");
	}

	@Override
	public void configure(
			ProcessEngineConfigurationImpl processEngineConfiguration) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

}
