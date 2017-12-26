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
import gov.df.fap.api.workflow.activiti.saveModel.ICreateActMode;
import gov.df.fap.api.workflow.activiti.saveModel.ICreateModel2;
import gov.df.fap.api.workflow.activiti.saveModel.IDeleteModel;

import java.util.HashMap;

import org.activiti.engine.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Tijs Rademakers
 */
@Controller
public class ModelSaveRestResource implements ModelDataJsonConstants {

	protected static final Logger LOGGER = LoggerFactory
			.getLogger(ModelSaveRestResource.class);
	protected ClassLoader classloader;
	protected static final String BPMN_XSD = "com/yonyou/bpm/bpmn/parser/BPMN20.xsd";

	/*
	 * @Autowired private RepositoryService repositoryService;
	 */

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ICreateModel2 createModel;

	@Autowired
	private ICreateActMode iCreateActMode;

	@Autowired
	private IDeleteModel deleteModel;

	@Autowired
	private IActivitiInit iActivitiInit;

	@RequestMapping(value = "/df/service/model/save", method = RequestMethod.POST)
	public @ResponseBody
	HashMap saveModel(@RequestParam String modelId, boolean isFirstCreate,
			@RequestBody MultiValueMap<String, String> values) throws Exception{
		ObjectMapper resultModel = new ObjectMapper();
		ObjectNode resultNode = resultModel.createObjectNode();
		HashMap createModelView = new HashMap();

		try {
			JsonNode editorNode = new ObjectMapper().readTree(values.getFirst(
					"json_xml").getBytes("utf-8"));
			RepositoryService repositoryService = iActivitiInit
					.getRepositoryService();
			createModelView = createModel.createModelView(isFirstCreate,
					modelId, editorNode, repositoryService, objectMapper);
			if (createModelView.get("proCode") != null) {
				iCreateActMode.saveActModel(modelId, values, repositoryService,
						objectMapper,
						createModelView.get("proCode").toString(),
						isFirstCreate);
				createModelView.put("responseFlag", "true");
			} else {
				createModelView.put("responseFlag", "false");
			}
		} catch (Exception e) {
			LOGGER.error("Error saving model", e);
			createModelView.put("error", "未预期的错误：" + e.getMessage());
			throw e;
		}
		return createModelView;
	}

}
