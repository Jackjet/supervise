package gov.df.fap.install;

import javax.sql.DataSource;

import com.longtu.framework.datasource.SystemDataSourceManager;
import com.longtu.framework.install.InstallConfig;

public class DfInstallConfig implements InstallConfig {

  @Override
  public void expandPartition() {

  }

  @Override
  public String getAppid() {
    return "df";
  }

  @Override
  public String getInstallScriptURI() {
    return "script.df";
  }

  @Override
  public String getInstallVersion() {

    return "V1_0_0_0";
  }

  @Override
  public String getSpecial() {
    return null;
  }

  @Override
  public DataSource getTagDataSource(String ds) {
    return SystemDataSourceManager.getDataSource(ds);
  }

  @Override
  public boolean needReboot() {

    return false;
  }

}
