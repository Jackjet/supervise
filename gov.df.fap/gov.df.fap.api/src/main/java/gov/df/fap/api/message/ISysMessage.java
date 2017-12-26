package gov.df.fap.api.message;

import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;

/**
 * 日志管理服务端接口
 * @author a
 *
 */
public interface ISysMessage {
  public XMLData findMessage(String userid, String roleid, String plSql, FPaginationDTO page) throws Exception;

  public XMLData findMessage2(String userid, String roleid, String plSql, FPaginationDTO page, int type)
    throws Exception;

  public boolean sendMessage(List msgs, int canceltype) throws Exception;

  public boolean cancelMessage(String msgid, String receivetime, String cancelid, boolean isnewmsg) throws Exception;

  public boolean deleteMessage(String msgid) throws Exception;

  public Map haveNewMessage(String userid, String roleid, String curtime) throws Exception;

  public List findMessageByMessageId(String msgId) throws Exception;

  public void updateMessage(String msgId) throws Exception;

  public byte[] downLoadAttachment(String attm_id, String file_name);
}
