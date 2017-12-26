package gov.df.fap.service.gl.je;

import gov.df.fap.bean.gl.dto.JournalDTO;
import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.service.gl.core.DaoSupport;
import gov.df.fap.service.util.datasource.TypeOfDB;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author 
 * @version 
 */
@Component("journalDao")
public class JournalDao implements IJournalDao {
  @Autowired
  @Qualifier("df.fap.daoSupport")
  private DaoSupport daoSupport;

  public void setDaoSupport(DaoSupport daoSupport) {
    this.daoSupport = daoSupport;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.je.IJournalDao#insertJournalCache(java.util.List)
   */
  public void insertJournalCache(List dtoList) {
    StringBuffer strsql = new StringBuffer();
    strsql.append("insert into gl_journal_cache ").append("(journal_id, set_year, rg_code, vou_id, vou_type_id, ")
      .append("billtype_code, vou_money, set_month, ccid, rcid, is_end, ")
      .append("is_valid, action_id, bill_date, remark, bal_version, ")
      .append("create_date, latest_op_date, last_ver,index_, ctrl_level)").append(" values (").append("#journal_id#")
      .append(",#set_year#").append(",#rg_code#").append(",#vou_id#").append(",#vou_type_id#")
      .append(",#billtype_code#").append(",#vou_money#").append(",#set_month#").append(",#ccid#").append(",#rcid#")
      .append(",#is_end#").append(",#is_valid#").append(",#action_id#").append(",#bill_date#").append(",#remark#")
      .append(",#bal_version#").append(",#create_date#").append(",#latest_op_date#").append(",#last_ver#")
      .append(",#index_#").append(",#ctrlLevel#)");
    if (dtoList != null && dtoList.size() > 0) {
      daoSupport.batchExcute(strsql.toString(), dtoList);
    }
  }

  /**
   * 将传入的交易凭证journalid批量更新日志正式表set is_valid=is_valid 实现行级锁定避免同一笔数据做两次记账金额修改
   * @param dtoList 交易凭证列表
   * @throws Exception
   */
  public void lockedJournalByUpdate(List dtoList) {
    String strsql = "update gl_journal set is_valid = is_valid where journal_id=#journal_id#";
    if (dtoList != null && dtoList.size() > 0) {
      daoSupport.batchExcute(strsql.toString(), dtoList);
    }
  }

  /* (non-Javadoc)
  * @see gov.gfmis.fap.gl.je.IJournalDao#insertJournalByCache()
  */
  public void insertJournalByCache() {
    StringBuffer buffer = new StringBuffer();
    buffer
      .append("insert into gl_journal(")
      .append("journal_id, set_year, rg_code, vou_id,")
      .append("vou_type_id, billtype_code, vou_money, set_month, ccid, rcid, is_end, is_valid, action_id,")
      .append("bill_date, remark, bal_version, create_date, latest_op_date, last_ver)")
      .append("select ")
      .append("journal_id, set_year, rg_code, vou_id,")
      .append("vou_type_id, billtype_code, vou_money, set_month, ccid, rcid, is_end, is_valid, action_id,")
      .append("bill_date, remark, bal_version, create_date, latest_op_date, last_ver ")
      .append(
        " from gl_journal_cache c where not exists(select 1 from gl_journal j where " + getRelantionSql("j", "c") + ")");
    daoSupport.executeUpdate(buffer.toString());
  }

  /* (non-Javadoc)
  * @see gov.gfmis.fap.gl.je.IJournalDao#backUpServiceWithCache()
  */
  public void backUpServiceWithCache() {
    StringBuffer buffer = new StringBuffer();
    //将日志缓存插入到历史日志中
    buffer.append("insert into gl_journal_his  (journal_id,set_year,rg_code,vou_id,vou_type_id,billtype_code,")
      .append("vou_money,set_month,ccid,rcid,is_end,is_valid,")
      .append("action_id,bill_date,remark,bal_version,create_date,latest_op_date,").append("last_ver)")
      .append(" select ").append("journal_id,set_year,rg_code,vou_id,vou_type_id,billtype_code,")
      .append("vou_money,set_month,ccid,rcid,is_end,is_valid,")
      .append("action_id,bill_date,remark,bal_version,create_date,latest_op_date,").append("last_ver")
      .append(" from gl_journal_cache");
    daoSupport.executeUpdate(buffer.toString());
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.je.IJournalDao#updateJournalWithCache()
   */
  public void updateJournalWithCache() {
    StringBuffer strSql = new StringBuffer();
    if (TypeOfDB.isOracle()) {
      strSql.append("update /*+index(j IDX_GL_JOURNAL_VOU) */ gl_journal j set (j.billtype_code,")
        .append("j.vou_money,j.set_month,j.ccid,j.rcid,j.is_end,j.is_valid,")
        .append("j.action_id,j.bill_date,j.remark,j.bal_version,j.latest_op_date,").append("j.last_ver)=")
        .append("(select c.billtype_code,").append("c.vou_money,c.set_month,c.ccid,c.rcid,c.is_end,c.is_valid,")
        .append("c.action_id,c.bill_date,c.remark,c.bal_version,c.latest_op_date,")
        .append("c.last_ver from gl_journal_cache c where ").append(getRelantionSql("c", "j")).append(")")
        .append(" where exists(select 1 from gl_journal_cache c where " + getRelantionSql("c", "j") + ")");
    } else if (TypeOfDB.isMySQL()) {
      strSql
        .append(
          "UPDATE gl_journal j INNER JOIN gl_journal_cache c "
            + "SET j.billtype_code = c.billtype_code,j.vou_money = c.vou_money,"
            + "j.set_month = c.set_month,j.ccid = c.ccid,j.rcid = c.rcid,"
            + "j.is_end = c.is_end, j.is_valid = c.is_valid, j.action_id = c.action_id, "
            + "j.bill_date = c.bill_date, j.remark = c.remark, j.bal_version= c.bal_version, "
            + "j.latest_op_date = c.latest_op_date, j.last_ver = c.last_ver on")
        .append(getRelantionSql("c", "j"))
        .append(
          " WHERE  EXISTS (    SELECT      1   FROM      gl_journal_cache c8 where" + getRelantionSql("c8", "j") + ")");
    }
    daoSupport.executeUpdate(strSql.toString());
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.je.IJournalDao#deleteJournal()
   */
  public void deleteJournal() throws Exception {
    StringBuffer strsql = new StringBuffer();
    strsql.append(" delete from gl_journal_his t where exists (").append(
      " select 1 from gl_journal_cache c where " + getRelantionSql("t", "c") + ")");
    daoSupport.executeUpdate(strsql.toString());
    strsql.delete(0, strsql.length());
    strsql.append(" delete from gl_journal t where exists (").append(
      " select 1 from gl_journal_cache c where " + getRelantionSql("t", "c") + ")");
    daoSupport.executeUpdate(strsql.toString());
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.je.IJournalDao#findExistsJournalWithCache()
   */
  public List findExistsJournalWithCache() {

    String sql = "select /*+index(j IDX_GL_JOURNAL_VOU) */ j.*, (select  ctrlid from gl_balance_trace t where "
      + getRelantionSql("t", "j")
      + " and t.is_primary=1 and t.ctrl_side=1 and rownum=1) primarySourceId, c.index_, c.ctrl_level ctrlLevel from gl_journal j, gl_journal_cache c where "
      + getRelantionSql("c", "j") + " order by c.index_";

    if (TypeOfDB.isMySQL()) {
      sql = "select /*+index(j IDX_GL_JOURNAL_VOU) */ j.*, (select  ctrlid from gl_balance_trace t where "
        + getRelantionSql("t", "j")
        + " and t.is_primary=1 and t.ctrl_side=1 limit 0,1) primarySourceId, c.index_, c.ctrl_level ctrlLevel from gl_journal j, gl_journal_cache c where "
        + getRelantionSql("c", "j") + " order by c.index_";
    }

    return daoSupport.genericQuery(sql, JournalDTO.class);
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.gl.je.IJournalDao#findNotExistsJournalWithCache()
   */
  public List findNotExistsJournalWithCache() {
    StringBuffer strsql = new StringBuffer();
    strsql.append(" select a.* from gl_journal_cache a ").append(" where not exists")
      .append("(select 1 from gl_journal b where ").append(getRelantionSql("b", "a")).append(")");
    return daoSupport.genericQuery(strsql.toString(), JournalDTO.class);
  }

  private String getRelantionSql(String cacheAlias, String journalAlias) {
    StringBuffer sb = new StringBuffer();
    sb.append(cacheAlias).append(".vou_id=").append(journalAlias).append(".vou_id and ").append(cacheAlias)
      .append(".vou_type_id=").append(journalAlias).append(".vou_type_id and ").append(cacheAlias).append(".set_year=")
      .append(journalAlias).append(".set_year and ").append(cacheAlias).append(".rg_code=").append(journalAlias)
      .append(".rg_code ");
    return sb.toString();
  }

  /* (non-Javadoc)
  * @see gov.gfmis.fap.gl.je.IJournalDao#deleteJournalCache()
  */
  public void deleteJournalCache() {
    daoSupport.executeUpdate(" delete from gl_journal_cache");
  }

  /* (non-Javadoc)
  * @see gov.gfmis.fap.gl.je.IJournalDao#findVoucherByBalance(java.lang.String, int)
  */
  public List findVoucherByBalance(String sumId, int side) {
    return daoSupport.genericQuery(
      "select * from gl_journal j where j.is_valid=1 and exists(select 1 from gl_balance_trace t where t.ctrlid="
        + sumId
        + " and t.vou_id=j.vou_id and t.vou_type_id=j.vou_type_id and t.set_year=j.set_year and t.rg_code=j.rg_code)",
      FVoucherDTO.class);
  }
}