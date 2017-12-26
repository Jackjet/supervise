/*
 * @(#)CDD_Element.java	1.0 16/03/06
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.service.relation;

import gov.df.fap.bean.dictionary.dto.FElementDTO;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * CDD_Element 数据字典客户端组件接口 调用数据字典组件服务端远程接口，
 * 对要素管理表（SYS_ELEMENT）及要素表（ELE_*）进行增、删、查、改操作。
 * 
 * @version 1.0
 * @author 
 * @since java 1.6.34
 */
@Service
public class CDD_Element extends CommonOperation {

  /**
   * 根据要素管理表唯一ID查询数据管理表数据。
   * @param id  要素管理表唯一ID
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getEleSetById(String id) {
    return getEleSetByCondition("and chr_id='" + id + "'");
  }

  /**
   * 根据要素表编码查询数据管理表数据。
   * @param code 要素表编码
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getEleSetByCode(String code) {
    return (XMLData) getIDictionary().getElementSetByCode(code);
  }

  /**
   * 根据要素表表名查询数据管理表数据
   * @param source 要素表名
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getEleSetBySource(String source) {
    return getEleSetByCondition("and ele_source='" + source + "'");
  }

  /**
   * 得到自定义级次的数据
   * @param coa_id
   * @param element
   * @return List
   */
  public List getCOADetailCode(String coa_id, String element) {
    return getIDictionary().getCOADetailCode(coa_id, element);
  }

  /**
   * 根据附加过滤条件查询要素管理表
   * @param condition 附加过滤条件
   * @return 查询结果
   */
  public XMLData getEleSetByCondition(String condition) {
    List list = getIDictionary().getElementSet(condition);
    XMLData data = new XMLData();
    data.put("row", list);
    data.put("total_count", String.valueOf(list.size()));
    return data;
  }

  /**
   * ymj 得到刷新视图的列数据
   * @param condition 附加过滤条件
   * @return 查询结果
   */
  public XMLData getFreshViewCol(String ele_code, String old_name, String new_name) {
    List list = getIDictionary().getFreshViewCol(ele_code, old_name, new_name);
    XMLData data = new XMLData();
    data.put("row", list);
    data.put("total_count", String.valueOf(list.size()));
    return data;
  }

  /**
   * ymj 刷新视图的列数据
   * @param condition 附加过滤条件
   * @return 查询结果
   */
  public void freshViewCol(String ele_code, String ele_name) {
    getIDictionary().freshViewCol(ele_code, ele_name);

  }

  public List getAllElements() {
    return getIDictionary().getElementSet(
      " and enabled=1 and is_system=1 and rg_code='" + SessionUtil.getRgCode() + "'");
  }

  /**
   * 要素管理表数据插入，返回插入数据的唯一ID
   * @param fieldInfo 值集
   * @return 返回插入数据的唯一ID
   * @throws Exception 操作失败原因
   */
  public XMLData insertEleSet(Map fieldInfo) throws Exception {
    return insertData("SYS_ELEMENT" + Tools.addDbLink(), fieldInfo);
  }

  /**
   * 根据要素管理唯一ID修改要素管理表数据
   * @param id 要素管理唯一ID
   * @param fieldInfo 要素管理值集（字段名 - 字段值）
   * @return 数据修改操作是否成功
   * @throws Exception 数据修改操作失败原因
   */
  public XMLData modifyEleSet(String id, Map fieldInfo) throws Exception {
    return modifyData("SYS_ELEMENT" + Tools.addDbLink(), "CHR_ID", id, fieldInfo);
  }

  /**
   * 根据要素管理唯一ID删除要素管理数据
   * @param id 要素管理唯一ID
   * @return 数据删除操作是否成功
   * @throws Exception 数据删除操作失败原因
   */
  public boolean deleteEleSet(String id) throws Exception {
    return getIDDElement().deleteEleSet(id);
  }

  /**
   * 根据要素表别名、分页当前页、是否需要权限过滤查询要素表数据， 
   * 返回分页当前页码的查询结果，分页页大小默认为20。
   * @param element 要素表别名
   * @param pageIndex  分页当前页
   * @param isNeedRight 是否需要权限过滤
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getEle(String element, int pageIndex, boolean isNeedRight) {
    return getEle(element, pageIndex, 0, null, isNeedRight, null, null, "order by chr_code");
  }

  /**
   * 根据要素表别名、分页当前页、分页页大小、显示字段、 是否需要权限过滤、要素COA、要素关联关系、查询条件字符串查询要素表数据，
   * 按显示字段返回分页处理后的查询结果。
   * @param element 要素表别
   * @param pageIndex 分页当前页
   * @param pageCount 分页页大小
   * @param displayField 所有需要显示的字段名
   * @param isNeedRight 是否需要权限过滤
   * @param coa_id 要素表COA
   * @param relation 要素关联关系
   * @param condition 查询条件字符串
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getEle(String element, int pageIndex, int pageCount, String displayField[], boolean isNeedRight,
    String coa_id, Map relation, String condition) {
    FPaginationDTO page = new FPaginationDTO();
    page.setCurrpage(pageIndex);
    page.setPagecount(pageCount);
    List list = getIDictionary().findEleValues(element, page, displayField, isNeedRight, coa_id, relation, condition);
    XMLData data = new XMLData();
    data.put("row", list);
    return data;
  }

  public XMLData getEleWithRgCode(String element, int pageIndex, int pageCount, String displayField[],
    boolean isNeedRight, String coa_id, Map relation, String condition, String rgCode) {
    FPaginationDTO page = new FPaginationDTO();
    page.setCurrpage(pageIndex);
    page.setPagecount(pageCount);
    List list = getIDictionary().findEleValuesWithRgCode(element, page, displayField, isNeedRight, coa_id, relation,
      condition, rgCode);
    XMLData data = new XMLData();
    data.put("row", list);
    return data;
  }

  /**
   * 根据要素表别名、要素唯一ID查询要素表数据。
   * @param element 要素表别名
   * @param id 要素唯一ID
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getEleById(String element, String id) {
    return getEle(element, 0, 0, null, false, null, null, "and chr_id='" + id + "'");
  }

  /**
   * 根据要素表别名、要素编码、是否需要权限过滤查询要素表数据。
   * @param element 要素表别名
   * @param code 要素编码
   * @param isNeedRight  是否需要权限过滤
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getEleByCode(String element, String code, boolean isNeedRight) {
    return getEle(element, 0, 0, null, isNeedRight, null, null, "and chr_code='" + code + "'");
  }

  /**
   * 根据要素表别名、要素COA查询要素表数据。
   * @param element  要素表名
   * @param coa_id  要素表COA
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getEleByCoa(String element, String coa_id) {
    return getEle(element, 0, 0, null, false, coa_id, null, "order by chr_code");
  }

  /**
   * 根据要素关联关系查询要素表数据。
   * @param element 要素表别名
   * @param relation 要素关联关系主控要素编码、数据
   * @param isNeedRight 是否需要权限过滤
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getEleByRelation(String element, Map relation, boolean isNeedRight) {
    return getEle(element, 0, 0, null, isNeedRight, null, relation, "order by chr_code");
  }

  /**
   * 要素表数据插入并返回插入数据的CHR_ID。
   * @param element 要素表别名
   * @param fieldInfo 要素值集（字段名 - 字段值）
   * @return 插入对象
   * @throws Exception 数据插入操作失败原因
   */
  public XMLData insertEle(String element, Map fieldInfo) throws Exception {
    return (XMLData) getIDictionary().insertValue(element, fieldInfo);
  }

  /**
   * 根据要素唯一ID修改指定要素表数据。
   * @param element 要素表别名
   * @param id 要素唯一ID
   * @param fieldInfo 要素值集（字段名 - 字段值）
   * @return 修改后的对象
   * @throws Exception 数据修改操作失败原因
   */
  public XMLData modifyEle(String element, String id, Map fieldInfo) throws Exception {
    return (XMLData) getIDictionary().updateEleValue(element, id, fieldInfo);
  }

  /**
   * 根据要素唯一ID修改指定要素表数据，并刷新CCID。
   * @param element 要素表别名
   * @param id 要素唯一ID
   * @param fieldInfo 要素值集（字段名 - 字段值）
   * @return 修改后的对象
   * @throws Exception 数据修改操作失败原因
   */
  public XMLData modifyEleAndRefreshCCID(String element, String id, Map fieldInfo) throws Exception {
    return (XMLData) getIDictionary().updateEleValueAndRefreshCCID(element, id, fieldInfo);
  }

  /**
   * 级联删除要素表数据
   * @param element 要素表别名
   * @param id 要素唯一ID
   * @param level_num 要素级次
   * @return 操作失败原因
   * @throws Exception 操作失败原因
   */
  public boolean deleteEle(String element, String id) throws Exception {
    return getIDictionary().deleteEleValue(element, id);
  }

  /**
   * 根据要素表别名、要素值查询处理后的查询结果。
   * @param elementDto 传入的要素dto对象
   * @param tableAlias 要素表别名
   * @return SQL字符串
   */
  public String getCondition(FElementDTO elementDto, String tableAlias) {
    return getIDictionary().getCondition(elementDto, tableAlias);
  }

  /**
   * 根据分页当前页和当前用户的权限过滤查询预算单位数据， 
   * 返回分页当前页码的查询结果，分页页大小默认为20。
   * @param page  分页当前页
   * @param rgCode  指定区划
   * @return 返回查询结果对应的XMLData
   */
  public XMLData findUserEnterpariseValues(FPaginationDTO page, String[] column, String strCondition) throws Exception {
    return getIDictionary().findUserEnterpariseValues(page, column, strCondition);
  }

  /**
   * 新增一个预算单位（带权限）， 
   * @param 要新增的预算单位信息  分页当前页
   * @return 新增成功的预算单位信息
   * @throws Exception 操作失败原因 
   */
  public XMLData insertEnterpriseWithRight(XMLData fieldInfo) throws Exception {
    return getIDictionary().insertEnterpriseWithRight(fieldInfo);
  }

  /**
   * 
   * @param chr_id
   * @return
   * @throws Exception
   */
  public List getElementData(String chr_id) throws Exception {
    return getIDictionary().getElementData(chr_id);
  }

  /**
   * 判断当预算单位修改时，此时的预算单位属性是否可以修改。
   * @param fieldInfo 
   * @return 
   */
  public boolean checkIsReform(Map fieldInfo) {
    return getIDictionary().checkIsReform(fieldInfo);
  }
}
