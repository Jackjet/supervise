package gov.df.fap.bean.workflow.activiti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;
/**
 * 流程定义参与者
 * @author zhaohb
 *
 */
public class ProcessParticipant implements Serializable{
	private static final long serialVersionUID = 1L;
	/**每一种参与者是唯一的*/
	private ProcessParticipantItem[] processParticipantItems;
	/**
	 * 过滤器类型
	 */
	private String[] filters;
	
	public ProcessParticipant(){
		
	}
	public void addProcessParticipantItem(ProcessParticipantItem processParticipantItem){
		List<ProcessParticipantItem> list=new ArrayList<ProcessParticipantItem>();
		if(processParticipantItems!=null&&processParticipantItems.length>0){
			for (ProcessParticipantItem processParticipantItem2 : list) {
				list.add(processParticipantItem2);
			}
		}
		list.add(processParticipantItem);
		processParticipantItems=list.toArray(new ProcessParticipantItem[0]);
	}
	public ProcessParticipantItem getProcessParticipantItem(String type){
		if(processParticipantItems==null||processParticipantItems.length==0)return null;
		for (ProcessParticipantItem processParticipantItem : processParticipantItems) {
			if(type.equals(processParticipantItem.getType())){
				return processParticipantItem;
			}
		}
		return null;
	}
	public ProcessParticipant(ProcessParticipantItem[] processParticipantItems){
		this.processParticipantItems=processParticipantItems;
	}
	
	public ProcessParticipantItem[] getProcessParticipantItems() {
		return processParticipantItems;
	}
	public void setProcessParticipantItems(
			ProcessParticipantItem[] processParticipantItems) {
		this.processParticipantItems = processParticipantItems;
	}
	public String[] getFilters() {
		return filters;
	}
	public void setFilters(String[] filters) {
		this.filters = filters;
	} 
	
	public String toJSONStr(){
		if(processParticipantItems==null||processParticipantItems.length==0)return "";
		for (ProcessParticipantItem processParticipantItem : processParticipantItems) {
			int num=0;
			if(processParticipantItem!=null)
			{
				String type=processParticipantItem.getType();
				for (ProcessParticipantItem processParticipantItem2 : processParticipantItems) {
					if(processParticipantItem2!=null)
					{
						String type2=processParticipantItem2.getType();
						if(type.equals(type2)){
							num++;
						}
					}
				}
			}
			if(num>1){
				throw new RuntimeException("同一类型的参与者不能重复出现！");
			}
		}
		JSONObject  jsonObj =JSONObject.fromObject(this);
		return jsonObj.toString();
	}
}
