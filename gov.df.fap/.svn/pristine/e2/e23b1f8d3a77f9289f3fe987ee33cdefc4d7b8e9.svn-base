package gov.df.fap.api.util;

import java.sql.SQLException;
import java.util.List;

/**
 * <p>
 * Title:用户子系统查询类
 * </p>
 * <p>
 * Description:用于帮助dictionary、workflow及其他子系统查询用户子系统列表
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006 北京方正春元科技发展有限公司
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
public interface ISysAppUtil {
  /**
   * 得到所有应用系统数据
   * 
   * @return List (XMLData)具体内容查询表sys_app
   * @throws SQLException
   *             向上抛出sql错误
   * 
   */
  public List findAllSysApps() throws SQLException;

  /**
   * 获取所有子系统模块信息，提供给FComboBox初始化数据方法
   * 
   * @return Object[] 返回FComboBox需要的两种数据id和data
   * 
   * @throws SQLException
   *             向前抛出sql错误
   * 
   */
  public Object[] getAllSysAppforFComboBox() throws SQLException;
  
  /**
   * 根据sysId得到应用系统数据
   * 
   * @return List (XMLData)具体内容查询表sys_app
   * @throws SQLException
   *             向上抛出sql错误
   * 
   */
  public List findSysAppsById(String sysId) throws SQLException;
  
  
}
