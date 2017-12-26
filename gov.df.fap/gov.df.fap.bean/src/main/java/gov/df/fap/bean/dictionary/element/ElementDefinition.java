package gov.df.fap.bean.dictionary.element;

/**
 * 要素参数的一些常量定义
 * @author lwq
 * @version 
 */
public interface ElementDefinition {

	/**底级*/
	public static final int LEVEL_NUM_BOTTOM = -1;
	/**
	 * 任意级次,在生成CCID时,如有任意级次且指定模糊匹配,则会进行模糊匹配.
	 */
	public static final int LEVEL_NUM_AUTO = -2;
	/**自定义级次*/
	public static final int LEVEL_NUM_CUSTOMER = 0;
	
	/**要素ID*/
	public final static String CHR_ID="chr_id";
	/**要素编码*/
	public final static String CHR_CODE = "chr_code";
	/**要素名称*/
	public final static String CHR_NAME = "chr_name";
	/**银行名称*/
	public final static String BANK_NAME = "bank_name";
	/**账户名称*/
	public final static String ACCOUNT_NAME = "account_name";
	/**账户号码*/
	public final static String ACCOUNT_NO = "account_no";
	
	/**级次*/
	public final static String LEVEL_NUM = "level_num";
	/**级次编码*/
	public final static String LEVEL_CODE = "level_code";
	/**是否叶子*/
	public final static String IS_LEAF = "is_leaf";
	
	/**要素简称*/
	public final static String ELE_CODE = "ele_code";
	/**要素表别名,对应数据库表*/
	public final static String ELE_SOURCE = "ele_source";
	/**要素名称*/
	public final static String ELE_NAME = "ele_name";
	/**区域码*/
	public final static String RG_CODE = "rg_code";
	
	public final static String SET_YEAR = "set_year";
	
	public final static String PARENT_ID = "parent_id";
}
