package gov.df.fap.api.gl.coa;

import java.util.List;

/**
 * 批量刷新CCID功能 服务接口
 * @author 
 * @version 
 */
public interface IBatchCcidRefreshService {

	public final static String CALCULATE_SINGLE_CCID_MODEL = "1";
	
	public final static String CALCULATE_TABLE_MODEL = "2";
	
	public final static String CALCULATE_GL_CCID_MODEL = "3";
	
	public final static String CALCULATE_BUSINESS_MODEL = "4";
	
	/**
	 * 获取刷新CCID列表
	 * @return
	 */
	public List getRefreshCcidTables();
	
	/**
	 * 保存列表
	 * @param tableList
	 */
	public void saveRefreshCcidTables(List tableList); 
	
	/**
	 * 重新计算CCID
	 * @param calculateModel 计算CCID 模式
	 * @param calParam 如果是单挑CCID计算 传CCID;如果是表计算 传表名;如果是整个CCID表计算 参数无效
	 * @param isResetCcid 是否刷写回维护列表
	 * @throws Exception
	 */
	public void calculateCcid(String calculateModel, Object calParam, boolean isResetCcid) throws Exception;
	
	/**
	 * 获取列
	 * @param tableName
	 * @return
	 */
	public List getTableColumn(String tableName);

	/**
	 * 查询业务表数据
	 * @param tableName
	 * @param condition
	 */
	public List getBusnessData(String tableName, String condition);
	
}
