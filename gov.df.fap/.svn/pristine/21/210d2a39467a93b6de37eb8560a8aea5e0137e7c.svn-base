/**
 * <p>Copyright 2007 by Digiark Software corporation,
 * All rights reserved.
 * <p>版权所有：用友政务软件有限公司
 * <p>未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 * <p>侵权者将受到法律追究。
 */
package gov.df.fap.bean.gl.dto;

import gov.df.fap.util.StringUtil;
import gov.df.fap.util.number.NumberUtil;

import java.math.BigDecimal;

/**
 * @author
 * @version 
 * 注意：这里对CtrlRecordDTO 有个特殊处理！此处没有重写 HashCode 方法，
 * 可以使CtrlRecordDTO 作Key 时，即使equals 也要区分开来。
 */
public class CtrlRecordDTO implements Cloneable {

  /**冲销用*/
  public static final int BALANCE_TYPE_NEGATIVE = -1;

  /**普通生成*/
  public static final int BALANCE_TYPE_POSITIVE = 1;

  /**已汇总*/
  public static final int BALANCE_TYPE_SUM = 0;

  /**无负数*/
  public static final int NEGATIVE_NONE = -1;

  /**AVI为负*/
  public static final int NEGATIVE_AVI = 0;

  /**USE为负*/
  public static final int NEGATIVE_USE = 1;

  /**minus为负*/
  public static final int NEGATIVE_MINUS = 2;

  /**AVING为负*/
  public static final int NEGATIVE_AVING = 3;

  /*额度ID*/
  private String sum_id;

  /*业务明细ID*/
  private String vou_id;

  /*记账模板类型*/
  private long vou_type_id;

  /*年度*/
  private int set_year;

  /*区域码*/
  private String rg_code;

  /*科目ID*/
  private String account_id;

  /*科目Code*/
  private String account_code;

  /*要素组合*/
  private long ccid;

  /*权限组合*/
  private String rcid;

  /*月份*/
  private int set_month;

  /*生效金额*/
  private BigDecimal avi_money = NumberUtil.BIG_DECIMAL_ZERO;

  /*已申请金额*/
  private BigDecimal use_money = NumberUtil.BIG_DECIMAL_ZERO;

  /*在途调减金额*/
  private BigDecimal minus_money = NumberUtil.BIG_DECIMAL_ZERO;

  /*在途调增金额*/
  private BigDecimal aving_money = NumberUtil.BIG_DECIMAL_ZERO;

  /*备注*/
  private String remark;

  /*额度版本号*/
  private String bal_version;

  /*创建时间*/
  private String create_date;

  /*最后修改时间*/
  private String latest_op_date;

  /*最后版本*/
  private String last_ver;

  //    /*权限ID*/
  //    private String rule_id;
  /*来源去向，0_去向,1_来源*/
  private int ctrl_side;

  /*主来源/去向，0_非主来源/去向,1_主来源/去向*/
  //    private int is_primary;
  /**强制入账  0:非强制  1:强制*/
  private int is_enforce = 0;

  //在列表中的序号,用来加速.
  private int index = 0;

  private String balance_id;

  /*主来源额度ID*/
  private String primarySourceId;

  /**状态,默认普通*/
  private int balanceType = BALANCE_TYPE_POSITIVE;

  private String agency_id = "";

  private String mb_id = "";

  public CtrlRecordDTO() {

  }

  public void init() {
    sum_id = null;
    vou_id = null;
    vou_type_id = 0;
    set_year = 0;
    rg_code = null;
    /*科目ID*/
    account_id = null;
    account_code = null;
    ccid = 0;
    rcid = null;
    set_month = 0;
    avi_money = NumberUtil.BIG_DECIMAL_ZERO;
    use_money = NumberUtil.BIG_DECIMAL_ZERO;
    minus_money = NumberUtil.BIG_DECIMAL_ZERO;
    aving_money = NumberUtil.BIG_DECIMAL_ZERO;
    remark = null;
    bal_version = null;
    create_date = null;
    latest_op_date = null;
    last_ver = null;
    //        rule_id = null;
    ctrl_side = 0;
    //        is_primary = 0;
    is_enforce = 0;
    index = 0;
    balance_id = null;
    primarySourceId = null;
  }

  public String getPrimarySourceId() {
    return primarySourceId;
  }

  public void setPrimarySourceId(String primarySourceId) {
    this.primarySourceId = primarySourceId;
  }

  public String getBalance_id() {
    return balance_id;
  }

  public void setBalance_id(String balance_id) {
    this.balance_id = balance_id;
  }

  public String getSum_id() {
    return this.sum_id;
  }

  public void setSum_id(String sum_id) {
    this.sum_id = sum_id;
  }

  public String getVou_id() {
    return this.vou_id;
  }

  public void setVou_id(String vou_id) {
    this.vou_id = vou_id;
  }

  public String getAccount_id() {
    return this.account_id;
  }

  public void setAccount_id(String account_id) {
    this.account_id = account_id;
  }

  public BigDecimal getAvi_money() {
    return this.avi_money;
  }

  public void setAvi_money(BigDecimal avi_money) {
    this.avi_money = avi_money;
  }

  public BigDecimal getUse_money() {
    return this.use_money;
  }

  public void setUse_money(BigDecimal use_money) {
    this.use_money = use_money;
  }

  public BigDecimal getMinus_money() {
    return this.minus_money;
  }

  public void setMinus_money(BigDecimal minus_money) {
    this.minus_money = minus_money;
  }

  public BigDecimal getAving_money() {
    return this.aving_money;
  }

  public void setAving_money(BigDecimal aving_money) {
    this.aving_money = aving_money;
  }

  public String getRemark() {
    return this.remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getBal_version() {
    return this.bal_version;
  }

  public void setBal_version(String bal_version) {
    this.bal_version = bal_version;
  }

  public String getCreate_date() {
    return this.create_date;
  }

  public void setCreate_date(String create_date) {
    this.create_date = create_date;
  }

  public String getLatest_op_date() {
    return this.latest_op_date;
  }

  public void setLatest_op_date(String latest_op_date) {
    this.latest_op_date = latest_op_date;
  }

  public String getLast_ver() {
    return this.last_ver;
  }

  public void setLast_ver(String Last_ver) {
    this.last_ver = Last_ver;
  }

  public int getCtrl_side() {
    return ctrl_side;
  }

  public void setCtrl_side(int ctrl_side) {
    this.ctrl_side = ctrl_side;
  }

  public int getIs_enforce() {
    return is_enforce;
  }

  public void setIs_enforce(int is_enforce) {
    this.is_enforce = is_enforce;
  }

  public long getCcid() {
    return ccid;
  }

  public void setCcid(long ccid) {
    this.ccid = ccid;
  }

  public String getRg_code() {
    return rg_code;
  }

  public void setRg_code(String rg_code) {
    this.rg_code = rg_code;
  }

  public int getSet_month() {
    return set_month;
  }

  public void setSet_month(int set_month) {
    this.set_month = set_month;
  }

  public int getSet_year() {
    return set_year;
  }

  public void setSet_year(int set_year) {
    this.set_year = set_year;
  }

  public long getVou_type_id() {
    return vou_type_id;
  }

  public void setVou_type_id(long vou_type_id) {
    this.vou_type_id = vou_type_id;
  }

  public boolean equals(Object balance) {
    if (balance == null)
      return false;
    if (balance == this)
      return true;
    if (balance instanceof CtrlRecordDTO) {
      CtrlRecordDTO ctrl = (CtrlRecordDTO) balance;

      return StringUtil.equals(ctrl.getAccount_id(), account_id) && ctrl.getCcid() == ccid
        && ctrl.getSet_year() == set_year && StringUtil.equals(ctrl.getRg_code(), rg_code)
        && ctrl.getSet_month() == set_month;
    }
    return false;
  }

  public int hashCode() {
    int accountId = this.getAccount_id() == null ? 0 : this.getAccount_id().hashCode();
    int rgCode = this.getRg_code() == null ? 0 : this.getRg_code().hashCode();
    return (int) (accountId ^ ccid ^ set_year ^ rgCode ^ set_month);
  }

  public String getRcid() {
    return rcid;
  }

  public void setRcid(String rcid) {
    this.rcid = rcid;
  }

  public String getAccount_code() {
    return account_code;
  }

  public void setAccount_code(String account_code) {
    this.account_code = account_code;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("[balance_id=").append(this.balance_id).append(",sum_id=").append(this.sum_id).append(",ccid=")
      .append(this.ccid).append(",account_id=").append(this.account_id).append(",avi_money=").append(this.avi_money)
      .append(",use_money=").append(this.use_money).append(",minus_money=").append(this.minus_money)
      .append(",aving_money=").append(this.aving_money).append(",set_year=").append(this.set_year)
      .append(",set_month=").append(this.set_month).append(",rg_code=").append(this.rg_code).append(",agency_id=")
      .append(this.agency_id).append(",mb_id=").append(this.mb_id).append("]");
    return sb.toString();
  }

  public int getBalanceType() {
    return balanceType;
  }

  public void setBalanceType(int balanceType) {
    this.balanceType = balanceType;
  }

  /**
   * 本身金额取反
   */
  public void negative() {
    this.setAvi_money(this.getAvi_money().negate());
    this.setUse_money(this.getUse_money().negate());
    this.setMinus_money(this.getMinus_money().negate());
    this.setAving_money(this.getAving_money().negate());
    this.setBalanceType(this.getBalanceType() * -1);
  }

  /**
   * 判断额度可用数是否会减少
   * @return
   * ydz 判断临时额度是否会导致可用额度减少，只需要看额度合度的和值是否小于0。 小于0 true 反则 false
   * 同时如果 临时额度未发生变化，即 等于 0 的情况。 说明进行更新记账操作，由于进入此方法时，已是异常状态，已发现超支，谨慎原则，进行控制。
   */
  public boolean isBalanceWillDecrease() {
    return avi_money.add(use_money.negate()).add(minus_money.negate()).compareTo(NumberUtil.BIG_DECIMAL_ZERO) < 1;
  }

  /**
   * 判断额度金额是否存在负数
   * @return 为负数的金额
   * @author huanglifeng
   * @since 6.3.90.00
   */
  public int checkMoneyValid() {
    if (avi_money.signum() == -1) {
      return NEGATIVE_AVI;
    } else if (use_money.signum() == -1) {
      return NEGATIVE_USE;
    } else if (minus_money.signum() == -1) {
      return NEGATIVE_MINUS;
    } else if (aving_money.signum() == -1) {
      return NEGATIVE_AVING;
    } else
      return NEGATIVE_NONE;
  }

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public String getAgency_id() {
    if (agency_id == null || agency_id.equals(""))
      return "#";
    else
      return agency_id;
  }

  public void setAgency_id(String agency_id) {
    this.agency_id = agency_id;
  }

  public String getMb_id() {
    if (mb_id == null || mb_id.equals(""))
      return "#";
    else
      return mb_id;
  }

  public void setMb_id(String mb_id) {
    this.mb_id = mb_id;
  }

}
