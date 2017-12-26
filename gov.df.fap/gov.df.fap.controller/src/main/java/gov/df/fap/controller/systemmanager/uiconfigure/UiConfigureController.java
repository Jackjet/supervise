package gov.df.fap.controller.systemmanager.uiconfigure;

import gov.df.fap.api.dictionary.ElementOperation;
import gov.df.fap.api.systemmanager.uiconfigure.IUIConfigure;
import gov.df.fap.api.systemmanager.uiconfigure.IViewConfigure;
import gov.df.fap.bean.view.SysUidetail;
import gov.df.fap.bean.view.SysUimanager;
import gov.df.fap.util.xml.XMLData;

import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/df/viewConfig")
public class UiConfigureController {
	@Autowired
	IViewConfigure iUiConfigureService;
	@Autowired
	@Qualifier("elementOperationWrapper")       
	ElementOperation elementOperationImpl;
	@Autowired
	IUIConfigure uIConfigureImplBO;
	String msg ="";
	
	@RequestMapping(value="/initCombox")
	public @ResponseBody Map<String, Object> initViewCombox(HttpServletRequest request ,HttpServletResponse response){
		Map<String, Object> map =null;
		map=iUiConfigureService.initViewCombox();
		return map;
		
	}
	
	@RequestMapping(value="/getAllInfoForView.do")
	public @ResponseBody Map<String, Object> getAllInfoForView(HttpServletRequest request ,HttpServletResponse response){
		Map<String, Object> map =null;
		String viewId = request.getParameter("viewId");
		map = iUiConfigureService.getAllInfoForView(viewId);
		//map=iUiConfigureService.initViewCombox();
		return map;
		
	}
	@RequestMapping(value="/delView.do")
	public @ResponseBody Map<String, Object> delView(HttpServletRequest request ,HttpServletResponse response){
		Map<String, Object> map =null;
		String viewId = request.getParameter("viewId");
		map = iUiConfigureService.delView(viewId);
		return map;
		
	}
	
	@RequestMapping(value="/getEleInfo.do")
	public @ResponseBody Map<String, Object> getEleInfo(HttpServletRequest request ,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		List list =  elementOperationImpl.getEleSetByCondition("");
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Map obj = (Map) iterator.next();
			obj.put("parentid", "0");
			obj.put("elename", obj.get("ele_code")+" " + obj.get("ele_name"));
		}
		
		Map<String, Object> mapnew =new HashMap<String, Object>();
		mapnew.put("chr_id", "0");
		mapnew.put("elename", "全部");
		mapnew.put("parentid", "");
		mapnew.put("open",true);
		list.add(0,mapnew);
		
		map.put("eleinfo", list);
		
		return map;
	}
	
	@RequestMapping(value="/saveView",method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveView(HttpServletRequest resq,HttpServletResponse response){
		Map<String, Object> map =new HashMap<String, Object>();
		try {
			String aaString = resq.getParameter("viewData");
			String optType = resq.getParameter("optType");
			JSONObject jsonObject = JSONObject.fromObject(aaString);
			List<SysUidetail> sysUidetails =new ArrayList<SysUidetail>();
			
			if(jsonObject.containsKey("uidetails")){
				JSONArray json_to_array = jsonObject.getJSONArray("uidetails");
				 for (Object object : json_to_array) {//循环读取即可
				     JSONObject detailJsonObject = JSONObject.fromObject(object);
				     SysUidetail sysUidetail = (SysUidetail) JSONObject.toBean(detailJsonObject,  SysUidetail.class);
				     if(sysUidetail.getValue()!=null){
				       sysUidetail.setValue(URLDecoder.decode(sysUidetail.getValue(), "utf-8"));
				     }
				     sysUidetails.add(sysUidetail);  
				 }
			}
			
			SysUimanager entity = (SysUimanager) JSONObject.toBean(jsonObject,  SysUimanager.class);
			
			entity.setUidetails(sysUidetails);
			
			if(checkData(entity,optType)){
				map = iUiConfigureService.saveOrUpdateView(entity,optType);
				map.put("flg", "sucess");
				map.put("msg", msg);
			}else {
				map.put("flg", "fail");
				map.put("msg", msg);
			}

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("flg", "fail");
			map.put("msg", "保存失败！！！");
		}
		
		return map;
		
	}
	
	
	public boolean checkData(SysUimanager entity,String optType) {	  
		msg="";
	    String ui_code_temp = (String) entity.getUi_code();
	    if (ui_code_temp == null) {
	      ui_code_temp = "";
	    }

	    if ("".equals(ui_code_temp)) {
	      msg ="视图编号不能为空！";
	      return false;
	      // 界面编号长度不是3的倍数
	    } else if ((ui_code_temp.length() % 3) != 0) {
	      msg = "视图编号的位数必须为3的倍数！";
	      return false;
	    }
	    String ui_name_temp = (String) entity.getUi_name();

	    if (ui_name_temp == null || "".equalsIgnoreCase(ui_name_temp)) {
	      // 面板的界面名字为空
	      msg = "视图名称不能为空！";
	      return false;
	    }

	    // 视图类型的模型数据的默认值
	    String ui_type = (String) entity.getUi_type();

	    if (ui_type == null || "".equalsIgnoreCase(ui_type)) {
	      // 面板的界面名字为空
	      msg = "视图类型不能为空！";
	      return false;
	    }

	    String sys_id = (String) entity.getSys_id();

	    if (sys_id == null || "".equalsIgnoreCase(sys_id)) {
	      // 面板的界面名字为空
	      msg = "所属子系统模块不能为空！";
	      return false;
	    }

	    // 获得列名
	    
	    List<SysUidetail> detailList = entity.getUidetails();
	    int size = detailList.size();
	    
	    for (int i = 0; i < size; i++) {
	    	SysUidetail detailData = (SysUidetail) detailList.get(i);
	      if (detailData.getId() == null || ("").equals(detailData.getId())) {
	        msg = "明细列控件字段不能为空！请检查";
	        return false;
	      }
	      // 判断枚举项数据格式是否正确
	      if (detailData.getRef_model() != null && !"".equals(detailData.getRef_model())) {
	        String refModel = detailData.getRef_model().toString();
	        String[] model = refModel.split("\\+");
	        for (int j = 0; j < model.length; j++) {
	          String temp = model[j];
	          if (temp.indexOf("#") == -1) {
	            msg = "明细列" + detailData.getId() + "的枚举项数据格式不正确，请检查！";
	            return false;
	          }
	        }
	      }
	      
	      if (detailData.getDisp_mode() == null || ("").equals(detailData.getDisp_mode())) {
          msg = "明细列" + detailData.getId() + "的显示方式不能为空，请检查！";
          return false;
        }
	      
	      if ((detailData.getRef_model() == null || ("").equals(detailData.getRef_model())) && "checkbox".equals(detailData.getDisp_mode())) {
	    	  	msg = "明细列" + detailData.getId() + "的显示方式为【选择框】时枚举项不能为空，请检查！";
		        return false;
		      }
	      if ((detailData.getRef_model() == null || ("").equals(detailData.getRef_model())) && "radio".equals(detailData.getDisp_mode())) {
	    	  	msg = "明细列" + detailData.getId() + "的显示方式为【单选框】时枚举项不能为空，请检查！";
		        return false;
		      }
	      if ((detailData.getRef_model() == null || ("").equals(detailData.getRef_model())) && "combobox".equals(detailData.getDisp_mode())) {
	    	  	msg = "明细列" + detailData.getId() + "的显示方式为【下拉框】时枚举项不能为空，请检查！";
		        return false;
		      }

	      if (detailData.getValue() != null && "decimal".equals(detailData.getDisp_mode())) {
	        String value = detailData.getValue().toString();
	        for (int j = 0; j < value.length(); j++) {
	          if (!(value.charAt(j) >= 48 && value.charAt(j) <= 57) && !(value.charAt(j) == 46)) {
	            msg ="明细列" + detailData.getId() + "的默认值只能为数字，请检查！";
	            return false;
	          }
	        }
	      }

	      if (detailData.getValue() != null && "int".equals(detailData.getDisp_mode())) {
	        String value = detailData.getValue().toString();
	        for (int j = 0; j < value.length(); j++) {
	          if (!(value.charAt(j) >= 48 && value.charAt(j) <= 57)) {
	            msg = "明细列" + detailData.getId() + "的默认值只能为整数，请检查！";
	            return false;
	          }
	        }
	      }

	      if (detailData.getValue() != null && "datetime".equals(detailData.getDisp_mode())) {
	        String value = detailData.getValue().toString();
	        String field_name = (String) detailData.getId();
	        if (!isValidDateYearMonDay(value, field_name)) {
	          return false;
	        }
	      }
	      // ydz 对虚拟字段添加 非空校验
	      if (detailData.getIs_virtual_column() != null//增加非空判断，modify by 张君阳 on 2011-05-12
	        && Boolean.valueOf(detailData.getIs_virtual_column().toString().trim()).booleanValue()
	        && (detailData.getVirtual_column_sql() == null || detailData.getVirtual_column_sql().toString()
	          .trim().equals(""))) {
	        msg = "明细列控件【" + detailData.getId() + "】为虚拟字段，虚拟列SQL不能为空，请检查！";
	        return false;
	      }

	    }
	    // 判断明细列控件ID是否重复
	    for (int i = 0; i < size; i++) {
	    	SysUidetail detailData = (SysUidetail) detailList.get(i);
	    	String fieldName1 = detailData.getId().toString();
	      for (int j = i + 1; j < size; j++) {
	        String fieldName2 = ((SysUidetail) detailList.get(j)).getId().toString();
	        if (fieldName1.equalsIgnoreCase(fieldName2)) {
	          msg = "明细列控件【" + fieldName1 + "】ID重复，请检查！";
	          return false;
	        }
	      }
	    }
	    
	    XMLData uIXmlData = null;
		try {
			uIXmlData = uIConfigureImplBO.getAllByUiCode(ui_code_temp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 msg = "该视图编号不正确！";
	          return false;
			
		}  
	    if(uIXmlData!=null){
	    	 if ("new".equals(optType)){
	    		 msg = "该视图编号已存在！";
		          return false;
	    	 }else if(("modify").equals(optType)){
	    		 if(!(uIXmlData.get("ui_id").toString()).equals(entity.getUi_id().toString())){
	    			 msg = "该视图编号已存在！";
	   	          return false;
	    		 }
	    	 }
	    }

	    return true;
	  }
	
	
	/**
	   * 通过实现的日期字符串得日期对象
	   * 
	   * @param value
	   *            日期字符串，与存放在库中的相一致
	   * @return 日期对象
	   */
	  private boolean isValidDateYearMonDay(String value, String field_name) {
	    Format format = new SimpleDateFormat("yyyy-MM-dd");
	    String msg = "";
	    try {
	      format.parseObject(value);
	      String year = value.substring(0, value.indexOf("-"));
	      if (year.length() != 4 && Integer.valueOf(year).intValue() < 1000) {
	        msg = "明细列" + field_name + "的默认值格式不正确！正确格式应为'yyyy-MM-dd'。";
	        return false;
	      }
	      if (!isLeapYear(Integer.valueOf(year).intValue())
	        && ("02-29".equals(value.substring(value.indexOf("-") + 1)) || "2-29".equals(value.substring(value
	          .indexOf("-") + 1)))) {
	        msg = "明细列" + field_name + "的默认值不正确！" + year + "年不是闰年，没有2月29日。";
	        return false;
	      }
	    } catch (Exception ex) {
	      msg = "明细列" + field_name + "的默认值格式不正确！正确格式应为'yyyy-MM-dd'。";
	      return false;
	    }
	    return true;
	  }
	  
	  /**
	   * 判断是否为闰年 
	   */
	  private static boolean isLeapYear(int year) {
	    return year % 4 == 0 && (year % 400 == 0 || year % 100 != 0);
	  }


}
