package gov.df.fap.service.util.exceptions.gl;

import java.util.List;

/**
 * 凭证无法找到异常,可能是由于凭证未入账导致.
 * @author 
 * @version 2017-03-24
 */
public class VoucherNotFoundException extends GlException {

  private static final long serialVersionUID = 1L;

  List journalNotFound = null;

  public void setJournalNotFound(List list) {
    this.journalNotFound = list;
  }

  public VoucherNotFoundException(List errorList) {
    this.journalNotFound = errorList;
  }

  public VoucherNotFoundException(String msg, List errorList) {
    super(msg);
    this.journalNotFound = errorList;
  }

  public VoucherNotFoundException() {
    super();
  }

  public VoucherNotFoundException(Exception arg0) {
    super(arg0);
  }

  public VoucherNotFoundException(String msg, Exception arg0) {
    super(msg, arg0);
  }

  public VoucherNotFoundException(String arg0) {
    super(arg0);
  }

}
