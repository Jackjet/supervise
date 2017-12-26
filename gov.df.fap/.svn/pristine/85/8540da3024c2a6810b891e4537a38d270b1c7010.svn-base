package gov.df.fap.service.workflow.activiti.design.data;

import gov.df.fap.api.menu.IMenuFilter;
import gov.df.fap.api.role.IRoleDfService;
import gov.df.fap.api.workflow.activiti.ModelDataJsonConstants;
import gov.df.fap.api.workflow.activiti.design.IActivitiInit;
import gov.df.fap.api.workflow.activiti.design.UpGradeService;
import gov.df.fap.bean.workflow.SysWfNode;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.dao.GeneralDAO;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class UpGradeImplBO implements UpGradeService {

	protected ClassLoader classloader;
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(UpGradeImplBO.class);

	@Autowired
	@Qualifier("generalDAO")
	private GeneralDAO dao;

	public GeneralDAO getDao() {
		return dao;
	}

	public void setDao(GeneralDAO dao) {
		this.dao = dao;
	}

	@Autowired
	private IActivitiInit iActivitiInit;

	@Autowired
	private IMenuFilter iMenuFilter;

	@Autowired
	private IRoleDfService iRoleDfService;

	Map<String, Map<String, String>> nodeConditionsMap = null;
	Map<String, Map<String, String>> nodeToUUIDMap = null;
	Map<String, String> sfUUidToNodeMap = null;
	Map<String, Map<String, String>> sfUUidToConditionIdMap = null;

	Map<String, Map<String, JSONObject>> nodeIdAndPosMap = null;

	@Override
	public Map<String, String> getUpGradeBLOB() throws Exception {
		List<Map> list = getWfId();
		Map<String, String> map = new HashMap<String, String>();
		for (Map m : list) {
			String wfId = (String) m.get("wf_id");
			String wfCode = (String) m.get("wf_code");
			List<Map> modelIdList = getModelIdByNodeCode(wfCode);
			List<Map> nodeIdList = getNodeId(wfId);
			if (modelIdList.isEmpty() && !(nodeIdList.isEmpty())) {
				JSONObject upGradeEveryBLOB = UpGradeEveryBLOB(wfId);
				saveBLOB(upGradeEveryBLOB);
				map.put("flag", "true");
			} else {
				map.put("flag", "false");
			}
		}
		return map;
	}

	/**
	 * 保存 act_ge_bytearray
	 * 
	 * @param upGradeEveryBLOB
	 * @throws UnsupportedEncodingException
	 */
	public void saveBLOB(JSONObject upGradeEveryBLOB) throws Exception {
		byte[] bytes = upGradeEveryBLOB.toString().getBytes("UTF-8");
		JsonNode editorNode = new ObjectMapper().readTree(bytes);
		JsonNode jsonNode = editorNode.get("properties");
		JsonNode proCodeValue = jsonNode.get("process_id");
		JsonNode processnameJson = jsonNode.get("processname");
		RepositoryService repositoryService = iActivitiInit
				.getRepositoryService();
		Model model = repositoryService.newModel();
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode modelObjectNode = objectMapper.createObjectNode();
		modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME,
				processnameJson.asText());
		modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
		modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, "");
		model.setMetaInfo(modelObjectNode.toString());
		model.setName(processnameJson.asText());
		model.setKey(proCodeValue.asText());
		repositoryService.saveModel(model);
		repositoryService.addModelEditorSource(model.getId(), bytes);
	}

	/**
	 * 根据工作流主表wf_id,导出每个大字段
	 */
	public JSONObject UpGradeEveryBLOB(String wfId) throws Exception {
		String wfcode = "";
		String wfName = "";
		String wfTableName = "";
		String idColumnName = "";
		String conditionId = "";
		String expression = "";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("resourceId", "canvas");
		List<Map> properties = getProperties(wfId);
		for (Map m : properties) {
			wfcode = (String) m.get("wf_code");
			wfName = (String) m.get("wf_name");
			wfTableName = (String) m.get("wf_table_name");
			idColumnName = (String) m.get("id_column_name");
			conditionId = (String) m.get("condition_id");
		}
		expression = getConditionLines(conditionId);
		JSONObject prosJO = new JSONObject();
		prosJO.put("process_id", wfcode);
		prosJO.put("processname", wfName);
		prosJO.put("multiinstance_maintablename", wfTableName + " "
				+ idColumnName);
		prosJO.put("expreson", expression);
		prosJO.put("idfield", idColumnName);
		jsonObject.put("properties", prosJO);
		JSONObject stenJO = new JSONObject();
		stenJO.put("id", "BPMNDiagram");
		jsonObject.put("stencil", stenJO);
		JSONArray chilJA = getChildShapes(wfId);
		jsonObject.put("childShapes", chilJA);
		JSONObject bounJO = new JSONObject();
		JSONObject LR_bounJO = new JSONObject();
		LR_bounJO.put("x", 1200);
		LR_bounJO.put("y", 1050);
		JSONObject UL_bounJO = new JSONObject();
		UL_bounJO.put("x", 0);
		UL_bounJO.put("y", 0);
		bounJO.put("lowerRight", LR_bounJO);
		bounJO.put("upperLeft", UL_bounJO);
		jsonObject.put("bounds", bounJO);
		JSONObject ssetJO = new JSONObject();
		ssetJO.put("url", "stencilsets/bpmn2.0/bpmn2.0.json");
		ssetJO.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
		jsonObject.put("stencilset", ssetJO);
		JSONArray sionJA = new JSONArray();
		jsonObject.put("ssextensions", sionJA);
		return jsonObject;
	}

	/**
	 * 返回childShapes 即所有节点（开始，结束，任务节点）和流转线信息
	 * 
	 * @return
	 */
	public JSONArray getChildShapes(String wfId) throws Exception {
		JSONArray jSONArray = new JSONArray();
		nodeConditionsMap = getNodeConditionsMap(wfId);
		nodeToUUIDMap = (Map) nodeConditionsMap.get("nodeToUUIDMap");
		sfUUidToNodeMap = nodeConditionsMap.get("sfUUidToNodeMap");
		sfUUidToConditionIdMap = (Map) nodeConditionsMap
				.get("sfUUidToConditionIdMap");
		nodeIdAndPosMap = getNodeIdAndPosMap(wfId);
		JSONObject startNode = null;
		JSONObject taskNode = null;
		JSONObject sequenceFlow = null;
		// update 节点编号
		List<SysWfNode> list = new ArrayList<SysWfNode>();
		for (String nodeID : nodeToUUIDMap.keySet()) {
			List<Map> nodesInfo = getNodesInfo(wfId, nodeID);
			String nodeTyp = "";
			String nodeCod = "";
			SysWfNode sysWfNode = new SysWfNode();
			for (Map map : nodesInfo) {
				nodeTyp = (String) map.get("node_type");
			}
			if (nodeTyp != null && nodeTyp != "" && nodeTyp.equals("001")) {
				nodeCod = "1";
			} else if (nodeTyp != null && nodeTyp != ""
					&& nodeTyp.equals("003")) {
				nodeCod = "2";
			} else {
				Map<String, String> m = nodeToUUIDMap.get(nodeID);
				nodeCod = m.get("NodeCode");
				
				/**
				 * 修改节点编号生成策略
				 */
//				nodeCod = String.valueOf(Integer.parseInt(nodeCod) + 2);
			}
			sysWfNode.setWF_ID(wfId);
			sysWfNode.setNODE_ID(nodeID);
			sysWfNode.setNODE_CODE(nodeCod);
			list.add(sysWfNode);
		}
		/**
		 * 修改节点编号生成策略
		 */
//		updateNodeCode(list);
		List<Map> nodeIdList = getNodeId(wfId);
		for (Map m : nodeIdList) {
			String nodeId = (String) m.get("node_id");
			String nodeType = (String) m.get("node_type");
			if (nodeType.equals("001") || nodeType.equals("003")) {
				startNode = getStartOrEndNode(wfId, nodeId);
				if (startNode != null) {
					jSONArray.add(startNode);
				}
			} else {
				taskNode = getTaskNode(wfId, nodeId);
				if (taskNode != null) {
					jSONArray.add(taskNode);
				}
			}
		}

		for (String sfUUID : sfUUidToConditionIdMap.keySet()) {
			Map<String, String> ConditionIdAndNextNodeIdMap = sfUUidToConditionIdMap
					.get(sfUUID);
			sequenceFlow = getSequenceFlow(wfId, sfUUID,
					ConditionIdAndNextNodeIdMap);
			if (sequenceFlow != null) {
				jSONArray.add(sequenceFlow);
			}
		}
		return jSONArray;
	}

	/**
	 * 更新库中NodeCode
	 * 
	 * @param list
	 */
	public void updateNodeCode(List<SysWfNode> list) throws Exception {
		try {
			String sql = "update sys_wf_nodes set node_code = ? where wf_id = ? and node_id = ?";
			String[] fieldParams = new String[] { "node_code", "wf_id",
					"node_id" };
			dao.executeBatchBySql(sql, fieldParams, list);
		} catch (Exception e) {
			LOGGER.error("更新【sys_wf_nodes】失败！" + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 组装开始或结束节点 Info
	 * 
	 * @param wfId
	 *            , node_code
	 * @return
	 */
	public JSONObject getStartOrEndNode(String wfId, String node_id)
			throws Exception {
		String node_name = "";
		String remark = "";
		String node_type = "";
		String wf_table_name = "";
		String gather_flag = "";
		String id_column_name = "";
		String outer_table_name = "";
		String outer_column_name = "";
		String relation_column_name = "";
		JSONObject jsonObject = new JSONObject();
		List<Map> startNodesOrEndInfo = getNodesInfo(wfId, node_id);
		for (Map m : startNodesOrEndInfo) {
			node_name = (String) m.get("node_name");
			node_type = (String) m.get("node_type");
			remark = (String) m.get("remark");
			wf_table_name = (String) m.get("wf_table_name");
			gather_flag = (String) m.get("gather_flag");
			id_column_name = (String) m.get("id_column_name");
			outer_table_name = (String) m.get("outer_table_name");
			outer_column_name = (String) m.get("outer_column_name");
			relation_column_name = (String) m.get("relation_column_name");
		}
		if (null != nodeToUUIDMap && !nodeToUUIDMap.isEmpty()) {
			Map<String, String> map = nodeToUUIDMap.get(node_id);
			String uuid = map.get("uuid");
			jsonObject.put("resourceId", uuid);
		}
		JSONArray outgJA = new JSONArray();
		if (node_type.equals("001") && null != sfUUidToNodeMap
				&& !sfUUidToNodeMap.isEmpty()) {
			for (String sfuuid : sfUUidToNodeMap.keySet()) {
				if (sfUUidToNodeMap.get(sfuuid).equals(node_id)) {
					JSONObject resoJO = new JSONObject();
					resoJO.put("resourceId", sfuuid);
					outgJA.add(resoJO);
				}
			}
		}
		JSONObject propJO = new JSONObject();
		propJO.put("name", node_name);
		if (node_type.equals("001")) {
			propJO.put("overrideid", "StartNoneEvent1");
			propJO.put("startnodetype", "001 开始节点");
		} else {
			propJO.put("overrideid", "EndNoneEvent1");
			propJO.put("startnodetype", "003 结束节点");
		}
		if (gather_flag.equals("1")) {
			propJO.put("multiinstance_person", "yibu");
		} else {
			propJO.put("multiinstance_person", "tongbu");
		}
		if (wf_table_name != null && !wf_table_name.equals("")) {
			StringBuffer sb1 = new StringBuffer(wf_table_name).append(" ")
					.append(id_column_name);
			propJO.put("multiinstance_handletype", sb1.toString());
		} else {
			propJO.put("multiinstance_handletype", "luru");
		}
		if (outer_table_name != null && !outer_table_name.equals("")) {
			StringBuffer sb2 = new StringBuffer(outer_table_name).append(" ")
					.append(outer_column_name);
			propJO.put("multiinstance_outtertrantablename", sb2.toString());
		} else {
			propJO.put("multiinstance_outtertrantablename", "luru");
		}
		propJO.put("etreid", relation_column_name);
		if (node_type.equals("001")) {
			JSONObject otmrJO = new JSONObject();
			List<Map> tollyActionType = getTollyActionType(node_id);
			Object refRJA = "";
			Object showValue = "";
			if (!tollyActionType.isEmpty()) {
				Map tollyActionTypeToZH = tollyActionTypeToZH(tollyActionType);
				refRJA = tollyActionTypeToZH.get("ja");
				showValue = tollyActionTypeToZH.get("sv");
				otmrJO.put("refResultData", refRJA);
				otmrJO.put("showValue", showValue);
				propJO.put("otmrti", otmrJO);
			}
		}
		propJO.put("remark", remark);
		jsonObject.put("properties", propJO);

		JSONObject stenJO = new JSONObject();
		String stencilId = "";
		if (node_type.equals("001")) {
			stencilId = "StartNoneEvent";
		} else {
			stencilId = "EndNoneEvent";
		}
		stenJO.put("id", stencilId);
		jsonObject.put("stencil", stenJO);
		JSONArray chilJA = new JSONArray();
		jsonObject.put("childShapes", chilJA);
		jsonObject.put("outgoing", outgJA);
		Map<String, JSONObject> strOrEndMap = null;
		if (node_type.equals("001")) {
			strOrEndMap = nodeIdAndPosMap.get("strPosMap");
		} else {
			strOrEndMap = nodeIdAndPosMap.get("endPosMap");
		}
		JSONObject bounJO = strOrEndMap.get(node_id);
		jsonObject.put("bounds", bounJO);
		JSONArray dockJA = new JSONArray();
		jsonObject.put("dockers", dockJA);
		return jsonObject;
	}

	/**
	 * 组装任务节点 Info
	 * 
	 * @param wfId
	 * @return
	 */
	public JSONObject getTaskNode(String wfId, String node_id) throws Exception {
		String node_code = "";
		String node_name = "";
		String remark = "";
		String wf_table_name = "";
		String gather_flag = "";
		String id_column_name = "";
		String outer_table_name = "";
		String outer_column_name = "";
		String relation_column_name = "";
		JSONObject jsonObject = new JSONObject();
		Map<String, String> map2 = nodeToUUIDMap.get(node_id);
		//////////
//		String overrideid = map2.get("NodeCode");
		/**
		 * 修改节点编号生成策略
		 */
		String overrideid = map2.get("NodeCode");
		overrideid = String.valueOf(Integer.parseInt(overrideid) + 2);
		////////////
		List<Map> taskNodesInfo = getNodesInfo(wfId, node_id);
		Map<String, JSONObject> taskPosMap = nodeIdAndPosMap.get("taskPosMap");
		for (Map m : taskNodesInfo) {
			node_code = (String) m.get("node_code");
			node_name = (String) m.get("node_name");
			remark = (String) m.get("remark");
			wf_table_name = (String) m.get("wf_table_name");
			gather_flag = (String) m.get("gather_flag");
			id_column_name = (String) m.get("id_column_name");
			outer_table_name = (String) m.get("outer_table_name");
			outer_column_name = (String) m.get("outer_column_name");
			relation_column_name = (String) m.get("relation_column_name");
			if (null != nodeToUUIDMap && !nodeToUUIDMap.isEmpty()) {
				Map<String, String> map = nodeToUUIDMap.get(node_id);
				String uuid = map.get("uuid");
				jsonObject.put("resourceId", uuid);
			}
			JSONArray outgJA = new JSONArray();
			if (null != sfUUidToNodeMap && !sfUUidToNodeMap.isEmpty()) {
				for (String sfuuid : sfUUidToNodeMap.keySet()) {
					if (sfUUidToNodeMap.get(sfuuid).equals(node_id)) {
						JSONObject resoJO = new JSONObject();
						resoJO.put("resourceId", sfuuid);
						outgJA.add(resoJO);
					}
				}
			}
			JSONObject propJO = new JSONObject();
			propJO.put("name", node_name);
			propJO.put("overrideid", new StringBuffer("ApproveUserTask")
					.append(overrideid).toString());
			propJO.put("startnodetype", "002 常规节点");
			if (gather_flag.equals("1")) {
				propJO.put("multiinstance_person", "yibu");
			} else {
				propJO.put("multiinstance_person", "tongbu");
			}
			if (wf_table_name != null && !wf_table_name.equals("")) {
				StringBuffer sb1 = new StringBuffer(wf_table_name).append(" ")
						.append(id_column_name);
				propJO.put("multiinstance_handletype", sb1.toString());
			} else {
				propJO.put("multiinstance_handletype", "luru");
			}
			if (outer_table_name != null && !outer_table_name.equals("")) {
				StringBuffer sb2 = new StringBuffer(outer_table_name).append(
						" ").append(outer_column_name);
				propJO.put("multiinstance_outtertrantablename", sb2.toString());
			} else {
				propJO.put("multiinstance_outtertrantablename", "luru");
			}
			propJO.put("etreid", relation_column_name);
			JSONObject funcJO = new JSONObject();
			JSONObject roleJO = new JSONObject();
			List<Map> roleByNodeId = getRoleByNodeId(node_id);
			if (!roleByNodeId.isEmpty()) {
				Map rolePro = getRolePro(roleByNodeId);
				Object reJA = rolePro.get("ja");
				Object sh = rolePro.get("sv");
				if (!sh.equals("")) {
					roleJO.put("refResultData", reJA);
					roleJO.put("showValue", sh);
					propJO.put("roleauth", roleJO);
				}
			}
			JSONObject otmrJO = new JSONObject();
			Object refRJA = "";
			Object showValue = "";
			List<Map> tollyActionType = getTollyActionType(node_id);
			if (!tollyActionType.isEmpty()) {
				Map tollyActionTypeToZH = tollyActionTypeToZH(tollyActionType);
				refRJA = tollyActionTypeToZH.get("ja");
				showValue = tollyActionTypeToZH.get("sv");
				otmrJO.put("refResultData", refRJA);
				otmrJO.put("showValue", showValue);
				propJO.put("otmrti", otmrJO);
			}
			propJO.put("remark", remark);
			jsonObject.put("properties", propJO);
			JSONObject stenJO = new JSONObject();
			stenJO.put("id", "ApproveUserTask");
			jsonObject.put("stencil", stenJO);
			JSONArray chilJA = new JSONArray();
			jsonObject.put("childShapes", chilJA);
			jsonObject.put("outgoing", outgJA);
			JSONObject bounJO = taskPosMap.get(node_id);
			jsonObject.put("bounds", bounJO);
			JSONArray dockJA = new JSONArray();
			jsonObject.put("dockers", dockJA);
		}
		return jsonObject;
	}

	/**
	 * 组装流转线 Info
	 * 
	 * @param
	 * @return
	 */
	public JSONObject getSequenceFlow(String wfId, String sfUUID,
			Map<String, String> ConditionIdAndNextNodeIdMap) throws Exception {
		JSONObject jsonObject = new JSONObject();
		String append = "";
		jsonObject.put("resourceId", sfUUID);
		JSONObject propJO = new JSONObject();
		String condition_id = ConditionIdAndNextNodeIdMap.get("conditionId");
		String nextNode_id = ConditionIdAndNextNodeIdMap.get("nextNodeId");
		append = getConditionLines(condition_id);
		propJO.put("name", "");
		propJO.put("documentation", "");
		propJO.put("conditionsequenceflow", append);
		propJO.put("defaultflow", "false");
		jsonObject.put("properties", propJO);
		JSONObject stenJO = new JSONObject();
		stenJO.put("id", "SequenceFlow");
		jsonObject.put("stencil", stenJO);
		JSONArray chilJA = new JSONArray();
		jsonObject.put("childShapes", chilJA);
		Map<String, String> map = nodeToUUIDMap.get(nextNode_id);
		String NNUuid = map.get("uuid");
		JSONArray outgJA = new JSONArray();
		JSONObject resoJO = new JSONObject();
		resoJO.put("resourceId", NNUuid);
		outgJA.add(resoJO);
		jsonObject.put("outgoing", outgJA);
		String nodeId = sfUUidToNodeMap.get(sfUUID);
		JSONObject bounJO = getNodePosByNodeId(wfId, nodeId, nextNode_id);
		jsonObject.put("bounds", bounJO);
		JSONArray dockJA = new JSONArray();
		jsonObject.put("dockers", dockJA);
		List<Map> nodesInfo = getNodesInfo(wfId, nodeId);
		for (Map m : nodesInfo) {
			String nodeType = (String) m.get("node_type");
			if (nodeType.equals("001")) {
				JSONArray dockJA1 = new JSONArray();
				JSONObject docJO1 = new JSONObject();
				JSONObject docJO2 = new JSONObject();
				docJO1.put("x", 12.000999999999998);
				docJO1.put("y", 12.000500000000002);
				docJO2.put("x", 45);
				docJO2.put("y", 30);
				dockJA1.add(docJO1);
				dockJA1.add(docJO2);
				jsonObject.put("dockers", dockJA1);
			}
		}
		return jsonObject;
	}

	/**
	 * 拼接条件INFO
	 * 
	 * @param condition_id
	 * @return
	 * @throws Exception
	 */
	public String getConditionLines(String condition_id) throws Exception {
		String bsh_expression = "";
		String expression = "";
		String append = "";
		if (!(condition_id.equals("#") || condition_id.equals(""))) {
			List<Map> conditions = getConditions(condition_id);
			for (Map m : conditions) {
				expression = (String) m.get("expression");
				bsh_expression = (String) m.get("bsh_expression");
			}
			if (null != expression && expression.equals("#")
					&& null != bsh_expression && bsh_expression.equals("#")) {
				return "";
			}
			List<Map> conditionsLines = getConditionsLines(condition_id);
			JSONArray jA = new JSONArray();
			for (Map m : conditionsLines) {
				JSONObject jO = new JSONObject();
				String left_pare = (String) m.get("left_pare");
				if(left_pare == null || left_pare.equals("")){
					jO.put("left_pare", " ");
				}else{
					jO.put("left_pare", left_pare);
				}
				String logic_operator = (String) m.get("logic_operator");
				if(logic_operator == null || logic_operator.equals("")){
					jO.put("logic_operator", " ");
				}else{
					jO.put("logic_operator", logic_operator);
				}
				String left_paraid = (String) m.get("left_paraid");
				if(left_paraid == null || left_paraid.equals("")){
					jO.put("left_paraid", " ");
				}else{
					jO.put("left_paraid", left_paraid);
				}
				String left_paravaluetype = (String) m
						.get("left_paravaluetype");
				if(left_paravaluetype == null || left_paravaluetype.equals("")){
					jO.put("left_paravaluetype", " ");
				}else{
					jO.put("left_paravaluetype", left_paravaluetype);
				}
				String left_paraname = (String) m.get("left_paraname");
				if (null != left_paraname && !(left_paraname.equals(""))) {
					String[] split = left_paraname.split("]");
					if (split.length == 2) {
						String subs1 = split[0].substring(1);
						int indexOf = split[1].indexOf("[");
						if (indexOf == 0) {
							jO.put("left_paraname", " ");
						} else {
							String subs2 = split[1].substring(0, indexOf);
							jO.put("left_paraname", subs2);
						}
						String subs3 = split[1].substring(indexOf + 1);
						jO.put("left_paraname_noFlag", subs1 + " " + subs3);
					} else {
						jO.put("left_paraname", " ");
						jO.put("left_paraname_noFlag", " ");
					}
				}else{
					jO.put("left_paraname", " ");
					jO.put("left_paraname_noFlag", " ");
				}
				String operator = (String) m.get("operator");
				if(operator == null || operator.equals("")){
					jO.put("operator", " ");
				}else{
					jO.put("operator", operator);
				}
				String right_paraid = (String) m.get("right_paraid");
				if(right_paraid == null || right_paraid.equals("")){
					jO.put("right_paraid", " ");
				}else{
					jO.put("right_paraid", right_paraid);
				}
				String right_paravaluetype = (String) m
						.get("right_paravaluetype");
				if(right_paravaluetype == null || right_paravaluetype.equals("")){
					jO.put("right_paravaluetype", " ");
				}else{
					jO.put("right_paravaluetype", right_paravaluetype);
				}
				String right_pare = (String) m.get("right_pare");
				if(right_pare == null || right_pare.equals("")){
					jO.put("right_pare", " ");
				}else{
					jO.put("right_pare", right_pare);
				}
				String right_paraname = (String) m.get("right_paraname");
				if (null != right_paraname && !(right_paraname.equals(""))) {
					String[] split = right_paraname.split("]");
					if (split.length == 2) {
						String subs1 = split[0].substring(1);
						String str = split[1];
						jO.put("right_paraname", str);
						jO.put("right_paraname_noFlag", subs1);
					} else {
						jO.put("right_paraname", "");
						jO.put("right_paraname_noFlag", "");
					}
				}else{
					jO.put("right_paraname", " ");
					jO.put("right_paraname_noFlag", " ");
				}
				jO.put("line_id", " ");
				//
				jO.put("ele_source_info", " ");
				jA.add(jO);
			}
			append = new StringBuffer().append(expression).append("*")
					.append(bsh_expression).append("*\"").append(jA.toString())
					.append("\"").append("*0").toString();
			return append;
			/*append = new StringBuffer().append(expression).append("*")
					.append(bsh_expression).append("*\"").append("[]")
					.append("\"").toString();
			return append;*/
		}
		return "";
	}

	/**
	 * 根据节点ID获得节点坐标
	 * 
	 * @return
	 */
	public JSONObject getNodePosByNodeId(String wfId, String nodeId,
			String nextNode_id) throws Exception {
		JSONObject bounJO = new JSONObject();
		JSONObject lowerRightJO = new JSONObject();
		JSONObject upperLeftJO = new JSONObject();
		JSONObject lowerRightJO2 = new JSONObject();
		JSONObject upperLeftJO2 = new JSONObject();
		JSONObject lowerRightJO3 = new JSONObject();
		JSONObject upperLeftJO3 = new JSONObject();
		List<Map> nodesInfo = getNodesInfo(wfId, nodeId);
		String node_type = "";
		String NNode_type = "";
		for (Map m : nodesInfo) {
			node_type = (String) m.get("node_type");
		}
		List<Map> nodesInfo2 = getNodesInfo(wfId, nextNode_id);
		for (Map m2 : nodesInfo2) {
			NNode_type = (String) m2.get("node_type");
		}
		if (node_type.equals("001")) {
			lowerRightJO.put("x", 64);
			lowerRightJO.put("y", 64);
			upperLeftJO.put("x", 40);
			upperLeftJO.put("y", 40);
		} else {
			Map<String, JSONObject> taskPosMap = nodeIdAndPosMap
					.get("taskPosMap");
			JSONObject JO = taskPosMap.get(nodeId);
			lowerRightJO = (JSONObject) JO.get("lowerRight");
			upperLeftJO = (JSONObject) JO.get("upperLeft");
		}
		if (NNode_type.equals("003")) {
			lowerRightJO2.put("x", 744);
			lowerRightJO2.put("y", 504);
			upperLeftJO2.put("x", 720);
			upperLeftJO2.put("y", 480);
		} else {
			Map<String, JSONObject> taskPosMap = nodeIdAndPosMap
					.get("taskPosMap");
			JSONObject JO = taskPosMap.get(nextNode_id);
			lowerRightJO2 = (JSONObject) JO.get("lowerRight");
			upperLeftJO2 = (JSONObject) JO.get("upperLeft");
		}
		Map<String, JSONObject> map = getSfPos(lowerRightJO, upperLeftJO,
				lowerRightJO2, upperLeftJO2);
		upperLeftJO3 = map.get("upperLeft");
		lowerRightJO3 = map.get("lowerRight");
		bounJO.put("upperLeft", upperLeftJO3);
		bounJO.put("lowerRight", lowerRightJO3);
		return bounJO;
	}

	/**
	 * 获得流转线坐标
	 * 
	 * @param lowerRightJO
	 * @param upperLeftJO
	 * @param lowerRightJO2
	 * @param upperLeftJO2
	 * @return
	 */
	public Map<String, JSONObject> getSfPos(JSONObject lowerRightJO,
			JSONObject upperLeftJO, JSONObject lowerRightJO2,
			JSONObject upperLeftJO2) {
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();
		JSONObject loweOJ = new JSONObject();
		JSONObject uppeOJ = new JSONObject();
		double sux = Double.parseDouble(upperLeftJO.get("x").toString());
		double slx = Double.parseDouble(lowerRightJO.get("x").toString());
		BigDecimal suxb = new BigDecimal(Double.valueOf(sux));
		BigDecimal slxb = new BigDecimal(Double.valueOf(slx));
		BigDecimal subtract = slxb.subtract(suxb);
		BigDecimal cs = new BigDecimal(Double.valueOf(2));
		BigDecimal divide = subtract.divide(cs, 6);
		double u_x2 = suxb.add(divide).doubleValue();
		double suy = Double.parseDouble(upperLeftJO.get("y").toString());
		double sly = Double.parseDouble(lowerRightJO.get("y").toString());
		BigDecimal suyb = new BigDecimal(Double.valueOf(suy));
		BigDecimal slyb = new BigDecimal(Double.valueOf(sly));
		BigDecimal subtract2 = slyb.subtract(suyb);
		BigDecimal divide2 = subtract2.divide(cs, 6);
		double u_y2 = suyb.add(divide2).doubleValue();
		uppeOJ.put("x", u_x2);
		uppeOJ.put("y", u_y2);
		if (upperLeftJO.get("x").toString().equals("40")) {
			uppeOJ.put("x", 63.87);
			uppeOJ.put("y", 54.38);
		}
		double xux = Double.parseDouble(upperLeftJO2.get("x").toString());
		double xlx = Double.parseDouble(lowerRightJO2.get("x").toString());
		BigDecimal xuxb = new BigDecimal(Double.valueOf(xux));
		BigDecimal xlxb = new BigDecimal(Double.valueOf(xlx));
		BigDecimal subtract3 = xlxb.subtract(xuxb);
		BigDecimal divide3 = subtract3.divide(cs, 6);
		double nu_x2 = xuxb.add(divide3).doubleValue();
		double xuy = Double.parseDouble(upperLeftJO2.get("y").toString());
		double xly = Double.parseDouble(lowerRightJO2.get("y").toString());
		BigDecimal xuyb = new BigDecimal(Double.valueOf(xuy));
		BigDecimal xlyb = new BigDecimal(Double.valueOf(xly));
		BigDecimal subtract4 = xlyb.subtract(xuyb);
		BigDecimal divide4 = subtract4.divide(cs, 6);
		double nu_y2 = xuyb.add(divide4).doubleValue();
		loweOJ.put("x", nu_x2);
		loweOJ.put("y", nu_y2);
		map.put("upperLeft", uppeOJ);
		map.put("lowerRight", loweOJ);
		return map;
	}

	/**
	 * 
	 * @param wf_id
	 * @return
	 * @throws Exception
	 */
	public Map getNodeConditionsMap(String wf_id) throws Exception {
		Map map = new HashMap();
		Map<String, String> sfUUidToNodeMap = new HashMap<String, String>();
		Map<String, Map> sfUUidToConditionIdMap = new HashMap<String, Map>();
		Map<String, Map<String, String>> nodeToUUIDMap = new HashMap<String, Map<String, String>>();
		List<Map> nodeIdList = getNodeId(wf_id);
		int i = 0;
		for (Map m : nodeIdList) {
			String nodeId = (String) m.get("node_id");
			String nodeType = (String) m.get("node_type");
			String uuid = getUUID();
			Map<String, String> hashMap = new HashMap<String, String>();
			if (nodeType.equals("002")) {
				i++;
				hashMap.put("uuid", uuid);
				hashMap.put("NodeCode", String.valueOf(i));
			} else {
				hashMap.put("uuid", uuid);
				hashMap.put("NodeCode", "");
			}
			nodeToUUIDMap.put(nodeId, hashMap);
		}
		List<Map> nodeConditionsList = getNodeConditions(wf_id, "001");
		for (Map m : nodeConditionsList) {
			String nodeId = (String) m.get("node_id");
			String nextNodeId = (String) m.get("next_node_id");
			String conditionId = (String) m.get("condition_id");
			String uuid = getUUID();
			sfUUidToNodeMap.put(uuid, nodeId);
			Map hashMap = new HashMap();
			hashMap.put("conditionId", conditionId);
			hashMap.put("nextNodeId", nextNodeId);
			sfUUidToConditionIdMap.put(uuid, hashMap);
		}
		map.put("nodeToUUIDMap", nodeToUUIDMap);
		map.put("sfUUidToNodeMap", sfUUidToNodeMap);
		map.put("sfUUidToConditionIdMap", sfUUidToConditionIdMap);
		return map;
	}

	/**
	 * 上游节点获得下游节点List
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map> getNextNodeIdList(String wf_id, String node_id)
			throws Exception {
		List list = null;
		try {
			String sql = "select node_id,next_node_id,condition_id from sys_wf_node_conditions where wf_id=? and routing_type='001' and node_id=?";
			list = dao.findBySql(sql, new Object[] { wf_id, node_id });
		} catch (Exception e) {
			LOGGER.error("上游节点获得下游节点List失败！" + e.getMessage(), e);
			throw e;
		}
		return list;
	}

	/**
	 * 获得节点关系
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map> getNodeConditions(String wf_id, String routing_type)
			throws Exception {
		List list = null;
		try {
			String sql = "select node_id,next_node_id,condition_id from sys_wf_node_conditions where wf_id=? and routing_type=?";
			list = dao.findBySql(sql, new Object[] { wf_id, routing_type });
		} catch (Exception e) {
			LOGGER.error("获得节点关系失败！" + e.getMessage(), e);
			throw e;
		}
		return list;
	}

	/**
	 * 获取节点node_id List
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map> getNodeId(String wf_id) throws Exception {
		List list = null;
		try {
			String sql = "select node_id,node_code,node_type from sys_wf_nodes where wf_id=?";
			list = dao.findBySql(sql, new Object[] { wf_id });
		} catch (Exception e) {
			LOGGER.error("获取节点node_id失败！" + e.getMessage(), e);
			throw e;
		}
		return list;
	}

	/**
	 * 获得操作记账类型
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map> getTollyActionType(String node_id) throws Exception {
		List list = null;
		try {
			String sql = "select action_type_code,tolly_flag from sys_wf_node_tolly_action_type where node_id=?";
			list = dao.findBySql(sql, new Object[] { node_id });
		} catch (Exception e) {
			LOGGER.error("获取操作记账类型失败！" + e.getMessage(), e);
			throw e;
		}
		return list;
	}

	/**
	 * 获得节点挂菜单
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map> getMenuByNodeId(String node_id) throws Exception {
		List list = null;
		try {
			String sql = "select module_id from sys_wf_module_node where node_id=?";
			list = dao.findBySql(sql, new Object[] { node_id });
		} catch (Exception e) {
			LOGGER.error("获得节点挂菜单失败！" + e.getMessage(), e);
			throw e;
		}
		return list;
	}

	/**
	 * 获得节点挂角色
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map> getRoleByNodeId(String node_id) throws Exception {
		List list = null;
		try {
			String sql = "select role_id from sys_wf_role_node where node_id=?";
			list = dao.findBySql(sql, new Object[] { node_id });
		} catch (Exception e) {
			LOGGER.error("获得节点挂角色失败！" + e.getMessage(), e);
			throw e;
		}
		return list;
	}

	/**
	 * 获取节点Info
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map> getNodesInfo(String wf_id, String node_id)
			throws Exception {
		List list = null;
		try {
			String sql = "select node_code,node_name,node_type,remark,node_id,wf_table_name,gather_flag,id_column_name,outer_table_name,outer_column_name,relation_column_name from sys_wf_nodes where wf_id = ? and node_id=?";
			list = dao.findBySql(sql, new Object[] { wf_id, node_id });
		} catch (Exception e) {
			LOGGER.error("获取节点Info失败！" + e.getMessage(), e);
			throw e;
		}
		return list;
	}

	/**
	 * 获取条件明细
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map> getConditionsLines(String condition_id) throws Exception {
		List list = null;
		try {
			String sql = "select LINE_ID,OPERATOR,LOGIC_OPERATOR,LEFT_PARE,RIGHT_PARE,LAST_VER,LEFT_PARAID,RIGHT_PARAID,LINE_SORT,PARA_TYPE,LEFT_PARAVALUETYPE,RIGHT_PARAVALUETYPE,LEFT_PARANAME,RIGHT_PARANAME from sys_wf_condition_lines where condition_id = ? order by LINE_SORT asc";
			list = dao.findBySql(sql, new Object[] { condition_id });
		} catch (Exception e) {
			LOGGER.error("获取条件明细失败！" + e.getMessage(), e);
			throw e;
		}
		return list;
	}

	/**
	 * 获取入口条件
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map> getConditions(String condition_id) throws Exception {
		List list = null;
		try {
			String sql = "select expression,bsh_expression from sys_wf_conditions where condition_id=?";
			list = dao.findBySql(sql, new Object[] { condition_id });
		} catch (Exception e) {
			LOGGER.error("获取入口条件失败！" + e.getMessage(), e);
			throw e;
		}
		return list;
	}

	/**
	 * 获取properties属性值
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map> getProperties(String wf_id) throws Exception {
		List list = null;
		try {
			String sql = "select wf_code,wf_name,wf_table_name,id_column_name,condition_id from sys_wf_flows where wf_id=?";
			list = dao.findBySql(sql, new Object[] { wf_id });
		} catch (Exception e) {
			LOGGER.error("获取properties属性值失败！" + e.getMessage(), e);
			throw e;
		}
		return list;
	}

	/**
	 * 判断是否已有模型
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map> getModelIdByNodeCode(String key) throws Exception {
		List list = null;
		try {
			String sql = "select editor_source_value_id_ from act_re_model where key_=?";
			list = dao.findBySql(sql, new Object[] { key });
		} catch (Exception e) {
			LOGGER.error("查询【act_re_model】失败！" + e.getMessage(), e);
			throw e;
		}
		return list;
	}

	/**
	 * 获取工作流主表全部wf_id
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map> getWfId() throws Exception {
		List list = null;
		try {
			String sql = "select wf_id,wf_name,wf_code from sys_wf_flows";
			list = dao.findBySql(sql);
		} catch (Exception e) {
			LOGGER.error("查询【sys_wf_flows】失败！" + e.getMessage(), e);
			throw e;
		}
		return list;
	}

	/**
	 * 节点挂角色树形拼装
	 * 
	 * @param otmr
	 * @return
	 */
	public Map getRolePro(List<Map> list) {
		JSONArray ja = new JSONArray();
		Map map = new HashMap();
		StringBuffer stringBuffer = new StringBuffer();
		Map<String, Object> allRole = iRoleDfService.getAllRole();
		List<Map> list2 = (List) allRole.get("rolelist");
		for (Map m : list) {
			String roleId = (String) m.get("role_id");
			JSONObject roleJO = new JSONObject();
			for (Map m2 : list2) {
				String id_ = (String) m2.get("guid");
				if (roleId.equals(id_)) {
					String name_ = (String) m2.get("name");
					String remark1_ = (String) m2.get("remark1");
					String admdiv_ = (String) m2.get("admdiv");
					String province_ = (String) m2.get("province");
					String year_ = (String) m2.get("year");
					String roletype_ = String.valueOf(m2.get("roletype"));
					String rolenature_ = String.valueOf(m2.get("rolenature"));
					String status_ = (String) m2.get("status");
					String rolelevel_ = (String) m2.get("rolelevel");
					roleJO.put("nocheck", false);
					roleJO.put("remark1", remark1_);
					roleJO.put("admdiv", admdiv_);
					roleJO.put("id", id_);
					roleJO.put("totalCount", 1);
					roleJO.put("name", name_);
					roleJO.put("province", province_);
					roleJO.put("year", year_);
					roleJO.put("user_guid", id_);
					roleJO.put("roletype", roletype_);
					roleJO.put("canselect", true);
					roleJO.put("rolenature", rolenature_);
					roleJO.put("status", status_);
					roleJO.put("rolelevel", rolelevel_);
					roleJO.put("isParent", false);
					roleJO.put("guid", id_);
					roleJO.put("pk", id_);
					roleJO.put("open", false);
					roleJO.put("zAsync", true);
					roleJO.put("isAjaxing", false);
					roleJO.put("pId", roletype_);
					roleJO.put("checked", true);
					roleJO.put("checkedOld", false);
					roleJO.put("chkDisabled", false);
					roleJO.put("halfCheck", false);
					roleJO.put("check_Child_State", -1);
					roleJO.put("check_Focus", false);
					roleJO.put("isHover", false);
					roleJO.put("editNameFlag", false);
					stringBuffer = stringBuffer.append(name_).append(",");
				}
			}
			ja.add(roleJO);
		}
		String sv = stringBuffer.toString();
		if (!sv.equals("")) {
			sv = sv.substring(0, sv.length() - 1);
		}
		map.put("ja", ja);
		map.put("sv", sv);
		return map;
	}

	/**
	 * 操作记账类型 中英文转化
	 * 
	 * @param otmr
	 * @return
	 */
	public Map tollyActionTypeToZH(List<Map> list) {
		JSONArray ja = new JSONArray();
		String action_type_code = "";
		String tolly_flag = "";
		Map map = new HashMap();
		StringBuffer stringBuffer = new StringBuffer();
		for (Map m : list) {
			action_type_code = (String) m.get("action_type_code");
			if (action_type_code.equals("DISTILL")) {
				action_type_code = "提取";
			}
			if (action_type_code.equals("STRIDE_HANG")) {
				action_type_code = "跨节点挂起";
			}
			if (action_type_code.equals("STRIDE_DELETE")) {
				action_type_code = "跨节点删除";
			}
			if (action_type_code.equals("STRIDE_DISCARD")) {
				action_type_code = "跨节点作废";
			}
			if (action_type_code.equals("INPUT")) {
				action_type_code = "录入";
			}
			if (action_type_code.equals("EDIT")) {
				action_type_code = "修改";
			}
			if (action_type_code.equals("NEXT")) {
				action_type_code = "审核";
			}
			if (action_type_code.equals("BACK")) {
				action_type_code = "退回";
			}
			if (action_type_code.equals("RECALL")) {
				action_type_code = "撤消";
			}
			if (action_type_code.equals("DELETE")) {
				action_type_code = "删除";
			}
			if (action_type_code.equals("DISCARD")) {
				action_type_code = "作废";
			}
			if (action_type_code.equals("HANG")) {
				action_type_code = "挂起";
			}
			tolly_flag = (String) m.get("tolly_flag");
			if (tolly_flag.equals("0")) {
				tolly_flag = "在途记账";
			}
			if (tolly_flag.equals("1")) {
				tolly_flag = "终审记账";
			}
			JSONObject jo = new JSONObject();
			jo.put("Name1", action_type_code);
			jo.put("Name2", tolly_flag);
			ja.add(jo);
			stringBuffer = stringBuffer.append(action_type_code).append(",");
		}
		String sv = stringBuffer.toString();
		sv = sv.substring(0, sv.length() - 1);
		map.put("ja", ja);
		map.put("sv", sv);
		return map;
	}

	/**
	 * 拼接格式 sid-uuid
	 * 
	 * @return
	 */
	public String getUUID() {
		String uuid = UUIDRandom.generate();
		uuid = uuid.substring(1, uuid.length() - 1);
		StringBuffer sb = new StringBuffer("sid-").append(uuid);
		return sb.toString();
	}

	/**
	 * 组装nodeId和节点坐标Map
	 * 
	 * @return
	 */
	public Map<String, Map<String, JSONObject>> getNodeIdAndPosMap(String wf_id)
			throws Exception {
		Map<String, Map<String, JSONObject>> map = new HashMap<String, Map<String, JSONObject>>();
		Map<String, JSONObject> strPosMap = new HashMap<String, JSONObject>();
		Map<String, JSONObject> endPosMap = new HashMap<String, JSONObject>();
		Map<String, JSONObject> taskPosMap = new HashMap<String, JSONObject>();
		double lrx = 244;
		double lry = 64;
		double ulx = 154;
		double uly = 4;
		List<Map> nodeIdList = getNodeId(wf_id);
		for (Map m : nodeIdList) {
			JSONObject bounJO = new JSONObject();
			JSONObject lowerRightJO = new JSONObject();
			JSONObject upperLeftJO = new JSONObject();
			String nodeId = (String) m.get("node_id");
			String nodeType = (String) m.get("node_type");
			if (nodeType.equals("001")) {
				lowerRightJO.put("x", 64);
				lowerRightJO.put("y", 64);
				upperLeftJO.put("x", 40);
				upperLeftJO.put("y", 40);
				bounJO.put("lowerRight", lowerRightJO);
				bounJO.put("upperLeft", upperLeftJO);
				strPosMap.put(nodeId, bounJO);
			} else if (nodeType.equals("003")) {
				lowerRightJO.put("x", 744);
				lowerRightJO.put("y", 504);
				upperLeftJO.put("x", 720);
				upperLeftJO.put("y", 480);
				bounJO.put("lowerRight", lowerRightJO);
				bounJO.put("upperLeft", upperLeftJO);
				endPosMap.put(nodeId, bounJO);
			} else {
				List positionAlg = positionAlg(lrx, lry, ulx, uly);
				lrx = Double.parseDouble(positionAlg.get(0).toString());
				lry = Double.parseDouble(positionAlg.get(1).toString());
				ulx = Double.parseDouble(positionAlg.get(2).toString());
				uly = Double.parseDouble(positionAlg.get(3).toString());
				lowerRightJO.put("x", lrx);
				lowerRightJO.put("y", lry);
				upperLeftJO.put("x", ulx);
				upperLeftJO.put("y", uly);
				bounJO.put("lowerRight", lowerRightJO);
				bounJO.put("upperLeft", upperLeftJO);
				taskPosMap.put(nodeId, bounJO);
			}
		}
		map.put("strPosMap", strPosMap);
		map.put("endPosMap", endPosMap);
		map.put("taskPosMap", taskPosMap);
		return map;
	}

	/**
	 * 对任务节点位置飘移
	 * 
	 * @param lrx
	 * @param lry
	 * @param ulx
	 * @param uly
	 * @return
	 */
	public List positionAlg(double lrx, double lry, double ulx, double uly) {
		List list = new ArrayList();
		lrx = lrx + 90;
		lry = lry + 30;
		ulx = ulx + 90;
		uly = uly + 30;
		list.add(lrx);
		list.add(lry);
		list.add(ulx);
		list.add(uly);
		return list;
	}
}
