package gov.df.fap.service.gl.coa.impl;

import gov.df.fap.api.gl.coa.CodeCombination;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.service.dictionary.elecache.DefaultCacheFactory;
import gov.df.fap.service.gl.configure.impl.EngineConfiguration;
import gov.df.fap.service.util.dictionary.interfaces.Cache;
import gov.df.fap.util.StringUtil;

import org.springframework.stereotype.Component;

/**
 * CCID生成加速器
 * @author 
 */
@Component("CcidGenAccelerator")
public class CcidGenAccelerator extends AbstractCcidAccelerator implements CcidAccelerator {

  //用于判断是否已在数据库存储CCID且已在加速器中记录.
  protected static Cache ccidGenCache = DefaultCacheFactory.getInstance().getCacheInstance();

  static {
    ccidGenCache.setCacheCapability(ccidCacheSize);
  }

  /**
   * misMatch在此不起作用
   */
  protected Object buildCcidObject(FCoaDTO coa, Object elementsContainer, int setYear, boolean misMatch) {
    return coaService.generateCcidObject(coa, elementsContainer, setYear);
  }

  /**
   * 从数据库查询该CCID是否已生成
   */
  protected CodeCombination getDbCodeCombination(FCoaDTO coa, CodeCombination ccid) {
    return null;
  }

  protected void persistCache() {
    //do nothing
  }

  protected CodeCombination getInputCodeCombination(FCoaDTO coa, Object elementsContainer) {
    return coaService.caculateCcid(coa, elementsContainer);
  }

  public Cache getCache() {
    return ccidGenCache;
  }

  protected void checkGenerateResult(CodeCombination inputCodeCmb, CodeCombination newCodeCmb) {
    if (inputCodeCmb.getCcid() != newCodeCmb.getCcid()
      || !StringUtil.equals(inputCodeCmb.getMd5(), newCodeCmb.getMd5()))
      throw new RuntimeException("生成前后的CCID对象不一致!生成前计算CCID:" + inputCodeCmb.getCcid() + ", MD5:"
        + inputCodeCmb.getMd5() + ", 生成后计算CCID:" + newCodeCmb.getCcid() + ", MD5:" + newCodeCmb.getMd5());
  }

  /**
   * 从全局配置中读取判断,是否允许CCID生成缓存
   */
  protected boolean allowCcidGenCache() {
    return EngineConfiguration.getConfig().isEnableCcidGenCache();
  }

}
