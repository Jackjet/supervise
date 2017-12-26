package gov.df.supervise.service.itemsupervision;

import java.util.List;
import java.util.Map;

import gov.df.supervise.bean.task.CsofTaskDepAgency;
import gov.df.supervise.service.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface CsofTaskDepAgencyDao {
    /**
     *
     * @mbggenerated 2017-09-06
     */
    int deleteByPrimaryKey(String daid);

    /**
     *
     * @mbggenerated 2017-09-06
     */
    int insert(CsofTaskDepAgency record);

    /**
     *
     * @mbggenerated 2017-09-06
     */
    int insertSelective(CsofTaskDepAgency record);

    /**
     *
     * @mbggenerated 2017-09-06
     */
    CsofTaskDepAgency selectByPrimaryKey(String daid);

    /**
     *
     * @mbggenerated 2017-09-06
     */
    int updateByPrimaryKeySelective(CsofTaskDepAgency record);

    /**
     *
     * @mbggenerated 2017-09-06
     */
    int updateByPrimaryKey(CsofTaskDepAgency record);

	@SuppressWarnings("rawtypes")
	List<Map> selectBySidType(Map<String, Object> param);

	String selectNum(Map<String, Object> param1);
	
}