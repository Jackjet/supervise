package gov.df.fap.api.workflow;

import gov.df.fap.bean.rule.SysRuleFunction;
import gov.df.fap.bean.rule.SysRulePara;
import gov.df.fap.bean.workflow.SysWfCondition;

import java.util.List;

/**
 * Title: 规则管理持久层服务接口
 * 
 * Description: 规则管理持久层服务接口 操作对象包含(SysRuleFunction 规则函数对象, SysRulePara
 * 规则参数对象,SysWfCondition 规则定义对象, SysWfConditionLine 规则明细对象
 * 
 * @author 
 * 
 * @since：2007-11-30 下午12:51:54 <br>
 *                   Company: 
 * 
 * @version 1.0
 */

public interface ISysRegulationConf {

  /**
   * 根据规则id得到详细信息
   * 
   * @param conditionId
   *            规则id
   * @return 返回包含XMLData类型的List
   * @throws Exception
   */
  public List getListByConditionById(String conditionId) throws Exception;

  /**
   * 
   * 功能：取得所有的规则定义列表
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:03:47
   * @return 返回包含XMLData类型的规则信息LIST
   * @throws Exception
   */
  public List getAllRules() throws Exception;

  /**
   * 
   * 功能：取得指定类型的规则定义列表
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:04:49
   * @param ruleType
   *            规则类型
   * @return 返回包含XMLData类型的规则信息LIST
   * @throws Exception
   */
  public List getAllRulesByType(String ruleType) throws Exception;

  /**
   * 
   * 功能：根据规则编号取得规则定义对象
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:09:43
   * @param ruleId
   *            规则ID
   * @return 规则对象
   * @throws Exception
   */
  public SysWfCondition getRuleById(String ruleId) throws Exception;

  /**
   * 
   * 功能： 保存规则定义对象（新增/修改）
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:10:43
   * @param ruleDto
   *            规则对象
   * @return 返回当前对象的ID
   * @throws Exception
   */
  public String saveRule(SysWfCondition ruleDto) throws Exception;

  /**
   * 
   * 功能：删除规则定义对象
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:11:23
   * @param ruleDto
   *            规则对象
   * @throws Exception
   */
  public void deleteRule(SysWfCondition ruleDto) throws Exception;

  /**
   * 
   * 功能：根据规则参数编号取得规则参数定义对象
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:12:54
   * @param paraId
   *            参数ID
   * @return 规则参数对象
   * @throws Exception
   */
  public SysRulePara getRuleParaById(String paraId) throws Exception;

  /**
   * 
   * 功能：保存规则参数定义对象
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:13:27
   * @param paraDto
   *            规则参数对象
   * @return 返回当前对象的ID
   * @throws Exception
   */
  public String saveRulePara(SysRulePara paraDto) throws Exception;

  /**
   * 
   * 功能：保存规则函数
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-6 上午09:42:00
   * @param function
   *            规则函数对象
   * @return 返回当前对象的ID
   * @throws Exception
   */
  public String saveFuntion(SysRuleFunction function) throws Exception;

  /**
   * 
   * 功能：取得所有的公用的规则参数定义列表
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:14:16
   * @return 返回XMLData类型的公有参数列表
   * @throws Exception
   */
  public List getSharedRuleParas() throws Exception;

  /**
   * 
   * 功能：取得所给出的规则对应的参数定义列表
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:14:45
   * @param ruleId
   * @return 包含XMLData类型规则的私有参数列表
   * @throws Exception
   */
  public List getRuleParasByRule(String ruleId) throws Exception;

  /**
   * 
   * 功能：删除规则参数定义对象
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:15:18
   * @param paraId
   *            参数ID
   * @return 删除结果字符串
   * @throws Exception
   */
  public String deleteRulePara(String paraId) throws Exception;

  /**
   * 
   * 功能：从配置文件中取得所有的规则函数配置列表
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:15:47
   * @return XMLData类型的结果集
   * @throws Exception
   */
  //	public List getAllRuleFuns() throws Exception;

  /**
   * 
   * 功能：从函数表中取得所有的函数列表.
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-6 上午09:15:24
   * @return XMLData类型的结果集
   * @throws Exception
   */
  public List getAllFunction() throws Exception;

  /**
   * 
   * 功能：通过参数类型查询匹配的相应的参数集合
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-4 下午04:54:07
   * @param paraType
   *            参数类型
   * @param ruleId
   *            公有时为null
   * @return XMLData类型的结果集
   * @throws Exception
   */
  public List getRuleParasByType(String paraType, String ruleId) throws Exception;

  /**
   * 
   * 功能：获得数据库所有表信息
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-4 下午04:55:29
   * @return XMLData类型的结果集
   * @throws Exception
   */
  public List getAllTables() throws Exception;

  /**
   * 
   * 功能：获得指定表的所有字段信息
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-4 下午04:56:48
   * @param tableName
   *            表名称
   * @return XMLData类型的结果集
   * @throws Exception
   */
  public List getTablesField(String tableName) throws Exception;

  /**
   * 
   * 功能：通过函数ID 取得函数参数列表
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-6 下午12:57:14
   * @param fun_id
   *            函数ID
   * @return XMLData类型的结果集
   * @throws Exception
   */
  public List getFunctionParasByFunId(String fun_id) throws Exception;

  /**
   * 
   * 功能：删除函数
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-6 下午02:30:20
   * @param srf
   *            函数对象
   * @return 返回删除结果信息 成功为空
   * @throws Exception
   */
  public String deleteFunction(SysRuleFunction srf) throws Exception;

  /**
   * 
   * 功能：通过规则ID 查询对应的组成信息.
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-16 下午10:39:41
   * @param rule_id
   *            规则ID
   * @return 返回XMLData类型的结果集
   * @throws Exception
   */
  public List getRule4RuleFactory(String rule_id) throws Exception;

  /**
   * 
   * 功能：加载个规则参数对象
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-17 上午12:10:36
   * @param para_id
   *            参数ID
   * @return 规则参数对象
   * @throws Exception
   */
  public SysRulePara getSysRulePara(String para_id) throws Exception;

  /**
   * 
   * 
   * 功能：加载个规则函数对象
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-17 上午12:10:36
   * @param fun_id
   *            函数ID
   * @return 规则函数对象
   * @throws Exception
   */
  public SysRuleFunction getSysRuleFunction(String fun_id) throws Exception;

  /**
   * 
   * 功能： 删除没有规则ＩＤ的私有参数
   *
   * @author bing-zeng 
   * <br>Date ：2008-1-17 下午01:52:37
   * @throws Exception
   */
  public void delConditionPara4Private() throws Exception;

  public List getEleByCode(String char_code);

  public List getEles();

  public List findComments(String tableName, String field);

  public List getAllSysModulesAsTree() throws Exception;

  public List getAllSysRolesAsTree() throws Exception;

  public List getUserTree() throws Exception;
}
