package gov.df.fap.service.dictionary;

import gov.df.fap.api.dictionary.ElementOperation;
import gov.df.fap.api.dictionary.interfaces.EleChangeListener;
import gov.df.fap.api.dictionary.interfaces.IControlDictionary;
import gov.df.fap.api.dictionary.interfaces.IDictionary;
import gov.df.fap.api.rule.IDataRight;
import gov.df.fap.bean.dictionary.dto.FElementDTO;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.service.dictionary.element.ElementOperationWrapperBO;
import gov.df.fap.service.util.DatabaseAccess;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.xml.XMLData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Dictionary 数据字典服务端组件DTO实现（数据元、要素关联）
 * @version 1.0
 * @author 
 * @since java 1.6.0.34
 */

@Component("sys.dictionaryService")
public class DictionaryBO implements IDictionary, IControlDictionary {
  @Autowired
  @Qualifier("elementOperationWrapper")
  ElementOperation eleOp = null;

  @Autowired
  DDSet setService = null;

  @Autowired
  IDataRight dataRightBO = null;

  @Autowired
  DictionaryRight dicRight = null;

  @Autowired
  RelationDAOService relationDAO = null;

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao;

  /**
   * 要素联动监听器列表
   */
  private List eleChangeListener = new ArrayList();

  /**
   * 要素修改联动配置信息列表
   */
  private List eleChangeSet = new ArrayList();

  /**
   * 要素删除联动配置信息列表
   */
  private List eleDelSet = new ArrayList();

  public void setEleOp(ElementOperation eleOp) {
    this.eleOp = eleOp;
  }

  public void setDicRight(DictionaryRight right) {
    this.dicRight = right;
  }

  /**
   * 查询当前所有表描述信息
   * @return 所有表描述信息列表
   */
  public List findTables() {
    return null;
  }

  /**
   * 根据表类型获取当前的表描述信息列表
   * @param tableType 表类型
   * @return 表描述信息列表
   */
  public List findTables(String tableType) {
    return null;
  }

  /**
   * 根据传入的过滤条件筛选要素配置信息
   * @param condition　过滤条件
   * @return List 要素配置信息列表
   */
  public List getElementSet(String condition) {
    return eleOp.getEleSetByCondition(condition);
  }

  /**
   * ymj 得到刷新视图的列数据
   * @param condition　过滤条件
   * @return List 要素配置信息列表
   */
  public List getFreshViewCol(String ele_code, String old_name, String new_name) {
    return eleOp.getFreshViewCol(ele_code, old_name, new_name);
  }

  /**
   * ymj 刷新视图的列数据
   * @param condition　过滤条件
   * @return List 要素配置信息列表
   */
  public void freshViewCol(String ele_code, String ele_name) {
    eleOp.freshViewCol(ele_code, ele_name);
  }

  /**
   * 根据要素简称查询要素配置信息
   * @param code 要素简称
   * @return 要素配置信息信息
   */
  public XMLData getElementSetByCode(String code) {
    return eleOp.getEleSetByCode(code);
  }

  /**
   * 得到自定义级次的数据
   * @param coa_id
   * @param element
   * @return List
   */
  public List getCOADetailCode(String coa_id, String element) {
    return eleOp.getCOADetailCode(coa_id, element);
  }

  /**
   * 根据要素别名、显示字段、是否需要权限、要素coa、要素关联关系、附加查询条件等查询要素
   * 数据并返回
   * @param element 要素别名
   * @param page 分页对象
   * @param sqlColumn 显示字段
   * @param isNeedRight 是否需要权限标识
   * @param coa_id 要素coa
   * @param ctrlElementValues 要素关联关系
   * @param sPlusSQL 附加查询条件
   * @return 要素值集信息对应XMLData对象列表
   */
  public List findEleValues(String element, FPaginationDTO page, String[] sqlColumn, boolean isNeedRight,
    String coa_id, Map ctrlElementValues, String sPlusSQL) {
    List dataList = new ArrayList();
    try {
      if (eleOp.isElement(element))//如果是要素
      {
        XMLData result = eleOp.getEleByCondition(element, page == null ? 0 : page.getCurrpage(), page == null ? 0
          : page.getPagecount(), sqlColumn, isNeedRight, coa_id, ctrlElementValues, sPlusSQL);
        dataList = result.getRecordList();
        //设置总数
        if (page != null) {
          page.setTotalrows(Integer.parseInt((String) result.get("total_count") == null ? "-1" : (String) result
            .get("total_count")));
        }
        //临时兼容原有设置
        if (dataList.size() > 0) {
          ((Map) dataList.get(0)).put("total_count", result.get("total_count"));
        }
      } else//普通数据元信息
      {
        XMLData meta = this.getMetaDataByCode(element);
        String source = (String) meta.get("source");
        if (source != null && !source.equals("")) {
          dataList = this.findFieldEnumValueList(source);
        } else {
          dataList = this.findFieldEnumValueList(element);
        }
        //设置总数
        if (page != null) {
          page.setTotalrows(dataList.size());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataList;
  }

  /**
   * 根据要素别名、显示字段、是否需要权限、要素coa、要素关联关系、附加查询条件等查询要素
   * 数据并返回
   * @param element 要素别名
   * @param page 分页对象
   * @param sqlColumn 显示字段
   * @param isNeedRight 是否需要权限标识
   * @param coa_id 要素coa
   * @param ctrlElementValues 要素关联关系
   * @param sPlusSQL 附加查询条件
   * @param rgCode 区划码
   * @return 要素值集信息对应XMLData对象列表
   */
  public List findEleValuesRG(String element, FPaginationDTO page, String[] sqlColumn, boolean isNeedRight,
    String coa_id, Map ctrlElementValues, String sPlusSQL, String rgCode) {
    boolean isExternalRg = false;
    String oldRgCode = SessionUtil.getUserInfoContext().getRgCode();
    try {
      if (!StringUtils.isEmpty(rgCode)) {
        SessionUtil.getUserInfoContext().setRgCode(rgCode);
        isExternalRg = true;
      }
      return findEleValues(element, page, sqlColumn, isNeedRight, coa_id, ctrlElementValues, sPlusSQL);
    } finally {
      if (isExternalRg)
        SessionUtil.getUserInfoContext().setRgCode(oldRgCode);
    }
  }

  /**
   * 根据要素别名、显示字段、是否需要权限、要素coa、要素关联关系、附加查询条件等查询要素
   * 数据并返回
   * @param element 要素别名
   * @param page 分页对象
   * @param sqlColumn 显示字段
   * @param isNeedRight 是否需要权限标识
   * @param coa_id 要素coa
   * @param ctrlElementValues 要素关联关系
   * @param sPlusSQL 附加查询条件
   * @return 要素值集信息对应XMLData对象列表
   */
  public List findEleValuesWithRgCode(String element, FPaginationDTO page, String[] sqlColumn, boolean isNeedRight,
    String coa_id, Map ctrlElementValues, String sPlusSQL, String rgCode) {
    String oldRgCode = CommonUtil.getRgCode();
    //特殊处理，将传入的rg作为区划处理数据
    if (!StringUtils.isEmpty(rgCode))
      SessionUtil.getUserInfoContext().getContext().put("rg_code", rgCode);

    List dataList = new ArrayList();
    try {
      if (eleOp.isElement(element))//如果是要素
      {
        XMLData result = eleOp.getEleByConditionWithRgCode(element, page == null ? 0 : page.getCurrpage(),
          page == null ? 0 : page.getPagecount(), sqlColumn, isNeedRight, coa_id, ctrlElementValues, sPlusSQL);
        dataList = result.getRecordList();
        //设置总数
        if (page != null) {
          page.setTotalrows(Integer.parseInt((String) result.get("total_count") == null ? "-1" : (String) result
            .get("total_count")));
        }
        //临时兼容原有设置
        if (dataList.size() > 0) {
          ((Map) dataList.get(0)).put("total_count", result.get("total_count"));
        }
      } else//普通数据元信息
      {
        XMLData meta = this.getMetaDataByCode(element);
        String source = (String) meta.get("source");
        if (source != null && !source.equals("")) {
          dataList = this.findFieldEnumValueList(source);
        } else {
          dataList = this.findFieldEnumValueList(element);
        }
        //设置总数
        if (page != null) {
          page.setTotalrows(dataList.size());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      SessionUtil.getUserInfoContext().getContext().put("rg_code", oldRgCode);
    }
    return dataList;
  }

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
  * @throws IOException 
  *
  */
  public byte[] findEleValuesAsBytes(String element, FPaginationDTO page, boolean isNeedRight, String coa_id,
    Map ctrlElementValues, String sPlusSQL, Class elementObjectClass) throws IOException {
    List data = this.findEleValuesAsObj(element, page, isNeedRight, coa_id, ctrlElementValues, sPlusSQL,
      elementObjectClass);
    if (data.size() == 0)
      return null;

    // 建立字节数组输出流
    ByteArrayOutputStream byteOS = null;
    // 建立gzip压缩输出流
    GZIPOutputStream gzout = null;
    // 建立对象序列化输出流
    ObjectOutputStream out = null;
    // 建立字节数组输出流
    byteOS = new ByteArrayOutputStream();
    // 建立gzip压缩输出流
    gzout = new GZIPOutputStream(byteOS);
    out = new ObjectOutputStream(gzout);
    out.writeObject(data);
    gzout.finish();
    out.flush();
    out.close();
    gzout.close();
    byteOS.close();
    return byteOS.toByteArray();
  }

  /**
  *
  * 根据指定Class返回要素对象列表。
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
  * @return  根据指定的Class返回要素对象。
  *
  */
  public List findEleValuesAsObj(String element, FPaginationDTO page, boolean isNeedRight, String coa_id,
    Map ctrlElementValues, String sPlusSQL, Class elementObjectClass) {
    try {
      if (eleOp.isElement(element)) {//如果是要素 
        List dataList = /*dataList = */eleOp.getEleByConditionAsObj(element, page, isNeedRight, coa_id,
          ctrlElementValues, sPlusSQL, elementObjectClass);

        //设置总数
        if (page != null) {
          page.setTotalrows(dataList.size());
        }
        return dataList;
      } else {//普通数据元信息
        throw new RuntimeException("查询的" + element + "不是要素");
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 将 findEleValues 压缩成byte 传递
   * @param element 要素别名
   * @param page 分页对象
   * @param sqlColumn 显示字段
   * @param isNeedRight 是否需要权限标识
   * @param coa_id 要素coa
   * @param ctrlElementValues 要素关联关系
   * @param sPlusSQL 附加查询条件
   * @return 要素值集信息对应XMLData对象列表
  * @throws IOException 
   */
  public byte[] findEleValuesBytes(String element, FPaginationDTO page, String[] sqlColumn, boolean isNeedRight,
    String coa_id, Map ctrlElementValues, String sPlusSQL) throws IOException {
    List dataList = this.findEleValues(element, page, sqlColumn, isNeedRight, coa_id, ctrlElementValues, sPlusSQL);
    if (dataList.size() == 0)
      return null;

    // 建立字节数组输出流
    ByteArrayOutputStream byteOS = null;
    // 建立gzip压缩输出流
    GZIPOutputStream gzout = null;
    // 建立对象序列化输出流
    ObjectOutputStream out = null;
    // 建立字节数组输出流
    byteOS = new ByteArrayOutputStream();
    // 建立gzip压缩输出流

    gzout = new GZIPOutputStream(byteOS);
    out = new ObjectOutputStream(gzout);
    out.writeObject(dataList);
    out.flush();
    out.close();
    gzout.close();
    byteOS.close();
    return byteOS.toByteArray();

  }

  /**
   * 根据要素别名、是否需要权限查询要素数据并返回
   * @param element 要素别名
   * @param page 分页对象
   * @param isNeedRight 是否需要权限标识
   * @return 要素值集信息对应XMLData对象列表
   */
  public List findEleValues(String element, FPaginationDTO page, boolean isNeedRight) {
    return findEleValues(element, page, null, isNeedRight, null, null, "order by chr_code");
  }

  /**
   * 根据要素别名、要素coa查询要素数据并返回
   * @param element 要素别名
   * @param page 分页对象
   * @param coa_id 要素coa
   * @return 要素值集信息对应XMLData对象列表 
   */
  public List findEleValues(String element, FPaginationDTO page, String coa_id) {
    return findEleValues(element, page, null, true, coa_id, null, "order by chr_code");
  }

  /**
   * 根据要素别名、是否需要权限、要素关联关系查询要素数据并返回
   * @param element 要素别名
   * @param page 分页对象
   * @param isNeedRight 是否需要权限标识
   * @param ctrlElementValues 要素关联关系
   * @return 要素值集信息对应XMLData对象列表 
   */
  public List findEleValues(String element, FPaginationDTO page, boolean isNeedRight, Map ctrlElementValues) {
    return findEleValues(element, page, null, isNeedRight, null, ctrlElementValues, "order by chr_code");
  }

  /**
   * 根据要素别名和唯一显示码准确定位一条要素值集信息
   * @param element 要素别名
   * @param code 唯一显示码
   * @return 要素值集信息对应FElementDTO对象
   */
  public FElementDTO findEleValueByCode(String element, String code) {
    FElementDTO returnDTO = new FElementDTO();
    List list = null;
    try {
      list = findEleValues(element, null, null, false, null, null, " and chr_code = '" + code + "'");
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (list == null || list.size() == 0) {
      return null;
    } else {
      returnDTO.putAll((Map) list.get(0));
      return returnDTO;
    }
  }

  /**
   * 根据要素别名和唯一显示码准确定位一条要素值集信息
   * @param element 要素别名
   * @param name 唯一显示码
   * @return 要素值集信息对应FElementDTO对象
   */
  public FElementDTO findEleValueByName(String element, String name) {
    FElementDTO returnDTO = new FElementDTO();
    List list = null;
    try {
      list = findEleValues(element, null, null, false, null, null, " and chr_name = '" + name + "'");
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (list == null || list.size() == 0) {
      return null;
    } else {
      returnDTO.putAll((Map) list.get(0));
      return returnDTO;
    }
  }

  /**
   * 根据要素别名和唯一显示码获取要素值集信息ID
   * @param element 要素别名
   * @param code 唯一显示码
   * @return 要素值集唯一ID
   */
  public String getEleValueIDByAlias(String element, String code) {
    List list = null;
    try {
      list = findEleValues(element, null, new String[] { "chr_id" }, false, null, null, " and chr_code = '" + code
        + "'");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return (list == null || list.size() == 0) ? "" : (((XMLData) list.get(0)).get("chr_id") == null ? ""
      : (String) ((XMLData) list.get(0)).get("chr_id"));
  }

  /**
   * 根据要素别名和唯一ID获取要素值集信息
   * @param element 要素别名
   * @param id 唯一ID
   * @return 要素值集信息对应XMLData对象
   */
  public FElementDTO findEleValueById(String element, String id) {
    FElementDTO return_dto = new FElementDTO();
    List list = null;
    try {
      list = findEleValues(element, null, null, false, null, null, " and chr_id = '" + id + "'");
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (list == null || list.size() == 0) {
      return null;
    } else {
      return_dto.putAll((Map) list.get(0));
      return return_dto;
    }
  }

  /**
   * 插入一条要素值集信息
   * @param element 要素别名
   * @param fieldInfo 字段值对组合
   * @return 插入操作是否成功
   * @throws Exception 异常分析
   */
  public Map insertValue(String element, Map fieldInfo) throws Exception {
    if (fieldInfo == null || fieldInfo.size() == 0) {
      throw new Exception("传入数据对象错误,无法执行插入操作!");
    }
    XMLData xml = new XMLData();
    xml.putAll(fieldInfo);
    xml.put("element_code", element);
    return eleOp.insertEle(xml);
  }

  /**
   * 插入一条要素值集信息
   * @param element 要素别名
   * @param fieldInfo 字段值对组合
   * @return 插入操作是否成功
   * @throws Exception 异常分析
   */
  public Map insertValueRG(String element, Map fieldInfo, String rgCode) throws Exception {
    boolean isExternalRg = false;
    String oldRgCode = SessionUtil.getUserInfoContext().getRgCode();
    try {
      if (!StringUtils.isEmpty(rgCode)) {
        SessionUtil.getUserInfoContext().setRgCode(rgCode);
        isExternalRg = true;
      }
      return insertValue(element, fieldInfo);
    } finally {
      if (isExternalRg)
        SessionUtil.getUserInfoContext().setRgCode(oldRgCode);
    }
  }

  /**
   * 修改一条要素值集信息
   * @param element 要素别名
   * @param id 要素主键
   * @param fieldInfo 字段值对组合
   * @return 修改操作是否成功
   * @throws Exception 异常分析
   */
  public Map updateEleValue(String element, String id, Map fieldInfo) throws Exception {
    return eleOp.modifyEle(element, id, fieldInfo);
  }

  /**
   *  修改一条要素值集信息
   * @param element 要素别名
   * @param id 要素主键
   * @param fieldInfo 字段值对组合
   * @return 修改操作是否成功
   * @throws Exception 异常分析
   */
  public Map updateEleValueRG(String element, String id, Map fieldInfo, String rgCode) throws Exception {
    //    boolean isExternalRg = false;
    //    String oldRgCode = SessionUtil.getUserInfoContext().getRgCode();
    try {
      if (!StringUtils.isEmpty(rgCode)) {
        SessionUtil.getUserInfoContext().setRgCode(rgCode);
        //        isExternalRg = true;
      }
      return updateEleValue(element, id, fieldInfo);
    } finally {
      //      if (isExternalRg)
      //        SessionUtil.getUserInfoContext().setRgCode(oldRgCode);
    }
  }

  /**
   *  修改一条要素值集信息
   * @param element 要素别名
   * @param id 要素主键
   * @param fieldInfo 字段值对组合
   * @return 修改操作是否成功
   * @throws Exception 异常分析
   */
  public Map updateEleValueRGSetYear(String element, String id, Map fieldInfo, String rgCode, String setYear)
    throws Exception {
    boolean isExternalRg = false;
    String oldRgCode = SessionUtil.getUserInfoContext().getRgCode();
    boolean isExternalSetYear = false;
    String oldSetYear = SessionUtil.getUserInfoContext().getSetYear();
    try {
      if (!StringUtils.isEmpty(rgCode)) {
        SessionUtil.getUserInfoContext().setRgCode(rgCode);
        isExternalRg = true;
      }
      if (!StringUtils.isEmpty(setYear)) {
        SessionUtil.getUserInfoContext().setSetYear(setYear);
        isExternalSetYear = true;
      }
      return updateEleValue(element, id, fieldInfo);
    } finally {
      if (isExternalRg)
        SessionUtil.getUserInfoContext().setRgCode(oldRgCode);
      if (isExternalSetYear)
        SessionUtil.getUserInfoContext().setSetYear(oldSetYear);
    }
  }

  /**
   * 增加一条要素值集信息
   * @param element 要素别名
   * @param fieldInfo 字段值对组合
   * @param rgCode 区划码
   * @param setyear 年度
   * @return 修改后的数据对象
   * @throws Exception 异常分析
   */
  public Map insertEleValueRGSetYear(String element, Map fieldInfo, String rgCode, String setYear) throws Exception {
    boolean isExternalRg = false;
    boolean isExternalSetYear = false;
    String oldRgCode = SessionUtil.getUserInfoContext().getRgCode();
    String oldSetYear = SessionUtil.getUserInfoContext().getSetYear();
    try {
      if (!StringUtils.isEmpty(rgCode)) {
        SessionUtil.getUserInfoContext().setRgCode(rgCode);
        isExternalRg = true;
      }
      if (!StringUtils.isEmpty(setYear)) {
        SessionUtil.getUserInfoContext().setSetYear(setYear);
        isExternalSetYear = true;
      }
      return insertValue(element, fieldInfo);
    } finally {
      if (isExternalRg)
        SessionUtil.getUserInfoContext().setRgCode(oldRgCode);
      if (isExternalSetYear)
        SessionUtil.getUserInfoContext().setSetYear(oldSetYear);
    }
  }

  /**
   * 根据要素值集唯一ID删除要素值集信息
   * @param element 要素别名
   * @param id 唯一ID
   * @return 删除是否成功
   * @throws Exception 异常分析
   */
  public boolean deleteEleValue(String element, String id) throws Exception {
    String eleCode = "";
    FElementDTO eleDto = this.findEleValueById(element, id);
    if (eleDto != null)
      eleCode = eleDto.getChr_code();
    this.validateDelete(element, id, eleCode);
    return eleOp.deleteEle(element, id);
  }

  /**
   * 根据要素值集唯一ID删除要素值集信息
   * @param element 要素别名
   * @param id 唯一ID
   * @return 删除是否成功
   * @throws Exception 异常分析
   */
  public boolean deleteEleValueRG(String element, String id, String rgCode) throws Exception {
    boolean isExternalRg = false;
    String oldRgCode = SessionUtil.getUserInfoContext().getRgCode();
    try {
      if (!StringUtils.isEmpty(rgCode)) {
        SessionUtil.getUserInfoContext().setRgCode(rgCode);
        isExternalRg = true;
      }
      return deleteEleValue(element, id);
    } finally {
      if (isExternalRg)
        SessionUtil.getUserInfoContext().setRgCode(oldRgCode);
    }
  }

  /**
   * 根据要素别名和唯一显示码获取值集历史信息
   * @param element 要素别名
   * @param code 唯一显示码
   * @return 要素值集历史信息对应XMLData对象列表
   */
  public List findHistoryValuesByCode(String element, String code) {
    return null;
  }

  /**
   * 根据要素表别名、要素值查询处理后的查询结果。
   * @param inObj 传入的map
   * @param tableAlias 要素表别名
   * @return 查询结果xml
   */
  public String getCondition(FElementDTO elementDto, String tableAlias) {
    try {
      return eleOp.getCondition(elementDto, tableAlias);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 返回根据分页当前页和当前用户的权限过滤查询预算单位数据，
   * @param page-------------分页当前面
   * @param column-----------查询字段
   * @param strCondition-------过滤条件
   * @throws Exception---------错误信息
   */
  public XMLData findUserEnterpariseValues(FPaginationDTO page, String[] column, String strCondition) throws Exception {
    String strByDataRight = dataRightBO.getSqlBusiRightByUserNoCCID(CommonUtil.getUserId(), "alias_EN");
    strByDataRight = strByDataRight.replaceAll("en_id", "chr_id");
    strByDataRight = strByDataRight.replaceAll("EN_id", "chr_id") + strCondition;
    return eleOp.getEleByCondition("EN", page.getCurrpage(), page.getPagecount(), column, true, null, null,
      strByDataRight);
  }

  /**
   * 批量保存新增数据
   * @param batchData 由XMLData组成的List
   * @param tableName 业务表
   * @param primaryKey 业务表主键
   * @throws Exception
   */
  public void batchInsert(List batchData, String tableName) throws Exception {
    Connection conn = null;
    Statement ps = null;
    Session session = null;
    try {
      session = dao.getSession();
      if (session == null) {
        throw new Exception("数据库连接已关闭,无法使用");
      }
      conn = session.connection();

      //批处理开始
      ps = conn.createStatement();
      Map fieldMap = DatabaseAccess.getFieldMap(tableName);

      for (int i = 0; batchData != null && i < batchData.size(); i++) {
        //插入明细单
        Map map = (Map) batchData.get(i);
        ps.addBatch(DatabaseAccess.getInsetSql(tableName, fieldMap, map));
      }
      //批提交
      ps.executeBatch();
      ps.clearBatch();
      ps.close();
    } catch (Exception e) {
      throw e;
    } finally {
      try {
        if (ps != null) {
          ps.close();
          ps = null;
        }
        if (session != null) {
          dao.closeSession(session);
        }
      } catch (Exception e) {
        System.out.println(e.getMessage());
        //				Log.error(e.getMessage());
      }
    }
  }

  /**
   * 新增一个预算单位（带权限）， 
   * @param 要新增的预算单位信息  分页当前页
   * @return 新增成功的预算单位信息
   * @throws Exception 操作失败原因 
   */
  public XMLData insertEnterpriseWithRight(XMLData fieldInfo) throws Exception {
    Map result = null;
    if (StringUtils.isEmpty((String) fieldInfo.get("rg_code")))
      result = insertValue("EN", fieldInfo);
    else
      result = insertValueRG("EN", fieldInfo, fieldInfo.get("rg_code").toString());

    List list = new ArrayList();
    XMLData user_enterprise = new XMLData();
    user_enterprise.put("user_id", CommonUtil.getUserId());
    user_enterprise.put("en_id", result.get("chr_id"));
    list.add(user_enterprise);
    dataRightBO.saveUserRight(list);
    return (XMLData) result;
  }

  /**
    * 查询数据元对应的字段信息
    * @param code 数据元字段
    * @return 字段信息
    */
  public XMLData getMetaDataByCode(String code) {
    return setService.doMetaDataQueryAsXMLData(code);
  }

  /**
   * 根据传入的字段名获得本字段枚举值
   * @param field_code 字段名
   * @return String 枚举值字符串
   */
  public String findFieldEnumValueString(String field_code) {
    return setService.getFieldEnumValueString(field_code);
  }

  /**
   * 根据传入的字段名获得本字段枚举值
   * @param field_code 字段名
   * @return List 枚举值列表,List中的Map已模拟成要素模式chr_id,chr_code,chr_name
   */
  public List findFieldEnumValueList(String field_code) {
    return setService.getFieldEnumValueList(field_code);
  }

  /**
  * 插入枚举信息项（累加的方式）
  * @param fieldCode 名称，对应sys_enumerate中的field_code字段
  * @param value  由Map组成，键名chr_code,chr_name分别对应表sys_enumerate中的enu_code和enu_name字段
  * @return 操作是否成功
  * @throws Exception 异常
  */
  public boolean insertFieldEnumValue(String fieldCode, List value) throws Exception {
    return setService.insertFieldEnumValue(fieldCode, value);
  }

  /**
   * 插入枚举信息项（全部替换）
   * @param fieldCode 名称，对应sys_enumerate中的field_code字段
   * @param value  由Map组成，键名chr_code,chr_name分别对应表sys_enumerate中的enu_code和enu_name字段
   * @return 操作是否成功
   * @throws Exception 异常
   */
  public boolean replaceFieldEnumValue(String fieldCode, List value) throws Exception {
    return setService.replaceFieldEnumValue(fieldCode, value);
  }

  /**
  * 删除枚举信息项
  * @param fieldCode 名称，对应sys_enumerate中的field_code字段
  * @param value  对应表sys_enumerate中的enu_code字段
  * @return 操作是否成功
  * @throws Exception 异常
  */
  public boolean deleteFieldEnumValue(String fieldCode, String value) throws Exception {
    return setService.deleteFieldEnumValue(fieldCode, value);
  }

  /**
   * 往数据元表中插入一条数据
   * @param fieldInfo map值对
   * @return 插入后的数据元XMLData对象
   * @throws Exception
   */
  public Map insertMetaData(Map fieldInfo) throws Exception {
    return setService.insertMetaData(fieldInfo);
  }

  /**
   * 根据被关联要素编码查询主控要素编码
   * @param slaveElementCode 被关联要素编码
   * @return 主控要素编码对象列表
   */
  public List getPrimaryRelationElement(String slaveElementCode) {
    return setService.doPrimaryRelationQuery(slaveElementCode);

  }

  /**
   * 根据主控要素编码查询被关联要素编码
   * @param primaryElementCode 主控要素编码
   * @return 被关联要素对象列表
   */
  public List getSlaveRelationElement(String primaryElementCode) {
    return setService.doSlaveRelationQuery(primaryElementCode);
  }

  /**
   * 实现返回基础要素表的数据权限sql语句
   * @param userid-------------用户id
   * @param roleid-------------角色id
   * @param elemcode-----------要素简称
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */
  public String getSqlElemRight(String userid, String roleid, String elemcode, String tablealias) throws Exception {
    return dicRight.getSqlElemRight(userid, roleid, elemcode, tablealias);
  }

  public IDataRight getDataRightBO() {
    return dataRightBO;
  }

  public void setDataRightBO(IDataRight dataRightBO) {
    this.dataRightBO = dataRightBO;
  }

  public DDSet getSetService() {
    return setService;
  }

  public void setSetService(DDSet setService) {
    this.setService = setService;
  }

  public Map updateEleValueAndRefreshCCID(String element, String id, Map fieldInfo) throws Exception {
    Map result = eleOp.modifyEle(element, id, fieldInfo);
    eleOp.refreshCCID(element, id, fieldInfo.get("chr_code").toString(), fieldInfo.get("chr_name").toString());
    return result;
  }

  public List getElementData(String chr_id) throws Exception {
    // TODO Auto-generated method stub
    return eleOp.getElementData(chr_id);
  }

  public void removeElementCache(String tableName) {
    if (eleOp instanceof ElementOperationWrapperBO) {
      String element = eleOp.getEleCodeFromTableName(tableName);
      if (!element.equals("")) {
        ElementOperationWrapperBO op = (ElementOperationWrapperBO) eleOp;
        op.removeElementCache(element);
      }
    }
  }

  public void removeElementCacheByEleCode(String eleCode) {
    if (eleOp instanceof ElementOperationWrapperBO && eleCode != null && !eleCode.equals("")) {

      ElementOperationWrapperBO op = (ElementOperationWrapperBO) eleOp;
      op.removeElementCache(eleCode.toLowerCase());

    }
  }

  public boolean isElement(String element) {
    // TODO Auto-generated method stub
    return eleOp.isElement(element);
  }

  /**
   * 返回通过用户的预算单位机构权限过滤基础数据的sql语句
   * 
   * @param userid-------------用户id
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */
  public String getEnOrgRightSqlByUser(String userid, String tablealias) {
    return dicRight.getEnOrgRightSqlByUser(userid, tablealias);
  }

  // 张晓楠添加，2010-10-18，start
  /**
   * 判断当预算单位修改时，此时的预算单位属性是否可以修改。
   * @param fieldInfo 
   * @return 
   */
  public boolean checkIsReform(Map fieldInfo) {
    return eleOp.checkIsReform(fieldInfo);
  }

  // 张晓楠添加，2010-10-18，end

  // 张晓楠添加，2010-10-26，从湖北包移至主线，start
  /**
   * 判断当预算单位修改时，此时的预算单位属性是否可以修改。
   * @param fieldInfo 
   * @return 
   */
  public String checkNecessaryFields(String element, String code) {
    XMLData eleSet = eleOp.getEleSetByCode(element);
    String source = (String) eleSet.get("ele_source");
    String eleName = (String) eleSet.get("ele_name");
    String uiCode = (String) eleSet.get("level_name");
    uiCode = uiCode.substring(0, uiCode.indexOf('&'));
    List mustInputs = eleOp.getMustInputFields(uiCode);
    XMLData elem = eleOp.getEleByCode(source, code);
    StringBuffer result = new StringBuffer();
    int size = mustInputs.size();

    for (int i = 0; i < size; i++) {
      Map map = (Map) mustInputs.get(i);
      Object val = elem.get((String) map.get("name"));

      if (val == null || val.toString().length() == 0) {
        String title = (String) map.get("title");
        int lastIdx = title.length() - 1;
        char lastChar = title.charAt(lastIdx);

        if (':' == lastChar || '：' == lastChar) {
          title = title.substring(0, lastIdx);
        }

        result.append(title).append("、");
      }
    }

    if (result.length() > 0) {
      result.insert(0, code + eleName + "基础数据不完整：");
      result.deleteCharAt(result.length() - 1);
      return result.toString();
    }

    return null;
  }

  // 张晓楠添加，2010-10-26，从湖北包移至主线，end

  /**
   * 添加要素联动更新接口
   * @param elechange 添加的要素联动更新接口
   */
  public void addEleChangeListener(EleChangeListener elechange) {

    if (eleChangeListener == null)
      eleChangeListener = new ArrayList();
    if (eleChangeListener.contains(elechange))
      eleChangeListener.remove(elechange);
    eleChangeListener.add(elechange);
    System.out.println("eleChangeListener=" + eleChangeListener);
  }

  /**
     * 基础数据删除后进行的联动删除
     * @return 联动删除后的提示信息
     * 
     */
  public String fireEleDelete(List delEle) {
    StringBuffer hint = new StringBuffer();
    if (this.eleChangeListener != null && !eleChangeListener.isEmpty()) {

      for (int i = 0; i < this.eleChangeListener.size(); i++) {
        try {
          ((EleChangeListener) eleChangeListener.get(i)).deleteEleAction(delEle);
          addSuccessHint(((EleChangeListener) eleChangeListener.get(i)).getDelSet(), hint);
        } catch (Exception e) {
          hint.append(e.getMessage() + "\n");
        }
      }

    }

    return hint.toString();
  }

  /**
     * 基础数据修改后进行的联动处理
     * @param element 要素别名，如BS，EN
   * @param id 修改要素id
   * @param oldCode 要素修改前的code
   * @param newCode 修改后的编码
   * @param newName 修改后的名称
     * @return 联动更新的提示信息
     * @throws Exception 更新失败后异常提示
     */
  public String fireEleModify(String element, String id, String oldCode, String oldName, String newCode, String newName) {
    StringBuffer hint = new StringBuffer();
    if (this.eleChangeListener != null && !eleChangeListener.isEmpty()) {

      for (int i = 0; i < this.eleChangeListener.size(); i++) {
        try {
          ((EleChangeListener) eleChangeListener.get(i)).updateEleAction(element, id, oldCode, oldName, newCode,
            newName);
          addSuccessHint(((EleChangeListener) eleChangeListener.get(i)).getChangeSet(), hint);
        } catch (Exception e) {
          hint.append(e.getMessage() + "\n");
        }
      }

    }

    return hint.toString();
  }

  /**
     * 得到基础数据删除后联动更新列表
     * @return list中对象是字符串，删除的配置列表
     */
  public List getEleDeleteSet() {
    if (eleDelSet.isEmpty())
      initEleChangeList();
    return this.eleDelSet;
  }

  /**
     * 得到基础数据修改后联动更新列表
     * @return list中对象是字符串，修改的配置列表
     */
  public List getEleModifySet() {
    if (eleChangeSet.isEmpty())
      initEleChangeList();
    return this.eleChangeSet;
  }

  private void validateDelete(String element, String id, String code) throws Exception {
    if (this.eleChangeListener != null && !eleChangeListener.isEmpty()) {

      for (int i = 0; i < this.eleChangeListener.size(); i++) {
        ((EleChangeListener) eleChangeListener.get(i)).isEleCanDelete(element, id, code);
      }
    }
  }

  /**
   * 初始化更新联动刷新列表
   *
   */
  private void initEleChangeList() {
    if (this.eleChangeListener != null && !eleChangeListener.isEmpty()) {
      List changeSet = null;
      List del = null;
      for (int i = 0; i < this.eleChangeListener.size(); i++) {

        changeSet = ((EleChangeListener) eleChangeListener.get(i)).getChangeSet();
        if (changeSet != null)
          this.eleChangeSet.addAll(changeSet);
        del = ((EleChangeListener) eleChangeListener.get(i)).getDelSet();
        if (del != null)
          this.eleDelSet.addAll(del);

      }
    }
  }

  /**
   * 添加刷新提示信息
   * @param elechange
   * @param hint
   */
  private void addSuccessHint(List setList, StringBuffer hint) {
    if (setList != null && !setList.isEmpty()) {
      for (int i = 0; i < setList.size(); i++) {
        hint.append("刷新" + setList.get(i));
        hint.append("成功!\n");
      }
    }
  }

  public boolean checkEbankNo(String enterpriseNo, String rg_code, String set_year) {

    String sql = "select count(t.chr_id) num from ELE_ENTERPRISE t where t.ebank_enterprise_no=? and t.rg_code=? and t.set_year=?";
    Object[] param = { enterpriseNo, rg_code, set_year };
    List list = dao.findBySql(sql, param);
    if (list.size() > 0 && Integer.parseInt(((Map) list.get(0)).get("num").toString()) > 0) {
      return true;
    }
    return false;
  }

  public boolean checkEbankData(String en_code, String rg_code, String set_year) {
    String sql = "select count(1) num from pay_voucher a,(select ccid,en_code from gl_ccids) b where a.ccid=b.ccid and b.en_code=? and is_end=? and a.rg_code=? and a.set_year=?";
    Object[] param = { en_code, "0", rg_code, set_year };
    List list = dao.findBySql(sql, param);
    if (list.size() > 0 && Integer.parseInt(((Map) list.get(0)).get("num").toString()) > 0) {
      return true;
    }
    return false;
  }

  /**
   * 根据要素清除要素缓存
   * @param eleCodes 要素简称集合
   * @param setYear 年度
   * @param rg_code 区划
   */
  public void clearElementCatchByEleCode(List eleCodes, String setYear, String rg_code) throws Exception {
    eleOp.clearElementCatchByEleCode(eleCodes, setYear, rg_code);
  }

  public boolean delRelation(String relation_id) {
    try {
      return relationDAO.deleteRelation(relation_id);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return false;
    }
  }

}
