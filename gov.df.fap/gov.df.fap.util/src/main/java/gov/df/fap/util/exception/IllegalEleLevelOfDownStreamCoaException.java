package gov.df.fap.util.exception;

/**
 * 描述不合法的下游Coa要素级别的异常
 * @author 张轲
 */
public class IllegalEleLevelOfDownStreamCoaException extends Exception {

  private static final long serialVersionUID = 1L;

  public IllegalEleLevelOfDownStreamCoaException(String message) {
		super(message);
	}

	public IllegalEleLevelOfDownStreamCoaException(String message, Throwable cause) {
		super(message, cause);
	}
}
