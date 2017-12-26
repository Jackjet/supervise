package gov.df.fap.api.systemmanager.uiconfigure;

import gov.df.fap.bean.view.SysUimanager;

import java.util.Map;

public interface IViewConfigure {

	public Map<String, Object> initViewCombox();

	public Map<String, Object> getAllInfoForView(String viewId);

	public Map<String, Object> saveOrUpdateView(SysUimanager entity,
			String optType);

	public Map<String, Object> delView(String viewId);


}
