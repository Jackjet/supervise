package gov.df.fap.service.gl.je;

import gov.df.fap.bean.gl.dto.JournalDTO;
import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.service.gl.balance.BalanceTracer;
import gov.df.fap.service.util.exceptions.gl.GlException;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.date.DateHandler;

import java.math.BigDecimal;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 
 * @version
 */
@Service
public class JournalService implements IJournalService {
  @Autowired
  private IJournalDao dao;

  public void setDao(IJournalDao dao) {
    this.dao = dao;
  }

  /**
   * 对传入的交易凭证作校验
   * @param dto 交易凭证
   * @throws Exception
   */
  public void checkBill(FVoucherDTO voucherDTO) throws Exception {
    //业务明细ID
    if (voucherDTO.getVou_id() == null || voucherDTO.getVou_id().length() == 0) {
      throw new GlException("业务明细主键不存在,无法入账!");
    }
    //交易凭证类型
    if (voucherDTO.getBilltype_id() == null || voucherDTO.getBilltype_id().length() == 0) {
      if (voucherDTO.getBilltype_code() == null || voucherDTO.getBilltype_code().length() == 0) {
        throw new GlException(new StringBuffer("未指定").append(voucherDTO.getVou_id()).append("的交易凭证类型,无法入账！").toString());
      }
    }
    //金额信息
    if (voucherDTO.getVou_money() == null || voucherDTO.getVou_money().length() == 0) {
      throw new GlException(new StringBuffer("未指定").append(voucherDTO.getVou_id()).append("的金额信息,无法入账！").toString());
    }

    if (StringUtils.split(new BigDecimal(voucherDTO.getVou_money()).toString(), ".")[0].length() > 14)
      throw new GlException(new StringBuffer("入账金额").append(new BigDecimal(voucherDTO.getVou_money()).toString())
        .append("的值超过应用长度,无法入账！").toString());

    //要素信息
    if (voucherDTO.getCcid() == null || voucherDTO.getCcid().length() == 0 || voucherDTO.getCcid().equals("0")) {
      throw new GlException(new StringBuffer("未指定").append(voucherDTO.getVou_id()).append("的各要素信息,无法入账！").toString());
    }
    //权限信息
    if (voucherDTO.getRcid() == null || voucherDTO.getRcid().length() == 0) {
      throw new GlException(new StringBuffer("未指定").append(voucherDTO.getVou_id()).append("的权限信息,无法入账！").toString());
    }
    //流程状态信息
    //		if(voucherDTO.getIs_end()<0||voucherDTO.getIs_end()>1){
    //			throw new GlException(new StringBuffer("未指定").append(voucherDTO.getVou_id()).append("的流程状态信息,无法入账！").toString());				
    //		}
    //数据有效信息
    //		if(voucherDTO.getIs_valid()<0||voucherDTO.getIs_valid()>1){
    //			throw new GlException(new StringBuffer("未指定").append(voucherDTO.getVou_id()).append("的数据有效信息,无法入账！").toString());				
    //		}
    //业务年度信息
    if (voucherDTO.getSet_year() < 1900 || voucherDTO.getSet_year() > 2999) {
      throw new GlException(new StringBuffer("未指定").append(voucherDTO.getVou_id()).append("的业务年度信息,无法入账！").toString());
    }
    //月份信息
    if (voucherDTO.getSet_month() < 0 || voucherDTO.getSet_month() > 12) {
      voucherDTO.setSet_month(DateHandler.getCurrentMonth());
    }
  }

  /**
   * 将传入的交易凭证批量插入临时表
   * @param dtoList 交易凭证列表
   * @throws Exception
   */
  public void insertJournalCache(List dtoList) {
    //生成journal_id
    ListIterator itrator = dtoList.listIterator();
    JournalDTO journalDto = null;
    while (itrator.hasNext()) {
      journalDto = (JournalDTO) itrator.next();
      if (StringUtil.equals(StringUtil.ZERO, journalDto.getJournal_id()))
        ;
      journalDto.setJournal_id(StringUtil.toStr(JournalUtil.generateJournalId(journalDto)));
    }
    dao.lockedJournalByUpdate(dtoList);
    dao.insertJournalCache(dtoList);
  }

  /**
   * 更新日志表及日志历史表
   * @throws Exception
   */
  public void updateJournalWithCache() {
    dao.updateJournalWithCache();
  }

  /**
   * 删除日志表及日志历史表
   * @throws Exception
   */
  public void backUpJournalWithCache() throws Exception {
    dao.backUpServiceWithCache();
  }

  public void insertJournalByCache() {
    dao.insertJournalByCache();
  }

  public List findJournalWithCache(boolean isExists) {
    return isExists ? dao.findExistsJournalWithCache() : dao.findNotExistsJournalWithCache();
  }

  public void clearJournalCache() {
    dao.deleteJournalCache();
  }

  public List findVoucherByBalance(String sumId, int ctrlSide) {
    if (ctrlSide != BalanceTracer.CTRL_SIDE_SOURCE && ctrlSide != BalanceTracer.CTRL_SIDE_TARGET)
      throw new IllegalArgumentException("追溯参数side不合法!");

    return dao.findVoucherByBalance(sumId, ctrlSide);
  }
}
