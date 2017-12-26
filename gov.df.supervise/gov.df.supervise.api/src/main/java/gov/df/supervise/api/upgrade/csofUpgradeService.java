package gov.df.supervise.api.upgrade;

import java.util.List;
import java.util.Map;

/**
 * 补丁升级实现类接口
 * @author tongya
 *
 */
public interface csofUpgradeService {
  /**
   * 校验补丁升级密码
   * @return
   */
  public Map<String, Object> getUpdatePswd(String usercode, String pwd);

  /**
   * 查询补丁列表
   */
  public List getUpgrade();

  /**
   * 通过条件查询补丁列表
   */
  public List getUpgradeByIscommit(int iscommit);

  /**
   * 删除待升级的补丁
   */
  public void deleteUpgrade(String id);

  /**
   * 上传
   */
  public void saveUpgrade(String id, String remark);

  /**
   * 执行升级补丁
   */
  public void doUpgrade(String id, String filePath);
}
