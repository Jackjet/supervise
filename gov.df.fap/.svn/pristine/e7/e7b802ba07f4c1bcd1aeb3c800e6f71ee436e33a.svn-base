package gov.df.fap.service.gl.balance;

import gov.df.fap.api.gl.balance.RefreshBalanceHandler;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.configure.BusVouType;
import gov.df.fap.bean.gl.dto.Condition;
import gov.df.fap.bean.gl.dto.FCtrlRecordDTO;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.service.util.exceptions.gl.AlreadCloseMonthEndException;
import gov.df.fap.service.util.exceptions.gl.ExecutingMonthlyBalanceException;
import gov.df.fap.service.util.exceptions.gl.IllegalBalanceException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author 
 * @version 2017-03-03
 */
public interface IBalanceService {

	/**
	 * 保存临时额度,当额度不存在则新增一条,当额度存在则将金额叠加上去.同时校验额度合法性
	 * @param tmpRecordList 临时额度列表
	 * @param accountList 科目列表
	 * @param factory factory of balancesaver
	 * @throws SQLException 
	 * @throws Exception 额度不足
	 */
	public void saveBalance(List tmpRecordList, List accountList,BalanceSaverFactory factory) throws IllegalBalanceException, SQLException;
	
	
	/**
	 * 更新CCID被修改的额度追溯表
	 * @throws Exception
	 */
	public void reTraceCcidChangeBalance(List balanceTracers);
	
	/**
	 * 新插入额度追溯
	 * @param 追溯对象列表
	 */
	public void insertBalanceTrace(List list);
    
    /**
     * 通过日志缓存表获取额度追溯对象
     * @return
     */
    public Map findBalanceTraceWithCache();
    
    /**
     * 删除额度缓存表数据
     * @throws Exception
     */
    public void deleteBalanceCache() throws Exception ;
    
//    /**
//     * 校验额度合法性  
//     * @param accountList 科目列表
//     * @return
//     */
//    public void validBalance(BusVouAccount account, List tempCtrlList) throws Exception;
//     
    /**
     * 
     * @param billType
     * @param page
     * @param plusDetailSQL
     * @return
     */
    public List findSumCtrlRecords(BusVouAccount account, FPaginationDTO page, Condition plusDetailSQL);
    
    /**
     * 读取单条额度明细
     * @return
     */
    public FCtrlRecordDTO loadBalance(String sumId, BusVouAccount account, Condition condition);
    
    /**
     * 读取单条额度明细
     * @return
     */
    public List findBalance(BusVouAccount account, Condition condition);
    
    /**
     * 额度查询服务
     * @param bvType
     * @param account
     * @param vouId
     * @param side
     * @return
     */
    public List findBalanceByJournal(BusVouType bvType, BusVouAccount account, String vouId, int side);
    
    /**
     * 是否已执行了本月的月结.
     * @param month 月结月份
     * @return 如果为true则表明已做月结;如果为false则表明没做
     */
    public boolean alreadyCloseMonthEnd(int month);
    
    /**
     * 手工执行月结任务.
     * @throws ExecutingMonthlyBalanceException 如果在开始执行月结之前发现已有线程在执行月结,则抛出此异常
     * @throws AlreadCloseMonthEndException 已经做过月结则抛出异常
     */
    public void closeMonthEnd() throws ExecutingMonthlyBalanceException,Exception ;
   
    /**
     * 通用额度刷新方法
     * @param account
     * @param handler
     */
    public void refreshBalance (BusVouAccount account, RefreshBalanceHandler handler) throws Exception; 
    
}
