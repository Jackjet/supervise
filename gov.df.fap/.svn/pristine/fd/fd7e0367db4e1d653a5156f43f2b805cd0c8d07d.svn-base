/*
 * $Id: ElementSetOperation.java 198706 2012-04-11 00:44:55Z dingyy $
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.service.dictionary;

import gov.df.fap.api.dictionary.IElementSet;
import gov.df.fap.api.dictionary.IElementSetOperation;
import gov.df.fap.service.relation.CDD_Element;
import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 要素配置客户端操作类
 * 
 * @author 
 * 
 */
@Service
public class ElementSetOperation implements IElementSetOperation {

  @Autowired
  private IElementSet ElementSetBean;

  /**
   * 构造函数
   */
  public ElementSetOperation() {
  }

  /**
   * 得到要素配置数据
   * 
   * @return List
   */
  public List getElementByCondition(String condition) {
    CDD_Element cdd = new CDD_Element();
    return cdd.getEleSetByCondition(condition).getRecordList();
  }

  /**
   * 得到刷新视图的列数据
   * 
   * @return List
   */
  public List getFreshViewCol(String ele_code, String old_name, String new_name) {
    CDD_Element cdd = new CDD_Element();
    return cdd.getFreshViewCol(ele_code, old_name, new_name).getRecordList();
  }

  /**
   * 刷新视图的列数据
   * 
   * @return List
   */
  public void freshViewCol(String ele_code, String ele_name) {
    CDD_Element cdd = new CDD_Element();
    cdd.freshViewCol(ele_code, ele_name);
  }

  
  /**
   * 保存要素配置数据 
   * 2017-03-23 yanyga 
   * @param xmlData
   * @throws Exception
   */
  public void saveRightSet(Map<String,Object> map) throws Exception
  {
	  ElementSetBean.saveRightSet(map);
  }
  
  /**
   * 保存要素配置数据
   * 
   * @param xmlData
   * @throws Exception
   */
  public void saveorUpdateElement(XMLData xmlData) throws Exception {
    ElementSetBean.saveorUpdateElement(xmlData);
    ElementSetBean.freshView();
  }

  /**
   * 保存要素配置数据
   * 
   * @param xmlData
   * @throws Exception
   */
  public void updateElementSet(XMLData xmlData) throws Exception {
    ElementSetBean.updateElementSet(xmlData);
    ElementSetBean.freshView();
  }

  /**
   * 保存要素配置数据
   * 
   * @param xmlData
   * @throws Exception
   */
  public void updateElementCodeRule(XMLData xmlData) throws Exception {
    ElementSetBean.updateElementCodeRule(xmlData);
    ElementSetBean.freshView();
  }

  /**
   * 删除要素配置数据
   * 
   * @param wf_id
   * @throws Exception
   */
  public void deleteElement(String ui_id) throws Exception {
    ElementSetBean.deleteElement(ui_id);
    ElementSetBean.freshView();
  }

  /**
   * 删除要素配置数据
   * 
   * @param wf_id
   * @throws Exception
   */
  public boolean checkRight() throws Exception {
    return ElementSetBean.checkRight();
  }

  public List getListUIs() {
    return ElementSetBean.getElementListUIs();
  }

  public List getInputUIs() {
    return ElementSetBean.getElementInputUIs();
  }

  public void freshViewColById(String ele_code, String new_name, String ui_detail_id) {
    ElementSetBean.freshViewColById(ele_code, new_name, ui_detail_id);
  }

}
