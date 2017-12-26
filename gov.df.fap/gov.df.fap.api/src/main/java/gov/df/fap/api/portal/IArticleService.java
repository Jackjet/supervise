package gov.df.fap.api.portal;

import gov.df.fap.bean.portal.ArticleEntity;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IArticleService {

  
  /**
   * 获取文章数据
   */
	ArticleEntity  getArticleData(String ruleId, Map params);
  
}
