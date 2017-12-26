/*
 * $Id: IElementSet.java 198545 2012-01-12 03:33:57Z zhangwh1 $
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.api.dictionary;

import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;

/**
 * 要素配置服务端接口
 * 
 * @author zhadaopeng
 * 
 */
public interface IElementSet {
  /**
   * 得到所有要素配置数据
   * 
   * @return List
   */
  public List getElementByCondition(String condition);

  /**
   * 保存要素配置数据
   * 
   * @param xmlData
   * @throws Exception
   */
  public void saveorUpdateElement(XMLData xmlData) throws Exception;

  
  /**
   * 保存要素配置数据 
   * 2017-03-23 yanyga 
   * @param xmlData
   * @throws Exception
   */
  public void saveRightSet(Map<String,Object> map) throws Exception;
  
  /**
   * 保存要素配置数据
   * 
   * @param xmlData
   * @throws Exception
   */
  public void updateElementSet(XMLData xmlData) throws Exception;

  /**
   * 保存要素配置数据
   * 
   * @param xmlData
   * @throws Exception
   */
  public void updateElementCodeRule(XMLData xmlData) throws Exception;

  /**
   * 删除要素配置数据
   * 
   * @param wf_id
   * @throws Exception
   */
  public void deleteElement(String wf_id) throws Exception;

  /**
   * 删除要素配置数据
   * 
   * @param wf_id
   * @throws Exception
   */
  public boolean checkRight() throws Exception;

  public List getElementListUIs();

  public List getElementInputUIs();

  public void freshViewColById(String ele_code, String new_name, String ui_detail_id);

  public void freshView() throws Exception;
  
  
  

}
