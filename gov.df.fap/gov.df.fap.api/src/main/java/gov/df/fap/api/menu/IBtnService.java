package gov.df.fap.api.menu;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IBtnService {

  Map<String, Object> insertMenuBtn(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> BtnGrid(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> BtnTree(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> delBtn(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> BtnExist(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> BtnCheck(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> insertBtnCheck(HttpServletRequest request, HttpServletResponse response);

  Map<String, Object> delBtnCheck(HttpServletRequest request, HttpServletResponse response);
}
