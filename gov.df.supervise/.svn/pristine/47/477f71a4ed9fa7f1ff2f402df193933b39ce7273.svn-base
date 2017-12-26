package gov.df.supervise.service.common;

import gov.df.fap.api.workflow.IBillTypeServices;
import gov.df.fap.bean.log.LogDTO;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.util.xml.XMLData;
import gov.df.supervise.api.common.CommonActionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ufgov.ip.apiUtils.UUIDTools;

@Service
public class CommonActionBo implements CommonActionService {
	
	@Autowired
	@Qualifier("generalDAO")
	GeneralDAO dao;
	
	@Autowired
	private IBillTypeServices billTypeService;
	
	public Map<String,Object> action(String action, String billtype_code , String pageInfo, Map operationMap ,
			List batchData ,Map conditionMap , Map conditionRela){
		String errorMsg = "";
		String errorCode = "0";
		String action_name = "";
		String log_remark = "";
		boolean flag = true;
	    Map result = new HashMap();
		LogDTO logData = new LogDTO();
	    Map billInfo = getBillInfo(billtype_code , operationMap);
	    
		Map<String,Object> message = new HashMap<String,Object>();
		try {
			if(!billInfo.equals("table_name")){
				if(action.equals("B")){
					action_name = "批量新增";
					saveBatachData(billtype_code,batchData);
			    }else{
			    	if(action.equals("I")){
			    		action_name = "新增";
						saveData(billInfo, operationMap);
					}else if (action.equals("D")){
						action_name = "删除";
						deleteData(billInfo, conditionMap,conditionRela);
					}else if (action.equals("U")){
						action_name = "修改";
//						updateData(billInfo, conditionMap, operationMap,conditionRela);
						updateDataByFiled(billInfo, conditionMap, operationMap,conditionRela);
					}else if (action.equals("Q")){
						action_name = "查询";
						result = getData( billInfo , pageInfo , conditionMap , conditionRela);
						message.put("dataDetail", result.get("dataDetail"));
						message.put("totalElements", result.get("totalElements"));
					}
			    }
	    		logData.setAction_name(action_name);

			}else {
				errorCode = "1";
				errorMsg = "系统无法获取单据表";
			}
			message.put("flag", flag);
			message.put("errorCode", errorCode);
			message.put("errorMsg", errorMsg);
		}catch (Exception e) {
		    e.printStackTrace();
		    message.put("errorCode", "1");
		    errorMsg = "获取数据出现异常";
		    message.put("errorMsg", errorMsg);
		}
		logData.setRemark("表:["+billInfo.get("table_name")+"],异常:["+errorMsg+"]");
        
		return message ;
	}
	
	/**
	 * 新增
	 * @param tableName
	 * @param saveData
	 * @return
	 */
	public int saveData (Map billInfo , Map saveData) {
		int resultNum = 0;
		String field_name = billInfo.get("field_name").toString();
		String table_name = billInfo.get("table_name").toString();
		
		//如果新增数据没有主键则新生成主键
		if((null == saveData.get(field_name.toLowerCase()) || saveData.get(field_name.toLowerCase()).equals("")) && 
				(null == saveData.get(field_name.toUpperCase()) || saveData.get(field_name.toUpperCase()).equals(""))){
			saveData.put(field_name, UUIDTools.uuidRandom());
		}
		
		if((null!=saveData.get("BILL_NO") && saveData.get("BILL_NO").equals(""))
				|| (null!=saveData.get("bill_no") && saveData.get("bill_no").equals(""))){
			saveData.put("bill_no", billInfo.get("bill_no"));
		}
		
		String sql = getInsertSql (table_name, saveData);
		resultNum = dao.executeBySql(sql);
		return resultNum;
	}
	
	/**
	 * 批量新增
	 * @param tableName 新增表名
	 * @param saveList  新增数据集合
	 * @return 新增条目数
	 */
	public int saveBatachData (String billtype_code , List saveList) {
		List batchSql = new ArrayList();

		for ( int i=0; i<saveList.size(); i++ ) {
			Map saveData = (Map) saveList.get(i);
		    Map billInfo = getBillInfo(billtype_code , saveData);

			String table_name = billInfo.get("table_name").toString();
			String field_name = billInfo.get("field_name").toString();
			//如果新增数据没有主键则新生成主键
			if((null == saveData.get(field_name.toLowerCase()) || saveData.get(field_name.toLowerCase()).equals("")) && 
					(null == saveData.get(field_name.toUpperCase()) || saveData.get(field_name.toUpperCase()).equals(""))){
				saveData.put(field_name, UUIDTools.uuidRandom());
			}
			
			if((null!=saveData.get("BILL_NO") && saveData.get("BILL_NO").equals(""))
					|| (null!=saveData.get("bill_no") && saveData.get("bill_no").equals(""))){
				
				saveData.put("bill_no", billInfo.get("bill_no"));
			}
			
			String sql = getInsertSql (table_name, saveData);
			batchSql.add(sql);
		}
		
		int [] resultNum = dao.executeBatchBySql(batchSql);
		return resultNum.length ;
	}
	
	/**
	 * 删除
	 * @param tableName  删除表名
	 * @param conditionMap  删除数据过滤条件Map集合
	 * @return 数据执行条目数
	 */
	public int deleteData (Map billInfo , Map conditionMap ,Map conditionRela) {
		String condition = getCondition (conditionMap ,conditionRela);
		String table_name = billInfo.get("table_name").toString();

		String sql = " DELETE FROM " + table_name + " WHERE 1=1 " + condition;
		return dao.executeBySql(sql) ;
	}
	
	/**
	 * 修改 先删除后新增,受事务控制
	 * @param tableName 表名
	 * @param conditionMap 修改过滤条件
	 * @param saveData 修改字段
	 * @return
	 */
	@Transactional(readOnly = false)
	public int updateData (Map billInfo , Map conditionMap , Map saveData,Map conditionRela) {
		int resultNum = 0 ;
		String field_name = billInfo.get("field_name").toString();

		if(conditionMap.isEmpty() && null!= saveData.get(field_name)){
			conditionMap.put(field_name, saveData.get(field_name).toString());
		}else{
			return 0;
		}
		deleteData(billInfo, conditionMap,conditionRela);
		
		resultNum = saveData(billInfo , saveData);
		
		return resultNum ;
	}
	
	/**
	 * 修改 先删除后新增,受事务控制
	 * @param tableName 表名
	 * @param conditionMap 修改过滤条件
	 * @param saveData 修改字段
	 * @return
	 */
	public int updateDataByFiled (Map billInfo , Map conditionMap , Map updateData,Map conditionRela) {
		int resultNum = 0 ;
		String field_name = billInfo.get("field_name").toString();
        String condition = getCondition(conditionMap,conditionRela);
		if(conditionMap.isEmpty() && null!= updateData.get(field_name)){
			conditionMap.put(field_name, updateData.get(field_name).toString());
		}else{
			return 0;
		}
		
		Iterator iter = updateData.entrySet().iterator();
		String setStr = "";
		while( iter.hasNext() ) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = entry.getKey().toString();

			if(entry.getValue() instanceof String){
				setStr += key +" = '"+entry.getValue().toString()+"' ,";
			}else{
				setStr += key +" = "+entry.getValue()+" ,";
			}
		}
		if(!setStr.equals("")){
			setStr = setStr.substring(0, setStr.lastIndexOf(","));
		}
		String  sql ="update "+field_name+" set "+setStr +condition;

		resultNum = dao.executeBySql(sql);
		
		return resultNum ;
	}
	
	/**
	 * 分页查询
	 * @param tableName
	 * @param pageInfo
	 * @param conditionMap
	 * @return
	 */
	public Map getData (Map billInfo , String pageInfo, Map conditionMap , Map conditionRela) {
		Map resultMap = new HashMap();
		List resultList = new ArrayList();
		List allList = new ArrayList();
		String condition = getCondition(conditionMap,conditionRela);
		String view_name = billInfo.get("view_name")!=null?billInfo.get("view_name").toString():billInfo.get("table_name").toString();

		String sql = "SELECT * FROM " +view_name+ " WHERE 1=1 " + condition ;
		resultList = dao.findByPageSql(pageInfo, sql);
		int sum  = dao.getCount(sql);
		resultMap.put("dataDetail", resultList);
		resultMap.put("totalElements", sum);

		return resultMap;
	}
	
	/**
	 * 获得插入sql
	 * @param tableName 表名
	 * @param saveData  保存数据
	 * @return sql
	 */
	public String getInsertSql(String tableName , Map <String,String>saveData ) {
				
		String keyStr = "";
	    String valueStr = "";
	    
	    for (String key : saveData.keySet()) {
	      keyStr += key + ",";
	      valueStr += "'" + saveData.get(key) + "',";
	    }
	    
	    if (!("".equals(keyStr))) {
	      keyStr = keyStr.substring(0, keyStr.length() - 1);
	    }
	    if (!("".equals(valueStr))) {
	      valueStr = valueStr.substring(0, valueStr.length() - 1);
	    }
	    
	    return "insert into " + tableName + "(" + keyStr + ") values (" + valueStr + ")";
	}
	
	/**
	 * 单一查询条件拼接 只满足 and A = B 情况
	 * @param conditionMap 查询条件Map key为条件字段 value为查询字段值
	 * @return 拼接的查询条件
	 */
	public  String getCondition (Map conditionMap,Map conditionRela) {
		StringBuffer condition = new StringBuffer("");
		
		if( !conditionMap.isEmpty() ) {
			
			Iterator iter = conditionMap.entrySet().iterator();
			
			while( iter.hasNext() ) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = entry.getKey().toString();
				String relation = (String)conditionRela.get(key);
                if(!key.equals("")){
                	
                	String value = entry.getValue().toString();
    				if(!value.equals("")){
    					if(key.equals("IDS")){
    						if(!value.contains("'")){
    							String  [] values = value.split(",");
    							String v = "";
    							for(int i = 0 ;i<values.length; i++){
    								v += "'"+values[i]+"',";
    							}
    							value = v.substring(0, v.lastIndexOf(","));
    						}
    					}
    					String oldKey =key;
    					if(key.endsWith("_date")){
    						key="substr("+key+",1,10)";
    					}
    					String con_str ="";
    					if(relation==null||"".equals(relation)){
    						relation = "0";
    					}
    					if(relation.equals("-1")){
    						con_str = " in ("+value+")";
    					}else if(relation.equals("0")){
    						con_str = " = '"+value+"'";
    					}else if(relation.equals("1")){
    						con_str = " like '%"+value+"%'";
    					}else if(relation.equals("2")){
    						con_str = " like '"+value+"%'";
    					}else if(relation.equals("3")){
    						con_str = " <= '"+value+"'";
    					}else if(relation.equals("4")){
    						con_str = " <= "+value;
    					}else if(relation.equals("5")){
    						con_str = " >= '"+value+"'";
    					}else if(relation.equals("6")){
    						con_str = " >= "+value;
    					}else if(relation.equals("7")){
    						String [] array = value.split(oldKey);
    						con_str = " >= "+array[0]+"  and "+key+" <= "+array[0];
    					}else if(relation.equals("8")){
    						String [] array = value.split(oldKey);
    						con_str = " >= '"+array[0]+"' and "+key+" <= '"+array[1]+"'";
    					}
    					condition.append(" and " + key+con_str);
    				}
					
				}
				
			}
			
		}
		
		return condition.toString();
	}
	
	/**
	 * 模糊查询条件拼接
	 * @param likeConditionMap
	 * @return
	 */
	public static String getLikeCondition (Map likeConditionMap) {
		StringBuffer condition = new StringBuffer("");
		
		if( !likeConditionMap.isEmpty() ) {
			
			Iterator iter = likeConditionMap.entrySet().iterator();
			
			while( iter.hasNext() ) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				condition.append(" and " + key + " like '%" + value + "%'");
			}
			
		}
		
		return condition.toString();
	}

	/**
	 * 查询单据信息
	 * @param billtype_code 单据编码
	 * @param data 要生成单据信息所需的实体数据
	 * @return 单据信息Map集合
	 */
	public Map getBillInfo(String billtype_code, Map data) {
	  
		Map<String, String> billInfo = new HashMap<String, String>();
		try {
			if(!data.isEmpty()){
				String bill_no = billTypeService.getBillNo(billtype_code, data);
				billInfo.put("bill_no", bill_no);
			}
			
			billInfo.put("billtype_code", billtype_code);
			List billList = dao.findBySql(" select  b.billtype_id , b.field_name,b.billtype_code , b.billtype_name ," +
      									  " b.table_name , b.view_name   from sys_billtype b where b.billtype_code = ? ",
      									  new Object[] { billtype_code });
      
			if (billList.size() == 1) {
				XMLData billData = (XMLData) billList.get(0);
				if ( null != billData.get("billtype_name")) {
					billInfo.put("billtype_name", billData.get("billtype_name").toString());
				}
				if ( null != billData.get("table_name")) {
					billInfo.put("table_name", billData.get("table_name").toString());
				}
				if ( null != billData.get("view_name")) {
					billInfo.put("view_name", billData.get("view_name").toString());
				}
				if ( null != billData.get("field_name")) {
					billInfo.put("field_name", billData.get("field_name").toString());
				}
			}else {
				billInfo.put("errorMessage", "无法查询到唯一对应单据");
			}

		} catch (Exception e) {
			billInfo.put("errorMessage", "查询出现异常！");
			e.printStackTrace();
		}

		return billInfo;
	}

}
