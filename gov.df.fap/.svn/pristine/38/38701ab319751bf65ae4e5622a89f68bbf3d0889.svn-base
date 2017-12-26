package gov.df.fap.service.util.gl.coa;



import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.coa.FCoaDetail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * coa配置界面 辅助类
 * @author LiuYan
 * @version Apr 27, 2011
 */
public class CoaUIUtil {

	
	/**
	 * 移除账务的COA
	 */
	public static List removeGlCoa(List coaList){
		if (coaList == null || coaList.isEmpty())
			return coaList;
		
		List retList = new ArrayList();
		for (int i = 0, len = coaList.size(); i < len; i++){
			final FCoaDTO coa = (FCoaDTO) coaList.get(i);
			if (!coa.getCoaCode().startsWith("900"))
				retList.add(coa);
		}
		return retList;
	}
	
	/**
	 * 根据要素编码查询coa中设置
	 * 
	 * @param coaDto
	 * @param eleCode
	 * @return
	 */
	public static FCoaDetail getCoaDetailByEleCode(FCoaDTO coaDto, String eleCode) {
		final Iterator iterator = coaDto.getCoaDetail().iterator();
		FCoaDetail coaDetail = null;
		FCoaDetail findCoaDetail = null;
		while (iterator.hasNext()) {
			coaDetail = (FCoaDetail) iterator.next();
			if (coaDetail.getEleCode().equals(eleCode)) {
				findCoaDetail = coaDetail;
				break ;
			}
		}
		return findCoaDetail;
	}
}
