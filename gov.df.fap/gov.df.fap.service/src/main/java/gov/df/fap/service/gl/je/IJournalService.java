/**
 * <p>Copyright 2007 by Digiark Software corporation,
 * All rights reserved.
 * <p>版权所有：用友政务软件有限公司
 * <p>未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 * <p>侵权者将受到法律追究。
 */
package gov.df.fap.service.gl.je;

import gov.df.fap.bean.rule.FVoucherDTO;

import java.util.List;

/**
 * @author 
 * @version 
 */
public interface IJournalService {

	/**
	 * 对传入的交易凭证作校验
	 * @param dto 交易凭证
	 * @throws Exception
	 */
	public void checkBill(FVoucherDTO dto) throws Exception;

	/**
	 * 将传入的交易凭证批量插入临时表
	 * @param dtoList 交易凭证列表
	 * @throws Exception
	 */
	public void insertJournalCache(List dtoList) throws Exception;
	
	/**
	 * 通过临时数据批量插入日志,只插入不存在的日志
	 * @throws Exception
	 */
	public void insertJournalByCache();
	
	/**
	 * 通过临时数据关联查询日志记录
	 * @param 是否查询存在数据, true查询已存在数据; false查询不存在数据
	 * @return
	 * @throws Exception
	 */
	public List findJournalWithCache(boolean isExists);
	
	/**
	 * 更新日志表及日志历史表
	 * @throws Exception
	 */
	public void updateJournalWithCache();
	
	/**
	 * 删除日志表及日志历史表
	 * @throws Exception
	 */
	public void backUpJournalWithCache() throws Exception;
	
	/**
	 * 清除日志临时数据
	 *
	 */
	public void clearJournalCache();
	
	/**
	 * 通过额度追溯信息查询明细
	 * @param sumId
	 * @param ctrlSide
	 * @return
	 */
	public List findVoucherByBalance(String sumId, int ctrlSide);
}
