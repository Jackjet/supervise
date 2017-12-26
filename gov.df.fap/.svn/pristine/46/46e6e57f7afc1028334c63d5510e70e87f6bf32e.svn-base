package gov.df.fap.controller.elementConfig;

import gov.df.fap.api.elementConfig.IElementConfigService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/df/elementConfig")
public class ElementConfigController {

  @Autowired
  private IElementConfigService iElementConfigService;

  @RequestMapping(value = "/init.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> init(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.init(request, response);
    return map;
  }

  @RequestMapping(value = "/sysGrid.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> sysGrid(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.sysGrid(request, response);
    return map;
  }

  @RequestMapping(value = "/entertree.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> entertree(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.entertree(request, response);
    return map;
  }

  @RequestMapping(value = "/listtree.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> listtree(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.listtree(request, response);
    return map;
  }

  @RequestMapping(value = "/deleteEle.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> deleteEle(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.deleteEle(request, response);
    return map;
  }

  @RequestMapping(value = "/insertEle.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> insertEle(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.insertEle(request, response);
    return map;
  }

  @RequestMapping(value = "/updateEle.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> updateEle(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.updateEle(request, response);
    return map;
  }

  @RequestMapping(value = "/queryenter.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> queryenter(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.queryenter(request, response);
    return map;
  }

  @RequestMapping(value = "/updateEleCode.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> updateEleCode(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.updateEleCode(request, response);
    return map;
  }

  @RequestMapping(value = "/dataElementTree.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> dataElementTree(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.dataElementTree(request, response);
    return map;
  }

  @RequestMapping(value = "/dataElementTree1.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> dataElementTree1(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.dataElementTree1(request, response);
    return map;
  }

  @RequestMapping(value = "/eleGridQuery.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> eleGridQuery(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.eleGridQuery(request, response);
    return map;
  }

  @RequestMapping(value = "/insertEleData.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> insertEleData(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.insertEleData(request, response);
    return map;
  }

  @RequestMapping(value = "/updateEleData.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> updateEleData(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.updateEleData(request, response);
    return map;
  }

  @RequestMapping(value = "/delEleData.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> delEleData(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.delEleData(request, response);
    return map;
  }

  @RequestMapping(value = "/queryFormData.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> queryFormData(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.queryFormData(request, response);
    return map;
  }

  @RequestMapping(value = "/getElementDetailTree.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getElementDetailTree(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.getElementDetailTree(request, response);
    return map;
  }

  @RequestMapping(value = "/getElementSourceTree.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getElementSourceTree(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.getElementSourceTree(request, response);
    return map;
  }

  @RequestMapping(value = "/getElementDetailGrid.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getElementDetailGrid(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.getElementDetailGrid(request, response);
    return map;
  }

  @RequestMapping(value = "/getElementSourceGrid.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getElementSourceGrid(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.getElementSourceGrid(request, response);
    return map;
  }

  @RequestMapping(value = "/getElementColumn.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getElementColumn(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.getElementColumn(request, response);
    return map;
  }

  @RequestMapping(value = "/insertElementColumn.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> insertElementColumn(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.insertElementColumn(request, response);
    return map;
  }

  @RequestMapping(value = "/queryElementColumn.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> queryElementColumn(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.queryElementColumn(request, response);
    return map;
  }

  @RequestMapping(value = "/updateElementColumn.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> updateElementColumn(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.updateElementColumn(request, response);
    return map;
  }

  @RequestMapping(value = "/getElementForm.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getElementForm(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.getElementForm(request, response);
    return map;
  }

  @RequestMapping(value = "/updateElementDate.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> updateElementDate(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.updateElementDate(request, response);
    return map;
  }

  @RequestMapping(value = "/updateEnableDate.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> updateEnableDate(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.updateEnableDate(request, response);
    return map;
  }

  @RequestMapping(value = "/updateElementData1.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> updateElementData1(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.updateElementData1(request, response);
    return map;
  }

  @RequestMapping(value = "/delElementData1.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> delElementData1(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.delElementData1(request, response);
    return map;
  }

  @RequestMapping(value = "/delElementData2.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> delElementData2(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.delElementData2(request, response);
    return map;
  }

  @RequestMapping(value = "/delElementData3.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> delElementData3(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.delElementData3(request, response);
    return map;
  }

  @RequestMapping(value = "/updateNodataElementColumn.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> updateNodataElementColumn(HttpServletRequest request, HttpServletResponse response) {
    Map map = iElementConfigService.updateNodataElementColumn(request, response);
    return map;
  }
}
