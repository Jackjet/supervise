package gov.df.fap.controller.messageplatform;


import gov.df.fap.api.messageplatform.IMessageSend;
import gov.df.fap.api.redis.CacheUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/df/imaccess")
public class IMAccessController {
  
  
  	@Autowired
  	private IMessageSend messageSendService;
	
		/**
		 * 获取用户配置
		 */
		@RequestMapping(method = RequestMethod.POST,value="/getImParam.do")
		public  @ResponseBody Map<String,Object> getImParam(HttpServletRequest request,HttpServletResponse response){
		  Map<String,Object> imParamMap = new HashMap<String, Object>();
		  imParamMap=messageSendService.getImParam();
		  return imParamMap;
		}
	 
	/**
	 * 获取用户的Token
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getUserToken.do")
	public @ResponseBody Map<String, Object> getUserToken(
			HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> res = new HashMap<String, Object>();
		String username=request.getParameter("username");
		res=messageSendService.getUserToken(username);
		return res;
	}
}
