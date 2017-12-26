package gov.df.fap.api.gl.coa;

import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.util.FBaseDTO;
import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;

/**
 * COA相关操作，主要用于CCID生成及CCID转换
 * @author 
 * @version 
 */
public interface ICoaService {

  /*
   * (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.interfaces.ICoa#getCCIDByXMLData(java.lang.String, gov.gfmis.fap.util.XMLData)
   */
  public String getCCIDByXMLData(long coa_id, XMLData xmlEleValue) throws Exception;

  /*
   * (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.interfaces.ICoa#getCCIDByBaseDTO(gov.gfmis.fap.util.FBaseDTO)
   */
  public abstract String getCCIDByBaseDTO(FBaseDTO inputFVoucherDto) throws Exception;

  /**
   * 批量生成CCID方法,在生成ccid后会自动填充回原对象中,所有原对象必须含有.
   * @param beanList 列表中的对象必须含有coa_id、ccid及各个要素的xx_id属性.
   */
  public void getCcidBatch(List beanList);

  public List getCcidBatchReturn(List benList);

  /**
   * 根据s_ccid和和t_coa_id定位相关上下游业务单ccid,目前默认不做模糊匹配!
   * 
   * @param t_coa_id
   *            目的业务单coa配置
   * @param s_ccid
   *            当前业务单ccid要素配置
   * @return String 上下游业务单对应的ccid
   */
  public String preCCIDTrans(long toCoaId, String ccid, boolean misMatch);

  /**
   * CCID转换
   * @param targetCoa 目标COA对象
   * @param ccidObj 要素值容器
   * @param ccidAccelerator 转换加速器
   * @return 转换成的CCID值
   */
  public String preCcidTrans(FCoaDTO targetCoa, String ccid, int setYear, boolean misMatch);

  /**
   * 转换CCID对象
   * @param toCoaId
   * @param data 可以为Map对象也可以为 FBaseDTO对象
   * @param setYear
   * @return 转换以后的CCID对象
   */
  public void preCcidTransBatch(BatchCodeCombinationProcesser processer);

  /**
   * 转换CCID对象
   * @param toCoaId
   * @param data 可以为Map对象也可以为 FBaseDTO对象
   * @param setYear
   * @return 转换以后的CCID对象
   */
  public Object doPreCcidTrans(FCoaDTO targetCoa, Object data, int setYear);

  /**
   * 判断CCID是否匹配权限
   * @param ruleId
   * @param ccid
   * @return
   */
  public boolean ccidMatchRule(String ruleId, long ccid);

  /**
   * 取COA对象
   * @param coaId COA ID
   * @return COA对象
   */
  public FCoaDTO getCoa(long coaId);

  /**
   * 生成RCID封装
   * @param ccid
   * @param setYear
   * @return
   */
  public String createRcid(long ccid, int setYear);

  /**
   * 根据ccid取出具体信息
   * @param ccid
   * @return
   */
  public FBaseDTO getCCIDInfo(FCoaDTO coa, long ccid);

  /**
   * 根据CCID取出具体信息
   * @param coa
   * @param ccid
   * @return
   */
  public Map getCcidInfoMap(FCoaDTO coa, long ccid);

  /**
   * 生成一个CCID对象
   * @param coa
   * @param elementsContainer
   * @param setYear
   * @return
   */
  public Object generateCcidObject(FCoaDTO coa, Object elementsContainer, int setYear);

  /**
   * 计算要素组合ID,会修正已知冲突.
   * @param coa
   * @param elementsContainer
   * @return
   */
  public CodeCombination caculateCcid(FCoaDTO coa, Object elementsContainer);

  /**
   * 计算要素组合ID,会修正已知冲突.
   * @param coa
   * @param elementsContainer
   * @return
   */
  public CodeCombination caculateCcidWithElementInfo(FCoaDTO coa, Object elementsContainer);

  /**
   * 解决冲突并保存冲突结果
   * @param coa 指定COA 
   * @param conflictCodeCmb 冲突的CCID
   */
  public CodeCombination fixCodeCombinationConflict(FCoaDTO coa, CodeCombination beCurrectOne);

  /**
   * 重新保存coa所有配置
   * 强制保存数据,先删除原有配置在保存传入数据
   * @param coaList
   */
  public void reinstallCoa(List coaList) throws Exception;

  /**
   * 保存coa配置 
   * @param coaDto
   * @throws Exception
   */
  public FCoaDTO saveCoaDto(FCoaDTO coaDto) throws Exception;

  /**
   * 更新coa配置
   * @param coaDto
   * @param isCascadeSave 是否级联保存
   * @throws Exception
   */
  public void updateCoaDto(FCoaDTO coaDto, boolean isCascadeSave) throws Exception;

  /**
   * 根据dto中coa配置生成条件语句
   * @param baseDTO
   * @param setYear
   * @param misMatch
   * @return
   */
  public String preCcidTrans(FBaseDTO baseDTO, int setYear, boolean misMatch);

  /**
   * 判断传入coa是否生成了ccid
   * @param coaDto
   * @return
   */
  public boolean isGenCcid(FCoaDTO coaDto);

  /**
   * add by fangbb
   * @return
   */
  public List loadCoaCascade();

}
