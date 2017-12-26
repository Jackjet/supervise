/*
 * @(#)UIRelationOperation.java	1.0 25/05/06
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.service.relation;

import gov.df.fap.api.dictionary.interfaces.IControlDictionary;
import gov.df.fap.api.relation.IRelationService;
import gov.df.fap.bean.control.FAssistObjectDTO;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * UIRelationOperation 关联关系界面功能操作类
 * 
 * @version 1.0
 * @author victor
 * @since java 1.4.2
 */
@Component
public class UIRelationOperation implements IRelationService{
  /**
   * 获取所有关联关系列表
   * 
   * @return 关联关系信息列表
   */

  public XMLData getRMList() {
    XMLData data = null;
	   
    try {
      String[] displayField = new String[7];
      // 由于接口不规范,暂时处理方法
      displayField[0] = "relation_id";
      displayField[1] = "relation_code";
      displayField[2] = "pri_ele_code";
      displayField[3] = "sec_ele_code";
      displayField[4] = "(select ele_name from sys_element" + Tools.addDbLink() + " where ele_code = pri_ele_code "
        + " and rg_code='" + getRgCode() + "' and set_year=" + getSetYear() + ") as pri_ele_name ";
      displayField[5] = "(select ele_name from sys_element" + Tools.addDbLink() + " where ele_code = sec_ele_code"
        + " and set_year=" + getSetYear() + " and rg_code='" + getRgCode() + "'" + ") as sec_ele_name";

      /*** Add by fengdz TM:2012-03-23 ***/
      displayField[6] = "relation_type";
      CDD_Relation client = new CDD_Relation();
	
      data = client.getRM(displayField);
      // data=client.getRM(20);
    } catch (Exception e) {
    }
    return data == null ? new XMLData() : data;
  }

  /*** Add by fengdz TM:2012-03-19 ***/
  public String getSetYear() {
    String set_year = (String) SessionUtil.getLoginYear();

    return set_year;
  }

  public String getRgCode() {

    String rg_code = (String) SessionUtil.getRgCode();

    return rg_code;
  }

  /*** Add by fengdz TM:2012-03-19 ***/
  /**
   * 根据relationCode获取关联关系列表
   * 
   * @param relationCodes
   *            关联关系Code，形式如：001|002|003
   * @return 关联关系信息列表
   * @author zhangjunyang
   * @since 2011-04-13
   */
  public XMLData getRMList(String relationCodes) {
    XMLData result = getRMList();
	   
    // 如果 没有条件限制则返回全部
    if (relationCodes == null || relationCodes.trim().equals("")) {
      return result;
    }
    // 过滤
    String[] contidion = relationCodes.trim().replace('|', ',').split(",");
    List list = result.getRecordList();
    if (list != null && list.size() > 0) {
      for (int i = list.size() - 1; i >= 0; i--) {
        if (list.get(i) != null && list.get(i) instanceof Map) {
          int j = 0;
          for (; j < contidion.length; j++) {
            if (contidion[j] != null && ((Map) list.get(i)).get("relation_code") != null
              && ((Map) list.get(i)).get("relation_code").toString().trim().equals(contidion[j].trim())) {
              contidion[j] = null;// 判断过的条件直接删除，形成短路与，避免条件三的方法调用。
              break;
            }
          }
          // 移除
          if (j == contidion.length) {
            list.remove(i);
          }
        }
      }
    }
    return result;

  }

  /**
   * 通过relation_id获取对应关联关系的详细配置信息
   * 
   * @param relation_id
   *            关联关系唯一guid
   * @return 详细配置信息
   */
  public XMLData getRMDetailByID(String relation_id) {
    XMLData data = null;
	   
    try {
      CDD_Relation client = new CDD_Relation();
      data = client.getRelationByRelationId(relation_id);
    } catch (Exception e) {
    }
    return data == null ? new XMLData() : data;
  }

  /**
   * 根据要素简称获取要素列表
   * 
   * @param element
   *            要素简称
   * @return 要素信息对象
   */
  public XMLData getEle(String element) {
	  
	  
		   
    // modify by zhangjunyang on 2011-04-06
    // // CDD_Element cDD = new CDD_Element();
    // // String[] displayField = new
    // String[]{"chr_id","chr_code","chr_name","level_num","is_leaf","parent_id"};
    // // XMLData data =
    // cDD.getEle(element,0,0,displayField,true,null,null,"order by chr_code");
    // // return data==null?new XMLData():data;
    //
    // IControlDictionary dictionary = null;
    // if (Global.loginmode == 0) {
    // BeanFactory cfactory = BeanFactoryUtil
    // .getBeanFactory("gov/gfmis/fap/dictionary/interfaces/control_dictionary.xml");
    // dictionary = (IControlDictionary) cfactory
    // .getBean("sys.controlDictionaryService");
    // } else {
    // dictionary = (IControlDictionary) SessionUtil
    // .getOffServerBean("sys.controlDictionaryService");
    // }
    //
    // FPaginationDTO page = new FPaginationDTO();
    // page.setCurrpage(-1);
    // page.setPagecount(10);
    // // long beginTime = System.currentTimeMillis();
    // // System.out.println(this.getDataSource());
    // List data = dictionary.findEleValuesAsObj(element, page,
    // false, "",
    // null, " order by chr_code",
    // FAssistObjectDTO.class);
    //
    // XMLData ret = new XMLData();
    // ret.put("row", data);
    // return ret;

    return this.getEle(element, false);

  }

  /**
   * 根据要素简称获取要素列表
   * 
   * @param element
   *            要素简称
   * @param isNeedRight
   *            是否需要权限
   * @return 要素信息对象
   * @author zhangjunyang on 2010-04-06
   */
  public XMLData getEle(String element, boolean isNeedRight) {
		   
    IControlDictionary dictionary = (IControlDictionary) SessionUtil.getServerBean("sys.dictionaryService");

    FPaginationDTO page = new FPaginationDTO();
    page.setCurrpage(-1);
    page.setPagecount(10);

    List data = dictionary.findEleValuesAsObj(element, page, isNeedRight, "", null, " and enabled=1 order by chr_code",
      FAssistObjectDTO.class);

    XMLData ret = new XMLData();
    ret.put("row", data);
    return ret;

  }

  /**
   * 获取当前所有启用要素信息
   * 
   * @return 要素信息对象
   */
  public XMLData getAllElement() {
		   
    CDD_Element cDD = new CDD_Element();
    XMLData data = cDD.getEleSetByCondition(" and enabled=1");
    return data == null ? new XMLData() : data;
  }

  /**
   * 根据要素简称获取要素中文名
   * 
   * @param ele_code
   *            要素简称
   * @return 要素中文名
   */
  public String getEleCName(String ele_code) {
    String cName = "";
	   
    if (ele_code == null || ele_code.equals("")) {
      return cName;
    }
    CDD_Element cdd = new CDD_Element();
    XMLData data = cdd.getEleSetByCode(ele_code);
    if (data != null && data.size() > 0) {
      cName = (String) data.getSubObject("ele_name");
    }
    return cName;
  }

  /**
   * 通过关联关系id获取当前所有的主控要素值
   * 
   * @param relation_id
   *            关联关系id
   * @return
   */
  public XMLData getPriEleValueByID(String relation_id) {
		   
    CDD_Relation cDD = new CDD_Relation();
    XMLData data = cDD.getPriEleValueByID(relation_id);
    return data == null ? new XMLData() : data;
  }

  /**
   * 通过关联关系id获取当前所有的被控要素值
   * 
   * @param relation_id
   *            关联关系id
   * @param pri_ele_value
   *            主控要素值
   * @return
   */
  public XMLData getSecEleValueByID(String relation_id, String pri_ele_value) {
    CDD_Relation cDD = new CDD_Relation();
	   
    XMLData data = cDD.getSecEleValueByID(relation_id, pri_ele_value);
    return data == null ? new XMLData() : data;
  }

  /**
   * 根据对应关联关系配置信息对象新增对应的关联关系配置
   * 
   * @param relationData
   *            关联关系配置信息
   * @return XMLData 保存后对象
   * @throws Exception
   *             异常
   */
  public XMLData saveRelation(XMLData relationData) throws Exception {
		   
    CDD_Relation cDD = new CDD_Relation();
    relationData.put("relation_id", cDD.insertRelation(relationData));
    return relationData;
  }

  /**
   * 根据对应关联关系配置信息对象新增对应的关联关系配置
   * 
   * @param relationData
   *            关联关系配置信息
   * @return boolean 保存是否成功
   * @throws Exception
   *             异常
   */
  public boolean updateRelation(XMLData relationData) throws Exception {
		   
    CDD_Relation cDD = new CDD_Relation();
    return cDD.modifyRelation(relationData);
  }

  /**
   * 根据关联关系唯一码删除对应的关联关系配置
   * 
   * @param relation_id
   *            关联关系唯一码
   * @throws Exception
   *             异常
   */
  public boolean deleteRelation(String relation_id) throws Exception {
		   
    CDD_Relation cDD = new CDD_Relation();
    return cDD.deleteRelation(relation_id);
  }
}
