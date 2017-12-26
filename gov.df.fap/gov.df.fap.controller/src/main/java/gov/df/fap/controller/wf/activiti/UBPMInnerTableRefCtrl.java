/**
 * 
 */
package gov.df.fap.controller.wf.activiti;

import gov.df.fap.api.workflow.activiti.design.IGetInnerTable;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author chouhl
 * @param <V>
 *
 */
@Controller
public class UBPMInnerTableRefCtrl{

	/**
	 * 树请求映射方法
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 * @throws JsonProcessingException
	 * 
	 */
	
	@Autowired
	private IGetInnerTable iGetInnerTable;
	
	@RequestMapping(value = "/df/service/get/innerTable", method = RequestMethod.GET)
	public @ResponseBody String getDatas_Get(HttpServletRequest request,
			HttpServletResponse response) throws SQLException,
			JsonProcessingException, IOException {
		String sql = "select table_code,table_name,id_column_name from sys_tablemanager order by table_code";
		String jsonStr = iGetInnerTable.getDatas_Get(sql);
		return jsonStr;
	}
	
	/*@RequestMapping(value = "/get/innerTableName", method = RequestMethod.GET)
	public @ResponseBody String getInnerTableName(HttpServletRequest request,
			HttpServletResponse response,String tablecode) throws SQLException,
			JsonProcessingException, IOException {
		try {
			Connection con = null;
			Statement stam = null;
			ResultSet rs = null;
			Map map = new HashMap();
			JSONArray jsonArray = new JSONArray();
			Class.forName("oracle.jdbc.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
					"fasp_zbc_0320_df", "1");
			String sql = "select TABLE_NAME from sys_tablemanager where TABLE_CODE='"+tablecode+"'";
			stam = con.createStatement();
			rs = stam.executeQuery(sql);
			while (rs.next()) {
				String TABLE_NAME = rs.getString("TABLE_NAME");
				map.put("TABLE_NAME", TABLE_NAME);
				jsonArray.add(map);
			}
			String string = jsonArray.toString();
			return string;
				
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	
	
	
	
	
	
	

	/*@RequestMapping(value = "/reference/tree/user123", method = RequestMethod.POST)
	public @ResponseBody String getDatas_Post(
			@RequestBody Map<String, Object> data, HttpServletRequest request,
			HttpServletResponse response) {

		

		*//**
		 * jdbc查询sys_module树
		 *//*
		String nodeId = (String) data.get("id");
		int count = 0;
		JSONArray jsonArray = new JSONArray();
		Map map = new HashMap();
		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
		String str = "";
		Connection con = null;
		Statement stam = null;
		ResultSet rs = null;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
					"fasp_zbc_0320_df", "1");
			String sql = "select module_code,module_name from sys_module";
			if ("".equals(nodeId) || null == nodeId || "null".equals(nodeId)) {
				stam = con.createStatement();
				rs = stam.executeQuery(sql);
				while (rs.next()) {
					String moduleCode = rs.getString("module_code");
					String moduleName = rs.getString("module_name");
					map.put("module_code", moduleCode);
					map.put("module_name", moduleName);
				}
				list.add(map);
				for (Map<Object, Object> mc : list) {
					String moduleCode = (String) mc.get("module_code");
					String moduleNode = (String) mc.get("module_name");
					// mc.put("isParent", false);
					for (Map<Object, Object> mc2 : list) {
						String sub2 = (String) mc2.get("module_code");
						sub2 = sub2.substring(0, sub2.length() - 3);
						if (moduleCode.equals(sub2)) {
							mc.put("name", moduleNode);
							mc.put("isParent", true);
							mc.put("id", moduleCode);
							mc.put("nocheck", false);
							mc.put("canselect", true);
							mc.put("user_guid", moduleCode);
							count++;
						}
						mc.put("totalCount", String.valueOf(count));
					}
					jsonArray.add(mc);
				}
				// for (Map<Object, Object> mc : list) {
				// String moduleCode = (String) mc.get("module_code");
				// String moduleNode = (String) mc.get("module_name");
				// moduleCode.substring(0, moduleCode.length() - 3);
				// }

				String string = jsonArray.toString();
				return string;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stam != null) {
					stam.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return str;
	}*/
	
}
