package gov.df.fap.api.button;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IButtonConfigService {

  Map<String, Object> initBtnTree(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> BtnGrid(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> insertBtn(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> updateBtn(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> delBtn(HttpServletRequest request, HttpServletResponse response);
}
