package gov.df.fap.util.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * xml对象模型
 *             /属性(值对模式保存)
 *             
 *       XMLData  -父子结构则递归保存XMLData      
 *                   /单个要素节点则存放属性XMLData     
 *             \子节点
 *                   \多个相同要素节点则存放List
 * 使用这种模式应避免让元素名和属性名相同
 * eg:
 * <data>                                          
 *   <aa>                                   /XMLData aa --XMLData bb
 *     </bb>                        /List aa
 *   </aa>       ------>XMLData data        \XMLData aa
 *   <aa />                         \XMLData cc
 *   <cc/>
 * </data>
 * @version 1.0
 * @author 
 * @see XMLObjectVisitor#visit(Element)
 * @see XMLObjectVisitor#visit(Attribute)
 * @since java 1.6
 */
public class XMLData extends HashMap {
  private static final long serialVersionUID = 100000000000000L;

  /**
   * 重载Map的put方法,实现个性化需求
   * @param childName 对象名
   * @param child 对象
   * @return 原值对象,若不存在则为null
   */
  public Object put(Object childName, Object child) {
    return this.addObject(this, (String) childName, child);
  }

  /**
   * 根据对象名,将对象保存在父对象中,覆盖原同名对象
   * @param parent 父Map对象
   * @param child 子Map对象
   * @param childName 子对象名称
   * @return 原值对象,若不存在则为null
   */
  private Object addObject(XMLData parent, String childName, Object child) {
    Object oldObj = parent.get(childName);
    if (parent.get(childName) == null) {
      super.put(childName, child);
    } else if (parent.get(childName) instanceof Map) {
      List list = new ArrayList();
      list.add(parent.get(childName));
      list.add(child);
      super.put(childName, list);
    } else if (parent.get(childName) instanceof List) {
      List ls = (List) parent.get(childName);
      ls.add(child);
    } else
      super.put(childName, child);

    return oldObj;
  }

  /**
   * 根据名称定位属性或元素
   * @param objName 名称
   * @return 第一个匹配对象{如果为属性则为String、如果为单标签则为Map,如果为多标签则为List},如果不存在则返回null
   */
  public Object getSubObject(String objName) {
    return getSubObjectByName(this, objName);
  }

  /**
   * 使用递归的方法进行解析
   * @param object 元素对象
   * @param objName 所需定位的属性名/元素名
   * @return 第一个匹配对象{如果为属性则为String、如果为单标签则为Map,如果为多标签则为List},如果不存在则返回null
   */
  public Object getSubObjectByName(Object object, String objName) {
    if (object instanceof Map) {
      Map map = (Map) object;
      if (map.containsKey(objName))
        return map.get(objName);
      else {
        if (map.toString().matches(".*\\s*" + objName + "\\s*[=].*")) {
          Object[] keyset = map.keySet().toArray();
          for (int i = 0; i < keyset.length; i++) {
            if (map.get(keyset[i]) instanceof Map || map.get(keyset[i]) instanceof List) {
              Object subObject = getSubObjectByName(map.get(keyset[i]), objName);
              if (subObject != null) {
                return subObject;
              }
            }
          }
        }
      }
    } else if (object instanceof List) {
      List ls = (List) object;
      if (ls.toString().matches(".*\\s*" + objName + "\\s*[=]\\s*.*")) {
        for (int i = 0; i < ls.size(); i++) {
          Object subObject = getSubObjectByName(ls.get(i), objName);
          if (subObject != null) {
            return subObject;
          }
        }
      }
    }
    return null;
  }

  /**
   * 根据XML动态为对象本身赋值
   * @param xml xml字符串
   * @return 赋值前的XMLData对象
   * @throws Exception
   */
  public XMLData loadXML(String xml) throws Exception {
    XMLData xmlData = this;
    this.putAll(ParseXML.convertXmlToObj(xml));
    return xmlData;
  }

  /**
   * 动态将当前对象转换成xml字符串
   * @param rootName xml根节点名称
   * @return xml字符串
   * @throws Exception
   */
  public String asXML(String rootName) throws Exception {
    return ParseXML.convertObjToXml(this, rootName);
  }

  /**
   * 对于从一张数据表返回的规范形式的记录，返回记载信息Map的List
   * @return 记录信息数组，由一个或多个Map组成
   */
  public List getRecordList() {
    return getRecordListByTag("row");
  }

  /**
   * 对于从一张数据表返回的规范形式的记录，返回记载信息Map的List
   * @param tagName 标签名
   * @return 记录信息数组，由一个或多个Map组成
   */
  public List getRecordListByTag(String tagName) {
    List data = null;
    Object object = this.getSubObject(tagName);
    if (object instanceof Map) {
      Map map = (Map) object;
      data = new ArrayList();
      data.add(map);
    } else if (object instanceof List) {
      data = (List) object;
    }
    return data == null ? new ArrayList() : data;
  }

  public Object clone() {
    return super.clone();
  }
}
