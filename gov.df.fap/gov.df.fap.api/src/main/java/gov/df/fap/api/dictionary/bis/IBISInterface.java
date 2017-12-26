package gov.df.fap.api.dictionary.bis;

import gov.df.fap.bean.dictionary.dto.BISDTO;
import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;

public interface IBISInterface {

  /**
   * 录入界面新增项目
   * @param dataList 项目信息列表
   * @return
   * @throws Exception
   */
  public Map saveBisDataForInput(List dataList) throws Exception;

  /**
   * 新增项目
   * @param dataList 项目信息列表
   * @return
   * @throws Exception
   */
  public Map saveBisData(List dataList) throws Exception;

  /**
   * 修改项目
   * @param bisdto 项目DTO
   * @return
   * @throws Exception
   */
  public boolean updateBisData(BISDTO bisdto) throws Exception;

  /**
   * <br>
   * Description:根据传入要素获取预算项目查询条件 <br>
   * Author:王鑫磊(wangxinlei@ufida.com.cn) <br>
   * Date:May 13, 2015
   * @param conditions
   * @return
   */
  public String getBisSqlCondition(BISDTO conditions) throws Exception;

  /**
   * <br>
   * Description:根据传入要素获取预算项目 <br>
   * Author:王鑫磊(wangxinlei@ufida.com.cn) <br>
   * Date:May 13, 2015
   * @param conditions
   * @return
   */
  public List getBisData(BISDTO conditions) throws Exception;

  /**
   * 删除项目
   * @param bis_id项目id
   * @return
   * @throws Exception
   */
  public Map deleteBisData(String bis_id, String sys_id) throws Exception;

  /**
   * 项目信息查询
   * @param bis_id项目id
   * @return
   * @throws Exception
   */
  public BISDTO queryBisById(String bis_id) throws Exception;

  /**
   * <br>Description:判断传入预算项目信息是否符合项目所属
   * <br>Author:王鑫磊(wangxinlei@ufida.com.cn)
   * <br>Date:May 12, 2015
   * @param bis_id
   * @param mb_id
   * @param en_id
   * @param bi_id
   * @return
   * @throws Exception
   */
  public boolean checkBisBelong(String bis_id, String mb_id, String en_id, String bi_id) throws Exception;

  /**
   * <br>Description:判断传入预算项目信息是否符合项目所属
   * <br>Author:王鑫磊(wangxinlei@ufida.com.cn)
   * <br>Date:May 12, 2015
   * @param BISDTO
   * @return
   * @throws Exception
   */
  public boolean checkBisBelong(BISDTO BISDTO) throws Exception;

  /**
   * 项目信息查询视图
   * @param bis_ids项目id
   * @return
   * @throws Exception
   */
  public List queryBisDataById(List bisidList) throws Exception;

  /**
   * 项目信息查询视图
   * @return
   * @throws Exception
   */
  public List queryAllBisData(XMLData condition) throws Exception;

  /**
   * 项目信息查询视图
   * @return
   * @throws Exception
   */
  public List queryAllBisData(XMLData condition, boolean withENABLED) throws Exception;

  /**
   * 年度初始化
   * @param newYear 新年度
   * @param oldYear 旧年度
   * @return
   * @throws Exception
   */
  public boolean copyBISDataToNewYear(String newYear, String oldYear) throws Exception;

  /**
   * 查询项目所属信息
   * @param bis_id 项目ID
   * @return
   * @throws Exception
   */
  public List queryBisBelongByID(List bisidList) throws Exception;

  /**
   * 查询项目资金信息
   * @param bis_id 项目ID
   * @return
   * @throws Exception
   */
  public List queryBisMoneyByID(List bisidList) throws Exception;

  /**
     * 根据参数ID更新系统参数值
     */
  public void updateBisConfigPara(String para_value, String para_code) throws Exception;

  /**
   * 根据参数ID获得系统参数列表
   */
  public List getBisConfigParas(String paracodes) throws Exception;

  /**
   * 获得所有系统参数列表
   */
  public List getAllBisConfigPara() throws Exception;

  /**
   * 根据参数ID获得系统参数值
   */
  public String getBisConfigPara(String paracode);

  public void saveBisConfigPara(Map data) throws Exception;

  /**查询部门管理数据
   * 
   */
  public List queryBmData(XMLData data) throws Exception;

  /**
  * 项目信息综合信息
  */
  public List queryIntegratedBisData(XMLData queryData) throws Exception;

  /**
   * 项目明细界面配置信息
   */
  public List getBisDetailConfig(boolean withFB) throws Exception;

  /**
   * 根据参数年度获取某年资金使用情况数据
   */
  public void createBisMoney(String year) throws Exception;
}
