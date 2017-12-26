package gov.df.fap.service.workflow.activiti.design.data;

import gov.df.fap.api.workflow.activiti.design.IGetInnerTable;
import gov.df.fap.service.util.dao.GeneralDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class IGetInnerTableImplBO implements IGetInnerTable {

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
	public String getDatas_Get(String sql) {
		JSONArray jsonArray = new JSONArray();
		List<Map> findBySql = dao.findBySql(sql);
		Map dataMap = new HashMap();
		Map nullMap = new HashMap();
		
		for (Map map : findBySql) {
			String TABLE_CODE = (String) map.get("table_code");
			String TABLE_NAME = (String) map.get("table_name");
			String ID_COLUMN_NAME = (String) map.get("id_column_name");
			ID_COLUMN_NAME=ID_COLUMN_NAME==null?"":ID_COLUMN_NAME;
			map.put("value", TABLE_CODE+" "+ID_COLUMN_NAME);
			map.put("text", TABLE_CODE + " " + TABLE_NAME + " " + ID_COLUMN_NAME);
			jsonArray.add(map);
		}
		nullMap.put("value", "luru");
		jsonArray.add(0, nullMap);
		String jsonStr = jsonArray.toString();
		return jsonStr;
	}
}
