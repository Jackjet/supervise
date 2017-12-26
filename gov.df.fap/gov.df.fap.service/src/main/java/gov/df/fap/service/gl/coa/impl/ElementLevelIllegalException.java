package gov.df.fap.service.gl.coa.impl;

import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.coa.FCoaDetail;

/**
 * 要素级次非法异常
 * @author
 *
 */
public class ElementLevelIllegalException extends RuntimeException {
	
  private static final long serialVersionUID = 1L;
  
  FCoaDetail targetCoaDetail = null;
	
	public ElementLevelIllegalException(FCoaDetail targetCoaDetail){
		super();
		this.targetCoaDetail = targetCoaDetail;
	}
	
	public ElementLevelIllegalException(FCoaDetail targetCoaDetail, String msg, Throwable t){
		super(msg, t);
		this.targetCoaDetail  = targetCoaDetail;
	}
	
	public String getCoaInfo(){
		FCoaDTO coa = targetCoaDetail.getCoaDto();
		return coa.getCoaCode()+" "+coa.getCoaName();
	}
	
	public String getEleCode() {
		return targetCoaDetail.getEleCode();
	}	
}
