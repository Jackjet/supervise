package gov.df.fap.api.dictionary;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IDictionaryService {

  public Map<String, Object> getdictree(HttpServletRequest request, HttpServletResponse response);

}
