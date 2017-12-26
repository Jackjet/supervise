package gov.df.fap.service.resource;

import gov.df.fap.api.fapcommon.IUserSync;
import gov.df.fap.api.resource.IResourceService;
import gov.df.fap.bean.user.UserDTO;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 资源相关操作--资源相关操作使用
 * @author hp
 * @version 2017-3-15
 */
@Service
public class ResourceService implements IResourceService {

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO generalDAO;

  @Autowired
  private IUserSync iUserSyncManager;

  public ResourceService() {

  }

  public Map<String, Object> addResourceMenu(HttpServletRequest request, HttpServletResponse response) {

    String btnCode = request.getParameter("btnCode");
    String btnName = request.getParameter("btnName");
    String btnRemark = request.getParameter("btnRemark");
    String menuGuid = request.getParameter("menuGuid");
    Map<String, Object> map = new HashMap<String, Object>();
    UUID uuid = UUID.randomUUID();
    try {
      String sql = "insert into SYS_MENU_BUTTON (BTN_ID,BTN_NAME,MENU_ID,BTN_CODE,REMARK) values(?,?,?,?,?)";
      generalDAO.executeBySql(sql, new Object[] { uuid.toString(), btnName, menuGuid, btnCode, btnRemark });
      map.put("flag", 1);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      map.put("flag", 0);
    }

    return map;
  }

  public Map<String, Object> delResourceMenu(HttpServletRequest request, HttpServletResponse response) {
    String selguid = request.getParameter("selguid");
    String[] selAllGuid = selguid.split(",");
    Map<String, Object> map = new HashMap<String, Object>();
    String inSql = "('',";
    for (int i = 0; i < selAllGuid.length; i++) {
      inSql = inSql + "'" + selAllGuid[i] + "'" + ",";
    }
    inSql = inSql + "'')";
    String sql = "delete from SYS_MENU_BUTTON where BTN_ID in " + inSql;
    try {
      int k = generalDAO.executeBySql(sql, new Object[] {});
      map.put("num", k);
      map.put("flag", 1);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      map.put("flag", 0);
    }

    return map;
  }

  public Map<String, Object> changeResRemark(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String btnRemark = request.getParameter("btnRemark");
    String btn_id = request.getParameter("btn_id");
    String type = null;
    if ("1".equals(btnRemark)) {
      type = "0";
    } else {
      type = "1";
    }
    try {
      String sql = "update SYS_MENU_BUTTON t set t.remark = ? where t.btn_id = ?";
      int k = generalDAO.executeBySql(sql, new Object[] { type, btn_id });
      map.put("num", k);
      map.put("flag", 1);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      map.put("flag", 0);
    }
    return map;
  }

  public Map<String, Object> queryResByRole(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String guid = request.getParameter("guid");
    String roleid = request.getParameter("role_id");
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String sql = "select t.btn_id, t.btn_name, a.role_id from sys_role_menu_button a, sys_menu_button t"
      + " where t.remark = '1' and t.menu_id = ?  and t.menu_id = a.menu_id(+)  and t.btn_id = a.button_id(+)  and a.role_id(+)=? "
      + " and a.rg_code(+) = ? and a.set_year(+) = ? order by t.btn_id";
    List list = generalDAO.findBySql(sql, new Object[] { guid, roleid, rg_code, year });
    map.put("btnlist", list);

    return map;
  }

  public Map<String, Object> changeResCheck(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String btnid = request.getParameter("btnid");
    String tokenId = request.getParameter("tokenid");
    String roleid = request.getParameter("role_id");
    String menuid = request.getParameter("menu_id");
    String checked = request.getParameter("checked");
    try {
      UserDTO userdto = (UserDTO) iUserSyncManager.getUser(tokenId);
      String year = userdto.getSet_year();
      String rg_code = userdto.getRg_code();
      if ("true".endsWith(checked)) {
        String insql = "insert into sys_role_menu_button (role_id,button_id,set_year,menu_id,last_ver,rg_code) values(?,?,?,?,?,?)";
        int k = generalDAO.executeBySql(insql, new Object[] { roleid, btnid, year, menuid, "", rg_code });
        map.put("num", k);
        map.put("flag", 1);
      } else {
        String delSql = "delete from sys_role_menu_button t where t.role_id=? and t.button_id = ? and t.set_year = ? and t.menu_id = ? and t.rg_code = ? ";
        int k = generalDAO.executeBySql(delSql, new Object[] { roleid, btnid, year, menuid, rg_code });
        map.put("num", k);
        map.put("flag", 1);
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return map;
  }

}
