package gov.df.fap.service.workflow.activiti.saveModel;

import gov.df.fap.api.workflow.activiti.ModelDataJsonConstants;
import gov.df.fap.api.workflow.activiti.saveModel.ICreateModel2;
import gov.df.fap.api.workflow.activiti.saveModel.IDeleteModel;
import gov.df.fap.bean.workflow.SysWfCondition;
import gov.df.fap.bean.workflow.SysWfConditionLine;
import gov.df.fap.bean.workflow.SysWfLineRule;
import gov.df.fap.bean.workflow.SysWfModuleNode;
import gov.df.fap.bean.workflow.SysWfNode;
import gov.df.fap.bean.workflow.SysWfNodeCondition;
import gov.df.fap.bean.workflow.SysWfNodeTollyActionType;
import gov.df.fap.bean.workflow.SysWfRoleNode;
import gov.df.fap.bean.workflow.SysWfRuleMonitor;
import gov.df.fap.bean.workflow.SysWfRuleNode;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.service.util.wf.activiti.ApproveUserTaskXMLConverter;
import gov.df.fap.service.util.wf.activiti.BpmnJsonConverter;
import gov.df.fap.service.util.wf.activiti.SequenceJumpFlowXMLConverter;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

@Service
public class ICreateModelImplBO implements ICreateModel2, ModelDataJsonConstants {

  protected ClassLoader classloader;

  protected static final Logger LOGGER = LoggerFactory.getLogger(ICreateModelImplBO.class);

  private static Map TableFieldsSQLMap = new HashMap();

  private String mtStr = "";

  private String idfieldStr = "";

  private String process_name = "";

  private String pro_Code = "";

  private String expreson = "";

  private String conRemark = "";

  private String expreson_circulation_line = "";

  private String expressionDesc = "";

  private String expressionScript = "";

  private String expressionSetting = "";

  private String uuid = "#";

  private String user_id = "";

  private String date = "";

  private String remark = "";

  // private String inletConditionId = "";

  private boolean exflag = false;

  // 规则类型：001 工作流规则 ， 002监控规则
  private String conditionType = "001";

  @Autowired
  private HibernateTemplate hibernateTemplate;

  @Autowired
  private IDeleteModel deleteModel;

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao;

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  public Object execute(HibernateCallback action) {
    return getHibernateTemplate().execute(action);
  }

  public final HibernateTemplate getHibernateTemplate() {
    return hibernateTemplate;
  }

  @Override
  public HashMap createModelView(boolean isFirstCreate, String modelId, JsonNode editorNode,
    RepositoryService repositoryService, ObjectMapper objectMapper) {
    HashMap regMap = new HashMap();
    Model model = null;
    try {
      // TODO
      if (modelId != null && !(modelId.equals(""))) {
        model = repositoryService.getModel(modelId);
      }
      // JsonNode editorNode = new
      // ObjectMapper().readTree(values.getFirst(
      // "json_xml").getBytes("utf-8"));
      JsonNode jsonNode = editorNode.get("properties");
      JsonNode multiinstance_maintablename = jsonNode.get("multiinstance_maintablename");
      // JsonNode idfield = jsonNode.get("idfield");
      JsonNode proCode = jsonNode.get("process_id");
      JsonNode processnameJson = jsonNode.get("processname");
      JsonNode expreson_ = jsonNode.get("expreson");
      mtStr = multiinstance_maintablename.asText();
      String[] split = mtStr.split(" ");
      if (split.length == 1) {
        mtStr = split[0];
        idfieldStr = "";
      } else {
        mtStr = split[0];
        idfieldStr = split[1];
      }
      pro_Code = proCode.asText();
      String key = "";
      // TODO
      if (model != null) {
        key = model.getKey();
        // if (!(key.equals(pro_Code))) {
        // // 更新act_re_model表的key
        // model.setKey(pro_Code);
        // }
      }
      process_name = processnameJson.asText();
      expreson = expreson_.asText();
      BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
      BpmnModel bpmModel = jsonConverter.convertToBpmnModel(editorNode);
      checkDataVal(editorNode, regMap, idfieldStr);
      if (!regMap.isEmpty()) {
        return regMap;
      }
      if (!isFirstCreate && model == null) {
        regMap.put("pCodeError", "请先选择模型再保存！");
        return regMap;
      }
      BpmnXMLConverter.addConverter(new ApproveUserTaskXMLConverter());
      BpmnXMLConverter.addConverter(new SequenceJumpFlowXMLConverter());
      byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmModel);
      /* FileOutputStream fo = new FileOutputStream(new
       File("D:/test/jumpLine.source"));
       fo.write(bpmnBytes);*/
      ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
      Map<String, List<Element>> elementMap = getAppStaEndSqufElement(in, editorNode);
      // boolean addOrUpdate = isAddOrUpdate(model);
      if (isFirstCreate) {
        HashMap<String, String> isRepeat = addModel(elementMap, editorNode, pro_Code);
        if (isRepeat != null) {
          return isRepeat;
        }
      } else {
        HashMap<String, String> isRepeat = updateModel(elementMap, editorNode, key);
        if (isRepeat != null) {
          return isRepeat;
        }
      }
      regMap.put("proCode", pro_Code);
    } catch (Exception e) {
      LOGGER.error("Error saving model", e);
      regMap.put("error", "未预期的错误：" + e.getMessage());
      throw new RuntimeException("解析xml发生错误--模型保存失败" + e.getMessage());
    }
    return regMap;
  }

  /**
   * 新增模型
   * 
   * @param elementMap
   * @param editorNode
   * @throws Exception
   */
  public HashMap<String, String> addModel(Map<String, List<Element>> elementMap, JsonNode editorNode, String nocode)
    throws Exception {
    boolean mflag = true;
    boolean rflag = true;
    boolean oflag = true;
    boolean raflag = true;
    List<Element> alist = elementMap.get("approveUserTask");
    List<Element> selist = elementMap.get("startEvent");
    List<Element> nelist = elementMap.get("endEvent");
    List<Element> sflist = elementMap.get("sequenceFlow");
    //begin_增加跨节点流转线_2017-6-8
    List<Element> sfJumplist = elementMap.get("SequenceJumpFlow");
    //end_增加跨节点流转线_2017-6-8

    // 获取set_year和rg_code
    String set_yeartmp = SessionUtil.getLoginYear();
    if (set_yeartmp == null || set_yeartmp.equals("")) {
      throw new Exception("年份获取异常，当前为空！");
    }
    int set_year = Integer.parseInt(set_yeartmp);
    String rg_code = SessionUtil.getRgCode();
    // 获取用户信息
    user_id = (String) SessionUtil.getUserInfoContext().getAttribute("user_id");
    if ("".equals(user_id) || null == user_id) {
      user_id = "";
    }
    String wf_id = "";
    Map<String, String> idAndNodMap = getIdAndNodenumber(alist, selist, nelist);
    // TODO 判断节点是否重复
    HashMap<String, String> isRepeat = isNodeCodeRepeat(idAndNodMap);
    if (!isRepeat.isEmpty()) {
      return isRepeat;
    }

    List<Map<String, String>> module = getMenuOrRole(editorNode, "functionauth");
    if (null == module) {
      mflag = false;
    }
    List<Map<String, String>> role = getMenuOrRole(editorNode, "roleauth");
    if (null == role) {
      rflag = false;
    }
    List<Map<String, String>> rulesAss = getMenuOrRole(editorNode, "preferra");
    if (null == rulesAss) {
      raflag = false;
    }
    List<Map<String, String>> otmrti = getMenuOrRole(editorNode, "otmrti");
    if (null == otmrti) {
      oflag = false;
    }
    List<Map<String, String>> appInfo = getApproveUserTaskInfo(editorNode);
    List<Map<String, String>> startOrEndBaseInfo = getStartOrEndBaseInfo(editorNode);
    // TODO 用nodeCode 插入
    // updateSysWfFlows(wf_id);
    setSysWfFlows(nocode, set_year, rg_code);
    List<Map> wfIdAndConditionId = getWfIdAndConditionId(nocode);
    for (Map m : wfIdAndConditionId) {
      wf_id = (String) m.get("wf_id");
    }
    setSysWfNodes(set_year, rg_code, appInfo, wf_id, startOrEndBaseInfo);
    Map<String, String> nodeIdAndNodeCode = getNodeIdAndNodeCode(wf_id);

    //begin_节点主键冲突测试_20170718
    Set<Entry<String, String>> entrySet = nodeIdAndNodeCode.entrySet();
    String str = "";
    for (Entry<String, String> entry : entrySet) {
      String value = "'" + entry.getValue() + "'";

      str += value + ",";
    }
    System.out.println(str);

    //end_节点主键冲突测试_20170718

    List<Map<String, String>> moduleCodeAndNodeId = null;
    List<Map<String, String>> roleCodeAndNodeId = null;
    List<Map<String, String>> otmrtiCodeAndNodeId = null;
    List<Map<String, String>> rulesAssCodeAndNodeId = null;
    if (mflag) {
      moduleCodeAndNodeId = getMenuCodeAndNodeId(nodeIdAndNodeCode, module);
    }
    if (rflag) {
      roleCodeAndNodeId = getMenuCodeAndNodeId(nodeIdAndNodeCode, role);
    }
    if (raflag) {
      rulesAssCodeAndNodeId = getMenuCodeAndNodeId(nodeIdAndNodeCode, rulesAss);
    }
    if (oflag) {
      otmrtiCodeAndNodeId = getMenuCodeAndNodeId(nodeIdAndNodeCode, otmrti);
    }
    if (null != moduleCodeAndNodeId) {
      setSysWfModuleNode(moduleCodeAndNodeId, set_year, rg_code);
    }
    if (null != roleCodeAndNodeId) {
      setSysWfRoleNode(roleCodeAndNodeId, set_year, rg_code);
    }
    if (null != rulesAssCodeAndNodeId) {
      setSysWfRuleNode(rulesAssCodeAndNodeId, set_year, rg_code);
    }
    Map<String, String> nodeIdByWfIdAndNodeCode = getNodeIdByWfIdAndNodeCode(idAndNodMap, wf_id);
    setSysWfNodeConditionsAndSysWfConditions(sflist, sfJumplist, set_year, rg_code, wf_id, nodeIdByWfIdAndNodeCode,
      editorNode);
    if (null != otmrtiCodeAndNodeId) {
      setSysWfNodeTollyActionType(otmrtiCodeAndNodeId, set_year, rg_code);
    }
    return null;
  }

  /**
   * 更新模型
   * 
   * @param elementMap
   * @param editorNode
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public HashMap<String, String> updateModel(Map<String, List<Element>> elementMap, JsonNode editorNode, String key)
    throws Exception {
    boolean mflag = true;
    boolean rflag = true;
    boolean oflag = true;
    boolean raflag = true;
    List<Element> alist = elementMap.get("approveUserTask");
    List<Element> selist = elementMap.get("startEvent");
    List<Element> nelist = elementMap.get("endEvent");
    List<Element> sflist = elementMap.get("sequenceFlow");
    //begin_增加跨节点流转线_2017-6-8
    List<Element> sfJumplist = elementMap.get("SequenceJumpFlow");
    //end_增加跨节点流转线_2017-6-8

    String set_yeartmp = SessionUtil.getLoginYear();
    if (set_yeartmp == null || set_yeartmp.equals("")) {
      throw new Exception("年份获取异常，当前为空！");
    }
    int set_year = Integer.parseInt(set_yeartmp);
    String rg_code = SessionUtil.getRgCode();
    user_id = (String) SessionUtil.getUserInfoContext().getAttribute("user_id");
    if ("".equals(user_id) || null == user_id) {
      user_id = "";
    }
    String wf_id = "";
    Map<String, String> idAndNodMap = getIdAndNodenumber(alist, selist, nelist);
    // TODO 判断节点是否重复
    HashMap<String, String> isRepeat = isNodeCodeRepeat(idAndNodMap);
    if (!isRepeat.isEmpty()) {
      return isRepeat;
    }
    List<Map<String, String>> module = getMenuOrRole(editorNode, "functionauth");
    if (null == module) {
      mflag = false;
    }
    List<Map<String, String>> role = getMenuOrRole(editorNode, "roleauth");
    if (null == role) {
      rflag = false;
    }
    List<Map<String, String>> ruleAcc = getMenuOrRole(editorNode, "preferra");
    if (null == ruleAcc) {
      raflag = false;
    }
    List<Map<String, String>> otmrti = getMenuOrRole(editorNode, "otmrti");
    if (null == otmrti) {
      oflag = false;
    }
    List<Map> wfIdAndConditionId = getWfIdAndConditionId(key);
    for (Map m : wfIdAndConditionId) {
      wf_id = (String) m.get("wf_id");
    }
    delSysWfRoMoToCoCos(key);
    updateSysWfFlows(wf_id);
    List<Map<String, String>> startOrEndBaseInfo = getStartOrEndBaseInfo(editorNode);
    List<Map<String, String>> appInfo = getApproveUserTaskInfo(editorNode);

    Map<String, String> newNodeIdAndNodeCode = getNewNodeIdAndNodeCode(wf_id, idAndNodMap);
    updateSysWfNodes(newNodeIdAndNodeCode, appInfo, startOrEndBaseInfo, wf_id, set_year, rg_code);
    List<Map<String, String>> moduleCodeAndNodeId = null;
    List<Map<String, String>> roleCodeAndNodeId = null;
    List<Map<String, String>> ruleAccCodeAndNodeId = null;
    List<Map<String, String>> otmrtiCodeAndNodeId = null;
    if (mflag) {
      moduleCodeAndNodeId = getMenuCodeAndNodeId(newNodeIdAndNodeCode, module);
    }
    if (rflag) {
      roleCodeAndNodeId = getMenuCodeAndNodeId(newNodeIdAndNodeCode, role);
    }
    if (raflag) {
      ruleAccCodeAndNodeId = getMenuCodeAndNodeId(newNodeIdAndNodeCode, ruleAcc);
    }
    if (oflag) {
      otmrtiCodeAndNodeId = getMenuCodeAndNodeId(newNodeIdAndNodeCode, otmrti);
    }
    if (null != moduleCodeAndNodeId) {
      setSysWfModuleNode(moduleCodeAndNodeId, set_year, rg_code);
    }
    if (null != roleCodeAndNodeId) {
      setSysWfRoleNode(roleCodeAndNodeId, set_year, rg_code);
    }
    if (null != ruleAccCodeAndNodeId) {
      setSysWfRuleNode(ruleAccCodeAndNodeId, set_year, rg_code);
    }
    Map<String, String> nodeIdByWfIdAndNodeCode = getNodeIdByWfIdAndNodeCode(idAndNodMap, wf_id);
    setSysWfNodeConditionsAndSysWfConditions(sflist, sfJumplist, set_year, rg_code, wf_id, nodeIdByWfIdAndNodeCode,
      editorNode);
    if (null != otmrtiCodeAndNodeId) {
      setSysWfNodeTollyActionType(otmrtiCodeAndNodeId, set_year, rg_code);
    }
    return null;
  }

  /**
   * 删除工作流角色表, 删除工作流功能表, 删除工作流节点挂规则表, 删除工作流记账类型表, 删除工作流节点关系表, 若有条件删除工作流条件表
   * 
   * @param key
   * @throws Exception
   */
  public void delSysWfRoMoToCoCos(String key) throws Exception {
    String get_wf_id = "select wf_id,condition_id from sys_wf_flows where wf_code=?";
    String get_node_id = "select node_id,node_code from sys_wf_nodes where wf_id=?";
    String sql_wf_module_node_del = "delete from sys_wf_module_node where node_id=?";
    String sql_role_node_del = "delete from sys_wf_role_node where node_id=?";
    String sql_rule_node_del = "delete from sys_wf_rule_node where node_id=?";
    String sql_node_conditions_del = "delete from sys_wf_node_conditions where wf_id=? and node_id=?";
    String sql_sys_wf_node_tolly_action_type_del = "delete from sys_wf_node_tolly_action_type where node_id=?";
    String sql_sys_wf_conditions_del = "delete from sys_wf_conditions where condition_id=?";
    String get_condition_id = "select distinct condition_id from sys_wf_node_conditions where wf_id=?";
    String sql_del_con_lines = "delete from sys_wf_condition_lines where condition_id=?";// 删除条件明细表
    String get_line_rule_id = "select distinct line_rule_id from sys_wf_node_conditions where wf_id=?";
    String sql_del_line_rule = "delete from sys_wf_line_rule where line_rule_id=?";
    String sql_del_rule_monitor = "delete from sys_wf_rule_monitor where line_rule_id=?";
    HashMap regMap = new HashMap();
    try {
      String wfIdUp = "";
      String conditionId = "";
      List<Map> findBySql1 = dao.findBySql(get_wf_id, new Object[] { key });
      for (Map map : findBySql1) {
        wfIdUp = (String) map.get("wf_id");
        conditionId = (String) map.get("condition_id");
      }
      List<Map> wfnodesLists = dao.findBySql(get_node_id, new Object[] { wfIdUp });
      List list2 = new ArrayList();
      List list3 = new ArrayList();
      List list4 = new ArrayList();
      for (Map map : wfnodesLists) {
        String nodeId = (String) map.get("node_id");
        SysWfNodeCondition sysWC = new SysWfNodeCondition();
        SysWfNodeCondition sysWC2 = new SysWfNodeCondition();
        sysWC.setWF_ID(wfIdUp);
        sysWC.setNODE_ID(nodeId);
        sysWC2.setNODE_ID(nodeId);
        list2.add(sysWC2);
        list3.add(sysWC);
      }
      String[] fields2 = new String[] { "node_id" };
      dao.executeBatchBySql(sql_wf_module_node_del, fields2, list2);
      dao.executeBatchBySql(sql_role_node_del, fields2, list2);
      dao.executeBatchBySql(sql_rule_node_del, fields2, list2);
      dao.executeBatchBySql(sql_sys_wf_node_tolly_action_type_del, fields2, list2);
      List<Map> wfcis = dao.findBySql(get_condition_id, new Object[] { wfIdUp });
      if (conditionId != null && !(conditionId.equals("") || conditionId.equals("#"))) {
        Map map = new HashMap();
        map.put("condition_id", conditionId);
        wfcis.add(map);
      }
      for (Map c : wfcis) {
        SysWfConditionLine line = new SysWfConditionLine();
        String conditioId = (String) c.get("condition_id");
        if (conditioId != null && !conditioId.equals("#")) {
          line.setCONDITION_ID(conditioId);
          list4.add(line);
        }
      }
      List<Map> lineRuleIdList = dao.findBySql(get_line_rule_id, new Object[] { wfIdUp });
      List list5 = new ArrayList();
      for (Map m : lineRuleIdList) {
        SysWfLineRule sysWfLineRule = new SysWfLineRule();
        String lineRuleId = (String) m.get("line_rule_id");
        if (lineRuleId != null && !lineRuleId.equals("#") && !lineRuleId.equals("")) {
          sysWfLineRule.setLINE_RULE_ID(lineRuleId);
          list5.add(sysWfLineRule);
        }
      }
      String[] fields4 = new String[] { "condition_id" };
      // TODO 删除入口条件
      dao.executeBatchBySql(sql_sys_wf_conditions_del, fields4, list4);
      // begin_删除条件明细_2017_05_12
      dao.executeBatchBySql(sql_del_con_lines, fields4, list4);
      // end_删除条件明细_2017_05_12
      String[] fields3 = new String[] { "wf_id", "node_id" };
      dao.executeBatchBySql(sql_node_conditions_del, fields3, list3);
      String[] fields5 = new String[] { "line_rule_id" };
      dao.executeBatchBySql(sql_del_line_rule, fields5, list5);
      dao.executeBatchBySql(sql_del_rule_monitor, fields5, list5);
    } catch (Exception e) {
      LOGGER.error("更新【】失败！" + e.getMessage(), e);
      throw e;
    }
  }

  /**
   * 批量更新工作流节点表
   * 
   * @param newNodeIdAndNodeCode
   * @param alist
   * @param startOrEndBaseInfo
   * @throws Exception
   */
  public void updateSysWfNodes(Map<String, String> newNodeIdAndNodeCode, List<Map<String, String>> appInfo,
    List<Map<String, String>> startOrEndBaseInfo, String wf_id, int set_year, String rg_code) throws Exception {
    String sql = "update sys_wf_nodes set node_name = ?," + "node_type = ?,remark = ?,latest_op_user = ?,"
      + "latest_op_date = ?,node_id =?,wf_table_name = ?,gather_flag = ?,"
      + "id_column_name = ?,outer_table_name = ?,outer_column_name = ?,"
      + "relation_column_name = ?,set_year = ?,rg_code = ?,"
      + "SEND_MSG_FLAG = 0,SEND_MSG_TIME = 0,AUTO_AUDIT_FLAG = 0,"
      + " AUTO_AUDIT_TIME = 0,SEND_MSG_TIME_UNIT = 0,AUTO_AUDIT_TIME_UNIT = 0" + " where wf_id = ? and node_code = ?";
    List<SysWfNode> list = new ArrayList<SysWfNode>();
    try {
      for (String k : newNodeIdAndNodeCode.keySet()) {
        for (Map<String, String> m : appInfo) {
          String nodeNum = m.get("overrideid");
          String[] split = nodeNum.split("ApproveUserTask");
          String nodeNumber = String.valueOf(Integer.parseInt(split[1]));//去掉编号+2
          if (nodeNumber.equals(k)) {
            SysWfNode sysWfNode = new SysWfNode();
            sysWfNode.setWF_ID(wf_id);
            sysWfNode.setNODE_CODE(nodeNumber);
            String nodeName = m.get("name");
            if ("".equals(nodeName) || null == nodeName) {
              nodeName = "";
            }
            sysWfNode.setNODE_NAME(nodeName);
            sysWfNode.setNODE_TYPE("002");
            remark = m.get("remark");
            if ("".equals(remark) || null == remark) {
              remark = "";
            }
            sysWfNode.setREMARK(remark);
            sysWfNode.setLATEST_OP_USER(user_id);
            date = getDate();
            sysWfNode.setLATEST_OP_DATE(date);
            sysWfNode.setNODE_ID(newNodeIdAndNodeCode.get(k));
            sysWfNode.setREMARK(remark);
            String multiinstanceHandletype = m.get("multiinstance_handletype");
            String itmti = "";
            if ("luru".equals(multiinstanceHandletype) || null == multiinstanceHandletype) {
              multiinstanceHandletype = "";
            } else {
              String[] split2 = multiinstanceHandletype.split(" ");
              multiinstanceHandletype = split2[0];
              if (split2.length == 2) {
                itmti = split2[1];
              }
            }
            sysWfNode.setWF_TABLE_NAME(multiinstanceHandletype);
            String multiinstance_person = m.get("multiinstance_person");
            int multiinstancePerson = 0;
            if (multiinstance_person.contains("yibu")) {
              multiinstancePerson = 1;
            }
            sysWfNode.setGATHER_FLAG(multiinstancePerson);
            sysWfNode.setID_COLUMN_NAME(itmti);
            String multiinstanceOuttertrantablename = m.get("multiinstance_outtertrantablename");
            String otmti = "";
            if ("luru".equals(multiinstanceOuttertrantablename) || null == multiinstanceOuttertrantablename) {
              multiinstanceOuttertrantablename = "";
            } else {
              String[] split2 = multiinstanceOuttertrantablename.split(" ");
              multiinstanceOuttertrantablename = split2[0];
              if (split2.length == 2) {
                otmti = split2[1];
              }
            }
            sysWfNode.setOUTER_TABLE_NAME(multiinstanceOuttertrantablename);
            sysWfNode.setOUTER_COLUMN_NAME(otmti);
            String etreid = m.get("etreid");
            if ("".equals(etreid) || null == etreid) {
              etreid = "";
            }
            sysWfNode.setRELATION_COLUMN_NAME(etreid);
            sysWfNode.setSET_YEAR(set_year);
            sysWfNode.setRG_CODE(rg_code);
            list.add(sysWfNode);
          }
        }
      }
      Map<String, String> startKV = startOrEndBaseInfo.get(0);// 开始
      Map<String, String> endKV = startOrEndBaseInfo.get(1);// 结束
      Set<Entry<String, String>> startEntrySet = startKV.entrySet();
      SysWfNode sysWfNode = new SysWfNode();
      sysWfNode.setWF_ID(wf_id);
      for (Entry<String, String> entry : startEntrySet) {
        setNodeValue("start", sysWfNode, set_year, rg_code, list, entry);
      }
      sysWfNode.setLATEST_OP_USER(user_id);
      sysWfNode.setNODE_ID(newNodeIdAndNodeCode.get("1"));
      date = getDate();
      sysWfNode.setLATEST_OP_DATE(date);
      list.add(sysWfNode);
      SysWfNode sysWfNode1 = new SysWfNode();
      sysWfNode1.setWF_ID(wf_id);
      Set<Entry<String, String>> endEntrySet = endKV.entrySet();
      for (Entry<String, String> entry : endEntrySet) {
        setNodeValue("end", sysWfNode1, set_year, rg_code, list, entry);
      }
      sysWfNode1.setLATEST_OP_USER(user_id);
      sysWfNode1.setNODE_ID(newNodeIdAndNodeCode.get("2"));
      date = getDate();
      sysWfNode1.setLATEST_OP_DATE(date);
      list.add(sysWfNode1);
      String[] fieldParams = new String[] { "node_name", "node_type", "remark", "latest_op_user", "latest_op_date",
        "node_id", "wf_table_name", "gather_flag", "id_column_name", "outer_table_name", "outer_column_name",
        "relation_column_name", "set_year", "rg_code", "wf_id", "node_code" };

      dao.executeBatchBySql(sql, fieldParams, list);
    } catch (Exception e) {
      LOGGER.error("更新【sys_wf_nodes 】 失败！" + e.getMessage(), e);
      throw e;
    }
  }

  /**
   * 获得工作流节点表更新节点id和节点code
   * 
   * @param wf_id
   * @param alist
   * @param selist
   * @param nelist
   * @return
   * @throws Exception
   */
  public Map<String, String> getNewNodeIdAndNodeCode(String wf_id, Map<String, String> idAndNodMap) throws Exception {
    List<String> nodeCode = getNodeCode(idAndNodMap);
    Map<String, String> nodeIdAndNodeCode = getNodeIdAndNodeCode(wf_id);
    Map mapUP = new HashMap();
    List<SysWfNode> list = new ArrayList<SysWfNode>();
    List<SysWfNode> list3 = new ArrayList<SysWfNode>();
    for (String c : nodeCode) {
      boolean up = nodeIdAndNodeCode.containsKey(c);
      if (up) {
        mapUP.put(c, nodeIdAndNodeCode.get(c));
      } else {
        SysWfNode sysWfNode = new SysWfNode();
        sysWfNode.setNODE_CODE(c);
        sysWfNode.setWF_ID(wf_id);
        list.add(sysWfNode);
      }
    }
    if (!list.isEmpty()) {
      List<Map<String, String>> newList = getNodeId(wf_id, list);
      for (Map<String, String> m : newList) {
        for (String s : m.keySet()) {
          mapUP.put(s, m.get(s));
        }
      }
    }

    Map<String, String> map = new HashMap();
    for (String n : nodeIdAndNodeCode.keySet()) {
      boolean flag = false;
      for (String c : nodeCode) {
        if (n.equals(c)) {
          flag = true;
        }
      }
      if (!flag) {
        map.put(n, nodeIdAndNodeCode.get(n));
      }
    }
    if (!map.isEmpty()) {
      for (String s : map.keySet()) {
        SysWfNode sysWfNode = new SysWfNode();
        sysWfNode.setNODE_CODE(s);
        sysWfNode.setWF_ID(wf_id);
        list3.add(sysWfNode);
      }
    }
    delSysWfNodes(list3);
    return mapUP;
  }

  /**
   * 在工作流节点表中删除一个节点
   * 
   * @param wf_id
   * @param node_code
   * @throws Exception
   */
  public void delSysWfNodes(List<SysWfNode> list) throws Exception {
    String sql = "delete from sys_wf_nodes where wf_id =? and node_code=?";
    try {
      String[] fieldParams = new String[] { "wf_id", "node_code" };
      dao.executeBatchBySql(sql, fieldParams, list);
    } catch (Exception e) {
      LOGGER.error("删除【sys_wf_nodes 节点】 失败！" + e.getMessage(), e);
      throw e;
    }
  }

  /**
   * 新增工作流节点 node_id
   * 
   * @param wf_id
   * @param list
   * @return
   * @throws Exception
   */
  public List<Map<String, String>> getNodeId(String wf_id, List<SysWfNode> list) throws Exception {
    String sql = "INSERT INTO sys_wf_nodes (wf_id,node_code,node_id,gather_flag,set_year,rg_code) VALUES (?,?,"
      + (TypeOfDB.isOracle() ? "SEQ_SYS_WF_ID.Nextval" : "Nextval('SEQ_SYS_WF_ID')") + ",0,0,0)";
    List<Map<String, String>> ist = new ArrayList();
    Map<String, String> map = new HashMap();
    try {
      String[] fileld = new String[] { "wf_id", "node_code" };
      dao.executeBatchBySql(sql, fileld, list);
      for (SysWfNode s : list) {
        String node_CODE = s.getNODE_CODE();
        map = getNodeCodeAndNodeId(wf_id, node_CODE);
        ist.add(map);
      }
    } catch (Exception e) {
      LOGGER.error("新增 【NOID_ID】 失败！" + e.getMessage(), e);
      throw e;
    }
    return ist;
  }

  /**
   * 获取NodeCodeAndNodeId Map
   * 
   * @param wf_id
   * @param node_code
   * @return
   * @throws Exception
   */
  public Map<String, String> getNodeCodeAndNodeId(String wf_id, String node_code) throws Exception {
    String sql = "select node_id from sys_wf_nodes where wf_id=? and node_code=?";
    Map<String, String> map = new HashMap();
    String nodeId = "";
    try {
      String[] fileld = new String[] { wf_id, node_code };
      List<Map> list = dao.findBySql(sql, fileld);
      for (Map m : list) {
        nodeId = (String) m.get("node_id");
      }
      map.put(node_code, nodeId);
    } catch (Exception e) {
      LOGGER.error("查询 【NOID_ID】 失败！" + e.getMessage(), e);
      throw e;
    }
    return map;
  }

  /**
   * 获得node_code
   * 
   * @param idAndNodMap
   * @return
   */
  public List<String> getNodeCode(Map<String, String> idAndNodMap) {
    List<String> list = new ArrayList<String>();
    for (String c : idAndNodMap.keySet()) {
      String id = idAndNodMap.get(c);
      list.add(id);
    }
    return list;
  }

  /**
   * 判断是新增还是更新模型
   * 
   * @param model
   * @return
   */
  public boolean isAddOrUpdate(Model model) throws Exception {
    boolean flag = false;
    String key = model.getKey();
    List<Map> list = getWfIdAndConditionId(key);
    for (Map m : list) {
      String wfId = (String) m.get("wf_id");
      List<Map> nodeId = getNodeIdBywfId(wfId);
      if (nodeId.isEmpty()) {
        flag = true;
      }
    }
    return flag;
  }

  /**
   * 获得工作流主表sys_wf_flows的wf_id和condition_id
   */
  public List<Map> getWfIdAndConditionId(String wf_code) throws Exception {
    Map<String, String> map = new HashMap<String, String>();
    List<Map> list = null;
    try {
      String sql = "select wf_id,condition_id from sys_wf_flows where wf_code=?";
      list = dao.findBySql(sql, new Object[] { wf_code });
    } catch (Exception e) {
      LOGGER.error("查询【sys_wf_flows】失败！" + e.getMessage(), e);
      throw e;
    }
    return list;
  }

  /**
   * 获得工作流主表sys_wf_flows的condition_id
   */
  public List<Map> getConditionIdByWfId(String wf_id) throws Exception {
    Map<String, String> map = new HashMap<String, String>();
    List<Map> list = null;
    try {
      String sql = "select condition_id from sys_wf_flows where wf_id=?";
      list = dao.findBySql(sql, new Object[] { wf_id });
    } catch (Exception e) {
      LOGGER.error("查询【sys_wf_flows】失败！" + e.getMessage(), e);
      throw e;
    }
    return list;
  }

  /**
   * 获得工作流节点表sys_wf_flows的node_id
   */
  public List<Map> getNodeIdBywfId(String wf_id) throws Exception {
    Map<String, String> map = new HashMap<String, String>();
    List<Map> list = null;
    try {
      String sql = "select node_id from sys_wf_nodes where wf_id=?";
      list = dao.findBySql(sql, new Object[] { wf_id });
    } catch (Exception e) {
      LOGGER.error("查询【sys_wf_nodes】失败！" + e.getMessage(), e);
      throw e;
    }
    return list;
  }

  /**
   * 流程图数据校验
   * 
   * @author zhangbch
   * @param jsonNode
   * @return
   */
  private HashMap checkDataVal(JsonNode jsonNode, HashMap regNode, String idfieldStr) {
    JsonNode shapesArrayNode = (ArrayNode) jsonNode.get("childShapes");
    if (shapesArrayNode.size() == 0) {
      regNode.put("nullError", "流程图中必须存在一个节点");
    }
    JsonNode processIdJson = jsonNode.get("properties").get("process_id");
    JsonNode processNameJson = jsonNode.get("properties").get("processname");
    JsonNode MtJson = jsonNode.get("properties").get("multiinstance_maintablename");
    JsonNode idfieldJson = jsonNode.get("properties").get("idfield");

    // 流程基本信息校验
    if (processIdJson.asText() == null || "".equals(processIdJson.asText())) {
      regNode.put("pidError", "流程Id不能为空");
    }

    if (processNameJson.asText() == null || "".equals(processNameJson.asText())) {
      regNode.put("pNameError", "流程名称不能为空");
    }

    if (MtJson.asText() == null || "".equals(MtJson.asText()) || "luru".equals(MtJson.asText())) {
      regNode.put("mtError", "流程主表名称不能为空");
    }

    if (idfieldStr == null || "".equals(idfieldStr)) {
      regNode.put("idfieldError", "流程主表字段不能为空");
    }

    if (!(processIdJson.asText().replaceAll("[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() == 0))
      regNode.put("proCharFormatError", "流程ID中包含特殊字符！");
    boolean isNum = processIdJson.asText().matches("[0-9]+");
    if (!isNum) {
      regNode.put("proNameFormaterror", "流程ID不能包含字母");
    } else {
      if (processIdJson.asText().length() % 3 != 0) {
        regNode.put("proNameFormaterror", "流程编号的位数必须为3的倍数");
      }
    }
    // 节点属性校验
    Map<String, String> map = new HashMap<String, String>();
    for (JsonNode shapeNode : shapesArrayNode) {
      String nodeType = shapeNode.get("stencil").get("id").asText();
      JsonNode jsonpropertiesNode = shapeNode.get("properties");
      if (nodeType.equals("ApproveUserTask")) {
        String asText = jsonpropertiesNode.get("overrideid").asText();
        ArrayNode functionauthArr = (ArrayNode) jsonpropertiesNode.get("functionauth").get("refResultData");
        ArrayNode roleauthArr = (ArrayNode) jsonpropertiesNode.get("roleauth").get("refResultData");
        ArrayNode ormArr = (ArrayNode) jsonpropertiesNode.get("otmrti").get("refResultData");

        if (functionauthArr == null) {
          regNode.put("taskMenuError", "任务节点菜单不能为空");
        }
        if (roleauthArr == null) {
          regNode.put("taskRoleError", "任务节点角色不能为空");
        }
      }

      //begin_增加开始节点内部事务提醒主表信息校验_2017-6-15
      if (nodeType.equals("StartNoneEvent")) {
        JsonNode jsonNode2 = jsonpropertiesNode.get("multiinstance_handletype");
        String asText = jsonNode2.asText();
        // ArrayNode multiinstance_handletype = (ArrayNode) jsonpropertiesNode.get("multiinstance_handletype");
        if ("luru".equals(asText)) {
          regNode.put("StartNodeError", "开始节点内部事务提醒主表信息不能为空！");
        }

      }
      //end_增加开始节点内部事务提醒主表信息校验_2017-6-15

    }
    return regNode;
  }

  /**
   * 获得开始，结束，任务节点和流转线ElementList
   * 
   * @param in
   * @param jsonNode
   * @param model
   * @param repositoryService
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public Map<String, List<Element>> getAppStaEndSqufElement(ByteArrayInputStream in, JsonNode jsonNode)
    throws Exception {
    Document document = null;
    List<Element> alist = new ArrayList<Element>();
    List<Element> selist = new ArrayList<Element>();
    List<Element> nelist = new ArrayList<Element>();
    List<Element> sflist = new ArrayList<Element>();
    //begin_增加跨节点流转线_2017_6-8
    List<Element> sfJumplist = new ArrayList<Element>();
    //end_增加跨节点流转线_2017_6-8

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
      if ("approveUserTask".equals(e.getName())) {
        alist.add(e);
      }
      if ("startEvent".equals(e.getName())) {
        selist.add(e);
      }
      if ("endEvent".equals(e.getName())) {
        nelist.add(e);
      }
      if ("sequenceFlow".equals(e.getName())) {
        sflist.add(e);
      }
      //begin_增加跨节点流转线_2017_6-8
      if ("SequenceJumpFlow".equals(e.getName())) {
        sfJumplist.add(e);
      }
      //end_增加跨节点流转线_2017_6-8

    }
    map.put("approveUserTask", alist);
    map.put("startEvent", selist);
    map.put("endEvent", nelist);
    map.put("sequenceFlow", sflist);
    map.put("SequenceJumpFlow", sfJumplist);
    return map;
  }

  /**
   * 在工作流主表插入数据
   * 
   * @param set_year
   * @param rg_code
   * @throws Exception
   */
  public void setSysWfFlows(String node_code, int set_year, String rg_code) throws Exception {
    String sql = "INSERT INTO sys_wf_flows (wf_id,wf_code,wf_name,wf_table_name,id_column_name,condition_id,latest_op_user,latest_op_date,set_year,rg_code) VALUES ("
      + (TypeOfDB.isOracle() ? "SEQ_SYS_WF_ID.Nextval" : "Nextval('SEQ_SYS_WF_ID')") + ",?,?,?,?,?,?,?,?,?)";
    List list = new ArrayList();
    try {
      if (!("".equals(expreson) || null == expreson)) {
        uuid = UUIDRandom.generate();
        // inletConditionId = uuid;
      } else {
        uuid = "#";
      }
      date = getDate();
      list.add(node_code);
      list.add(process_name);
      list.add(mtStr);// 主表名称
      list.add(idfieldStr);// ID字段
      list.add(uuid);
      list.add(user_id);
      list.add(date);
      list.add(set_year);
      list.add(rg_code);
      dao.executeBySql(sql, list.toArray());
    } catch (Exception e) {
      LOGGER.error("插入 【sys_wf_flows】失败！" + e.getMessage(), e);
      throw e;
    }
  }

  /**
   * 在工作流主表插入数据
   * 
   * @param set_year
   * @param rg_code
   * @throws Exception
   */
  public void updateSysWfFlows(String wf_id) throws Exception {
    String sql = "update sys_wf_flows set wf_code=?, wf_name=?, wf_table_name=?, id_column_name=?, condition_id=?, latest_op_user=?, latest_op_date=? where wf_id = ?";
    List list = new ArrayList();
    try {
      if (!("".equals(expreson) || null == expreson)) {
        uuid = UUIDRandom.generate();
        // inletConditionId = uuid;
      } else {
        uuid = "#";
      }
      date = getDate();
      list.add(pro_Code);
      list.add(process_name);
      list.add(mtStr);// 主表名称
      list.add(idfieldStr);// ID字段
      list.add(uuid);
      list.add(user_id);
      list.add(date);
      list.add(wf_id);
      dao.executeBySql(sql, list.toArray());
    } catch (Exception e) {
      LOGGER.error("插入 【sys_wf_flows】失败！" + e.getMessage(), e);
      throw e;
    }
  }

  /**
   * 在工作流节点表插入数据
   * 
   * @param set_year
   * @param rg_code
   * @param alist
   * @param wfId
   * @throws Exception
   */
  public void setSysWfNodes(int set_year, String rg_code, List<Map<String, String>> appInfo, String wfId,
    List<Map<String, String>> startOrEndBaseInfo) throws Exception {
    String sql = "INSERT INTO sys_wf_nodes (wf_id,node_code,node_name,node_type,remark,latest_op_user,latest_op_date,wf_table_name,gather_flag,id_column_name,outer_table_name,outer_column_name,relation_column_name,set_year,rg_code,node_id,SEND_MSG_FLAG,SEND_MSG_TIME,AUTO_AUDIT_FLAG,AUTO_AUDIT_TIME,SEND_MSG_TIME_UNIT,AUTO_AUDIT_TIME_UNIT) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
      + (TypeOfDB.isOracle() ? "SEQ_SYS_WF_ID.Nextval" : "Nextval('SEQ_SYS_WF_ID')") + ",0,0,0,0,0,0)";
    List<SysWfNode> list = new ArrayList<SysWfNode>();
    try {
      for (Map<String, String> m : appInfo) {
        SysWfNode sysWfNode = new SysWfNode();
        sysWfNode.setWF_ID(wfId);
        String nodeNum = m.get("overrideid");
        String[] split = nodeNum.split("ApproveUserTask");
        String nodeNumber = String.valueOf(Integer.parseInt(split[1]));//去掉编号+2
        nodeNumber = ("".equals(nodeNumber) || null == nodeNumber) ? "" : nodeNumber;
        sysWfNode.setNODE_CODE(nodeNumber);
        String nodeName = m.get("name");
        if ("".equals(nodeName) || null == nodeName) {
          nodeName = "";
        }
        sysWfNode.setNODE_NAME(nodeName);
        sysWfNode.setNODE_TYPE("002");
        remark = m.get("remark");
        if ("".equals(remark) || null == remark) {
          remark = "";
        }
        sysWfNode.setREMARK(remark);
        sysWfNode.setLATEST_OP_USER(user_id);
        date = getDate();
        sysWfNode.setLATEST_OP_DATE(date);
        sysWfNode.setREMARK(remark);
        String multiinstanceHandletype = m.get("multiinstance_handletype");
        String itmti = "";
        if ("luru".equals(multiinstanceHandletype) || null == multiinstanceHandletype) {
          multiinstanceHandletype = "";
        } else {
          String[] split2 = multiinstanceHandletype.split(" ");
          multiinstanceHandletype = split2[0];
          if (split2.length == 2) {
            itmti = split2[1];
          }
        }
        sysWfNode.setWF_TABLE_NAME(multiinstanceHandletype);
        String multiinstance_person = m.get("multiinstance_person");
        int multiinstancePerson = 0;
        if (multiinstance_person.contains("yibu")) {
          multiinstancePerson = 1;
        }
        sysWfNode.setGATHER_FLAG(multiinstancePerson);
        sysWfNode.setID_COLUMN_NAME(itmti);
        String multiinstanceOuttertrantablename = m.get("multiinstance_outtertrantablename");
        String otmti = "";
        if ("luru".equals(multiinstanceOuttertrantablename) || null == multiinstanceOuttertrantablename) {
          multiinstanceOuttertrantablename = "";
        } else {
          String[] split2 = multiinstanceOuttertrantablename.split(" ");
          multiinstanceOuttertrantablename = split2[0];
          if (split2.length == 2) {
            otmti = split2[1];
          }
        }
        sysWfNode.setOUTER_TABLE_NAME(multiinstanceOuttertrantablename);
        sysWfNode.setOUTER_COLUMN_NAME(otmti);
        String etreid = m.get("etreid");
        if ("".equals(etreid) || null == etreid) {
          etreid = "";
        }
        sysWfNode.setRELATION_COLUMN_NAME(etreid);
        sysWfNode.setSET_YEAR(set_year);
        sysWfNode.setRG_CODE(rg_code);
        list.add(sysWfNode);
      }

      Map<String, String> startKV = startOrEndBaseInfo.get(0);// 开始
      Map<String, String> endKV = startOrEndBaseInfo.get(1);// 结束
      Set<Entry<String, String>> startEntrySet = startKV.entrySet();

      // begin_流程图不完整时保存_开始节点_2017_04_21
      if (!startEntrySet.isEmpty()) {
        SysWfNode sysWfNode = new SysWfNode();
        sysWfNode.setWF_ID(wfId);
        for (Entry<String, String> entry : startEntrySet) {
          setNodeValue("start", sysWfNode, set_year, rg_code, list, entry);
        }
        sysWfNode.setLATEST_OP_USER(user_id);
        date = getDate();
        sysWfNode.setLATEST_OP_DATE(date);
        list.add(sysWfNode);
      }
      // end_流程图不完整时保存_开始节点_2017_04_21

      // begin_流程图不完整时保存_结束节点_2017_04_21
      Set<Entry<String, String>> endEntrySet = endKV.entrySet();
      if (!endEntrySet.isEmpty()) {
        SysWfNode sysWfNode1 = new SysWfNode();
        sysWfNode1.setWF_ID(wfId);
        for (Entry<String, String> entry : endEntrySet) {
          setNodeValue("end", sysWfNode1, set_year, rg_code, list, entry);
        }
        sysWfNode1.setLATEST_OP_USER(user_id);
        date = getDate();
        sysWfNode1.setLATEST_OP_DATE(date);
        list.add(sysWfNode1);
      }
      // end_流程图不完整时保存_结束节点_2017_04_21

      // begin_流程图不完整时保存_保存开始/结束节点_2017_04_21
      // if (!endEntrySet.isEmpty() || (!startEntrySet.isEmpty())) {
      String[] fieldsByTableName = new String[] { "wf_id", "node_code", "node_name", "node_type", "remark",
        "latest_op_user", "latest_op_date", "wf_table_name", "gather_flag", "id_column_name", "outer_table_name",
        "outer_column_name", "relation_column_name", "set_year", "rg_code" };
      dao.executeBatchBySql(sql, fieldsByTableName, list);
      // }
      // end_流程图不完整时保存_保存开始/结束节点_2017_04_21

    } catch (Exception e) {
      LOGGER.error("插入 【sys_wf_nodes】失败！" + e.getMessage(), e);
      throw e;
    }
  }

  /**
   * 获取工作流节点表Node_Id和Node_Code
   * 
   * @param wfId
   * @return
   * @throws Exception
   */
  public Map<String, String> getNodeIdAndNodeCode(String wf_id) throws Exception {
    String sql = "select node_id,node_code from sys_wf_nodes where wf_id=?";
    Map<String, String> map = new HashMap<String, String>();
    try {
      List<Map> list = dao.findBySql(sql, new Object[] { wf_id });
      for (Map m : list) {
        String nodeCode = (String) m.get("node_code");
        String nodeId = (String) m.get("node_id");
        map.put(nodeCode, nodeId);
      }
    } catch (Exception e) {
      LOGGER.error("查询 【sys_wf_nodes】失败！" + e.getMessage(), e);
      throw e;
    }
    return map;
  }

  /**
   * 在工作流功能表中插入菜单
   * 
   * @param moduleCodeAndNodeId
   * @param set_year
   * @param rg_code
   * @return
   * @throws Exception
   */
  public void setSysWfModuleNode(List<Map<String, String>> moduleCodeAndNodeId, int set_year, String rg_code)
    throws Exception {
    String sql = "INSERT INTO sys_wf_module_node (node_id,module_id,set_year,rg_code) VALUES (?,?,?,?)";
    try {
      List<SysWfModuleNode> list = new ArrayList<SysWfModuleNode>();
      for (Map<String, String> ma : moduleCodeAndNodeId) {
        for (String nd : ma.keySet()) {
          String moduleid = ma.get(nd);
          SysWfModuleNode sysWfModuleNode = new SysWfModuleNode();
          sysWfModuleNode.setNODE_ID(nd);
          sysWfModuleNode.setMODULE_ID(moduleid);
          sysWfModuleNode.setSET_YEAR(set_year);
          sysWfModuleNode.setRG_CODE(rg_code);
          list.add(sysWfModuleNode);
        }
      }
      String[] field = new String[] { "node_id", "module_id", "set_year", "rg_code" };
      dao.executeBatchBySql(sql, field, list);
    } catch (Exception e) {
      LOGGER.error("插入【sys_wf_module_node】失败！" + e.getMessage(), e);
      throw e;
    }
  }

  /**
   * 在工作流角色表中插入角色
   * 
   * @param roleCodeAndNodeId
   * @param set_year
   * @param rg_code
   * @return
   * @throws Exception
   */
  public void setSysWfRoleNode(List<Map<String, String>> roleCodeAndNodeId, int set_year, String rg_code)
    throws Exception {
    String sql = "INSERT INTO sys_wf_role_node (node_id,role_id,set_year,rg_code) VALUES (?,?,?,?)";
    try {
      ArrayList<SysWfRoleNode> list = new ArrayList<SysWfRoleNode>();
      for (Map<String, String> mr : roleCodeAndNodeId) {
        for (String nd : mr.keySet()) {
          String roleid = mr.get(nd);
          SysWfRoleNode sysWfRoleNode = new SysWfRoleNode();
          sysWfRoleNode.setNODE_ID(nd);
          sysWfRoleNode.setROLE_ID(roleid);
          sysWfRoleNode.setSET_YEAR(set_year);
          sysWfRoleNode.setRG_CODE(rg_code);
          list.add(sysWfRoleNode);
        }
      }
      String[] field = new String[] { "node_id", "role_id", "set_year", "rg_code" };
      dao.executeBatchBySql(sql, field, list);
    } catch (Exception e) {
      LOGGER.error("插入【sys_wf_role_node】失败！" + e.getMessage(), e);
      throw e;
    }
  }

  /**
   * 在工作流节点挂规则表中插入
   * 
   * @param ruleAccCodeAndNodeId
   * @param set_year
   * @param rg_code
   * @return
   * @throws Exception
   */
  public void setSysWfRuleNode(List<Map<String, String>> ruleAccCodeAndNodeId, int set_year, String rg_code)
    throws Exception {
    String sql = "INSERT INTO sys_wf_rule_node (node_id,rule_id,set_year,rg_code) VALUES (?,?,?,?)";
    try {
      ArrayList<SysWfRuleNode> list = new ArrayList<SysWfRuleNode>();
      for (Map<String, String> mr : ruleAccCodeAndNodeId) {
        for (String nd : mr.keySet()) {
          String ruleid = mr.get(nd);
          SysWfRuleNode sysWfRuleNode = new SysWfRuleNode();
          sysWfRuleNode.setNODE_ID(nd);
          sysWfRuleNode.setRULE_ID(ruleid);
          sysWfRuleNode.setSET_YEAR(set_year);
          sysWfRuleNode.setRG_CODE(rg_code);
          list.add(sysWfRuleNode);
        }
      }
      String[] field = new String[] { "node_id", "rule_id", "set_year", "rg_code" };
      dao.executeBatchBySql(sql, field, list);
    } catch (Exception e) {
      LOGGER.error("插入【sys_wf_rule_node】失败！" + e.getMessage(), e);
      throw e;
    }
  }

  /**
   * 用wf_id和node_code查询工作流节点表，获取node_id
   * 
   * @param idAndNodMap
   * @return
   * @throws Exception
   */
  public Map<String, String> getNodeIdByWfIdAndNodeCode(Map<String, String> idAndNodMap, String wf_id) throws Exception {
    String sql = "select node_id from sys_wf_nodes where wf_id=? and node_code=?";
    Map<String, String> hashMap = new HashMap<String, String>();
    try {
      for (String s : idAndNodMap.keySet()) {
        String ne = idAndNodMap.get(s);
        List<Map> nodeIdsList = dao.findBySql(sql, new Object[] { wf_id, ne });
        for (Map map : nodeIdsList) {
          String nodeId = (String) map.get("node_id");
          hashMap.put(s, nodeId);
        }
      }
    } catch (Exception e) {
      LOGGER.error("查询【sys_wf_role_node 2】失败！" + e.getMessage(), e);
      throw e;
    }
    return hashMap;
  }

  /**
   * 在工作流节点关系表和条件主表中插入数据
   * 
   * @param sflist
   * @param set_year
   * @param rg_code
   * @param wf_id
   * @throws Exception
   */
  public void setSysWfNodeConditionsAndSysWfConditions(List<Element> sflist, List<Element> sfJumplist, int set_year,
    String rg_code, String wf_id, Map<String, String> nodeIdByWfIdAndNodeCode, JsonNode editorNode) throws Exception {
    String sql1 = "INSERT INTO sys_wf_node_conditions (wf_id,node_id,next_node_id,condition_id,routing_type,set_year,rg_code,line_rule_id) VALUES (?,?,?,?,?,?,?,?)";
    String sql2 = "INSERT INTO sys_wf_conditions (CONDITION_ID,CONDITION_CODE,CONDITION_NAME,EXPRESSION,REMARK,CREATE_USER,CREATE_DATE,LATEST_OP_USER,LATEST_OP_DATE,LAST_VER,BSH_EXPRESSION,CONDITION_TYPE,SET_YEAR,RG_CODE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    String sql3 = "INSERT INTO sys_wf_line_rule (LINE_RULE_ID,RULE_ID,RULE_ORDER,LAST_VER,SET_YEAR,RG_CODE) VALUES (?,?,?,?,?,?)";
    String sql4 = "INSERT INTO sys_wf_rule_monitor (LINE_RULE_ID,CLASS_NAME,PARA_NAME,LAST_VER,SET_YEAR,RG_CODE) VALUES (?,?,?,?,?,?)";

    String sql_lines = "INSERT INTO sys_wf_condition_lines(LINE_ID,CONDITION_ID,OPERATOR,LOGIC_OPERATOR,"
      + "CREATE_USER,CREATE_DATE,LATEST_OP_USER,LATEST_OP_DATE,LEFT_PARE,RIGHT_PARE,LAST_VER,LEFT_PARAID"
      + ",RIGHT_PARAID,LINE_SORT,SET_YEAR,RG_CODE,LEFT_PARAVALUETYPE,RIGHT_PARAVALUETYPE,LEFT_PARANAME,RIGHT_PARANAME) values ("
      + (TypeOfDB.isOracle() ? "SEQ_SYS_WF_ID.Nextval" : "Nextval('SEQ_SYS_WF_ID')")
      + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    List<SysWfNodeCondition> SysWfNodeConditionList = new ArrayList<SysWfNodeCondition>();
    List<SysWfLineRule> sysWfLineRuleList = new ArrayList<SysWfLineRule>();
    List<SysWfRuleMonitor> sysWfRuleMonitorList = new ArrayList<SysWfRuleMonitor>();
    List<SysWfLineRule> sysWfLineRuleList2 = new ArrayList<SysWfLineRule>();
    List<SysWfRuleMonitor> sysWfRuleMonitorList2 = new ArrayList<SysWfRuleMonitor>();
    List<SysWfCondition> SysWfConditionList = new ArrayList<SysWfCondition>();
    List<SysWfNodeCondition> SysWfNodeJumpConditionList = new ArrayList<SysWfNodeCondition>();//跨节点
    try {
      for (Element e : sflist) {
        SysWfNodeCondition sysWfNodeCondition = new SysWfNodeCondition();
        SysWfNodeCondition sysWfNodeCondition2 = new SysWfNodeCondition();
        SysWfRuleMonitor sysWfRuleMonitor = new SysWfRuleMonitor();
        List<SysWfConditionLine> SysWfConditionLineList = new ArrayList<SysWfConditionLine>();// 创建明细表实体集合
        SysWfCondition sysWfCondition = new SysWfCondition();
        String sourceRef = e.attributeValue("sourceRef");
        String targetRef = e.attributeValue("targetRef");
        String id = e.attributeValue("id");
        Map existRule = isExistRule(id, editorNode);
        String rulesequenceflow = (String) existRule.get("rulesequenceflow");
        String monitorclass = (String) existRule.get("monitorclass");
        String monitorparameter = (String) existRule.get("monitorparameter");
        String lineRuleId = "";
        boolean ruleFlag = false;
        if (rulesequenceflow.equals("true") && monitorclass.equals("true") && monitorparameter.equals("true")) {
          lineRuleId = UUIDRandom.generate();
          ruleFlag = true;
        } else {
          lineRuleId = "#";
        }
        String showValueStr = (String) existRule.get("rulesequenceflowStr");
        String monitorclassStr = (String) existRule.get("monitorclassStr");
        String monitorparameterStr = (String) existRule.get("monitorparameterStr");
        Element element = e.element("conditionExpression");
        if (null != element) {
          exflag = true;
          expreson_circulation_line = element.getText();
          uuid = UUIDRandom.generate();

          // begin_解析表达式内容，3段信息，最后一个入条件明细表_2017_05_12
          JSONArray parseExpressionSetting = null;
          if ("#".equals(expreson_circulation_line)) {
            expressionDesc = "#";
            expressionScript = "#";
          } else {
            String[] split = expreson_circulation_line.split("\\*");
            expressionDesc = split[0];
            expressionScript = split[1];
            expressionSetting = split[2];
            parseExpressionSetting = parseExpressionSetting();
          }

          if (!"#".equals(expreson_circulation_line)) {
            insertIntoConDetail(set_year, rg_code, sql_lines, SysWfConditionLineList, parseExpressionSetting, uuid);
          }

        } else {
          uuid = "#";
          //begin_表达式为空的情况_2017-5-31_16:33
          expressionDesc = "#";
          expressionScript = "#";
          //end_表达式为空的情况_2017-5-31_16:33
        }
        Element element2 = e.element("documentation");
        if (null != element2) {
          conRemark = element2.getText();
        }
        // 当前时间
        date = getDate();
        String snoid = nodeIdByWfIdAndNodeCode.get(sourceRef);
        String tnoid = nodeIdByWfIdAndNodeCode.get(targetRef);
        sysWfNodeCondition.setWF_ID(wf_id);
        sysWfNodeCondition.setNODE_ID(snoid);
        sysWfNodeCondition.setNEXT_NODE_ID(tnoid);
        sysWfNodeCondition.setCONDITION_ID(uuid);
        sysWfNodeCondition.setROUTING_TYPE("001");
        sysWfNodeCondition.setSET_YEAR(set_year);
        sysWfNodeCondition.setRG_CODE(rg_code);
        sysWfNodeCondition.setLINE_RULE_ID(lineRuleId);
        SysWfNodeConditionList.add(sysWfNodeCondition);

        sysWfNodeCondition2.setWF_ID(wf_id);
        sysWfNodeCondition2.setNODE_ID(tnoid);
        sysWfNodeCondition2.setNEXT_NODE_ID(snoid);
        sysWfNodeCondition2.setCONDITION_ID(uuid);
        sysWfNodeCondition2.setROUTING_TYPE("002");
        sysWfNodeCondition2.setSET_YEAR(set_year);
        sysWfNodeCondition2.setRG_CODE(rg_code);
        sysWfNodeCondition2.setLINE_RULE_ID("#");
        SysWfNodeConditionList.add(sysWfNodeCondition2);

        if (ruleFlag) {
          String[] sp = showValueStr.split(",");
          int r = 1;
          for (int i = 0; i < sp.length; i++) {
            String str = sp[i];
            String ruleId = str.split(" ")[0];
            SysWfLineRule sysWfLineRule = new SysWfLineRule();
            sysWfLineRule.setLINE_RULE_ID(lineRuleId);
            sysWfLineRule.setRULE_ID(ruleId);
            sysWfLineRule.setRULE_ORDER(r);
            sysWfLineRule.setLAST_VER(date);
            sysWfLineRule.setSET_YEAR(set_year);
            sysWfLineRule.setRG_CODE(rg_code);
            sysWfLineRuleList.add(sysWfLineRule);
            r++;
          }
          sysWfRuleMonitor.setLINE_RULE_ID(lineRuleId);
          sysWfRuleMonitor.setCLASS_NAME(monitorclassStr);
          sysWfRuleMonitor.setPARA_NAME(monitorparameterStr);
          sysWfRuleMonitor.setLAST_VER(date);
          sysWfRuleMonitor.setSET_YEAR(set_year);
          sysWfRuleMonitor.setRG_CODE(rg_code);
          sysWfRuleMonitorList.add(sysWfRuleMonitor);
        }

        if (exflag) {
          sysWfCondition.setCONDITION_ID(uuid);
          sysWfCondition.setCONDITION_CODE(uuid);
          sysWfCondition.setCONDITION_NAME(uuid);
          sysWfCondition.setREMARK(conRemark);
          sysWfCondition.setCREATE_USER(user_id);
          sysWfCondition.setCREATE_DATE(date);
          sysWfCondition.setLATEST_OP_USER(user_id);
          sysWfCondition.setLATEST_OP_DATE(date);
          sysWfCondition.setLAST_VER(date);
          sysWfCondition.setBSH_EXPRESSION(expressionScript);// 录入脚本信息
          sysWfCondition.setEXPRESSION(expressionDesc);// 录入描述信息
          sysWfCondition.setCONDITION_TYPE(conditionType);
          sysWfCondition.setSET_YEAR(set_year);
          sysWfCondition.setRG_CODE(rg_code);
          SysWfConditionList.add(sysWfCondition);
        }
        conRemark = "";
      }

      //begin_取出所有的红线信息_2017_6-8_sfJumplist
      for (Element eJumpList : sfJumplist) {
        SysWfRuleMonitor sysWfRuleMonitor = new SysWfRuleMonitor();
        SysWfNodeCondition sysWfNodeCondition2 = new SysWfNodeCondition();
        List<SysWfConditionLine> SysWfConditionLineList = new ArrayList<SysWfConditionLine>();// 创建明细表实体集合
        SysWfCondition sysWfCondition = new SysWfCondition();
        String sourceRef = eJumpList.attributeValue("sourceRef");
        String targetRef = eJumpList.attributeValue("targetRef");
        //*********回退线上增加规则***********
        String id = eJumpList.attributeValue("id");
        Map existRule = isExistRule(id, editorNode);
        String rulesequenceflow = (String) existRule.get("rulesequenceflow");
        String monitorclass = (String) existRule.get("monitorclass");
        String monitorparameter = (String) existRule.get("monitorparameter");
        String lineRuleId = "";
        boolean ruleFlag = false;
        if (rulesequenceflow.equals("true") && monitorclass.equals("true") && monitorparameter.equals("true")) {
          lineRuleId = UUIDRandom.generate();
          ruleFlag = true;
        } else {
          lineRuleId = "#";
        }
        String showValueStr = (String) existRule.get("rulesequenceflowStr");
        String monitorclassStr = (String) existRule.get("monitorclassStr");
        String monitorparameterStr = (String) existRule.get("monitorparameterStr");
        //*********回退线上增加规则***********
        Element element = eJumpList.element("conditionExpression");
        if (null != element) {
          exflag = true;
          expreson_circulation_line = element.getText();
          uuid = UUIDRandom.generate();

          // begin_解析表达式内容，3段信息，最后一个入条件明细表_2017_05_12
          JSONArray parseExpressionSetting = null;
          if ("#".equals(expreson_circulation_line)) {
            expressionDesc = "#";
            expressionScript = "#";
          } else {
            String[] split = expreson_circulation_line.split("\\*");
            expressionDesc = split[0];
            expressionScript = split[1];
            expressionSetting = split[2];
            parseExpressionSetting = parseExpressionSetting();
          }

          if (!"#".equals(expreson_circulation_line)) {
            insertIntoConDetail(set_year, rg_code, sql_lines, SysWfConditionLineList, parseExpressionSetting, uuid);
          }

        } else {
          uuid = "#";
          //begin_表达式为空的情况_2017-5-31_16:33
          expressionDesc = "#";
          expressionScript = "#";
          //end_表达式为空的情况_2017-5-31_16:33
        }
        Element element2 = eJumpList.element("documentation");
        if (null != element2) {
          conRemark = element2.getText();
        }
        // 当前时间
        date = getDate();
        String snoid = nodeIdByWfIdAndNodeCode.get(sourceRef);
        String tnoid = nodeIdByWfIdAndNodeCode.get(targetRef);

        sysWfNodeCondition2.setWF_ID(wf_id);
        sysWfNodeCondition2.setNODE_ID(snoid);
        sysWfNodeCondition2.setNEXT_NODE_ID(tnoid);
        sysWfNodeCondition2.setCONDITION_ID(uuid);
        sysWfNodeCondition2.setROUTING_TYPE("002");
        sysWfNodeCondition2.setSET_YEAR(set_year);
        sysWfNodeCondition2.setRG_CODE(rg_code);
        sysWfNodeCondition2.setLINE_RULE_ID(lineRuleId);
        SysWfNodeJumpConditionList.add(sysWfNodeCondition2);//将节点条件数据放进跨节点集合中
        if (ruleFlag) {
          String[] sp = showValueStr.split(",");
          int r = 1;
          for (int i = 0; i < sp.length; i++) {
            String str = sp[i];
            String ruleId = str.split(" ")[0];
            SysWfLineRule sysWfLineRule = new SysWfLineRule();
            sysWfLineRule.setLINE_RULE_ID(lineRuleId);
            sysWfLineRule.setRULE_ID(ruleId);
            sysWfLineRule.setRULE_ORDER(r);
            sysWfLineRule.setLAST_VER(date);
            sysWfLineRule.setSET_YEAR(set_year);
            sysWfLineRule.setRG_CODE(rg_code);
            sysWfLineRuleList2.add(sysWfLineRule);
            r++;
          }
          sysWfRuleMonitor.setLINE_RULE_ID(lineRuleId);
          sysWfRuleMonitor.setCLASS_NAME(monitorclassStr);
          sysWfRuleMonitor.setPARA_NAME(monitorparameterStr);
          sysWfRuleMonitor.setLAST_VER(date);
          sysWfRuleMonitor.setSET_YEAR(set_year);
          sysWfRuleMonitor.setRG_CODE(rg_code);
          sysWfRuleMonitorList2.add(sysWfRuleMonitor);
        }
        if (exflag) {
          sysWfCondition.setCONDITION_ID(uuid);
          sysWfCondition.setCONDITION_CODE(uuid);
          sysWfCondition.setCONDITION_NAME(uuid);
          sysWfCondition.setREMARK(conRemark);
          sysWfCondition.setCREATE_USER(user_id);
          sysWfCondition.setCREATE_DATE(date);
          sysWfCondition.setLATEST_OP_USER(user_id);
          sysWfCondition.setLATEST_OP_DATE(date);
          sysWfCondition.setLAST_VER(date);
          sysWfCondition.setBSH_EXPRESSION(expressionScript);// 录入脚本信息
          sysWfCondition.setEXPRESSION(expressionDesc);// 录入描述信息
          sysWfCondition.setCONDITION_TYPE(conditionType);
          sysWfCondition.setSET_YEAR(set_year);
          sysWfCondition.setRG_CODE(rg_code);
          SysWfConditionList.add(sysWfCondition);
        }
        conRemark = "";
      }
      //end_取出所有的红线信息_2017_6-8

      //begin_过滤有跨节点源节点的所有逆向_2017_5_25
      List<SysWfNodeCondition> SysWfNodeFinalConditionList = new ArrayList<SysWfNodeCondition>();//跨节点
      HashMap<String, SysWfNodeCondition> hashMap = new HashMap<String, SysWfNodeCondition>();
      HashMap<String, SysWfNodeCondition> delHashMap = new HashMap<String, SysWfNodeCondition>();
      if (SysWfNodeJumpConditionList.size() != 0) {
        for (SysWfNodeCondition jumpCondition : SysWfNodeJumpConditionList) {
          for (SysWfNodeCondition condition : SysWfNodeConditionList) {
            if ("002".equals(condition.getROUTING_TYPE())) {
              if (jumpCondition.getNODE_ID().equals(condition.getNODE_ID())) {
                //SysWfNodeFinalConditionList.add(condition);
                // hashMap.put(condition.getNODE_ID()+condition.getNEXT_NODE_ID(), condition);
                delHashMap.put(condition.getNODE_ID() + condition.getNEXT_NODE_ID(), condition);
                if (!hashMap.isEmpty()) {
                  if (hashMap.containsKey(condition.getNODE_ID() + condition.getNEXT_NODE_ID())) {
                    hashMap.remove(condition.getNODE_ID() + condition.getNEXT_NODE_ID());
                  }

                }
              } else {
                if (delHashMap.isEmpty()) {
                  hashMap.put(condition.getNODE_ID() + condition.getNEXT_NODE_ID(), condition);
                } else {
                  if (!delHashMap.containsKey(condition.getNODE_ID() + condition.getNEXT_NODE_ID())) {
                    hashMap.put(condition.getNODE_ID() + condition.getNEXT_NODE_ID(), condition);
                  }
                }

              }
            } else {
              // SysWfNodeFinalConditionList.add(condition);
              hashMap.put(condition.getNODE_ID() + condition.getNEXT_NODE_ID(), condition);
            }
          }
          //加上跨节点的
          //SysWfNodeFinalConditionList.add(jumpCondition);
          hashMap.put(jumpCondition.getNODE_ID() + jumpCondition.getNEXT_NODE_ID(), jumpCondition);
        }

        for (SysWfNodeCondition value : hashMap.values()) {
          SysWfNodeFinalConditionList.add(value);
        }
      }
      //end_过滤有跨节点源节点的所有逆向_2017_5_25

      String[] wfNodeConditionsFields = new String[] { "wf_id", "node_id", "next_node_id", "condition_id",
        "routing_type", "set_year", "rg_code", "line_rule_id" };
      dao.executeBatchBySql(sql1, wfNodeConditionsFields,
        SysWfNodeFinalConditionList.size() == 0 ? SysWfNodeConditionList : SysWfNodeFinalConditionList);
      if (!("".equals(expreson) || null == expreson)) {
        String con_id = "";
        List<Map> wfIdAndConditionId = getConditionIdByWfId(wf_id);
        for (Map m : wfIdAndConditionId) {
          con_id = (String) m.get("condition_id");
        }
        // begin_解析入口条件数据_2017_5_22
        List<SysWfConditionLine> SysWfConditionLineList = new ArrayList<SysWfConditionLine>();// 创建明细表实体集合
        JSONArray parseExpressionSetting = null;
        if ("#".equals(expreson)) {
          expressionDesc = "#";
          expressionScript = "#";
        } else {
          String[] split = expreson.split("\\*");
          expressionDesc = split[0];
          expressionScript = split[1];
          expressionSetting = split[2];
          parseExpressionSetting = parseExpressionSetting();
        }
        if (!"#".equals(expreson)) {
          insertIntoConDetail(set_year, rg_code, sql_lines, SysWfConditionLineList, parseExpressionSetting, con_id);
        }
        // end_解析入口条件数据_2017_5_22

        SysWfCondition sysWfCondition1 = new SysWfCondition();
        sysWfCondition1.setCONDITION_ID(con_id);
        sysWfCondition1.setCONDITION_CODE(con_id);
        sysWfCondition1.setCONDITION_NAME(con_id);
        sysWfCondition1.setREMARK("");
        sysWfCondition1.setCREATE_USER(user_id);
        date = getDate();
        sysWfCondition1.setCREATE_DATE(date);
        sysWfCondition1.setLATEST_OP_USER(user_id);
        sysWfCondition1.setLATEST_OP_DATE(date);
        sysWfCondition1.setLAST_VER(date);
        sysWfCondition1.setBSH_EXPRESSION(expressionScript);// 录入脚本信息
        sysWfCondition1.setEXPRESSION(expressionDesc);// 录入描述信息
        sysWfCondition1.setCONDITION_TYPE(conditionType);
        sysWfCondition1.setSET_YEAR(set_year);
        sysWfCondition1.setRG_CODE(rg_code);
        SysWfConditionList.add(sysWfCondition1);
      }
      String[] wfConditionFields = new String[] { "condition_id", "condition_code", "condition_name", "EXPRESSION",
        "remark", "create_user", "create_date", "latest_op_user", "latest_op_date", "last_ver", "bsh_expression",
        "condition_type", "set_year", "rg_code" };
      dao.executeBatchBySql(sql2, wfConditionFields, SysWfConditionList);
      String[] wfLineRuleFields = new String[] { "line_rule_id", "rule_id", "rule_order", "last_ver", "set_year",
        "rg_code" };
      dao.executeBatchBySql(sql3, wfLineRuleFields, sysWfLineRuleList);
      dao.executeBatchBySql(sql3, wfLineRuleFields, sysWfLineRuleList2);
      String[] wfRuleMonitorFields = new String[] { "line_rule_id", "class_name", "para_name", "last_ver", "set_year",
        "rg_code" };
      dao.executeBatchBySql(sql4, wfRuleMonitorFields, sysWfRuleMonitorList);
      dao.executeBatchBySql(sql4, wfRuleMonitorFields, sysWfRuleMonitorList2);
    } catch (Exception e) {
      LOGGER.error("插入【sys_wf_node_conditions】 和 【sys_wf_conditions】失败！" + e.getMessage(), e);
      throw e;
    }
  }

  /**zhaoxda
   * 判断改流转线上有无规则，监听类，参数
   * @param id 流转线ID
   * @param editorNode
   * @return
   */
  private Map isExistRule(String id, JsonNode editorNode) {
    Map map = new HashMap();
    ArrayNode childShapes = (ArrayNode) editorNode.get("childShapes");
    for (JsonNode object : childShapes) {
      if (id.equals(object.get("resourceId").asText())) {
        JsonNode properties = object.get("properties");
        JsonNode rulesequenceflow = properties.get("rulesequenceflow");
        JsonNodeType nodeType = rulesequenceflow.getNodeType();
        String nt = nodeType.toString();
        if (rulesequenceflow.isNull() || nt.equals("STRING")) {
          map.put("rulesequenceflow", "false");
        } else {
          String showValue = rulesequenceflow.get("showValue").asText();
          map.put("rulesequenceflow", "true");
          map.put("rulesequenceflowStr", showValue);
        }
        String monitorclass = properties.get("monitorclass").asText();
        if ("".equals(monitorclass)) {
          map.put("monitorclass", "false");
        } else {
          map.put("monitorclass", "true");
          map.put("monitorclassStr", monitorclass);
        }
        String monitorparameter = properties.get("monitorparameter").asText();
        if ("".equals(monitorparameter)) {
          map.put("monitorparameter", "false");
        } else {
          map.put("monitorparameter", "true");
          map.put("monitorparameterStr", monitorparameter);
        }
      }
    }
    return map;
  }

  private void insertIntoConDetail(int set_year, String rg_code, String sql_lines,
    List<SysWfConditionLine> SysWfConditionLineList, JSONArray parseExpressionSetting, String condition_ID) {
    // 录入明细表
    for (int i = 0; i < parseExpressionSetting.size(); i++) {
      SysWfConditionLine sysWfConditionLine = new SysWfConditionLine();// 创建明细表实体
      JSONObject jsStr = JSONObject.parseObject(parseExpressionSetting.getString(i));
      String left_paraname_noFlag = (String) jsStr.get("left_paraname_noFlag");
      String left_paraname = (String) jsStr.get("left_paraname");

      String right_paraname_noFlag = (String) jsStr.get("right_paraname_noFlag");
      String right_paraname = (String) jsStr.get("right_paraname");

      //*******异常数据升级保存***********
      if (!(left_paraname_noFlag == null || left_paraname_noFlag.equals(" ") || left_paraname_noFlag.equals(""))) {
        String[] left_paraname_noFlagArr = left_paraname_noFlag.split(" ");
        sysWfConditionLine.setLEFT_PARANAME("[" + left_paraname_noFlagArr[0] + "]" + left_paraname + "["
          + left_paraname_noFlagArr[1] + "]");// 左名称
      } else {
        sysWfConditionLine.setLEFT_PARANAME(" ");
      }
      if (!(right_paraname_noFlag == null || right_paraname_noFlag.equals(" ") || right_paraname_noFlag.equals(""))) {
        sysWfConditionLine.setRIGHT_PARANAME("[" + right_paraname_noFlag + "]" + right_paraname);// 右名称
      } else {
        sysWfConditionLine.setRIGHT_PARANAME(" ");
      }
      //*******异常数据升级保存***********

      sysWfConditionLine.setRIGHT_PARAID((String) jsStr.get("right_paraid"));

      sysWfConditionLine.setRIGHT_PARAVALUETYPE((String) jsStr.get("right_paravaluetype"));

      sysWfConditionLine.setRIGHT_PARE((String) jsStr.get("right_pare"));

      sysWfConditionLine.setLOGIC_OPERATOR((String) jsStr.get("logic_operator"));

      sysWfConditionLine.setOPERATOR((String) jsStr.get("operator"));

      sysWfConditionLine.setLEFT_PARE((String) jsStr.get("left_pare"));

      sysWfConditionLine.setLEFT_PARAID((String) jsStr.get("left_paraid"));

      sysWfConditionLine.setLEFT_PARAVALUETYPE((String) jsStr.get("left_paravaluetype"));

      sysWfConditionLine.setCONDITION_ID(condition_ID);

      sysWfConditionLine.setLINE_SORT(Long.valueOf(String.valueOf(i)));

      sysWfConditionLine.setRG_CODE(rg_code);

      sysWfConditionLine.setSET_YEAR(set_year);

      sysWfConditionLine.setCREATE_DATE(getDate());

      sysWfConditionLine.setCREATE_USER(user_id);

      sysWfConditionLine.setLATEST_OP_DATE(getDate());

      sysWfConditionLine.setLATEST_OP_USER(user_id);

      sysWfConditionLine.setLAST_VER(getDate());

      // sysWfConditionLine.setPara_type("");

      SysWfConditionLineList.add(sysWfConditionLine);

    }

    String[] wfNodeConditionsLineFields = new String[] {/*
                                                        * "LINE_ID",
                                                        * "CONDITION_ID"
                                                        * ,"OPERATOR"
                                                        * ,"LOGIC_OPERATOR"
                                                        * ,"CREATE_USER",
                                                        * "CREATE_DATE"
                                                        * ,"LATEST_OP_USER"
                                                        * ,"LATEST_OP_DATE"
                                                        * ,"LEFT_PARE",
                                                        * "RIGHT_PARE"
                                                        * ,"LAST_VER"
                                                        * ,"LEFT_PARAID"
                                                        * ,"RIGHT_PARAID"
                                                        * ,"LINE_SORT"
                                                        * ,"SET_YEAR"
                                                        * ,"RG_CODE"
                                                        * ,"PARA_TYPE"
                                                        * ,"LEFT_PARAVALUETYPE"
                                                        * ,
                                                        * "RIGHT_PARAVALUETYPE"
                                                        * ,"LEFT_PARANAME",
                                                        * "RIGHT_PARANAME"
                                                        */

    "CONDITION_ID", "OPERATOR", "LOGIC_OPERATOR", "CREATE_USER", "CREATE_DATE", "LATEST_OP_USER", "LATEST_OP_DATE",
      "LEFT_PARE", "RIGHT_PARE", "LAST_VER", "LEFT_PARAID", "RIGHT_PARAID", "LINE_SORT", "SET_YEAR", "RG_CODE",
      "LEFT_PARAVALUETYPE", "RIGHT_PARAVALUETYPE", "LEFT_PARANAME", "RIGHT_PARANAME"

    };

    dao.executeBatchBySql(sql_lines, wfNodeConditionsLineFields, SysWfConditionLineList);

    // end_解析表达式内容，3段信息，最后一个入条件明细表_2017_05_12
  }

  // /begin_解析表达式内容，3段信息，最后一个入条件明细表_2017_05_12
  private JSONArray parseExpressionSetting() {
    expressionSetting = expressionSetting.substring(1, expressionSetting.length() - 1);
    JSONObject pp = new JSONObject();
    JSONArray op = pp.parseArray(expressionSetting);
    /*
     * for(int i=0;i<op.size();i++){ JSONObject jsStr =
     * JSONObject.parseObject(op.getString(i)); String left_paraname_noFlag
     * = (String) jsStr.get("left_paraname_noFlag");
     * 
     * 
     * }
     */
    return op;
  }

  // end_解析表达式内容，3段信息，最后一个入条件明细表_2017_05_12

  /**
   * 在工作流操作记账类型表中插入数据
   * 
   * @param otmrtiCodeAndNodeId
   * @param set_year
   * @param rg_code
   * @throws Exception
   */
  public void setSysWfNodeTollyActionType(List<Map<String, String>> otmrtiCodeAndNodeId, int set_year, String rg_code)
    throws Exception {
    String sql = "INSERT INTO sys_wf_node_tolly_action_type (node_id,action_type_code,tolly_flag,set_year,rg_code) VALUES (?,?,?,?,?)";
    List<SysWfNodeTollyActionType> sysWfNodeTollyActionTypeList = new ArrayList<SysWfNodeTollyActionType>();
    try {
      for (Map<String, String> mr : otmrtiCodeAndNodeId) {
        for (String nd : mr.keySet()) {
          String otm = mr.get(nd);
          String otmr = otmrti(otm);
          String[] split = otmr.split(" ");
          String action_type = split[0];
          String tolly_flag = split[1];
          int tollyFlag = Integer.parseInt(tolly_flag);
          SysWfNodeTollyActionType sysWfNodeTollyActionType = new SysWfNodeTollyActionType();
          sysWfNodeTollyActionType.setNODE_ID(nd);
          sysWfNodeTollyActionType.setACTION_TYPE_CODE(action_type);
          sysWfNodeTollyActionType.setTOLLY_FLAG(tollyFlag);
          sysWfNodeTollyActionType.setSET_YEAR(set_year);
          sysWfNodeTollyActionType.setRG_CODE(rg_code);
          sysWfNodeTollyActionTypeList.add(sysWfNodeTollyActionType);
        }
      }
      String[] wfActionTypes = new String[] { "node_id", "action_type_code", "tolly_flag", "set_year", "rg_code" };
      dao.executeBatchBySql(sql, wfActionTypes, sysWfNodeTollyActionTypeList);
    } catch (Exception e) {
      LOGGER.error("插入【sys_wf_node_tolly_action_type 】失败！" + e.getMessage(), e);
      throw e;
    }
  }

  public String getDate() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String date = simpleDateFormat.format(new Date());
    return date;
  }

  public String otmrti(String otmr) {
    String[] split = otmr.split(" ");
    String otmr1 = split[0];
    String otmr2 = split[1];
    if ("提取".equals(otmr1)) {
      otmr1 = "DISTILL";
    }
    if ("跨节点挂起".equals(otmr1)) {
      otmr1 = "STRIDE_HANG";
    }
    if ("跨节点删除".equals(otmr1)) {
      otmr1 = "STRIDE_DELETE";
    }
    if ("跨节点作废".equals(otmr1)) {
      otmr1 = "STRIDE_DISCARD";
    }
    if ("录入".equals(otmr1)) {
      otmr1 = "INPUT";
    }
    if ("修改".equals(otmr1)) {
      otmr1 = "EDIT";
    }
    if ("审核".equals(otmr1)) {
      otmr1 = "NEXT";
    }
    if ("退回".equals(otmr1)) {
      otmr1 = "BACK";
    }
    if ("撤消".equals(otmr1)) {
      otmr1 = "RECALL";
    }
    if ("删除".equals(otmr1)) {
      otmr1 = "DELETE";
    }
    if ("作废".equals(otmr1)) {
      otmr1 = "DISCARD";
    }
    if ("挂起".equals(otmr1)) {
      otmr1 = "HANG";
    }
    if ("在途记账".equals(otmr2)) {
      otmr2 = "0";
    }
    if ("终审记账".equals(otmr2)) {
      otmr2 = "1";
    }
    String str = otmr1 + " " + otmr2;
    return str;
  }

  private void setNodeValue(String startOrEnd, SysWfNode sysWfNode, int set_year, String rg_code,
    List<SysWfNode> paramsWfnodes, Entry<String, String> entry) {
    String key = entry.getKey();
    String value = entry.getValue() == null ? "" : entry.getValue();
    if (key.equals("nodenumber")) {
      if (value.contains("StartNoneEvent")) {
        String nodeNumber = "1";
        sysWfNode.setNODE_CODE(nodeNumber);
      } else if (value.contains("EndNoneEvent")) {
        String nodeNumber = "2";
        sysWfNode.setNODE_CODE(nodeNumber);
      } else {
        sysWfNode.setNODE_CODE(value);
      }
    }
    if (key.equals("nodename")) {
      sysWfNode.setNODE_NAME(value);
    }
    if ("start".equals(startOrEnd)) {
      if (key.equals("startnodetype")) {
        sysWfNode.setNODE_TYPE("001");
      }
    } else {
      if (key.equals("endnodetype")) {
        sysWfNode.setNODE_TYPE("003");
      }
    }
    if (key.equals("remark")) {
      sysWfNode.setREMARK(value);
    }
    if (key.equals("multiinstance_handletype")) {
      String[] split = value.split(" ");
      value = split[0];
      sysWfNode.setWF_TABLE_NAME(value);
      if (split.length == 2) {
        value = split[1];
        sysWfNode.setID_COLUMN_NAME(value);
      } else {
        sysWfNode.setID_COLUMN_NAME("");
      }
    }
    // TODO
    // if (key.equals("multiinstance_handletype")) {
    // if (value != null && !(value.equals("luru"))) {
    // String[] split = value.split(" ");
    // value = split[0];
    // sysWfNode.setWF_TABLE_NAME(value);
    // if (split.length == 2) {
    // value = split[1];
    // sysWfNode.setID_COLUMN_NAME(value);
    // } else {
    // sysWfNode.setID_COLUMN_NAME("");
    // }
    // } else {
    // sysWfNode.setWF_TABLE_NAME("");
    // sysWfNode.setID_COLUMN_NAME("");
    // }
    // }
    if (key.equals("multiinstance_person")) {
      if (value.contains("yibu")) {
        sysWfNode.setGATHER_FLAG(1);
      } else {
        sysWfNode.setGATHER_FLAG(0);
      }
    }
    // if (key.equals("itmti")) {
    // sysWfNode.setID_COLUMN_NAME(value);
    // }
    if (key.equals("multiinstance_outtertrantablename")) {
      String[] split = value.split(" ");
      value = split[0];
      sysWfNode.setOUTER_TABLE_NAME(value);
      if (split.length == 2) {
        value = split[1];
        sysWfNode.setOUTER_COLUMN_NAME(value);
      } else {
        sysWfNode.setOUTER_COLUMN_NAME("");
      }
    }
    // if (key.equals("otmti")) {
    // sysWfNode.setOUTER_COLUMN_NAME(value);
    // }
    // TODO
    // if (key.equals("multiinstance_outtertrantablename")) {
    // if (value != null && !(value.equals("luru"))) {
    // String[] split = value.split(" ");
    // value = split[0];
    // sysWfNode.setOUTER_TABLE_NAME(value);
    // if (split.length == 2) {
    // value = split[1];
    // sysWfNode.setOUTER_COLUMN_NAME(value);
    // } else {
    // sysWfNode.setOUTER_COLUMN_NAME("");
    // }
    // } else {
    // sysWfNode.setOUTER_TABLE_NAME("");
    // sysWfNode.setOUTER_COLUMN_NAME("");
    // }
    // }
    if (key.equals("etreid")) {
      sysWfNode.setRELATION_COLUMN_NAME(value);
    }
    sysWfNode.setSET_YEAR(set_year);
    sysWfNode.setRG_CODE(rg_code);
  }

  /**
   * 返回节点类型和NodeCode
   */
  public Map<String, String> getIdAndNodenumber(List<Element> list, List<Element> selist, List<Element> nelist) {
    Map<String, String> map = new HashMap<String, String>();
    for (Element e : list) {
      String id = e.attributeValue("id");
      String[] split = id.split("ApproveUserTask");
      String nodenumber = String.valueOf(Integer.parseInt(split[1]));//去掉编号+2
      map.put(id, nodenumber);
    }
    for (Element e : selist) {
      String id = e.attributeValue("id");
      String nodenumber = "1";
      map.put(id, nodenumber);
    }
    for (Element e : nelist) {
      String id = e.attributeValue("id");
      String nodenumber = "2";
      map.put(id, nodenumber);
    }
    return map;
  }

  /**
   * 获取nodeId和code
   */
  public List<Map<String, String>> getRo(List<Map<String, String>> moduleCodeAndNo) {
    List<Map<String, String>> list = new ArrayList();
    String nodeId = "";
    String moduleId = "";
    for (Map<String, String> moduleCodeAndNodeId : moduleCodeAndNo) {
      for (String c : moduleCodeAndNodeId.keySet()) {
        nodeId = c;
        moduleId = moduleCodeAndNodeId.get(c);
        Map<String, String> ma = new HashMap<String, String>();
        ma.put(nodeId, moduleId);
        list.add(ma);
      }
    }
    return list;
  }

  /**
   * 获取审批任务节点 菜单、角色、操作记账类型、规则 id
   * 
   */
  public List getMenuOrRole(JsonNode modelNode, String fun) {
    ArrayNode shapesArrayNode = (ArrayNode) modelNode.get("childShapes");
    List list = new ArrayList();
    boolean flag = false;
    if ("otmrti".equals(fun)) {
      flag = true;
    }
    for (JsonNode shapeNode : shapesArrayNode) {
      // begin_根据节点属性，获得属性值
      String nodeType = shapeNode.get("stencil").get("id").asText();
      JsonNode jsonpropertiesNode = shapeNode.get("properties");
      if (!nodeType.equals("") && nodeType != null) {
        // 获得任务节点信息
        if (nodeType.equals("ApproveUserTask")) {
          String funNodeNum = jsonpropertiesNode.get("overrideid").asText();
          String[] split = funNodeNum.split("ApproveUserTask");
          String funNodeNumber = String.valueOf(Integer.parseInt(split[1]));//去掉编号+2
          JsonNode functionauthNode = jsonpropertiesNode.get(fun);
          if (functionauthNode == null) {
            return null;
          } else {
            JsonNode funRo = functionauthNode.get("refResultData");
            if (funRo != null) {
              for (JsonNode jsonNode : funRo) {
                String funRole = "";
                if (flag) {
                  String funRol1 = jsonNode.get("Name1").asText();
                  String funRol2 = jsonNode.get("Name2").asText();
                  funRole = funRol1 + " " + funRol2;
                } else {
                  funRole = jsonNode.get("id").asText();
                }
                Map<String, String> map = new HashMap<String, String>();
                map.put(funNodeNumber, funRole);
                list.add(map);
              }
            }
          }
          // 获得开始节点 操作记账授权
        } else if (flag && nodeType.equals("StartNoneEvent")) {
          String funNodeNum = jsonpropertiesNode.get("overrideid").asText();
          String[] split = funNodeNum.split("StartNoneEvent");
          String funNodeNumber = "1";
          JsonNode functionauthNode = jsonpropertiesNode.get(fun);
          if (functionauthNode.isNull()) {
            return null;
          } else {
            JsonNode funRo = functionauthNode.get("refResultData");
            if (funRo != null) {
              for (JsonNode jsonNode : funRo) {
                String funRol1 = jsonNode.get("Name1").asText();
                String funRol2 = jsonNode.get("Name2").asText();
                String funRole = funRol1 + " " + funRol2;
                Map<String, String> map = new HashMap<String, String>();
                map.put(funNodeNumber, funRole);
                list.add(map);
              }
            }
          }
        }
      }
    }
    return list;
  }

  /**
   * 获得开始/结束节点的基本信息
   * 
   * @author zhangbch
   * @param modelNode
   * @param fun
   * @return
   */
  public List<Map<String, String>> getStartOrEndBaseInfo(JsonNode modelNode) {
    ArrayNode shapesArrayNode = (ArrayNode) modelNode.get("childShapes");
    Map<String, String> startMap = new HashMap<String, String>();
    Map<String, String> endMap = new HashMap<String, String>();
    ArrayList<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();

    for (JsonNode shapeNode : shapesArrayNode) {
      String nodeType = shapeNode.get("stencil").get("id").asText();
      JsonNode jsonpropertiesNode = shapeNode.get("properties");
      if (!nodeType.equals("") && nodeType != null) {
        if (nodeType.equals("StartNoneEvent")) {
          getStartMap(startMap, jsonpropertiesNode);
          // 加入sid和node
          startMap.put(
            jsonpropertiesNode.get("overrideid").toString()
              .substring(1, jsonpropertiesNode.get("overrideid").toString().length() - 1), shapeNode.get("resourceId")
              .toString().substring(1, shapeNode.get("resourceId").toString().length() - 1));
        }
        if (nodeType.equals("EndNoneEvent")) {
          getEndMap(endMap, jsonpropertiesNode);
          endMap.put(
            jsonpropertiesNode.get("overrideid").toString()
              .substring(1, jsonpropertiesNode.get("overrideid").toString().length() - 1), shapeNode.get("resourceId")
              .toString().substring(1, shapeNode.get("resourceId").toString().length() - 1));
        }
      }
    }
    arrayList.add(startMap);
    arrayList.add(endMap);
    return arrayList;
  }

  /**
   * 获取审批任务节点信息
   * 
   * @param editorNode
   * @return
   */
  public List<Map<String, String>> getApproveUserTaskInfo(JsonNode editorNode) {
    ArrayNode shapesArrayNode = (ArrayNode) editorNode.get("childShapes");
    ArrayList<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
    for (JsonNode shapeNode : shapesArrayNode) {
      String nodeType = shapeNode.get("stencil").get("id").asText();
      JsonNode jsonpropertiesNode = shapeNode.get("properties");
      if (!nodeType.equals("") && nodeType != null) {
        if (nodeType.equals("ApproveUserTask")) {
          Map<String, String> appMap = new HashMap<String, String>();
          String name = jsonpropertiesNode.get("name").asText();
          String overrideid = jsonpropertiesNode.get("overrideid").asText();
          String nodetype = jsonpropertiesNode.get("nodetype").asText();
          String multiinstance_person = jsonpropertiesNode.get("multiinstance_person").asText();
          String multiinstance_handletype = jsonpropertiesNode.get("multiinstance_handletype").asText();
          String multiinstance_outtertrantablename = jsonpropertiesNode.get("multiinstance_outtertrantablename")
            .asText();
          String etreid = jsonpropertiesNode.get("etreid").asText();
          String remark = jsonpropertiesNode.get("remark").asText();
          appMap.put("name", name);
          appMap.put("overrideid", overrideid);
          appMap.put("nodetype", nodetype);
          appMap.put("multiinstance_person", multiinstance_person);
          appMap.put("multiinstance_handletype", multiinstance_handletype);
          appMap.put("multiinstance_outtertrantablename", multiinstance_outtertrantablename);
          appMap.put("etreid", etreid);
          appMap.put("remark", remark);
          arrayList.add(appMap);
        }
      }
    }
    return arrayList;
  }

  /**
   * 
   * @param startMap
   * @param jsonpropertiesNode
   */
  private void getStartMap(Map<String, String> startMap, JsonNode jsonpropertiesNode) {// 带扩展，还有别的信息
    String funNodeNumber = jsonpropertiesNode.get("overrideid").asText();
    if ("".equals(funNodeNumber) || null == funNodeNumber) {
      funNodeNumber = "";
    }
    startMap.put("nodenumber", funNodeNumber);

    String nodename = jsonpropertiesNode.get("name").asText();
    if ("".equals(nodename) || null == nodename) {
      nodename = "";
    }
    startMap.put("nodename", nodename);

    String nodetype = jsonpropertiesNode.get("startnodetype").asText();
    if ("".equals(nodetype) || null == nodetype) {
      nodetype = "";
    } else {
      String[] split = nodetype.split(" ");
      nodetype = split[0];
    }
    startMap.put("startnodetype", nodetype);

    String multiinstance_person = jsonpropertiesNode.get("multiinstance_person")// 节点流转类型
      .asText();
    if ("".equals(multiinstance_person) || null == multiinstance_person) {
      multiinstance_person = "";
    }
    startMap.put("multiinstance_person", multiinstance_person);

    String multiinstance_handletype = jsonpropertiesNode.get("multiinstance_handletype")// 内部事务提醒主表名称
      .asText();
    // String itmti = "";
    // if ("luru".equals(multiinstance_handletype)
    // || null == multiinstance_handletype) {
    // multiinstance_handletype = "";
    // } else {
    // String[] split2 = multiinstance_handletype.split(" ");
    // multiinstance_handletype = split2[0];
    // if (split2.length == 2) {
    // itmti = split2[1];
    // }
    // }

    if ("luru".equals(multiinstance_handletype)) {
      multiinstance_handletype = "";
    }

    startMap.put("multiinstance_handletype", multiinstance_handletype);

    // String itmti = jsonpropertiesNode.get("itmti")// 内部事务提醒主表ID
    // .asText();
    // if ("".equals(itmti) || null == itmti) {
    // itmti = "";
    // }
    // startMap.put("itmti", itmti);

    String multiinstance_outtertrantablename = jsonpropertiesNode.get("multiinstance_outtertrantablename")// 外部事务提醒主表名称
      .asText();
    if ("luru".equals(multiinstance_outtertrantablename)) {
      multiinstance_outtertrantablename = "";
    }

    // String otmti = "";
    // if ("luru".equals(multiinstance_outtertrantablename)
    // || null == multiinstance_outtertrantablename) {
    // multiinstance_outtertrantablename = "";
    // } else {
    // String[] split2 = multiinstance_outtertrantablename.split(" ");
    // multiinstance_outtertrantablename = split2[0];
    // if (split2.length == 2) {
    // otmti = split2[1];
    // }
    // }
    startMap.put("multiinstance_outtertrantablename", multiinstance_outtertrantablename);

    // String otmti = jsonpropertiesNode.get("otmti")// 外部事务提醒主表ID
    // .asText();
    // if ("".equals(otmti) || null == otmti) {
    // otmti = "";
    // }
    // startMap.put("otmti", otmti);

    String etreid = jsonpropertiesNode.get("etreid")// 外部事务提醒关联ID
      .asText();
    if ("".equals(etreid) || null == etreid) {
      etreid = "";
    }
    startMap.put("etreid", etreid);

    remark = jsonpropertiesNode.get("remark")// 备注
      .asText();
    if ("".equals(remark) || null == remark) {
      remark = "";
    }
    startMap.put("remark", remark);

    String otmrti = jsonpropertiesNode.get("otmrti")// 操作记账授权
      .asText();
    if ("".equals(otmrti) || null == otmrti) {
      otmrti = "";
    }
    startMap.put("otmrti", otmrti);
  }

  private void getEndMap(Map<String, String> endMap, JsonNode jsonpropertiesNode) {
    String funNodeNumber = jsonpropertiesNode.get("overrideid").asText();
    endMap.put("nodenumber", funNodeNumber);

    String nodename = jsonpropertiesNode.get("name").asText();
    endMap.put("nodename", nodename);

    String nodetype = jsonpropertiesNode.get("endnodetype").asText();
    String[] split = nodetype.split(" ");
    endMap.put("endnodetype", split[0]);

    String multiinstance_person = jsonpropertiesNode.get("multiinstance_person")// 节点流转类型
      .asText();
    endMap.put("multiinstance_person", multiinstance_person);

    String multiinstance_handletype = jsonpropertiesNode.get("multiinstance_handletype")// 内部事务提醒主表名称
      .asText();
    // String itmti = "";
    // if ("luru".equals(multiinstance_handletype)
    // || null == multiinstance_handletype) {
    // multiinstance_handletype = "";
    // } else {
    // String[] split2 = multiinstance_handletype.split(" ");
    // multiinstance_handletype = split2[0];
    // if (split2.length == 2) {
    // itmti = split2[1];
    // }
    // }

    // begin_04_27
    if ("luru".equals(multiinstance_handletype)) {
      multiinstance_handletype = "";
    }
    // end_04_27
    endMap.put("multiinstance_handletype", multiinstance_handletype);

    // String itmti = jsonpropertiesNode.get("itmti")// 内部事务提醒主表ID
    // .asText();
    // endMap.put("itmti", itmti);

    String multiinstance_outtertrantablename = jsonpropertiesNode.get("multiinstance_outtertrantablename")// 外部事务提醒主表名称
      .asText();
    // String otmti = "";
    // if ("luru".equals(multiinstance_outtertrantablename)
    // || null == multiinstance_outtertrantablename) {
    // multiinstance_outtertrantablename = "";
    // } else {
    // String[] split2 = multiinstance_outtertrantablename.split(" ");
    // multiinstance_outtertrantablename = split2[0];
    // if (split2.length == 2) {
    // otmti = split2[1];
    // }
    // }

    // begin_04_27
    if ("luru".equals(multiinstance_outtertrantablename)) {
      multiinstance_outtertrantablename = "";
    }
    // end_04_27

    endMap.put("multiinstance_outtertrantablename", multiinstance_outtertrantablename);

    // String otmti = jsonpropertiesNode.get("otmti")// 外部事务提醒主表ID
    // .asText();
    // endMap.put("otmti", otmti);

    String etreid = jsonpropertiesNode.get("etreid")// 外部事务提醒关联ID
      .asText();
    endMap.put("etreid", etreid);

    remark = jsonpropertiesNode.get("remark")// 备注
      .asText();
    endMap.put("remark", remark);
  }

  /**
   * 获取getMenuCodeAndNodeId
   * 
   * @param m1
   * @param m2
   * @return
   */
  public List<Map<String, String>> getMenuCodeAndNodeId(Map<String, String> m1, List<Map<String, String>> map) {
    List list = new ArrayList();
    for (String key : m1.keySet()) {
      for (Map<String, String> m2 : map) {
        for (String mkey : m2.keySet()) {
          if (key.equals(mkey)) {
            String nodeId = m1.get(key);
            String moduleCode = m2.get(mkey);
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put(nodeId, moduleCode);
            list.add(hashMap);
          }
        }
      }
    }
    return list;
  }

  /**
   * 判断节点编号是否重复
   * 
   * @param idAndNodMap
   * @return
   */
  public HashMap<String, String> isNodeCodeRepeat(Map<String, String> idAndNodMap) {
    // {StartNoneEvent1=1, EndNoneEvent1=2, ApproveUserTask1=3}
    HashMap<String, String> map = new HashMap<String, String>();
    List<String> list = new ArrayList<String>();
    List<String> list2 = new ArrayList<String>();
    for (String nodeCode : idAndNodMap.keySet()) {
      if (nodeCode.contains("ApproveUserTask")) {
        list.add(nodeCode.split("ApproveUserTask")[1]);
      }
    }
    // [1,2,3,4,4,4,5]
    for (String nc : list) {
      int i = 0;
      for (String nc2 : list) {
        if (nc.equals(nc2)) {
          i++;
        }
      }
      if (i > 1) {
        list2.add(new StringBuffer("ApproveUserTask").append(nc).toString());
      }
    }
    if (!list2.isEmpty()) {
      StringBuffer buffer = new StringBuffer("以下节点编号重复：");
      for (String str : list2) {
        buffer.append(str).append(" ");
      }
      map.put("isRepeat", buffer.toString());
    }
    return map;
  }

  public String[] getFieldsByTableName(final String tableName) {
    Object result = TableFieldsSQLMap.get(tableName.toUpperCase());
    if (result == null) {
      result = execute(new HibernateCallback() {

        public Object doInHibernate(Session session) throws HibernateException, SQLException {
          Connection conn = session.connection();
          String sql = "select * from " + tableName + " where 1=0 ";
          PreparedStatement ps = conn.prepareStatement(sql);
          ResultSet rs = ps.executeQuery();
          ResultSetMetaData metaData = rs.getMetaData();

          int n = metaData.getColumnCount();
          String[] fieldNames = new String[n];
          // modified by zhoulingling
          // 2011-03-08数组下标从0开始，metaData.getColumnName()下标从1开始
          for (int i = 0; i < n; i++) {
            fieldNames[i] = metaData.getColumnName(i + 1).toLowerCase();
          }
          rs.close();
          ps.close();
          return fieldNames;
        }
      });
      TableFieldsSQLMap.put(tableName.toUpperCase(), result);
    }
    return (String[]) result;
  }

}
