package gov.df.fap.controller.portal;

import gov.df.fap.api.portal.IBaseService;
import gov.df.fap.util.factory.ServiceFactory;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/portal/")
public class GetPageJsonDataController {
	
	private static final long serialVersionUID = -4496743740887638033L;
	private static final Logger log = Logger.getLogger(GetPageJsonDataController.class);
	
	//获取serviceBean
	private IBaseService baseService =(IBaseService) ServiceFactory.getBean("df.fap.portal.service.getBaseService"); 
	@RequestMapping(value="/GetPageJsonData.do", method = RequestMethod.GET)
	public @ResponseBody String GetPageJsonData(HttpServletRequest request,HttpServletResponse response) throws Exception{
//		Map parameter = (Map)request.getParameterMap();
//		Map params = new HashMap();
//		Iterator iter = parameter.entrySet().iterator(); 
//		while   (iter.hasNext())   { 
//			Entry  entry = (Entry)iter.next(); 
//			String mapKey =(String) entry.getKey(); 
//			String mapValue = ((String[]) entry.getValue())[0]; 
//			String str=new String(mapValue.getBytes("ISO-8859-1"),"UTF-8");
//			params.put(mapKey, str);
//		}
		String tokenId = request.getParameter("tokenId");
		String ruleId = request.getParameter("ruleID");
		String pgPletId = request.getParameter("pgPletId");
		String userId = request.getParameter("userId");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		Map<String, String> params = new HashMap<String, String>();
		//params.put("tokenid", tokenId);
		params.put("ruleID", ruleId);
		params.put("start", start);
		params.put("limit", limit);
		params.put("userId", userId);
		
		/**
		 * 初始化分页值
		 */
		int startNum = 0;//起始行
		int pageSize = 0;//页记录数
		//String start = (String)params.get("start");
		if(start != null){
			startNum = Integer.parseInt(start);
		}
		//String limit = (String)params.get("limit");
		if(limit != null){
			pageSize = Integer.parseInt(limit);
		}
		int limitSize = startNum + pageSize;
		
		//String ruleId = (String)params.get("ruleID");
		if(ruleId != null && !"".equals(ruleId)){
			PrintWriter out = null;
			try{
				StringBuffer resultJson = new StringBuffer();
				int recordNum = baseService.getTotalRecordCount(ruleId, params);
				
				params.put("startNum", startNum+"");
				params.put("limitNum", limitSize+"");
				List dataList = baseService.getPageDataList(ruleId, params);
				
				JSONArray jsonArray = new JSONArray();
				if(dataList != null && dataList.size() > 0){
					for(int i = 0 ; i < dataList.size(); i++){
						Map map = (Map)dataList.get(i);
						Map temp = new HashMap();
						Iterator it = map.entrySet().iterator();
						while(it.hasNext()){
							Entry ent = (Entry)it.next();
							String mapKey = ((String)ent.getKey()).toLowerCase();
							String mapValue = "";
							if(ent.getValue() != null)mapValue = ent.getValue().toString();
							temp.put(mapKey, mapValue);
						}
						jsonArray.put(temp);
					}
				}
				resultJson.append(jsonArray.toString());
				response.setContentType("text/xml; charset=UTF-8");
		    response.setHeader("Cache-Control", "no-cache");
				out = response.getWriter();
		    out.println(resultJson.toString());
		    out.flush();
			}catch(Exception e){
	//			log.error(e);
				throw new RuntimeException(e);
			}finally{
			  if(out != null){
			    out.close();
			  }
			}
		}
		return null;
	}
}
