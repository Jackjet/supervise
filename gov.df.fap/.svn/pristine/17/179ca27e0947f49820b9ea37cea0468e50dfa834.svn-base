package gov.df.fap.service.util.wf.activiti.ext;

import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.UserTask;

public class FlowJumpNode extends UserTask
{
  protected List<SequenceJumpFlow> incomingFlowsOfJump = new ArrayList();
  protected List<SequenceJumpFlow> outgoingFlowsOfJump = new ArrayList();

  /*public List<SequenceJumpFlow> getIncomingFlows() {
    return this.incomingFlows;
  }

  public void setIncomingFlows(List<SequenceJumpFlow> incomingFlows) {
    this.incomingFlows = incomingFlows;
  }

  public List<SequenceJumpFlow> getOutgoingFlows() {
    return this.outgoingFlows;
  }

  public void setOutgoingFlows(List<SequenceJumpFlow> outgoingFlows) {
    this.outgoingFlows = outgoingFlows;
  }*/


  
  public void setValues(FlowJumpNode otherNode) {
    super.setValues(otherNode);
  }

  public List<SequenceJumpFlow> getIncomingFlowsOfJump() {
    return incomingFlowsOfJump;
  }

  public void setIncomingFlowsOfJump(List<SequenceJumpFlow> incomingFlowsOfJump) {
    this.incomingFlowsOfJump = incomingFlowsOfJump;
  }

  public List<SequenceJumpFlow> getOutgoingFlowsOfJump() {
    return outgoingFlowsOfJump;
  }

  public void setOutgoingFlowsOfJump(List<SequenceJumpFlow> outgoingFlowsOfJump) {
    this.outgoingFlowsOfJump = outgoingFlowsOfJump;
  }
}