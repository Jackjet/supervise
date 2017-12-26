package gov.df.fap.service.workflow.activiti.design.data;

import gov.df.fap.api.workflow.activiti.design.IGetModuleAndRoleTreeData;
import gov.df.fap.service.util.dao.GeneralDAO;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class IGetModuleTreeDataImplBO implements IGetModuleAndRoleTreeData {

	@Autowired
  @Qualifier("generalDAO")
	private GeneralDAO dao;

	public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  @Override
	public List<Map> getCodeAndNameFromModuleOrRole(String sql) {
		List<Map> findBySql = dao.findBySql(sql);
		return findBySql;
	}

	@Override
	public List<Map> getCodeAndNameFromModuleOrRoleByCode(String sql,
			Object[] params) {
		List<Map> findBySql = dao.findBySql(sql,params);
		return findBySql;
	}
	
	
	
	
	
	
	
	
}
