package gov.df.fap.controller.subsystem;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.df.fap.api.subsystem.Isubsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/subsystem")
public class SubsystemController {
	  @Autowired
	  private Isubsystem isubsystem;
	  
	  @RequestMapping(value = "/getSubsystemInfo.do", method = RequestMethod.GET)
	  public @ResponseBody
	  Map<String, Object> a(HttpServletRequest request, HttpServletResponse response) {
	    Map<String, Object> map = null;
	    map = isubsystem.getSubsystemInfo(request, response);
	    return map;
	  }
	  
	  @RequestMapping(value = "/deleSubsystemInfo.do", method = RequestMethod.POST)
	  public @ResponseBody
	  Map<String, Object> b(HttpServletRequest request, HttpServletResponse response) {
	    Map<String, Object> map = null;
	    map = isubsystem.deleSubsystemInfo(request, response);
	    return map;
	  }
	  
	  
	  
	  @RequestMapping(value = "/updSubsystemInfo.do", method = RequestMethod.POST)
	  public @ResponseBody
	  Map<String, Object> c(HttpServletRequest request, HttpServletResponse response) {
	    Map<String, Object> map = null;
	    map = isubsystem.updSubsystemInfo(request, response);
	    return map;
	  }
	  
	  
	  
	  
	  
	  
}
