package gov.df.fap.service.util.exceptions.workflow;

/**
 * 工作流
 * 业务数据节点异常
 *
 * @author  
 * @version 
 * @since   java 1.6
 */

public class WFErrorNodeException extends Exception{
   
	/**
	 * 
	 */
	private static final long serialVersionUID = -3350317114789348719L;

	/**
     * Constructor.
     */
    public WFErrorNodeException() {
	super();
    }

    /**
     * Constructor with a detail message.
     *
     * @param s the detail message
     */
    public WFErrorNodeException(String s) {
	super(s);
    }
}
