package gov.df.fap.bean.gl.configure;

import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.util.StringUtil;

import java.io.Serializable;

/**
 * 科目Bean
 * @author LiuYan
 * @version 2008-03-07
 */
public class BusVouAccount implements Serializable, Cloneable {

  /**
   * 
   */
  private static final long serialVersionUID = 3412283771742889982L;

  /**全年控制*/
  public static final int BALANCE_PERIOD_TYPE_YEAR = 0;

  /**累计月*/
  public static final int BALANCE_PERIOD_TYPE_SUM_MONTH = 1;

  /**分月*/
  public static final int BALANCE_PERIOD_TYPE_MONTH = 2;

  /**余额方向:借方*/
  public static final int BALANCE_SIDE_DEBIT = 1;

  /**余额方向:贷方*/
  public static final int BALANCE_SIDE_CREDIT = 0;

  private String accountId = StringUtil.EMPTY;

  private String chr_id = StringUtil.EMPTY;

  private String chr_name = StringUtil.EMPTY;

  public String getChr_id() {
    return chr_id;
  }

  public void setChr_id(String chr_id) {
    this.chr_id = chr_id;
  }

  public String getChr_name() {
    return chr_name;
  }

  public void setChr_name(String chr_name) {
    this.chr_name = chr_name;
  }

  private int setYear = 0;

  private String rgCode = StringUtil.EMPTY;

  private String accountCode = StringUtil.EMPTY;

  private String chr_code = StringUtil.EMPTY;

  public String getChr_code() {
    return chr_code;
  }

  public void setChr_code(String chr_code) {
    this.chr_code = chr_code;
  }

  private String accountName = StringUtil.EMPTY;

  private String codename = StringUtil.EMPTY;

  public String getCodename() {
    return codename;
  }

  public void setCodename(String codename) {
    this.codename = codename;
  }

  private int balanceSide = 1;//默认借方 

  private int balancePeriodType = 0; //默认全年 

  private long coaId = 0;

  private String tableName = StringUtil.EMPTY;

  private String monthDetailTableName = StringUtil.EMPTY;

  private FCoaDTO coaDto = null;

  private int subjectType = 0;//科目类型(1#资产类,2#负债类,3#净资产类,4#收入类,5#支出类,6#其他类)

  private int subjectKind = -1;//科目性质,科目性质（0：一般科目、1：固定资产、2：往来、3：日记帐）

  private int enabled = 0;

  private String latestOpDate = StringUtil.EMPTY;

  private String dispCode = StringUtil.EMPTY;

  private int levelNum = 0;

  private int isLeaf = 0;

  private String createDate = StringUtil.EMPTY;

  private String createUser = StringUtil.EMPTY;

  private int isDeleted = 0;

  private String lastVer = StringUtil.EMPTY;

  private String latestOpUser = StringUtil.EMPTY;

  private String chrCode1 = StringUtil.EMPTY;

  private String chrCode2 = StringUtil.EMPTY;

  private String chrCode3 = StringUtil.EMPTY;

  private String chrCode4 = StringUtil.EMPTY;

  private String chrCode5 = StringUtil.EMPTY;

  private String chrId1 = StringUtil.EMPTY;

  private String chrId2 = StringUtil.EMPTY;

  private String chrId3 = StringUtil.EMPTY;

  private String chrId4 = StringUtil.EMPTY;

  private String chrId5 = StringUtil.EMPTY;

  private String parentId = StringUtil.EMPTY;

  private String parent_id = StringUtil.EMPTY;

  public String getParent_id() {
    return parent_id;
  }

  public void setParent_id(String parent_id) {
    this.parent_id = parent_id;
  }

  private String stId = StringUtil.EMPTY;

  private String hintCode = StringUtil.EMPTY;

  public Object getKey() {
    return new CommonKey(accountId, String.valueOf(setYear), rgCode);
  }

  public Object getCodeKey() {
    return new CommonKey(accountCode, String.valueOf(setYear), rgCode);
  }

  public String getChrCode1() {
    return chrCode1;
  }

  public void setChrCode1(String chrCode1) {
    this.chrCode1 = chrCode1;
  }

  public String getChrCode2() {
    return chrCode2;
  }

  public void setChrCode2(String chrCode2) {
    this.chrCode2 = chrCode2;
  }

  public String getChrCode3() {
    return chrCode3;
  }

  public void setChrCode3(String chrCode3) {
    this.chrCode3 = chrCode3;
  }

  public String getChrCode4() {
    return chrCode4;
  }

  public void setChrCode4(String chrCode4) {
    this.chrCode4 = chrCode4;
  }

  public String getChrCode5() {
    return chrCode5;
  }

  public void setChrCode5(String chrCode5) {
    this.chrCode5 = chrCode5;
  }

  public String getChrId1() {
    return chrId1;
  }

  public void setChrId1(String chrId1) {
    this.chrId1 = chrId1;
  }

  public String getChrId2() {
    return chrId2;
  }

  public void setChrId2(String chrId2) {
    this.chrId2 = chrId2;
  }

  public String getChrId3() {
    return chrId3;
  }

  public void setChrId3(String chrId3) {
    this.chrId3 = chrId3;
  }

  public String getChrId4() {
    return chrId4;
  }

  public void setChrId4(String chrId4) {
    this.chrId4 = chrId4;
  }

  public String getChrId5() {
    return chrId5;
  }

  public void setChrId5(String chrId5) {
    this.chrId5 = chrId5;
  }

  public String getCreateDate() {
    return createDate;
  }

  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }

  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }

  public String getDispCode() {
    return dispCode;
  }

  public void setDispCode(String dispCode) {
    this.dispCode = dispCode;
  }

  public String getHintCode() {
    return hintCode;
  }

  public void setHintCode(String hintCode) {
    this.hintCode = hintCode;
  }

  public int getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(int isDeleted) {
    this.isDeleted = isDeleted;
  }

  public int getIsLeaf() {
    return isLeaf;
  }

  public void setIsLeaf(int isLeaf) {
    this.isLeaf = isLeaf;
  }

  public String getLastVer() {
    return lastVer;
  }

  public void setLastVer(String lastVer) {
    this.lastVer = lastVer;
  }

  public String getLatestOpUser() {
    return latestOpUser;
  }

  public void setLatestOpUser(String latestOpUser) {
    this.latestOpUser = latestOpUser;
  }

  public int getLevelNum() {
    return levelNum;
  }

  public void setLevelNum(int levelNum) {
    this.levelNum = levelNum;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public String getStId() {
    return stId;
  }

  public void setStId(String stId) {
    this.stId = stId;
  }

  public String getLatestOpDate() {
    return latestOpDate;
  }

  public void setLatestOpDate(String latestOpDate) {
    this.latestOpDate = latestOpDate;
  }

  public int getEnabled() {
    return enabled;
  }

  public void setEnabled(int enabled) {
    this.enabled = enabled;
  }

  //界面中显示用的方法
  public String toString() {
    return accountCode + " " + accountName;
  }

  public FCoaDTO getCoaDto() {
    return coaDto;
  }

  public void setCoaDto(FCoaDTO coaDto) {
    this.coaDto = coaDto;
  }

  public int getBalancePeriodType() {
    return balancePeriodType;
  }

  public void setBalancePeriodType(int balancePeriodType) {
    this.balancePeriodType = balancePeriodType;
  }

  public String getTableName() {
    if (StringUtil.isEmpty(tableName))
      return "gl_balance";
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getMonthDetailTableName() {
    if (StringUtil.isEmpty(monthDetailTableName))
      return "gl_balance";
    return monthDetailTableName;
  }

  public void setMonthDetailTableName(String monthDetailTableName) {
    this.monthDetailTableName = monthDetailTableName;
  }

  public String getAccountCode() {
    return accountCode;
  }

  public void setAccountCode(String accountCode) {
    this.accountCode = accountCode;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public int getBalanceSide() {
    return balanceSide;
  }

  public void setBalanceSide(int balanceSide) {
    this.balanceSide = balanceSide;
  }

  public long getCoaId() {
    return coaId;
  }

  public void setCoaId(long coa_id) {
    this.coaId = coa_id;
  }

  public String getRgCode() {
    return rgCode;
  }

  public void setRgCode(String rgCode) {
    this.rgCode = rgCode;
  }

  public int getSetYear() {
    return setYear;
  }

  public void setSetYear(int setYear) {
    this.setYear = setYear;
  }

  public int getSubjectKind() {
    return subjectKind;
  }

  public void setSubjectKind(int subjectKind) {
    this.subjectKind = subjectKind;
  }

  public int getSubjectType() {
    return subjectType;
  }

  public void setSubjectType(int subjectType) {
    this.subjectType = subjectType;
  }

  /**
   * 判断科目是否相等，仅仅使用ID判断
   */
  public boolean equals(Object account) {
    if (account == null)
      return false;

    if (!(account instanceof BusVouAccount))
      return false;

    BusVouAccount a = (BusVouAccount) account;

    return StringUtil.equals(this.accountId, a.getAccountId());
  }

  public int hashCode() {
    return this.accountId.hashCode();
  }

  /**
   * 根据另外传入的对象更新本身属性
   * @param acount
   */
  public void copy(BusVouAccount account) {
    if (account == null)
      return;
    setAccountCode(account.getAccountCode());
    setAccountName(account.getAccountName());
    setBalancePeriodType(account.getBalancePeriodType());
    setBalanceSide(account.getBalanceSide());
    setCoaId(account.getCoaId());
    setTableName(account.getTableName());
    setMonthDetailTableName(account.getMonthDetailTableName());
    setSubjectKind(account.getSubjectKind());
    setSubjectType(account.getSubjectType());
    if (account.getCoaDto() != null) {
      setCoaId(new Long(account.getCoaDto().getCoaId()).longValue());
      setCoaDto(account.getCoaDto());
    }
  }

  /**
   * 克隆方法
   */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
