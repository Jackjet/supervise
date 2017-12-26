package gov.df.fap.service.portal.impl;
import gov.df.fap.api.portal.IArticleService;
import gov.df.fap.bean.portal.ArticleEntity;
import gov.df.fap.service.login.AbstractComponentService;
import gov.df.fap.service.portal.dao.BaseDao;

import java.sql.SQLException;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl extends AbstractComponentService implements IArticleService {

  @Resource
  private BaseDao baseDao;

  @Override
  public ArticleEntity getArticleData(String ruleId, Map params) {
	  ArticleEntity article = new ArticleEntity();
	  try {
	     article= (ArticleEntity) baseDao.queryForObject(ruleId, params);
	} catch (SQLException e) {
		e.printStackTrace();
	}
	  return article;
  }

}
