package gov.df.fap.service.util.sessionmanager;

import gov.df.fap.bean.user.UserInfoContext;

import java.io.Serializable;

/**
 * 会话session对应的数据model类
 * @author swj
 * @version 1.0
 */
public class Session implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5250345193173758137L;
	//超时最大时间
	private static long validateTime = 3600000;
	//客户端身份信息
	private UserInfoContext userInfoContext = new UserInfoContext();
	//保存当前session最近被访问时间
	private long time;

	public Session() {
		if(userInfoContext == null)
			userInfoContext = new UserInfoContext();
	}

	public int createSession() {
		time = System.currentTimeMillis();
		return this.hashCode();
	}

	public void resetTime() {
		time = System.currentTimeMillis();
	}

	public long getTime() {
		return this.time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public UserInfoContext getUserInfoContext() {
		return userInfoContext;
	}

	public void setUserInfoContext(UserInfoContext userInfoContext) {
		this.userInfoContext = userInfoContext;
	}

	public static long getValidateTime() {
		return validateTime;
	}

	public static void setValidateTime(long validateTime) {
		Session.validateTime = validateTime;
	}
}