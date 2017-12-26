package gov.df.fap.service.gl.coa.impl;

import gov.df.fap.api.gl.coa.BatchCodeCombinationProcesser;
import gov.df.fap.api.gl.coa.CodeCombination;
import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.service.gl.coa.impl.AbstractCcidAccelerator.ConfilctFixCallback;

import java.util.List;
import java.util.Map;

public interface CcidAccelerator {

  public abstract void setCoaService(ICoaService coa);

  public abstract void setCoaDao(CoaDao coaDao);

  /**
   * 取新生成的CCID
   * @return
   */
  public abstract Map getNewCcids();

  public abstract Map getPreCcidGenCache();

  /**
   * 返回已存在的CCID
   * @param coaDto
   * @param ccid
   * @return null表示不存在
   */
  public abstract CodeCombination getCcidExists(FCoaDTO targetCoa, CodeCombination sourceCodeCmb);

  /**
   * 生成CCID.
   * @param targetCoa
   * @param elementsContainer
   * @param setYear
   * @return
   */
  public abstract long generateCcid(FCoaDTO targetCoa, Object elementsContainer, int setYear, boolean misMatch);

  /**
   * 处理冲突,这个方法会去CCID缓存和线程缓存中查询原来有冲突的记录并修改.
   * @param conflictCodeCombinations
   * @param fixCall 修复处理,每修复一个ccid会调用一次该类的fixingCall方法
   */
  public abstract void handleCcidObjectConflict(List conflictCodeCombinations, ConfilctFixCallback fixCall);

  /**
   * 批量生成CCID
   * @param processer 批量处理器
   * @param setYear 年度
   */
  public abstract void generateCcidBatch(BatchCodeCombinationProcesser processer, int setYear);

  /**
   * 读取CCID对象,因为有可能存在于当前线程变量中,所以必须在这里取.
   * @param coa COA对象
   * @param ccid CCID
   * @return CCID对象,是以MAP的形式放置要素组合,里面全是ElementInfo
   */
  public abstract Map getCcidObject(FCoaDTO coa, long ccid);

}