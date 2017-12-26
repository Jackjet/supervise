package gov.df.supervise.api.bill;

import java.util.List;
import java.util.Map;

/**
 * 监管单据接口
 * @author Administrator
 *
 */
public interface BillService {
	
	/**
	 * 任务下达
	 * @param ids 主键串
	 * @param workMap 流程参数
	 * @return
	 */
	public int approveTask(String ids,Map workMap);
	
	/**
	 * 任务发布
	 * @param id
	 * @return
	 */
	public int publishTask(String id,Map workMap);
	
	/**
	 * 任务发布
	 * @param id
	 * @return
	 */
	public int unpublishTask(String id,Map workMap);
	
	/**
	 * 任务登记接收
	 * @param ids  主键串
	 * @param workMap  流程参数
	 * @return
	 */
	public int receiveTask(String ids,Map workMap);

	public int deleteMofBill(String ids, String key);

	public int saveMofBill(List officeList,List supList,String billtype_code,Map workMap);
		
	public int saveTaskBill(List objectList, List agencyList, Map mofData, List taskList , Map mofWorkMap ,Map taskWorkMap);
	
	public int updateMofBill(Map mofBillData,String billtype_code);
	
	public int deleteTaskBill(Map mofData, Map mofWorkMap ,Map taskWorkMap);
}
