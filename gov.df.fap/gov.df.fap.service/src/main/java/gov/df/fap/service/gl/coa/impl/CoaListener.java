package gov.df.fap.service.gl.coa.impl;

/**
 * Coa Listener
 * @author
 *
 */
public interface CoaListener {

  /**
   * 任何COA有变动,会调用该方法.
   *
   */
  public void coaUpdate();

}
