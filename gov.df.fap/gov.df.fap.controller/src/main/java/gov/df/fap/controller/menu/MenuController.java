package gov.df.fap.controller.menu;

import gov.df.fap.api.menu.IBtnService;
import gov.df.fap.api.menu.IMenuFilter;
import gov.df.fap.api.menu.IStatusService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 菜单相关操作controller
 * @author hp
 * @version 2017-3
 */

@Controller
@RequestMapping(value = "/df/menu")
public class MenuController {
  @Autowired
  @Qualifier("df.fap.menu")
  private IMenuFilter iMenuFilter;

  @Autowired
  private IStatusService iStatusService;

  @Autowired
  private IBtnService iBtnService;

  @RequestMapping(value = "/getbyrole.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getMenuByRole(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    map = iMenuFilter.getMenuByRole(request, response);
    return map;
  }

  @RequestMapping(value = "/getbyuser.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getMenuByUser(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;

    return map;
  }

  @RequestMapping(value = "/getAllMenu.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getAllMenu(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    map = iMenuFilter.getAllMenu(request, response);
    return map;
  }

  @RequestMapping(value = "/insertMenuStatues.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> insertMenuStatues(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iStatusService.insertMenuStatues(request, response);
    return map;
  }

  @RequestMapping(value = "/StatuesGrid.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> StatuesGrid(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iStatusService.StatuesGrid(request, response);
    return map;
  }

  @RequestMapping(value = "/updateMenuStatues.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> updateMenuStatues(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iStatusService.updateMenuStatues(request, response);
    return map;
  }

  @RequestMapping(value = "/delStatus.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> delStatus(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iStatusService.delStatus(request, response);
    return map;
  }

  @RequestMapping(value = "/insertMenuBtn.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> insertMenuBtn(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iBtnService.insertMenuBtn(request, response);
    return map;
  }

  @RequestMapping(value = "/BtnGrid.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> BtnGrid(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iBtnService.BtnGrid(request, response);
    return map;
  }

  @RequestMapping(value = "/delBtn.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> delBtn(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iBtnService.delBtn(request, response);
    return map;
  }

  @RequestMapping(value = "/BtnTree.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> BtnTree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iBtnService.BtnTree(request, response);
    return map;
  }

  @RequestMapping(value = "/BtnExist.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> BtnExist(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iBtnService.BtnExist(request, response);
    return map;
  }

  @RequestMapping(value = "/menuStatusTree.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> menuStatusTree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iStatusService.menuStatusTree(request, response);
    return map;
  }

  @RequestMapping(value = "/insertmenuStatus.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> insertmenuStatus(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iStatusService.insertmenuStatus(request, response);
    return map;
  }

  @RequestMapping(value = "/delmenuStatus.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> delmenuStatus(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iStatusService.delmenuStatus(request, response);
    return map;
  }

  @RequestMapping(value = "/BtnCheck.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> BtnCheck(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iBtnService.BtnCheck(request, response);
    return map;
  }

  @RequestMapping(value = "/insertBtnCheck.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> insertBtnCheck(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iBtnService.insertBtnCheck(request, response);
    return map;
  }

  @RequestMapping(value = "/delBtnCheck.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> delBtnCheck(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iBtnService.delBtnCheck(request, response);
    return map;
  }
}
