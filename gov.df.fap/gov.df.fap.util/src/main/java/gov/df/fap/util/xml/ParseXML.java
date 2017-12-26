package gov.df.fap.util.xml;

import gov.df.fap.util.file.FileUtil;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Visitor;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * XML解析和加载类
 * 
 * @version 1.0
 * @author 
 * @see ParseXML#convertXmlToSql(String)
 * @since java 1.6
 */
public class ParseXML {
  public ParseXML() {

  }

  /**
   * 根据传入的xml解析形成对应的SQL查询语句
   * 
   * @param xml
   *            xml源串
   * @return 对应的查询语句
   * @throws Exception
   */
  public static String convertXmlToQuerySQL(String xml) throws Exception {
    return convertXmlToSQL(xml, "query");
  }

  /**
   * 根据传入的xml解析形成对应的SQL删除语句
   * 
   * @param xml
   *            xml源串
   * @return 对应的删除语句
   * @throws Exception
   */
  public static String covertXmlToDeleteSQL(String xml) throws Exception {
    return convertXmlToSQL(xml, "delete");
  }

  /**
   * 根据解析传入的xml形成对应的SQL插入语句
   * 
   * @param xml
   *            xml源串
   * @return 对应的插入语句
   * @throws Exception
   */
  public static String covertXmlToInsertSQL(String xml) throws Exception {
    return convertXmlToSQL(xml, "insert");
  }

  /**
   * 根据解析传入的xml和附加值对形成对应的SQL插入语句
   * 
   * @param xml
   *            xml源串
   * @param addition
   *            附加插入值对,主要满足添加部分默认字段用
   * @return 对应的插入语句
   * @throws Exception
   */
  public static String covertXmlToInsertSQL(String xml, Map addition) throws Exception {
    XMLSQLVisitor visitor = new XMLSQLVisitor();
    visitor.setSqlType("insert");
    visitor.setOperaAddition(addition);
    convertXml(xml, visitor);
    if (visitor.getError() != 0)
      throw new Exception(visitor.parseError(visitor.getError()));
    return visitor.getTotalSQL();
  }

  /**
   * 根据解析传入的xml形成对应的SQL修改语句
   * 
   * @param xml
   *            xml源串
   * @return 对应的修改语句
   * @throws Exception
   */
  public static String covertXmlToModifySQL(String xml) throws Exception {
    return convertXmlToSQL(xml, "modify");
  }

  /**
   * 根据解析传入的xml和附加值对形成对应的SQL修改语句
   * 
   * @param xml
   *            xml源串
   * @param addition
   *            附加修改值对,主要满足更新部分默认字段用
   * @return 对应的修改语句
   * @throws Exception
   */
  public static String covertXmlToModifySQL(String xml, Map addition) throws Exception {
    XMLSQLVisitor visitor = new XMLSQLVisitor();
    visitor.setSqlType("modify");
    visitor.setOperaAddition(addition);
    convertXml(xml, visitor);
    if (visitor.getError() != 0)
      throw new Exception(visitor.parseError(visitor.getError()));
    return visitor.getTotalSQL();
  }

  /**
   * 将XML按照设计规则转化成对应的SQL语句
   * 
   * @param xml
   *            对应xml字符串
   * @param opera_type
   *            操作类型 "query":形成查询语句 "delete":形成删除语句 "insert":形成插入语句
   *            "modify":形成修改语句
   * @return SQL语句
   * @throws Exception
   *             xml解析异常
   */
  public static String convertXmlToSQL(String xml, String opera_type) throws Exception {
    XMLSQLVisitor visitor = new XMLSQLVisitor();
    visitor.setSqlType(opera_type);
    convertXml(xml, visitor);
    if (visitor.getError() != 0)
      throw new Exception(visitor.parseError(visitor.getError()));
    return visitor.getTotalSQL();
  }

  /**
   * 按照设计规则将xml串转化为对象
   * 
   * @param xml
   *            字符串
   * @return Map对象
   * @throws Exception
   *             转化异常
   */
  public static XMLData convertXmlToObj(String xml) throws Exception {
    XMLObjectVisitor visitor = new XMLObjectVisitor();
    convertXml(xml, visitor);
    return visitor.getXmlObj();
  }

  /**
   * 根据设计规则将Xml转化成对象,并根据子对象名定位
   * 
   * @param xml
   *            字符串
   * @param objName
   *            子对象名
   * @return 相应对象{如果为属性则为String、如果为单标签则为Map,如果为多标签则为List},如果不存在则返回null
   * @throws Exception
   */
  public static Object getSubObjectOfXml(String xml, String objName) throws Exception {
    XMLObjectVisitor visitor = new XMLObjectVisitor();
    convertXml(xml, visitor);
    return visitor.getXmlObj().getSubObject(objName);
  }

  /**
   * 根据设计规则将Map对象转化成xml串
   * 
   * @param map
   *            值对
   * @param rootName
   *            根节点名
   * @return 相应的xml字符串
   * @throws Exception
   */
  public static String convertObjToXml(Map map, String rootName) throws Exception {
    rootName = (rootName == null || rootName.equalsIgnoreCase("")) ? "data" : rootName;
    if (map == null)
      return "<" + rootName + "></" + rootName + ">";
    Document doc = null;
    StringWriter stringwriter = null;
    XMLWriter writer = null;
    OutputFormat format = null;
    try {
      doc = DocumentHelper.createDocument();
      doc.add(constructElement(map, rootName));
      stringwriter = new StringWriter();
      format = OutputFormat.createPrettyPrint();
      format.setEncoding("GBK");
      writer = new XMLWriter(format);
      writer.setWriter(stringwriter);
      writer.write(doc);
      return stringwriter.toString();
    } catch (Exception e) {
      throw new Exception("传入的Map对象不符合要求,无法正常转换成xml串!");
    } finally {
      doc = null;
      stringwriter = null;
      writer = null;
      format = null;
    }
  }

  /**
   * 根据传入的visitor模型执行相应的解析步骤
   * 
   * @param xml
   *            xml文件
   * @param visitor
   *            visitor对象
   * @return Visitor 对象
   * @throws Exception
   *             解析过程中的异常
   */
  public static Visitor convertXml(String xml, Visitor visitor) throws Exception {
    if (visitor == null)
      return null;
    try {
      Document doc = DocumentHelper.parseText(xml);
      Element root = doc.getRootElement();
      root.accept(visitor);
    } catch (Exception e) {
      throw new Exception("无法正常解析,xml中存在非法字符,例如<、>、'等等,请进行检查!");
    }
    return visitor;
  }

  /**
   * 根据传入的visitor模型执行相应的解析步骤
   * 
   * @param filePath
   *            xml文件路径
   * @param visitor
   *            visitor对象
   * @return Visitor 对象
   * @throws Exception
   *             解析过程中的异常
   */
  public static Visitor convertXmlByFile(String filePath, Visitor visitor) throws Exception {
    byte[] byteFile = FileUtil.file2Bytes(filePath);
    String xml = new String(byteFile, "GBK");
    return convertXml(xml, visitor);
  }

  /**
   * 读取文件并形成字符串输出
   * 
   * @param filePath
   *            文件路径
   * @return xml字符串
   * @throws Exception
   *             异常信息
   */
  public static String toXMLStr(String filePath) throws Exception {
    return new String(FileUtil.file2Bytes(filePath), "GBK");
  }

  /**
   * 使用递归的方法进行分解Map,构建对应xml的Element对象
   * 
   * @param map
   *            map值对象
   * @param eleName
   *            元素名
   * @return 元素对象
   */
  protected static Element constructElement(Map map, String eleName) {
    Object[] object = map.keySet().toArray();
    Element element = DocumentHelper.createElement(eleName);
    for (int i = 0; i < object.length; i++) {
      if (map.get(object[i]) instanceof String) {
        element.addAttribute((String) object[i], (String) map.get(object[i]));
      } else if (map.get(object[i]) instanceof Map) {
        element.add(constructElement((Map) map.get(object[i]), (String) object[i]));
      } else if (map.get(object[i]) instanceof List) {
        List ls = (List) map.get(object[i]);
        for (int j = 0; j < ls.size(); j++) {
          element.add(constructElement((Map) ls.get(j), (String) object[i]));
        }
      }
    }
    return element;
  }

  /**
   * dom4j解析xml文件,主要解决xml文件中带dtd并且没有网络时,报org.dom4j.DocumentException:
   * www.springframework.org Nested exception:异常的问题
   * 
   * @param fileName
   *            文件名
   * @return xml文件的Document对象
   * @throws Exception
   * @author zhupeidong at 2008-11-6下午03:30:26
   */
  public static Document parseFileToDocument(String fileName) throws Exception {
    Document doc = null;
    SAXReader saxReader = new SAXReader();
    saxReader.setValidation(false);
    saxReader.setEntityResolver(new EntityResolver() {
      public InputSource resolveEntity(String publicId, String systemId) {
        if (systemId.toLowerCase().endsWith(".dtd")) {
          String s4 = "<?xml version='1.0' encoding='GB2312'?>";
          return new InputSource(new StringReader(s4));
        } else {
          return null;
        }
      }
    });
    byte[] xml = FileUtil.file2Bytes(fileName);
    ByteArrayInputStream stream = new ByteArrayInputStream(xml);
    doc = saxReader.read(stream);
    return doc;
  }
}