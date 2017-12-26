package gov.df.fap.controller.menuedit;

import gov.df.fap.api.menuEdit.ImenueditService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/menuedit")
public class MenueditController {

  @Autowired
  private ImenueditService imenueditService;

  @RequestMapping(value = "/initMenuTree.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> initMenuTree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = imenueditService.initMenuTree(request, response);
    return map;
  }

  @RequestMapping(value = "/queryGrid.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> queryGrid(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = imenueditService.queryGrid(request, response);
    return map;
  }

  @RequestMapping(value = "/insertMenu.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> insertMenu(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = imenueditService.insertMenu(request, response);
    return map;
  }

  @RequestMapping(value = "/delMenu.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> delMenu(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = imenueditService.delMenu(request, response);
    return map;
  }

  @RequestMapping(value = "/parenttree.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> parenttree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = imenueditService.parenttree(request, response);
    return map;
  }

  @RequestMapping(value = "/updateMenu.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> updateMenu(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = imenueditService.updateMenu(request, response);
    return map;
  }
}
