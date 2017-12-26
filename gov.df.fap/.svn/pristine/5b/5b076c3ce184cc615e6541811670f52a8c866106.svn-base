package gov.df.fap.bean.workflow.activiti;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * 流程定义中参与者配置项
 * @author zhaohb
 *
 */
public class ProcessParticipantItem implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**流程定义中参与者标识*/
	private ProcessParticipantDetail[] details;
	/**参与者配置类型编码*/
	private String type;
	/**参与者限定配置编码*/
	private String[] filters;
	/**参与者扩展信息*/
	private Map<String,String>  others;
	


	public ProcessParticipantItem(){}
	
	public ProcessParticipantItem(ProcessParticipantDetail[] details,String type){
		this.details=details;
		this.type=type;
	}
	public ProcessParticipantItem(ProcessParticipantDetail[] details,String type,String[] filters){
		this.details=details;
		this.type=type;
		this.filters=filters;
	}
	
	public ProcessParticipantDetail[] getDetails() {
		return details;
	}

	public void setDetails(ProcessParticipantDetail[] details) {
		this.details = details;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getFilters() {
		return filters;
	}

	public void setFilters(String[] filters) {
		this.filters = filters;
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
