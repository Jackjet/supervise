package gov.df.fap.service.portal.dao;

import gov.df.fap.api.portal.IBaseDao;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author zhukn
 * 
 */
@Repository
public class BaseDao implements IBaseDao {

	public BaseDao() {

	}
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	

	@Override
	public Object insert(String paramString, Object paramObject)
			throws SQLException {
		return sqlSessionTemplate.insert(paramString, paramObject);
	}

	@Override
	public Object insert(String paramString) throws SQLException {
		return sqlSessionTemplate.insert(paramString);
	}

	@Override
	public int update(String paramString, Object paramObject)
			throws SQLException {
		return sqlSessionTemplate.update(paramString, paramObject);
	}

	@Override
	public int update(String paramString) throws SQLException {
		return sqlSessionTemplate.update(paramString);
	}

	@Override
	public int delete(String paramString, Object paramObject)
			throws SQLException {
		return sqlSessionTemplate.delete(paramString, paramObject);
	}

	@Override
	public int delete(String paramString) throws SQLException {
		return sqlSessionTemplate.delete(paramString);
	}

	@Override
	public Object queryForObject(String paramString, Object paramObject)
			throws SQLException {
		return sqlSessionTemplate.selectOne(paramString, paramObject);
	}

	@Override
	public Object queryForObject(String paramString) throws SQLException {
		return sqlSessionTemplate.selectOne(paramString);
	}

	@Override
	public List queryForList(String paramString, Object paramObject)
			throws SQLException {
		return sqlSessionTemplate.selectList(paramString, paramObject);
	}

	@Override
	public List queryForList(String paramString) throws SQLException {
		return sqlSessionTemplate.selectList(paramString);
	}

	@Override
	public List queryForList(String paramString, Object paramObject,
			int paramInt1, int paramInt2) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int queryForCount(String paramString, Object paramObject)
			throws SQLException {
		return sqlSessionTemplate.selectList(paramString, paramObject).size();

	}

}
