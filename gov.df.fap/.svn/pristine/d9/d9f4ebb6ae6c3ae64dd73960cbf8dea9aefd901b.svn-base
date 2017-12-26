package gov.df.fap.service.bis;

import gov.df.fap.api.dictionary.bis.IBISInterface;
import gov.df.fap.api.dictionary.interfaces.IControlDictionary;
import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.regex.Pattern;

public class BisRuleCode extends RuleValueUtil {

  private IBISInterface ibis = null;

  public BisRuleCode(IBISInterface ibis) {
    this.ibis = ibis;
  }

  public String getBisInputBillNo(String arg0, String arg1) {
    return null;
  }

  /**
     * 返回子类实现的预算项目编码规则
     * @param currentXML 主界面当前数据
     * @param iCDictionaryobj 数据库访问类
     * @return XMLData里必须要两个参数 condition查询现有的预算项目过滤条件，给空值就默认为原有过滤条件，isContinue是否继续保存1，继续，0不继续。
     */
  public XMLData getBisChrCode(XMLData currentXML, Object iCDictionaryobj) {
    IControlDictionary iCDictionary = (IControlDictionary) iCDictionaryobj;
    int num = 0;
    num = Integer.parseInt(currentXML.get("num") != null ? currentXML.get("num").toString() : "0");
    XMLData returnXML = new XMLData();
    if (currentXML.get("chr_name") == null || currentXML.get("chr_name").equals("")) {
      returnXML.put("isContinue", "0");
      return returnXML;
    }
    String condition;
    String chr_code = "";
    if (currentXML.get("parent_id") != null && !currentXML.get("parent_id").equals("")
      && ibis.getBisConfigPara("bis_codemode").equals("1")) {
      String bis_subcoderule = ibis.getBisConfigPara("bis_subcoderule");
      //bis_subcoderule  0#使用父项目编码+1#使用父项目编码规则+2#父项目编码-3位随机码
      if (bis_subcoderule != null && bis_subcoderule.equals("0")) {//0#使用父项目编码
        List Bislist = iCDictionary.findEleValues("BIS", null, null, false, null, null,
          "   and chr_id =" +"'"+ currentXML.get("parent_id")+"'");
        if (Bislist == null || Bislist.size() == 0) {// 如果没有对应预算项目父节点
          //          new MessageBox("  没有找到对应父项目数据！", MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
          returnXML.put("isContinue", "0");
          return returnXML;
        } else {// 如果有对应预算项目父节点
          chr_code = ((XMLData) Bislist.get(0)).get("chr_code").toString();
        }
      } else if (bis_subcoderule != null && bis_subcoderule.equals("1")) {
        //1#使用父项目编码规则
        chr_code = autocode(iCDictionary, 8, num, "   and length(chr_code)=8 order by chr_code desc");
      } else if (bis_subcoderule != null && bis_subcoderule.equals("2")) {
        List Bislist = iCDictionary.findEleValues("BIS", null, null, false, null, null,
          "   and chr_id =" + currentXML.get("parent_id"));
        if (Bislist == null || Bislist.size() == 0) {// 如果没有对应预算项目父节点
        //          new MessageBox("  没有找到对应父项目数据！", MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
          returnXML.put("isContinue", "0");
          return returnXML;
        } else {// 如果有对应预算项目父节点
          chr_code = ((XMLData) Bislist.get(0)).get("chr_code").toString();
        }
        //2#父项目编码-3位随机码
        chr_code = chr_code
          + autocode(iCDictionary, 3, num, "   and length(chr_code)=11 and parent_id is not null and chr_code like '"
            + chr_code + "%' order by chr_code desc");
      }
    } else {
      chr_code = autocode(iCDictionary, 8, num, "   and length(chr_code)=8 order by chr_code desc");

    }

    returnXML.put("chr_code", chr_code);
    returnXML.put("public_sign", "0");
    returnXML.put("isContinue", "1");
    return returnXML;
  }

  private String autocode(IControlDictionary iCDictionary, int codeLength, int num, String condition) {
    String chr_code = "";
    String maxCodeNum = "";
    List Bislist = iCDictionary.findEleValues("BIS", null, null, false, null, null, condition);
    if (Bislist == null || Bislist.size() == 0) {// 如果没有对应预算项目编码就从1开始
      maxCodeNum = String.valueOf(1 + num);
    } else {// 如果有就最大值+1
      String maxChr_code = ((XMLData) Bislist.get(0)).get("chr_code").toString();
      Pattern pattern = Pattern.compile("[0-9]*");
      if (pattern.matcher(maxChr_code).matches()) {
        maxCodeNum = String.valueOf(Integer.parseInt(maxChr_code.substring(maxChr_code.length() - codeLength,
          maxChr_code.length())) + 1 + num);
      }
    }
    for (int i = (maxCodeNum.length()); i < codeLength; i++) {// 补零
      maxCodeNum = "0" + maxCodeNum;
    }
    chr_code = maxCodeNum;

    return chr_code;
  }

}
