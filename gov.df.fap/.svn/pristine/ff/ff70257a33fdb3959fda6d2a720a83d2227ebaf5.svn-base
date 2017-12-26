package gov.df.fap.api.rule;



import java.util.List;

import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.bean.util.FBaseDTO;

/**
 * 规则组件运行时接口
 * 
 * @version 1.0
 * @author 
 * @since java 1.6
 * 
 */
public interface IRule {
	/**
	 * 校验数据是否在范围内(如:记账范围，匹配处理)
	 * 
	 * @param ruleid-------------
	 * @param baseDTO------------
	 * @return true或false
	 * @throws Exception---------错误信息
	 */
	public boolean isMatch(String  ruleid, FBaseDTO baseDTO)  throws Exception ;
	
	/**
	 * 获取符合范围列表(如:帐户范围，筛选列表)
	 * 
	 * @param ruleids------------
	 * @param baseDTO------------
	 * @return true或false
	 * @throws Exception---------错误信息
	 */	
	public List getMatchedRules(List  ruleids,FBaseDTO baseDTO)  throws Exception ;
	
	/**
	 * 获取符合范围列表
	 * 
	 * @param eleRuleid------------
	 * @param baseDTO------------
	 * @return List
	 * @throws Exception---------错误信息
	 */		
	public List  getMatchedELECODES(String  eleRuleid,FBaseDTO baseDTO) throws Exception ;
	
	/**
	 *对FVoucherDTO进行定值处理
	 * 
	 * @param eleRuleid 定值规则ID
	 * @param FVoucherDTO 需定值对象
	 * @param isForceWriteValue 是否强制设定第一条匹配的定值
	 * @return void
	 * @throws Exception---------错误信息
	 */	
	public void updateFVoucherDto(String eleRuleId, FVoucherDTO baseDTO,boolean isForceWriteValue) throws Exception ;
	
	/**
	 * 对一组的dto进行定值
	 * @param dtoList 需要定值的对象
	 */
	public List updateFixedValueRuleInfo(List dtoList) throws Exception;
	
	/**
	 * 对一组FVoucherDTO进行定值处理（通用）
	 * @param dtoList 需定值对象列表
	 * @param matchedList 需要定值的规则
	 * @param isForceWriteValue 是否强制设定第一条匹配的定值
	 * @return dtoList 定值后对象列表
	 * @throws 异常
	 */
	 public List getMatchedValues(List dtoList,String[] matchedList,boolean isForceWriteValue)throws Exception;

}
