/**
 * Copyright 2013 by  Yonyou Software Co., Ltd.
 * All rights reserved.
 *
 * 版权所有：北京用友政务软件有限公司
 * 未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 * 侵权者将受到法律追究。
 */
package gov.df.fap.service.autotask;

import org.quartz.SchedulerConfigException;
import org.quartz.spi.ThreadPool;

/**
 * <p>
 * DESCRIPTION:
 * <p>
 * DESCRIPTION:
 * <p>
 * CALLED BY:
 * <p>
 * CREATE DATE: Jan 15, 2015
 * <p>
 * HISTORY: 1.0
 * 
 * @version 1.0
 * @author peak.edge
 * @since jdk 1.6.0_31
 * @see
 */
public class ZeroSizeThreadPool implements ThreadPool {

	public boolean runInThread(Runnable runnable) {
		if (runnable == null) {
			return false;
		}
		try {
			new Thread(runnable).start();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public int blockForAvailableThreads() {
		return 1;
	}

	public void initialize() throws SchedulerConfigException {
	}

	public void shutdown(boolean waitForJobsToComplete) {
	}

	public int getPoolSize() {
		return 0;
	}

	@Override
	public void setInstanceId(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInstanceName(String arg0) {
		// TODO Auto-generated method stub
		
	}
}
