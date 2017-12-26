package gov.df.fap.service.util.dao.portaldao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PortalDao {

  @Resource
  private SqlSessionTemplate sqlSessionTemplate;
  
  /**
   * 获取用户信息
   */
  public List getUserInfo(){
    List resultList = new ArrayList();
    resultList = sqlSessionTemplate.selectList("selectCauserINfo");
    return resultList;
  }
  
}
