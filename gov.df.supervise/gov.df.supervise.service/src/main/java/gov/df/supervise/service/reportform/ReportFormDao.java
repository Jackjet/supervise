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
 * CreateData: 2017-8-27下午3:00:57
 * </p>
 * 
 * @author songlr3
 * @version 1.0
 */
package gov.df.supervise.service.reportform;

import gov.df.supervise.bean.report.CsofEReportEntity;
import gov.df.supervise.service.common.persistence.annotation.MyBatisDao;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface ReportFormDao {
	
	List<?> selectSupReportBySidBillcode(Map<String, Object> param);
	
	CsofEReportEntity selectEReportByChrId(String chrId);

}
