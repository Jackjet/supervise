package gov.df.fap.api.framework;

import gov.df.fap.bean.sysdb.SysRegion;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录前得取系统信息操作
 */
public interface IPreLogin {
  List queryBusiYear() throws Exception;

  List getUserNameByCode(String userCode, String rgCode, String setYear) throws Exception;

  public String getSysIdByNoStartApp();

  String getSystemNameById(String sysid) throws Exception;

  public String getUserSysLoginImgById(String userSysId) throws Exception;

  public String getSysPara(String paraCode, String setCode);

  public String getSysPara(String paraCode, String setCode, String rg_code, String set_year);

  public String getUserIdbyCode(String userCode);

  public String getUserIdbyCodeYear(String userCode, Integer setYear);

  public byte[] getFileAtServer(String url) throws IOException;

  public Map getValueFromSysapp(String sysid);

  /**
   * 先获取总帐的bean注入，如果为空，取全部帐套
   * 
   * @param userId
   *            用户id
   * @param year
   *            选择的年份
   * @return List（XMLData字段符合表ele_book_set） 返回帐套信息
   * @throws Exception
   *             抛出sql错误
   * @author 
   */
  public List getBookSetByUserId(String userId, int year) throws Exception;

  /**
   * 先获取总帐的bean注入，如果为空，去全部帐套，否则先返回没有数据的list
   * 
   * @param year
   *            传入的年份，取全部帐套用
   * @return List（XMLData字段符合表ele_book_set） 返回帐套信息
   * @throws Exception
   *             抛出sql错误
   * @author 
   */
  public List queryBookSet(int year) throws Exception;

  /**
   * 查询登录时的所有启用区划
   * 
   * @return List
   * @throws Exception
   */
  public List queryRegion() throws Exception;

  /**
   * 判断是否需要设置顶级区划
  * @return
   */
  public SysRegion getInitAppCurRegion();

  /**
   * 用户登录三级改造 Begin
   */

  public abstract HashMap lockedUserAccount(String user_id, String setYear, String rg_code, int errorNum,
    String userName);

  public abstract HashMap checkUserAccount(String user_id, String setYear, String rg_code);

  // 通过年度和区划查找是否存在这个数据源
  public abstract boolean isExistsSourceByRgAndYear(String year, String rgCode) throws Exception;
  
  /**
	 * 清除记录，清除sys_session表所有满足条件{login_date>当前时间-1天}的记录
	 * 
	 * @author 黄节2007年9月15日添加
	 */
  public void removeInvalidateSession();

}
