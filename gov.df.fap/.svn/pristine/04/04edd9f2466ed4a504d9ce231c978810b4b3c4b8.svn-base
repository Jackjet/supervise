package gov.df.fap.controller.resource;

import gov.df.fap.api.resource.IResourceService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 资源相关操作controller
 * @author hp
 * @version 2017-3
 */

@Controller
@RequestMapping(value = "/df/res")
public class ResourceController {

  @Autowired
  private IResourceService iResourceMenuService;

  /**
   * updateBtnMenu : 更新菜单与按钮
   * 新增菜单中按钮
   */
  @RequestMapping(value = "/updateResMenu.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> updateBtnMenu(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    map = iResourceMenuService.addResourceMenu(request, response);
    return map;
  }

  /**
   * delBtnMenu : 更新菜单与按钮
   * 删除菜单中按钮
   */

  @RequestMapping(value = "/delResMenu.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> delBtnMenu(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    map = iResourceMenuService.delResourceMenu(request, response);
    return map;
  }

  /**
   * changeBtnRemark : 更新菜单与按钮
   * 修改菜单中按钮显示状态
   */
  @RequestMapping(value = "/changeResRemark.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> changeBtnRemark(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    map = iResourceMenuService.changeResRemark(request, response);
    return map;
  }

  /**
   * queryRolebtn : 根据角色查询按钮状态
   * 通过角色查询相应菜单下按钮的显示状态
   */
  @RequestMapping(value = "/queryRoleRes.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> queryRolebtn(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    map = iResourceMenuService.queryResByRole(request, response);
    return map;
  }

  @RequestMapping(value = "/changeResCheck.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> changeBtnCheck(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    map = iResourceMenuService.changeResCheck(request, response);
    return map;
  }

}
