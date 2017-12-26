package gov.df.fap.service.gl;

import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.api.gl.interfaces.IVoucherService;
import gov.df.fap.api.rule.IRule;
import gov.df.fap.api.workflow.IBillTypeServices;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.configure.BusVouAcctmdl;
import gov.df.fap.bean.gl.configure.BusVouType;
import gov.df.fap.bean.gl.dto.Condition;
import gov.df.fap.bean.gl.dto.ConditionObj;
import gov.df.fap.bean.gl.dto.FCtrlRecordDTO;
import gov.df.fap.bean.gl.dto.IConditionItem;
import gov.df.fap.bean.gl.dto.JournalDTO;
import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.bean.util.FBaseDTO;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.bean.workflow.FBillTypeDTO;
import gov.df.fap.service.gl.Handler.AccountHandler;
import gov.df.fap.service.gl.Handler.ActionDefinitions;
import gov.df.fap.service.gl.Handler.AuditActionHandler;
import gov.df.fap.service.gl.Handler.CancelAuditActionHandler;
import gov.df.fap.service.gl.Handler.InvalidActionHandler;
import gov.df.fap.service.gl.Handler.MatchTargetInvalidActionHandler;
import gov.df.fap.service.gl.Handler.SaveActionHandler;
import gov.df.fap.service.gl.Handler.UpdateActionHandler;
import gov.df.fap.service.gl.balance.BalanceGenResult;
import gov.df.fap.service.gl.balance.BalanceList;
import gov.df.fap.service.gl.balance.BalanceSaverFactory;
import gov.df.fap.service.gl.balance.BalanceTracer;
import gov.df.fap.service.gl.balance.IBalanceDao;
import gov.df.fap.service.gl.balance.IBalanceService;
import gov.df.fap.service.gl.balance.impl.DefaultRefreshBalanceHandler;
import gov.df.fap.service.gl.balance.matcher.AbstractBalanceMatcher;
import gov.df.fap.service.gl.balance.matcher.DefaultBalanceMatcher;
import gov.df.fap.service.gl.balance.matcher.TargetBalanceMatcher;
import gov.df.fap.service.gl.balance.saver.DefaultBalanceSaverFactory;
import gov.df.fap.service.gl.balance.saver.ResetFromctrlidSaverFactory;
import gov.df.fap.service.gl.configure.impl.EngineConfiguration;
import gov.df.fap.service.gl.core.DaoSupport;
import gov.df.fap.service.gl.je.IJournalService;
import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.number.NumberUtil;
import gov.df.fap.util.xml.XMLData;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author LWQ
 * @version 2017-03-06
 */

@Service
public class VoucherService implements IVoucherService, InitializingBean {

  private static final Log logger = LogFactory.getLog(VoucherService.class);

  private static final Logger voucherLog = java.util.logging.Logger.getLogger("VoucherService");

  @Autowired
  private EngineConfiguration engineConfigure;

  @Autowired
  private IJournalService journalService;

  @Autowired
  private IBalanceService balanceService;

  @Autowired
  private IBillTypeServices billtypeService;

  @Autowired
  private ICoaService coaService;

  @Autowired
  private IBalanceDao balanceDao;

  @Autowired
  private DaoSupport daoSupport;

  @Autowired
  private IRule ruleService;

  public void setRuleService(IRule ruleService) {
    this.ruleService = ruleService;
  }

  public void setDaoSupport(DaoSupport daoSupport) {
    this.daoSupport = daoSupport;
  }

  public void setBalanceDao(IBalanceDao balanceDao) {
    this.balanceDao = balanceDao;
  }

  public void setBalanceService(IBalanceService balanceService) {
    this.balanceService = balanceService;
  }

  public void setJournalService(IJournalService impl) {
    this.journalService = impl;
  }

  public void setCoaService(ICoaService coaService) {
    this.coaService = coaService;
  }

  public void setBilltypeService(IBillTypeServices service) {
    this.billtypeService = service;
  }

  /**
   * 新增入帐接口,根据传入的交易凭证信息进行入账,实现额度控制
   * @param dto 传入的交易凭证信息体
   * @return 入账后的交易凭证信息对象
   * @throws Exception 入账中的错误或者校验失败的地方
   */
  public FVoucherDTO save(FVoucherDTO voucherDTO) throws Exception {
    List list = new ArrayList();
    list.add(voucherDTO);
    return (FVoucherDTO) saveBatch(list).get(0);
  }

  /**
   * 修改入帐接口,根据传入的交易凭证信息 进行入账,实现额度控制
   * @param voucherDTO 传入的交易凭证信息
   * @return 入账后的交易凭证信息对象
   * @throws Exception 入账中的错误或者校验失败的地方
   */
  public FVoucherDTO update(FVoucherDTO voucherDTO) throws Exception {
    List list = new ArrayList();
    list.add(voucherDTO);
    return (FVoucherDTO) updateBatch(list).get(0);
  }

  /**
   * 删除入帐接口,根据传入的交易凭证信息进行入账,实现额度控制
   * @param voucherDTO 传入的交易凭证信息
   * @return  boolean 是否成功
   * @throws Exception 入账中的错误或者校验失败的地方
   */
  public FVoucherDTO delete(FVoucherDTO voucherDTO) throws Exception {
    List list = new ArrayList();
    list.add(voucherDTO);
    deleteBatch(list);
    return voucherDTO;
  }

  /**
   * 终审入帐接口,根据传入的交易凭证信息进行入账,实现额度控制
   * @param voucherDTO 传入的交易凭证信息体
   * @return 入账后的交易凭证信息对象
   * @throws Exception 入账中的错误或者校验失败的地方
   */
  public FVoucherDTO audit(FVoucherDTO voucherDTO) throws Exception {
    List list = new ArrayList();
    list.add(voucherDTO);
    return (FVoucherDTO) auditBatch(list).get(0);
  }

  /**
   * 撤消终审入帐接口,根据传入的交易凭证信息进行入账,实现额度控制
   * @param voucherDTO 传入的交易凭证信息体
   * @return 入账后的交易凭证信息对象
   * @throws Exception 入账中的错误或者校验失败的地方
   */
  public FVoucherDTO cancelAudit(FVoucherDTO voucherDTO) throws Exception {
    List list = new ArrayList();
    list.add(voucherDTO);
    return (FVoucherDTO) cancelAuditBatch(list).get(0);
  }

  /**
   * 删除入帐接口,根据传入的交易凭证信息进行入账,实现额度控制
   * @param voucherDTO 传入的交易凭证信息体
   * @return 入账后的交易凭证信息对象
   * @throws Exception 入账中的错误或者校验失败的地方
   */
  public FVoucherDTO invalid(FVoucherDTO voucherDTO) throws Exception {
    List list = new ArrayList();
    list.add(voucherDTO);
    return (FVoucherDTO) invalidBatch(list).get(0);
  }

  /**
   * 回写fromctrlid与toctrlid, 注意!voucherDTOList顺序与balanceTrancerList都按vou_id排序.
   * @param voucherDTOList
   * @param balanceTracerList
   */
  protected void reSetCtrlid(List voucherDTOList, List balanceTracerList) {
    //设置业务数据fromctrlid和toctrlid
    Iterator tracersIt = balanceTracerList.iterator();
    boolean isSetFrom = true;
    boolean isSetTo = true;
    int voucherDtoOffset = 0;
    FVoucherDTO dto = null;
    while (tracersIt.hasNext()) {
      final BalanceTracer tracer = (BalanceTracer) tracersIt.next();
      dto = (FVoucherDTO) voucherDTOList.get(voucherDtoOffset);
      while (!StringUtil.equals(tracer.getVou_id(), dto.getVou_id())
        || !StringUtil.equals(tracer.getJournal().getBilltype_code(), dto.getBilltype_code())) {
        voucherDtoOffset++;
        dto = (FVoucherDTO) voucherDTOList.get(voucherDtoOffset);
        isSetFrom = isSetTo = true;
      }
      if (tracer.getCtrl_side() == 1 && isSetFrom && StringUtil.isEmpty(dto.getFromctrlid())) {
        isSetFrom = !(tracer.getIs_primary() == 1);
        dto.setFromctrlid(tracer.getCtrlId());
      } else if (tracer.getCtrl_side() == 0 && isSetTo) {
        isSetTo = !(tracer.getIs_primary() == 1);
        dto.setToctrlid(tracer.getCtrlId());
      }
    }
  }

  /**
   * 
   * @param inputList
   * @param handler
   * @param factory
   * @return
   * @throws Exception
   */
  protected List account(List inputList, AccountHandler handler) throws Exception {
    return account(inputList, handler, DefaultBalanceSaverFactory.newInstance(coaService, balanceDao));
  }

  protected List account(List inputList, AccountHandler handler, BalanceSaverFactory factory) throws Exception {
    return account(inputList, handler, factory, new DefaultBalanceMatcher(daoSupport));
  }

  protected List account(List inputList, AccountHandler handler, AbstractBalanceMatcher balanceMatcher)
    throws Exception {
    return account(inputList, handler, DefaultBalanceSaverFactory.newInstance(coaService, balanceDao), balanceMatcher);
  }

  /**
   * 统一记账方法
   * @param inputList
   * @param handler
   * @return
   * @throws Exception
   */
  protected List account(List inputList, AccountHandler handler, BalanceSaverFactory factory,
    AbstractBalanceMatcher balanceMatcher) throws Exception {
    voucherLog.info("当前线程: " + Thread.currentThread().toString() + " 记账类型:" + handler.getClass().getName() + " 开始记账操作");
    try {
      long start = System.currentTimeMillis();
      if (logger.isDebugEnabled()) {
        logger.debug(DateHandler.getDateFromNow(0) + "批量" + ActionDefinitions.ACTION_NAMES[(int) handler.getActionId()]
          + "记账开始, 时间戳:" + start);
      }
      if (inputList == null || inputList.isEmpty())
        return inputList;
      List voucherDTOList = this.makeUpdetails(inputList);

      //日志列表
      List inputJournals = new ArrayList();
      Iterator voucherDTOIt = voucherDTOList.iterator();
      int offset = 0;

      if (logger.isDebugEnabled())
        logger.debug("生成日志对象");

      while (voucherDTOIt.hasNext()) {
        final FVoucherDTO voucherDTO = (FVoucherDTO) voucherDTOIt.next();
        //交易凭证校验
        journalService.checkBill(voucherDTO);
        JournalDTO journalDTO = new JournalDTO(voucherDTO);
        journalDTO.setRg_code(CommonUtil.getRgCode());
        //			操作ID
        journalDTO.setAction_id(handler.getActionId());
        journalDTO.setIs_end(handler.getIsEnd());
        journalDTO.setIs_valid(handler.getIsValid());
        journalDTO.setIndex_(offset);
        journalDTO.setVou_money(handler.journalMoney(voucherDTO.getVou_money()));
        //2013-06-12 额度表机构冗余(李文全)
        String tmp_mb_id = (String) voucherDTO.getMb_id();
        if (tmp_mb_id == null || tmp_mb_id.equals("")) {
          tmp_mb_id = "#";
        }
        String tmp_agency_id = (String) voucherDTO.getAgency_id();
        if (tmp_agency_id == null || tmp_agency_id.equals("")) {
          tmp_agency_id = "#";
        }
        journalDTO.setMb_id(tmp_mb_id);
        journalDTO.setAgency_id(tmp_agency_id);

        final String billTypeCode = voucherDTO.getBilltype_code();
        //记账模板类型
        final BusVouType vouType = engineConfigure.getBvTypeByBill(billTypeCode);
        if (vouType == null)
          throw new RuntimeException(voucherDTO.getVou_no() + " id:" + voucherDTO.getVou_id() + "指定的交易凭证"
            + voucherDTO.getBilltype_code() + "没有挂接记账模板,记账失败!");

        journalDTO.setVou_type_id(vouType.getVou_type_id());
        inputJournals.add(journalDTO);
        offset++;

        voucherLog.info("当前线程: " + Thread.currentThread().toString() + " 记账业务数据主键: " + journalDTO.getVou_id()
          + " 业务数据金额: " + journalDTO.getVou_money());
      }

      if (logger.isDebugEnabled())
        logger.debug("插入日志临时表");
      //插入日志中间表
      journalService.insertJournalCache(inputJournals);
      List existsJournals = null;
      //检查是否存在gl_journal表中
      existsJournals = handler.findExistsJournalsWithCache();
      if (logger.isDebugEnabled())
        logger.debug("查询历史日志记录, 数量:" + (existsJournals == null ? 0 : existsJournals.size()));

      if (logger.isDebugEnabled())
        logger.debug("通过临时表校验数据日志数据合法性");
      //记账数据和己存在日志数据进行检查
      handler.checkJournalWithCache(existsJournals, inputJournals);
      //生成额度
      BalanceGenResult balanceGenresult = handler.generateBalance(existsJournals, inputJournals, balanceMatcher);

      //临时额度列表
      BalanceList tmprecordList = balanceGenresult.getBalanceList();
      //额度追溯对象列表
      List balanceTracerList = balanceGenresult.getBalanceTracerList();

      //根据临时额度列表更新额度表
      if (tmprecordList.size() > 0) {
        List accountList = new ArrayList();
        List accountIdList = tmprecordList.getAccountIdList();
        for (int i = 0; i < accountIdList.size(); i++) {
          accountList.add(engineConfigure.getAccount((String) accountIdList.get(i)));
        }
        balanceService.saveBalance(tmprecordList, accountList, factory);
      }
      //额度追溯
      if (balanceTracerList.size() > 0) {
        if (logger.isDebugEnabled())
          logger.debug("保存额度追溯对象, 数量:" + balanceTracerList.size());
        handler.tracerBalance(balanceTracerList);
      }
      //记账日志
      if (logger.isDebugEnabled())
        logger.debug("更新日志表及日志历史表");
      handler.storeJournalWithCache();

      if (logger.isDebugEnabled())
        logger.debug("删除日志缓存表数据");
      journalService.backUpJournalWithCache();

      if (logger.isDebugEnabled())
        logger.debug("将生成的主来源与主去向设置到传入的FVoucherDTO对象中,如原来已传入fromctrlid,则不修改.");
      reSetCtrlid(voucherDTOList, balanceTracerList);

      handler.handleAfterProperties(inputList);

      if (logger.isDebugEnabled())
        logger.debug(DateHandler.getDateFromNow(0) + ", 批量"
          + ActionDefinitions.ACTION_NAMES[(int) handler.getActionId()] + "记账结束, 时间戳:" + start + ", 耗时:"
          + ((System.currentTimeMillis() - start)) + "毫秒");
      return inputList;
    } finally {
      if (logger.isDebugEnabled())
        logger.debug("删除日志缓存表数据");
      journalService.clearJournalCache();
      voucherLog.info("当前线程: " + Thread.currentThread().toString() + " 记账类型:" + handler.getClass().getName()
        + " 记账操作结束");
    }
  }

  public List resetFromctrlidAudit(List voucherDTOList) throws Exception {
    BalanceSaverFactory defaultBalanceSaverFactory = DefaultBalanceSaverFactory.newInstance(coaService, balanceDao);
    ResetFromctrlidSaverFactory decorFactory = new ResetFromctrlidSaverFactory(defaultBalanceSaverFactory);
    return account(voucherDTOList, new AuditActionHandler(balanceService, journalService, coaService, engineConfigure),
      decorFactory);
  }

  /**
   * 批量新增入帐接口,根据传入的交易凭证信息 List 进行入账,实现额度控制
   * @param voucherDTOList 传入的交易凭证信息体List
   * @return 入账后的交易凭证信息对象List
   * @throws Exception 入账中的错误或者校验失败的地方
   */
  public List saveBatch(List voucherDTOList) throws Exception {
    return account(voucherDTOList, new SaveActionHandler(balanceService, journalService, coaService, engineConfigure));
  }

  /*
   * (non-Javadoc)
   * @see gov.gfmis.fap.gl.interfaces.IVoucherService#updateBatch(java.util.List)
   */
  public List updateBatch(List voucherDTOList) throws Exception {
    String rg_code = SessionUtil.getRgCode();
    String set_year = SessionUtil.getLoginYear();

    return account(voucherDTOList, new UpdateActionHandler(balanceService, journalService, coaService, engineConfigure));
  }

  public List auditBatch(List voucherDTOList) throws Exception {
    return account(voucherDTOList, new AuditActionHandler(balanceService, journalService, coaService, engineConfigure));
  }

  public List matchByToctrlidInvalid(List voucherDTOList) throws Exception {
    return account(voucherDTOList, new MatchTargetInvalidActionHandler(balanceService, journalService, coaService,
      engineConfigure), new TargetBalanceMatcher(balanceDao));
  }

  public List cancelAuditBatch(List voucherDTOList) throws Exception {
    return account(voucherDTOList, new CancelAuditActionHandler(balanceService, journalService, coaService,
      engineConfigure));
  }

  public List invalidBatch(List voucherDTOList) throws Exception {
    return account(voucherDTOList,
      new InvalidActionHandler(balanceService, journalService, coaService, engineConfigure));
  }

  public List deleteBatch(List voucherDTOList) throws Exception {
    return invalidBatch(voucherDTOList);
  }

  /**
   * 对所有记账数据形成统一的明细结构
   * @param voucherDTOList
   * @return
   */
  protected List makeUpdetails(List voucherDTOList) {
    if (voucherDTOList == null || voucherDTOList.isEmpty())
      return new LinkedList();

    List detailList = new LinkedList();
    Iterator it = voucherDTOList.iterator();
    while (it.hasNext()) {
      final FVoucherDTO billDto = (FVoucherDTO) it.next();
      if (billDto.getDetails() != null) {
        if (billDto.getDetails().isEmpty())
          detailList.add(billDto);
        else
          detailList.addAll(billDto.getDetails());
      } else
        detailList.add(billDto);
    }
    return detailList;
  }

  //---------------额度查询接口
  private BusVouAccount getQueryAccountByBilldType(String billTypeCode) {
    BusVouAccount queryAccount = null;
    if (!EngineConfiguration.getConfig().isBillTypeAccountAssociate()) {//用记账模板关联
      BusVouType bvType = engineConfigure.getBvTypeByBill(billTypeCode);

      List acctmdls = bvType.getBusVouAcctmdl();
      for (int i = 0; i < acctmdls.size(); i++) {
        final BusVouAcctmdl acctmdl = (BusVouAcctmdl) acctmdls.get(i);
        if (acctmdl.getIs_primary_source() == NumberUtil.INT_TRUE) {
          queryAccount = acctmdl.getBusVouAccount();
        }
      }
    } else {//直接使用交易凭证上的科目
      FBillTypeDTO billtypeDto = billtypeService.getBillTypeByCode(billTypeCode);
      String accountId = billtypeDto.getVou_control_id();
      queryAccount = engineConfigure.getAccount(accountId);
    }

    return queryAccount;
  }

  public List findSumCtrlRecords(String billType, FPaginationDTO page, Condition plusDetailSQL) throws Exception {
    BusVouAccount queryAccount = getQueryAccountByBilldType(billType);

    if (queryAccount == null)
      throw new RuntimeException("无法找到来源科目,额度查询失败!");
    return balanceService.findSumCtrlRecords(queryAccount, page, plusDetailSQL);
  }

  public List simpleFindSumCtrlRecords(String billType, FPaginationDTO page, Condition plusDetailSQL) throws Exception {
    if (plusDetailSQL != null) {
      List itemList = plusDetailSQL.getCondition();
      for (int i = 0; i < itemList.size(); i++) {
        IConditionItem item = (IConditionItem) itemList.get(i);
        item.setStrictGenerate(true);
      }
    }
    return findSumCtrlRecords(billType, page, plusDetailSQL);
  }

  public List getSumCtrlRecordsByAccount(String accountCode, FPaginationDTO page, Condition condition) throws Exception {
    BusVouAccount account = engineConfigure.getAccountByCode(accountCode);
    return balanceService.findSumCtrlRecords(account, page, condition);
  }

  /**
   * 该接口在V6.2.60.00版本号特殊处理过,在以后版本要和支付组一起修改正确,保证返回的是BusVouAccount对象.
   */
  public List getAccountsByBillTypeCode(String billType, Condition plusSQL) throws Exception {
    BusVouType bvType = engineConfigure.getBvTypeByBill(billType);
    if (bvType == null)
      return null;
    List accounts = new ArrayList();
    List acctmdls = bvType.getBusVouAcctmdl();
    for (int i = 0; i < acctmdls.size(); i++) {
      final BusVouAccount account = ((BusVouAcctmdl) acctmdls.get(i)).getBusVouAccount();
      final XMLData xmlData = new XMLData();
      xmlData.put("sum_type_id", account.getAccountId());
      xmlData.put("sum_type_code", account.getAccountCode());
      xmlData.put("sum_type_name", account.getAccountName());
      xmlData.put("table_name", account.getTableName());
      xmlData.put("coa_id", StringUtil.toStr(account.getCoaId()));
      accounts.add(xmlData);
    }
    return accounts;
  }

  public FCtrlRecordDTO getCtrlRecord(String sumId, String accountCode, int set_month) throws Exception {
    Condition condition = new ConditionObj();
    condition.add("set_month", Condition.EQUAL, StringUtil.toStr(set_month), true);
    return balanceService.loadBalance(sumId, engineConfigure.getAccountByCode(accountCode), condition);
  }

  public List getCtrlRecords(String billType, FPaginationDTO page, Condition plusSQL) throws Exception {
    BusVouAccount queryAccount = getQueryAccountByBilldType(billType);
    return balanceService.findBalance(queryAccount, plusSQL);
  }

  public List getCtrlRecordsByAccount(String accountCode, FPaginationDTO page, Condition plusSQL) {
    BusVouAccount queryAccount = engineConfigure.getAccountByCode(accountCode);
    return balanceService.findBalance(queryAccount, plusSQL);
  }

  public List getTargetVoucher(String ctrlId, int set_month) {
    return journalService.findVoucherByBalance(ctrlId, BalanceTracer.CTRL_SIDE_SOURCE);//额度的去向就是凭证的来源
  }

  public List getTargetBalance(String vouId, String billTypeCode) {
    return getBalance(vouId, billTypeCode, BalanceTracer.CTRL_SIDE_TARGET);
  }

  public List getSourceVoucher(String ctrlId, int set_month) {
    return journalService.findVoucherByBalance(ctrlId, BalanceTracer.CTRL_SIDE_TARGET);
  }

  public List getSourceBalance(String vouId, String billTypeCode) {
    return getBalance(vouId, billTypeCode, BalanceTracer.CTRL_SIDE_SOURCE);
  }

  public List getBalance(String vouId, String billTypeCode, int ctrlSide){
    BusVouType bvType = engineConfigure.getBvTypeByBill(billTypeCode);
    BusVouAccount queryAccount = getQueryAccountByBilldType(billTypeCode);
    return balanceService.findBalanceByJournal(bvType, queryAccount, vouId, ctrlSide);
  }

  /**
   * 根据记账模板对象获取ccid
   * @param inputFVoucherDto 输入业务信息
   * @return 结果ccid
   * @throws Exception 异常
   */
  public String getCCID(FVoucherDTO inputFVoucherDto) throws Exception {
    return coaService.getCCIDByBaseDTO(inputFVoucherDto);
  }

  public String getCondition(String billTypeCode, ConditionObj obj) throws Exception {
    return null;
  }

  public boolean updateCtrlForBudget(String billTypeCode, String ctrlid, String detailCcid, String version)
    throws Exception {
    return false;
  }

  public void budgetFileBalanceReferesh(String accountCode, String tableName, String whereStr) throws Exception {
    final BusVouAccount account = engineConfigure.getAccountByCode(accountCode);
    balanceService.refreshBalance(account,
      new DefaultRefreshBalanceHandler(balanceDao, daoSupport, tableName, whereStr));
  }

  public BusVouType loadBvtypeByBillType(String billtypeCode) throws Exception {
    final BusVouType bvType = engineConfigure.getBvTypeByBill(billtypeCode);
    final BusVouType returnType = EngineConfiguration.configureEleCode(bvType);
    return returnType;
  }

  public List findAccountsMatched(String billtypeCode, FBaseDTO ruleElements) throws Exception {
    final BusVouType bvType = engineConfigure.getBvTypeByBill(billtypeCode);
    final Iterator iterator = bvType.getBusVouAcctmdl().iterator();
    final List returnList = new LinkedList();
    BusVouAcctmdl acctmdl = null;
    while (iterator.hasNext()) {
      acctmdl = (BusVouAcctmdl) iterator.next();
      if (StringUtil.isEmpty(acctmdl.getRule_id()) || StringUtil.ZERO.equals(acctmdl.getRule_id())) {
        returnList.add(acctmdl);
      } else if (ruleService.isMatch(acctmdl.getRule_id(), ruleElements))
        returnList.add(acctmdl);
    }
    return returnList;
  }

  /**
   * 创建Bean实例时，创建记账日志
   */
  public void afterPropertiesSet() throws Exception {
    File logFile = new File("fap_gl");
    if (!logFile.isDirectory())
      logFile.mkdir();

    FileHandler voucherLogHandler = new FileHandler("fap_gl/fap_gl.log", 5000000, 30);
    voucherLogHandler.setFormatter(new SimpleFormatter());
    voucherLog.addHandler(voucherLogHandler);
    voucherLog.setUseParentHandlers(false);
  }

}
