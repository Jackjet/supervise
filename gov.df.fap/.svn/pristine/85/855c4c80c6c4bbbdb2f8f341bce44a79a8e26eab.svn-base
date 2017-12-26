package gov.df.fap.api.workflow.sysregulation;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * Title: 规则运行时接口类
 * </p>
 * <p>
 * Description:规则运行时接口类
 * </p>
 * <p>
 * Copyright: Copyright
 * </p>
 * @version 1.0
 * @author  2017年03月08日
 * @since java 1.6
 */
public interface IWorkFlowRuleFactory {

	/**
	 * 
	 * 功能：校验数据是否在范围内(如:记账范围，匹配处理)
	 * 
	 * @author bing-zeng <br>
	 *         Date 创建时间：2007-12-25 下午05:46:57
	 * @param conditionId
	 *            规则ID
	 * @param baseData
	 *            参数值MAP
	 * @return true-是，false-否
	 * @throws Exception
	 *             抛出错误
	 */
	public boolean isMatchByBSH(String conditionId, Object baseData)
			throws Exception;

	
	/**
	 * 
	 * 功能： 规则增加页面验证规则表达试使用方法。
	 * 
	 * @author bing-zeng <br>
	 *         Date 创建时间：2007-12-24 上午10:58:19
	 * @param list
	 *            table的值列表
	 * @param map
	 *            参数值MAP
	 * @return beanshell 字符串
	 * @throws Exception
	 *             抛出错误
	 */
	public String getBshFlag(List list, Map map) throws Exception;

	/**
	 * 
	 * 功能：设置discription值 即：描述
	 * 
	 * @author bing-zeng <br>
	 *         Date ：2008-1-1 上午10:35:23
	 * @param list
	 *            规则明细集合
	 * @return 返回表达试描述
	 */
	public String setDescription(List list);
}
