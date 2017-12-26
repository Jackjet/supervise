package gov.df.fap.service.dictionary.element;

import gov.df.fap.api.dictionary.ElementCacheKey;
import gov.df.fap.api.dictionary.ElementConfigurationService;
import gov.df.fap.api.dictionary.ElementInfo;
import gov.df.fap.api.dictionary.ElementOperation;
import gov.df.fap.api.dictionary.ElementSetXMLData;
import gov.df.fap.bean.dictionary.dto.FElementDTO;
import gov.df.fap.bean.dictionary.element.ElementDefinition;
import gov.df.fap.bean.gl.GlConstant;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.service.dictionary.elecache.ElementCacheFactory;
import gov.df.fap.service.dictionary.elecache.ElementDataCache;
import gov.df.fap.service.gl.configure.impl.EngineConfiguration;
import gov.df.fap.service.gl.core.DaoSupport;
import gov.df.fap.service.gl.core.interfaces.ResultSetMapper;
import gov.df.fap.service.util.datasource.SQLUtil;
import gov.df.fap.service.util.dictionary.interfaces.CacheObjectCloner;
import gov.df.fap.service.util.dictionary.interfaces.CacheObjectLoader;
import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.service.util.memcache.MemCacheMap;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.Properties.PropertiesUtil;
import gov.df.fap.util.number.NumberUtil;
import gov.df.fap.util.xml.ParseXML;
import gov.df.fap.util.xml.XMLData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 要素相关操作外覆类(Wrapper, 或者叫作代理类delegate/proxy),包了一层缓存.
 * 目前缓存了要素信息、基础数据表，具体的缓存配置见EngineConfiguration类.
 * 
 * @author 
 * @version 
 * @see 
 */
@Component("elementOperationWrapper")
public class ElementOperationWrapperBO extends AbstractElementOperation implements InitializingBean, ElementOperation {

  /** 要素操作实现类，被代理的类 */
  @Autowired
  @Qualifier("elementOperationBO")
  protected ElementOperation eleOpImpl;

  /** 缓存用到一些特殊的SQL语句 */

  @Autowired
  protected DaoSupport daoSupport;

  /**
   * 用来缓存sys_element中的配置信息的HashMap,该HashMap不是线程安全,
   * 在读取缓存时如果HashMap的结构,会有异常,所以加入sysElementSetLock 作为锁同步缓存操作
   */
  private static Map sysElementMap = MemCacheMap.getCacheMap(ElementOperationWrapperBO.class);//分布式缓存

  /** 要素信息锁 */
  private Object sysElementSetLock = new Object();

  /** 各个要素数据源缓存 */
  protected static Map elementSourceCacheMap = new HashMap();

  /** 引擎基本配置 */
  @Autowired
  protected ElementConfigurationService elementConfiguration = null;

  private static Log logger = LogFactory.getLog(ElementOperationWrapperBO.class);

  private EngineConfiguration engineConfiguration = null;

  public void setEngineConfiguration(EngineConfiguration engineConfiguration) {
    this.engineConfiguration = engineConfiguration;
  }

  public void setDaoSupport(DaoSupport daoSupport) {
    this.daoSupport = daoSupport;
  }

  public void setEleOpImpl(ElementOperation eleOpImpl) {
    this.eleOpImpl = eleOpImpl;
  }

  public void setElementConfiguration(ElementConfigurationService elementConfiguration) {
    this.elementConfiguration = elementConfiguration;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperation#deleteEle(java.lang
   * .String, java.lang.String)
   */
  public boolean deleteEle(String element, String chr_id) throws Exception {
    if (elementConfiguration.isElementSourceCache() && elementConfiguration.isEleCached(element)) {
      boolean isDel = eleOpImpl.deleteEle(element, chr_id);
      if (isDel) {
        Map eleSet = getEleSetByCode(element);
        String upperTableName = ((String) eleSet.get("ele_source")).toUpperCase();
        elementSourceCacheMap.remove(this.getElementSourceCacheKey(upperTableName));
      }
      if (element.equalsIgnoreCase("AS"))
        engineConfiguration.clearAccountCache();
      return isDel;
    } else {
      if (element.equalsIgnoreCase("AS"))
        engineConfiguration.clearAccountCache();
      return eleOpImpl.deleteEle(element, chr_id);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperation#deleteEleSet(java.lang
   * .String)
   */
  public boolean deleteEleSet(String id) throws Exception {
    if (elementConfiguration.isElementSetCache()) {
      removeCacheEleSet(getEleSetByID(id));// 清除缓存
    }
    return eleOpImpl.deleteEleSet(id);
  }

  public String getCondition(FElementDTO elementDto, String tableAlias) throws Exception {
    return eleOpImpl.getCondition(elementDto, tableAlias);
  }

  protected String getSelectedField(String element, String tableName, String tableAlias, String[] column) {
    return " chr_id ";
  }

  public XMLData getEleByCondition(int setYear, String element, FPaginationDTO page, String[] column,
    boolean isNeedRight, String coaId, Map relation, String condition) throws Exception {

    String upperEleCode = element.toUpperCase();
    if (elementConfiguration.isElementSourceCache() && elementConfiguration.isEleCached(upperEleCode)) {

      XMLData result = new XMLData();

      final XMLData elementSet = getEleSetByCode(upperEleCode);
      final String tableName = (String) elementSet.get("ele_source");
      String sql = getQuerySql(NumberUtil.toInt(CommonUtil.getSetYear()), upperEleCode, tableName, column, isNeedRight,
        relation, coaId, condition);

      // 兼容基础数据的chr_code是纯数字型或含有字符的排序问题
      List eleList = getEleFromCache(sql, upperEleCode, new DbColumnMapperCloner(column));

      String totalRow = StringUtil.toStr(daoSupport.getRowCount(sql));

      String eleName = (String) elementSet.get("ele_name");

      result.put("ele_code", upperEleCode);
      result.put("table_name", tableName);
      result.put("ele_name", eleName);
      result.put("total_count", totalRow);
      result.put("row", eleList);

      return result;
    } else
      return eleOpImpl.getEleByCondition(setYear, element, page, column, isNeedRight, coaId, relation, condition);

  }

  public XMLData getEleByConditionWithRgCode(int setYear, String element, FPaginationDTO page, String[] column,
    boolean isNeedRight, String coaId, Map relation, String condition) throws Exception {

    String upperEleCode = element.toUpperCase();
    if (elementConfiguration.isElementSourceCache() && elementConfiguration.isEleCached(upperEleCode)) {

      XMLData result = new XMLData();

      final XMLData elementSet = getEleSetByCode(upperEleCode);
      final String tableName = (String) elementSet.get("ele_source");
      String sql = getQueryLikeSql(NumberUtil.toInt(CommonUtil.getSetYear()), upperEleCode, tableName, column,
        isNeedRight, relation, coaId, condition);

      // modify by wanghongtao 兼容基础数据的chr_code是纯数字型或含有字符的排序问题
      List eleList = getEleFromCache(sql, upperEleCode, new DbColumnMapperCloner(column));

      String totalRow = StringUtil.toStr(daoSupport.getRowCount(sql));

      String eleName = (String) elementSet.get("ele_name");

      result.put("ele_code", upperEleCode);
      result.put("table_name", tableName);
      result.put("ele_name", eleName);
      result.put("total_count", totalRow);
      result.put("row", eleList);

      return result;
    } else
      return eleOpImpl.getEleByCondition(setYear, element, page, column, isNeedRight, coaId, relation, condition);

  }

  public List getEleByConditionAsObj(String element, FPaginationDTO page, boolean isNeedRight, String coa_id,
    Map ctrlElementValues, String sPlusSQL, Class elementObjectClass) throws Exception {

    String upperEleCode = element.toUpperCase();
    if (elementConfiguration.isElementSourceCache() && elementConfiguration.isEleCached(upperEleCode)) {

      final XMLData elementSet = getEleSetByCode(upperEleCode);
      final String tableName = (String) elementSet.get("ele_source");
      final String sql = getQuerySql(NumberUtil.toInt(CommonUtil.getSetYear()), upperEleCode, tableName, null,
        isNeedRight, ctrlElementValues, coa_id, sPlusSQL);
      List eleList = getEleFromCache(sql, upperEleCode, new JavaBeanCacheObjectCloner(elementObjectClass));
      return eleList;
    } else
      return eleOpImpl.getEleByConditionAsObj(element, page, isNeedRight, coa_id, ctrlElementValues, sPlusSQL,
        elementObjectClass);

  }

  public XMLData getEleByCondition(String element, int pageIndex, int pageCount, final String[] column,
    boolean isNeedRight, String coaId, Map relation, String condition) throws Exception {
    int setYear = NumberUtil.toInt(CommonUtil.getSetYear());
    FPaginationDTO page = new FPaginationDTO();
    page.setCurrpage(pageIndex);
    page.setPagecount(pageCount);
    return getEleByCondition(setYear, element, page, column, isNeedRight, coaId, relation, condition);
  }

  public XMLData getEleByConditionWithRgCode(String element, int pageIndex, int pageCount, final String[] column,
    boolean isNeedRight, String coaId, Map relation, String condition) throws Exception {
    int setYear = NumberUtil.toInt(CommonUtil.getSetYear());
    FPaginationDTO page = new FPaginationDTO();
    page.setCurrpage(pageIndex);
    page.setPagecount(pageCount);
    return getEleByCondition(setYear, element, page, column, isNeedRight, coaId, relation, condition);
  }

  /**
   * 从缓存中读取要素,由于读取要素对象时可能需要作一些特殊的处理,所以加入CacheObjectCloner作为参数,在方法 中回调进行对象克隆.
   * 
   * @param idQuerySql
   *            只查询ID的要素查询SQL语句
   * @param eleCode
   *            要素CODE
   * @param cloner
   *            克隆操作
   * @return
   */
  protected List getEleFromCache(String idQuerySql, String eleCode, CacheObjectCloner cloner) {
    ElementDataCache eleCache = getElementSourceCache(eleCode);
    final String setYear = CommonUtil.getSetYear();
    final String rgCode = CommonUtil.getRgCode();

    List keyList = daoSupport.genericQuery(idQuerySql, new ResultSetMapper() {
      public Object handleRow(ResultSet rs) throws SQLException {
        final String chrId = rs.getString(1);
        return new ElementCacheKey(chrId, setYear, rgCode);
      }
    });

    if (elementConfiguration.isNeedResultCache()) {
      List elementsFromCache = eleCache.getCacheObjectByIndex(idQuerySql);
      if (elementsFromCache == null) {
        eleCache.generateIndexByKeys(idQuerySql, keyList);
        return eleCache.getCacheObjectsByKeys(keyList, cloner);
      } else
        return elementsFromCache;
    } else
      return eleCache.getCacheObjectsByKeys(keyList, cloner);
  }

  /**
   * 加载基础数据缓存
   * 
   * @param eleCode
   */
  public void loadELementSourceCache(String eleCode) {
    getElementSourceCache(eleCode);
  }

  /**
   * 取出一个要素缓存
   * 
   * @param eleCode
   * @return
   */
  protected ElementDataCache getElementSourceCache(String eleCode) {
    XMLData eleSet = getEleSetByCode(eleCode);
    if (eleSet == null)
      throw new RuntimeException("不存在" + eleCode + "要素!");

    String upperTableName = ((String) eleSet.get("ele_source")).toUpperCase();
    String cachedKey = this.getElementSourceCacheKey(upperTableName);
    ElementDataCache eleCache = (ElementDataCache) elementSourceCacheMap.get(cachedKey);
    if (eleCache == null) {
      synchronized (this) {//增加二次判断,避免重复初始化
        if (elementSourceCacheMap.get(cachedKey) != null)
          return (ElementDataCache) elementSourceCacheMap.get(cachedKey);
        if (eleCache == null) {
          eleCache = initCache(upperTableName, cachedKey);
        }
      }
      eleCache.setCacheObjectLoader(new ElementDataCacheLoader(eleCode, upperTableName));
    }
    return eleCache;
  }

  /**
   * 初始化一个要素缓存
   * 
   * @param upperEleTableName
   *            大写的表名
   * @param cachedKey
   * @return
   */
  protected ElementDataCache initCache(String upperEleTableName, String cachedKey) {
    List linkedList = daoSupport.genericQuery("select e.*," + SQLUtil.replaceLinkChar("CHR_CODE||' '||CHR_NAME") + " as codename, (select chr_name from "
      + upperEleTableName
      + " ep  where ep.chr_id=e.parent_id and ep.rg_code = e.rg_code and ep.set_year= e.set_year) as parent_name from "
      + upperEleTableName + " e where rg_code=? and set_year=? order by  chr_code, parent_id", new String[] {
      CommonUtil.getCurrRegion(), CommonUtil.getSetYear() }, XMLData.class);

    Iterator it = linkedList.iterator();
    ElementDataCache eleCache = (ElementDataCache) ElementCacheFactory.getInstance().getCacheInstance(); // (eleCode);
    eleCache.setCacheCapability(0);// 设置为0表示不限制
    eleCache.setNeedSynchronized(false);
    while (it.hasNext()) {
      final XMLData cahcedElement = (XMLData) it.next();
      final ElementCacheKey key = new ElementCacheKey((String) cahcedElement.get(ElementDefinition.CHR_ID),
        (String) cahcedElement.get(GlConstant.SET_YEAR), (String) cahcedElement.get(ElementDefinition.RG_CODE));
      eleCache.addCacheObject(key, cahcedElement);
    }
    elementSourceCacheMap.put(cachedKey, eleCache);
    return eleCache;
  }

  public XMLData getEleByID(String table_name, String chr_id) {
    if (isElement(table_name)) {
      final XMLData eleSet = getEleSetByCode(table_name);
      final String eleCode = table_name;
      XMLData sourceElementValue = null;
      final String setYear = CommonUtil.getSetYear();
      final String rgCode = CommonUtil.getRgCode();
      // 判断如果有缓存，则从缓存中取数据,暂时写if，以后再重构，
      if (elementConfiguration.isElementSourceCache() && elementConfiguration.isEleCached(eleCode)) {
        ElementDataCache eleCache = getElementSourceCache(eleCode);
        sourceElementValue = getElementInfoFromCache(eleCache, chr_id, setYear, rgCode);
      } else {
        sourceElementValue = eleOpImpl.getEleByID((String) eleSet.get("ele_source"), chr_id);
      }
      return sourceElementValue;
    } else
      return eleOpImpl.getEleByID(table_name, chr_id);
  }

  /**
   * 从缓存读取基础数据
   * 
   * @param eleCache
   * @param eleValue
   * @return
   * @throws Exception
   */
  private XMLData getElementInfoFromCache(ElementDataCache eleCache, String eleValue, String setYear, String rgCode) {
    return (XMLData) eleCache.getCacheObject(new ElementCacheKey(eleValue, setYear, rgCode), new CacheObjectCloner() {
      public Object clone(Object beCloned) {
        return beCloned;
      }
    });
  }

  public XMLData getEleSetByCode(String eleCode) {
    XMLData data = null;
    if (StringUtil.isEmpty(eleCode))
      return data;
    if (elementConfiguration.isElementSetCache())
      data = (XMLData) sysElementMap.get(SessionUtil.getLoginYear() + SessionUtil.getRgCode() + eleCode.toLowerCase());

    if (data == null) {
      data = eleOpImpl.getEleSetByCode(eleCode);
      if (data != null && elementConfiguration.isElementSetCache())
        cachedEleSet(data, true);// 同时通过id和code缓存
    }
    return data;
  }

  public XMLData getEleSetByID(String chr_id) {
    XMLData data = null;

    if (elementConfiguration.isElementSetCache()) {
      data = (XMLData) sysElementMap.get(SessionUtil.getLoginYear() + SessionUtil.getRgCode() + chr_id);
    }

    if (data == null) {
      data = eleOpImpl.getEleSetByID(chr_id);
      if (data != null && elementConfiguration.isElementSetCache())
        cachedEleSet(data, true);// 同时通过id和code缓存
    }
    return data;
  }

  public void modifyMaxLevel(String element, int curLevel) throws Exception {
    if (elementConfiguration.isElementSetCache()) {
      XMLData eleSet = getEleSetByCode(element);
      if (eleSet == null)
        throw new RuntimeException("根据要素编码" + element + "找不到要素信息, 无法修改最大值!");

      String originalMaxLevel = (String) eleSet.get("max_level");
      if ((StringUtils.trimToNull(originalMaxLevel) == null) || curLevel > Integer.parseInt(originalMaxLevel)) {
        eleSet.put("max_level", StringUtil.toStr(curLevel));
        cachedEleSet(eleSet, true);
        eleOpImpl.modifyMaxLevel(element, curLevel);
      }
    } else {
      eleOpImpl.modifyMaxLevel(element, curLevel);
    }
  }

  public String getEleSetByXml(String inXmlObj, boolean isNeedCount) throws Exception {
    return eleOpImpl.getEleSetByXml(inXmlObj, isNeedCount);
  }

  public String getLTEleCode(String fEleCode) {
    return eleOpImpl.getLTEleCode(fEleCode);
  }

  public String getSqlElemRight(String userid, String roleid, String elemcode, String tablealias) throws Exception {
    return eleOpImpl.getSqlElemRight(userid, roleid, elemcode, tablealias);
  }

  public XMLData insertEleByXml(String inXmlObj) throws Exception {
    XMLData intXML = ParseXML.convertXmlToObj(inXmlObj);
    String eleCode = (String) intXML.get("element_code");
    XMLData saveObject = eleOpImpl.insertEleByXml(inXmlObj);
    String chrId = (String) saveObject.get(ElementDefinition.CHR_ID);
    if (elementConfiguration.isElementSourceCache() && elementConfiguration.isEleCached(eleCode)) {
      ElementDataCache cache = getElementSourceCache(eleCode);
      cache.addCacheObject(chrId, saveObject);
      List updateList = (List) saveObject.get("update_list");
      if (updateList != null)
        for (int i = 0; i < updateList.size(); i++) {
          final XMLData updateOne = (XMLData) updateList.get(i);
          cache.addCacheElement(updateOne);
        }
    }
    return saveObject;
  }

  public XMLData insertEleSetByXml(String inXmlObj) throws Exception {
    return eleOpImpl.insertEleSetByXml(inXmlObj);
  }

  public boolean isElement(String element) {
    if (elementConfiguration.isElementSetCache()) {
      try {
        return getEleSetByCode(element) != null;
      } catch (Exception ex) {
        // do nothing
      }
    }
    return eleOpImpl.isElement(element);
  }

  public boolean isExistsInCcid(String element, String chr_id) {
    return eleOpImpl.isExistsInCcid(element, chr_id);
  }

  public XMLData modifyEleByXml(String inXmlObj) throws Exception {
    XMLData xml = ParseXML.convertXmlToObj(inXmlObj);
    String element = (String) xml.getSubObject("element");
    if (elementConfiguration.isElementSourceCache() && elementConfiguration.isEleCached(element)) {
      XMLData result = eleOpImpl.modifyEleByXml(inXmlObj);
      // 原来的保存方法太过复杂，且不够对象化，这样暂时就删除缓存，以免复杂性感染缓存封装
      Map eleSet = getEleSetByCode(element);
      String upperTableName = ((String) eleSet.get("ele_source")).toUpperCase();
      String key = getElementSourceCacheKey(upperTableName);
      elementSourceCacheMap.remove(key);
      // 删除sys_element中此要素的缓存
      sysElementMap.remove(SessionUtil.getLoginYear() + SessionUtil.getRgCode() + element);
      return result;
    } else {
      return eleOpImpl.modifyEleByXml(inXmlObj);
    }

  }

  /**
   * 生成要素数据源缓存KEY
   * 
   * @param upperTableName
   * @return
   */
  private String getElementSourceCacheKey(String upperTableName) {
    String setYear = CommonUtil.getSetYear();
    String rgCode = CommonUtil.getRgCode();

    return upperTableName + "#" + setYear + "#" + rgCode;
  }

  public XMLData modifyEleSetByXml(String inXmlObj) throws Exception {
    // 暂时全部删除缓存的要素信息
    synchronized (sysElementSetLock) {
      sysElementMap.clear();
    }
    return eleOpImpl.modifyEleSetByXml(inXmlObj);
  }

  /**
   * 缓存要素信息
   * 
   * @param xmlData
   */
  private void cachedEleSet(XMLData xmlData, boolean overWrite) {
    if (xmlData == null)
      return;

    synchronized (this.sysElementSetLock) {
      // 同时通过id和code缓存
      if (overWrite
        || !sysElementMap.containsKey(SessionUtil.getLoginYear() + SessionUtil.getRgCode() + xmlData.get("chr_id")))
        sysElementMap.put(SessionUtil.getLoginYear() + SessionUtil.getRgCode() + xmlData.get("chr_id"), xmlData);
      String eleCode = (String) xmlData.get("ele_code");
      if (overWrite
        || !sysElementMap.containsKey(SessionUtil.getLoginYear() + SessionUtil.getRgCode() + eleCode.toLowerCase()))
        sysElementMap.put(SessionUtil.getLoginYear() + SessionUtil.getRgCode() + eleCode.toLowerCase(), xmlData);
    }
  }

  /**
   * 删除要素信息缓存
   * 
   * @param xmlData
   */
  private void removeCacheEleSet(XMLData xmlData) {
    if (xmlData == null)
      return;
    synchronized (sysElementSetLock) {
      sysElementMap.remove(SessionUtil.getLoginYear() + SessionUtil.getRgCode() + xmlData.get("chr_id"));
      sysElementMap.remove(SessionUtil.getLoginYear() + SessionUtil.getRgCode()
        + ((String) xmlData.get("chr_code")).toLowerCase());
    }
  }

  public boolean checkIsReform(Map fieldInfo) {
    return eleOpImpl.checkIsReform(fieldInfo);
  }

  public XMLData insertEle(XMLData inXmlObj) throws Exception {
    String eleCode = (String) inXmlObj.get("element_code");
    XMLData saveObject = eleOpImpl.insertEle(inXmlObj);
    if (saveObject != null) {
      String chrId = (String) saveObject.get(ElementDefinition.CHR_ID);
      String parentId = (String) saveObject.get(ElementDefinition.PARENT_ID);
      if (elementConfiguration.isElementSourceCache() && elementConfiguration.isEleCached(eleCode)) {
        if (parentId == null || parentId.equals("")) {
          this.getElementSourceCache(eleCode).addCacheObject(chrId, saveObject);
        } else {
          // 仿照modifyEleByXml的方法,删除此要素的所有缓存
          Map eleSet = getEleSetByCode(eleCode);
          String upperTableName = ((String) eleSet.get("ele_source")).toUpperCase();
          elementSourceCacheMap.remove(this.getElementSourceCacheKey(upperTableName));
          // 删除sys_element此要素的缓存
          sysElementMap.remove(SessionUtil.getLoginYear() + SessionUtil.getRgCode() + eleCode);
        }
      }
    }
    return saveObject;
  }

  public XMLData modifyEle(String element, String chrId, Map fieldInfo) throws Exception {
    XMLData saveObject = eleOpImpl.modifyEle(element, chrId, fieldInfo);
    if (elementConfiguration.isElementSourceCache() && elementConfiguration.isEleCached(element)) {
      this.getElementSourceCache(element).addCacheObject(chrId, saveObject);
      // 仿照modifyEleByXml的方法,删除此要素的所有缓存
      Map eleSet = getEleSetByCode(element);
      String upperTableName = ((String) eleSet.get("ele_source")).toUpperCase();
      elementSourceCacheMap.remove(this.getElementSourceCacheKey(upperTableName));
      // 删除sys_element此要素的缓存
      sysElementMap.remove(SessionUtil.getLoginYear() + SessionUtil.getRgCode() + element);
    }
    if (element.equalsIgnoreCase("AS"))
      engineConfiguration.clearAccountCache();
    return saveObject;
  }

  public List getEleSetByCondition(String condition) {
    return eleOpImpl.getEleSetByCondition(condition);
  }

  /**
   * @author hujinfeng 2009.10.28修改 解决问题： 缓存类ElementDataCache没有用 SET_YEAR
   *         年度进行区分。
   */
  public ElementInfo getElementInfo(String eleCode, String chr_id) {
    if (elementConfiguration.isElementSourceCache() && elementConfiguration.isEleCached(eleCode)) {
      ElementDataCache eleCache = getElementSourceCache(eleCode);

      final String setYear = CommonUtil.getSetYear();
      final String rgCode = CommonUtil.getRgCode();
      ElementCacheKey key = new ElementCacheKey(chr_id, setYear, rgCode);
      return (ElementInfo) eleCache.getCacheItem(key);
    } else {
      return eleOpImpl.getElementInfo(eleCode, chr_id);
    }
  }

  public void clearElementCache() {
    sysElementMap.clear();
  }

  public void clearElementSrcCache() {
    elementSourceCacheMap.clear();
  }

  /*
   * inner class for parse db column statement with alias
   */

  class JavaBeanCacheObjectCloner implements CacheObjectCloner {

    Class elementClass = null;

    public JavaBeanCacheObjectCloner(Class eleClz) {
      this.elementClass = eleClz;
    }

    public Object clone(Object beCloned) {
      try {
        return PropertiesUtil.getData((XMLData) beCloned, elementClass);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

  }

  /**
   * 用于有别名的数据查询,使用克隆处理的方法来进行.
   */
  protected class DbColumnMapperCloner implements CacheObjectCloner {

    /** 使用两个数组来进行别名转换是因为直接使用数组比较快 */
    String[] field = null;

    String[] alias = null;

    int columnLen = 0;

    boolean needHandleAlias = false;

    public DbColumnMapperCloner(String[] columns) {
      if (columns != null && columns.length > 0 && !"*".equals(columns[0])) {
        needHandleAlias = true;

        String[] actualColumns = StringUtil.split(StringUtil.join(columns, ","), ",");

        columnLen = actualColumns.length;
        field = new String[columnLen];
        alias = new String[columnLen];
        for (int i = 0; i < columnLen; i++) {
          if (actualColumns[i] == null)
            continue;

          divField(actualColumns[i].trim(), i);
        }
      }
    }

    public void divField(String trimColumn, int index) {
      if (trimColumn.indexOf(" ") > -1) {
        field[index] = trimColumn.substring(0, trimColumn.indexOf(" ")).toLowerCase();
        alias[index] = trimColumn.substring(trimColumn.lastIndexOf(" ") + 1).toLowerCase();
      } else {
        field[index] = trimColumn.toLowerCase();
        alias[index] = trimColumn.toLowerCase();
      }
    }

    public Object clone(Object beCloned) {
      final XMLData toUseObject = (XMLData) beCloned;
      final XMLData returnObject = new XMLData();

      if (!needHandleAlias)
        return toUseObject.clone();

      for (int i = 0; i < columnLen; i++) {
        returnObject.put(alias[i], toUseObject.get(field[i]));
      }
      return returnObject;
    }
  }

  /**
   * 缓存要素读取
   */
  class ElementDataCacheLoader implements CacheObjectLoader {

    public String eleCode = null;

    public String eleTableName = null;

    public ElementDataCacheLoader(String eleCode, String tableName) {
      this.eleCode = eleCode;
      this.eleTableName = tableName;
    }

    /**
     * 从数据库读取要素.
     */
    public Object loaderElement(Object key) {
      if (key == null)
        return null;
      ElementCacheKey cacheKey = (ElementCacheKey) key;
      return eleOpImpl.getEleByID(eleTableName, cacheKey.getChrId());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperation#getCOADetailCode(java
   * .lang.String, java.lang.String)
   */
  public List getCOADetailCode(String coa_id, String element) {
    return eleOpImpl.getCOADetailCode(coa_id, element);
  }

  public void afterPropertiesSet() throws Exception {
    //    if (!elementConfiguration.isLazyLoadCache()) {
    //      logger.info("采用服务启动加载基础数据缓存模式, 开始加载基础数据缓存");
    //      if (elementConfiguration.getCachedEleList() == null)
    //        return;
    //      for (int i = 0; i < elementConfiguration.getCachedEleList().size(); i++) {
    //        final String upperEleCode = ((String) elementConfiguration.getCachedEleList().get(i)).toUpperCase();
    //        loadELementSourceCache(upperEleCode);
    //        logger.info("加载要素" + upperEleCode + "缓存");
    //      }
    //      logger.info("基础数据缓存加载结束");
    //    }
  }

  public List getFreshViewCol(String ele_code, String old_name, String new_name) {
    return eleOpImpl.getFreshViewCol(ele_code, old_name, new_name);
  }

  public void freshViewCol(String ele_code, String ele_name) {
    eleOpImpl.freshViewCol(ele_code, ele_name);
  }

  public void refreshCCID(String eleName, String chr_id, String chr_code, String chr_name) throws Exception {
    eleOpImpl.refreshCCID(eleName, chr_id, chr_code, chr_name);
  }

  public List getElementData(String chr_id) throws Exception {
    return eleOpImpl.getElementData(chr_id);
  }

  public void removeElementCache(String element) {
    Map eleSet = getEleSetByCode(element);
    String upperTableName = ((String) eleSet.get("ele_source")).toUpperCase();
    elementSourceCacheMap.remove(this.getElementSourceCacheKey(upperTableName));
    elementSourceCacheMap.remove(this.getElementSourceCacheKey(upperTableName.toLowerCase()));
    sysElementMap.remove(SessionUtil.getLoginYear() + SessionUtil.getRgCode() + element);
    sysElementMap.remove(SessionUtil.getLoginYear() + SessionUtil.getRgCode() + element.toLowerCase());
  }

  public String getEleCodeFromTableName(String tableName) {
    // TODO Auto-generated method stub
    return eleOpImpl.getEleCodeFromTableName(tableName);
  }

  // 张晓楠添加，2010-10-26，从湖北包移至主线，start
  /**
   * 获得必录入的字段。
   * 
   * @return XMLData值集对象。
   */
  public List getMustInputFields(String uiCode) {
    return eleOpImpl.getMustInputFields(uiCode);
  }

  /**
   * 根据chr_code获取指定要素的值集。
   * 
   * @param table_name
   *            指定要素表名。
   * @param chr_code
   *            要素代码。
   * @return XMLData值集对象。
   */
  public XMLData getEleByCode(String table_name, String chr_code) {
    return eleOpImpl.getEleByCode(table_name, chr_code);
  }

  // 张晓楠添加，2010-10-26，从湖北包移至主线，end

  /**
   * 根据要素清除要素缓存
   * @param eleCodes 要素简称集合(当为空时刷新全部基础要素)
   * @param setYear 当前年份 不能为空
   * @param rg_code 当前区划 不能为空
   */
  public void clearElementCatchByEleCode(List eleCodes, String setYear, String rg_code) throws Exception {
    if (StringUtil.isEmpty(setYear)) {
      throw new Exception("当前年份不能为空!");
    }
    if (StringUtil.isEmpty(rg_code)) {
      throw new Exception("当前区划不能为空!");
    }
    int eleLegth = 0;
    if (null != eleCodes) {
      eleLegth = eleCodes.size();
    }
    if (elementConfiguration.isElementSourceCache()) {
      //当传入的要素集合为空时刷新全部基础数据
      if (eleLegth == 0) {
        List eleList = eleOpImpl.getEleSetByCondition(" and enabled=1 ");
        int eleSize = eleList.size();
        //循环删除要素的缓存
        for (int i = 0; i < eleSize; i++) {
          ElementSetXMLData eleSetXmlDate = (ElementSetXMLData) eleList.get(i);
          String elementCode = eleSetXmlDate.getEleCode();
          removeElementSourceCacheMapByEleCode(elementCode);
        }
      } else {//按传入的要素简码删除缓存
        for (int i = 0; i < eleLegth; i++) {
          String eleCode = (String) eleCodes.get(i);
          removeElementSourceCacheMapByEleCode(eleCode);
        }
      }
    }
  }

  /**
   * 根据要素简码删除缓存中的基础数据
   * @param eleCode 要素简码
   */
  public void removeElementSourceCacheMapByEleCode(String eleCode) {
    if (elementConfiguration.isEleCached(eleCode)) {
      //删除此要素的所有缓存
      Map eleSet = getEleSetByCode(eleCode);
      String upperTableName = ((String) eleSet.get("ele_source")).toUpperCase();
      elementSourceCacheMap.remove(this.getElementSourceCacheKey(upperTableName));
    }
  }
}
