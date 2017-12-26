package gov.df.fap.api.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IViewService {

  public Map<String, Object> getViewByGuid(HttpServletRequest request, HttpServletResponse response);

  public Map<String, Object> getViewTree(HttpServletRequest request, HttpServletResponse response);

  public Map<String, Object> updateMenuView(HttpServletRequest request, HttpServletResponse response);

  public Map<String, Object> delMenuView(HttpServletRequest request, HttpServletResponse response);

  public Map<String, Object> getViewDetail(HttpServletRequest request, HttpServletResponse response);

  public Map<String, Object> getAllMenuView(HttpServletRequest request, HttpServletResponse response);

  public Map<String, Object> test(HttpServletRequest request, HttpServletResponse response);

  public List getSysMenuViewByViewId(String viewId);

}
