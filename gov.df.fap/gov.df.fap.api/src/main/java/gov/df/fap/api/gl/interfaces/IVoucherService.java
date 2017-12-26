/*
 * @(#)IVoucherService	1.0 09/06/06
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.api.gl.interfaces;

import java.util.List;

import gov.df.fap.bean.gl.configure.BusVouType;
import gov.df.fap.bean.gl.dto.Condition;
import gov.df.fap.bean.gl.dto.ConditionObj;
import gov.df.fap.bean.gl.dto.FCtrlRecordDTO;
import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.bean.util.FBaseDTO;
import gov.df.fap.bean.util.FPaginationDTO;

/**
 * 记账模板组件接口DTO实现类, 重构该接口及其实现时应该考虑用Bridge模式和Adaptor模式.
 * <p>用Bridge:入账接口有不同实现,而且可能会在运行期切换,所有将实现和接口用Bridge分开,
 * 再加上用cglib动态字节码生成来做到方法拦截,在每个方法调用前判断桥接到哪个入账接口上.</p>
 * <p>用Adaptor:Adaptor,由于龙图接口与方正接口不同,所以通过编写Adaptor(适配器,
 * <code>LtService</code>实现<code>IVoucherService</code>)来屏蔽这种差异.</p>
 * @version 1.0
 * @author 
 * @author 
 * @since java 1.6
 * @version 2007-3-24
 */
public interface IVoucherService 
{	
	//----------------------------------入账接口
	
	/**
	 * 新增入帐接口,根据传入的交易凭证信息进行入账,实现额度控制
	 * @param voucherDTO 传入的交易凭证信息体
	 * @return 入账后的交易凭证信息对象
	 * @throws Exception 入账中的错误或者校验失败的地方
	 */
	public FVoucherDTO save(FVoucherDTO voucherDTO) throws Exception;

	/**
	 * 批量新增入帐接口,根据传入的交易凭证信息 List 进行入账,实现额度控制
	 * @param voucherDTOList 传入的交易凭证信息体List
	 * @return 入账后的交易凭证信息对象List
	 * @throws Exception 入账中的错误或者校验失败的地方
	 */
	public List saveBatch(List voucherDTOList) throws Exception;

	/**
	 * 修改入帐接口,根据传入的交易凭证信息 进行入账,实现额度控制
	 * @param voucherDTO 传入的交易凭证信息
	 * @return 入账后的交易凭证信息对象
	 * @throws Exception 入账中的错误或者校验失败的地方
	 */
	public FVoucherDTO update(FVoucherDTO voucherDTO) throws Exception;
	
	/**
	 * 批量修改入帐接口,根据传入的交易凭证信息 List 进行入账,实现额度控制
	 * @param voucherDTOList 传入的交易凭证信息List
	 * @return 入账后的交易凭证信息对象List
	 * @throws Exception 入账中的错误或者校验失败的地方
	 */
	public List updateBatch(List voucherDTOList) throws Exception;
	
	/**
	 * 删除入帐接口,根据传入的交易凭证信息进行入账,实现额度控制
	 * @param voucherDTO 传入的交易凭证信息
	 * @return  boolean 是否成功
	 * @throws Exception 入账中的错误或者校验失败的地方
	 */
	public FVoucherDTO delete(FVoucherDTO voucherDTO) throws Exception;
	
	/**
	 * 批量删除入帐接口,根据传入的交易凭证信息 List 进行入账,实现额度控制
	 * @param voucherDTOList 传入的交易凭证信息体List
	 * @return  boolean 是否成功
	 * @throws Exception 入账中的错误或者校验失败的地方
	 */
	public List deleteBatch(List voucherDTOList) throws Exception;
	
	/**
	 * 终审入帐接口,根据传入的交易凭证信息进行入账,实现额度控制
	 * @param voucherDTO 传入的交易凭证信息体
	 * @return 入账后的交易凭证信息对象
	 * @throws Exception 入账中的错误或者校验失败的地方
	 */
	public FVoucherDTO audit(FVoucherDTO voucherDTO) throws Exception;
	
	/**
	 * 批量终审入帐接口,根据传入的交易凭证信息 List 进行入账,实现额度控制
	 * @param voucherDTOList 传入的交易凭证信息体List
	 * @return 入账后的交易凭证信息对象List
	 * @throws Exception 入账中的错误或者校验失败的地方
	 */
	public List auditBatch(List voucherDTOList) throws Exception;

	/**
	 * 撤消终审入帐接口,根据传入的交易凭证信息进行入账,实现额度控制
	 * @param voucherDTO 传入的交易凭证信息体
	 * @return 入账后的交易凭证信息对象
	 * @throws Exception 入账中的错误或者校验失败的地方
	 */
	public FVoucherDTO cancelAudit(FVoucherDTO voucherDTO) throws Exception;
    
    /**
     * 作废接口,根据传入的交易凭证信息进行入账,实现额度控制
     * @param voucherDTO 传入的交易凭证信息体
     * @return 入账后的交易凭证信息对象
     * @throws Exception 入账中的错误或者校验失败的地方
     */
    public FVoucherDTO invalid(FVoucherDTO voucherDTO) throws Exception;
	
    /**
     * 作废接口,根据传入的交易凭证信息进行入账,实现额度控制
     * @param voucherDTO 传入的交易凭证信息体
     * @return 入账后的交易凭证信息对象
     * @throws Exception 入账中的错误或者校验失败的地方
     */
    public List invalidBatch(List voucherDTOList) throws Exception;

	/**
	 * 批量撤消终审入帐接口,根据传入的交易凭证信息 List 进行入账,实现额度控制
	 * @param voucherDTOList 传入的交易凭证信息体List
	 * @return 入账后的交易凭证信息对象List
	 * @throws Exception 入账中的错误或者校验失败的地方
	 */
	public List cancelAuditBatch(List voucherDTOList) throws Exception;	
	
	//------------------------------额度查询接口
	
	/**
	 * 同时更新fromctrlid 的终审操作。
	 * @param voucherDTOList
	 * @return
	 * @throws Exception
	 */
	public List resetFromctrlidAudit(List voucherDTOList) throws Exception;
	
	/**
	 * 获取具体交易凭证类型对应控制(额度)汇总信息
	 * @param billType 交易凭证类型编码或ID
	 * @param page 分页对象
	 * @param plusDetailSQL 过滤条件
	 * @param founderVoucherDTO
	 * @return
	 * @throws Exception 
	 */
	public List findSumCtrlRecords(String billType,
			FPaginationDTO page,
			Condition plusDetailSQL) throws Exception;
	
	/**
	 * 简单额度查询接口,所有要素信息都精确匹配
	 * @param billType 交易凭证类型编码或ID
	 * @param page 分页对象
	 * @param plusDetailSQL 过滤条件
	 * @return
	 * @throws Exception 
	 */
	public List simpleFindSumCtrlRecords(String billType, FPaginationDTO page,
			Condition plusDetailSQL) throws Exception;

	/**
	 * 获取具体交易凭证类型对应控制(额度)汇总信息
	 * 
	 * @param account 科目编码
	 * @param page
	 *            分页对象
	 * @param plusDetailSQL
	 *            汇总前过滤条件
	 * @param plusSumSQL
	 *            汇总过滤条件
	 * @return 控制表信息DTO对象列表
	 * @throws Exception
	 */
	public List getSumCtrlRecordsByAccount(String accountCode,
			FPaginationDTO page, Condition condition) throws Exception;
	
	/**
	 * 根据交易凭证类型获取所有的汇总类型，支持龙图衔接   
	 * @param billType  交易凭证Code
	 * @param plusSQL   附加过滤SQL
	 * @return 科目列表，每一个科目为一个Map，包含编码、名称等。
	 * @throws Exception
	 */
	public List  getAccountsByBillTypeCode(String billType,Condition plusSQL)throws Exception;
	

	/**
	 * 根据指定信息获取一条控制表信息
	 * @param ctrlId 控制表id
	 * @param 科目编码
	 * @param set_month 月份信息
	 * @return 控制表信息DTO对象
	 * @throws Exception 
	 */
	public FCtrlRecordDTO getCtrlRecord(String ctrlId,String accountCode,int set_month) throws Exception;
	
	/**
	 * 获取具体交易凭证类型对应控制(额度)明细信息
	 * @param billType 交易凭证类型编码或ID
	 * @param page 分页对象
	 * @param plusSQL 过滤条件
	 * @return 控制表信息DTO对象列表
	 * @throws Exception
	 */
	public List getCtrlRecords(String billType,FPaginationDTO page,Condition plusSQL) throws Exception;
	
	/**
	 * 获取科目余额明细信息
	 * @param accountCode 科目
	 * @param page 分页对象
	 * @param plusSQL 过滤条件
	 * @return
	 */
	public List getCtrlRecordsByAccount(String accountCode, FPaginationDTO page, Condition plusSQL);
	
	/**
	 * 返回额度下游业务明细信息（如从指标额度查找相关计划明细）
	 * @param ctrlId 控制表ID
	 * @param subAcctId 下级会计客户ID
	 * @param set_month 截至月份
	 * @return 控制表信息DTO对象列表
	 */
	public List getTargetVoucher(String ctrlId, int set_month);
	
	/**
	 * 追回去向额度
	 * @param vouId 业务明细ID
	 * @param billTypeCode 交易凭证类型
	 * @return 去向额度列表
	 */
	public List getTargetBalance(String vouId, String billTypeCode);
	
	/**
	 * 追溯额度的来源明细信息（如根据指标额度查找指标明细来源）
	 * @param ctrlId 控制表ID
	 * @param subAcctId 下级会计客户ID
	 * @param set_month 截至月份
	 * @return 控制表信息DTO对象列表
	 */
	public List getSourceVoucher(String ctrlId, int set_month);
	
	/**
	 * 追溯凭证的来源额度
	 * @param vouId
	 * @param billTypeCode
	 * @return
	 */
	public List getSourceBalance(String vouId, String billTypeCode);

	//------------------------其它
	
	/**
	 * 根据记账模板对象获取ccid
	 * @param inputFVoucherDto 输入业务信息
	 * @return 结果ccid
	 * @throws Exception 异常
	 */
	public String getCCID(FVoucherDTO inputFVoucherDto) throws Exception;
	
	/**
	 * 根据传入的ConditionObj获取对应的额度查询条件
	 * @param billTypeCode 交易凭证编码
	 * @param obj 条件对象
	 * @return 额度查询条件
	 * @throws Exception
	 */
	public String getCondition(String billTypeCode,ConditionObj obj)throws Exception;	

	/**
	 * 根据版本号修改额度要素信息
	 * @param billTypeCode 交易凭证类型编码
	 * @param ctrlid 额度ID
	 * @param detailCcid 明细ccid
	 * @param version 版本号
	 * @return 是否修改成功
	 * @exception 修改异常
	 */
	public boolean updateCtrlForBudget(String billTypeCode,String ctrlid,String detailCcid,String version)throws Exception;
	
	/**
	 * 年初数替换额度刷新接口
	 * @param accountCode 科目编码
	 * @param tableName	要刷新数据的表名
	 * @param whereStr	条件
	 */
	public void budgetFileBalanceReferesh(String accountCode, String tableName, String whereStr) throws Exception;
	
	/**
	 * 
	 * @param dto 传入的交易凭证信息体
	 * @return 入账后的交易凭证信息对象
	 * @throws Exception 入账中的错误或者校验失败的地方
	 */
	public List matchByToctrlidInvalid(List FVoucherDTOList) throws Exception;
	
	/**
	 * 根据单据类型获取记账模板
	 * @param billtypecode 单据类型编码
	 * @return
	 * @throws Exception
	 */
	public BusVouType loadBvtypeByBillType(String billtypecode) throws Exception;
	
	/**
	 * 根据单据类型中记账模板挂接科目，返回符合传入dto权限科目
	 * @param billtypeCode 单据类型编码
	 * @param ruleElements 权限要素dto
	 * @return
	 * @throws Exception
	 */
	public List findAccountsMatched(String billtypeCode, FBaseDTO ruleElements) throws Exception; 
}
