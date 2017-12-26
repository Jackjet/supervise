package gov.df.fap.service.para;

import gov.df.fap.api.util.paramanage.IParaManage;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.SQLUtil;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.Tools;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 系统参数管理组件实现类 实现了取当前子系统的系统参数、根据setCode和paraCode访问系统参数等功能
 * 
 * @author
 * 
 */

@Component("sys.paraManBO")
public class ParaManageBO implements IParaManage {
  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao = null;

  // 所有区划的
  private Map globalParaMapAllRG = new HashMap();

  public ParaManageBO() {
  }

  /**
   * 根据用户登录年度查询该年度系统参数,登录年度内部通过SessionUtil上下文获取
   * 
   * @return 用户登录年度系统参数Map
   * @author
   */
  protected synchronized Map getLoginParaMap(String year) throws Exception {
    Map loginParaMap = new HashMap();
    String sql = "select table_name from user_tables where table_name='SYS_USERPARA'";
    List result = dao.findBySqlWithYear(Integer.parseInt(year), sql);

    if (result.size() > 0) {
      HashMap map = null;
      for (int i = 0; i < result.size(); i++) {
        map = (HashMap) result.get(i);
        String paraCode = (String) map.get("chr_code");
        String paraValue = (String) map.get("chr_value");
        String setCode = (String) map.get("set_id");
        String key = "";
        if (setCode != null && !"".equalsIgnoreCase(setCode.trim()))
          key = paraCode + "_" + setCode;
        else
          key = paraCode;
        if (loginParaMap.containsKey(key)) {
          loginParaMap.remove(key);
          loginParaMap.put(key, paraValue);
        } else
          loginParaMap.put(key, paraValue);
      }
    }
    System.out.println("当前服务端有" + loginParaMap.keySet().size() + "个系统变量！");
    return loginParaMap;
  }

  /**
   * @author -czh add
   * @param sys_id
   *            ,子系统Id
   * @return 根据子系统Id 取出子系统名称
   */

  public List getSysNamebySysId(String sys_id) {

    List result = null;
    StringBuffer hql = new StringBuffer();
    hql.append("select sys_name from sys_app where sys_id='");
    hql.append(sys_id);
    hql.append("'");

    result = dao.findBySql(hql.toString());
    return result;

  }

  /* Add by CZH 2007-10-11 END */

  public List getAllEnabledSysPara(String sysId) {
    // TODO 自动生成方法存根
    List result = null;
    String sysCond = " and 1=1";
    if (!sysId.equals("001"))
      sysCond = " and app.user_sys_id ='" + sysId + "'";
    String hql = "select * from Sys_Userpara para ,sys_user_sys_app app where app.sys_id=para.sys_id and para.IS_VISIBLE = 1 "
      + sysCond + SessionUtil.setYearAndRgCodeForCon("para") + " order by para.GROUP_NAME";
    result = dao.findBySql(hql);
    return result;
  }

  public List getGroupCount(String sysId) {
    // TODO 自动生成方法存根
    List result = null;
    String sysCond = "";
    if (!sysId.equals("001")) {
      sysCond = " and t.sys_id ='" + sysId + "'";
    }

    String sql = "select count(*) count from sys_userpara" + Tools.addDbLink() + " t where t.is_visible =1" + sysCond
      + SessionUtil.setYearAndRgCodeForCon("t") + " group by t.group_name order by t.group_name";

    result = dao.findBySql(sql);
    if (result == null)
      result = new ArrayList();
    return result;
  }

  /**
   * 根据setCode和paraCode访问系统参数等功能
   * 
   * @param paraCode
   *            系统参数code
   * @param setCode
   *            系统账套code
   * @return 返回要访问的系统参数值
   */
  public String getPara(String paraCode, String setCode) {
    // TODO 自动生成方法存根
    Map globalMap = SessionUtil.getParaMap();
    String key = "";
    if (setCode == null || setCode.equals(""))
      key = paraCode;
    else
      key = paraCode + "_" + setCode;
    if (globalMap.containsKey(key)) {
      return (String) globalMap.get(key);
    } else
      return "";
  }

  /**
   * 根据setCode和paraCode设置系统参数等功能
   * 
   * @param paraCode
   *            系统参数code
   * @param setCode
   *            系统账套code
   * @param paraValue
   *            设置参数返回值
   * @author 黄节2007年8月14日，修改系统环境变量不需要重启服务
   */
  public void setPara(final String paraCode, final String paraValue, final String setCode) {
    String key = "";
    if (setCode == null || setCode.equals(""))
      key = paraCode;
    else
      key = paraCode + "_" + setCode;
    // globalParaMap保存的是多年度系统参数,通过getGlobalParaMap()获取用户登录年度系统参数Map
    getGlobalParaMap().remove(key);
    getGlobalParaMap().put(key, paraValue);
    UpdatePara(paraCode, paraValue, setCode);
  }

  public void UpdatePara(String paraCode, String paraValue, String setCode) {
    if (setCode == null)
      setCode = "";
    String setCond = "";
    if (setCode.equals(""))
      setCond = " and set_id is null";
    else
      setCond = " and set_id = '" + setCode + "'";
    String sql = "update sys_userpara" + Tools.addDbLink() + " set chr_value='" + paraValue + "'" + " where chr_code='"
      + paraCode + "'" + setCond + SessionUtil.setYearAndRgCodeForCon(null);

    dao.executeBySql(sql, null);

    if ("App_Curr_Region".equalsIgnoreCase(paraCode)) {
      String strSql = "select DISTINCT T.TABLE_NAME from user_tables t,user_tab_columns c where t.table_name=c.TABLE_NAME and c.COLUMN_NAME='RG_CODE'";
      List list = dao.findBySql(strSql);
      String updateSql = "";
      Map map = null;
      if (list != null) {
        for (int i = 0; i < list.size(); i++) {
          map = (Map) list.get(i);
          updateSql = "update " + map.get("table_name") + " set rg_code=" + paraValue;
          dao.executeBySql(updateSql);
        }
      }
    }
  }

  public String getNumber() {
    String sql = "select " + SQLUtil.getSeqExpr("F_NUMBERID.NEXTVAL") + " as val FROM DUAL";
    GeneralDAO dao = (GeneralDAO) SessionUtil.getServerBean("generalDAO");
    List lst = dao.findBySql(sql);
    if (lst.size() > 0) {
      Map map = (Map) lst.get(0);
      String value = (String) map.get("val");
      return value;
    } else {
      // 请建立sequences
      return "-1";
    }
  }

  /**
   * 获取数据库时间 
   * update by yangzs
   */
  public String getDatabaseTime() {
    String sql = "select " + (TypeOfDB.isOracle() ? "sysdate" : "sysdate() as sysdate") + " from dual";
    List res = dao.findBySql(sql);
    Map map = (Map) res.get(0);
    String tmp = (String) map.get("sysdate");
    String result = tmp.toString().substring(0, tmp.lastIndexOf("."));
    return result;
  }

  public String getServerTime() {
    Calendar calender = Calendar.getInstance();
    SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    return f.format(calender.getTime());

  }

  public String getWebPath() {
    return SessionUtil.getWebPath();
  }

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
    if (dao != null)
      initiaGlobalParaByYear();
  }

  /**
   * 获取用户登录年度对应的系统参数Map
   * 
   * @return 用户登录年度对应的系统参数Map
   * @see gov.gfmis.fap.util.paramanage.IParaManage#getGlobalParaMap()
   * @author zhupeidong at 2008-11-24下午03:34:33
   */
  public Map getGlobalParaMap() {

    return initiaGlobalParaByYear();
  }

  public void setGlobalParaMap(Map globalParaMap) {
    initiaGlobalParaByYear();
  }

  /**
   * 根据年度初始化相应年度系统参数
   * 
   * @param loginParaMap
   *            如果不为空,直接保存;如果为空,则查询数据库初始化系统参数
   * @author jutin
   *   
   */
  private Map initiaGlobalParaByYear() {
    String year = SessionUtil.getLoginYear();
    String rgCode = SessionUtil.getRgCode();

    if (rgCode == null || "".equals(rgCode)) {
      List rgs = dao.findBySql("select chr_value from sys_userpara where chr_code = 'App_Curr_Region'");
      if (rgs != null && rgs.size() > 0) {
        rgCode = (String) (((Map) rgs.get(0)).get("chr_value"));
      }
    }
    if (globalParaMapAllRG.size() <= 0) {
      try {
        globalParaMapAllRG = getLoginParaMapNew(year);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    Map currentLoginParaMap = (Map) globalParaMapAllRG.get(year.trim().concat(rgCode.trim()));
    // 如果从全局map找也找不到当前年度+区划的map数据说明用户启用了新的财政,重新查询
    if (currentLoginParaMap == null || currentLoginParaMap.size() <= 0) {
      Map te = null;
      try {
        te = getEnableRGPara(year, rgCode);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      // 更新全局缓存
      globalParaMapAllRG.put(year.trim().concat(rgCode), te);
      // 从全局缓存取出当前年度区划的数据
      currentLoginParaMap = (Map) globalParaMapAllRG.get(year.trim().concat(rgCode.trim()));

    }
    return currentLoginParaMap;
  }

  /**
   * 
   * @param year
   * @param rg
   * @return
   * @throws Exception
   */
  protected synchronized Map getEnableRGPara(String year, String rg) throws Exception {
    Map loginParaMap = new HashMap();
    String sql = "select table_name from user_tables where table_name='SYS_USERPARA'";
    List result = dao.findBySqlWithYear(Integer.parseInt(year), sql);

    if (result.size() > 0) {
      HashMap map = null;
      for (int i = 0; i < result.size(); i++) {
        map = (HashMap) result.get(i);
        String paraCode = (String) map.get("chr_code");
        String paraValue = (String) map.get("chr_value");
        String setCode = (String) map.get("set_id");
        String key = "";
        if (setCode != null && !"".equalsIgnoreCase(setCode.trim()))
          key = paraCode + "_" + setCode;
        else
          key = paraCode;
        if (loginParaMap.containsKey(key)) {
          loginParaMap.remove(key);
          loginParaMap.put(key, paraValue);
        } else
          loginParaMap.put(key, paraValue);
      }
    }

    return loginParaMap;
  }

  /**
   * 服务自动加载所有系统参数
   * 
   * @author fengdz
   * @param year
   * @return
   * @throws Exception
   */
  protected synchronized Map getLoginParaMapNew(String year) throws Exception {
    Map loginParaMap = new HashMap();
    Map returnMap = new HashMap();
    String sql = "select table_name from user_tables where table_name='SYS_USERPARA'";
    List result = dao.findBySql(sql);
    int isdstore = 0;//是否是分布式库结构：1是， 0否
    if (result.size() > 0) {
      isdstore = 0;
    } else {
      isdstore = 1;
    }
    sql = "select chr_code,chr_value,set_id,rg_code,set_year from sys_userpara" + Tools.addDbLink() + ""
      + " where is_visible=1 and chr_value is not null";
    loginParaMap = dao.findBySqlWithAllDatasource(sql);
    //解决配置多个年度在表里没有这个年度产生无值，导致无法初始化系统参数的问题 
    int lp = loginParaMap.size();
    if (lp > 0) {
      Iterator it = loginParaMap.keySet().iterator();
      while (it.hasNext()) {
        String keyString = (String) it.next();
        Map valueMap = (Map) loginParaMap.get(keyString);
        if (valueMap != null && valueMap.size() > 0) {
          returnMap.put(keyString, valueMap);
        }
      }
    }
    if (returnMap != null && returnMap.size() > 0) {
      String reKey1 = (String) returnMap.keySet().iterator().next();
      System.out.println("加载【 " + returnMap.keySet().size() + "】个年度/区划系统参数！" + "每个区划加载【 "
        + ((HashMap) returnMap.get(reKey1)).size() + " 】个系统变量！");
    } else {
      System.out.println("没有加载系统参数！");
    }

    return returnMap;
  }

  /**
   * <strong>Description:</strong><br>
   * result参数为系统参数表sys_userpara中所有年度区划的数据<br>
   * 利用算法将不同区划（作为key）的值（作为value）构造一个新的Map返回<br>
   * 
   * @author fengdz
   * @date Mar 25, 2013 10:12:04 AM
   * 
   */
  private HashMap getSysParaMapForRGYEAR(List result) {
    // 返回的map，key=rg_code+set_year
    HashMap returnMap = new HashMap();
    // key2=rg_code+set_year
    String key2 = StringUtil.EMPTY;
    // 临时map作为returnMap的value
    HashMap tempMap1 = null;
    String mapKey = StringUtil.EMPTY;
    String setCode = StringUtil.EMPTY;
    HashMap tempMap2 = null;
    HashMap m1 = null;
    if (result.size() > 0) {
      int size = result.size();
      // int j = 0;
      for (int j = 0; j < size; j++) {

        tempMap2 = (HashMap) result.get(j);
        setCode = (String) tempMap2.get("set_id");
        key2 = String.valueOf(tempMap2.get("set_year")).trim().concat(String.valueOf(tempMap2.get("rg_code")).trim());
        // 返回map（returnMap）中的value（map）键

        if (returnMap.containsKey(key2)) {
          m1 = (HashMap) returnMap.get(key2);// 第一次放进的map
          // 重新组织
          if (setCode != null && !"".equalsIgnoreCase(setCode.trim())) {
            mapKey = String.valueOf(tempMap2.get("chr_code")).trim() + "_" + setCode;
          } else {
            mapKey = String.valueOf(tempMap2.get("chr_code")).trim();
          }
          m1.put(String.valueOf(mapKey).trim(), String.valueOf(tempMap2.get("chr_value")).trim());// 新的
          // code和value

        } else {
          // 只要是新的区划+年度map中没有先走这里
          tempMap1 = new HashMap();
          if (setCode != null && !"".equalsIgnoreCase(setCode.trim())) {
            mapKey = String.valueOf(tempMap2.get("chr_code")).trim() + "_" + setCode;
          } else {
            mapKey = String.valueOf(tempMap2.get("chr_code")).trim();
          }

          tempMap1.put(mapKey, String.valueOf(tempMap2.get("chr_value")).trim());
          returnMap.put(key2, tempMap1);
        }

      }
    }
    return returnMap;

  }

  /**
   * @author -ymj
   * @param String
   *            sql 更新参数
   */
  public void updateParam(String sql, String key, String value) {
    // TODO Auto-generated method stub

    dao.executeBySql(sql);
    //    if ("App_Curr_Region".equalsIgnoreCase(key)) {
    //      String strSql = "select DISTINCT T.TABLE_NAME from user_tables t,user_tab_columns c where t.table_name=c.TABLE_NAME and c.COLUMN_NAME='RG_CODE'";
    //      List list = dao.findBySql(strSql);
    //      String updateSql = "";
    //      Map map = null;
    //      if (list != null) {
    //        for (int i = 0; i < list.size(); i++) {
    //          map = (Map) list.get(i);
    //          updateSql = "update " + map.get("table_name") + " set rg_code=" + value;
    //          dao.executeBySql(updateSql);
    //        }
    //      }
    //    }

    /**
     * add by fengdz 2013.03.26 修改参数后不存在清空缓存而是修改缓存 Begin
     */
    // globalParaMap = new HashMap();
    //
    // SessionUtil.refleshParaMap();
    refleshParaCachedMap(key, value);
    /**
     * add by fengdz 2013.03.26 修改参数后不存在清空缓存而是修改缓存 End
     */

  }

  /**
   * 
   * <strong>Description:</strong><br>
   * <br>
   * 
   * @author fengdz
   * @date Aug 15, 2013 2:15:00 PM
   * @param is
   * @return
   * @throws SQLException
   */
  private void refleshParaCachedMap(String key, String value) {
    String setYear = SessionUtil.getLoginYear();
    String rgCode = SessionUtil.getRgCode();

    // 更新全局缓存
    if (!globalParaMapAllRG.isEmpty() && globalParaMapAllRG.size() > 0) {
      ((Map) globalParaMapAllRG.get(setYear.trim().concat(rgCode.trim()))).put(key, value);
    }

  }

  /**
   * 批量获取系统参数，其中将webpath不需要参数传递，直接查询传出传递
   * @param params 对应的设置参数名
   * @return
   */
  public Map getParams(String[] params) {
    Map map = SessionUtil.getParaMap();
    Map paraMap = new HashMap();
    for (int i = 0, n = params.length; i < n; i++) {
      Object param = map.get(params[i]);
      paraMap.put(params[i], param == null ? "" : param);
    }
    paraMap.put("webpath", getWebPath());
    return paraMap;
  }

  public String getPara(String paraCode, String setCode, String rg_code, String set_year) {
    Map ParaMapRG = (Map) globalParaMapAllRG.get(set_year + rg_code);
    return (String) ParaMapRG.get(paraCode + (setCode == null ? "" : setCode));
  }

}
