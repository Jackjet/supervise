package gov.df.fap.api.gl.balance;

import java.util.List;

/**
 * 科目余额处理器
 * @author Administrator
 *
 */
public interface PeriodBalanceSaver {

	/**
	 * 处理保存额度,对于不同类型的科目,处理方式不一致.
	 * @param dao
	 * @param account
	 * @param balances
	 * @throws SQLException 
	 */
	public void saveBalance(List balances);
	
	/**
	 * 进行月结
	 * @param setYear TODO
	 * @param month TODO
	 * @throws SQLException 
	 *
	 */
	public void closeMonth(int setYear, int month);
}
