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
import gov.df.fap.service.util.wf.activiti.ext.SequenceJumpFlow;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.activiti.bpmn.converter.SequenceFlowXMLConverter;
import org.activiti.bpmn.converter.util.BpmnXMLUtil;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.SequenceFlow;
import org.apache.commons.lang3.StringUtils;


/**
 * @author Tijs Rademakers
 */
public class SequenceJumpFlowXMLConverter extends SequenceFlowXMLConverter {
  
  public Class<? extends BaseElement> getBpmnElementType() {
    return SequenceJumpFlow.class;
  }
  
  @Override
  protected String getXMLElementName() {
    return "SequenceJumpFlow";
  }
  
  @Override
  protected BaseElement convertXMLToElement(XMLStreamReader xtr, BpmnModel model) throws Exception {
    SequenceJumpFlow sequenceJumpFlow = new SequenceJumpFlow();
    BpmnXMLUtil.addXMLLocation(sequenceJumpFlow, xtr);
    sequenceJumpFlow.setSourceRef(xtr.getAttributeValue(null, ATTRIBUTE_FLOW_SOURCE_REF));
    sequenceJumpFlow.setTargetRef(xtr.getAttributeValue(null, ATTRIBUTE_FLOW_TARGET_REF));
    sequenceJumpFlow.setName(xtr.getAttributeValue(null, ATTRIBUTE_NAME));
    
    parseChildElements(getXMLElementName(), sequenceJumpFlow, model, xtr);
    
    return sequenceJumpFlow;
  }

  @Override
  protected void writeAdditionalAttributes(BaseElement element, BpmnModel model, XMLStreamWriter xtw) throws Exception {
    SequenceJumpFlow sequenceJumpFlow = (SequenceJumpFlow) element;
    writeDefaultAttribute(ATTRIBUTE_FLOW_SOURCE_REF, sequenceJumpFlow.getSourceRef(), xtw);
    writeDefaultAttribute(ATTRIBUTE_FLOW_TARGET_REF, sequenceJumpFlow.getTargetRef(), xtw);
  }
  
  @Override
  protected void writeAdditionalChildElements(BaseElement element, BpmnModel model, XMLStreamWriter xtw) throws Exception {
    SequenceJumpFlow sequenceJumpFlow = (SequenceJumpFlow) element;
    
    if (StringUtils.isNotEmpty(sequenceJumpFlow.getConditionExpression())) {
      xtw.writeStartElement(ELEMENT_FLOW_CONDITION);
      xtw.writeAttribute(XSI_PREFIX, XSI_NAMESPACE, "type", "tFormalExpression");
      xtw.writeCData(sequenceJumpFlow.getConditionExpression());
      xtw.writeEndElement();
    }
  }
}
