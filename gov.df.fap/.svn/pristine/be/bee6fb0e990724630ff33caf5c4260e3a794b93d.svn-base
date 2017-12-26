package gov.df.fap.controller.relation;

import gov.df.fap.api.relation.IRelationService;
import gov.df.fap.bean.relation.detail.RelationDetail;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * 关联关系
 */
@Controller
@RequestMapping(value = "/df/fap/system/config/relation")
public class RelationController {
  @Autowired
  IRelationService relationService;

  /**
   * 初始化获取关联关系列表
   */
  @RequestMapping(value = "/list.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> listRelationManager() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      XMLData data = relationService.getRMList();
      String total_count = data.get("total_count").toString(); //查询到的总数据数
      List<Object> list = new ArrayList<Object>();
      if ("1".equals(data.get("total_count"))) {
        XMLData row = (XMLData) data.get("row");
        data.clear();
        data.put("total_count", total_count);
        list.add(row);
        data.put("row", list);
        result.put("data", data);
      } else if ("0".equals(data.get("total_count"))) {
        data.clear();
        data.put("total_count", total_count);
        data.put("row", list);
        result.put("data", data);
      } else {
        result.put("data", data);
      }
      result.put("errorCode", "0");
    } catch (Exception e) {
      // TODO: handle exception
      result.put("errorCode", "-1");
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 通过关联关系ID获取详细配置信息
   */
  @RequestMapping(value = "/get.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> getRMDetailByID(String relation_id) {

    Map<String, Object> result = new HashMap<String, Object>();
    try {
      XMLData data = new XMLData();
      data = relationService.getRMDetailByID(relation_id);
      String total_count = data.get("total_count").toString();//查询到的总数据数
      List<Object> list = new ArrayList<Object>();
      if ("1".equals(data.get("total_count"))) {
        XMLData row = (XMLData) data.get("row");
        data.clear();
        data.put("total_count", total_count);
        list.add(row);
        data.put("row", list);
        result.put("data", data);
      } else if ("0".equals(data.get("total_count"))) {
        data.clear();
        data.put("total_count", total_count);
        data.put("row", list);
        result.put("data", data);
      } else {
        result.put("data", data);
      }
      result.put("errorCode", "0");
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
      result.put("errorCode", "-1");
    }

    return result;
  }

  /**
   * 获取所有有效的要素
   */
  @RequestMapping(value = "/getAllElement.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> getAllElement() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {

      XMLData data = new XMLData();
      data = relationService.getAllElement();
      String total_count = data.get("total_count").toString();//查询到的总数据数
      List<Object> list = new ArrayList<Object>();
      if ("1".equals(data.get("total_count"))) {
        XMLData row = (XMLData) data.get("row");
        data.clear();
        data.put("total_count", total_count);
        list.add(row);
        data.put("row", list);
        result.put("data", data);
      } else if ("0".equals(data.get("total_count"))) {
        data.clear();
        data.put("total_count", total_count);
        data.put("row", list);
        result.put("data", data);
      } else {
        result.put("data", data);
      }

      result.put("errorCode", "0");

    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
      result.put("errorCode", "-1");
    }

    return result;
  }

  /**
   * @param element
   *            通过要素名称获取要素值
   */
  @RequestMapping(value = "/getEle.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> getEle(String ele_code) {
    Map<String, Object> result = new HashMap<String, Object>();

    try {
      XMLData data = relationService.getEle(ele_code);
      result.put("data", data);
      result.put("errorCode", "0");
    } catch (Exception e) {
      // TODO: handle exception
      result.put("errorCode", "-1");
      e.printStackTrace();
    }
    return result;
  }

  @RequestMapping(value = "/save.do", method = RequestMethod.POST)
  @ExceptionHandler({ HttpMessageNotReadableException.class })
  @ResponseBody
  public Map<String, Object> saveRelation(String relation_code, String pri_ele_code, String sec_ele_code,
    String relation_type, String relationDetail) {
    XMLData relationData = new XMLData();
    XMLData detail = new XMLData();
    List<RelationDetail> listRelationDetail = JSONObject.parseArray(relationDetail, RelationDetail.class);
    Map<String, Object> result = new HashMap<String, Object>();
    String pri_ele_value;
    List<String> list = new ArrayList<String>();
    relationData.put("relation_code", relation_code);
    relationData.put("pri_ele_code", pri_ele_code);
    relationData.put("sec_ele_code", sec_ele_code);
    relationData.put("relation_type", relation_type);
    // tempData.put("sec_ele_value", "102");
    // list.add(tempData);
    relationData.remove("row");
    // detail.put("12", list);
    for (int i = 0; i < listRelationDetail.size(); i++) {
      List<XMLData> templist = new ArrayList<XMLData>();
      pri_ele_value = listRelationDetail.get(i).getPri_ele_value();
      list = listRelationDetail.get(i).getSec_ele_value();
      for (int j = 0; j < list.size(); j++) {
        XMLData temp = new XMLData();
        temp.put("sec_ele_value", (String) list.get(j));
        templist.add(temp);
      }
      detail.put(pri_ele_value, templist);
    }
    relationData.put("row", detail);
    try {
      relationService.saveRelation(relationData);
      result.put("errorCode", "0");
    } catch (Exception e) {
    	result.put("errorCode", "-1");
    	result.put("errorMsg", e.getMessage());
    }
    return result;

  }

  @RequestMapping(value = "/update.do", method = RequestMethod.POST)
  @ExceptionHandler({ HttpMessageNotReadableException.class })
  @ResponseBody
  public Map<String, Object> updateRelation(String relation_code, String relation_id, String pri_ele_code,
    String sec_ele_code, String relation_type, String relationDetail) {
    XMLData relationData = new XMLData();
    XMLData detail = new XMLData();

    Map<String, Object> result = new HashMap<String, Object>();
    String pri_ele_value;
    List<RelationDetail> listRelationDetail = JSONObject.parseArray(relationDetail, RelationDetail.class);
    List<String> list = new ArrayList<String>();
    relationData.put("relation_id", relation_id);
    relationData.put("relation_code", relation_code);
    relationData.put("pri_ele_code", pri_ele_code);
    relationData.put("sec_ele_code", sec_ele_code);
    relationData.put("relation_type", relation_type);
    relationData.remove("row");
    for (int i = 0; i < listRelationDetail.size(); i++) {
      List<XMLData> templist = new ArrayList<XMLData>();
      pri_ele_value = listRelationDetail.get(i).getPri_ele_value();
      list = listRelationDetail.get(i).getSec_ele_value();
      for (int j = 0; j < list.size(); j++) {
        XMLData temp = new XMLData();
        temp.put("sec_ele_value", (String) list.get(j));
        templist.add(temp);
      }

      detail.put(pri_ele_value, templist);
    }
    relationData.put("row", detail);
    try {
      relationService.updateRelation(relationData);
      result.put("errorCode", "0");
    } catch (Exception e) {
    	result.put("errorCode", "-1");
    	result.put("errorMsg", e.getMessage());
    }
    return result;
  }

  @RequestMapping(value = "/del.do", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> deleteRelation(String relation_id) {
    Map<String, Object> result = new HashMap<String, Object>();

    try {
      if (relationService.deleteRelation(relation_id)) {
        result.put("errorCode", 0);
      } else {
        result.put("errorCode", -1);
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      result.put("errorCode", -1);
    }
    return result;
  }

}
