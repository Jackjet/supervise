package gov.df.fap.service.gl.coa.impl;

import gov.df.fap.api.dictionary.ElementInfo;
import gov.df.fap.api.gl.coa.CodeCombination;
import gov.df.fap.bean.gl.GlConstant;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.coa.FCoaDetail;
import gov.df.fap.service.dictionary.elecache.DefaultCacheFactory;
import gov.df.fap.service.gl.configure.impl.EngineConfiguration;
import gov.df.fap.service.util.dictionary.interfaces.Cache;
import gov.df.fap.service.util.gl.coa.SimpleCodeCombination;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.Properties.PropertiesUtil;
import gov.df.fap.util.number.NumberUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * CCDID转换加速器
 * @author 
 *
 */
@Component("CcidTransAccelerator")
public class CcidTransAccelerator extends AbstractCcidAccelerator implements CcidAccelerator {

  private static final Log log = LogFactory.getLog(CcidTransAccelerator.class);

  //用于判断是否已在数据库存储CCID且已在加速器中记录.
  protected static Cache ccidGenCache = DefaultCacheFactory.getInstance().getCacheInstance();

  static {
    ccidGenCache.setCacheCapability(ccidCacheSize);
  }

  /**
   * 将传入的要素根据COA指定级次进行转换.
   */
  protected Object buildCcidObject(FCoaDTO coa, Object elementsContainer, int setYear, boolean misMatch) {
    Object objectTrans = coaService.doPreCcidTrans(coa, elementsContainer, setYear);

    //转换后判断是否存在,如存在就不插入了,直接返回
    if (misMatch && coa.hasAutoLevelElement() && EngineConfiguration.getConfig().isAllowElementFuzzyMatch()) {
      /*
       * TODO 此处存在一个BUG,有可能因为冲突而匹配错额度,但目前解决起来比较复杂,
       * 考虑到匹配错也只影响金额,以后可以将问题追溯出来,暂时不处理这个BUG.
       */
      Object fuzzyMatchResult = ccidFuzzyMatch(coa, objectTrans);
      if (fuzzyMatchResult != null)
        return fuzzyMatchResult;
    }

    return objectTrans;
  }

  /**
   * 进行要素匹配,匹配出多个CCID
   * @param objectTrans 需要匹配的原始CCID对象
   * @param misMatch 是否进行模糊匹配
   * @return CCID列表,也就是匹配结果,查询时将会按任意级次的顺序排序
   */
  private Object ccidFuzzyMatch(FCoaDTO coa, Object objectTrans) {
    List ccidObjects = new LinkedList();
    ccidObjects.add(objectTrans);
    ElementInfo elementInfo = null;
    for (int i = 0; i < coa.size(); i++) {
      final FCoaDetail coaDetail = coa.get(i);
      elementInfo = (ElementInfo) PropertiesUtil.getProperty(objectTrans, coaDetail.getEleCode());
      if (elementInfo != null)
        addElementInfo(ccidObjects, coaDetail, elementInfo);
      if (elementInfo != null && coaDetail.getLevelNum() == -2) {
        ccidObjects = branchCcidTransObjects(ccidObjects, coaDetail, elementInfo);
      }
    }

    Iterator it = ccidObjects.iterator();
    while (it.hasNext()) {
      final Object ccidObject = it.next();
      final CodeCombination codeCmb = coaService.caculateCcidWithElementInfo(coa, ccidObject);
      PropertiesUtil.setProperty(ccidObject, GlConstant.CCID_KEY, new Long(codeCmb.getCcid()));
      PropertiesUtil.setProperty(ccidObject, GlConstant.MD5_KEY, codeCmb.getMd5());
      PropertiesUtil.setProperty(ccidObject, GlConstant.COA_ID_KEY, codeCmb.getCoaId());

    }
    //有可能返回空值
    List fuzzyQueryResult = coaDao.fuzzyQueryCcid(coa, ccidObjects);
    CodeCombination fuzzyMatchOne = null;

    if (fuzzyQueryResult.size() > 1)
      log.info("要素模糊匹配出多个要素组合(CCID),有可能导致错误数据!");

    if (fuzzyQueryResult.size() > 0) {
      fuzzyMatchOne = (CodeCombination) fuzzyQueryResult.get(0);
      it = ccidObjects.iterator();
      while (it.hasNext()) {
        final Object ccidObject = it.next();
        final long ccid = ((Long) PropertiesUtil.getProperty(ccidObject, GlConstant.CCID_KEY)).longValue();
        final String md5 = (String) PropertiesUtil.getProperty(ccidObject, GlConstant.MD5_KEY);
        if (ccid == fuzzyMatchOne.getCcid() && StringUtil.equals(md5, fuzzyMatchOne.getMd5()))
          return ccidObject;
      }
    }
    //匹配不出或者有HASH冲突,返回空.
    return null;
  }

  /**
   * 单纯的将一个要素加入CCID对象
   * @param ccidTransObjects CCID对象列表
   * @param elementInfo 要加入的要素,可以为空值
   */
  private void addElementInfo(List ccidTransObjects, FCoaDetail coaDetail, ElementInfo elementInfo) {
    for (int i = 0; i < ccidTransObjects.size(); i++) {
      final Object ccidTransObject = ccidTransObjects.get(i);
      PropertiesUtil.setProperty(ccidTransObject, coaDetail.getEleCode(), elementInfo);
    }
  }

  /**
   * 将匹配出来的CCID对象"分叉",因为进行模糊匹配的话,上下游的要素都得进行查询,所以原来构建的CCID对象
   * 会由一个分裂出来多个.
   * @param ccidTransObjects CCID对象列表
   * @param elementInfo 要加入的要素,且为任意级次
   * @return
   */
  private List branchCcidTransObjects(List ccidTransObjects, FCoaDetail coaDetail, ElementInfo elementInfo) {
    List linkedList = new LinkedList();
    ElementInfo elementInfoPut = null;
    for (int i = 0; i < ccidTransObjects.size(); i++) {
      elementInfoPut = elementInfo;
      final HashMap ccidTransObject = (HashMap) ccidTransObjects.get(i);
      while (elementInfoPut != null && !elementInfoPut.isRoot()) {
        final HashMap newTransObject = (HashMap) ccidTransObject.clone();
        PropertiesUtil.setProperty(newTransObject, coaDetail.getEleCode(), elementInfoPut);
        linkedList.add(newTransObject);
        elementInfoPut = elementInfoPut.getParent();
      }
    }
    return linkedList;
  }

  /**
   * 判断该CCID是否已经转换为对应COA的CCID.
   * @param coa 转换的目标COA对象
   * @param ccid 转换前的CCID
   */
  protected CodeCombination getDbCodeCombination(FCoaDTO targetCoa, CodeCombination sourceCodeCmb) {
    /*
     * 因为CCID转换时CCID是外部传入,且已经是读取了一个已存在的CCID,所以此处不用再读取源CCID,
     * 直接判断目标COA与源COA是否一致,加速返回.
     * TODO 但这方方法是从数据库读取,这个加速逻辑放这里不是很合适,以后待重构
     */
    if (StringUtil.equals(targetCoa.getCoaId(), sourceCodeCmb.getCoaId()))
      return sourceCodeCmb;

    long quickQueryCcid = coaDao.quickQueryCcid(targetCoa, sourceCodeCmb.getCcid());
    if (quickQueryCcid != CodeCombination.CCID_NULL) {
      //特殊处理,不存放MD5值
      return new SimpleCodeCombination(quickQueryCcid, targetCoa.getCoaId(), null);
    }
    //不存匹配出要素后再次查询
    return null;
  }

  /**
   * 缓存CCID转换信息到gl_ccid_trans表,以便在下次查询的时候加速
   */
  protected void persistCache() {
    Map tempMap = (Map) preCcidGenCache.get();
    Iterator it = tempMap.entrySet().iterator();
    List ccidTransCache = new LinkedList();
    while (it.hasNext()) {
      final Entry entry = (Entry) it.next();
      final Map cache = new HashMap();
      final CcidCacheKey key = (CcidCacheKey) entry.getKey();
      ccidGenCache.addCacheObject(entry.getKey(), entry.getValue());
      cache.put("coa_id", ((CodeCombination) entry.getValue()).getCoaId());
      cache.put("source_ccid", key.getCcid());
      cache.put("target_ccid", StringUtil.toStr(((CodeCombination) entry.getValue()).getCcid()));
      ccidTransCache.add(cache);
    }
    coaDao.saveCcidTransCache(ccidTransCache);
  }

  /**
   * 取得需要进行要素匹配的要素所生成CCID,因为这个要素容器是已读取了数据的,所以直接从里面取ccid,md5值,coa_id.
   * 这些值都是正确的.
   * 针对传入的Container没有三种键时,返回空值
   */
  protected CodeCombination getInputCodeCombination(FCoaDTO targetCoa, Object elementsContainer) {
    try {
      long inputCcid = NumberUtil.toLong(PropertiesUtil.getProperty(elementsContainer, GlConstant.CCID_KEY).toString());
      String sourceCoaId = (String) PropertiesUtil.getProperty(elementsContainer, GlConstant.COA_ID_KEY).toString();
      String inputMd5 = (String) PropertiesUtil.getProperty(elementsContainer, GlConstant.MD5_KEY);
      return new SimpleCodeCombination(inputCcid, sourceCoaId, inputMd5);
    } catch (Exception ex) {
      return null;
    }
  }

  public Cache getCache() {
    return ccidGenCache;
  }

  protected void checkGenerateResult(CodeCombination inputCodeCmb, CodeCombination newCodeCmb) {
    //do nothing
  }

  /**
   * 从总账引擎全局配置中读取是否允许CCID转换缓存
   */
  protected boolean allowCcidGenCache() {
    return EngineConfiguration.getConfig().isEnableCcidTransCache();
  }
}
