package gov.df.fap.service.paging;

import gov.df.fap.api.paging.IPagingUtilService;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 分页公共方法--供自动生成表格使用
 * @author hp
 * @version 2017-3-15
 */
@Service
public class PagingUtilService implements IPagingUtilService {

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO generalDAO;

  /**
   * 分页公共方法--供自动生成表格使用
   * @author hp
   * @version 2017-3-15
   * sql:查询语句   obj:参数  getInfo:由前端传入的数据
   */
  public Map<String, Object> PagingCommon(String sql, Object[] obj, HttpServletRequest request) throws Exception {

    //获取分页信息  数据     每页个数,第几页,总条数
    String getInfo = request.getParameter("pageInfo");
    //获取总数
    String total = request.getParameter("totals");
    //获取排序方式
    String sortType = request.getParameter("sortType");
    Map<String, Object> map = new HashMap<String, Object>();
    if (!"".equals(sql) && sql != null) {
      String[] getText = getInfo.split(",");
      String size = getText[0];
      String pageIndex = getText[1];
      String totleCount = "0";
      try {
        if (getText.length == 2) {
          String totleSql = "select count(1) tot from (  " + sql + " )";
          List totlist = generalDAO.findBySql(totleSql, obj);
          Map<String, String> totMap = (Map<String, String>) totlist.get(0);
          totleCount = totMap.get("tot");
          if (total != null && !"".equals(total)) {
            Map sumMap = new HashMap<String, Object>();
            String[] codeName = total.split(",");
            for (int k = 0; k < codeName.length; k++) {
              String sumsql = "select sum(" + codeName[k] + ") sum from (  " + sql + " )";
              List sumlist = generalDAO.findBySql(sumsql, obj);
              if (sumlist.size() > 0) {
                Map codemap = (Map) sumlist.get(0);
                sumMap.put(codeName[k], codemap.get("sum"));
              }
            }
            //返回列数据合计
            map.put("totals", sumMap);
          }
        } else {
          totleCount = getText[2];
        }
        //返回总条数数据
        map.put("totalElements", totleCount);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        throw new Exception("sql异常:" + e.getMessage());
      }
      try {

        int maxNum = Integer.parseInt(size) * (Integer.parseInt(pageIndex) + 1);
        int minNum = Integer.parseInt(size) * Integer.parseInt(pageIndex);
        String pagingSql = "";
        JSONObject jason = JSONObject.fromObject(sortType);
        Map<String, Object> map1 = (Map<String, Object>) jason;
        if (map1.size() > 0) {
        	if(TypeOfDB.isOracle()) {
        		pagingSql = "SELECT * FROM ( SELECT A.* , ROWNUM RN FROM ( select * from (" + sql + " ) order by "
        				+ map1.get("name") + ",  " + map1.get("type") + " ) A WHERE ROWNUM <= " + maxNum + " ) WHERE  RN >" + minNum;
        	} else if(TypeOfDB.isMySQL()) {
        		pagingSql = "select * from (" + sql + ") order by " + map1.get("name") + ", " + map1.get("type") + " limit " + pageIndex + "," + size;
        	}

        } else {
        	if(TypeOfDB.isOracle()) {
        		pagingSql = "SELECT * FROM ( SELECT A.* , ROWNUM RN FROM ( " + sql + " ) A WHERE ROWNUM <= " + maxNum
        				+ " ) WHERE  RN >" + minNum;
        	} else if(TypeOfDB.isMySQL()) {
        		pagingSql = "SELECT A.* FROM (" + sql + ") A limit " + pageIndex + ", " + size; 
        	}
        }
        List datalist = generalDAO.findBySql(pagingSql, obj);
        //返回分页信息
        map.put("dataDetail", datalist);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        throw new Exception("sql异常:" + e.getMessage());
      }
    } else {
      throw new Exception("sql不能为空");
    }
    return map;
  }

}
