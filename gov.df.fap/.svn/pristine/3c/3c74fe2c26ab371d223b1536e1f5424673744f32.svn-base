package gov.df.fap.api.workflow.activiti.design;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

public interface IGetElementVal {
  
    public List<Map> getEleVal(String eleType,String tableName);
    public List<Map> getInEleVal(String tableName);
    public List<Map> findComments(String tableName,String fieldName);
    public List<Map> getEleSourceData(String eleSource);
    public List<Map> getNoElementData(String eleSource);
    
    public void deleteRuleParam(String ruleParamID);
    public void createNoEleParaVal(String paramName_val,String paramDesc_val,String paramCheck_val,String paramvaluetypeInit,String paraType_c);
    public void updateNoEleParaVal(String paramName_val,String paramDesc_val,String paramCheck_val,String paramvaluetypeInit,String paraType_c,String noElePara_id_val);
    
    public String getExpressionBySetting(List<Map> setting) throws Exception;
    public String getDescExpressionBySetting(List<Map> setting) throws Exception;
    
    
    
    

}
