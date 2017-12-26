package gov.df.fap.service.util.sessionmanager;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpSession;
 

/**
 * 在线session清除类，系统进入时记录session，每隔interval秒判断循环，将已经失效的Session从系统中移除
 * @author hult
 *
 */
public class OnlineSessionInvalid  {
	private  Map globalSessionMap = new Hashtable();
	private  int INTERVAL = 60*1000;
	OnlineUsers onlineUsers = OnlineUsers.getInstance();
	Method methodRemoveSession = null;
	//单例模式
	private static OnlineSessionInvalid sessionInvalid= new OnlineSessionInvalid();
	private OnlineSessionInvalid()
	{
		new Thread(){
			public void run(){
				while(true)
				{
					try {
						sleep(INTERVAL);
					} catch (InterruptedException e) {
					}
					try {
						Object[] keys = globalSessionMap.keySet().toArray();
						for(int i =0,n=keys.length;i<n;i++)
						{
							try{
								//目前没有Session中提供验证Session是否有效的方法只能是调用一次失效会报错，在异常处理中将Sessionr移除;
								((HttpSession)globalSessionMap.get(keys[i])) .getLastAccessedTime();
							}catch(Throwable e)
							{
								removeInvalidSessionListerner((String)keys[i]);
							}
						}
					} catch (Throwable e) {
					}
				}
			}
		}.start();
		try {
			methodRemoveSession = Class.forName("com.ufgov.app.manager.http.GlobalWebContext").getMethod("removeSession",new Class[]{String.class});
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 获取实例
	 * @return
	 */
	public static OnlineSessionInvalid getInstance(){
		return sessionInvalid;
	}
	/**
	 * 增加Session失效监听
	 * @param session
	 */
	public   void addInvalidSessionListerner(HttpSession session)
	{
		if(session==null)
			return;
		String id = session.getId();
		if(!globalSessionMap.containsKey(id))
		{
			globalSessionMap.put(session.getId(), session);
		}
	}
	/**
	 * 已明确Session失效的主动从监听列表中移除
	 * @param session
	 */
	public void removeInvalidSessionListerner(String sessionid)
	{
 		if(globalSessionMap.containsKey(sessionid))
		{
			globalSessionMap.remove(sessionid);
			if(methodRemoveSession!=null)
			{
				try {
				  methodRemoveSession.invoke(null, new Object[]{sessionid});
				   
				} catch (Exception e) {
					
				} 
			}
			//删除在线用户
			try{
				onlineUsers.removeUser(sessionid, 0);
			}catch(Exception ex)
			{
				
			}
		}
	}

}
