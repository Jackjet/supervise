/**
 * 
 */
package gov.df.supervise.api.common;

import gov.df.fap.util.xml.XMLData;
import gov.df.supervise.bean.common.PageListBean;
import gov.df.supervise.bean.common.QueryParameter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Title:查询接口
 * </p>
 * <p>
 * Description:查询接口，必须是可复制的对象，且得保证复制时的线程安全
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: 北京用友政务软件有限公司
 * </p>
 * <p>
 * CreateDate 2010-10-29
 * </p>
 * 
 * @author hlf
 * @version 3.0.01.06
 */
public interface Query extends Cloneable {

  /**
   * 执行查询
   * @param param 查询参数
   * @return 查询结果
   */
  PageListBean query(QueryParameter param);

  /**
   * 查询方法，根据界面视图字段查询，例：select column1, column2 from table
   * @param param 查询参数
   * @param viewIdList 界面视图id集合
   * @return PageListBean
   */
  PageListBean queryByView(QueryParameter param, List<String> viewIdList);

  /**
   * 根据前台请求及其参数，查询数据
   * @param request
   * @param response
   * @return PageListBean
   * */
  PageListBean queryByRequest(HttpServletRequest request, HttpServletResponse response);

  /**
   * 要支持对象克隆
   * @return
   */
  public Object clone();

  XMLData doDetailQuery(HttpServletRequest request, HttpServletResponse response);
}
