package gov.df.fap.service.util.gl.coa.cascade;

import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 保存Coa对象
 * @author 
 * @version 
 */
@Service
public class CoaObject implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  private String coaId = StringUtil.EMPTY;

  private boolean isUpStream = false;

  @Autowired
  private ICoaService coaService;

  public String getCoaId() {
    return coaId;
  }

  public CoaObject() {

  }

  public CoaObject(String coaId) {
    this.coaId = coaId;
  }

  public CoaObject(String coaId, boolean isUpStream) {
    this.coaId = coaId;
    this.isUpStream = isUpStream;
  }

  public FCoaDTO getCoa() {
    if (coaService == null) {
      coaService = (ICoaService) SessionUtil.getServerBean("coaService");
    }
    return coaService.getCoa(Long.parseLong(coaId));
  }

  public boolean isUpStream() {
    return isUpStream;
  }
}
