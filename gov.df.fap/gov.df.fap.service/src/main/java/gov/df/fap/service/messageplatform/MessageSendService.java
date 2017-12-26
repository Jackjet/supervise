package gov.df.fap.service.messageplatform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gov.df.fap.api.messageplatform.IMessageSend;
import gov.df.fap.service.messageplatform.dao.MsgSettingDao;
import gov.df.fap.service.util.dao.GeneralDAO;

@Service
public class MessageSendService implements IMessageSend {

  //日志
  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao;
  
  @Autowired
  private MsgSettingDao msgSettingDao;
  
  //工具类 （获取token和发送http请求）
  private IMAPIHelperUtil imHelperUtil=IMAPIHelperUtil.getSingletonIMAPIHelperUtil();
  
  @Override
  public JSONObject getSystemMessageByUserId(String userId) {
    // TODO Auto-generated method stub
    //https://im.yyuap.com/sysadmin/rest/version/{etpId}/{appId}/{username}?token=&resource=&version=&start=&size=&ignoreConsumed=
    Map paramMap=imHelperUtil.getParam();
    String imClient=(String)paramMap.get("imClient");
    String eptId=(String)paramMap.get("eptId");
    String appId=(String)paramMap.get("appId");
    JSONObject userToken=imHelperUtil.getUserToken(userId);
    
    String url = imClient+"version/"+eptId + "/" + appId
    + "/"+userId+"?token="+userToken.getString("token")+"&resource=web-v2.1";
    String[] params = new String[] { "resource=" + "web-v2.1"};
    String message=imHelperUtil.postJson(url, "GET",null,params);
    JSONObject jo=JSONObject.fromObject(message);
    return jo;
  }

  @Override
  public boolean sendMessage(String messageTempleteId, List dataList, List receiverList) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String getContentByMessageTempleteCode(String msgRuleCode) {
    // TODO Auto-generated method stub
    String result=null;
    List list = msgSettingDao.getMsgRuleData(msgRuleCode);
    if(list != null && list.size()>0) {
      Map temp=(Map) list.get(0);
      result=(String)temp.get("content_title")+(String)temp.get("content_model");
    }
    return result;
  }
  
  
  @Override
  public String getContentByMessageTempleteId(String msgRuleId) {
    // TODO Auto-generated method stub
    String result=null;
    List list = msgSettingDao.getMsgRuleDataByMsgID(msgRuleId);
    if(list != null && list.size()>0) {
      Map temp=(Map) list.get(0);
      result=(String)temp.get("content_title")+(String)temp.get("content_model");
    }
    return result;
  }

  @Override
  public List getReveiverList(String belong_org, String roleId) {
    // TODO Auto-generated method stub
    StringBuffer sql =new StringBuffer();
    sql.append("select t.* from sys_usermanage t,sys_user_role_rule u ");
    sql.append("where t.user_id=u.user_id and t.belong_org=? and u.role_id=?");
    List list=dao.findBySql(sql.toString(), new String[] { belong_org,roleId });
    return list;
  }

  @Override
  public Map<String, Object> getImParam() {
    // TODO Auto-generated method stub
    Map<String,Object> imParamMap = new HashMap<String, Object>();
    try {
      Map paramMap=imHelperUtil.getParam();
      imParamMap.put("result", "success");
      imParamMap.put("eptId",paramMap.get("eptId"));  
      imParamMap.put("appId",paramMap.get("appId"));  
      imParamMap.put("servlet",paramMap.get("servlet"));
      imParamMap.put("safeServlet",paramMap.get("safeServlet"));
      imParamMap.put("wsport",paramMap.get("wsport"));
      imParamMap.put("hbport",paramMap.get("hbport"));
      imParamMap.put("address",paramMap.get("address"));
      
      return imParamMap;
    } catch (Exception e) {
      logger.error(e.getMessage());
      imParamMap.put("result", "fail");
      return imParamMap;
    }   
  }

  @Override
  public Map<String, Object> getUserToken(String username) {
    // TODO Auto-generated method stub
    Map<String, Object> res = new HashMap<String, Object>();
    JSONObject userToken=imHelperUtil.getUserToken(username);
    res.put("result", "success");
    res.put("data", userToken);
    return res;
  }

}
