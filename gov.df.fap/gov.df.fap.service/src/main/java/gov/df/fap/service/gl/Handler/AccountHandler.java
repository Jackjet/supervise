package gov.df.fap.service.gl.Handler;

import gov.df.fap.bean.gl.dto.JournalDTO;
import gov.df.fap.service.gl.balance.BalanceGenResult;
import gov.df.fap.service.gl.balance.matcher.AbstractBalanceMatcher;

import java.math.BigDecimal;
import java.util.List;

/**
 * 记账处理器接口.
 * @author 
 * @version  2017-03-06
 */
public interface AccountHandler {

	/**
	 * 生成临时额度对象及形成追溯对象列表
	 * @param accelerator TODO
	 * @param journals
	 * @return
	 */
	public BalanceGenResult generateBalance(List existsJournals, List inputjournals, AbstractBalanceMatcher balanceMatcher);
	
	/**
	 * 关联日志临时表,查询已存在的日志
	 * @return
	 */
	public List findExistsJournalsWithCache();
	
	/**
	 * 保存追溯对象
	 * @param balanceTracers
	 */
	public void tracerBalance(List balanceTracers);
	
	/**
	 * 关联日志临时表保存日志,可以是insert, update操作
	 */
	public void storeJournalWithCache();
	
	/**
	 * 该操作的终审状态
	 * @return
	 */
	public int getIsEnd();
	
	/**
	 * 该操作的操作ID
	 * @return
	 */
	public long getActionId();
	
	/**
	 * 该操作的有效状态
	 * @return
	 */
	public int getIsValid();
	
	/**
	 * 历史明细与现传入明细之间对比
	 * @param existsjournal
	 * @param inputJournal
	 */
	public void checkJournal(JournalDTO existsjournal, JournalDTO inputJournal);
	
	/**
	 * 校验两个日志列表
	 * @param existsJournals TODO
	 * @param inputJournals TODO
	 */
	public void checkJournalWithCache(List existsJournals, List inputJournals);
	
	/**
	 * 返回此种处理的金额
	 * @param money
	 * @return
	 */
	public BigDecimal journalMoney(String money);

	/**
	 * 记账成功后，回写记账dto数据 特殊属性
	 * @param inputList
	 */
	public void handleAfterProperties(List inputList);
}
