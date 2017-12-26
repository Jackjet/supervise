package gov.df.fap.service.gl.balance.impl;

import gov.df.fap.api.gl.balance.RefreshBalanceHandler;
import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.api.rule.IDataRight;
import gov.df.fap.bean.gl.balance.MonthlyBalanceDTO;
import gov.df.fap.bean.gl.balance.TransRecord;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.configure.BusVouType;
import gov.df.fap.bean.gl.dto.Condition;
import gov.df.fap.bean.gl.dto.ConditionObj;
import gov.df.fap.bean.gl.dto.FCtrlRecordDTO;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.service.gl.balance.BalanceList;
import gov.df.fap.service.gl.balance.BalanceSaverFactory;
import gov.df.fap.service.gl.balance.BalanceTracer;
import gov.df.fap.service.gl.balance.IBalanceDao;
import gov.df.fap.service.gl.balance.IBalanceService;
import gov.df.fap.service.gl.balance.saver.DefaultBalanceSaverFactory;
import gov.df.fap.service.gl.coa.impl.CcidAccelerator;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.exceptions.gl.ExecutingMonthlyBalanceException;
import gov.df.fap.service.util.exceptions.gl.GlException;
import gov.df.fap.service.util.exceptions.gl.IllegalBalanceException;
import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.util.date.DateHandler;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 
 * 额度服务,提供额度相关逻辑操作.包括生成临时额度,额度查询,累计月额度的月结等.
 * @author justin
 * 	       
 * @version  2017-03-15
 */
@Service
public class BalanceService implements IBalanceService {

  private static Log log = LogFactory.getLog(BalanceService.class);

  @Autowired
  private ICoaService coaService;

  @Autowired
  private IBalanceDao dao;

  @Autowired
  private IDataRight dataRight;

  private TransactionTemplate transactionTemplate;

  @Autowired
  @Qualifier("CcidGenAccelerator")
  private CcidAccelerator accelerator;

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO generalDAO;

  public CcidAccelerator getAccelerator() {
    return this.accelerator;
  }

  public void setAccelerator(CcidAccelerator accelerator) {
    this.accelerator = accelerator;
  }

  public void setDataRight(IDataRight dataRight) {
    this.dataRight = dataRight;
  }

  public void setCoaService(ICoaService coa) {
    this.coaService = coa;
  }

  public void setDao(IBalanceDao dao) {
    this.dao = dao;
  }

  public GeneralDAO getGeneralDAO() {
    return generalDAO;
  }

  public void setGeneralDAO(GeneralDAO generalDAO) {
    this.generalDAO = generalDAO;
  }

  public TransactionTemplate getTransactionTemplate() {
    return transactionTemplate;
  }

  public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
    this.transactionTemplate = transactionTemplate;
  }

  public void saveBalance(List tmpRecordList, List accountList, BalanceSaverFactory factory)
    throws IllegalBalanceException, SQLException {
    //将临时额度插入缓存表
    if (log.isDebugEnabled())
      log.debug("插入临时额度到额度临时表:gl_balance_cache, 数量:" + tmpRecordList.size());

    dao.saveBalanceCache(tmpRecordList);
    try {
      if (log.isDebugEnabled())
        log.debug("批量生成rcid, 表:gl_balance_cache, 条件:and rcid is null");

      dataRight.createBatchRCID("gl_balance_cache", " and rcid is null");
    } catch (Exception e) {
      log.error("生成rcid错误", e);
      dao.deleteBalanceCache();
      throw new GlException("生成rcid错误." + e.getMessage());
    }
    try {
      //根据不同科目循环生成额度
      for (Iterator iter = accountList.iterator(); iter.hasNext();) {
        BusVouAccount account = (BusVouAccount) iter.next();

        if (log.isDebugEnabled()) {
          log.debug("处理额度:" + account + " ");
          log.debug("填充额度ID");
        }
        dao.fillCachedBalanceKey(account);
        //通过缓存表来批量插入/更新额度
        factory.newSaverInstance(account).saveBalance(tmpRecordList);
      }
      if (log.isDebugEnabled())
        log.debug("填充临时额度对象的sum_id与balance_id");
      dao.fillCtrlRecordDTOKeyByCache(tmpRecordList);
    } finally {
      dao.deleteBalanceCache();
    }
  }

  /**
   * 是否已经执行当月的月结
   * @param month 指定月份
   * @return
   */
  public boolean alreadyCloseMonthEnd(int month) {
    int setYear = CommonUtil.getIntSetYear();
    int currentMonth = DateHandler.getCurrentMonth();
    MonthlyBalanceDTO dto = dao.loadMonthlyBalanceDTO(setYear, currentMonth - 1);
    return dto != null;
  }

  public synchronized void closeMonthEnd() throws ExecutingMonthlyBalanceException, Exception {
    Session session = generalDAO.getSession();
    Connection con = session.connection();
    CallableStatement cs = null;
    try {
      cs = con.prepareCall("{call GL_CLOSE_MONTH_END()}");
      cs.execute();
    } catch (Exception ex) {
      log.info("手动执行月结任务失败！");
      throw new ExecutingMonthlyBalanceException(ex.getMessage());
    } finally {
      if (cs != null)
        cs.close();
      if (session != null)
        generalDAO.closeSession(session);
    }
    log.info("累计月额度月结定时任务结束");
  }

  /*
   * (non-Javadoc)
   * @see gov.gfmis.fap.gl.balance.IBalanceService#reTraceCcidChangeBalance(java.util.List)
   */
  public void reTraceCcidChangeBalance(List balanceTracers) {
    BalanceTracer tracer = null;
    Set dataSet = new HashSet();
    //删除ccid有改动的
    Iterator iterator = balanceTracers.iterator();
    while (iterator.hasNext()) {
      tracer = (BalanceTracer) iterator.next();
      dataSet.add(tracer.getVou_id() + ":" + tracer.getVou_type_id() + ":" + tracer.getSet_year());
    }
    iterator = dataSet.iterator();
    String[] s = null;
    while (iterator.hasNext()) {
      String content = (String) iterator.next();
      s = content.split(":");
      dao.deleteBalanceTrace(s[0], s[1], s[2]);
    }
    //重新将ccid有改动的额度追溯对象插入追溯表
    if (balanceTracers.size() > 0)
      dao.saveBalanceTracer(balanceTracers);
  }

  public Map findBalanceTraceWithCache() {
    Map cacheMap = new HashMap();
    List returnList = dao.loadTracerByJournalCache();
    Iterator iterator = returnList.iterator();
    BalanceTracer tracer = null;
    while (iterator.hasNext()) {
      tracer = (BalanceTracer) iterator.next();
      if (cacheMap.get(tracer.getVou_id()) == null) {
        cacheMap.put(tracer.getVou_id(), new LinkedList());
        ((List) cacheMap.get(tracer.getVou_id())).add(tracer);
      } else
        ((List) cacheMap.get(tracer.getVou_id())).add(tracer);
    }
    return cacheMap;
  }

  public void deleteBalanceCache() throws Exception {
    dao.deleteBalanceCache();
  }

  public void insertBalanceTrace(List list) {
    dao.saveBalanceTracer(list);
  }

  public static void main(String[] args) {
    BigDecimal num = new BigDecimal("11.22");
    System.out.println(num.negate());
  }

  /*
   * (non-Javadoc)
   * @see gov.gfmis.fap.gl.balance.IBalanceService#findSumCtrlRecords(java.lang.String, gov.gfmis.fap.util.FPaginationDTO, gov.gfmis.fap.gl.dao.Condition)
   */
  public List findSumCtrlRecords(BusVouAccount queryAccount, FPaginationDTO page, Condition plusDetailSQL) {
    return dao.findBalance(queryAccount, page, plusDetailSQL);
  }

  /*
   * (non-Javadoc)
   * @see gov.gfmis.fap.gl.balance.IBalanceService#loadBalance(java.lang.String, java.lang.String, gov.gfmis.fap.gl.dao.Condition)
   */
  public FCtrlRecordDTO loadBalance(String sumId, BusVouAccount account, Condition condition) {
    if (condition == null)
      condition = new ConditionObj();
    condition.add("sum_id", Condition.EQUAL, sumId, true);
    condition.setAllowGroup(false);
    return dao.loadBalance(account, condition);
  }

  public List findBalance(BusVouAccount account, Condition condition) {
    return dao.findBalance(account, condition);
  }

  public List findBalanceByJournal(BusVouType bvType, BusVouAccount account, String vouId, int side) {
    return dao.findBalanceByJournal(bvType, account, vouId, side);
  }

  public void refreshBalance(BusVouAccount account, RefreshBalanceHandler handler) throws Exception {
    final List transList = new LinkedList();
    //final List genCcidList = new LinkedList();
    final List balanceList = new LinkedList();
    final List accountList = new LinkedList();
    //final List newCtrlList = new LinkedList();
    final BalanceList sumList = new BalanceList(new LinkedList());

    //初始化刷新数据
    handler.initRefreshData();
    accountList.add(account);

    if (log.isDebugEnabled())
      log.debug("生成额度对象");

    for (int i = 0; i < handler.getRefreshDataSize(); i++) {
      //final FCtrlRecordDTO fctrlDto = handler.getFCtrlRecordDTO(i);
      final TransRecord record = handler.getTransRecord(i);
      //handler.doRefreshElement(i);

      //保存转换额度对象
      transList.add(record);

      //保存入账额度
      balanceList.add(record.getNegativeBalance());
      balanceList.add(record.getPositiveBalance());

      //newCtrlList.add(record.getPositiveBalance());
      //genCcidList.add(fctrlDto);
    }
    if (log.isDebugEnabled())
      log.debug("批量生成ccid");
    //accelerator.generateCcidBatch(new BatchCcidGenProcesser(newCtrlList, genCcidList, coaService), CommonUtil.getIntSetYear());
    sumList.addAll(balanceList);
    if (log.isDebugEnabled())
      log.debug("保存额度对象");
    this.saveBalance(sumList, accountList, DefaultBalanceSaverFactory.newInstance(coaService, dao));
    //刷新表层数据,保存转换记录
    if (log.isDebugEnabled())
      log.debug("刷新表层数据");
    //重新生成表层额度的ccid
    coaService.getCcidBatch(handler.getRefreshBalanceData());
    coaService.getCcidBatch(handler.getRefreshPayData());
    handler.afterRefreshBalance(transList);
  }
}
