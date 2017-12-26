package gov.df.fap.api.elementConfig;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IElementConfigService {

  Map<String, Object> init(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> sysGrid(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> entertree(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> listtree(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> deleteEle(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> insertEle(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> updateEle(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> queryenter(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> updateEleCode(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> dataElementTree(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> dataElementTree1(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> eleGridQuery(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> insertEleData(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> updateEleData(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> delEleData(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> queryFormData(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> getElementDetailTree(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> getElementSourceTree(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> getElementDetailGrid(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> getElementSourceGrid(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> getElementColumn(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> insertElementColumn(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> queryElementColumn(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> updateElementColumn(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> getElementForm(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> updateElementDate(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> updateEnableDate(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> updateElementData1(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> delElementData1(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> delElementData2(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> delElementData3(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> updateNodataElementColumn(HttpServletRequest request, HttpServletResponse response);
}
