/*
 * $Id: SysLogBO.java 570987 2015-04-29 16:37:59Z zhaoqiang1 $
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.service.systemmanager.log.ibs;





import gov.df.fap.api.systemmanager.log.ibs.ISysLog;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.xml.XMLData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 日志管理服务端接口实现类
 * 
 * @author a
 */
@Service
public class SysLogBO implements ISysLog {


  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao = null;

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  //private final String SYS_LOGINFO_TABLE = "mappingfiles.sysdb.SysLoginfo";

  /**
       * 获得符合条件的数据，以List格式返回
       * 
       * @param pageIndex
       *                分页当前页
       * @param pageRows
       *                每页条数
       * @param condition
       *                查询条件
       * @param table
       *                表名
       * @param state
       *                指标状态 return 以row为单位的指标
       */
  public XMLData findDatas(String sql, String countSql) throws Exception {
    XMLData data = new XMLData();
    try {
      Integer totalCount = new Integer(0);
      // 查询并返回数据
      List rows = dao.findBySql(sql);
      // 获得总数据条数
      if (countSql != null && !countSql.equals("")) {
        List count = dao.findBySql(countSql);
        totalCount = new Integer((String) ((XMLData) count.get(0)).get("count(*)"));
      }
      data.put("total_count", totalCount);
      data.put("row", rows);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return data;
  }

  /**
       * 删除日志数据
       */
  public void deleteSysLoginfo(XMLData xmlData) throws Exception {

    try {
      String loginYear = SessionUtil.getLoginYear();
      String rg_code = SessionUtil.getRgCode();
      xmlData.put("set_year", loginYear);
      xmlData.put("rg_code", rg_code);
      dao.deleteDataBySql("SYS_LOGINFO", xmlData, new String[] { "log_id", "set_year", "rg_code" });
      //dao.deleteDataBySql("SYS_LOGINFO", xmlData,new String[]{"log_id"});
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  /**
       * 批量删除日志数据
       */
  public void batchDeleteSysLoginfos(List datas) throws Exception {
    for (int i = 0; datas != null && i < datas.size(); i++) {
      XMLData xmlData = (XMLData) datas.get(i);
      String loginYear = SessionUtil.getLoginYear();
      String rg_code = SessionUtil.getRgCode();
      xmlData.put("set_year", loginYear);
      xmlData.put("rg_code", rg_code);
      dao.deleteDataBySql("SYS_LOGINFO", xmlData, new String[] { "log_id", "set_year", "rg_code" });

      //dao.deleteDataBySql("SYS_LOGINFO", xmlData,new String[]{"log_id"});
    }
  }

  public void batchBackUpDataToBakTable(String conditions) {
    dao.executeBySql("insert into SYS_LOGINFO_BAK select *from SYS_LOGINFO t where 1=1  " + conditions);
    dao.executeBySql("delete from  SYS_LOGINFO t   where 1=1  " + conditions);
  }

  /**
       * 清除sys_loginfo表所有满足条件{oper_time>当前时间-30天}的记录
       * 
       * @author 黄节2007年9月15日添加
       */
  public void removeInvalidateLog() {
    Calendar now = Calendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    now.add(Calendar.DAY_OF_YEAR, -30);
    String nowDate = formatter.format(now.getTime());
    String loginYear = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    // 备份sys_loginfo表中的过期数据到sys_loginfo_bak表
    String sql = "insert into sys_loginfo_bak (select * from sys_loginfo sl where sl.oper_time<? and set_year=? and rg_code=?)";
    dao.executeBySql(sql, new Object[] { nowDate, loginYear, rg_code });
    // 删除sys_loginfo表中的过期数据
    sql = "delete from sys_loginfo sl where sl.oper_time<? and set_year=? and rg_code=?";
    dao.executeBySql(sql, new Object[] { nowDate, loginYear, rg_code });
  }


}