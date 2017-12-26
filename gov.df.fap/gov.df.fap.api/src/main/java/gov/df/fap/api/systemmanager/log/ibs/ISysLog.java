/*
 * $Id: ISysLog.java 570988 2015-04-29 16:38:14Z zhaoqiang1 $
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.api.systemmanager.log.ibs;

import  gov.df.fap.util.xml.XMLData;

import java.util.List;


/**
 * 日志管理服务端接口
 * 
 * @author a
 * 
 */
public interface ISysLog {
	/**
	 * 获得符合条件的操作日志
	 * @return 缓存实体对象(CacheEntity)列表
	 * @throws Exception
	 */
    public XMLData findDatas(String sql, String countSql) throws Exception;
    
    /**
     * 删除日志数据
     * @param xmlData 日志XML对象
     * @throws Exception
     */
    public void deleteSysLoginfo(XMLData xmlData) throws Exception;

    /**
     * 批量删除业务数据
     * @param datas 业务数据集合
     * @throws Exception
     */
    public void batchDeleteSysLoginfos(List datas) throws Exception;
    
    public void batchBackUpDataToBakTable(String conditions);

    /**
         * 清除sys_loginfo表所有满足条件{oper_time>当前时间-30天}的记录
         * 
         * @author 黄节2007年9月15日添加
         */
    public void removeInvalidateLog();
}
