package gov.df.fap.service.fapcommon;

import gov.df.fap.api.fapcommon.IUserSync;
import gov.df.fap.bean.portal.UserInfoDFCommon;
import gov.df.fap.bean.user.UserDTO;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.factory.ServiceFactory;
import gov.mof.fasp2.ca.user.service.IUserService;
import gov.mof.fasp2.sec.syncuserinfo.UserInfo;
import gov.mof.fasp2.sec.syncuserinfo.manager.IUserSyncManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;

import com.longtu.framework.cache.exceptions.NoPrivilegeException;
import com.longtu.framework.exception.AppException;

@Service
public class UserCommonService implements IUserSync {

  private IUserService userService = null;

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO generalDAO;

  @Autowired
  private AbstractRefreshableWebApplicationContext abstractRefreshableWebApplicationContext;

  private static String starttype = "df";

  public List findAllUsers() {
    List list = new ArrayList();
    starttype = abstractRefreshableWebApplicationContext.getServletContext().getInitParameter("startup");
    if ("fasp2.0".equals(starttype)) {
      userService = (IUserService) ServiceFactory.getBean("fasp2.ca.user.service");
    }

    if ("df".equals(starttype)) {
      String sql = "select t.user_id guid , t.user_code code , t.user_name name ,t.* from sys_usermanage t ";
      list = generalDAO.findBySql(sql);
    } else if ("fasp2.0".equals(starttype)) {
      try {
        list = userService.findAllUsers();
        // 只查询 启用状态的数据
        list = userService.getValidUserByIDCode(" 1=1");
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("guid", "-1");
        map2.put("name", "管理类用户");
        map2.put("usertype", 9999);
        list.add(map2);

        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("guid", "1");
        map3.put("name", "财政类用户");
        map3.put("usertype", 9999);
        list.add(map3);

        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("guid", "0");
        map4.put("name", "单位类用户");
        map4.put("usertype", 9999);
        list.add(map4);

        Map<String, Object> map5 = new HashMap<String, Object>();
        map5.put("guid", "4");
        map5.put("name", "银行类用户");
        map5.put("usertype", 9999);
        list.add(map5);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return list;
  }

  @Override
  public Object getUser(String tokenId) {
    starttype = abstractRefreshableWebApplicationContext.getServletContext().getInitParameter("startup");
    if ("fasp2.0".equals(starttype)) {
      UserDTO tmpuser = new UserDTO();
      IUserSyncManager userServicefasp = (IUserSyncManager) ServiceFactory.getBean("fasp.ca.UserSyncManager");
      UserInfo userinfo;
      try {
        userinfo = (UserInfo) userServicefasp.getUser(tokenId);
        tmpuser.setUser_code(userinfo.getUser().getCode());
        tmpuser.setUser_id(userinfo.getUser().getGuid());
        tmpuser.setUser_name(userinfo.getUser().getName());
        tmpuser.setPassword(userinfo.getUser().getPassword());
        tmpuser.setSet_year(String.valueOf(userinfo.getUser().getYear()));
        tmpuser.setRg_code(userinfo.getUser().getProvince());
      } catch (NoPrivilegeException e) {
        e.printStackTrace();
      } catch (AppException e) {
        e.printStackTrace();
      }
      return tmpuser;
    } else {
      return UserInfoDFCommon.getUser(tokenId);
    }
  }

  @Override
  public boolean doLogout(String tokenId) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Object login(String account, String password, Integer year, String province) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String doLogin(Object userdto, String year, String province) {
    // TODO Auto-generated method stub
    return null;
  }

  public List getUserOrg(String userId) {
    List list = new ArrayList();
    starttype = abstractRefreshableWebApplicationContext.getServletContext().getInitParameter("startup");
    if ("fasp2.0".equals(starttype)) {
      userService = (IUserService) ServiceFactory.getBean("fasp2.ca.user.service");
    }

    if ("df".equals(starttype)) {
      //暂未实现
    } else if ("fasp2.0".equals(starttype)) {
      try {
        String orgType = null;
        String userType = "";
        List userTypeList = generalDAO.findBySql(
          "select org_type from SYS_USER_ORGTYPE su where su.user_id=? and su.set_year=? ", new String[] { userId,
            SessionUtil.getLoginYear() });
        if (userTypeList != null && userTypeList.size() > 0) {
          userType = (String) ((Map) userTypeList.get(0)).get("org_type");
        }
        orgType = StringUtil.isNull(userType) ? null : userType;
        switch (Integer.parseInt(orgType)) {
        case 1://新增管理员
          System.out.println("管理员");
          break;
        case 2: //单位 
          list = generalDAO.findBySql(QUERYAGENCYUSERORG, new String[] { SessionUtil.getLoginYear(), userId });
          break;
        case 7: //处室
          list = generalDAO.findBySql(QUERYMBUSERORG, new String[] { SessionUtil.getLoginYear(), userId });
          break;
        default:
          System.out.println("空");
          break;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return list;
  };

  private static String QUERYAGENCYUSERORG = "select chr_id,chr_code,chr_name,set_year,rg_code,parent_id from VW_FASP_AGENCY agency where agency.SET_YEAR=? and exists (select 1 from SYS_USER_ORG su where  su.set_year=agency.SET_YEAR and su.org_id=agency.CHR_ID and su.user_id=? )";

  private static String QUERYMBUSERORG = "select chr_id,chr_code,chr_name,set_year,rg_code,parent_id from VW_FASP_MANAGE_BRANCH agency where agency.SET_YEAR=? and exists (select 1 from SYS_USER_ORG su where  su.set_year=agency.SET_YEAR and su.org_id=agency.CHR_ID and su.user_id=? )";;
}
