package gov.df.fap.api.rule;

import gov.df.fap.util.xml.XMLData;

import java.util.List;

/**
 * 权限管理服务端接口
 * 
 * @author zhadaopeng
 * 
 */
public interface ISysRight {
  /**
   * 得到所有权限组数据
   * 
   * @return
   */
  public List getAllSysRightGroup();

  /**
   * 获取右边树的列表 新  add by yanyga 
   * 2017-04-07 10:40
   * @param ruleid
   * @return
   */
  public List getMainInfoTreeByRuleIdNew(String ruleid);

  public List getAllSysApp();

  /**
   * 根据权限组ID得到权限组数据
   * 
   * @return
   */
  public List getSysRightGroup(String right_group_id);

  /**
   * 根据权限组ID得到权限组TYPE数据
   * 
   * @return
   */
  public List getSysRightGroupType(String right_group_id);

  /**
   * 根据权限组ID得到权限组明细数据
   * 
   * @return
   */
  public List getSysRightGroupDetail(String right_group_id);

  /**
   * 得到所有启用的基础要素
   * 
   * @return
   */
  public List getEnabledEle(String setYear);

  /**
   * 得到基础数据列表
   * 
   * @return
   * @deprecated
   */
  public List getEleByRight(String eleTable);

  /**
   * 得到所有角色数据
   * 
   * @return
   */
  public List getRoles();

  /**
   * 通过条件得到所有权限组信息数据
   * 
   * @return
   */
  public List getRightGroupByCondition(String condition);

  /**
   * 保存权限组数据
   * 
   * @param xmlData
   */
  public String saveOrUpdateSysRight(XMLData xmlData) throws Exception;

  /**
   * 删除权限组数据
   * 
   * @param
   */
  public String deleteSysRight(String roleId, String rightGroupId, String right_type);

  /*
   * update daiwei 20070331 start
   */
  /**
   * 得到主范围的信息
   * @param ruleid
   * @return List
   */
  public List getMainRightInfoByRuleId(String ruleid);

  /**
   * 得到追加范围的信息
   * @param ruleid
   * @return List
   */
  public List getSuperAdditionRightInfoByRuleId(String ruleid);

  /**
   * 得到排除范围的信息
   * @param ruleid
   * @return List
   */
  public List getMinusRightInfoByRuleId(String ruleid);

  /**
   * 判断是否是全部权限
   */
  public boolean isAllRight(String ruleid);

  /*
   * update daiwei 20070331 end
   */

  public List getMainRightInfoTreeByRuleId(String ruleid);
  
  /**
   * 得到要素配置数据
   * 
   * @return List
   */
  public List getElementByCondition(String condition);
  
}
