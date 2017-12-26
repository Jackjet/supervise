package gov.df.fap.service.portal.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
@Repository
public class GetUserInfoDao  {

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;
	public List getUserInfo(){
		System.out.println("this is Dao #################################");
		List resultList = new ArrayList();
		resultList= sqlSessionTemplate.selectList("selectCauserINfo", "");
		return resultList;
	}

}
