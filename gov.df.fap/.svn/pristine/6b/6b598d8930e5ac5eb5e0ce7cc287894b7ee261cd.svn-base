package gov.df.fap.service.workflow.activiti.design.data;

import gov.df.fap.api.workflow.activiti.design.IActivitiInit;
import gov.df.fap.api.workflow.activiti.design.IGetBpmJson;
import gov.df.fap.bean.workflow.FWFLogDTO;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.service.util.wf.activiti.ApproveUserTaskXMLConverter;
import gov.df.fap.service.util.wf.activiti.BpmnJsonConverter;
import gov.df.fap.service.util.wf.activiti.SequenceJumpFlowXMLConverter;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.factory.ServiceFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class IGetBpmJsonImplBO implements IGetBpmJson {

  protected static final Logger LOGGER = LoggerFactory.getLogger(IGetBpmJsonImplBO.class);

  private ObjectMapper objectMapper = null;

  @Autowired
  private IActivitiInit IActivitiInit;

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao;

  public IGetBpmJsonImplBO() {

    objectMapper = (ObjectMapper) ServiceFactory.getBean("sys.objectMapper");
  }

  @Override
  public ObjectNode getBpmJsonData(String modelId) {

    try {
      RepositoryService repositoryService = IActivitiInit.getRepositoryService();
      Model model = repositoryService.getModel(modelId);
      ObjectNode modelNode = null;
      if (StringUtil.isNull(model.getMetaInfo())) {
        modelNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
      } else {
        modelNode = objectMapper.createObjectNode();
        modelNode.put("name", model.getName());
      }
      modelNode.put("modelId", model.getId());
      ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(new String(repositoryService
        .getModelEditorSource(model.getId()), "utf-8"));
      return editorJsonNode;
    } catch (JsonProcessingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public String getSysWfFlowsCode(String wfid) {
    String get_wf_id = "select wf_code from sys_wf_flows where wf_id=?";
    List<Map> findBySql1 = dao.findBySql(get_wf_id, new Object[] { wfid });
    String wfCode = "";
    for (Map map : findBySql1) {
      wfCode = (String) map.get("wf_code");
    }

    return wfCode;
  }

  @Override
  public ObjectNode setBpmJsonData(ObjectNode bpmJsonData, String idInfo, String entityId, List<FWFLogDTO> logList,
    List<Map> currentNodeNameAndId) {
    // TODO Auto-generated method stub
    ObjectMapper resultModel = new ObjectMapper();
    ObjectNode resultNode = resultModel.createObjectNode();
    ArrayNode arrayNode = resultModel.createArrayNode();
    ArrayNode arrayNode2 = resultModel.createArrayNode();
    ArrayNode arrayNode7 = resultModel.createArrayNode();
    ArrayNode arrayNode8 = resultModel.createArrayNode();

    ObjectNode processDefinitionNode = resultModel.createObjectNode();

    HashMap<String, String> done = new HashMap<String, String>();
    HashMap<String, String> current = new HashMap<String, String>();

    Iterator<FWFLogDTO> it = logList.iterator();
    while (it.hasNext()) {
      FWFLogDTO x = it.next();
      if (x.getNode_code().equals("1") || x.getNode_code().equals("2")) {
        it.remove();
      }
    }

    for (FWFLogDTO log : logList) {
      String create_user = log.getCreate_user() == null ? "" : log.getCreate_user();
      String operation_date = log.getOperation_date() == null ? "" : log.getOperation_date();
      done.put(log.getNode_code(), create_user + "," + operation_date);
    }
    for (Map log2 : currentNodeNameAndId) {
      String create_user = (String) log2.get("create_user") == null ? "" : (String) log2.get("create_user");
      String create_date = (String) log2.get("create_date") == null ? "" : (String) log2.get("create_date");

      current.put((String) log2.get("node_code"), create_user + "," + create_date);

    }

    JsonNode jsonNode = bpmJsonData.get("properties");//获得工作流主信息
    //处理processDefinition信息
    String key = (String) jsonNode.get("process_id").asText();//key
    String name = (String) jsonNode.get("processname").asText();//流程name
    String id = idInfo;
    String version = "1";
    String deploymentId = entityId;
    boolean isGraphicNotationDefined = true;

    processDefinitionNode.put("id", id);
    processDefinitionNode.put("key", key);
    processDefinitionNode.put("name", name);
    processDefinitionNode.put("version", version);
    processDefinitionNode.put("deploymentId", deploymentId);
    processDefinitionNode.put("isGraphicNotationDefined", isGraphicNotationDefined);
    resultNode.put("processDefinition", processDefinitionNode);

    //begin_获得正在执行节点的node_id_20170706
    //1.通过接口获得node_id
    //2.通过node_id获得ApproveUserTaskxx
    //end_获得正在执行节点的node_id_20170706

    //arrayNode7.add("ApproveUserTask33");

    /*arrayNode8.add("sid-08D4572D-B31A-4AA9-8326-893993951FFF");
    arrayNode8.add("sid-7F1F51FE-8F07-4CC8-960D-7D24A9D902D9");
    arrayNode8.add("sid-5EF2771B-7107-4538-A787-68EA49EF9F93");*/

    resultNode.put("highLightedActivities", arrayNode7);
    resultNode.put("highLightedFlows", arrayNode8);

    //处理activities部分数据，遍历childShapes信息
    JsonNode childShapesJsonNode = bpmJsonData.get("childShapes");//获得节点信息
    Map<String, ObjectNode> flowMaps = new HashMap<String, ObjectNode>();

    for (JsonNode jsonNode2 : childShapesJsonNode) {
      ObjectNode activitiesNode = resultModel.createObjectNode();
      JsonNode nodeElement = jsonNode2.get("properties");
      String nodeType = jsonNode2.get("stencil").get("id").asText();
      // for (JsonNode jsonNode3 : nodeElement) {

      if ("StartNoneEvent".equals(nodeType) || "EndNoneEvent".equals(nodeType) || "ApproveUserTask".equals(nodeType)) {

        String assignment0 = null;
        String createTime0 = null;
        String state = null;
        String taskNum = "0";
        String multiInstance = null;
        String activityId = nodeElement.get("overrideid").asText();
        String eleName = nodeElement.get("name").asText();
        String type = "";
        String operatorType = "";
        ObjectNode activitiesNodeProperties = resultModel.createObjectNode();
        if ("StartNoneEvent".equals(nodeType)) {
          type = "startEvent";
          activitiesNodeProperties.put("assignment0", assignment0);
          activitiesNodeProperties.put("createTime0", createTime0);
          activitiesNodeProperties.put("state", state);
          activitiesNodeProperties.put("taskNum", taskNum);
          activitiesNodeProperties.put("multiInstance", multiInstance);
          activitiesNodeProperties.put("name", eleName);
          activitiesNodeProperties.put("type", type);
        } else if ("EndNoneEvent".equals(nodeType)) {
          type = "endEvent";
          activitiesNodeProperties.put("assignment0", assignment0);
          activitiesNodeProperties.put("createTime0", createTime0);
          activitiesNodeProperties.put("state", state);
          activitiesNodeProperties.put("taskNum", taskNum);
          activitiesNodeProperties.put("multiInstance", multiInstance);
          activitiesNodeProperties.put("name", eleName);
          activitiesNodeProperties.put("type", type);
        } else if ("ApproveUserTask".equals(nodeType)) {
          type = "approveUserTask";

          if (done.get((Integer.valueOf(activityId.substring("ApproveUserTask".length())) + 2) + "") != null) {
            assignment0 = done.get((Integer.valueOf(activityId.substring("ApproveUserTask".length())) + 2) + "").split(
              ",")[0];
            createTime0 = done.get((Integer.valueOf(activityId.substring("ApproveUserTask".length())) + 2) + "").split(
              ",")[1];
            taskNum = (Integer.valueOf(activityId.substring("ApproveUserTask".length())) + 2) + "";
            activitiesNodeProperties.put(
              "assignment" + (Integer.valueOf(activityId.substring("ApproveUserTask".length())) + 2) + "", assignment0);
            activitiesNodeProperties.put(
              "createTime" + (Integer.valueOf(activityId.substring("ApproveUserTask".length())) + 2) + "", createTime0);
            operatorType = "done";
          } else if (current.get((Integer.valueOf(activityId.substring("ApproveUserTask".length())) + 2) + "") != null) {
            assignment0 = current.get((Integer.valueOf(activityId.substring("ApproveUserTask".length())) + 2) + "")
              .split(",")[0];
            createTime0 = current.get((Integer.valueOf(activityId.substring("ApproveUserTask".length())) + 2) + "")
              .split(",")[1];
            taskNum = (Integer.valueOf(activityId.substring("ApproveUserTask".length())) + 2) + "";
            activitiesNodeProperties.put(
              "assignment" + (Integer.valueOf(activityId.substring("ApproveUserTask".length())) + 2) + "", assignment0);
            activitiesNodeProperties.put(
              "createTime" + (Integer.valueOf(activityId.substring("ApproveUserTask".length())) + 2) + "", createTime0);
            operatorType = "current";
          }

          //begin_为兼容当前节点编号保持一致_20170725
          if (done.get((Integer.valueOf(activityId.substring("ApproveUserTask".length()))) + "") != null) {
            assignment0 = done.get((Integer.valueOf(activityId.substring("ApproveUserTask".length()))) + "").split(",")[0];
            createTime0 = done.get((Integer.valueOf(activityId.substring("ApproveUserTask".length()))) + "").split(",")[1];
            taskNum = (Integer.valueOf(activityId.substring("ApproveUserTask".length()))) + "";
            activitiesNodeProperties.put(
              "assignment" + (Integer.valueOf(activityId.substring("ApproveUserTask".length()))) + "", assignment0);
            activitiesNodeProperties.put(
              "createTime" + (Integer.valueOf(activityId.substring("ApproveUserTask".length()))) + "", createTime0);
            operatorType = "done";
          } else if (current.get((Integer.valueOf(activityId.substring("ApproveUserTask".length()))) + "") != null) {
            assignment0 = current.get((Integer.valueOf(activityId.substring("ApproveUserTask".length()))) + "").split(
              ",")[0];
            createTime0 = current.get((Integer.valueOf(activityId.substring("ApproveUserTask".length()))) + "").split(
              ",")[1];
            taskNum = (Integer.valueOf(activityId.substring("ApproveUserTask".length()))) + "";
            activitiesNodeProperties.put(
              "assignment" + (Integer.valueOf(activityId.substring("ApproveUserTask".length()))) + "", assignment0);
            activitiesNodeProperties.put(
              "createTime" + (Integer.valueOf(activityId.substring("ApproveUserTask".length()))) + "", createTime0);
            operatorType = "current";
          }
          //end_为兼容当前节点编号保持一致_20170725

          activitiesNodeProperties.put("state", state);
          activitiesNodeProperties.put("taskNum", taskNum);
          activitiesNodeProperties.put("multiInstance", multiInstance);
          activitiesNodeProperties.put("name", eleName);
          activitiesNodeProperties.put("type", type);
          activitiesNodeProperties.put("operatorType", operatorType);

        }

        activitiesNode.put("activityId", activityId);
        activitiesNode.put("multiInstance", multiInstance);
        activitiesNode.put("properties", activitiesNodeProperties);

        String xlr = jsonNode2.get("bounds").get("lowerRight").get("x").asText();
        String ylr = jsonNode2.get("bounds").get("lowerRight").get("y").asText();
        String xur = jsonNode2.get("bounds").get("upperLeft").get("x").asText();
        String yur = jsonNode2.get("bounds").get("upperLeft").get("y").asText();

        double width = Double.valueOf(xlr) - Double.valueOf(xur);
        double height = Double.valueOf(ylr) - Double.valueOf(yur);
        double x = Double.valueOf(xur);
        double y = Double.valueOf(yur);

        activitiesNode.put("x", x);
        activitiesNode.put("y", y);
        activitiesNode.put("width", width);
        activitiesNode.put("height", height);
        arrayNode.add(activitiesNode);
        resultNode.put("activities", arrayNode);

      }

      if ("SequenceFlow".equals(nodeType)) {

        ObjectNode hashMap = resultModel.createObjectNode();

        String resourceId = jsonNode2.get("resourceId").asText();

        /*String xlr = jsonNode2.get("bounds").get("lowerRight").get("x").asText();
        String ylr = jsonNode2.get("bounds").get("lowerRight").get("y").asText();
        String xur = jsonNode2.get("bounds").get("upperLeft").get("x").asText();
        String yur = jsonNode2.get("bounds").get("upperLeft").get("y").asText();*/

        //dockers
        String x = "";
        String y = "";
        JsonNode jsonNode3 = (ArrayNode) jsonNode2.get("dockers");
        if (jsonNode3.size() != 0) {
          for (JsonNode shapeNode : jsonNode3) {

            String dx = shapeNode.get("x").asText();
            String dy = shapeNode.get("y").asText();
            x += dx + " ";
            y += dy + " ";
          }
          x = x.trim();
          y = y.trim();

          String[] y_split = y.split(" ");
          double number = Double.valueOf(y_split[y_split.length - 1]);
          String yf = "";
          for (String yy : y_split) {

            double yInfo = Double.valueOf(yy) - number;
            yf += yInfo + " ";
          }

          y = yf.trim();

          hashMap.put("xPointArray", x);
          hashMap.put("yPointArray", y);
        } else {

          hashMap.put("xPointArray", "empty");
          hashMap.put("yPointArray", "empty");

        }

        // flowLists.add(hashMap);
        flowMaps.put(resourceId, hashMap);
      }

      //         }
    }

    //处理sequenceFlows数据信息
    BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
    BpmnModel bpmModel = jsonConverter.convertToBpmnModel(bpmJsonData);
    BpmnXMLConverter.addConverter(new ApproveUserTaskXMLConverter());
    BpmnXMLConverter.addConverter(new SequenceJumpFlowXMLConverter());
    byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmModel);

    ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
    try {
      Map<String, List<Element>> elementMap = getAppStaEndSqufElement(in, bpmJsonData);
      List<Element> sflist = elementMap.get("sequenceFlow");
      for (Element element : sflist) {

        ArrayNode arrayNode3 = resultModel.createArrayNode();
        ArrayNode arrayNode4 = resultModel.createArrayNode();
        ArrayNode arrayNode5 = resultModel.createArrayNode();
        ArrayNode arrayNode6 = resultModel.createArrayNode();

        ObjectNode sequenceFlowNode = resultModel.createObjectNode();
        String sourceRef = element.attributeValue("sourceRef");
        String targetRef = element.attributeValue("targetRef");
        String id_info = element.attributeValue("id");

        String flowId = id_info;
        String flowName = "";
        String flow = "(" + sourceRef + ")--" + id_info + "-->(" + targetRef + ")";
        String sourseName = "";
        String destinationName = "";

        if (sourceRef.startsWith("ApproveUserTask")) {
          sourseName = "审批任务";
        } else if (sourceRef.startsWith("StartNoneEvent")) {
          sourseName = "开始";
        } else {
          sourseName = "结束";
        }

        if (targetRef.startsWith("ApproveUserTask")) {
          destinationName = "审批任务";
        } else if (targetRef.startsWith("StartNoneEvent")) {
          destinationName = "开始";
        } else {
          destinationName = "结束";
        }

        sequenceFlowNode.put("id", flowId);
        sequenceFlowNode.put("name", flowName);
        sequenceFlowNode.put("flow", flow);

        sequenceFlowNode.put("sourseName", sourseName);
        sequenceFlowNode.put("destinationName", destinationName);

        //for (ObjectNode jsonNode2 : flowLists) {

        ObjectNode xyNode = resultModel.createObjectNode();
        ObjectNode jsonNode2 = flowMaps.get(id_info);

        String xPointArray = (String) jsonNode2.get("xPointArray").asText();
        String yPointArray = (String) jsonNode2.get("yPointArray").asText();

        String[] xPointArrays = new String[0];
        String[] yPointArrays = new String[0];

        if (!"empty".equals(xPointArray)) {
          xPointArrays = xPointArray.split(" ");
        }

        if (!"empty".equals(yPointArray)) {
          yPointArrays = yPointArray.split(" ");
        }

        /* String[] xPointArrays = xPointArray.split(" ");
         String[] yPointArrays = yPointArray.split(" ");*/

        for (int k = 0; k < xPointArrays.length; k++) {
          arrayNode5.add(xPointArrays[k]);
          arrayNode6.add(yPointArrays[k]);
        }

        /* JSONArray jsonArray_x = new JSONArray();
         jsonArray_x.add(xPointArrays);
         
         JSONArray jsonArray_y = new JSONArray();
         jsonArray_y.add(xPointArrays);*/

        arrayNode3.addAll(arrayNode5);
        arrayNode4.addAll(arrayNode6);
        sequenceFlowNode.put("xPointArray", arrayNode3);
        sequenceFlowNode.put("yPointArray", arrayNode4);
        arrayNode2.add(sequenceFlowNode);
        // }
      }
      resultNode.put("sequenceFlows", arrayNode2);
      return resultNode;

    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public Map<String, List<Element>> getAppStaEndSqufElement(ByteArrayInputStream in, JsonNode jsonNode)
    throws Exception {
    Document document = null;
    List<Element> sflist = new ArrayList<Element>();
    Map map = new HashMap<String, List<Element>>();
    try {
      SAXReader reader = new SAXReader();
      document = reader.read(in);
    } catch (Exception e) {
      LOGGER.error("解析xml发生错误--模型保存失败" + e.getMessage(), e);
      throw e;
    }
    Element rootElement = document.getRootElement();
    Element processElement = rootElement.element("process");
    for (Iterator it = processElement.elementIterator(); it.hasNext();) {
      Element e = (Element) it.next();
      if ("sequenceFlow".equals(e.getName())) {
        sflist.add(e);
      }
    }
    map.put("sequenceFlow", sflist);
    return map;
  }

  @Override
  public ObjectNode getHighlightsProcessInstance(String id, String deploymentId, List<FWFLogDTO> logList,
    List<Map> list, List<Map> queryFlowDataTask) {

    String sql_get_act_ref_fap = "select * from sys_wf_nodes t where t.NODE_ID in (";
    StringBuffer buffer_done = new StringBuffer(sql_get_act_ref_fap);
    StringBuffer Buffer_current = new StringBuffer(sql_get_act_ref_fap);

    ObjectMapper resultModel = new ObjectMapper();
    ObjectNode resultNode = resultModel.createObjectNode();
    ArrayNode arrayNode1 = resultModel.createArrayNode();
    ArrayNode arrayNode2 = resultModel.createArrayNode();
    resultNode.put("processInstanceId", deploymentId);
    resultNode.put("processDefinitionId", id);
    ArrayList<String> nodeIdLists_done = new ArrayList<String>();
    ArrayList<String> nodeIdLists_current = new ArrayList<String>();

    //begin_通过接口获得已办理的任务节点信息_20170706

    //过滤出已办节点的node_id
    getNodeDoneList(list, queryFlowDataTask);

    //设置查询参数，目的为了通过已办节点的node_id查出对应的node_code
    setSelCurrentAndNextNodeParam(logList, queryFlowDataTask, buffer_done, nodeIdLists_done);

    //设置查询参数，目的为了通过代办节点的node_id查出对应的node_code
    setSelCurrentAndNextNodeParam(logList, list, Buffer_current, nodeIdLists_current);

    //将查询出的node_code放进已办高亮显示的json中
    List<Map> findBySql = dao.findBySql(buffer_done.toString(), nodeIdLists_done.toArray());
    setCurrentAndNextNodeData(arrayNode2, findBySql);

    //将查询出的node_code放进代办高亮显示的json中
    List<Map> findBySql2 = dao.findBySql(Buffer_current.toString(), nodeIdLists_current.toArray());
    setCurrentAndNextNodeData(arrayNode1, findBySql2);

    //end_通过接口获得已办理的任务节点信息_20170706
    resultNode.put("activities", arrayNode2);
    resultNode.put("currentactivities", arrayNode1);
    return resultNode;
  }

  @Override
  public List queryFlowDataTask(String wfId, String entityId) {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    StringBuffer sql = new StringBuffer();
    sql.append("select distinct node_id, aaa ");
    sql.append("  from (select  t1.node_id,t1.entity_id, t2.wf_id,'1' as aaa ");
    sql
      .append("from sys_wf_current_item t1,sys_wf_nodes t2 where t1.rg_code = ? and t1.set_year = ? and t2.node_id=t1.node_id ");
    sql.append("union all ");
    sql.append("select current_node_id, entity_id, wf_id, '2' as aaa ");
    sql.append("from sys_wf_complete_tasks where rg_code=? and set_year =? ");
    sql.append("union all ");
    sql.append("select current_node_id, entity_id, wf_id, '2' as aaa ");
    sql.append("from sys_wf_current_tasks ");
    sql.append(" where rg_code=? and set_year=? and action_type_code in ('INPUT', 'NEXT', 'BACK') ");
    sql.append("union all ");
    sql.append(" select next_node_id, entity_id, wf_id, '2' as aaa ");
    sql.append("from sys_wf_end_tasks ");
    sql.append(" where rg_code=? and set_year=? and action_type_code in ('INPUT', 'NEXT', 'BACK') ");
    sql.append(" union all ");
    sql.append(" select t3.node_id, t3.entity_id, t4.wf_id, '2' as aaa");
    sql.append(" from sys_wf_complete_item t3, sys_wf_nodes t4");
    sql.append(" where t3.rg_code=? and t3.set_year=? and t4.node_id = t3.node_id ) t ");
    sql.append(" where wf_id = ? and entity_id=?");

    List list = new ArrayList();
    list = dao.findBySql(sql.toString(), new Object[] { rg_code, setYear, rg_code, setYear, rg_code, setYear, rg_code,
      setYear, rg_code, setYear, wfId, entityId });
    return list;
  }

  public List<Map> getCurrentNodeName(String entityId) {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    String sql = "select t2.node_name,t2.node_id,t2.node_code,t1.create_user,t1.create_date from sys_wf_current_tasks t1,sys_wf_nodes t2 where t1.entity_id = ? and t1.next_node_id = t2.node_id and t1.rg_code=? and t1.set_year=?";
    List<Map> list = new ArrayList<Map>();
    list = dao.findBySql(sql.toString(), new Object[] { entityId, rg_code, setYear });
    return list;
  }

  private void getNodeDoneList(List<Map> list, List<Map> queryFlowDataTask) {
    Iterator<Map> it = queryFlowDataTask.iterator();
    while (it.hasNext()) {
      Map x = it.next();
      for (Map curInfo : list) {
        if (((String) x.get("node_id")).equals((String) curInfo.get("node_id"))) {
          it.remove();
          break;
        }
      }
    }
  }

  private void setSelCurrentAndNextNodeParam(List<FWFLogDTO> logList1, List<Map> queryFlowDataTask,
    StringBuffer stringBuffer, ArrayList<String> nodeIdLists) {
    int num = 0;
    for (Map fWfLogDTO : queryFlowDataTask) {
      num++;
      nodeIdLists.add((String) fWfLogDTO.get("node_id"));
      if (num == queryFlowDataTask.size()) {
        stringBuffer.append("?");
      } else {
        stringBuffer.append("?,");
      }
    }
    stringBuffer.append(") and wf_id=" + logList1.get(0).getWf_id());
  }

  private void setCurrentAndNextNodeData(ArrayNode arrayNode2, List<Map> findBySql) {
    for (Map kv : findBySql) {
      String nodeCode = (String) kv.get("node_code");
      if (!nodeCode.equals("1") || nodeCode.equals("2")) {
        arrayNode2.add("ApproveUserTask" + (Integer.valueOf(nodeCode) - 2));
      }
    }
  }

  public String getSetYear() {
    String set_year = (String) SessionUtil.getUserInfoContext().getSetYear();
    if (set_year == null || set_year.equalsIgnoreCase("")) {
      set_year = String.valueOf(DateHandler.getCurrentYear());
    }
    return set_year;
  }

  private String getRgCode() {

    String rg_code = (String) SessionUtil.getUserInfoContext().getContext().get("rg_code");
    return rg_code;
  }

}
