package gov.df.fap.service.util.gl.configure;

import gov.df.fap.bean.gl.configure.BusVouAccount;

import java.util.List;

public interface IAccountDao {

	/**
	 * 查询科目
	 * @param accountId 科目ID
	 * @return
	 */
	public abstract BusVouAccount loadAccount(String accountId);

	/**
	 * 读取科目
	 * @param accountCode 科目编码
	 * @return
	 */
	public abstract BusVouAccount loadAccountByCode(String accountCode);

	/**
	 * 去处所有科目信息
	 * 数据源是vw_gl_account,若取出缺少数据项,更改此视图
	 * @return
	 */
	public abstract List allAccount();

	/**
	 * 保存科目信息
	 * @param bvAccount
	 */
	public abstract void saveAccount(BusVouAccount bvAccount);

	/**
	 * 保存科目信息,不补充辅助信息
	 * @param bvAccount
	 */
	public abstract void saveAccountByOrigin(BusVouAccount bvAccount);

	/**
	 * 根据传入科目的id更新传入的科目
	 * @param bvAccount
	 */
	public abstract void updateAccount(BusVouAccount bvAccount);

	/**
	 * 删除传入的科目信息
	 * @param bvAccount
	 */
	public abstract void deleteAccount(BusVouAccount bvAccount);

	public abstract void deleteAllVwGlAccount();

	/**
	 * 删除所有科目信息
	 */
	public abstract void deleteAllAccount();

}