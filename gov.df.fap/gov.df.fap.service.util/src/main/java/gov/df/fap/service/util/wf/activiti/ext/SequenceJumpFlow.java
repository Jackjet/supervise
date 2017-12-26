package gov.df.fap.service.util.wf.activiti.ext;

import org.activiti.bpmn.model.FlowElement;

public class SequenceJumpFlow extends FlowElement {
  protected String conditionExpression;
  protected String sourceRef;
  protected String targetRef;

  public SequenceJumpFlow()
  {
  }

  public SequenceJumpFlow(String sourceRef, String targetRef)
  {
    this.sourceRef = sourceRef;
    this.targetRef = targetRef;
  }

  public String getConditionExpression() {
    return this.conditionExpression;
  }
  public void setConditionExpression(String conditionExpression) {
    this.conditionExpression = conditionExpression;
  }
  public String getSourceRef() {
    return this.sourceRef;
  }
  public void setSourceRef(String sourceRef) {
    this.sourceRef = sourceRef;
  }
  public String getTargetRef() {
    return this.targetRef;
  }
  public void setTargetRef(String targetRef) {
    this.targetRef = targetRef;
  }
  public String toString() {
    return this.sourceRef + " --> " + this.targetRef;
  }

  public SequenceJumpFlow clone() {
    SequenceJumpFlow clone = new SequenceJumpFlow();
    clone.setValues(this);
    return clone;
  }

  public void setValues(SequenceJumpFlow otherFlow) {
    super.setValues(otherFlow);
    setConditionExpression(otherFlow.getConditionExpression());
    setSourceRef(otherFlow.getSourceRef());
    setTargetRef(otherFlow.getTargetRef());
  }}
