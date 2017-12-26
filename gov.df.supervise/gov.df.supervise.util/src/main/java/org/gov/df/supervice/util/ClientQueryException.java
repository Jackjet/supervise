/**
 * 
 */
package org.gov.df.supervice.util;

/**
 * <p>
 * Title:查询异常
 * </p>
 * <p>
 * Description:查询工厂
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: 北京用友政务软件有限公司
 * </p>
 * <p>
 * CreateDate 2010-10-29
 * </p>
 * 
 * @author hlf
 * @version 3.0.01.06
 */
public class ClientQueryException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2168162623523061895L;

	/**
	 * 
	 */
	public ClientQueryException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ClientQueryException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ClientQueryException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ClientQueryException(Throwable cause) {
		super(cause);
	}

}
