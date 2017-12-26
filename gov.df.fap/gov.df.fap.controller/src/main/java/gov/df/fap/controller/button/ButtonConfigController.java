package gov.df.fap.controller.button;

import gov.df.fap.api.button.IButtonConfigService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/buttoncofnig")
public class ButtonConfigController {

  @Autowired
  private IButtonConfigService iButtonConfigService;

  @RequestMapping(value = "/initBtnTree.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> initBtnTree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iButtonConfigService.initBtnTree(request, response);
    return map;
  }

  @RequestMapping(value = "/BtnGrid.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> BtnGrid(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iButtonConfigService.BtnGrid(request, response);
    return map;
  }

  @RequestMapping(value = "/insertBtn.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> insertBtn(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iButtonConfigService.insertBtn(request, response);
    return map;
  }

  @RequestMapping(value = "/updateBtn.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> updateBtn(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iButtonConfigService.updateBtn(request, response);
    return map;
  }

  @RequestMapping(value = "/delBtn.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> delBtn(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iButtonConfigService.delBtn(request, response);
    return map;
  }
}
