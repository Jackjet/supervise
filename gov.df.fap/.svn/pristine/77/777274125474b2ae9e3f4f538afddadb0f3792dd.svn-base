package gov.df.fap.api.fapcommon;

import java.util.List;

/**
 * 用户相关接口
 * @author Administrator
 *
 */
public interface IUserSync {

  List findAllUsers();

  Object getUser(String tokenId);

  boolean doLogout(String tokenId);

  Object login(String account, String password, Integer year, String province);

  //生成tokenid
  String doLogin(Object userdto, String year, String province);

  //根据用户ID获取用户机构权限
  public List getUserOrg(String userId);
}
