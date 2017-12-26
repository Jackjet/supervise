package gov.df.fap.service.menu;

import gov.df.fap.api.menu.IBtnService;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.SQLUtil;
import gov.df.fap.service.util.datasource.TypeOfDB;
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
public class BtnService implements IBtnService {
  @Autowired
  TypeOfDB typeOfDB;

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO generalDAO;

  public Map<String, Object> insertMenuBtn(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String action_id = request.getParameter("action_id");
    String menu_id = request.getParameter("menu_id");

    String sql = "insert into SYS_MENU_ACTION(chr_id,ACTION_ID,MENU_ID,RG_CODE,SET_YEAR,LAST_VER) values ("
      + (TypeOfDB.isOracle() ? "newid" : " CONCAT('{', UPPER(UUID()), '}') ") + " ,?,?,?,?,"
      + SQLUtil.getSysdateToCharSql() + ")";
    generalDAO.executeBySql(sql, new Object[] { action_id, menu_id, rg_code, set_year });
    map.put("flag", "1");
    return map;
  }

  public Map<String, Object> BtnGrid(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String menu_id = request.getParameter("menu_id");
    String sql = "select t.chr_id , a.action_code ,a.action_name,a.func_name,a.param from sys_action a , SYS_MENU_ACTION t "
      + "where a.action_id = t.action_id and t.menu_id = ? and t.rg_code = ? and t.set_year = ? order by a.action_code";
    List list = generalDAO.findBySql(sql, new Object[] { menu_id, rg_code, set_year });
    map.put("btngrid", list);
    map.put("flag", "1");
    return map;
  }

  public Map<String, Object> BtnTree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String sql = "select t.action_id guid, " + SQLUtil.replaceLinkChar("t.action_code || ' ' ||t.action_name")
      + " name , t.sys_id parentid from sys_action t  order by t.action_code ";
    List list = generalDAO.findBySql(sql);
    String sql2 = "select t.sys_id guid  ," + SQLUtil.replaceLinkChar("t.sys_id || ' '||t.sys_name")
      + " name , '0' parentid from sys_app t where t.enabled = '1' order by t.sys_id ";
    List list2 = generalDAO.findBySql(sql2);
    list.addAll(list2);
    map.put("btntree", list);
    return map;
  }

  public Map<String, Object> delBtn(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String chr_id = request.getParameter("chr_id");
    String sql = "delete SYS_MENU_ACTION t where t.chr_id = ? and t.set_year = ?";
    generalDAO.executeBySql(sql, new Object[] { chr_id, set_year });
    map.put("flag", "1");
    return map;
  }

  public Map<String, Object> BtnExist(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String menu_id = request.getParameter("menu_id");
    String action_id = request.getParameter("action_id");
    String sql = "select * from SYS_MENU_ACTION t where t.menu_id = ? and t.action_id = ? and t.rg_code = ? and t.set_year =?";
    List list = generalDAO.findBySql(sql, new Object[] { menu_id, action_id, rg_code, set_year });
    if (list.size() > 0) {
      map.put("flag", "0");
    } else {
      map.put("flag", "1");
    }
    return map;

  }

  public Map<String, Object> BtnCheck(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String menu_id = request.getParameter("menu_id");
    String status_id = request.getParameter("status_id");
    String role_id = request.getParameter("role_id");
    String sql = "select c.chr_id , t.action_id ,  "
      + SQLUtil.replaceLinkChar("a.action_code||' '||a.action_name")
      + " name  from sys_action a join sys_menu_action t on a.action_id = t.action_id "
      + " left join sys_role_menu_status_button c on c.button_id = t.action_id and c.menu_id = t.menu_id and c.rg_code=t.rg_code and c.set_year = t.set_year and c.role_id = ? and c.status_id = ? "
      + " where t.menu_id = ? and t.rg_code = ? and t.set_year = ? order by a.action_code ";
    List list = generalDAO.findBySql(sql, new Object[] { role_id, status_id, menu_id, rg_code, set_year });
    map.put("btncheck", list);
    return map;
  }

  public Map<String, Object> insertBtnCheck(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String menu_id = request.getParameter("menu_id");
    String status_id = request.getParameter("status_id");
    String role_id = request.getParameter("role_id");
    String action_id = request.getParameter("action_id");
    String sql = "insert into sys_role_menu_status_button(chr_id,role_id,menu_id,status_id,button_id,set_year,rg_code,last_ver) values ("
      + (TypeOfDB.isOracle() ? "newid" : " CONCAT('{', UPPER(UUID()), '}') ")
      + " ,?,?,?,?,?,?,"
      + SQLUtil.getSysdateToCharSql() + ")";
    generalDAO.executeBySql(sql, new Object[] { role_id, menu_id, status_id, action_id, set_year, rg_code });
    return map;
  }

  public Map<String, Object> delBtnCheck(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String chr_id = request.getParameter("chr_id");
    String sql = " delete sys_role_menu_status_button t where t.chr_id = ? and t.set_year = ?";
    generalDAO.executeBySql(sql, new Object[] { chr_id, set_year });
    return map;
  }
}
