package gov.df.fap.api.workflow;

import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.bean.util.FBaseDTO;
import gov.df.fap.bean.workflow.FBillTypeDTO;
import gov.df.fap.bean.workflow.FCombinationRuleDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title: 交易凭证组件接口类
 * </p>
 * <p>
 * Description:交易凭证组件服务接口类 对应设计：综合业务系统交易凭证详细设计
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015-2017 北京用友政务软件有限公司
 * </p>
 * <p>
 * Company:北京用友政务软件有限公司
 * </p>
 * 
 * @version 1.0
 * @author lwq
 * @author
 * @since java 1.6
 */
public interface IBillTypeServices {
  /**
   * 根据交易凭证ID获取交易凭证详细信息
   * 
   * @param billtypeID
   *            交易凭证ID
   * @return 交易凭证详细信息
   */
  public FBillTypeDTO getBillTypeByID(String billtypeID);

  /**
   * 根据交易凭证Code获取交易凭证详细信息
   * 
   * @param billtypeCode
   *            交易凭证Code
   * @return 交易凭证详细信息
   */
  public FBillTypeDTO getBillTypeByCode(String billtypeCode);

  /**
   * 根据附加过滤条件获取交易凭证详细信息
   * 
   * @param plusSql
   *            附加过滤条件(以 and 开始)
   * @return 交易凭证详细信息列表
   */
  public List getBillType(String plusSql);

  /**
   * 根据交易凭证ID获取交易凭证合单规则
   * 
   * @param billtypeCode
   *            交易凭证类型编号
   * @return 交易凭证合单规则对象
   */
  public FCombinationRuleDTO getBillCombination(String billtypeCode);

  /**
   * 根据交易凭证类型获取交易凭证当前编号
   * 
   * @param billTypeCode
   *            交易凭证类型编号
   * @param billInfo
   *            凭证信息对象
   * @return 编号字符串
   * @throws Exception
   *             获取交易凭证编号过程异常
   */
  public String getBillNo(String billTypeCode, Map billInfo) throws Exception;

  /**
   * add by 2017.8.3
   * 根据交易凭证类型获取交易凭证当前编号：先取编号前缀，根据编号前缀从断号表sys_billno_deleted表中查询
   * 如果断号存在，则取用断号并删除该断号
   * @param billTypeCode
   *            交易凭证类型编号
   * @param billInfo
   *            凭证信息对象
   * @return 编号字符串
   * @throws Exception
   */
  public String getDeletedBillNo(String billTypeCode, Map billInfo) throws Exception;

  /**
   * add by 2017.8.3
   * 为防止单号不连续，保存被撤销掉的单号，以备以后取用
   * @param billNoDeletedList  Map中包含billtype_code, bill_no数据
   * @throws Exception
   */
  public void saveDeletedBillNo(List delPayVoucherBills, String billTypeCode) throws Exception;

  /**
   * 根据具体单号规则编码获取单号规则信息,单例模式保证编号的正确性
   * 
   * @param billNoRuleCode
   *            单号规则编码
   * @param billInfo
   *            凭证信息对象
   * @return 编号字符串
   * @throws Exception
   *             获取交易凭证编号过程异常
   */
  public String getBillNoByBillNoRuleCode(String billNoRuleCode, Map billInfo) throws Exception;

  /**
   * 获取定值处理后的列表
   * 
   * @param billTypecode
   *            交易凭证类型编号
   * @param field_Code
   *            要素字段
   * @param baseDTO
   *            dto
   * @return 编号字符串
   * @throws Exception
   *             获取交易凭证编号过程异常
   */
  public List findEleValues(String billTypecode, String field_Code, FBaseDTO baseDTO) throws Exception;

  /**
   * 对FVoucherDTO进行定值处理
   * 
   * @param fvoucherdto
   *            dto
   * @param isForceWriteValue
   *            是否强制设定第一条匹配的定值
   * @return 编号字符串
   * @throws Exception
   *             获取交易凭证编号过程异常
   */
  public FVoucherDTO getMatchedELECODE(FVoucherDTO fvoucherdto, boolean isForceWriteValue) throws Exception;

  /**
   * 对一组FVoucherDTO进行定值处理
   * 
   * @param dtoList
   *            需定值对象列表
   * @param isForceWriteValue
   *            是否强制设定第一条匹配的定值
   * @return dtoList 定值后对象列表
   * @throws Exception
   *             获取交易凭证编号过程异常
   */
  public List getMatchedELECODEToList(List dtoList, boolean isForceWriteValue) throws Exception;

  /**
   * 通过交易凭证编号获取已设置要素规则或默认值的字段信息
   * 
   * @param billType_code
   *            交易凭证编号
   * @return 已设置要素规则或默认值的字段信息,
   *         信息中包括(要素规则id,交易凭证id,字段编码,操作类型0:指定默认值;1:规则授权,默认值,要素简称)
   *         例:billtype_id='uuid1', ele_rule_id='uuid2', default_value=null,
   *         valueset_type(操作类型)=1, ele_code=EN, field_code=EN_ID
   */
  public List getValueSetField(String billType_code);

  /**
   * 根据传入条件得到相应的编号规则项数据
   * 
   * @param condition
   *            默认格式为“and + 条件”
   * @return 编号规则项数据列表
   * @throws Exception
   *             获取交易凭证编号过程异常
   */
  public List getBillNoRule(String condition) throws Exception;

  /**
   * 对一组FVoucherDTO进行定值处理（通用）
   * 
   * @param dtoList
   *            需定值对象列表
   * @param matchedList
   *            需要定值的规则
   * @param isForceWriteValue
   *            是否强制设定第一条匹配的定值
   * @return dtoList 定值后对象列表
   * @throws Exception
   *             获取交易凭证编号过程异常
   */
  public List getMatchedValues(List dtoList, String[] matchedList, boolean isForceWriteValue) throws Exception;

  /**
   * 得到所有被关联要素编码
   * 
   * @param priCode
   *            主控要素数据
   * @return 所有被关联要素编码
   * @author lgc add
   */
  public List getAllRelationsCode(String priCode, String priEle, String secEle);

  /**
   * 根据单号规则编码使用单号（支持断号）
   * 
   * @param billTypeCode
   *            凭证类型编码
   * @param billInfo
   *            凭证信息对象
   * @param breakFlag
   *            0:只取断号 1：优先取断号 2：正常取
   * @return 单号
   * @throws Exception
   *             获取交易凭证编号过程异常
   * @author lgc
   */
  public String getBillNoDirect(String billTypeCode, Map billInfo, String breakFlag) throws Exception;

  /**
   * 单号占用确认
   * 
   * @param billTypeCode
   *            凭证类型编码
   * @param billNo
   *            单号
   * @return true:表示确认成功 False:表示该单号已经被使用，确认失败
   * @throws Exception
   *             获取交易凭证编号过程异常
   * @author lgc
   */
  public boolean confirmBillNo(String billTypeCode, String billNo) throws Exception;

  /**
   * 单号回收
   * 
   * @param billNo
   *            单号
   * @param billTypeCode
   *            凭证类型编码
   * @return 单号回收是否成功 true：回收成功 false:回收失败
   * 
   * @throws Exception
   *             获取交易凭证编号过程异常
   * @author lgc
   */
  public boolean recoveryBillNo(String billTypeCode, String billNo) throws Exception;

  public void clearCache(List list) throws Exception;

  /**
   * 根据单据类型编码查询打印模板信息
   * @param billtype_code
   * @return
   */
  public List getPrintModeDataByBilltypeCode(String billtype_code);

  /**
   * 根据单据类型获取对应的coa_id;
   * @param billtype_code
   * @return
   */
  public String getCoaIdByByBilltypeCode(String billtype_code);

}
