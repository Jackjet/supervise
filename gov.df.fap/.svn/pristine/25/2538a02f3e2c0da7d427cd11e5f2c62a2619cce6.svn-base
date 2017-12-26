package gov.df.fap.service.portal.impl;

import gov.df.fap.api.portal.IBaseService;
import gov.df.fap.service.portal.dao.BaseDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class BaseServiceImpl implements IBaseService {
	
	private static final Logger log = Logger.getLogger(BaseServiceImpl.class);
	@Resource
	private BaseDao baseDao;

  public BaseDao getIBaseDao() {
    return baseDao;
  }

  public void setBaseDao(BaseDao baseDao) {
    this.baseDao = baseDao;
  }
  
  public List getPageDataList(String ruleId, Map params){
		List pageDataList = new ArrayList();
		List newList = new ArrayList();
		try {
			pageDataList = baseDao.queryForList(ruleId, params);
			//分页处理
			String startNum = "";
			String limitNum = "";
			if(!"".equals(params.get("startNum"))){
				startNum=params.get("startNum").toString();
			}
			
			if(!"".equals(params.get("limitNum"))){
				limitNum=params.get("limitNum").toString();
			}
			
			if(pageDataList.size()< Integer.parseInt(params.get("limitNum").toString())){
				limitNum = String.valueOf(pageDataList.size());
			}
			
			if(!"".equals(startNum) && !"".equals(limitNum)){
				for(int i = Integer.parseInt(startNum); i<Integer.parseInt(limitNum);i++){
					newList.add(pageDataList.get(i));
				}
			}
		} catch (SQLException e) {
			log.error(e);
		}
		return newList;
  }
  
  public int getTotalRecordCount(String ruleId, Map params){
		int totalCount = 0;
		try {
			totalCount = baseDao.queryForCount(ruleId, params);
		} catch (SQLException e) {
			log.error(e);
		}
		return totalCount;
	}
  
}
