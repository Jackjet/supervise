package gov.df.fap.service.portal.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import sun.misc.BASE64Decoder;

/**
 * 转移request请求，覆盖getParameter方法并返回base64解密后的值
 */
@SuppressWarnings("restriction")
public class HttpServletRequestWrapper2 extends HttpServletRequestWrapper {

  // 原request参数
  private Map<String, String[]> parameterMap;
  
  // 本次访问地址
  //private String url;
  
  // 本次访问携带的参数
  //private String paramsFromUrl;

  @SuppressWarnings("unchecked")
  public HttpServletRequestWrapper2(HttpServletRequest request) {
    super(request);
    parameterMap = request.getParameterMap();
    //url = request.getRequestURI();
    //paramsFromUrl = request.getQueryString();
  }

  @Override
  public String getParameter(String name) {
    String[] values = parameterMap.get(name);
    if (values == null || values.length == 0) {
      return null;
    }
    if (name.equals("tokenid")){
      return values[0];
    }
    return base64Decode(values[0]);
  }

  public String base64Decode(String base64CodedString) {
    String decodeStr = "";
    try {
      BASE64Decoder dec = new BASE64Decoder();
      byte[] decodeBuffer = dec.decodeBuffer(base64CodedString);
      decodeStr = new String(decodeBuffer);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return decodeStr;
  }
  
  // TODO wait URL过滤
  public boolean isUrlPass(String url) {
    return true;
  }

}
