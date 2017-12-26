package gov.df.fap.api.rule;

import gov.df.fap.bean.rule.dto.RuleDTO;
import gov.df.fap.bean.rule.entity.RuleEntity;
import gov.df.fap.bean.sysdb.SysRoleRule;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.List;

/**
 * 规则组件配置时接口
 * 
 * @version 1.0
 * @author jusitn
 * @since java 1.6
 * 
 */

public interface IRuleConfigure {
  /**
   * 范围查重
   * 
   * @param ruleid-------------
   * @return list--------------返回范围重复笛卡儿集
   * @throws Exception---------错误信息
   */
  public List checkDuplication(List ruleids) throws Exception;

  /**
   * 范围查重
   * 
   * @param ruleid-------------
   * @return list--------------返回范围重复笛卡儿集
   * @throws Exception---------错误信息
   */
  public XMLData checkDuplication(String ruleid1, String ruleid2) throws Exception;

  /**
   * 范围查漏
   * 
   * @param ruleids------------
   * @return list--------------返回范围遗漏笛卡儿集
   * @throws Exception---------错误信息
   */
  public XMLData checkMiss(List ruleids) throws Exception;

  /**
   * 得到所有启用的基础要素
   * 
   * @return
   */
  public ArrayList getRoleEnabledEle(String setYear);

  /**
   * 得到所有启用的基础要素
   * 
   * @return
   */
  public ArrayList getAllEnabledEle(String setYear);

  /**
   * 得到所有角色数据
   * 
   * @return
   */
  public List getAllRoles();

  /**
   * 得到所有子系统
   * 
   * @return
   */
  public List getAllSysApp();

  /**
   * 保存规则数据
   * 
   * @param xmlData
   */
  public void saveOrUpdateRule(RuleDTO xmlData) throws Exception;

  /**
   * 要素定值规则：保存和修改规则
   * @param ele_value
   * @param ele_code
   * @param ele_name
   * @param rule_id
   * @param ele_rule_id
   * @param xmlData
   * @throws Exception
   */
  public void saveAndUpdateRule(String ele_value, String ele_code, String ele_name, String rule_id, String ele_rule_id,
    RuleEntity xmlData,String is_relation_ele) throws Exception;

  /**
   * 保存角色与规则关联数据
   * 
   * @param xmlData
   */
  public void saveOrUpdateRoleRule(SysRoleRule xmlData, String role_new_right_type) throws Exception;

  /**
   * 删除规则数据
   * 
   * @param 
   */
  public void deleteRule(String ruleid) throws Exception;

  /**
   * 删除用户角色与规则的关联数据
   * 
   * @param 
   */
  public void deleteRoleRule(String roleid, String userid) throws Exception;

  /**
   * 通过规则ID得到规则DTO（界面DTO）
   * 
   * @param 
   */
  public RuleDTO getRuleDto(String ruleid) throws Exception;

  /**
   * 通过角色ID得到规则ID
   * 
   * @param 
   */
  public String getRuleIdByRoleId(String roleId) throws Exception;

  /**
   * 通过规则ID得到预览数据
   * 
   * @param 
   */
  public XMLData getDisplayData(String ruleid) throws Exception;

  /**
   * 通过规则DTO得到预览数据
   * 
   * @param 
   */
  public XMLData getDisplayDataByRuleData(RuleDTO ruleData) throws Exception;

  /**
   * 通过RoleId来得到预览数据
   * 
   * @return
   */
  public XMLData getDisplayDataByRoleId(String roleid) throws Exception;

  /**
   * 通过rule_id来得到是否做过高级设置
   * 
   */
  public boolean isAdvanced(String rule_id) throws Exception;

  /**
   * 找到user_role_rule表是否已启用的该规则
   */
  public boolean isUsedInUserRoleRule(String ruleid) throws Exception;

  /**
   * 找到user_rule表是否已启用的该规则
   */
  public boolean isUsedInUserRule(String ruleid) throws Exception;

  /*
   * add daiwei 20070331 start
   */
  /**
   * 得到所有已启用权限
   * @return
   * @throws Exception
   */
  public List getAllEnabledRule() throws Exception;

  /**
   *判断该权限是否已存在 
   */
  public boolean isExist(String ruleCode) throws Exception;

  /*
   * add daiwei 20070331 end
   */

  /**
   * add by wanghongtao
   */
  public List getAllFieldByTableName(String tableName) throws Exception;

  /**
   * 判断权限编码是否存在 
   * yanyga 2017-04-14
   * @param ruleCode
   * @return
   */
  public List checkRightCodeExist(String ruleCode);

}
