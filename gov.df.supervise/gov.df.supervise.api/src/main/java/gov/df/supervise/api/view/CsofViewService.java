/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017 北京用友政务软件有限公司
 * </p>
 * <p>
 * Company: 北京用友政务软件有限公司
 * </p>
 * <p>
 * CreateData: 2017-11-15下午8:35:34
 * </p>
 * 
 * @author songlr3
 * @version 1.0
 */
package gov.df.supervise.api.view;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import gov.df.supervise.bean.view.CsofCViewCellEntity;
import gov.df.supervise.bean.view.CsofCViewEntity;
import gov.df.supervise.bean.view.CsofCViewSheetEntity;
import gov.df.supervise.bean.view.CsofSupDataEntity;

public interface CsofViewService {

	void saveCsofCView(CsofCViewEntity csofCView);

	void saveCsofCViewSheet(CsofCViewSheetEntity csofCViewSheet);

	void saveCsofCViewCell(CsofCViewCellEntity csofCViewCell);

	List<CsofCViewCellEntity> selectIsEdit(String sheetId);

	void createTable(String dataTable, String sql);

	void getExcelInfoX(XSSFWorkbook xWb, String viewId, String viewCode,
			String updateflag);

	void getExcelInfoH(HSSFWorkbook hWb, String viewId, String viewCode,
			String updateflag);

	@SuppressWarnings("rawtypes")
	Map<String, List<Map>> getExcel(String viewId);

	@SuppressWarnings("rawtypes")
	void saveExcel(Map<String, List<Map>> mapExcel, String billtypeCode,
			String billId, String objtypeId, String objId, String supCycle,
			String supDate);

	List<CsofCViewEntity> selectAllExcel();

	void deleteAllExcel(String viewId);

	List<Map> getData(Map map);

	void saveExcelDataX(XSSFWorkbook xWb, String viewId, String billtypeCode,
			String billId, String objtypeId, String objId, String supCycle,
			String supDate);

	void saveExcelDataH(HSSFWorkbook hWb, String viewId, String billtypeCode,
			String billId, String objtypeId, String objId, String supCycle,
			String supDate);

	Map<String, List<Map>> getExcelBySheetId(String sheetId);

}
