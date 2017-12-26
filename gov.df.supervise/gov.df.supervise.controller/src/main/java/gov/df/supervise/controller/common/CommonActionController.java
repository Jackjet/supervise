package gov.df.supervise.controller.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.df.supervise.api.common.CommonActionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ufgov.ip.apiUtils.UUIDTools;

@Controller
@RequestMapping(value = "/df/common")
public class CommonActionController {

	@Autowired
	private CommonActionService commonActionService;

	@RequestMapping(method = RequestMethod.GET, value = "/commonAction.do")
	public @ResponseBody
	Map<String, Object> commonAction(HttpServletRequest request, HttpServletResponse response) {
		
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
				conditionMap = JSONObject.parseObject(request.getParameter("condition_data").toString());
			}
			
			if( null != request.getParameter("condition_rela")){
				conditionRela = JSONObject.parseObject(request.getParameter("condition_rela").toString());
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
		}catch (Exception e) {
		    e.printStackTrace();
		    message.put("errorCode", "1");
		    message.put("errorMsg", "获取数据出现异常");
		}
		return message;
	}
	
	public static void main(String [] args){
		String s= "{1:2, 3:4}";
		Map map = JSONObject.parseObject(s);
		for (int i=0;i<map.size();i++){
		   map.get(1);
		}
	}

	/**
	 * 获取UUID
	 * 
	 * @param request
	 * @param response
	 * @return UUID
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getUUID.do")
	public @ResponseBody
	String getUUID(HttpServletRequest request, HttpServletResponse response) {
		return UUIDTools.uuidRandom();
	}
	
}
