package gov.df.fap.service.dictionary;

import gov.df.fap.api.dictionary.IDictionaryService;
import gov.df.fap.api.dictionary.interfaces.IDictionary;
import gov.df.fap.api.redis.CacheUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DictionaryService implements IDictionaryService {

  @Autowired
  @Qualifier("sys.dictionaryService")
  private IDictionary iDictionary;

  @Autowired
  @Qualifier("df.cacheUtil")
  private CacheUtil cacheUtil;

  /**
   * 带权限的基础要素查询
   * 根据要素简称、COA、关联关系、附加条件sql进行查询
   * 其中COA、关联关系、附加条件sql非必要条件
   */
  public Map<String, Object> getdictree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    List list = null;
    String element = request.getParameter("element");
    String coa_id = request.getParameter("coa_id");
    String ele_value = request.getParameter("ele_value");
    // 传入条件需要 and code = ''的形式 
    String condition = request.getParameter("condition");
    String sqlplus = " order by chr_code ";
    if(condition != null && !"".equals(condition)){
      sqlplus = " " + condition + sqlplus;
    }
    map.put("flag", 1);
    if (ele_value != null && !"".equals(ele_value)) {
      sqlplus = "and chr_code like '" + ele_value + "%' " + sqlplus;
    }
    Map relationPriEleCode = new HashMap();
    String relations = request.getParameter("relations");

    if (relations != null && relations.indexOf("@@") > 0) {
      for (String eleCodeValue : relations.split("@@")) {
        String[] value = eleCodeValue.split(":");
        relationPriEleCode.put(value[0], value[1]);
      }
    }
    try {
      String key = cacheUtil.getCacheKey(element, true, coa_id, relationPriEleCode.toString(), sqlplus);
      if (!cacheUtil.exist(key)) {
        synchronized (this) {
          list = iDictionary.findEleValues(element, null, null, true, coa_id, relationPriEleCode, sqlplus);
          cacheUtil.put(key, list);
        }
        map.put("eleDetail", list);
      } else {
        Object obj = cacheUtil.get(key, List.class);
        map.put("eleDetail", obj);
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      map.put("flag", 0);
    }
    return map;
  }
}
