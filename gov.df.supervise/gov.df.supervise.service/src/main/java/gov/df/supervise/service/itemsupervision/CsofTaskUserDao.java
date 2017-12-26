package gov.df.supervise.service.itemsupervision;

import gov.df.supervise.bean.task.CsofTaskUser;
import gov.df.supervise.service.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface CsofTaskUserDao {
    /**
     *
     * @mbggenerated 2017-09-01
     */
    int deleteByPrimaryKey(String id);

    /**
     *
     * @mbggenerated 2017-09-01
     */
    int insert(CsofTaskUser record);

    /**
     *
     * @mbggenerated 2017-09-01
     */
    int insertSelective(CsofTaskUser record);

    /**
     *
     * @mbggenerated 2017-09-01
     */
    CsofTaskUser selectByPrimaryKey(String id);

    /**
     *
     * @mbggenerated 2017-09-01
     */
    int updateByPrimaryKeySelective(CsofTaskUser record);

    /**
     *
     * @mbggenerated 2017-09-01
     */
    int updateByPrimaryKey(CsofTaskUser record);
}