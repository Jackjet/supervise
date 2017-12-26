package gov.df.supervise.service.user;

import gov.df.supervise.api.user.user;
import gov.df.supervise.bean.user.depDTO;
import gov.df.supervise.bean.user.officeDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  用户登录前后台获取公共参数和菜单
 * 
 * @author tongya
 *
 */
@Service
public class userBO implements user {
  @Autowired
  private userDao userdao;

  //通过oid获取officeDTO实体的数据
  public officeDTO queryOfficeByOrgCode(String oid) {
    //String oid = (String) SessionUtil.getUserInfoContext().getOrgCode();//获取org_code作为专员办id(oid)
    return userdao.queryOfficeByOrgCode(oid);
  }

  //通过dep_id获取depDTO实体的数据
  public depDTO queryDepByBelongOrg(String dep_id) {
    // String dep_id = (String) SessionUtil.getUserInfoContext().getBelongOrg();//获取org_code作为专员办id(oid)
    return userdao.queryDepByBelongOrg(dep_id);
  }

  //动态获取菜单
  public List getMenu(String user_id, String oid, String menu_id) {
    Map<String, Object> param = new HashMap<String, Object>();
    param.put("user_id", user_id);
    param.put("oid", oid);
    return userdao.getMenu(param);
  }
}
