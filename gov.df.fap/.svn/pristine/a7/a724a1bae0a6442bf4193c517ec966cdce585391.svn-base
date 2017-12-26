package gov.df.fap.service.workflow.activiti.design.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;

import gov.df.fap.api.workflow.activiti.design.IWFRuleService;
import gov.df.fap.service.util.dao.GeneralDAO;

@Service
public class IWFRuleImplBO implements IWFRuleService {

	protected ClassLoader classloader;
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(IWFRuleImplBO.class);

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
	public String getTreeStr() throws Exception {
		JSONArray jsonArray = new JSONArray();
		List<Map> list = getRootDir();
		for (Map m : list) {
			Map map = new HashMap();
			String sys_type = (String) m.get("sys_id");
			String name = (String) m.get("sys_name");
			map.put("isParent", "true");
			map.put("name", sys_type +" "+ name);
			map.put("id", sys_type);
			map.put("pId", 0);
			map.put("guid", sys_type);
			map.put("user_guid", sys_type);
			map.put("pk", sys_type);
			map.put("nocheck", "false");
			map.put("canselect", "true");
			jsonArray.add(map);
			jsonArray = getSeconDir(sys_type,jsonArray);
		}
		return jsonArray.toString();
	}

	/**
	 * 判断请求是否获取二级目录
	 * 
	 * @return
	 */
	public boolean isGetSeconDir(String sysType) throws Exception {
		boolean flag = false;
		List<Map> list = getRootDir();
		for (Map m : list) {
			String sys_type = (String) m.get("sys_type");
			if (sysType.equals(sys_type)) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 获取根级目录
	 * 
	 * @return
	 */
	public List<Map> getRootDir() throws Exception {
		String sql = "select t.sys_id, t.sys_name from sys_app t where t.sys_id in " +
				" (select distinct t1.sys_type from sys_wf_extend_rule t1) order by t.sys_id asc";
		List<Map> list = null;
		try {
			list = dao.findBySql(sql);
		} catch (Exception e) {
			LOGGER.error("【获取根级目录】 失败！" + e.getMessage(), e);
			throw e;
		}
		return list;
	}

	/**
	 * 获取二级目录
	 * 
	 * @return
	 */
	public JSONArray getSeconDir(String sys_type,JSONArray jsonArray) throws Exception {
		String sql = "select distinct t.rule_type from sys_wf_extend_rule t where t.sys_type=?";
		String sql2 = "select t.field_valueset from sys_metadata t where t.field_code='RULE_TYPE'";
		try {
			List<Map> list2 = dao.findBySql(sql2);
			Map map2 = new HashMap();
			for (Map m : list2) {
				String field_valueset = (String) m.get("field_valueset");
				String[] split = field_valueset.split("\\+");
				for (String str : split) {
					String[] split2 = str.split("\\#");
					map2.put(split2[0], split2[1]);
				}
			}
			String[] fieldParams = new String[] { sys_type };
			List<Map> list = dao.findBySql(sql, fieldParams);
			for (Map m : list) {
				Map map = new HashMap();
				String rule_type = (String) m.get("rule_type");
				String name = (String)map2.get(rule_type);
				map.put("isParent", "true");
				map.put("name", name);
				map.put("id", rule_type + "_" + sys_type);
				map.put("pId", sys_type);
				map.put("guid", rule_type + "_" + sys_type);
				map.put("user_guid", rule_type + "_" + sys_type);
				map.put("pk", rule_type + "_" + sys_type);
				map.put("nocheck", "false");
				map.put("canselect", "true");
				jsonArray.add(map);
				jsonArray = getThreeDir(rule_type + "_" + sys_type,jsonArray);
			}
		} catch (Exception e) {
			LOGGER.error("【获取二级目录】 失败！" + e.getMessage(), e);
			throw e;
		}
		return jsonArray;
	}

	/**
	 * 获取三级目录
	 * 
	 * @return
	 */
	public JSONArray getThreeDir(String id,JSONArray jsonArray) throws Exception {
		String sql = "select t.rule_id,t.rule_name from sys_wf_extend_rule t where t.sys_type=? "
				+ "and t.rule_type=? order by t.rule_id";
		try {
			String[] split = id.split("_");
			String rule_type = split[0];
			String sys_type = split[1];
			String[] fieldParams = new String[] { sys_type, rule_type };
			List<Map> list = dao.findBySql(sql, fieldParams);
			for (Map m : list) {
				Map map = new HashMap();
				String rule_id = (String) m.get("rule_id");
				String name = (String) m.get("rule_name");
				map.put("isParent", "false");
				map.put("name", rule_id + " " + name);
				map.put("id", rule_id);
				map.put("pId", id);
				map.put("guid", rule_id);
				map.put("user_guid", rule_id);
				map.put("pk", rule_id);
				map.put("nocheck", "false");
				map.put("canselect", "true");
				jsonArray.add(map);
			}
		} catch (Exception e) {
			LOGGER.error("【获取三级目录】 失败！" + e.getMessage(), e);
			throw e;
		}
		return jsonArray;
	}
}
