/*
 * $Id: IUIConfigure.java 198738 2012-04-11 08:32:02Z wuleia $
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.api.systemmanager.uiconfigure;

import gov.df.fap.bean.view.SysUimanager;
import gov.df.fap.util.xml.XMLData;

import java.sql.SQLException;
import java.util.List;


/**
 * 界面配置服务端接口
 * 
 * @author zhadaopeng
 * 
 */
public interface IUIConfigure {
	
	/**
	 * 得到所有界面配置视图详细数据
	 * @param viewId
	 * @return List
	 */
	public List getViewDetails(String viewId);
	
	
	public List getViewDetailsWithEleName(String viewId);
	/**
	 * 得到所有界面配置视图数据
	 * 
	 * @return List 界面配置信息信息（具体内容参考表sys_uimanager）
	 */
	public List getUIManagers(String sql);

	/**
	 * 根据任务ui_code得到所有界面配置视图数据
	 * 
	 * @param ui_code
	 *            int ui_code
	 * @return XMLData 界面配置信息信息（具体内容参考表sys_uimanager）
	 * @throws Exception
	 *             向前抛出sql错误
	 * @author 朱建
	 */
	public XMLData getAllByUiCode(String ui_code) throws SQLException;

	/**
	 * 得到所有界面配置视图详细数据
	 * 
	 * @return List
	 */
	public List getUIDetails(String ui_id);

	/**
	 * 得到所有界面配置视图高级属性数据
	 * 
	 * @return List
	 */
	public List getUIDetailConf(String ui_id);

	/**
	 * 得到所有界面配置视图高级属性配置数据
	 * 
	 * @return List
	 */
	public List getUIConf(String uiconf_type);

	/**
	 * 根据流程得到所有业务表数据
	 * 
	 * @return List
	 */
	public List getTables();

	/**
	 * 根据流程得到所有业务表数据
	 * 
	 * @return List
	 */
	public List getUIDetailsByTable(String table_name, String ui_id);

	/**
	 * 保存界面配置视图数据
	 * 
	 * @param xmlData
	 * @throws Exception
	 */
	public void saveorUpdateUIConfigure(XMLData xmlData) throws Exception;

	/**
	 * 删除界面配置视图数据
	 * 
	 * @param wf_id
	 * @throws Exception
	 */
	public void deleteUIConfigure(String wf_id) throws Exception;

	/**
	 * ============以下篇幅为“功能视图关系管理”相关代码*==============/
	 * 
	 * /** 得到所有功能数据
	 */
	public List getAllSysModules() throws Exception;
	
	public List findAllSysApps() throws Exception;
	
	/**
	 * 得到所有操作数据
	 * 
	 * @return	所有操作数据的List
	 */
	public List getAllSysActions() throws Exception;

	/**
	 * 根据功能ID查找相关联的界面视图
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public List findSysModuleViewByModuleId(String moduleId) throws Exception;
	
	/**
	 * 根据操作ID查找相关联的界面视图
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public List findSysActionViewByActionId(String actionId) throws Exception;

	/**
	 * 根据功能ID修改sys_module_view
	 * 
	 * @param moduleId
	 * @param newViews
	 * @throws Exception
	 */
	public void updateSysModuleViewByModuleId(String moduleId, List newViews,
			String flag) throws Exception;
	
	/**
	 * 根据操作ID修改sys_action_view
	 * 
	 * @param moduleId
	 * @param newViews
	 * @throws Exception
	 */
	public void updateSysActionViewByActionId(String actionId, List newViews,
			String flag) throws Exception;

	/**
	 * 删除一条sys_module_view记录
	 * 
	 * @param data
	 */
	public void deleteSysModuleView(XMLData data, List brotherViews)
			throws Exception;
	
	/**
	 * 删除一条sys_action_view记录
	 * 
	 * @param data
	 */
	public void deleteSysActionView(XMLData data, List brotherViews)
			throws Exception;

	/**
	 * 批量修改功能视图显示顺序
	 * 
	 * @param viewList
	 * @throws Exception
	 */
	public void batchUpdateModuleViewDispOrder(List viewList) throws Exception;

	/**
	 * 修改视图的显示序号
	 * 
	 * @param rowData
	 *            修改的数据
	 * @throws Exception
	 */
	public void modifyViewOrder(XMLData rowData, String order) throws Exception;
	
	/**
	 * 修改操作视图的显示序号
	 * 
	 * @param rowData
	 *            修改的数据
	 * @throws Exception
	 */
	public void modifyActionViewOrder(XMLData rowData, String order) throws Exception;

	/**
	 * 从UI_Code获取UI_Id
	 * 
	 * @return List
	 */
	public List getUIIdByUICode(String ui_code);

	/**
	 * 根据uiId查询视图明细
	 * 
	 * @param uiId
	 * @author lwq
	 * @return
	 */
	public List getViewDetailByUIId(String uiId);

	/**
	 * 根据uiconfId查询视图明细
	 * 
	 * @param uiId
	 * @author lwq
	 * @return
	 */
	public List getSuperViewDetailByConfId(String uiConfId);

	/**
	 * 根据ui_id查找视图的引用信息
	 * 
	 * @param ui_id
	 * @author wanghongtao
	 * @return
	 */
	public List findViewUsedInfoByUIID(String ui_id);

	/**
	 * 根据参数ui_code查找它下层的最大ui_code
	 * 
	 * @param ui_code
	 * @return
	 */
	public String findMaxUiCodeByParentUiCode(String ui_code);

	/**
	 * 根据ui_id查找视图的所有信息
	 * 
	 * @param ui_id
	 * @return
	 */
	public XMLData findViewAllInfo(String ui_id);
	
	/**
	 * 以数组类型返回对应明细列的默认值
	 * 
	 * @param detailData
	 *            明细列信息
	 * @return 数组格式的默认值
	 */
	public String[] getMultiValueAsArray(String ui_detail_id);
	
	/**
	 * 判断在区划内是否存在相同的视图编号 
	 * add by wl 20110327 增加视图时同一区划内编码不允许相同、不同区划内编码允许相同
	 * @param ui_code
	 * @return
	 */
	public boolean isExistTheSameUiCode(String ui_code);
	
	/***
	 * 新增保存视图数据 
	 * @param entity
	 * @throws Exception 
	 */
	public void saveUIConfigure(SysUimanager entity,String optType) throws Exception;


	public void delView(String viewId);
	
}
