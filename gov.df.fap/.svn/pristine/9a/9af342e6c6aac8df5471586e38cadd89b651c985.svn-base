package gov.df.fap.service.workflow.activiti.design.data;

import gov.df.fap.api.workflow.activiti.ModelDataJsonConstants;
import gov.df.fap.api.workflow.activiti.design.TreeMenu;
import gov.df.fap.api.workflow.activiti.saveModel.ICreateModel2;
import gov.df.fap.api.workflow.activiti.saveModel.IDeleteModel;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class TreeMenuImplBO implements TreeMenu {

  protected ClassLoader classloader;

  protected static final Logger LOGGER = LoggerFactory.getLogger(TreeMenuImplBO.class);

  @Autowired
  private IDeleteModel deleteModel;

  @Autowired
  private ICreateModel2 createModel;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao;

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  /**
   * 导入EditorSourc
   */
  @Override
  public Map<String, String> leadin(byte[] modelEditorSource, RepositoryService repositoryService, String leadIn) {
    Model model = null;
    Map<String, String> map = new HashMap<String, String>();
    FileInputStream in = null;
    try {
      JsonNode editorNode = new ObjectMapper().readTree(modelEditorSource);
      JsonNode jsonNode = editorNode.get("properties");
      JsonNode proCodeValue = jsonNode.get("process_id");
      String wf_code = proCodeValue.asText();
      JsonNode processnameJson = jsonNode.get("processname");
      if (leadIn == null || leadIn.equals("")) {
        List<Map> wfIdAndConditionId = createModel.getWfIdAndConditionId(wf_code);
        if (!wfIdAndConditionId.isEmpty()) {
          map.put("leadin", "true");
          return map;
        }
      } else if (leadIn.equals("true") || map.isEmpty()) {
        delProcess(wf_code, repositoryService);
      }
      createModel.createModelView(true, "", editorNode, repositoryService, objectMapper);
      model = repositoryService.newModel();
      ObjectMapper objectMapper = new ObjectMapper();
      ObjectNode modelObjectNode = objectMapper.createObjectNode();
      modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processnameJson.asText());
      modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
      modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, "");
      model.setMetaInfo(modelObjectNode.toString());
      model.setName(processnameJson.asText());
      model.setKey(wf_code);
      repositoryService.saveModel(model);
      repositoryService.addModelEditorSource(model.getId(), modelEditorSource);
      map.put("leadin", "false");
      return map;
    } catch (Exception e) {
      LOGGER.error("导入失败 ： " + e.getMessage(), e);
      throw new RuntimeException("导入失败" + e.getMessage());
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 导出EditorSourc
   */
  @Override
  public byte[] leadout(String code, RepositoryService repositoryService) {
    Model model = null;
    String getModelId = "select id_ from act_re_model where key_=?";
    byte[] modelEditorSource = null;
    try {
      List<Map> modelList = dao.findBySql(getModelId, new Object[] { code });
      if (!modelList.isEmpty()) {
        String modelId = (String) modelList.get(0).get("id_");
        modelEditorSource = repositoryService.getModelEditorSource(modelId);
      }
    } catch (Exception e) {
      LOGGER.error("导出失败 ： " + e.getMessage(), e);
      throw new RuntimeException("导出失败" + e.getMessage());
    }
    return modelEditorSource;
  }

  @Override
  public void addProcess(String code, String name, String ptname, String field, RepositoryService repositoryService) {
    String sql_wf_flows = "INSERT INTO sys_wf_flows (wf_id,wf_code,wf_name,wf_table_name,id_column_name,set_year,rg_code) VALUES ("
      + (TypeOfDB.isOracle() ? "SEQ_SYS_WF_ID.Nextval" : "Nextval('SEQ_SYS_WF_ID')") + ",?,?,?,?,?,?)";
    String set_yeartmp = SessionUtil.getLoginYear();
    if (set_yeartmp == null || set_yeartmp.equals("")) {
      set_yeartmp = SessionUtil.getDefaultYear();
    }
    int set_year = Integer.parseInt(set_yeartmp);
    String rg_code = SessionUtil.getRgCode();
    Map map = new HashMap();
    try {
      List paramsWfFlows = new ArrayList();
      paramsWfFlows.add(code);
      paramsWfFlows.add(name);
      paramsWfFlows.add(ptname);
      paramsWfFlows.add(field);
      paramsWfFlows.add(set_year);
      paramsWfFlows.add(rg_code);
      dao.executeBySql(sql_wf_flows, paramsWfFlows.toArray());
      String str = name;
      ObjectMapper objectMapper = new ObjectMapper();
      ObjectNode editorNode = objectMapper.createObjectNode();
      editorNode.put("id", "canvas");
      editorNode.put("resourceId", "canvas");
      ObjectNode stencilSetNode = objectMapper.createObjectNode();
      stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
      editorNode.put("stencilset", stencilSetNode);
      Model modelData = repositoryService.newModel();
      ObjectNode modelObjectNode = objectMapper.createObjectNode();
      modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, str);
      modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
      modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, "");
      modelData.setMetaInfo(modelObjectNode.toString());
      modelData.setName(str);
      modelData.setKey(code);
      repositoryService.saveModel(modelData);
      repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
    } catch (Exception e) {
      LOGGER.error("新增数据入库失败 ： " + e.getMessage(), e);
      throw new RuntimeException("新增数据入库失败" + e.getMessage());
    }
  }

  @Override
  public void delProcess(String code, RepositoryService repositoryService) {
    Model model = null;
    String sql_act_re_models = "select ID_,EDITOR_SOURCE_VALUE_ID_,EDITOR_SOURCE_EXTRA_VALUE_ID_ from act_re_model where KEY_=?";
    String sql_act_ge_bytearray = "delete from act_ge_bytearray where id_=?";
    Map map = new HashMap();
    deleteModel.deleteModel(code);
    List<Map> modelList = dao.findBySql(sql_act_re_models, new Object[] { code });
    if (modelList.size() != 0) {
      String modelId = (String) modelList.get(0).get("id_");
      String ESVID_ = (String) modelList.get(0).get("editor_source_value_id_");
      String ESEXTRAVID_ = (String) modelList.get(0).get("editor_source_extra_value_id_");
      repositoryService.deleteModel(modelId);
      if (null != ESVID_ || "".equals(ESVID_)) {
        dao.executeBySql(sql_act_ge_bytearray, new Object[] { ESVID_ });
      }
      if (null != ESEXTRAVID_ || "".equals(ESEXTRAVID_)) {
        dao.executeBySql(sql_act_ge_bytearray, new Object[] { ESEXTRAVID_ });
      }
    }
  }

  @Override
  public List queryPrimaryName() {
    String sys_tablemanager = "select table_code,table_name,id_column_name from sys_tablemanager order by table_code";
    List list = new ArrayList();
    try {
      List<Map> queryList = dao.findBySql(sys_tablemanager);
      for (Map map : queryList) {
        String tableCode = (String) map.get("table_code");
        String tableName = (String) map.get("table_name");
        String idColumnName = (String) map.get("id_column_name");
        Map m = new HashMap();
        m.put("tableCode", tableCode);
        m.put("tableName", tableName);
        m.put("idColumnName", idColumnName);
        list.add(m);
      }
    } catch (Exception e) {
      LOGGER.error("主表名称查询失败 ： " + e.getMessage(), e);
    }
    return list;
  }

  @Override
  public Map getZtree() {
    String sql_flows_root = "select wf_code,wf_name from sys_wf_flows ORDER BY wf_code";
    String wfCode = "wf_code";
    String wfName = "wf_name";
    String str = "";
    List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
    Map wfMap = new HashMap();
    try {
      List<Map> queryList = dao.findBySql(sql_flows_root);
      for (Map map : queryList) {
        String code = (String) map.get("wf_code");
        String name = (String) map.get("wf_name");
        Map m = new HashMap();
        m.put(wfCode, code);
        m.put(wfName, name);
        m.put("zpid", "0");
        m.put("name", code + " " + name);
        list.add(m);
      }
    } catch (Exception e) {
      LOGGER.error("流程树加载失败 ： " + e.getMessage(), e);
    }
    for (Map<Object, Object> mc : list) {
      String code = (String) mc.get(wfCode);
      for (Map<Object, Object> mc2 : list) {
        String sub2 = (String) mc2.get(wfCode);
        sub2 = sub2.substring(0, sub2.length() - 3);
        if (code.equals(sub2)) {
          mc2.put("zpid", code);
        }
      }
    }
    wfMap.put("wf", list);
    return wfMap;
  }

  public static byte[] readBytes(InputStream in) throws IOException {
    BufferedInputStream bufin = new BufferedInputStream(in);
    int buffSize = 1024;
    ByteArrayOutputStream out = new ByteArrayOutputStream(buffSize);
    byte[] temp = new byte[buffSize];
    int size = 0;
    while ((size = bufin.read(temp)) != -1) {
      out.write(temp, 0, size);
    }
    bufin.close();
    in.close();
    byte[] content = out.toByteArray();
    out.close();
    return content;
  }

  public static StringBuffer getFullContent3(InputStream in, String charset) throws IOException {
    StringBuffer sbuffer = new StringBuffer();
    InputStreamReader inReader;
    inReader = new InputStreamReader(in, charset);
    char[] ch = new char[1024];
    int readCount = 0;
    while ((readCount = inReader.read(ch)) != -1) {
      sbuffer.append(ch, 0, readCount);
    }
    inReader.close();
    in.close();
    return sbuffer;
  }
}
