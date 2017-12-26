/**
 * <p>Copyright 2007 by Digiark Software corporation,
 * All rights reserved.
 * <p>版权所有：用友政务软件有限公司
 * <p>未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 * <p>侵权者将受到法律追究。
 */
package gov.df.fap.service.gl.balance.impl;

import gov.df.fap.api.rule.IDataRight;
import gov.df.fap.bean.gl.balance.MonthlyBalanceDTO;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.configure.BusVouType;
import gov.df.fap.bean.gl.dto.Condition;
import gov.df.fap.bean.gl.dto.ConditionObj;
import gov.df.fap.bean.gl.dto.CtrlRecordDTO;
import gov.df.fap.bean.gl.dto.FCtrlRecordDTO;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.service.gl.balance.BalanceTracer;
import gov.df.fap.service.gl.balance.IBalanceDao;
import gov.df.fap.service.gl.core.DaoSupport;
import gov.df.fap.service.gl.core.interfaces.ResultSetMapper;
import gov.df.fap.service.gl.core.sqlgen.PlusSql;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.gl.balance.BalanceUtil;
import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.date.DateHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 * @version
 */
@Component("BalanceDao")
public class BalanceDao implements IBalanceDao {

	/** 非累计月控制select 字段 */
	private static final String NOT_SUM_FOR_MONTH_SELECT_FIELD = " sum_id as sum_id,nvl((avi_money-use_money-minus_money),0) as canuse_money,nvl(avi_money,0) as avi_money,nvl(use_money,0) as use_money, nvl(minus_money,0) as minus_money,nvl(aving_money,0) as aving_money,ccid as ccid, set_month as set_month, fromctrlid as fromctrlid, rcid as rcid, bal_version as bal_version ";
	
	//为了兼容MySQL nvl -> ifnull
	private static final String NOT_SUM_FOR_MONTH_SELECT_FIELD_MYSQL = " sum_id as sum_id,ifnull((avi_money-use_money-minus_money),0) as canuse_money,ifnull(avi_money,0) as avi_money,ifnull(use_money,0) as use_money, ifnull(minus_money,0) as minus_money,ifnull(aving_money,0) as aving_money,ccid as ccid, set_month as set_month, fromctrlid as fromctrlid, rcid as rcid, bal_version as bal_version ";
	
	private static final String ALIAS_SELECT_FIELD = " #alias#.sum_id as sum_id,nvl((#alias#.avi_money-#alias#.use_money-#alias#.minus_money),0) as canuse_money,nvl(#alias#.avi_money,0) as avi_money,nvl(#alias#.use_money,0) as use_money, nvl(#alias#.minus_money,0) as minus_money,nvl(#alias#.aving_money,0) as aving_money,#alias#.ccid as ccid, #alias#.set_month as set_month, #alias#.fromctrlid as fromctrlid, #alias#.rcid as rcid, #alias#.bal_version as bal_version,  #alias#.remark as remark ";
	
	//为了兼容MySQL nvl -> ifnull
	private static final String ALIAS_SELECT_FIELD_MYSQL = " #alias#.sum_id as sum_id,ifnull((#alias#.avi_money-#alias#.use_money-#alias#.minus_money),0) as canuse_money,ifnull(#alias#.avi_money,0) as avi_money,ifnull(#alias#.use_money,0) as use_money, ifnull(#alias#.minus_money,0) as minus_money,ifnull(#alias#.aving_money,0) as aving_money,#alias#.ccid as ccid, #alias#.set_month as set_month, #alias#.fromctrlid as fromctrlid, #alias#.rcid as rcid, #alias#.bal_version as bal_version,  #alias#.remark as remark ";

	private static final String ALIAS_SELECT_FIELD_FOR_DETAIL = " #alias#.sum_id as sum_id,nvl(sum(#alias#.avi_money-#alias#.use_money-#alias#.minus_money),0) as canuse_money,nvl(sum(#alias#.avi_money),0) as avi_money,nvl(sum(#alias#.use_money),0) as use_money, nvl(sum(#alias#.minus_money),0) as minus_money,nvl(sum(#alias#.aving_money),0) as aving_money,max(#alias#.ccid) as ccid, max(#alias#.set_month) as set_month, max(#alias#.fromctrlid) as fromctrlid, max(#alias#.rcid) as rcid, max(#alias#.bal_version) as bal_version ";

	//为了兼容MySQL nvl -> ifnull
	private static final String ALIAS_SELECT_FIELD_FOR_DETAIL_MYSQL = " #alias#.sum_id as sum_id,ifnull(sum(#alias#.avi_money-#alias#.use_money-#alias#.minus_money),0) as canuse_money,ifnull(sum(#alias#.avi_money),0) as avi_money,ifnull(sum(#alias#.use_money),0) as use_money, ifnull(sum(#alias#.minus_money),0) as minus_money,ifnull(sum(#alias#.aving_money),0) as aving_money,max(#alias#.ccid) as ccid, max(#alias#.set_month) as set_month, max(#alias#.fromctrlid) as fromctrlid, max(#alias#.rcid) as rcid, max(#alias#.bal_version) as bal_version ";

	@Autowired
	private DaoSupport daoSupport;

	@Autowired
	private PlusSql plusSql = null;

	@Autowired
	private IDataRight dataRight;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#setDaoSupport(gov.gfmis.fap
	 * .gl.core.DaoSupport)
	 */
	public void setDaoSupport(DaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#setPlusSql(gov.gfmis.fap.gl
	 * .core.sqlgen.PlusSql)
	 */
	public void setPlusSql(PlusSql plusSql) {
		this.plusSql = plusSql;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#setDataRight(gov.gfmis.fap.
	 * rule.interfaces.IDataRight)
	 */
	public void setDataRight(IDataRight dataRight) {
		this.dataRight = dataRight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#saveBalanceTracer(java.util
	 * .List)
	 */
	public void saveBalanceTracer(List dtoList) {
		StringBuffer strSql = new StringBuffer();
		strSql.append(
				"insert into gl_balance_trace(vou_id, set_year, rg_code, vou_type_id, ctrlid, ctrl_type, ctrl_side, is_primary, create_date, latest_op_date, last_ver) values(")
				.append("#vou_id#").append(",#set_year#").append(",#rg_code#")
				.append(",#vou_type_id#").append(",#ctrlId#")
				.append(",#ctrl_type#").append(",#ctrl_side#")
				.append(",#is_primary#").append(",#create_date#")
				.append(",#latest_op_date#").append(",#last_ver#").append(")");
		daoSupport.batchExcute(strSql.toString(), dtoList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gfmis.fap.gl.balance.impl.IBalanceDao#deleteBalanceCache()
	 */
	public void deleteBalanceCache() {
		daoSupport.executeUpdate("delete from gl_balance_cache");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#deleteBalanceTrace(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	public void deleteBalanceTrace(String vouId, String vouTypeId,
			String setYear) {
		StringBuffer strsql = new StringBuffer();
		// 将明细对应追溯表的信息删除
		strsql.append("delete from gl_balance_trace where vou_id = #vouId# and set_year = #setYear# and vou_type_id = #vouTypeId#");
		Map map = new HashMap();
		map.put("vouId", vouId);
		map.put("setYear", setYear);
		map.put("vouTypeId", vouTypeId);
		daoSupport.executeUpdate(strsql.toString(), map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#saveBalanceCache(java.util.
	 * List)
	 */
	public void saveBalanceCache(List dtoList) {
		StringBuffer strsql = new StringBuffer();
		// 插入临时额度表
		strsql.append("insert into gl_balance_cache ")
				.append("(sum_id,fromctrlid, set_year, rg_code, account_id, ccid, ")
				.append("set_month, avi_money, use_money, minus_money, aving_money, remark, ")
				.append("bal_version, create_date, latest_op_date, last_ver, balance_id, is_enforce, index_,agency_id,mb_id)")
				.append(" values (").append("#sum_id#")
				.append(",#primarySourceId#").append(",#set_year#")
				.append(",#rg_code#").append(",#account_id#").append(",#ccid#")
				.append(",#set_month#").append(",#avi_money#")
				.append(",#use_money#").append(",#minus_money#")
				.append(",#aving_money#").append(",#remark#")
				.append(",#bal_version#").append(",#create_date#")
				.append(",#latest_op_date#").append(",#last_ver#")
				.append(",#balance_id#")
				.append(",#is_enforce#, #index#,#agency_id#,#mb_id#)");
		if (dtoList != null && dtoList.size() > 0) {
			daoSupport.batchExcute(strsql.toString(), dtoList);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#saveSumMonthBalanceWithCache
	 * (gov.gfmis.fap.gl.configure.BusVouAccount)
	 */
	public void saveSumMonthBalanceWithCache(BusVouAccount account) {
		String accountId = account.getAccountId();
		Integer setMonth = Integer.valueOf(BalanceUtil.getDefaultMonth() + "");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(
				"insert into " + account.getTableName()
						+ " (sum_id, set_year, rg_code, account_id, ccid,")
				.append(" set_month, remark, balance_id,")
				.append(" bal_version, create_date, latest_op_date, last_ver, fromctrlid, rcid,agency_id,mb_id)")
				.append(" (select t.sum_id, max(t.set_year), max(t.rg_code), max(t.account_id), max(t.ccid),")
				.append(" max(t.set_month), max(t.remark), max(t.balance_id),")
				.append(" max(t.bal_version), max(t.create_date), max(t.latest_op_date), max(t.last_ver), max(t.fromctrlid), max(t.rcid),max(t.agency_id),max(t.mb_id)")
				.append(" from gl_balance_cache t where t.set_month <= #setMonth# and account_id = #accountId# and not exists (select 1 from ")
				.append(account.getTableName())
				.append(" b where b.sum_id = t.sum_id) group by t.sum_id)");
		Map map = new HashMap();
		map.put("setMonth", setMonth);
		map.put("accountId", accountId);
		daoSupport.executeUpdate(sbuffer.toString(), map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#saveSumMonthBalanceDetailWithCache
	 * (gov.gfmis.fap.gl.configure.BusVouAccount)
	 */
	public void saveSumMonthBalanceDetailWithCache(BusVouAccount account) {
		String accountId = account.getAccountId();
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(
				"insert into " + account.getMonthDetailTableName()
						+ " (sum_id, set_year, rg_code, account_id, ccid,")
				.append(" set_month, remark, balance_id,")
				.append(" bal_version, create_date, latest_op_date, last_ver, fromctrlid, rcid,agency_id,mb_id)")
				.append(" (select t.sum_id, t.set_year, t.rg_code, t.account_id, t.ccid,")
				.append(" t.set_month, t.remark, t.balance_id,")
				.append(" t.bal_version, t.create_date, t.latest_op_date, t.last_ver, t.fromctrlid, t.rcid,agency_id,mb_id")
				.append(" from gl_balance_cache t")
				.append(" where account_id = #accountId# and not exists (select 1 from "
						+ account.getMonthDetailTableName()
						+ " b where b.balance_id = t.balance_id))");
		Map map = new HashMap();
		map.put("accountId", accountId);
		daoSupport.executeUpdate(sbuffer.toString(), map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#saveCommonBalanceWithCache(
	 * gov.gfmis.fap.gl.configure.BusVouAccount)
	 */
	public void saveCommonBalanceWithCache(BusVouAccount account) {
		String accountId = account.getAccountId();
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append("insert into ")
				.append(account.getTableName())
				.append(" (sum_id, set_year, rg_code, account_id, ccid, ")
				.append(" set_month, remark, balance_id,")
				.append(" bal_version, create_date, latest_op_date, last_ver, fromctrlid, rcid,mb_id,agency_id)")
				.append(" (select t.sum_id, t.set_year, t.rg_code, t.account_id, t.ccid, ")
				.append(" t.set_month, t.remark, t.balance_id,")
				.append(" t.bal_version, t.create_date, t.latest_op_date, t.last_ver, t.fromctrlid, t.rcid,t.mb_id,t.agency_id")
				.append(" from gl_balance_cache t")
				.append(" where account_id='").append(accountId).append("'")
				.append(" and not exists (select 1 from ")
				.append(account.getTableName())
				.append(" b where b.sum_id = t.sum_id))");
		daoSupport.execute(sbuffer.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#deleteBalance(gov.gfmis.fap
	 * .gl.configure.BusVouAccount, gov.gfmis.fap.gl.dao.Condition)
	 */
	public void deleteBalance(BusVouAccount account, Condition condition) {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("delete from ").append(account.getTableName())
				.append(" b").append(" where b.account_id='")
				.append(account.getAccountId()).append("' and")
				.append(plusSql.getConditionSql(account, condition));
		daoSupport.executeUpdate(strSQL.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#deleteBalanceTracer(java.util
	 * .List)
	 */
	public void deleteBalanceTracer(List dtoList) {
		StringBuffer strsql = new StringBuffer();
		strsql.append("delete gl_balance_trace where vou_id=#vou_id# and vou_type_id=#vou_type_id#");
		daoSupport.batchExcute(strsql.toString(), dtoList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gfmis.fap.gl.balance.impl.IBalanceDao#execute(java.lang.String)
	 */
	public void execute(String sql) {
		daoSupport.executeUpdate(sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gfmis.fap.gl.balance.impl.IBalanceDao#loadTracerByJournalCache()
	 */
	public List loadTracerByJournalCache() {
		StringBuffer strsql = new StringBuffer();
		strsql.append("select j.index_, t.* from gl_balance_trace t, gl_journal_cache j where j.vou_id=t.vou_id and j.vou_type_id=t.vou_type_id and j.set_year=t.set_year order by j.index_");
		return daoSupport.genericQuery(strsql.toString(), BalanceTracer.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#findBalanceIsExist(gov.gfmis
	 * .fap.gl.configure.BusVouAccount, boolean)
	 */
	public List findBalanceIsExist(BusVouAccount account, boolean isExists) {
		StringBuffer strSql = new StringBuffer();
		strSql.append("select * from gl_balance_cache c  where  c.account_id='")
				.append(account.getAccountId())
				.append("' and c.use_money>0 ")
				.append(" and ")
				.append(isExists ? "exists" : "not exists")
				.append(" (select 1 from ")
				.append(account.getTableName())
				.append(" b where b.account_id='")
				.append(account.getAccountId())
				.append("' and (")
				.append(getBalanceRelationSql("b", "c"))
				.append("))")
				.append(" or ")
				.append(isExists ? "exists" : "not exists")
				.append(" (select 1 from gl_balance_cache x where x.account_id='")
				.append(account.getAccountId()).append("' and (")
				.append(this.getBalanceRelationSql("x", "c")).append("))");
		return daoSupport.genericQuery(strSql.toString(), HashMap.class);
	}

	private String getBalanceRelationSql(String cacheAlias, String balanceAlias) {
		StringBuffer sb = new StringBuffer();
		sb.append(cacheAlias).append(".account_id=").append(balanceAlias)
				.append(".account_id and ").append(cacheAlias).append(".ccid=")
				.append(balanceAlias).append(".ccid and ").append(cacheAlias)
				.append(".set_month=").append(balanceAlias)
				.append(".set_month and ").append(cacheAlias)
				.append(".set_year=").append(balanceAlias)
				.append(".set_year and ").append(cacheAlias)
				.append(".rg_code=").append(balanceAlias).append(".rg_code ");
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#loadMarkingMonthBalance(gov
	 * .gfmis.fap.gl.configure.BusVouAccount)
	 */
	// 将计算更新成先更新gl_balance再查询是否超支 gl_balance_cache表按0金额处理
	public List loadMarkingMonthBalance(BusVouAccount account) {

		List findList = null;
		StringBuffer strSql = new StringBuffer();
		if(TypeOfDB.isOracle()) {
			strSql.append(
					"select b.balance_id, b.sum_id, b.ccid, nvl(b.avi_money,0)-c.avi_money avi_money, nvl(b.use_money,0)-c.use_money use_money, nvl(b.minus_money,0)-c.minus_money minus_money, nvl(b.aving_money,0)-c.aving_money aving_money, b.set_year, b.rg_code, b.account_id ")
					.append(" from gl_balance_cache c left join (select balance_id, sum_id, account_id, ccid, avi_money, use_money, minus_money, aving_money, set_year, rg_code, set_month from ")
					.append(account.getTableName())
					.append(" d where d.account_id='" + account.getAccountId()
							+ "') b on (")
							.append(this.getBalanceRelationSql("b", "c"))
							.append(") ")
							.append(" where c.account_id='"
									+ account.getAccountId()
									+ "' and (nvl(b.avi_money,0) - nvl(b.use_money,0) - nvl(b.minus_money,0) < 0 or (nvl(b.avi_money,0) ) < 0 or (nvl(b.use_money,0)) < 0 or (nvl(b.minus_money,0)) < 0 or (nvl(b.aving_money,0)) < 0) and c.is_enforce <> 1");
		} else if(TypeOfDB.isMySQL()) {
			strSql.append(
					"select b.balance_id, b.sum_id, b.ccid, ifnull(b.avi_money,0)-c.avi_money avi_money, ifnull(b.use_money,0)-c.use_money use_money, ifnull(b.minus_money,0)-c.minus_money minus_money, ifnull(b.aving_money,0)-c.aving_money aving_money, b.set_year, b.rg_code, b.account_id ")
					.append(" from gl_balance_cache c left join (select balance_id, sum_id, account_id, ccid, avi_money, use_money, minus_money, aving_money, set_year, rg_code, set_month from ")
					.append(account.getTableName())
					.append(" d where d.account_id='" + account.getAccountId()
							+ "') b on (")
							.append(this.getBalanceRelationSql("b", "c"))
							.append(") ")
							.append(" where c.account_id='"
									+ account.getAccountId()
									+ "' and (ifnull(b.avi_money,0) - ifnull(b.use_money,0) - ifnull(b.minus_money,0) < 0 or (ifnull(b.avi_money,0) ) < 0 or (ifnull(b.use_money,0)) < 0 or (ifnull(b.minus_money,0)) < 0 or (ifnull(b.aving_money,0)) < 0) and c.is_enforce <> 1");
		} else {
			return findList;
		}
		findList = daoSupport.genericQuery(strSql.toString(),
				CtrlRecordDTO.class);
		return findList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#loadCalculateBeforeMonthBalance
	 * (gov.gfmis.fap.gl.configure.BusVouAccount)
	 */
	// hult 20110712将计算更新成先更新gl_balance再查询是否超支
	public List loadCalculateBeforeMonthBalance(BusVouAccount account) {
		StringBuffer strSql = new StringBuffer();
		String currentMonth = String.valueOf(BalanceUtil.getDefaultMonth());
		List findList = null;
		if(TypeOfDB.isOracle()) {
			strSql.append(
					"select b.sum_id, b.ccid, nvl(b.avi_money, 0)- c.avi_money avi_money, nvl(b.use_money, 0) - c.use_money use_money,")
					.append(" nvl(b.minus_money, 0) - c.minus_money minus_money, nvl(b.aving_money, 0) - c.aving_money aving_money, b.set_year, b.rg_code, b.account_id")
					.append(" from (select sum_id, ccid, avi_money, use_money, minus_money, aving_money as aving_money, set_year, rg_code, account_id from ")
					.append(account.getTableName())
					.append(" where account_id = #accountId#) b")
					.append(" right join (select sum_id, sum(avi_money) as avi_money, sum(use_money) as use_money, sum(minus_money) as minus_money, sum(aving_money) as aving_money from gl_balance_cache")
					.append(" where account_id = #accountId# and set_month <= #currentMonth# group by sum_id) c")
					.append(" on b.sum_id = c.sum_id where nvl(b.avi_money, 0) - nvl(b.use_money, 0) - nvl(b.minus_money, 0) < 0");
		} else if(TypeOfDB.isMySQL()) {
			strSql.append(
					"select b.sum_id, b.ccid, ifnull(b.avi_money, 0)- c.avi_money avi_money, ifnull(b.use_money, 0) - c.use_money use_money,")
					.append(" ifnull(b.minus_money, 0) - c.minus_money minus_money, ifnull(b.aving_money, 0) - c.aving_money aving_money, b.set_year, b.rg_code, b.account_id")
					.append(" from (select sum_id, ccid, avi_money, use_money, minus_money, aving_money as aving_money, set_year, rg_code, account_id from ")
					.append(account.getTableName())
					.append(" where account_id = #accountId#) b")
					.append(" right join (select sum_id, sum(avi_money) as avi_money, sum(use_money) as use_money, sum(minus_money) as minus_money, sum(aving_money) as aving_money from gl_balance_cache")
					.append(" where account_id = #accountId# and set_month <= #currentMonth# group by sum_id) c")
					.append(" on b.sum_id = c.sum_id where ifnull(b.avi_money, 0) - ifnull(b.use_money, 0) - ifnull(b.minus_money, 0) < 0");
		} else {
			return findList;
		}
		Map map = new HashMap();
		map.put("accountId", account.getAccountId());
		map.put("accountId", account.getAccountId());
		map.put("currentMonth", currentMonth);
		findList = daoSupport.genericQuery(strSql.toString(), map,
				CtrlRecordDTO.class);
		return findList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#loadCalculateAfterMonthBalance
	 * (gov.gfmis.fap.gl.configure.BusVouAccount)
	 */
	public List loadCalculateAfterMonthBalance(BusVouAccount account) {
		StringBuffer strSql = new StringBuffer();
		String currentMonth = String.valueOf(BalanceUtil.getDefaultMonth());
		List findList = null;
		if(TypeOfDB.isOracle()) {
			strSql.append(
					"select b.sum_id, b.ccid, nvl(b.avi_money, 0) - c.avi_money avi_money, nvl(b.use_money, 0) - c.use_money use_money,")
					.append(" nvl(b.minus_money, 0) - c.minus_money minus_money, nvl(b.aving_money, 0) - c.aving_money aving_money, b.set_year, b.rg_code, b.account_id")
					.append(" from (select sum_id, balance_id, ccid, avi_money, use_money, minus_money, aving_money, set_year, rg_code, account_id from ")
					.append(account.getMonthDetailTableName())
					.append(" where account_id = #accountId# and set_month > #currentMonth#) b")
					.append(" right join (select sum_id, balance_id, avi_money, use_money, minus_money, aving_money from gl_balance_cache")
					.append(" where account_id = #accountId# and set_month > #currentMonth#) c")
					.append(" on b.balance_id = c.balance_id where nvl(b.avi_money, 0) - nvl(b.use_money, 0) - nvl(b.minus_money, 0) < 0");
		} else if(TypeOfDB.isMySQL()) {
			strSql.append(
					"select b.sum_id, b.ccid, ifnull(b.avi_money, 0) - c.avi_money avi_money, ifnull(b.use_money, 0) - c.use_money use_money,")
					.append(" ifnull(b.minus_money, 0) - c.minus_money minus_money, ifnull(b.aving_money, 0) - c.aving_money aving_money, b.set_year, b.rg_code, b.account_id")
					.append(" from (select sum_id, balance_id, ccid, avi_money, use_money, minus_money, aving_money, set_year, rg_code, account_id from ")
					.append(account.getMonthDetailTableName())
					.append(" where account_id = #accountId# and set_month > #currentMonth#) b")
					.append(" right join (select sum_id, balance_id, avi_money, use_money, minus_money, aving_money from gl_balance_cache")
					.append(" where account_id = #accountId# and set_month > #currentMonth#) c")
					.append(" on b.balance_id = c.balance_id where ifnull(b.avi_money, 0) - ifnull(b.use_money, 0) - ifnull(b.minus_money, 0) < 0");
		} else {
			return findList;
		}
		Map map = new HashMap();
		map.put("accountId", account.getAccountId());
		map.put("currentMonth", currentMonth);
		map.put("accountId", account.getAccountId());
		map.put("currentMonth", currentMonth);
		findList = daoSupport.genericQuery(strSql.toString(), map,
				CtrlRecordDTO.class);
		return findList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#loadVouIdByBalance(java.util
	 * .List)
	 */
	public List loadVouIdByBalance(List dtoList) {
		StringBuffer strSql = new StringBuffer();
		strSql.append("select vou_id from gl_balance_trace where ctrlid="
				+ ((Map) dtoList.get(0)).get("sum_id"));
		return daoSupport.genericQuery(strSql.toString(), HashMap.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#findBalance(gov.gfmis.fap.gl
	 * .configure.BusVouAccount, gov.gfmis.fap.util.FPaginationDTO,
	 * gov.gfmis.fap.gl.dao.Condition)
	 */
	public List findBalance(BusVouAccount account, FPaginationDTO page,
			Condition condition) {
		int setYear = Integer.parseInt(CommonUtil.getSetYear());
		String rgCode = SessionUtil.getRgCode();
		String userid = CommonUtil.getUserId();
		String roleid = CommonUtil.getRoleId();
		String balanceField = null;
		String tableName = null;
		FCoaDTO coaDto = account.getCoaDto();

		String innerPlusSql = plusSql.getConditionSql(account, condition);

		StringBuffer balanceQuerySql = new StringBuffer();
		String groupBy = StringUtil.EMPTY;
		if (condition.isBalanceQueryByDetailtable()) {
			balanceField = getAliasBalanceDetailField("b");
			tableName = account.getMonthDetailTableName();
			groupBy = "group by sum_id";
			// 开始拼SQL语句
			balanceQuerySql.append("select ");
			balanceQuerySql.append(balanceField);
			balanceQuerySql
					.append(" from ")
					.append(tableName + " b")
					.append(" where b.set_year=" + setYear + " and b.rg_code='"
							+ rgCode + "' and b.account_id='"
							+ account.getAccountId() + "' ");

			// 添加上额度版本,先特殊处理
			if (innerPlusSql == null
					|| innerPlusSql.indexOf("bal_version") == -1) {
				balanceQuerySql.append("and b.bal_version<>-1 ");
			}
			balanceQuerySql.append(innerPlusSql);
			balanceQuerySql.append(groupBy);

			// 拼外部SQL与CCID表关联
			balanceQuerySql
					.insert(0,
							"select * from (select bal.sum_id,"
									+ setYear
									+ " as set_year,bal.canuse_money,bal.avi_money,bal.use_money,bal.minus_money,bal.aving_money,bal.set_month,bal.rcid,bal.fromctrlid,bal.bal_version, "
									+ plusSql.generateCcidSelectFields(coaDto,
											"c") + " from (");
			balanceQuerySql.append(") bal, gl_ccids c where bal.ccid=c.ccid ");
			// 拼要素SQL
			balanceQuerySql.append(plusSql.getEleConditionSql(account,
					condition));
			balanceQuerySql.append(") b where 1=1 ");
		} else {
			balanceField = getAliasBalanceField("b").replaceAll(
					"b.ccid as ccid,", ""); // 去掉CCID字段，否则SQL出错上次查询到的CCID列重复
			tableName = account.getTableName();
			// 开始拼SQL语句
			balanceQuerySql.append("select ");
			balanceQuerySql.append(balanceField);
			balanceQuerySql.append(","
					+ plusSql.generateCcidSelectFields(coaDto, "c"));
			balanceQuerySql
					.append(" from ")
					.append(tableName + " b,gl_ccids c")
					.append(" where b.set_year=" + setYear + " and b.rg_code='"
							+ rgCode + "' and b.account_id='"
							+ account.getAccountId() + "' ");
			if (innerPlusSql == null
					|| innerPlusSql.indexOf("bal_version") == -1) {
				balanceQuerySql.append("and b.bal_version<>-1 ");
			}
			balanceQuerySql.append(" and b.ccid=c.ccid ");
			balanceQuerySql.append(plusSql.getEleConditionSql(account,
					condition));
			balanceQuerySql.append(innerPlusSql);
		}
		String sum_type_code = account.getAccountCode();
		if ((condition == null || condition.isAllowRight())
				&& ("651".equals(sum_type_code) || "635".equals(sum_type_code) || "631"
						.equals(sum_type_code))) {
			// 拼权限SQL
			List enList = daoSupport.findUserEnOrgByUserId(userid, rgCode,
					setYear);
			if (enList != null && enList.size() > 0)// 如果是单位权限并存在单位列表
			{
				try {
					balanceQuerySql.append("and (b.agency_id ='#' or (");
					String inSql = Tools
							.getInSql("b", "agency_id", enList, "1");
					balanceQuerySql.append(inSql);
					balanceQuerySql.append(")) ");
					balanceQuerySql.append(dataRight
							.getSqlBusiRightByUserAndRole(userid, roleid, "b"));
				} catch (Exception e) {
					throw new RuntimeException("额度查询失败,原因:权限组件异常", e);
				}
				balanceQuerySql.append(plusSql.getOrderStatement(condition));

				boolean queryWithMaxCount = condition == null ? true
						: condition.isQueryWithMaxCount();
				// System.out.println("额度查询SQL："+balanceQuerySql.toString());
				return daoSupport.genericQueryByPage(
						balanceQuerySql.toString(), enList.toArray(),
						FCtrlRecordDTO.class, page, queryWithMaxCount);
			}
		}
		try {
			if (condition == null || condition.isAllowRight()) {
				balanceQuerySql.append(dataRight.getSqlBusiRight(userid,
						roleid, "c"));
			}
		} catch (Exception e) {
			throw new RuntimeException("额度查询失败,原因:权限组件异常", e);
		}
		balanceQuerySql.append(plusSql.getOrderStatement(condition));

		boolean queryWithMaxCount = condition == null ? true : condition
				.isQueryWithMaxCount();
		// System.out.println("额度查询SQL："+balanceQuerySql.toString());
		return daoSupport.genericQueryByPage(balanceQuerySql.toString(),
				FCtrlRecordDTO.class, page, queryWithMaxCount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#loadBalance(gov.gfmis.fap.gl
	 * .configure.BusVouAccount, gov.gfmis.fap.gl.dao.Condition)
	 */
	public FCtrlRecordDTO loadBalance(BusVouAccount account, Condition condition) {
		List list = findBalance(account, null, condition);
		if (list.size() == 1)
			return (FCtrlRecordDTO) list.get(0);
		else if (list.size() > 1)
			throw new RuntimeException("单条额度查询返回大于一条记录");
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#findBalance(gov.gfmis.fap.gl
	 * .configure.BusVouAccount, gov.gfmis.fap.gl.dao.Condition)
	 */
	public List findBalance(BusVouAccount account, Condition condition) {
		int set_year = Integer.parseInt(CommonUtil.getSetYear());

		StringBuffer strSQL = new StringBuffer();
		String selectedField = null;
		String tableName = account.getMonthDetailTableName();

		if (tableName == null || "".equals(tableName)) {
			throw new RuntimeException("相应控制类型未正确设置,无法获取控制表信息!");
		}

		if(TypeOfDB.isOracle()) {
			selectedField = NOT_SUM_FOR_MONTH_SELECT_FIELD; // select fields
		} else if(TypeOfDB.isMySQL()) {
			selectedField = NOT_SUM_FOR_MONTH_SELECT_FIELD_MYSQL; // select fields
		} else {
			return null;
		}
		
		strSQL.append("select ").append("b.*, c.*").append(" from ")
		.append("(select ").append(selectedField).append(" from ")
		.append(tableName).append(" where ").append(" set_year=")
		.append(set_year).append(" and account_id = '")
		.append(account.getAccountId()).append("'");
		strSQL.append(plusSql.getConditionSql(account, condition))
		.append(")  b, gl_ccids c where 1=1 ")
		.append(plusSql.getEleConditionSql(account, condition))
		.append(" and b.ccid=c.ccid");
		
		return daoSupport.genericQuery(strSQL.toString(), FCtrlRecordDTO.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#findBalanceByJournal(gov.gfmis
	 * .fap.gl.configure.BusVouType, gov.gfmis.fap.gl.configure.BusVouAccount,
	 * java.lang.String, int)
	 */
	public List findBalanceByJournal(BusVouType bvType, BusVouAccount account,
			String vouId, int side) {
		Condition cond = new ConditionObj();
		StringBuffer sb = new StringBuffer();
		sb.append("and exists(").append(
				"select 1 from gl_balance_tracer t where t.vou_type_id="
						+ bvType.getVou_type_id() + " and t.vou_id=" + vouId
						+ " and ctrl_side=" + side + " b.sum_id=t.ctrlid");

		cond.addSqlCondition(Condition.INNER_SQL_FLAG, sb.toString());
		return findBalance(account, null, cond);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#updateSumMonthBalanceMoneyWithCache
	 * (gov.gfmis.fap.gl.configure.BusVouAccount)
	 */
	public int updateSumMonthBalanceMoneyWithCache(BusVouAccount account) {
		String accountId = account.getAccountId();
		Integer currentMonth = Integer.valueOf(BalanceUtil.getDefaultMonth()
				+ "");
		StringBuffer sbuffer = new StringBuffer();
		if(TypeOfDB.isOracle()) {
			sbuffer.append("update ")
			.append(account.getTableName())
			.append(" t set (t.avi_money, t.use_money, t.minus_money, t.aving_money, latest_op_date)")
			.append(" = (select t.avi_money + sum(s.avi_money),")
			.append(" t.use_money + sum(s.use_money), t.minus_money + sum(s.minus_money),")
			.append(" t.aving_money + sum(s.aving_money), #latest_op_date#")
			.append(" from gl_balance_cache s where s.sum_id = t.sum_id and s.set_month <= #currentMonth#) where t.account_id = #accountId#")
			.append(" and exists (select 1 from gl_balance_cache z where t.sum_id = z.sum_id and z.set_month <= #currentMonth#)");
		} else if(TypeOfDB.isMySQL()) {
			sbuffer.append("update " + account.getTableName() + " t ")
			.append(" inner join gl_balance_cache s on s.sum_id = t.sum_id and s.set_month <= #currentMonth#")
			.append(" set (t.avi_money, t.use_money, t.minus_money, t.aving_money, latest_op_date)")
			.append(" = (select t.avi_money + sum(s.avi_money),")
			.append(" t.use_money + sum(s.use_money), t.minus_money + sum(s.minus_money),")
			.append(" t.aving_money + sum(s.aving_money), #latest_op_date#")
			.append(" ) where t.account_id = #accountId#")
			.append(" and exists (select 1 from gl_balance_cache z where t.sum_id = z.sum_id and z.set_month <= #currentMonth#)");
		}
		
		Map map = new HashMap();
		map.put("latest_op_date", DateHandler.getDateFromNow(0));
		map.put("currentMonth", currentMonth);
		map.put("accountId", accountId);
		map.put("currentMonth", currentMonth);
		return daoSupport.executeUpdate(sbuffer.toString(), map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gfmis.fap.gl.balance.impl.IBalanceDao#
	 * updateSumMonthBalanceDetailMoneyWithCache
	 * (gov.gfmis.fap.gl.configure.BusVouAccount)
	 */
	public int updateSumMonthBalanceDetailMoneyWithCache(BusVouAccount account) {
		String accountId = account.getAccountId();
		StringBuffer sbuffer = new StringBuffer();
		if(TypeOfDB.isOracle()) {
			sbuffer.append(
					"update "
							+ account.getMonthDetailTableName()
							+ " t set (t.avi_money, t.use_money, t.minus_money, t.aving_money, latest_op_date)")
					.append(" = ( select t.avi_money + sum(s.avi_money),")
					.append(" t.use_money + sum(s.use_money), t.minus_money + sum(s.minus_money),")
					.append(" t.aving_money + sum(s.aving_money), #latest_op_date#")
					.append(" from gl_balance_cache s where t.balance_id = s.balance_id)")
					.append(" where t.account_id = #accountId#")
					.append(" and exists (select 1 from gl_balance_cache c where t.balance_id = c.balance_id)");
		} else if(TypeOfDB.isMySQL()) {
			sbuffer.append("update " + account.getMonthDetailTableName() + " t ")
					.append(" inner join gl_balance_cache s on t.balance_id = s.balance_id ")
					.append(" set (t.avi_money, t.use_money, t.minus_money, t.aving_money, latest_op_date)")
					.append(" = ( select t.avi_money + sum(s.avi_money),")
					.append(" t.use_money + sum(s.use_money), t.minus_money + sum(s.minus_money),")
					.append(" t.aving_money + sum(s.aving_money), #latest_op_date#)")
					.append(" where t.account_id = #accountId#")
					.append(" and exists (select 1 from gl_balance_cache c where t.balance_id = c.balance_id)");
		}
		Map map = new HashMap();
		map.put("latest_op_date", DateHandler.getDateFromNow(0));
		map.put("accountId", accountId);
		return daoSupport.executeUpdate(sbuffer.toString(), map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#updateFromctrlidWithCache(gov
	 * .gfmis.fap.gl.configure.BusVouAccount)
	 */
	public int updateFromctrlidWithCache(BusVouAccount account) {
		String accountId = account.getAccountId();
		StringBuffer sbuffer = new StringBuffer();
		if(TypeOfDB.isOracle()) {
			sbuffer.append(
					"update " + account.getTableName()
					+ " t set ( latest_op_date, fromctrlid) ")
					.append("=( select '")
					.append(DateHandler.getDateFromNow(0))
					.append("', s.fromctrlid")
					.append(" from gl_balance_cache s where s.balance_id=t.balance_id)")
					.append(" where t.account_id='")
					.append(accountId)
					.append("'")
					.append(" and exists (select 1 from gl_balance_cache z where t.balance_id=z.balance_id and z.fromctrlid is not null)");
		} else if(TypeOfDB.isMySQL()) {
			sbuffer.append("update " + account.getTableName() + " t ")
					.append("inner join gl_balance_cache s on s.balance_id=t.balance_id")
					.append(" set ( latest_op_date, fromctrlid) ")
					.append(" =( select '")
					.append(DateHandler.getDateFromNow(0))
					.append("', s.fromctrlid)")
					.append(" where t.account_id='")
					.append(accountId)
					.append("'")
					.append(" and exists (select 1 from gl_balance_cache z where t.balance_id=z.balance_id and z.fromctrlid is not null)");
		}
		return daoSupport.executeUpdate(sbuffer.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#updateCommonBalanceMoneyWithCache
	 * (gov.gfmis.fap.gl.configure.BusVouAccount)
	 */
	public int updateCommonBalanceMoneyWithCache(BusVouAccount account) {
		String accountId = account.getAccountId();
		StringBuffer sbuffer = new StringBuffer();
		if(TypeOfDB.isOracle()) {
			sbuffer.append(
					"update "
					+ account.getTableName()
					+ " t set (t.avi_money,t.use_money,t.minus_money,t.aving_money, latest_op_date) ")
					.append("=( select t.avi_money+sum(s.avi_money),")
					.append("t.use_money+sum(s.use_money),t.minus_money+sum(s.minus_money),")
					.append("t.aving_money+sum(s.aving_money),#date# from gl_balance_cache s where s.sum_id=t.sum_id)")
					.append(" where t.account_id='")
					.append(accountId)
					.append("'")
					.append(" and exists (select 1 from gl_balance_cache z where t.sum_id=z.sum_id)");
		} else if(TypeOfDB.isMySQL()) {
			sbuffer.append("update " + account.getTableName() + " t ")
					.append(" inner join gl_balance_cache s on s.sum_id=t.sum_id ")
					.append(" set (t.avi_money,t.use_money,t.minus_money,t.aving_money, latest_op_date) ")
					.append("=( select t.avi_money+sum(s.avi_money),")
					.append(" t.use_money+sum(s.use_money),t.minus_money+sum(s.minus_money),")
					.append(" t.aving_money+sum(s.aving_money),#date# )")
					.append(" where t.account_id='")
					.append(accountId)
					.append("'")
					.append(" and exists (select 1 from gl_balance_cache z where t.sum_id=z.sum_id)");
		}
		HashMap date = new HashMap();
		date.put("date", DateHandler.getDateFromNow(0));
		return daoSupport.executeUpdate(sbuffer.toString(), date);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gfmis.fap.gl.balance.impl.IBalanceDao#loadMonthlyBalance(int)
	 */
	public List loadMonthlyBalance(int year) {
		String sql = "select * from gl_monthly_balance where set_year=" + year;
		return daoSupport.genericQuery(sql, MonthlyBalanceDTO.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gfmis.fap.gl.balance.impl.IBalanceDao#loadMonthlyBalanceDTO(int,
	 * int)
	 */
	public MonthlyBalanceDTO loadMonthlyBalanceDTO(int year, int month) {
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append("select * from gl_monthly_balance")
				.append(" where set_year=").append(year)
				.append(" and set_month=").append(month);

		List list = daoSupport.genericQuery(sbuffer.toString(),
				MonthlyBalanceDTO.class);
		if (list == null || list.size() == 0)
			return null;
		else
			return (MonthlyBalanceDTO) list.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#saveMonthlyBalanceRecord(int,
	 * int)
	 */
	public int saveMonthlyBalanceRecord(int year, int month) {
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append("insert into gl_monthly_balance g values(").append(year)
				.append(",").append(month).append(",");
		if(TypeOfDB.isOracle()) {
			sbuffer.append("to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')) ");
		} else if (TypeOfDB.isMySQL()) {
			sbuffer.append("sysdate() ) ");
		}
		return daoSupport.executeUpdate(sbuffer.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#getNeededMonthsWhenDoMonthlyBalance
	 * (int, int)
	 */
	public List getNeededMonthsWhenDoMonthlyBalance(int year, int month) {
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append("select * from gl_monthly_balance")
				.append(" where set_year=").append(year)
				.append(" and set_month<=").append(month);
		List existMonthList = daoSupport.genericQuery(sbuffer.toString(),
				MonthlyBalanceDTO.class);

		List notExistMonthList = new ArrayList();
		// 1月不存在月结
		for (int i = 2; i <= month; i++) {
			boolean isExist = false;
			for (Iterator iter = existMonthList.iterator(); iter.hasNext();) {
				MonthlyBalanceDTO dto = (MonthlyBalanceDTO) iter.next();
				String existMonth = dto.getSet_month();
				if (Integer.parseInt(existMonth) == i) {
					isExist = true;
					break;
				}
			}

			if (!isExist)
				notExistMonthList.add(String.valueOf(i));
		}

		return notExistMonthList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#updateBalanceByDetail(gov.gfmis
	 * .fap.gl.configure.BusVouAccount, int, int)
	 */
	public void updateBalanceByDetail(BusVouAccount account, int year, int month) {
		// 更新额度表相应额度的金额
		StringBuffer sbuffer = new StringBuffer();
		if(TypeOfDB.isOracle()) {
			sbuffer.append(
					"update "
							+ account.getTableName()
							+ " b set (b.avi_money,b.use_money,b.minus_money,b.aving_money)")
					.append(" =(select nvl(sum(m.avi_money),0)")
					.append(",nvl(sum(m.use_money),0)")
					.append(",nvl(sum(m.minus_money),0)")
					.append(",nvl(sum(m.aving_money),0)")
					.append(" from " + account.getMonthDetailTableName() + " m ")
					.append(" where b.sum_id=m.sum_id ")
					.append(" and b.set_year=m.set_year")
					.append(" and m.set_year=")
					.append(year)
					.append(" and m.account_id='")
					.append(account.getAccountId())
					.append("'")
					.append(" and m.set_month <= ")
					.append(month)
					.append(")")
					.append(" where b.account_id='")
					.append(account.getAccountId())
					.append("' and exists (select 1 from "
							+ account.getMonthDetailTableName() + " d")
					.append(" where d.sum_id=b.sum_id and d.set_month <= ")
					.append(month).append(")");
			
		} else if(TypeOfDB.isMySQL()) {
			sbuffer.append("update " + account.getTableName() + " b ")
					.append("inner join " + account.getMonthDetailTableName() + " m ")
					.append(" on b.sum_id=m.sum_id ")
					.append(" and b.set_year=m.set_year")
					.append(" and m.set_year=" + year)
					.append(" and m.account_id='" + account.getAccountId() + "'")
					.append(" and m.set_month <= " + month)
					.append(" set (b.avi_money,b.use_money,b.minus_money,b.aving_money)")
					.append(" =(select nvl(sum(m.avi_money),0)")
					.append(",ifnull(sum(m.use_money),0)")
					.append(",ifnull(sum(m.minus_money),0)")
					.append(",ifnull(sum(m.aving_money),0)")
					.append(")")
					.append(" where b.account_id='")
					.append(account.getAccountId())
					.append("' and exists (select 1 from "
							+ account.getMonthDetailTableName() + " d")
					.append(" where d.sum_id=b.sum_id and d.set_month <= ")
					.append(month).append(")");
		}
		daoSupport.executeUpdate(sbuffer.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#clearBalanceMoney(gov.gfmis
	 * .fap.gl.configure.BusVouAccount)
	 */
	public void clearBalanceMoney(BusVouAccount account) {
		daoSupport
				.executeUpdate(
						"update "
								+ account.getTableName()
								+ " set avi_money=0, use_money=0, minus_money=0, aving_money=0 where account_id=#accountId#",
						account);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#insertBalanceNotExists(gov.
	 * gfmis.fap.gl.configure.BusVouAccount, int)
	 */
	public void insertBalanceNotExists(BusVouAccount account, int maxMonth) {
		// 插入额度表中没有的额度
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append("insert into ")
				.append(account.getTableName())
				.append("(account_id, sum_id, create_date, latest_op_date, set_year, set_month, avi_money, use_money, minus_money, aving_money, last_ver, fromctrlid, bal_version, rcid, ccid, rg_code, balance_id) (")
				.append("select ")
				.append(" '")
				.append(account.getAccountId())
				.append("', sum_id, min(create_date), max(latest_op_date), max(set_year), ")
				.append(maxMonth)
				.append(", 0, 0, 0, 0, max(last_ver), max(fromctrlid), max(bal_version), max(rcid), max(ccid), max(rg_code), max(balance_id)")
				.append(" from " + account.getMonthDetailTableName()
						+ " d where d.account_id='")
				.append(account.getAccountId())
				.append("' and d.set_month <=")
				.append(maxMonth)
				.append(" and not exists")
				.append(" (select 1 from " + account.getTableName()
						+ " b where b.account_id='")
				.append(account.getAccountId())
				.append("' and b.sum_id=d.sum_id) group by sum_id").append(")");
		daoSupport.execute(sbuffer.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#updateBalanceMonth(gov.gfmis
	 * .fap.gl.configure.BusVouAccount, int)
	 */
	public void updateBalanceMonth(BusVouAccount account, int month) {
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append("update ").append(account.getTableName())
				.append(" set set_month=").append(month)
				.append(" where account_id='").append(account.getAccountId())
				.append("'");
		daoSupport.execute(sbuffer.toString());
	}

	private String getAliasBalanceField(String alias) {
		String result = null;
		if(TypeOfDB.isOracle()) {
			result = ALIAS_SELECT_FIELD.replaceAll("#alias#", alias);
		} else if(TypeOfDB.isMySQL()) {
			result = ALIAS_SELECT_FIELD_MYSQL.replaceAll("#alias#", alias);
		}
		return result;
	}

	private String getAliasBalanceDetailField(String alias) {
		String result = null;
		if(TypeOfDB.isOracle()) {
			result = ALIAS_SELECT_FIELD_FOR_DETAIL.replaceAll("#alias#", alias);
		} else if(TypeOfDB.isMySQL()) {
			result = ALIAS_SELECT_FIELD_FOR_DETAIL_MYSQL.replaceAll("#alias#", alias);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#fillCachedBalanceKey(gov.gfmis
	 * .fap.gl.configure.BusVouAccount)
	 */
	public void fillCachedBalanceKey(BusVouAccount account) {
		if(TypeOfDB.isOracle()) {
			StringBuffer strSql = new StringBuffer();
			strSql.append("update gl_balance_cache c set sum_id = (select distinct b.sum_id from "
					+ account.getMonthDetailTableName()
					+ " b where b.account_id = #accountId# and (c.account_id = b.account_id and c.ccid = b.ccid and c.set_year = b.set_year and c.rg_code = b.rg_code)) where c.account_id = #accountId# and c.sum_id is null");
			Map map = new HashMap();
			map.put("accountId", account.getAccountId());
			daoSupport.executeUpdate(strSql.toString(), map);
			strSql = new StringBuffer();
			strSql.append("update gl_balance_cache c set sum_id = SEQ_CCID_ORDER.Nextval where account_id = #accountId# and sum_id is null");
			map = new HashMap();
			map.put("accountId", account.getAccountId());
			daoSupport.executeUpdate(strSql.toString(), map);
			strSql = new StringBuffer();
			strSql.append("update gl_balance_cache c set sum_id = (select max(b.sum_id) sum_id from gl_balance_cache b where b.account_id = #accountId# and (c.account_id = b.account_id and c.ccid = b.ccid and c.set_year = b.set_year and c.rg_code = b.rg_code) group by account_id, set_year, ccid, rg_code) where c.account_id = #accountId#");
			map = new HashMap();
			map.put("accountId", account.getAccountId());
			daoSupport.executeUpdate(strSql.toString(), map);
			strSql = new StringBuffer();
			strSql.append("update gl_balance_cache c set balance_id = sum_id || '000' || set_month where c.account_id = #accountId#");
			map = new HashMap();
			map.put("accountId", account.getAccountId());
			daoSupport.executeUpdate(strSql.toString(), map);	
		} else if(TypeOfDB.isMySQL()) {
			StringBuffer strSql = new StringBuffer();
			strSql.append("update gl_balance_cache c set sum_id = (select distinct b.sum_id from "
					+ account.getMonthDetailTableName()
					+ " b where b.account_id = #accountId# and (c.account_id = b.account_id and c.ccid = b.ccid and c.set_year = b.set_year and c.rg_code = b.rg_code)) where c.account_id = #accountId# and isnull(c.sum_id)");
			Map map = new HashMap();
			map.put("accountId", account.getAccountId());
			daoSupport.executeUpdate(strSql.toString(), map);
			strSql = new StringBuffer();
			strSql.append("update gl_balance_cache c set sum_id = SEQ_CCID_ORDER.Nextval where account_id = #accountId# and isnull(sum_id)");
			map = new HashMap();
			map.put("accountId", account.getAccountId());
			daoSupport.executeUpdate(strSql.toString(), map);
			strSql = new StringBuffer();
			strSql.append("update gl_balance_cache c set sum_id = (select max(b.sum_id) sum_id from gl_balance_cache b where b.account_id = #accountId# and (c.account_id = b.account_id and c.ccid = b.ccid and c.set_year = b.set_year and c.rg_code = b.rg_code) group by account_id, set_year, ccid, rg_code) where c.account_id = #accountId#");
			map = new HashMap();
			map.put("accountId", account.getAccountId());
			daoSupport.executeUpdate(strSql.toString(), map);
			strSql = new StringBuffer();
			strSql.append("update gl_balance_cache c set balance_id = concat(sum_id, '000', set_month) where c.account_id = #accountId#");
			map = new HashMap();
			map.put("accountId", account.getAccountId());
			daoSupport.executeUpdate(strSql.toString(), map);
		}
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#fillCtrlRecordDTOKeyByCache
	 * (java.util.List)
	 */
	public void fillCtrlRecordDTOKeyByCache(final List ctrlRecords) {
		daoSupport
				.genericQuery(
						"select balance_id, sum_id from gl_balance_cache order by index_ asc",
						new ResultSetMapper() {
							int index = 0;

							CtrlRecordDTO beFilled = null;

							public Object handleRow(ResultSet rs)
									throws SQLException {
								beFilled = (CtrlRecordDTO) ctrlRecords
										.get(index++);
								beFilled.setBalance_id(rs.getString(1));
								beFilled.setSum_id(rs.getString(2));
								return null;
							}
						});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gfmis.fap.gl.balance.impl.IBalanceDao#saveTransData(java.lang.String,
	 * java.util.List)
	 */
	public void saveTransData(String tableName, List transRecordList) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("insert into ").append(tableName)
				.append("(S_SUM_ID,T_SUM_ID,LATEST_OP_DATE) values")
				.append("(#negativeBalance.sum_id#,#positiveBalance.sum_id#,'")
				.append(DateHandler.getDateFromNow(0)).append("')");
		daoSupport.batchExcute(buffer.toString(), transRecordList);
	}

	public void deletePayClearTmpData(String tableName, String transTableName) {
		if (!checkTable(tableName))
			return;
		StringBuffer buffer = new StringBuffer();
		buffer.append("delete ")
				.append(tableName)
				.append(" v ")
				.append(" where exists (select 1 from budget_useable_voucher bv")
				.append(" where v.budget_id = bv.toctrlid and exists (select 1")
				.append(" from ").append(transTableName).append(" t")
				.append(" where t.s_sum_id = bv.fromctrlid))");
		daoSupport.executeUpdate(buffer.toString());
	}

	/**
	 * 校验表是否存在
	 * 
	 * @param tableName
	 * @return
	 */
	public boolean checkTable(String tableName) {
		try {
			daoSupport.genericQuery("select 1 from " + tableName
					+ " where 1<>1", HashMap.class);
		} catch (Exception ex) {
			if (ex.getCause().getMessage().startsWith("ORA-00942"))
				return false;
		}
		return true;
	}

}