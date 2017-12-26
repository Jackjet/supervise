package gov.df.fap.service.gl.configure.impl;

import gov.df.fap.api.dictionary.interfaces.IDictionary;
import gov.df.fap.api.gl.coa.ibs.ICoa;
import gov.df.fap.api.gl.configure.AccountService;
import gov.df.fap.api.rule.IRuleConfigure;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.configure.BusVouAcctmdl;
import gov.df.fap.bean.gl.configure.BusVouType;
import gov.df.fap.bean.rule.dto.RightGroupDTO;
import gov.df.fap.bean.rule.dto.RightGroupDetailDTO;
import gov.df.fap.bean.rule.dto.RuleDTO;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.service.util.exceptions.gl.CoaNotExistsException;
import gov.df.fap.service.util.exceptions.gl.ExistOnWayDataOfBusVouException;
import gov.df.fap.service.util.exceptions.gl.GlException;
import gov.df.fap.service.util.gl.configure.IBusVouTypeDao;
import gov.df.fap.service.util.gl.configure.IBusVouTypeService;
import gov.df.fap.service.util.memcache.MemCacheMap;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.exception.IllegalEleLevelOfDownStreamCoaException;
import gov.df.fap.util.exception.LackEleOfDownStreamCoaException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 记账模板服务
 * 
 * @author  justin
 * @version
 */
@Component("busVouTypeServiceImpl")
public class BusVouTypeServiceImpl implements IBusVouTypeService {

  @Autowired
  private IBusVouTypeDao bvTypeDao = null;

  @Autowired
  private IDictionary dictionary;

  @Autowired
  private IRuleConfigure ruleService;

  @Autowired
  private ICoa coaService;

  private AccountService accountService;

  public final static String ELEMENT_COUNT = "eleCount";

  // 图形化记账模板保存时,更新和删除时需要进行强制保存的校验.从界面中传入
  private boolean isForceSave = false;

  // 所有要素个数的缓存,计算权限信息时加速
  // final Map allElementCountMap = null;
  Map allElementCountMap = MemCacheMap.getCacheMap(BusVouTypeServiceImpl.class);

  public void setBvTypeDao(IBusVouTypeDao dao) {
    this.bvTypeDao = dao;
  }

  public void setCoaService(ICoa coaService) {
    this.coaService = coaService;
  }

  public void setRuleService(IRuleConfigure ruleService) {
    this.ruleService = ruleService;
  }

  public void setDictionary(IDictionary dictionary) {
    this.dictionary = dictionary;
  }

  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }

  public void saveSetBusVouType(BusVouType busVouType) throws IllegalEleLevelOfDownStreamCoaException,
    LackEleOfDownStreamCoaException {

    bvTypeDao.saveBusVouType(busVouType);
    try {
      if (busVouType.getBusVouAcctmdl() != null && !busVouType.getBusVouAcctmdl().isEmpty()) {
        final Iterator iterator = busVouType.getBusVouAcctmdl().iterator();
        while (iterator.hasNext())
          ((BusVouAcctmdl) iterator.next()).setVou_type_id(busVouType.getVou_type_id());
        bvTypeDao.saveBusVouAcctmdl(busVouType.getBusVouAcctmdl());
      }
    } catch (RuntimeException ex) {
      throw new IllegalArgumentException("记账模板编码为" + busVouType.getVou_type_code()
        + "记账模板Acctmdl中有非法信息.请确认Acctmdl中挂接的科目id是否在科目表存在");
    }
  }

  public void updateSetBusVouType(BusVouType busVouType) throws IllegalEleLevelOfDownStreamCoaException,
    LackEleOfDownStreamCoaException, ExistOnWayDataOfBusVouException, SQLException {
    // validCoa(busVouType.getBusVouAcctmdl());
	  if(busVouType!=null){
		  List list = busVouType.getBusVouAcctmdl();
	  }
    if (bvTypeDao.existOnWayData(busVouType.getVou_type_id()))
      throw new ExistOnWayDataOfBusVouException("记账模板编码为" + busVouType.getVou_type_code() + "存在在途数据,不允许更新");
    updateSetBusVouTypeQuiet(busVouType);
  }

  public void updateSetBusVouTypeQuiet(BusVouType busVouType) throws SQLException {
    bvTypeDao.updateBusVouType(busVouType);
    bvTypeDao.updateBusVouAcctmdl(busVouType.getBusVouAcctmdl(), busVouType.getVou_type_id());
  }

  public void deleteVouType(BusVouType busVouType) throws ExistOnWayDataOfBusVouException {
    if (busVouType == null || busVouType.getVou_type_id() == 0)
      throw new GlException("参数中缺少id");
    if (bvTypeDao.existOnWayData(busVouType.getVou_type_id()))
      throw new ExistOnWayDataOfBusVouException("记账模板编码为" + busVouType.getVou_type_code() + "存在在途数据,不允许删除");

    deleteVouTypeQuiet(busVouType);
  }

  private void deleteVouTypeQuiet(BusVouType busVouType) {
    bvTypeDao.deleteAcctmdlByBvTypeId(busVouType.getVou_type_id());
    bvTypeDao.deleteBusVouType(busVouType);
  }

  public BusVouType loadVouTypeByBill(String billTypeCode) {
    long busVouTypeId = bvTypeDao.getVouTypeIdByBill(billTypeCode);
    if (busVouTypeId == 0)
      throw new GlException("没有查询到相应记账模板设置");
    return loadVouType(busVouTypeId);
  }

  private BusVouType loadVouType(long busVouTypeId) {
    BusVouType busVouType = loadBusVouType(busVouTypeId);
    List list = bvTypeDao.loadVouAcctdml(busVouType.getVou_type_id());
    busVouType.setBusVouAcctmdl(list);
    return busVouType;
  }

  public long getVouTypeIdByBill(String billtypeCode) {
    return bvTypeDao.getVouTypeIdByBill(billtypeCode);
  }

  public BusVouType loadBusVouType(long vouTypeId) {
    return (BusVouType) bvTypeDao.loadBusVouType(vouTypeId);
  }

  public List validateRule(BusVouType bvType) {
    // 下游科目集合
    final List downList = new LinkedList();
    // 上游科目集合
    final List upList = new LinkedList();
    final List acctmdlList = bvType.getBusVouAcctmdl();
    final List messageList = new LinkedList();
    Iterator iterator = acctmdlList.iterator();
    BusVouAcctmdl acctmdl = null;
    while (iterator.hasNext()) {
      acctmdl = (BusVouAcctmdl) iterator.next();
      if (acctmdl.getBusVouAccount() == null)
        continue;
      if (acctmdl.getEntry_side() == acctmdl.getBusVouAccount().getBalanceSide())
        downList.add(acctmdl);
      else
        upList.add(acctmdl);
    }
    final EleCountMap elementCountMap = new EleCountMap();
    try {
      // 区分上下游处理是为了 排除不同方向的一种科目
      Map tempMap = null;
      Map eleCountMap = null;
      elementCountMap.putAll(countElement(downList));
      elementCountMap.putAll(countElement(upList));
      iterator = elementCountMap.keySet().iterator();
      while (iterator.hasNext()) {
        final Object obj = iterator.next();
        // elementCountMap中存在这两种键值
        if (!(obj instanceof BusVouAccount))
          continue;
        final BusVouAccount account = (BusVouAccount) obj;
        tempMap = (Map) elementCountMap.get(account);
        eleCountMap = (Map) elementCountMap.get(ELEMENT_COUNT);
        final Iterator it = tempMap.keySet().iterator();
        while (it.hasNext()) {
          final String eleCode = it.next().toString();
          final int souceEleNum = ((Integer) tempMap.get(eleCode)).intValue();
          final int eleNum = ((Set) eleCountMap.get(eleCode)).size();
          final String eleValue = ((Set) eleCountMap.get(eleCode)).iterator().next().toString();
          int allEleNum = 0;
          // 从服务器缓存中取出要素个数
          if (!allElementCountMap.containsKey(SessionUtil.getRgCode() + eleCode)) {
            final List list = dictionary.findEleValues(eleCode, (FPaginationDTO) null, new String[] { "1" }, true, "",
              new HashMap(), null);
            allEleNum = list.size();
            allElementCountMap.put(SessionUtil.getRgCode() + eleCode, new Integer(allEleNum));
          } else
            allEleNum = ((Integer) allElementCountMap.get(SessionUtil.getRgCode() + eleCode)).intValue();
          if (souceEleNum > eleNum && !eleValue.equals("-1"))
            messageList.add(" " + account.getAccountCode() + ":" + account.getAccountName() + "科目权限中配置要素" + eleCode
              + "存在交集");
          else if (eleNum < allEleNum && !eleValue.equals("-1"))
            messageList.add(" " + account.getAccountCode() + ":" + account.getAccountName() + "科目权限中配置要素" + eleCode
              + "尚未覆盖全集");
        }
      }
    } catch (Exception ex) {
      messageList.add(ex.getMessage());
    }
    return messageList;
  }

  /**
   * 统计出acctmdl中配置权限的各个要素的个数
   * 
   * @param acctmdlList
   * @return Map 中科目id为键,值为一个Map -- Map中 键是要素对象，值为要素的个数
   * @throws Exception
   */
  private Map countElement(List acctmdlList) throws Exception {
    final Map handleMap = new HashMap();
    Map tempMap = null;
    Set tempSet = null;
    BusVouAcctmdl acctmdl = null;
    RuleDTO ruleDto = null;
    RightGroupDTO rightGroupDto = null;
    RightGroupDetailDTO rightGroupDetailDto = null;
    Iterator it = null;
    final Iterator iterator = acctmdlList.iterator();
    while (iterator.hasNext()) {
      acctmdl = (BusVouAcctmdl) iterator.next();
      BusVouAccount account = containsKeyId(handleMap, acctmdl.getBusVouAccount().getAccountId());
      if (account == null)
        account = acctmdl.getBusVouAccount();
      handleMap.put(account, handleMap.get(account) == null ? new HashMap() : handleMap.get(account));
      tempMap = (Map) handleMap.get(ELEMENT_COUNT);
      if (tempMap == null)
        tempMap = new HashMap();
      handleMap.put(ELEMENT_COUNT, tempMap);

      final Map eleCountMap = (Map) handleMap.get(account);
      // 通过rule_id取得权限配置
      ruleDto = ruleService.getRuleDto(acctmdl.getRule_id());
      final List rightGroupList = ruleDto.right_group_list;
      for (int i = 0; i < rightGroupList.size(); i++) {
        rightGroupDto = (RightGroupDTO) rightGroupList.get(i);
        final List detailList = rightGroupDto.detail_list;
        it = detailList.iterator();
        while (it.hasNext()) {
          rightGroupDetailDto = (RightGroupDetailDTO) it.next();
          // 将要素值存储到Set中,保证唯一性. 查重是可以与handleMap中的account键中要素对比
          tempSet = (Set) tempMap.get(rightGroupDetailDto.getELE_CODE());
          if (tempSet == null) {
            tempSet = new HashSet();
            tempMap.put(rightGroupDetailDto.getELE_CODE(), tempSet);
          }
          int count = 0;
          if (((Map) handleMap.get(account)).get(rightGroupDetailDto.getELE_CODE()) != null)
            count = ((Integer) ((Map) handleMap.get(account)).get(rightGroupDetailDto.getELE_CODE())).intValue();
          // 权限配置中选中了要素,但没勾选具体哪个要素值的
          if (rightGroupDetailDto.getELE_VALUE().equals("#")) {
            if (!allElementCountMap.containsKey(SessionUtil.getRgCode() + rightGroupDetailDto.getELE_CODE())) {
              final List list = dictionary.findEleValues(rightGroupDetailDto.getELE_CODE(), (FPaginationDTO) null,
                new String[] { "1" }, true, "", new HashMap(), null);
              final int allEleNum = list.size();
              allElementCountMap.put(SessionUtil.getRgCode() + rightGroupDetailDto.getELE_CODE(),
                new Integer(allEleNum));
            }
            final int num = ((Integer) allElementCountMap.get(SessionUtil.getRgCode()
              + rightGroupDetailDto.getELE_CODE())).intValue();
            eleCountMap.put(rightGroupDetailDto.getELE_CODE(), new Integer(count + num));
            tempSet.add("-1");
            continue;
          }
          eleCountMap.put(rightGroupDetailDto.getELE_CODE(), new Integer(count + 1));
          tempSet.add(rightGroupDetailDto.getELE_VALUE());
        }
      }
    }
    return handleMap;
  }

  /**
   * 校验科目是否存在
   * 
   * @param dataMap
   * @param id
   * @return 存在返回科目对象,不存在返回null
   */
  private BusVouAccount containsKeyId(Map dataMap, String id) {
    final Iterator iterator = dataMap.keySet().iterator();
    BusVouAccount account = null;
    while (iterator.hasNext()) {
      final Object obj = iterator.next();
      if (obj instanceof BusVouAccount) {
        account = (BusVouAccount) obj;
        if (id.equals(account.getAccountId())) {
          return account;
        }
      }
    }
    return null;
  }

  public boolean validCoa(List acctmdlList) throws IllegalEleLevelOfDownStreamCoaException,
    LackEleOfDownStreamCoaException, IllegalArgumentException {
    List fromList = new ArrayList(acctmdlList.size());
    List toList = new ArrayList(acctmdlList.size());

    // 找出上游科目集合和下游科目集合
    BusVouAcctmdl acctmdl = null;
    BusVouAccount account = null;
    for (int i = 0; i < acctmdlList.size(); i++) {
      acctmdl = ((BusVouAcctmdl) acctmdlList.get(i));
      account = acctmdl.getBusVouAccount();
      // 针对图形化中没有配置科目的acctmdl做的特殊处理
      if (account == null)
        continue;
      int oriented = acctmdl.getEntry_side();
      if (oriented == account.getBalanceSide())
        toList.add(account);
      else
        fromList.add(account);
    }
    BusVouAccount upAccount = null;
    BusVouAccount downAccount = null;
    try {
      // 开始校验
      for (int i = 0; i < fromList.size(); i++) {

        upAccount = (BusVouAccount) fromList.get(i);
        long fromCoa = upAccount.getCoaId();
        boolean test = false;
        for (int j = 0; j < toList.size(); j++) {
          downAccount = (BusVouAccount) toList.get(j);
          long toCoa = downAccount.getCoaId();
          test = coaService.validateDownStreamCoaLevel(fromCoa, toCoa);
          if (!test)
            return false;
        }
      }
    } catch (CoaNotExistsException e) {
      boolean oriented = e.isOriented();
      final StringBuffer buffer = new StringBuffer();
      buffer.append(oriented == true ? "上游科目" : " 下游科目")
        .append(oriented == true ? upAccount.getAccountCode() : downAccount.getAccountCode()).append(":")
        .append(oriented == true ? upAccount.getAccountName() : downAccount.getAccountName()).append("科目尚未挂接coa配置");
      throw new CoaNotExistsException(buffer.toString());
    }
    return true;
  }

  public List allBusVouType() {
    List allBusVouType = bvTypeDao.allBusVouType();
    for (int i = 0; i < allBusVouType.size(); i++) {
      final BusVouType bvType = (BusVouType) allBusVouType.get(i);
      bvType.setBusVouAcctmdl(bvTypeDao.loadVouAcctdml(bvType.getVou_type_id()));
    }
    return allBusVouType;
  }

  public List allBusVouTypeSimple() {
    return bvTypeDao.allBusVouType();
  }

  public List loadVouAcctdml(long vouTypeId) {
    return bvTypeDao.loadVouAcctdml(vouTypeId);
  }

  public List validateData(BusVouType bvType) {
    List messageList = new LinkedList();
    int sumAccount = 0;
    // 校验权限规则
    Iterator iterator = bvType.getBusVouAcctmdl().iterator();
    BusVouAcctmdl acctmdl = null;
    while (iterator.hasNext()) {
      acctmdl = (BusVouAcctmdl) iterator.next();
      if (acctmdl.getBusVouAccount() != null)
        sumAccount++;
    }
    messageList.addAll(validateRule(bvType));
    // 校验coa
    try {
      if (sumAccount > 1)
        validCoa(bvType.getBusVouAcctmdl());
    } catch (CoaNotExistsException e) {
      messageList.add(e.getMessage());
    } catch (IllegalEleLevelOfDownStreamCoaException e) {
      messageList.add(e.getMessage());
    } catch (LackEleOfDownStreamCoaException e) {
      messageList.add(e.getMessage());
    } catch (IllegalArgumentException e) {
      messageList.add("记账模板" + bvType.getVou_type_name() + e.getMessage());
    }
    return messageList;
  }

  public void saveGraphConfig(List bvTypeList, List accountList, byte[] content) throws Exception {
    this.reinstallBvType(bvTypeList);
    accountService.reinstallAccount(accountList);
    bvTypeDao.saveConfigString(content);
  }

  public boolean getIsForceSave() {
    return isForceSave;
  }

  public byte[] loadConfigUIByte() {
    return bvTypeDao.loadConfigString();
  }

  public void reinstallBvType(List bvTypeList) throws Exception {
    bvTypeDao.deleteAllBusVouType();
    batchSaveBusVouType(bvTypeList);
  }

  /**
   * 批量保存记账模板
   * 
   * @param bvTypeList
   */
  private void batchSaveBusVouType(List bvTypeList) throws Exception {
    final Iterator iterator = bvTypeList.iterator();
    while (iterator.hasNext()) {
      saveSetBusVouType((BusVouType) iterator.next());
    }
  }

  public BusVouType loadBusVouTypeByCode(String vouTypeCode) {
    return bvTypeDao.loadBusVouTypeByCode(vouTypeCode);
  }

  /**
   * 重写HashMap中的putAll方法 对要素统计个数ELEMENT_COUNT中存储的对象进行累加
   */
  class EleCountMap extends HashMap {

    private static final long serialVersionUID = 5421515716644779680L;

    public void putAll(Map map) {
      Map.Entry entry;// put(entry.getKey(), entry.getValue())
      for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
        entry = (Map.Entry) iterator.next();
        if (entry.getKey().equals(ELEMENT_COUNT)) {
          Map eleCountMap = (Map) this.get(ELEMENT_COUNT);
          if (eleCountMap == null) {
            eleCountMap = new HashMap();
            put(ELEMENT_COUNT, eleCountMap);
          }
          final Iterator it = ((Map) map.get(ELEMENT_COUNT)).keySet().iterator();
          while (it.hasNext()) {
            final String eleString = (String) it.next();
            final Set tempSet = (Set) ((Map) map.get(ELEMENT_COUNT)).get(eleString);
            if (!eleCountMap.containsKey(eleString)) {
              eleCountMap.put(eleString, tempSet);
            } else
              ((Set) eleCountMap.get(eleString)).addAll(tempSet);
          }
        } else
          put(entry.getKey(), entry.getValue());
      }
    }
  }

  /**
   * 删除所有记账模板
   */
  public void removeAllBvType() {
    bvTypeDao.deleteAllBusVouType();
  }

  public void clearCache() {

  }
}
