package gov.df.fap.service.messageplatform;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 即时通讯发送消息辅助类
 * @author yanyga
 * 2017-07-28
 */
public class IMAPIHelperUtil {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  public Map<String, String> getParam() {

    Map<String, String> map = new HashMap<String, String>();
    try {
      //如果缓存中有 直接从缓冲中获取
      if (paramMap == null) {
        Resource resource = new ClassPathResource("IMConfig.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);

        String eptId = (String) props.get("im.eptid");
        String appId = (String) props.get("im.appid");
        String clientID = (String) props.get("im.clientid");
        String clientSecret = (String) props.get("im.clientsecret");
        String imClient = "http://" + (String) props.get("im.client") + "/sysadmin/rest/";
        String servlet = "http://" + (String) props.get("im.client") + "/";
        String safeServlet = "https://" + (String) props.get("im.client") + "/";
        String wsport = (String) props.get("im.wsport");
        String hbport = (String) props.get("im.hbport");
        String address = (String) props.get("im.address");

        map.put("eptId", eptId);
        map.put("appId", appId);
        map.put("clientID", clientID);
        map.put("clientSecret", clientSecret);
        map.put("imClient", imClient);
        map.put("servlet", servlet);
        map.put("safeServlet", safeServlet);
        map.put("wsport", wsport);
        map.put("hbport", hbport);
        map.put("address", address);
      } else {
        return paramMap;
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return map;
  }

  Map<String, String> paramMap = getParam();

  Map<String, Object> userTokenCache = new HashMap<String, Object>();

  // 企业ID
  private final String eptId = paramMap.get("eptId");

  // 应用ID
  private final String appId = paramMap.get("appId");

  // Client ID
  private final String clientID = paramMap.get("clientID");

  // Client Secret
  private final String clientSecret = paramMap.get("clientSecret");

  // im Client
  private final String imClient = paramMap.get("imClient");

  //appToken
  public static JSONObject appToken = null;

  private volatile static IMAPIHelperUtil imAPIHelperUtil;

  private IMAPIHelperUtil() {
  }

  public static IMAPIHelperUtil getSingletonIMAPIHelperUtil() {
    if (imAPIHelperUtil == null) {
      synchronized (IMAPIHelperUtil.class) {
        if (imAPIHelperUtil == null) {
          imAPIHelperUtil = new IMAPIHelperUtil();
        }
      }
    }
    return imAPIHelperUtil;
  }

  /**
   * 发送ajax 请求，传输json数据
   * @param url
   * @param method
   * @param params
   * return 
   */
  public String postJson(String url, String method, String[] arr, String... params) {
    String result = "";
    try {
      JSONObject obj = new JSONObject();
      if (params.length > 0) {
        for (String param : params) {
          String key = param.split("=")[0];
          String value = " ";
          if (param.lastIndexOf('=') + 1 == param.length()) {
            continue;
          }
          value = param.split("=")[1];
          obj.element(key, value);
        }
      }
      if (!(arr == null || arr.length == 0)) {
        obj.element("operand", arr);
      }
      System.out.println(obj);
      // 创建url资源
      URL urlHttp = new URL(url);
      // 建立http连接
      HttpURLConnection conn = (HttpURLConnection) urlHttp.openConnection();

      conn.setDoInput(true);

      // 设置不用缓存
      conn.setUseCaches(false);
      // 设置传递方式
      conn.setRequestMethod(method);
      // 设置维持长连接
      conn.setRequestProperty("Connection", "Keep-Alive");

      if ("POST".equals(method.toUpperCase())) {
        // 设置文件字符集:
        conn.setRequestProperty("Charset", "UTF-8");
        // 转换为字节数组
        byte[] data = (obj.toString()).getBytes();
        // 设置文件长度
        conn.setRequestProperty("Content-Length", String.valueOf(data.length));

        // 设置文件类型:
        conn.setRequestProperty("Content-Type", "application/json");
        // 设置允许输出
        conn.setDoOutput(true);
        OutputStream out = conn.getOutputStream();
        // 写入请求的字符串
        out.write((obj.toString()).getBytes());
        out.flush();
        out.close();
      }

      // 开始连接请求
      conn.connect();

      System.out.println(conn.getResponseCode());

      // 请求返回的状态
      if (conn.getResponseCode() == 200) {
        System.out.println("连接成功");
        // 请求返回的数据
        InputStream in = conn.getInputStream();
        String a = null;
        BufferedReader inBuff = new BufferedReader(new InputStreamReader(in,"UTF-8"));  
        StringBuffer buffer = new StringBuffer();  
        String line = "";  
        while ((line = inBuff.readLine()) != null){  
          buffer.append(line);  
        }  
        result = buffer.toString();  
//        try {
//          byte[] data1 = new byte[in.available()];
//          in.read(data1);
//          // 转成字符串
//          a = new String(data1);
//          result = a;
//          System.out.println(a);
//        } catch (Exception e1) {
//          // TODO Auto-generated catch block
//          e1.printStackTrace();
//        }
      } else {
        System.out.println("n");
      }
    } catch (Exception e) {
      logger.error("请求失败！", e);
      System.out.println(e.getMessage());
    }
    return result;
  }

  /* 
   * 获取AppToken
  */
  public JSONObject getAPPToken() {
    if (appToken == null) {
      JSONObject json = null;
      String url = imClient + eptId + "/" + appId + "/token";
      String[] params = new String[] { "clientId=" + clientID, "clientSecret=" + clientSecret };
      String resStr = postJson(url, "POST", null, params);
      json = JSONObject.fromObject(resStr);
      appToken=json;
      return json;
    } else {
      return appToken;
    }
  }

  /**
   * 获取用户token
   * @param username 
   * @return
   */
  public JSONObject getUserToken(String username) {
    String url = imClient + eptId + "/" + appId + "/token";
    String[] params = new String[] { "clientId=" + clientID, "clientSecret=" + clientSecret, "username=" + username };
    Map userCache = new HashMap();
    JSONObject jsobj = null;
    try {
      if (userTokenCache == null || userTokenCache.size() == 0) {
        String resStr = postJson(url, "POST", null, params);
        jsobj = JSONObject.fromObject(resStr);
        userCache = (Map) jsobj;
        userTokenCache.put(username, userCache);
      } else if (userTokenCache.get(username) != null) {
        Map temp = (Map) userTokenCache.get(username);
        Long expireTime = (Long) temp.get("expiration");
        Long nowTime = System.currentTimeMillis();
        if (expireTime < nowTime) {
          String resStr = postJson(url, "POST", null, params);
          jsobj = JSONObject.fromObject(resStr);
          userCache = (Map) jsobj;
          userTokenCache.put(username, userCache);
        } else {
          jsobj = (JSONObject) userTokenCache.get(username);
        }
      } else {
        String resStr = postJson(url, "POST", null, params);
        jsobj = JSONObject.fromObject(resStr);
        userCache = (Map) jsobj;
        userTokenCache.put(username, userCache);
      }
    } catch (Exception e) {
      logger.error("获取token失败", e);
    }
    
    return jsobj;
  }

  /**
   * 调用该接口给指定的某个用户发送消息
   * @param fromUser 发送消息者，如果为系统消息该字段设为“admin”
   * @param content  消息内容
   */
  public void sendMessageToUser(String fromUser, String toUser, String content) {
    //https://im.yyuap.com/sysadmin/rest/{etpId}/{appId}/message?token=
    if (appToken == null) {
      getAPPToken();
    }
    String url = imClient + eptId + "/" + appId + "/message?token=" + appToken.getString("token");
    String[] params = new String[] { "from=" + fromUser, "to=" + toUser, "content=" + content, "contentType=" + 2 };
    postJson(url, "POST", null, params);
  }

  /**
   * 获取应用下的用户列表
   */
  public void getMIUserList() {
    //URL  https://im.yyuap.com/sysadmin/rest/remote/user/list?token=&etpId=&appId=&start=&size=
    if (appToken == null) {
      getAPPToken();
    }
    String url = imClient + "remote/user/list?token=" + appToken.getString("token") + "&etpId=" + eptId + "&appId="
      + appId + "";
    String[] params = new String[] {};
    String result = postJson(url, "GET", null, params);
  }

}
