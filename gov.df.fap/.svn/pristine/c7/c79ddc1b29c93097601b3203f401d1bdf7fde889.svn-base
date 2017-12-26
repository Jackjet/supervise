package gov.df.fap.controller.dictionary;

import gov.df.fap.api.dictionary.interfaces.IDictionary;
import gov.df.fap.bean.control.FAssistObjectDTO;
import gov.df.fap.bean.util.FPaginationDTO;

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
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 获取要素配置信息controller
 */
@Controller
@RequestMapping(value = "/df")
public class ElementController {

  @Autowired
  private IDictionary dictionary;

  /**
   * 获取要素配置信息列表
   * @param setYear 年度
   * @return
   */

  @RequestMapping(value = "/dictionary/elementset/list.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> list(String setYear) {
    Map<String, Object> result = new HashMap<String, Object>();
    List<?> data = new ArrayList<Object>();
    try {// 不传入条件查询所有要素配置列表
      data = dictionary.getElementSet(" and enabled=1");
      result.put("data", data);
      result.put("errorCode", 0);
    } catch (Exception e) {
      result.put("errorCode", -1);
      e.printStackTrace();
    }
    return result;
  }

  /**
    * 根据要素代码获取要素值集
    * @param request
    * @param response
    * @return
    */
  @RequestMapping(value = "/dictionary/elevalues/list.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> SelectEleValue(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> result = new HashMap<String, Object>();
    List<?> data = new ArrayList<Object>();
    String ele_code = request.getParameter("ele_code");
    String condition_sql = "";
    //前导编码 是否是对应的值 用于要素的细化
    String ele_value = request.getParameter("ele_value");
    if(ele_value!= null){
    	condition_sql = " and chr_code like '"+ele_value+"'% ";
    }
//    FPaginationDTO page = new FPaginationDTO();
//    page.setCurrpage(-1);
//    page.setPagecount(10);
    String coa_id =  request.getParameter("coa_id");
   
	Map relationPriEleCode = new HashMap();
    String relations = request.getParameter("relations");
    
    if(relations!=null && relations.indexOf("@@")>0 ){
    	 
    	for(String eleCodeValue:relations.split("@@")){
    		
    		String[] value = eleCodeValue.split(":");
    		relationPriEleCode.put(value[0], value[1]);
    	}
    	
    }
    
    try {
      data = dictionary.findEleValuesAsObj(ele_code, null, false, coa_id, relationPriEleCode, condition_sql
        + " and enabled=1 order by chr_code", FAssistObjectDTO.class);
      result.put("data", data);
      result.put("errorCode", 0);
    } catch (Exception e) {
      result.put("errorCode", -1);
      e.printStackTrace();
    }
    return result;
  }
}
