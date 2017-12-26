package gov.df.fap.bean.gl.configure;

import gov.df.fap.util.StringUtil;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 记账模板科目挂接Bean
 * @author LiuYan
 * @version 2008-03-07
 */
public class BusVouAcctmdl implements Cloneable, Serializable {

  /**
  * 
  */
  private static final long serialVersionUID = 4419403136088063397L;

  private long acctmdl_id;

  private int entry_side;

  private int set_year;

  private String rg_code;

  private long vou_type_id;

  private BusVouType bvType;

  private BusVouAccount busVouAccount;

  private int is_primary_source;

  private int is_primary_target;

  /**
   * 0 不控制 1 警告控制 2 严格控制';
   */
  private int ctrl_level;

  private String account_id;

  private String account_name;

  public String getAccount_name() {
    return account_name;
  }

  public void setAccount_name(String account_name) {
    this.account_name = account_name;
  }

  private String lastest_op_date;

  private String lastest_op_user;

  private String last_ver;

  private String rule_id;

  private AccountEntry accountEntry;

  private List ruleEleConfigure = new LinkedList();

  public AccountEntry getAccountEntry() {
    return accountEntry;
  }

  public void setAccountEntry(AccountEntry accountEntry) {
    this.accountEntry = accountEntry;
  }

  public BusVouAcctmdl() {
    super();
  }

  //界面中显示自动调用此方法
  public String toString() {
    return accountEntry == null ? "" : accountEntry.getEntryCode() + " " + accountEntry.getEntryName();
  }

  public String getLast_ver() {
    return last_ver;
  }

  public void setLast_ver(String last_ver) {
    this.last_ver = last_ver;
  }

  public String getLastest_op_date() {
    return lastest_op_date;
  }

  public void setLastest_op_date(String lastest_op_date) {
    this.lastest_op_date = lastest_op_date;
  }

  public String getLastest_op_user() {
    return lastest_op_user;
  }

  public void setLastest_op_user(String lastest_op_user) {
    this.lastest_op_user = lastest_op_user;
  }

  public String getAccount_id() {
    return busVouAccount == null ? account_id : busVouAccount.getAccountId();
  }

  public void setAccount_id(String account_id) {
    this.account_id = account_id;
  }

  public long getAcctmdl_id() {
    return acctmdl_id;
  }

  public void setAcctmdl_id(long acctmdl_id) {
    this.acctmdl_id = acctmdl_id;
  }

  public int getCtrl_level() {
    return ctrl_level;
  }

  public void setCtrl_level(int ctrl_level) {
    this.ctrl_level = ctrl_level;
  }

  public int getEntry_side() {
    return entry_side;
  }

  public void setEntry_side(int entry_side) {
    this.entry_side = entry_side;
  }

  public int getIs_primary_source() {
    return is_primary_source;
  }

  public void setIs_primary_source(int is_primary_source) {
    this.is_primary_source = is_primary_source;
  }

  public int getIs_primary_target() {
    return is_primary_target;
  }

  public void setIs_primary_target(int is_primary_target) {
    this.is_primary_target = is_primary_target;
  }

  /**
   * 设置交易令中同一种科目并且是同一种方向的isPrimarySource和isPirmartTarget属性。
   * @author 
   * @since 6.2.61.07
   */
  public void synchAssociatPrimary() {
    if (getBvType() != null) {
      final Iterator iterator = this.getBvType().getBusVouAcctmdl().iterator();
      BusVouAcctmdl acctmdl = null;
      while (iterator.hasNext()) {
        acctmdl = (BusVouAcctmdl) iterator.next();
        if (this.getBusVouAccount().equals(acctmdl.getBusVouAccount())
          && acctmdl.isPlusAccountBalance() == this.isPlusAccountBalance()) {
          acctmdl.setIs_primary_source(this.is_primary_source);
          acctmdl.setIs_primary_target(this.is_primary_target);
        }
      }
    }
  }

  public String getRg_code() {
    return rg_code;
  }

  public void setRg_code(String rg_code) {
    this.rg_code = rg_code;
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

  public BusVouAccount getBusVouAccount() {
    return busVouAccount;
  }

  public void setBusVouAccount(BusVouAccount busVouAccount) {
    this.busVouAccount = busVouAccount;
  }

  public String getRule_id() {
    if (rule_id == null)
      rule_id = "0";
    return rule_id;
  }

  public void setRule_id(String rule_id) {
    this.rule_id = rule_id;
  }

  public BusVouType getBvType() {
    return bvType;
  }

  public void setBvType(BusVouType bvType) {
    this.bvType = bvType;
  }

  /**
   * 连接记账模板，先断开与原来记账模板连接，再连接新的。
   * @author
   * @since 6.2.61.07
   */
  public void connectBvType(BusVouType bvType) {
    if (bvType == this.bvType)
      return;

    disConnectBvType();

    List acctmdls = bvType.getBusVouAcctmdl();
    if (!acctmdls.contains(this))
      acctmdls.add(this);

    this.bvType = bvType;
  }

  /**
   * 断开也记账模板关联
   * @author
   * @since 6.2.61.07
   */
  public void disConnectBvType() {
    if (bvType != null) {
      BusVouType parent = bvType;
      List acctmdls = parent.getBusVouAcctmdl();
      acctmdls.remove(this);
      bvType = null;
    }
  }

  /**
   * 判断是否生成额度
   * @return true 生成额度  false使用额度
   */
  public boolean isPlusAccountBalance() {
    if (getBusVouAccount() == null)
      throw new RuntimeException("线上科目为空！");
    return getEntry_side() == getBusVouAccount().getBalanceSide();
  }

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  /**
   * 获取借方名称
   * @return
   */
  public String getDebit() {
    String accountName = "";
    if (this.getBusVouAccount() != null)
      accountName = getBusVouAccount().getAccountName();
    return !isPlusAccountBalance() ? accountName : "";
  }

  /**
   * 使用account_id判断两个acctmdl对象是否相等
   */
  public boolean equals(Object acctmdl) {
    if (acctmdl == null)
      return false;

    if (!(acctmdl instanceof BusVouAcctmdl)) {
      return false;
    }

    BusVouAcctmdl acm = (BusVouAcctmdl) acctmdl;
    return acm.getAcctmdl_id() == this.acctmdl_id;
  }

  public int hashCode() {
    return (int) this.acctmdl_id;
  }

  /**
   * 获取贷方名称
   * @return
   */
  public String getCredit() {
    String accountName = StringUtil.EMPTY;
    if (this.getBusVouAccount() != null)
      accountName = getBusVouAccount().getAccountName();
    return isPlusAccountBalance() ? accountName : "";
  }

  /**
   * copy部分重要属性
   * @param acctmdl
   */
  public void copy(BusVouAcctmdl acctmdl) {
    this.setCtrl_level(acctmdl.getCtrl_level());
    this.setEntry_side(acctmdl.getEntry_side());
    this.setIs_primary_source(acctmdl.getIs_primary_source());
    this.setIs_primary_target(acctmdl.getIs_primary_target());
    this.setRule_id(acctmdl.getRule_id());
    this.setBusVouAccount(acctmdl.getBusVouAccount());
    this.setBvType(acctmdl.getBvType());
  }

  public List getRuleEleConfigure() {
    return ruleEleConfigure;
  }

  public void setRuleEleConfigure(List ruleEleConfigure) {
    this.ruleEleConfigure = ruleEleConfigure;
  }
}
