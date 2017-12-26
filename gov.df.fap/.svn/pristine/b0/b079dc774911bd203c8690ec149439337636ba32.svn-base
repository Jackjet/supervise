package gov.df.fap.api.rule;

import gov.df.fap.util.xml.XMLData;

import java.util.List;

/**
 * 菜单管理服务端接口
 * @author a
 *
 */
public interface ISysBillNoRule {
  public List findAllSysBillNoRules() throws Exception;

  public List findSysBillNoRuleLinesByBillnoruleId(String billnoruleId) throws Exception;

  public List findSysBillNoRuleElesByBillnorulelineId(String billnorulelineId) throws Exception;

  public List findSysBilltypesByBillnoruleId(String billnoruleId) throws Exception;

  public void saveorUpdateSysBillNoRule(XMLData xmlData) throws Exception;

  public List getBillType(String billNoRuleId) throws Exception;

  public void deleteSysBillNoRule(XMLData xmlData) throws Exception;

  public void deleteSysBillNoRuleByBillNoRuleId(String billnoruleId) throws Exception;

  public XMLData findSysBillNoRuleById(String bnr_id) throws Exception;

  /**
   * 根据子系统查询单号规则
   * 
   * @param billNoruleId
   * @author tyy
   * @return List
   */
  public List getSysBillNoRuleBySysId(String sysId);

  /**
   * 根据billNoruleId查询单号规则详细信息
   * 
   * @param billNoruleId
   * @author tyy
   * @return List
   */
  public List getSysBillNoruleBybillnoruleId(String billNoruleId);

  /**
   * 根据billnorule_id查询单号规则项
   * 
   * @param sysId
   *            子系统值
   * @author tyy
   * @return List
   */
  public List getSysBillNoruleBybillnorulelineId(String billnorule_id);

  /**
   * 根据billNorulelineId查询单号规则项关联要素
   * 
   * @param billNorulelineId
   * @author tyy
   * @return List
   */
  public List findSysbillnoruleeleId(String billNorulelineId);

  public boolean isBillNoRuleCodeExist(String billnoruleCode) throws Exception;
  
  /**
	 * 查询有关构建树数据信息
	 * @return
	 * @throws Exception 
	 */
	public List findTreeBillNoInfo() throws Exception;
	public List findTreeSysAppInfo() throws Exception;
}
