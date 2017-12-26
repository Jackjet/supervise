package gov.df.fap.service.util.exceptions.gl;

/**
 * @author lwq
 */
public class ExistOnWayDataOfBusVouException extends Exception {

  private static final long serialVersionUID = 1L;

  public ExistOnWayDataOfBusVouException(String message) {
		super(message);
	}

	public ExistOnWayDataOfBusVouException(String message, Throwable cause) {
		super(message, cause);
	}
}
