package gov.df.fap.bean.gl;

import java.util.HashMap;
import java.util.Map;

/**
 * gl constant.
 * @author 
 * @version 2007-5-20
 *
 */
public class GlConstant {

  /**
   * 不控制
   */
  public final static int CTRLLEVEL_NONE = 0;

  /**
   * 警告控制
   */
  public final static int CTRLLEVEL_WARN = 1;

  /**
   * 严格控制，CCID、MONTH要素没变化则不重新进行额度控制
   */
  public final static int CTRLLEVEL_STRICT = 2;

  /**
   * 严格控制，无论如果，总是重新计划额度进行控制
   */
  public final static int CTRLLEVEL_ALWAYS = 3;

  public final static String DEFAULT_SET_YEAR = "2008";

  public static final String MD5_KEY = "md5";

  public final static String CCID_KEY = "ccid";

  public static final String COA_ID_KEY = "coa_id";

  public final static String ID_SUBFIX = "_id";

  public final static String CODE_SUBFIX = "_code";

  public final static String NAME_SUBFIX = "_name";

  /**逗号*/
  public final static String COMMA = ",";

  public final static String NULL_HASH_STRING_ITEM = "#";

  public final static String FIELD_ROUNDER = "#";

  public final static String EXCEP_ILLEGAL_KEY = "gl.generic.illegal";

  public final static String EXCEP_GL0001_KEY = "gl0001-setgroupid";

  public final static String EXCEP_GL0002_KEY = "gl0002-setgroupid";

  public final static String EXCEP_GL0003_KEY = "gl0003-setgroupid";

  public final static String EXCEP_GL0004_KEY = "gl0004-setgroupid";

  public final static String EXCEP_GL0005_KEY = "gl0005";

  public final static String EXCEP_DIC0001_KEY = "dic0001_getccid";

  public final static String EXCEP_DIC0002_KEY = "dic0002_getccid";

  public final static String EXCEP_DIC0003_KEY = "dic0003_getccid";

  public final static String EXCEP_DIC0004_KEY = "dic0004_getccid";

  public final static String ROW = "row";

  public final static String SEQ_CCID_KEY = "SEQ_CCID_ORDER";

  public final static String SEQ_BVTYPE_KEY = "SEQ_GL_BUSVOU_TYPE";

  public final static String SEQ_ACCTMDL_KEY = "SEQ_GL_BUSVOU_ACCTMDL";

  public final static String SEQ_COA_KEY = "SEQ_GL_COA";

  public final static String SEQ_ACCOUNT_KEY = "SEQ_GL_BV_ACCOUNT";

  //sequence 基础数据生成chr_id number类型   
  public final static String SEQ_ELE_CHR_ID = "SEQ_ELE_CHR_ID";

  public static final Map accountPrefixMapping = new HashMap();

  public final static String BAI_KEY = "BAI";

  public final static String BAC_KEY = "BAC";

  public final static String BAP_KEY = "BAP";

  public final static String GL_GRAPH_SUFFIX = ".gml";

  public static final String SET_YEAR = "set_year";

  /**
   * exception message,异常消息, 如果有要替换的关键字,用#arg?#写在消息中, 序号从0开始,
   * 将会用DicException.dicExceptionFactroy(String key, Object[] msgs)中的msgs
   * 数组的i号去替换.
   */
  public static Map exceptionsConstant = new HashMap();
  static {

    exceptionsConstant.put(EXCEP_ILLEGAL_KEY, "#arg0#非法!");

    exceptionsConstant.put(EXCEP_GL0001_KEY, "分组号为空,无法分组!");
    exceptionsConstant.put(EXCEP_GL0002_KEY, "分组列表为空,无法分组!");
    exceptionsConstant.put(EXCEP_GL0003_KEY, "ID为:#arg0#额度分组号更新异常!");
    exceptionsConstant.put(EXCEP_GL0004_KEY, "额度分组失败");
    exceptionsConstant.put(EXCEP_GL0005_KEY, "根据传入来源额度ID无法找到额度");

    exceptionsConstant.put(EXCEP_DIC0001_KEY, "#arg0#级次为#arg1#,输入值不符合业务单据要素级次设置,无法生成对应要素配置实例!");
    exceptionsConstant.put(EXCEP_DIC0002_KEY, "非法的coa要素级次配置,无法生成业务单ccid!");
    exceptionsConstant.put(EXCEP_DIC0003_KEY, "数据库要素表#arg0#的chr_name,chr_code为空");
    exceptionsConstant.put(EXCEP_DIC0004_KEY, "要素#arg0#级次为#arg1#，传入要素值为空，生成CCID失败！");

    accountPrefixMapping.put(BAI_KEY, "income");
    accountPrefixMapping.put(BAC_KEY, "clear");
    accountPrefixMapping.put(BAP_KEY, "pay");
  }
}
