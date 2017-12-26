package gov.df.fap.api.gl.coa;

import gov.df.fap.bean.gl.coa.FCoaDTO;


public interface CoaObserver {

	/**
	 * 新增修改对树的刷新
	 * @param coaDTO
	 */
	public void addNewModify(FCoaDTO coaDTO);
	
	/**
	 * 更新操作后对树的刷新
	 * @param coaDTO
	 */
	public void updateModify(FCoaDTO coaDTO);
	/**
	 * 删除操作后对树的刷新
	 *
	 */
	public void deleteModify();
	
	/**
	 * 刷新coa树
	 */
	public void refreshCoaTree();
}
