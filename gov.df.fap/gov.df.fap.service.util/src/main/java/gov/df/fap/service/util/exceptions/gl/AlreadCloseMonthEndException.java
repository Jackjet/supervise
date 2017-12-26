package gov.df.fap.service.util.exceptions.gl;

/**
 * 已年结异常
 * @author lwq
 *
 */
public class AlreadCloseMonthEndException extends Exception {

  private static final long serialVersionUID = 1L;

  public AlreadCloseMonthEndException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AlreadCloseMonthEndException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AlreadCloseMonthEndException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AlreadCloseMonthEndException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
