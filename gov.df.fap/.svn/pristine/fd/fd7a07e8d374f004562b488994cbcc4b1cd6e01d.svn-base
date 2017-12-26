package gov.df.fap.api.gl.balance;

import gov.df.fap.bean.gl.balance.TransRecord;
import gov.df.fap.bean.gl.dto.FCtrlRecordDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * 额度刷新辅助类
 * @author lwq
 */
public interface RefreshBalanceHandler {

	/**
	 * 初始化额度刷新数据
	 * @param tableName
	 * @param whereStr
	 */
	public void initRefreshData() throws SQLException;
	
	/**
	 * 根据索引取出已缓存的要刷新数据
	 * @param index
	 * @return
	 */
	public FCtrlRecordDTO getFCtrlRecordDTO(int index);
	
	/**
	 * 额度刷新后的操作 例如更新表层数据和保存刷新记录
	 * @param transList 存储为Map形式
	 */
	public void afterRefreshBalance(List transList);
	
	/**
	 * 取出要刷新数据的记录数
	 * @return
	 */
	public int getRefreshDataSize();
	
	/**
	 * 取得额度冲销/生成的一对临时额度对象.
	 * @param index
	 * @return
	 */
	public TransRecord getTransRecord(int index);
	
	/**
	 * 获取要刷新额度数据 根据所有下游追溯对象查询
	 * @return
	 */
	public List getRefreshBalanceData();
	
	/**
	 * 获取要刷新支付数据 根据所有下游追溯对象查询
	 * @return
	 */
	public List getRefreshPayData();
}
