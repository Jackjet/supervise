package gov.df.fap.service.gl.core.daosupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A SQL Class, describe the mapping relation between sql statement and JavaBean property.
 * @author 
 * @version 
 */
public class PreparedForBeanSql {

	String[] propertyNames = null;
	String preparedSql = null;

	public PreparedForBeanSql(String specifiedSql){
		parseSql(specifiedSql);
	}
	
	private void parseSql(String sql){
		List propertyList = new ArrayList();
		StringBuffer sb = new StringBuffer(sql);
		int index = -1;
		int nextIndex = 0;
		boolean propertyEnd = true;
		while(index < sb.length()){
			nextIndex = sb.indexOf("#", index+1);
			if (nextIndex > -1){
				propertyEnd = !propertyEnd;
				if (propertyEnd){
					propertyList.add(sb.substring(index+1, nextIndex));
					sb.replace(index, nextIndex+1, "?");
					continue;
				}
			}else{
				break;
			}
			index = nextIndex;
		}

		if (!propertyEnd)
			throw new RuntimeException("\"#\" illegal!in sql :"+sql);
		
		this.preparedSql = sb.toString();
		this.propertyNames = (String[]) propertyList.toArray(new String[propertyList.size()]);
	}
	
	public String getPreparedSql() {
		return preparedSql;
	}

	public String[] getPropertyNames() {
		return propertyNames;
	}

	public int propertyCount(){
		return propertyNames.length;
	}
	
	public static void main(String[] args){
		
		new PreparedForBeanSql("select * from gl_bus_voucher_detail where vou_id=#property1# and ds_id=#property2#");
		try{
			new PreparedForBeanSql("select * from gl_bus_voucher_detail where vou_id=#property1# and ds_id=#");
		}catch(Exception e){
			e.printStackTrace();
		}
		new PreparedForBeanSql("#select * from gl_bus_voucher_detail where vou_id=#property1# and ds_id=#fdsalkfasdf ##");
		
	}
}
