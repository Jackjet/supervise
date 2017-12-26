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
package gov.df.fap.controller.wf.activiti;

import gov.df.fap.api.workflow.activiti.ModelDataJsonConstants;
import gov.df.fap.api.workflow.activiti.design.IActivitiInit;
import gov.df.fap.api.workflow.activiti.saveModel.IGetModelBaseData;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.factory.ServiceFactory;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Tijs Rademakers
 */
@Controller
public class ModelEditorJsonRestResource implements ModelDataJsonConstants {

  protected static final Logger LOGGER = LoggerFactory.getLogger(ModelEditorJsonRestResource.class);

  /*@Autowired
  private RepositoryService repositoryService;*/

  @Autowired
  private IActivitiInit IActivitiInit;

  @Autowired
  private IGetModelBaseData iGetModelBaseData;

  private ObjectMapper objectMapper = null;

  public ModelEditorJsonRestResource() {
    //repositoryService= (RepositoryServiceImpl) ServiceFactory.getBean("sys.wf");
    objectMapper = (ObjectMapper) ServiceFactory.getBean("sys.objectMapper");
  }

  @RequestMapping(value = "/df/service/model/{modelId}/json", method = RequestMethod.GET, produces = "application/json")
  //@RequestMapping(value="/model/{modelId}/json", method = RequestMethod.GET, produces = "application/json")
  public @ResponseBody
  ObjectNode getEditorJson(@PathVariable
  String modelId) {
    ObjectNode modelNode = null;

    /**
     *  "resourceId": "180001",
      "properties": {
        "process_id": "111933",
        "processname": "你猜9",
        "multiinstance_maintablename": "BUDGET_USEABLE_VOUCHER",
        "idfield": "scccc",
        "expreson": ""
       },
     */
    if (1 == 1) {
      try {
        RepositoryService repositoryService = IActivitiInit.getRepositoryService();
        Model model = repositoryService.getModel(modelId);
        if (StringUtil.isNull(model.getMetaInfo())) {
          modelNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
        } else {
          modelNode = objectMapper.createObjectNode();
          modelNode.put(MODEL_NAME, model.getName());
        }
        modelNode.put(MODEL_ID, model.getId());
        ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(new String(repositoryService
          .getModelEditorSource(model.getId()), "utf-8"));
        String expresonFull = editorJsonNode.get("properties").get("expreson").asText();//获得当前入口条件表达式 2017_5_22
        ObjectNode modelBaseData = iGetModelBaseData.getModelBaseData(editorJsonNode, model, expresonFull);
        modelNode.put("model", modelBaseData);
      } catch (Exception e) {
        e.printStackTrace();
        LOGGER.error("Error creating model JSON", e);
        throw new ActivitiException("Error creating model JSON", e);
      }
    }
    return modelNode;
  }
}
