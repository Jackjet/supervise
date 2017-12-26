package gov.df.fap.controller.globalConfigDialog;

import gov.df.fap.api.globalConfigDialog.IGlobalConfigDialogService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/df/globalConfig")
public class GlobalConfigDialogController {

  @Autowired
  private IGlobalConfigDialogService iGlobalConfigDialogService;

  @RequestMapping(value = "/updateFilter.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> updateFilter(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iGlobalConfigDialogService.updateFilter(request, response);
    return map;
  }

  @RequestMapping(value = "/getElementTree.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getElementTree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iGlobalConfigDialogService.getElementTree(request, response);
    return map;
  }

  @RequestMapping(value = "/getMenuTree.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getMenuTree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iGlobalConfigDialogService.getMenuTree(request, response);
    return map;
  }

  @RequestMapping(value = "/getfilterinit.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getfilterinit(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iGlobalConfigDialogService.filterInit(request, response);
    return map;
  }

  @RequestMapping(value = "/updateCommonFilter.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> updateCommonFilter(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iGlobalConfigDialogService.updateCommonFilter(request, response);
    return map;
  }

  /**
   * （初始化）基本的配置信息
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/initBasePageInfo.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> initBasePageInfo(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iGlobalConfigDialogService.initBasePageInfo(request, response);
    return map;
  }

  /**
   * （保存）公务卡页面信息
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/savePayCardInfo.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> savePayCardInfo(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iGlobalConfigDialogService.savePayCardInfo(request, response);
    return map;
  }

  /**
   * （保存）配置信息
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/saveConfig.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> saveConfig(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String configdata = request.getParameter("configdata");
    if ("".equals(configdata) || configdata == null) {
      map.put("flag", "0");
      map.put("msg", "信息项没有变更，不需要保存！");
      return map;
    }
    try {
      Map configdataMap = new HashMap();
      JSONObject jsonObject = JSONObject.fromObject(configdata);
      configdataMap = (Map) jsonObject;
      map = iGlobalConfigDialogService.saveConfig(configdataMap);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      map.put("flag", "0");
      map.put("msg", "服务器繁忙,请稍后再试！");
    }
    return map;
  }

  @RequestMapping(value = "/loadBilltypeConfig.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> loadBilltype(HttpServletRequest request, HttpServletResponse response) {
    long l = System.currentTimeMillis();
    Map<String, Object> map = new HashMap<String, Object>();
    List list = new ArrayList();
    try {
      // 凭证类型 
      map = iGlobalConfigDialogService.initBasePageInfo(request, response);
      list = iGlobalConfigDialogService.loadBilltype("0");
      map.put("billtype", list);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      map.put("flag", "0");
    }
    long l1 = System.currentTimeMillis();

    System.out.println(l1 - l);
    return map;
  }

  /**
   * 批量录入规则下拉列表
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/initDropDownList.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> initDropDownList(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iGlobalConfigDialogService.initDropDownList(request, response);
    return map;
  }

  /**
   * 保存工资接口配置数据
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/saveSalaryInterface.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> saveSalaryInterface(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iGlobalConfigDialogService.saveSalaryInterface(request, response);
    return map;
  }

  /**
   * 保存常规接口配置数据
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/saveConventionalInterface.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> saveConventionalInterface(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iGlobalConfigDialogService.saveConventionalInterface(request, response);
    return map;
  }

  /**
   * 保存银行配置数据
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/saveBankSettingData.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> saveBankSettingData(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iGlobalConfigDialogService.saveBankSettingData(request, response);
    return map;
  }

  /**
   * 保存政府采购接口配置数据
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/saveGovProcurementInterfaceData.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> saveGovProcurementInterfaceData(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iGlobalConfigDialogService.saveGovProcurementInterfaceData(request, response);
    return map;
  }

  /**
   * 保存用户参数-支付管理配置数据
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/savePayManagementData.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> savePayManagementData(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iGlobalConfigDialogService.savePayManagementData(request, response);
    return map;
  }

  /**
   * 初始化客户端查询参数配置界面数据
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/initClientQuerySetting.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> initClientQuerySetting(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iGlobalConfigDialogService.initClientQuerySetting(request, response);
    return map;
  }

  /**
   * 保存客户端查询参数配置界面数据
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/saveClientQuerySettingData.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> saveClientQuerySettingData(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = iGlobalConfigDialogService.saveClientQuerySettingData(request, response);
    return map;
  }

}
