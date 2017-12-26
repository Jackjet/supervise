package gov.df.fap.service.role;

import gov.df.fap.api.fapcommon.IUserSync;
import gov.df.fap.api.role.IRoleDfService;
import gov.df.fap.bean.user.SysUserRoleRule;
import gov.df.fap.bean.user.UserDTO;
import gov.df.fap.service.util.dao.GeneralDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleDfService {

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO generalDAO;

  @Autowired
  private IUserSync iUserSyncManager;

  @Autowired
  private gov.df.fap.api.fapcommon.IRoleDfCommonService iRoleService;

  public RoleService() {

  }

  public Map<String, Object> getAllRole(HttpServletRequest request, HttpServletResponse response) {
    String tokenId = request.getParameter("tokenid");
    Map<String, Object> map = new HashMap<String, Object>();

    try {
      UserDTO userdto = (UserDTO) iUserSyncManager.getUser(tokenId);
      String guid = userdto.getUser_id();
      String sql = "  fasp_t_carole.status = '1' and remark1 = 'df'  order  by fasp_t_carole.code asc";
      List roleList = iRoleService.queryRolesBySql(sql);
      map.put("rolelist", roleList);
      map.put("flag", 1);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      map.put("flag", 0);
    }
    return map;
  }

  public Map<String, Object> getAllRole() {
    Map<String, Object> map = new HashMap<String, Object>();
    UserDTO userdto = null;
    try {
      String sql = "  fasp_t_carole.status = '1' and remark1 = 'df'  order  by fasp_t_carole.code asc";
      List roleList = iRoleService.queryRolesBySql(sql);
      map.put("rolelist", roleList);
      map.put("flag", 1);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("flag", 0);
    }
    return map;
  }

  public List queryRolesByUserId(String userid) {
    StringBuffer sql = new StringBuffer();
    sql
      .append("select t.user_id, t.role_id, t.role_name,t.rg_code, t.set_year from sys_user_role_rule t where t.user_id = ?");
    return generalDAO.findBeanBySql(sql.toString(), new String[] { userid }, SysUserRoleRule.class);
  }
}
