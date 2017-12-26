package gov.df.fap.service.workflow.activiti.design.data;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import gov.df.fap.api.workflow.activiti.design.IGetModelId;
import gov.df.fap.service.util.dao.GeneralDAO;


@Service
public class IGetModelIdImplBO implements IGetModelId {

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
	public String getModelId(String code) {
		
		String selModelId="select id_ from act_re_model where key_=?";
		List<Map> findBySql = dao.findBySql(selModelId, new Object[]{code});
		String modelId="";
		for (Map map : findBySql) {
			modelId = (String) map.get("id_");
		}
		
		return modelId;
	}

}
