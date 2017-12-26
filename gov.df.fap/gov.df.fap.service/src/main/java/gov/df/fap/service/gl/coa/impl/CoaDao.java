package gov.df.fap.service.gl.coa.impl;

import gov.df.fap.api.gl.coa.CodeCombination;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.coa.FCoaDetail;
import gov.df.fap.service.gl.core.DaoSupport;
import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;



/**
 * 
 * @author 
 * @version 
 */

public interface CoaDao {

	/**
	 * 根据coa_id和对应要素配置信息对象新增对应的coa要素级次配置 
	 * @param coaData 要素配置
	 * @return XMLData 保存后的对象
	 * @throws Exception 异常
	 */
	public abstract XMLData saveCoa(XMLData coaData) throws Exception;


	/**
	 * 根据coa_id和对应要素配置信息对象修改对应的coa要素级次配置 
	 * @param coaData 要素配置
	 * @throws Exception 异常
	 */
	public abstract void updateCoa(XMLData coaData) throws Exception;

	/**
	 * 删除一条coa要素配置
	 * @param coa_id 要素配置信息
	 * @return 是否成功
	 * @throws Exception 异常
	 */
	public abstract void deleteCoa(String coa_id) throws Exception;
	
	/**
	 * 删除所有Coa配置
	 */
	public void deleteAllCoa();

	/**
	 * 取得COA的结构描述
	 * 
	 * @param coa_id：
	 *            标示COA唯一的ID
	 * @return coa对应XMLData对象
	 */
	public abstract XMLData getCoabyID(String coa_id);

	/**
	 * 读取COA对象
	 * @param coaId
	 * @return
	 */
	public abstract FCoaDTO loadCoa(long coaId);

	/**
	 * 
	 * @param condition
	 * @return
	 */
	public abstract List getCoaList(String condition);
	
	/**
	 * 取得所有COA对象
	 * @return
	 */
	public abstract List getCoaDTOList(); 

	/**
	 * 在CCID表中按照指定要素查询CCID.
	 * @param coaDto
	 * @param data
	 * @param setYear
	 * @return
	 */
	public abstract CodeCombination findCcidKey(FCoaDTO coa, long ccid);
	
	/**
	 * 在CCID表中按照指定要素查询CCID.
	 * @param coaDto
	 * @param data
	 * @param setYear
	 * @return
	 */
	public abstract Map findCcid(FCoaDTO coa, long ccid);

	/**
	 * 在CCID表中按照指定要素查询CCID.
	 * @param coaDto
	 * @param data
	 * @param setYear
	 * @return
	 */
	public abstract Map findCcid(FCoaDTO coa, CodeCombination codeCombination);
	
	/**
	 * 模糊查找CCID,当CCID或者MD5等于指定值就会返回.
	 * @param coa
	 * @param ccid
	 * @return
	 */
	public Map findCcidFuzzy(FCoaDTO coa, CodeCombination ccid);
	
	/**
	 * 在CCID表中按照指定要素查询CCID.
	 * @param coaDto
	 * @param data
	 * @param setYear
	 * @return
	 */
	public abstract List fuzzyQueryCcid(FCoaDTO coa, List ccidObjects);


	public abstract void insertCcid(FCoaDTO coa, Object elementContainer,
			int setYear) throws CodeCombinationConflictException;

	/**
	 * 插入CCID
	 * @param coas 插入的coa
	 * @param newCcids 要插入的要素组合对象
	 * @exception CodeCombinationConflictException 冲突异常,发生CCID主键冲突时则抛出该异常.
	 * @return 生成的ccid的id
	 */
	public abstract void insertCcidBatch(List coas, List newCcids) throws CodeCombinationConflictException;

	/**
	 * 通过CCID转换缓存加速匹配CCID.
	 * @param toCoaId
	 * @param ccid
	 * @param setYear
	 * @return
	 */
	public abstract long quickQueryCcid(FCoaDTO coa, long ccid);

	/**
	 * 保存CCID转换缓存,
	 * @param objects MAP对象,key为source ccid, value 为target ccid
	 */
	public abstract void saveCcidTransCache(List objects);

	/**
	 * 判断是否自定义级次中的要素
	 * @param coaDetail
	 * @param eleValueCode
	 * @return
	 */
	public abstract boolean isCustomerElementValid(FCoaDetail coaDetail,
			String eleValueCode);

	public abstract void setDaoSupport(DaoSupport daoSupport);

	/**
	 * 插入FCoaDTO格式coa
	 * @param coaDto
	 * @return
	 * @throws Exception
	 */
	public abstract void saveCoa(FCoaDTO coaDto) throws Exception;

	/**
	 * 更新coa配置
	 * @param coaDto FCoaDTO 格式
	 */
	public abstract void updateCoa(FCoaDTO coaDto) throws Exception;

	/**
	 * 保存冲突
	 * @param conflict 冲突的要素组合
	 * @param currect 正确的要素组合
	 * @param type 冲突类型
	 * @return
	 */
	public abstract void saveConflict(FixedConflicts fixConflict);
	
	/**
	 * 查找冲突
	 * @return
	 */
	public abstract List findFixedConflict();
	
	/**
	 * CCID表是否存在
	 * @param ccids_table
	 * @return
	 */
	public boolean isExistCcidsTable(String ccids_table);
	
	/**
	 * 是否已生成CCID
	 * @param coa
	 * @return true 存在ccid, false 不存在ccid
	 */
	public boolean checkCcidIsExists(FCoaDTO coa);

	/**
	 * 查询coa的级联设置
	 * @return
	 */
	public abstract List loadCoaCascade();
	
	/**
	 * 判断传入coa是否生成了ccid
	 * @param coaDto
	 * @return
	 */
	public boolean isGenCcid(FCoaDTO coaDto);
	
}