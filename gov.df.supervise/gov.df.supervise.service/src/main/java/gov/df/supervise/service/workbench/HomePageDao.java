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
 * CreateData: 2017-8-25上午9:57:11
 * </p>
 * 
 * @author songlr3
 * @version 1.0
 */
package gov.df.supervise.service.workbench;

import gov.df.supervise.service.common.persistence.annotation.MyBatisDao;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface HomePageDao {

  List<?> selectToDo(Map<String, Object> param);

  List<?> selectGongaoTitle();

  List<?> selectGongaoById(String Id);

  /**
   * 查询公告
   */
  public List getBulletin();
}
