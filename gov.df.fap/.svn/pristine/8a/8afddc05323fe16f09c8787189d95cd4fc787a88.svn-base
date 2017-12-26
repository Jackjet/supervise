package gov.df.fap.service.gl.configure.impl;

import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.service.gl.core.DaoSupport;
import gov.df.fap.service.util.gl.configure.IAccountDao;
import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.date.DateHandler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 科目数据访问类,封装数据库操作.
 * @author  justin
 * @version 2017-03-30
 */
@Component
public class AccountDao implements IAccountDao {

  @Autowired
  @Qualifier("df.fap.daoSupport")
  DaoSupport daoSupport;

  public void setDaoSupport(DaoSupport dao) {
    this.daoSupport = dao;
  }

  /**
   * 查询科目
   * @param accountId 科目ID
   * @return
   */
  public BusVouAccount loadAccount(String accountId) {
    return (BusVouAccount) daoSupport
      .genericQueryForObject(
        "select account_id accountId, set_year setYear, rg_code rgCode, account_code accountCode, account_name accountName, balance_side balanceSide, balance_period_type balancePeriodType, coa_id coaId, table_name tableName, monthdetail_table_name monthDetailTableName, subject_kind subjectKind, subject_type subjectType, enabled, st_id stId from"
          + " vw_gl_account where account_id = ? and rg_code = ? and set_year = ?", new Object[] { accountId,
          CommonUtil.getRgCode(), CommonUtil.getSetYear() }, BusVouAccount.class);
  }

  /**
   * 读取科目
   * @param accountCode 科目编码
   * @return
   */
  public BusVouAccount loadAccountByCode(String accountCode) {
    String rgCode = CommonUtil.getRgCode();
    return (BusVouAccount) daoSupport
      .genericQueryForObject(
        "select account_id accountId, set_year setYear, rg_code rgCode, account_code accountCode, account_name accountName, balance_side balanceSide, balance_period_type balancePeriodType, coa_id coaId, table_name tableName, monthdetail_table_name monthDetailTableName, subject_kind subjectKind, subject_type subjectType, enabled, st_id stId from"
          + " vw_gl_account where account_code = ? and rg_code = ? and set_year = ?", new Object[] { accountCode,
          rgCode, CommonUtil.getSetYear() }, BusVouAccount.class);
  }

  /**
   * 去处所有科目信息
   * 数据源是vw_gl_account,若取出缺少数据项,更改此视图
   * @return
   */
  public List allAccount() {
    String setYear = CommonUtil.getSetYear();
    String rgCode = CommonUtil.getRgCode();
    return daoSupport
      .genericQuery(
        "select account_id accountId, set_year setYear, rg_code rgCode, account_code accountCode, account_name accountName, balance_side balanceSide, balance_period_type balancePeriodType, coa_id coaId, table_name tableName, monthdetail_table_name monthDetailTableName, subject_kind subjectKind, subject_type subjectType, enabled, "
          + "disp_code dispCode, level_num levelNum, is_leaf isLeaf, create_date createDate, create_user createUser, is_deleted isDeleted, last_ver lastVer, latest_op_user latestOpUser, chr_code1 chrCode1, chr_code2 chrCode2, chr_code3 chrCode3, chr_code4 chrCode4, chr_code5 chrCode5, chr_id1 chrId1, chr_id2 chrId2,"
          + " chr_id3 chrId3, chr_id4 chrId4, chr_id5 chrId5, parent_id parentId, st_id stId, hint_code from vw_gl_account where set_year = ? and rg_code = ? order by account_code",
        new Object[] { setYear, rgCode }, BusVouAccount.class);
  }

  /**
   * 保存科目信息
   * @param bvAccount
   */
  public void saveAccount(BusVouAccount bvAccount) {
    bvAccount.setAccountId(CommonUtil.generateUuid(CommonUtil.GUID_STRING));
    bvAccount.setSetYear(CommonUtil.getIntSetYear());
    saveAccountByOrigin(bvAccount);
  }

  /**
   * 保存科目信息,不补充辅助信息
   * @param bvAccount
   */
  public void saveAccountByOrigin(BusVouAccount bvAccount) {
    bvAccount.setLatestOpDate(DateHandler.getDateFromNow(0));
    if (StringUtil.isEmpty(bvAccount.getStId()))
      bvAccount.setStId(getStId());
    StringBuffer insertSQL = new StringBuffer();
    try {
      insertSQL
        .append(
          "insert into ele_accountant_subject(chr_id, chr_code, chr_name, is_debit, balance_period_type, coa_id, table_name, monthdetail_table_name, subject_type, subject_kind, enabled, latest_op_date, set_year, disp_code, level_num, is_leaf, create_date, create_user, is_deleted, latest_op_user, last_ver, chr_code1, chr_code2, chr_code3, chr_code4, chr_code5, rg_code, parent_id, chr_id1, chr_id2, chr_id3, chr_id4, chr_id5, st_id, hint_code) values (")
        .append(" #accountId#").append(",#accountCode#").append(",#accountName#").append(",#balanceSide#")
        .append(",#balancePeriodType#").append(",#coaId#").append(",#tableName#").append(",#monthDetailTableName#")
        .append(",#subjectType#").append(",#subjectKind#").append(",#enabled#").append(",#latestOpDate#")
        .append(",#setYear#").append(",#accountCode#").append(",#levelNum#").append(",#isLeaf#")
        .append(",#createDate#").append(",#createUser#").append(",#isDeleted#").append(",#latestOpUser#")
        .append(",#lastVer#").append(",#chrCode1#").append(",#chrCode2#").append(",#chrCode3#").append(",#chrCode4#")
        .append(",#chrCode5#").append(",#rgCode#").append(",#parentId#").append(",#chrId1#").append(",#chrId2#")
        .append(",#chrId3#").append(",#chrId4#").append(",#chrId5#").append(",#stId#").append(",#hintCode#")
        .append(")");
      daoSupport.executeUpdate(insertSQL.toString(), bvAccount);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * 查询返回指标帐套ID
   * @return
   */
  public String getStId() {
    String sql = "select chr_id from ele_book_set where chr_code='000000' and rg_code=? and set_year=?"; //dingyy20120530 加上区划过滤
    return daoSupport.queryForString(sql, new String[] { SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * 根据传入科目的id更新传入的科目
   * @param bvAccount
   */
  public void updateAccount(BusVouAccount bvAccount) {
    bvAccount.setLatestOpDate(DateHandler.getDateFromNow(0));
    StringBuffer updateSQL = new StringBuffer();
    updateSQL.append("update ele_accountant_subject set chr_code=#accountCode#,").append("chr_name=#accountName#,")
      .append("latest_op_date=#latestOpDate#,").append("is_debit=#balanceSide#,")
      .append("balance_period_type=#balancePeriodType#,").append("coa_id=#coaId#,").append("table_name=#tableName#,")
      .append("monthdetail_table_name=#monthDetailTableName#,").append("subject_kind=#subjectKind#,")
      .append("subject_type=#subjectType#,").append("enabled=#enabled# ").append("where chr_id=#accountId#");
    daoSupport.executeUpdate(updateSQL.toString(), bvAccount);
  }

  /**
   * 删除传入的科目信息
   * @param bvAccount
   */
  public void deleteAccount(BusVouAccount bvAccount) {
    StringBuffer deleteSQL = new StringBuffer();
    deleteSQL.append("delete ele_accountant_subject where chr_id=#accountId#");
    daoSupport.executeUpdate(deleteSQL.toString(), bvAccount);
  }

  public void deleteAllVwGlAccount() {
    String sql = "delete from vw_gl_account where rg_code='" + CommonUtil.getRgCode() + "' and set_year='"
      + CommonUtil.getSetYear() + "'";
    daoSupport.execute(sql);
  }

  /**
   * 删除所有科目信息
   */
  public void deleteAllAccount() {
    String sql = "delete ele_accountant_subject where rg_code='" + CommonUtil.getRgCode() + "' and set_year='"
      + CommonUtil.getSetYear() + "'";
    daoSupport.execute(sql);
  }
}
