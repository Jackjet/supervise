package gov.df.fap.service.dictionary;

import gov.df.fap.bean.dictionary.dto.EleRelationSQLDTO;
import gov.df.fap.service.util.UtilCache;
import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RelationOperation 关联关系逻辑实现类,控制功能逻辑
 * @version 1.0
 * @author Zhang Peng
 * @since java 1.6
 */
@Component
public class RelationOperation {
	@Autowired
	
  protected RelationDAOService relationDAO = null;

  /**
   * 默认构造函数
   */
  public RelationOperation() {
  }

  /**
   * 通过传入的xml查询数据并返回结果
   * @param inXmlObj 传入的xml
   * @param isNeedCount 是否需要分页
   * @return 查询结果xml
   * @throws Exception
   */
  public XMLData getRelation(String inXmlObj, boolean isNeedCount) throws Exception {
    return relationDAO.getRelation(inXmlObj, isNeedCount);
  }

  /**
   * 通过传入的xml查询数据并返回结果
   * @param inXmlObj 传入的xml
   * @param isNeedCount 是否需要分页
   * @return 查询结果xml
   * @throws Exception
   */
  public String getRelationByXml(String inXmlObj, boolean isNeedCount) throws Exception {
    return relationDAO.getRelationByXml(inXmlObj, isNeedCount);
  }
  public String getRelationByXml(String inXmlObj) throws Exception {
	    return relationDAO.getRelationByXml(inXmlObj);
	  }

  /**
   * 关联关系明细表数据插入。
   *                                                     /关联关系主表信息
   * @param relationData 关联关系数据结构,结构为:relationData                         
   *                                                     \row 明细表配置信息detail --> (主控要素编码 - 被控要素列表)值对 
   * @return 关联关系唯一id 
   * @throws Exception 数据插入操作失败原因
   */
  public String insertRelation(XMLData relationData) throws Exception {
    return relationDAO.insertRelation(relationData);
  }

  /**
   * 关联关系明细表数据修改。
   *                                                     /关联关系主表信息
   * @param relationData 关联关系数据结构,结构为:relationData                         
   *                                                     \row 明细表配置信息XMLData --> (主控要素编码 - 被控要素列表)值对 
   * @return boolean 修改是否成功 
   * @throws Exception 数据修改操作失败原因
   */
  public boolean modifyRelation(XMLData relationData) throws Exception {
    return relationDAO.modifyRelation(relationData);
  }

  /**
   * 通过传入的关联关系id删除关联关系配置数据
   * @param relation_id 关联关系id
   * @return 操作是否成功的结果
  * @throws Exception 数据删除操作失败原因
   */
  public boolean deleteRelation(String relation_id) throws Exception {
    return relationDAO.deleteRelation(relation_id);
  }

  public RelationDAOService getRelationDAO() {
    return relationDAO;
  }

  public void setRelationDAO(RelationDAOService relationDAO) {
    this.relationDAO = relationDAO;
  }

  /**
   * add by sungy 20080409
   * 取得指定关联关系的某个主控要素的被控要素结果集
   * @param relation_code 关联关系编码
   * @param priEleValue 主控要素编码值
   * @param set_year 业务年度
   * @return 被控要素结果集（XMLData对象列表集合）
   * XMLData对象包含的键值：pri_ele_code、pri_ele_value、sec_ele_code、sec_ele_value
   * @throws Exception
   */
  public List getRelationByPriEleValue(String relation_code, String priEleValue, int set_year) throws Exception {
    return relationDAO.getRelationByPriEleValue(relation_code, priEleValue, set_year);

  }

  /** add by bigdog 20080513
   * 根据被控要素编码，主控要素编码和值，拼凑from和where子句
   * @param element 被控要素编码
   * @param relation 主控要素编码和值
   * @param tableAlias 被控要素表的别名
   * @return EleRelationSQLDTO对象
   */
  public EleRelationSQLDTO getEleRelationSQLDTO(String element, Map relation, String tableAlias) {
    EleRelationSQLDTO returnDTO = new EleRelationSQLDTO();
    String pri_ele_code = "", pri_ele_value = "";
    StringBuffer fromSQL = new StringBuffer();
    StringBuffer whereSQL = new StringBuffer();
    String alias = "", relation_id = "";

    Object[] keyArr = relation.keySet().toArray();

    for (int i = 0; i < keyArr.length; i++) {
      pri_ele_code = keyArr[i] == null ? "" : (String) keyArr[i];
      pri_ele_value = relation.get(keyArr[i]) == null ? "" : (String) relation.get(keyArr[i]);
      alias = "S" + String.valueOf(i + 1);

      if ("".equals(fromSQL.toString())) {
        fromSQL.append(" sys_relations ").append(alias);
      } else {
        fromSQL.append(" , sys_relations ").append(alias);
      }

      //缓存处理
      relation_id = UtilCache.getEleRelationID(pri_ele_code + element);
      if (relation_id == null || "".equals(relation_id)) {
        //System.out.println(pri_ele_code + element + " 缓存没有命中");
        relation_id = relationDAO.getRelationID(pri_ele_code, element);
        UtilCache.putEleRelationID(pri_ele_code + element, relation_id);
      } else {
        //System.out.println(pri_ele_code + element + " 缓存命中");
      }

      whereSQL.append(" and ").append(alias).append(".relation_id = '").append(relation_id).append("' and ")
        .append(alias).append(".pri_ele_value = '").append(pri_ele_value).append("' and ").append(tableAlias)
        .append(".chr_code = ").append(alias).append(".sec_ele_value");
    }

    returnDTO.setFromSQL(fromSQL.toString());
    returnDTO.setWhereSQL(whereSQL.toString());
    return returnDTO;
  }
}

/*and s2.relation_id = '{47CACE67-F1A6-11DB-A055-E7AA0D40C168}'
    and s2.pri_ele_value = '002' and "EN".chr_code =
                                       s2.sec_ele_value*/

