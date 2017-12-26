/*
 * $Id: UIConfigureImplBO.java 393255 2013-05-01 00:28:44Z liwq2 $
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.service.systemmanager.uiconfigure;

import gov.df.fap.api.redis.CacheUtil;
import gov.df.fap.api.systemmanager.uiconfigure.IUIConfigure;
import gov.df.fap.bean.view.SysUidetail;
import gov.df.fap.bean.view.SysUimanager;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.UtilCache;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.SQLUtil;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.xml.XMLData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 界面配置服务端接口实现类
 * 
 */
@Service
public class UIConfigureImplBO implements IUIConfigure {

  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao;

  @Autowired
  @Qualifier("df.cacheUtil")
  private CacheUtil cacheUtil;

  /**
   * 得到所有界面配置视图详细数据
   * 
   * @param viewId
   *            视图ID
   * @return List
   */
  public List getViewDetails(String viewId) {
    // TODO Auto-generated method stub
    List return_list = dao.findBySql("select a.*,a.field_name ||' '|| a.title detailname from sys_uidetail"
      + Tools.addDbLink() + " a where a.ui_id='" + viewId + "' order by a.field_index");

    return return_list;
  }

  /**
   * 得到所有界面配置视图详细数据配节，包含简称+名称
   * 
   * @param viewId
   *            视图ID,
   * @return List
   */
  public List getViewDetailsWithEleName(String viewId) {
    // TODO Auto-generated method stub
    List return_list = dao.findBySql("select  p.*," + SQLUtil.replaceLinkChar("p.field_name ||' '|| p.title") + " as detailname,"
      + SQLUtil.replaceLinkChar(" o.ele_code || ' ' || o.ele_name") + " as source_name from sys_uidetail p "
      + " left join sys_element o on o.ele_code=p.source" + " where p.ui_id='" + viewId + "' order by p.field_index");

    return return_list;
  }

  /************************************* V6.2 ********************************************/

  /**
   * 得到所有界面配置视图数据
   * 
   * @param sql
   *            String sql
   * @return List 界面配置信息信息（具体内容参考表sys_uimanager）
   * 
   * @author 朱建
   */
  public List getUIManagers(String sql) {
    // add by wl 20110327 sys_uimanager分区划 查询时加入区划信息
    String rg_code = SessionUtil.getRgCode();
    StringBuffer sb = new StringBuffer();
    sb.append("select * from sys_uimanager ");
    sb.append(" where rg_code= ? and set_year=?");
    sb.append(sql);
    sb.append(" order by ui_code");
    List return_list = dao.findBySql(sb.toString(), new Object[] { rg_code, SessionUtil.getLoginYear() });
    return return_list;
  }

  /**
   * 根据任务ui_code得到所有界面配置视图数据
   * 
   * @param ui_code
   *            int ui_code
   * @return XMLData 界面配置信息信息（具体内容参考表sys_uimanager）
   * @throws Exception
   *             向前抛出sql错误
   * @author 朱建
   */
  public XMLData getAllByUiCode(String ui_code) throws SQLException {
    XMLData task = null;
    // 取得所有的自动任务定义列表
    // add by wl 20110327 sys_uimanager分区划 查询时加入区划信息
    String rg_code = SessionUtil.getRgCode();
    String sql = "select * from sys_uimanager sa where sa.ui_code=? and sa.rg_code=? and sa.set_year=? ";
    List task_List = dao.findBySql(sql, new Object[] { ui_code, rg_code, SessionUtil.getLoginYear() });
    if (task_List.size() > 0) {
      task = (XMLData) task_List.get(0);
    }
    return task;
  }

  /**
   * 保存界面配置视图数据
   * 
   * @param xmlData
   *            XMLData xmlData
   * @throws Exception
   */
  public void saveorUpdateUIConfigure(XMLData xmlData) throws Exception {
    XMLData ui_manager = (XMLData) xmlData.getSubObject("ui_manager");
    List ui_detail_list = (List) xmlData.getSubObject("ui_detail_list");
    List ui_detail_conf_list = (List) xmlData.getSubObject("ui_detail_conf_list");

    // add by wl 20110327获取区划编码
    String rg_code = SessionUtil.getRgCode();
    String set_year = SessionUtil.getLoginYear();

    if (UtilCache.uiMap != null) {
      UtilCache.uiMap.clear();
    }

    if (UtilCache.billUiMap != null) {
      UtilCache.billUiMap.clear();
    }
    if (!ui_manager.isEmpty()) {

      dao.executeBySql("delete from sys_uiconf_detail where uiconf_id='" + ui_manager.get("ui_id").toString() + "'");
      dao
        .executeBySql("delete from sys_uiconf_detail where uiconf_id in (select ui_detail_id from sys_uidetail where ui_id='"
          + ui_manager.get("ui_id").toString() + "')");

      // 删除配置的多个默认值信息 add by wanghongtao
      dao
        .executeBySql("delete from sys_uidetail_multivalue where ui_detail_id in (select ui_detail_id from sys_uidetail where ui_id='"
          + ui_manager.get("ui_id").toString() + "')");

      dao.executeBySql("delete from sys_uimanager" + Tools.addDbLink() + " where ui_id ='"
        + ui_manager.get("ui_id").toString() + "'");
      dao.executeBySql("delete from sys_uidetail" + Tools.addDbLink() + " where ui_id ='"
        + ui_manager.get("ui_id").toString() + "'");
      // ui_manager.put("table_name", "mappingfiles.sysdb.SysUimanager");
      // dao.saveorUpdate(ui_manager);
      // add by wl 20120410 去掉年份 增加rg_code
      ui_manager.put("rg_code", rg_code);
      ui_manager.put("set_year", set_year);
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date date = new Date();
      ui_manager.put("last_ver", dateFormat.format(date));
      dao.saveDataBySql("sys_uimanager", ui_manager);
    }

    Iterator iterator_ui_detail_list = ui_detail_list.iterator();
    while (iterator_ui_detail_list.hasNext()) {
      XMLData rowData = (XMLData) iterator_ui_detail_list.next();
      if (!rowData.isEmpty()) {
        // 通过判断value的类型是否是数组判断是否设置了多个默认值
        String[] values = null;
        if (rowData.get("value") != null && (rowData.get("value") instanceof String[])) {
          values = (String[]) rowData.get("value");
          // 如果默认值多于1个，则把标识"MULTI_DEFAULT_VALUE"赋值给rowData的value
          if (values.length > 1) {
            rowData.put("value", "MULTI_DEFAULT_VALUE");
          } else {
            // 如果默认值只有一个，则直接把value[0]赋值给rowData的value
            rowData.put("value", values[0]);
          }
        }
        // 执行保存
        // rowData.put("table_name", "mappingfiles.sysdb.SysUidetail");
        // dao.saveorUpdate(rowData);
        if (rowData.get("is_nessary") == null) {
          rowData.put("is_nessary", "0");
        }
        if (rowData.get("is_virtual_column") == null) {
          rowData.put("is_virtual_column", "false");
        }

        // add by wl 20110327 sys_uidetail 分区划 保存时加入区划信息 去掉年份
        rowData.put("rg_code", rg_code);
        rowData.put("set_year", set_year);
        // rowData.put("set_year", SessionUtil.getLoginYear());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        rowData.put("last_ver", dateFormat.format(date));
        dao.saveDataBySql("sys_uidetail", rowData);
        // add by wanghongtao 保存多默认值
        // 如果rowData.get("value")的值等于MULTI_DEFAULT_VALUE，说明设置了多个默认值，要把所有值保存到sys_uidetail_multivalue表中
        if ("MULTI_DEFAULT_VALUE".equalsIgnoreCase((String) rowData.get("value"))) {
          // 获取Session
          Session session = dao.getSession();
          // 获取连接
          Connection conn = session.connection();
          try {
            // 插入默认值的sql
            StringBuffer insertSql = new StringBuffer();
            insertSql.append("insert into sys_uidetail_multivalue (ui_detail_id,value) ").append("values (?, ?)");
            PreparedStatement insertPsmt = conn.prepareStatement(insertSql.toString());
            // 明细列的id
            String ui_detail_id = (String) rowData.get("ui_detail_id");
            // 根据默认值个数，通过循环添加批处理sql语句
            for (int i = 0; i < values.length; i++) {
              insertPsmt.setString(1, ui_detail_id);
              insertPsmt.setString(2, values[i]);
              insertPsmt.addBatch();
            }
            // 执行批处理
            insertPsmt.executeBatch();
          } catch (Exception e) {
            // 如果执行异常，抛出异常
            throw e;
          } finally {
            // 关闭session
            if (session != null) {
              dao.closeSession(session);
            }
          }
        }
      }
    }

    Iterator iterator_ui_detail_conf_list = ui_detail_conf_list.iterator();
    while (iterator_ui_detail_conf_list.hasNext()) {
      XMLData rowData = (XMLData) iterator_ui_detail_conf_list.next();
      if (!rowData.isEmpty()) {
        rowData.remove("uiconf_field_name");
        // rowData.put("table_name",
        // "mappingfiles.sysdb.SysUiconfDetail");
        // dao.saveorUpdate(rowData);
        dao.saveDataBySql("sys_uiconf_detail", rowData);
      }
    }
    // 保存操作日志
    /************* qiaohd******start *******/
    /*
     * LogDTO logDto = new LogDTO(); logDto.setLog_type("1");
     * logDto.setRemark(SessionUtil.getOnlineUser().getUser_name() +
     * "新增或修改了界面视图：ui_code=" + ui_manager.get("ui_code"));
     * FLog.writeLog(logDto);
     */
    /************************* end *******/
  }

  /**
   * 删除界面配置视图数据
   * 
   * @param wf_id
   *            String wf_id
   * @throws Exception
   */
  public void deleteUIConfigure(String ui_id) throws Exception {
    String delete_sql = "";
    // 删除操作对应该视图的信息
    delete_sql = "delete from sys_action_view where ui_id='" + ui_id + "'";
    dao.executeBySql(delete_sql);

    // 删除功能对应该视图的信息
    delete_sql = "delete from sys_module_view where ui_id='" + ui_id + "'";
    dao.executeBySql(delete_sql);

    // 删除配置的多个默认值
    delete_sql = "delete from sys_uidetail_multivalue where ui_detail_id in (select ui_detail_id from sys_uidetail where ui_id='"
      + ui_id + "')";
    dao.executeBySql(delete_sql);

    // 删除视图对应列信息
    delete_sql = "delete from  sys_uidetail" + Tools.addDbLink() + " b where b.ui_id = '" + ui_id + "'";
    dao.executeBySql(delete_sql);
    // 删除界面视图主表信息
    delete_sql = "delete from  sys_uimanager" + Tools.addDbLink() + " b where b.ui_id = '" + ui_id + "'";
    dao.executeBySql(delete_sql);

    // 保存操作日志
    /************* qiaohd******start *******/
    /*
     * LogDTO logDto = new LogDTO(); logDto.setLog_type("1");
     * logDto.setRemark(SessionUtil.getOnlineUser().getUser_name() +
     * "删除了界面视图：ui_id" + ui_id); FLog.writeLog(logDto);
     */
    /************* qiaohd******end *******/
  }

  /**
   * 得到所有界面配置视图详细数据
   * 
   * @param ui_id
   *            int ui_id
   * @return List
   */
  public List getUIDetails(String ui_id) {
    List return_list = dao.findBySql("select a.* from sys_uidetail" + Tools.addDbLink() + " a where a.ui_id='" + ui_id
      + "' order by a.field_index");

    // add by wanghongtao
    // 判断是否配置了多个默认值，如果return_list中的明细列的value值为"MULTI_DEFAULT_VALUE"，
    // 说明配置了多个默认值，需要查询sys_uidetail_multivalue表获取所有默认值
    for (int i = 0; return_list != null && i < return_list.size(); i++) {
      XMLData detailData = (XMLData) return_list.get(i);
      if (detailData.get("value") != null && "MULTI_DEFAULT_VALUE".equalsIgnoreCase(detailData.get("value").toString())) {
        detailData.put("value", getMultiValueAsArray((String) detailData.get("ui_detail_id")));
      }
    }
    return return_list;
  }

  /**
   * 根据流程得到所有业务表数据
   * 
   * @param table_name
   *            String table_name
   * @param ui_id
   *            String ui_id
   * @return List
   */
  public List getUIDetailsByTable(String table_name, String ui_id) {
    String sql = "  select * from (select newid()" + Tools.addDbLink() + " as ui_detail_id,'" + ui_id + "' as ui_id,"
      + " t.COLUMN_NAME as field_name,null as disp_mode,0 as is_nessary,0 as is_enabled,null as set_year,"
      + " null as last_ver,999 as field_index,null as id, a.field_name as title,null as cols,null as visible,"
      + " null as editable,null as value,null as ref_model,null as source,null as width" + "  from user_tab_columns"
      + Tools.addDbLink() + " t, sys_metadata" + Tools.addDbLink() + " a" + " where t.TABLE_NAME = '" + table_name
      + "'" + "   and t.COLUMN_NAME(+) = a.field_code" + "   and not exists (select 1 from  sys_uidetail"
      + Tools.addDbLink() + " a where " + "   a.ui_id='" + ui_id + "'" + "   and a.field_code=t.COLUMN_NAME) union    "
      + "   select a.* from sys_uidetail" + Tools.addDbLink() + " a where " + "   a.ui_id='" + ui_id
      + "' )  order by field_index";
    List return_list = dao.findBySql(sql);
    return return_list;
  }

  /**
   * 得到所有界面配置视图高级属性数据
   * 
   * @param ui_id
   *            String ui_id
   * @return List
   */
  public List getUIDetailConf(String ui_id) {
    List return_list = dao.findBySql("select a.*, b.uiconf_field_name, b.uiconf_field_type from sys_uiconf_detail"
      + Tools.addDbLink() + " a, sys_uiconf" + Tools.addDbLink() + " b,sys_uidetail" + Tools.addDbLink() + " c"
      + " where a.uiconf_field = b.uiconf_field and a.uiconf_id = c.ui_detail_id and c.disp_mode = b.uiconf_type"
      + " and c.ui_id = ? union" + " select a.*, b.uiconf_field_name, b.uiconf_field_type" + " from sys_uiconf_detail"
      + Tools.addDbLink() + " a, sys_uiconf" + Tools.addDbLink() + " b, sys_uimanager" + Tools.addDbLink() + " c"
      + " where a.uiconf_field = b.uiconf_field" + " and a.uiconf_id = c.ui_id" + " and c.ui_type = b.uiconf_type"
      + " and c.ui_id = ?", new Object[] { ui_id, ui_id });
    return return_list;
  }

  /**
   * 根据流程得到所有业务表数据
   * 
   * @return List
   */
  public List getTables() {
    // String sql =
    // "select ta.table_code,ta.table_name,ta.id_column_name from Sys_tablemanager"
    // + Tools.addDbLink()
    // + "  ta "
    // + "where  ta.table_type='201' and ta.set_year="
    // + SessionUtil.getUserInfoContext().getSetYear();
    String sql = "select ta.table_code,ta.table_name,ta.id_column_name from Sys_tablemanager" + Tools.addDbLink()
      + "  ta " + "where  ta.table_type='201'";
    List list = null;
    try {
      list = dao.findBySql(sql);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return list;
  }

  /**
   * 根据流程得到所有业务表数据
   * 
   * @param table_name
   *            String table_name
   * @return List
   */
  public List getTableColumns(String table_name) {
    String sql = "select t.COLUMN_NAME,a.field_name from user_tab_columns" + Tools.addDbLink() + " t,sys_metadata"
      + Tools.addDbLink() + " a " + " where t.TABLE_NAME='" + table_name + "' and t.COLUMN_NAME=a.field_code(+)";
    List list = null;
    try {
      list = dao.findBySql(sql);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return list;
  }

  /**
   * 得到所有界面配置视图高级属性配置数据
   * 
   * @param uiconf_type
   *            String uiconf_type
   * @return List
   */
  public List getUIConf(String uiconf_type) {
    List return_list = dao
      .findBySql("select a.uiconf_field,'' as uiconf_value,a.uiconf_field_name,a.uiconf_field_type  from sys_uiconf"
        + Tools.addDbLink() + " a" + " where  a.uiconf_type = '" + uiconf_type + "'");
    return return_list;
  }

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  /**
   * 得到所有功能数据
   * 
   * @return List
   */
  public List getAllSysModules() throws Exception {

    String hql = "select *  from Sys_Module  module " + "order by module.MODULE_CODE";
    List list = null;
    try {

      list = dao.findBySql(hql);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 得到所有操作数据
   * 
   * @return 所有操作数据的List
   * @author wanghongtao
   */
  public List getAllSysActions() throws Exception {
    String sql = "select * from sys_action" + Tools.addDbLink() + " order by action_code ";
    List list = null;
    try {
      list = dao.findBySql(sql);
    } catch (Exception e) {
      throw e;
    }

    return list;
  }

  /**
   * 根据功能ID查找相关联的界面视图
   * 
   * @param moduleId
   *            String moduledId
   * @return List
   * @throws Exception
   * @author 黄节2008年5月9日修改功能对界面视图管理中缺数据的bug
   */
  public List findSysModuleViewByModuleId(String moduleId) throws Exception {
    // String sql =
    // "select smv.*, su.* from sys_module_view  smv, sys_uimanager su where smv.ui_id = su.ui_id and smv.set_year = su.set_year and smv.module_id = ? order by smv.disp_order";
    // add by wl 20110327 sys_uimanager分区划 关联查询时加入区划条件
    String rg_code = SessionUtil.getRgCode();
    String sql = "select smv.*, su.* from sys_module_view  smv, sys_uimanager su where smv.ui_id = su.ui_id and  smv.module_id = ? and su.rg_code=? and su.set_year=? order by smv.disp_order";
    List list = null;
    try {
      list = dao.findBySql(sql, new Object[] { moduleId, rg_code, SessionUtil.getLoginYear() });
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 根据操作ID查找相关联的界面视图
   * 
   * @param actionId
   *            操作Id
   * @return 操作id对应的视图列表
   * @throws Exception
   * @author wanghongtao
   */
  public List findSysActionViewByActionId(String actionId) throws Exception {
    // add by wl 20110327 sys_uimanager分区划 关联查询时加入区划条件
    String sql = "select sav.*, su.* from sys_action_view  sav, sys_uimanager su where sav.ui_id = su.ui_id and sav.rg_code = su.rg_code and sav.set_year = su.set_year and sav.action_id = ? and su.rg_code=? and sav.set_year=? order by sav.disp_order";
    List list = null;
    try {
      list = dao.findBySql(sql, new Object[] { actionId, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 根据功能ID修改sys_module_view
   * 
   * @param moduleId
   *            Stirng moduleId
   * @param newViews
   *            List newViews
   * @throws Exception
   */
  public void updateSysModuleViewByModuleId(String moduleId, List newViews, String flag) throws Exception {
    // 删除相关联的功能视图数据
    if (flag.equals("modify")) {
      String delSql = "delete sys_module_view where module_id = ? and rg_code = ? and set_year=?";
      dao.executeBySql(delSql, new Object[] { moduleId, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
    }

    // 重新写入相关联的功能视图数据
    if (newViews != null && newViews.size() > 0) {
      Session session = dao.getSession();
      Connection conn = session.connection();
      try {
        StringBuffer insertSql = new StringBuffer();
        insertSql.append("insert into sys_module_view" + Tools.addDbLink() + " ")
          .append("(module_id, ui_id, disp_order, remark, last_ver,key_value,rg_code,set_year ) ")
          .append("values (?, ?, ?, ?, ?, ?,?,?) ");
        PreparedStatement insertPsmt = conn.prepareStatement(insertSql.toString());
        for (int i = 0; i < newViews.size(); i++) {
          XMLData view = (XMLData) newViews.get(i);
          insertPsmt.setString(1, moduleId);
          insertPsmt.setString(2, (String) view.get("ui_id"));
          insertPsmt.setInt(3, ((Integer) view.get("disp_order")).intValue());
          insertPsmt.setString(4, (String) view.get("remark"));
          insertPsmt.setString(5, Tools.getCurrDate());
          insertPsmt.setString(6, (String) view.get("key_value"));
          insertPsmt.setString(7, SessionUtil.getRgCode());
          insertPsmt.setString(8, SessionUtil.getLoginYear());
          insertPsmt.addBatch();
        }
        insertPsmt.executeBatch();

        // 保存操作日志
        /************* qiaohd******start *******/
        /*
         * LogDTO logDto = new LogDTO(); logDto.setLog_type("1");
         * logDto.setRemark(SessionUtil.getOnlineUser().getUser_name() +
         * "新增了module_id=" + moduleId + "的功能对界面视图关系");
         * FLog.writeLog(logDto);
         */
        /************* qiaohd******end *******/
      } catch (Exception e) {
        throw e;
      } finally {
        if (session != null) {
          dao.closeSession(session);
        }
      }
    }

  }

  /**
   * 根据操作ID修改sys_action_view
   * 
   * @param moduleId
   *            Stirng actionId 操作Id
   * @param newViews
   *            List newViews 操作对应视图的信息
   * @param flag
   *            String flag 标识是信息或者修改（modify）
   * @throws Exception
   * @author wanghongtao
   */
  public void updateSysActionViewByActionId(String actionId, List newViews, String flag) throws Exception {
    // 删除相关联的操作视图数据
    if (flag.equals("modify")) {
      String delSql = "delete sys_action_view" + Tools.addDbLink() + " where action_id = '" + actionId + "'";
      dao.executeBySql(delSql);
    }

    // 重新写入相关联的操作视图数据
    if (newViews != null && newViews.size() > 0) {
      Session session = dao.getSession();
      Connection conn = session.connection();
      try {
        StringBuffer insertSql = new StringBuffer();
        insertSql.append("insert into sys_action_view" + Tools.addDbLink() + " ")
          .append("(action_id, ui_id, disp_order, set_year, remark, last_ver,key_value,rg_code ) ")
          .append("values (?, ?, ?, ?, ?, ?, ?,?) ");
        PreparedStatement insertPsmt = conn.prepareStatement(insertSql.toString());
        for (int i = 0; i < newViews.size(); i++) {
          XMLData view = (XMLData) newViews.get(i);
          insertPsmt.setString(1, actionId);
          insertPsmt.setString(2, (String) view.get("ui_id"));
          insertPsmt.setInt(3, ((Integer) view.get("disp_order")).intValue());
          insertPsmt.setString(4, (String) view.get("set_year"));
          insertPsmt.setString(5, (String) view.get("remark"));
          insertPsmt.setString(6, Tools.getCurrDate());
          insertPsmt.setString(7, (String) view.get("key_value"));
          insertPsmt.setString(8, SessionUtil.getRgCode());// add by
          // kongcy
          // at
          // 2012-05-09
          insertPsmt.addBatch();
        }
        insertPsmt.executeBatch();

        // 保存操作日志
        /************* qiaohd******start *******/
        /*
         * LogDTO logDto = new LogDTO(); logDto.setLog_type("1");
         * logDto.setRemark(SessionUtil.getOnlineUser().getUser_name() +
         * "新增了action_id=" + actionId + "的操作对界面视图关系");
         * FLog.writeLog(logDto);
         */
        /************* qiaohd******end *******/
      } catch (Exception e) {
        throw e;
      } finally {
        if (session != null) {
          dao.closeSession(session);
        }
      }
    }

  }

  /**
   * 删除一条sys_module_view记录
   * 
   * @param data
   *            XMLData data
   * @param brotherViews
   *            List brotherViews
   * @throws Exception
   */
  public void deleteSysModuleView(XMLData data, List brotherViews) throws Exception {
    StringBuffer delSql = new StringBuffer();
    delSql.append("delete from sys_module_view" + Tools.addDbLink() + " ").append("where module_id = ? ")
      .append("and ui_id = ? ");
    if (data.get("key_value") != null && !"".equals(data.get("key_value"))) {
      delSql.append("and key_value = ? ");
    }
    try {
      if (data.get("key_value") == null || "".equals(data.get("key_value"))) {
        dao
          .executeBySql(delSql.toString(), new Object[] { (String) data.get("module_id"), (String) data.get("ui_id") });
      } else {
        dao.executeBySql(delSql.toString(), new Object[] { (String) data.get("module_id"), (String) data.get("ui_id"),
          (String) data.get("key_value") });
      }
      // batchUpdateModuleViewDispOrder(brotherViews);

      // 保存操作日志
      /************* qiaohd******start *******/
      /*
       * LogDTO logDto = new LogDTO(); logDto.setLog_type("1");
       * logDto.setRemark(SessionUtil.getOnlineUser().getUser_name() +
       * "删除一条功能视图（sys_module_view）记录"); FLog.writeLog(logDto);
       */
      /************* qiaohd******end *******/
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
  }

  /**
   * 删除一条sys_action_view记录
   * 
   * @param data
   *            XMLData data 要删除的数据信息
   * @param brotherViews
   *            List brotherViews 要删除数据相关的视图信息
   * @throws Exception
   * @author wanghongtao
   */
  public void deleteSysActionView(XMLData data, List brotherViews) throws Exception {
    StringBuffer delSql = new StringBuffer();
    delSql.append("delete from sys_action_view" + Tools.addDbLink() + " ").append("where action_id = ? ")
      .append("and ui_id = ? ").append("and set_year = ? ");
    if (data.get("key_value") != null && !"".equals(data.get("key_value"))) {
      delSql.append("and key_value = ? ");
    }
    try {
      if (data.get("key_value") == null || "".equals(data.get("key_value"))) {
        dao.executeBySql(delSql.toString(), new Object[] { (String) data.get("action_id"), (String) data.get("ui_id"),
          (String) data.get("set_year") });
      } else {
        dao.executeBySql(delSql.toString(), new Object[] { (String) data.get("action_id"), (String) data.get("ui_id"),
          (String) data.get("set_year"), (String) data.get("key_value") });
      }
      // batchUpdateModuleViewDispOrder(brotherViews);

      // 保存操作日志
      /************* qiaohd******start *******/
      /*
       * LogDTO logDto = new LogDTO(); logDto.setLog_type("1");
       * logDto.setRemark(SessionUtil.getOnlineUser().getUser_name() +
       * "删除一条操作对视图（sys_action_view）记录"); FLog.writeLog(logDto);
       */
      /************* qiaohd******end *******/
    } catch (Exception ex) {
      throw ex;
    }
  }

  /**
   * 批量修改功能视图显示顺序
   * 
   * @param viewList
   *            List viewList
   * @throws Exception
   */
  public void batchUpdateModuleViewDispOrder(List viewList) throws Exception {
    Session session = dao.getSession();
    Connection conn = session.connection();
    try {
      PreparedStatement updateDispOrderSql = conn.prepareStatement("update sys_module_view" + Tools.addDbLink()
        + " set disp_order = ? where module_id = ? and ui_id = ?  and key_value = ?");
      for (int i = 0; viewList != null && i < viewList.size(); i++) {
        XMLData view = (XMLData) viewList.get(i);
        updateDispOrderSql.setInt(1, ((Integer) view.get("disp_order")).intValue());
        updateDispOrderSql.setString(2, (String) view.get("module_id"));
        updateDispOrderSql.setString(3, (String) view.get("ui_id"));
        updateDispOrderSql.setString(4, (String) view.get("key_value"));
        updateDispOrderSql.addBatch();
      }
      updateDispOrderSql.executeBatch();

      // 保存操作日志
      /************* qiaohd******start *******/
      /*
       * LogDTO logDto = new LogDTO(); logDto.setLog_type("1");
       * logDto.setRemark(SessionUtil.getOnlineUser().getUser_name() +
       * "新增或修改了界面视图"); FLog.writeLog(logDto);
       */
      /************* qiaohd******end *******/
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (session != null) {
        dao.closeSession(session);
      }
    }
  }

  /**
   * 修改视图的显示序号
   * 
   * @param rowData
   *            XMLData rowData
   * @param order
   *            String order
   * @throws Exception
   */
  public void modifyViewOrder(XMLData rowData, String order) throws Exception {
    try {
      String sql = "update sys_module_view t set t.key_value='" + rowData.get("key_value") + "',t.disp_order =" + order
        + " where t.ui_id = '" + (String) rowData.get("ui_id") + "' and  t.module_id = "
        + (String) rowData.get("module_id") + "";
      dao.executeBySql(sql);

      // 保存操作日志
      /************* qiaohd******start *******/
      /*
       * LogDTO logDto = new LogDTO(); logDto.setLog_type("1");
       * logDto.setRemark(SessionUtil.getOnlineUser().getUser_name() +
       * "修改了功能module_id=" + (String) rowData.get("module_id") +
       * "对应界面视图的显示顺序或关键字"); FLog.writeLog(logDto);
       */
      /************* qiaohd******end *******/
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
  }

  /**
   * 修改操作视图的显示序号
   * 
   * @param rowData
   *            XMLData rowData 要修改的视图信息
   * @param order
   *            String order 要修改的显示序号
   * @throws Exception
   * @author wanghongtao
   */
  public void modifyActionViewOrder(XMLData rowData, String order) throws Exception {
    try {
      String sql = "update sys_action_view t set t.key_value='" + rowData.get("key_value") + "',t.disp_order =" + order
        + " where t.ui_id = '" + (String) rowData.get("ui_id") + "' and  t.action_id = '"
        + (String) rowData.get("action_id") + "'";
      dao.executeBySql(sql);

      // 保存操作日志
      /************* qiaohd******start *******/
      /*
       * LogDTO logDto = new LogDTO(); logDto.setLog_type("1");
       * logDto.setRemark(SessionUtil.getOnlineUser().getUser_name() +
       * "修改了操作action_id=" + (String) rowData.get("action_id") +
       * "对应界面视图的显示顺序或关键字"); FLog.writeLog(logDto);
       */
      /************* qiaohd******end *******/
    } catch (Exception ex) {
      throw ex;
    }
  }

  /**
   * 从UI_Code获取UI_Id add by wl 20110328 ui_code 不唯一要加入区划编码
   * 
   * @return List
   */
  public List getUIIdByUICode(String ui_code) {
    // List return_list = dao.findBySql("select a.* from sys_uimanager"
    // + Tools.addDbLink() + " a where a.ui_code='" + ui_code
    // + "' order by a.ui_id");
    String rg_code = SessionUtil.getRgCode();
    String sql = " select su.* from sys_uimanager su where su.ui_code=? and su.rg_code=? and su.set_year=? order by su.ui_id ";
    List return_list = dao.findBySql(sql, new Object[] { ui_code, rg_code, SessionUtil.getLoginYear() });
    return return_list;
  }

  /**
   * 根据uiId查询视图明细
   * 
   * @param uiId
   * @author lwq
   * @return
   */
  public List getViewDetailByUIId(String uiId) {
    List viewDetailList = null;
    String sql = "select t.* from sys_uidetail t where t.ui_id = ?";
    viewDetailList = dao.findBySql(sql, new String[] { uiId });
    return viewDetailList;
  }

  /**
   * 根据uiconfId查询视图明细
   * 
   * @param uiId
   * @author lwq
   * @return
   */
  public List getSuperViewDetailByConfId(String uiConfId) {
    List viewDetailList = null;
    String sql = "select t.* from SYS_UICONF_DETAIL t where t.UICONF_ID = ?";
    viewDetailList = dao.findBySql(sql, new String[] { uiConfId });
    return viewDetailList;
  }

  /**
   * 根据ui_id查找视图的引用信息
   * 
   * @param ui_id
   * @return
   */
  public List findViewUsedInfoByUIID(String ui_id) {
    List moduleList = null;
    StringBuffer sql = new StringBuffer();
    sql.append("select sm1.menu_code,sm1.menu_name, sm.module_name, sm.module_code")
      .append(" from sys_module sm, sys_module_view smv")
      .append(" left join sys_menu_module smm on smm.module_id = smv.module_id")
      .append(" left join sys_menu sm1 on smm.menu_id = sm1.menu_id")
      .append(" where sm.module_id = smv.module_id and smv.ui_id = ?");

    moduleList = dao.findBySql(sql.toString(), new String[] { ui_id });
    return moduleList;
  }

  /**
   * 根据参数ui_code查找它下层的最大ui_code
   * 
   * @param ui_code
   * @return
   */
  public String findMaxUiCodeByParentUiCode(String ui_code) {
    String newUiCode = "";
    // String sql =
    // "select max(su.ui_code) as ui_code from sys_uimanager su where su.ui_code like '"
    // + ui_code + "_%'";
    // add by wl 20110327 sys_uimanager 分区划根据ui_code模糊查询时加入区划
    String rg_code = SessionUtil.getRgCode();
    String sql = "select max(su.ui_code) as ui_code from sys_uimanager su where su.ui_code like '" + ui_code
      + "_%' and su.rg_code=? and su.set_year=?";
    List list = dao.findBySql(sql, new Object[] { rg_code, SessionUtil.getLoginYear() });
    if (list.size() > 0) {
      newUiCode = ((XMLData) list.get(0)).get("ui_code").toString();
    }
    return newUiCode;
  }

  /**
   * 根据ui_id查找视图的所有信息
   * 
   * @param ui_id
   * @return
   */
  public XMLData findViewAllInfo(String ui_id) {
    XMLData viewInfo = new XMLData();
    List ui_manager_list = null;
    List ui_detail_list = null;
    List ui_conf_detail_list = null;
    // add by wl 20110328 获取区划编码
    String rg_code = SessionUtil.getRgCode();
    // 查询视图主表
    StringBuffer ui_manager_sql = new StringBuffer();
    // ui_manager_sql
    // .append("select * from Sys_Uimanager where UI_ID=? or ui_code=?");
    ui_manager_sql.append("select * from Sys_Uimanager where UI_ID=? or (ui_code=? and rg_code=? and set_year=?)");// add
    // by
    // wl
    // 20110328
    // 加入区划编码

    // 查询视图明细表
    StringBuffer ui_detail_sql = new StringBuffer();
    ui_detail_sql.append("select * from sys_uidetail sud where sud.ui_id=?");

    // 视图高级属性表信息
    StringBuffer ui_conf_detail_sql = new StringBuffer();
    ui_conf_detail_sql.append("select * from Sys_Uiconf_detail");
    // ui_conf_detail_sql
    // .append(" where uiconf_id =? or uiconf_id in (select ui_id from sys_uimanager where ui_code =?)");
    ui_conf_detail_sql
      .append(" where uiconf_id =? or uiconf_id in (select ui_id from sys_uimanager where ui_code =? and rg_code=? and set_year=?)");// add
    // by
    // wl
    // 20110328
    // 加入区划
    ui_conf_detail_sql.append(" union all ");
    ui_conf_detail_sql.append(" select * from Sys_Uiconf_detail");
    ui_conf_detail_sql
      .append(" where uiconf_id in (select ui_detail_id from sys_uidetail where is_enabled=1 and ui_id =?");
    // ui_conf_detail_sql
    // .append(" or (ui_id in (select ui_id from sys_uimanager where ui_code = ?)))");
    ui_conf_detail_sql
      .append(" or (ui_id in (select ui_id from sys_uimanager where ui_code = ? and rg_code=? and set_year=?)))");// add
    // by
    // wl
    // 20110328
    // sys_uimanager
    // 加入区划信息

    ui_manager_list = dao.findBySql(ui_manager_sql.toString(),
      new Object[] { ui_id, ui_id, rg_code, SessionUtil.getLoginYear() });

    // 视图明细表视图明细表信息（有哪些控件）
    // System.out.println("视图明细表视图明细表信息（有哪些控件）"+ui_detail_sql.toString());
    ui_detail_list = dao.findBySql(ui_detail_sql.toString(), new Object[] { ui_id });
    // 判断明细列是否有设置多个默认值的情况，如果ui_detail表中相应value值为"MULTI_DEFAULT_VALUE",说明配置多个值
    for (int i = 0; ui_detail_list != null && i < ui_detail_list.size(); i++) {
      XMLData detailData = (XMLData) ui_detail_list.get(i);
      if (detailData.get("value") != null && "MULTI_DEFAULT_VALUE".equalsIgnoreCase(detailData.get("value").toString())) {
        detailData.put("value", getMultiValueAsArray((String) detailData.get("ui_detail_id")));
      }
    }
    // 视图高级属性表信息
    // System.out.println("视图高级属性表信息"+ui_conf_detail_sql.toString());
    ui_conf_detail_list = dao.findBySql(ui_conf_detail_sql.toString(), new Object[] { ui_id, ui_id, rg_code,
      SessionUtil.getLoginYear(), ui_id, ui_id, rg_code, SessionUtil.getLoginYear() });

    viewInfo.put("ui_manager_list", ui_manager_list);
    viewInfo.put("ui_detail_list", ui_detail_list);
    viewInfo.put("ui_conf_detail_list", ui_conf_detail_list);

    return viewInfo;
  }

  /**
   * 得到所有应用系统数据
   */
  public List findAllSysApps() throws Exception {
    String sql = "select * from sys_app" + Tools.addDbLink() + " order by sys_id ";
    List list = null;
    try {
      list = dao.findBySql(sql);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 以数组类型返回对应明细列的默认值
   * 
   * @param detailData
   *            明细列信息
   * @return 数组格式的默认值
   */
  public String[] getMultiValueAsArray(String ui_detail_id) {
    // ui_detail_id为空时，返回空
    if (ui_detail_id == null || "".equalsIgnoreCase(ui_detail_id)) {
      return null;
    }
    // 返回值
    String[] return_arr = null;
    // 查询对应明细列的所有默认值
    List tempValues = dao.findBySql("select value from sys_uidetail_multivalue where ui_detail_id='" + ui_detail_id
      + "'");
    // 如果有默认值，通过循环构建成默认值数组
    if (tempValues != null && tempValues.size() > 0) {
      return_arr = new String[tempValues.size()];
      for (int j = 0; j < tempValues.size(); j++) {
        return_arr[j] = ((XMLData) tempValues.get(j)).get("value").toString();
      }
    }
    // 返回获取的默认值
    return return_arr;
  }

  /**
   * 判断在区划内是否存在相同的视图编号 add by wl 20110327 增加视图时同一区划内编码不允许相同、不同区划内编码允许相同
   * 
   * @param ui_code
   * @return
   */
  public boolean isExistTheSameUiCode(String ui_code) {

    String rg_code = SessionUtil.getRgCode();
    String sql = " SELECT * FROM SYS_UIMANAGER SU WHERE SU.UI_CODE=? AND SU.RG_CODE=? and su.set_year=?";
    List result = dao.findBySql(sql, new Object[] { ui_code, rg_code, SessionUtil.getLoginYear() });
    if (result.size() == 0) {
      return false;
    } else {
      return true;
    }
  }

  @Override
  public void saveUIConfigure(SysUimanager entity, String optType) throws Exception {

    // 获取区划、年度
    String rg_code = SessionUtil.getRgCode();
    String set_year = SessionUtil.getLoginYear();
    String ui_id = entity.getUi_id();
    String ui_detail_id = "";
    if ("new".equals(optType) || "".equals(ui_id)) {
      ui_id = UUIDRandom.generate();
    } else {
      ui_id = entity.getUi_id();
    }

    /*
     * if (UtilCache.uiMap != null) { UtilCache.uiMap.clear(); }
     * 
     * if (UtilCache.billUiMap != null) { UtilCache.billUiMap.clear(); }
     */
    if (entity != null) {

      dao.executeBySql("delete from sys_uimanager" + Tools.addDbLink() + " where ui_id ='" + ui_id.toString() + "'");
      dao.executeBySql("delete from sys_uidetail" + Tools.addDbLink() + " where ui_id ='" + ui_id.toString() + "'");

      entity.setSet_year(Integer.valueOf(set_year));
      entity.setRg_code(rg_code);
      entity.setUi_id(ui_id);
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date date = new Date();
      entity.setLast_ver(dateFormat.format(date));
      dao.saveDataBySql("sys_uimanager", entity);
      cacheUtil.delete("uiviewtree");
    }

    List<SysUidetail> ui_detail_list = entity.getUidetails();
    List<SysUidetail> ui_detail_lists = new ArrayList<SysUidetail>();
    Iterator<SysUidetail> iterator_ui_detail_list = ui_detail_list.iterator();
    while (iterator_ui_detail_list.hasNext()) {
      SysUidetail rowData = (SysUidetail) iterator_ui_detail_list.next();
      if (rowData != null) {

        ui_detail_id = rowData.getUi_detail_id();
        if ("new".equals(optType) || "".equals(ui_detail_id)) {
          ui_detail_id = UUIDRandom.generate();

        }
        if ("".equals(rowData.getIs_virtual_column()) || rowData.getIs_virtual_column() == null) {
          rowData.setIs_virtual_column("false");
        }

        rowData.setField_name(rowData.getId());
        rowData.setRg_code(rg_code);
        rowData.setSet_year(Integer.valueOf(set_year));
        rowData.setUi_id(ui_id);
        rowData.setUi_detail_id(ui_detail_id);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        rowData.setLast_ver(dateFormat.format(date));
        ui_detail_lists.add(rowData);
      }
    }
    try {
      dao.batchSaveDataBySql("sys_uidetail", ui_detail_lists);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw e;
    }

    // dao.saveDataBySql("sys_uidetail", aaSysUidetail);

    // 保存操作日志
    /************* qiaohd******start *******/
    /*
     * LogDTO logDto = new LogDTO(); logDto.setLog_type("1");
     * logDto.setRemark(SessionUtil.getOnlineUser().getUser_name() +
     * "新增或修改了界面视图：ui_code=" + ui_manager.get("ui_code"));
     * FLog.writeLog(logDto);
     */
    /************************* end *******/
  }

  @Override
  public void delView(String viewId) {
    // TODO Auto-generated method stub
    if (viewId == null || "".equals(viewId)) {
      return;
    }
    dao.executeBySql("delete from sys_uimanager" + Tools.addDbLink() + " where ui_id ='" + viewId.toString() + "'");
    dao.executeBySql("delete from sys_uidetail" + Tools.addDbLink() + " where ui_id ='" + viewId.toString() + "'");
    dao.executeBySql("delete from sys_menu_view" + Tools.addDbLink() + " where ui_id ='" + viewId.toString() + "'");
    // 清缓存
    cacheUtil.delete("uiviewtree");
  }
}
