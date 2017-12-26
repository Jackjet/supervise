import java.util.Collection;
import java.util.Map;

public abstract interface ILoadAppid {
  public abstract Map<String, Map<String, CommonDTO>> getAppCommons();

  public abstract Map<String, CommonDTO> getAllApp();

  public abstract Collection<String> getAppid();

  public abstract Collection<CommonDTO> getAppDTO();

  public abstract boolean isCommon();

  public abstract void setThisIsCommon();

  public abstract CommonDTO getDTOByAppid(String paramString);

  public abstract CommonDTO getDTOByAppid(String paramString1, String paramString2, String paramString3);

  public abstract String getDomainName();

  public abstract Map<String, String> getServerInfo();

  public abstract String getServerArguments();

  public abstract String getRootPath();

  public abstract void setContentRootPath(String paramString);

  public abstract String getContentRootPath();

  public abstract void initAppupgrade();

  public abstract boolean isAppupgrade();

  public abstract String getGuid();

  public abstract String getDbGuid();

  public abstract String getServerGuid();

  public abstract String getZkAddress();

  public abstract boolean isZkStrat();
}