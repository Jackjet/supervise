package gov.df.fap.service.bis;

import gov.df.fap.api.dictionary.bis.IBISaveInterface;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BISaveBO implements IBISaveInterface {
  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao;

  @Override
  public Map getSaveData(HttpServletRequest request, HttpServletResponse response) throws Exception {

    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put("chr_name", request.getParameter("chr_name"));
    dataMap.put("chr_code", request.getParameter("chr_code"));
    dataMap.put("parent_id", request.getParameter("parent_id"));
    dataMap.put("public_sign", request.getParameter("public_sign"));
    dataMap.put("enabled", request.getParameter("enabled"));
    dataMap.put("mb_id", request.getParameter("mb_id"));
    dataMap.put("disp_code", request.getParameter("chr_code"));
    dataMap.put("level_num", "1");
    dataMap.put("is_deleted", "0");
    dataMap.put("sys_id", "001");
    dataMap.put("chr_id", request.getParameter("bis_id"));
    dataMap.put("set_year", SessionUtil.getLoginYear());
    dataMap.put("rg_code", SessionUtil.getRgCode());
    List belongList = new ArrayList();
    Map belong = null;
    String mb = request.getParameter("mb_id");
    String en = request.getParameter("agency_id");
    String bi = request.getParameter("agencyexp_id");
    if (mb != null && en != null) {

      belong = new HashMap();
      belong.put("mb_id", mb);
      belong.put("agency_id", en);
      belong.put("agencyexp_id", bi);
      belong.put("year", SessionUtil.getLoginYear());
      belongList.add(belong);

    } else if (mb != null && en == null) {

      belong = new HashMap();
      belong.put("mb_id", mb);
      belong.put("agencyexp_id", bi);
      belong.put("year", SessionUtil.getLoginYear());
      belongList.add(belong);

    } else if (mb == null && en != null) {

      belong = new HashMap();
      belong.put("agency_id", en);
      belong.put("agencyexp_id", bi);
      belong.put("year", SessionUtil.getLoginYear());
      belongList.add(belong);

    } else if (mb == null && en == null) {
      belong = new HashMap();
      belong.put("agencyexp_id", bi);
      belong.put("year", SessionUtil.getLoginYear());
      belongList.add(belong);
    }
    dataMap.put("belongList", belongList);
    return dataMap;
  }

  public Map checkBis(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String bis_id = request.getParameter("bis_id");
    String bis_code = request.getParameter("bis_code");
    Map ckMap = new HashMap();
    if (bis_id == null || "".equals(bis_id)) {
      String sql = "select * from ELE_BUDGET_ITEM_SUMMARY where chr_code=" + "'" + bis_code + "'";
      List cklist = dao.findBySql(sql);
      if (cklist != null && cklist.size() > 0) {
        ckMap.put("isok", 0);
      } else {
        ckMap.put("isok", 1);
      }
    } else {
      String sql = "select * from ELE_BUDGET_ITEM_SUMMARY where chr_code=" + "'" + bis_code + "'" + "and chr_id=" + "'"
        + bis_id + "'";
      List cklist = dao.findBySql(sql);
      if (cklist != null && cklist.size() > 0) {
        ckMap.put("isok", 1);
      } else if (cklist == null || cklist.size() == 0) {
        String sql1 = "select * from ELE_BUDGET_ITEM_SUMMARY where chr_code=" + "'" + bis_code + "'";
        List cklist1 = dao.findBySql(sql1);
        if (cklist1 != null && cklist1.size() > 0) {
          ckMap.put("isok", 0);
        } else {
          ckMap.put("isok", 1);
        }
      }

    }
    return ckMap;
  }

  public Map getEleTreeFile(HttpServletRequest request, HttpServletResponse response) {
    Map map = new HashMap();
    String agency_id = request.getParameter("agency_id");
    String agencyexp_id = request.getParameter("agencyexp_id");
    String mb_id = request.getParameter("mb_id");
    if (mb_id != null && !"".equals(mb_id)) {
      String sql = "select chr_code,chr_name  from VW_FASP_MANAGE_BRANCH where chr_id='" + mb_id + "'";
      List list1 = dao.findBySql(sql);
      map.put("mb", list1);
    } else {
      map.put("mb", null);
    }
    if (agency_id != null && !"".equals(agency_id)) {
      String sql = "select chr_code,chr_name  from VW_FASP_AGENCY where chr_id='" + agency_id + "'";
      List list2 = dao.findBySql(sql);
      map.put("agency", list2);
    } else {
      map.put("agency", null);
    }
    if (agencyexp_id != null && !"".equals(agencyexp_id)) {
      String sql = "select chr_code,chr_name  from VW_FASP_AGENCYEXP where chr_id='" + agencyexp_id + "'";
      List list3 = dao.findBySql(sql);
      map.put("agencyexp", list3);
    } else {
      map.put("agencyexp", null);
    }

    return map;
  }

  @Override
  public Map selectBis(HttpServletRequest request, HttpServletResponse response) {
    String bis_id = request.getParameter("bis_id");
    Map map = new HashMap();
    String sql = "select * from (select * from ELE_BUDGET_ITEM_SUMMARY a where a.chr_id='"
      + bis_id
      + "' or a.parent_id = '" + bis_id +"' or a.parent_id in (select chr_id from ELE_BUDGET_ITEM_SUMMARY where parent_id = '" + bis_id + "')) s left join ELE_BUDGET_ITEM_SUMMARY_BELONG b on 1=1 and s.chr_id=b.bis_id and s.is_deleted=0  order by chr_code";
    String countsql = null;
    if(TypeOfDB.isOracle()) {
    	countsql = "select count(1) from (" +sql+")"; 
    } else if(TypeOfDB.isMySQL()) {
    	countsql = "select count(1) from (" +sql+") t"; 
    }
    List countList = dao.findBySql(countsql);
    String count = (String) ((XMLData) (countList.get(0))).get("count(1)");
    int rowsCount = Integer.parseInt(count);
    int pageCount;
    if (rowsCount % 20 > 0) {
      pageCount = rowsCount / 20 + 1;
    } else {
      pageCount = rowsCount / 20;
    }
    List list = dao.findBySql(sql);
    map.put("pageCount", pageCount);
    map.put("rowsCount", rowsCount);
    map.put("List", list);
    return map;
  }

  @Override
  public Map initPage(HttpServletRequest request, HttpServletResponse response){
	  Map map = new HashMap();
	    int pageIndex = Integer.parseInt((String) request.getParameter("pageIndex")) + 1;
	    int pageRows = Integer.parseInt((String) request.getParameter("pageRows"));
	    String rg_code = SessionUtil.getRgCode();
	    String con = " and rg_code='" + rg_code + "' ";
	    String ENABLED_STR = " and ENABLED=1 ";
	    String subsql = null;
	    if(TypeOfDB.isOracle()) {
	    	subsql = "select * from (select e1.*,e1.chr_code||' '||e1.chr_name codeName,e2.chr_code||' '||e2.chr_name as parent_name from ele_budget_item_summary e1 left join ele_budget_item_summary e2 on e1.parent_id=e2.chr_id) s left join ELE_BUDGET_ITEM_SUMMARY_BELONG b on 1=1 and s.chr_id=b.bis_id and s.is_deleted=0"
	    			+ ENABLED_STR + con + " order by chr_code";
	    } else if(TypeOfDB.isMySQL()) {
	    	subsql = "select * from (select e1.*, concat(e1.chr_code, ' ', e1.chr_name) codeName, concat(e2.chr_code, ' ', e2.chr_name) as parent_name from ele_budget_item_summary e1 left join ele_budget_item_summary e2 on e1.parent_id=e2.chr_id) s left join ELE_BUDGET_ITEM_SUMMARY_BELONG b on 1=1 and s.chr_id=b.bis_id and s.is_deleted=0"
	    			+ ENABLED_STR + con + " order by chr_code";
	    }
//	    String subsql = "select * from (select * from ELE_BUDGET_ITEM_SUMMARY a where a.chr_id='"
//	    	      + bis_id
//	    	      + "' or a.parent_id = '" + bis_id +"' or a.parent_id in (select chr_id from ELE_BUDGET_ITEM_SUMMARY where parent_id = '" + bis_id + "')) s left join ELE_BUDGET_ITEM_SUMMARY_BELONG b on 1=1 and s.chr_id=b.bis_id and s.is_deleted=0  order by chr_code";
//	    	    
//	    
	    String sql = null;
	    if(TypeOfDB.isOracle()) {
	    	sql = "select b.* from (select p.*,rownum as id from (" + subsql + ") p where rownum<=" + pageIndex
	    			* pageRows + ")" + " b where b.id>" + (pageIndex - 1) * pageRows;
	    } else if(TypeOfDB.isMySQL()) {
	    	sql = "select b.* from (select p.* from (" + subsql + ") p limit " + (pageIndex - 1) + "," + pageRows;
	    }

	    String countSql = "select count(1) from (" + subsql + ") t";

	    List bisList = dao.findBySql(sql);

	    List countList = dao.findBySql(countSql);

	    String count = (String) ((XMLData) (countList.get(0))).get("count(1)");
	    int rowsCount = Integer.parseInt(count);
	    int pageCount;
	    if (rowsCount % pageRows > 0) {
	      pageCount = rowsCount / pageRows + 1;
	    } else {
	      pageCount = rowsCount / pageRows;
	    }
	    map.put("pageCount", pageCount);
	    map.put("rowsCount", rowsCount);
	    map.put("resultList", bisList);

	    return map;
  }
  
  @Override
  public Map pageChange(HttpServletRequest request, HttpServletResponse response) {
    Map map = new HashMap();
    int pageIndex = Integer.parseInt((String) request.getParameter("pageIndex")) + 1;
    int pageRows = Integer.parseInt((String) request.getParameter("pageRows"));
    String bis_id = (String)request.getParameter("bis_id");
    String rg_code = SessionUtil.getRgCode();
    String con = " and rg_code='" + rg_code + "' ";
    String ENABLED_STR = " and ENABLED=1 ";
    String subsql = null;
    if(bis_id==null||bis_id.equals("")){
    	if(TypeOfDB.isOracle()) {
    		subsql = "select * from (select e1.*,e1.chr_code||' '||e1.chr_name codeName,e2.chr_code||' '||e2.chr_name as parent_name from ele_budget_item_summary e1 left join ele_budget_item_summary e2 on e1.parent_id=e2.chr_id) s left join ELE_BUDGET_ITEM_SUMMARY_BELONG b on 1=1 and s.chr_id=b.bis_id and s.is_deleted=0"
    				+ ENABLED_STR + con + " order by chr_code";
    	} else if(TypeOfDB.isMySQL()) {
    		subsql = "select * from (select e1.*, concat(e1.chr_code, ' ', e1.chr_name) codeName, concat(e2.chr_code, ' ', e2.chr_name) as parent_name from ele_budget_item_summary e1 left join ele_budget_item_summary e2 on e1.parent_id=e2.chr_id) s left join ELE_BUDGET_ITEM_SUMMARY_BELONG b on 1=1 and s.chr_id=b.bis_id and s.is_deleted=0"
    				+ ENABLED_STR + con + " order by chr_code";
    	}
        
    }else{
    	subsql = "select * from (select * from ELE_BUDGET_ITEM_SUMMARY a where a.chr_id='"
      	      + bis_id
      	      + "' or a.parent_id = '" + bis_id +"' or a.parent_id in (select chr_id from ELE_BUDGET_ITEM_SUMMARY where parent_id = '" + bis_id + "')) s left join ELE_BUDGET_ITEM_SUMMARY_BELONG b on 1=1 and s.chr_id=b.bis_id and s.is_deleted=0  order by chr_code";
      	
    }
//    String subsql = "select * from (select e1.*,e1.chr_code||' '||e1.chr_name codeName,e2.chr_code||' '||e2.chr_name as parent_name from ele_budget_item_summary e1 left join ele_budget_item_summary e2 on e1.parent_id=e2.chr_id) s left join ELE_BUDGET_ITEM_SUMMARY_BELONG b on 1=1 and s.chr_id=b.bis_id and s.is_deleted=0"
//    		+ ENABLED_STR + con + " order by chr_code";
        
    String sql = null;
    if(TypeOfDB.isOracle()) {
    	sql = "select b.* from (select p.*,rownum as id from (" + subsql + ") p where rownum<=" + pageIndex
    			* pageRows + ")" + " b where b.id>" + (pageIndex - 1) * pageRows;
    } else if(TypeOfDB.isMySQL()) {
    	sql = "select b.* from (select p.* from (" + subsql + ") p limit " + (pageIndex - 1) + "," + pageRows;
    }

    String countSql = null;
    if(TypeOfDB.isOracle()) {
    	countSql = "select count(1) from (" + subsql + ")";
    } else if(TypeOfDB.isMySQL()) {
    	countSql = "select count(1) from (" + subsql + ") t";
    }

    List bisList = dao.findBySql(sql);

    List countList = dao.findBySql(countSql);

    String count = (String) ((XMLData) (countList.get(0))).get("count(1)");
    int rowsCount = Integer.parseInt(count);
    int pageCount;
    if (rowsCount % pageRows > 0) {
      pageCount = rowsCount / pageRows + 1;
    } else {
      pageCount = rowsCount / pageRows;
    }
    map.put("pageCount", pageCount);
    map.put("rowsCount", rowsCount);
    map.put("resultList", bisList);

    return map;
  }

public XMLData getBisAdd() {
	String sql="select chr_code,chr_value,chr_desc from sys_userpara where chr_code='bis_add'";
	List list=dao.findBySql(sql);
	XMLData data=null;
	if(list!=null&&list.size()>0){
		 data=(XMLData)list.get(0);
	}

	return data;
}
}
