package gov.df.supervise.service.upgrade;

import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.portal.EncryptUtil;
import gov.df.supervise.api.upgrade.csofUpgradeService;
import gov.df.supervise.service.common.SessionUtilEx;
import gov.df.supervise.service.document.csofDocumentDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 补丁升级
 * @author tongya
 *
 */
@Service
public class csofUpgradeBO implements csofUpgradeService {
  @Autowired
  private csofUpgradeDao csofUpgradeDao;

  @Autowired
  private csofDocumentDao csofDocumentDao;

  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 校验补丁升级密码
   * @return
   */
  public Map<String, Object> getUpdatePswd(String usercode, String pwd) {

    //    StringBuffer strSQL = new StringBuffer("select chr_value from sys_userpara" + Tools.addDbLink()
    //      + " where  chr_code = 'UPDATE_PSWD' ");
    //    List list = dao.findBySql(strSQL.toString());
    //    XMLData data = (XMLData) list.get(0);
    //    return (String) data.get("chr_value");
    Map<String, Object> result = new HashMap<String, Object>();
    String password = null;
    int year = Integer.parseInt(SessionUtilEx.getLoginYear());
    String rg_code = SessionUtilEx.getRgCode();
    String oid = (String) SessionUtil.getUserInfoContext().getOrgCode();//获取org_code作为专员办id(oid)
    StringBuilder sql = new StringBuilder();
    List<Object> list = new ArrayList<Object>();
    sql.append("select * from sys_usermanage where user_code='" + usercode + "' ");
    sql.append(rg_code != null ? " and rg_code='" + rg_code + "' " : " ");
    sql.append(oid != null ? " and org_code='" + oid + "' " : " ");
    sql.append(year != 0 ? " and set_year='" + year + "' " : " ");
    list = dao.findBySql(sql.toString());
    if (list.size() > 0) {
      Map<String, Object> data = (Map<String, Object>) list.get(0);
      password = (String) data.get("password");
    }
    // 密码匹配
    if (!StringUtil.isNull(password) && (password.length() == 32) && (pwd != null) && (pwd.length() != 32)) {
      pwd = EncryptUtil.hash(pwd);
    }
    if ((!(password.equals(pwd))) && (!(EncryptUtil.hash(password).equals(pwd)))) {
      try {
        result.put("errorCode", "-1");
        result.put("errorMsg", "密码错误，");
        throw new Exception("11010003 密码错误!");
      } catch (Exception e) {
      }
    } else {
      result.put("errorCode", "0");
      result.put("errorMsg", "密码正确，");
    }
    return result;

  }

  /**
   * 查询补丁列表
   */
  public List getUpgrade() {
    return csofUpgradeDao.getUpgrade();
  }

  /**
   * 通过条件查询补丁列表
   */
  public List getUpgradeByIscommit(int iscommit) {
    return csofUpgradeDao.getUpgradeByIscommit(iscommit);
  }

  /**
   * 删除待升级的补丁
   */
  public void deleteUpgrade(String id) {
    csofUpgradeDao.deleteUpgrade(id);
  }

  /**
   * 执行升级补丁
   * @throws Exception 
   * 
   */
  public void doUpgrade(String id, String filePath) {
    Map resultMap = new HashMap<Object, Object>();
    Map param = new HashMap<Object, Object>();
    try {
      dao.execSqlFile(filePath);
      param.put("id", id);
      param.put("iscommit", 1);
      param.put("is_now", 1);
      csofUpgradeDao.doUpgrade(param);
    } catch (Exception e) {
      e.printStackTrace();
      resultMap.put("message", "异常"); // zfn 未来改成标准消息异常
      resultMap.put("error", 1);
      resultMap.put("message", "执行发生异常");
    }
  }

  /**
   * 上传
   */
  public void saveUpgrade(String id, String remark) {
    Map<String, Object> param = new HashMap<String, Object>();
    String user_id = (String) SessionUtil.getUserInfoContext().getUserID();
    String date = Tools.getCurrDate();
    String sql_id = csofDocumentDao.getFileId(id);
    String app_version = csofUpgradeDao.getNameById(id);
    param.put("id", id);
    param.put("app_version", app_version);
    param.put("sql_id", sql_id);
    param.put("iscommit", 0);
    param.put("remark", remark);
    param.put("modular", "SYS");
    param.put("create_user", user_id);
    param.put("create_date", date);
    param.put("latest_op_user", user_id);
    param.put("latest_op_date", date);
    param.put("is_now", 0);
    param.put("sys_id", 105);
    csofUpgradeDao.saveUpgrade(param);
  }

}
