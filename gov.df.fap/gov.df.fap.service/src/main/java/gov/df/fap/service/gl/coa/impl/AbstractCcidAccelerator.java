package gov.df.fap.service.gl.coa.impl;

import gov.df.fap.api.gl.coa.BatchCodeCombinationProcesser;
import gov.df.fap.api.gl.coa.CodeCombination;
import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.gl.GlConstant;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.service.util.dictionary.interfaces.Cache;
import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.Properties.PropertiesUtil;
import gov.df.fap.util.number.NumberUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * CCID生成加速器,用于CCID批量生成.
 * @author
 * @version 
 */
public abstract class AbstractCcidAccelerator implements CcidAccelerator {

  protected static final Log logger = LogFactory.getLog(AbstractCcidAccelerator.class);

  protected static int ccidCacheSize = 20000;

  @Autowired
  protected CoaDao coaDao = null;

  @Autowired
  protected ICoaService coaService = null;

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CcidAccelerator#setCoaService(gov.gfmis.fap.dictionary.coa.CoaService)
   */
  public void setCoaService(ICoaService coa) {
    this.coaService = coa;
  }

  //	/**一个事务线程中涉及要素*/
  //	protected static final ThreadLocal elements = new ThreadLocal();
  /**一个事务线程中CCID生成缓存*/
  protected static final ThreadLocal preCcidGenCache = new ThreadLocal();

  /**一个事务线程中生成的新的CCID*/
  protected static final ThreadLocal newCcids = new ThreadLocal();

  public AbstractCcidAccelerator() {

  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CcidAccelerator#setCoaDao(gov.gfmis.fap.dictionary.coa.CoaDao)
   */
  public void setCoaDao(CoaDao coaDao) {
    this.coaDao = coaDao;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CcidAccelerator#getNewCcids()
   */
  public Map getNewCcids() {
    if (newCcids.get() == null)
      newCcids.set(new HashMap());
    return (Map) newCcids.get();
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CcidAccelerator#getPreCcidGenCache()
   */
  public Map getPreCcidGenCache() {
    if (preCcidGenCache.get() == null)
      preCcidGenCache.set(new HashMap());
    return (Map) preCcidGenCache.get();
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CcidAccelerator#getCcidExists(gov.gfmis.fap.dictionary.coa.FCoaDTO, gov.gfmis.fap.dictionary.coa.CodeCombination)
   */
  public CodeCombination getCcidExists(FCoaDTO targetCoa, CodeCombination sourceCodeCmb) {

    if (sourceCodeCmb == null)
      return null;

    //从缓存读取
    Object cacheKey = ccidCacheKey(targetCoa.getCoaId(), sourceCodeCmb.getCcid(), sourceCodeCmb.getMd5());
    CodeCombination ccidInCache = getCachedCodeCombination(cacheKey);
    if (ccidInCache != null) {
      if (logger.isDebugEnabled())
        logger.debug("从CCID生成缓存发现已生成CCID:" + ccidInCache.getCcid() + ",直接返回");
      return ccidInCache;
    }
    //从数据库读取
    CodeCombination ccidInDb = getDbCodeCombination(targetCoa, sourceCodeCmb);
    if (ccidInDb != null) {
      cacheCodeCombination(cacheKey, ccidInDb);
      return ccidInDb;
    }
    //不存在,返回空值
    return null;
  }

  //	protected void preCacheCodeCombination(CodeCombination sourceCodeCmb, CodeCombination targetCodeCmb){
  //		getPreCcidGenCache().put(ccidCacheKey(sourceCodeCmb.getCoaId(), sourceCodeCmb.getCcid(), sourceCodeCmb.getMd5()), targetCodeCmb);
  //	}

  /**
   * 缓存要素生成
   * @param sourceCodeCmb
   * @param targetCodeCmb
   */
  protected void cacheCodeCombination(CodeCombination sourceCodeCmb, CodeCombination targetCodeCmb) {
    cacheCodeCombination(ccidCacheKey(sourceCodeCmb.getCoaId(), sourceCodeCmb.getCcid(), sourceCodeCmb.getMd5()),
      targetCodeCmb);
  }

  /**
   * 缓存要素生成
   * @param cacheKey
   * @param targetCcid
   */
  protected void cacheCodeCombination(Object cacheKey, CodeCombination targetCcid) {
    getCache().addCacheObject(cacheKey, targetCcid);
  }

  /**
   * 删除要素生成缓存 
   * @param sourceCodeCmb
   */
  protected void removeCodeCombinationCache(CodeCombination sourceCodeCmb) {
    getCache().removeCacheObject(
      ccidCacheKey(sourceCodeCmb.getCoaId(), sourceCodeCmb.getCcid(), sourceCodeCmb.getMd5()));
  }

  private CodeCombination getCachedCodeCombination(Object cacheKey) {
    CodeCombination ccidExists = null;
    if (allowCcidGenCache())
      ccidExists = (CodeCombination) getCache().getCacheObject(cacheKey);
    if (ccidExists != null)
      return ccidExists;
    ccidExists = (CodeCombination) getPreCcidGenCache().get(cacheKey);
    if (ccidExists != null)
      return ccidExists;
    return null;
  }

  /**
   * CCID缓存所使用KEY
   * @param codeCmb TODO
   * @param newCcid
   * @return
   */
  protected Object ccidCacheKey(String coaId, long ccid, String md5) {
    return new CcidCacheKey(coaId, new Long(ccid), md5);
  }

  /**
   * CCID缓存所使用KEY,传入COAID,在要素时候传入COAID会是目标COAID.
   * @param codeCmb TODO
   * @param newCcid
   * @return
   */
  private Object ccidCacheKey(FCoaDTO targetCoa, CodeCombination codeCmb) {
    return new CcidCacheKey(targetCoa.getCoaId(), new Long(codeCmb.getCcid()), codeCmb.getMd5());
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CcidAccelerator#generateCcid(gov.gfmis.fap.dictionary.coa.FCoaDTO, java.lang.Object, int, boolean)
   */
  public long generateCcid(FCoaDTO targetCoa, Object elementsContainer, int setYear, boolean misMatch) {
    try {
      clearTmpCache();
      CodeCombination inputCodeCmb = getInputCodeCombination(targetCoa, elementsContainer);
      CodeCombination existsCodeCmb = getCcidExists(targetCoa, inputCodeCmb);
      if (existsCodeCmb != null) {
        return existsCodeCmb.getCcid();
      }

      //appendCoa(targetCoa);
      final Object newCcidObject = buildCcidObject(targetCoa, elementsContainer, setYear, misMatch);
      final CodeCombination newCodeCmb = coaService.caculateCcidWithElementInfo(targetCoa, newCcidObject);
      checkGenerateResult(inputCodeCmb, newCodeCmb);
      //查询数据库,使用ccid与md5值一起查,如有冲突,会在插入时报错,到时再处理.
      Map map = coaDao.findCcid(targetCoa, newCodeCmb);
      boolean excep = true;
      //单条处理直接插入
      if (map == null) {
        //TODO 插入不成功要处理冲突
        while (excep) {
          try {
            coaDao.insertCcid(targetCoa, newCcidObject, setYear);
            if (logger.isDebugEnabled())
              logger.debug("插入单条ccid");

            excep = false;
          } catch (CodeCombinationConflictException ex) {//有可能是由于主键冲突引起的异常.
            if (logger.isDebugEnabled())
              logger.debug("发现CCID冲突");

            List conflictCodeCombinations = ex.getConflictCodeCombinations();
            handleCcidObjectConflict(conflictCodeCombinations, new ConfilctFixCallback() {
              public void fixingCall(CodeCombination conflict, CodeCombination currect) {
                if (logger.isDebugEnabled())
                  logger.debug("修复ccid,错误ccid:" + conflict.getCcid() + ",正确ccid:" + currect.getCcid());

                PropertiesUtil.setProperty(newCcidObject, GlConstant.CCID_KEY, StringUtil.toStr(currect.getCcid()));
                PropertiesUtil.setProperty(newCcidObject, GlConstant.MD5_KEY, currect.getMd5());
                // 此处要返回修正过的codeCmb
                newCodeCmb.setCcid(currect.getCcid());
                newCodeCmb.setMd5(currect.getMd5());
              }
            });
          }
        }
        //插入缓存
        if (inputCodeCmb != null)
          cacheCodeCombination(inputCodeCmb, newCodeCmb);
      }

      return newCodeCmb.getCcid();
    } finally {
      clearTmpCache();
    }
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CcidAccelerator#handleCcidObjectConflict(java.util.List, gov.gfmis.fap.dictionary.coa.AbstractCcidAccelerator.ConfilctFixCallback)
   */
  public void handleCcidObjectConflict(List conflictCodeCombinations, ConfilctFixCallback fixCall) {

    getCache().clear();
    getPreCcidGenCache().clear();
    for (int i = 0; i < conflictCodeCombinations.size(); i++) {
      //conflict列表中是CodeCombination对象
      final CodeCombination confilctOne = (CodeCombination) conflictCodeCombinations.get(i);
      final FCoaDTO coa = coaService.getCoa(NumberUtil.toLong(confilctOne.getCoaId()));
      final CodeCombination beCurrectOne = coaService.fixCodeCombinationConflict(coa, confilctOne);
      //修正CCID对象中的CCID值
      fixCall.fixingCall(confilctOne, beCurrectOne);
    }

  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CcidAccelerator#generateCcidBatch(gov.gfmis.fap.dictionary.coa.BatchCodeCombinationProcesser, int)
   */
  public void generateCcidBatch(final BatchCodeCombinationProcesser processer, int setYear) {
    //先清除所有的生成缓存.
    long startTime = System.currentTimeMillis();
    if (logger.isDebugEnabled()) {
      System.out.println("批量生成/转换CCID开始,时间戳:" + startTime);
    }
    try {
      clearTmpCache();
      List coas = new ArrayList();
      //processer包括存在、录入额度，循环处理
      for (int i = 0; i < processer.size(); i++) {
        //CCID
        final Object elementsContainer = processer.getElementContainer(i);
        //COA
        final FCoaDTO targetCoa = processer.getCoa(i);
        //明细CCID对象ccid、coaid、md5
        CodeCombination sourceCodeCmb = getInputCodeCombination(targetCoa, elementsContainer);
        //根据明细ccid生成目标CCID
        CodeCombination existsCodeCmb = getCcidExists(targetCoa, sourceCodeCmb);
        if (existsCodeCmb != null) {
          processer.setCodeCombination(i, existsCodeCmb);
          continue;
        }

        Object newCcidObject = buildCcidObject(targetCoa, elementsContainer, setYear, processer.needFuzzyMatch(i));
        CodeCombination newCodeCmb = coaService.caculateCcidWithElementInfo(targetCoa, newCcidObject);
        checkGenerateResult(sourceCodeCmb, newCodeCmb);

        //未生成同样数据
        if (!getNewCcids().containsKey(newCodeCmb)) {
          getNewCcids().put(newCodeCmb, newCcidObject);
          coas.add(targetCoa);
          //暂时没发现问题,与数据库要素组合的冲突校验放到后面作批量处理,先设置要素组合ID.
          //				processer.setCodeCombination(i, newCodeCmb);
          //同时在线程中缓存已生成的CCID,出现冲突后再覆盖缓存
          getPreCcidGenCache().put(ccidCacheKey(targetCoa, sourceCodeCmb), newCodeCmb);
        }
        //			暂时没发现问题,与数据库要素组合的冲突校验放到后面作批量处理,先设置要素组合ID.
        processer.setCodeCombination(i, newCodeCmb);
      }

      List ccidObjects = new ArrayList();
      ccidObjects.addAll(getNewCcids().values());
      boolean excep = true;
      while (excep) {
        try {
          coaDao.insertCcidBatch(coas, ccidObjects);
          if (logger.isDebugEnabled())
            logger.debug("批量插入ccid " + ccidObjects.size() + "条");

          excep = false;
        } catch (CodeCombinationConflictException ex) {
          //主键冲突引起的异常.
          List conflictCodeCombinations = ex.getConflictCodeCombinations();
          handleCcidObjectConflict(conflictCodeCombinations, new ConfilctFixCallback() {
            public void fixingCall(CodeCombination conflict, CodeCombination currect) {
              if (logger.isDebugEnabled())
                logger.debug("修复ccid,错误ccid:" + conflict.getCcid() + ",正确ccid:" + currect.getCcid());

              final Object ccidObject = getNewCcids().get(conflict);
              PropertiesUtil.setProperty(ccidObject, GlConstant.CCID_KEY, StringUtil.toStr(currect.getCcid()));
              PropertiesUtil.setProperty(ccidObject, GlConstant.MD5_KEY, currect.getMd5());
              // 此处要返回修正过的codeCmb
              processer.updateCodeCombinationId(conflict, currect);
              // 更新缓存主键为current
              getNewCcids().put(currect, ccidObject);
              getNewCcids().remove(conflict);
            }
          });
        }
      }
      commit();
    } finally {
      clearTmpCache();
    }
    if (logger.isDebugEnabled()) {
      System.out.println("批量生成/转换CCID结束,时间戳:" + startTime + ", 耗时:" + (System.currentTimeMillis() - startTime) + "毫秒");
    }
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.coa.CcidAccelerator#getCcidObject(gov.gfmis.fap.dictionary.coa.FCoaDTO, long)
   */
  public Map getCcidObject(FCoaDTO coa, long ccid) {
    Set set = getNewCcids().keySet();
    Iterator it = set.iterator();
    while (it.hasNext()) {
      final CodeCombination key = (CodeCombination) it.next();
      if (key.getCcid() == ccid)
        return (Map) getNewCcids().get(key);
    }
    return null;
  }

  /**
   * 由传入的要素容器,生成对应的要素组合对象.
   * @param coa
   * @param elementsContainer
   * @return 要素组合对象CodeCombination
   */
  protected abstract CodeCombination getInputCodeCombination(FCoaDTO coa, Object elementsContainer);

  /**
   * 校验要素信息或者转换要素（要素匹配）,同时填充要素的CODE与NAME
   * @param coa COA对象
   * @param elementsContainer 包含生成/转换CCID所需的要素
   * @param setYear 年度
   * @param 是否模糊匹配
   * @return 根据指定COA生成的要素容器.
   */
  protected abstract Object buildCcidObject(FCoaDTO coa, Object elementsContainer, int setYear, boolean misMatch);

  /**
   * 判断CCID是否已持久化
   * @param coa COA对象
   * @param ccid CCID对象
   * @return CCID是否已持久化
   */
  protected abstract CodeCombination getDbCodeCombination(FCoaDTO coa, CodeCombination sourceCodeCmb);

  /**
   * 在提交后保存生成缓存,主要用于ccid转换
   */
  protected abstract void persistCache();

  /**
   * 读取要素
   * @return
   */
  protected abstract Cache getCache();

  /**
   * 较难生成前后数据的正确性.
   * @param inputCodeCmb 转入的要素容器生成的对象
   * @param newCodeCmb 生成/要素匹配后的对象
   */
  protected abstract void checkGenerateResult(CodeCombination inputCodeCmb, CodeCombination newCodeCmb);

  /**允许CCID生成缓存*/
  protected abstract boolean allowCcidGenCache();

  protected void commit() {
    //正式缓存已存在CCID或者转换后的CCID
    Map tempMap = (Map) preCcidGenCache.get();
    Iterator it = tempMap.entrySet().iterator();
    while (it.hasNext()) {
      final Entry entry = (Entry) it.next();
      getCache().addCacheObject(entry.getKey(), entry.getValue());
    }
    if (logger.isDebugEnabled())
      logger.debug("缓存CCID生成");

    persistCache();
  }

  protected void clearTmpCache() {
    if (logger.isDebugEnabled())
      logger.debug("清除CCID生成临时缓存");

    getNewCcids().clear();
    getPreCcidGenCache().clear();
  }

  //----------------inner class

  interface ConfilctFixCallback {

    /**
     * 修复
     * @param conflict 有冲突的
     * @param currect 已修复的.
     */
    public void fixingCall(CodeCombination conflict, CodeCombination currect);

  }

  /**
   * CCID生成缓存KEY，支持多年度CCID缓存。
   * @author 
   * @since 6.2.61.04
   *
   */
  protected class CcidCacheKey {
    String coaId = null;

    Long ccid = null;

    String md5 = null;

    String setYear = null;

    String rgCode = null;

    CcidCacheKey(String coaId, Long ccid, String md5) {
      if (coaId == null)
        throw new RuntimeException("COA ID can not null in class CcidCacheKey!");
      if (ccid == null)
        throw new RuntimeException("CCID can not null in class CcidCacheKey!");
      if (md5 == null)
        throw new RuntimeException("MD5 can ont null!");
      this.coaId = coaId;
      this.ccid = ccid;
      this.md5 = md5;

      //加入多年度缓存支持.
      setYear = CommonUtil.getSetYear();
      rgCode = CommonUtil.getRgCode();
    }

    public String getCoaId() {
      return coaId;
    }

    public Long getCcid() {
      return ccid;
    }

    public String getMd5() {
      return md5;
    }

    public boolean equals(Object key) {
      if (this == key)
        return true;

      if (key instanceof CcidCacheKey) {
        CcidCacheKey t = (CcidCacheKey) key;
        return t.coaId.equals(coaId) && t.ccid.equals(ccid) && t.md5.equals(md5) && t.setYear.equals(setYear)
          && t.rgCode.equals(rgCode);
      }
      return false;
    }

    public int hashCode() {
      return (coaId + ccid + md5 + setYear + rgCode).hashCode();
    }
  }
}
