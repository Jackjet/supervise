package gov.df.fap.api.fapcommon;

import java.util.List;

/*
 * 菜单相关接口
 */
public interface IMenuDfService {
  public List getMenusTreeByWhereSql(String whereSql);

  public List getMenuByCode(String menuCode);
}
