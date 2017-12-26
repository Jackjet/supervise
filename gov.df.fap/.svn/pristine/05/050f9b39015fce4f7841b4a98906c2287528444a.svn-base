package gov.df.fap.service.dictionary.element;

import gov.df.fap.api.dictionary.ElementCacheKey;
import gov.df.fap.api.dictionary.ElementInfo;
import gov.df.fap.api.dictionary.ElementOperation;
import gov.df.fap.api.dictionary.ElementSetXMLData;
import gov.df.fap.bean.dictionary.dto.FElementDTO;
import gov.df.fap.bean.dictionary.element.ElementDefinition;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.service.gl.core.DaoSupport;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.SQLUtil;
import gov.df.fap.service.util.gl.configure.AccountIllegalException;
import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.service.util.log.Log;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.number.NumberUtil;
import gov.df.fap.util.xml.ParseXML;
import gov.df.fap.util.xml.XMLData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * ElementOperation 内部实现类
 * 
 * @version 1.0
 * @author 
 * @since java 1.6
 */
@Component("elementOperationBO")
public class ElementOperationBO extends AbstractElementOperation implements ElementOperation {
  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao = null;

  @Autowired
  @Qualifier("df.fap.daoSupport")
  protected DaoSupport daoSupport;

  /*
   * (non-Javadoc)
   * 
   * @see gov.gfmis.fap.dictionary.service.ElementOperationA#getDao()
   */
  public GeneralDAO getDao() {
    return dao;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#setDao(gov.gfmis.fap
   * .util.dao.springdao.GeneralDAO)
   */
  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  public DaoSupport getDaoSupport() {
    return daoSupport;
  }

  public void setDaoSupport(DaoSupport daoSupport) {
    this.daoSupport = daoSupport;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#isElement(java.lang
   * .String)
   */
  /*** Add by fengdz TM:2012-03-19 ***/
  public String getSetYear() {
    return (String) SessionUtil.getLoginYear();

  }

  public String getRgCode() {
    return (String) SessionUtil.getRgCode();

  }

  /*** modify by wl 变量绑定修改 ***/
  public boolean isElement(String element) {
    String strSQL = "select 1 from sys_element where ele_code = upper(?) and set_year=? and rg_code=?";
    if (dao.findBySql(strSQL.toString(), new Object[] { element, new Integer(getSetYear()), getRgCode() }).size() > 0)
      return true;
    else
      return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#getCOADetailCode(java
   * .lang.String, java.lang.String)
   */
  public List getCOADetailCode(String coa_id, String element) {
    StringBuffer strSQL = new StringBuffer("select b.* From Gl_Coa_Detail").append(Tools.addDbLink())
      .append(" a,GL_Coa_Detail_Code").append(Tools.addDbLink()).append(" b ")
      .append(" Where a.coa_detail_id = b.coa_detail_id and a.coa_id =? and a.ele_code =? ");
    List return_list = dao.findBySql(strSQL.toString(), new Object[] { coa_id, element.toUpperCase() });
    return return_list;
  }

  protected String getSelectedField(String element, String tableName, String tableAlias, String[] column) {
    return getEleFieldSQL(element, tableName, tableAlias, column);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperation#getEleByCondition(int,
   * java.lang.String, gov.gfmis.fap.util.FPaginationDTO, java.lang.String[],
   * boolean, java.lang.String, java.util.Map, java.lang.String)
   */
  public XMLData getEleByCondition(int setYear, String element, FPaginationDTO page, String[] column,
    boolean isNeedRight, String coa_id, Map relation, String condition) throws Exception {
    XMLData data = new XMLData();
    try {
      if (element == null || element.equals(""))
        throw new Exception("非法的要素简称,无法执行查询!");

      element = element.toUpperCase();
      // 查询要素简称对应的表
      XMLData setData = getEleSetByCode(element);
      // 保存部分信息
      String tableName = (String) setData.get("ele_source");
      String eleName = (String) setData.get("ele_name");
      // 构造需要查询的字段,由于只查询单表,不需要添加别名
      String sql = getQuerySql(setYear, element, tableName, column, isNeedRight, relation, coa_id, condition);
      // 查询条件字符串
      // modify by wanghongtao 兼容基础数据的chr_code是纯数字型或含有字符的排序问题
      String total_count = StringUtil.toStr(CommonUtil.getRowCount(sql, dao));
      // 查询结果
      List ret = CommonUtil.findByPage(sql, page, dao);

      // 存储信息
      data.put("ele_code", element);
      data.put("table_name", tableName);
      data.put("ele_name", eleName);
      data.put("total_count", total_count);
      data.put("row", ret);
    } catch (Exception e) {
      throw e;
    }
    return data;
  }

  /**
   * 
   * 根据指定Class返回要素对象列表。 注意！使用约定：Class内属性名必须与数据库基础数据表的字段对应，且是小写。
   * Class的属性类型可以是基本数据类型(int, float, double等)、String、BigDecimal，
   * 但必须与基础数据表存储内容相符。
   * 
   * @param element
   *            要素简称
   * @param page
   *            分页对象
   * @param isNeedRight
   *            是否需要权限
   * @param coa_id
   *            coa id
   * @param ctrlElementValues
   *            关联关系条件
   * @param plusSql
   *            附加sql
   * @param elementObjectClass
   *            返回对象的Class。
   * @return 根据指定的Class返回要素对象。
   * 
   */
  public List getEleByConditionAsObj(String element, FPaginationDTO page, boolean isNeedRight, String coa_id,
    Map ctrlElementValues, String sPlusSQL, Class elementObjectClass) throws Exception {
    if (element == null || element.equals(""))
      throw new Exception("非法的要素简称,无法执行查询!");
    String upperElement = element.toUpperCase();
    // 查询要素简称对应的表
    XMLData setData = getEleSetByCode(upperElement);
    // 保存部分信息
    String tableName = null;
    if (setData != null && setData.size() > 0) {
      tableName = (String) setData.get("ele_source");
    }
    // 构造需要查询的字段,由于只查询单表,不需要添加别名
    String sql = getQuerySql(Integer.valueOf(CommonUtil.getSetYear()).intValue(), element, tableName, null,
      isNeedRight, ctrlElementValues, coa_id, sPlusSQL);
    return daoSupport.genericQuery(sql, elementObjectClass);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#getEleByCondition(
   * java.lang.String, int, int, java.lang.String[], boolean,
   * java.lang.String, java.util.Map, java.lang.String)
   */
  public XMLData getEleByCondition(String element, int pageIndex, int pageCount, String[] column, boolean isNeedRight,
    String coa_id, Map relation, String condition) throws Exception {
    int setYear = NumberUtil.toInt(CommonUtil.getSetYear());
    FPaginationDTO page = new FPaginationDTO();
    page.setCurrpage(pageIndex);
    page.setPagecount(pageCount);
    return getEleByCondition(setYear, element, page, column, isNeedRight, coa_id, relation, condition);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#insertEleByXml(java
   * .lang.String)
   */
  public synchronized XMLData insertEleByXml(String inXmlObj) throws Exception {
    return insertEle(ParseXML.convertXmlToObj(inXmlObj));
  }

  /**
   * 此方法实现的业务逻辑如下: 1.生成一个UUID的主键 2.检查传入要素的编码规则 3.对一些字段进行特殊处理,具体如下:
   * a.level_num:参见后面"对一级要素和非一级要素的处理" b.is_leaf:参见后面"对一级要素和非一级要素的处理"
   * c.set_year:如果要素对象中没有值则设置为平台提供的set_year
   * d.rg_code:如果要素对象中没有值则设置为登陆用户所属的rg_code(部分要素不需要加入rg_code)
   * 4.校验在要素表中是否存在和待插入要素重复的要素(不同要素有不同的重复判断规则)
   * 5.将插入到此要素对应的表并执行相关特殊处理(参见后面"对一级要素和非一级要素的处理")
   * 6.更新sys_element中此要素对应记录的level_num
   * 
   * 对一级要素和非一级要素的处理: 由于系统对插入一级要素和非一级要素有不同的要求,所以要分开处理 (个人觉的这地方使用数据库触发器更合适)
   * 1.如果插入的是一级要素(存在parent_id),则需要 a.其level_num被设置为一级 b.其is_leaf被设置为1(是叶子节点)
   * 2.如果插入的不是一级要素,则需要: a.根据它的父要素的level_num得到它的level_num
   * b.更新父要素的is_leaf为0(即父要素不再是叶子)
   * 
   * @author 张轲
   */
  public synchronized XMLData insertEle(XMLData xml) throws Exception {
    XMLData data = null;
    String element = (String) xml.get("element_code");
    xml.remove("element_code");
    // String id = CommonUtil.generateUuid(CommonUtil.GUID_STRING);
    // chr_id 不再使用uuid生成，现在类型为number
    String id = CommonUtil.generateUuid(CommonUtil.GUID_CHR_ID);
    try {
      XMLData eleSetData = this.getEleSetByCode(element);
      String table_name = ((String) eleSetData.get("ele_source")).toUpperCase();

      if ("FSBAP".equals(element)) {
        table_name = "ELE_ACCOUNT";
      }
      if (table_name == null || table_name.equalsIgnoreCase("")) {
        throw new Exception("根据要素简称" + element + "无法获取");
      }

      String rg_code = CommonUtil.getRgCode();
      String thisYear = CommonUtil.getSetYear();
      // 保存值以供后用
      Map map = new HashMap();

      int curLevel_num = 1;
      StringBuffer field = new StringBuffer();
      StringBuffer value = new StringBuffer();

      map.putAll(xml);
      map.remove("chr_id");
      map.remove("level_num");
      map.remove("is_leaf");
      map.remove("set_year");
      map.remove("user_id");
      Iterator keyIt = map.keySet().iterator();
      while (keyIt.hasNext()) {
        final String fieldInMap = (String) keyIt.next();
        if ("rg_code".equalsIgnoreCase(fieldInMap)) {
          continue;
        }
        final Object valueInMap = map.get(fieldInMap);
        field.append(fieldInMap).append(",");
        if (valueInMap != null) {
          String valueStr = valueInMap.toString();
          valueStr = valueStr.replaceAll("'", "''");
          value.append("'").append(valueStr).append("',");
        } else {
          value.append("null,");
        }
      }

      String parent_id = (String) map.get("parent_id");
      String chr_code = (String) map.get("chr_code");
      if (map.get("set_year") != null) {
        thisYear = (String) map.get("set_year");
      }
      // 插入验证编码规则
      checkCodeRule(element, chr_code);
      field.append("chr_id,level_num,set_year,create_date,create_user")
        .append(",latest_op_date,latest_op_user,last_ver,chr_code1,chr_id1,chr_code2,chr_id2,")
        .append("chr_code3,chr_id3,chr_code4,chr_id4,chr_code5,chr_id5,is_leaf,disp_code");
      if (parent_id != null && !parent_id.equalsIgnoreCase("")) {
        XMLData parent = getEleByID(table_name, parent_id);
        curLevel_num = Integer.parseInt((String) parent.get("level_num")) + 1;
        parent = null;
        value.append("'").append(id).append("',").append(curLevel_num).append(",").append(thisYear).append(",")
          .append("'").append(DateHandler.getLastVerTime()).append("',").append("'").append(CommonUtil.getUserId())
          .append("',").append("'").append(DateHandler.getLastVerTime()).append("',").append("'")
          .append(CommonUtil.getUserId()).append("',").append("'").append(DateHandler.getLastVerTime()).append("',");
        for (int i = 1; i < 6; i++) {
          if (i < curLevel_num) {
            value.append("chr_code").append(i).append(",chr_id").append(i).append(",");
          } else {
            value.append("'").append(chr_code).append("','").append(id).append("',");
          }
        }
        value.append("1,'" + chr_code + "'");
      } else {
        // 离线段提交 收款人账户和摘要时，取自己数据的用户id作为create_user
        String userId = StringUtils.isEmpty(CommonUtil.getUserId()) ? (String) xml.get("user_id") : CommonUtil
          .getUserId();
        value.append("'").append(id).append("',1,").append(thisYear).append(",'").append(DateHandler.getLastVerTime())
          .append("','").append(userId).append("','").append(DateHandler.getLastVerTime()).append("','").append(userId)
          .append("','").append(DateHandler.getLastVerTime()).append("',");
        for (int i = 1; i < 6; i++) {
          value.append("'").append(chr_code).append("','").append(id).append("',");
        }
        value.append("1,'" + chr_code + "'");
      }
      // 区域
      if (!element.equalsIgnoreCase("RG") && !element.equalsIgnoreCase("FM") && !element.equalsIgnoreCase("UNIT")
        && !element.equalsIgnoreCase("EDITOR")) {
        field.append(",rg_code");
        // 收款人区域可以自定义
        if (element.equalsIgnoreCase("PAYEE_ACCOUNT") && map.get("rg_code") != null) {
          rg_code = (String) map.get("rg_code");
        }
        value.append(",'").append(rg_code).append("'");
      }
      StringBuffer strSQL = new StringBuffer();
      if (StringUtil.isNotEmpty(parent_id)) {
        strSQL.append("insert into ").append(table_name).append(" (").append(field).append(") ").append("select ")
          .append(value).append(" from ").append(table_name).append(" where chr_id='").append(parent_id).append("'");
      } else {
        strSQL.append("insert into ").append(table_name).append(" (").append(field).append(") ").append("values (")
          .append(value).append(")");
      }
      // 数据查重
      // 对于账套(不同账套类别编码可以重复)
      // 对于凭证类别和科目(不同账套编码可以重复)
      // 对于账户根据不同的账户类型,编码允许重
      // 对于银行根据不同的银行类型,编码允许重
      StringBuffer strWhere = new StringBuffer();
      if (element.equalsIgnoreCase("ST")) {
        String set_type = (String) map.get("set_type");
        strWhere.append("and rg_code='").append(rg_code).append("' and set_type='").append(set_type)
          .append("' and set_year=").append(thisYear);
      } else if (element.equalsIgnoreCase("DEFINE") || element.equalsIgnoreCase("AS")) {
        if ("1".equals(xml.get("balance_period_type"))
          && StringUtil.equalsIgnoreCase((String) xml.get("table_name"), (String) xml.get("monthdetail_table_name"))) {
          throw new AccountIllegalException("累计月科目的余额表不能与余额明细表相同!");
        }
        String st_id = map.get("st_id") == null ? "" : (String) map.get("st_id");
        strWhere.append("and rg_code='").append(rg_code);
        if (st_id.equals(""))
          strWhere.append("' and st_id is null ");
        else
          strWhere.append("' and st_id = '").append(st_id).append("'");
        strWhere.append(" and set_year=").append(thisYear);
      } else if (StringUtil.equalsIgnoreCase(element, new String[] { "ABC", "BAC", "BAI", "BAP", "BLACK_ACCOUNT",
        "PAYEE_ACCOUNT" }))// 账户
      {
        String account_type = (String) map.get("account_type");
        strWhere.append("and rg_code='").append(rg_code).append("' and account_type='").append(account_type)
          .append("' and set_year=").append(thisYear);
      } else if (element.equalsIgnoreCase("PB"))// 代理银行
      {
        strWhere.append("and rg_code='").append(rg_code).append("' and agentflag=1 and set_year=").append(thisYear);
      } else if (element.equalsIgnoreCase("CB"))// 清算银行
      {
        strWhere.append("and rg_code='").append(rg_code).append("' and clearflag=1 and set_year=").append(thisYear);
      } else if (element.equalsIgnoreCase("IB"))// 收入银行
      {
        strWhere.append("and rg_code='").append(rg_code).append("' and incomeflag=1 and set_year=").append(thisYear);
      } else {
        strWhere.append("and set_year=").append(thisYear);
        if (!element.equalsIgnoreCase("RG") && !element.equalsIgnoreCase("FM") && !element.equalsIgnoreCase("UNIT")
          && !element.equalsIgnoreCase("EDITOR")) {
          strWhere.append(" and rg_code='").append(rg_code).append("'");
        }
      }

      // 不再调用总账引擎的检查编码重复的接口，
      StringBuffer checkSql = new StringBuffer();
      checkSql.append("select 1 from ").append(table_name).append(" where ").append("chr_code").append("='")
        .append(chr_code).append("' ").append(strWhere.toString());
      List checkList = dao.findBySql(checkSql.toString());
      if (checkList.size() > 0) {
        throw new Exception("显示码不能重复，请重新录入！");
      }

      List updateList = null;
      if (parent_id != null && !parent_id.equalsIgnoreCase("")) {
        dao.executeBySql("update " + table_name + " set is_leaf=0 where chr_id='" + parent_id + "'");
        updateList = dao.findBySql("select * from " + table_name + " where chr_id='" + parent_id + "'");
      }
      dao.executeBySql(strSQL.toString());
      // 修改sys_element中的最大级次
      modifyMaxLevel(element, curLevel_num);

      // data = getEleByID(table_name,id);
      if ("FSBAP".equalsIgnoreCase(element)) {
        // 如果是资金调存系统，则从视图中获得数据，否则在主界面中无法显示“开户行”和“账户所属”的名称（原因是ELE_ACCOUNT表中只包括了银行CODE而没有名称，故无法在页面显示银行名称）
        data = getEleByID("VW_FS_PAYMENTACCOUNT", id);
      } else {
        data = getEleByID(table_name, id);
      }
      if (data != null) {
        data.put("update_list", updateList);
      }
    } catch (Exception e) {
      throw e;
    }
    return data;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#modifyEleByXml(java
   * .lang.String)
   */
  public synchronized XMLData modifyEleByXml(String inXmlObj) throws Exception {
    XMLData xml = ParseXML.convertXmlToObj(inXmlObj);
    String element = (String) xml.getSubObject("element");
    String chr_id = (String) xml.getSubObject("par_value"); // 被修改记录的唯一ID
    return modifyEle(element, chr_id, ParseXML.convertXmlToObj(inXmlObj));
  }

  /**
   * 对要素进行修改,重点在于要素的移动,即将此要素加到要素树中其他要素底下,注意此要素的子节点也要跟着移动.
   * 移动前提:此节点不能移动到自己以及自己子节点下面 方法实现的业务逻辑如下: 1.得到修改前的要素以作备份
   * 2.检查要素编码及前面所说的"移动前提"的检查 3.更新要素本身信息 4.如果更新要素存在父要素,则更新后要将父要素的is_leaf设置为0
   * 5.更新要素自己的level_num和chr_id1..5,chr_code1..5
   * 6.更新此要素的所有子节点的level_num和chr_id1..5,chr_code1..5
   * 
   * @author 张轲
   */
  public XMLData modifyEle(String element, String chrId, Map fieldInfo) throws Exception {

    XMLData eleSetData = this.getEleSetByCode(element);
    // String element = eleSetData.get("ele_code");// 被修改的表名
    String table_name = (String) eleSetData.get("ele_source");// 被修改记录的唯一ID
    String chr_id = chrId;

    if (table_name == null || table_name.equalsIgnoreCase("")) {
      throw new Exception("根据要素简称" + element + "无法获取");
    } else// 针对配置要进行修改
    {
      table_name = (!"1".equals(eleSetData.get("is_local"))) ? table_name + Tools.addDbLink() : table_name;
    }

    // 张晓楠注释，2010-10-18，服务器端不做判断，到前台去判断。
    /*
     * if ("EN".equalsIgnoreCase(element) ||
     * "ENMANAGER".equalsIgnoreCase(element)) {
     * 
     * if (!checkIsReform(fieldInfo)) { XMLData result = new XMLData();
     * result.put("error", "error"); return result; // throw new
     * Exception("此单位已有录入指标,不能修改“改革单位属性”!"); } }
     */

    // 保存值以供后用
    Map map = new HashMap();
    String rg_code = CommonUtil.getRgCode();
    String thisYear = CommonUtil.getSetYear();
    // 得到将要修改数据的原有数据
    XMLData src = (XMLData) getEleByID(table_name, chr_id);
    if (src == null) {
      throw new Exception("未能找到需要修改的数据!");
    }
    // 记录移动前父节点
    String srcParentId = (String) src.getSubObject("parent_id");

    String chr_code = (String) fieldInfo.get("chr_code");
    // 检查要素编码
    checkCodeRule(element, chr_code);
    // 需要修改字段的值对
    map.putAll(fieldInfo);
    String to_parent_id = (String) map.get("parent_id");
    if (map.get("set_year") != null) {
      thisYear = (String) map.get("set_year");
    }
    if (element.equalsIgnoreCase("PAYEE_ACCOUNT") && map.get("rg_code") != null) {
      rg_code = (String) map.get("rg_code");
    }
    // 得到移动后的父节点的数据
    XMLData parent = new XMLData();
    String p_chr_code1 = "", p_chr_code2 = "", p_chr_code3 = "";
    String p_chr_code4 = "", p_chr_code5 = "";
    if (to_parent_id != null && !to_parent_id.equalsIgnoreCase(""))// 如果目标parent不是root
    {
      parent = getEleByID(table_name, to_parent_id);
      if (parent == null) {
        throw new Exception("基础数据配置错误,无法找到要修改数据的父级数据!");
      }
      p_chr_code1 = (String) parent.getSubObject("chr_code1") == null ? "" : (String) parent.getSubObject("chr_code1");
      p_chr_code2 = (String) parent.getSubObject("chr_code2") == null ? "" : (String) parent.getSubObject("chr_code2");
      p_chr_code3 = (String) parent.getSubObject("chr_code3") == null ? "" : (String) parent.getSubObject("chr_code3");
      p_chr_code4 = (String) parent.getSubObject("chr_code4") == null ? "" : (String) parent.getSubObject("chr_code4");
      p_chr_code5 = (String) parent.getSubObject("chr_code5") == null ? "" : (String) parent.getSubObject("chr_code5");
    }

    // 分析修改数据时的各种可能性，及注意条件：
    // <1>得到要移动节点的现有parent_id，得到移动后节点的parent_id，别名to_parent_id（也就是把当前节点放到谁的下面）。
    // <2>如果to_parent_id=parent_id，就是不移动节点。
    // <3>如果to_parent_id=chr_id，这是要把自己放到自己下面，报错。
    // <4>取得节点现有chr_code1-5集合。（{chr_code1,chr_code2,chr_code3,chr_code4,chr_code5}）
    // <5>如果to_parent_id为空，那么chr_code1-5为{,,,,}，否则取chr_id为to_parent_id的chr_code1-5集合。
    // <6>如果to_parent_id是要移动节点的子节点，不能移动，报错。
    // <7>组合要移动节点的新chr_code1-5。
    // <8>更新要移动节点及其所有子节点的chr_code1-5。
    if (to_parent_id != null && to_parent_id.equalsIgnoreCase(chr_id)) {
      // 要移动到自己下面，报错
      throw new Exception("不能将父节点设置为自身");
    } else if (to_parent_id != null
      && (p_chr_code1.equalsIgnoreCase(chr_code) || p_chr_code2.equalsIgnoreCase(chr_code)
        || p_chr_code3.equalsIgnoreCase(chr_code) || p_chr_code4.equalsIgnoreCase(chr_code) || p_chr_code5
          .equalsIgnoreCase(chr_code))) {
      // 要移动到子节点下面
      throw new Exception("不能移动本节点到原有子节点下");
    } else {
      // 不移动节点
      // 针对基础数据的默认值设置
      map.put("latest_op_date", DateHandler.getLastVerTime());
      map.put("latest_op_user", CommonUtil.getUserId());
      map.put("last_ver", DateHandler.getLastVerTime());
      map.remove("parent_name");
      map.remove("update_list");
      // 数据查重
      // 对于账套(不同账套类别编码可以重复)
      // 对于凭证类别和科目(不同账套编码可以重复)
      // 对于账户根据不同的账户类型,编码允许重
      // 对于银行根据不同的银行类型,编码允许重
      StringBuffer strWhere = new StringBuffer();
      if (element.equalsIgnoreCase("ST")) {
        String set_type = (String) map.get("set_type");
        if (set_type == null) {
          set_type = (String) src.getSubObject("set_type");
        }
        strWhere.append("and rg_code='").append(rg_code).append("' and set_type='").append(set_type)
          .append("' and set_year=").append(thisYear).append(" and chr_id <> '").append(chr_id).append("'");
      } else if (element.equalsIgnoreCase("DEFINE") || element.equalsIgnoreCase("AS")) {
        String st_id = (String) map.get("st_id");
        if (st_id == null) {
          st_id = (String) src.getSubObject("st_id");
        }
        strWhere.append("and rg_code='").append(rg_code).append("' and st_id='").append(st_id)
          .append("' and set_year=").append(thisYear).append(" and chr_id <> '").append(chr_id + "'");
      } else if (element.equalsIgnoreCase("AGENT_ACCOUNT")// 账户
        || element.equalsIgnoreCase("BAC") || element.equalsIgnoreCase("BAI")
        || element.equalsIgnoreCase("BAP")
        || element.equalsIgnoreCase("BLACK_ACCOUNT") || element.equalsIgnoreCase("PAYEE_ACCOUNT")) {
        String account_type = (String) map.get("account_type");
        if (account_type == null) {
          account_type = (String) src.getSubObject("account_type");
        }
        strWhere.append("and rg_code='").append(rg_code).append("' and account_type='").append(account_type)
          .append("' and set_year=").append(thisYear).append(" and chr_id <> '").append(chr_id).append("'");
      } else if (element.equalsIgnoreCase("PB"))// 代理银行
      {
        strWhere.append("and rg_code='").append(rg_code).append("' and agentflag=1 and set_year=").append(thisYear)
          .append(" and chr_id <> '").append(chr_id).append("'");
      } else if (element.equalsIgnoreCase("CB"))// 清算银行
      {
        strWhere.append("and rg_code='").append(rg_code).append("' and clearflag=1 and set_year=").append(thisYear)
          .append(" and chr_id <> '").append(chr_id).append("'");
      } else if (element.equalsIgnoreCase("IB"))// 收入银行
      {
        strWhere.append("and rg_code='").append(rg_code).append("' and incomeflag=1 and set_year=").append(thisYear)
          .append(" and chr_id <> '").append(chr_id).append("'");
      } else {
        strWhere.append("and set_year=").append(thisYear).append(" and chr_id <> '").append(chr_id).append("'");
        if (!element.equalsIgnoreCase("RG") && !element.equalsIgnoreCase("FM") && !element.equalsIgnoreCase("UNIT")
          && !element.equalsIgnoreCase("EDITOR")) {
          strWhere.append(" and rg_code='").append(rg_code).append("'");
        }
      }

      // 不再调用总账引擎的检查编码是否重复的方法
      // CommonUtil.checkRepeat(table_name, "chr_code", chr_code, strWhere
      // .toString(), dao);
      StringBuffer checkSql = new StringBuffer();
      checkSql.append("select 1 from ").append(table_name).append(" where ").append("chr_code").append("='")
        .append(chr_code).append("' ").append(strWhere.toString());
      List checkList = dao.findBySql(checkSql.toString());
      if (checkList.size() > 0) {
        throw new Exception("显示码不能重复，请重新录入！");
      }

      StringBuffer updateStatement = new StringBuffer();
      updateStatement.append("update ").append(table_name).append(" set ");
      Iterator it = map.keySet().iterator();
      final String strRounder = "'";
      final String breaker = " ";
      final String equal = "=";
      final String seperator = ",";
      while (it.hasNext()) {
        final String field = (String) it.next();
        updateStatement.append(field).append(equal).append(strRounder)
          .append(map.get(field) == null ? "" : map.get(field)).append(strRounder).append(seperator).append(breaker);
      }
      updateStatement.deleteCharAt(updateStatement.length() - 2);
      updateStatement.append("where chr_id='").append(chrId).append(strRounder);

      dao.executeBySql(updateStatement.toString());
      /**
       * 2011.01.26 张东华 增加
       * 如果要素是与银行相关的（如PB,CB,IB,TB）,当修改基础要素银行（ele_bank）的名称时
       * 收款账户(ele_payee_account)的开户行名称也需要对应修改
       */
      if (element.equalsIgnoreCase("PB") || element.equalsIgnoreCase("CB") || element.equalsIgnoreCase("IB")
        || element.equalsIgnoreCase("TB")) {
        StringBuffer updateAccountBankNameStatement = new StringBuffer();
        updateAccountBankNameStatement.append("update ele_payee_account ").append(
          " set bank_name = ? where bank_code = ? and rg_code = ? and set_year = ? ");
        dao
          .executeBySql(
            updateAccountBankNameStatement.toString(),
            new Object[] { map.get("chr_name"), map.get("chr_code"), SessionUtil.getRgCode(),
              SessionUtil.getLoginYear() });
      }
      // 得到将要修改数据的原有数据
      src = (XMLData) getEleByID(table_name, chr_id);
      // 修改本条及其所有子节点关系数据
      modNode(src, parent, table_name, element);
      // 将父级is_leaf修改为0
      if (to_parent_id != null && !StringUtil.isEmpty(to_parent_id)) {
        dao.executeBySql("update " + table_name + " set is_leaf=0 where chr_id = '" + to_parent_id + "'");
      }
      if (srcParentId != null && !StringUtil.isEmpty(srcParentId)) {
        // 查询子集
        List list = dao.findBySql("select * from " + table_name + " where parent_id = '" + srcParentId + "'");
        if (list.size() == 0) {
          dao.executeBySql("update " + table_name + " set is_leaf = 1 where chr_id = '" + srcParentId + "'");
        }
      }
    }
    return getEleByID(table_name, chr_id);
  }

  /**
   * 将节点及其子节点有序的构造
   */
  private void modNode(XMLData curNode, XMLData parent, String table_name, String element) throws Exception {
    String chr_id = (String) curNode.get("chr_id");
    String chr_code = (String) curNode.get("chr_code");
    int parent_level_num = parent.get("level_num") == null ? 0 : Integer.parseInt(parent.get("level_num").toString());
    String parent_chr_id = (String) parent.get("chr_id");
    StringBuffer strSQL = new StringBuffer();
    // 保存相关信息
    curNode.put("level_num", String.valueOf(parent_level_num + 1));
    strSQL.append("update ").append(table_name).append(" set level_num=").append(parent_level_num + 1);

    // 对于预算单位维护，如果修改了父级单位类型，那么其所有子集的单位类型都要统一修改
    String enType = null;
    if ("EN".equalsIgnoreCase(element) || "ENMANAGER".equalsIgnoreCase(element)) {
      enType = (String) parent.get("en_type");
      if (enType != null && !"".equals(enType)) {
        curNode.remove("en_type");
        curNode.put("en_type", enType);
        strSQL.append(",en_type=").append(parent.get("en_type"));
      }
    }
    //

    for (int i = 1; i <= parent_level_num; i++) {
      // 保存相关信息
      curNode.put("chr_code" + i, parent.get("chr_code" + i));
      curNode.put("chr_id" + i, parent.get("chr_id" + i));
      // 形成相关脚本
      strSQL.append(",chr_code").append(i).append("='").append(parent.get("chr_code" + i)).append("'")
        .append(",chr_id").append(i).append("='").append(parent.get("chr_id" + i)).append("'");
    }
    // 保存相关信息
    curNode.put("chr_code" + (parent_level_num + 1), chr_code);
    curNode.put("chr_id" + (parent_level_num + 1), chr_id);
    // 形成相关脚本
    strSQL.append(",disp_code='").append(chr_code).append("',chr_code").append(parent_level_num + 1).append("='")
      .append(chr_code).append("'").append(",chr_id").append(parent_level_num + 1).append("='").append(chr_id)
      .append("'");
    for (int i = parent_level_num + 2; i < 6; i++) {
      strSQL.append(",chr_code").append(i).append("=null,chr_id").append(i).append("=null");
    }
    if (parent_chr_id == null) {
      strSQL.append(",parent_id=null where chr_id = '").append(chr_id).append("'");
    } else {
      strSQL.append(",parent_id='").append(parent_chr_id).append("' where chr_id = '").append(chr_id).append("'");
    }
    dao.executeBySql(strSQL.toString());
    // 查询子集
    List list = dao.findBySql("select * from " + table_name + " where parent_id = '" + chr_id + "'");
    if (list.size() > 0) {
      for (int i = 0; i < list.size(); i++) {
        modNode((XMLData) list.get(i), curNode, table_name, element);
      }
    } else {
      // 修改最大级次
      modifyMaxLevel(element, parent_level_num + 1);
    }
  }

  /**
   * 根据当前的级次,比较更新要素表sys_element中的最大级次
   * 
   * @param element
   *            要素简称
   * @param curLevel
   *            当前级次
   * @throws Exception
   *             修改异常
   */
  public void modifyMaxLevel(String element, int curLevel) throws Exception {
    XMLData elementSet = getEleSetByCode(element);
    String maxLevel = (String) elementSet.get("max_level");
    if ((StringUtils.trimToNull(maxLevel) == null) || curLevel > Integer.parseInt(maxLevel)) {
      // 修改最大级次
      dao.executeBySql(new StringBuffer("update sys_element").append(Tools.addDbLink()).append(" set max_level=")
        .append(curLevel).append(" where ele_code ='")
        /*** Modify by fengdz TM:2012-03-19 ***/
        // .append(element.toUpperCase()).append("'").toString());
        .append(element.toUpperCase()).append("'").append(" and set_year=").append(getSetYear())
        .append(" and rg_code='" + getRgCode()).append("'").toString());
      /*** Modify by fengdz TM:2012-03-19 ***/

    }
  }

  /**
   * 检查要素的编码规则
   * 
   * @param element
   *            要素简称
   * @param chr_code
   *            要素编码
   * @throws Exception
   *             异常
   */
  private void checkCodeRule(String element, String chr_code) throws Exception {
    XMLData elementSet = getEleSetByCode(element);
    String code_rule = elementSet == null ? "" : (String) elementSet.get("code_rule");
    if (code_rule != null && !"".equals(code_rule)) {
      int totallevel = 0;
      try {

        StringTokenizer st = new StringTokenizer(code_rule, "-");
        while (st.hasMoreTokens()) {
          totallevel += Integer.parseInt(st.nextToken());
          if (chr_code.length() == totallevel) {
            return;
          }
        }
      } catch (Exception e) {
        throw new Exception("要素编码规则设置有误,请检查配置!");
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#deleteEle(java.lang
   * .String, java.lang.String)
   */
  public boolean deleteEle(String element, String chr_id) throws Exception {

    int delCount = 0;
    try {
      if (element == null || element.equals("")) {
        throw new Exception("非法的要素简称,无法执行删除!");
      }
      if (chr_id == null || chr_id.equals("")) {
        throw new Exception("非法的基础数据ID,无法执行删除!");
      }
      element = element.toUpperCase();
      // 查询要素简称对应的表
      XMLData setData = getEleSetByCode(element);
      // 保存部分信息
      String tableName = (String) setData.get("ele_source");
      if (tableName == null || "".equals(tableName)) {
        throw new Exception("根据要素简称" + element + "无法获取要素源表,无法执行查询!");
      }
      String isLocal = (String) setData.get("is_local");
      if (!"1".equals(isLocal)) {
        if (tableName.indexOf("@db_link") == -1)
          tableName += Tools.addDbLink();
      }
      StringBuffer strSQL = new StringBuffer();
      strSQL.append("select level_num,parent_id,set_year from ").append(tableName).append(" where chr_id = '")
        .append(chr_id)

        /**
         * ����˫�� �鿴��ǰѡ������Ƿ�ǰ��ȡ������� add by fengdz
         */
        // .append("'");
        .append("'").append(" and rg_code =").append(String.valueOf(SessionUtil.getRgCode())).append(" and set_year=")
        .append((String) SessionUtil.getLoginYear());
      /**
       * ����˫�� add by fengdz
       */

      List list = dao.findBySql(strSQL.toString());
      if (list.size() == 0) {
        throw new Exception("未查询到相应基础数据值,无法执行删除!");
      }
      String level_num = (String) ((XMLData) list.get(0)).get("level_num");
      String parent_id = (String) ((XMLData) list.get(0)).get("parent_id");
      String thisYear = (String) ((XMLData) list.get(0)).get("set_year");
      strSQL.delete(0, strSQL.length());
      if (isExistsInCcid(element.equalsIgnoreCase("ENMANAGER") ? "en" : element, chr_id)) {
        throw new Exception("部分要素已经被使用,无法删除!可修改为不启用！");
      }
      // 查询节点及所有子节点chr_id
      if (Integer.parseInt(level_num) > 0) {
        // zpd 2010-05-24 修改删除编码时判断是否银行编码，且被其他银行引用
        StringBuffer sql = new StringBuffer();
        boolean isBankEle = isBankElement(element);
        if (isBankEle) {// 如果是银行要素，则查询出银行标志
          sql.append("SELECT CHR_ID,chr_code,chr_name,AGENTFLAG,CLEARFLAG,INCOMEFLAG,SALARYFLAG FROM ");
        } else {
          sql.append("SELECT CHR_ID,chr_code,chr_name FROM ");
        }
        sql.append(tableName).append(" WHERE SET_YEAR='").append(thisYear).append("' AND CHR_ID").append(level_num)
          .append("='").append(chr_id).append("'");
        List ret = dao.findBySql(sql.toString());
        for (int i = 0; i < ret.size(); i++) {
          /**
           * 如果删除的是预算单位，则要判断当前要删除的预算单位是否拥有单位用户，
           * 如果有单位用户，则不能进行删除,对应bug：bug-2901
           */
          if ((element.equalsIgnoreCase("EN") || element.equalsIgnoreCase("ENMANAGER"))) {
            // 查询是否有用户的用户所属（belong_org）为当前预算单位的chr_id
            List userList = dao.findBySql("select belong_org from sys_usermanage where belong_org='"
              + ((XMLData) ret.get(i)).get("chr_id") + "'");
            // 如果userList不为空，则说明该预算单位下有单位用户，提示该预算单位不能删除
            if (userList != null && userList.size() > 0) {
              throw new Exception("单位【" + ((XMLData) ret.get(i)).get("chr_name") + "】拥有单位用户，不能执行删除！");
            }
          }
          // zpd 2010-05-24 修改删除编码时判断是否银行编码，且被其他银行引用
          String chrId = (String) ((XMLData) ret.get(i)).get("chr_id");
          StringBuffer delSb = new StringBuffer("DELETE FROM ").append(tableName).append(" WHERE CHR_ID='")
            .append(chrId).append("'");
          // 删除所有相关节点数据
          if (isBankEle) {// 如果是银行要素，则查询出银行标志
            if (isOnlyOneBankElement(element, (XMLData) ret.get(i))) {// 只被一个银行引用，执行删除操作
              String bapBankSql = "select 1 from vw_paymentaccount where bank_id='" + chrId + "'";
              List bapBankList = dao.findBySql(bapBankSql);
              if (null != bapBankList && bapBankList.size() > 0) {// 银行编码已经被bap支付账户引用
                throw new Exception("编码为 " + ((XMLData) ret.get(i)).get("chr_code") + " 的银行编码已经被BAP支付账户引用，请先删除引用再删除！");
              } else {// 未被bap支付账户引用的直接删除
                delCount = dao.executeBySql(delSb.toString());
              }
            } else {// 被多个银行引用，执行更新操作
              String updateSql = getUpdateBankSql(element, tableName, (String) ((XMLData) ret.get(i)).get("chr_id"));
              delCount = dao.executeBySql(updateSql);
            }
          } else {// 非银行要素
            delCount = dao.executeBySql(delSb.toString());
          }
        }
        // 如果父级下面没有任意子节点,则is_leaf需要修改为0
        if (parent_id != null && !"".equals(parent_id)) {
          sql.delete(0, sql.length());
          sql.append("update ").append(tableName).append(" a set is_leaf=1 where chr_id = '").append(parent_id)
            .append("' and (select count(*) from ").append(tableName).append(" where parent_id = '").append(parent_id)
            .append("')=0");
          dao.executeBySql(sql.toString());
        }
      }
      // 修改sys_element最大级次
      List maxLevelData = dao.findBySql(new StringBuffer("select nvl(max(level_num),0) as max_level from ")
        .append(tableName).append(" where set_year=").append(CommonUtil.getSetYear()).toString());
      String maxLevel = (String) ((XMLData) maxLevelData.get(0)).get("max_level");
      dao.executeBySql(new StringBuffer("update sys_element").append(Tools.addDbLink()).append(" set max_level=")
        .append(maxLevel).append(" where ele_code ='")
        /*** Modify by fengdz TM:2012-03-19 ***/
        .append(element.toUpperCase()).append("' and set_year=").append(getSetYear()).append(" and rg_code='")
        .append(getRgCode()).append("'").toString());
      // .append(element.toUpperCase()).append("'").toString());
      /*** Modify by fengdz TM:2012-03-19 ***/
    } catch (Exception e) {
      throw e;
    }
    return delCount > 0;
  }

  /**
   * 更新要素对应银行表标识为0
   * 
   * @param element
   *            要素名称
   * @param tableName
   *            表名
   * @param chrId
   *            银行编码数据
   * @return 更新ele_bank对应的银行标志为0
   * @author zhupeidong at 2010-5-24下午05:16:35
   */
  private String getUpdateBankSql(String element, String tableName, String chrId) {
    StringBuffer updateSb = new StringBuffer("UPDATE ").append(tableName);
    if (element.equals("PB")) {
      updateSb.append(" set agentflag=0 ");
    }
    if (element.equals("CB")) {
      updateSb.append(" set clearflag=0 ");
    }
    if (element.equals("IB")) {
      updateSb.append(" set incomeflag=0 ");
    }
    if (element.equals("TB")) {
      updateSb.append(" set salaryflag=0 ");
    }

    updateSb.append(" WHERE CHR_ID='").append(chrId).append("'");
    return updateSb.toString();
  }

  /**
   * 判断该银行是否被其他银行引用
   * 
   * @param element
   *            要素名称
   * @param bankMap
   *            银行编码数据
   * @return true没有被其他银行引用，false被其他银行引用
   * @author zhupeidong at 2010-5-24下午05:17:50
   */
  private boolean isOnlyOneBankElement(String element, Map bankMap) {
    String pb = (String) bankMap.get("agentflag");
    String cb = (String) bankMap.get("clearflag");
    String ib = (String) bankMap.get("incomeflag");
    String tb = (String) bankMap.get("salaryflag");
    if (!element.equals("PB") && pb.equals("1")) {
      return false;
    }
    if (!element.equals("CB") && cb.equals("1")) {
      return false;
    }
    if (!element.equals("IB") && ib.equals("1")) {
      return false;
    }
    if (!element.equals("TB") && tb.equals("1")) {
      return false;
    }
    return true;
  }

  /**
   * 是否银行要素
   * 
   * @param element
   *            要素名称
   * @return true银行编码 false非银行编码
   * @author zhupeidong at 2010-5-24下午05:19:38
   */
  private boolean isBankElement(String element) {
    if (element.equals("PB") || element.equals("CB") || element.equals("IB") || element.equals("TB")) {
      return true;
    } else {
      return false;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#getEleByID(java.lang
   * .String, java.lang.String)
   */
  public XMLData getEleByID(String table_name, String chr_id) throws RuntimeException {
    XMLData data = null;
    StringBuffer sql = new StringBuffer();
    try {
      sql
        .append(
          "select e.*," + SQLUtil.replaceLinkChar("e.chr_code || ' ' || e.chr_name") + " as codename, (select chr_name from "
            + table_name
            + " ee where ee.chr_id=e.parent_id and ee.Set_Year = e.Set_Year and ee.RG_CODE = e.RG_CODE) as parent_name from ")
        .append(table_name).append(" e where e.chr_id = '").append(chr_id)
        .append("' AND e.Set_Year = ? and e.RG_CODE=? ");
      List list = dao.findBySql(sql.toString(), new Object[] { this.getSetYear(), this.getRgCode() });
      if (list.size() > 0) {
        data = (XMLData) list.get(0);
      }
    } catch (Exception e) {
      Log.error(e.getMessage() + org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
      System.out.println("执行sql出错：\n" + e.getMessage() + "\nSQL开始\n" + sql + "\nSQL结束");
      throw new RuntimeException(e.getMessage() + "sql语句：\n" + sql);
    }
    return data;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#getCondition(gov.gfmis
   * .fap.dictionary.dto.FElementDTO, java.lang.String)
   */
  /** modify by wl 修改变量绑定*/
  public String getCondition(FElementDTO elementDto, String tableAlias) throws Exception {
    StringBuffer return_sql = new StringBuffer();
    String ele_code = "";
    String ele_source = "";
    String ele_value = "";
    String level_num = "";
    boolean isIDOrCode = true; // 标定传入的是id还是code,true为id,false为code
    if (elementDto != null) {
      if (elementDto.getEle_code() == null || elementDto.getEle_code().toString().equals("")) {
        throw new Exception("请传入要素简称!");
      } else {
        ele_code = elementDto.getEle_code().toString();
      }
      if (elementDto.getChr_id() == null || elementDto.getChr_id().toString().equals("")) {
        // 同时允许传入id和code,以id为主,code为辅
        if (elementDto.getChr_code() == null || elementDto.getChr_code().equals("")) {
          throw new Exception("请传入要素值!");
        } else {
          isIDOrCode = false;
          ele_value = elementDto.getChr_code();
        }
      } else {
        isIDOrCode = true;
        ele_value = elementDto.getChr_id();
      }

      if (elementDto.getEle_source() == null || elementDto.getEle_source().toString().equals("")) {
        List ret = dao.findBySql(
          new StringBuffer("select * from sys_element").append(Tools.addDbLink()).append(" where ele_code=upper(?")
            .append(") and set_year = ?")
            /*** Modify by fengdz TM:2012-03-19 ***/
            // .append(CommonUtil.getSetYear()).toString());
            .append(" and rg_code=?").toString(),
          new Object[] { ele_code, new Integer(this.getSetYear()), this.getRgCode() });
        /*** Modify by fengdz TM:2012-03-19 ***/
        if (ret.size() > 0) {
          ele_source = ((XMLData) ret.get(0)).get("ele_source").toString();
        } else {
          throw new Exception("根据要素简称无法获取要素表信息!");
        }
      } else {
        ele_source = elementDto.getEle_source().toString();
      }

      if (elementDto.getLevel_num() == null || elementDto.getLevel_num().toString().equals("")) {
        List ret = dao.findBySql(new StringBuffer("select * from ").append(ele_source).append(Tools.addDbLink())
          .append(" where ").append((isIDOrCode ? "chr_id='" : "chr_code='")).append(ele_value)
          .append("' and set_year = ").append(CommonUtil.getSetYear()).toString());
        if (ret.size() > 0) {
          level_num = ((XMLData) ret.get(0)).get("level_num").toString();
        } else {
          throw new Exception("要素不存在,无法获取查询条件!");
        }
      } else {
        level_num = elementDto.getLevel_num().toString();
      }
      if (tableAlias == null || tableAlias.equals("")) {
        throw new Exception("请传入相应别名!");
      }
      return_sql.append(" and exists (select 1 from ").append(ele_source).append(Tools.addDbLink()).append(" b where ")
        .append((isIDOrCode ? "b.chr_id" : "b.chr_code")).append(level_num).append("='").append(ele_value)
        .append("' and set_year = ").append(CommonUtil.getSetYear()).append(" and b.chr_id = ").append(tableAlias)
        .append(".").append(ele_code).append("_id)");
    }
    return return_sql.toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#getLTEleCode(java.
   * lang.String)
   */
  /** modify by wl 修改变量绑定*/
  public String getLTEleCode(String fEleCode) {
    StringBuffer strSQL = new StringBuffer("select czgb_code from sys_element ").append(Tools.addDbLink())
    /*** Modify by fengdz TM:2012-03-19 ***/
    // .append(" where ele_code='").append(fEleCode).append("'");
      .append(" where ele_code=?").append(" and set_year=?").append(" and rg_code=?");
    /*** Modify by fengdz TM:2012-03-19 ***/
    List lst = dao.findBySql(strSQL.toString(),
      new Object[] { fEleCode, new Integer(this.getSetYear()), this.getRgCode() });
    if (lst.size() > 0) {
      Map map = (Map) lst.get(0);
      return (String) map.get("czgb_code");
    }
    return "";

  }

  /**
   * 根据不同的要素简称,返回最基本的查询字段
   * 
   * @param element
   *            要素简称
   * @param tableName
   *            要素表名
   * @param tableAlias
   *            要素表简称
   * @param column
   *            设定的字段
   */
  protected String getEleFieldSQL(String element, String tableName, String tableAlias, String[] column) {
    StringBuffer strSQL = new StringBuffer();
    // 添加查询的字段列表
    if (column == null || column.length == 0) {
      column = new String[] { "CHR_ID", "CHR_CODE", "CHR_NAME", "IS_LEAF", "LEVEL_NUM", "PARENT_ID", "SET_YEAR",
        "ENABLED"
      //        , "CHR_CODE1", "CHR_CODE2", "CHR_CODE3", "CHR_CODE4", "CHR_CODE5" 
      };
    }
    if (column[0].equals("*")) {
      strSQL.append(tableAlias).append(".*");
    } else {
      Map map = new HashMap();
      for (int i = 0; i < column.length; i++) {
        map.put(column[i], "");
      }
      // 目的为了去掉重复字段
      if (element.equalsIgnoreCase("bap") || element.equalsIgnoreCase("bac") || element.equalsIgnoreCase("bai")
        || element.equalsIgnoreCase("agent_account") || element.equalsIgnoreCase("black_account")
        /* added by lixf 20061208 start */
        || element.equalsIgnoreCase("FSBAP"))
      /* added by lixf 20061208 end */
      {
        map.put("bank_id", "");
        map.put("bank_code", "");
        map.put("bank_name", "");
        map.put("account_no", "");
        map.put("account_name", "");
        map.put("owner_code", "");
        map.put("owner_name", "");
      } else if (element.equalsIgnoreCase("payee_account")) {
        map.put("bank_code", "");
        map.put("bank_name", "");
        map.put("account_no", "");
        map.put("account_name", "");
        map.put("owner_code", "");
        map.put("payee_name", "");
        map.put("account_type", "");

      }
      Object[] object = map.keySet().toArray();
      for (int i = 0; i < object.length; i++) {
        strSQL.append(tableAlias).append(".").append(object[i].toString()).append(",");
      }
      // 添加上parent_name
      // parent_name增加多年度多财政过滤 modify by zhangsheng 2012.6.11
      if (tableName.equalsIgnoreCase("ELE_REGION")) {
        strSQL.append("(select chr_name from ").append(tableName).append(" where chr_id = ").append(tableAlias)
          .append(".parent_id and set_year = " + SessionUtil.getLoginYear() + ") as parent_name");
      } else {
        strSQL
          .append("(select chr_name from ")
          .append(tableName)
          .append(" where chr_id = ")
          .append(tableAlias)
          .append(
            ".parent_id and set_year = " + SessionUtil.getLoginYear() + " and rg_code = " + SessionUtil.getRgCode()
              + ") as parent_name");
      }
    }
    strSQL.append(" , " + SQLUtil.replaceLinkChar("CHR_CODE||' '||CHR_NAME") + " as codename");
    return strSQL.toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#getEleSetByXml(java
   * .lang.String, boolean)
   */
  public String getEleSetByXml(String inXmlObj, boolean isNeedCount) throws Exception {
    String total_count = "-1";
    XMLData condition = ParseXML.convertXmlToObj(inXmlObj);
    FPaginationDTO page = new FPaginationDTO();
    List ret = null;

    String sql = ParseXML.convertXmlToQuerySQL(inXmlObj);
    sql += " and set_year = '" + SessionUtil.getLoginYear() + "' and rg_code = '" + SessionUtil.getRgCode() + "'";
    total_count = StringUtil.toStr(CommonUtil.getRowCount(sql, dao));

    if (isNeedCount) {
      String index = (String) condition.getSubObject("page_index");
      int indexInt = (index == null || index.equals("")) ? 0 : NumberUtil.toInt(index);
      String count = (String) condition.getSubObject("page_count");
      int countInt = (count == null || count.equals("")) ? 0 : NumberUtil.toInt(count);
      // 设置分页信息
      page.setCurrpage(indexInt);
      page.setPagecount(countInt);

      ret = CommonUtil.findByPage(sql, page, dao);
    } else {
      ret = dao.findBySql(sql);
    }

    XMLData data = new XMLData();
    data.put("total_count", total_count);
    data.put("row", ret);
    return ParseXML.convertObjToXml(data, "data");
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#insertEleSetByXml(
   * java.lang.String)
   */
  public synchronized XMLData insertEleSetByXml(String inXmlObj) throws Exception {
    XMLData data = null;
    try {
      String chr_id = UUIDRandom.generate();

      String thisYear = CommonUtil.getSetYear();
      String ele_code = (String) ParseXML.getSubObjectOfXml(inXmlObj, "ele_code");
      // 针对基础数据管理的默认值设置
      Map defaultField = new XMLData();
      defaultField.put("SET_YEAR", thisYear);
      defaultField.put("RG_CODE", getRgCode());
      defaultField.put("CHR_ID", chr_id);
      defaultField.put("CREATE_DATE", DateHandler.getToday());
      defaultField.put("CREATE_USER", CommonUtil.getUserId());
      defaultField.put("LATEST_OP_DATE", DateHandler.getToday());
      defaultField.put("LATEST_OP_USER", CommonUtil.getUserId());
      defaultField.put("LAST_VER", DateHandler.getLastVerTime());

      // 数据查重
      CommonUtil.checkRepeat("sys_element" + Tools.addDbLink(), "ele_code",

      ele_code, " and chr_id <>'" + chr_id + "' and rg_code='" + getRgCode() + "' and set_year=" + getSetYear(), dao);
      dao.executeBySql(ParseXML.covertXmlToInsertSQL(inXmlObj, defaultField));
      data = getEleSetByID(chr_id);// 查询同时通过id和code缓存
    } catch (Exception e) {
      throw e;
    }
    return data;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#modifyEleSetByXml(
   * java.lang.String)
   */
  public synchronized XMLData modifyEleSetByXml(String inXmlObj) throws Exception {
    XMLData data = null;
    try {
      XMLData xml = ParseXML.convertXmlToObj(inXmlObj);
      String chr_id = (String) xml.getSubObject("par_value"); // 被修改记录的唯一ID
      String ele_code = (String) ParseXML.getSubObjectOfXml(inXmlObj, "ele_code");
      // 针对基础数据管理的默认值设置
      Map defaultField = new XMLData();
      defaultField.put("LATEST_OP_DATE", DateHandler.getToday());
      defaultField.put("LATEST_OP_USER", CommonUtil.getUserId());
      defaultField.put("LAST_VER", DateHandler.getLastVerTime());

      // 数据查重
      CommonUtil.checkRepeat("sys_element" + Tools.addDbLink(), "ele_code", ele_code, " and chr_id <>'" + chr_id + "'",
        dao);
      dao.executeBySql(ParseXML.covertXmlToModifySQL(inXmlObj, defaultField));
      data = getEleSetByID(chr_id);// 同时通过id和code缓存
    } catch (Exception e) {
      throw e;
    }
    return data;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#deleteEleSet(java.
   * lang.String)
   */
  public boolean deleteEleSet(String id) throws Exception {
    int operationResult = 0;
    try {

      if (id == null || id.equals("")) {
        throw new Exception("非法的基础数据ID,无法执行删除!");
      }
      XMLData removeData = getEleSetByID(id);
      if (removeData == null) {
        throw new Exception("未查询到相应要素配置信息,无法执行删除!");
      }

      operationResult = dao.executeBySql("delete from sys_element where chr_id=? and rg_code=? and set_year=? ",
        new Object[] { id, getRgCode(), getSetYear() });
    } catch (Exception e) {
      throw e;
    }
    return operationResult > 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#getEleSetByID(java
   * .lang.String)
   */
  /** modify by wl 修改变量绑定*/
  public XMLData getEleSetByID(String chr_id) {
    StringBuffer sql = new StringBuffer();
    sql.append("select * from sys_element where chr_id = ? and rg_code=? and set_year=?");
    List list = dao
      .findBySql(sql.toString(), new Object[] { chr_id, this.getRgCode(), new Integer(this.getSetYear()) });
    if (list.size() > 0)
      return (XMLData) list.get(0);
    else
      return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#getEleSetByCode(java
   * .lang.String)
   */
  /** modify by wl 修改变量绑定*/
  public XMLData getEleSetByCode(String eleCode) {
    XMLData data = null;
    if (eleCode != null && !eleCode.equals("")) {
      StringBuffer sql = new StringBuffer();
      sql.append("select * from sys_element where ele_code = upper(?) and set_year = ? and rg_code=? ");
      List list = dao.findBySql(sql.toString(),
        new Object[] { eleCode, new Integer(this.getSetYear()), this.getRgCode() });
      if (list.size() > 0) {
        data = (XMLData) list.get(0);
      }
    }
    if (data == null)
      return null;
    return data;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#cachedEleSet(gov.gfmis
   * .fap.util.XMLData, boolean)
   */
  public List getEleSetByCondition(String condition) {
    String sql = "select * from sys_element where rg_code =? and set_year=? " + condition;
    return daoSupport.genericQuery(sql, new Object[] { getRgCode(), getSetYear() }, ElementSetXMLData.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#cachedEleSet(gov.gfmis
   * .fap.util.XMLData, boolean) ymj 返回刷新视图列信息
   */
  public List getFreshViewCol(String ele_code, String old_name, String new_name) {

    return daoSupport.genericQuery(
      "select m.*,d.ui_detail_id, d.title as old_name , '" + new_name
        + "' as new_name from sys_uimanager m,sys_uidetail d where " + " m.rg_code='" + getRgCode()
        + "' and m.set_year='" + getSetYear() + "' and m.set_year=d.set_year and m.rg_code=d.rg_code and "
        + "m.ui_id = d.ui_id and  (lower(d.id)='" + ele_code.toLowerCase() + "_id' or lower(d.id)='"
        + ele_code.toLowerCase() + "_code' or lower(d.id)='" + ele_code.toLowerCase() + "_name')",
      ElementSetXMLData.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#cachedEleSet(gov.gfmis
   * .fap.util.XMLData, boolean) ymj 刷新视图列信息
   */
  public void freshViewCol(String ele_code, String ele_name) {
    dao.executeBySql("update sys_uidetail set title='" + ele_name + "' where " + " rg_code='" + getRgCode()
      + "' and set_year='" + getSetYear() + "' and " + "lower(id)='" + ele_code.toLowerCase() + "_id' or lower(id)='"
      + ele_code.toLowerCase() + "_code' or lower(id)='" + ele_code.toLowerCase() + "_name'");
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#getSqlElemRight(java
   * .lang.String, java.lang.String, java.lang.String, java.lang.String)
   */
  public String getSqlElemRight(String userid, String roleid, String elemcode, String tablealias) throws Exception {
    return right.getSqlElemRight(userid, roleid, elemcode, tablealias);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.ElementOperationA#isExistsInCcid(java
   * .lang.String, java.lang.String)
   */
  public boolean isExistsInCcid(String element, String chr_id) {
    boolean isExists = false;
    StringBuffer sql = new StringBuffer();
    List ccidTables = getCcidTables();
    for (int i = 0; i < ccidTables.size(); i++) {
      sql.delete(0, sql.length());
      sql.append("select ").append(element).append("_id from ").append(ccidTables.get(i)).append(Tools.addDbLink())
        .append(" where ").append(element).append("_id = '").append(chr_id).append("'");
      try {
        List list = dao.findBySql(sql.toString());
        if (list.size() > 0) {
          isExists = true;
          break;
        }
      } catch (Exception e) {
        // 不存在相应字段默认为不控制
      }
    }
    return isExists;
  }

  /** 判断当预算单位修改时，此时的预算单位属性是否可以修改 */
  /** modify bu wl 修改变量绑定*/
  public boolean checkIsReform(Map fieldInfo) {
    String chr_code = (String) fieldInfo.get("chr_code");
    String isReform = (String) fieldInfo.get("is_reform");
    StringBuffer isCheck = new StringBuffer();
    isCheck.append("select is_reform from ele_enterprise where chr_code=? and set_year=? and rg_code=? ");
    List isCheckList = dao.findBySql(isCheck.toString(),
      new Object[] { chr_code, new Integer(this.getSetYear()), this.getRgCode() });

    if (isCheckList != null && isCheckList.size() > 0) {
      XMLData dat = (XMLData) isCheckList.get(0);
      String str = (String) dat.get("is_reform");
      if (!str.equalsIgnoreCase(isReform)) {
        StringBuffer validateSql1 = new StringBuffer();
        StringBuffer validateSql2 = new StringBuffer();
        validateSql1.append("select * from gl_journal  where ccid in (select ccid from gl_ccids where en_code = ? )");
        validateSql2
          .append("select * from gl_journal_his where ccid in (select ccid from gl_ccids where en_code = ? )");
        List list1 = dao.findBySql(validateSql1.toString(), new Object[] { chr_code });
        List list2 = dao.findBySql(validateSql2.toString(), new Object[] { chr_code });
        if (list1.size() != 0 || list2.size() != 0) {
          return false;
        } else {
          return true;
        }
      }
    }
    return true;
  }

  public ElementInfo getElementInfo(final String eleCode, String chr_id) {
    XMLData eleSet = getEleSetByCode(eleCode);
    final String tableName = (String) eleSet.get("ele_source");
    final XMLData eleInfo = getEleByID(tableName, chr_id);
    if (eleInfo == null)
      return null;
    return new XmlElementInfo(eleInfo, eleCode, tableName);
  }

  /*
   * 取得 存放CCID列表，默认gl_ccids表。
   */
  public List getCcidTables() {
    List ccidTables = daoSupport
      .queryForStrings("select distinct ccids_table from gl_coa where ccids_table is not null ");
    if (!ccidTables.contains("gl_ccids") || !ccidTables.contains("GL_CCIDS")) {
      ccidTables.add("gl_ccids");
    }
    return ccidTables;
  }

  class XmlElementInfo implements ElementInfo {
    XMLData eleInfo = null;

    String eleCode;

    String tableName;

    public XmlElementInfo(XMLData data, String eleCode, String tableName) {
      eleInfo = data;
      this.eleCode = eleCode;
      this.tableName = tableName;
    }

    public String getChrCode() {
      return (String) eleInfo.get(ElementDefinition.CHR_CODE);
    }

    public String getChrId() {
      return (String) eleInfo.get(ElementDefinition.CHR_ID);
    }

    public String getChrName() {
      return (String) eleInfo.get(ElementDefinition.CHR_NAME);
    }

    public int getLevelNum() {
      return NumberUtil.toInt((String) eleInfo.get(ElementDefinition.LEVEL_NUM));
    }

    public boolean isLeaf() {
      return "1".equals(eleInfo.get(ElementDefinition.IS_LEAF));
    }

    public String getParentId() {
      return (String) eleInfo.get(ElementDefinition.PARENT_ID);
    }

    public List getChildren() {
      return dao.findBySql("select * from " + tableName + " where parent_id='" + getParentId() + "'");
    }

    public ElementInfo getParent() {
      return getElementInfo(eleCode, getParentId());
    }

    public void removeChild(ElementInfo elementInfo) {
      throw new UnsupportedOperationException();
    }

    public String getAccountNo() {
      return (String) eleInfo.get(ElementDefinition.ACCOUNT_NO);
    }

    public String getAccountName() {
      return (String) eleInfo.get(ElementDefinition.ACCOUNT_NAME);
    }

    public String getBankName() {
      return (String) eleInfo.get(ElementDefinition.BANK_NAME);
    }

    public boolean isRoot() {
      return false;
    }

    public String getSetYear() {
      return (String) CommonUtil.getSetYear();
    }

    public String getRgCode() {
      return (String) CommonUtil.getRgCode();
    }

    public Object getKey() {
      return new ElementCacheKey((String) eleInfo.get(ElementDefinition.CHR_ID), (String) CommonUtil.getSetYear(),
        (String) eleInfo.get(ElementDefinition.RG_CODE));
    }

    public Object getParentKey() {
      return new ElementCacheKey((String) eleInfo.get(ElementDefinition.PARENT_ID), (String) CommonUtil.getSetYear(),
        (String) eleInfo.get(ElementDefinition.RG_CODE));
    }
  }

  public void refreshCCID(String eleName, String chr_id, String chr_code, String chr_name) throws Exception {
    String sql = "select 1 from user_tab_columns t where t.TABLE_NAME = 'GL_CCIDS' and t.COLUMN_NAME = ?";
    List ls = dao.findBySql(sql, new Object[] { eleName.toUpperCase() + "_ID" });
    if (ls == null || ls.isEmpty())
      return;

    StringBuffer sb = new StringBuffer();
    sb.append("update gl_ccids set ").append(eleName).append("_code = '").append(chr_code).append("', ")
      .append(eleName).append("_name = '").append(chr_name).append("' where ").append(eleName).append("_id = '")
      .append(chr_id).append("'");
    dao.executeBySql(sb.toString());
  }

  public List getElementData(String chr_id) throws Exception {
    StringBuffer sb = new StringBuffer();
    sb.append("select * from sys_element where ele_code = ?  and rg_code=? and set_year=? ");
    return dao.findBySql(sb.toString(), new Object[] { chr_id, getRgCode(), getSetYear() });
  }

  public String getEleCodeFromTableName(String tableName) {
    StringBuffer sb = new StringBuffer();
    sb.append("select ele_code from sys_element where ele_source = ? and rg_code=? and set_year=? ");
    List ls = dao.findBySql(sb.toString(), new Object[] { tableName.toUpperCase(), getRgCode(), getSetYear() });
    if (ls == null || ls.isEmpty())
      return "";
    else {
      XMLData xd = (XMLData) ls.get(0);
      return xd.get("ele_code") == null ? "" : xd.get("ele_code").toString();
    }
  }

  // 张晓楠添加，2010-10-26，从湖北包移至主线，start
  /**
   * 获得必录入的字段。
   * 
   * @return
   */
  public List getMustInputFields(String uiCode) {
    StringBuffer sql = new StringBuffer();
    sql.append("select uidtl.title as title, uidtl.field_name as name, uidtl.id as id ");
    sql.append("from sys_uimanager uimgr, sys_uidetail uidtl ");
    sql
      .append("where uidtl.ui_id = uimgr.ui_id and uimgr.ui_code= ? and uimgr.set_year=? and uidtl.is_must_input='true' and uidtl.set_year = uimgr.set_year ");
    return dao.findBySql(sql.toString(), new Object[] { uiCode, getSetYear() });
  }

  /***
   * modify bu wl 修改变量绑定
   */
  public XMLData getEleByCode(String table_name, String chr_code) {
    StringBuffer sql = new StringBuffer();
    sql.append("select e.*, (select chr_name from ").append(table_name).append(" ee ");
    sql.append("where ee.chr_id=e.parent_id) as parent_name from ").append(table_name).append(" e ");
    sql.append("where chr_code = ?");
    List list = dao.findBySql(sql.toString(), new Object[] { chr_code });
    if (list.size() > 0) {
      return (XMLData) list.get(0);
    }

    return null;
  }

  // 张晓楠添加，2010-10-26，从湖北包移至主线，end

  public XMLData getEleByConditionWithRgCode(String element, int pageIndex, int pageCount, String[] column,
    boolean isNeedRight, String coa_id, Map relation, String condition) throws Exception {
    return null;
  }

  /**
   * 清除要素缓存
   * @param element 要素简称
   */
  public void clearElementCatchByEleCode(List eleCodes, String setYear, String rg_code) throws Exception {

  }
}