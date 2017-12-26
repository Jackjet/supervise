package gov.df.fap.controller.roleConfig;

import gov.df.fap.api.roleConfig.IRoleConfigService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/roleConfig")
public class RoleConfigServiceController {

  @Autowired
  private IRoleConfigService iRoleConfigService;

  @RequestMapping(value = "/initRoleTree.do")
  public @ResponseBody
  Map<String, Object> initRoleTree(HttpServletRequest request, HttpServletResponse response) {
    Map map = iRoleConfigService.initRoleTree(request, response);
    return map;
  }

  @RequestMapping(value = "/queryRole.do")
  public @ResponseBody
  Map<String, Object> queryRole(HttpServletRequest request, HttpServletResponse response) {
    Map map = iRoleConfigService.queryRole(request, response);
    return map;
  }

  @RequestMapping(value = "/saveRole.do")
  public @ResponseBody
  Map<String, Object> saveRole(HttpServletRequest request, HttpServletResponse response) {
    Map map = iRoleConfigService.saveRole(request, response);
    return map;
  }

  @RequestMapping(value = "/updateRole.do")
  public @ResponseBody
  Map<String, Object> updateRole(HttpServletRequest request, HttpServletResponse response) {
    Map map = iRoleConfigService.updateRole(request, response);
    return map;
  }

  @RequestMapping(value = "/delRole.do")
  public @ResponseBody
  Map<String, Object> delRole(HttpServletRequest request, HttpServletResponse response) {
    Map map = iRoleConfigService.delRole(request, response);
    return map;
  }
}
