package gov.df.fap.service.util.gl.coa;

import gov.df.fap.bean.gl.GlConstant;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.coa.FCoaDetail;
import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.Properties.PropertiesUtil;
import gov.df.fap.util.number.NumberUtil;

import java.util.Iterator;
import java.util.List;


/**
 * CCID工具类
 * @author 
 * @version 
 */
public class CcidUtil {

  public static final String CCID_SEPERATOR = "9";

  public static final String CODE_COMBINATION_MSG_SEPERATOR = "-";

  public static final String ELEMENT_EMPTY_HASH0 = StringUtil.ZERO;

  public static final String ELEMENT_EMPTY_HASH1 = "#";

  /**
   * 计算CCID, 对象中存有各要素的xx_id
   * @param coaDto
   * @param data
   * @return
   */
  public static SimpleCodeCombination caculateCcid(FCoaDTO coaDto, Object data) {
    return caculateCcid(coaDto, data, false);
  }

  /**
   * 对象中存有ElementInfo
   * @param coaDto
   * @param data
   * @return
   */
  public static SimpleCodeCombination caculateCcidWithElementInfo(FCoaDTO coaDto, Object data) {
    return caculateCcid(coaDto, data, true);
  }

  public static SimpleCodeCombination caculateCcid(FCoaDTO coaDto, Object data, boolean elementInfo) {

    if (coaDto.getCoaDetail() == null || coaDto.getCoaDetail().isEmpty())
      throw new RuntimeException(coaDto + "不包含要素,配置有误,生成CCID失败!");

    int hash = 0;
    final Iterator queryCcidIt = coaDto.getCoaDetailList().iterator();
    StringBuffer codeComMsg = new StringBuffer();//要素组合字符串.
    String eleField = null;
    String beHash = null;
    String rule = CommonUtil.getCcidGenRule();
    String empty = StringUtil.equals(StringUtil.ONE, rule) ? ELEMENT_EMPTY_HASH1 : ELEMENT_EMPTY_HASH0;
    String seperator = StringUtil.equals(StringUtil.ONE, rule) ? CODE_COMBINATION_MSG_SEPERATOR
      : StringUtil.EMPTY;
    while (queryCcidIt.hasNext()) {
      final FCoaDetail element = (FCoaDetail) queryCcidIt.next();
      if (elementInfo)
        eleField = element.getEleCode() + ".chrId";
      else
        eleField = element.getEleCode().toLowerCase() + GlConstant.ID_SUBFIX;
      final String inputChrId = (String) PropertiesUtil.getProperty(data, eleField);
      if (StringUtil.isEmpty(inputChrId)) {
        if (StringUtil.isEmpty(element.getDefaultValue()))
          beHash = empty;
        else {
          beHash = element.getDefaultValue();
        }
      } else {
        beHash = inputChrId;
      }
      hash ^= beHash.hashCode();
      codeComMsg.append(beHash);
      codeComMsg.append(seperator);
    }
    //modified by ydz 提高CCID 生成的唯一性。
    String beHsashComStr = coaDto.getCoaId() + CODE_COMBINATION_MSG_SEPERATOR + codeComMsg.toString();
    long ccid = 0;
    if (!StringUtil.equals(StringUtil.ONE, rule)) {
      ccid = NumberUtil.toLong(Integer.toString(NumberUtil.toInt(coaDto.getCoaId()), 9) + CCID_SEPERATOR
        + Math.abs(hash));
    } else {
      ccid = NumberUtil.toLong(Integer.toString(NumberUtil.toInt(coaDto.getCoaId()), 9) + CCID_SEPERATOR
        + Math.abs(codeComMsg.toString().hashCode()));
    }
    return new SimpleCodeCombination(ccid, coaDto.getCoaId(), StringUtil.genMD5(beHsashComStr));
  }

  /**
   * 通过ccid取得coaId
   * @param ccid
   * @return
   */
  public static long getCoaIdByCcid(long ccid) {
    //		return NumberUtil.toLong(StringUtil.toStr(ccid));
    return getCoaIdByCcid(StringUtil.toStr(ccid));
  }

  /**
   * 通过ccid取得coaId
   * @param ccid
   * @return
   */
  public static long getCoaIdByCcid(String ccid) {
    String ccidStr = ccid;
    String coaId = ccidStr.substring(0, ccidStr.indexOf(CCID_SEPERATOR));
    return Long.parseLong(coaId, 9);
  }

  /**
   * 生成COA查询的字段名
   * @param coa
   * @param alias
   * @return
   */
  public static String generateCcidSelectFields(FCoaDTO coa, String alias) {
    List coaDetails = coa.getCoaDetail();
    StringBuffer sb = new StringBuffer(alias + ".coa_id coa_id," + alias + ".ccid ccid,");
    FCoaDetail detail = null;
    String eleCodeLower = null;
    for (int i = 0; i < coaDetails.size(); i++) {
      detail = (FCoaDetail) coaDetails.get(i);
      eleCodeLower = detail.getEleCode();
      sb.append(alias + "." + eleCodeLower + "_id " + eleCodeLower + "_id,")
        .append(alias + "." + eleCodeLower + "_code " + eleCodeLower + "_code,")
        .append(alias + "." + eleCodeLower + "_name " + eleCodeLower + "_name,");
    }

    return sb.substring(0, sb.length() - 1);
  }
}
