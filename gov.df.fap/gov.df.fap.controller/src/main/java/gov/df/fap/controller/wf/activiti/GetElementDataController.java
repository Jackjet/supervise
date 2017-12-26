package gov.df.fap.controller.wf.activiti;

import gov.df.fap.api.workflow.activiti.design.IGetElementVal;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@Controller
public class GetElementDataController {
  
  @Autowired
  private IGetElementVal IGetElementVal;
  
  
  
  @RequestMapping(value = "/df/service/get/eleValue", method = RequestMethod.GET)
  public @ResponseBody List<Map> getDatas_Get(HttpServletRequest request,
      HttpServletResponse response,@RequestParam String eleType,@RequestParam String tableName) throws SQLException,
      JsonProcessingException, IOException {
        
        List constituent = new ArrayList();
        List nonConstituent = new ArrayList();
        
      if("1".equals(eleType)){
          //通过eleType查询要素信息
            List<Map> field = IGetElementVal.getEleVal(eleType,tableName);
           //重组要素信息
            for (int i = 0; i < field.size(); i++) {
              String temp = ((Map) field.get(i)).get("ele_code").toString();
              String ele_source = ((Map) field.get(i)).get("ele_source").toString();
              Map data=new HashMap();
              Map data1=new HashMap();
              data.put("para_name", temp + "_ID");
              data.put("para_chs", ((Map) field.get(i)).get("ele_name"));
              data.put("ele_source", ele_source);
              constituent.add(data);
              data1.put("para_name", temp + "_CODE");
              data1.put("para_chs", ((Map) field.get(i)).get("ele_name"));
              data1.put("ele_source", ele_source);
              constituent.add(data1);
            }
    
            Map data1=new HashMap();
            data1.put("para_name", "USER_ID");
            data1.put("para_chs", "用户ID");
            constituent.add(data1);
    
            data1 = new HashMap();
            data1.put("para_name", "MODULE_ID");
            data1.put("para_chs", "功能ID");
            constituent.add(data1);
    
            data1 = new HashMap();
            data1.put("para_name", "ROLE_ID");
            data1.put("para_chs", "角色ID");
            constituent.add(data1);
            return constituent;
      }else{
        
        List<Map> field = IGetElementVal.getEleVal(eleType,tableName);
        List<Map> field1 = IGetElementVal.getInEleVal(tableName);
        Map type = new HashMap();
        for (int i = 0; i < field1.size(); i++) {
          String temp = ((Map) field1.get(i)).get("para_name").toString();
          String data_type = ((Map) field1.get(i)).get("type").toString();
          List ele = IGetElementVal.findComments(tableName.toUpperCase(), temp.toUpperCase());
          boolean b = false;
          for (int j = 0; j < field.size(); j++) {
            if (temp.toUpperCase().equals(
              ((Map) field.get(j)).get("ele_code").toString().toUpperCase() + "_ID")
              || temp.toUpperCase().equals(
                ((Map) field.get(j)).get("ele_code").toString().toUpperCase() + "_CODE")) {
              b = true;
              break;
            }
          }
          if (ele.size() > 0 && !b) {
            Map data = ((Map) ele.get(0));
            data.put("para_name", temp);
            data.put("para_chs", data.get("COMMENTS"));
            nonConstituent.add(data);
          }
          type.put(temp, data_type);
        }
        return nonConstituent;
      }
       
     
  }
  
  
  @RequestMapping(value = "/df/service/get/eleParamValTree", method = RequestMethod.GET)
  public @ResponseBody String getDatas_paramValTree(HttpServletRequest request,
      HttpServletResponse response,@RequestParam String ele_source_val) throws SQLException,
      JsonProcessingException, IOException {
    String header = request.getHeader("Referer");
    String id = request.getParameter("id");
    String mId = id;
    
    String parentid = "pId";
    String guid = "guid";
    String name = "name";
    String isleaf = "isleaf";
    String isParent = "isParent";
    int count = 0;
    JSONArray jsonArray = new JSONArray();
    List<Map> list = new ArrayList<Map>();
    List<Map> li = new ArrayList<Map>();
     
    if (null == mId || "".equals(mId)) {
      String[] ele_source_valArr = ele_source_val.split("\\|");
     List<Map> eleSourceData = IGetElementVal.getEleSourceData(ele_source_valArr[0]);
      for (Map map : eleSourceData) {

       String code_ = (String) map.get("chr_code");
       String name_DB = (String) map.get("chr_name");
       String pid_DB = (String) map.get("parent_id");
       String id_DB = (String) map.get("chr_id");
       String isleaf_DB = String.valueOf(map.get("is_leaf"));
       Map<String, String> hashMap = new HashMap<String, String>();
       hashMap.put(parentid, pid_DB);
       hashMap.put(guid, id_DB);
       hashMap.put(name, code_ + " " + name_DB);
       hashMap.put(isleaf, isleaf_DB);
       hashMap.put(isParent, "false");
       list.add(hashMap);
    }
     for (Map<String, String> map : list) {
       String flag = (String) map.get(isleaf);
       if ("0".equals(flag)) {
         map.put(isParent, "true");
       }
         count++;
         map.put("pk", map.get(guid));
         map.put("id", map.get(guid));
         map.put("nocheck", "false");
         map.put("canselect", "true");
         map.put("user_guid", map.get(guid));
         jsonArray.add(map);
         map.put("totalCount", String.valueOf(count));
     }
   }
     return jsonArray.toString();
    
  }
  
  
  @RequestMapping(value = "/df/service/get/getNoElementData", method = RequestMethod.GET)
  public @ResponseBody List<Map> getNoElementData(HttpServletRequest request,
      HttpServletResponse response,@RequestParam String ele_type) throws SQLException,
      JsonProcessingException, IOException {
       
        List<Map> noElementData = IGetElementVal.getNoElementData(ele_type);
    
        return noElementData;
    
  }
  
  
  @RequestMapping(value = "/df/service/create/saveParaValue", method = RequestMethod.GET)
  public @ResponseBody Map saveParaValue(HttpServletRequest request,
      HttpServletResponse response,@RequestParam String paramName_val,
      @RequestParam String paramDesc_val,
      @RequestParam String paramCheck_val,
      @RequestParam String paramvaluetypeInit,
      @RequestParam String paraType_c,
      @RequestParam String noElePara_id_val) throws SQLException,
      JsonProcessingException, IOException {

        Map reg=new HashMap();
        paramName_val=new String(paramName_val.getBytes("iso8859-1"),"utf-8");
        paramDesc_val=new String(paramDesc_val.getBytes("iso8859-1"),"utf-8");
        paramCheck_val=new String(paramCheck_val.getBytes("iso8859-1"),"utf-8");
        paramvaluetypeInit=new String(paramvaluetypeInit.getBytes("iso8859-1"),"utf-8");
        
       try {
            if("".equals(noElePara_id_val) || null==noElePara_id_val){
              IGetElementVal.createNoEleParaVal(paramName_val, paramDesc_val, paramCheck_val, paramvaluetypeInit, paraType_c);
              paraType_c="1".equals(paraType_c)?"常量":"变量";
              reg.put("returnReg", paraType_c+"定义成功");
              return reg;
            }else{
              IGetElementVal.updateNoEleParaVal(paramName_val, paramDesc_val, paramCheck_val, paramvaluetypeInit, paraType_c, noElePara_id_val);
              paraType_c="1".equals(paraType_c)?"常量":"变量";
              reg.put("returnReg", paraType_c+"修改成功");
              return reg;
              
            }
            
            
      } catch (Exception e) {
        if("".equals(noElePara_id_val) || null==noElePara_id_val){
          reg.put("returnReg", paraType_c+"定义失败");
        }else{
          reg.put("returnReg", paraType_c+"修改失败");
        }
       
        e.printStackTrace();
        return reg;
      }
    
  }
  
  
  @RequestMapping(value = "/df/service/get/deleteRuleParam", method = RequestMethod.GET)
  public @ResponseBody Map deleteRuleParam(HttpServletRequest request,
      HttpServletResponse response,@RequestParam String ruleParamID,@RequestParam String paraType_c) throws SQLException,
      JsonProcessingException, IOException {
       Map reg=new HashMap();
      try {
        paraType_c = new String(paraType_c.getBytes("iso8859-1"),"utf-8");   
            IGetElementVal.deleteRuleParam(ruleParamID);
             reg.put("returnReg", paraType_c+"删除成功");
            return reg;
      } catch (Exception e) {
        reg.put("returnReg", paraType_c+"删除失败");
        e.printStackTrace();
        return reg;
      }
    
    
  }
  
  
  @RequestMapping(value = "/df/service/get/getExpBySetting", method = RequestMethod.POST)
  public @ResponseBody Map getExpBySetting(HttpServletRequest request,
      HttpServletResponse response,@RequestParam("tableExpVal") String tableExpVal) throws SQLException,
      JsonProcessingException, IOException {
        
       ObjectMapper mapper = new ObjectMapper(); 
       HashMap reg = new HashMap();
       
       
       // String json="[{\"left_pare\": \"(\", \"logic_operator\": \"\", \"left_paraid\": \"BGTSOURCE_ID\", \"left_paravaluetype\": \"2\", \"left_paraname\": \"999\", \"operator\": \"=\", \"right_paraid\": \"01687hWrrIAh5\", \"right_paravaluetype\": \"\", \"right_pare\": \")\", \"right_paraname\": \"9999\", \"line_id\": \"\"}]";       
     
       //tableExpVal=new String(tableExpVal.getBytes("iso8859-1"),"utf-8").toString();
       tableExpVal=tableExpVal.substring(tableExpVal.indexOf("["), tableExpVal.length()-(tableExpVal.indexOf("[")));
       JSONObject pp = new JSONObject();
       JSONArray op = pp.parseArray(tableExpVal);
       ArrayList<Map> arrayList = new ArrayList<Map>();
       for(int i=0;i<op.size();i++){
         JSONObject jsStr = JSONObject.parseObject(op.getString(i));
         String left_paraname_noFlag = (String) jsStr.get("left_paraname_noFlag");
         String left_paraname = (String) jsStr.get("left_paraname");
         String right_paraname_noFlag = (String) jsStr.get("right_paraname_noFlag");
         String right_paraname = (String) jsStr.get("right_paraname");
         String[] left_paraname_noFlagArr = left_paraname_noFlag.split(" ");
         if(left_paraname_noFlagArr.length==2){
           jsStr.put("left_paraname", "["+left_paraname_noFlagArr[0]+"]"+left_paraname+"["+left_paraname_noFlagArr[1]+"]");
         }else{
           jsStr.put("left_paraname", "");
         }
         //jsStr.put("left_paraname", "["+left_paraname_noFlagArr[0]+"]"+left_paraname+"["+left_paraname_noFlagArr[1]+"]");
         if(!"".equals(right_paraname_noFlag)){
           jsStr.put("right_paraname", "["+right_paraname_noFlag+"]"+right_paraname);
         }else{
           jsStr.put("right_paraname", "");
         }
         //jsStr.put("right_paraname", "["+right_paraname_noFlag+"]"+right_paraname);
         op.set(i, jsStr);
         
         HashMap hashMap = new HashMap();
         hashMap.put("right_paraid", (String) jsStr.get("right_paraid"));
         hashMap.put("right_paravaluetype", (String) jsStr.get("right_paravaluetype"));
         hashMap.put("right_pare", (String) jsStr.get("right_pare"));
         hashMap.put("right_paraname", (String) jsStr.get("right_paraname"));
         
         hashMap.put("line_id", (String) jsStr.get("line_id"));
         hashMap.put("logic_operator", (String) jsStr.get("logic_operator"));
         hashMap.put("operator", (String) jsStr.get("operator"));
         
         hashMap.put("left_pare", (String) jsStr.get("left_pare"));
         hashMap.put("left_paraid", (String) jsStr.get("left_paraid"));
         hashMap.put("left_paravaluetype", (String) jsStr.get("left_paravaluetype"));
         hashMap.put("left_paraname", (String) jsStr.get("left_paraname"));
         arrayList.add(hashMap);
       }
       
       try {
        String scriptExpressionBySetting = IGetElementVal.getExpressionBySetting(arrayList);
        String descExpressionBySetting = IGetElementVal.getDescExpressionBySetting(arrayList);
        reg.put("scriptExpressionBySetting", scriptExpressionBySetting);
        reg.put("descExpressionBySetting", descExpressionBySetting);
        return reg;
      } catch (Exception e) {
        
        String msg=e.getMessage();
        reg.put("errorMsg", msg);
        //e.printStackTrace();
        return reg;
      }
  }
  
  
  
  
  
  
  
  
  
  
}
