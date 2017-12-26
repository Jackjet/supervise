package gov.df.fap.bean.workflow.activiti;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 扩展信息
 * @author zhaohb
 *
 */
public class ProcessParticipantDetail implements Serializable{
	private static final long serialVersionUID = 1L;
	/**主键*/
	private String id;
	/**编码*/
	private String code;
	/**名称*/
	private String name;
	/**akey:avalue;bkey:bvalue*/
	private Map<String,String> others;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String,String> getOthers() {
		return others;
	}
	public void setOthers(Map<String,String> others) {
		this.others = others;
	}
	public void addOthers(String key,String value){
		if(key==null||"".equals(key.trim())
				||value==null||"".equals(value.trim())){
			throw  new IllegalArgumentException("'key'||'value' can not be null");
		}
		if(others==null){
			others=new HashMap<String, String>(4);
		}
		
			others.put(key, value);
		
	}
	public String getFromOthers(String key){
		if(key==null||"".equals(key.trim())){
			throw new IllegalArgumentException("'key' can not be null");
		}
		if(others==null||others.size()==0)return null;
		return others.get(key);
	}

}
