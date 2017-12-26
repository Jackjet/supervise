package gov.df.fap.service.hibernate;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.HibernateException;
/**
 * 
 * @author hult
 *
 */
public class GovHibernateTemplate extends org.springframework.orm.hibernate3.HibernateTemplate {
	 protected org.hibernate.Session getSession(){
		 org.hibernate.Session session = super.getSession();
		 //从前台接到申请后，事务代理的必须启动事务
		 if(!gov.df.fap.service.login.filter.UserSyncFilter.beginFilter){
			 return session;
		 }
		 Connection con = session.connection();
		    try {
		      if (con.getAutoCommit()) {
		        throw new HibernateException("未通过事务代理方式调用，请检查对应配置或代码逻辑业务实现类要以BO结尾");
		      }
		    } catch (SQLException e) {

		    }
		return session;
		 
	 }
}
