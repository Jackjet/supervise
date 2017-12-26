package gov.df.fap.api.gl.coa.ibs;

import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.util.exception.IllegalEleLevelOfDownStreamCoaException;
import gov.df.fap.util.exception.LackEleOfDownStreamCoaException;
import gov.df.fap.util.xml.XMLData;

import java.sql.SQLException;
import java.util.List;

/**
 * coa组件对外接口
 * @version 1.0
 * @author 
 * @since java 1.6
*/
public interface ICoa extends ICoaService
{
	/**
	 * 取得要素的级次信息(服务端直接使用,不对界面表现层公开)
	 * @param Coa_Id Coa_Id
	 * @param Ele_Code 要素编码
	 * @param tableAlias 表别名
	 * @return String SQL语句段
	 * @throws SQLException 
	 */
	public String getEleLevelNum(String Coa_Id,String Ele_Code,String tableAlias);
	
	/**
	 * 得到所有的COA列表
	 * @param condition 过滤条件
	 * @return coa XMLData列表
	 * @throws SQLException 
	 */	
	public XMLData getCoalist(String condition);
	
	/**
	 * 取得COA的结构描述
	 * @param coa_id： 标示COA唯一的ID
	 * @return coa对应XMLData对象
	 */	
	public XMLData getCoabyID(String coa_id);
	
	/**
	 * 得到所有coa信息
	 * @return
	 * @throws SQLException 
	 */
	public List getAllCoa();
	
	/**
	 * 通过user_id 得到 user_name
	 * @param userId
	 * @return
	 */
    public String getCreateUser(String userId);
    
    /**
     * 手工批量生成CCID 检查转换表的有效性。返回 -1 表示 表不存在 ，0 表示 new_ccid 字段不存在， 1 表示 合法
     * @param table
     * @return
     */
    public int checkTempTable(String table);
    
    /**
     * 获取手工批量生成CCID 的要数信息
     * @param table
     * @return
     */
    public List loadManualCcidInfo(String table);
    
    /**
     * 手工批量生成CCID 后插入新CCID 到转换表
     * @param dataList
     */
    public void insertNewCcid(String table,List dataList);
    
    /**
     * 加载一个CCID 信息
     * @param ccid
     * @return
     */
    public List loadCcid(String alias,String condition);
    
	/**
	 * 需要对应的值符合coa_id的定义，如果CCID存在，直接使用存在的CCID，如果不存在，需要创建新的CCID
	 * @param coa_id 标示唯一的业务要素级次设置
	 * @param xmlEleValue 要素信息 
	 * @param 成功调用接口时返回CCID
	 * @deprecated
	 */	
	public String getCCIDbyELEvalue(String coa_id,String xmlEleValue)throws Exception;
	
	/**
	 * 根据ccid获取对应的要素id,code,name信息
	 * @param ccid 要素配置唯一码
	 * @return CCID要素信息XML字符串
	 */
	public String getCCIDDetail(String ccid);
	/**
	 * coa对比函数,次别排序 -1 > 正数级次 > 0 > -2(-1底级,正数级次为具体级次,0自定义,-2任意)
	 * @param left_coa_id 左比较coa
	 * @param right_coa_id 右比较coa
	 * @return -1:left_coa_id<right_coa_id 
	 *         0:left_coa_id=right_coa_id
	 *         1:left_coa_id>right_coa_id
	 */
	public int compareCoa(String left_coa_id,String right_coa_id)throws Exception;
	/**
	 * 比较获取coa相对列表
	 * @param compareType(GT,GTE,EQ,LT,LTE) 比较类型(大于、等于、小于等) 
	 * @param compareCoaID 比较目标coaID
	 * @param condition 其他过滤条件
	 * @return 结果列表
	 * @throws SQLException 
	 */
	public List getCoaCompareList(int compareType,String compareCoaID,String condition);
    
    /**
     * 检验下游额度配置coa是否符合上游额度的coa配置
     * @param upStreamCoaId 下游coa的id
     * @param downStreamCoaId 上游coa的id
     * @return boolean 校验通过返回true,否则抛出异常
     * @throws IllegalEleLevelOfDownStreamCoaException
     * @throws LackEleOfDownStreamCoaException
     */
    public boolean validateDownStreamCoaLevel(long upStreamCoaId,
			long downStreamCoaId)
			throws IllegalEleLevelOfDownStreamCoaException,
			LackEleOfDownStreamCoaException;
    
    /**
     * 重新保存coa
     * 1.清空gl_coa和gl_coa_detail表 2.插入传入的coa集合
     * @param coaList
     */
    public void reinstallCoa(List coaList) throws Exception;
    
    /**
     * 获取当前所有启用要素信息
     * @return 要素信息对象
     */
    public List getAllElement();
    
    /**
	  * 得到自定义级次的数据
	  * @param coa_id
	  * @param element
	  * @return List
	  */
    public List getCOADetailCode(String coa_id,String element);
    
    /**
     * 根据要素简称获取要素列表
     * @param element 要素简称
     * @return 要素信息对象
     */
    public XMLData getEle(String element);
    
    
    
    
    
}
