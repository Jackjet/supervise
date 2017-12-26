package gov.df.fap.service.gl.coa.impl;

import gov.df.fap.api.dictionary.ElementInfo;
import gov.df.fap.api.dictionary.ElementOperation;
import gov.df.fap.api.gl.coa.BatchCodeCombinationProcesser;
import gov.df.fap.api.gl.coa.CodeCombination;
import gov.df.fap.api.gl.coa.CodeCombinationDefinition;
import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.api.gl.core.daosupport.CallableStatementHandler;
import gov.df.fap.bean.gl.GlConstant;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.coa.FCoaDetail;
import gov.df.fap.bean.gl.configure.CommonKey;
import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.bean.util.FBaseDTO;
import gov.df.fap.service.dictionary.elecache.DefaultElementInfo;
import gov.df.fap.service.gl.configure.impl.EngineConfiguration;
import gov.df.fap.service.gl.core.DaoSupport;
import gov.df.fap.service.gl.core.interfaces.ConnectionProvider;
import gov.df.fap.service.relation.CDD_Element;
import gov.df.fap.service.util.DatabaseAccess;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.exceptions.gl.CoaCascadeException;
import gov.df.fap.service.util.exceptions.gl.CoaNotExistsException;
import gov.df.fap.service.util.exceptions.gl.GlException;
import gov.df.fap.service.util.gl.coa.CcidUtil;
import gov.df.fap.service.util.gl.coa.cascade.CascadeCoaMap;
import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.service.util.memcache.MemCacheMap;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.DbUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.Properties.PropertiesUtil;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.exception.IllegalEleLevelOfDownStreamCoaException;
import gov.df.fap.util.exception.LackEleOfDownStreamCoaException;
import gov.df.fap.util.number.NumberUtil;
import gov.df.fap.util.xml.ParseXML;
import gov.df.fap.util.xml.XMLData;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * coa组件内部实现
 * 
 * @author lwq
 * @version 2017-03-06
 */
@Service
public class CoaService implements gov.df.fap.api.gl.coa.ibs.ICoa, gov.df.fap.api.gl.coa.IConfigCoa, ICoaService {

  private static final String PROPS_COA_ID = "coa_id";

  private static final String CCID_NOT_QUERY_ELEMENTS = "CCID_NOT_QUERY_ELEMENTS";

  private static final Log logger = LogFactory.getLog(CoaService.class);

  public final static int GTE = 2;// 大于且等于

  public final static int GT = 1;// 大于

  public final static int EQ = 0;// 等于

  public final static int LT = -1;// 小于

  public final static int LTE = -2;// 小于且等于

  protected static Map coaCache = MemCacheMap.getCacheMap(CoaService.class);

  @Autowired
  @Qualifier("df.fap.sessionFactoryConnectionProvider")
  private ConnectionProvider connectionProvider = null;

  @Autowired
  @Qualifier("df.fap.daoSupport")
  private DaoSupport daoSupport = null;

  @Autowired
  @Qualifier("elementOperationWrapper")
  private ElementOperation eleOp = null;

  @Autowired
  protected CoaDao dao = null;

  /** ccid 转换加速器 */
  @Autowired
  @Qualifier("CcidTransAccelerator")
  protected CcidAccelerator ccidTransAccelerator = null;

  /** ccid生成加速器 */
  @Autowired
  @Qualifier("CcidGenAccelerator")
  protected CcidAccelerator ccidGenAccelerator = null;

  private Map conflicts = new HashMap();

  private List coaListener = new ArrayList();

  @Autowired
  private CascadeCoaMap coaCascadeMap;

  public void setConnectionProvider(ConnectionProvider connPro) {
    this.connectionProvider = connPro;
  }

  public void setDaoSupport(DaoSupport daoSupport) {
    this.daoSupport = daoSupport;
  }

  public void setEleOp(ElementOperation eleOp) {
    this.eleOp = eleOp;
  }

  public void setCoaDao(CoaDao coaDao) {
    this.dao = coaDao;
  }

  public void setCcidTransAccelerator(CcidAccelerator ccid) {
    ccidTransAccelerator = ccid;
  }

  public void setCcidGenAccelerator(CcidAccelerator ccid) {
    ccidGenAccelerator = ccid;
  }

  protected CcidAccelerator getAccelerator() {
    return ccidGenAccelerator;
  }

  public CoaService() {
    this.addCoaListener(new CoaCacheListener());
  }

  /**
   * 取得用户名
   */
  public String getCreateUser(String userId) {
    return daoSupport.queryForString("select user_name from sys_user where user_id = '" + userId + "'");
  }

  /**
   * 手工批量生成CCID 检查转换表的有效性。返回 -1 表示 表不存在 ，0 表示 new_ccid 字段不存在， 1 表示 合法
   */
  public int checkTempTable(String table) {
    try {
      daoSupport.execute("select 1 from " + table);
    } catch (Exception e) {
      return -1;
    }
    try {
      daoSupport.execute("select new_ccid from " + table);
    } catch (Exception e) {
      return 0;
    }
    try {
    	String sql = null;
    	if(TypeOfDB.isOracle()) {
    		sql = "select alias.ccid vou_id,alias.* from "
    				+ table
    				+ " alias where rownum<501 and new_ccid is null and not exists(select 1 from gl_ccids t where t.ccid=alias.new_ccid) ";
    	} else if(TypeOfDB.isMySQL()) {
    		sql = "select alias.ccid vou_id,alias.* from "
    				+ table
    				+ " alias where new_ccid is null and not exists(select 1 from gl_ccids t where t.ccid=alias.new_ccid) limit 501 ";
    	}

      List list = this.daoSupport.genericQuery(sql, FVoucherDTO.class);
      while (list.size() > 0) {
        list = getCcidBatchReturn(list);
        insertNewCcid(table, list);
        this.daoSupport.execute("commit");
        List newlist = this.daoSupport.genericQuery(sql, FVoucherDTO.class);
        if (list.size() < 500 && list.size() == newlist.size()) {
          if ((((FVoucherDTO) list.get(0)).getVou_id()).equals(((FVoucherDTO) newlist.get(0)).getVou_id())) {
            throw new RuntimeException("存在未正常生成ccid的数据，需要重启系统再手工生成");
          }
        }
        list = newlist;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return 1;
  }

  /**
   * 获取手工批量生成CCID 的要数信息
   * 
   * @return
   */
  public List loadManualCcidInfo(String table) {
    String sql = "select alias.ccid vou_id,alias.* from " + table + " alias";
    List dataList = daoSupport.genericQuery(sql, FVoucherDTO.class);
    return dataList;
  }

  /**
   * 手工批量生成CCID 后插入新CCID 到转换表
   * 
   * @param table
   * @param dataList
   */
  public void insertNewCcid(String table, List dataList) {
    daoSupport.batchExcute("update " + table + " set new_ccid = #ccid# where ccid = #vou_id# and new_ccid is null",
      dataList);
  }

  /**
   * 从ccidTable 中加载CCID 要素信息
   */
  public List loadCcid(String alias, String condition) {
    if (alias == null)
      alias = "";
    return daoSupport.genericQuery("select * from gl_ccids " + alias + " where 1=1 " + condition, FVoucherDTO.class);
  }

  /**
   * 取得要素的级次信息(服务端直接使用,不对界面表现层公开)
   * 
   * @param coaId
   *            CoaId
   * @param eleCode
   *            要素编码
   * @param tableAlias
   *            表别名
   * @return String SQL语句段
 * @throws SQLException 
   */
  public String getEleLevelNum(String coaId, String eleCode, String tableAlias) {
    StringBuffer return_sql = new StringBuffer();
    String set_year = "";
    set_year = CommonUtil.getSetYear();
    if(TypeOfDB.isOracle()) {
    	return_sql.append(" and (").append(tableAlias)
    	.append(".level_num <= (select nvl(decode(level_num,'-2',9,'-1',9,level_num),9) ").append("from gl_coa_detail")
    	.append(Tools.addDbLink()).append(" where coa_id='").append(coaId).append("' and ele_code= '").append(eleCode)
    	.append("' and level_num <> 0) ").append("or exists (select 1 from gl_coa_detail").append(Tools.addDbLink())
    	.append(" a ").append("left join gl_coa_detail_code").append(Tools.addDbLink())
    	.append(" b on a.coa_detail_id = b.coa_detail_id ").append("and a.set_year = b.set_year where a.set_year = ")
    	.append(set_year).append(" and a.coa_id='").append(coaId).append("' and a.ele_code='").append(eleCode)
    	.append("' and a.level_num=0 and ").append(tableAlias).append(".chr_code = b.level_code))");
    } else if(TypeOfDB.isMySQL()) {
    	return_sql
    	.append(" and (")
    	.append(tableAlias)
    	.append(".level_num <= (select ifnull((case level_num when '-2' then 9 when '-1' then 9 else 9 end),9) ")
    	.append("from gl_coa_detail")
    	.append(Tools.addDbLink())
    	.append(" where coa_id='")
    	.append(coaId)
    	.append("' and ele_code= '")
    	.append(eleCode)
    	.append("' and level_num <> 0) ")
    	.append("or exists (select 1 from gl_coa_detail")
    	.append(Tools.addDbLink())
    	.append(" a ")
    	.append("left join gl_coa_detail_code")
    	.append(Tools.addDbLink())
    	.append(" b on a.coa_detail_id = b.coa_detail_id ")
    	.append("and a.set_year = b.set_year where a.set_year = ")
    	.append(set_year).append(" and a.coa_id='")
    	.append(coaId)
    	.append("' and a.ele_code='")
    	.append(eleCode)
    	.append("' and a.level_num=0 and ")
    	.append(tableAlias)
    	.append(".chr_code = b.level_code))");
    }
    return return_sql.toString();

  }

  /**
   * 比较获取coa相对列表
   * 
   * @param compareType
   *            (GT,GTE,EQ,LT,LTE) 比较类型(大于、等于、小于等)
   * @param compareCoaID
   *            比较目标coaID
   * @param condition
   *            其他过滤条件
   * @return 结果列表
   */
  public List getCoaCompareList(int compareType, String compareCoaID, String condition) {
    List cmpList = new ArrayList();
    XMLData coaData = getCoalist(condition);
    List coaList = coaData.getRecordList();
    for (int i = 0; i < coaList.size(); i++) {
      XMLData coa = (XMLData) coaList.get(i);
      String coa_id = (String) coa.get(PROPS_COA_ID);
      try {
        switch (compareType) {
        case GTE:
          if (compareCoa(coa_id, compareCoaID) >= 0) {
            cmpList.add(coa);
          }
          break;
        case GT:
          if (compareCoa(coa_id, compareCoaID) > 0) {
            cmpList.add(coa);
          }
          break;
        case EQ:
          if (compareCoa(coa_id, compareCoaID) == 0) {
            cmpList.add(coa);
          }
          break;
        case LT:
          if (compareCoa(coa_id, compareCoaID) < 0) {
            cmpList.add(coa);
          }
          break;
        case LTE:
          if (compareCoa(coa_id, compareCoaID) <= 0) {
            cmpList.add(coa);
          }
          break;
        }

      } catch (Exception e) {

      }
    }
    return cmpList;
  }

  /**
   * 得到所有的COA列表
   * 
   * @param condition
   *            过滤条件
   * @return coa XMLData列表
   */
  public XMLData getCoalist(String condition) {
    XMLData data = new XMLData();
    List list = dao.getCoaList(condition);
    if (list == null || list.isEmpty())
      return data;
    data.put("row", list);
    return data;
  }

  /**
   * 通过COA_ID读取COA对象
   * 
   * @param coaId
   * @return
   */
  public FCoaDTO getCoa(long coaId) {
    try {
      Object coaKey = new CommonKey(StringUtil.toStr(coaId), CommonUtil.getSetYear(), CommonUtil.getRgCode());
      FCoaDTO resultObject = (FCoaDTO) coaCache.get(coaKey);
      if (resultObject == null) {
        resultObject = dao.loadCoa(coaId);
        if (resultObject != null)
          coaCache.put(coaKey, resultObject);
      }

      return resultObject;
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  /**
   * 取得COA的结构描述
   * 
   * @param coa_id
   *            ： 标示COA唯一的ID
   * @return coa对应XMLData对象
   */
  public XMLData getCoabyID(String coa_id) {
    return dao.getCoabyID(coa_id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.CoaService#getCCIDByXMLData(java.lang
   * .String, gov.gfmis.fap.util.XMLData)
   */
  public String getCCIDByXMLData(long coa_id, XMLData xmlEleValue) throws Exception {
    if (xmlEleValue == null) {
      throw new GlException("未正确传入业务数据信息,无法获取ccid");
    } else if (coa_id == 0) {
      throw new GlException("未正确传入coa_id信息,无法获取ccid");
    }
    return getCCID(coa_id, xmlEleValue);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.CoaService#getCCIDByBaseDTO(gov.gfmis
   * .fap.util.FBaseDTO)
   */
  public String getCCIDByBaseDTO(FBaseDTO inputFVoucherDto) throws Exception {
    if (inputFVoucherDto == null) {
      throw new GlException("未正确传入业务数据信息,无法获取ccid");
    } else if (inputFVoucherDto.getCoa_id() == null || inputFVoucherDto.getCoa_id().equals("")) {
      throw new GlException("未正确传入coa_id信息,无法获取ccid");
    }

    return getCCID(NumberUtil.toLong(inputFVoucherDto.getCoa_id()), inputFVoucherDto);
  }

  /**
   * 根据具体的要素配置和coa配置查询其ccid，如果CCID存在，直接使用存在的CCID，如果不存在，需要创建新的CCID
   * 
   * @param session
   *            当前的数据库连接
   * @param coa_id
   *            标示唯一的业务要素级次设置
   * @param xmlEleValue
   *            要素信息
   * @return 成功调用接口时返回CCID
   * @throws 异常明细
   * @deprecated
   */
  public String getCCIDbyELEvalue(String coa_id, String xmlEleValue) throws Exception {
    return getCCIDByXMLData(NumberUtil.toLong(coa_id), ParseXML.convertXmlToObj(xmlEleValue));
  }

  /**
   * 根据Bean对象生成ccid
   * 
   * @param data
   *            该bean可以是任何对象,只要含有getXX_id方法便可,xx是要素简称;bean也可以是Map对象.只要
   *            对象中含有xx_id的key就可以.
   * @return ccid值
   * @throws 内部异常
   */
  public synchronized String getCCID(long coaId, Object elementContainer) {
    FCoaDTO coaDto = getCoa(coaId);
    int setYear = CommonUtil.getIntSetYear();
    long ccid = ccidGenAccelerator.generateCcid(coaDto, elementContainer, setYear, false);
    return StringUtil.toStr(ccid);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.CoaService#getCcidBatch(java.util.List)
   */
  public synchronized void getCcidBatch(final List beanList) {
    if (beanList == null || beanList.isEmpty())
      return;
    int setYear = CommonUtil.getIntSetYear();
    ccidGenAccelerator.generateCcidBatch(new BatchCcidGenProcesser(beanList, beanList, this), setYear);
  }

  /**
   * 主要用于手工生成CCID调用
   */
  public List getCcidBatchReturn(List beanList) {
    getCcidBatch(beanList);
    try {
      for (int i = 0; i < beanList.size(); i++)
        saveManualCcid(((FBaseDTO) beanList.get(i)).getCcid());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return beanList;
  }

  /**
   * 保存一份手工生成的CCID 到gl_manual_ccids 作为记录
   * 
   * @param ccid
   */
  public void saveManualCcid(String ccid) {
    int m = daoSupport.queryForInt("select 1 from gl_manual_ccids where ccid =" + ccid);
    if (m == 0)
      daoSupport.execute("insert into gl_manual_ccids(create_date,ccid) values ('" + DateHandler.getLastVerTime()
        + "'," + ccid + ")");
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.CoaService#preCCIDTrans(java.lang.String
   * , java.lang.String)
   */
  public String preCCIDTrans(long toCoaId, String ccid, boolean misMatch) {
    int setYear = NumberUtil.toInt(CommonUtil.getSetYear());
    FCoaDTO targetCoa = getCoa(toCoaId);
    return preCcidTrans(targetCoa, ccid, setYear, misMatch);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.coa.CoaService#preCcidTrans(gov.gfmis.fap.dictionary
   * .coa.FCoaDTO, java.lang.String, int, boolean)
   */
  public String preCcidTrans(FCoaDTO targetCoa, String ccid, int setYear, boolean misMatch) {
    if (targetCoa == null) {

      logger.error("COA is null!");

      throw new RuntimeException("根据传入的coa id无法找到coa, ccid转换失败!");
    }
    long intInputCcid = NumberUtil.toLong(ccid);
    Object elementContainer = dao.findCcid(getCoa(CcidUtil.getCoaIdByCcid(ccid)), intInputCcid);
    CodeCombination existsCodeCmb = ccidTransAccelerator.getCcidExists(targetCoa,
      dao.findCcidKey(targetCoa, intInputCcid));
    if (existsCodeCmb != null)
      return StringUtil.toStr(existsCodeCmb.getCcid());
    return preCcidTrans(targetCoa, elementContainer, setYear, misMatch);
  }

  public String preCcidTrans(FBaseDTO baseDTO, int setYear, boolean misMatch) {
    return preCcidTrans(getCoa(NumberUtil.toLong(baseDTO.getCoa_id())), baseDTO, setYear, misMatch);
  }

  public String preCcidTrans(FCoaDTO targetCoa, Object elementContainer, int setYear, boolean misMatch) {
    // doPreCcidTrans(targetCoa, elementContainer, setYear);
    long genCcid = ccidTransAccelerator.generateCcid(targetCoa, elementContainer, setYear, misMatch);
    return StringUtil.toStr(genCcid);
  }

  public void preCcidTransBatch(BatchCodeCombinationProcesser processer) {
    int setYear = NumberUtil.toInt(CommonUtil.getSetYear());
    ccidTransAccelerator.generateCcidBatch(processer, setYear);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.service.CoaService#doPreCcidTrans(gov.gfmis.
   * fap.dictionary.coa.FCoaDTO, java.lang.Object, int)
   */
  public Object doPreCcidTrans(final FCoaDTO targetCoa, Object elementsContainer, int setYear) {
    return generateCcidObject(targetCoa, elementsContainer, setYear, new CoaElementAccessorAdaptor() {
      ElementInfo targetElement = null;

      ElementInfo tempElement = null;

      String eleCode = null;

      public ElementInfo elementAccess(FCoaDetail targetCoaDetail, String sourceEleChrId) {
        if (StringUtil.isEmpty(sourceEleChrId))
          return null;
        int targetEleLevelNum = targetCoaDetail.getLevelNum();
        eleCode = targetCoaDetail.getEleCode();
        final ElementInfo sourceEleValue = eleOp.getElementInfo(eleCode, sourceEleChrId);
        // modified by ydz 2010.1.4
        if (sourceEleValue == null && (getSpeclialElements().contains(targetCoaDetail.getEleCode().toUpperCase()))) {
          // 如果找不到指标明细要素,允许程序往下走,因为目前指标明细要素是从指标明细表中取出来的,指标录入时无法找到要素值.
          targetElement = new DefaultElementInfo(sourceEleChrId, sourceEleChrId, sourceEleChrId, true, targetCoaDetail
            .getLevelNum(), null);
          return targetElement;
        }

        // 任意级次,底级的取数方式一样,校验方式不一样.
        targetElement = sourceEleValue;

        if (targetEleLevelNum > 0) {
          while (targetElement.getLevelNum() > targetEleLevelNum) {
            targetElement = targetElement.getParent();
          }
        }

        // modified by zhoulingling 2011-05-17
        // 若targetElement为null，说明根据id没有找到要素值，抛出异常
        if (targetElement == null) {
          throw new RuntimeException("对不起，根据要素chr_id " + sourceEleChrId + " ，无法找到 " + targetCoaDetail.getEleName()
            + " 的要素值！");
        }

        // modified by yl/ydz 2010/01/26 支持自定义 匹配
        String tempCoaCode = targetElement.getChrCode();
        if (targetEleLevelNum == 0) {
          if (logger.isDebugEnabled())
            System.out.println("进入自定义级次要素匹配");
          if (isSelfDefined(tempCoaCode, targetCoaDetail)) {
            tempElement = targetElement;
            if (logger.isDebugEnabled())
              System.out.println("匹配为本级的要素");

          } else {
            if (logger.isDebugEnabled())
              System.out.println("寻找上级要素");
            tempElement = targetElement;
            do {
              String pchr_id = tempElement.getParent().getChrId();
              tempElement = tempElement.getParent();
              tempCoaCode = tempElement.getChrCode();
              if (pchr_id == null || tempCoaCode == null || pchr_id.equals("") || pchr_id.length() == 0) {
                throw new RuntimeException("对不起，要素" + targetElement.getChrCode() + targetElement.getChrName()
                  + "无法找到要素, 数据生成失败！");
              }

            } while (!isSelfDefined(tempCoaCode, targetCoaDetail));
          }
          if (logger.isDebugEnabled())
            System.out.println(tempElement.getChrCode().toString());
          targetElement = tempElement;
        }

        if (logger.isDebugEnabled())
          logger.debug("匹配" + targetCoa + ", 要素" + targetCoaDetail.getEleCode() + targetCoaDetail.getLevelName()
            + " 源要素值chr_code=" + (sourceEleValue == null ? "NULL" : sourceEleValue.getChrCode()) + " 匹配到"
            + (targetElement == null ? "NULL" : targetElement.getChrCode()));

        return targetElement;
      }

      // 判断是否为自定义级次
      private boolean isSelfDefined(String tempCoaCode, FCoaDetail targetCoaDetail) {
        if (targetCoaDetail.getCoaDetailCode() == null)
          return false;
        for (int i = 0; i < targetCoaDetail.getCoaDetailCode().size(); i++) {
          if (tempCoaCode.equals(targetCoaDetail.getCoaDetailCode().get(i)))
            return true;
        }
        return false;
      }

      // modified by ydz
      public boolean isCheckElement(FCoaDetail coaDetail) {
        return !getSpeclialElements().contains(coaDetail.getEleCode().toUpperCase());
      }
    });
  }

  /**
   * 生成CCID对象
   * 
   * @param targetCoa
   * @param data
   * @param setYear
   * @return 生成的新CCID
   * @author
   */
  public Object generateCcidObject(FCoaDTO targetCoa, Object data, int setYear) {
    return generateCcidObject(targetCoa, data, setYear, new CoaElementAccessorAdaptor() {
      ElementInfo targetElement = null;

      public ElementInfo elementAccess(FCoaDetail targetCoaDetail, String sourceEleChrId) {
        if (StringUtil.isEmpty(sourceEleChrId))
          return null;
        targetElement = eleOp.getElementInfo(targetCoaDetail.getEleCode(), sourceEleChrId);
        if (targetElement == null && (getSpeclialElements().contains(targetCoaDetail.getEleCode().toUpperCase()))) {

          // 如果找不到指标明细要素,允许程序往下走,因为目前指标明细要素是从指标明细表中取出来的,指标录入时无法找到要素值.
          if (logger.isDebugEnabled())
            logger.debug("特殊处理要素BUDGET_VOU、GP_VOU");

          targetElement = new DefaultElementInfo(sourceEleChrId, sourceEleChrId, sourceEleChrId, true, targetCoaDetail
            .getLevelNum(), null);
        }

        if (logger.isDebugEnabled())
          logger.debug("生成CCID过程,读取要素" + targetCoaDetail.getEleCode() + " chr_id="
            + (targetElement == null ? null : targetElement.getChrId()) + " chr_code="
            + (targetElement == null ? null : targetElement.getChrCode()));

        return targetElement;
      }

      // modified by ydz
      public boolean isCheckElement(FCoaDetail coaDetail) {
        return !getSpeclialElements().contains(coaDetail.getEleCode().toUpperCase());
      }
    });
  }

  /**
   * 根据指定的要素读取方式,生成CCID对象
   * 
   * @param targetCoa
   * @param data
   * @param setYear
   * @param accessor
   * @return
   */
  private Object generateCcidObject(FCoaDTO targetCoa, Object data, int setYear, CoaElementAccessor accessor) {

    String sourceEleChrId = null;
    ElementInfo targetElement = null;
    HashMap elementsContainer = new HashMap();
    final Iterator insertCcidIt = targetCoa.getCoaDetailList().iterator();
    while (insertCcidIt.hasNext()) {
      final FCoaDetail targetCoaDetail = (FCoaDetail) insertCcidIt.next();
      final String eleCode = targetCoaDetail.getEleCode();
      final String eleLower = eleCode.toLowerCase();
      final String eleLowerKey = eleLower + GlConstant.ID_SUBFIX;
      sourceEleChrId = (String) PropertiesUtil.getProperty(data, eleLowerKey);
      targetCoaDetail.setCoaDTO(targetCoa);
      // 默认值
      if (StringUtil.isEmpty(sourceEleChrId)) {
        sourceEleChrId = targetCoaDetail.getDefaultValue();
      }
      int targetEleLevelNum = targetCoaDetail.getLevelNum();
      if (targetEleLevelNum > 6 || targetEleLevelNum < -2)
        throw new RuntimeException("目前只支持6级以内的基础数据");

      targetElement = accessor.elementAccess(targetCoaDetail, sourceEleChrId);
      if (accessor.isCheckElement(targetCoaDetail))
        checkElementInfo(targetCoaDetail, targetElement, sourceEleChrId);
      elementsContainer.put(targetCoaDetail.getEleCode(), targetElement);
    }
    CodeCombination newCodeCmb = caculateCcidWithElementInfo(targetCoa, elementsContainer);
    elementsContainer.put(GlConstant.CCID_KEY, StringUtil.toStr(newCodeCmb.getCcid()));
    elementsContainer.put(GlConstant.COA_ID_KEY, newCodeCmb.getCoaId());
    elementsContainer.put(GlConstant.MD5_KEY, newCodeCmb.getMd5());

    if (logger.isDebugEnabled()) {
      logger.debug("生成要素组合对象, coa_id=" + newCodeCmb.getCoaId() + " ccid=" + newCodeCmb.getCcid() + " md5="
        + newCodeCmb.getMd5());
    }
    return elementsContainer;
  }

  /**
   * 判断是否账户信息
   * 
   * @param eleCode
   *            要素简称
   * @return 要素是否账户
   */
  public static boolean isAccount(String eleCode) {
    return eleCode.equalsIgnoreCase(GlConstant.BAC_KEY) || eleCode.equalsIgnoreCase(GlConstant.BAP_KEY)
      || eleCode.equalsIgnoreCase(GlConstant.BAI_KEY);
  }

  /**
   * 读取账户附加字段名
   * 
   * @param eleCode
   *            要素简称
   * @return 要素附加的字段名(多个,以","分割)
   */
  public static String getAccountAddtionalField(String eleCode) {

    final String prefix = (String) GlConstant.accountPrefixMapping.get(eleCode.toUpperCase());
    StringBuffer accountAppend = new StringBuffer();
    return accountAppend.append(prefix).append("_account_no,").append(prefix).append("_account_name,").append(prefix)
      .append("_account_bank").toString();
  }

  /**
   * 根据要素级次读取要素值
   * 
   * @param targetElement
   * @param eleValue
   * @return
   * @throws Exception
   */
  private void checkElementInfo(FCoaDetail targetElement, ElementInfo sourceElementValue, String sourceEleChrId) {

    boolean throwExcep = false;

    if (sourceElementValue == null) {
      if (targetElement.getLevelNum() == -2 && StringUtil.isEmpty(sourceEleChrId))// 任意级次且传入要素chr_id为空
        return;// 正常情况,验证通过
      else if (!StringUtil.isEmpty(sourceEleChrId)) {// 传入要素chr_id有值,但在基础数据中找不到,提示错误
        throwExcep = true;
      } else if (targetElement.getIsMustInput() == 0) {
        return;// 不必录入
      }
      throwExcep = true;
    }
    if (throwExcep)
      throwEleLevelNumException(targetElement, sourceElementValue, sourceEleChrId);

    int sourceElementValueLevel = sourceElementValue.getLevelNum();
    boolean sourceElementValueIsLeaf = sourceElementValue.isLeaf();

    if (targetElement.getLevelNum() > 0) {// 指定级次,也要支持低于指定级级次的底级.
      throwExcep = sourceElementValueLevel != targetElement.getLevelNum()
        && !(targetElement.getLevelNum() > sourceElementValueLevel && sourceElementValueIsLeaf);
    } else if (targetElement.getLevelNum() == -1) {// 底级
      throwExcep = !sourceElementValueIsLeaf;
    } else if (targetElement.getLevelNum() == -2) {// 任意级次
      throwExcep = false;
    } else if (targetElement.getLevelNum() == 0) {
      throwExcep = dao.isCustomerElementValid(targetElement, sourceElementValue.getChrCode());
    }

    if (throwExcep) {
      throwEleLevelNumException(targetElement, sourceElementValue, sourceEleChrId);
    }
  }

  /**
   * 根据要素指定级次及错误要素信息抛出异常信息.
   * 
   * @param errorElement
   * @param errorElementValue
   */
  public void throwEleLevelNumException(FCoaDetail errorElement, ElementInfo errorElementValue, String sourceEleChrId) {
    StringBuffer sb = new StringBuffer();
    XMLData eleSet = eleOp.getEleSetByCode(errorElement.getEleCode());
    String eleName = (String) eleSet.get(CodeCombinationDefinition.ELE_NAME);
    sb.append(errorElement.getCoaDto()).append("中").append(eleName).append("级次为").append(errorElement.getLevelName())
      .append("，").append("传入要素");
    if (errorElementValue == null) {
      if (StringUtil.isEmpty(sourceEleChrId))
        sb.append("为空，请重新输入").append(errorElement.getLevelName()).append("的").append(eleName).append("！");
      else
        sb.append("CHR_ID：" + sourceEleChrId + "无法在基础数据中找到！").append("请重新输入").append(errorElement.getLevelName())
          .append("的").append(eleName).append("！");
    } else {
      String chrCode = errorElementValue.getChrCode();
      String chrName = errorElementValue.getChrName();
      int level = errorElementValue.getLevelNum();
      sb.append(chrCode).append(chrName).append("的级次为").append(FCoaDetail.LEVEL_NAMES[level + 2]).append("，请重新输入")
        .append(errorElement.getLevelName()).append("的").append(eleName).append("！");
    }

    if (logger.isErrorEnabled())
      logger.error(sb.toString());

    throw new RuntimeException(sb.toString());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * gov.gfmis.fap.dictionary.coa.CoaService#ccidMatchRule(java.lang.String,
   * long)
   */
  public boolean ccidMatchRule(final String ruleId, final long ccid) {
    Integer intBool = (Integer) daoSupport.executeCall("{? = call RULE_IS_MATCH_ADAPTER(?,?)}",
      new CallableStatementHandler() {

        public Object handleCallStatement(CallableStatement callSt) throws SQLException {
          callSt.registerOutParameter(1, Types.INTEGER);
          callSt.setString(2, ruleId);
          callSt.setString(3, StringUtil.toStr(ccid));
          callSt.execute();
          return callSt.getObject(1);
        }
      });
    return intBool.intValue() == NumberUtil.INT_TRUE;
  }

  public String createRcid(final long ccid, final int setYear) {
    return (String) daoSupport.executeCall("{? = call CREATE_RCID(?,?)}", new CallableStatementHandler() {

      public Object handleCallStatement(CallableStatement callSt) throws SQLException {
        callSt.registerOutParameter(1, Types.VARCHAR);
        callSt.setString(2, StringUtil.toStr(ccid));
        callSt.setString(3, StringUtil.toStr(setYear));
        callSt.execute();
        return callSt.getObject(1);
      }
    });
  }

  public FBaseDTO getCCIDInfo(FCoaDTO coa, long ccid) {
    String sql = "select * from " + coa.getCcidsTable() + " where ccid=" + ccid;
    FBaseDTO result = (FBaseDTO) daoSupport.genericQueryForObject(sql, FBaseDTO.class);
    Map ccidObject = null;
    if (result == null && ccidObject == null)
      ccidObject = ccidTransAccelerator.getCcidObject(coa, ccid);
    if (result == null && ccidObject == null)
      ccidObject = ccidGenAccelerator.getCcidObject(coa, ccid);

    if (result == null && ccidObject != null) {
      result = new FBaseDTO();
      for (int i = 0; i < coa.size(); i++) {
        final String upperEleCode = coa.get(i).getEleCode();
        final String lowerEleCode = upperEleCode.toLowerCase();
        final Object setValue = PropertiesUtil.getProperty(ccidObject, upperEleCode + ".chrId");
        final Object setCodeValue = PropertiesUtil.getProperty(ccidObject, upperEleCode + ".chrCode");
        final Object setNameValue = PropertiesUtil.getProperty(ccidObject, upperEleCode + ".chrName");
        PropertiesUtil.setProperty(result, lowerEleCode + "_id", setValue);
        PropertiesUtil.setProperty(result, lowerEleCode + "_code", setCodeValue);
        PropertiesUtil.setProperty(result, lowerEleCode + "_name", setNameValue);
      }
    }
    if (result != null)
      return result;
    else
      throw new GlException("没有该ccid信息");
  }

  /**
   * 根据ccid获取对应的要素id,code,name信息
   * 
   * @param ccid
   *            要素配置唯一码
   * @return CCID要素信息XML字符串
   */
  public String getCCIDDetail(String ccid) {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuffer strSQL = new StringBuffer();
    XMLData data = new XMLData();
    XMLData eleMap = new XMLData();
    String xmlString = "<data/>";
    if (ccid == null || ccid.equalsIgnoreCase(""))
      return xmlString;
    try {
      String set_year = "2006";
      try {
        set_year = CommonUtil.getSetYear();
      } catch (Exception e) {
        return xmlString;
      }

      conn = connectionProvider.getConnection();
      strSQL.append("select distinct a.ele_code,c.ele_source from gl_coa_detail").append(Tools.addDbLink())
        .append(" a,").append(this.getCoa(CcidUtil.getCoaIdByCcid(ccid)).getCcidsTable()).append(Tools.addDbLink())
        .append(" b,sys_element").append(Tools.addDbLink()).append(" c where a.set_year=").append(set_year)
        .append(" and a.set_year = b.set_year and a.set_year=")
        .append("c.set_year and a.coa_id = b.coa_id and a.ele_code = c.ele_code ").append("and b.ccid = '")
        .append(ccid).append("'");
      ps = conn.prepareStatement(strSQL.toString());
      rs = ps.executeQuery();
      while (rs.next()) {
        eleMap.put(rs.getString(1), rs.getString(2));
      }
      rs.close();
      rs = null;
      ps.close();
      ps = null;
      strSQL.delete(0, strSQL.length());
      strSQL.append("select ");
      Object[] ele_array = eleMap.keySet().toArray();
      if (ele_array.length == 0) {
        throw new Exception("根据传入的ccid" + ccid + "未查询到任何要素级次配置信息,请检查相关配置!");
      }
      for (int i = 0; i < ele_array.length; i++) {
        String ele = (String) ele_array[i];
        String ele_source = (String) eleMap.get(ele_array[i]);
        strSQL.append("a.").append(ele).append("_id,").append("(select chr_code from ").append(ele_source)
          .append(Tools.addDbLink()).append(" where chr_id = a.").append(ele).append("_id and set_year = a.set_year)")
          .append(" as ").append(ele).append("_code,").append("(select chr_name from ").append(ele_source)
          .append(Tools.addDbLink()).append(" where chr_id = a.").append(ele).append("_id and set_year = a.set_year)")
          .append(" as ").append(ele).append("_name");
        if (i < ele_array.length - 1) {
          strSQL.append(",");
        }
      }
      strSQL.append(" from ").append(this.getCoa(CcidUtil.getCoaIdByCcid(ccid)).getCcidsTable())
        .append(Tools.addDbLink()).append(" a where a.ccid = '").append(ccid).append("' and a.set_year=")
        .append(set_year);
      ps = conn.prepareStatement(strSQL.toString());
      rs = ps.executeQuery();
      if (rs.next()) {
        data = (XMLData) DatabaseAccess.getProperties(rs);
      }
      xmlString = data.asXML("data");
    } catch (Exception e) {
      logger.error(e.getMessage());
    } finally {
      DbUtil.closeResultSet(rs);
      DbUtil.closeStatement(ps);
    }
    return xmlString;
  }

  /**
   * coa对比函数,次别排序 -1 > 正数级次 > 0 > -2(-1底级,正数级次为具体级次,0自定义,-2任意)
   * 
   * @param left_coa_id
   *            左比较coa
   * @param right_coa_id
   *            右比较coa
   * @return -1:left_coa_id<right_coa_id 0:left_coa_id=right_coa_id
   *         1:left_coa_id>right_coa_id
   */
  public int compareCoa(String left_coa_id, String right_coa_id) throws Exception {
    int compareInt = -1;// 默认小于
    XMLData leftData = getCoabyID(left_coa_id);
    XMLData rightData = getCoabyID(right_coa_id);
    if (leftData == null || rightData == null) {
      if (leftData == rightData)
        compareInt = 0;
      else if (leftData == null)
        compareInt = -1;
      else if (rightData == null)
        compareInt = 1;
    } else {
      List leftList = leftData.getRecordList();
      List rightList = rightData.getRecordList();
      if (leftList.size() == 0 || rightList.size() == 0) {
        if (leftList.size() > 0)
          compareInt = 1;
        else if (rightList.size() > 0)
          compareInt = -1;
        else
          compareInt = 0;
      } else {
        for (int i = 0; i < leftList.size(); i++) {
          XMLData left = (XMLData) leftList.get(i);
          String left_ele_code = (String) left.get("ele_code");
          int left_level_num = Integer.parseInt((String) left.get("level_num"));
          for (int j = 0; j < rightList.size(); j++) {
            XMLData right = (XMLData) rightList.get(j);
            String right_ele_code = (String) right.get("ele_code");
            int right_level_num = Integer.parseInt((String) right.get("level_num"));
            if (left_ele_code.equalsIgnoreCase(right_ele_code)) {
              // 此处利用coa界面上正数级次(1,2)只能配置到level_num-1,而底级只能用-1表达的特性
              compareInt = left_level_num == right_level_num ? 0 : left_level_num == -1 ? 1
                : right_level_num == -1 ? -1 : left_level_num > right_level_num ? 1 : -1;
              if (compareInt != 0)
                return compareInt;
              else
                break;
            }
          }
        }
      }
    }
    return compareInt;
  }

  /**
   * 根据coa_id和对应要素配置信息对象新增对应的coa要素级次配置
   * 
   * @param coaData
   *            coa配置信息
   * @return XMLData 保存后的对象
   * @throws Exception
   *             异常
   */
  public XMLData saveCoa(XMLData coaData) throws Exception {
    XMLData coaReturn = dao.saveCoa(coaData);
    this.fireCoaListener();
    return coaReturn;
  }

  /**
   * 根据coa_id和对应要素配置信息对象修改对应的coa要素级次配置
   * 
   * @param coaData
   *            coa配置信息
   * @throws Exception
   *             异常
   */
  public void updateCoa(XMLData coaData) throws Exception {
    dao.updateCoa(coaData);
    this.fireCoaListener();
  }

  /**
   * 校验修改的级联coa
   * 
   * @param coaData
   * @throws CoaCascadeException
   */
  public void checkCascadeCoa(FCoaDTO coaData) throws Exception {
    boolean isNeedCheck = coaCascadeMap.isNeedCascade(coaData);
    if (isNeedCheck)
      coaCascadeMap.checkCoaCascadeMessage(coaData);
  }

  /**
   * 更新coa配置
   * 
   * @param coaDto
   * @param isCascadeSave
   *            是否级联保存
   * @throws Exception
   */
  public void updateCoaDto(FCoaDTO coaData, boolean isCascadeSave) throws Exception {
//    if (isCascadeSave && coaCascadeMap.isNeedCascade(coaData))
//      coaCascadeMap.checkCoaCascade(coaData);
//    else {
//      if (isGenCcid(coaData))
//        throw new Exception("要素级次设置" + coaData.getCoaCode() + " " + coaData.getCoaName() + "已经生成ccid不能修改!");
      dao.updateCoa(coaData);
//      coaCascadeMap.initCascadeMap(); 
//    }
    this.fireCoaListener();
  }

  /**
   * COA要素是否改变
   * 
   * @param sourceCoa
   *            源COA
   * @param targetCoa
   *            目标COA
   * @return 允许COA进行修改
   */
  private boolean allowCoaModify(FCoaDTO sourceCoa, FCoaDTO targetCoa) {

    if (targetCoa == null)
      return !dao.checkCcidIsExists(sourceCoa);

    if (sourceCoa.getCoaDetail().size() != targetCoa.getCoaDetail().size())
      return !dao.checkCcidIsExists(sourceCoa);

    List sourceDetailList = sourceCoa.getCoaDetail();
    List targetDetailList = targetCoa.getCoaDetail();
    boolean pass = false;
    for (int i = 0; i < sourceDetailList.size(); i++) {
      pass = false;
      final FCoaDetail sourceDetail = (FCoaDetail) sourceDetailList.get(i);
      for (int j = 0; j < targetDetailList.size(); j++) {
        final FCoaDetail targetDetail = (FCoaDetail) targetDetailList.get(j);
        if (StringUtil.equalsIgnoreCase(sourceDetail.getEleCode(), targetDetail.getEleCode())) {
          pass = true;
          break;
        }
      }
      if (!pass)
        break;
    }
    return pass || (!pass && !dao.checkCcidIsExists(sourceCoa));
  }

  /**
   * 删除一条coa要素配置
   * 
   * @param coa_id
   *            要素配置信息
   * @return 是否成功
   * @throws Exception
   *             异常
   */
  public void deleteCoa(String coa_id) throws Exception {
    FCoaDTO sourceCoa = dao.loadCoa(NumberUtil.toLong(coa_id));
    if (!allowCoaModify(sourceCoa, null)) {
      throw new RuntimeException("该COA已经生成CCID, 要素不可以修改!只可以修改要素默认值与要素级次");
    }

    dao.deleteCoa(coa_id);
    this.fireCoaListener();
  }

  public boolean validateDownStreamCoaLevel(long upStreamCoaId, long downStreamCoaId)
    throws IllegalEleLevelOfDownStreamCoaException, LackEleOfDownStreamCoaException {
    // if (upStreamCoaId <= 0 )
    // throw new IllegalArgumentException("上游科目尚未挂接coa");
    // if( downStreamCoaId <= 0)
    // throw new IllegalArgumentException("下游科目尚未挂接coa");
    if (upStreamCoaId == downStreamCoaId)
      return true;

    FCoaDTO upStreamCoa = getCoa(upStreamCoaId);
    FCoaDTO downStreamCoa = getCoa(downStreamCoaId);
    if (upStreamCoa == null)
      throw new CoaNotExistsException(true);
    if (downStreamCoa == null)
      throw new CoaNotExistsException(false);

    List upStreamCoaDetailList = upStreamCoa.getCoaDetailList();
    List downStreamCoaDetailList = downStreamCoa.getCoaDetailList();

    for (int i = 0; i < upStreamCoaDetailList.size(); i++) {
      FCoaDetail upStreamCoaDetail = (FCoaDetail) upStreamCoaDetailList.get(i);
      String upStreamEleCode = upStreamCoaDetail.getEleCode();
      int upStreamEleNum = upStreamCoaDetail.getLevelNum();

      boolean existEleInDownStreamCoa = false;
      for (int j = 0; j < downStreamCoaDetailList.size(); j++) {
        FCoaDetail downStreamCoaDetail = (FCoaDetail) downStreamCoaDetailList.get(j);
        String downStreamEleCode = downStreamCoaDetail.getEleCode();
        int downStreamEleNum = downStreamCoaDetail.getLevelNum();
        // 校验下游中是否存在上游配置的要素
        if (downStreamEleCode.equals(upStreamEleCode)) {
          existEleInDownStreamCoa = true;
          // 如果下游的要素是底级；下游中的要素级次要比上游细；上游和下游都是底级
          if (downStreamEleNum == -1 || downStreamEleNum >= upStreamEleNum) {
            break;
          } else {
            throw new IllegalEleLevelOfDownStreamCoaException("下游coa的要素[eleCode=" + downStreamEleCode + "]的级别为"
              + downStreamEleNum + ",而上游coa的此要素的级别为" + upStreamEleNum + ",不满足下游要素比上游要素级次细的条件");
          }
        }
      }
      if (!existEleInDownStreamCoa)
        throw new LackEleOfDownStreamCoaException("下游coa缺少上游coa所有的要素[" + upStreamEleCode + "]");
    }

    return true;
  }

  public FCoaDTO saveCoaDto(FCoaDTO coaDto) throws Exception {
    // coaCache.clear();
    dao.saveCoa(coaDto);
    this.fireCoaListener();
    return coaDto;
  }

  public List getAllCoa(){
    List list = new LinkedList();
    List coaList = (List) getCoalist("").get("row");
    Iterator iterator = coaList.iterator();
    while (iterator.hasNext()) {
      long coaId = NumberUtil.toLong(((XMLData) iterator.next()).get(PROPS_COA_ID).toString());
      list.add(getCoa(coaId));
    }
    return list;
  }

  public void reinstallCoa(List coaList) throws Exception {
    final Iterator iterator = dao.getCoaDTOList().iterator();
    Iterator tempIt = null;
    FCoaDTO coaDto = null;
    FCoaDTO tempCoaDto = null;
    while (iterator.hasNext()) {
      coaDto = (FCoaDTO) iterator.next();
      tempIt = coaList.iterator();
      while (tempIt.hasNext()) {
        tempCoaDto = (FCoaDTO) tempIt.next();
        if (coaDto.getCoaCode().equals(tempCoaDto.getCoaCode())) {
          tempCoaDto.setCoaId(coaDto.getCoaId());
          break;
        }
      }
    }
    dao.deleteAllCoa();
    batchSaveCoa(coaList);
  }

  public void batchSaveCoa(List coaList) throws Exception {
    final Iterator iteraotr = coaList.iterator();
    while (iteraotr.hasNext()) {
      dao.saveCoa((FCoaDTO) iteraotr.next());
    }
  }

  public void batchUpdateCoa(List coaList) throws Exception {
    final Iterator iteraotr = coaList.iterator();
    while (iteraotr.hasNext()) {
      dao.updateCoa((FCoaDTO) iteraotr.next());
    }
  }

  public void batchDeleteCoa(List coaList) throws Exception {
    final Iterator iteraotr = coaList.iterator();
    while (iteraotr.hasNext()) {
      dao.deleteCoa(((FCoaDTO) iteraotr.next()).getCoaId());
    }
  }

  public CodeCombination caculateCcid(FCoaDTO coa, Object elementsContainer) {
    CodeCombination codeCombination = CcidUtil.caculateCcid(coa, elementsContainer);
    CodeCombination fixCodeCombination = fixConflictExisted(coa, codeCombination, this.getCacheConflicts());
    return fixCodeCombination == null ? codeCombination : fixCodeCombination;
  }

  public CodeCombination caculateCcidWithElementInfo(FCoaDTO coa, Object elementsContainer) {
    CodeCombination codeCombination = CcidUtil.caculateCcidWithElementInfo(coa, elementsContainer);
    CodeCombination fixCodeCombination = fixConflictExisted(coa, codeCombination, this.getCacheConflicts());
    return fixCodeCombination == null ? codeCombination : fixCodeCombination;
  }

  /**
   * 取得冲突列表，支持多年度同一服务部署方式
   * 
   * @return 当前年度冲突列表
   */
  private List getCacheConflicts() {
    CommonKey key = new CommonKey(CommonUtil.getSetYear(), CommonUtil.getSetYear(), CommonUtil.getRgCode());
    List fixedConflicts = (List) conflicts.get(key);
    if (fixedConflicts == null) {
      fixedConflicts = dao.findFixedConflict();
      conflicts.put(key, fixedConflicts);
    }

    return fixedConflicts;
  }

  /**
   * 修正已知冲突，传入冲突列表
   * 
   * @param targetCoa
   * @param codeCmbKey
   * @return 没有修正返回空值
   */
  public CodeCombination fixConflictExisted(FCoaDTO targetCoa, CodeCombination codeCmbKey, List fixedConflicts) {

    Iterator it = fixedConflicts.iterator();
    while (it.hasNext()) {
      final FixedConflicts fixedConflict = (FixedConflicts) it.next();
      if (codeCmbKey.getCcid() == fixedConflict.getConflictCcid()
        && StringUtil.equals(codeCmbKey.getMd5(), fixedConflict.getConflictMd5())) {
        CodeCombination codeCmbCloned = (CodeCombination) codeCmbKey.clone();
        if (fixedConflict.getConflictType() == FixedConflicts.CONFLICT_TYPE_CCID)
          codeCmbCloned.setCcid(NumberUtil.toLong(fixedConflict.getFixedValue()));
        else if (fixedConflict.getConflictType() == FixedConflicts.CONFLICT_TYPE_MD5)
          codeCmbCloned.setMd5(fixedConflict.getFixedValue());
        // 返回复制出来的内容
        return codeCmbCloned;
      }
    }
    // 否则返回原来的
    return null;
  }

  /**
   * 解决冲突并保存冲突结果
   * 
   * @param coa
   *            指定COA
   * @param conflictCodeCmb
   *            冲突的CCID
   */
  public CodeCombination fixCodeCombinationConflict(FCoaDTO coa, CodeCombination conflict) {

    List conflicts = getCacheConflicts();

    CodeCombination existsConflictFixing = fixConflictExisted(coa, conflict, conflicts);
    if (existsConflictFixing != null)
      return existsConflictFixing;

    CodeCombination cloneCodeCombination = (CodeCombination) conflict.clone();
    int conflictType = -1;

    for (Map ccidExists = dao.findCcidFuzzy(coa, conflict); (ccidExists != null && !(StringUtil.equals(
      cloneCodeCombination.getMd5(), (String) ccidExists.get(GlConstant.MD5_KEY)) && cloneCodeCombination.getCcid() == NumberUtil
      .toLong((String) ccidExists.get(GlConstant.CCID_KEY))))
      || (ccidExists == null && cloneCodeCombination.getStatus() > 1); ccidExists = dao.findCcidFuzzy(coa,
      cloneCodeCombination)) {// conflict

      /*
       * Status 2 标志 gl_ccids_cache 表中出现的MD5 冲突 这里将其中一个处理
       */
      if (cloneCodeCombination.getStatus() == 2) {
        do {
          cloneCodeCombination.setMd5(StringUtil.genMD5(cloneCodeCombination.getMd5()));
        } while (isFixedValueExists(cloneCodeCombination, conflicts, 2));
        conflictType = 0;
        cloneCodeCombination.setStatus(0);
      }
      /*
       * Status 3 标志 gl_ccids_cache 表中出现的CCID 冲突 这里将其中一个处理
       */
      else if (cloneCodeCombination.getStatus() == 3) {
        do {
          cloneCodeCombination.setCcid(cloneCodeCombination.getCcid() + 1);
        } while (isFixedValueExists(cloneCodeCombination, conflicts, 3));
        conflictType = 1;
        cloneCodeCombination.setStatus(1);
      }

      /*
       * 而如果碰到MD5冲突，则不停使用MD5算法去取MD5值，直到不重复为止.
       */
      else if (StringUtil.equals(cloneCodeCombination.getMd5(), (String) ccidExists.get(GlConstant.MD5_KEY))) {
        do {
          cloneCodeCombination.setMd5(StringUtil.genMD5(cloneCodeCombination.getMd5()));
        } while (isFixedValueExists(cloneCodeCombination, conflicts, 0));
        if (conflictType == -1)
          conflictType = 0;
      }

      /*
       * 碰到冲突则在原HASH值基础上加1,直到找不到记录为止,因为这样的冲突 记录已加载到冲突缓存中,早已读取出.
       */
      else if (cloneCodeCombination.getCcid() == NumberUtil.toLong((String) ccidExists.get(GlConstant.CCID_KEY))) {
        do {
          cloneCodeCombination.setCcid(cloneCodeCombination.getCcid() + 1);
        } while (isFixedValueExists(cloneCodeCombination, conflicts, 1));
        if (conflictType == -1)
          conflictType = 1;
      }

    }
    // 解决冲突后将本次冲突信息记录下来,同时缓存到内存中.
    if (conflictType != -1) // 发生冲突，但处理后的结果已在库中存在，不需要在 mark
      markConflict(conflict, cloneCodeCombination, conflicts, conflictType);
    return cloneCodeCombination;
  }

  /**
   * 记录冲突,同时回写到传入的列表中.
   * 
   * @param conflictCcid
   * @param currectCodeCmb
   * @param fixedConflicts
   * @param type
   *            1 CCID冲突;0 MD5冲突
   */
  protected void markConflict(CodeCombination conflictCodeCmb, CodeCombination currectCodeCmb, List fixedConflicts,
    int type) {

    FixedConflicts fixedConflict = new FixedConflicts();
    String fixedValue = (type == 1 ? StringUtil.toStr(currectCodeCmb.getCcid()) : currectCodeCmb.getMd5());
    fixedConflict.setConflictCcid(conflictCodeCmb.getCcid());
    fixedConflict.setConflictMd5(conflictCodeCmb.getMd5());
    fixedConflict.setConflictType(type);
    fixedConflict.setFixedValue(fixedValue);

    if (!fixedConflicts.contains(fixedConflict))
      fixedConflicts.add(fixedConflict);

    dao.saveConflict(fixedConflict);
  }

  /**
   * 判断定值后的CCID 或MD5 是否已经在 冲突列表fixedConflicts中存在
   */
  public boolean isFixedValueExists(CodeCombination cloneCodeCombination, List fixedConflicts, int conflictType) {
    boolean isExists = false;
    for (int i = 0; i < fixedConflicts.size(); i++) {
      final FixedConflicts fixedConflict = (FixedConflicts) fixedConflicts.get(i);
      if ((conflictType == 0 || conflictType == 2) && fixedConflict.getConflictType() == 0) {
        isExists = cloneCodeCombination.getMd5().equals(((FixedConflicts) fixedConflicts.get(i)).getFixedValue());
      } else if ((conflictType == 1 || conflictType == 3) && fixedConflict.getConflictType() == 1) {
        isExists = StringUtil.toStr(cloneCodeCombination.getCcid()).equals(
          (((FixedConflicts) fixedConflicts.get(i)).getFixedValue()));
      }
      if (isExists == true)
        break;
    }
    return isExists;
  }

  public Map getCcidInfoMap(FCoaDTO coa, long ccid) {
    return dao.findCcid(coa, ccid);
  }

  public boolean isExistCcidsTable(String ccids_table) {
    return dao.isExistCcidsTable(ccids_table);
  }

  protected void fireCoaListener() {
    Iterator it = coaListener.iterator();
    while (it.hasNext()) {
      final CoaListener coaListener = (CoaListener) it.next();
      coaListener.coaUpdate();
    }
  }

  public void addCoaListener(CoaListener c) {
    if (c == null || coaListener.contains(c))
      return;

    this.coaListener.add(c);
  }

  /**
   * 
   * @autho
   * 
   */
  public static class RuleMatchKey {
    String ruleId = null;

    long ccid = 0;

    public RuleMatchKey(String ruleId, long ccid) {
      this.ruleId = ruleId;
      this.ccid = ccid;
    }

    public int hashCode() {
      return ruleId.hashCode() ^ new Long(ccid).hashCode();
    }

    public boolean equals(Object o) {
      if (o == null)
        return false;
      if (o instanceof RuleMatchKey) {
        RuleMatchKey key = (RuleMatchKey) o;
        return key.ccid == ccid && key.ruleId.equals(ruleId);
      }
      return false;
    }
  }

  /**
   * 需要特殊处理的要素 add by dyz
   */
  private Set getSpeclialElements() {
    String elementString = (String) SessionUtil.getParaMap().get(CCID_NOT_QUERY_ELEMENTS);
    Set specialElementSet = new HashSet();
    if (!StringUtils.isEmpty(elementString)) {
      if (logger.isDebugEnabled())
        logger.debug("特殊处理要素 " + elementString);
      String[] specialElements = StringUtils.split(elementString, "|");
      for (int i = 0; i < specialElements.length; i++) {
        specialElementSet.add(specialElements[i].toUpperCase());
      }
    }
    return specialElementSet;
  }

  /**
   * for clear the cache assosiate with coa.
   * 
   * @author
   * 
   */
  class CoaCacheListener implements CoaListener {

    /**
     * clear account/bvType/coa cache.
     */
    public void coaUpdate() {
      coaCache.clear();
      EngineConfiguration config = EngineConfiguration.getConfig();
      config.clearAccountCache();
    }

  }

  /**
   * 获取级联信息
   * 
   * @return
   */
  public Map getCoaCascade() {
    return coaCascadeMap.getCoaCascadeMap();
  }

  /**
   * 判断传入coa是否生成了ccid
   * 
   * @param coaDto
   * @return
   */
  public boolean isGenCcid(FCoaDTO coaDto) {
    return dao.isGenCcid(coaDto);
  }

  /**
   * 查询coa的级联设置
   */
  public List loadCoaCascade() {
    return dao.loadCoaCascade();
  }

  /**
   * 移除账务的COA
   */
  public List removeGlCoa(List coaList) {
    if (coaList == null || coaList.isEmpty())
      return coaList;

    List retList = new ArrayList();
    for (int i = 0, len = coaList.size(); i < len; i++) {
      final FCoaDTO coa = (FCoaDTO) coaList.get(i);
      if (!coa.getCoaCode().startsWith("900"))
        retList.add(coa);
    }
    return retList;
  }

  /**
   * 根据要素编码查询coa中设置
   * 
   * @param coaDto
   * @param eleCode
   * @return
   */
  public FCoaDetail getCoaDetailByEleCode(FCoaDTO coaDto, String eleCode) {
    Iterator iterator = coaDto.getCoaDetail().iterator();
    FCoaDetail coaDetail = null;
    FCoaDetail findCoaDetail = null;
    while (iterator.hasNext()) {
      coaDetail = (FCoaDetail) iterator.next();
      if (coaDetail.getEleCode().equals(eleCode)) {
        findCoaDetail = coaDetail;
        break;
      }
    }
    return findCoaDetail;
  }

  /**
   * 获取当前所有启用要素信息
   * 
   * @return 要素信息对象
   */
  @Override
  public List getAllElement() {
    CDD_Element cDD = new CDD_Element();
    List data = cDD.getAllElements();
    return data == null ? new ArrayList() : data;
  }

  /**
   * 得到自定义级次的数据
   * 
   * @param coa_id
   * @param element
   * @return List
   */
  @Override
  public List getCOADetailCode(String coa_id, String element) {
    final Iterator iterator = new CDD_Element().getCOADetailCode(coa_id, element).iterator();
    final List coaCodeList = new ArrayList();
    while (iterator.hasNext()) {
      coaCodeList.add(((XMLData) iterator.next()).get("level_code"));
    }
    return coaCodeList;
  }

  /**
   * 根据要素简称获取要素列表
   * 
   * @param element
   *            要素简称
   * @return 要素信息对象
   */
  @Override
  public XMLData getEle(String element) {
    CDD_Element cDD = new CDD_Element();
    String[] displayField = new String[] { "chr_id", "chr_code", "chr_name", "level_num", "is_leaf", "parent_id" };
    XMLData data = cDD.getEle(element, 0, 0, displayField, true, null, null, "order by chr_code");
    return data == null ? new XMLData() : data;
  }
}
