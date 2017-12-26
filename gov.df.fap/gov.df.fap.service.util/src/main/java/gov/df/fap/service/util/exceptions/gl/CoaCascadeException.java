package gov.df.fap.service.util.exceptions.gl;

/**
 * 要素级次级联校验异常
 * @author lwq
 * @version
 */
public class CoaCascadeException extends Exception {

	private static final long serialVersionUID = 1L;

	public CoaCascadeException(String message) {
		super(message);
    }
}
