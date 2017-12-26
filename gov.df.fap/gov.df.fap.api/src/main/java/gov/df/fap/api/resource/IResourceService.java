package gov.df.fap.api.resource;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IResourceService {

  public Map<String, Object> addResourceMenu(HttpServletRequest request, HttpServletResponse response);

  public Map<String, Object> delResourceMenu(HttpServletRequest request, HttpServletResponse response);

  public Map<String, Object> changeResRemark(HttpServletRequest request, HttpServletResponse response);

  public Map<String, Object> queryResByRole(HttpServletRequest request, HttpServletResponse response);

  public Map<String, Object> changeResCheck(HttpServletRequest request, HttpServletResponse response);

}
