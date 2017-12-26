package gov.df.fap.service.message;

import gov.df.fap.api.message.ISysMessage;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.bean.workflow.FAttachmentDTO;
import gov.df.fap.bean.workflow.FMessageDTO;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.SQLUtil;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.ftp.AttachmentTransferFactory;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 日志管理服务端接口实现类
 * 
 * @author a
 * 
 */
@Service
public class SysMessageBO implements ISysMessage {

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao;

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  /**
   * 获得符合条件的数据，以List格式返回
   * 
   * @param pageIndex
   *            分页当前页
   * @param pageRows
   *            每页条数
   * @param condition
   *            查询条件
   * @param table
   *            表名
   * @param state
   *            指标状态 return 以row为单位的指标
   */
  public XMLData findMessage(String userid, String roleid, String plSql, FPaginationDTO page) throws Exception {
    XMLData data = new XMLData();
    try {
      int pageIndex = page.getCurrpage();
      int pageRows = page.getRows();
      String sql = "select * from ( select "
        + (TypeOfDB.isOracle() ? "rownum" : "@rn:=@rn+1")
        + " rn, u.user_name fromuser,m.sent_time, u2.user_name,"
        + " r.role_name,"
        + (TypeOfDB.isOracle() ? " sysdate " : " sysdate() ")
        + " receive_time,m.cancel_relation_id, "
        + " m.msg_content,m.msg_type_code,m.send_type,m.msg_id,m.fromuser fromuserid ,m.attm_id, "
        + " case when m.attm_id is null then null else (select sma.file_name from sys_message_attachment sma where sma.attm_id = m.attm_id) end file_name, "
        + " m.msg_title from " + (TypeOfDB.isOracle() ? "" : " (select @rn:=0 ) r, ") + " sys_message"
        + Tools.addDbLink() + " m left join sys_usermanage" + Tools.addDbLink()
        + " u on m.fromuser=u.user_id left join sys_usermanage" + Tools.addDbLink()
        + " u2 on m.user_id=u2.user_id left join sys_role" + Tools.addDbLink()
        + " r on m.role_id=r.role_id where m.user_id=? and m.role_id=? order by m.sent_time) tab where tab.rn > "
        + (pageIndex - 1) * pageRows + " and tab.rn <= " + pageIndex * pageRows;

      String countSql = "select count(*) from sys_message" + Tools.addDbLink() + " s where s.user_id=? and role_id=? ";

      Integer totalCount = new Integer(0);
      // 查询并返回数据
      List rows = dao.findBySql(sql, new Object[] { userid, roleid });
      // 获得总数据条数
      if (countSql != null && !countSql.equals("")) {
        List count = dao.findBySql(countSql, new Object[] { userid, roleid });
        totalCount = new Integer((String) ((XMLData) count.get(0)).get("count(*)"));
      }
      data.put("total_count", totalCount);
      data.put("row", rows);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return data;
  }

  public XMLData findMessage2(String userid, String roleid, String plSql, FPaginationDTO page, int type)
    throws Exception {
    String condition = "";
    String rg_code = SessionUtil.getRgCode();
    String set_year = SessionUtil.getLoginYear();
    // 未读消息
    if (type == 0) {
      // condition = " and cancel_relation_id is not null";
      condition = " and is_receive is null ";
    }
    if (type == 1) {
      // condition = " and cancel_relation_id is null";
      condition = " and is_receive is not null ";
    }
    XMLData data = new XMLData();
    try {
      int pageIndex = page.getCurrpage();
      int pageRows = page.getRows();

      String countSql = "select count(*) from sys_message " + Tools.addDbLink() + " s where s.user_id=? " + condition;
      if (type == 0 || type == 1) {//2为己发送状态，不加此条件
        countSql = countSql + " and (role_id=? or role_id is null)";
      }
      Integer totalCount = new Integer(0);
      // 获得总数据条数
      if (countSql != null && !countSql.equals("")) {
        List count = null;
        if (type == 0 || type == 1) {
          count = dao.findBySql(countSql, new Object[] { userid, roleid });
        } else {
          count = dao.findBySql(countSql, new Object[] { userid });
        }
        totalCount = new Integer((String) ((XMLData) count.get(0)).get("count(*)"));
      }
      int count = totalCount.intValue();
      String sql = "select * from ( select "
        + (TypeOfDB.isOracle() ? "rownum" : "@rn:=@rn+1")
        + " rn, u.user_name fromuser,m.sent_time,m.msg_title,u2.user_name,"
        + (TypeOfDB.isOracle() ? "nvl(" : "ifnull(")
        + "receive_time,"
        + SQLUtil.getSysdateToCharSql()
        + ") receive_time, r.role_name,m.cancel_relation_id,m.msg_content,m.msg_type_code,m.send_type,m.msg_id,m.fromuser fromuserid ,m.attm_id,"
        //添加attm_id 和 file_name
        + " case when m.attm_id is null then null else (select sma.file_name from sys_message_attachment sma where sma.attm_id = m.attm_id) end file_name from "
        + (TypeOfDB.isOracle() ? "" : " (select @rn:=0 ) r, ") + "sys_message" + Tools.addDbLink()
        + " m left join sys_usermanage" + Tools.addDbLink() + " u on m.fromuser=u.user_id left join sys_usermanage"
        + Tools.addDbLink() + " u2 on m.user_id=u2.user_id left join sys_role" + Tools.addDbLink()
        // + " r where m.fromuser=u.user_id(+) and m.is_receive is
        // null and "
        + " r on m.role_id=r.role_id and r.rg_code=? and r.set_year=? where  m.user_id=? ";
      if (type == 0 || type == 1) {//2为己发送状态，不加此条件
        sql = sql + " and (m.role_id=? or m.role_id is null) ";
      }

      sql = sql + condition + " order by sent_time desc) tab " + " where tab.rn > " + (count - pageIndex * pageRows)
        + " and tab.rn <= " + (count - (pageIndex - 1) * pageRows);
      // 查询并返回数据
      List msgdata = new ArrayList();
      List rows = null;
      if (type == 0 || type == 1) {
        rows = dao.findBySql(sql, new Object[] { rg_code, new Integer(set_year), userid, roleid });
      } else {
        rows = dao.findBySql(sql, new Object[] { rg_code, new Integer(set_year), userid });
      }

      if (rows.size() > 0) {
        // msgdata = new Object[rows.size()][cols];
        MsgCellObject cell = null;
        Map row = null;
        Iterator it = rows.iterator();
        int i = 0;
        while (it.hasNext()) {
          row = new XMLData();
          Map map = (Map) it.next();
          String cancel_relation_id = (String) map.get("cancel_relation_id");
          boolean mark = false;
          if (cancel_relation_id != null && !cancel_relation_id.equals("")) {
            String fromuser = (String) map.get("fromuser");
            cell = new MsgCellObject(fromuser, false);
            cell.setSendUser(true);
            row.put("fromuser", cell);
            String sent_time = (String) map.get("sent_time");
            cell = new MsgCellObject(sent_time, false);
            row.put("sent_time", cell);
            String msg_title = (String) map.get("msg_title");
            cell = new MsgCellObject(msg_title, false);
            row.put("msg_title", cell);
            String user_name = (String) map.get("user_name");
            cell = new MsgCellObject(user_name, false);
            row.put("user_name", cell);
            String receive_time = (String) map.get("receive_time");
            cell = new MsgCellObject(receive_time, false);
            row.put("receive_time", cell);
            String msg_content = (String) map.get("msg_content");
            cell = new MsgCellObject(msg_content, false);
            row.put("msg_content", cell);
            String msg_id = (String) map.get("msg_id");
            cell = new MsgCellObject(msg_id, false);
            row.put("msg_id", cell);
            String fromuserid = (String) map.get("fromuserid");
            cell = new MsgCellObject(fromuserid, false);
            row.put("fromuserid", cell);
            cell = new MsgCellObject(cancel_relation_id, false);
            row.put("cancel_relation_id", cell);
          } else {
            String fromuser = (String) map.get("fromuser");
            cell = new MsgCellObject(fromuser, true);
            cell.setSendUser(true);
            row.put("fromuser", cell);
            String sent_time = (String) map.get("sent_time");
            cell = new MsgCellObject(sent_time, true);
            row.put("sent_time", cell);
            String msg_title = (String) map.get("msg_title");
            cell = new MsgCellObject(msg_title, true);
            row.put("msg_title", cell);
            String user_name = (String) map.get("user_name");
            cell = new MsgCellObject(user_name, true);
            row.put("user_name", cell);
            String receive_time = (String) map.get("receive_time");
            cell = new MsgCellObject(receive_time, true);
            row.put("receive_time", cell);
            String msg_content = (String) map.get("msg_content");
            cell = new MsgCellObject(msg_content, true);
            row.put("msg_content", cell);
            String msg_id = (String) map.get("msg_id");
            cell = new MsgCellObject(msg_id, true);
            row.put("msg_id", cell);
            String fromuserid = (String) map.get("fromuserid");
            cell = new MsgCellObject(fromuserid, true);
            row.put("fromuserid", cell);
            cell = new MsgCellObject("", true);
            row.put("cancel_relation_id", cell);
            mark = true;
          }
          String attm_id = (String) map.get("attm_id");
          if (attm_id == null) {
            attm_id = "";
          }
          String file_name = (String) map.get("file_name");
          if (file_name == null) {
            file_name = "";
          }
          cell = new MsgCellObject(attm_id, mark);
          row.put("attm_id", cell);
          cell = new MsgCellObject(file_name, mark);
          row.put("file_name", cell);
          i++;
          msgdata.add(row);
        }
      } else {
      }
      data.put("total_count", totalCount);
      data.put("customrow", msgdata);
      data.put("row", rows);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return data;
  }

  public boolean cancelMessage(String msgid, String receivetime, String cancelid, boolean isnewmsg) throws Exception {
    boolean result = false;
    if (isnewmsg == true) {
      String sql = "update sys_message set receive_time='" + receivetime + "' , is_receive=1 where msg_id='" + msgid
        + "'";
      dao.executeBySql(sql);
      this.updateMessage(msgid);
      // dao.executeBySql("update sys_message set cancel_relation_id=null
      // where cancel_relation_id=?", new Object[] { cancelid });
    }
    result = true;
    return result;
  }

  public boolean sendMessage(List msgs, int canceltype) throws Exception {
    Map msg = null;
    String adminUserId = "2529";
    String cancelid = UUIDRandom.generate();
    //		增加附件ID
    String attm_id = null;
    if (msgs != null && msgs.size() > 0) {
      FMessageDTO fd = (FMessageDTO) msgs.get(0);
      if (fd.getAttachment() != null && !(fd.getAttachment().isEmpty())) {
        attm_id = UUIDRandom.generateNumberBySeqServer("SEQ_SYS_MESSAGE_ATTACHMENT");
        fd.getAttachment().setAttm_id(attm_id);//需要将ID放入实体，以便生成路径
        String insert_sql = "insert into SYS_MESSAGE_ATTACHMENT (ATTM_ID , FILE_NAME ,TIME_PATH) values (? , ? , ?)";
        dao.executeBySql(insert_sql, new Object[] { attm_id, fd.getAttachment().getFileNameCombination(),
          fd.getAttachment().getTime_path() });
        AttachmentTransferFactory.getUploadingHelper().uploadFiles(fd.getAttachment().getUploadList());
      }
    }
    Iterator it = msgs.iterator();
    while (it.hasNext()) {
      FMessageDTO dto = (FMessageDTO) it.next();
      dto.setAttm_id(attm_id);//设置附件ID
      //dto.setCancel_relation_id(cancelid);
      String userid = dto.getUser_id();
      String roleid = dto.getRole_id();
      int isRelateRole = 1;
      if (dto.getIs_relaterole() != null)
        isRelateRole = dto.getIs_relaterole().intValue();
      if (userid == null && roleid == null)
        continue;
      else if (userid != null && roleid != null) {

        dto.setMsg_id(UUIDRandom.generate());
        dto.setSent_time(Tools.getCurrDate());
        dto.setFromuser(SessionUtil.getUserInfoContext().getUserID());
        if (dto.getFromuser() == null || "".equals(dto.getFromuser())) {
          dto.setFromuser(adminUserId);
        }
        dao.deleteDataBySql("sys_message", dto, new String[] { "msg_id" });
        dao.saveDataBySql("sys_message", dto);
      } else if (userid != null && roleid == null) {
        if (isRelateRole == 1) {
          List roles = dao.findBySql("select r.role_id from sys_user_role_rule ur, sys_role r "
            + "where ur.user_id=? and ur.role_id=r.role_id and r.enabled=1", new Object[] { userid });
          if (roles.size() > 0) {
            Iterator it2 = roles.iterator();
            while (it2.hasNext()) {
              msg = new XMLData();
              Map map = (Map) it2.next();
              String rid = (String) map.get("role_id");
              dto.setRole_id(rid);
              dto.setMsg_id(UUIDRandom.generate());
              dto.setSent_time(Tools.getCurrDate());
              dto.setFromuser(SessionUtil.getUserInfoContext().getUserID());
              if (dto.getFromuser() == null || "".equals(dto.getFromuser())) {
                dto.setFromuser(adminUserId);
              }
              if (canceltype == 1) {
                dto.setCancel_relation_id(cancelid);
              }
              dao.deleteDataBySql("sys_message", dto, new String[] { "msg_id" });
              dao.saveDataBySql("sys_message", dto);
            }
          } else
            continue;
        } else {
          dto.setRole_id("");
          dto.setMsg_id(UUIDRandom.generate());
          dto.setSent_time(Tools.getCurrDate());
          dto.setFromuser(SessionUtil.getUserInfoContext().getUserID());
          if (dto.getFromuser() == null || "".equals(dto.getFromuser())) {
            dto.setFromuser(adminUserId);
          }
          if (canceltype == 1) {
            dto.setCancel_relation_id(cancelid);
          }
          dao.deleteDataBySql("sys_message", dto, new String[] { "msg_id" });
          dao.saveDataBySql("sys_message", dto);
        }
      } else if (userid == null && roleid != null) {
        List users = dao.findBySql("select user_id from sys_user_role_rule ur, sys_usermanage u "
          + "where ur.role_id=? and ur.user_id=u.user_id and u.enabled=1", new Object[] { roleid });
        if (users.size() > 0) {
          Iterator it3 = users.iterator();
          while (it3.hasNext()) {
            msg = new XMLData();
            Map map = (Map) it3.next();
            String uid = (String) map.get("user_id");
            if (canceltype == 1) {
              dto.setCancel_relation_id(cancelid);
            }
            dto.setSent_time(Tools.getCurrDate());
            dto.setFromuser(SessionUtil.getUserInfoContext().getUserID());
            if (dto.getFromuser() == null || "".equals(dto.getFromuser())) {
              dto.setFromuser(adminUserId);
            }
            dto.setUser_id(uid);
            dto.setMsg_id(UUIDRandom.generate());
            dao.deleteDataBySql("sys_message", dto, new String[] { "msg_id" });
            dao.saveDataBySql("sys_message", dto);
          }
        } else
          continue;
      }
    }
    return true;
  }

  public boolean deleteMessage(String msgid) throws Exception {
    //增加删除附件，多消息共用一个附件时，删除最后一条信息时，删除附件
    //查找带附件，并且不被其他消息占用的附件
    List list = dao
      .findBySql(
        "select attm_id from sys_message sy where sy.msg_id= ? and sy.attm_id is not null and not exists (select 1 from sys_message s where s.msg_id <> sy.msg_id and s.attm_id = sy.attm_id )",
        new Object[] { msgid });
    if (list != null && list.size() > 0) {//存在这样的附件
      Map map = (Map) list.get(0);
      String attm_id = (String) map.get("attm_id");
      if (attm_id != null) {
        list = dao.findBySql("select file_name , time_path from sys_message_attachment where attm_id = ?",
          new Object[] { attm_id });
        if (list != null && list.size() > 0) {
          Map result = (Map) list.get(0);
          FAttachmentDTO dto = new FAttachmentDTO();
          dto.setAttm_id(attm_id);
          dto.setTime_path((String) result.get("time_path"));
          dto.setFile_name((String) result.get("file_name"));
          AttachmentTransferFactory.getUploadingHelper().deleteFiles(dto.getDownloadList());
          dao.executeBySql("delete from sys_message_attachment where attm_id = ?", new Object[] { attm_id });
        }
      }
    }
    dao.executeBySql("delete from sys_message where msg_id=?", new Object[] { msgid });
    return true;
  }

  public Map haveNewMessage(String userid, String roleid, String curtime) throws Exception {
    String condition = "";
    if (curtime != null && !curtime.equals(""))
      condition = "and sent_time>'" + curtime + "'";
    String sql = " select 1 from sys_message" + Tools.addDbLink()
      + " m where m.user_id=? and (m.role_id=? or m.role_id is null) and m.is_receive is null ";
    List result = dao.findBySql(sql + condition, new Object[] { userid, roleid });
    List result2 = dao.findBySql(sql, new Object[] { userid, roleid });
    Map map = new HashMap();
    if (result.size() > 0) {
      map.put("result", "true");
    } else {
      map.put("result", "false");
    }
    if (result2.size() > 0) {
      map.put("new", "true");
      map.put("newcount", String.valueOf(result2.size()));
    } else {
      map.put("new", "false");
    }
    map.put("time", Tools.getCurrDate());
    return map;
  }

  /**
   * 跟据message ID查找消息的信息
   * 
   * @param msgId
   *            消息ID
   * @return result 一条消息的信息
   * @exception Exception
   * @author 项奕铭
   */
  public List findMessageByMessageId(String msgId) throws Exception {

    List result = null;
    String sql = null;
    if (msgId != null) {
      sql = "select  m.msg_id,m.msg_title,m.msg_content,m.fromuser,m.sent_time,"
        + (TypeOfDB.isOracle() ? "nvl(" : "ifnull(")
        + "m.receive_time, "
        + SQLUtil.getSysdateToCharSql()
        + ") receive_time,m.cancel_relation_id,u.user_name ,m.attm_id, case when m.attm_id is null then null else (select sma.file_name from sys_message_attachment sma where sma.attm_id = m.attm_id) end file_name from sys_message"
        + Tools.addDbLink() + " m , sys_user " + Tools.addDbLink() + " u where m.msg_id = '" + msgId
        + "' and m.fromuser = u.user_id ";
      result = dao.findBySql(sql);
    }
    return result;

  }

  public void updateMessage(String msgId) throws Exception {
    //		String sql = "update sys_message set is_receive=1 where msg_id =?";
    //		dao.executeBySql(sql, new Object[] { msgId });
    /** *****************处理是否一次核销消息的问题****************************** */
    List result = findMessageByMessageId(msgId);
    Map data = (Map) result.get(0);
    if (data.get("cancel_relation_id") != null) {
      String sql = "update sys_message set is_receive=1 ,receive_time =? where fromuser=? and cancel_relation_id=? ";
      //String msg_title = data.get("msg_title") == null ? "" : data.get("msg_title").toString();
      String fromuser = data.get("fromuser").toString();
      //String sent_time = data.get("sent_time").toString();
      String receive_time = data.get("receive_time").toString();
      dao.executeBySql(sql, new Object[] { receive_time, fromuser, (String) data.get("cancel_relation_id") });
    }
    /** ******************************************************************* */
  }

  public byte[] downLoadAttachment(String attm_id, String file_name) {
    if (attm_id == null || "".equals(attm_id) || file_name == null || "".equals(file_name)) {
      return null;
    }
    //是在查消息时查出来还是在这里查一遍，确保存在，以后重新考虑下，暂时这样处理
    List list = dao.findBySql("select time_path from sys_message_attachment where attm_id = ?",
      new Object[] { attm_id });
    if (list != null && list.size() > 0) {
      XMLData data = (XMLData) list.get(0);
      if (data != null && data.get("time_path") != null && !"".equals(data.get("time_path").toString())) {
        FAttachmentDTO dto = new FAttachmentDTO();
        dto.setAttm_id(attm_id);
        dto.setTime_path(data.get("time_path").toString());
        dto.setFile_name(file_name);
        return AttachmentTransferFactory.getUploadingHelper().downloadFiles((XMLData) dto.getDownloadList().get(0));
      }
    }
    return null;
  }

  public XMLData findMessage3(String userid, String roleId, String plSql, FPaginationDTO page, int type)
    throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public byte[] downLoadAttachmentByNum(String attm_id, String file_name, int Num) {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean addMessage(Map messageMap) {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean readMessage(Map messageMap) {
    // TODO Auto-generated method stub
    return false;
  }

}
