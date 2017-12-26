/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017 北京用友政务软件有限公司
 * </p>
 * <p>
 * Company: 北京用友政务软件有限公司
 * </p>
 * <p>
 * CreateData: 2017-11-22下午3:40:53
 * </p>
 * 
 * @author songlr3
 * @version 1.0
 */
package gov.df.supervise.controller.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie工具类
 * 
 */
public class CookieUtil {

    private CookieUtil() {
    }

    /**
     * 添加cookie
     * 
     * @param response
     * @param name
     * @param value
     * @param maxAge
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        try {
            name = URLEncoder.encode(name, "utf-8");
            value = URLEncoder.encode(value, "utf-8");
            Cookie cookie = new Cookie(name, value);
            cookie.setPath("/");
            if (maxAge > 0) {
                cookie.setMaxAge(maxAge);
            }
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加会话级cookie
     *
     * @param response
     * @param name
     * @param value
     */
    public static void addCookieOnce(HttpServletResponse response, String name, String value) {
        try {
            name = URLEncoder.encode(name, "utf-8");
            value = URLEncoder.encode(value, "utf-8");
            Cookie cookie = new Cookie(name, value);
            cookie.setPath("/");
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除cookie
     * 
     * @param response
     * @param name
     */
    public static void removeCookie(HttpServletResponse response, String name) {
        Cookie uid = new Cookie(name, null);
        uid.setPath("/");
        uid.setMaxAge(0);
        response.addCookie(uid);
    }

    /**
     * 获取cookie值
     * 
     * @param request
     * @return
     */
    public static String getUid(HttpServletRequest request,String cookieName) {
        Cookie cookies[] = request.getCookies();
        if(cookies != null){
	        for (Cookie cookie : cookies) {
	            if (cookie.getName().equals(cookieName)) {
	                return cookie.getValue();
	            }
	        }
        }
        return null;
    }
}