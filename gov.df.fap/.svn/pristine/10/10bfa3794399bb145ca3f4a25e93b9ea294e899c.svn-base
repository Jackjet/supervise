package gov.df.fap.service.rule;

import gov.df.fap.api.rule.IRule;
import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.bean.util.FBaseDTO;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.Properties.PropertiesUtil;
import gov.df.fap.util.xml.XMLData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RuleBO implements IRule {

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao;

  /**
   * 
   * 查询主权限和追加权限的权限要素
   *
   */
  private final static String UNION_SELECT_SQL = "SELECT DISTINCT B.ELE_CODE  FROM SYS_RIGHT_GROUP A, SYS_RIGHT_GROUP_TYPE B WHERE A.RIGHT_GROUP_ID = B.RIGHT_GROUP_ID  AND (A.RIGHT_GROUP_DESCRIBE='001' OR A.RIGHT_GROUP_DESCRIBE='002')  AND  A.RIGHT_TYPE='1'  AND  A.RULE_ID=? ";

  /**
   * 查询排除权限的权限要素
   */
  private final static String MINUS_SELECT_SQL = "SELECT DISTINCT B.ELE_CODE FROM SYS_RIGHT_GROUP A, SYS_RIGHT_GROUP_TYPE B WHERE A.RIGHT_GROUP_ID = B.RIGHT_GROUP_ID AND  A.RIGHT_GROUP_DESCRIBE='003' AND  A.RIGHT_TYPE='1'  AND  A.RULE_ID=?";

  /**
   * 清空批量定值 临时表
   */
  private final static String CLEAR_RULE_CATCH_SQL = "delete sys_rule_data_catch";

  private final static String VALUESET_DEFAULT_SQL = "select t.* from sys_billtype_valueset t, sys_billtype tt where tt.billtype_code = ? and t.valueset_type = 0 and t.billtype_id = tt.billtype_id and t.rg_code = tt.rg_code and t.rg_code=? and t.set_year=?";

  /**
   * 由交易凭证编码获取要素定值要素信息
   */
  private final static String VALUESET_RULE_BILLTYPE_SQL = "select c.rule_id,a.ele_rule_id, b.ele_code ele_code_alias, c.ele_value, c.ele_code, c.ele_name from SYS_BILLTYPE_VALUESET a, SYS_ELE_RULE b, SYS_ELE_RULE_DETAIL c , SYS_BILLTYPE d where a.ele_rule_id=b.ele_rule_id and b.ele_rule_id=c.ele_rule_id and a.billtype_id= d.billtype_id and a.rg_code =b.rg_code and b.rg_code = c.rg_code and a.rg_code=d.rg_code and d.billtype_code = ? and a.rg_code=? and a.set_year=?";

  /**
   * 批量定值缓存表字段
   */
  private static List RULE_DATA_CATCH_COL = null;

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  /**
   * 校验数据是否在范围内(如:记账范围，匹配处理)
   * 
   * @param ruleid-------------
   * @param baseDTO------------
   * @return true或false
   * @throws Exception---------错误信息
   */
  public boolean isMatch(String ruleid, FBaseDTO baseDTO) throws Exception {
    // 通用查询SQL
    String select_sql = "";

    // 最终查询SQL
    String return_select_sql = "select 1 from dual where 1=1";

    // 主权限和追加权限的查询SQL
    String union_select_sql = " and (1=0 ";

    // 排除权限的查询SQL
    String minus_select_sql = "";
    List select_list = new ArrayList();
    List right_group_list = new ArrayList();

    // 查询规则的基本信息
    select_sql = "select * from sys_rule" + Tools.addDbLink() + " where rule_id=? and rg_code=? and set_year=?";
    select_list = dao.findBySql(select_sql,
      new String[] { ruleid, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
    if (select_list == null || select_list.size() == 0) {
      // throw new Exception("不存在该条规则：" + ruleid);
      return false;
    } else {
      // 如果该规则是全部权限，则直接返回TRUE
      if (((Map) select_list.get(0)).get("right_type").equals("0")) {
        return true;
      }
    }

    // 查询该规则对应的所有权限组
    select_sql = "select * from sys_right_group" + Tools.addDbLink() + " where rule_id=? and rg_code=? and set_year=?";
    right_group_list = dao.findBySql(select_sql,
      new String[] { ruleid, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });

    // 循环调用权限组
    for (int i = 0; i < right_group_list.size(); i++) {
      String describe = ((Map) right_group_list.get(i)).get("right_group_describe").toString();
      String right_group_id = ((Map) right_group_list.get(i)).get("right_group_id").toString();
      // 如果是主权限或者是追加权限
      if (describe.equals("001") || describe.equals("002")) {
        union_select_sql = union_select_sql + getUnionGroupSql(right_group_id, baseDTO);
      }
      // 如果是排除权限
      if (describe.equals("003")) {
        minus_select_sql = minus_select_sql + getMinusGroupSql(right_group_id, baseDTO);
      }
    }

    // 拼成最终查询SQL
    return_select_sql = return_select_sql + union_select_sql + ") " + minus_select_sql;
    select_list = dao.findBySql(return_select_sql);
    // 如果结果为空，则返回FALSE；如果条数》1，则返回TRUE
    if (select_list == null || select_list.size() == 0) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * 得到主权限和追加权限的SQL语句
   * 
   * @throws Exception---------错误信息
   */
  private String getUnionGroupSql(String right_group_id, Object inputXml) {
    String return_sql = " Or Exists (select 1 from dual where 1=1";
    List select_list = null;
    // 查找该权限组的TYPE信息
    String sql = "select distinct d.ele_code,e.ele_source " + " from sys_right_group_type" + Tools.addDbLink()
      + " d, sys_element" + Tools.addDbLink() + " e"
      + " where  d.ele_code=e.ele_code and d.rg_code=e.rg_code and d.right_group_id=? and e.rg_code=? and e.set_year=?";
    select_list = dao.findBySql(sql,
      new String[] { right_group_id, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
    // 循环调用TYPE信息
    for (int i = 0; i < select_list.size(); i++) {
      Map map = (Map) select_list.get(i);
      String ele_code = map.get("ele_code").toString();
      String ele_value = "";
      String ele_source = map.get("ele_source").toString();

      // 从传入的DTO 中得到对应要素值
      if (PropertiesUtil.getProperty(inputXml, ele_code.toLowerCase() + "_id") == null) {
        ele_value = "";
      } else {
        ele_value = PropertiesUtil.getProperty(inputXml, ele_code.toLowerCase() + "_id").toString();
      }
      // 根据各授权要素拼成SQL语句
      return_sql = return_sql + " and exists (select 1 from sys_right_group_detail" + Tools.addDbLink() + " b where "
        + " b.right_group_id='" + right_group_id + "' and b.ele_code = '" + ele_code
        + "' and (b.ele_value = (select chr_code from " + ele_source + "" + Tools.addDbLink() + " where chr_id='"
        + ele_value + "') or b.ele_value = '#'))";
    }
    return return_sql + ")";
  }

  /**
   * 得到排除权限的SQL语句
   * 
   * @throws Exception---------错误信息
   */
  private String getMinusGroupSql(String right_group_id, Object inputXml) {

    String return_sql = " And Not Exists (select 1 from dual where 1=1";
    List select_list = null;
    // 查找该权限组的TYPE信息
    String sql = "select distinct d.ele_code,e.ele_source " + " from sys_right_group_type" + Tools.addDbLink()
      + " d, sys_element" + Tools.addDbLink() + " e"
      + " where  d.ele_code=e.ele_code and d.rg_code=e.rg_code and d.right_group_id=? and e.rg_code=? and e.set_year=?";
    select_list = dao.findBySql(sql,
      new String[] { right_group_id, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
    // 循环调用TYPE信息
    for (int i = 0; i < select_list.size(); i++) {

      Map map = (Map) select_list.get(i);
      String ele_code = map.get("ele_code").toString();
      String ele_value = "";
      String ele_source = map.get("ele_source").toString();
      // 从传入的DTO 中得到对应要素值
      try {
        ele_value = (String) PropertiesUtil.getProperty(inputXml, ele_code.toLowerCase() + "_id");
      } catch (Exception ex) {
        ele_value = "";
      }
      if (ele_value == null) {
        ele_value = "";
      }
      // 根据各授权要素拼成SQL语句
      return_sql = return_sql + " and exists (select 1 from sys_right_group_detail" + Tools.addDbLink() + " b where "
        + " b.right_group_id='" + right_group_id + "' and b.ele_code = '" + ele_code
        + "' and (b.ele_value = (select chr_code from " + ele_source + "" + Tools.addDbLink() + " where chr_id='"
        + ele_value + "') or b.ele_value = '#'))";
    }
    return return_sql + ")";

  }

  /**
   * 获取符合范围列表(如:帐户范围，筛选列表)
   * 
   * @param ruleids------------
   * @param baseDTO------------
   * @return true或false
   * @throws Exception---------错误信息
   */
  public List getMatchedRules(List ruleids, FBaseDTO baseDto) throws Exception {
    List return_list = new ArrayList();
    for (int i = 0; i < ruleids.size(); i++) {
      String ruleid = ((Map) ruleids.get(i)).get("rule_id").toString();
      if (this.isMatch(ruleid, baseDto)) {
        return_list.add(ruleids.get(i));
      }
    }
    return return_list;
  }

  /**
   * 获取符合范围列表
   * 
   * @param eleRuleid------------
   * @param baseDTO------------
   * @return List
   * @throws Exception
   * @throws Exception---------错误信息
   */
  public List getMatchedELECODES(String eleRuleId, FBaseDTO baseDTO) throws Exception {
    // 返回列表
    List return_list = new ArrayList();

    // 待处理要素列表
    List ele_list = new ArrayList();
    String sql = "select * from SYS_ELE_RULE" + Tools.addDbLink() + " where ELE_RULE_ID=? and rg_code=? and set_year=?";
    ele_list = dao.findBySql(sql, new String[] { eleRuleId, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });

    // 待处理要素值列表
    List value_list = new ArrayList();
    String sql2 = "select * from SYS_ELE_RULE_DETAIL" + Tools.addDbLink()
      + " where ELE_RULE_ID=? and rg_code=? and set_year=?";
    value_list = dao.findBySql(sql2, new String[] { eleRuleId, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });

    String ele_code = "";
    String ele_rule_id = "";
    String ele_value = "";

    // 对每个要素进行循环
    for (int i = 0; i < ele_list.size(); i++) {
      ele_code = ((Map) ele_list.get(i)).get("ele_code").toString();
      ele_rule_id = ((Map) ele_list.get(i)).get("ele_rule_id").toString();
      ele_value = "";
      // 对要素值进行循环
      for (int j = 0; j < value_list.size(); j++) {
        String tmp_ele_rule_id = ((Map) value_list.get(j)).get("ele_rule_id").toString();
        String tmp_rule_id = ((Map) value_list.get(j)).get("rule_id").toString();
        String tmp_ele_value = ((Map) value_list.get(j)).get("ele_value").toString();
        // 如果要素值列表中的ELE_CODE相同
        if (ele_rule_id.equals(tmp_ele_rule_id)) {
          // 如果校验成功，将ELE_VALUE赋值
          if (isMatch(tmp_rule_id, baseDTO)) {
            ele_value = tmp_ele_value;
            // break;
          }
        }
      }

      // 将满足的值填入，如果都不满足，则返回空
      XMLData rowData = new XMLData();
      rowData.put("ele_code", ele_code);
      rowData.put("ele_value", ele_value);
      return_list.add(rowData);
    }

    return return_list;
  }

  /**
   * 要素定值
   * @param eleRuleId 定值规则ID
   * @param voucherDTO  定值对象
   * @param isForceWriteValue 找到多个时，是否强制定值
   * @throws Exception
   */
  public void updateFVoucherDto(String eleRuleId, FVoucherDTO voucherDTO, boolean isForceWriteValue) throws Exception {
    StringBuffer sql = new StringBuffer();

    sql
      .append("select a.ele_code as ele_alias,a.ele_rule_name, b.* from SYS_ELE_RULE")
      .append(Tools.addDbLink())
      .append(" a,SYS_ELE_RULE_DETAIL")
      .append(Tools.addDbLink())
      .append(
        " b where a.ELE_RULE_ID=b.ELE_RULE_ID and a.rg_code =b.rg_code and a.ELE_RULE_ID= ? and b.rg_code=? and b.set_year=?");
    // 待处理要素值列表
    List value_list = dao.findBySql(sql.toString(),
      new Object[] { eleRuleId, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
    Map tempMap = null;
    int matchCount = 0;
    String eleRuleName = null;
    //要素简称
    String ele_alias = null;
    // 对要素值进行循环
    for (int j = 0; j < value_list.size(); j++) {

      tempMap = (Map) value_list.get(j);
      if (eleRuleName == null)
        eleRuleName = (String) PropertiesUtil.getProperty(tempMap, "ele_rule_name");
      if (ele_alias == null)
        ele_alias = (String) PropertiesUtil.getProperty(tempMap, "ele_alias");
      final String tmp_rule_id = (String) tempMap.get("rule_id");
      final String tmp_value_id = (String) tempMap.get("ele_value");
      final String tmp_value_code = (String) tempMap.get("ele_code");
      final String tmp_value_name = (String) tempMap.get("ele_name");
      // 如果校验成功，将ELE_VALUE赋值
      if (isMatch(tmp_rule_id, voucherDTO)) {
        if (matchCount == 0) {
          PropertiesUtil.setProperty(voucherDTO, ele_alias.toLowerCase() + "_id", tmp_value_id);
          PropertiesUtil.setProperty(voucherDTO, ele_alias.toLowerCase() + "_name", tmp_value_name);
          PropertiesUtil.setProperty(voucherDTO, ele_alias.toLowerCase() + "_code", tmp_value_code);
        }
        matchCount++;
      }
    }
    // 如果强制选择第一个匹配值或者本身匹配就一个,则更改dto中的对应值
    if (!isForceWriteValue && matchCount > 1) {
      throw new Exception("要素定值规则：" + eleRuleName + "权限设置不唯一");
    }
  }

  /**
   * 对一组FVoucherDTO进行定值处理（通用）
   * @param dtoList 需定值对象列表
   * @param matchedList 需要定值的规则
   * @param isForceWriteValue 是否强制设定第一条匹配的定值
   * @return dtoList 定值后对象列表
   * @throws 异常
   */
  public List getMatchedValues(List dtoList, String[] matchedList, boolean isForceWriteValue) throws Exception {

    StringBuffer select_sql = new StringBuffer();
    //定义变量
    List eleRule = null;
    /*
     * 获得待定要素的简称（如资金性质为“MK”等）、值及名称等
     */
    //拼凑选择待定元素的SQL语句
    select_sql.append(" select c.rule_id, b.ele_code ele_code_alias, c.ele_value, c.ele_code, c.ele_name ")
      .append(" from SYS_ELE_RULE b, SYS_ELE_RULE_DETAIL c ").append(" where b.ele_rule_id=c.ele_rule_id")
      .append(" and b.ele_rule_code in ('");
    for (int j = 0; j < matchedList.length; j++) {
      select_sql.append(matchedList[j]);
      if (j < matchedList.length - 1) {
        select_sql.append("','");
      }
    }
    select_sql.append("')");
    eleRule = dao.findBySql(select_sql.toString());

    return getMatchedValueData(dtoList, eleRule);
  }

  /**
   * 批量定值 核心逻辑
   * @param vouList  业务信息列表
   * @param eleRuleData 要素规则信息
   * @user ydz
   * @return
   */
  private List getMatchedValueData(List vouList, List eleRuleData) throws Exception {

    Session session = dao.getSession();
    Connection conn = null;
    Statement statement = null;
    try {
      List union_right_rs = null;
      List minus_right_rs = null;
      List shortNameArr = new ArrayList();

      String eleName = null;
      String eleCode = null;
      String eleValue = null;
      String rule_id = null;
      String shortName = null;

      //批处理
      conn = session.connection();
      statement = conn.createStatement();
      statement.addBatch(CLEAR_RULE_CATCH_SQL);

      if (vouList != null && vouList.size() > 0) {
        for (int i = 0; i < vouList.size(); i++) {
          statement.addBatch(insertSql(vouList.get(i), i));
        }
      }
      /*
       * 定值临时表中数据
       */
      if (eleRuleData != null && eleRuleData.size() > 0) {
        for (int i = 0; i < eleRuleData.size(); i++) {
          // 循环处理待定要素，将结果分别放到eleValue,eleCode,eleName中，同时编写更新临时表的前部分SQL语句
          StringBuffer update_sql = new StringBuffer();
          XMLData xml = (XMLData) eleRuleData.get(i);
          shortName = xml.get("ele_code_alias").toString();
          shortNameArr.add(shortName);
          eleName = (String) xml.get("ele_name");
          eleCode = (String) xml.get("ele_code");

          eleValue = (String) xml.get("ele_value");
          rule_id = (String) xml.get("rule_id");

          update_sql.append("update SYS_RULE_DATA_CATCH t set t." + shortName + "_id='").append(eleValue)
            .append("', t." + shortName + "_code='").append(eleCode).append("', t." + shortName + "_name='")
            .append(eleName).append("' where 1=1 ");

          // 根据定植规则ID（RULE_ID）取得所有主范围和追加范围的权限要素
          union_right_rs = dao.findBySql(UNION_SELECT_SQL, new Object[] { rule_id });
          // 根据追加权限要素拼接追加要素的SQL片段
          if (union_right_rs != null && union_right_rs.size() > 0) {
            update_sql.append(" and exists (select 1 from sys_rule_cross_join_add c where 1=1 ");
            for (int j = 0; j < union_right_rs.size(); j++) {
              XMLData union_right_xml = (XMLData) union_right_rs.get(j);
              String union_right_element = union_right_xml.get("ele_code").toString();
              update_sql.append(" and c.").append(union_right_element + "_id").append("=t.")
                .append(union_right_element + "_id");
            }
            // modify by wanghongtao  修改bug-2975
            update_sql.append(" and c.rule_id = ").append(rule_id).append(")");
          }

          // 根据定植规则ID（RULE_ID）取得所有排除范围的权限要素
          minus_right_rs = dao.findBySql(MINUS_SELECT_SQL, new Object[] { rule_id });
          // 根据追加权限要素拼接追加要素的SQL片段
          if (minus_right_rs != null && minus_right_rs.size() > 0) {
            update_sql.append(" and not exists (select 1 from sys_rule_cross_join_add c  where 1=1 ");
            for (int k = 0; k < minus_right_rs.size(); k++) {
              XMLData minus_right_xml = (XMLData) minus_right_rs.get(k);
              String minus_right_element = minus_right_xml.get("ele_code").toString();
              update_sql.append(" and c.").append(minus_right_element + "_id").append("=t.")
                .append(minus_right_element + "_id");
            }
            // modify by wanghongtao  修改bug-2975
            update_sql.append(" and c.rule_id = ").append(rule_id).append(")");
          }
          if ((minus_right_rs == null || minus_right_rs.size() == 0)
            && (union_right_rs == null || union_right_rs.size() == 0)) {
            update_sql.append(" and 1=0");
          }
          // 批处理
          statement.addBatch(update_sql.toString());
        }
        // 执行批处理的sql
        statement.executeBatch();
      }

      // 取得临时表中的数据
      List newxmlDataList = dao.findBySql("select * from sys_rule_data_catch order by "
        + (TypeOfDB.isOracle() ? " to_number(item_index) " : " CAST(item_index AS SIGNED INTEGER) "));
      // 将修改后的数据从临时表中取出来，然后更新旧数据中相应的值
      List returnList = retValue(shortNameArr, vouList, newxmlDataList);
      return returnList;
    } catch (Exception e) {
      throw new Exception("对一组业务数据进行定值处理时出错,请检查");
    } finally {
      // 删除临时表内容
      dao.executeBySql(CLEAR_RULE_CATCH_SQL);
      //关闭statement和session，释放资源
      if (statement != null)
        statement.close();
      if (session != null)
        dao.closeSession(session);
    }

  }

  /**
   * 对一组的dto进行定值,把传入的凭证按照单据类型分组，然后分别进行批量定值
   * 
   * @param dtoList
   *            需要定值的对象
   */
  public List updateFixedValueRuleInfo(List dtoList) throws Exception {
    Map m = new HashMap();
    List ls = null;
    for (int i = 0; i < dtoList.size(); ++i) {
      Object vou = dtoList.get(i);
      if (vou != null) {
        String billTypeCode = (String) PropertiesUtil.getProperty(vou, "billtype_code");
        if (billTypeCode == null)
          //如果单据为空，直接跳过
          continue;
        ls = (List) m.get(billTypeCode);
        if (ls == null) {
          ls = new ArrayList();
          m.put(billTypeCode, ls);
        }
        ls.add(vou);
      }
    }

    List resultList = new ArrayList();
    for (int i = 0; i < m.keySet().size(); ++i) {
      String key = m.keySet().toArray()[i].toString();
      List retList = updateFixedValueRuleInfoHelper((List) m.get(key));
      resultList.addAll(retList);
    }
    return resultList;
  }

  /**
   * 对一组的dto进行定值
   * 
   * @param dtoList
   *            需要定值的对象
   */
  public List updateFixedValueRuleInfoHelper(List dtoList) throws Exception {

    //要素定值信息
    List rs = null;
    String billType_code = null;
    if (dtoList == null || dtoList.size() == 0)
      return dtoList;
    billType_code = (String) PropertiesUtil.getProperty(dtoList.get(0), "billtype_code");

    //如果单据为空，直接返回
    if (billType_code == null || billType_code.equals(""))
      return dtoList;

    //****** 处理默认值
    String rgCode = SessionUtil.getRgCode();
    String setYear = SessionUtil.getLoginYear();
    List defList = dao.findBySql(VALUESET_DEFAULT_SQL, new Object[] { billType_code, rgCode, setYear });
    if (defList.size() > 0) {
      for (int i = 0; i < dtoList.size(); ++i) {
        Object vou = dtoList.get(i);
        if (vou != null) {
          for (int j = 0; j < defList.size(); ++j) {
            String code = ((Map) defList.get(j)).get("field_code").toString();
            String value = ((Map) defList.get(j)).get("default_value").toString();
            PropertiesUtil.setProperty(vou, code.toLowerCase(), value);
          }
        }

      }
    }
    //****** 批量定值
    // 根据凭证类型ID（BILLTYPE_ID）获得待定要素的简称（如资金性质为“MK”等）、值及名称等
    rs = dao.findBySql(VALUESET_RULE_BILLTYPE_SQL, new Object[] { billType_code, rgCode, setYear });
    return this.getMatchedValueData(dtoList, rs);
  }

  /**
   * 
   * 将修改后的数据从临时表中取出来，然后更新旧数据中相应的值
   * 
   * @param shortNameArr
   *            ----待定要素集
   * @param vouList
   *            ----旧数据List 可以是 DTO 或XMLData
   * @param newxmlDataList
   *            ----更新够的List
   * @return
   */
  private List retValue(List shortNameArr, List vouList, List newxmlDataList) {
    //待定值业务数据
    Object vouData = null;
    //定值后数据
    XMLData newData = null;

    for (int j = 0; j < newxmlDataList.size(); j++) {
      vouData = vouList.get(j);
      newData = (XMLData) newxmlDataList.get(j);
      for (int i = 0; i < shortNameArr.size(); i++) {
        final String shortName = shortNameArr.get(i).toString().toLowerCase();
        final String eleIdStr = shortName + "_id";
        final String eleCodeStr = shortName + "_code";
        final String eleNameStr = shortName + "_name";
        final String ele_id = (String) newData.get(eleIdStr);
        ;
        final String ele_code = (String) newData.get(eleCodeStr);
        final String ele_name = (String) newData.get(eleNameStr);
        //设置属性
        PropertiesUtil.setProperty(vouData, eleIdStr, ele_id);
        PropertiesUtil.setProperty(vouData, eleCodeStr, ele_code);
        PropertiesUtil.setProperty(vouData, eleNameStr, ele_name);
      }
    }
    return vouList;
  }

  /**
   * 获取插入临时表SQL
   * @param vouData 插入数据
   * @param item_index 插入序号
   * @return
   */
  private String insertSql(Object vouData, int item_index) throws SQLException {

    if (RULE_DATA_CATCH_COL == null || RULE_DATA_CATCH_COL.size() == 0) {
      String selectColsStr = "select lower(t.COLUMN_NAME) as col_name from "
        + (TypeOfDB.isOracle() ? " user_tab_columns t " : " information_schema.columns t ")
        + "where t.TABLE_NAME = 'SYS_RULE_DATA_CATCH' "
        + "  and t.COLUMN_NAME <> 'IS_SET' and t.COLUMN_NAME <> 'ITEM_INDEX'";
      RULE_DATA_CATCH_COL = dao.findBySql(selectColsStr);
    }

    StringBuffer colNamesSb = new StringBuffer();
    StringBuffer valuesSb = new StringBuffer();

    for (int i = 0; RULE_DATA_CATCH_COL != null && i < RULE_DATA_CATCH_COL.size(); i++) {
      String colName = (String) ((XMLData) RULE_DATA_CATCH_COL.get(i)).get("col_name");
      String valueStr = "null";
      String value = null;
      try {
        value = (String) PropertiesUtil.getProperty(vouData, colName);
      } catch (Exception ex) {
        value = "null";
      }

      if (value != null && !value.equals("")) {
        valueStr = "'" + value + "'";
      }
      if (i < RULE_DATA_CATCH_COL.size() - 1) {
        colNamesSb.append(colName).append(",");
        valuesSb.append(valueStr).append(",");
      } else {
        colNamesSb.append(colName);
        valuesSb.append(valueStr);
      }

    }

    StringBuffer insert_sql = new StringBuffer();
    insert_sql.append("insert into sys_rule_data_catch(").append(colNamesSb).append(",item_index")
      .append(") values ( ").append(valuesSb).append(",").append(String.valueOf(item_index)).append(")");
    return insert_sql.toString();
  }
}
