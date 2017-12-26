package gov.df.fap.service.util;

/**
 * filename:UUIDRandom.java
 *
 * Version:1.0
 *
 * Date:2004-08-06
 *
 * Copyright  2003 by Founder Sprint 1st CO. Ltd
 */

import gov.df.fap.api.util.interfaces.IFindLog;
import gov.df.fap.api.util.paramanage.IParaManage;
import gov.df.fap.service.util.sessionmanager.SessionUtil;

import java.util.UUID;

/**
 * <p>类名： UUIDRandom</p>
 * <p>Copyright 2003 by Founder Sprint 1st CO. Ltd
 * <p>All rights reserved.
 * <p>版权所有：用友政务软件有限公司
 * <p>未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 * <p>侵权者将受到法律追究。
 * <p>DERIVED FROM:   NONE
 * <p>PURPOSE:        产生全球唯一的UUID
 * <p>DESCRIPTION:    产生UUID的方法，需要引入jug.jar
 * <p>CALLED BY:
 * <p>UPDATE:         tim
 * <p>DATE:           2003年06月10日
 * <p>HISTORY:        1.0
 * @version 1.0
 * @author tim
 * @since java 1.6
 */

public class UUIDRandom {
  private UUIDRandom() {
  }

  /**
   * 取得一个UUID的方法
   * @return 一个UUID
   */
  public static String generate() {
    UUID uuid = UUID.randomUUID();
    String id = uuid.toString();
    id = "{" + id + "}";
    return id.toUpperCase();
  }

  public static String generateNumberBySeq(String seq) {
    return null;
  }

  /**
   * 服务器端条用取seq
   * @param seq seq名称
   * @return seq
   * @author jerry
   */
  public static String generateNumberBySeqServer(String seq) {
    IFindLog findLog = (IFindLog) SessionUtil.getServerBean("sys.userLogFinderService");
    String returnString = findLog.generateNumberBySeq(seq);
    if (returnString != null && !returnString.equals("")) {
      return returnString;
    } else {
      return null;
    }
  }

  public static String generateServer() {
    IParaManage ipara = (IParaManage) SessionUtil.getServerBean("sys.paraManService");
    String switch01 = ipara.getPara("switch01", "");
    if (switch01.equals("1"))
      return ipara.getNumber();
    else
      return generate();
  }

  public static String generateNumber() {
    return generateNumberBySeq("SEQ_SYS_ID");
  }
}