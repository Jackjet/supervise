package gov.df.fap.service.gl.coa.impl;

import gov.df.fap.api.gl.coa.BatchCodeCombinationProcesser;
import gov.df.fap.api.gl.coa.CodeCombination;
import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.gl.GlConstant;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.Properties.PropertiesUtil;
import gov.df.fap.util.number.NumberUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量CCID生成处理器
 * @author 
 *
 */
public class BatchCcidGenProcesser implements BatchCodeCombinationProcesser {

  Map codeCombRecord = new HashMap();

  List beanList = null;

  List elementsList = null;

  ICoaService coaService = null;

  /**
   * CCID生成处理器构造方法,传入的两个List必须是有序的.
   * @param beanList
   * @param elementsList
   * @param coaService
   */
  public BatchCcidGenProcesser(List beanList, List elementsList, ICoaService coaService) {
    this.beanList = beanList;
    this.elementsList = elementsList;
    this.coaService = coaService;
    if (beanList.size() != elementsList.size())
      throw new RuntimeException("Bean List's size is different with Elements List!");
  }

  /**
   * 返回外部请求生成CCID的长度
   */
  public int size() {
    return beanList.size();
  }

  /**
   * 要素容器直接使用请求生成CCID的FVoucherDTO对象
   */
  public Object getElementContainer(int index) {
    return elementsList.get(index);
  }

  /**
   * COA使用外部申请生成CCID的FVoucherDTO对象去取
   */
  public FCoaDTO getCoa(int index) {
    final String coaId = (String) PropertiesUtil.getProperty(elementsList.get(index), GlConstant.COA_ID_KEY);
    return coaService.getCoa(NumberUtil.toLong(coaId));
  }

  /**
   * 批量生成CCID.
   */
  public void setCodeCombination(int index, CodeCombination cachedCodeCmb) {
    final Object bean = beanList.get(index);
    PropertiesUtil.setPropertyIgnoreFit(bean, GlConstant.CCID_KEY, StringUtil.toStr(cachedCodeCmb.getCcid()));
    if (codeCombRecord.containsKey(cachedCodeCmb)) {
      StringBuffer indexs = (StringBuffer) codeCombRecord.get(cachedCodeCmb);
      indexs.append(GlConstant.COMMA).append(index);
    } else {
      StringBuffer indexs = new StringBuffer();
      indexs.append(index);
      codeCombRecord.put(cachedCodeCmb, indexs);
    }
  }

  /**
   * 更新冲突反馈到申请生成CCID的对象中.
   */
  public void updateCodeCombinationId(CodeCombination oldOne, CodeCombination newOne) {
    if (codeCombRecord.containsKey(oldOne)) {
      StringBuffer indexs = (StringBuffer) codeCombRecord.get(oldOne);
      String[] indexArray = indexs.toString().split(GlConstant.COMMA);
      for (int i = 0; i < indexArray.length; i++) {
        final int index = NumberUtil.toInt(indexArray[i]);
        final Object dto = beanList.get(index);
        PropertiesUtil.setProperty(dto, GlConstant.CCID_KEY, StringUtil.toStr(newOne.getCcid()));
      }
      codeCombRecord.put(newOne, indexs);
      codeCombRecord.remove(oldOne);
    } else {
      throw new RuntimeException("在批量处理中未生成过coa=" + oldOne.getCoaId() + ",ccid=" + oldOne.getCcid() + ",md5="
        + oldOne.getMd5() + " 的要素编码组合, 冲突更新失败!");
    }
  }

  /**
   * CCI生成过程不使用模糊匹配
   */
  public boolean needFuzzyMatch(int index) {
    return false;
  }

}
