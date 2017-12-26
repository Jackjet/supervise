package gov.df.fap.controller.wf.activiti;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
/**
 * @author chouhl
 *
 * 参照控制基类
 */
@Controller
public class UBPMBaseRefCtrl {
	
	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public ObjectMapper getMapper(){
		return objectMapper;
	}
	/**
	 * 创建ObjectNode对象
	 * @return
	 */
	protected ObjectNode createObjectNode(){
		return objectMapper.createObjectNode();
	}
	
	protected ArrayNode createArrayNode(){
		return objectMapper.createArrayNode();
	}
	
	/**
	 * 组装树节点
	 * @param items
	 * @param map
	 * @return
	 */
	/*@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List<Item> assembleTreeNodes(List<Item> items, Map<Object, Item> map){
		int size = items != null ? items.size() : 0;
		
		for(int i = size - 1; i >= 0; i--){
			if(items.get(i) instanceof PropertysetItem){
				PropertysetItem propItem = (PropertysetItem)items.get(i);
				
				Property prop = propItem.getItemProperty(ReferenceJsonConverterUtil.REFERENCE_TREE_FATHER);
				if(prop == null || prop.getValue() == null || prop.getValue().equals("")){
					propItem.removeItemProperty(ReferenceJsonConverterUtil.REFERENCE_TREE_FATHER);
					propItem.addItemProperty(ReferenceJsonConverterUtil.REFERENCE_TREE_FATHER, new ObjectProperty<String>("root", String.class));
				}else{
					Item father = map.get(prop.getValue());
					if(father != null){
						Property children = father.getItemProperty(ReferenceJsonConverterUtil.REFERENCE_TREE_NODES);
						if(children == null){
							children = new ObjectProperty<List>(new ArrayList<Item>(), List.class);
						}
						
						List<Item> list = (List<Item>)children.getValue();
						list.add(propItem);
						
						father.addItemProperty(ReferenceJsonConverterUtil.REFERENCE_TREE_NODES, children);
					}else{
						propItem.removeItemProperty(ReferenceJsonConverterUtil.REFERENCE_TREE_FATHER);
						propItem.addItemProperty(ReferenceJsonConverterUtil.REFERENCE_TREE_FATHER, new ObjectProperty<String>("root", String.class));
					}
				}
				
				prop = propItem.getItemProperty(ReferenceJsonConverterUtil.REFERENCE_TREE_FATHER);
				if(!prop.getValue().equals("root")){
					items.remove(i);
				}
			}else{
				items.remove(i);
			}
		}
		
		return items;
	}
	
	*//**
	 * 转换PropertysetItem 数据为ObjectNode对象
	 * @param items
	 * @return
	 *//*
	@SuppressWarnings("unchecked")
	protected ObjectNode changeItem2All(List<Item> items){
		ObjectNode root = this.createObjectNode();
		
		ArrayNode nodes = this.createArrayNode();
		
		root.put(ReferenceJsonConverterUtil.REFERENCE_RESULTS, nodes);
		
		int size = items != null ? items.size() : 0;
		
		for(int i = size - 1; i >= 0; i--){
			if(items.get(i) instanceof PropertysetItem){
				List<ValuedDataObject> dataObjects = this.changeItemToVDOs(items.get(i));
				
				ObjectNode dataNode = this.changeVDOs2ObjectNode(dataObjects);
				
			    ArrayNode dataNode_nodes = this.createArrayNode();
			    
			    dataNode.put(ReferenceJsonConverterUtil.REFERENCE_TREE_NODES, dataNode_nodes);
			    
			    nodes.add(dataNode);
			    
			    Property children = items.get(i).getItemProperty(ReferenceJsonConverterUtil.REFERENCE_TREE_NODES);
			    if(children != null){
			    	List<Item> list = (List<Item>)children.getValue();
				    int s = list != null ? list.size() : 0;
				    for(int j = 0; j < s; j++){
				    	this.getChildren(list.get(j), dataNode_nodes);
				    }
			    }
			}
		}
		
		return root;
	}
	
	@SuppressWarnings("unchecked")
	protected void getChildren(Item child, ArrayNode nodes){
		if(!(child instanceof PropertysetItem)){
			return;
		}
		
		List<ValuedDataObject> dataObjects = this.changeItemToVDOs(child);
		
		ObjectNode dataNode = this.createObjectNode();
	    
	    for (ValuedDataObject dObj : dataObjects) {
	    	if(dObj instanceof StringDataObject){
	    		if(dObj.getValue() == null){
	    			dataNode.put(dObj.getId(), "");
	    		}else{
	    			dataNode.put(dObj.getId(), (String)dObj.getValue());
	    		}
	    		//日期类型格式化
	    		
	    		dataNode.put(ReferenceJsonConverterUtil.REFERENCE_DATA_TYPE, dObj.getClass().getSimpleName());
	    	}
	    }
		
	    ArrayNode dataNode_nodes = this.createArrayNode();
	    dataNode.put(ReferenceJsonConverterUtil.REFERENCE_TREE_NODES, dataNode_nodes);
	    
	    nodes.add(dataNode);
	    
	    Property children = child.getItemProperty(ReferenceJsonConverterUtil.REFERENCE_TREE_NODES);
	    if(children != null){
	    	List<Item> list = (List<Item>)children.getValue();
		    int s = list != null ? list.size() : 0;
		    for(int j = 0; j < s; j++){
		    	this.getChildren(list.get(j), dataNode_nodes);
		    }
	    }
	}
	
	*//**
	 * 转换ValueDataObject 集合为ObjectNode对象
	 * @param dataObjects
	 * @return
	 *//*
	protected ObjectNode changeVDOs2ObjectNode(List<ValuedDataObject> dataObjects){
		ObjectNode dataNode = objectMapper.createObjectNode();
	    
	    for (ValuedDataObject dObj : dataObjects) {
	    	if(dObj instanceof StringDataObject){
	    		if(dObj.getValue() == null){
	    			dataNode.put(dObj.getId(), "");
	    		}else{
	    			dataNode.put(dObj.getId(), (String)dObj.getValue());
	    		}
	    		//日期类型格式化
	    		
	    		dataNode.put(ReferenceJsonConverterUtil.REFERENCE_DATA_TYPE, dObj.getClass().getSimpleName());
	    	}
	    }
	    
	    return dataNode;
	}
	
	*//**
	 * 转换记录Item对象为ValueDataObject列表对象
	 * @param item
	 * @return
	 *//*
	protected List<ValuedDataObject> changeItemToVDOs(Item item){
		List<ValuedDataObject> dataObjects = null;
		
		if(item instanceof PropertysetItem){
			PropertysetItem propItem = (PropertysetItem)item;
			Object[] ids = propItem.getItemPropertyIds().toArray();
			int len = ids != null ? ids.length : 0;
			
			dataObjects = new ArrayList<ValuedDataObject>(len);
			
			for(int j = 0; j < len; j++){
				Property prop = propItem.getItemProperty(ids[j]);
				if(prop != null){
					if(prop.getType() != null && prop.getType().isAssignableFrom(String.class)){
						StringDataObject dataObject = new StringDataObject();
						dataObject.setId(String.valueOf(ids[j]));
						dataObject.setName(String.valueOf(ids[j]));
						dataObject.setValue((prop.getValue() != null ? prop.getValue() : ""));
						
						dataObjects.add(dataObject);
						
						if("id".equals(ids[j])){
							dataObject = new StringDataObject();
							dataObject.setId("pk");
							dataObject.setName("pk");
							dataObject.setValue((prop.getValue() != null ? prop.getValue() : ""));
							dataObjects.add(dataObject);
						}
					}
				}
			}
		}else{
			dataObjects = new ArrayList<ValuedDataObject>(0);
		}
		
		return dataObjects;
	}*/
	
}
