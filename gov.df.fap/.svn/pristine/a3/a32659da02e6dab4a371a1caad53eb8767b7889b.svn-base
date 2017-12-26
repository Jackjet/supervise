package gov.df.fap.service.subsystem;

import gov.df.fap.api.subsystem.Isubsystem;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.dao.GeneralDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;


@Service
public class SubsystemService implements Isubsystem {

	@Autowired
	@Qualifier("generalDAO")
	private GeneralDAO generalDAO;

	public Map<String, Object> getSubsystemInfo(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> info_map = new HashMap<String, Object>();
		List info_list = new ArrayList();
		try {
			String sql = "select sys_guid,sys_id,sys_name,sys_desc,enabled,version from sys_app ORDER BY sys_id ASC";
			info_list = generalDAO.findBySql(sql, new Object[] {});
			info_map.put("gridData", info_list);
			info_map.put("flag", "successs");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			info_map.put("flag", "error");
			info_map.put("msg","获取数据失败！");
		}

		return info_map;
	}

	public Map<String, Object> deleSubsystemInfo(HttpServletRequest request, HttpServletResponse response) {
		String sys_guid = request.getParameter("sys_guid");
		Map<String, Object> info_map = new HashMap<String, Object>();
		String sql = "delete from SYS_APP where SYS_GUID =?";
		int k = 0;
		try {
			String[] guid = sys_guid.split(",");
			for(int i = 0 ; i < guid.length ; i++){
				generalDAO.executeBySql(sql, new Object[] {guid[i]});
				k++;
			}
			info_map.put("num", k);
			info_map.put("flag", "successs");
			info_map.put("msg", "删除成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			info_map.put("num", k);
			info_map.put("flag", "error");
			info_map.put("msg","删除数据失败！");
		}

		return info_map;
	}

	public Map<String, Object> updSubsystemInfo(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> info_map = new HashMap<String, Object>();  
		//更新list
		String upd_rows = request.getParameter("upd_rows");
		//插入list
		String new_rows = request.getParameter("new_rows");

		JSONArray jsonArray =JSONArray.fromObject(upd_rows);
		JSONArray jsonArrayAddData =JSONArray.fromObject(new_rows);

		try {
			//更新数据处理 - 开始
			Iterator<Object> it = jsonArray.iterator(); 
			while (it.hasNext()) {
				String sql="update sys_app t set ";
				String guid="";
				StringBuffer sb=new StringBuffer();
				Map<String,Object> ob = (Map<String,Object>) it.next();
				for (String key : ob.keySet()) {
					//前端表格加入的特殊字段  此处忽略掉
					if("$_#_@_id".equals(key)) {
						continue;
					}
					String value="";
					Object objValue=ob.get(key);
					//空值特殊处理
					if(objValue instanceof JSONNull) {
						value="";
					} else {
						value=(String)ob.get(key);
					}
					//更新时候根据 sys_guid 进行更新
					if("sys_guid".equals(key)) {
						guid=value;
					} else {
						sb.append(key+"='"+value+"' ,");
					}
				}
				String tempSql=sb.substring(0, sb.length()-1);
				sql+=tempSql;
				sql+=" where t.sys_guid='"+guid+"'";
				generalDAO.executeBySql(sql);
			}
			//更新数据处理 - 结束

			//添加数据处理 - 开始
			Iterator<Object> itAddRows = jsonArrayAddData.iterator(); 
			while (itAddRows.hasNext()) {
				String sqlInsert="insert into sys_app t  ";
				String guid=UUIDRandom.generate();
				//field  字段 字符串拼写
				StringBuffer sbField = new StringBuffer();
				sbField.append("(");
				//value  字段值字符串拼写
				StringBuffer sbValue = new StringBuffer();
				sbValue.append("(");
				Map<String,Object> ob = (Map<String,Object>) itAddRows.next();
				for (String key : ob.keySet()) {
					//前端表格加入的特殊字段  此处忽略掉
					if("$_#_@_id".equals(key)) {
						continue;
					}
					String value="";
					Object objValue=ob.get(key);
					//空值特殊处理
					if(objValue instanceof JSONNull) {
						value="";
					} else {
						value=(String)ob.get(key);
					}

					sbField.append(key+",");
					sbValue.append("'"+value+"',");
				}
				//插入时候  sys_guid 特殊处理
				sbField.append("sys_guid");
				sbValue.append("'"+guid+"'");

				sbField.append(")");
				sbValue.append(")");

				sqlInsert+=sbField.toString();
				sqlInsert+=" values "+sbValue.toString();
				generalDAO.executeBySql(sqlInsert);
			}
			//添加数据处理 - 结束

			info_map.put("flag", "successs");
			info_map.put("msg", "保存成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			info_map.put("flag", "error");
			info_map.put("msg","保存数据失败！");
		}

		return info_map;
	}
}
