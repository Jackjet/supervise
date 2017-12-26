package gov.df.fap.service.button;

import gov.df.fap.api.button.IButtonConfigService;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ButtonConfigService implements IButtonConfigService {

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO generalDAO;

  public Map<String, Object> initBtnTree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String sys_id = request.getParameter("sys_id");
    String sql = " select t.sys_id , t.sys_id || ' ' || t.sys_name name , '0' pid from sys_app t  where t.enabled = '1'  order by t.sys_id ";
    List list = generalDAO.findBySql(sql);
    Map<String, Object> map1 = new HashMap<String, Object>();
    map1.put("sys_id", "0");
    map1.put("name", "全部");
    map1.put("parent_id", "");
    list.add(map1);
    map.put("datatree", list);
    return map;
  }

  public Map<String, Object> BtnGrid(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String sys_id = request.getParameter("sys_id");
    if ("0".equals(sys_id)) {
      String sql = " select t.action_id , t.action_code , t.action_name ,t.enabled , t.func_name ,t.param ,"
        + "t.sys_id ,t.icon_name from sys_action t where t.rg_code = ? and t.set_year = ?  order by t.sys_id , t.action_code ";
      List list = generalDAO.findBySql(sql, new Object[] { rg_code, year });
      map.put("griddata", list);
    } else {
      String sql = " select t.action_id , t.action_code , t.action_name ,t.enabled , t.func_name ,t.param ,"
        + "t.sys_id ,t.icon_name from sys_action t where t.rg_code = ? and t.set_year = ? and t.sys_id = ?  order by t.sys_id , t.action_code";
      List list = generalDAO.findBySql(sql, new Object[] { rg_code, year, sys_id });
      map.put("griddata", list);
    }
    return map;
  }

  public Map<String, Object> insertBtn(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String sys_id = request.getParameter("sys_id");
    String action_code = request.getParameter("action_code");
    String action_name = request.getParameter("action_name");
    String func_name = request.getParameter("func_name");
    String enabled = "1";
    String param = request.getParameter("param");
    String icon_name = request.getParameter("icon_name");
    String sql = "insert into sys_action ( action_id,action_code,action_name,enabled,func_name,param,sys_id,rg_code,set_year,icon_name,last_ver)"
      + "values(newid,?,?,?,?,?,?,?,?,?,to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'))";
    generalDAO.executeBySql(sql, new Object[] { action_code, action_name, enabled, func_name, param, sys_id, rg_code,
      set_year, icon_name });
    map.put("flag", "1");
    return map;
  }

  public Map<String, Object> updateBtn(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String sys_id = request.getParameter("sys_id");
    String action_id = request.getParameter("action_id");
    String action_code = request.getParameter("action_code");
    String action_name = request.getParameter("action_name");
    String func_name = request.getParameter("func_name");
    String enabled = "1";
    String param = request.getParameter("param");
    String icon_name = request.getParameter("icon_name");
    String sql = "update sys_action t set t.action_code = ? ,t.action_name =? ,t.enabled=?,t.func_name=?,t.param=?,t.sys_id=?,t.icon_name =? where t.action_id = ? and t.rg_code = ? and t.set_year = ?";
    generalDAO.executeBySql(sql, new Object[] { action_code, action_name, enabled, func_name, param, sys_id, icon_name,
      action_id, rg_code, set_year });
    map.put("flag", "1");
    return map;
  }

  public Map<String, Object> delBtn(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String acIdlist = request.getParameter("acIdlist");
    String[] action_id = acIdlist.split("@");
    String insql = "(";
    for (int i = 0; i < action_id.length; i++) {
      insql = insql + "'" + action_id[i] + "',";
    }
    insql = insql + "'')";
    String sql = "delete sys_action t where t.action_id in " + insql + " and t.rg_code = ? and t.set_year = ?";
    int cout = generalDAO.executeBySql(sql, new Object[] { rg_code, set_year });
    map.put("flag", "1");
    map.put("cout", cout);
    return map;
  }

}
