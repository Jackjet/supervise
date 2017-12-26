package gov.df.fap.api.dictionary;

import gov.df.fap.bean.dictionary.dto.FElementDTO;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;


public interface ElementOperation {
  /**
   * 是否要素判断函数
   * @param element 要素简称
   * @return 判断结果
   */
  public abstract boolean isElement(String element);

  /**
   * 得到刷新视图的列数据
   * ymj
   * @return List
   */
  public List getFreshViewCol(String ele_code, String old_name, String new_name);

  /**
   * 刷新视图的列数据
   * ymj
   * @return List
   */
  public void freshViewCol(String ele_code, String ele_name);

  /**
   * 得到自定义级次的数据,目前没有被用到
   * @param coa_id
   * @param element
   * @return List
   */
  public abstract List getCOADetailCode(String coa_id, String element);

  /**
   * 根据要素表别名、分页当前页、分页页大小、显示字段、
   * 是否需要权限过滤、要素COA、要素关联关系、查询条件字符串查询要素表数据，
   * 按显示字段返回分页处理后的查询结果。
   * @param element 要素简称
   * @param page
   * @param column 需要显示的字段
   * @param isNeedRight 是否需要权限过滤
   * @param coa_id 要素表COA
   * @param relation 要素关联关系
   * @param condition 查询条件字符串
   * @return 查询结果XMLData对象
   * @throws Exception 查询异常
   */
  public XMLData getEleByCondition(int setYear, String element, FPaginationDTO page, String[] column,
    boolean isNeedRight, String coaId, Map relation, String condition) throws Exception;

  /**
  *
  * 注意！使用约定：Class内属性名必须与数据库基础数据表的字段对应，且是小写。
  * Class的属性类型可以是基本数据类型(int, float, double等)、String、BigDecimal，
  * 但必须与基础数据表存储内容相符。
  *
  * @param element 要素简称
  * @param page 分页对象
  * @param isNeedRight 是否需要权限
  * @param coa_id coa id
  * @param ctrlElementValues 关联关系条件
  * @param plusSql 附加sql
  * @param elementObjectClass 返回对象的Class。
  * @return 根据指定的Class返回要素对象。
  *
  */
  public List getEleByConditionAsObj(String element, FPaginationDTO page, boolean isNeedRight, String coa_id,
    Map ctrlElementValues, String sPlusSQL, Class elementObjectClass) throws Exception;

  /**
   * 根据要素表别名、分页当前页、分页页大小、显示字段、
   * 是否需要权限过滤、要素COA、要素关联关系、查询条件字符串查询要素表数据，
   * 按显示字段返回分页处理后的查询结果。
   * @param element 要素简称
   * @param pageIndex 当前页
   * @param pageCount 当前页大小
   * @param column 需要显示的字段
   * @param isNeedRight 是否需要权限过滤
   * @param coa_id 要素表COA
   * @param relation 要素关联关系
   * @param condition 查询条件字符串
   * @param tableAlias 要素表名
   * @return 查询结果XMLData对象
   * @throws Exception 查询异常
   */
  public abstract XMLData getEleByCondition(String element, int pageIndex, int pageCount, String[] column,
    boolean isNeedRight, String coa_id, Map relation, String condition) throws Exception;

  /**
   * 根据要素表别名、分页当前页、分页页大小、显示字段、
   * 是否需要权限过滤、要素COA、要素关联关系、查询条件字符串查询要素表数据，
   * 按显示字段返回分页处理后的查询结果。
   * @param element 要素简称
   * @param pageIndex 当前页
   * @param pageCount 当前页大小
   * @param column 需要显示的字段
   * @param isNeedRight 是否需要权限过滤
   * @param coa_id 要素表COA
   * @param relation 要素关联关系
   * @param condition 查询条件字符串
   * @param tableAlias 要素表名
   * @return 查询结果XMLData对象
   * @throws Exception 查询异常
   */
  public abstract XMLData getEleByConditionWithRgCode(String element, int pageIndex, int pageCount,
    String[] column, boolean isNeedRight, String coa_id, Map relation, String condition) throws Exception;

  /**
   * 通过传入的xml插入要素表数据，并返回插入后的对象.
   * @param inXmlObj 传入的xml(来自Client)
   * @return 插入对象
   * @throws Exception 操作失败原因
   */
  public abstract XMLData insertEleByXml(String inXmlObj) throws Exception;

  /**
   * 通过传入的xml插入要素表数据，并返回插入后的对象
   * @param inXmlObj 传入的XMLData对象
   * @return 插入对象
   * @throws Exception 操作失败原因
   */
  public abstract XMLData insertEle(XMLData inXmlObj) throws Exception;

  /**
   * 修改要素值集,并返回修改后的对象
   * @param inXmlObj 修改描述xml
   * @return 修改后的对象
   * @throws Exception 异常
   */
  public abstract XMLData modifyEleByXml(String inXmlObj) throws Exception;

  /**
   * 修改要素值集,并返回修改后的对象
   * @param element 要素简称
   * @param chrId 要素唯一标识
   * @param fieldInfo 修改的字段
   * @return 修改后的对象
   * @throws Exception 异常
   */
  public XMLData modifyEle(String element, String chrId, Map fieldInfo) throws Exception;

  /**
   * 级联删除要素表数据
   * @param element 要素简称
   * @param chr_id 数据唯一id
   * @return 操作状态
   * @throws Exception 操作失败原因
   */
  public abstract boolean deleteEle(String element, String chr_id) throws Exception;

  /**
   * 根据chr_id获取指定要素的值集
   * @param chr_id 唯一码
   * @return XMLData值集对象
   */
  public abstract XMLData getEleByID(String table_name, String chr_id);

  /**
   * 根据chr_id获取指定要素的值集
   * @param chr_id 唯一码
   * @return XMLData值集对象
   */
  public abstract ElementInfo getElementInfo(String eleCode, String chr_id);

  /**
   * 根据要素表别名、要素值查询处理后的查询结果。
   * @param inObj 传入的xml
   * @param tableAlias 要素表名
   * @return 查询结果xml
   */
  public abstract String getCondition(FElementDTO elementDto, String tableAlias) throws Exception;

  /**
   * 根据要素别名查询是否存在龙图要素对应的要素
   * @param fEleCode 方正的要素别名
   * @return 龙图对应要素别名
   */
  public abstract String getLTEleCode(String fEleCode);

  /**
   * 通过传入的xml查询要素管理表数据并返回结果
   * @param inXmlObj 传入的xml
   * @param isNeedCount 是否需要分页
   * @return 查询结果xml
   * @throws Exception 操作失败原因
   */
  public abstract String getEleSetByXml(String inXmlObj, boolean isNeedCount) throws Exception;

  /**
   * 通过传入的xml插入要素管理表数据
   * @param inXmlObj 传入的xml(来自Client)
   * @return 插入对象
   * @throws Exception 操作失败原因
   */
  public abstract XMLData insertEleSetByXml(String inXmlObj) throws Exception;

  /**
   * 通过传入的xml修改数据
   * @param inXmlObj 传入的xml(来自Client)
   * @return 修改后的对象
   * @throws Exception 操作失败原因
   */
  public abstract XMLData modifyEleSetByXml(String inXmlObj) throws Exception;

  /**
   * 通过传入的id删除数据
   * @param id 要素配置信息唯一ID
   * @return 操作是否成功的结果
   * @throws Exception 操作失败原因
   */
  public abstract boolean deleteEleSet(String id) throws Exception;

  /**
   * 根据chr_id获取指定要素配置信息
   * @param chr_id 唯一码
   * @return XMLData对象
   */
  public abstract XMLData getEleSetByID(String chr_id);

  /**
   * 根据要素简称获取对应要素配置信息
   * @param ele_code 要素简称
   * @return XMLData对象
   */
  public abstract XMLData getEleSetByCode(String eleCode);

  /**
   * 根据条件查询要素
   * @param condition
   * @return
   */
  public List getEleSetByCondition(String condition);

  /**
   * 实现返回基础要素表的数据权限sql语句
   * @param userid-------------用户id
   * @param roleid-------------角色id
   * @param elemcode-----------要素简称
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */
  public abstract String getSqlElemRight(String userid, String roleid, String elemcode, String tablealias)
    throws Exception;

  /**
   * 是否存在于gl_ccid表中
   * @param element 要素简称
   * @param chr_id  要素值
   * @return true存在 false不存在
   */
  public abstract boolean isExistsInCcid(String element, String chr_id);

  /**
   * 根据当前的级次,比较更新要素表sys_element中的最大级次
   * @param element 要素简称
   * @param curLevel 当前级次
   * @throws Exception 修改异常
   */
  public void modifyMaxLevel(String element, int curLevel) throws Exception;

  /**判断当预算单位修改时，此时的预算单位属性是否可以修改*/
  public boolean checkIsReform(Map fieldInfo);

  /**
   * 刷新ccid
   * @param chr_id 
   * @param chr_code
   * @param chr_name
   * @throws Exception
   */
  public void refreshCCID(String eleName, String chr_id, String chr_code, String chr_name) throws Exception;

  /**
   * 
   * @param chr_id
   * @return
   * @throws Exception
   */
  public List getElementData(String chr_id) throws Exception;

  public String getEleCodeFromTableName(String tableName);

  // 张晓楠添加，2010-10-26，从湖北包移至主线，start
  /**
   * 获得必录入的字段。
   * @return XMLData值集对象。
   */
  public List getMustInputFields(String uiCode);

  /**
   * 根据chr_code获取指定要素的值集。
   * @param table_name 指定要素表名。
   * @param chr_code 要素代码。
   * @return XMLData值集对象。
   */
  public XMLData getEleByCode(String table_name, String chr_code);
  // 张晓楠添加，2010-10-26，从湖北包移至主线，end
  
  /**
	 * 根据要素清除要素缓存
	 * @param eleCodes 要素简称集合
	 * @param setYear 年度
	 * @param rg_code 区划
	 */
  public void clearElementCatchByEleCode(List eleCodes,String setYear,String rg_code)throws Exception;
}