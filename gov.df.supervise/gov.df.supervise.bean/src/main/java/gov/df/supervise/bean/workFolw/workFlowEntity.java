package gov.df.supervise.bean.workFolw;

import java.io.Serializable;

public class workFlowEntity implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String jid;

  private String id;

  private String tableName;

  private String status;

  public String getJid() {
    return jid;
  }

  public void setJid(String jid) {
    this.jid = jid;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

}
