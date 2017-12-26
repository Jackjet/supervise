package gov.df.fap.service.workflow.activiti.saveModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import gov.df.fap.api.workflow.activiti.saveModel.IDeleteModel;
import gov.df.fap.bean.workflow.SysWfConditionLine;
import gov.df.fap.bean.workflow.SysWfLineRule;
import gov.df.fap.bean.workflow.SysWfNodeCondition;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;

@Service
public class IDeleteModelImplBO implements IDeleteModel {

	protected ClassLoader classloader;
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(IDeleteModelImplBO.class);

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
	public HashMap deleteModel(String code) {
		String get_wf_id = "select wf_id,condition_id from sys_wf_flows where wf_code=?";
		String get_node_id = "select node_id,node_code from sys_wf_nodes where wf_id=?";
		String sql_wf_flows_del = "delete from sys_wf_flows where wf_id=?";
		String sql_wf_nodes_del = "delete from sys_wf_nodes where node_id=?";
		String sql_wf_module_node_del = "delete from sys_wf_module_node where node_id=?";
		String sql_role_node_del = "delete from sys_wf_role_node where node_id=?";
		String sql_rule_node_del = "delete from sys_wf_rule_node where node_id=?";
		String sql_node_conditions_del = "delete from sys_wf_node_conditions where wf_id=? and node_id=?";
		String sql_sys_wf_node_tolly_action_type_del = "delete from sys_wf_node_tolly_action_type where node_id=?";
		String sql_sys_wf_conditions_del = "delete from sys_wf_conditions where condition_id=?";
		String sql_sys_wf_conditions_line_del = "delete from sys_wf_condition_lines where condition_id=?";
		String get_condition_id = "select distinct condition_id from sys_wf_node_conditions where wf_id=?";

		// TODO *17**
		String sql_del_line_rule = "delete from sys_wf_line_rule where line_rule_id=?";
		String sql_del_rule_monitor = "delete from sys_wf_rule_monitor where line_rule_id=?";
		String get_line_rule_id = "select distinct line_rule_id from sys_wf_node_conditions where wf_id=?";

		HashMap regMap = new HashMap();
		try {
			String wfIdUp = "";
			String conditionId = "";
			List<Map> findBySql1 = dao.findBySql(get_wf_id,
					new Object[] { code });
			for (Map map : findBySql1) {
				wfIdUp = (String) map.get("wf_id");
				conditionId = (String) map.get("condition_id");
			}
			List<Map> wfnodesLists = dao.findBySql(get_node_id,
					new Object[] { wfIdUp });
			List list2 = new ArrayList();
			List list3 = new ArrayList();
			List list4 = new ArrayList();
			for (Map map : wfnodesLists) {
				String nodeId = (String) map.get("node_id");
				SysWfNodeCondition sysWC = new SysWfNodeCondition();
				SysWfNodeCondition sysWC2 = new SysWfNodeCondition();
				sysWC.setWF_ID(wfIdUp);
				sysWC.setNODE_ID(nodeId);
				sysWC2.setNODE_ID(nodeId);
				list2.add(sysWC2);
				list3.add(sysWC);
			}
			String[] fields2 = new String[] { "node_id" };
			dao.executeBatchBySql(sql_wf_nodes_del, fields2, list2);
			dao.executeBatchBySql(sql_wf_module_node_del, fields2, list2);
			dao.executeBatchBySql(sql_role_node_del, fields2, list2);
			dao.executeBatchBySql(sql_rule_node_del, fields2, list2);
			dao.executeBatchBySql(sql_sys_wf_node_tolly_action_type_del,
					fields2, list2);
			List<Map> wfcis = dao.findBySql(get_condition_id,
					new Object[] { wfIdUp });
			if (null != conditionId || "".equals(conditionId)) {
				Map map = new HashMap();
				map.put("condition_id", conditionId);
				wfcis.add(map);
			}
			for (Map c : wfcis) {
				SysWfConditionLine line = new SysWfConditionLine();
				String conditioId = (String) c.get("condition_id");
				line.setCONDITION_ID(conditioId);
				list4.add(line);
			}

			// TODO *18**
			List<Map> lineRuleIdList = dao.findBySql(get_line_rule_id,
					new Object[] { wfIdUp });
			List list5 = new ArrayList();
			for (Map m : lineRuleIdList) {
				SysWfLineRule sysWfLineRule = new SysWfLineRule();
				String lineRuleId = (String) m.get("line_rule_id");
				if (lineRuleId != null && !lineRuleId.equals("#")
						&& !lineRuleId.equals("")) {
					sysWfLineRule.setLINE_RULE_ID(lineRuleId);
					list5.add(sysWfLineRule);
				}
			}

			String[] fields4 = new String[] { "condition_id" };
			// TODO 删除入口条件
			dao.executeBatchBySql(sql_sys_wf_conditions_del, fields4, list4);
			dao.executeBatchBySql(sql_sys_wf_conditions_line_del, fields4,
					list4);
			String[] fields3 = new String[] { "wf_id", "node_id" };
			dao.executeBatchBySql(sql_node_conditions_del, fields3, list3);
			dao.executeBySql(sql_wf_flows_del, new Object[] { wfIdUp });

			// TODO *19**
			String[] fields5 = new String[] { "line_rule_id" };
			dao.executeBatchBySql(sql_del_line_rule, fields5, list5);
			dao.executeBatchBySql(sql_del_rule_monitor, fields5, list5);

		} catch (Exception e) {
			regMap.put("error", "删除流程节点失败");
			throw new RuntimeException("删除流程节点失败" + e.getMessage());
		}
		return regMap;
	}

}
