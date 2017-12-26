/*
 * $Id: ISysBillType.java 384950 2013-04-08 01:02:07Z dingyy $
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.api.sysbilltype;

import gov.df.fap.bean.system.billtype.BillTypeForm;
import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;

/**
 * 菜单管理服务端接口
 * 
 * @author a
 * @author 
 */
public interface ISysBillType {

  public List findPrintFormat(String flag, String name);

  public Map<String, Object> getsBilltypeAsselesInit() throws Exception;

  public List findAllSysBillTypes() throws Exception;

  public List findBillTypesBySysId(String sysId) throws Exception;

  public List findSysBillTypesByBilltypeClass(String billtypeClass) throws Exception;

  public List findSysBillTypesBySysName(String billtypeClass) throws Exception;

  public List findGLCoaDetails(String coa_id) throws Exception;

  public List findAllGlCoas() throws Exception;

  public List findGLCoaEleValues(String ele, String coa_id) throws Exception;

  public List findGlBusvouTypes() throws Exception;

  public List findSysTableManagers() throws Exception;

  public List findAssistSysElements() throws Exception;

  public Integer getMaxLevelNumByEleSource(String eleSource) throws Exception;

  public List findSysBilltypeAsselesByBillTypeId(String billTypeId) throws Exception;

  public Integer getSysBilltypeAsselesLevelNumByBillTypeIdEleCode(String billtypeId, String eleCode) throws Exception;

  public void saveorUpdateSysBillType(XMLData xmlData) throws Exception;

  public void deleteSysBillType(XMLData xmlData) throws Exception;

  public void deleteSysBillTypeByBillTypeId(String billTypeId) throws Exception;

  public void updateSysBilltypeAssele(String billtypeId, List newBilltypeAsseles) throws Exception;

  public List getFieldByBillType(String billType_id) throws Exception;

  public List getFieldByBillTypeViaCoaID(String billType_id, String coa_id) throws Exception;

  public List getEleRule(String condStr);

  public List getEleRuleWithField(String field, String condStr);

  public List getEleRuleAndEleValueSet(String fieldCode, String billType);

  public void insertOrUpdateEleRule(Map value) throws Exception;

  public List getRuleInfo(String ele_rule_id, int pageIndex, int pageRows);

  public int getRuleInfoTotalCount(String eleRuleID);

  public List getEleByBillType(String billType_id) throws Exception;

  public void insertOrUpdateDetailRule(String ele_value, String ele_code, String ele_name, String rule_id,
    String ele_rule_id) throws Exception;

  public List getSourceDataBySourceID(String source, String sourceID);

  public void deleteEleRule(String eleRuleID) throws Exception;

  /**
       * 通过交易凭证类型得到字段
       * 
       * @author xunyuqing
       * @param condition
       *                查询条件 默认为:and + 条件
       */
  public List getBillTypeData(String condition);

  /**
       * 通过交易凭证类型得到字段
       * 
       * @param billType_id
       *                交易凭证类型ID
       * @param coaID
       * @param sourceName
       */
  public List getFieldByCoaAndSource(String coaID, String sourceName) throws Exception;

  public void deleteEleRuleDetail(String eleRuleID, String eleValue) throws Exception;

  public List getInfoByTableName(String fields, String tableName);

  /**
       * 获取子系统信息
       * 
       * @return List（XMLData参考sys_app表中的sys_id和sys_name字段
       * @author 黄节2007年7月12日添加
       * 
       */
  public List getApp_id();

  /**
       * 获取所有业务口径
       * 
       * @return List（XMLData参考GL_COA表中的coa_id、coa_code和coa_name字段）
       * @author 黄节2007年7月12日添加
       * 
       */
  public List getCoa();

  /**
       * 获取所有单号规则
       * 
       * @return List（XMLData参考Sys_Billnorule表中的billnorule_id、billnorule_code和billnorule_name字段）
       * @author 黄节2007年7月12日添加
       * 
       */
  public List getBillnorule();

  /**
       * 获取录入视图
       * 
       * @return List（XMLData参考SYS_UIMANAGER表中的ui_id、ui_code和ui_name字段）
       * @author 黄节2007年7月12日添加
       * 
       */

  public List getUI();

  /**
       * 获取单据主/明细表
       * 
       * @param table_type
       *                int 202-主表 201－表示明细表
       * @return List
       *         （XMLData参考sys_tablemanager表中的chr_id、table_code和table_name字段）
       * @author 黄节2007年7月12日添加
       * 
       */
  public List geTable_name(int table_type);

  /**
       * 获取“来源单据明细类型”和“去向单据明细类型”
       * 
       * @param sys_id
       *                String 要获取的子系统的id
       * @return List
       *         (XMLData参考SYS_BILLTYPE表中的billtype_id、billtype_code和billtype_name)
       * @author 黄节2007年7月12日添加
       * 
       */
  public List getBilltype(String sys_id);

  public List getBilltype();

  /**
       * 获取记帐凭证类型
       * 
       * @return List
       *         (XMLData参考GL_BUSVOU_TYPE表中的vou_type_id、vou_type_code和vou_type_name)
       * @author 黄节2007年7月12日添加
       * 
       */
  public List getBusvou_type();

  /**
       * 获取录入额度控制类型
       * 
       * @return List（XMLData参考Gl_Sum_Type表中的sum_type_id、sum_type_code和sum_type_name字段）
       * @author 黄节2007年7月12日添加
       */
  public List getVou_control();

  /**
       * 获取打印格式
       * 
       * @return List（XMLData参考reportcy_manager表中的report_id、report_code和report_name字段）
       * @author 黄节2007年7月12日添加
       */
  public List getReport_id();

  public List getReport_id_ss();

  public List getReport();

  /**
   * 根据单据id获取“打印模版”的数据
   * 
   * @param billtype_id String 单据类型的id
   * 
   * @return List（XMLData,字段参考表sys_print_models）
   * @author 黄节2007年7月20日添加
   */
  public List getPrintModeData(String billtype_id);

  /**
       * 保存“打印模版”的数据
       * 
       * @param printModeData
       *                （XMLData,字段参考表sys_print_models）
       * @param billtype_id
       *                String 对应的单据类型id
       * @throws Exception
       *                 向外抛出sql错误
       * @author 黄节2007年7月23日添加
       */
  public void savePrintModeData(List printModeData, String billtype_id) throws Exception;

  /**
       * 删除“打印模版”的数据
       * 
       * @param billtype_id
       *                String 对应的单据类型id
       * @throws Exception
       *                 向外抛出sql错误
       * @author 黄节2007年7月23日添加
       */
  public void deletePrintModeData(String billtype_id) throws Exception;

  /**
       * 根据单据类型id获取单据类型所有信息
       * 
       * @param billtype_id
       *                String 对应的单据类型id
       * @throws Exception
       *                 向外抛出sql错误
       * @return XMLData (数据参考： sys_billtype表字段 ＋ assele（List
       *         XMLData－sys_billtype_assele表字段） ＋ valueset（List
       *         XMLData－sys_print_models表字段） ＋ report（List
       *         XMLData－sys_billtype_valueset表字段）)， 没有数据返回null
       */
  public XMLData getSysBilltypeById(String billtype_id) throws Exception;

  public XMLData getOneSysBilltypeById(String billtype_id) throws Exception;

  /**
       * 根据单据类型id获取单据类型基本信息
       * 
       * @param billtype_id
       *                String 对应的单据类型id
       * @throws Exception
       *                 向外抛出sql错误
       * @return XMLData (数据参考：sys_billtype表字段)，没有数据返回null
       */
  public XMLData getBasicSysBilltypeById(String billtype_id) throws Exception;

  /**
       * 根据单据类型id获取单据类型辅助要素信息
       * 
       * @param billtype_id
       *                String 对应的单据类型id
       * @throws Exception
       *                 向外抛出sql错误
       * @return List（XMLData 参考sys_billtype_assele表字段）
       */
  public List getSysBilltypeAsseleById(String billtype_id) throws Exception;

  /**
       * 根据单据类型id获取单据类型打印模版信息
       * 
       * @param billtype_id
       *                String 对应的单据类型id
       * @throws Exception
       *                 向外抛出sql错误
       * @return List（XMLData 参考sys_billtype_valueset表字段）
       */
  public List getSysBilltypeValuesetById(String billtype_id) throws Exception;

  /**
       * 根据单据类型id获取单据类型定值规则信息
       * 
       * @param billtype_id
       *                String 对应的单据类型id
       * @throws Exception
       *                 向外抛出sql错误
       * @return List（XMLData 参考sys_print_models表字段）
       */
  public List getSysBilltypeReportById(String billtype_id) throws Exception;

  public void createNewEleRuleViaExistsEleRule(String old_rule_id, String ele_rule_id, String ele_value,
    String ele_code, String ele_name) throws Exception;

  public List getRelatedBillType(String billTypeId, String ele_rule_id) throws Exception;

  /**
   * 根据eleRuleId查询要素定值规则详细信息列表
   * @param ele_rule_id
   * @return
   */
  public List getRuleInfo(String ele_rule_id) throws Exception;

  /**
   * 新增要素定值规则主表
   * @param value
   * @return 新增的要素定值规则主表的信息,以Map形式保存
   * @throws Exception
   */
  public Map<String, Object> insertEleRule(Map value) throws Exception;

  /**
   * 编辑要素定值规则主表
   * @param value
   * @return 编辑的要素定值规则主表的信息,以Map形式保存
   * @throws Exception
   */
  public Map<String, Object> updateEleRule(Map value) throws Exception;

  /**
   * 根据要素定值规则id查询要素定值规则详细表
   * @param eleRuleDetailId
   * @return
   * @throws Exception
   */
  public XMLData selectByDetailId(String eleRuleDetailId) throws Exception;

  /**
   * 新增时获取rule_id
   * @param seq
   * @return rule_id
   */
  public String generateNumberBySeq(String seq);

  /**
   * 新增
   *
   * @param form
   * @return
   */
  public void insertBillType( BillTypeForm form, String userCode) throws Exception;
  
  /**
   * 删除
   * @param form
   */
  public void delete( BillTypeForm form) throws Exception;


  /**
   * 取消规则(删除定值规则详细表和规则相关数据)
   * @param eleRuleID
   * @param eleValue
   * @param ruleId
   * @throws Exception
   */
  public void deleteEleRuleDetailAndRule(String eleRuleID, String eleValue, String ruleId) throws Exception;

  /**
   * 通过英文名查询中文名
   * @param fieldCode 英文名
   * @return
   * @throws Exception
   */
  public List getFieldNameByFieldCode(String fieldCode) throws Exception;

}