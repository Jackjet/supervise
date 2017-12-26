package gov.df.fap.controller.init;

import gov.df.fap.api.init.IInitService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 页面初始化
 * @author hp
 * @version 2017-3
 */
@Controller
@RequestMapping(value = "/df/init")
public class InitController {

  @Autowired
  private IInitService iInitService;

  @RequestMapping(value = "/initMsg.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> initMsg(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    try {
      map = iInitService.queryResByRole(request, response);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return map;
  }

  @RequestMapping(value = "/ResState.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> ResState(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    try {
      map = iInitService.ResState(request, response);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return map;
  }

  @RequestMapping(value = "/menuStatuebtn.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> menuStatuebtn(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iInitService.menuStatuebtn(request, response);
    return map;
  }
}
