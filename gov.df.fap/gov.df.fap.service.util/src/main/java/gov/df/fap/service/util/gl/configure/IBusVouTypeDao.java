package gov.df.fap.service.util.gl.configure;

import gov.df.fap.bean.gl.configure.BusVouType;

import java.util.List;

public interface IBusVouTypeDao {

	/**
	 * 新增记账模板,同时会补记账模板ID及年度,时间,区划.
	 * @param bvType 记账模板对象
	 */
	public abstract void saveBusVouType(BusVouType bvType);

	/**
	 * 更新记账模板,同时更新最新版本号.
	 * @param bvType 记账模板对象
	 */
	public abstract void updateBusVouType(BusVouType bvType);

	/**
	 * 删除记账模板
	 * @param busVouType
	 */
	public abstract void deleteBusVouType(BusVouType busVouType);

	public abstract void deleteAllBusVouType();

	/**
	 * 读取记账模板
	 * @param vouTypeCode 记账模板编码
	 * @return
	 */
	public abstract BusVouType loadBusVouTypeByCode(String vouTypeCode);

	/**
	 * 读取记账模板
	 * @param vouTypeId
	 * @return
	 */
	public abstract BusVouType loadBusVouType(long vouTypeId);

	/**
	 * 查询所有记账模板信息
	 * @return
	 */
	public abstract List allBusVouType();

	/**
	 * 新增记账模板与科目关系.
	 * @param busVouAcctmdlList
	 * @param busVouTypeId
	 */
	public abstract void saveBusVouAcctmdl(List busVouAcctmdlList);

	/**
	 * 更新记账模板科目关系对象,采用先删除后插入的更新方法.
	 * @param busVouAcctmdlList
	 * @param busVouTypeId
	 */
	public abstract void updateBusVouAcctmdl(List busVouAcctmdlList,
			long busVouTypeId);

	public abstract void deleteAcctmdlByBvTypeId(long bvTypeId);

	/**
	 * 读取记账模板科目关系
	 * @param vouTypeId 记账模板ID
	 * @return
	 */
	public abstract List loadVouAcctdml(long vouTypeId);

	/**
	 * 校验此记账模板是否已存在在途数据
	 * @param vouTypeId
	 * @return
	 */
	public abstract boolean existOnWayData(long vouTypeId);

	/**
	 * 保存图形信息,不包括配置信息
	 * @param content
	 */
	public abstract void saveConfigString(final byte[] content);

	/**
	 * 读取图形信息
	 * @return
	 */
	public abstract byte[] loadConfigString();
	
	/**
	 * 通过交易凭证关联,读取记账模板id
	 * @param billTypeCode
	 * @return
	 */
	public long getVouTypeIdByBill(String billTypeCode);

}