package gov.df.fap.service.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.emory.mathcs.backport.java.util.Arrays;

public class MenuOrder {

	public Map pubMenuListSort(List menuList){
		List menulistNew = new ArrayList();
		
		for(Map menuMap : (List<Map>) menuList){
			Object[] childArray = new Object[0];
			menuMap.put("child", childArray);
			 if ("0".equals(menuMap.get("parentid"))) {
				 menulistNew.add(menuMap);
	            }
			 for (Map menuMap2 : (List<Map>) menuList) {
	                if (((String)menuMap2.get("parentid")).equals((String) menuMap.get("guid"))) {
	                    if (menuMap.get("child") == null) {
	                    	Object[] child = new Object[1];
	                    	child[0] = menuMap2;
	                    	menuMap.put("child",child);
	                    }else{
	                    	Object[] prtArray = (Object[])menuMap.get("child");
	                    	Object[] newprtArray = Arrays.copyOf(prtArray, prtArray.length+1);
	                    	newprtArray[prtArray.length] = menuMap2;
	                        menuMap.put("child",newprtArray);
	                    }
	                }
	            } 
		}
		if(menulistNew.size()>0){
			return (Map) menulistNew.get(0);
		}else{
			return null;
		}
		
	}
}
