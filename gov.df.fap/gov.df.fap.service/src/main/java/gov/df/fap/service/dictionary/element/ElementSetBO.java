/*
 * $Id: ElementSetBO.java 361095 2013-01-06 02:15:31Z zhaoyy1 $
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.service.dictionary.element;

import gov.df.fap.api.dictionary.IElementSet;
import gov.df.fap.api.dictionary.interfaces.IDictionary;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 要素配置服务端接口实现类
 * 
 * @author zhadaopeng
 * 
 */
@Service
public class ElementSetBO implements IElementSet {

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao;

  @Autowired
  private IDictionary dic;

  public IDictionary getDic() {
    return dic;
  }

  public void setDic(IDictionary dic) {
    this.dic = dic;
  }

  /**
   * 得到要素数据
   * 
   * @return List
   */
  public List getElementByCondition(String condition) {
    StringBuffer strSQL = new StringBuffer("select * from sys_element").append(Tools.addDbLink()).append(" where 1=1 ")
      .append(condition);
    List return_list = dao.findBySql(strSQL.toString());
    return return_list;
  }

  /**
   * 保存要素配置数据
   * 
   * @param xmlData
   * @throws Exception
   */
  public void saveorUpdateElement(XMLData xmlData) throws Exception {
    if (xmlData == null) {
      throw new Exception("无效要素设置数据,无法更新!");
    }
    String chr_id = (String) xmlData.get("chr_id");
    // 编码查重
    StringBuffer strSQL = new StringBuffer();
    strSQL.append("select 1 from sys_element").append(Tools.addDbLink()).append(" where ele_code = '")
      .append(xmlData.get("ele_code"))
      /*** Modify by fengdz TM:2012-03-19 ***/
      // .append("' and chr_id <> '").append(chr_id).append("'");
      .append("' and chr_id <> '").append(chr_id).append("' and set_year=").append(getSetYear())
      .append(" and rg_code='").append(getRgCode()).append("'");
    /*** Modify by fengdz TM:2012-03-19 ***/
    if (dao.findBySql(strSQL.toString()).size() > 0) {
      throw new Exception("要素编码为" + xmlData.get("ele_code") + "的要素已经存在!");
    }
    xmlData.put("rg_code", getRgCode());
    xmlData.put("set_year", getSetYear());
    xmlData.put("latest_op_date", DateHandler.getToday());
    xmlData.put("latest_op_user", CommonUtil.getUserId());

    xmlData.put("is_deleted", "0");
    dao.deleteDataBySql("sys_element", xmlData, new String[] { "chr_id" });
    dao.saveDataBySql("sys_element", xmlData);

  }

  /**
   * 保存要素配置数据 
   * 2017-03-23 yanyga 
   * @param xmlData
   * @throws Exception
   */
  public void saveRightSet(Map<String, Object> map) throws Exception {
    if (map == null) {
      throw new Exception("无效要素设置数据,无法更新!");
    }
    String[] left = (String[]) (map.get("left_list"));
    String[] right = (String[]) (map.get("right_list"));

    String rightInSql = returnWhereSql(right);
    String leftInSql = returnWhereSql(left);

    /*String leftUpdateSql = "update sys_element" + Tools.addDbLink() + " set is_rightfilter=0 where " + leftInSql
      + " and rg_code='" + "000000" + "' and set_year=" + "2017";
    dao.executeBySql(leftUpdateSql);

    String rightUpdateSql = "update sys_element" + Tools.addDbLink() + " set is_rightfilter=1 where " + rightInSql
      + " and rg_code='" + "000000" + "' and set_year=" + "2017";
    dao.executeBySql(rightUpdateSql);*/
    String leftUpdateSql = "update sys_element" + Tools.addDbLink() + " set is_rightfilter=0 where " + leftInSql
      + " and rg_code='" + getRgCode() + "' and set_year=" + getSetYear();
    dao.executeBySql(leftUpdateSql);

    String rightUpdateSql = "update sys_element" + Tools.addDbLink() + " set is_rightfilter=1 where " + rightInSql
      + " and rg_code='" + getRgCode() + "' and set_year=" + getSetYear();
    dao.executeBySql(rightUpdateSql);

  }

  public String returnWhereSql(String[] right) {
    String rightInSql = "1=1";
    rightInSql += " and chr_id in ('','";
    for (int i = 0; i < right.length; i++) {
      rightInSql = rightInSql + right[i] + "','";
    }
    rightInSql = rightInSql + "')";
    return rightInSql;
  }

  /*** Add by fengdz TM:2012-03-19 ***/
  public String getSetYear() {
    return (String) SessionUtil.getUserInfoContext().getContext().get("set_year");

  }

  public String getRgCode() {
    return (String) SessionUtil.getUserInfoContext().getContext().get("rg_code");

  }

  /*** Add by fengdz TM:2012-03-19 ***/
  /**
   * 保存要素配置数据
   * 
   * @param xmlData
   * @throws Exception
   */
  public void updateElementSet(XMLData xmlData) throws Exception {
    String set_type = xmlData.getSubObject("set_type").toString();
    List right_list = (List) xmlData.getSubObject("right_list");
    List left_list = (List) xmlData.getSubObject("left_list");
    if (set_type.equals("VOU")) {
      for (int i = 0; i < right_list.size(); i++) {
        dao.executeBySql("update sys_element" + Tools.addDbLink() + " set ele_type=1 where chr_id='"
          + ((XMLData) right_list.get(i)).get("chr_id")
          /*** Modify by fengdz TM:2012-03-19 ***/
          // .toString() + "'");
            .toString() + "' and rg_code='" + getRgCode() + "' and set_year=" + getSetYear());
        /*** Modify by fengdz TM:2012-03-19 ***/
      }
      for (int i = 0; i < left_list.size(); i++) {
        dao.executeBySql("update sys_element" + Tools.addDbLink() + " set ele_type=2 where chr_id='"
          + ((XMLData) left_list.get(i)).get("chr_id").toString()
          /*** Modify by fengdz TM:2012-03-19 ***/
          + "'" + " and rg_code='" + getRgCode() + "' and set_year=" + getSetYear());
        /*** Modify by fengdz TM:2012-03-19 ***/
      }
    }
    if (set_type.equals("RIGHT")) {
      for (int i = 0; i < right_list.size(); i++) {
        dao.executeBySql("update sys_element" + Tools.addDbLink() + " set is_rightfilter=1 where chr_id='"
          + ((XMLData) right_list.get(i)).get("chr_id")
          /*** Modify by fengdz TM:2012-03-19 ***/
          // .toString() + "'");
            .toString() + "' and rg_code='" + getRgCode() + "' and set_year=" + getSetYear());
        /*** Modify by fengdz TM:2012-03-19 ***/
      }
      for (int i = 0; i < left_list.size(); i++) {
        dao.executeBySql("update sys_element" + Tools.addDbLink() + " set is_rightfilter=0 where chr_id='"
          + ((XMLData) left_list.get(i)).get("chr_id").toString()
          /*** Modify by fengdz TM:2012-03-19 ***/
          + "'" + " and rg_code='" + getRgCode() + "' and set_year=" + getSetYear());
        /*** Modify by fengdz TM:2012-03-19 ***/
      }
    }
  }

  /**
   * 保存要素配置数据
   * 
   * @param xmlData
   * @throws Exception
   */
  public void updateElementCodeRule(XMLData xmlData) throws Exception {
    // 由于界面上已经控制了max_leve和code_rule非空,所以不需要再进行判断.
    StringBuffer strSQL = new StringBuffer("update sys_element").append(Tools.addDbLink()).append(" set max_level=")
      .append(xmlData.get("max_level")).append(",code_rule='").append(xmlData.get("code_rule")).append("' ");

    strSQL.append("where chr_id = '").append(xmlData.get("chr_id"))
    /*** Modify by fengdz TM:2012-03-19 ***/
    // .append("'");
      .append("' and rg_code='").append(getRgCode()).append("' and set_year=").append(getSetYear());
    /*** Modify by fengdz TM:2012-03-19 ***/
    dao.executeBySql(strSQL.toString());

    //IDictionary dic = (IDictionary) SessionUtil.getServerBean("sys.dictionaryService");
    if (dic != null) {
      dic.removeElementCacheByEleCode(((String) xmlData.get("ele_code")).toLowerCase());
    }
  }

  /**
   * 保存要素配置数据
   * 
   * @param xmlData
   * @throws Exception
   */
  public boolean checkRight() throws Exception {
        StringBuffer strSQL = new StringBuffer("select * from sys_right_combination")
        /*** Modify by fengdz TM:2012-03-19 ***/
        // .append(Tools.addDbLink());
        .append(Tools.addDbLink()).append("  where  rg_code='").append(getRgCode()).append("' and set_year=")
        .append(getSetYear());

    List return_list = dao.findBySql(strSQL.toString());
    if (return_list.size() > 0) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * 删除要素配置数据
   * 
   * @param wf_id
   * @throws Exception
   */
  public void deleteElement(String chr_id) throws Exception {
    StringBuffer delete_sql = new StringBuffer("delete from  sys_element").append(Tools.addDbLink())
      .append(" b where b.chr_id = '")

      /*** Modify by fengdz TM:2012-03-19 ***/
      // .append(chr_id).append("'");
      .append(chr_id).append("'").append("  and rg_code='").append(getRgCode()).append("' and set_year=")
      .append(getSetYear());
    /*** Modify by fengdz TM:2012-03-19 ***/
    dao.executeBySql(delete_sql.toString());
  }

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  public List getElementListUIs() {
    StringBuffer sb = new StringBuffer();
    sb.append("select t.ui_code chr_id, t.ui_code chr_code, t.ui_name chr_name, null parent_id ")
      .append(" from sys_uimanager t ")
      // .append(" where t.ui_type = '002' and t.ui_code like '001004_%'");
      .append(
        " where t.ui_type = '002' and t.ui_code like '001004_%' and t.rg_code='" + this.getRgCode()
          + "' and t.set_year='" + this.getSetYear() + "'");// add by wl 20120401

    return dao.findBySql(sb.toString());
  }

  public List getElementInputUIs() {
    StringBuffer sb = new StringBuffer();
    sb.append("select t.ui_code chr_id, t.ui_code chr_code, t.ui_name chr_name, null parent_id ")
      .append(" from sys_uimanager t ")
      // .append(" where t.ui_type = '001' and t.ui_code like '001004_%'");
      .append(
        " where t.ui_type = '001' and t.ui_code like '001004_%' and t.rg_code='" + this.getRgCode()
          + "' and t.set_year='" + this.getSetYear() + "'");// add by wl 20120401

    return dao.findBySql(sb.toString());
  }

  public void freshViewColById(String ele_code, String new_name, String ui_detail_id) {
    StringBuffer sb = new StringBuffer();
    // sb.append("update sys_uidetail d set d.title='")
    // .append(new_name)
    // .append("' where d.ui_id='")
    // .append(ui_id)
    // .append("' and lower(substr(id,1,instr(id,'_')-1))='").append(ele_code.toLowerCase()).append("'");
    sb.append("update sys_uidetail d set d.title='").append(new_name).append("' where d.ui_detail_id='")
      .append(ui_detail_id).append("' and d.rg_code=").append(getRgCode()).append(" and d.set_year=")
      .append(getSetYear());
    dao.executeBySql(sb.toString());
  }

  public void freshView() throws Exception {
    try {
      dao.executeBySql("{call pf_build_vw_rightgroup}");
      // 保存操作日志
      //      LogDTO logDto = new LogDTO();
      //      logDto.setLog_type("1");
      //      logDto.setLog_level(new Integer(0));
      //      logDto.setRemark(SessionUtil.getOnlineUser().getUser_name() + "更新了要素权限视图");
      //      FLog.writeLog(logDto);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
}