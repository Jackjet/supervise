package gov.df.fap.api.dictionary;

import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;

public interface IElementSetOperation {

  /**
   * 得到要素配置数据
   * 
   * @return List
   */
  public List getElementByCondition(String condition);

  /**
   * 得到刷新视图的列数据
   * 
   * @return List
   */
  public List getFreshViewCol(String ele_code, String old_name, String new_name);

  /**
   * 刷新视图的列数据
   * 
   * @return List
   */
  public void freshViewCol(String ele_code, String ele_name);

  /**
   * 保存要素配置数据
   * 
   * @param xmlData
   * @throws Exception
   */
  public void saveorUpdateElement(XMLData xmlData) throws Exception;

  /**
   * 保存要素配置数据
   * 
   * @param xmlData
   * @throws Exception
   */
  public void updateElementSet(XMLData xmlData) throws Exception;

  
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
  public void updateElementCodeRule(XMLData xmlData) throws Exception;

  /**
   * 删除要素配置数据
   * 
   * @param wf_id
   * @throws Exception
   */
  public void deleteElement(String ui_id) throws Exception;

  /**
   * 删除要素配置数据
   * 
   * @param wf_id
   * @throws Exception
   */
  public boolean checkRight() throws Exception;

  public List getInputUIs();

  public void freshViewColById(String ele_code, String new_name, String ui_detail_id);
}
