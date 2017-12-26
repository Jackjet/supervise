package gov.df.fap.service.util.dao.ufgovdao;

import gov.df.fap.service.util.datasource.MultiDataSource;

import java.util.List;

public class UfSqlLogBusiness implements IUfSqlLogBusiness {
	private long time = 0;
	/**
	 * 获取sql语句
	 */
	public List getSqlList() {
		time = System.currentTimeMillis();
		return UfConnection.pushOutSQL();
		
	}
	/**
	 * 开始sql语句收集调试
	 */
	public void startSQLDebug() {
		if(time==0)
		{
			MultiDataSource.isDebugSql = true;
			time = System.currentTimeMillis();
			new TimeEndThread().start();
		}
		
	}
	/**
	 * 结束sql语句收集
	 */
	public void stopSQLDebug() {

		MultiDataSource.isDebugSql = false;
		time = 0;
		
	}
	/**
	 * 超时停止sql收集30秒
	 * @author Administrator
	 *
	 */
	private class TimeEndThread extends Thread{
		public void run(){
			//如果超过180秒没有接收sql自动终止sql收集
			while(time!=0 && (System.currentTimeMillis()-time)< 180000l ){
				try {
					sleep(15000);
				} catch (InterruptedException e) {
				}
			}
			//终止sql收集
			time=0;
			MultiDataSource.isDebugSql = false;
			UfConnection.pushOutSQL().clear();
		}
	}

}
