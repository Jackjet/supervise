package gov.df.fap.service.gl.coa;

import gov.df.fap.api.gl.coa.CoaObserver;
import gov.df.fap.bean.gl.coa.FCoaDTO;




/**
 * COA变化观察默认实现，所有方法为空
 * @author Liu yan
 * @since 6.2.61.00
 *
 */
public class CoaObserverAdapter implements CoaObserver {

	public void addNewModify(FCoaDTO coaDTO) {
	}

	public void updateModify(FCoaDTO coaDTO) {
	}

	public void deleteModify() {
	}

	public void refreshCoaTree() {
	}
	
}