package gov.df.fap.service.gl.Handler;

/**
 * 记账操作定义
 * @author 
 * @version 2017-03-06
 *
 */
public interface ActionDefinitions {

	public static final String[] ACTION_NAMES = {"", "录入", "修改", "终审", "撤销终审", "作废"};
	
	/**
	 * 录入记账操作标志,录入记账时只生成额度,不进行冲销.
	 * 对追溯对象也只有插入.
	 */
	public static final int SAVE_ACTION = 1;
	
	/**
	 * 更新操作标志,当CCID,MONEY,MONTH变更时生成冲销额度.
	 * 当以上所说几个属性有更新时,对追溯对象进行更新(先删除后插入).
	 */
	public static final int UPDATE_ACTION = 2;
	
	/**
	 * 终审操作标志,生成冲销额度.
	 * 如果CCID,MONEY,MONTH几个属性变更时更新追溯对象.
	 */
	public static final int AUDIT_ACTION = 3;
	
	/**
	 * 撤销终审标志,行为类似终审操作.
	 */
	public static final int CANCEL_AUDIT_ACTION = 4;
	
	/**
	 * 作废操作,只生成冲销额度.不生成追溯对象.
	 */
	public static final int INVALID_ACTION = 5;
}
