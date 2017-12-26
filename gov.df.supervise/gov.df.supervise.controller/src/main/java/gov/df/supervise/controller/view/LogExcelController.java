/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017 北京用友政务软件有限公司
 * </p>
 * <p>
 * Company: 北京用友政务软件有限公司
 * </p>
 * <p>
 * CreateData: 2017-12-17下午4:58:45
 * </p>
 * 
 * @author songlr3
 * @version 1.0
 */
package gov.df.supervise.controller.view;

import gov.df.supervise.api.common.CommonActionService;
import gov.df.supervise.bean.view.SysLogEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gov.df.supervice.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "/LogExcel")
public class LogExcelController {
	
	@Autowired
	private CommonActionService commonActionService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/expLogExcel.do")
	public @ResponseBody
	Map<String, Object> expLogExcel(HttpServletRequest request, HttpServletResponse response) {
		String action = "";		//操作类型 新增：INPUT 修改：UPDATE 删除：DELETE 查询:QUERY 批量保存 ：INPUTBATCH
		String pageInfo = "";
		String billtype_code = "";
		Map  operationMap = new HashMap();
		Map  conditionMap = new HashMap(); 
		List batchData =  new ArrayList ();
		Map conditionRela = new HashMap();
		
		Map<String,Object> message = new HashMap<String,Object>();
		try{
			if( null != request.getParameter("action")){
				action = request.getParameter("action").toString();
			}
			
			if( null != request.getParameter("pageInfo")){
				pageInfo = request.getParameter("pageInfo").toString();
			}
			
			if( null != request.getParameter("billtype_code")) {
				billtype_code = request.getParameter("billtype_code");
			}
			
			if( null != request.getParameter("condition_data")){
				String str = request.getParameter("condition_data").toString();
				str =str.replace("\\", "");
				str =str.substring(1,str.length()-1);
				conditionMap = JSONObject.parseObject(str);
			}
			
			if( null != request.getParameter("condition_rela")){
				String str = request.getParameter("condition_rela").toString();
				str =str.replace("\\", "");
				conditionRela = JSONObject.parseObject(str);
			}
			
			if( null != request.getParameter("operation_data")){
				if(action.equals("I")) {
			        operationMap = JSONObject.parseObject(request.getParameter("operation_data").toString());
				}else if(action.equals("B")) {
					batchData = JSONObject.parseArray(request.getParameter("operation_data").toString(), Map.class);
				}
			}

			if(billtype_code.equals("") || action.equals("")){
				message.put("errorCode", "1");
				message.put("errorMsg", "参数传输错误,请检查！");
			}else{
				message = commonActionService.action(action, billtype_code, pageInfo, operationMap, batchData , conditionMap , conditionRela);
			}
			if(message!=null){
				List<Map> resultList = new ArrayList<Map>();
				resultList = (List<Map>) message.get("dataDetail");
				message.clear();
				if(resultList!=null){
					JSONArray ja = new JSONArray();
					for(int i = 0 ; i < resultList.size() ; i++){
						SysLogEntity s = new SysLogEntity();
						s.setOP_NAME((String) resultList.get(i).get("op_name"));
						s.setOP_DATE((String) resultList.get(i).get("op_date"));
						s.setMENU_NAME((String) resultList.get(i).get("menu_name"));
						s.setUSER_NAME((String) resultList.get(i).get("user_name"));
						s.setUSER_DEP((String) resultList.get(i).get("user_dep"));
						s.setREMARK((String) resultList.get(i).get("remark"));
						s.setTYPE_NAME((String) resultList.get(i).get("type_name"));
						ja.add(s);
					}
			        Map<String,String> headMap = new LinkedHashMap<String,String>();
			        headMap.put("oP_NAME","操作名称");
			        headMap.put("oP_DATE","操作日期");
			        headMap.put("mENU_NAME","菜单名称");
			        headMap.put("uSER_NAME","操作人");
			        headMap.put("uSER_DEP","操作人部门");
			        headMap.put("rEMARK","备注");
			        headMap.put("tYPE_NAME","日志类型");
					String title = "日志统计表";
					ExcelUtil.downloadExcelFile(title,headMap,ja,response);
					message.put("errorCode", "0");
				    message.put("errorMsg", "导出成功！");
				}else{
					message.put("errorCode", "1");
				    message.put("errorMsg", "日志无数据，无法导出！");
				}
			}else{
			    message.put("errorCode", "1");
			    message.put("errorMsg", "日志无数据，无法导出！");
			}
		}catch (Exception e) {
		    e.printStackTrace();
		    message.put("errorCode", "1");
		    message.put("errorMsg", "获取数据出现异常");
		}
		return message;
	}

}
