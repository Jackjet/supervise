package gov.df.fap.service.util.exceptions.gl;

import gov.df.fap.bean.gl.GlConstant;


public class GlException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public GlException() {
		super();
	}

	public GlException(String arg0) {
		super(arg0);
	}

	public GlException(Exception arg0) {
		super(arg0);
	}

	public GlException(String msg, Exception arg0) {
		super(msg, arg0);
	}
	
	/**
	 * exception factory
	 * @param key exception key or exception msg
	 * @param msgs replacement msg
	 * @return thr GlException with the input parameter when prepared message can not founder by the input paremter, or return the prepared message.
	 */
	public static GlException glExceptionFactory(String keyOrMsg){
		return glExceptionFactory(keyOrMsg, (Object[])null, null); 
	}

	/**
	 * exception factory
	 * @param keyOrMsg exception key or exception msg
	 * @param ex the new exception caused by 
	 * @return thr GlException with the input parameter when prepared message can not founder by the input paremter, or return the prepared message.
	 */
	public static GlException glExceptionFactory(String keyOrMsg, Exception causeBy){
		return glExceptionFactory(keyOrMsg, (Object[])null, causeBy); 
	}
	
	/**
	 * exception factory
	 * @param key exception key
	 * @param msgs replacement msg
	 * @return
	 */
	public static GlException glExceptionFactory(String key, Object[] msgs){
		return glExceptionFactory(key, msgs, null);
	}
	
	/**
	 * exception factory
	 * @param key exception key
	 * @param msgs replacement msg
	 * @return
	 */
	public static GlException glExceptionFactory(String key, Object[] msgs, Exception ex){
		
		String excepMsg = (String)GlConstant.exceptionsConstant.get(key);
		
		if (excepMsg != null){
			for (int i = 0; i < msgs.length; i++){
				if (msgs[i] == null)
					msgs[i] = "";
				excepMsg = excepMsg.replaceAll("#arg"+i+"#", msgs[i].toString());
			}
		}else{
			excepMsg = key;
		}
		
		return new GlException(excepMsg.replaceAll("#arg\\d#", ""), ex);
	}
	
	/**
	 * 
	 * @param key exception key 
	 * @return
	 
	public static GlException GlExceptionFactory(String key){
		return GlExceptionFactory(key, null);
	}
	*/
	
	/**
	 * 
	 * @param key exception key
	 * @param msg
	 * @return
	 */
	public static GlException glExceptionFactory(String key, String msg){
		return glExceptionFactory(key, new Object[]{msg}, null);
	}
}
