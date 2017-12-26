package gov.df.fap.api.globalConfigDialog;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IGlobalConfigDialogService {

  Map<String, Object> updateFilter(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> updateCommonFilter(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> getElementTree(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> getMenuTree(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> filterInit(HttpServletRequest request, HttpServletResponse response);

  /**
   * 初始化（批量录入规则）下拉列表
   * @param request
   * @param response
   * @return
   */
  public Map<String, Object> initDropDownList(HttpServletRequest request, HttpServletResponse response);

  /**
   * （初始化）基本的配置信息
   * @param request
   * @param response
   * @return
   */
  Map<String, Object> initBasePageInfo(HttpServletRequest request, HttpServletResponse response);

  /**
   * （保存）公务卡页面信息
   * @param request
   * @param response
   * @return
   */
  Map<String, Object> savePayCardInfo(HttpServletRequest request, HttpServletResponse response);

  /**
   * （保存）配置信息
   * @param request
   * @param response
   * @return
   */
  Map<String, Object> saveConfig(Map configdataMap);

  /**
   * 保存工资接口配置数据
   * @param request
   * @param response
   * @return
   */
  Map<String, Object> saveSalaryInterface(HttpServletRequest request, HttpServletResponse response);

  /**
   * 根据billtype_class得到类型
   * @param type
   * @return
   */
  public List loadBilltype(String type);

  /**
   * 保存常规接口配置数据
   * @param request
   * @param response
   * @return
   */
  Map<String, Object> saveConventionalInterface(HttpServletRequest request, HttpServletResponse response);

  /**
   * 保存银行配置数据
   * @param request
   * @param response
   * @return
   */
  Map<String, Object> saveBankSettingData(HttpServletRequest request, HttpServletResponse response);

  /**
   * 保存政府采购接口配置数据
   * @param request
   * @param response
   * @return
   */
  Map<String, Object> saveGovProcurementInterfaceData(HttpServletRequest request, HttpServletResponse response);

  /**
   * 保存用户参数-支付管理配置数据
   * @param request
   * @param response
   * @return
   */
  Map<String, Object> savePayManagementData(HttpServletRequest request, HttpServletResponse response);

  /**
   * 初始化客户端查询参数配置界面数据
   * @param request
   * @param response
   * @return
   */
  Map<String, Object> initClientQuerySetting(HttpServletRequest request, HttpServletResponse response);

  /**
   * 保存用户参数-支付管理配置数据
   * @param request
   * @param response
   * @return
   */
  Map<String, Object> saveClientQuerySettingData(HttpServletRequest request, HttpServletResponse response);
}
