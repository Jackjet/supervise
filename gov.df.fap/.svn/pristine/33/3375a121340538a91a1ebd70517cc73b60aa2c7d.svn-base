package gov.df.fap.service.util.exceptions.gl;

import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.dto.CtrlRecordDTO;
import gov.df.fap.bean.util.FBaseDTO;

/**
 * 非法额度异常.
 * @author 
 * @version 2017-03-24
 */
public class IllegalBalanceException extends GlException {

  private static final long serialVersionUID = 1L;

  /**实际额度*/
  private CtrlRecordDTO existsBalance = null;

  /**临时计算的影响额度*/
  private CtrlRecordDTO tempBalance = null;

  /**额度要素组合*/
  private FBaseDTO ccidInfo = null;

  public FBaseDTO getCcidInfo() {
    return ccidInfo;
  }

  public void setCcidInfo(FBaseDTO ccidInfo) {
    this.ccidInfo = ccidInfo;
  }

  public CtrlRecordDTO getExistsBalance() {
    return existsBalance;
  }

  public void setExistsBalance(CtrlRecordDTO existsBalance) {
    this.existsBalance = existsBalance;
  }

  public IllegalBalanceException(BusVouAccount account, CtrlRecordDTO existsBalance, CtrlRecordDTO tempBalance,
    FBaseDTO ccidInfo) {
    this.existsBalance = existsBalance;
    this.tempBalance = tempBalance;
    this.ccidInfo = ccidInfo;

  }

  /**
  * 构造函数
  * @param message
  */
  public IllegalBalanceException(String message) {
    super(message);
  }

  /**
   * 构造函数
   * @param status 非法额度类型
   * @param message
   * @param e
   */
  public IllegalBalanceException(String message, Exception e) {
    super(message, e);
  }

  public CtrlRecordDTO getTempBalance() {
    return tempBalance;
  }

  public void setTempBalance(CtrlRecordDTO tempBalance) {
    this.tempBalance = tempBalance;
  }
}
