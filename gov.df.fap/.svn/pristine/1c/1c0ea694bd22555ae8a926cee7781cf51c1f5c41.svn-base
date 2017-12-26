package gov.df.fap.service.rule;

import gov.df.fap.api.rule.ISysBillNoRule;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.xml.XMLData;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 编号规则管理服务端接口实现类
 * 
 * @author a<BR>
 * 
 * @author liuzw 2012年3月23日修改实现多财政多年度 1.增加从sessionUtil中获得RG_CODE的方法
 *         2.在涉及表SYS_BILLNO,SYS_BILLNORULE,SYS_BILLNOBREAK,SYS_BILLNORULEELE,
 *         SYS_BILLNORULELINE相关的增删改查操作加入行政区划RG_CODE和业务年度SET_YEAR 的判断条件
 * 
 */
@Service
public class SysBillNoRuleBO implements ISysBillNoRule {

  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao = null;

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  private final String SYS_BILLNORULE_TABLE_NAME = "mappingfiles.sysdb.SysBillnorule";

  private final String SYS_BILLNORULELINE_TABLE_NAME = "mappingfiles.sysdb.SysBillnoruleline";

  private final String SYS_BILLNORULEELE_TABLE_NAME = "mappingfiles.sysdb.SysBillnoruleele";

  /**
   * 得到所有编号规则数据
   */
  public List findAllSysBillNoRules() throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    StringBuffer strSQL = new StringBuffer("select * from sys_billnorule where rg_code='").append(rg_code).append("'")
      .append(" and set_year =").append(setYear).append(Tools.addDbLink()).append("  order by billnorule_code");

    List list = null;
    try {
      list = dao.findBySql(strSQL.toString());
      strSQL = null;
    } catch (Exception e) {
      throw e;
    }
    return list;
  }

  /**
   * 根据编号规则ID得到相应的编号规则项数据
   */
  public List findSysBillNoRuleLinesByBillnoruleId(String billnoruleId) throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    StringBuffer strSQL = new StringBuffer("select * from sys_billnoruleline").append(Tools.addDbLink())
    /*** Modify by fengdz TM:2012-03-22 ***/
    .append(" where billnorule_id = ?  and rg_code=?  and set_year=? order by line_no");
    /*** Modify by fengdz TM:2012-03-22 ***/

    List list = null;
    try {
      list = dao.findBySql(strSQL.toString(), new Object[] { billnoruleId, rg_code, setYear });

      // 根据规则项ID查找关联关系数据，并将对应的关联关系数据并入规则项
      StringBuffer findElesSql = new StringBuffer("select * from sys_billnoruleele").append(Tools.addDbLink()).append(
        " where billnoruleline_id = ? and rg_code=? and set_year=?");
      for (int i = 0; list != null && i < list.size(); i++) {
        XMLData line = (XMLData) list.get(i);
        String lineId = (String) line.get("billnoruleline_id");
        List eleList = dao.findBySql(findElesSql.toString(), new Object[] { lineId, rg_code, setYear });
        if (eleList != null && eleList.size() > 0) {
          line.put("eles", eleList);
        }
      }
      findElesSql = null;
      strSQL = null;
    } catch (Exception e) {
      throw e;
    }

    return list;
  }

  /**
   * 根据编号规则项ID得到相应的编号规则项关联要素数据
   */
  public List findSysBillNoRuleElesByBillnorulelineId(String billnorulelineId) throws Exception {

    String rg_code = getRgCode();
    String setYear = getSetYear();

    StringBuffer strSQL = new StringBuffer("select * from sys_billnoruleele").append(Tools.addDbLink()).append(
      " where billnoruleline_id = ? and rg_code=?  and set_year=?");
    List list = null;
    try {
      // modify by liuzw 20120322
      list = dao.findBySql(strSQL.toString(), new Object[] { billnorulelineId, rg_code, setYear });
      strSQL = null;
    } catch (Exception e) {
      throw e;
    }
    return list;
  }

  /**
   * 查询有关构建树数据信息(查询单号规则)
   */
  public List findTreeBillNoInfo() throws Exception {
    StringBuffer strSQL = (TypeOfDB.isOracle() ? new StringBuffer(
      "select billnorule_id as ID,billnorule_code||'t' as Cid,sys_id as Sys_ID,billnorule_code||' '||billnorule_name as NAME from sys_billnorule order by billnorule_code")
      : new StringBuffer(
        "select billnorule_id as ID, concat(billnorule_code,'t') as Cid,sys_id as Sys_ID,concat(billnorule_code,' ',billnorule_name) as NAME from sys_billnorule order by billnorule_code"));
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString());
      strSQL = null;
    } catch (Exception e) {
      gov.df.fap.service.util.log.Log.error(e.getMessage()
        + org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
      throw e;
    }
    return list;
  }

  /**
   * 查询有关构建树数据信息(查询子系统)
   */
  public List findTreeSysAppInfo() throws Exception {
    StringBuffer strSQL = new StringBuffer("select sys_id as ID,sys_id as Cid,sys_id as Sys_ID,"
      + (TypeOfDB.isOracle() ? "sys_id||' '||sys_name" : " concat(sys_id,' ',sys_name) ") + " as NAME from sys_app"
      + Tools.addDbLink() + " where sys_id<>'000' order by sys_id ");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString());
      strSQL = null;
    } catch (Exception e) {
      gov.df.fap.service.util.log.Log.error(e.getMessage()
        + org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
      throw e;
    }
    return list;
  }

  /**
   * 根据编号规则ID得到相关联的交易凭证类型数据
   */
  public List findSysBilltypesByBillnoruleId(String billnoruleId) throws Exception {

    String rg_code = getRgCode();
    String setYear = getSetYear();
    StringBuffer strSQL = new StringBuffer("select * from sys_billtype").append(Tools.addDbLink()).append(
      " where billnorule_id = ? and rg_code=? and set_year=?");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString(), new Object[] { billnoruleId, rg_code, setYear });
      strSQL = null;
    } catch (Exception e) {
      throw e;
    }
    return list;
  }

  /**
   * 将form中数据取出放入XMLData中
   * 
   * @param form
   * @return value
   */
  private XMLData getXMLData(Object form) {
    XMLData value = new XMLData();
    Class<?> classForm = form.getClass();
    Field[] fields = classForm.getDeclaredFields(); // 得到SysEleRuleForm全部属性
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        PropertyDescriptor pd = new PropertyDescriptor(field.getName(), classForm);
        Method getMethod = pd.getReadMethod(); // 获得get方法
        Object obj = getMethod.invoke(form); // 执行get方法返回一个Object对象
        if (obj != null) {
          value.put(field.getName(), obj);
        }
      } catch (Exception e) {
        // logger.error("form表单转XMLData出错：", e);
        e.printStackTrace();
      }
    }
    return value;
  }

  /**
   * 保存编号规则数据
   */
  public void saveorUpdateSysBillNoRule(XMLData xmlData) throws Exception {
    String userId = (String) SessionUtil.getUserInfoContext().getUserID();
    String time = DateHandler.getLastVerTime();
    String setYear = getSetYear();
    String rg_code = getRgCode();
    try {
      xmlData.put("table", SYS_BILLNORULE_TABLE_NAME);
      xmlData.put("latest_op_user", userId);
      xmlData.put("latest_op_date", time);
      xmlData.put("set_year", setYear);
      xmlData.put("rg_code", rg_code);
      /***
       * 此处删除单号规则可不用加上行政区的条件，只需在查询的时候把当前行政区的单号规则过滤出来，那么删除的是肯定就是当前行政区划的
       * remark by fengdz
       **/
      dao.deleteDataBySql("sys_billnorule", xmlData, new String[] { "billnorule_id" });
      dao.saveDataBySql("sys_billnorule", xmlData);
      //
      String billnoruleId = (String) xmlData.get("billnorule_id");
      // 第二步：在修改规则数据时，将确定要删除的规则项sys_billnoruleline删除
      List lineIdsToBeDeleted = (List) xmlData.get("lineIdsToBeDeleted");
      if (lineIdsToBeDeleted != null && lineIdsToBeDeleted.size() > 0) {
        this.deleteSysBillNoRuleLinesToBeDeleted(billnoruleId, lineIdsToBeDeleted);
      }
      //
      List lines = (List) xmlData.get("billnorulelines");
      for (int i = 0; lines != null && i < lines.size(); i++) {
        // 第三步：保存或修改sys_billnoruleline
        XMLData line = (XMLData) getXMLData(lines.get(i));
        line.put("table", SYS_BILLNORULELINE_TABLE_NAME);
        line.remove("billnorule_id");
        line.put("billnorule_id", billnoruleId);
        line.put("latest_op_user", userId);
        line.put("latest_op_date", time);
        line.put("set_year", setYear);
        // modify by liuzw 20120331
        line.put("rg_code", rg_code);
        dao.deleteDataBySql("sys_billnoruleline", line, new String[] { "billnoruleline_id" });
        dao.saveDataBySql("sys_billnoruleline", line);
        //
        String lineId = (String) line.get("billnoruleline_id");
        // 第四步：根据lineId删除对应的关联关系sys_billnoruleele
        this.deleteSysBillNoRuleElesByBillnorulelineId(lineId);
        //
        List eles = (List) line.get("eles");
        for (int j = 0; eles != null && j < eles.size(); j++) {
          // 第五步：保存或修改sys_billnoruleele
          if (getXMLData(eles.get(j)) instanceof XMLData) {
            XMLData ele = (XMLData) getXMLData(eles.get(j));
            ele.put("table", SYS_BILLNORULEELE_TABLE_NAME);
            ele.remove("billnoruleline_id");
            ele.put("billnoruleline_id", lineId);
            ele.put("set_year", setYear);
            ele.put("rg_code", rg_code);
            dao.deleteDataBySql("sys_billnoruleele", ele, new String[] { "billnoruleline_id", "ele_code" });
            dao.saveDataBySql("sys_billnoruleele", ele);
          }
        }
      }

    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * 删除编号规则数据
   */
  public void deleteSysBillNoRule(XMLData xmlData) throws Exception {
    xmlData.put("table", SYS_BILLNORULE_TABLE_NAME);
    try {
      dao.deleteDataBySql("sys_billnorule", xmlData, new String[] { "billnorule_id" });
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * 级联删除相关联的编号规则数据
   * 
   * @param billnoruleId
   */
  public void deleteSysBillNoRuleByBillNoRuleId(String billnoruleId) throws Exception {
    StringBuffer delSysBillnoruleeleSql = new StringBuffer("delete from sys_billnoruleele").append(Tools.addDbLink())
      .append("  where billnoruleline_id in (select billnoruleline_id from sys_billnoruleline ")
      .append("where billnorule_id = '").append(billnoruleId).append("' )");
    StringBuffer delSysBillnorulelineSql = new StringBuffer("delete from sys_billnoruleline").append(Tools.addDbLink())
      .append(" where billnorule_id = '").append(billnoruleId).append("'");
    StringBuffer delSysBillnoruleSql = new StringBuffer("delete from sys_billnorule").append(Tools.addDbLink())
      .append(" where billnorule_id = '").append(billnoruleId).append("'");
    StringBuffer updateSysBilltypeSql = new StringBuffer("update sys_billtype").append(Tools.addDbLink())
      .append(" set billnorule_id = null where billnorule_id = '").append(billnoruleId).append("'");
    try {
      int count = 0;
      // 删除相关联的 sys_billnoruleele
      count = dao.executeBySql(delSysBillnoruleeleSql.toString());
      // 删除相关联的 sys_billnoruleline
      count = dao.executeBySql(delSysBillnorulelineSql.toString());
      // 删除相关联的 sys_billnorule
      count = dao.executeBySql(delSysBillnoruleSql.toString());
      // 修改相关联的sys_billtype
      count = dao.executeBySql(updateSysBilltypeSql.toString());
    } catch (Exception e) {
      throw e;
    }
    delSysBillnoruleeleSql = null;
    delSysBillnorulelineSql = null;
    delSysBillnoruleSql = null;
    updateSysBilltypeSql = null;
  }

  /**
   * 级联删除相关联的编号规则项数据
   * 
   * @param billnoruleId
   * @param lineIdsToBeDeleted
   */
  private void deleteSysBillNoRuleLinesToBeDeleted(String billnoruleId, List lineIdsToBeDeleted) throws Exception {
    StringBuffer delSysBillnoruleeleSql = new StringBuffer("delete from sys_billnoruleele").append(Tools.addDbLink())
      .append(" where billnoruleline_id = ? ");
    StringBuffer delSysBillnorulelineSql = new StringBuffer("delete from sys_billnoruleline").append(Tools.addDbLink())
      .append(" where billnorule_id = ? and billnoruleline_id = ? ");
    try {
      for (int i = 0; lineIdsToBeDeleted != null && i < lineIdsToBeDeleted.size(); i++) {
        // 删除相关联的 sys_billnoruleele
        dao.executeBySql(delSysBillnoruleeleSql.toString(), new Object[] { lineIdsToBeDeleted.get(i) });
        // 删除sys_billnoruleline
        dao.executeBySql(delSysBillnorulelineSql.toString(), new Object[] { billnoruleId, lineIdsToBeDeleted.get(i) });
      }
      delSysBillnoruleeleSql = null;
      delSysBillnorulelineSql = null;
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * 根据规则项ID删除相关联的关联关系数据
   * 
   * @param billnorulelineId
   */
  private void deleteSysBillNoRuleElesByBillnorulelineId(String billnorulelineId) throws Exception {
    StringBuffer delSysBillnoruleeleSql = new StringBuffer("delete from sys_billnoruleele").append(Tools.addDbLink())
      .append(" where billnoruleline_id = '").append(billnorulelineId).append("' ");
    try {
      dao.executeBySql(delSysBillnoruleeleSql.toString());
      delSysBillnoruleeleSql = null;
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * 得到系统业务年度
   * 
   * @return
   */
  public String getSetYear() {
    String set_year = (String) SessionUtil.getLoginYear();
    if (set_year == null || set_year.equalsIgnoreCase("")) {
      set_year = String.valueOf(DateHandler.getCurrentYear());
    }
    return set_year;
  }

  public String getRgCode() {

    String rg_code = (String) SessionUtil.getRgCode();
    return rg_code;
  }

  /**
   * 根据sysId查询记录
   * 
   * @param sysId
   *            子系统id
   * @author 2008-2-4 tyy
   * @return
   */
  public List getSysBillNoRuleBySysId(String sysId) {
    List list = null;
    String rg_code = getRgCode();
    String setYear = getSetYear();

    String sql = "select billnorule_id,billnorule_id as chr_id,billnorule_code as chr_code,billnorule_name as chr_name,"
      + "'billnoRuleRoot' as chr_parent_id from sys_billnorule where sys_id=? and rg_code=? and set_year=?";

    try {
      list = dao.findBySql(sql, new Object[] { sysId, rg_code, setYear });
    } catch (Exception e) {
    }
    return list;
  }

  /**
   * 根据sysId查询记录
   * 
   * @param sysId
   *            子系统id
   * @author 2008-2-3 tyy
   * @return
   */
  public List getSysBillNoruleBybillnoruleId(String billnoruleId) {
    List list = null;
    String rg_code = getRgCode();
    String setYear = getSetYear();
    String sql = "select * from sys_billnorule where billnorule_id=? and　rg_code=? and set_year=?";

    try {
      list = dao.findBySql(sql, new Object[] { billnoruleId, rg_code, setYear });
    } catch (Exception e) {
    }
    return list;
  }

  /**
   * 根据billnorule_id查询单号规则项
   * 
   * @param sysId
   *            子系统值
   * @author tyy
   * @return List
   */
  public List getSysBillNoruleBybillnorulelineId(String billnorule_id) {

    String rg_code = getRgCode();
    String setYear = getSetYear();

    List list = null;
    String sql = "select billnorule_id ,billnoruleline_id ,line_no ,line_type ,line_format ,init_value ,ele_code ,"
      + "level_num ,set_year from sys_billnoruleline where billnorule_id=? and rg_code=? and set_year=?";
    try {
      list = dao.findBySql(sql, new Object[] { billnorule_id, rg_code, setYear });
    } catch (Exception e) {

    }
    return list;
  }

  /**
   * 根据billNorulelineId查询单号规则项关联要素
   * 
   * @param billNorulelineId
   * @author tyy
   * @return List
   */
  public List findSysbillnoruleeleId(String billnorulelineId) {

    String rg_code = getRgCode();
    String setYear = getSetYear();

    List list = null;
    String sql = "select * from sys_billnoruleele where billnoruleline_id=?  and rg_code=? and set_year=?";
    try {
      list = dao.findBySql(sql, new Object[] { billnorulelineId, rg_code, setYear });
    } catch (Exception e) {
    }
    return list;
  }

  public XMLData findSysBillNoRuleById(String bnr_id) throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    StringBuffer strSQL = new StringBuffer("select * from sys_billnorule").append(Tools.addDbLink()).append(
      " where billnorule_id = ? and  rg_code=? and set_year=?");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString(), new Object[] { bnr_id, rg_code, setYear });
      strSQL = null;
    } catch (Exception e) {
      throw e;
    }
    return list.size() == 0 ? null : (XMLData) list.get(0);
  }

  public boolean isBillNoRuleCodeExist(String billnoruleCode) throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    StringBuffer strSQL = new StringBuffer("select * from sys_billnorule").append(Tools.addDbLink()).append(
      " where billnorule_code = ? and rg_code=? and set_year=?");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString(), new Object[] { billnoruleCode, rg_code, setYear });
      strSQL = null;
    } catch (Exception e) {
      throw e;
    }
    return !list.isEmpty();
  }

  public List getBillType(String billNoRuleId) throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    StringBuffer sql = new StringBuffer("select billtype_code from sys_billtype")
      .append(" where billnorule_id =? and rg_code=? and set_year=?");
    List list = dao.findBySql(sql.toString(), new Object[] { billNoRuleId, rg_code, setYear });
    return list;
  }
}
