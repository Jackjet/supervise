package gov.df.fap.api.gl.coa;

/**
 * 要素组合,即CCID.
 * @author
 *
 */
public interface CodeCombination extends Cloneable{

	/**ccid值均为正数*/
	public static final long CCID_NULL = -1;
	
	/**不冲突,但不在数据库中存在*/
	public static final int STATUS_CONFLICT_NONE = -1;
	
	/**在数据库中存在,也表示不会冲突,因为在数据库中的记录都是正确的*/
	public static final int STATUS_EXISTS_IN_DB = 2;
	
	/**
	 * 取编码组合的唯一标识
	 * @return
	 */
	public long getCcid();
	
	/**
	 * 取编码组合的对应的COA_ID
	 * @return
	 */
	public String getCoaId();
	
	/**
	 * 取编码组合的MD5值
	 * @return
	 */
	public String getMd5();
	
	/**
	 * 设置MD5值
	 *
	 */
	public void setMd5(String md5);
	
	/**
	 * 设置CCID
	 * @param ccid
	 */
	public void setCcid(long ccid);
	
	/**
	 * 取出冲突状态
	 * @return
	 */
	public int getStatus();

	/**
	 * 取出冲突状态
	 * @return
	 */
	public void setStatus(int status);
	
	/**
	 * 复制一份
	 * @return
	 */
	public Object clone();
	
//	/**
//	 * 取该CCID的对应MD5值
//	 * @return
//	 */
//	public String getMd5();
}
