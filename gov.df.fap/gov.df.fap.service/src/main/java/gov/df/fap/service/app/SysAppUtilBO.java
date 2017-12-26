package gov.df.fap.service.app;

import gov.df.fap.api.util.ISysAppUtil;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.log.Log;
import gov.df.fap.util.Tools;
import gov.df.fap.util.xml.XMLData;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Title:用户子系统查询类
 * </p>
 * <p>
 * Description:用于帮助dictionary、workflow及其他子系统查询用户子系统列表
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: 北京方正春元科技发展有限公司
 * </p>
 * <p>
 * CreateData 2008-2-15
 * </p>
 * 
 * @author 黄节
 * @version 1.0
 */

@Service
public class SysAppUtilBO implements ISysAppUtil {
  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao;

  /**
   * 得到所有应用系统数据
   * 
   * @return List (XMLData)具体内容查询表sys_app
   * @throws SQLException
   *             向上抛出sql错误
   * 
   */
  public List findAllSysApps() throws SQLException {
    String sql = "select * from sys_app" + Tools.addDbLink() + " where sys_id<>'000' order by sys_id ";
    List list = dao.findBySql(sql);
    return list;
  }

  /**
   * 获取所有子系统模块信息，提供给FComboBox初始化数据方法
   * 
   * @return Object[] 返回FComboBox需要的两种数据id和data
   * 
   * @throws SQLException 向前抛出sql错误
   * 
   */
  public Object[] getAllSysAppforFComboBox() throws SQLException {
    Object[] back = new Object[2];// 创建一个对象数组,放String数组show_data和向量id
    List tmp = this.findAllSysApps();
    int size = tmp.size();
    if (size > 0) {
      Vector id = new Vector();
      String[] show_data = new String[size];
      Iterator itr = tmp.iterator();
      XMLData tmp_data;
      String user_sys_id, user_sys_name;
      int i = 0;
      while (itr.hasNext()) {
        tmp_data = (XMLData) itr.next();
        user_sys_id = (String) tmp_data.get("sys_id");
        user_sys_name = (String) tmp_data.get("sys_name");
        id.add(user_sys_id);
        show_data[i] = user_sys_id + " " + user_sys_name;
        i++;
      }
      back[0] = id;
      back[1] = show_data;
      return back;
    } else { // 如果数组列表为空，则返回null
      return null;
    }
  }

  /**
   * @return the dao
   */
  public GeneralDAO getDao() {
    return dao;
  }

  /**
   * @param dao the dao to set
   */
  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

@Override
public List findSysAppsById(String sysId) throws SQLException {
	// TODO Auto-generated method stub
	 String sql = "select * from sys_app" + Tools.addDbLink() + " where sys_id<>'000' and  sys_id =?" ; 
	    List list = null;
	    try {
	      list = dao.findBySql(sql, new Object[] { sysId });
	    } catch (Exception e) {
	      Log.error(e.getMessage());
	    }
	    return list;	    
	    
}
}
