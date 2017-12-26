package gov.df.fap.api.gl.coa;

import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.coa.FCoaDetail;
import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;
/**
 * ICoa coa配置管理服务接口
 * @version 1.0
 * @author lwq
 * @since java 1.6
 */
public interface IConfigCoa 
{
 /**
 * 根据coa_id和对应要素配置信息对象新增对应的coa要素级次配置 
 * @param coaData coa配置信息
 * @param XMLData 保存后的对象
 * @throws Exception 异常
 */
   public XMLData saveCoa(XMLData eleData)throws Exception;
   
   public FCoaDTO saveCoaDto(FCoaDTO eleData)throws Exception;
   
   /**
   * 根据coa_id和对应要素配置信息对象修改对应的coa要素级次配置 
   * @param coaData coa配置信息
   * @throws Exception 异常
   */
   public void updateCoa(XMLData eleData)throws Exception;
   
   public void updateCoaDto(FCoaDTO eleData, boolean isCascadeSave)throws Exception;
      
   /**
   * 删除一条coa要素配置
   * @param coa_id 要素配置信息
   * @return 是否成功
   * @throws Exception 异常
   */
   public void deleteCoa(String coa_id)throws Exception;
   
   public boolean isExistCcidsTable(String ccids_table);
   
   /**
	 * 校验修改的级联coa
	 * @param coaData
	 * @throws CoaCascadeException
	 */
	public void checkCascadeCoa(FCoaDTO coaData) throws Exception;
	
	/**
	 * 获取Coa级联信息
	 * @return
	 */
    public Map getCoaCascade();

    /**
     * 通过COA_ID读取COA对象
     * 
     * @param coaId
     * @return
     */
	public FCoaDTO getCoa(long coaid);

	 /**
	   * 查询coa的级联设置
	   */
	public List loadCoaCascade();
	
	 /**
		 * 移除账务的COA
		 */
	public List removeGlCoa(List coaList);
	
	/**
	 * 根据要素编码查询coa中设置
	 * 
	 * @param coaDto
	 * @param eleCode
	 * @return
	 */
	public  FCoaDetail getCoaDetailByEleCode(FCoaDTO coaDto, String eleCode);
	
	
}
