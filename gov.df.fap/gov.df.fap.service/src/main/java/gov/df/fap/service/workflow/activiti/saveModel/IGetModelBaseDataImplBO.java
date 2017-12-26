package gov.df.fap.service.workflow.activiti.saveModel;

import gov.df.fap.api.workflow.activiti.saveModel.IGetModelBaseData;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class IGetModelBaseDataImplBO implements IGetModelBaseData {

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
   *  "resourceId": "180001",
    "properties": {
      "process_id": "111933",
      "processname": "你猜9",
      "multiinstance_maintablename": "BUDGET_USEABLE_VOUCHER",
      "idfield": "scccc",
      "expreson": ""
     },
   */

  @Override
  public ObjectNode getModelBaseData(ObjectNode editorJsonNode, Model model, String expresonFull) {

    ObjectMapper resultModel = new ObjectMapper();
    //ObjectNode resultNode = resultModel.createObjectNode();
    String wfName = "";
    String wfTableName = "";
    String idColumnName = "";
    String bsh_expression = "";

    /* String sql_sys_work_flows="select wf_name,wf_table_name,id_column_name,condition_id from sys_wf_flows where wf_code=?";
     String sql_sys_wf_conditions="select bsh_expression from sys_wf_flows where wf_code=?";*/

    String sql_wf_infp = "select distinct wf_name,wf_table_name,id_column_name,condition_id,"
      + (TypeOfDB.isOracle() ? "nvl(" : "ifnull(")
      + "(select k.bsh_expression from sys_wf_conditions k where s.condition_id=k.condition_id and s.condition_id <> '#'),'') as bsh_expression "
      + "from sys_wf_flows s where s.wf_code=?";

    //begin_通过model获得 code（process_id）、流程名称、主表名称、主键
    String wf_code = model.getKey();//流程图code
    List<Map> findBySql1 = dao.findBySql(sql_wf_infp, new Object[] { wf_code });

    for (Map map : findBySql1) {
      wfName = (String) map.get("wf_name");
      wfTableName = (String) map.get("wf_table_name");
      idColumnName = (String) map.get("id_column_name");
      bsh_expression = (String) map.get("bsh_expression");
      bsh_expression = bsh_expression == null ? "" : bsh_expression;
    }

    try {
      HashMap hashMap = new HashMap();
      hashMap.put("process_id", wf_code);
      hashMap.put("processname", wfName);
      hashMap.put("multiinstance_maintablename", wfTableName + " " + idColumnName);
      hashMap.put("idfield", idColumnName);
      hashMap.put("expreson", expresonFull);//入口条件表达式全信息 2017_5_22

      String userMapJson = resultModel.writeValueAsString(hashMap);
      editorJsonNode.put("properties", resultModel.readTree(userMapJson));

      return editorJsonNode;

    } catch (JsonProcessingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return null;
  }

}
