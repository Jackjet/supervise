package gov.df.fap.service.workflow.activiti.design.data;

import gov.df.fap.api.workflow.activiti.design.IGetElementVal;
import gov.df.fap.service.rule.RuleFactory;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class IGetElementValImplBO implements IGetElementVal {

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao;

  @Override
  public List<Map> getEleVal(String eleType, String tableName) {

    String re_code = (String) SessionUtil.getRgCode();
    String setYear = (String) SessionUtil.getLoginYear();
    String get_element_sql = "select * from sys_element where rg_code=?  and set_year=?";
    List<Map> findBySql1 = dao.findBySql(get_element_sql, new Object[] { re_code, setYear });
    return findBySql1;
  }

  /* private String getRgCode() {
     return SessionUtil.getRgCode();
   }

   public String getSetYear() {
     String set_year = (String) SessionUtil.getLoginYear();
     if (set_year == null || set_year.equalsIgnoreCase("")) {
       set_year = String.valueOf(DateHandler.getCurrentYear());
     }
     return set_year;
   }*/

  @Override
  public List<Map> getInEleVal(String tableName) {

    String SELECT4TABLEFIELD = "select column_name as para_name,data_type as type from "
      + (TypeOfDB.isOracle() ? "user_tab_columns" : "information_schema.`COLUMNS`") + " where table_name = ? ";
    List<Map> findBySql = dao.findBySql(SELECT4TABLEFIELD, new Object[] { tableName });
    return findBySql;
  }

  @Override
  public List<Map> findComments(String tableName, String fieldName) {
    List<Map> list = null;
    StringBuffer sql = new StringBuffer();
    if (TypeOfDB.isOracle()) {
      sql.append("SELECT * FROM USER_COL_COMMENTS WHERE TABLE_NAME=? and column_name=?");
    } else {
      sql
        .append("SELECT column_comment as COMMENTS FROM information_schema.`COLUMNS` WHERE TABLE_NAME=? and column_name=?");
    }
    list = dao.findBySql(sql.toString(), new Object[] { tableName, fieldName });
    return list;
  }

  @Override
  public List<Map> getEleSourceData(String eleSource) {
    String get_ele_source = "SELECT * FROM " + eleSource;
    List findBySql = dao.findBySql(get_ele_source.toString(), new Object[] {});
    return findBySql;
  }

  @Override
  public List<Map> getNoElementData(String paramType) {
    //para_type
    String sql_getNoElementData = "select * from  sys_wf_condition_paras where para_type=?";

    List findBySql = dao.findBySql(sql_getNoElementData.toString(), new Object[] { paramType });
    return findBySql;

  }

  @Override
  public void createNoEleParaVal(String paramName_val, String paramDesc_val, String paramCheck_val,
    String paramvaluetypeInit, String paraType_c) {

    String rgCode = SessionUtil.getRgCode();
    String defaultYear = SessionUtil.getLoginYear();

    String sql_insert_paraVal = "insert into sys_wf_condition_paras values("
      + (TypeOfDB.isOracle() ? "SEQ_SYS_WF_ID.Nextval" : "Nextval('SEQ_SYS_WF_ID')")
      + ",null,?,?,?,null,?,null,?,null,?,?)";
    dao.executeBySql(sql_insert_paraVal, new Object[] { paramDesc_val, Integer.valueOf(paraType_c), paramName_val,
      Integer.valueOf(paramvaluetypeInit), Integer.valueOf(paramCheck_val), defaultYear, rgCode });

  }

  @Override
  public void deleteRuleParam(String ruleParamID) {

    String del_sys_wf_condition_paras_byParaID = "delete from sys_wf_condition_paras where para_id=?";
    dao.executeBySql(del_sys_wf_condition_paras_byParaID, new Object[] { ruleParamID });
  }

  @Override
  public void updateNoEleParaVal(String paramName_val, String paramDesc_val, String paramCheck_val,
    String paramvaluetypeInit, String paraType_c, String noElePara_id_val) {
    String sql_update_paraVal = "update sys_wf_condition_paras set para_name=?,para_desc=?,para_valuetype=?,is_shared=? where para_id=?";
    dao.executeBySql(sql_update_paraVal, new Object[] { paramName_val, paramDesc_val, paramvaluetypeInit,
      paramCheck_val, noElePara_id_val });

  }

  @Override
  public String getExpressionBySetting(List<Map> setting) throws Exception {
    List list = setting;

    /*  */
    List<Map> listCopy = new ArrayList<Map>();

    /* 如果集合为空 直接返回false */
    /* if (list == null) {
       return false;
     }*/

    /* 循环集合. 判断规则合法性 */
    int j = list.size();
    for (int i = 0; i < j; i++) {

      /* 单条数据信息 */
      Map xmldata = (Map) list.get(i);

      /* 检验逻辑运算符 */
      if (("".equals(xmldata.get("logic_operator")) || null == xmldata.get("logic_operator")) && i < j - 1) {
        //table_list.setRowSelectionInterval(i, i);
        throw new Exception("逻辑运算符不能为空!");
      }

      Object leftobj = xmldata.get("left_paraid");
      /* 如果当前位置的MAP 中不包含参数ID 说明没有赋值 */
      if (null == leftobj || "".equals(leftobj)) {
        /* 定位错误行 */
        //table_list.setRowSelectionInterval(i, i);
        throw new Exception("规则" + (i + 1) + "行左变量没有赋值！");
      }

      /* 检查关系运算符 */
      if (null == xmldata.get("operator") || "".equals(xmldata.get("operator"))) {
        //table_list.setRowSelectionInterval(i, i);
        throw new Exception("关系运算符不能为空!");
      }

      /* 右变量检查， 赋值 */
      Object obj = xmldata.get("right_paraid");
      if (null == obj || "".equals(obj)) {
        /* 定位当前错误行 */
        //table_list.setRowSelectionInterval(i, i);
        throw new Exception("规则" + (i + 1) + "行右变量没有赋值！");
      }

      listCopy.add(xmldata);
    }

    /* beanShell调用验证 */
    RuleFactory rf = new RuleFactory();

    String beanShellStr = "";
    try {

      //将Map转XmlData

      ArrayList<XMLData> arrayList = new ArrayList<XMLData>();
      for (Map object : listCopy) {
        XMLData xmlData = new XMLData();
        xmlData.put("right_paraid", (String) object.get("right_paraid"));
        xmlData.put("right_paravaluetype", (String) object.get("right_paravaluetype"));
        xmlData.put("right_pare", (String) object.get("right_pare"));
        xmlData.put("right_paraname", (String) object.get("right_paraname"));

        xmlData.put("line_id", (String) object.get("line_id"));
        xmlData.put("logic_operator", (String) object.get("logic_operator"));
        xmlData.put("operator", (String) object.get("operator"));

        xmlData.put("left_pare", (String) object.get("left_pare"));
        xmlData.put("left_paraid", (String) object.get("left_paraid"));
        xmlData.put("left_paravaluetype", (String) object.get("left_paravaluetype"));
        xmlData.put("left_paraname", (String) object.get("left_paraname"));
        arrayList.add(xmlData);
      }
      //Collections.reverse(arrayList);
      beanShellStr = rf.getBshFlag(arrayList, new HashMap());
    } catch (Exception e) {
      // LogClient.error(e.getMessage());
      throw new Exception("规则表达式逻辑配置不正确!");
    }
    return beanShellStr;
  }

  @Override
  public String getDescExpressionBySetting(List<Map> setting) throws Exception {

    List<Map> listCopy = new ArrayList<Map>();
    List<XMLData> arrayList = new ArrayList<XMLData>();
    //Collections.copy(listCopy, setting);
    for (Map object : setting) {
      XMLData xmlData = new XMLData();
      xmlData.put("right_paraid", (String) object.get("right_paraid"));
      xmlData.put("right_paravaluetype", (String) object.get("right_paravaluetype"));
      xmlData.put("right_pare", (String) object.get("right_pare"));
      xmlData.put("right_paraname", (String) object.get("right_paraname"));

      xmlData.put("line_id", (String) object.get("line_id"));
      xmlData.put("logic_operator", (String) object.get("logic_operator"));
      xmlData.put("operator", (String) object.get("operator"));

      xmlData.put("left_pare", (String) object.get("left_pare"));
      xmlData.put("left_paraid", (String) object.get("left_paraid"));
      xmlData.put("left_paravaluetype", (String) object.get("left_paravaluetype"));
      xmlData.put("left_paraname", (String) object.get("left_paraname"));
      arrayList.add(xmlData);
    }

    /* 连接规则描述 */
    StringBuffer string4discrption = new StringBuffer();

    /* 循环组合规则描述 */
    Iterator it = arrayList.iterator();
    while (it.hasNext()) {
      XMLData xml = (XMLData) it.next();
      /* 左括号 */
      Object leftPare = xml.get("left_pare");
      if (null != leftPare)
        string4discrption.append(leftPare);
      string4discrption.append("  ");

      /* 左变量 */
      if (xml.get("left_paraname").toString().indexOf("要素") == -1) {
        string4discrption.append(xml.get("left_paraname").toString().substring(4));
        string4discrption.append("  ");
      } else {
        string4discrption.append(xml.get("left_paraid").toString());
        string4discrption.append("  ");
      }

      /* 关系运算符 */
      if ("like".equals(xml.get("operator").toString())) {
        string4discrption.append("like");
        string4discrption.append("  ");
      } else if ("LLike".equals(xml.get("operator").toString())) {
        string4discrption.append("like");
        string4discrption.append("  ");
      } else if ("RLike".equals(xml.get("operator").toString())) {
        string4discrption.append("like");
        string4discrption.append("  ");
      } else if ("NLike".equals(xml.get("operator").toString())) {
        string4discrption.append("not like");
        string4discrption.append("  ");
      } else {
        string4discrption.append(xml.get("operator"));
        string4discrption.append("  ");
      }

      /* 右变量 */
      if ("like".equals(xml.get("operator").toString())) {
        if (xml.get("right_paraname").toString().indexOf("要素") == 1
          && xml.get("right_paraname").toString().indexOf("非") == -1) {
          string4discrption.append("'%" + xml.get("right_paraid").toString() + "%'");
          string4discrption.append("  ");
        } else {
          string4discrption.append("'%" + xml.get("right_paraname").toString().substring(4) + "%'");
          string4discrption.append("  ");
        }
      } else if ("LLike".equals(xml.get("operator").toString())) {
        if (xml.get("right_paraname").toString().indexOf("要素") == 1
          && xml.get("right_paraname").toString().indexOf("非") == -1) {
          string4discrption.append("'%" + xml.get("right_paraid").toString() + "'");
          string4discrption.append("  ");
        } else {
          string4discrption.append("'%" + xml.get("right_paraname").toString().substring(4) + "'");
          string4discrption.append("  ");
        }
      } else if ("RLike".equals(xml.get("operator").toString())) {
        if (xml.get("right_paraname").toString().indexOf("要素") == 1
          && xml.get("right_paraname").toString().indexOf("非") == -1) {
          string4discrption.append("'" + xml.get("right_paraid").toString() + "%'");
          string4discrption.append("  ");
        } else {
          string4discrption.append("'" + xml.get("right_paraname").toString().substring(4) + "%'");
          string4discrption.append("  ");
        }
      } else if ("NLike".equals(xml.get("operator").toString())) {
        if (xml.get("right_paraname").toString().indexOf("要素") == 1
          && xml.get("right_paraname").toString().indexOf("非") == -1) {
          string4discrption.append("'%" + xml.get("right_paraid").toString() + "%'");
          string4discrption.append("  ");
        } else {
          string4discrption.append("'%" + xml.get("right_paraname").toString().substring(4) + "%'");
          string4discrption.append("  ");
        }
      } else {
        if (xml.get("right_paraname").toString().indexOf("要素") == 1
          && xml.get("right_paraname").toString().indexOf("非") == -1) {
          string4discrption.append("'" + xml.get("right_paraid").toString() + "'");
          string4discrption.append("  ");
        } else {
          string4discrption.append("'" + xml.get("right_paraname").toString().substring(4) + "'");
          string4discrption.append("  ");
        }
      }

      /* 右括号 */
      Object rightPare = xml.get("right_pare");
      if (null != rightPare)
        string4discrption.append(rightPare);
      string4discrption.append("  ");

      /* 逻辑运算符 */
      Object logicOperator = xml.get("logic_operator");
      if (null != logicOperator)
        string4discrption.append(logicOperator);
      string4discrption.append("  ");
    }

    return string4discrption.toString();
  }

}
