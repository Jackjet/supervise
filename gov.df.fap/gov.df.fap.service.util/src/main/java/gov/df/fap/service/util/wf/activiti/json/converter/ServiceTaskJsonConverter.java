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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.FieldExtension;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.ServiceTask;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Tijs Rademakers
 */
public class ServiceTaskJsonConverter extends BaseBpmnJsonConverter {

    public static void fillTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap, Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {

        fillJsonTypes(convertersToBpmnMap);
        fillBpmnTypes(convertersToJsonMap);
    }

    public static void fillJsonTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap) {
        convertersToBpmnMap.put(STENCIL_TASK_SERVICE, ServiceTaskJsonConverter.class);
    }

    public static void fillBpmnTypes(Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
        convertersToJsonMap.put(ServiceTask.class, ServiceTaskJsonConverter.class);
    }

    protected String getStencilId(BaseElement baseElement) {
        return STENCIL_TASK_SERVICE;
    }

    protected void convertElementToJson(ObjectNode propertiesNode, BaseElement baseElement) {
        ServiceTask serviceTask = (ServiceTask) baseElement;

        if ("mail".equalsIgnoreCase(serviceTask.getType())) {
            setPropertyFieldValue(PROPERTY_MAILTASK_TO, serviceTask, propertiesNode);
            setPropertyFieldValue(PROPERTY_MAILTASK_FROM, serviceTask, propertiesNode);
            setPropertyFieldValue(PROPERTY_MAILTASK_SUBJECT, serviceTask, propertiesNode);
            setPropertyFieldValue(PROPERTY_MAILTASK_CC, serviceTask, propertiesNode);
            setPropertyFieldValue(PROPERTY_MAILTASK_BCC, serviceTask, propertiesNode);
            setPropertyFieldValue(PROPERTY_MAILTASK_TEXT, serviceTask, propertiesNode);
            setPropertyFieldValue(PROPERTY_MAILTASK_HTML, serviceTask, propertiesNode);
            setPropertyFieldValue(PROPERTY_MAILTASK_CHARSET, serviceTask, propertiesNode);

        } else if ("camel".equalsIgnoreCase(serviceTask.getType())) {
            setPropertyFieldValue(PROPERTY_CAMELTASK_CAMELCONTEXT, "camelContext", serviceTask, propertiesNode);
            
        } else if ("mule".equalsIgnoreCase(serviceTask.getType())) {
            setPropertyFieldValue(PROPERTY_MULETASK_ENDPOINT_URL, "endpointUrl", serviceTask, propertiesNode);
            setPropertyFieldValue(PROPERTY_MULETASK_LANGUAGE, "language", serviceTask, propertiesNode);
            setPropertyFieldValue(PROPERTY_MULETASK_PAYLOAD_EXPRESSION, "payloadExpression", serviceTask, propertiesNode);
            setPropertyFieldValue(PROPERTY_MULETASK_RESULT_VARIABLE, "resultVariable", serviceTask, propertiesNode);
            
        } else {

            if (ImplementationType.IMPLEMENTATION_TYPE_CLASS.equals(serviceTask.getImplementationType())) {
                propertiesNode.put(PROPERTY_SERVICETASK_CLASS, serviceTask.getImplementation());
            } else if (ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION.equals(serviceTask.getImplementationType())) {
                propertiesNode.put(PROPERTY_SERVICETASK_EXPRESSION, serviceTask.getImplementation());
            } else if (ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION.equals(serviceTask.getImplementationType())) {
                propertiesNode.put(PROPERTY_SERVICETASK_DELEGATE_EXPRESSION, serviceTask.getImplementation());
            }

            if (StringUtils.isNotEmpty(serviceTask.getResultVariableName())) {
                propertiesNode.put(PROPERTY_SERVICETASK_RESULT_VARIABLE, serviceTask.getResultVariableName());
            }
            for (FieldExtension extension : serviceTask.getFieldExtensions()) {
                if (StringUtils.isNotEmpty(extension.getExpression())&&extension.getFieldName().equals("wsdl")) {
                    propertiesNode.put("wsdl", extension.getExpression());    
                }
                if (StringUtils.isNotEmpty(extension.getExpression())&&extension.getFieldName().equals("operation")) {
                    propertiesNode.put("operation", extension.getExpression());
                }
                if (StringUtils.isNotEmpty(extension.getExpression())&&extension.getFieldName().equals("parameters")) {
                    propertiesNode.put("parameters", extension.getExpression());
                }
                if (StringUtils.isNotEmpty(extension.getStringValue())&&extension.getFieldName().equals("returnValue")) {
                    propertiesNode.put("returnvalue", extension.getStringValue());
                }
            }
            List<FieldExtension> fieldExensions=new ArrayList<FieldExtension>();
            for (FieldExtension extension : serviceTask.getFieldExtensions()) {
                if (!(extension.getFieldName().equals("wsdl")||
                		extension.getFieldName().equals("operation")||extension.getFieldName().equals("parameters")
                		||extension.getFieldName().equals("returnValue"))) {
                	fieldExensions.add(extension);
                }
            }
            addFieldExtensions(fieldExensions, propertiesNode);
        }
    }

    protected FlowElement convertJsonToElement(JsonNode elementNode, JsonNode modelNode, Map<String, JsonNode> shapeMap) {
        ServiceTask task = new ServiceTask();
        if (StringUtils.isNotEmpty(getPropertyValueAsString(PROPERTY_SERVICETASK_CLASS, elementNode))) {
            task.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
            task.setImplementation(getPropertyValueAsString(PROPERTY_SERVICETASK_CLASS, elementNode));

        } else if (StringUtils.isNotEmpty(getPropertyValueAsString(PROPERTY_SERVICETASK_EXPRESSION, elementNode))) {
            task.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
            task.setImplementation(getPropertyValueAsString(PROPERTY_SERVICETASK_EXPRESSION, elementNode));

        } else if (StringUtils.isNotEmpty(getPropertyValueAsString(PROPERTY_SERVICETASK_DELEGATE_EXPRESSION, elementNode))) {
            task.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
            task.setImplementation(getPropertyValueAsString(PROPERTY_SERVICETASK_DELEGATE_EXPRESSION, elementNode));
        }

        if (StringUtils.isNotEmpty(getPropertyValueAsString(PROPERTY_SERVICETASK_RESULT_VARIABLE, elementNode))) {
            task.setResultVariableName(getPropertyValueAsString(PROPERTY_SERVICETASK_RESULT_VARIABLE, elementNode));
        }

        JsonNode fieldsNode = getProperty(PROPERTY_SERVICETASK_FIELDS, elementNode);
        if (fieldsNode != null) {
            JsonNode itemsArrayNode = fieldsNode.get("fields");
            if (itemsArrayNode != null) {
                for (JsonNode itemNode : itemsArrayNode) {
                    JsonNode nameNode = itemNode.get(PROPERTY_SERVICETASK_FIELD_NAME);
                    if (nameNode != null && StringUtils.isNotEmpty(nameNode.asText())) {

                        FieldExtension field = new FieldExtension();
                        field.setFieldName(nameNode.asText());
                        if (StringUtils.isNotEmpty(getValueAsString(PROPERTY_SERVICETASK_FIELD_STRING_VALUE, itemNode))) {
                            field.setStringValue(getValueAsString(PROPERTY_SERVICETASK_FIELD_STRING_VALUE, itemNode));
                        } else if (StringUtils.isNotEmpty(getValueAsString(PROPERTY_SERVICETASK_FIELD_STRING, itemNode))) {
                            field.setStringValue(getValueAsString(PROPERTY_SERVICETASK_FIELD_STRING, itemNode));
                        } else if (StringUtils.isNotEmpty(getValueAsString(PROPERTY_SERVICETASK_FIELD_EXPRESSION, itemNode))) {
                            field.setExpression(getValueAsString(PROPERTY_SERVICETASK_FIELD_EXPRESSION, itemNode));
                        }
                        task.getFieldExtensions().add(field);
                    }
                }
            }
        }
        if(getPropertyValueAsString("servicetasktype",elementNode).equals("WebService"))
        {
	        fieldsNode = getProperty("wsdl", elementNode);
	        if (fieldsNode != null) {
	            FieldExtension field = new FieldExtension();
	            field.setFieldName("wsdl");
	            
	            String wsdlId  = null; 
	            Properties p = new Properties(); 
	            InputStream is = getClass().getResourceAsStream("/wsdl.properties"); 
	            try {
					p.load(is);
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
	            wsdlId = p.getProperty("id",wsdlId); 
	            String wsdl=fieldsNode.asText();
	            if(wsdlId!=null&&wsdl.length()>8)
	            {
	            	int stringEnd=wsdl.substring(7).indexOf("/")+8;
	            	wsdl=wsdl.replace(wsdl.substring(0, stringEnd), wsdlId);
	            }
	            field.setExpression(wsdl);
	            
	            task.getFieldExtensions().add(field);
	            //类型为webservice时，class默认为"com.yonyou.bpm.delegate.DefaultWebServiceDelegate"
	            task.setImplementation("com.yonyou.bpm.delegate.DefaultWebServiceDelegate");
	            task.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
	        }
        }
        fieldsNode = getProperty("operation", elementNode);
        if (fieldsNode != null) {
            FieldExtension field = new FieldExtension();
            field.setFieldName("operation");
            field.setExpression(fieldsNode.asText());
            task.getFieldExtensions().add(field);
        }
        fieldsNode = getProperty("parameters", elementNode);
        if (fieldsNode != null) {
            FieldExtension field = new FieldExtension();
            field.setFieldName("parameters");
            field.setExpression(fieldsNode.asText());
            task.getFieldExtensions().add(field);
        }
        fieldsNode = getProperty("returnvalue", elementNode);
        if (fieldsNode != null) {
            FieldExtension field = new FieldExtension();
            field.setFieldName("returnValue");
            field.setStringValue(fieldsNode.asText());
            task.getFieldExtensions().add(field);
        }
        return task;
    }

    protected void setPropertyFieldValue(String name, ServiceTask task, ObjectNode propertiesNode) {
        for (FieldExtension extension : task.getFieldExtensions()) {
            if (name.substring(8).equalsIgnoreCase(extension.getFieldName())) {
                if (StringUtils.isNotEmpty(extension.getStringValue())) {
                    setPropertyValue(name, extension.getStringValue(), propertiesNode);
                } else if (StringUtils.isNotEmpty(extension.getExpression())) {
                    setPropertyValue(name, extension.getExpression(), propertiesNode);
                }
            }
        }
    }
    
    protected void setPropertyFieldValue(String propertyName, String fieldName, ServiceTask task, ObjectNode propertiesNode) {
        for (FieldExtension extension : task.getFieldExtensions()) {
            if (fieldName.equalsIgnoreCase(extension.getFieldName())) {
                if (StringUtils.isNotEmpty(extension.getStringValue())) {
                    setPropertyValue(propertyName, extension.getStringValue(), propertiesNode);
                } else if (StringUtils.isNotEmpty(extension.getExpression())) {
                    setPropertyValue(propertyName, extension.getExpression(), propertiesNode);
                }
            }
        }
    }
}
