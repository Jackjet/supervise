package gov.df.fap.api.gl.coa;

import gov.df.fap.bean.gl.coa.FCoaDTO;


/**
 * 批量CCID生成处理器
 * @author 
 *
 */
public interface BatchCodeCombinationProcesser {

	/**
	 * 批量数据的大小
	 * @return
	 */
	public int size();

	/**
	 * 批量数据的数据源.
	 * @param index
	 * @return
	 */
	public Object getElementContainer(int index);

	/**
	 * 取出数据的COA.
	 * @param index
	 * @return
	 */
	public FCoaDTO getCoa(int iindex);

	/**
	 * 生成CCID到批量数据中.
	 * @param index
	 * @param cachedCodeCmb
	 */
	public void setCodeCombination(int index, CodeCombination cachedCodeCmb);

	/**
	 * 更新要素组合ID
	 * @param oldOne 旧对象
	 * @param newOne 新对象
	 */
	public void updateCodeCombinationId(CodeCombination oldOne, CodeCombination newOne);

	/**
	 * 是否需要模糊匹配,默认否
	 * @param index
	 * @return
	 */
	public boolean needFuzzyMatch(int index);

}
