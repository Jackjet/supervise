package gov.df.fap.service.bis;

import gov.df.fap.api.dictionary.bis.IBISInterface;
import gov.df.fap.api.dictionary.interfaces.IDictionary;
import gov.df.fap.bean.dictionary.dto.BISDTO;
import gov.df.fap.bean.dictionary.dto.FElementDTO;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.xml.XMLData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BISInterfaceBO implements IBISInterface {
  @Autowired
  private IDictionary dictionary = null;

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao;

  private String input_type = "0";//保存调用类型0为通过 接口批量保存1为录入界面增加保存

  /**
   * 录入界面新增项目
   * @param dataList 项目信息列表
   * @return
   * @throws Exception
   */
  public Map saveBisDataForInput(List dataList) throws Exception {
    input_type = "1";
    return saveBisData(dataList);
  }

  /**
   * 新增项目
   * @param dataList 项目信息列表
   * @return
   * @throws Exception
   */
  public Map saveBisData(List dataList) throws Exception {
    //返回结果
    Map result = new HashMap();
    List idList = new ArrayList();
    //项目所属信息
    List belongList = new ArrayList();
    //资金安排情况信息
    List moneyList = new ArrayList();
    List insertList = new ArrayList();
    BisRuleCode brc = new BisRuleCode(this);
    XMLData data = null;
    XMLData codeData = null;
    try {
      String bis_codemode = getBisConfigPara("bis_codemode");//0#使用原系统编码+1#自动编号
      String bis_autoCode = getBisConfigPara("bis_autoCode");//0#不启用+1#启用
      String bis_supcode = getBisConfigPara("bis_supcode");//0#不启用+1#启用
      for (int i = 0, j = dataList.size(); i < j; i++) {
        BISDTO bis = (BISDTO) dataList.get(i);
        List bislist = getBisData(bis);
        String id = bis.getCHR_ID();
        if (id == null || "".equals(id)) {
          id = getBISSEQ();
        } else {
          if (bislist == null || bislist.isEmpty()) {
            //当传入带id数据在预算项目 表中查不到 时给出错误提示
            throw new Exception("第" + (i + 1) + "行记录的预算项目按项目ID和项目名称未查找到相关信息，项目id为：" + id + ",项目名称为："
              + bis.getCHR_NAME() + ",请检查数据是否正确！");
          } else {
            //当传入带id数据在预算项目 表中能查到 时直接返回
            result.put(bis.getUPPER_ID(), id);
            continue;
          }
        }
        //当传入不带id数据在预算项目 表中能查到 时直接返回
        if (bislist != null && !bislist.isEmpty()) {
          result.put(bis.getUPPER_ID(), ((XMLData) bislist.get(0)).get("chr_id"));
          continue;
        }

        //自动编码
        else if ((input_type.equals("0") && bis_codemode.equals("1"))
          || (input_type.equals("1") && bis_autoCode.equals("1"))) {
          input_type = "0";
          data = new XMLData();
          data.put("chr_name", bis.getCHR_NAME());
          data.put("is_public", bis.getPUBLIC_SIGN());
          data.put("parent_id", bis.getPARENT_ID());
          data.put("num", i + "");
          codeData = brc.getBisChrCode(data, dictionary);
          bis.setCHR_CODE(codeData.get("chr_code") != null ? codeData.get("chr_code").toString() : bis.getCHR_CODE());
        }
        if (input_type.equals("0") && bis_codemode.equals("0")) {
          //					System.out.println("upperid:"+bis.getUPPER_ID());
          if (bis.getUPPER_ID() == null || "".equals(bis.getUPPER_ID())) {
            data = new XMLData();
            data.put("chr_name", bis.getCHR_NAME());
            data.put("is_public", bis.getPUBLIC_SIGN());
            data.put("parent_id", bis.getPARENT_ID());
            data.put("num", i + "");
            codeData = brc.getBisChrCode(data, dictionary);
            bis.setCHR_CODE(codeData.get("chr_code") != null ? codeData.get("chr_code").toString() : bis.getCHR_CODE());
          } else {
            bis.setCHR_CODE(bis.getUPPER_ID());//调用时将BIS_CODE转换为预算的PRJ_CODE
          }
        }

        //检验数据正确性
        checkBisData(bis, i);
        //设置相关默认值
        bis.setCHR_ID(id);
        bis.setSET_YEAR(SessionUtil.getLoginYear());
        bis.setRG_CODE(SessionUtil.getRgCode());
        String operationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        bis.setLATEST_OP_DATE(operationDate);
        bis.setLATEST_OP_USER(SessionUtil.getUserInfoContext().getUserID());
        bis.setCREATE_DATE(operationDate);
        bis.setCREATE_USER(SessionUtil.getUserInfoContext().getUserID());
        bis.setIS_DELETED("0");
        bis.setLEVEL_NUM("1");
        bis.setIS_LEAF("1");
        bis.setCHR_CODE1(bis.getCHR_CODE());
        bis.setCHR_ID1(id);
        bis.setCHR_CODE2(bis.getCHR_CODE());
        bis.setCHR_ID2(id);
        bis.setCHR_CODE3(bis.getCHR_CODE());
        bis.setCHR_ID3(id);
        bis.setCHR_CODE4(bis.getCHR_CODE());
        bis.setCHR_ID4(id);
        bis.setCHR_CODE5(bis.getCHR_CODE());
        bis.setCHR_ID5(id);
        if (bis.getDISP_CODE() == null || bis.getDISP_CODE().equals("")) {
          bis.setDISP_CODE(bis.getCHR_CODE());
        }
        //取得项目所属信息
        List belongs = bis.getBELONG_LIST();
        if (belongs != null) {
          for (int n = 0, m = belongs.size(); n < m; n++) {
            Map oneBelong = (Map) belongs.get(n);
            oneBelong.put("bis_id", id);
            oneBelong.put("chr_code", bis.getCHR_CODE());
          }
        }

        //自动进行辅助编码
        if (bis_supcode.equals("1")) {
          codeData = getBisChrCodeSup(belongs, belongList.size());
          belongs = codeData.get("belongList") != null ? (ArrayList) codeData.get("belongList") : belongs;
        }
        //取得资金安排情况信息
        List moneys = bis.getMONEY_LIST();
        if (moneys != null) {
          for (int n = 0, m = moneys.size(); n < m; n++) {
            Map oneMoney = (Map) moneys.get(n);
            oneMoney.put("bis_id", id);
          }
        }
        if (belongs != null && !belongs.isEmpty() && bis.getPUBLIC_SIGN() != null && bis.getPUBLIC_SIGN().equals("0"))
          belongList.addAll(belongs);
        if (moneys != null && !moneys.isEmpty())
          moneyList.addAll(moneys);
        if (bis.getUPPER_ID() != null && !"".equals(bis.getUPPER_ID())) {
          result.put(bis.getUPPER_ID(), id);
          result.put(bis.getUPPER_ID() + "_id", id);
          result.put(bis.getUPPER_ID() + "_name", id);
          result.put(bis.getUPPER_ID() + "_code", id);
        } else {
          result.put(bis.getCHR_NAME() + "_id", id);
          result.put(bis.getCHR_NAME() + "_name", bis.getCHR_NAME());
          result.put(bis.getCHR_NAME() + "_code", bis.getCHR_CODE());
        }
        idList.add(id);
        insertList.add(bis);
      }
      dao.batchSaveDataBySql("ELE_BUDGET_ITEM_SUMMARY", insertList);
      updateParentIdCode("ELE_BUDGET_ITEM_SUMMARY", idList);
      if (belongList != null && !belongList.isEmpty()) {
        insertBisBelong(belongList, idList);
      }
      if (moneyList != null && !moneyList.isEmpty()) {
        insertBisMoney(moneyList, idList);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e.getMessage());
    }
    return result;
  }

  private void updateParentIdCode(String tableName, List dataList) throws Exception {
    String condition = getInCondition("bis.chr_id", dataList);
    StringBuffer sql = new StringBuffer();
    sql.append("select bis.chr_id,bis.chr_code,bis.parent_id,bis.sys_id,bis.public_sign,bis.set_year,");
    sql.append("bis.disp_code,bis.create_date,bis.create_user,bis.latest_op_date,bis.latest_op_user,");
    sql.append("bis.chr_name,bis.is_leaf,bis.enabled,bis.level_num,bis.latest_op_date,bis.is_deleted,");
    sql.append("bis1.chr_code1,bis1.chr_id1,bis1.chr_code2,bis1.chr_id2,bis1.chr_code3,bis1.chr_id3,");
    sql.append("bis1.chr_code4,bis1.chr_id4,bis1.chr_code5,bis1.chr_id5,bis.rg_code ");
    sql.append(" from ele_budget_item_summary bis,ele_budget_item_summary bis1 where ");
    sql.append(" bis1.chr_id= bis.parent_id and bis1.set_year=bis.set_year and ");
    sql.append(" bis1.rg_code=bis.rg_code and bis.parent_id is not null ").append(condition);
    List updateList = dao.findBySql(sql.toString());
    for (int i = 0, j = updateList.size(); i < j; i++) {
      XMLData data = (XMLData) updateList.get(i);
      int level_num = Integer.parseInt(data.get("level_num").toString()) + 1;
      data.put("level_num", String.valueOf(level_num));
      data.put("chr_id" + level_num, data.get("chr_id").toString());
      data.put("chr_code" + level_num, data.get("chr_code").toString());
      for (int k = level_num + 1; k <= 5; k++) {
        data.put("chr_id" + k, "");
        data.put("chr_code" + k, "");
      }
    }
    dao.batchUpdateDataBySql(tableName, updateList, new String[] { "chr_id" });

    sql.setLength(0);
    //更新父级不是叶子节点
    sql.append("update ele_budget_item_summary bis1 set is_leaf=0 where exists (");
    sql
      .append("select 1 from ele_budget_item_summary bis where bis1.chr_id= bis.parent_id and bis1.set_year=bis.set_year and bis1.rg_code=bis.rg_code and ");
    sql.append("bis.parent_id is not null ").append(condition.length() > 0 ? condition + ")" : ")");
    dao.executeBySql(sql.toString());
  }

  /**
   * <br>Description:根据传入要素获取预算项目查询条件
   * <br>Author:王鑫磊(wangxinlei@ufida.com.cn)
   * <br>Date:May 13, 2015
   * @param conditions
   * @return
   */
  public String getBisSqlCondition(BISDTO conditions) throws Exception {
    String condition = "";
    String Sql = " and (public_sign=1";
    String bis_name = conditions.getCHR_NAME();
    String bis_name_sql = "";
    if (bis_name != null && !"".equals(bis_name)) {
      bis_name_sql = " and chr_name ='" + bis_name + "' ";
    }
    String mbSql = "";
    String enSql = "";
    String biSql = "";
    List belongList = conditions.getBELONG_LIST();
    if (conditions.getCHR_ID() != null && !conditions.getCHR_ID().equals("")) {
      condition = " and chr_id='" + conditions.getCHR_ID() + "' ";
    }
    if (belongList == null || belongList.isEmpty()) {
      return " and is_deleted=0 " + bis_name_sql + condition;
    }
    Map belong = (HashMap) belongList.get(0);
    String mb_id = belong.get("mb_id") != null ? belong.get("mb_id").toString() : "";
    String en_id = belong.get("en_id") != null ? belong.get("en_id").toString() : "";
    String bi_id = belong.get("bi_id") != null ? belong.get("bi_id").toString() : "";
    if ("1".equalsIgnoreCase(getBisConfigPara("bis_mb"))) {
      if (mb_id != null && !mb_id.equalsIgnoreCase("")) {
        mbSql = " mb_id='" + mb_id + "'";
      }
    }

    if ("1".equalsIgnoreCase(getBisConfigPara("bis_en"))) {
      if (en_id != null && !en_id.equalsIgnoreCase("")) {
        enSql = " (en_id='"
          + en_id
          + "' or"
          + //允许看其对应的部门的项目
          " en_id= (select parent_id From ele_enterprise where chr_id='"
          + en_id
          + "')"
          + " or en_id= (select parent_id From ele_enterprise where chr_id=(select parent_id From ele_enterprise where chr_id='"
          + en_id
          + "')) "
          + " or en_id= (select chr_id From ele_enterprise where chr_id=(select parent_id From ele_enterprise where chr_id=(select parent_id From ele_enterprise where chr_id=(select parent_id From ele_enterprise where chr_id='"
          + en_id + "')))) )";
      }
    }

    if ("1".equalsIgnoreCase(getBisConfigPara("bis_bi"))) {
      if (bi_id != null && !bi_id.equalsIgnoreCase("")) {
        biSql = " AGENCYEXP_ID='" + bi_id + "'";
      }
    }

    if (!mbSql.equalsIgnoreCase("") || !enSql.equalsIgnoreCase("") || !biSql.equalsIgnoreCase("")) {
      Sql = Sql
        + " or (public_sign=0 and exists(select 1 from ele_budget_item_summary_belong b where chr_id=b.bis_id and(";
      if (!mbSql.equalsIgnoreCase(""))
        Sql = Sql + mbSql + " and";
      if (!enSql.equalsIgnoreCase(""))
        Sql = Sql + enSql + " and";
      if (!biSql.equalsIgnoreCase(""))
        Sql = Sql + biSql + " and";
      Sql = Sql.substring(0, Sql.length() - 3) + ")))";
    } else {
      Sql = Sql
        + " or (public_sign=0 and exists(select 1 from ele_budget_item_summary_belong b where chr_id=b.bis_id))";
    }
    Sql += ") ";
    return Sql + " and is_deleted=0" + bis_name_sql + condition;
  }

  /**
   * <br>Description:根据传入要素获取预算项目
   * <br>Author:王鑫磊(wangxinlei@ufida.com.cn)
   * <br>Date:May 13, 2015
   * @param conditions
   * @return
   */
  public List getBisData(BISDTO conditions) throws Exception {
    return dictionary.findEleValues("BIS", null, null, false, null, null, getBisSqlCondition(conditions));
  }

  /**
   * <br>Description:判断传入预算项目信息是否符合项目所属
   * <br>Author:王鑫磊(wangxinlei@ufida.com.cn)
   * <br>Date:May 12, 2015
   * @param bis_id
   * @param mb_id
   * @param en_id
   * @param bi_id
   * @return
   * @throws Exception
   */
  public boolean checkBisBelong(String bis_id, String mb_id, String en_id, String bi_id) throws Exception {
    if (bis_id != null && !"".equals(bis_id)) {
      BISDTO bisData = new BISDTO();
      bisData.setCHR_ID(bis_id);

      XMLData BelongData = new XMLData();
      BelongData.put("bis_id", bis_id);
      BelongData.put("mb_id", mb_id);
      BelongData.put("en_id", en_id);
      BelongData.put("bi_id", bi_id);

      List BelongList = new ArrayList();
      BelongList.add(BelongData);

      bisData.setBELONG_LIST(BelongList);

      return checkBisBelong(bisData);
    } else {
      throw new Exception("请输入预算项目ID！");
    }

  }

  /**
   * <br>Description:判断传入预算项目信息是否符合项目所属
   * <br>Author:王鑫磊(wangxinlei@ufida.com.cn)
   * <br>Date:May 12, 2015
   * @param BISDTO
   * @return
   * @throws Exception
   */
  public boolean checkBisBelong(BISDTO BISDTO) throws Exception {
    List reList = getBisData(BISDTO);
    if (reList.size() > 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 检查数据合法性
   * @param bis 要检查的数据
   * @param i 数据所在行数
   * @throws Exception
   */
  private void checkBisData(BISDTO bis, int i) throws Exception {
    String chr_code = bis.getCHR_CODE();
    if (chr_code == null || "".equals(chr_code)) {
      throw new Exception("请输入第" + (i + 1) + "行记录的项目编码！");
    }
    if (!chr_code.matches("[\\w-_]+")) {
      throw new Exception("第" + (i + 1) + "行记录的预算项目编码错误，项目编码只允许字母、数字、中横杠-以及下横杠_！请重新录入！");
    }
    // 验证编码规则
    //		String code_rule = eleSet == null ? "" : (String) eleSet.get("code_rule");
    //	      if (code_rule != null && !"".equals(code_rule)) {
    //	        int totallevel = 0;
    //	        boolean isReach = false;
    //	        try {
    //	          StringTokenizer st = new StringTokenizer(code_rule, "-");
    //	          while (st.hasMoreTokens()) {
    //	            totallevel += Integer.parseInt(st.nextToken());
    //	            if (chr_code.length() == totallevel) {
    //	              isReach = true;
    //	            }
    //	          }
    //	        } catch (Exception e) {
    //	        	throw new Exception("第"+(i+1)+"行记录项目编码规则设置有误,请检查配置！");
    //	        }
    //	        if (!isReach) {
    //	        	throw new Exception("第"+(i+1)+"行记录项目编码不符合编码规则" + code_rule + "！");
    //	        }
    //	      }
    String chr_name = bis.getCHR_NAME();
    if (chr_name == null || "".equals(chr_name)) {
      throw new Exception("请输入第" + (i + 1) + "行记录的项目名称！");
    }

    if (chr_name.matches("[']") || chr_name.matches("[']")) {
      throw new Exception("第" + (i + 1) + "行记录的预算项目名称错误，项目名称中不能包含单引号！请重新录入！");
    }
    String parent_id = bis.getPARENT_ID();
    if (parent_id != null && !parent_id.equalsIgnoreCase("") && parent_id.equalsIgnoreCase(bis.getCHR_ID())) {
      throw new Exception("第" + (i + 1) + "行记录的预算项目父级项目错误，不允许将要素的父级指向自身！");
    }
    //		XMLData data = queryBisIsExistsByName(chr_name);
    //		if(data!=null&&!data.isEmpty()){
    //			throw new Exception("第"+(i+1)+"行记录的预算项目已经存在，请重新录入！");
    //		}
    if (bis.getPUBLIC_SIGN() != null && bis.getPUBLIC_SIGN().equals("0")) {
      List belongList = bis.getBELONG_LIST();
      if (belongList == null || belongList.isEmpty()) {
        throw new Exception("请输入第" + (i + 1) + "行记录的项目所属信息！");
      } else {
        int size = belongList.size();
        for (int k = 0; k < size; k++) {
          Map belongData = (HashMap) belongList.get(k);
          String MB_ID = (String) belongData.get("MB_ID");
          if (MB_ID == null || "".equals(MB_ID)) {
            MB_ID = (String) belongData.get("mb_id");
          }
          String EN_ID = (String) belongData.get("EN_ID");
          if (EN_ID == null || "".equals(EN_ID)) {
            EN_ID = (String) belongData.get("en_id");
          }
          String BI_ID = (String) belongData.get("BI_ID");
          if (BI_ID == null || "".equals(BI_ID)) {
            BI_ID = (String) belongData.get("bi_id");
          }
          if (MB_ID != null && !"".equals(MB_ID)) {
            FElementDTO res = dictionary.findEleValueById("MB", MB_ID);
            if (res == null) {
              throw new Exception("第" + (i + 1) + "行记录的项目所属信息MB，在基础要素中不存在！");
            }
          }
          if (EN_ID != null && !"".equals(EN_ID)) {
            FElementDTO res = dictionary.findEleValueById("EN", EN_ID);
            if (res == null) {
              throw new Exception("第" + (i + 1) + "行记录的项目所属信息EN，在基础要素中不存在！");
            }
          }
          if (BI_ID != null && !"".equals(BI_ID)) {
            FElementDTO res = dictionary.findEleValueById("AGENCYEXP", BI_ID);
            if (res == null) {
              throw new Exception("第" + (i + 1) + "行记录的项目所属信息BI，在基础要素中不存在！");
            }
          }
        }
      }
    }

    String sys_id = bis.getSYS_ID();
    if (sys_id == null || "".equals(sys_id)) {
      throw new Exception("请输入第" + (i + 1) + "行记录的项目所属系统！");
    }
  }

  /**
   * 修改项目
   * @param bisdto 项目DTO
   * @return
   * @throws Exception
   */
  public boolean updateBisData(BISDTO bisdto) throws Exception {
    BisRuleCode brc = new BisRuleCode(this);
    XMLData codeData = null;
    String bis_supcode = getBisConfigPara("bis_supcode");//0#不启用+1#启用
    String operationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    bisdto.setLATEST_OP_DATE(operationDate);
    bisdto.setLATEST_OP_USER(SessionUtil.getUserInfoContext().getUserID());
    //校验项目 是否在存在
    XMLData data = dictionary.findEleValueById("BIS", bisdto.getCHR_ID());
    if (data == null || data.isEmpty()) {
      throw new Exception("ID为:" + bisdto.getCHR_ID() + "的预算项目不存在，不能修改！");
    }
    //校验项目 是否在使用
    if (queryBisIsUsedByID(bisdto.getCHR_ID())) {
      throw new Exception("ID为:" + bisdto.getCHR_ID() + "的预算项目已使用并且存在实际额度,不能修改！");
    }
    //取得项目所属信息
    List belongs = bisdto.getBELONG_LIST();
    if (belongs != null) {
      for (int n = 0, m = belongs.size(); n < m; n++) {
        Map oneBelong = (Map) belongs.get(n);
        oneBelong.put("bis_id", bisdto.getCHR_ID());
        oneBelong.put("chr_code", bisdto.getCHR_CODE());
      }
    }
    //取得资金安排情况信息
    List moneys = bisdto.getMONEY_LIST();
    if (moneys != null) {
      for (int n = 0, m = moneys.size(); n < m; n++) {
        Map oneMoney = (Map) moneys.get(n);
        oneMoney.put("bis_id", bisdto.getCHR_ID());
      }
    }
    //		dao.updateDataBySql("ELE_BUDGET_ITEM_SUMMARY", bisdto, new String[] { "chr_id" });
    Map value = (Map) bisdto.toXMLData();
    value.remove("belongList");
    value.remove("moneyList");
    value.remove("bis_id");
    dictionary.updateEleValue("BIS", bisdto.getCHR_ID(), value);
    List idList = new ArrayList();
    idList.add(bisdto.getCHR_ID());
    //自动进行辅助编码
    if (bis_supcode.equals("1")) {
      codeData = getBisChrCodeSup(belongs, 0);
      belongs = codeData.get("belongList") != null ? (ArrayList) codeData.get("belongList") : belongs;
    }
    if (belongs != null) {
      if (bisdto.getPUBLIC_SIGN() != null && bisdto.getPUBLIC_SIGN().equals("1")) {//公有项目不添加项目所属信息
        belongs = null;
      }
      insertBisBelong(belongs, idList);
    }
    if (moneys != null) {
      insertBisMoney(moneys, idList);
    }
    return true;
  }

  /**
   * 插入项目所属信息
   * @param dataList 要插入的项目 所属信息
   * @param idList 项目ID列表 
   * @throws Exception
   */
  public void insertBisBelong(List dataList, List idList) throws Exception {
    String condition = getInCondition("bis_id", idList);
    String sql = "delete from ELE_BUDGET_ITEM_SUMMARY_BELONG where 1=1 " + condition;
    dao.executeBySql(sql);
    if (dataList != null && !dataList.isEmpty())
      dao.batchSaveDataBySql("ELE_BUDGET_ITEM_SUMMARY_BELONG", dataList);
  }

  /**
   * 插入项目资金安排情况信息
   * @param dataList 要插入的项目 资金安排情况信息
   * @param idList 项目ID列表 
   * @throws Exception
   */
  public void insertBisMoney(List dataList, List idList) throws Exception {
    String condition = getInCondition("bis_id", idList);
    String sql = "delete from ELE_BUDGET_ITEM_SUMMARY_MONEY where 1=1 " + condition;
    dao.executeBySql(sql);
    if (dataList != null && !dataList.isEmpty())
      dao.batchSaveDataBySql("ELE_BUDGET_ITEM_SUMMARY_MONEY", dataList);
  }

  /**
   * 删除项目
   * @param bis_id项目id
   * @return
   * @throws Exception
   */
  public Map deleteBisData(String bis_id, String sys_id) throws Exception {
    Map map = new HashMap();
    //    boolean flag = true;
    String deleteBelognSql = "delete from ele_budget_item_summary_belong where 1=1 and bis_id=? and year = ?";
    String deleteMoneySql = "delete from ele_budget_item_summary_money where 1=1 and bis_id=? and year = ?";
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();

    //校验项目 是否在存在
    //		XMLData data=dictionary.findEleValueById("BIS", bis_id);//平台方法中没有sys_id改为自己查询
    List dataList = dao.findBySql(
      "select * from ele_budget_item_summary where chr_id = ? and set_year = ? and rg_code = ?", new Object[] { bis_id,
        set_year, rg_code });
    XMLData data = new XMLData();
    if (dataList != null && dataList.size() > 0) {
      data = (XMLData) dataList.get(0);
    }
    if (dataList == null || dataList.isEmpty()) {
      map.put("flag", 0);
      map.put("notice", "ID为:" + bis_id + "的预算项目不存在，不能删除！");

    }

    //校验是否上游系统生成  预算调用接口也走这里
    else if (!data.get("sys_id").equals(sys_id)) {
      map.put("flag", 0);
      map.put("notice", "ID为:" + bis_id + "的预算项目非当前子系统生成，不能删除！");

    }
    //校验项目 是否在使用
    else if (queryBisIsUsedByID(bis_id)) {
      map.put("flag", 0);
      map.put("notice", "ID为:" + bis_id + "的预算项目已使用并且存在实际额度,不能删除！");

    } else {
      //		String delbissql="update ELE_BUDGET_ITEM_SUMMARY set is_deleted=1 where chr_id=?  and ENABLED=1";
      //		dao.executeBySql(delbissql, new Object[]{bis_id});
      try {
        dictionary.deleteEleValue("BIS", bis_id);
        dao.executeBySql(deleteBelognSql, new Object[] { bis_id, set_year });
        dao.executeBySql(deleteMoneySql, new Object[] { bis_id, set_year });
        map.put("flag", 1);
        map.put("notice", "删除成功");
      } catch (Exception e) {
        map.put("flag", 0);
        map.put("notice", "删除失败");
        throw e;
      }
    }
    return map;
  }

  /**
   * 查询项目信息是已使用并且存在实际额度
   * @param bis_id项目ID
   * @return
   * @throws Exception
   */
  public boolean queryBisIsUsedByID(String bis_id) throws Exception {
    boolean result = false;
    String sql = "select 1 from gl_balance v where exists(select 1 from gl_ccids c where  c.bis_id=? and c.ccid=v.ccid) and (avi_money-use_money-minus_money)>0 and set_year=?";
    List bisList = dao.findBySql(sql, new Object[] { bis_id, SessionUtil.getLoginYear() });
    if (bisList != null && !bisList.isEmpty())
      result = true;
    return result;
  }

  /**
   * 项目信息查询
   * @param bis_id项目id
   * @return
   * @throws Exception
   */
  public BISDTO queryBisById(String bis_id) throws Exception {
    List ids = new ArrayList();
    ids.add(bis_id);
    BISDTO bis = new BISDTO();
    XMLData data = null;
    String sql = "select * from ELE_BUDGET_ITEM_SUMMARY bis where is_deleted=0 and ENABLED=1 and chr_id=?";
    List bisList = dao.findBySql(sql, new Object[] { bis_id });
    if (bisList != null && !bisList.isEmpty()) {
      data = (XMLData) bisList.get(0);
      bis.copy(data);
      List belongList = queryBisBelongByID(ids);
      List moneyList = queryBisMoneyByID(ids);
      bis.setBELONG_LIST(belongList);
      bis.setMONEY_LIST(moneyList);
    }
    return bis;
  }

  /**
   * 项目信息查询
   * @param bis_ids项目id
   * @return
   * @throws Exception
   */
  public List queryBisDataById(List bisidList) throws Exception {
    List result = new ArrayList();
    String condition = getInCondition("chr_id", bisidList);
    String sql = "select * from ELE_BUDGET_ITEM_SUMMARY bis where 1=1 and is_deleted=0 and ENABLED=1 " + condition;
    List bisList = dao.findBySql(sql);
    List belongList = queryBisBelongByID(bisidList);
    List moneyList = queryBisMoneyByID(bisidList);
    for (int i = 0, j = bisList.size(); i < j; i++) {
      BISDTO bis = new BISDTO();
      List belongLists = null;
      List moneyLists = null;
      XMLData data = (XMLData) bisList.get(i);
      String bisid = data.get("bis_id") != null ? data.get("bis_id").toString() : "";
      for (int n = 0, m = belongList.size(); n < m; n++) {
        belongLists = new ArrayList();
        XMLData belongdata = (XMLData) belongList.get(i);
        String belongbis_id = belongdata.get("bis_id") != null ? belongdata.get("bis_id").toString() : "";
        if (!bisid.equals("") && !belongbis_id.equals("") && bisid.equals(belongbis_id)) {
          belongLists.add(belongdata);
        }
      }
      data.put("belongList", belongLists);
      for (int n = 0, m = moneyList.size(); n < m; n++) {
        moneyLists = new ArrayList();
        XMLData moneydata = (XMLData) moneyList.get(i);
        String moneybis_id = moneydata.get("bis_id") != null ? moneydata.get("bis_id").toString() : "";
        if (!bisid.equals("") && !moneybis_id.equals("") && bisid.equals(moneybis_id)) {
          moneyLists.add(moneydata);
        }
      }
      data.put("moneyList", moneyLists);
      bis.copy(data);
      result.add(bis);
    }
    return result;
  }

  /**
   * 查询项目所属信息
   * @param bis_id 项目ID
   * @return
   * @throws Exception
   */
  public List queryBisBelongByID(List bisidList) throws Exception {
    String condition = "";
    if (bisidList != null && !bisidList.isEmpty())
      condition = getInCondition("bis_id", bisidList);
    String sql = "select * from ELE_BUDGET_ITEM_SUMMARY_BELONG bis where 1=1  and exists (select 1 from ELE_BUDGET_ITEM_SUMMARY bis where 1=1 and is_deleted=0 and ENABLED=1 and set_year="
      + SessionUtil.getLoginYear() + ")" + condition;
    return dao.findBySql(sql);
  }

  /**
   * 查询项目资金信息
   * @param bis_id 项目ID
   * @return
   * @throws Exception
   */
  public List queryBisMoneyByID(List bisidList) throws Exception {
    String condition = "";
    if (bisidList != null && !bisidList.isEmpty())
      condition = getInCondition("bis_id", bisidList);
    String sql = "select * from ELE_BUDGET_ITEM_SUMMARY_MONEY bis where 1=1 and exists (select 1 from ELE_BUDGET_ITEM_SUMMARY bis where 1=1 and is_deleted=0 and ENABLED=1 and set_year="
      + SessionUtil.getLoginYear() + ")" + condition;
    return dao.findBySql(sql);
  }

  /**
  * 项目信息查询视图
  * @return
  * @throws Exception
  */
  public List queryAllBisData(XMLData condition) throws Exception {
    return queryAllBisData(condition, true);
  }

  /**
   * 项目信息查询视图
   * @return
   * @throws Exception
   */
  public List queryAllBisData(XMLData condition, boolean withENABLED) throws Exception {
    List result = new ArrayList();
    //    int pageIndex = Integer.parseInt((String) condition.get("pageIndex"));
    //    int pageRows = Integer.parseInt((String) condition.get("pageRows"));
    String con = condition.get("condition").toString();
    String treeCon = condition.get("treeCondition").toString();
    String rg_code = SessionUtil.getRgCode();
    con += " and rg_code='" + rg_code + "' ";
    String ENABLED_STR = " and ENABLED=1 ";
    if (!withENABLED) {
      ENABLED_STR = "";
    }
    //增加parent_name
//    String treesql = "select s.chr_code||' '||s.chr_name codeName,s.* from ELE_BUDGET_ITEM_SUMMARY s left join ELE_BUDGET_ITEM_SUMMARY_BELONG b on 1=1 and s.chr_id=b.bis_id and s.is_deleted=0 "
//      + ENABLED_STR + treeCon + "  order by s.latest_op_date desc";
    
    String treesql = null;
    if(TypeOfDB.isOracle()) {
    	treesql="select * from (select e1.*,e1.chr_code||' '||e1.chr_name codeName,e2.chr_code||' '||e2.chr_name as parent_name from ele_budget_item_summary e1 left join ele_budget_item_summary e2 on e1.parent_id=e2.chr_id) s left join ELE_BUDGET_ITEM_SUMMARY_BELONG b on 1=1 and s.chr_id=b.bis_id and s.is_deleted=0"
    			+ ENABLED_STR + con + " order by chr_code";
    } else if(TypeOfDB.isMySQL()) {
    	treesql="select * from (select e1.*, concat(e1.chr_code, ' ', e1.chr_name) codeName, concat(e2.chr_code, ' ', e2.chr_name) as parent_name from ele_budget_item_summary e1 left join ele_budget_item_summary e2 on e1.parent_id=e2.chr_id) s left join ELE_BUDGET_ITEM_SUMMARY_BELONG b on 1=1 and s.chr_id=b.bis_id and s.is_deleted=0"
    			+ ENABLED_STR + con + " order by chr_code";
    }
    
    //    String subsql = "select bis.*,(select chr_name from ele_budget_item_summary t1 where bis.parent_id=t1.chr_id and bis.rg_code=t1.rg_code and bis.set_year=t1.set_year) parent_name from ELE_BUDGET_ITEM_SUMMARY bis where 1=1 and is_deleted=0 "
    //      + ENABLED_STR + con + " order by chr_code";
    List allbis = dao.findBySql(treesql);
    //    String sql = "select b.* from (select p.*,rownum as id from (" + subsql + ") p where rownum<=" + pageIndex
    //      * pageRows + ")" + " b where b.id>" + (pageIndex - 1) * pageRows;

    //    String countSql = "select count(1) from (" + subsql + ")";
    //
    //    List bisList = dao.findBySql(sql);

    //    List countList = dao.findBySql(countSql);

    //    String count = (String) ((XMLData) (countList.get(0))).get("count(1)");
    //    //取得所有项目 的项目所属及资金情况
    //    List belongList = queryBisBelongByID(null);
    //    List moneyList = queryBisMoneyByID(null);
    //    //将项目所属提取并组装到数据中
    //    for (int i = 0, j = bisList.size(); i < j; i++) {
    //      List belongLists = null;
    //      List moneyLists = null;
    //      XMLData data = (XMLData) bisList.get(i);
    //      String bisid = data.get("chr_id") != null ? data.get("chr_id").toString() : "";
    //      String en_ids = "";
    //      String mb_ids = "";
    //      String agencyexp_ids = "";
    //      for (int n = 0, m = belongList.size(); n < m; n++) {
    //        belongLists = new ArrayList();
    //        XMLData belongdata = (XMLData) belongList.get(n);
    //        String belongbis_id = belongdata.get("bis_id") != null ? belongdata.get("bis_id").toString() : "";
    //        if (!bisid.equals("") && !belongbis_id.equals("") && bisid.equals(belongbis_id)) {
    //          if (belongdata.get("agency_id") != null && !"".equals(belongdata.get("agency_id"))
    //            && en_ids.indexOf(belongdata.get("agency_id").toString()) < 0) {
    //            en_ids += "#" + belongdata.get("agency_id");
    //          }
    //          if (belongdata.get("mb_id") != null && !"".equals(belongdata.get("mb_id"))
    //            && mb_ids.indexOf(belongdata.get("mb_id").toString()) < 0) {
    //            mb_ids += "#" + belongdata.get("mb_id");
    //          }
    //          //add by gaoqb
    //          if (belongdata.get("") != null && !"".equals(belongdata.get("agencyexp_id"))
    //            && agencyexp_ids.indexOf(belongdata.get("agencyexp_id").toString()) < 0) {
    //            agencyexp_ids += "#" + belongdata.get("agencyexp_id");
    //          }
    //          data.putAll(belongdata);
    //          belongLists.add(belongdata);
    //        }
    //      }
    //      data.put("agency_id", en_ids.length() > 0 ? en_ids.substring(1) : "");
    //      data.put("mb_id", mb_ids.length() > 0 ? mb_ids.substring(1) : "");
    //      data.put("agencyexp_id", agencyexp_ids.length() > 0 ? agencyexp_ids.substring(1) : "");
    //      data.put("belongList", belongLists);
    //      //将项目资金情况提取并组装到数据中
    //      for (int n = 0, m = moneyList.size(); n < m; n++) {
    //        moneyLists = new ArrayList();
    //        XMLData moneydata = (XMLData) moneyList.get(n);
    //        String moneybis_id = moneydata.get("bis_id") != null ? moneydata.get("bis_id").toString() : "";
    //        if (!bisid.equals("") && !moneybis_id.equals("") && bisid.equals(moneybis_id)) {
    //          moneyLists.add(moneydata);
    //        }
    //      }
    //      data.put("moneyList", moneyLists);
    //      result.add(data);
    //    }
    Map map = new HashMap();
    //    map.put("count", count);
    //    map.put("resultList", result);
    map.put("treeList", allbis);
    List resultList = new ArrayList();
    resultList.add(map);
    return resultList;
  }

  /**
   * 年度初始化
   * @param newYear 新年度
   * @param oldYear 旧年度
   * @return
   * @throws Exception
   */
  public boolean copyBISDataToNewYear(String newYear, String oldYear) throws Exception {
    return false;
  }

  private String getInCondition(String field, List bisidList) throws Exception {
    StringBuffer condition = new StringBuffer();
    if (bisidList != null && !bisidList.isEmpty()) {
      condition.append(" and " + field + " in (");
      int num = 1;
      for (int i = 0, size = bisidList.size(); i < size; i++) {
        if (num < 1000) {
          if (i == size - 1) {
            condition.append("'" + bisidList.get(i) + "')");
            num++;
          } else {
            condition.append("'" + bisidList.get(i) + "',");
            num++;
          }
        } else if (i == size - 1) {
          condition.append("'" + bisidList.get(i) + "')");
          num++;
        } else {
          condition.setLength(condition.length() - 1);
          condition.append(") or ('" + bisidList.get(i) + "',");
          num = 1;
        }
      }
    }
    return condition.toString();
  }

  /**
   * 获取项目ID主键
   * @return 
   * @throws Exception
   */
  private String getBISSEQ() throws Exception {
    String sql = "select SEQ_BIS_ID.NEXTVAL num from dual";
    return ((XMLData) (dao.findBySql(sql).get(0))).get("num").toString();
  }

  /**
     * 根据参数ID更新系统参数值
     */
  public void updateBisConfigPara(String para_value, String para_code) throws Exception {
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    StringBuffer sql = new StringBuffer(
      " UPDATE bis_config_parameter SET chr_value=?  WHERE chr_code=? AND set_year=? AND rg_code=? ");
    dao.executeBySql(sql.toString(), new Object[] { para_value, para_code, year, rg_code });
  }

  /**
   * 根据参数ID获得系统参数列表
   */
  public List getBisConfigParas(String paracodes) throws Exception {
    String rg_code = SessionUtil.getRgCode();
    String set_year = SessionUtil.getLoginYear();
    StringBuffer sql = new StringBuffer(
      " select chr_code, chr_value,chr_desc from bis_config_parameter s where s.rg_code='").append(rg_code)
      .append("'  and s.set_year=" + set_year + " and s.chr_code in ('").append(paracodes.replaceAll(",", "','"))
      .append("') order by chr_code ");

    return dao.findBySql(sql.toString());
  }

  /**
   * 获得所有系统参数列表
   */
  public List getAllBisConfigPara() throws Exception {
    String rg_code = SessionUtil.getRgCode();
    String set_year = SessionUtil.getLoginYear();
    StringBuffer sql = new StringBuffer(" select * from bis_config_parameter s WHERE  s.rg_code='").append(rg_code)
      .append("'  and s.set_year=" + set_year + " order by chr_code ");

    return dao.findBySql(sql.toString());
  }

  /**
   * 根据参数ID获得系统参数值
   */
  public String getBisConfigPara(String paracode) {
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String result = "";
    StringBuffer sql = new StringBuffer(" select  chr_code, chr_value from bis_config_parameter s WHERE s.rg_code='")
      .append(rg_code).append("' and s.set_year= ").append(year).append(" and s.chr_code ='").append(paracode)
      .append("' order by chr_code ");
    List list = dao.findBySql(sql.toString());
    if (list != null && list.size() > 0) {
      result = (String) ((Map) list.get(0)).get("chr_value");
    }
    return result;
  }

  public void saveBisConfigPara(Map data) throws Exception {
    Iterator it = data.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry entry = (Entry) it.next();
      if (entry.getKey().equals("bmdata")) {
        List bmdata = (ArrayList) entry.getValue();
        if (bmdata != null && !bmdata.isEmpty()) {
          saveBisBmData(bmdata);
        }
      } else {
        System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        updateBisConfigPara(entry.getValue().toString(), entry.getKey().toString());
      }
    }

  }

  public void saveBisBmData(List data) throws Exception {
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String sql = "delete from bis_config_bm where rg_code=? and set_year=?";
    for (int i = 0, j = data.size(); i < j; i++) {
      Map m = (HashMap) data.get(i);
      m.put("rg_code", rg_code);
      m.put("set_year", year);
    }
    dao.executeBySql(sql, new Object[] { rg_code, year });
    dao.batchSaveDataBySql("bis_config_bm", data);
  }

  /**
   * 查询部门管理数据
   * 
   */
  public List queryBmData(XMLData data) throws Exception {
    String highsql = "select * from bis_config_bm where 1=1 ";
    String condition = data.get("condition") != null ? data.get("condition").toString() : "";
    String pagesql = highsql + condition;
    List resultList = dao.findBySql(pagesql);
    return resultList;
  }

  /**
   * 项目信息综合信息
   */
  public List queryIntegratedBisData(XMLData queryData) throws Exception {
    int year = Integer.parseInt(SessionUtil.getLoginYear());
    String rg_code = SessionUtil.getRgCode();
    int pageIndex = Integer.parseInt((String) queryData.get("pageIndex"));
    int pageRows = Integer.parseInt((String) queryData.get("pageRows"));
    String sql = null;
    if(TypeOfDB.isOracle()) {
    	sql = "select bis.chr_id,bis.chr_code,bis.chr_name,nvl(bism.total_money,0.00) lxpfs,nvl((nvl(bism.money,0.00)+nvl(bism.adjust_money,0.00)+nvl(bism1.money,0.00)+nvl(bism1.adjust_money,0.00)+nvl(bism2.money,0.00)+nvl(bism2.adjust_money,0.00)),0.00) zyss,nvl(bism.money,0.00) ysap"
    		      + year
    		      + ",nvl(bism.adjust_money,0.00) ystz"
    		      + year
    		      + ",nvl(bism.budget_money,0.00) zb"
    		      + year
    		      + ",nvl(bism.budget_balance_money,'0.00') zbye"
    		      + year
    		      + ",nvl(bism.plan_money,0.00) jh"
    		      + year
    		      + ",nvl(bism.pay_money,0.00) zf"
    		      + year
    		      + ",nvl(round(decode(bism.budget_money,0,0,bism.pay_money/bism.budget_money)*100,2),to_char('0.00')) zcjd"
    		      + year
    		      + ",nvl(bism1.money,0.00) ysap"
    		      + (year - 1)
    		      + ",nvl(bism1.adjust_money,0.00) ystz"
    		      + (year - 1)
    		      + ",nvl(bism1.budget_money,0.00) zb"
    		      + (year - 1)
    		      + ",nvl(bism1.budget_balance_money,0.00) zbye"
    		      + (year - 1)
    		      + ",nvl(bism1.plan_money,0.00) jh"
    		      + (year - 1)
    		      + ",nvl(bism1.pay_money,0.00) zf"
    		      + (year - 1)
    		      + ",nvl(round(decode(bism1.budget_money,0,0,bism1.pay_money/bism1.budget_money)*100,2),to_char('0.00')) zcjd"
    		      + (year - 1)
    		      + ",nvl(bism2.money,0.00) ysap"
    		      + (year - 2)
    		      + ",nvl(bism2.adjust_money,0.00) ystz"
    		      + (year - 2)
    		      + ",nvl(bism2.budget_money,0.00) zb"
    		      + (year - 2)
    		      + ",nvl(bism2.budget_balance_money,0.00) zbye"
    		      + (year - 2)
    		      + ",nvl(bism2.plan_money,0.00) jh"
    		      + (year - 2)
    		      + ",nvl(bism2.pay_money,0.00) zf"
    		      + (year - 2)
    		      + ",nvl(round(decode(bism2.budget_money,0,0,bism2.pay_money/bism2.budget_money)*100,2),to_char('0.00')) zcjd"
    		      + (year - 2)
    		      + " from ele_budget_item_summary bis left join VW_ELE_BIS_MONEY bism on bis.set_year=bism.year and bis.chr_id=bism.bis_id"
    		      + " left join VW_ELE_BIS_MONEY bism1 on bis.set_year-1=bism1.year and bis.chr_id=bism1.bis_id"
    		      + " left join VW_ELE_BIS_MONEY bism2 on bis.set_year-2=bism2.year and bis.chr_id=bism2.bis_id"
    		      + " where bis.set_year='" + year + "' and bis.rg_code='" + rg_code + "'";
    } else if(TypeOfDB.isMySQL()) {
    	sql = "select bis.chr_id,bis.chr_code,bis.chr_name,ifnull(bism.total_money,0.00) lxpfs,ifnull((ifnull(bism.money,0.00)+ifnull(bism.adjust_money,0.00)+ifnull(bism1.money,0.00)+ifnull(bism1.adjust_money,0.00)+ifnull(bism2.money,0.00)+ifnull(bism2.adjust_money,0.00)),0.00) zyss,ifnull(bism.money,0.00) ysap"
    			  + year
    			  + ",ifnull(bism.adjust_money,0.00) ystz"
    			  + year
    			  + ",ifnull(bism.budget_money,0.00) zb"
    			  + year
    			  + ",ifnull(bism.budget_balance_money,'0.00') zbye"
    			  + year
    			  + ",ifnull(bism.plan_money,0.00) jh"
    			  + year
    			  + ",ifnull(bism.pay_money,0.00) zf"
    			  + year
    			  + ",ifnull(round((case bism.budget_money when 0 then 0 else bism.pay_money/bism.budget_mone end)*100,2), '0.00') zcjd"
    			  + year
    			  + ",ifnull(bism1.money,0.00) ysap"
    			  + (year - 1)
    			  + ",ifnull(bism1.adjust_money,0.00) ystz"
    			  + (year - 1)
    			  + ",ifnull(bism1.budget_money,0.00) zb"
    			  + (year - 1)
    			  + ",ifnull(bism1.budget_balance_money,0.00) zbye"
    			  + (year - 1)
    			  + ",ifnull(bism1.plan_money,0.00) jh"
    			  + (year - 1)
    			  + ",ifnull(bism1.pay_money,0.00) zf"
    			  + (year - 1)
    			  + ",ifnull(round((case bism1.budget_money when 0 then 0 else bism1.pay_money/bism1.budget_money end)*100,2), '0.00') zcjd"
    			  + (year - 1)
    			  + ",ifnull(bism2.money,0.00) ysap"
    			  + (year - 2)
    			  + ",ifnull(bism2.adjust_money,0.00) ystz"
    			  + (year - 2)
    			  + ",ifnull(bism2.budget_money,0.00) zb"
    			  + (year - 2)
    			  + ",ifnull(bism2.budget_balance_money,0.00) zbye"
    			  + (year - 2)
    			  + ",ifnull(bism2.plan_money,0.00) jh"
    			  + (year - 2)
    			  + ",ifnull(bism2.pay_money,0.00) zf"
    			  + (year - 2)
    			  + ",ifnull(round((case bism2.budget_money when 0 then 0 else bism2.pay_money/bism2.budget_money end)*100,2), '0.00') zcjd"
    			  + (year - 2)
    			  + " from ele_budget_item_summary bis left join VW_ELE_BIS_MONEY bism on bis.set_year=bism.year and bis.chr_id=bism.bis_id"
    			  + " left join VW_ELE_BIS_MONEY bism1 on bis.set_year-1=bism1.year and bis.chr_id=bism1.bis_id"
    			  + " left join VW_ELE_BIS_MONEY bism2 on bis.set_year-2=bism2.year and bis.chr_id=bism2.bis_id"
    			  + " where bis.set_year='" + year + "' and bis.rg_code='" + rg_code + "'";

    }
     
    

    String countsql = "select count(1) num from ele_budget_item_summary bis left join VW_ELE_BIS_MONEY bism on bis.set_year=bism.year and bis.chr_id=bism.bis_id"
      + " left join VW_ELE_BIS_MONEY bism1 on bis.set_year-1=bism1.year and bis.chr_id=bism1.bis_id"
      + " left join VW_ELE_BIS_MONEY bism2 on bis.set_year-2=bism2.year and bis.chr_id=bism2.bis_id "
      + " where bis.set_year='" + year + "' and bis.rg_code='" + rg_code + "'";
    List countList = dao.findBySql(countsql);

    if(TypeOfDB.isOracle()) {
    	sql = "select b.* from (select p.*,rownum as id from (" + sql + ") p where rownum<=" + pageIndex * pageRows + ")"
    			+ " b where b.id>" + (pageIndex - 1) * pageRows;
    } else if(TypeOfDB.isMySQL()) {
    	sql = "select b.* from (select p.* from (" + sql + ") p limit" + (pageIndex-1) + "," + pageRows;
    }

    List bisList = dao.findBySql(sql);

    String count = (String) ((XMLData) (countList.get(0))).get("num");
    Map map = new HashMap();
    map.put("count", count);
    map.put("resultList", bisList);
    List resultList = new ArrayList();
    resultList.add(map);
    return resultList;
  }

  /**
   * 项目明细界面配置信息
   */
  public List getBisDetailConfig(boolean withFB) throws Exception {
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String sql_conditions = "";
    if (!withFB) {
      sql_conditions = " and sys_id != 101 ";
    }

    String sql = "select * from bis_detail_config where is_valid=1 and set_year=? and rg_code=? " + sql_conditions
      + " order by node_code,order_num";
    List conList = dao.findBySql(sql, new Object[] { year, rg_code });
    return conList;
  }

  /**
   * 根据参数年度获取某年资金使用情况数据
   */
  public void createBisMoney(String year) throws Exception {
    String rg_code = SessionUtil.getRgCode();
    //		String deleteSql="delete from ele_budget_item_summary_money where  year=? and rg_code=?";
    String deleteSql = "delete from ele_budget_item_summary_money m where m.year = ?  and exists (select 1 from ele_budget_item_summary b where m.bis_id = b.chr_id and b.rg_code = ?)";
    String sql = "select * from vw_ele_bis_yearmoney where year=? and rg_code=? ";
    List moneyList = dao.findBySqlWithYear(Integer.parseInt(year), sql, new Object[] { year, rg_code });

    dao.executeBySql(deleteSql, new Object[] { year, rg_code });
    if (moneyList != null && !moneyList.isEmpty())
      dao.batchSaveDataBySql("ELE_BUDGET_ITEM_SUMMARY_MONEY", moneyList);
  }

  /**
   * 返回子类实现的预算项目辅助项目编码规则
   * @param currentXML 主界面当前数据
   * @param size增加数量
   * @return XMLData里必须要两个参数 condition查询现有的预算项目过滤条件，给空值就默认为原有过滤条件，isContinue是否继续保存1，继续，0不继续。
   */
  public XMLData getBisChrCodeSup(List currentList, int size) throws Exception {
    XMLData returnXML = new XMLData();
    String bis_supcode = getBisConfigPara("bis_supcode");//0#不启用+1#启用
    if (bis_supcode != null && !bis_supcode.equals("1")) {
      //    new MessageBox("  项目统一配置中未启用辅助项目编码！", MessageBox.MESSAGE,
      //        MessageBox.BUTTON_OK).show();
      returnXML.put("isContinue", "1");
      return returnXML;
    }
    String en_ids = "";
    for (int i = 0, j = currentList.size(); i < j; i++) {
      HashMap data = (HashMap) currentList.get(i);
      String en_id = data.get("en_id") != null && !data.get("en_id").equals("") ? data.get("en_id").toString() : null;
      if (en_id != null)
        en_ids = en_ids + "," + en_id;
    }
    en_ids = en_ids.length() > 0 ? en_ids.substring(1) : en_ids;
    List enLis = dictionary.findEleValues("EN", null, null, false, null, null,
      " and chr_id in('" + en_ids.replaceAll(",", "','") + "')");
    //bis_supcoderule   0#项目编码-预算单位编码+1#预算单位-3位随机码+2#预算单位编码前3位-年度编码后2位-3位随机码
    String bis_supcoderule = getBisConfigPara("bis_supcoderule");

    for (int i = 0, j = currentList.size(); i < j; i++) {
      Map belongdata = (Map) currentList.get(i);
      String chr_code_sup = "";
      for (int n = 0, m = enLis.size(); n < m; n++) {
        Map endata = (Map) enLis.get(n);
        if (belongdata.get("en_id") != null && !belongdata.get("en_id").equals("") && endata.get("chr_id") != null
          && !endata.get("chr_id").equals("") && endata.get("chr_id").equals(belongdata.get("en_id"))) {
          if (bis_supcoderule != null && bis_supcoderule.equals("0")) {//0#项目编码-预算单位编码
            chr_code_sup = belongdata.get("chr_code") + "" + endata.get("chr_code");
          } else if (bis_supcoderule != null && bis_supcoderule.equals("1")) {//1#预算单位-3位随机码
            String condition = "  and length(chr_code_sup)=" + (endata.get("chr_code").toString().length() + 3)
              + "  and chr_code_sup like '" + endata.get("chr_code") + "%' order by chr_code_sup desc";
            chr_code_sup = endata.get("chr_code") + autocode_sup(3, size, condition);
          } else if (bis_supcoderule != null && bis_supcoderule.equals("2")) {//2#预算单位编码前3位-年度编码后2位-8位随机码
            String set_year = SessionUtil.getLoginYear();
            int yearLong = set_year.length();
            String en_codeSub = endata.get("chr_code").toString().substring(0, 3);
            String chr_code = en_codeSub + set_year.substring(yearLong - 2, yearLong);
            chr_code_sup = chr_code
              + autocode_sup(8, size, " and length(chr_code_sup)=13  and  chr_code_sup like '" + en_codeSub
                + "%' order by chr_code_sup desc");
          }
          belongdata.put("chr_code_sup", chr_code_sup);
        }
      }
    }

    returnXML.put("belongList", currentList);
    returnXML.put("isContinue", "1");
    return returnXML;
  }

  private String autocode_sup(int codeLength, int num, String condition) {
    String chr_code = "";
    String maxCodeNum = "";
    List Bislist = dao.findBySql("select chr_code_sup from ele_budget_item_summary_belong where 1=1 " + condition);
    if (Bislist == null || Bislist.size() == 0) {// 如果没有对应预算项目编码就从1开始
      maxCodeNum = String.valueOf(1 + num);
    } else {// 如果有就最大值+1
      String maxChr_code = ((XMLData) Bislist.get(0)).get("chr_code_sup").toString();
      Pattern pattern = Pattern.compile("[0-9]*");
      if (pattern.matcher(maxChr_code).matches()) {
        maxCodeNum = String.valueOf(Integer.parseInt(maxChr_code.substring(maxChr_code.length() - codeLength,
          maxChr_code.length())) + 1 + num);
      }
    }
    for (int i = (maxCodeNum.length()); i < codeLength; i++) {// 补零
      maxCodeNum = "0" + maxCodeNum;
    }
    chr_code = maxCodeNum;

    return chr_code;
  }

  public IDictionary getDictionary() {
    return dictionary;
  }

  public void setDictionary(IDictionary dictionary) {
    this.dictionary = dictionary;
  }

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

}
