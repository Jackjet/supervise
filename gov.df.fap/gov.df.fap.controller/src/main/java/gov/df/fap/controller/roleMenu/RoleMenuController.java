package gov.df.fap.controller.roleMenu;

import gov.df.fap.api.roleMenu.IRoleMenuService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/rolemenu")
public class RoleMenuController {

  @Autowired
  private IRoleMenuService iRoleMenuService;

  @RequestMapping(value = "/initTree.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> initTree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    map = iRoleMenuService.initTree(request, response);
    return map;
  }

  @RequestMapping(value = "/TreeChecked.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> TreeChecked(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    map = iRoleMenuService.TreeChecked(request, response);
    return map;
  }

  @RequestMapping(value = "/updateRolemenu.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> updateRolemenu(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    map = iRoleMenuService.updateRolemenu(request, response);
    return map;
  }
}
