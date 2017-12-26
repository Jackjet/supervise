package gov.df.fap.service.util.exceptions.gl;

public class CoaNotExistsException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  boolean oriented = false;
	
	public boolean isOriented() {
		return oriented;
	}

	public CoaNotExistsException() {
	}
	
	public CoaNotExistsException(boolean oriented) {
		this.oriented = oriented;
	}
	
	public CoaNotExistsException(String message) {
		super(message);
	}

	public CoaNotExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
