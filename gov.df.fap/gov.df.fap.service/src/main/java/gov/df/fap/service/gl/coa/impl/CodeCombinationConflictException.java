package gov.df.fap.service.gl.coa.impl;

import gov.df.fap.api.gl.coa.CodeCombination;

import java.util.ArrayList;
import java.util.List;

/**
 * CCID冲突异常
 * @author 
 *
 */
public class CodeCombinationConflictException extends Exception {

  private static final long serialVersionUID = 1L;

  /**所以有冲突CCID,这里面内保存CodeCombination接口的实现*/
  private List conflictList = null;

  /**
   * 构造方法
   *
   */
  public CodeCombinationConflictException() {
  }

  /**
   * 传入冲突来构造异常
   * @param conflicts 冲突
   */
  public CodeCombinationConflictException(List conflicts) {
    this.conflictList = conflicts;
  }

  /**
   * 取出CCID异常的冲突
   * @return
   */
  public List getConflictCodeCombinations() {
    return conflictList;
  }

  public void addConflict(CodeCombination conflict) {
    if (conflictList == null)
      conflictList = new ArrayList();

    conflictList.add(conflict);
  }

}
