package gov.df.fap.controller.wf.activiti;

import gov.df.fap.api.workflow.activiti.design.UpGradeService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/sys")
public class UpGradeController {

	@Autowired
	private UpGradeService upGradeService;

	/**
	 * 系统升级
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/upg", method = RequestMethod.POST)
	public @ResponseBody
	Map<String,String> upGrade(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = upGradeService.getUpGradeBLOB();
		return map;
	}

}
