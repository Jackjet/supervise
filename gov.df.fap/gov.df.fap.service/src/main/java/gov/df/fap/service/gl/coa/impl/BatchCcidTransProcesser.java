package gov.df.fap.service.gl.coa.impl;

import gov.df.fap.api.gl.coa.BatchCodeCombinationProcesser;
import gov.df.fap.api.gl.coa.CodeCombination;
import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.gl.GlConstant;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.dto.CtrlRecordDTO;
import gov.df.fap.service.util.gl.coa.CcidUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.number.NumberUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量CCID转换/要素匹配处理器
 * @author
 *
 */
public class BatchCcidTransProcesser implements BatchCodeCombinationProcesser {

  Map codeCombRecord = new HashMap();

  List transItems = new ArrayList();

  ICoaService coaService = null;

  public BatchCcidTransProcesser(ICoaService coaService) {
    this.coaService = coaService;
  }

  public int size() {
    return transItems.size();
  }

  /**
   * 先解析源CCID得出COA后,从源COA物理表中读取源CCID信息才可以进行下一步的匹配.
   */
  public Object getElementContainer(int index) {
    BatchItem item = getItem(index);
    long sourceCoaId = CcidUtil.getCoaIdByCcid(StringUtil.toStr(item.getOriginalCcid()));
    FCoaDTO sourceCoa = coaService.getCoa(sourceCoaId);
    return coaService.getCcidInfoMap(sourceCoa, item.getOriginalCcid());
  }

  /**
   * 这里读取的是要素匹配的目标COA.
   */
  public FCoaDTO getCoa(int index) {
    return getItem(index).getTargetCoa();
  }

  /**
   * 
   */
  public void setCodeCombination(int index, CodeCombination cachedCodeCmb) {
    getItem(index).getCtrlRecordDTO().setCcid(cachedCodeCmb.getCcid());
    if (codeCombRecord.containsKey(cachedCodeCmb)) {
      StringBuffer indexs = (StringBuffer) codeCombRecord.get(cachedCodeCmb);
      indexs.append(GlConstant.COMMA).append(index);
    } else {
      StringBuffer indexs = new StringBuffer();
      indexs.append(index);
      codeCombRecord.put(cachedCodeCmb, indexs);
    }
  }

  public void updateCodeCombinationId(CodeCombination oldOne, CodeCombination newOne) {
    if (codeCombRecord.containsKey(oldOne)) {
      StringBuffer indexs = (StringBuffer) codeCombRecord.get(oldOne);
      String[] indexArray = indexs.toString().split(GlConstant.COMMA);
      for (int i = 0; i < indexArray.length; i++) {
        final int index = NumberUtil.toInt(indexArray[i]);
        getItem(index).getCtrlRecordDTO().setCcid(newOne.getCcid());
      }
      codeCombRecord.put(newOne, indexs);
      codeCombRecord.remove(oldOne);
    } else {
      throw new RuntimeException("在批量处理中未生成过coa=" + oldOne.getCoaId() + ",ccid=" + oldOne.getCcid() + ",md5="
        + oldOne.getMd5() + " 的要素编码组合, 冲突更新失败!");
    }
  }

  public boolean needFuzzyMatch(int index) {
    return getItem(index).needFuzzyMatch;
  }

  private BatchItem getItem(int index) {
    return (BatchItem) transItems.get(index);
  }

  public void addCtrlRecordNeedTrans(FCoaDTO targetCoa, long originalCcid, CtrlRecordDTO ctrlRecord, boolean needFuzzy) {
    transItems.add(new BatchItem(targetCoa, originalCcid, ctrlRecord, needFuzzy));
  }

  /**
   * 批量单元.
   * @author
   *
   */
  class BatchItem {
    private long originalCcid = 0;

    private boolean needFuzzyMatch = false;

    private CtrlRecordDTO ctrlRecordDTO = null;

    private FCoaDTO targetCoa = null;

    BatchItem(FCoaDTO coa, long originalCcid, CtrlRecordDTO objectToSetCcid, boolean needFuzzyMatch) {
      this.originalCcid = originalCcid;
      this.needFuzzyMatch = needFuzzyMatch;
      this.ctrlRecordDTO = objectToSetCcid;
      this.targetCoa = coa;
    }

    public boolean isNeedFuzzyMatch() {
      return needFuzzyMatch;
    }

    public void setNeedFuzzyMatch(boolean needFuzzyMatch) {
      this.needFuzzyMatch = needFuzzyMatch;
    }

    public CtrlRecordDTO getCtrlRecordDTO() {
      return ctrlRecordDTO;
    }

    public void setCtrlRecordDTO(CtrlRecordDTO ctrlRecordDTO) {
      this.ctrlRecordDTO = ctrlRecordDTO;
    }

    public long getOriginalCcid() {
      return originalCcid;
    }

    public void setOriginalCcid(long originalCcid) {
      this.originalCcid = originalCcid;
    }

    public FCoaDTO getTargetCoa() {
      return targetCoa;
    }

    public void setTargetCoa(FCoaDTO targetCoa) {
      this.targetCoa = targetCoa;
    }

  }
}
