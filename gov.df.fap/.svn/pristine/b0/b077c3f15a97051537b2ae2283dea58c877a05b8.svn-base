package gov.df.fap.service.menu;

import gov.df.fap.api.menu.IStatusService;
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
public class StatusService implements IStatusService {
  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO generalDAO;

  public List getAllStatus() {
    String sql = "select t.*," + SQLUtil.replaceLinkChar("status_code || ' ' || t.status_name ")
      + " name from sys_status t order by t.status_code";
    List list = generalDAO.findBySql(sql);
    return list;
  }

  public Map<String, Object> insertMenuStatues(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String menu_id = request.getParameter("menu_id");
    String status = request.getParameter("status");
    String belmenu = request.getParameter("belmenu");
    String belname = request.getParameter("belname");
    String sql2 = "select * from sys_menu_status t where t.menu_id = ? and t.status_id =? and t.rg_code = ? and t.set_year = ?";
    List list = generalDAO.findBySql(sql2, new Object[] { menu_id, status, rg_code, set_year });
    if (list.size() > 0) {
      map.put("flag", "0");
      map.put("message", "状态已存在");
      return map;
    }
    String sql = "insert into sys_menu_status(guid,menu_id,status_id,belone_menu,set_year,rg_code,belone_menu_name,last_ver)values("
      + (TypeOfDB.isOracle() ? "newid" : " CONCAT('{', UPPER(UUID()), '}') ")
      + ",?,?,?,?,?,?,"
      + SQLUtil.getSysdateToCharSql() + ")";
    generalDAO.executeBySql(sql, new Object[] { menu_id, status, belmenu, set_year, rg_code, belname });
    map.put("flag", "1");
    return map;
  }

  public Map<String, Object> updateMenuStatues(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String menu_id = request.getParameter("menu_id");
    String status = request.getParameter("status");
    String belmenu = request.getParameter("belmenu");
    String belname = request.getParameter("belname");
    String guid = request.getParameter("guid");
    String sql2 = "select * from sys_menu_status t where t.menu_id = ? and t.status_id =? and t.rg_code = ? and t.set_year = ? and t.guid <> ?";
    List list = generalDAO.findBySql(sql2, new Object[] { menu_id, status, rg_code, set_year, guid });
    if (list.size() > 0) {
      map.put("flag", "0");
      map.put("message", "状态已存在");
      return map;
    }
    String sql = "update sys_menu_status t set t.menu_id = ? , t.status_id = ? ,t.belone_menu = ?,t.belone_menu_name = ?,t.last_ver = "
      + SQLUtil.getSysdateToCharSql() + " where t.guid = ? and t.set_year = ?";
    generalDAO.executeBySql(sql, new Object[] { menu_id, status, belmenu, belname, guid, set_year });
    map.put("flag", "1");
    return map;
  }

  public Map<String, Object> StatuesGrid(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String menu_id = request.getParameter("menu_id");
    String sql = "select c.status_name,c.status_code,t.belone_menu_name menu_name ,t.menu_id , t.status_id , t.belone_menu , t.guid from sys_status c,  sys_menu_status t "
      + "where  t.status_id = c.status_id and t.rg_code = ? and t.set_year = ?  and t.menu_id = ? order by c.status_code";
    List list = generalDAO.findBySql(sql, new Object[] { rg_code, set_year, menu_id });
    map.put("statusgrid", list);
    return map;
  }

  public Map<String, Object> delStatus(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String guid = request.getParameter("guid");
    String sql = "delete sys_menu_status t where t.guid = ? and t.set_year = ?";
    generalDAO.executeBySql(sql, new Object[] { guid, set_year });
    map.put("flag", "1");
    return map;
  }

  public Map<String, Object> menuStatusTree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String menu_id = request.getParameter("menu_id");
    String role_id = request.getParameter("role_id");
    map.put("flag", "1");
    String sql = "select a.status_id ,"
      + SQLUtil.replaceLinkChar("a.status_code ||' '|| a.status_name")
      + " name , '0' pid  from sys_status a , sys_menu_status t where a.status_id = t.status_id and  t.menu_id = ? and t.rg_code = ? and t.set_year = ? order by a.status_code";
    List list = generalDAO.findBySql(sql, new Object[] { menu_id, rg_code, set_year });
    if (list.size() == 0) {
      Map map1 = new HashMap<String, Object>();
      map1.put("status_id", "#");
      map1.put("name", "默认初始状态");
      map1.put("pid", "0");
      list.add(map1);
      map.put("flag", "0");
    }
    map.put("statree", list);
    String sql1 = "select t.status_id from SYS_ROLE_MENU_STATUS t where t.role_id = ? and t.menu_id = ? and t.rg_code = ? and t.set_year = ? ";
    List list1 = generalDAO.findBySql(sql1, new Object[] { role_id, menu_id, rg_code, set_year });
    map.put("check", list1);

    return map;
  }

  public Map<String, Object> insertmenuStatus(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String menu_id = request.getParameter("menu_id");
    String role_id = request.getParameter("role_id");
    String status_id = request.getParameter("status_id");
    String sql = "insert into sys_role_menu_status(role_id,menu_id,status_id,set_year,rg_code,last_ver) values (?,?,?,?,?,"
      + SQLUtil.getSysdateToCharSql() + ")";
    generalDAO.executeBySql(sql, new Object[] { role_id, menu_id, status_id, set_year, rg_code });
    map.put("flag", "1");
    return map;
  }

  public Map<String, Object> delmenuStatus(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String menu_id = request.getParameter("menu_id");
    String role_id = request.getParameter("role_id");
    String status_id = request.getParameter("status_id");
    String sql = "delete sys_role_menu_status t where t.role_id = ? and t.menu_id=? and t.status_id=? and t.rg_code = ? and t.set_year =? ";
    generalDAO.executeBySql(sql, new Object[] { role_id, menu_id, status_id, rg_code, set_year });
    map.put("flag", "1");
    return map;
  }
}
