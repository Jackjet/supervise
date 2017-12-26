package gov.df.fap.controller.view;

import gov.df.fap.api.view.IViewService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/view")
public class ViewController {

  @Autowired
  private IViewService iViewService;

  @RequestMapping(value = "/getMenuView.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getMenuByRole(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    map = iViewService.getViewByGuid(request, response);
    return map;
  }

  @RequestMapping(value = "/getViewTree.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getViewTree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    map = iViewService.getViewTree(request, response);
    return map;
  }

  @RequestMapping(value = "/updateMenuView.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> updateMenuView(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    map = iViewService.updateMenuView(request, response);
    return map;
  }

  @RequestMapping(value = "/delMenuView.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> delMenuView(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    map = iViewService.delMenuView(request, response);
    return map;
  }

  @RequestMapping(value = "/getViewDetail.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getViewDetail(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    map = iViewService.getViewDetail(request, response);
    return map;
  }

  @RequestMapping(value = "/getAllMenuView.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getAllMenuView(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    map = iViewService.getAllMenuView(request, response);
    return map;
  }
}
