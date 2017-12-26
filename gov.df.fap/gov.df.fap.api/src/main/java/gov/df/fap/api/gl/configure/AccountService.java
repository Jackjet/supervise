package gov.df.fap.api.gl.configure;

import gov.df.fap.bean.gl.configure.BusVouAccount;

import java.util.List;

/**
 * 
 * @author lwq
 * @version 
 */
public interface AccountService {

  /**
   * 根据科目id取出科目列表数据
   * @param accountId
   * @return
   */
  public BusVouAccount loadBusVouAccount(String accountId);

  /**
   * 根据科目code取出科目列表数据
   * @param accountId
   * @return
   */
  public BusVouAccount loadBusVouAccountByCode(String accountCode);

  /**
   * 取出所有科目数据
   * @return
   */
  public List allBusVouAccount();

  /**
   * 保存科目信息
   * @param bvAccount
   */
  public void saveBusVouAccount(BusVouAccount bvAccount);

  /**
   * 更新科目信息
   * @param oldAccount
   * @param newAccount TODO
   */
  public void updateBusVouAccount(BusVouAccount oldAccount, BusVouAccount newAccount);

  /**
   * 删除科目
   * @param oldAccount
   */
  public void deleteBusVouAccount(BusVouAccount bvAccount);

  /**
   * 重新保存科目配置
   * 若库中存在的更新科目，若不存在删除
   * @param accountList
   */
  public void reinstallAccount(List accountList);

  /**
   * 根据科目编码获取ID
   * 
   * @param accountCode
   * @param rg_code
   * @param set_year
   * @return
   */
  public String getId(String accountCode, String rg_code, String set_year);

  public void clearCache();

}
