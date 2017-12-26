package gov.df.fap.service.util.gl.coa;

import gov.df.fap.api.gl.coa.CodeCombination;
import gov.df.fap.util.StringUtil;

/**
 * 简单CCID对象
 * @author 
 *
 */
public class SimpleCodeCombination implements CodeCombination, Cloneable {

  /**编码组合ID*/
  private long ccid = 0;

  /**COAID*/
  private String coaId = StringUtil.EMPTY;

  /**编码组合的MD5值*/
  private String md5 = StringUtil.EMPTY;

  /**默认无冲突*/
  private int conflictFlag = STATUS_CONFLICT_NONE;

  public SimpleCodeCombination() {
  }

  /**
   * 简单生成CCID对象
   * @param ccid ccid值
   * @param coaId coaId
   * @param md5 CCID组合的MD5值
   */
  public SimpleCodeCombination(long ccid, String coaId, String md5) {
    this.ccid = ccid;
    this.coaId = coaId;
    this.md5 = md5;
  }

  public long getCcid() {
    return ccid;
  }

  public void setCcid(long ccid) {
    this.ccid = ccid;
  }

  public String getCoaId() {
    return coaId;
  }

  public void setCoaId(String coaId) {
    this.coaId = coaId;
  }

  public String getMd5() {
    return md5;
  }

  public void setMd5(String md5) {
    this.md5 = md5;
  }

  public int getStatus() {
    return conflictFlag;
  }

  public void setStatus(int conflict) {
    this.conflictFlag = conflict;
  }

  public String getElementId(String eleCode) {
    throw new UnsupportedOperationException();
  }

  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean equals(Object key) {
    if (this == key)
      return true;

    if (key instanceof SimpleCodeCombination) {
      SimpleCodeCombination t = (SimpleCodeCombination) key;
      return t.coaId.equals(coaId) && t.ccid == ccid && StringUtil.equals(t.md5, md5);
    }
    return false;
  }

  public int hashCode() {
    return (coaId + ccid + md5).hashCode();
  }
}
