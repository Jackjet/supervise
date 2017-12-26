package gov.df.fap.service.datasourceconfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import gov.df.fap.api.datasourceconfig.IDatasourceConfig;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.dao.GeneralDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DataSourceConfigService implements IDatasourceConfig{

	@Autowired
	@Qualifier("generalDAO")
	private GeneralDAO generalDao = null;
	//保存数据源的配置信息
	@Override
	public Map<String, Object> saveDatasourceConfig(HttpServletRequest rq,HttpServletResponse rp){
		Map map = new HashMap();
		String deletesql = "delete sys_appdatasource";
		generalDao.executeBySql(deletesql);
		int flag = 0;		
		String msg = "";
		String data = rq.getParameter("datasouceconfig");
		JSONArray jsonArray = JSONArray.fromObject(data);
		Iterator it = jsonArray.iterator();
		while(it.hasNext()){
			JSONObject obj = (JSONObject)it.next();
			String[] params = new String[10];
			params[1] = (String) obj.get("tablespace");
			params[2] = (String) obj.get("type");
			params[3] = (String) obj.get("ip");
			params[4] = (String) obj.get("port");
			params[5] = (String) obj.get("name");
			params[6] = (String) obj.get("sid");
			params[7] = (String) obj.get("username");
			params[8] = (String) obj.get("passwd");
			params[9] = (String) obj.get("updatetime");
			
			if(obj.containsKey("guid")){
				params[0] = (String) obj.get("guid");
			}else{
				params[0] = UUIDRandom.generate();
			}
			String sql = "insert into sys_appdatasource values (?,?,?,?,?,?,?,?,?,?)";
			flag = generalDao.executeBySql(sql,params);
		}
		if(flag > 0){
			map.put("msg", "保存成功");
		}else{
			map.put("msg", "保存失败");
		}
		return map;
	}
	//获取数据源配置列表
	@Override
	public Map<String, Object> initDatasourceConfig(HttpServletRequest rq,HttpServletResponse rp){
		Map map = new HashMap();
		
		String sql = "select guid guid, tablespace tablespace,ip ip,port port,sid sid,username username,passwd passwd,updatetime updatetime,jndiname name,databasetype type from sys_appdatasource";
		
		List list = generalDao.findBySql(sql);
		map.put("data", list);
		return map;
	}

	//删除数据源配置信息（每次删除一条）
	@Override
	public Map deleteDatasourceConfig(HttpServletRequest rq,
			HttpServletResponse rp) {
		Map map = new HashMap();
		
		String guid =  rq.getParameter("guid");
		String sql = "delete sys_appdatasource where guid = '" + guid + "'";
		generalDao.executeBySql(sql);
		map.put("msg", "删除成功");
		return map;
	}

	//获取业务系统ID和名称
	@Override
	public Map getAppData(HttpServletRequest rq, HttpServletResponse rp) {
		Map map = new HashMap();
		String sql = "select sys_guid guid,sys_id id,(sys_id || ' ' || sys_name) name from sys_app";
		List list = generalDao.findBySql(sql);
		map.put("data", list);
		return map;
	}
	//获取数据源树
	@Override
	public Map getDataSourceTree(HttpServletRequest rq, HttpServletResponse rp) {
		Map map = new HashMap();
		String sql = "select guid id,(jndiname || ' ' || username) name,'1' parentid from sys_appdatasource";
		List list = generalDao.findBySql(sql);
		map.put("data", list);
		return map;
	}
	//保存关联关系
	@Override
	public Map saveConfigRelations(HttpServletRequest rq, HttpServletResponse rp) {
		Map map = new HashMap();
		String data = rq.getParameter("data");
		JSONObject obj = JSONObject.fromObject(data);
		String datasource = (String) obj.get("datasource");
		String apps = (String) obj.get("apps");
		String[] app = apps.split("#");
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String deletesql = "delete sys_appdatasource_relations where datasource = '" + datasource + "'";
		generalDao.executeBySql(deletesql);
		for(String appguid : app){
			String guid = UUIDRandom.generate();
			String sql = "insert into sys_appdatasource_relations values('" + guid + "','" + appguid + "','" + datasource + "','" + dateFormat.format(now).toString() + "')";
			generalDao.executeBySql(sql);
		}
		map.put("msg", "保存成功");
		return map;
	}
	
	//通过数据源获取关联关系
	@Override
	public Map getConfigRelationsByDS(HttpServletRequest rq,
			HttpServletResponse rp) {
		Map map = new HashMap();
		String datasource = rq.getParameter("id");
		String sql = "select appguid id from sys_appdatasource_relations where datasource = '" + datasource + "'";
		List list = generalDao.findBySql(sql);
		map.put("data", list);
		return map;
	}
	
	//测试数据库连接
	@Override
	public Map testConnection(HttpServletRequest rq,
			HttpServletResponse rp) {
		Map map = new HashMap();
		boolean flag = false;
		JSONArray jsonArray = JSONArray.fromObject(rq.getParameter("data"));
		Iterator it = jsonArray.iterator();
		while(it.hasNext()){
			JSONObject obj = (JSONObject)it.next();
			Connection conn = null;
			if(obj.get("type").equals("oracle")){
				try {
				      Class.forName("oracle.jdbc.driver.OracleDriver");
				    } catch (ClassNotFoundException e) {
				      e.printStackTrace();
				    }
				    try {
				      conn = DriverManager.getConnection("jdbc:oracle:thin:@" + obj.get("ip") + ":" + obj.get("port") + ":orcl", obj.getString("username"), obj.getString("passwd"));
				      Statement stmt = conn.createStatement();
				      flag = stmt.execute("select 1 from dual");
				    } catch (SQLException e) {
				      e.printStackTrace();
				    }
			}
		}
		
		
	    if(flag){
	    	map.put("msg", "测试成功");
	    }else{
	    	map.put("msg", "测试失败");
	    }
		return map;
	}
	
}
