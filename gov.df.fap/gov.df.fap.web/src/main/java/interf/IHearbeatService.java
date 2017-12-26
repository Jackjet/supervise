package interf;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.longtu.framework.heartbeat.IHearbeatManager;

public abstract interface IHearbeatService extends IHearbeatManager {
  public abstract boolean hearbeat(String paramString);

  public abstract String reg(String paramString1, String paramString2);

  public abstract Serializable send(String paramString1, String paramString2, Serializable[] paramArrayOfSerializable);

  public abstract Collection<String> getServersByDb(String paramString);

  public abstract String getService2Db(String paramString);

  public abstract void regServerInfo(Map paramMap);
}
