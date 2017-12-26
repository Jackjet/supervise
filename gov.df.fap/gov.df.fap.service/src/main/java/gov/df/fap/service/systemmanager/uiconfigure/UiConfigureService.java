package gov.df.fap.service.systemmanager.uiconfigure;

import gov.df.fap.api.dictionary.interfaces.IDDSet;
import gov.df.fap.api.fapcommon.IMenuDfService;
import gov.df.fap.api.systemmanager.uiconfigure.IUIConfigure;
import gov.df.fap.api.systemmanager.uiconfigure.IViewConfigure;
import gov.df.fap.api.util.ISysAppUtil;
import gov.df.fap.api.view.IViewService;
import gov.df.fap.bean.view.SysUimanager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UiConfigureService implements IViewConfigure {

  @Autowired
  ISysAppUtil sysAppUtilBo;

  @Autowired
  IDDSet ddSet;

  @Autowired
  IUIConfigure uiConfigure;

  @Autowired
  IViewService viewService;

  @Autowired
  private IMenuDfService iMenuService;

  public Map<String, Object> initViewCombox() {
    // TODO Auto-generated method stub
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List sysapps = sysAppUtilBo.findAllSysApps();
      List uiTypes = ddSet.getFieldEnumValueList("VIEW_TYPE");
      map.put("sys_id", sysapps);
      map.put("ui_type", uiTypes);

    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return map;

  }

  @Override
  public Map<String, Object> getAllInfoForView(String viewId) {
    // TODO Auto-generated method stub
    Map<String, Object> map = new HashMap<String, Object>();
    List viewDetail = new ArrayList();
    List viewMenuList = new ArrayList();
    List menulist = new ArrayList();
    if (!viewId.equals(null) && !viewId.equals("")) {
      // usedInfo = ddSet.findUIUsedInfoByUIID(viewId);
      viewDetail = uiConfigure.getViewDetailsWithEleName(viewId);

      viewMenuList = viewService.getSysMenuViewByViewId(viewId);
    }

    if (viewMenuList.size() > 0) {
      StringBuffer sqlbuffer = new StringBuffer(" appid='df'  and (");
      for (Map viewMenuMap : (List<Map>) viewMenuList) {
        sqlbuffer.append(" guid = '" + viewMenuMap.get("menu_guid").toString() + "' or ");
      }

      sqlbuffer.delete(sqlbuffer.length() - 3, sqlbuffer.length() - 1);
      sqlbuffer.append(")");
      try {
        menulist = iMenuService.getMenusTreeByWhereSql(sqlbuffer.toString());
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    map.put("menulist", menulist);
    map.put("viewDetail", viewDetail);
    return map;
  }

  @Override
  public Map<String, Object> saveOrUpdateView(SysUimanager entity, String optType) {
    // TODO Auto-generated method stub
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      uiConfigure.saveUIConfigure(entity, optType);
      map.put("flg", "sucess");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      map.put("flg", "fail");
    }
    return map;
  }

  @Override
  public Map<String, Object> delView(String viewId) {
    // TODO Auto-generated method stub
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      uiConfigure.delView(viewId);
      map.put("flg", "sucess");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      map.put("flg", "fail");
    }
    return map;
  }

}
