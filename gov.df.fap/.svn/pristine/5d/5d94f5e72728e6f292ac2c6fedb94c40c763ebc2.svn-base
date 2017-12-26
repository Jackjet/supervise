package gov.df.fap.service.util.gl.configure;

import gov.df.fap.bean.gl.configure.BusVouType;
import gov.df.fap.service.util.exceptions.gl.ExistOnWayDataOfBusVouException;
import gov.df.fap.util.exception.IllegalEleLevelOfDownStreamCoaException;
import gov.df.fap.util.exception.LackEleOfDownStreamCoaException;

import java.sql.SQLException;
import java.util.List;

/**
 * 记账模板服务接口
 * @author LiuYan
 * @version 2008-3-10
 */

public interface IBusVouTypeService {

  /**
   * 存储整套记账模板配置
   * @param busVouType
   * @throws IllegalEleLevelOfDownStreamCoaException
   * @throws LackEleOfDownStreamCoaException
   */
  public void saveSetBusVouType(BusVouType busVouType) throws IllegalEleLevelOfDownStreamCoaException,
    LackEleOfDownStreamCoaException;

  /**
   * 更新整套记账模板配置
   * @param busVouType
   * @throws IllegalEleLevelOfDownStreamCoaException
   * @throws LackEleOfDownStreamCoaException
   */
  public void updateSetBusVouType(BusVouType busVouType) throws IllegalEleLevelOfDownStreamCoaException,
    LackEleOfDownStreamCoaException, ExistOnWayDataOfBusVouException, SQLException;

  /**
   * 更新记账模板配置,不做再途数据校验
   * @param busVouType
   * @throws SQLException
   */
  public void updateSetBusVouTypeQuiet(BusVouType busVouType) throws SQLException;

  /**
   * 删除一组记账模板配置(通过对象中id属性删除)
   * @param busVouType
   * @return
   */
  public void deleteVouType(BusVouType busVouType) throws ExistOnWayDataOfBusVouException;

  /**
   * 根据交易凭证类型取出记账模板配置所有数据
   * @param billTypeCode
   * @return
   */
  public BusVouType loadVouTypeByBill(String billTypeCode);

  /**
   * 取出交易领中挂接科目列表所有数据
   * @param vouTypeId
   * @return
   */
  public List loadVouAcctdml(long vouTypeId);

  /**
   * 取出已配置好记账模板的信息
   * @param accountId
   * @return
   */
  public BusVouType loadBusVouType(long vouTypeId);

  /**
   * 取出已配置好记账模板的信息
   * @param accountId
   * @return
   */
  public BusVouType loadBusVouTypeByCode(String vouTypeCode);

  /**
   * 取出已配置好记账模板的信息
   * @param accountId
   * @return
   */
  public List allBusVouType();

  /**
   * 验证权限
   * @param bvType
   * @return
   */
  public List validateRule(BusVouType bvType);

  /**
   * 验证科目列表中coa的配置，验证上下游额度的科目中coa配置是否合法
   * 目前是校验上下游COA配置是否符合"顺向细化"规则
   * @param acctmdlList
   * @return
   * @throws IllegalEleLevelOfDownStreamCoaException
   * @throws LackEleOfDownStreamCoaException
   */
  public boolean validCoa(List acctmdlList) throws IllegalEleLevelOfDownStreamCoaException,
    LackEleOfDownStreamCoaException, IllegalArgumentException;

  /**
   * 验证数据,包括coa和权限验证
   * @param bvType 记账模板中acctmdl必须包含科目信息,权限的处理要区分上下游
   */
  public List validateData(BusVouType bvType);

  /**
   * 存储图形化记账模板配置
   * @param bvTypeList 命令集合
   * @param accountList 科目列表
   * @param content     图形xml文件二进制数组
   */
  public void saveGraphConfig(List bvTypeList, List accountList, byte[] content) throws Exception;

  /**
   * 取出记账模板图形配置的xml文件
   */
  public byte[] loadConfigUIByte();

  /**
   * 重新保存记账模板配置
   * 若库中存在的更新科目，若不存在删除
   * @param bvTypeList
   */
  public void reinstallBvType(List bvTypeList) throws Exception;

  /**
   * 删除所有记账模板
   * @param graphByte
   */
  public void removeAllBvType();

  public void clearCache();

  public long getVouTypeIdByBill(String billtypeCode);

  public List allBusVouTypeSimple();
}
