/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017 北京用友政务软件有限公司
 * </p>
 * <p>
 * Company: 北京用友政务软件有限公司
 * </p>
 * <p>
 * CreateData: 2017-8-27下午2:59:56
 * </p>
 * 
 * @author songlr3
 * @version 1.0
 */
package gov.df.supervise.service.reportform;

import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.util.Tools;
import gov.df.fap.util.xml.XMLData;
import gov.df.supervise.api.reportfprm.ReportFormService;
import gov.df.supervise.bean.report.CsofEReportEntity;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ReportFormServiceImpl implements ReportFormService {

  @Autowired
  private ReportFormDao reportFormDao;

  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao;

  public List<?> selectSupReportBySidBillcode(Map<String, Object> param) {
    return reportFormDao.selectSupReportBySidBillcode(param);
  }

  public CsofEReportEntity selectEReportByChrId(String chrId) {
    return reportFormDao.selectEReportByChrId(chrId);
  }

  /**
   * 获取统计报表的访问地址bi
   * @return
   * CSOF_REPORT_BI_URL
   * @author songlr3 at 2017-8-29下午3:11:14
   */
  public String getBiValue() {
    StringBuffer strSQL = new StringBuffer("select chr_value from sys_userpara" + Tools.addDbLink()
      + " where  chr_code = 'CSOF_REPORT_BI_URL' ");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString());
      strSQL = null;
    } catch (Exception e) {

    }
    XMLData data = (XMLData) list.get(0);
    String BiValue = (String) data.get("chr_value");
    return BiValue;
  }

  /**
   * 获取填报报表的访问地址bi
   * @return
   * CSOF_REPORT_IP_URL
   * @author songlr3 at 2017-8-29下午3:11:14
   */
  public String getIpValue() {
    StringBuffer strSQL = new StringBuffer("select chr_value from sys_userpara" + Tools.addDbLink()
      + " where  chr_code = 'CSOF_REPORT_IP_URL' ");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString());
      strSQL = null;
    } catch (Exception e) {

    }
    XMLData data = (XMLData) list.get(0);
    String IpValue = (String) data.get("chr_value");
    return IpValue;
  }

  /**
   * 获取报表的访问用户id
   * @return
   * CSOF_REPORT_USER
   * @author songlr3 at 2017-8-29下午3:11:14
   */
  public String getUser() {
    StringBuffer strSQL = new StringBuffer("select chr_value from sys_userpara" + Tools.addDbLink()
      + " where  chr_code = 'CSOF_REPORT_USER' ");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString());
      strSQL = null;
    } catch (Exception e) {

    }
    XMLData data = (XMLData) list.get(0);
    String User = (String) data.get("chr_value");
    return User;
  }

  /**
   * 获取报表的访问用户密码
   * @return
   * CSOF_REPORT_PWD
   * @author songlr3 at 2017-8-29下午3:11:14
   */
  public String getPw() {
    StringBuffer strSQL = new StringBuffer("select chr_value from sys_userpara" + Tools.addDbLink()
      + " where  chr_code = 'CSOF_REPORT_PWD' ");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString());
      strSQL = null;
    } catch (Exception e) {

    }
    XMLData data = (XMLData) list.get(0);
    String Pw = (String) data.get("chr_value");
    return Pw;
  }

  /**
   * 获取填报报表的访问地址BU
   * @return
   * CSOF_REPORT_IP_URL
   * @author songlr3 at 2017-8-29下午3:11:14
   */
  public String getBuValue() {
    StringBuffer strSQL = new StringBuffer("select chr_value from sys_userpara" + Tools.addDbLink()
      + " where  chr_code = 'CSOF_REPORT_BU_URL' ");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString());
      strSQL = null;
    } catch (Exception e) {

    }
    XMLData data = (XMLData) list.get(0);
    String IpValue = (String) data.get("chr_value");
    return IpValue;
  }

}
