package gov.df.fap.service.workflow.billtype;

import gov.df.fap.api.dictionary.interfaces.IDictionary;
import gov.df.fap.api.gl.coa.ibs.ICoa;
import gov.df.fap.service.dictionary.DBOperation;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.memcache.MemCacheMap;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.xml.XMLData;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 交易凭证组件服务接口实现数据库访问类 对应设计：综合业务系统交易凭证详细设计
 * 
 * @version 1.0
 * @author victor
 * @since java 1.4.2
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Component
public class BillTypeDao extends DBOperation {
  private static Map ruleMap = MemCacheMap.getCacheMap(BillTypeDao.class);

  // 编号规则常量类型
  protected final int RULE_CONST_TYPE = 0;

  // 编号规则日期类型
  protected final int RULE_DATE_TYPE = 1;

  // 编号规则要素类型
  protected final int RULE_ELE_TYPE = 2;

  // 编号规则要素自增类型
  protected final int RULE_ELEINCREASE_TYPE = 3;

  // 储存断号信息
  Map breakMess = null;

  // 断号Id
  List breakIdList = null;

  @Autowired
  IDictionary dictionary = null;

  @Autowired
  ICoa coa;

  /**
   * 根据交易凭证ID获取交易凭证详细信息
   * 
   * @param billTypeID
   *            交易凭证ID
   * @return 交易凭证详细信息XMLData对象
   */
  public XMLData getBillTypeByID(String billTypeID) {
    //add by liuzw 20120322
    String rg_code = getRgCode();
    String setYear = getSetYear();

    //modify by liuzw 20120322
    XMLData result = null;
    List list = getBillType("and billtype_id='" + billTypeID + "' and rg_code='" + rg_code + "' and set_year ="
      + setYear);
    if (list.size() > 0) {
      // 结果肯定唯一
      result = (XMLData) list.get(0);
    } else {
      result = new XMLData();
    }
    return result;
  }

  /**
   * 根据交易凭证Code获取交易凭证详细信息
   * 
   * @param billTypeCode
   *            交易凭证Code
   * @return 交易凭证详细信息XMLData对象
   */
  public XMLData getBillTypeByCode(String billTypeCode) {
    //add by liuzw 20120322
    String rg_code = getRgCode();
    String setYear = getSetYear();

    //modify by liuzw 20120322
    XMLData result = null;
    List list = getBillType("and billtype_code='" + billTypeCode + "' and rg_code='" + rg_code + "' and set_year ="
      + setYear);
    if (list.size() > 0) {
      // 结果肯定唯一
      result = (XMLData) list.get(0);
    } else {
      result = new XMLData();
    }
    return result;
  }

  /**
   * 根据附加过滤条件获取交易凭证详细信息列表
   * 
   * @param plusSql
   *            附加sql条件(以and开始)
   * @return 交易凭证详细信息XMLData对象列表
   */
  public List getBillType(String plusSql) {
    List result = new ArrayList();
    StringBuffer strSQL = new StringBuffer();
    String set_year = getSetYear();
    //add by liuzw 20120323
    String rg_code = getRgCode();
    //modify by liuzw 20120323
    strSQL.append("select * from sys_billtype").append(Tools.addDbLink()).append(" a where set_year=").append(set_year)
      .append(" and rg_code = '").append(rg_code).append("'").append(plusSql == null ? "" : plusSql);
    // 设置不分页

    result = dao.findBySql(strSQL.toString());

    for (int i = 0; i < result.size(); i++) {
      XMLData data = (XMLData) result.get(i);
      String coa_id = (String) data.get("coa_id");
      try {
        if (coa_id != null && !coa_id.equals("")) {
          XMLData coaData = coa.getCoabyID(coa_id);
          if (coaData != null) {
            data.put("coaMap", coaData);
          }
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  /**
   * 根据交易凭证编码获取交易凭证合单规则
   * 
   * @param billTypeCode
   *            交易凭证编码
   * @return 交易凭证合单规则XMLData对象
   */
  public XMLData getBillCombination(String billTypeCode) {
    XMLData result = new XMLData();
    StringBuffer strSQL = new StringBuffer();
    String set_year = getSetYear();
    //add by liuzw 20120323
    String rg_code = getRgCode();
    //modify by liuzw 20120323
    strSQL.append("select a.billtype_id,a.billtype_code,a.billtype_name,a.coa_id,b.ele_code,")
      .append("(select ele_name from sys_element").append(Tools.addDbLink())
      .append(" where ele_code = upper(b.ele_code) ")
      .append("and set_year = b.set_year and rg_code = b.rg_code) as ele_name,b.level_num from sys_billtype")
      .append(Tools.addDbLink()).append(" a ").append("left join sys_billtype_assele").append(Tools.addDbLink())
      .append(" b on a.billtype_id = b.billtype_id and a.set_year= b.set_year and a.rg_code=b.rg_code")
      .append(" where a.set_year=? and a.rg_code=? and a.billtype_code =?");

    List list = dao.findBySql(strSQL.toString(), new String[] { set_year, rg_code, billTypeCode });
    boolean isFirst = true;// 标注是否第一条记录
    for (int i = 0; i < list.size(); i++) {
      XMLData detail = (XMLData) list.get(i);
      if (isFirst)// 第一条
      {
        isFirst = false;
        String coa_id = (String) detail.get("coa_id");
        result.put("billtype_id", detail.get("billtype_id"));
        result.put("billtype_code", detail.get("billtype_code"));
        result.put("billtype_name", detail.get("billtype_name"));

        try {
          if (coa_id != null && !coa_id.equals("")) {
            XMLData data = coa.getCoabyID(coa_id);
            if (data != null) {
              result.put("row", data.getRecordList());
            }
          }

        } catch (Exception e) {
          e.printStackTrace();
        }

      }
      detail.remove("billtype_id");
      detail.remove("billtype_code");
      detail.remove("billtype_name");
      detail.remove("coa_id");
      if (detail.get("ele_code") != null && !((String) detail.get("ele_code")).equals("")) {
        result.put("row", detail);
      }
    }
    return result;
  }

  /**
   * 根据交易凭证类型获取交易凭证当前编号,单例模式保证编号的正确性
   * 
   * @param billTypeCode
   *            交易凭证类型编码
   * @param billInfo
   *            凭证信息对象
   * @return 编号字符串
   * @throws 获取交易凭证编号过程异常
   */
  public String getBillNo(String billTypeCode, Map billInfo) throws Exception {
    List ruleLine = null;
    StringBuffer strSQL = new StringBuffer();
    // 获取交易凭证对应的单号规则
    //add by liuzw 20120323
    //update by xuzx1 20121114 ruleMap缓存 加入年度  作为key，大集中区别不同区划和年度
    String rg_code = getRgCode();
    if (ruleMap != null && ruleMap.containsKey(SessionUtil.getRgCode() + getSetYear() + billTypeCode)) {
      ruleLine = (List) ruleMap.get(SessionUtil.getRgCode() + getSetYear() + billTypeCode);
    } else {
      String setYear = getSetYear();
      strSQL
        .append("select b.* from sys_billtype")
        .append(Tools.addDbLink())
        .append(" a,sys_billnoruleline")
        .append(Tools.addDbLink())
        .append(" b where a.billnorule_id = b.billnorule_id ")
        .append(
          "and a.set_year = b.set_year and a.rg_code = b.rg_code and a.billtype_code =? and a.set_year =? and a.rg_code=? order by b.line_no");

      ruleLine = dao.findBySql(strSQL.toString(), new String[] { billTypeCode, setYear, rg_code });
      if (ruleLine.size() == 0) {
        throw new Exception("传入类型未对应任何单号规则,请检查相关设置!");
      }
      // if (ruleMap == null) {
      // ruleMap = new HashMap();
      // ruleMap.put(billTypeCode, ruleLine);
      // } else {
      // ruleMap.put(billTypeCode, ruleLine);
      // }
      ruleMap.put(SessionUtil.getRgCode() + getSetYear() + billTypeCode, ruleLine);
    }
    // 循环组合成单号
    return getCurrentBillNoValue(ruleLine, billInfo);
  }

  /**
   * add by 2017.8.3
   * 根据交易凭证类型获取交易凭证当前编号：先取编号前缀，根据编号前缀从断号表sys_billno_deleted表中查询
   * 如果断号存在，则取用断号并删除该断号
   * @param billTypeCode
   *            交易凭证类型编号
   * @param billInfo
   *            凭证信息对象
   * @return 编号字符串
   * @throws 获取交易凭证编号过程异常
   */
  public String getDeletedBillNo(String billTypeCode, Map billInfo) throws Exception {
    List ruleLine = null;
    StringBuffer strSQL = new StringBuffer();
    // 获取交易凭证对应的单号规则
    String rg_code = getRgCode();
    if (ruleMap != null && ruleMap.containsKey(SessionUtil.getRgCode() + getSetYear() + billTypeCode)) {
      ruleLine = (List) ruleMap.get(SessionUtil.getRgCode() + getSetYear() + billTypeCode);
    } else {
      String setYear = getSetYear();
      strSQL
        .append("select b.* from sys_billtype")
        .append(Tools.addDbLink())
        .append(" a,sys_billnoruleline")
        .append(Tools.addDbLink())
        .append(" b where a.billnorule_id = b.billnorule_id ")
        .append(
          "and a.set_year = b.set_year and a.rg_code = b.rg_code and a.billtype_code =? and a.set_year =? and a.rg_code=? order by b.line_no");

      ruleLine = dao.findBySql(strSQL.toString(), new String[] { billTypeCode, setYear, rg_code });
      if (ruleLine.size() == 0) {
        throw new Exception("传入类型未对应任何单号规则,请检查相关设置!");
      }
      ruleMap.put(SessionUtil.getRgCode() + getSetYear() + billTypeCode, ruleLine);
    }
    // 循环组合成单号
    return getBillNoForDeleted(ruleLine, billInfo);
  }

  //批量保存被撤回的凭证单号数据
  public void saveBillNoDeleted(List delPayVoucherBills, String billTypeCode) {
    for (int i = 0; i < delPayVoucherBills.size(); i++) { //给map填充属性
      XMLData map = (XMLData) delPayVoucherBills.get(i);
      ((XMLData) delPayVoucherBills.get(i)).put("id", UUIDRandom.generate());
      if (null == map.get("billtype_code") || "".equals(map.get("billtype_code"))) {
        ((XMLData) delPayVoucherBills.get(i)).put("billtype_code", billTypeCode);
      }
      ((XMLData) delPayVoucherBills.get(i)).put("set_year", getSetYear());
      ((XMLData) delPayVoucherBills.get(i)).put("rg_code", SessionUtil.getRgCode());
    }
    String insertSql = "insert into sys_billno_deleted (id, billtype_code, bill_no, set_year, rg_code) values(?, ?, ?, ?, ?)";
    String[] fieldNames = new String[] { "id", "billtype_code", "bill_no", "set_year", "rg_code" };
    dao.executeBatchBySql(insertSql, fieldNames, delPayVoucherBills);
  }

  public void clearCache(String billTypeCode) {
    // if (ruleMap != null) {
    // ruleMap.remove(billTypeCode);
    // }
    ruleMap.remove(SessionUtil.getRgCode() + getSetYear() + billTypeCode);
  }

  /**
   * 根据具体单号规则编码获取单号规则信息,单例模式保证编号的正确性
   * 
   * @param billNoRuleCode
   *            单号规则编码
   * @param billInfo
   *            凭证信息对象
   * @return 编号字符串
   * @throws 获取交易凭证编号过程异常
   */
  public String getBillNoByBillNoRuleCode(String billNoRuleCode, Map billInfo) throws Exception {
    List ruleLine = null;
    StringBuffer strSQL = new StringBuffer();
    String rg_code = getRgCode();
    strSQL.append("select b.* from sys_billnorule a,sys_billnoruleline b")
      .append(" where a.billnorule_id = b.billnorule_id ")
      .append(" and a.set_year = b.set_year and a.rg_code=b.rg_code")
      .append(" and (a.billnorule_code =? or a.billnorule_id=?) and a.set_year =? and a.rg_code=?")
      .append(" order by b.line_no");

    ruleLine = dao.findBySql(strSQL.toString(), new String[] { billNoRuleCode, billNoRuleCode, getSetYear(), rg_code });
    if (ruleLine.size() == 0) {
      throw new Exception("传入类型未对应任何单号规则,请检查相关设置!");
    }
    // 循环组合成单号
    return getCurrentBillNoValue(ruleLine, billInfo);
  }

  /**
   * 根据具体单号规则项列表获取单号规则信息,单例模式保证编号的正确性
   * 
   * @param ruleLine
   *            单号规则项列表
   * @param billInfo
   *            凭证信息对象
   * @return 编号字符串
   * @throws 获取单号过程异常
   */
  private String getCurrentBillNoValue(List ruleLine, Map billInfo) throws Exception {
    Session session = dao.getSession();
    Connection conn = session.connection();
    //
    StringBuffer billNo = new StringBuffer();
    try {
      // 循环组合成单号
      for (int i = 0; i < ruleLine.size(); i++) {
        Map ruleLineMap = (Map) ruleLine.get(i);
        String line_type = (String) ruleLineMap.get("line_type");
        String formatStr = (String) ruleLineMap.get("line_format");
        int format = -1;
        // 格式化段位值
        try {
          format = Integer.parseInt(formatStr);
        } catch (Exception e) {
          format = -1;
        }
        if (String.valueOf(RULE_ELEINCREASE_TYPE).equals(line_type)) {
          String maxNo = reLoadBillNo(ruleLineMap, billInfo);
          for (int num = maxNo.length() + 1; num <= format; num++) {
            maxNo = "0" + maxNo;
          }
          billNo.append(maxNo);
        } else if (String.valueOf(RULE_CONST_TYPE).equals(line_type)) {
          billNo.append(ruleLineMap.get("init_value"));
        } else if (String.valueOf(RULE_DATE_TYPE).equals(line_type)) {
          GregorianCalendar calendar = new GregorianCalendar();
          SimpleDateFormat df = new SimpleDateFormat(formatStr.toLowerCase().replaceAll("m", "M"));
          billNo.append(df.format(calendar.getTime()));
        } else if (String.valueOf(RULE_ELE_TYPE).equals(line_type)) {
          if (billInfo == null) {
            throw new Exception("未正确传入业务单据信息,无法生成对应单号!");
          }
          String ele_code = (String) ruleLineMap.get("ele_code");
          String level_num = (String) ruleLineMap.get("level_num");
          String ele_value = (String) billInfo.get((ele_code + "_id").toLowerCase());
          if (ele_value == null) {
            throw new Exception("业务单据" + ele_code + "信息不全,无法生成单号!");
          }
          Map map = locateEleOfLevel(ele_code, ele_value, level_num);
          if (map == null) {
            throw new Exception("根据参数传入信息,无法定位要素" + ele_code + "的" + level_num + "级");
          }
          if (format == 0)
            billNo.append(map.get("chr_code"));
          else if (format == 1)
            billNo.append(map.get("chr_name"));
          else if (format == 2)
            billNo.append(map.get("disp_code"));
        }
      }
      //
    } catch (Exception e) {
      conn.rollback();
      e.printStackTrace();
      throw e;
    } finally {
      dao.closeSession(session);
    }
    return billNo.toString();
  }

  private String getBillNoForDeleted(List ruleLine, Map billInfo) throws Exception {
    Session session = dao.getSession();
    Connection conn = session.connection();
    //断号前缀
    StringBuffer billNoPre = new StringBuffer();
    try {
      // 循环组合成单号
      for (int i = 0; i < ruleLine.size(); i++) {
        Map ruleLineMap = (Map) ruleLine.get(i);
        String line_type = (String) ruleLineMap.get("line_type");
        String formatStr = (String) ruleLineMap.get("line_format");
        int format = -1;
        // 格式化段位值
        try {
          format = Integer.parseInt(formatStr);
        } catch (Exception e) {
          format = -1;
        }
        if (String.valueOf(RULE_ELEINCREASE_TYPE).equals(line_type)) {
          billNoPre.append("%");
        } else if (String.valueOf(RULE_CONST_TYPE).equals(line_type)) {
          billNoPre.append(ruleLineMap.get("init_value"));
        } else if (String.valueOf(RULE_DATE_TYPE).equals(line_type)) {
          GregorianCalendar calendar = new GregorianCalendar();
          SimpleDateFormat df = new SimpleDateFormat(formatStr.toLowerCase().replaceAll("m", "M"));
          billNoPre.append(df.format(calendar.getTime()));
        } else if (String.valueOf(RULE_ELE_TYPE).equals(line_type)) {
          if (billInfo == null) {
            throw new Exception("未正确传入业务单据信息,无法生成对应单号!");
          }
          String ele_code = (String) ruleLineMap.get("ele_code");
          String level_num = (String) ruleLineMap.get("level_num");
          String ele_value = (String) billInfo.get((ele_code + "_id").toLowerCase());
          if (ele_value == null) {
            throw new Exception("业务单据" + ele_code + "信息不全,无法生成单号!");
          }
          Map map = locateEleOfLevel(ele_code, ele_value, level_num);
          if (map == null) {
            throw new Exception("根据参数传入信息,无法定位要素" + ele_code + "的" + level_num + "级");
          }
          if (format == 0)
            billNoPre.append(map.get("chr_code"));
          else if (format == 1)
            billNoPre.append(map.get("chr_name"));
          else if (format == 2)
            billNoPre.append(map.get("disp_code"));
        }
      }
      //根据单号规则拼装的bill_no前缀去sys_billno_deleted表中查询可用凭证号,并删除该断号
      StringBuffer findSql = new StringBuffer(
        "select id, billtype_code, bill_no from sys_billno_deleted where billtype_code = '");
      findSql.append(billInfo.get("billtype_code")).append("' and bill_no like '").append(billNoPre.toString())
        .append("' and set_year = '").append(getSetYear()).append("' and rg_code = '").append(SessionUtil.getRgCode())
        .append("' order by bill_no");
      List<XMLData> list = dao.findBySql(findSql.toString());
      String bill_no = null;
      if (list != null && list.size() > 0) {
        XMLData xmlData = list.get(0); //取最小的bill_no，由于加了order by bill_no排序，第一个为最小
        bill_no = xmlData.get("bill_no").toString();
        if (bill_no != null && !"".equals(bill_no)) {
          //删除被撤销的凭证单号表
          StringBuffer deleteSql = new StringBuffer();
          deleteSql.append("delete from sys_billno_deleted where id = '").append(xmlData.get("id")).append("'");
          dao.executeBySql(deleteSql.toString());
        }
      }
      return bill_no;
    } catch (Exception e) {
      conn.rollback();
      e.printStackTrace();
      throw e;
    } finally {
      dao.closeSession(session);
    }

  }

  /**
   * 根据当前单据要素配置重新获取要素自增序列当前最大值,并进行缓存
   * 
   * @param ruleLineMap
   *            要素自增序列配置
   * @param billInfo
   *            当前单据信息
   * @return 当前最大值
   * @throws Exception
   *             单号生成异常
   */
  protected String reLoadBillNo(Map ruleLineMap, Map billInfo) throws Exception {
    String maxNo = "";
    StringBuffer strSQL = new StringBuffer();
    //add by liuzw 20120323
    String set_year = getSetYear();
    String rg_code = getRgCode();

    String billnoruleline_id = (String) ruleLineMap.get("billnoruleline_id");
    // 初始值
    String init_value = (String) ruleLineMap.get("init_value");
    // 格式化初始值并进行保护
    try {
      Integer.parseInt(init_value);
    } catch (Exception e) {
      init_value = "0";
    }
    List eleList = null;
    if (!ruleLineMap.containsKey("elelist") || !(ruleLineMap.get("elelist") instanceof List)) {
      // 根据编号规则关心的要素组合值字符串
      strSQL.append("select * from sys_billnoruleele").append(Tools.addDbLink())
        .append(" where billnoruleline_id = ? and set_year=? and rg_code=? order by ele_code");

      eleList = dao.findBySql(strSQL.toString(), new Object[] { billnoruleline_id, set_year, rg_code });
      // 缓存
      ruleLineMap.put("elelist", eleList);
    } else {
      eleList = (List) ruleLineMap.get("elelist");
    }
    StringBuffer ruleValueStr = new StringBuffer();
    for (int j = 0; j < eleList.size(); j++) {
      Map eleMap = (Map) eleList.get(j);
      String ele_code = (String) eleMap.get("ele_code");
      String level_num = (String) eleMap.get("level_num");
      // 为了满足不配置任何要素要求
      if (billInfo == null) {
        throw new Exception("未正确传入业务单据信息,无法生成对应单号!");
      }
      String ele_value = (String) billInfo.get((ele_code + "_id").toLowerCase());
      if (ele_value == null) {
        throw new Exception("业务单据" + ele_code + "信息不全,无法生成单号!");
      }
      Map map = locateEleOfLevel(ele_code, ele_value, level_num);
      if (map != null) {
        ruleValueStr.append(map.get("chr_id"));
      } else {
        throw new Exception("根据参数传入信息,无法定位要素" + ele_code + ",chr_id=" + ele_value + "的" + level_num + "级");
      }
    }
    /**
     * 增加变量绑定 add by zhangsheng at 2012.12.1 begin
     */
    strSQL.delete(0, strSQL.length());
    // 匹配查找当前序列值
    strSQL.append("select max_no - 1 as max_no from sys_billno where");
    if (!ruleValueStr.toString().equals("")) {
      strSQL.append(" ele_value = ? and billnoruleline_id = ? and set_year = ? and rg_code = ?");
    } else {
      // 未配置任何要素则默认为最简单的sequence,注意由于ele_value本身就是主键,所以特殊用NULL替代null
      strSQL.append(" ele_value = 'NULL' and billnoruleline_id = ? and set_year = ? and rg_code = ?");
    }
    String querySQL = strSQL.toString();

    // 直接修改max_no = max_no + 1
    strSQL.delete(0, strSQL.length());
    boolean isModify = true;// 如果更新到数据说明找到主键并更新 如果没有就插入一条新的
    strSQL.append("update sys_billno set max_no = max_no + 1 where billnoruleline_id = ?");
    if (!ruleValueStr.toString().equals("")) {
      strSQL.append(" and ele_value = ? and set_year = ? and rg_code = ?");
      isModify = modifyBySql(strSQL.toString(), new Object[] { billnoruleline_id, ruleValueStr.toString(), set_year,
        rg_code });
    } else {
      strSQL.append(" and ele_value = 'NULL' and set_year = ? and rg_code = ?");
      isModify = modifyBySql(strSQL.toString(), new Object[] { billnoruleline_id, set_year, rg_code });
    }

    if (isModify) {
      List maxList = null;
      if (!ruleValueStr.toString().equals("")) {
        maxList = dao.findBySql(querySQL,
          new Object[] { ruleValueStr.toString(), billnoruleline_id, set_year, rg_code });
      } else {
        maxList = dao.findBySql(querySQL, new Object[] { billnoruleline_id, set_year, rg_code });
      }
      maxNo = (String) ((Map) maxList.get(0)).get("max_no");
      /**
       * 增加变量绑定 add by zhangsheng at 2012.12.1 end
       */
    } else {
      // 否则插入一条数据,并置init_value = init_value + 1
      maxNo = init_value;
      strSQL.delete(0, strSQL.length());
      strSQL.append("insert into sys_billno").append(Tools.addDbLink())
        .append("(billnoruleline_id,max_no,latest_op_date,latest_op_user,rg_code,set_year,ele_value) ")
        .append("values('").append(billnoruleline_id).append("',").append(init_value).append("+1,'")
        .append(DateHandler.getLastVerTime()).append("','").append(getUserId()).append("','").append(rg_code)
        .append("',").append(getSetYear());
      if (!ruleValueStr.toString().equals("")) {
        strSQL.append(",'").append(ruleValueStr).append("')");
      } else {
        strSQL.append(",'NULL')");
      }
      dao.executeBySql(strSQL.toString());
    }

    return maxNo;
  }

  /**
   * 根据传入参数定位对应级次的基础数据
   * 
   * @param ele_code
   *            基础数据表简称
   * @param cur_id
   *            当前基础数据值
   * @param level_num
   *            需定位的级次
   * @return 重定位级次后的要素对象
   */
  private Map locateEleOfLevel(String ele_code, String cur_id, String level_num) {
    Map valueMap = null;
    List eleValue = null;
    if (!level_num.equals("-1")) {
      // 先从自身定位对应级次id,由于固定ID查找,所以不添加权限过滤
      List list = dictionary.findEleValues(ele_code, null, new String[] { "chr_id" + level_num + " as chr_id" }, false,
        null, null, " and chr_id='" + cur_id + "'");
      if (list.size() > 0) {
        eleValue = dictionary.findEleValues(ele_code, null, new String[] { "chr_code", "chr_name", "disp_code",
          "chr_id" }, false, null, null, " and chr_id = '" + ((Map) list.get(0)).get("chr_id") + "'");
      }

    } else {
      // 无需加上底级条件is_leaf=1,因为如果未找到则默认自身
      eleValue = dictionary.findEleValues(ele_code, null,
        new String[] { "chr_code", "chr_name", "disp_code", "chr_id" }, false, null, null, " and chr_id='" + cur_id
          + "'");
    }
    if (eleValue != null && eleValue.size() > 0) {
      valueMap = (Map) eleValue.get(0);
    }
    return valueMap;
  }

  public IDictionary getDictionary() {
    return dictionary;
  }

  public void setDictionary(IDictionary dictionary) {
    this.dictionary = dictionary;
  }

  /**
   * 获取定值处理后的列表
   * 
   * @param billTypeCode
   *            交易凭证类型编号
   * @param field_Code
   *            要素字段
   * @return 编号字符串
   * @throws 获取交易凭证编号过程异常
   */
  public List getEleRule(String billTypecode, String field_Code) {

    StringBuffer tmp_sql = new StringBuffer("select a.* from SYS_BILLTYPE_VALUESET").append(Tools.addDbLink())
      .append(" a,sys_billtype").append(Tools.addDbLink())
      .append(" b where a.BILLTYPE_ID=b.billtype_id and b.BILLTYPE_CODE='").append(billTypecode).append("'");
    if (field_Code != null) {
      tmp_sql.append(" and a.FIELD_CODE='").append(field_Code).append("'");
    }
    tmp_sql.append(" and a.rg_code='");//add by kongcy at 2012-05-04
    tmp_sql.append(SessionUtil.getRgCode());
    tmp_sql.append("' and a.set_year=");
    tmp_sql.append(SessionUtil.getLoginYear());
    tmp_sql.append(" and b.rg_code='");
    tmp_sql.append(SessionUtil.getRgCode());
    tmp_sql.append("' and b.set_year=");
    tmp_sql.append(SessionUtil.getLoginYear());
    return this.getDao().findBySql(tmp_sql.toString());

  }

  /**
   * 确认单号是否被占用
   * 
   * @param billTypeCode
   *            交易凭证类型编号
   * @param billNo
   *            单号
   * @return 如果确认被占用 返回true 否则false
   * @author lgc
   * @throws Exception
   */
  public boolean isConfirm(String billTypeCode, String billNo) throws Exception {
    StringBuffer sqlConfirm = new StringBuffer();
    List ruleLine = null;
    ruleLine = this.getRuleLine(billTypeCode);
    for (int i = 0; i < ruleLine.size(); i++) {
      String billnoruleline_id = ((XMLData) ruleLine.get(i)).get("billnoruleline_id").toString();
      sqlConfirm.append("'" + billnoruleline_id + "'");
      if (i != ruleLine.size() - 1) {
        sqlConfirm.append(",");
      }
    }
    String sql = "update SYS_BILLNOBREAK set BREAKNO_STATUS = '2' where BILL_NO ='" + billNo
      + "' and BILLNORULELINE_ID in (" + sqlConfirm.toString() + ")";
    return modifyBySql(sql.toString());

  }

  /**
   * 根据
   * 
   * @param ruleLineMap
   * @param billInfo
   * @return maxNo
   * @throws Exception
   */
  public String getMaxIncreaseValue(Map ruleLineMap, Map billInfo) throws Exception {
    String maxNo = "";
    StringBuffer strSQL = new StringBuffer();
    //add by liuzw 20120323
    String rg_code = getRgCode();

    String billnoruleline_id = (String) ruleLineMap.get("billnoruleline_id");
    // 初始值
    String init_value = (String) ruleLineMap.get("init_value");
    // 格式化初始值并进行保护
    try {
      Integer.parseInt(init_value);
    } catch (Exception e) {
      init_value = "0";
    }
    String ruleValueStr = null;
    ruleValueStr = this.getEleValue(ruleLineMap, billInfo);
    // 匹配查找当前序列值
    strSQL.delete(0, strSQL.length());
    if (!ruleValueStr.equals("")) {
      strSQL.append("select max_no -1 as max_no from sys_billno").append(Tools.addDbLink())
        .append(" where ele_value='").append(ruleValueStr).append("' and billnoruleline_id = '")
        .append(billnoruleline_id).append("'");
    } else {
      // 未配置任何要素则默认为最简单的sequence,注意由于ele_value本身就是主键,所以特殊用NULL替代null
      strSQL.append("select max_no -1 as max_no from sys_billno").append(Tools.addDbLink())
        .append(" where ele_value = 'NULL'").append(" and billnoruleline_id = '").append(billnoruleline_id).append("'");
    }
    String qeerySQL = strSQL.toString();

    strSQL.delete(0, strSQL.length());
    strSQL.append("update sys_billno").append(Tools.addDbLink())
      .append(" set max_no=max_no+1 where billnoruleline_id = '").append(billnoruleline_id);
    if (!ruleValueStr.toString().equals("")) {
      strSQL.append("' and ele_value='").append(ruleValueStr).append("'");
    } else {
      strSQL.append("' and ele_value = 'NULL'");
    }
    if (modifyBySql(strSQL.toString()))// 更新max = max+1更新到说明有数据查询更新不到插入新的
    {
      List maxList = dao.findBySql(qeerySQL);// 有更新数据肯定能查询到
      maxNo = (String) ((Map) maxList.get(0)).get("max_no");
    }

    else {
      // 否则插入一条数据,并置init_value = init_value + 1
      //modify by liuzw 20120323
      maxNo = init_value;
      strSQL.delete(0, strSQL.length());
      strSQL.append("insert into sys_billno").append(Tools.addDbLink())
        .append("(billnoruleline_id,max_no,latest_op_date,latest_op_user,rg_code,set_year,ele_value) ")
        .append("values('").append(billnoruleline_id).append("',").append(init_value).append("+1,'")
        .append(DateHandler.getLastVerTime()).append("','").append(getUserId()).append("','").append(rg_code)
        .append("',").append(getSetYear());
      if (!ruleValueStr.toString().equals("")) {
        strSQL.append(",'").append(ruleValueStr).append("')");
      } else {
        strSQL.append(",'NULL')");
      }
      modifyBySql(strSQL.toString());
    }
    breakMess = new HashMap();
    breakMess.put("maxNo", maxNo);
    breakMess.put("billnoruleline_id", billnoruleline_id);
    breakMess.put("ele_value", ruleValueStr);
    breakMess.put("init_value", init_value);

    return maxNo;
  }

  public String getBillNo1(String billTypeCode, Map billInfo, String breakFlag) throws Exception {
    // 循环组合成单号
    return getEleIncreaseValue(this.getRuleLine(billTypeCode), billInfo, breakFlag);
  }

  private String getEleIncreaseValue(List ruleLine, Map billInfo, String breakFlag) throws Exception {
    StringBuffer billNo = new StringBuffer();
    // 循环组合成单号
    for (int i = 0; i < ruleLine.size(); i++) {
      Map ruleLineMap = (Map) ruleLine.get(i);
      String line_type = (String) ruleLineMap.get("line_type");
      String formatStr = (String) ruleLineMap.get("line_format");
      int format = -1;
      // 格式化段位值
      try {
        format = Integer.parseInt(formatStr);
      } catch (Exception e) {
        format = -1;
      }
      if (String.valueOf(RULE_ELEINCREASE_TYPE).equals(line_type)) {
        List isBreak = null;
        String maxNo = null;
        String billnoruleline_id = (String) ruleLineMap.get("billnoruleline_id");
        if (breakFlag.equals("0")) {// 0:只取断号 1：优先取断号 2：正常取
          StringBuffer sql = new StringBuffer();
          sql.delete(0, sql.length());
          sql
            .append(
              "select LAST_VER,break_id,BILL_NO,BREAKNO_STATUS from sys_billnobreak where BILLNORULELINE_ID =? and BREAKNO_STATUS ='3'")
            .append(" or BREAKNO_STATUS='1' order by BILL_NO");
          isBreak = this.getDao().findBySql(sql.toString(), new Object[] { billnoruleline_id });
          String litime2 = DateHandler.getLastVerTime();
          if (isBreak != null && isBreak.size() > 0) {
            for (int a = 0; a < isBreak.size(); a++) {
              XMLData breakData = (XMLData) isBreak.get(a);
              String breakno_status = breakData.get("breakno_status").toString();
              // 当其状态为已占用并且超过规定时间 或者是未占用时 则返回单号并且将状态改为已占用 时间置为本地时间
              if ("1".equals(breakno_status) && this.isFourHour(breakData.get("last_ver").toString(), litime2)
                || "3".equals(breakno_status)) {
                sql.delete(0, sql.length());
                sql.append("update sys_billnobreak set BREAKNO_STATUS='1',LAST_VER='" + litime2 + "' where break_id='"
                  + breakData.get("break_id").toString() + "'");
                this.getDao().executeBySql(sql.toString());
                return breakData.get("bill_no").toString();
              }
            }
            return null;
          } else {
            return null;
          }

        } else if (breakFlag.equals("1")) {

          String ele_value = this.getEleValue(ruleLineMap, billInfo);
          StringBuffer sql = new StringBuffer();
          String breakNoStatus0 = "0";// 未使用
          String breakNoStatus1 = "1";// 占用
          String breakNoStatus3 = "3";// 断号未使用

          if (ele_value == null || ele_value.equals("")) {
            sql.delete(0, sql.length());
            sql
              .append(
                "select BREAK_ID,BREAK_NO,LAST_VER,BREAKNO_STATUS from sys_billnobreak where BILLNORULELINE_ID =? and ELE_VALUE is null")
              .append("  and BREAKNO_STATUS=? or BREAKNO_STATUS=? or BREAKNO_STATUS =? order by BREAKNO_STATUS desc");
            isBreak = this.getDao().findBySql(sql.toString(),
              new Object[] { billnoruleline_id, breakNoStatus1, breakNoStatus3, breakNoStatus0 });
          } else {
            sql.delete(0, sql.length());
            sql
              .append(
                "select BREAK_ID,BREAK_NO,LAST_VER,BREAKNO_STATUS from sys_billnobreak where BILLNORULELINE_ID =? and ELE_VALUE=?")
              .append("  and BREAKNO_STATUS=? or BREAKNO_STATUS=? or BREAKNO_STATUS =? order by BREAKNO_STATUS desc");
            isBreak = this.getDao().findBySql(sql.toString(),
              new Object[] { billnoruleline_id, ele_value, breakNoStatus1, breakNoStatus3, breakNoStatus0 });
          }

          if (isBreak != null && isBreak.size() > 0) {
            breakIdList = new ArrayList();
            String litime2 = DateHandler.getLastVerTime();
            for (int j = 0; j < isBreak.size(); j++) {
              XMLData breakData = (XMLData) isBreak.get(j);
              if (breakData.get("breakno_status").equals("1") || breakData.get("breakno_status").equals("3")) {
                if (this.isFourHour(breakData.get("last_ver").toString(), litime2)) {
                  maxNo = breakData.get("break_no").toString();
                  breakIdList.add(breakData.get("break_id").toString());
                  break;
                }
              } else if (breakData.get("breakno_status").equals("0")) {
                maxNo = breakData.get("break_no").toString();
                breakIdList.add(breakData.get("break_id").toString());
                break;
              } else {
                maxNo = getMaxIncreaseValue(ruleLineMap, billInfo);
                createBillNoBreak(null);
                break;
              }
            }
          } else {
            maxNo = getMaxIncreaseValue(ruleLineMap, billInfo);
            createBillNoBreak(null);
          }
        } else {
          maxNo = getMaxIncreaseValue(ruleLineMap, billInfo);
          createBillNoBreak(null);
        }

        for (int num = maxNo.length() + 1; num <= format; num++) {
          maxNo = "0" + maxNo;
        }
        billNo.append(maxNo);
      } else if (String.valueOf(RULE_CONST_TYPE).equals(line_type)) {
        billNo.append(ruleLineMap.get("init_value"));
      } else if (String.valueOf(RULE_DATE_TYPE).equals(line_type)) {
        GregorianCalendar calendar = new GregorianCalendar();
        SimpleDateFormat df = new SimpleDateFormat(formatStr.toLowerCase().replaceAll("m", "M"));
        billNo.append(df.format(calendar.getTime()));
      } else if (String.valueOf(RULE_ELE_TYPE).equals(line_type)) {
        if (billInfo == null) {
          throw new Exception("未正确传入业务单据信息,无法生成对应单号!");
        }
        String ele_code = (String) ruleLineMap.get("ele_code");
        String level_num = (String) ruleLineMap.get("level_num");
        String ele_value = (String) billInfo.get((ele_code + "_id").toLowerCase());
        if (ele_value == null) {
          throw new Exception("业务单据" + ele_code + "信息不全,无法生成单号!");
        }
        Map map = locateEleOfLevel(ele_code, ele_value, level_num);
        if (map == null) {
          throw new Exception("根据参数传入信息,无法定位要素" + ele_code + "的" + level_num + "级");
        }
        if (format == 0)
          billNo.append(map.get("chr_code"));
        else if (format == 1)
          billNo.append(map.get("chr_name"));
        else if (format == 2)
          billNo.append(map.get("disp_code"));
      }
    }
    // 当要返回单号的时候 将此单号在断号表中的状态设置成占用状态 1
    this.updateillNo(billNo.toString());
    return billNo.toString();
  }

  /**
   * 初始化10个单号
   * 
   * @param billNo
   *            单号
   */
  public void createBillNoBreak(String billNo) {
    StringBuffer init_valueSql = new StringBuffer();
    //add by liuzw 20120323
    String set_year = getSetYear();
    String rg_code = getRgCode();

    int maxNo = Integer.parseInt(breakMess.get("maxNo").toString());
    int init_value = 10;
    //modify by liuzw 20120323
    for (int i = maxNo; i < maxNo + init_value; i++) {
      init_valueSql.delete(0, init_valueSql.length());
      String breakId = UUIDRandom.generate();
      init_valueSql
        .append("insert into SYS_BILLNOBREAK ")
        .append(
          "(BREAK_ID, BILLNORULELINE_ID, ELE_VALUE, BILL_NO, BREAK_NO, BREAKNO_STATUS, LAST_VER, SET_YEAR, RG_CODE)")
        .append("values (").append(breakId).append(", '").append(breakMess.get("billnoruleline_id")).append("','")
        .append(breakMess.get("ele_value")).append("','").append(billNo).append("',").append(i).append(",'")
        .append("0").append("','").append(DateHandler.getLastVerTime()).append("',").append(set_year).append(",'")
        .append(rg_code).append("')");
      this.getDao().executeBySql(init_valueSql.toString());
      if (i == maxNo) {
        breakIdList = new ArrayList();
        breakIdList.add(breakId);
      }
    }
    breakMess = null;
  }

  /**
   * 将占用的单号状态设置为1
   * 
   * @param billNo
   *            单号
   */
  public void updateillNo(String billNo) {
    for (int i = 0; i < breakIdList.size(); i++) {
      String sql = "update SYS_BILLNOBREAK set BILL_NO ='" + billNo + "', BREAKNO_STATUS='1' where  break_id='"
        + breakIdList.get(i).toString() + "'";
      this.getDao().executeBySql(sql);

    }
    breakIdList = null;
  }

  /**
   * 得到ele_value
   * 
   * @param ruleLineMap
   * @param billInfo
   * @return
   * @throws Exception
   */
  public String getEleValue(Map ruleLineMap, Map billInfo) throws Exception {
    List eleList = null;
    String billnoruleline_id = (String) ruleLineMap.get("billnoruleline_id");
    StringBuffer strSQL = new StringBuffer();
    if (!ruleLineMap.containsKey("elelist") || !(ruleLineMap.get("elelist") instanceof List)) {
      // 根据编号规则关心的要素组合值字符串
      strSQL.append("select * from sys_billnoruleele").append(Tools.addDbLink()).append(" where billnoruleline_id = '")
        .append(billnoruleline_id).append("' order by ele_code");

      eleList = dao.findBySql(strSQL.toString());
      // 缓存
      ruleLineMap.put("elelist", eleList);
    } else {
      eleList = (List) ruleLineMap.get("elelist");
    }
    StringBuffer ruleValueStr = new StringBuffer();
    String ele_value = null;
    for (int j = 0; j < eleList.size(); j++) {
      Map eleMap = (Map) eleList.get(j);
      String ele_code = (String) eleMap.get("ele_code");
      String level_num = (String) eleMap.get("level_num");
      // 为了满足不配置任何要素要求
      if (billInfo == null) {
        throw new Exception("未正确传入业务单据信息,无法生成对应单号!");
      }
      ele_value = (String) billInfo.get((ele_code + "_id").toLowerCase());
      if (ele_value == null) {
        throw new Exception("业务单据" + ele_code + "信息不全,无法生成单号!");
      }
      Map map = locateEleOfLevel(ele_code, ele_value, level_num);
      if (map != null) {
        ruleValueStr.append(map.get("chr_id"));
      } else {
        throw new Exception("根据参数传入信息,无法定位要素" + ele_code + "的" + level_num + "级");
      }
    }
    return ruleValueStr.toString();
  }

  public List getRuleLine(String billTypeCode) throws Exception {
    List ruleLine = null;
    StringBuffer strSQL = new StringBuffer();
    String rg_code = getRgCode();
    // 获取交易凭证对应的单号规则
    //update by xuzx1 20121114 ruleMap缓存 加入年度  作为key，大集中区别不同区划和年度
    if (ruleMap != null && ruleMap.containsKey(SessionUtil.getRgCode() + getSetYear() + billTypeCode)) {
      ruleLine = (List) ruleMap.get(SessionUtil.getRgCode() + getSetYear() + billTypeCode);
    } else {
      strSQL.append("select b.* from sys_billtype a,sys_billnoruleline b where a.billnorule_id = b.billnorule_id ")
        .append(" and a.set_year = b.set_year and a.rg_code = b.rg_code ")
        .append(" and a.billtype_code = ? and a.set_year = ? and a.rg_code = ? order by b.line_no");
      ruleLine = dao.findBySql(strSQL.toString(), new String[] { billTypeCode, getSetYear(), rg_code });
      if (ruleLine.size() == 0) {
        throw new Exception("传入类型未对应任何单号规则,请检查相关设置!");
      }
      ruleMap.put(SessionUtil.getRgCode() + getSetYear() + billTypeCode, ruleLine);
    }
    return ruleLine;
  }

  /**
   * 单号回收
   * 
   * @param billNo
   *            单号
   * @param recoveryFlag
   *            0：不保存历史的回收
   * @return 单号回收是否成功 true：回收成功 false:回收失败
   * @throws Exception
   * @author lgc
   */
  public boolean recoveryBillNo(String billTypeCode, String billNo) throws Exception {
    List ruleLine = null;
    ruleLine = this.getRuleLine(billTypeCode);
    //add by liuzw 20120323
    String rg_code = getRgCode();
    String set_year = getSetYear();

    StringBuffer init_valueSql = new StringBuffer();

    // 删除表中存在的bill_no记录
    StringBuffer delete_existNo = new StringBuffer();

    delete_existNo.append("delete from SYS_BILLNOBREAK where BILL_NO = '");
    delete_existNo.append(billNo).append("' and rg_code='").append(rg_code).append("' and set_year='").append(set_year)
      .append("'");
    this.deleteBySql(delete_existNo.toString());

    //modify by liuzw 20120323
    for (int i = 0; i < ruleLine.size(); i++) {
      Map ruleLineMap = (Map) ruleLine.get(i);
      String line_type = (String) ruleLineMap.get("line_type");
      if (String.valueOf(RULE_ELEINCREASE_TYPE).equals(line_type)) {
        String billnoruleline_id = ((XMLData) ruleLine.get(i)).get("billnoruleline_id").toString();
        init_valueSql.delete(0, init_valueSql.length());
        String breakId = UUIDRandom.generate();
        init_valueSql.append("insert into SYS_BILLNOBREAK ")
          .append("(BREAK_ID, BILLNORULELINE_ID,BILL_NO,BREAKNO_STATUS, LAST_VER, SET_YEAR, RG_CODE)")
          .append("values ('").append(breakId).append("', '").append(billnoruleline_id).append("','").append(billNo)
          .append("','3").append("','").append(DateHandler.getLastVerTime()).append("',").append(set_year).append(",'")
          .append(rg_code).append("')");
        if (!modifyBySql(init_valueSql.toString())) {
          return false;
        }
      }
    }
    // String sql =
    // "update SYS_BILLNOBREAK set BREAKNO_STATUS=3 where BILL_NO = '"+billNo+"' and BILLNORULELINE_ID in ("+sqlConfirm.toString()+")";
    // StringBuffer init_valueSql = new StringBuffer();
    // String breakId = UUIDRandom.generateNumberBySeq("SEQ_SYS_FRAME_ID");
    // init_valueSql.append( "insert into SYS_BILLNOBREAK ").
    // append("(BREAK_ID, BILLNORULELINE_ID, ELE_VALUE, BILL_NO,BREAKNO_STATUS, USED_TIME)").
    // append("values (").append(breakId).append(", '").append(
    // "").append("','','").append(billNo).append("',")
    // .append(",'").append("0").append("','").append(DateHandler.getLastVerTime()).append("')");
    // this.dao.executeBySql(init_valueSql.toString());

    return true;
  }

  // 时间是否超过四个小时
  public boolean isFourHour(String limitDate1, String limitDate2) {
    DateHandler dh = new DateHandler();
    int bigTime = Integer.parseInt(SessionUtil.getParaMap().get("breakno_time").toString());
    if (dh.getLongCompare(limitDate1, limitDate2) >= 1) {
      return true;
    } else {
      int limit2 = Integer.parseInt(limitDate2.substring(11, 13));
      int limit1 = Integer.parseInt(limitDate1.substring(11, 13));
      if (limit2 - limit1 > bigTime) {
        return true;
      }
    }
    return false;
  }

  /**
   * 得到行政区划代码
   * 
   * @return
   * 
   * add by liuzw 20120326
   */
  private String getRgCode() {
    String rg_code = SessionUtil.getRgCode();
    return rg_code;
  }

  /**
   * 得到系统业务年度
   * 
   * @return
   * 
   * add by liuzw 20120326
   */
  @Override
  public String getSetYear() {
    String set_year = SessionUtil.getUserInfoContext().getSetYear();
    if (set_year == null || set_year.equalsIgnoreCase("")) {
      set_year = String.valueOf(DateHandler.getCurrentYear());
    }
    return set_year;
  }
}
