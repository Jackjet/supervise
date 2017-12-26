package gov.df.fap.api.util.paramanage;

import java.util.List;
import java.util.Map;

public interface IParaManage {
  public List getAllEnabledSysPara(String sysId);

  public List getGroupCount(String sysId);

  public String getPara(String paraCode, String setCode);

  public String getPara(String paraCode, String setCode, String rg_code, String set_year);

  public void setPara(String paraCode, String paraValue, String setCode);

  public Map getGlobalParaMap();

  public String getNumber();

  public String getWebPath();

  //获取数据库的时间 update 
  public String getDatabaseTime();

  //获取服务器的时间
  public String getServerTime();

  /**
   * @author -czh add
   * @param sys_id,子系统Id
   * @return 根据子系统Id 取出子系统名称
   */
  public List getSysNamebySysId(String sys_id);

  /**
   * @author -ymj
   * @param String sql
   * 更新参数
   */
  public void updateParam(String sql, String key, String value);

  /**
   * 批量获取系统参数，其中将webpath不需要参数传递，直接查询传出传递
   * @param params 对应的设置参数名
   * @return
   */
  public Map getParams(String[] params);
}
