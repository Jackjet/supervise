/*
 * @(#)CDD_Relation.java	1.0 20/03/06
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.service.relation;

import gov.df.fap.api.dictionary.interfaces.IDDSet;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.xml.XMLData;

import org.springframework.beans.factory.BeanFactory;

/**
 * CDD_Relation 数据字典客户端组件接口 调用数据字典组件服务端远程接口， 对关联关系管理表（SYS_RELATION_MANAGER）
 * 及关联关系明细表（SYS_RELATIONS）进行增、删、查、改操作。
 * 
 * @version 1.0
 * @author Zhang Peng
 * @since java 1.4.1
 */
public class CDD_Relation extends CommonOperation {
  BeanFactory factory = null;

  IDDSet ddSetBean = null;

  /**
   * update by sungy 20080409
   * 查询关联关系管理表数据，返回分页当前页码的查询结果。
   * 
   * @param pageIndex
   *            分页当前页
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getRM(int pageIndex) {
    //return getData("SYS_RELATION_MANAGER" + Tools.addDbLink(), String.valueOf(pageIndex), false);
    String[] displayField = new String[13];
    //由于接口不规范,暂时处理方法
    displayField[0] = "relation_id";
    displayField[1] = "relation_code";
    displayField[2] = "pri_ele_code";
    displayField[3] = "sec_ele_code";
    displayField[4] = "set_year";
    displayField[5] = "create_date";
    displayField[6] = "create_user";
    displayField[7] = "latest_op_date";
    displayField[8] = "latest_op_user";
    displayField[9] = "is_deleted";
    displayField[10] = "last_ver";
    displayField[11] = "sys_id";
    displayField[12] = "relation_type";
    return getDataByCondition(
      "SYS_RELATION_MANAGER" + Tools.addDbLink(),
      String.valueOf(pageIndex),
      "0",
      displayField,
      " and relation_type = 0 and rg_code='" + SessionUtil.getRgCode() + "' and set_year='"
        + SessionUtil.getLoginYear() + "' ", "");
  }

  /**
   * 查询关联关系管理表数据，按显示字段返回分页处理后的查询结果。
   * 
   * @param pageIndex
   *            分页当前页
   * @param pageCount
   *            分页页大小
   * @param displayField
   *            显示字段
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getRM(int pageIndex, int pageCount, String displayField[]) {
    return getDataByDispField("SYS_RELATION_MANAGER" + Tools.addDbLink(), String.valueOf(pageIndex),
      String.valueOf(pageCount), displayField);
  }
  public XMLData getRM(String displayField[]) {
	    return getDataByDispField("SYS_RELATION_MANAGER" + Tools.addDbLink(),
	      displayField);
	  }
  /**
   * 根据关联关系管理唯一ID查询要素关联管理表数据。
   * 
   * @param id
   *            关联关系管理唯一ID
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getRMById(String id) {
    return getDataBySingleField("SYS_RELATION_MANAGER" + Tools.addDbLink(), "RELATION_ID", id, false);
  }

  /**
   * 根据关联关系编码查询要素关联管理表数据。
   * 
   * @param code
   *            关联关系编码
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getRMByCode(String code) {
    return getDataBySingleField("SYS_RELATION_MANAGER" + Tools.addDbLink(), "RELATION_CODE", code, false);
  }

  /**
   * 通过关联关系id获取当前所有的主控要素值
   * 
   * @param relation_id
   *            关联关系id
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getPriEleValueByID(String relation_id) {
    return this.getDataByCondition("SYS_RELATIONS" + Tools.addDbLink(), "0", "0", new String[] { "pri_ele_value" },
      " and relation_id='" + relation_id + "' and rg_code='" + SessionUtil.getRgCode() + "' and set_year='"
        + SessionUtil.getLoginYear() + "' ", "");
  }

  /**
   * 通过关联关系id获取当前所有的被控要素值
   * 
   * @param relation_id
   *            关联关系id
   * @param pri_ele_value
   *            主控要素值
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getSecEleValueByID(String relation_id, String pri_ele_value) {
    StringBuffer condition = new StringBuffer();
    condition.append(" and relation_id='").append(relation_id)
      .append("' and rg_code='" + SessionUtil.getRgCode() + "' and set_year='" + SessionUtil.getLoginYear() + "' ");
    if (pri_ele_value != null && !pri_ele_value.equalsIgnoreCase("")) {
      condition.append(" and pri_ele_value = '").append(pri_ele_value).append("'");
    }
    return this.getDataByCondition("SYS_RELATIONS" + Tools.addDbLink(), "0", "0", new String[] { "sec_ele_value" },
      condition.toString(), "");
  }

  /**
   * 根据关联关系唯一ID查询关联关系明细表数据。
   * 
   * @param relationId
   *            关联关系管理唯一ID
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getRelationByRelationId(String relationId) {
    return getDataBySingleField("SYS_RELATIONS" + Tools.addDbLink(), "RELATION_ID", relationId);
  }

  /**
   * 关联关系明细表数据插入。 /关联关系主表信息
   * 
   * @param relationData
   *            关联关系数据结构,结构为:relationData \row 明细表配置信息detail --> (主控要素编码 -
   *            被控要素列表)值对
   * @return 关联关系唯一id
   * @throws Exception
   *             数据插入操作失败原因
   */
  public String insertRelation(XMLData relationData) throws Exception {
    return getIDDSet().insertRelation(relationData);
  }

  /**
   * 关联关系明细表数据修改。 /关联关系主表信息
   * 
   * @param relationData
   *            关联关系数据结构,结构为:relationData \row 明细表配置信息XMLData --> (主控要素编码 -
   *            被控要素列表)值对
   * @return boolean 修改是否成功
   * @throws Exception
   *             数据修改操作失败原因
   */
  public boolean modifyRelation(XMLData relationData) throws Exception {
    return getIDDSet().modifyRelation(relationData);
  }

  /**
   * 通过传入的关联关系id删除关联关系配置数据
   * 
   * @param relation_id
   *            关联关系id
   * @return 操作是否成功的结果
   * @throws Exception
   *             数据删除操作失败原因
   */
  public boolean deleteRelation(String relation_id) throws Exception {
    return getIDDSet().deleteRelation(relation_id);
  }

  /**
   * 根据被关联要素编码查询主控要素编码。
   * 
   * @param code
   *            被关联要素编码
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getPrimaryRelationByCode(String code) {
    return this.getDataBySingleField("SYS_RELATION_MANAGER" + Tools.addDbLink(), "SEC_ELE_CODE", code, false);
  }

  /**
   * 根据主控要素编码查询被关联要素编码
   * 
   * @param code
   *            主控要素编码
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getSlaveRelationByCode(String code) {
    return this.getDataBySingleField("SYS_RELATION_MANAGER" + Tools.addDbLink(), "PRI_ELE_CODE", code, false);
  }

}
