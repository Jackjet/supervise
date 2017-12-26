package gov.df.fap.api.dictionary.interfaces;

import gov.df.fap.bean.dictionary.dto.FElementDTO;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.util.xml.XMLData;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * IDictionary 数据字典服务端组件DTO接口（数据元、要素关联）
 * 
 * @version 1.0
 * @author justin
 * @since java 1.6
 */
public interface IControlDictionary {

	/**
	 * 查询当前所有表名
	 * 
	 * @return 所有表描述信息列表
	 */
	public List findTables();

	/**
	 * 根据表类型获取当前的表名列表
	 * 
	 * @param tableType
	 *            表类型
	 * @return 表描述信息列表
	 */
	public List findTables(String tableType);

	/**
	 * 根据传入的过滤条件筛选要素配置信息
	 * 
	 * @param condition
	 *            过滤条件
	 * @return List 要素配置信息列表
	 */
	public List getElementSet(String condition);

	/**
	 * 根据要素简称查询要素配置信息
	 * 
	 * @param code
	 *            要素简称
	 * @return 要素配置信息信息
	 */
	public XMLData getElementSetByCode(String code);

	/**
	 * 查询数据元对应的字段信息
	 * 
	 * @param code
	 *            数据元字段
	 * @return 字段信息
	 */
	public XMLData getMetaDataByCode(String code);

	/**
	 * 根据要素别名、显示字段、是否需要权限、要素coa、要素关联关系、附加查询条件等查询要素 数据并返回
	 * 
	 * @param element
	 *            要素别名
	 * @param page
	 *            分页对象
	 * @param sqlColumn
	 *            显示字段
	 * @param isNeedRight
	 *            是否需要权限标识
	 * @param coa_id
	 *            要素coa
	 * @param ctrlElementValues
	 *            要素关联关系
	 * @param sPlusSQL
	 *            附加查询条件
	 * @return 要素值集信息对应FElementDTO对象列表
	 */
	public List findEleValues(String element, FPaginationDTO page,
			String[] sqlColumn, boolean isNeedRight, String coa_id,
			Map ctrlElementValues, String sPlusSQL);

	/**
	 * 根据要素别名、是否需要权限查询要素数据并返回 (根据需要暂时屏蔽)
	 * 
	 * @param element
	 *            要素别名
	 * @param page
	 *            分页对象
	 * @param isNeedRight
	 *            是否需要权限标识
	 * @return 要素值集信息对应FElementDTO对象列表
	 */
	// public List findEleValues(String element,FPaginationDTO page,boolean
	// isNeedRight);
	/**
	 * 根据要素别名、要素coa查询要素数据并返回 (根据需要暂时屏蔽)
	 * 
	 * @param element
	 *            要素别名
	 * @param page
	 *            分页对象
	 * @param coa_id
	 *            要素coa
	 * @return 要素值集信息对应FElementDTO对象列表
	 */
	// public List findEleValues(String element,FPaginationDTO page,String
	// coa_id);
	/**
	 * 根据要素别名、是否需要权限、要素关联关系查询要素数据并返回 (根据需要暂时屏蔽)
	 * 
	 * @param element
	 *            要素别名
	 * @param page
	 *            分页对象
	 * @param isNeedRight
	 *            是否需要权限标识
	 * @param ctrlElementValues
	 *            要素关联关系
	 * @return 要素值集信息对应FElementDTO对象列表
	 */
	// public List findEleValues(String element,FPaginationDTO page,boolean
	// isNeedRight,Map ctrlElementValues);
	/**
	 * 根据要素别名和唯一显示码准确定位一条要素值集信息
	 * 
	 * @param element
	 *            要素别名
	 * @param code
	 *            唯一显示码
	 * @return 要素值集信息对应FElementDTO对象
	 */
	public FElementDTO findEleValueByCode(String element, String code);

	/**
	 * 根据要素别名和唯一显示码获取要素值集信息ID
	 * 
	 * @param element
	 *            要素别名
	 * @param code
	 *            唯一显示码
	 * @return 要素值集唯一ID
	 */
	public String getEleValueIDByAlias(String element, String code);

	/**
	 * 根据要素别名和唯一ID获取要素值集信息
	 * 
	 * @param element
	 *            要素别名
	 * @param id
	 *            唯一ID
	 * @return 要素值集信息对应FElementDTO对象
	 */
	public FElementDTO findEleValueById(String element, String id);

	/**
	 * 根据传入的字段名获得本字段枚举值
	 * 
	 * @param field_code
	 *            字段名
	 * @return String 枚举值字符串
	 */
	public String findFieldEnumValueString(String field_code);

	/**
	 * 根据传入的字段名获得本字段枚举值
	 * 
	 * @param field_code
	 *            字段名
	 * @return List 枚举值列表,List中的Map已模拟成要素模式chr_id,chr_code,chr_name
	 */
	public List findFieldEnumValueList(String field_code);

	/**
	 * 插入枚举信息项（累加的方式）
	 * 
	 * @param fieldCode
	 *            名称，对应sys_enumerate中的field_code字段
	 * @param value
	 *            由Map组成，键名chr_code,chr_name分别对应表sys_enumerate中的enu_code和enu_name字段
	 * @return 操作是否成功
	 * @throws Exception
	 *             异常
	 */
	public boolean insertFieldEnumValue(String fieldCode, List value)
			throws Exception;

	/**
	 * 插入枚举信息项（全部替换）
	 * 
	 * @param fieldCode
	 *            名称，对应sys_enumerate中的field_code字段
	 * @param value
	 *            由Map组成，键名chr_code,chr_name分别对应表sys_enumerate中的enu_code和enu_name字段
	 * @return 操作是否成功
	 * @throws Exception
	 *             异常
	 */
	public boolean replaceFieldEnumValue(String fieldCode, List value)
			throws Exception;

	/**
	 * 删除枚举信息项
	 * 
	 * @param fieldCode
	 *            名称，对应sys_enumerate中的field_code字段
	 * @param value
	 *            对应表sys_enumerate中的enu_code字段
	 * @return 操作是否成功
	 * @throws Exception
	 *             异常
	 */
	public boolean deleteFieldEnumValue(String fieldCode, String value)
			throws Exception;

	/**
	 * 往数据元表中插入一条数据
	 * 
	 * @param fieldInfo
	 *            map值对
	 * @return 插入后的数据元XMLData对象
	 * @throws Exception
	 */
	public Map insertMetaData(Map fieldInfo) throws Exception;

	/**
	 * 插入一条要素值集信息
	 * 
	 * @param element
	 *            要素别名
	 * @param fieldInfo
	 *            字段值对组合
	 * @return 插入后的数据对象
	 * @throws Exception
	 *             异常分析
	 */
	public Map insertValue(String element, Map fieldInfo) throws Exception;

	/**
	 * 修改一条要素值集信息
	 * 
	 * @param element
	 *            要素别名
	 * @param id
	 *            要素id
	 * @param fieldInfo
	 *            字段值对组合
	 * @return 修改后的数据对象
	 * @throws Exception
	 *             异常分析
	 */
	public Map updateEleValue(String element, String id, Map fieldInfo)
			throws Exception;

	/**
	 * 根据要素值集唯一ID删除要素值集信息
	 * 
	 * @param element
	 *            要素别名
	 * @param id
	 *            唯一ID
	 * @return 删除是否成功
	 * @throws Exception
	 *             异常分析
	 */
	public boolean deleteEleValue(String element, String id) throws Exception;

	/**
	 * 根据要素别名和唯一显示码获取值集历史信息
	 * 
	 * @param element
	 *            要素别名
	 * @param code
	 *            唯一显示码
	 * @return 要素值集历史信息对应FElementDTO对象列表
	 */
	public List findHistoryValuesByCode(String element, String code);

	/**
	 * 根据被关联要素编码查询主控要素编码
	 * 
	 * @param slaveElementCode
	 *            被关联要素编码
	 * @return 主控要素编码对象列表
	 */

	public List getPrimaryRelationElement(String slaveElementCode);

	/**
	 * 根据主控要素编码查询被关联要素编码
	 * 
	 * @param primaryElementCode
	 *            主控要素编码
	 * @return 被关联要素对象FElementDTO列表
	 */
	public List getSlaveRelationElement(String primaryElementCode);

	/**
	 * 根据要素表别名、要素值查询处理后的查询结果。
	 * 
	 * @param inObj
	 *            传入的map
	 * @param tableAlias
	 *            要素表别名
	 * @return 查询结果xml
	 */
	// public String getCondition(FElementDTO elementDto, String tableAlias);
	/**
	 * 实现返回基础要素表的数据权限sql语句
	 * 
	 * @param userid-------------用户id
	 * @param roleid-------------角色id
	 * @param elemcode-----------要素简称
	 * @param tablealias---------基础表别名
	 * @return sql语句
	 * @throws Exception---------错误信息
	 */
	public String getSqlElemRight(String userid, String roleid,
			String elemcode, String tablealias) throws Exception;

	/**
	 * 批量保存新增数据 spring bobo
	 * 
	 * @param batchData
	 *            新增数据XMLData组成集合
	 * @param tableName
	 *            新增数据表名
	 * @throws Exception
	 */
	public void batchInsert(List batchData, String tableName) throws Exception;

	/**
	 * 
	 * 根据指定Class返回要素对象列表。 注意！使用约定：Class内属性名必须与数据库基础数据表的字段对应，且是小写。
	 * <p>
	 * Class的属性类型可以是基本数据类型(int, float,
	 * double等)、String、BigDecimal，但必须与基础数据表存储内容相符。
	 * <p>
	 * 如Class类型是<code>java.util.Map</code>接口的实现类，则所有返回属性名为数据库字段名的小写，所有的属性以String形式保存。
	 * 
	 * @param element
	 *            要素简称
	 * @param page
	 *            分页对象
	 * @param isNeedRight
	 *            是否需要权限
	 * @param coa_id
	 *            coa id
	 * @param ctrlElementValues
	 *            关联关系条件
	 * @param plusSql
	 *            附加sql
	 * @param elementObjectClass
	 *            返回对象的Class。
	 * @return 根据指定的Class返回要素对象。
	 * 
	 */
	public List findEleValuesAsObj(String element, FPaginationDTO page,
			boolean isNeedRight, String coa_id, Map ctrlElementValues,
			String sPlusSQL, Class elementObjectClass);
	  /**
	   *
	   * 压缩传递 树结构
	   *
	   * @param element 要素简称
	 * @param page 分页对象
	 * @param isNeedRight 是否需要权限
	 * @param coa_id coa id
	 * @param ctrlElementValues 关联关系条件
	 * @param elementObjectClass 返回对象的Class。
	 * @param plusSql 附加sql
	   * @return  根据指定的Class返回要素对象。
	   *
	   */
	  public byte[] findEleValuesAsBytes(String element,FPaginationDTO page,boolean isNeedRight,String coa_id,
	 		   Map ctrlElementValues,String sPlusSQL,Class elementObjectClass) throws IOException; 
	
	/**
	 * 返回通过用户的预算单位机构权限过滤基础数据的sql语句
	 * 
	 * @param userid-------------用户id
	 * @param tablealias---------基础表别名
	 * @return sql语句
	 * @throws Exception---------错误信息
	 */
	public String getEnOrgRightSqlByUser(String userid, String tablealias);
}
