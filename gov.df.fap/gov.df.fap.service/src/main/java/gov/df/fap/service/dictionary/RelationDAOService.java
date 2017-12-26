package gov.df.fap.service.dictionary;

import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.xml.ParseXML;
import gov.df.fap.util.xml.XMLData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * /** RelationDAO 关联关系数据库访问类
 * 
 * @version 1.0
 * @author 
 * @since java 1.6.0.34 <BR>
 *        ==============================================<BR>
 *        修改记录<BR>
 *        Author ： 冯德贞<BR>
 *        数据权限（要素关联关系维护）<BR>
 *        【表：SSYS_RELATION_MANAGER】<BR>
 *        001 修改表结构 alter table SYS_RELATION_MANAGER add rg_code VARCHAR2(42)<BR>
 *        002 修改类RelationDAO修改方法insertRelation<BR>
 *        003 修改类RelationDAO修改方法modifyRelation<BR>
 *        004 修改类RelationDAO修改方法getRelationByPriEleValue<BR>
 *        005 修改类RelationDAO修改方法deleteRelation<BR>
 *        006 修改类RelationDAO修改方法getRelationID<BR>
 *        007 修改类RelationDAO修改方法getRelationByXml<BR>
 *        008 修改类RelationDAO修改方法getRelation<BR>
 *        009 修改表结构alter table sys_relations add rg_code VARCHAR2(42)
 */
@Component
public class RelationDAOService extends DBOperation {
  /**
   * 默认构造函数
   */
  @Autowired
  @Qualifier("generalDAO")
  protected GeneralDAO dao = null;

  public RelationDAOService() {
  }

  /**
   * 通过传入的xml查询数据并返回结果
   * 
   * @param inXmlObj
   *            传入的xml
   * @return 查询结果xml
   * @throws Exception
   */
  public String getRelationByXml(String inXmlObj, boolean isNeedCount) throws Exception {
    String total_count = "-1";
    XMLData condition = ParseXML.convertXmlToObj(inXmlObj);
    StringBuffer strSQL = new StringBuffer();
    strSQL.append(ParseXML.convertXmlToQuerySQL(inXmlObj));
    /*** Add by fengdz TM:2012-03-19 ***/
    String rg_code = getRgCode();
    String setYear = getSetYear();
    strSQL.append(" and rg_code='").append(rg_code).append("' and set_year=").append(setYear);
    /*** Add by fengdz TM:2012-03-19 ***/
    if (isNeedCount) {
      // total_count = getCount(strSQL.toString());
      String index = (String) condition.getSubObject("page_index");
      index = (index == null || index.equals("")) ? "0" : index;
      String count = (String) condition.getSubObject("page_count");
      count = (count == null || count.equals("")) ? "0" : count;
      int pageIndex = Integer.parseInt(index);
      int pageCount = Integer.parseInt(count);
      setPageInfo(pageIndex, pageCount); // 设置分页信息
    } else {
      this.setPageIndex(0);
    }
    List ret = queryBySql(strSQL.toString());
    XMLData data = new XMLData();
    data.put("total_count", total_count);
    data.put("row", ret);
    return ParseXML.convertObjToXml(data, "data");
  }

  /**
   * 通过传入的xml查询数据并返回结果
   * 无需分页
   * 
   * @param inXmlObj
   *            传入的xml
   * @return 查询结果xml
   * @throws Exception
   */
  public String getRelationByXml(String inXmlObj) throws Exception {
    String total_count = "-1";
    XMLData condition = ParseXML.convertXmlToObj(inXmlObj);
    StringBuffer strSQL = new StringBuffer();
    strSQL.append(ParseXML.convertXmlToQuerySQL(inXmlObj));
    /*** Add by fengdz TM:2012-03-19 ***/
    String rg_code = getRgCode();
    String setYear = getSetYear();
    strSQL.append(" and rg_code='").append(rg_code).append("' and set_year=").append(setYear);
    /*** Add by fengdz TM:2012-03-19 ***/
    total_count = getCount(strSQL.toString());
    String index = (String) condition.getSubObject("page_index");
    List ret = queryBySql(strSQL.toString());
    XMLData data = new XMLData();
    data.put("total_count", total_count);
    data.put("row", ret);
    return ParseXML.convertObjToXml(data, "data");
  }

  /*** Add by fengdz TM:2012-03-19 ***/
  public String getRgCode() {
    String rg_code = SessionUtil.getRgCode();

    return rg_code;
  }

  /*** Add by fengdz TM:2012-03-19 ***/
  /**
   * 通过传入的xml查询数据并返回结果
   * 
   * @param inXmlObj
   *            传入的xml
   * @return 查询结果xml
   * @throws Exception
   */
  public XMLData getRelation(String inXmlObj, boolean isNeedCount) throws Exception {
    String total_count = "-1";
    XMLData condition = ParseXML.convertXmlToObj(inXmlObj);
    StringBuffer strSQL = new StringBuffer();
    strSQL.append(ParseXML.convertXmlToQuerySQL(inXmlObj));
    /*** Add by fengdz TM:2012-03-19 ***/
    String rg_code = getRgCode();
    String setYear = getSetYear();
    strSQL.append(" and rg_code='").append(rg_code).append("' and set_year=").append(setYear);
    /*** Add by fengdz TM:2012-03-19 ***/
    if (isNeedCount) {
      total_count = this.getTotalCount(strSQL.toString());
      String index = (String) condition.getSubObject("page_index");
      index = (index == null || index.equals("")) ? "0" : index;
      String count = (String) condition.getSubObject("page_count");
      count = (count == null || count.equals("")) ? "0" : count;
      int pageIndex = Integer.parseInt(index);
      int pageCount = Integer.parseInt(count);
      setPageInfo(pageIndex, pageCount); // 设置分页信息
    } else {
      this.setPageIndex(0);
    }
    List ret = queryBySql(strSQL.toString());
    XMLData data = new XMLData();
    data.put("total_count", total_count);
    data.put("row", ret);
    return data;
  }

  /**
   * 关联关系明细表数据插入。 /关联关系主表信息
   * 
   * @param relationData
   *            关联关系数据结构,结构为:relationData \row 明细表配置信息XMLData --> (主控要素编码 -
   *            被控要素列表)值对
   * @return 关联关系唯一id
   * @throws Exception
   *             数据插入操作失败原因
   */
  public String insertRelation(XMLData relationData) throws Exception {
    String pri_ele_code = (String) relationData.get("pri_ele_code");
    String sec_ele_code = (String) relationData.get("sec_ele_code");
    String relation_code = (String) relationData.get("relation_code");
    String relation_type = (String) relationData.get("relation_type");
    XMLData detail = (XMLData) relationData.get("row");
    Connection conn = null;
    PreparedStatement ps = null;
    org.hibernate.Session session = null;
    StringBuffer strSQL = new StringBuffer();
    String set_year = "";
    String user_id = "";
    String relation_id = "";
    try {
      relation_id = UUIDRandom.generate();
      session = dao.getSession();
      conn = session.connection();
      set_year = SessionUtil.getLoginYear();
      user_id = (String) SessionUtil.getUserInfoContext().getAttribute("user_id");
      /*** Add by fengdz TM:2012-03-19 ***/
      String rg_code = getRgCode();
      /*** Add by fengdz TM:2012-03-19 ***/
      // 验证code是否重复
      strSQL.append("update sys_relation_manager").append(Tools.addDbLink())
      /*** Modify by fengdz TM:2012-03-19 ***/
      // .append(
      // " set relation_code=relation_code where relation_code=? and set_year=?");
        .append(" set relation_code=relation_code where relation_code=? and set_year=? and rg_code=? ");
      /*** Modify by fengdz TM:2012-03-19 ***/
      ps = conn.prepareStatement(strSQL.toString());
      ps.setString(1, relation_code);
      ps.setInt(2, Integer.parseInt(set_year));
      /*** Add by fengdz TM:2012-03-19 ***/
      ps.setString(3, rg_code);
      /*** Add by fengdz TM:2012-03-19 ***/
      if (ps.executeUpdate() != 0) {
        throw new Exception("关联关系编码已经存在!");
      }

      ps.close();
      ps = null;
      strSQL.delete(0, strSQL.length());
      strSQL.append("update sys_relation_manager").append(Tools.addDbLink()).append(
      /*** Modify by fengdz TM:2012-03-19 ***/
      // " set pri_ele_code=? where pri_ele_code=? and sec_ele_code=? and set_year=?");
        " set pri_ele_code=? where pri_ele_code=? and sec_ele_code=? and set_year=? and rg_code=? ");
      /*** Modify by fengdz TM:2012-03-19 ***/
      ps = conn.prepareStatement(strSQL.toString());
      ps.setString(1, pri_ele_code);
      ps.setString(2, pri_ele_code);
      ps.setString(3, sec_ele_code);
      ps.setInt(4, Integer.parseInt(set_year));
      /*** Add by fengdz TM:2012-03-19 ***/
      ps.setString(5, rg_code);
      /*** Add by fengdz TM:2012-03-19 ***/
      if (ps.executeUpdate() != 0) {
        throw new Exception("所选主控要素与被控要素之间的关联关系已存在!");
      }
      // 插入主表数据
      ps.close();
      ps = null;
      strSQL.delete(0, strSQL.length());
      strSQL.append("insert into sys_relation_manager").append(Tools.addDbLink())
        .append("(relation_id,relation_code,pri_ele_code,")
        .append("sec_ele_code,set_year,create_date,create_user,latest_op_date,latest_op_user,")
        /*** Modify by fengdz TM:2012-03-19 ***/
        // .append("is_deleted,last_ver,relation_type) values (?,?,?,?,?,?,?,?,?,?,?,?)");
        .append("is_deleted,last_ver,relation_type,rg_code) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
      /*** Modify by fengdz TM:2012-03-19 ***/
      ps = conn.prepareStatement(strSQL.toString());
      ps.setString(1, relation_id);
      ps.setString(2, relation_code);
      ps.setString(3, pri_ele_code);
      ps.setString(4, sec_ele_code);
      ps.setInt(5, Integer.parseInt(set_year));
      ps.setString(6, DateHandler.getLastVerTime());
      ps.setString(7, user_id);
      ps.setString(8, DateHandler.getLastVerTime());
      ps.setString(9, user_id);
      ps.setInt(10, 0);
      ps.setString(11, DateHandler.getLastVerTime());
      ps.setString(12, relation_type);
      /*** Add by fengdz TM:2012-03-19 ***/
      ps.setString(13, rg_code);
      /*** Add by fengdz TM:2012-03-19 ***/
      if (ps.executeUpdate() == 0) {
        throw new Exception("无法正常插入关联关系配置!");
      }
      // 循环插入关联关系配置明细信息
      ps.close();
      ps = null;
      strSQL.delete(0, strSQL.length());
      if (detail != null && detail.keySet().size() > 0) {
        // 实现批处理模式
        strSQL.append("insert into sys_relations").append(Tools.addDbLink())
          .append("(relation_detail_id,relation_id,")
          /*** Modify by fengdz TM:2012-03-19 ***/
          // .append("pri_ele_value,sec_ele_value,set_year,is_deleted,last_ver) values ")
          // .append("(?,?,?,?,?,?,?)");
          .append("pri_ele_value,sec_ele_value,set_year,is_deleted,last_ver,rg_code ) values ")
          .append("(?,?,?,?,?,?,?,?)");
        /*** Modify by fengdz TM:2012-03-19 ***/
        ps = conn.prepareStatement(strSQL.toString());
        Object[] key = detail.keySet().toArray();
        for (int i = 0; i < key.length; i++) {
          List keyData = detail.getRecordListByTag(key[i].toString());
          for (int j = 0; keyData != null && j < keyData.size(); j++) {
            String sec_ele_value = (String) ((XMLData) keyData.get(j)).get("sec_ele_value");
            ps.setString(1, UUIDRandom.generate());
            ps.setString(2, relation_id);
            ps.setString(3, key[i].toString());
            ps.setString(4, sec_ele_value);
            ps.setInt(5, Integer.parseInt(set_year));
            ps.setInt(6, 0);
            ps.setString(7, DateHandler.getLastVerTime());
            /*** Add by fengdz TM:2012-03-22 ***/
            ps.setString(8, rg_code);
            /*** Add by fengdz TM:2012-03-22 ***/
            ps.addBatch();
          }
        }
        ps.executeBatch();
        ps.close();
        ps = null;
        strSQL = null;
      }
    } catch (Exception e) {
      throw e;

    } finally {
      try {
        if (ps != null) {
          ps.close();
          ps = null;
        }
        if (session != null) {
          dao.closeSession(session);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return relation_id;
  }

  /**
   * 关联关系明细表数据修改。 /关联关系主表信息
   * 
   * @param relationData
   *            关联关系数据结构,结构为:relationData \row 明细表配置信息XMLData --> (主控要素编码 -
   *            被控要素列表)值对
   * @return boolean 修改是否成功
   * @throws Exception
   *             数据修改操作失败原因
   */
  public boolean modifyRelation(XMLData relationData) throws Exception {
    org.hibernate.Session session = null;
    Connection conn = null;
    PreparedStatement ps = null;
    PreparedStatement ps2 = null;// 为了批量处理
    StringBuffer strSQL = new StringBuffer();
    String user_id = "";
    String set_year = "";
    boolean ret = true;
    try {
      /*** Add by fengdz TM:2012-03-19 ***/
      String rg_code = getRgCode();
      /*** Add by fengdz TM:2012-03-19 ***/
      String pri_ele_code = (String) relationData.get("pri_ele_code");
      String sec_ele_code = (String) relationData.get("sec_ele_code");
      String relation_code = (String) relationData.get("relation_code");
      String relation_id = (String) relationData.get("relation_id");
      String relation_type = (String) relationData.get("relation_type");
      XMLData detail = (XMLData) relationData.get("row");
      if (relation_id == null) {
        throw new Exception("传入的修改信息中缺少唯一标示主键,无法进行修改!");
      }
      session = dao.getSession();
      conn = session.connection();
      user_id = (String) SessionUtil.getUserInfoContext().getAttribute("user_id");
      set_year = SessionUtil.getLoginYear();
      // 验证code是否重复
      strSQL.append("update sys_relation_manager").append(Tools.addDbLink())
        .append(" set relation_code=relation_code where relation_code=? and set_year=? ")
        /*** Modify by fengdz TM:2012-03-19 ***/
        // .append("and relation_id <> ?");
        .append("and relation_id <> ? and rg_code=? ");
      /*** Modify by fengdz TM:2012-03-19 ***/
      ps = conn.prepareStatement(strSQL.toString());
      ps.setString(1, relation_code);
      ps.setInt(2, Integer.parseInt(set_year));
      ps.setString(3, relation_id);
      /*** Add by fengdz TM:2012-03-19 ***/
      ps.setString(4, rg_code);
      /*** Add by fengdz TM:2012-03-19 ***/
      if (ps.executeUpdate() != 0) {
        throw new Exception("关联关系编码已存在!");
      }

      //
      ps.close();
      ps = null;
      strSQL.delete(0, strSQL.length());
      strSQL.append("update sys_relation_manager").append(Tools.addDbLink())
        .append(" set pri_ele_code=? where pri_ele_code=? and sec_ele_code=? and set_year=?")
        /*** Modify by fengdz TM:2012-03-19 ***/
        // .append("and relation_id <> ?");
        .append("and relation_id <> ? and rg_code=? ");
      /*** Modify by fengdz TM:2012-03-19 ***/
      ps = conn.prepareStatement(strSQL.toString());
      ps.setString(1, pri_ele_code);
      ps.setString(2, pri_ele_code);
      ps.setString(3, sec_ele_code);
      ps.setInt(4, Integer.parseInt(set_year));
      ps.setString(5, relation_id);
      /*** Add by fengdz TM:2012-03-19 ***/
      ps.setString(6, rg_code);
      /*** Add by fengdz TM:2012-03-19 ***/
      if (ps.executeUpdate() != 0) {
        throw new Exception("所选主控要素与被控要素之间的关联关系已存在!");
      }

      // 删除关联关系明细表
      ps.close();
      ps = null;
      strSQL.delete(0, strSQL.length());
      strSQL.append("delete from sys_relations").append(Tools.addDbLink()).append(" a where a.relation_id=?")
      /*** Modify by fengdz TM:2012-03-22 ***/
      // .append(" and a.set_year =? and pri_ele_value=? ");
        .append(" and a.set_year =? and rg_code=? ");
      /*** Modify by fengdz TM:2012-03-22 ***/
      ps = conn.prepareStatement(strSQL.toString());
      ps.setString(1, relation_id);
      ps.setInt(2, Integer.parseInt(set_year));
      //  ps.setString(3, key[i].toString());
      /*** Modify by fengdz TM:2012-03-22 ***/
      ps.setString(3, rg_code);
      ps.executeUpdate();
      strSQL.delete(0, strSQL.length());
      ps.close();
      ps = null;

      if (detail != null && detail.keySet().size() > 0) {
        strSQL.delete(0, strSQL.length());
        // 实现批处理模式
        strSQL.append("insert into sys_relations").append(Tools.addDbLink()).append("(relation_detail_id,relation_id,")
        /*** Modify by fengdz TM:2012-03-22 ***/
        .append("pri_ele_value,sec_ele_value,set_year,is_deleted,last_ver,rg_code ) values ")
          .append("(?,?,?,?,?,?,?,?)");
        /*** Modify by fengdz TM:2012-03-22 ***/
        ps2 = conn.prepareStatement(strSQL.toString());
        /*** Modify by fengdz TM:2012-03-22 ***/
        //   ps.addBatch();
        Object[] key = detail.keySet().toArray();
        for (int i = 0; i < key.length; i++) {

          List keyData = detail.getRecordListByTag(key[i].toString());
          for (int j = 0; keyData != null && j < keyData.size(); j++) {
            String sec_ele_value = (String) ((XMLData) keyData.get(j)).get("sec_ele_value");
            ps2.setString(1, UUIDRandom.generate());
            ps2.setString(2, relation_id);
            ps2.setString(3, key[i].toString());
            ps2.setString(4, sec_ele_value);
            ps2.setInt(5, Integer.parseInt(set_year));
            ps2.setInt(6, 0);
            ps2.setString(7, DateHandler.getLastVerTime());
            /*** Modify by fengdz TM:2012-03-22 ***/
            ps2.setString(8, rg_code);
            /*** Modify by fengdz TM:2012-03-22 ***/
            ps2.addBatch();
          }
        }

        ps2.executeBatch();
        ps2.close();
        ps2 = null;
        strSQL.delete(0, strSQL.length());
      }
      strSQL.append("update sys_relation_manager").append(Tools.addDbLink())
        .append(" set relation_code=?,pri_ele_code=?,sec_ele_code=?")
        .append(",latest_op_date=?,latest_op_user=?,last_ver=?,relation_type=?").append(" where relation_id='")
        .append(relation_id)
        /*** Modify by fengdz TM:2012-03-19 ***/
        // .append("' and set_year=").append(set_year);
        .append("' and set_year=").append(set_year).append(" and rg_code =").append(rg_code);
      /*** Modify by fengdz TM:2012-03-19 ***/
      ps = conn.prepareStatement(strSQL.toString());
      ps.setString(1, relation_code);
      ps.setString(2, pri_ele_code);
      ps.setString(3, sec_ele_code);
      ps.setString(4, DateHandler.getLastVerTime());
      ps.setString(5, user_id);
      ps.setString(6, DateHandler.getLastVerTime());
      ps.setString(7, relation_type);
      if (ps.executeUpdate() == 0) {
        throw new Exception("无法正常修改关联关系配置!");
      }
      ps.close();
      ps = null;
      strSQL = null;

    } catch (Exception e) {
      ret = false;
      throw e;
    } finally {
      try {
        if (ps != null) {
          ps.close();
          ps = null;
        }
        if (ps2 != null) {
          ps2.close();
          ps2 = null;
        }
        if (session != null) {
          dao.closeSession(session);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return ret;
  }

  /**
   * add by sungy 20080409 取得指定关联关系的某个主控要素的被控要素结果集
   * 
   * @param relation_code
   *            关联关系编码
   * @param priEleValue
   *            主控要素编码值
   * @param set_year
   *            业务年度
   * @return 被控要素结果集（XMLData对象列表集合）
   *         XMLData对象包含的键值：pri_ele_code、pri_ele_value、sec_ele_code
   *         、sec_ele_value
   * @throws Exception
   */
  public List getRelationByPriEleValue(String relation_code, String priEleValue, int set_year) throws Exception {
    /*** Add by fengdz TM:2012-03-19 ***/
    String rg_code = getRgCode();
    /*** Add by fengdz TM:2012-03-19 ***/
    /*
     * String mysql="select m.pri_ele_code,m.sec_ele_code,
     * r.pri_ele_value,r.sec_ele_value " + "from sys_relation_manager
     * m,sys_relations r "+ "where m.relation_id=m.relation_id " + "and
     * m.relation_code='"+relation_code+"' " + "and
     * r.pri_ele_value='"+priEleValue+"' " + "And
     * m.set_year='"+set_year+"'"; List list=this.queryBySql(mysql); return
     * list;
     */
    String yoursql = "select m.pri_ele_code,m.sec_ele_code, r.pri_ele_value,r.sec_ele_value "
      + "from sys_relation_manager m,sys_relations r " + "where m.relation_id=r.relation_id "
      + "and m.relation_code=? " + "and r.pri_ele_value=? "
      /*** Modify by fengdz TM:2012-03-19 ***/
      // + "And m.set_year=? ";
      + "And m.set_year=? and rg_code=? ";
    /*** Modify by fengdz TM:2012-03-19 ***/
    List relation = dao.findBySql(yoursql, new Object[] { relation_code, priEleValue, new Integer(set_year),
      new Integer(rg_code) });
    // priEleValue, new Integer(set_year) });
    /*** Modify by fengdz TM:2012-03-19 ***/
    return relation;

  }

  /**
   * 通过传入的关联关系id删除关联关系配置数据
   * 
   * @param relation_id
   *            关联关系id
   * @return 操作是否成功的结果
   * @throws Exception
   *             数据删除操作失败原因
   */
  public boolean deleteRelation(String relation_id) throws Exception {
    if (relation_id == null) {
      throw new Exception("未正确传入对应参数,无法删除对应关联关系配置!");
    }
    //    StringBuffer strSQL = new StringBuffer();
    //    strSQL.append("select * from sys_relations where relation_id='").append(relation_id).append("'");
    //    if(!dao.findBySql(strSQL.toString()).isEmpty()) {
    //    	    String set_year = "2016";
    //    	    String rg_code="110000";
    //    	    // rg_code = getRgCode();
    //    	  // 删除关联关系明细表
    //    	    strSQL.delete(0, strSQL.length());
    //    	  	strSQL.append("delete from sys_relations").append(Tools.addDbLink()).append(" where relation_id = '")
    //    	    .append(relation_id).append("' and set_year = ")
    //    	    // .append(set_year);
    //    	  .append(set_year).append(" and rg_code=").append(rg_code);
    //    	    dao.executeBySql(strSQL.toString());
    //    	    strSQL.delete(0, strSQL.length());
    //    	    strSQL.append("delete from sys_relation_manager").append(Tools.addDbLink()).append(" where relation_id = '")
    //    	    .append(relation_id).append("' and set_year=")
    //    	    // .append(set_year);
    //    	    .append(set_year).append(" and rg_code=").append(rg_code);
    //    	    dao.executeBySql(strSQL.toString());
    //    	    return true;
    //    } else {
    //    	return false;
    //    }
    org.hibernate.Session session = null;
    Connection conn = null;
    PreparedStatement ps = null;
    StringBuffer strSQL = new StringBuffer();
    String set_year = "2006";
    boolean operaState = true;
    try {
      session = dao.getSession();
      conn = session.connection();
      set_year = SessionUtil.getLoginYear();
      String rg_code = getRgCode();
      // 删除关联关系明细表
      strSQL.append("delete from sys_relations").append(Tools.addDbLink()).append(" where relation_id = '")
        .append(relation_id).append("' and set_year = ")
        // .append(set_year);
        .append(set_year).append(" and rg_code=").append(rg_code);
      ps = conn.prepareStatement(strSQL.toString());
      ps.executeUpdate();
      // 删除关联关系配置表
      ps.close();
      ps = null;
      strSQL.delete(0, strSQL.length());
      strSQL.append("delete from sys_relation_manager").append(Tools.addDbLink()).append(" where relation_id = '")
        .append(relation_id).append("' and set_year=")
        // .append(set_year);
        .append(set_year).append(" and rg_code=").append(rg_code);
      ps = conn.prepareStatement(strSQL.toString());
      if (ps.executeUpdate() == 0) {
        throw new Exception("无法正常删除关联关系配置!");
      }

    } catch (Exception e) {
      operaState = false;
      throw e;
    } finally {
      try {
        if (ps != null) {
          ps.close();
          ps = null;
        }
        if (session != null) {
          dao.closeSession(session);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return operaState;
  }

  /**
   * 获取关联关系的主键
   * 
   * @param pri_ele_code
   *            主控要素
   * @param sec_ele_code
   *            被控要素
   * @return 关联关系主键
   */
  public String getRelationID(String pri_ele_code, String sec_ele_code) {
    /*** Add by fengdz TM:2012-03-19 ***/
    String rg_code = getRgCode();
    /*** Add by fengdz TM:2012-03-19 ***/
    if ("".equals(pri_ele_code) || "".equals(sec_ele_code))
      return "";

    StringBuffer strSQL = new StringBuffer();
    String set_year = SessionUtil.getLoginYear();
    if ("".equals(set_year)) {
      set_year = String.valueOf(Calendar.getInstance().getTime().getYear());
    }
    strSQL.append("select t.relation_id from sys_relation_manager t").append(" where t.pri_ele_code = '")
      .append(pri_ele_code).append("' and t.sec_ele_code = '").append(sec_ele_code)
      .append("' and t.relation_type = 0 and t.is_deleted = 0").append(" and t.set_year = ").append(set_year)
      .append(" and t.rg_code=").append(rg_code);

    List result = dao.findBySql(strSQL.toString());
    if (result.isEmpty())
      return "";
    return ((Map) result.get(0)).get("relation_id") == null ? "" : ((Map) result.get(0)).get("relation_id").toString();

  }

  public String getCount(String sql) {
    String result = null;
    if (TypeOfDB.isOracle()) {
      StringBuffer countsql = new StringBuffer();
      countsql.append("select count(*) from (").append(sql).append(")");
      result = dao.findBySql(sql).size() + "";
    } else if (TypeOfDB.isMySQL()) {
      int temp = dao.findBySql(sql).size();
      result = String.valueOf(temp);
    }
    return result;
  }
}
