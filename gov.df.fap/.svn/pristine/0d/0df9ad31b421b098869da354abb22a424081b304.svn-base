package gov.df.fap.api.portal;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.client.Service;

public interface IPortalService {

  /**
   * 获取年度、区划
   */
  Map<String, Object> getYearRgcode(HttpServletRequest req, HttpServletResponse resp);

  /**
   * 初始化index页
   * @throws Exception 
   */
  Map<String, Object> initIndex(HttpServletRequest req, HttpServletResponse resp) throws Exception;

  /**
   * 根据用户角色获取菜单
   */
  Map<String, Object> getMenuByRole(HttpServletRequest req, HttpServletResponse resp);
  
  /**
   * 修改密码
   */
  Map<String, Object> registerPwd(HttpServletRequest req, HttpServletResponse resp);
  
  /**
   * 密码修改时输入原始密码错误记录
   */
  Map<String, Object> registerPwdWrongRecord(HttpServletRequest  req, HttpServletResponse resp);
  
  /**
   * 切换角色(弹出用户当前角色列表)
   */
  Map<String, Object> switchRole(HttpServletRequest req, HttpServletResponse resp);
  
  /**
   * 切换角色(重新加载userdto)<br>
   * 重置tokenid
   */
  Map<String, Object> switchRoleConfirm(HttpServletRequest req, HttpServletResponse resp);
  
  /**
   * 获取当前用户选定的常用操作
   */
  Map<String, Object> getCommonOperation(HttpServletRequest req, HttpServletResponse resp);
  
  /**
   * 获取全部常用操作
   */
  Map<String, Object> getAllCommonOperation(HttpServletRequest req, HttpServletResponse resp);
  
  /**
   * 获取全部常用操作
   */
  Map<String, Object> addToCommonOperation(HttpServletRequest req, HttpServletResponse resp);
  
  /**
   * 获取全部常用操作
   */
  Map<String, Object> removeCommonOperation(HttpServletRequest req, HttpServletResponse resp);
  
  /**
   * 获取待办事项
   */
  Map<String, Object> getDealingThing(HttpServletRequest req, HttpServletResponse resp);
  
  /**
   * 获取预算待办事项
   */
  Service service1 = new Service();
  List getBudgetTask(HttpServletRequest req, HttpServletResponse resp);
  /**
  
  /**
   * 获取年度
   */
  Map<String, Object> getUserSetyear(HttpServletRequest req, HttpServletResponse resp);

  /**
   * 获取区划
   */
  Map<String, Object> getUserRgcode(HttpServletRequest req, HttpServletResponse resp);
  
  /**
   * 获取区划确认
   */
  Map<String, Object> switchRgcodeConfirm(HttpServletRequest req, HttpServletResponse resp);
  
  
  /**
   * 获取支出进度情况，dubbo
   */
  Map<String, Object> getPayProgress(HttpServletRequest req, HttpServletResponse resp);
  
  /**
   * 获取预算指标
   */
  Map<String, Object> getBudget(HttpServletRequest req, HttpServletResponse resp);
  
  /**
   * 获取财政监控
   */
  Map<String, Object> getFundmonitor(HttpServletRequest req, HttpServletResponse resp);

  /**
   * 获取财政监控
   */
  Map<String, Object> getAllCompony(HttpServletRequest req, HttpServletResponse resp);
  
  
}
