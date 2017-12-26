package gov.df.fap.service.rule;

import gov.df.fap.api.workflow.ISysRegulationConf;
import gov.df.fap.api.workflow.sysregulation.IWorkFlowRuleFactory;
import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.bean.rule.SysRulePara;
import gov.df.fap.bean.util.FBaseDTO;
import gov.df.fap.bean.workflow.SysWfCondition;
import gov.df.fap.service.util.UtilCache;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.xml.XMLData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bsh.Interpreter;

/**
 * Title:规则业务实现类
 * 
 * Description: 规则业务实现类
 * 
 * @author 
 * 
 * <br>
 * Date 创建时间：2017-104-10 上午10:53:19
 * 
 * Company: 
 * 
 * @version 1.0
 */
@Service
public class RuleFactory implements IWorkFlowRuleFactory {
  private static Interpreter i = new Interpreter(); // Construct an

  /* 变量定义类型 */
  private final String PARA_INT = "2";

  /* 函数定义类型 */
  private final String FUNCTION_INT = "3";

  // /* 参数类型为整型 */
  private final String PARA_TYPE_INT = "1";

  /* 参数类型为字符串 */
  private final String PARA_TYPE_STRING = "2";

  /* 参数类型为布尔类型 */
  private final String PARA_TYPE_BOOLEAN = "3";

  @Autowired
  private ISysRegulationConf regulation = null;

  // 当前规则所有变量存储的MAP
  Map paraMap = new HashMap();

  /**
   * 
   * 功能：校验数据是否在范围内(如:记账范围，匹配处理)
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-25 下午05:46:57 beaf 2008-12-30 整理
   * @param conditionId
   *            规则ID
   * @param baseDataDTOOrMap
   *            参数值MAP
   * @return
   * @throws Exception
   */
  public boolean isMatchByBSH(String conditionId, Object baseDataDTOOrMap) throws Exception {
    // BeanShell表达试定义
    String str = "";

    // 从缓存中取得规则表达试
    str = UtilCache.getRuleConditionExpression(conditionId);

    if (null != str && str.equals("#")) {
      return true;
    }
    // add by wanghongtao
    if (null == str || "".equals(str)) {
      SysWfCondition wfCondition = regulation.getRuleById(conditionId);
      if (wfCondition != null) {
        String tempConditionId = wfCondition.getCONDITION_ID();
        String tempExpression = wfCondition.getBSH_EXPRESSION();
        if (null != tempExpression && !"#".equals(tempExpression)) {
          UtilCache.putRuleConditionExpression(tempConditionId, tempExpression);
        }
        str = UtilCache.getRuleConditionExpression(conditionId);
      }
    }

    if ("".equals(str) || null == str) {
      return true;
    }

    List params = getParams(str);
    for (int i = 0; i < params.size(); i++) {
      //加入空指针判断
      if (null != BeanUtils.getProperty(baseDataDTOOrMap, params.get(i).toString().toLowerCase())) {
        str = str.replaceAll("[\\s]#" + params.get(i) + "[\\s]",
          BeanUtils.getProperty(baseDataDTOOrMap, params.get(i).toString().toLowerCase()));
      } else {
        str = str.replaceAll("[\\s]#" + params.get(i) + "[\\s]", "");
      }
    }
    try {
      // 如果表达式存在操作符结尾， 直接去掉
      str = str.trim();
      if (str.endsWith("&&") || str.endsWith("||")) {
        str = str.substring(0, str.length() - 2);
      }

      return Boolean.valueOf(i.eval(str).toString()).booleanValue();
    } catch (Exception e) {
      throw new Exception("规则表达式配置逻辑不正确!");
    }
  }

  /**
   * 根据bsh表达式计算需要替换的变量列表
   * @param bshExpression
   * @return
   * @author hult 2011-03-15
   */
  private List getParams(String bshExpression) {
    List params = new ArrayList();
    char[] bash = bshExpression.toCharArray();
    for (int i = 0; i < bash.length; i++) {
      //找到一个变量参数
      if (bash[i] == '#') {

        StringBuffer param = new StringBuffer();
        //从#开始找到第一个变量名 字母或数字下划线
        //i++后是#后的第一个字母
        for (i++; i < bash.length
          && (('A' <= bash[i] && bash[i] <= 'Z') || ('a' <= bash[i] && bash[i] <= 'z')
            || ('0' <= bash[i] && bash[i] <= '9') || bash[i] == '_'); i++) {
          param.append(bash[i]);
        }
        params.add(param.toString());

        //将获取变量名后的i前移 可省略 一般在变量明后不会马上出现#
        i--;
      }
    }
    return params;
  }

  /**
   * 
   * 功能： 利用java反射机制. 把传入对象的所有属性值 转换成MAP对象 key:(字段名称) value:(字段值)
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-21 下午03:15:06
   * @param obj
   *            工作流对象
   * @return 属性和属性值对应的MAP
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  public Map getMapVoucherDto(FVoucherDTO obj) throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException {
    Map map = new HashMap();
    Method[] method = FVoucherDTO.class.getDeclaredMethods();
    for (int k = 0; k < method.length; k++) {
      /* 如果方法名是以get开头 进入代码块取得相应的属性值 */
      if (method[k].getName().startsWith("get")) {
        Object o = method[k].invoke(obj, null);
        /* 方法名称: valueName */
        String valueName = method[k].getName().intern();
        /* 如果调用GET方法的返回值不为空. 放入MAP中 */
        if (null != o && !"".equals(o)) {
          map.put(valueName.substring(3).toLowerCase(), o);
        }
      }
    }
    Method[] methodBase = FBaseDTO.class.getDeclaredMethods();
    for (int k = 0; k < methodBase.length; k++) {
      /* 如果方法名是以get开头 进入代码块取得相应的属性值 */
      if (methodBase[k].getName().startsWith("get")) {
        Object o = methodBase[k].invoke(obj, null);
        /* 方法名称: valueName */
        String valueName = methodBase[k].getName().intern();
        /* 如果调用GET方法的返回值不为空. 放入MAP中 */
        if (null != o && !"".equals(o)) {
          map.put(valueName.substring(3).toLowerCase(), o);
        }
      }
    }

    return map;
  }

  /**
   * 
   * 功能：函数参数组合规则
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-18 下午05:50:56
   * @param paraId
   *            参数ID
   * @return 规则组合表达试字符串
   * @throws Exception
   */
  private String getParas(String paraId) throws Exception {
    /* 函数参数组合规则 */
    StringBuffer sb = new StringBuffer();
    SysRulePara srp = getRegulation().getSysRulePara(paraId);
    /* 如果参数类型等于3即为函数 */
    if (srp.getPARA_TYPE().toString().equals(this.FUNCTION_INT)) {
      /* 递归查询函数 */
      String funParaType = srp.getPARA_TYPE().toString();
      /* 如果参数类型为空, 直接返回"" */
      if (funParaType == null)
        return "";

      sb.append(getFunction(srp.getFUN_ID(), srp.getPARA_NAME(), srp.getFUN_PARAS()));
      /* 变量 */
    } else if (srp.getPARA_TYPE().toString().equals(this.PARA_INT)) {
      paraMap.put(srp.getPARA_NAME(), "");

      sb.append(setParaValueType4BeanShell(null, srp.getPARA_VALUETYPE(), srp.getPARA_TYPE().toString(),
        " #" + srp.getPARA_NAME())
        + " ");
      /* 常量 */
    } else {
      sb.append(setParaValueType4BeanShell(null, srp.getPARA_VALUETYPE(), srp.getPARA_TYPE().toString(),
        srp.getPARA_NAME()));
    }

    return sb.toString();
  }

  /**
   * 
   * 功能：通过函数ID 和函数值组合逻辑语句
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-17 上午10:09:25
   * @param functionId
   *            函数ID
   * @param paraDesc
   *            参数描述 (例:new a().method)
   * @param funParas
   *            函数参数值 (ID以逗号区分)
   * @return 规则组合表达试字符串
   * @throws Exception
   */
  private String getFunction(String functionId, String paraDesc, String funParas) throws Exception {
    StringBuffer str = new StringBuffer();
    str.append(paraDesc);
    str.append("(");
    /* 参数值存在 */
    if (funParas != null) {
      // 函数参数值数组
      String[] funParasAll = funParas.split(",");
      int j = funParasAll.length;
      for (int i = 0; i < j; i++) {
        str.append(getParas(funParasAll[i]));
        str.append(",");
      }
    }

    // 去掉最后一个多余的","
    str.delete(str.length() - 1, str.length());
    str.append(")");

    return str.toString();
  }

  /**
   * 
   * 功能：从XMLData中取得para的属性值
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-17 下午07:23:15
   * @param xmlData
   * @param para
   *            属性名称ID
   * @return 属性值
   */
  private String getParaNotNull(XMLData xmlData, String para) {

    if (null != xmlData.get(para)) {
      return xmlData.get(para).toString();
    }
    return null;
  }

  public Map getParaMap() {
    return paraMap;
  }

  public void setParaMap(Map paraMap) {
    this.paraMap = paraMap;
  }

  /**
   * 
   * 功能：把变量值替换到字符串中。
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-24 上午10:56:58
   * @param str
   *            beanShell验证字符串
   * @param map
   *            字符串的变量值
   * @return 规则验证结果
   */
  public boolean getAlertFlag(String str, Map map) throws Exception {

    Set set = map.keySet();
    Iterator it = set.iterator();
    while (it.hasNext()) {
      String replace = it.next().toString();
      /* 匹配模式( #任意符号 ) */
      if (null == map.get(replace) && str.indexOf("#") != -1) {
        return false;
      }
      str = str.replaceAll("[\\s]#" + replace + "[\\s]", map.get(replace).toString()/*.toLowerCase()*/);

    }
    return getAlert(str);
  }

  /**
   * 
   * 功能： 规则增加页面验证规则表达试使用方法。
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-24 上午10:58:19
   * @param list
   *            table的值列表
   * @param map
   *            参数值MAP
   * @return beanshell 字符串
   * @throws Exception
   */
  public String getBshFlag(List list, Map map) throws Exception {
    StringBuffer str = new StringBuffer();
    /* 循环规则参数列表 */
    for (int i = 0; i < list.size(); i++) {
      XMLData xmlData = (XMLData) list.get(i);

      /* 左括号不为空 */
      if (null != xmlData.get("left_pare")) {
        str.append(xmlData.get("left_pare"));
      }

      str.append(setPara4BeanShell("left", this.getParaNotNull(xmlData, "left_paraid"), xmlData));

      str.append(setPara4BeanShell("right", this.getParaNotNull(xmlData, "right_paraid"), xmlData));

      /* 右括号赋值 */
      if (null != xmlData.get("right_pare")) {
        str.append(xmlData.get("right_pare"));
      }
      str.append(" ");
      Object o = xmlData.get("logic_operator");
      /* 如果逻辑运算符为AND 转换成&&否则转换为|| */
      if (o != null && i != list.size()) {
        if (o.toString().equals("AND")) {
          str.append(" && ");
        } else if (o.toString().equals("OR")) {
          str.append(" || ");
        }
      }
    }

    return str.toString();
  }

  /**
   * 
   * 功能： beanShell验证表达试
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-21 下午03:23:02
   * @param s
   *            需要验证的表达试
   * @return boolean
   */
  public static boolean getAlert(String s) throws Exception {
    /* BeanShell接口对象 */
    Interpreter i = new Interpreter(); // Construct an interpreter
    try {
      s = s.replaceAll("%", "");
      return Boolean.valueOf(i.eval(s).toString()).booleanValue();
    } catch (Exception e) {
      /* 脚本执行异常 待扩展处理 */
      throw new Exception("规则表达式配置逻辑不正确!");
    }
  }

  /**
   * 
   * 功能： 通过位置和参数ID以及页面值 返回参数处理BeanShell的字符串
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-25 下午04:15:59
   * @author 加入大于小于，模糊查询等其他情况的处理 add by beaf at 2008-12-30
   * @param position
   *            参数位置 left 左参数, right 右参数
   * @param paraId
   *            参数ID
   * @param xmlData
   *            主窗体TABLE值集合
   * @return BeanShell字符串
   * @throws Exception
   */
  private String setPara4BeanShell(String position, String paraId, XMLData xmlData) throws Exception {
    /* 规则参数对象定义 */
    SysRulePara leftSrp = null;
    String paraType = "";
    String para_valueType = "";
    int type = 0;
    try {
      Pattern pattern = Pattern.compile("[0-9]*");
      Matcher matcher = pattern.matcher(paraId);
      if ("left".equals(position) && !matcher.matches()) {
        type = 1;
      } else if ("right".equals(position) && xmlData.get("right_paraname").toString().indexOf("[要素]") != -1) {
        type = 1;
      } else {
        leftSrp = getRegulation().getSysRulePara(paraId);
      }
    } catch (Exception e) {
      throw new Exception("规则参数加载错误 " + e);
    }
    /* 返回的表达式 */
    StringBuffer sb = new StringBuffer();

    /* paraValue参数值 */
    StringBuffer paraValue = new StringBuffer();

    if (type != 1) {
      /* 规则类型定义 */
      paraType = leftSrp.getPARA_TYPE().toString();

      /* 参数类型 */
      para_valueType = leftSrp.getPARA_VALUETYPE().toString();
      if (paraType.equals(this.FUNCTION_INT)) {// 如果参数类型为3 。 调用查询函数
        try {
          paraValue.append(getFunction(leftSrp.getFUN_ID(), leftSrp.getPARA_NAME(), leftSrp.getFUN_PARAS()));
        } catch (Exception e) {
          throw new Exception("规则函数加载错误 " + e);
        }
      } else if (paraType.equals(this.PARA_INT)) {// 如果参数类型是2 . 对传入的MAP
        // 取得变量值
        paraMap.put(leftSrp.getPARA_NAME(), "");
        paraValue.append(" #");
        paraValue.append(leftSrp.getPARA_NAME());
        paraValue.append(" ");
      } else {// 如果不是直接显示值。
        paraValue.append(leftSrp.getPARA_NAME());
      }
    } else {
      if ("left".equals(position))
        paraMap.put(paraId, "");
      paraValue.append(paraId);
    }

    /* 组合参数BeanShell表达式 */
    if (type != 1) {
      sb.append(setParaValueType4BeanShell(position, para_valueType, paraType, paraValue.toString()));
    } else if ("right".equals(position)) {
      sb.append("\"" + paraValue.toString() + "\"");
    } else {
      //ymj 修改流转线规则定制时报错 false
      if (xmlData.get("right_paravaluetype").toString().equalsIgnoreCase(PARA_TYPE_INT)) {
        sb.append(" #" + paraValue.toString() + " ");
      } else {
        sb.append("\" #" + paraValue.toString() + " \"");
      }
    }
    /* 关系运算符定义 */
    Object operator = xmlData.get("operator");
    String rightPara = "";
    if (null != xmlData.get("right_paraname"))
      rightPara = (String) xmlData.get("right_paraname");

    //ymj 易用性改掉了para_valueType 判断左边如果是要素 按字符串来处理 如果非要素 按右边类型处理
    String left_paramName = xmlData.get("left_paraname") == null ? "" : xmlData.get("left_paraname").toString();
    if (left_paramName.indexOf("[要素]") != -1) {
      para_valueType = this.PARA_TYPE_STRING;
    } else {
      para_valueType = xmlData.get("right_paravaluetype") == null ? para_valueType : xmlData.get("right_paravaluetype")
        .toString();
    }
    /* 如果参数位置在左边.并且关系运算符为"="和"!=" 即添加关系运算符 */
    if ((operator.equals("!=") || operator.equals("=") || operator.equals("like") || operator.equals("LLike")
      || operator.equals("RLike") || operator.equals(">") || operator.equals("<") || operator.equals(">=") || operator
        .equals("<=")) && position.equals("left")) {// 如果是'='转换为逻辑'=='

      /* 当参数类型为字符串时 用equals来做关系运算符 */
      if (para_valueType.equals(this.PARA_TYPE_STRING)) {
        if (operator.equals("=")) {
          sb.append(" == ");
        }/*
         * else if (operator.equals("=")) { sb.append(" == "); }
         */else if (operator.equals("like")) {
          if (rightPara.length() - rightPara.replaceAll("%", "").length() == 2
            || rightPara.length() - rightPara.replaceAll("%", "").length() == 0)
            sb.append(".indexOf(");
          else {
            if (rightPara.endsWith("%"))
              sb.append(".startsWith(");
            else
              sb.append(".endsWith(");
          }
        } else if (operator.equals("LLike")) {
          sb.append(".endsWith(");
        } else if (operator.equals("RLike")) {
          sb.append(".startsWith(");
        } else if (operator.equals(">") || operator.equals("<") || operator.equals(">=") || operator.equals("<=")) {
          sb.append(".compareTo(");
        } else {
          sb.append(operator);
        }
      } else if (para_valueType.equals(this.PARA_TYPE_BOOLEAN)) {
        if (operator.equals("=")) {
          sb.append(" == ");
        }
      } else {
        if (operator.equals("=")) {
          sb.append(" == ");
        } else
          sb.append(operator);
      }

      /* 如果参数位置在左边并且关系运算符不为"="和"!=" 直接用关系运算符 */
    } else if (!(operator.equals("!=") || operator.equals("=")) && position.equals("left")) {
      sb.append(xmlData.get("operator"));

    } else if (!position.equals("left")) {
      /* 如果参数位置在右边 并且参数类型为字符串和布尔型添加反括号 */
      if (para_valueType.equals(this.PARA_TYPE_STRING)) {
        if (operator.equals("like")) {
          if (rightPara.length() - rightPara.replaceAll("%", "").length() == 2
            || rightPara.length() - rightPara.replaceAll("%", "").length() == 0)
            sb.append(")>-1");
          else
            sb.append(")");
        } else if (operator.equals(">")) {
          sb.append(")>0");
        } else if (operator.equals("LLike")) {
          sb.append(")");
        } else if (operator.equals("RLike")) {
          sb.append(")");
        } else if (operator.equals(">=")) {
          sb.append(")>=0");
        } else if (operator.equals("<")) {
          sb.append(")<0");
        } else if (operator.equals("<=")) {
          sb.append(")<=0");
        }
      }
    }

    return sb.toString();
  }

  /**
   * 
   * 功能： 通过参数位置和参数类型返回BeanShell的字符串
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-25 下午04:20:36
   * @param position
   *            参数位置左或者是右
   * @param para_valueType
   *            参数类型
   * @param value
   *            参数值
   * @return 组合字符串
   */
  private String setParaValueType4BeanShell(String position, String para_valueType, String para_type, String value) {
    StringBuffer sbf = new StringBuffer();

    if (para_valueType.equals(this.PARA_TYPE_STRING)) {
      /* 如果参数值为字符串并且参数类型为函数时, 去掉双引号 */
      if (para_type.equals(this.FUNCTION_INT)) {
        sbf.append(value);
      } else {
        sbf.append("\"");
        sbf.append(value);
        sbf.append("\"");
      }
    } else if (para_valueType.equals(this.PARA_TYPE_BOOLEAN)) {
      //			sbf.append("new Boolean(\"");
      sbf.append(value.toString());
      //			sbf.append("\")");
    } else {
      sbf.append(value);
    }

    return sbf.toString();
  }

  /**
   * 
   * 功能：设置discription值 即：描述
   * 
   * @author bing-zeng <br>
   *         Date ：2008-1-1 上午10:35:23
   * @param list
   *            规则明细集合
   * @return 返回表达试描述
   */
  public String setDescription(List list) {
    /* 连接规则描述 */
    StringBuffer string4discrption = new StringBuffer();

    /* 循环组合规则描述 */
    Iterator it = list.iterator();
    while (it.hasNext()) {
      XMLData xml = (XMLData) it.next();
      /* 左括号 */
      Object leftPare = xml.get("left_pare");
      if (null != leftPare)
        string4discrption.append(leftPare);
      string4discrption.append("  ");

      /* 左变量 */
      string4discrption.append(xml.get("left_paraname"));
      string4discrption.append("  ");

      /* 关系运算符 */
      string4discrption.append(xml.get("operator"));
      string4discrption.append("  ");

      /* 右变量 */
      string4discrption.append(xml.get("right_paraname"));
      string4discrption.append("  ");

      /* 右括号 */
      Object rightPare = xml.get("right_pare");
      if (null != rightPare)
        string4discrption.append(rightPare);
      string4discrption.append("  ");

      /* 逻辑运算符 */
      Object logicOperator = xml.get("logic_operator");
      if (null != logicOperator)
        string4discrption.append(logicOperator);
      string4discrption.append("  ");
    }
    return string4discrption.toString();
  }

  public ISysRegulationConf getRegulation() {
    if (regulation == null) {
      try {/* 从服务端取得 */
        regulation = (ISysRegulationConf) SessionUtil.getServerBean("sysRegulationManagerBO");
      } catch (Exception e) {
      }
    }
    return regulation;
  }

  public void setRegulation(ISysRegulationConf regulation) {
    this.regulation = regulation;
  }

}
