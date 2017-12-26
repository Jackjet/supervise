package gov.df.fap.util.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 工作流参数过滤器
 * @author dingyy@yonyou.com
 */
public class WfUserContextFilter implements Filter {
  protected FilterConfig filterConfig = null;

  protected String encoding = "";

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
    throws IOException, ServletException {
    if (encoding != null) {
      servletRequest.setCharacterEncoding(encoding);
    }
    WfUserContext.setRequest(servletRequest);
    filterChain.doFilter(servletRequest, servletResponse);
    WfUserContext.clear();
  }

  public void destroy() {
    filterConfig = null;
    encoding = null;
  }

  public void init(FilterConfig filterConfig) throws ServletException {
    this.filterConfig = filterConfig;
    this.encoding = filterConfig.getInitParameter("encoding");
  }
}