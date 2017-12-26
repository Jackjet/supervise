package gov.df.fap.api.login;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IUsersLogin {

  public Map<String, Object> loginsend(HttpServletRequest request, HttpServletResponse response);

  public Map<String, Object> loginMessage(HttpServletRequest request, HttpServletResponse response);

  public Map<String, Object> loginout(HttpServletRequest request, HttpServletResponse response);

  String doLogin(gov.df.fap.bean.user.UserDTO userdtoDF, String yearDF, String province, String oid,
    HttpServletRequest req, HttpServletResponse resp);

}
