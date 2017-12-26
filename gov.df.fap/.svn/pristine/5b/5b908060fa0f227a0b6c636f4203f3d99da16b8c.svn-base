package gov.df.fap.service.login;

import com.longtu.framework.annotation.parse.IParseMethodAnnotation;
import com.longtu.framework.annotation.parse.ParseMethodAnnotation;
import com.longtu.framework.component.Component;
import com.longtu.framework.component.ComponentService;
import com.longtu.framework.exception.AppException;
import com.longtu.framework.exception.CauseAppException;
import com.longtu.framework.init.ConsoleManager;
import com.longtu.framework.rpcfw.termial.RCPConsole;
import com.longtu.framework.rpcfw.termial.RCPConsoleManager;
import com.longtu.framework.server.PageBuilderOut;
import com.longtu.framework.server.ServerContext;
import com.longtu.framework.server.identify.ClientIdentify;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Servlet;
import javax.servlet.http.*;
import org.apache.log4j.Logger;

// Referenced classes of package com.longtu.framework.component:
//            ComponentService, Component

public class AbstractComponentService
    implements ComponentService
{

    public AbstractComponentService()
    {
        po = null;
        console = null;
    }

    public PageBuilderOut getPo()
    {
        return po;
    }

    public HttpServletRequest getRequest()
    {
        return ((ServerContext)tl.get()).getRequest();
    }

    public HttpServletResponse getResponse()
    {
        return ((ServerContext)tl.get()).getResponse();
    }

    public HttpSession getSession()
    {
        return ((ServerContext)tl.get()).getRequest().getSession();
    }

    public Servlet getPageServlet()
    {
        return ((ServerContext)tl.get()).getServlet();
    }

    public Component getComponent()
    {
        return (Component)tlcom.get();
    }

    public RCPConsole getConsole()
    {
        return ((ServerContext)tl.get()).getConsole();
    }

    public void saveComponent(HashMap hashmap)
    {
    }

    public void setComponent(Component com)
    {
        tlcom.set(com);
    }

    public void setServerContext(ServerContext context)
    {
        tl.set(context);
    }

    public void setConsole(String consoletype)
    {
        ((ServerContext)tl.get()).setConsole(RCPConsoleManager.createConsole(getRequest()));
    }

    public Object runFunction(String mothedname, Object params[])
        throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, AppException, Exception
    {
        Method invokeMethod = null;
        if(params == null)
        {
            invokeMethod = getClass().getMethod(mothedname, null);
        } else
        {
            Class paramscls[] = new Class[params.length];
            for(int i = 0; i < params.length; i++)
                paramscls[i] = params[i].getClass();

            invokeMethod = getClass().getMethod(mothedname, paramscls);
        }
        Object invoke = null;
        try
        {
            invoke = invokeMethod.invoke(this, params);
        }
        catch(InvocationTargetException e)
        {
            throw CauseAppException.causeAppException(e.getTargetException());
        }
        catch(Exception e)
        {
            throw CauseAppException.causeAppException(e);
        }
        if(getRequest().getParameter("isMethodValue") != null)
            return ParseMethodAnnotation.getInstance().parseComponentMethod(invokeMethod, invoke, getRequest());
        else
            return invoke;
    }

    public ComponentService getComponent(String serviceid)
        throws InstantiationException, IllegalAccessException, ClassNotFoundException, AppException
    {
        Component com = ConsoleManager.getManeger().getComponentByServiceid(serviceid);
        ComponentService cs = com.getComponentServiceInstance();
        cs.setComponent(com);
        cs.setServerContext((ServerContext)tl.get());
        return cs;
    }

    public Class getClazz()
    {
        return getClass();
    }

    public Map loadComponent(HttpServletRequest request, HttpServletResponse response, Map config)
        throws Exception
    {
        return config;
    }

    public void setPo(PageBuilderOut po)
    {
        this.po = po;
    }

    private static ClientIdentify clientIdentify = new ClientIdentify();
    private static ThreadLocal tl = new ThreadLocal();
    private static ThreadLocal tlcom = new ThreadLocal();
    private PageBuilderOut po;
    protected RCPConsole console;

}
