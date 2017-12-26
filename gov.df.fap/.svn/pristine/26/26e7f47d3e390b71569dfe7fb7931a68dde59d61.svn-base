/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gov.df.fap.controller.wf.activiti;


import java.io.InputStream;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author Tijs Rademakers
 */
@Controller
public class StencilsetRestResource {
  
  @RequestMapping(value="/df/service/editor/stencilset", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
  public @ResponseBody String getStencilset() {
    InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("wf/stencilset.json");
    String json=null;
    try {
    	json= IOUtils.toString(stencilsetStream,"UTF-8");
    } catch (Exception e) {
      throw new ActivitiException("Error while loading stencil set", e);
    }
	JSONObject datajson=new JSONObject(json);
    JSONArray stencilArray= (JSONArray) datajson.get("stencils");
    JSONArray propertiesArray=(JSONArray) datajson.get("propertyPackages");
    
    //begin_处理节点到流程图的映射_2017_04_06
   /* String process_idpackage="";
    String idfieldpackage="";
    String multiinstance_maintablenamepackage="";
    String processnamepackage="";
    for(int i=0;i<propertiesArray.length();i++){
    	JSONObject job = propertiesArray.getJSONObject(i);  
    	if(job.get("name").equals("process_idpackage")){
    		
    		JSONArray jsonArrObj = (JSONArray)job.get("properties");
    		JSONObject jsonObj= (JSONObject)jsonArrObj.get(0);
    		jsonObj.put("value", value);
    		
    	}
    	
    }*/
    
    //end_处理节点到流程图的映射_2017_04_06
    
    
    
    
    
    
    /*Map<String, ParticipantConfig> userServiceImpl=BpmServiceUtils.getBpmEngineConfiguration().getParticipantService().getParticipantConfigs();
    Collection<ParticipantConfig> c = userServiceImpl.values();
    Iterator it = c.iterator();
    for (; it.hasNext();) {
    	ParticipantConfig tempConfig=(ParticipantConfig) it.next();
    	if(!tempConfig.getCode().toLowerCase().equals("defaultuser"))
    	{
		    JSONObject defaultUser=new JSONObject();
		    defaultUser.put("name", tempConfig.getCode().toLowerCase()+"package");
		    JSONArray properties=new JSONArray();
		    JSONObject property=new JSONObject();
		    property.put("id", tempConfig.getCode().toLowerCase());
		    if(tempConfig.getEditType()!=null)
		    	property.put("type", tempConfig.getEditType());
		    else
		    	property.put("type", "Complex");
		    property.put("value", "");
		    property.put("title", tempConfig.getName());
		    property.put("description", tempConfig.getName());
		    property.put("group", "参与人");
		    property.put("required", false);
		    property.put("popular", true);
		    properties.put(property);
		    defaultUser.put("properties", properties);
		    propertiesArray.put(defaultUser);
		    
		    for(int i=0;i<stencilArray.length();i++)
		    {
		    	JSONObject stencilObject=(JSONObject) stencilArray.get(i);
		    	if(stencilObject.get("id").toString().indexOf("ApproveUserTask")!=-1)
		    	{
		    		JSONArray stencilProperites=(JSONArray) stencilObject.get("propertyPackages");
		    		stencilProperites.put(tempConfig.getCode().toLowerCase()+"package");
		    		stencilObject.put("propertyPackages",stencilProperites );
		    		stencilArray.put(i, stencilObject);
		    	}
		    }
	    }
    }*/
    datajson.put("propertyPackages", propertiesArray);
    datajson.put("stencils", stencilArray);
    return datajson.toString();
  }
}
