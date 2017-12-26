package gov.df.fap.service.gl.je;

import java.util.List;

public interface IJournalDao {

	/**
	 * 将传入的交易凭证批量插入临时表
	 * @param dtoList 交易凭证列表
	 * @throws Exception
	 */
	public abstract void insertJournalCache(List dtoList);
	/**
	 * 将传入的交易凭证journalid批量更新日志正式表set is_valid=is_valid 实现行级锁定避免同一笔数据做两次记账金额修改
	 * @param dtoList 交易凭证列表
	 * @throws Exception
	 */
	public abstract void lockedJournalByUpdate(List dtoList);
	/**
	 * 将日志缓存表数据写入日志正式表
	 */
	public abstract void insertJournalByCache();

	/**
	 * 将日志缓存表数据写入日志历史表
	 */
	public abstract void backUpServiceWithCache();

	/**
	 * 更新日志表及日志历史表
	 * @throws Exception
	 */
	public abstract void updateJournalWithCache();

	/**
	 * 删除日志表及日志历史表
	 * @throws Exception
	 */
	public abstract void deleteJournal() throws Exception;

	public abstract List findExistsJournalWithCache();

	public abstract List findNotExistsJournalWithCache();

	/**
	 * 删除日志缓存表数据
	 */
	public abstract void deleteJournalCache();

	/**
	 * 
	 * @param sumId
	 * @param side
	 *  	<l>BalanceTracer.CTRL_SIDE_TARGET<p>
	 *  	<l>BalanceTracer.CTRL_SIDE_SOURCE<p>
	 * @return
	 */
	public abstract List findVoucherByBalance(String sumId, int side);

}