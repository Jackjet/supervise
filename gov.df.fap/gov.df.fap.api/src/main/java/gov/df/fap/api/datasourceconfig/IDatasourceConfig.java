package gov.df.fap.api.datasourceconfig;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IDatasourceConfig {
	public Map saveDatasourceConfig(HttpServletRequest rq,HttpServletResponse rp);
	
	public Map initDatasourceConfig(HttpServletRequest rq,HttpServletResponse rp);
	
	public Map deleteDatasourceConfig(HttpServletRequest rq,HttpServletResponse rp);
	
	public Map getAppData(HttpServletRequest rq,HttpServletResponse rp);
	
	public Map getDataSourceTree(HttpServletRequest rq,HttpServletResponse rp);
	
	public Map saveConfigRelations(HttpServletRequest rq,HttpServletResponse rp);
	
	public Map getConfigRelationsByDS(HttpServletRequest rq,HttpServletResponse rp);
	
	public Map testConnection(HttpServletRequest rq,HttpServletResponse rp);

}
