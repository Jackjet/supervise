package gov.df.fap.controller.userparaConfig;

import gov.df.fap.api.userparaConfig.IUserparaConfigService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/userparaConfig")
public class UserparaConfigController {

  @Autowired
  private IUserparaConfigService iUserparaConfigService;

  @RequestMapping(value = "/dataQuery.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> dataQuery(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iUserparaConfigService.dataQuery(request, response);
    return map;
  }

  @RequestMapping(value = "/updatePara.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> updatePara(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iUserparaConfigService.updatePara(request, response);
    return map;
  }

  @RequestMapping(value = "/initpara.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> initpara(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iUserparaConfigService.initpara(request, response);
    return map;
  }
}
