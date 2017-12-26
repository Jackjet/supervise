package gov.df.fap.controller.userConfig;

import gov.df.fap.api.userConfig.IUserConfigService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/userConfig")
public class UserConfigController {

  @Autowired
  private IUserConfigService iUserConfigService;

  @RequestMapping(value = "/usertree.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> usertree(HttpServletRequest request, HttpServletResponse response) {
    Map map = iUserConfigService.usertree(request, response);
    return map;
  }

  @RequestMapping(value = "/usergrid.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> usergrid(HttpServletRequest request, HttpServletResponse response) {
    Map map = iUserConfigService.usergrid(request, response);
    return map;
  }

  @RequestMapping(value = "/findEleCodeByOrgtypeCode.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> findEleCodeByOrgtypeCode(HttpServletRequest request, HttpServletResponse response) {
    Map map = iUserConfigService.findEleCodeByOrgtypeCode(request, response);
    return map;
  }

  @RequestMapping(value = "/findrole.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> findrole(HttpServletRequest request, HttpServletResponse response) {
    Map map = iUserConfigService.findrole(request, response);
    return map;
  }

  @RequestMapping(value = "/addUser.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> addUser(HttpServletRequest request, HttpServletResponse response) {
    Map map = iUserConfigService.addUser(request, response);
    return map;
  }

  @RequestMapping(value = "/initMessage.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> initMessage(HttpServletRequest request, HttpServletResponse response) {
    Map map = iUserConfigService.initMessage(request, response);
    return map;
  }

  @RequestMapping(value = "/deleteUser.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> deleteUser(HttpServletRequest request, HttpServletResponse response) {
    Map map = iUserConfigService.deleteUser(request, response);
    return map;
  }

  @RequestMapping(value = "/locked.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> locked(HttpServletRequest request, HttpServletResponse response) {
    Map map = iUserConfigService.locked(request, response);
    return map;
  }

  @RequestMapping(value = "/unlocked.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> unlocked(HttpServletRequest request, HttpServletResponse response) {
    Map map = iUserConfigService.unlocked(request, response);
    return map;
  }
}
