package gov.df.fap.api.portal;

import java.util.List;
import java.util.Map;

public interface IGetPageJsonDataService {
	
	public List getPagingDataList(String ruleId, Map params, int startNum,
			int endNum);
	
	public int getTotalRecordCount(String ruleId, Map params);

	
}
