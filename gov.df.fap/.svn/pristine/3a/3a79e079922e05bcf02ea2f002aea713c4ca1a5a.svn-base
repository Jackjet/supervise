package gov.df.fap.service.gl.balance;

import gov.df.fap.api.rule.IDataRight;
import gov.df.fap.bean.gl.balance.MonthlyBalanceDTO;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.configure.BusVouType;
import gov.df.fap.bean.gl.dto.Condition;
import gov.df.fap.bean.gl.dto.FCtrlRecordDTO;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.service.gl.core.DaoSupport;
import gov.df.fap.service.gl.core.sqlgen.PlusSql;

import java.util.List;

public interface IBalanceDao {

	public abstract void setDaoSupport(DaoSupport daoSupport);

	public abstract void setPlusSql(PlusSql plusSql);

	public abstract void setDataRight(IDataRight dataRight);

	/**
	 * 对新增及CCID改变的数据更新额度追溯表
	 * @throws Exception
	 */
	public abstract void saveBalanceTracer(List dtoList);

	/**
	 * 删除临时额度缓存表数据
	 */
	public abstract void deleteBalanceCache();

	/**
	 * 对作废或者删除的数据删除额度追溯表
	 * @throws Exception
	 */
	public abstract void deleteBalanceTrace(String vouId, String vouTypeId,
			String setYear);

	/**
	 * 保存额度到缓存表
	 * @param dtoList
	 */
	public abstract void saveBalanceCache(List dtoList);

	/**
	 * 用缓存保存累计月额度.
	 * 其余的额度保存到gl_balance中
	 * @param dtoList
	 */
	public abstract void saveSumMonthBalanceWithCache(BusVouAccount account);

	/**
	 * 用缓存表来保存累计月额度的月份明细.
	 * 具体来说,就是将缓存表中所有明细表不存在的累计月额度月份明细保存到明细表中去,
	 * 不管是将来月的明细,还是本月的明细(以前月的明细不做处理,在业务上应该不存在这样的情况).
	 */
	public abstract void saveSumMonthBalanceDetailWithCache(
			BusVouAccount account);

	/**
	 * 用缓存来保存普通月的额度
	 */
	public abstract void saveCommonBalanceWithCache(BusVouAccount account);

	public abstract void deleteBalance(BusVouAccount account,
			Condition condition);

	/**
	 * 删除额度追溯对象
	 * @param dtoList
	 */
	public abstract void deleteBalanceTracer(List dtoList);

	public abstract void execute(String sql);

	/**
	 * 根据日志缓存查询出额度追溯对象
	 * @return
	 */
	public abstract List loadTracerByJournalCache();

	/**
	 * 查询额度缓存
	 * @param exists
	 * @return
	 */
	public abstract List findBalanceIsExist(BusVouAccount account,
			boolean isExists);

	/**
	 * 查询分月额度
	 * @return
	 */
	public abstract List loadMarkingMonthBalance(BusVouAccount account);

	/**
	 * 查询累积月额度--当前月前的累积额度 
	 * @return
	 */
	public abstract List loadCalculateBeforeMonthBalance(BusVouAccount account);

	/**
	 * 查询累积月额度--当前月后的累积额度 
	 * @return
	 */
	public abstract List loadCalculateAfterMonthBalance(BusVouAccount account);

	public abstract List loadVouIdByBalance(List dtoList);

	/**
	 * 科目余额查询
	 * @param account 科目
	 * @param page 翻页对象
	 * @param condition 条件对象
	 * @return
	 * @throws Exception
	 */
	public abstract List findBalance(BusVouAccount account,
			FPaginationDTO page, Condition condition);

	/**
	 * 查询单条额度
	 * @param sumId 额度ID
	 * @param account 科目
	 * @param condition 条件
	 * @return
	 * @throws SQLException 
	 * @throws Exception 
	 */
	public abstract FCtrlRecordDTO loadBalance(BusVouAccount account,
			Condition condition);

	/**
	 * 查询明细额度, 不进行汇总
	 * @param account 科目
	 * @param condition 条件
	 * @return
	 * @throws Exception 
	 */
	public abstract List findBalance(BusVouAccount account, Condition condition);

	/**
	 * 
	 * @param bvType
	 * @param account
	 * @param vouId
	 * @param side
	 * @return
	 */
	public abstract List findBalanceByJournal(BusVouType bvType,
			BusVouAccount account, String vouId, int side);

	/**
	 * 更新累计月额度的金额
	 * @return
	 */
	public abstract int updateSumMonthBalanceMoneyWithCache(
			BusVouAccount account);

	/**
	 * 利用缓存表中的数据更新额度月份明细表中的金额.
	 * 其中既包括插入将来月的额度明细的金额,也包括更新本月及以前月的额度明细的金额.
	 * 由于balance_id标识了额度和月份,所以以balance_id关联相等即可,不需要再加入set_month来关联
	 * @return 更新的纪录行数
	 */
	public abstract int updateSumMonthBalanceDetailMoneyWithCache(
			BusVouAccount account);

	/**
	 * 利用缓存表中的数据更新普通额度的金额
	 * @return 更新的纪录行数
	 */
	public abstract int updateFromctrlidWithCache(BusVouAccount account);

	/**
	 * 利用缓存表中的数据更新普通额度的金额
	 * @return 更新的纪录行数
	 */
	public abstract int updateCommonBalanceMoneyWithCache(BusVouAccount account);

	/**
	 * 获得累计月额度某年所有月的的月结情况
	 * @param year 年度
	 * @return
	 */
	public abstract List loadMonthlyBalance(int year);

	/**
	 * 获得累计月额度的某个月的月结执行情况
	 * @param year 年度
	 * @param month 月份
	 * @return
	 */
	public abstract MonthlyBalanceDTO loadMonthlyBalanceDTO(int year, int month);

	/**
	 * 保存累计月额度的月结执行情况
	 * @param year 月结的年份
	 * @param month 月结的月份
	 * @return int,保存的记录行数
	 */
	public abstract int saveMonthlyBalanceRecord(int year, int month);

	/**
	 * 获得执行月结任务时需要月结的月份
	 * @param year
	 * @param month
	 * @return List:元素为月份(String类型)的集合
	 */
	public abstract List getNeededMonthsWhenDoMonthlyBalance(int year, int month);

	/**
	 * 将额度月份明细表中小于指定月的各种money加到额度表中对应额度上去.
	 * @param account 结转科目
	 * @param year 年份
	 * @param month 结转月份
	 */
	public abstract void updateBalanceByDetail(BusVouAccount account, int year,
			int month);

	public abstract void clearBalanceMoney(BusVouAccount account);

	/**
	 * 往额度表中插入明细表中数据
	 * @param account
	 * @param maxMonth
	 */
	public abstract void insertBalanceNotExists(BusVouAccount account,
			int maxMonth);

	public abstract void updateBalanceMonth(BusVouAccount account, int month);

	/**
	 * 填充额度临时表内的sum_id与balance_id
	 * @param account
	 * @throws SQLException 
	 */
	public abstract void fillCachedBalanceKey(BusVouAccount account);

	public abstract void fillCtrlRecordDTOKeyByCache(final List ctrlRecords);

	/**
	 * 保存转换记录
	 * @param tableName 要保存的记录表
	 * @param oldRecord	旧额度
	 * @param newRecord 新额度
	 */
	public abstract void saveTransData(String tableName, List transRecordList);

	/**
	 * 删除支付环节中核销临时数据
	 * @param tableName 核销数据表
	 * @param transTableName 替换转换表
	 */
	public void deletePayClearTmpData(String tableName, String transTableName);
	
}