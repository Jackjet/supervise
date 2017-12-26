package gov.df.fap.api.userConfig;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IUserConfigService {

  /**
   * 左侧树加载
   * 
   */
  Map<String, Object> usertree(HttpServletRequest request, HttpServletResponse response);

  /**
   * 表格加载
   * 
   */
  Map<String, Object> usergrid(HttpServletRequest request, HttpServletResponse response);

  /**
   * 机构权限树
   * 
   */
  Map<String, Object> findEleCodeByOrgtypeCode(HttpServletRequest request, HttpServletResponse response);

  /**
   * 表格加载
   * 
   */
  Map<String, Object> findrole(HttpServletRequest request, HttpServletResponse response);

  /**
   * 添加用户
   * 
   */
  Map<String, Object> addUser(HttpServletRequest request, HttpServletResponse response);

  /**
   * 新增初始化
   * 
   */
  Map<String, Object> initMessage(HttpServletRequest request, HttpServletResponse response);

  /**
   * 删除用户
   * 
   */
  Map<String, Object> deleteUser(HttpServletRequest request, HttpServletResponse response);

  /**
   * 用户锁定
   * 
   */
  Map<String, Object> locked(HttpServletRequest request, HttpServletResponse response);

  /**
   * 用户解锁
   * 
   */
  Map<String, Object> unlocked(HttpServletRequest request, HttpServletResponse response);
}
