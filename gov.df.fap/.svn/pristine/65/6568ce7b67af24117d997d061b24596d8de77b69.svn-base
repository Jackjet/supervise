package gov.df.fap.service.rule;

import gov.df.fap.api.dictionary.interfaces.IControlDictionary;
import gov.df.fap.api.rule.IRuleConfigure;
import gov.df.fap.bean.control.FAssistObjectDTO;
import gov.df.fap.bean.rule.dto.RightGroupDTO;
import gov.df.fap.bean.rule.dto.RuleDTO;
import gov.df.fap.bean.rule.entity.RuleEntity;
import gov.df.fap.bean.sysdb.SysRoleRule;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 规则组件客户端操作类
 * 
 * @author 
 * 
 */
@Service
public class UIRuleOperation {

  @Autowired
  private IRuleConfigure sysRuleBean;

  /**
   * 构造函数
   */
  public UIRuleOperation() {
  }

  /**
   * 调用数据字典得到基础数据列表
   * 
   * @return
   */
  public List getEleByRight(String eleTable, String condition_sql) throws Exception {

    List data = new ArrayList();

    if (eleTable != null) {
      IControlDictionary dictionary = (IControlDictionary) SessionUtil.getOffServerBean("sys.controlDictionaryService");
      FPaginationDTO page = new FPaginationDTO();
      page.setCurrpage(-1);
      page.setPagecount(10);
      data = dictionary.findEleValuesAsObj(eleTable, page, false, "", null, condition_sql
        + " and enabled=1 order by chr_code", FAssistObjectDTO.class);

    }

    return data;
  }

  /**
   * 得到所有角色数据
   * 
   * @return
   */
  public List getAllRoles() {
    return sysRuleBean.getAllRoles();
  }

  /**
   * 得到所有子系统
   * 
   * @return
   */
  public List getAllSysApp() {
    return sysRuleBean.getAllSysApp();
  }

  /**
   * 得到所有角色启用的基础要素
   * 
   * @return
   */
  public ArrayList getRoleEnabledEle(String setyear) {
    return sysRuleBean.getRoleEnabledEle(setyear);
  }

  /**
   * 得到所有启用的基础要素
   * 
   * @return
   */
  public ArrayList getAllEnabledEle(String setYear) {
    return sysRuleBean.getAllEnabledEle(setYear);
  }

  /**
    * 保存规则数据
    * 
    * @param xmlData
    */
  public void saveOrUpdateRule(RuleDTO xmlData) throws Exception {
    sysRuleBean.saveOrUpdateRule(xmlData);
  }

  /**
   *  要素定值规则：保存规则数据
   * 
   * @param xmlData
   */
  public void saveAndUpdateRule(String ele_value, String ele_code, String ele_name, String rule_id, String ele_rule_id,
    RuleEntity xmlData,String is_relation_ele) throws Exception {
    sysRuleBean.saveAndUpdateRule(ele_value, ele_code, ele_name, rule_id, ele_rule_id, xmlData,is_relation_ele);
  }

  /**
   * 保存角色与规则关联数据
   * 
   * @param xmlData
   */
  public void saveOrUpdateRoleRule(SysRoleRule xmlData, String role_new_right_type) throws Exception {
    sysRuleBean.saveOrUpdateRoleRule(xmlData, role_new_right_type);
  }

  /**
   * 得到规则DTO（界面DTO）
   * 
   * @param 
   */
  public RuleDTO getRuleDto(String ruleid) throws Exception {
    return sysRuleBean.getRuleDto(ruleid);
  }

  /**
   * 校验权限编码是否存在
   * yanyga 2017-04-14
   * @param 
   */
  public List checkRightCodeExist(String ruleCode) throws Exception {
    return sysRuleBean.checkRightCodeExist(ruleCode);
  }

  /**
   * 通过角色ID得到规则ID
   * 
   * @param 
   */
  public String getRuleIdByRoleId(String roleid) throws Exception {
    return sysRuleBean.getRuleIdByRoleId(roleid);
  }

  /**
   * 根据传入的权限组信息得到角色的权限类型
   * 
   * @param tableData
   */
  public int getRightType(List tableData) {
    Iterator tableD = tableData.iterator();
    int return_str = 1;

    if (tableD.hasNext()) {
      while (tableD.hasNext()) {
        RightGroupDTO rowD = new RightGroupDTO();
        rowD = (RightGroupDTO) tableD.next();
        if (rowD.getRIGHT_GROUP_DESCRIBE().toString().equals("003")) {
          if (rowD.getRIGHT_TYPE() == 0) {
            return_str = 2;
            return return_str;
          } else {
            return_str = 1;
          }
        } else {
          if (rowD.getRIGHT_TYPE() == 0) {
            return_str = 0;
          }
        }
      }
    } else {
      return_str = 2;
    }
    return return_str;
  }

  /**
   * 删除规则数据
   * 
   * @param
   */
  public void deleteRule(String ruleid) throws Exception {
    sysRuleBean.deleteRule(ruleid);
  }

  /**
   * 删除角色与规则的关联数据
   * 
   * @param
   */
  public void deleteRoleRule(String roleid, String userid) throws Exception {
    sysRuleBean.deleteRoleRule(roleid, userid);
  }

  /**
   * 通过规则ID得到预览数据
   * 
   * @param 
   */
  public XMLData getDisplayData(String ruleid) throws Exception {
    return sysRuleBean.getDisplayData(ruleid);
  }

  /**
   * 通过规则DTO得到预览数据
   * 
   * @param 
   */
  public XMLData getDisplayDataByRuleData(RuleDTO ruleData) throws Exception {
    return sysRuleBean.getDisplayDataByRuleData(ruleData);
  }

  /**
   * 通过RoleId来得到预览数据
   * 
   * @return
   */
  public XMLData getDisplayDataByRoleId(String roleid) throws Exception {
    return sysRuleBean.getDisplayDataByRoleId(roleid);
  }

  /**
   * 通过rule_id来得到是否做过高级设置
   * 
   */
  public boolean isAdvanced(String ruleid) throws Exception {
    return sysRuleBean.isAdvanced(ruleid);
  }

  /**
   * 找到user_role_rule表是否已启用的该规则
   */
  public boolean isUsedInUserRoleRule(String ruleid) throws Exception {
    return sysRuleBean.isUsedInUserRoleRule(ruleid);
  }

  /**
   * 找到user_rule表是否已启用的该规则
   */
  public boolean isUsedInUserRule(String ruleid) throws Exception {
    return sysRuleBean.isUsedInUserRule(ruleid);
  }

  /*
   * add daiwei 20070331 start
   */
  /**
   * 得到所有已启用权限
   */
  public List getAllEnabledRule() throws Exception {
    return sysRuleBean.getAllEnabledRule();
  }

  /**
   * 判断该权限是否已存在 
   * @param ruleCode
   * @return
   * @throws Exception
   */
  public boolean isExist(String ruleCode) throws Exception {
    return sysRuleBean.isExist(ruleCode);
  }

  /*
   * add daiwei 20070331 end
   */

  /**
   * add by wanghongtao 获取表的字段
   */
  public List getAllFieldByTableName(String tableName) throws Exception {
    return sysRuleBean.getAllFieldByTableName(tableName);
  }
}
