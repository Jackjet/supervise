package gov.df.fap.service.gl.configure.impl;

import gov.df.fap.api.gl.configure.AccountService;
import gov.df.fap.bean.gl.configure.BusVouAcctmdl;
import gov.df.fap.bean.gl.configure.BusVouType;
import gov.df.fap.bean.gl.configure.CommonKey;
import gov.df.fap.service.dictionary.elecache.DefaultCacheFactory;
import gov.df.fap.service.util.dictionary.interfaces.Cache;
import gov.df.fap.service.util.dictionary.interfaces.CacheObjectCloner;
import gov.df.fap.service.util.exceptions.gl.ExistOnWayDataOfBusVouException;
import gov.df.fap.service.util.gl.configure.IBusVouTypeService;
import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.service.util.memcache.MemCacheMap;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.Properties.PropertiesUtil;
import gov.df.fap.util.exception.IllegalEleLevelOfDownStreamCoaException;
import gov.df.fap.util.exception.LackEleOfDownStreamCoaException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 记账模板维护缓存代理类.
 * 
 * @author justin
 * @version 
 */
@Service("busVouTypeServiceWrapper")
public class BusVouTypeServiceWrapper implements IBusVouTypeService {

  @Autowired
  @Qualifier("busVouTypeServiceImpl")
  private IBusVouTypeService bvTypeServiceImpl = null;

  @Autowired
  @Qualifier("accountServiceWrapper")
  private AccountService accountService = null;

  private Cache cache = DefaultCacheFactory.getInstance().getCacheInstance();

  private static Map billtypeMap = MemCacheMap.getCacheMap(BusVouTypeServiceWrapper.class);

  public void setBvTypeServiceImpl(BusVouTypeServiceImpl bvTypeServiceImpl) {
    this.bvTypeServiceImpl = bvTypeServiceImpl;
  }

  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }

  public void saveSetBusVouType(BusVouType busVouType) throws IllegalEleLevelOfDownStreamCoaException,
    LackEleOfDownStreamCoaException {
    bvTypeServiceImpl.saveSetBusVouType(busVouType);
    cacheBvType(busVouType);
  }

  public void updateSetBusVouType(BusVouType busVouType) throws IllegalEleLevelOfDownStreamCoaException,
    LackEleOfDownStreamCoaException, ExistOnWayDataOfBusVouException, SQLException {
    bvTypeServiceImpl.updateSetBusVouType(busVouType);
    cacheBvType(busVouType);
  }

  public void updateSetBusVouTypeQuiet(BusVouType busVouType) throws SQLException {
    bvTypeServiceImpl.updateSetBusVouTypeQuiet(busVouType);
    cacheBvType(busVouType);
  }

  public void deleteVouType(BusVouType busVouType) throws ExistOnWayDataOfBusVouException {
    bvTypeServiceImpl.deleteVouType(busVouType);
    cache.removeCacheObject(new CommonKey(String.valueOf(busVouType.getVou_type_id()), String.valueOf(busVouType
      .getSet_year()), busVouType.getRg_code()));
    // 暂时先清理所有交易凭证关联缓存
    billtypeMap.clear();
  }

  public BusVouType loadVouTypeByBill(String billTypeCode) {
    if (StringUtil.isEmpty(billTypeCode))
      return null;
    BusVouType returnType = null;
    Object key = new CommonKey(billTypeCode, CommonUtil.getSetYear(), CommonUtil.getRgCode());
    if (billtypeMap.containsKey(key)) {
      returnType = (BusVouType) billtypeMap.get(key);
    } else {
      long bvId = bvTypeServiceImpl.getVouTypeIdByBill(billTypeCode);
      returnType = loadBusVouType(bvId);
      if (returnType != null)
        billtypeMap.put(key, returnType);
    }
    return returnType;
  }

  public List loadVouAcctdml(long vouTypeId) {
    return bvTypeServiceImpl.loadVouAcctdml(vouTypeId);
  }

  public void clearCache() {
    cache.clear();
    billtypeMap.clear();
  }

  public BusVouType loadBusVouType(long vouTypeId) {
    Object key = new CommonKey(String.valueOf(vouTypeId), CommonUtil.getSetYear(), CommonUtil.getRgCode());
    BusVouType bvType = (BusVouType) cache.getCacheObject(key);
    if (bvType == null) {
      bvType = bvTypeServiceImpl.loadBusVouType(vouTypeId);
      if (bvType != null) {
        bvType.setBusVouAcctmdl(bvTypeServiceImpl.loadVouAcctdml(bvType.getVou_type_id()));
        cacheBvType(bvType);
      }
    }
    return bvType;
  }

  public List allBusVouType() {
    // 清除缓存,重新加载
    billtypeMap.clear();

    List allBvType = bvTypeServiceImpl.allBusVouTypeSimple();
    List returnList = new LinkedList();

    for (int i = 0; i < allBvType.size(); i++) {
      final BusVouType bvType = (BusVouType) allBvType.get(i);
      returnList.add(loadBusVouType(bvType.getVou_type_id()));
      Collections.sort(returnList, new Comparator() {
        public int compare(Object o1, Object o2) {
          final BusVouType bvType1 = (BusVouType) o1;
          final BusVouType bvType2 = (BusVouType) o2;
          return bvType1.getVou_type_code().compareTo(bvType2.getVou_type_code());
        }
      });
    }
    return returnList;
  }

  public boolean validCoa(List acctmdlList) throws IllegalEleLevelOfDownStreamCoaException,
    LackEleOfDownStreamCoaException, IllegalArgumentException {
    return bvTypeServiceImpl.validCoa(acctmdlList);
  }

  public List validateRule(BusVouType bvType) {
    return bvTypeServiceImpl.validateRule(bvType);
  }

  public List validateData(BusVouType bvType) {
    return bvTypeServiceImpl.validateData(bvType);
  }

  public void saveGraphConfig(List bvTypeList, List accountList, byte[] content) throws Exception {
    bvTypeServiceImpl.saveGraphConfig(bvTypeList, accountList, content);
    clearCache();
    allBusVouType();
  }

  public byte[] loadConfigUIByte() {
    return bvTypeServiceImpl.loadConfigUIByte();
  }

  /**
   * 填充记账模板的科目信息
   * 
   * @param bvType
   */
  private void fillAccount(BusVouType bvType) {
    List acctmdls = bvType.getBusVouAcctmdl();
    // 读取所有科目对象,再缓存
    for (int i = 0; i < acctmdls.size(); i++) {
      final BusVouAcctmdl acctmdl = (BusVouAcctmdl) acctmdls.get(i);
      acctmdl.setBusVouAccount(accountService.loadBusVouAccount(acctmdl.getAccount_id()));
    }
  }

  /**
   * 缓存一个记账模板
   * 
   * @param bvType
   * @return
   */
  private BusVouType cacheBvType(BusVouType bvType) {
    fillAccount(bvType);
    Object key = bvType.getKey();
    BusVouType returnType = (BusVouType) cache.getCacheObject(key);// new
    if (returnType == null) {// 新增
      resetAcctmdl(bvType);

      cache.addCacheObject(key, bvType);// new
      returnType = bvType;
    } else {// 更新
      try {
        PropertiesUtil.copyProperties(returnType, bvType);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    // 暂时先清理所有交易凭证关联缓存
    billtypeMap.clear();
    return returnType;
  }

  // 为了解决 支付年结时产生的额度fromctrlid为空。
  // 年结交易令上配置了 生成同时扣减额度的两条线，先生成下游额度后生成上游额度，导致下游额度id的fromctrlid为空
  // 在这里将上游科目的关联关系放到上面
  public static void resetAcctmdl(BusVouType bvType) {
    List acctmdlList = bvType.getBusVouAcctmdl();
    List newAcctmdlList = new ArrayList();
    newAcctmdlList.addAll(acctmdlList);
    BusVouAcctmdl tmpAcctmdl = null;
    int upAcctmdl = 0;
    int downAcctmdl = acctmdlList.size() - 1;
    for (int i = 0; i < acctmdlList.size(); i++) {
      tmpAcctmdl = (BusVouAcctmdl) acctmdlList.get(i);
      if (tmpAcctmdl.isPlusAccountBalance())
        newAcctmdlList.set(downAcctmdl--, tmpAcctmdl);
      else
        newAcctmdlList.set(upAcctmdl++, tmpAcctmdl);
    }
    bvType.setBusVouAcctmdl(newAcctmdlList);
  }

  class BusVouCloner implements CacheObjectCloner {

    public Object clone(Object beCloned) {
      try {
        return ((BusVouType) beCloned).clone();
      } catch (CloneNotSupportedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void reinstallBvType(List bvTypeList) throws Exception {
    final Iterator iterator = allBusVouType().iterator();
    Iterator tempIt = null;
    BusVouType bvType = null;
    BusVouType tempBvType = null;
    while (iterator.hasNext()) {
      bvType = (BusVouType) iterator.next();
      tempIt = bvTypeList.iterator();
      while (tempIt.hasNext()) {
        tempBvType = (BusVouType) tempIt.next();
        if (bvType.getVou_type_code().equals(tempBvType.getVou_type_code())) {
          tempBvType.setVou_type_id(bvType.getVou_type_id());
          break;
        }
      }
    }
    bvTypeServiceImpl.reinstallBvType(bvTypeList);
    billtypeMap.clear();
    cache.clear();
  }

  public BusVouType loadBusVouTypeByCode(String vouTypeCode) {
    return bvTypeServiceImpl.loadBusVouTypeByCode(vouTypeCode);
  }

  public void removeAllBvType() {
    bvTypeServiceImpl.removeAllBvType();
  }

  @Override
  public long getVouTypeIdByBill(String billtypeCode) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public List allBusVouTypeSimple() {
    // TODO Auto-generated method stub
    return null;
  }
}
