package gov.df.supervise.api.sup;

import java.util.List;
import java.util.Map;

/**
 * 监管事项基础数据接口
 * 
 * @author w 2017-08-22
 * 
 */
public interface SupService {
	
	/**
	 * 组织获取监管事项查询过滤树
	 * @return 查询结果集List
	 */
	public List getSupTree() ;
	
	/**
	 * 事项作废
	 * @param ids
	 * @return
	 */
	public int validSup(String ids);
	
	/**
	 * 查询监管事项
	 * @param tableName	  表名
	 * @param conditionMap  过滤条件map
	 * @param proFlag  是否带权限过滤
	 * @param pageInfo  分页参数
	 * @return 查询结果集 List
	 */
	public List getSup(String tableName, Map conditionMap, boolean proFlag, String pageInfo);

	/**
	 * 删除监管事项 csof_e_sup
	 * @param ids 主键值
	 * @param key 主键字段
	 * @return 执行条目数
	 */
	public int deleteSup(String ids, String key);

	/**
	 * 保存监管事项 csof_e_sup
	 * @param supList 要保存的监管事项数据集
	 * @param reportList 要保存的报表数据集
	 * @param billtype_code 单据编码
	 * @return 执行条目数
	 */
	public int saveSup(List supList,List reportList,String billtype_code);
	
	/**
	 * 修改监管事项 csof_e_sup
	 * @param supList  要修改的监管事项数据集
	 * @param reportList  事项关联报表
	 * @param billtype_code 单据编码
	 * @return 执行条目数
	 */
	public int updateSup(Map supData,List reportList,String billtype_code);
	

}
