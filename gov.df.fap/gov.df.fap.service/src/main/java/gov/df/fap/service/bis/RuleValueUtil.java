package gov.df.fap.service.bis;

import gov.df.fap.util.xml.XMLData;

public abstract class RuleValueUtil {
  /**
   * 根据bisType类型，调用相应规则公式，返回规则编码
   * 
   * @param inputValue
   *            输入值，如是：单位编码、业务处室
   * @param bisType
   *            0代表单位项目、1代表处室公用项目、2代表财政指标
   * @return 返回根据规则公式，生成的规则编码
   * @author 
   */
  public abstract String getBisInputBillNo(String inputValue, String bisType);

  /**
     * 返回子类实现的预算项目规则
     * @param currentXML 主界面当前数据
     * @param iCtrlDictionary 数据库访问类
     * @return XMLData里必须要两个参数 condition查询现有的预算项目过滤条件，给空值就默认为原有过滤条件，isContinue是否继续保存1，继续，0不继续。
     */
  public XMLData getBisChrCode(XMLData currentXML, Object iCtrlDictionary) {
    return null;
  }

}
