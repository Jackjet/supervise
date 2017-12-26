package gov.df.fap.service.gl.je;

import gov.df.fap.bean.gl.dto.JournalDTO;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.number.NumberUtil;

/**
 * 日志工具
 * @author 
 * @version 
 */
public class JournalUtil {

  public static final String JOURNAL_KEYS_SEPERATOR = ",";

  /**
   * 生成日志ID
   * @param setYear 年度
   * @param rgCode 区域码
   * @param vouId 业务明细ID
   * @param vouTypeId 记账模板ID
   * @return 根据以上的参数计算出来的日志ID
   */
  public static long generateJournalId(int setYear, String rgCode, String vouId, long vouTypeId) {
    long hash = 0;
    hash ^= setYear;
    hash ^= rgCode.hashCode();
    hash ^= StringUtil.stringHash(vouId);
    return NumberUtil.toLong(Long.toString(vouTypeId, 9) + "9" + Math.abs(hash));
  }

  /**
   * 
   * @param journal
   * @return
   */
  public static long generateJournalId(JournalDTO journal) {
    return generateJournalId(journal.getSet_year(), journal.getRg_code(), journal.getVou_id(), journal.getVou_type_id());
  }

  public static void main(String[] args) {
    System.out.println(JournalUtil.generateJournalId(2008, "410000", "{E491AD8D-2D62-11DD-ADAA-A5E0E5EF5D28}", 16));
    System.out.println(JournalUtil.generateJournalId(2008, "410000", "{3D4D4D30-F09D-11DC-9342-C1B05CC93C93}", 16));
    System.out.println(JournalUtil.generateJournalId(2008, "410000", "{43979FF3-285E-11DD-ADAA-A5E0E5EF5D28}", 16));
    System.out.println(JournalUtil.generateJournalId(2008, "410000", "{259397A3-E5AB-11DC-80ED-9964B6D077B9}", 16));
    System.out.println(JournalUtil.generateJournalId(2008, "410000", "{2C3CE0C7-428A-11DD-A187-B41ED51824D7}", 16));
    System.out.println(JournalUtil.generateJournalId(2008, "410000", "{7ECEC648-4250-11DD-8557-F86A3187661E}", 16));
  }
}
