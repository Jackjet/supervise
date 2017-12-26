package gov.df.fap.service.util.gl.coa.cascade;

import gov.df.fap.bean.gl.coa.FCoaDetail;
import gov.df.fap.util.StringUtil;

/**
 * 对比结果对象
 * @author LiuYan
 * @version Mar 22, 2011
 */
public class CompareItem {

	public final static int ADD_ELEMENT = 1;
	
	public final static int DEL_ELEMENT = 2;
	
	public final static int MODIFY_ELEMENT = 3;
	
	private int operateType = 0;
	
	private String eleSource = StringUtil.EMPTY;
	
	private FCoaDetail originCoaDetailDTO = null;
	
	private FCoaDetail targetCoaDetailDTO = null;
	
	public CompareItem(int operateType, String eleSource, FCoaDetail originCoaDetailDTO, FCoaDetail targetCoaDetailDTO) {
		this.operateType = operateType;
		this.eleSource = eleSource;
		this.originCoaDetailDTO = originCoaDetailDTO;
		this.targetCoaDetailDTO = targetCoaDetailDTO;
	}

	public int getOperateType() {
		return operateType;
	}

	public String getEleSource() {
		return eleSource;
	}

	public FCoaDetail getOriginCoaDetailDTO() {
		return originCoaDetailDTO;
	}

	public FCoaDetail getTargetCoaDetailDTO() {
		return targetCoaDetailDTO;
	}
	
}
