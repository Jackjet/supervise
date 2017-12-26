package gov.df.fap.util.xml;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;
/**
 * 使用Visitor模式动态遍历xml各组件，根据需求解析xml成通用对象。
 * 对象模型,
 *             /属性
 *             
 *         Map -父子结构则递归保存Map      
 *                   /单个要素节点则存放属性Map           
 *             \子节点
 *                   \多个相同要素节点则存放List
 * 具体参考XMLData类             
 * @version 1.0
 * @author justin
 * @see XMLData
 * @see XMLObjectVisitor#visit(Element)
 * @see XMLObjectVisitor#visit(Attribute)
 * @since java 1.6
 */
public class XMLObjectVisitor extends VisitorSupport
{
	private XMLData data = null;//描述整棵树
	private XMLData lastNodeMap = null;//描述节点
	private String lastNodeName = null;
	private Element root = null;
	private Element lastNode = null;
	private boolean hasPush = false;
	/**
	 * visitor模式,动态加载对象并执行不同的方法体。本方法用来组合成统一的对象
	 * @param element 根据传入xml要素对象执行相应的操作。
	 */
	public void visit(Element element)
	{
		try
		{
			String eleName = element.getName();
			/*将每个节点的属性保存在map对象中*/
			XMLData map = new XMLData();
			Object obj = null;
			for(Iterator it = element.attributeIterator();it.hasNext();)
			{
				Attribute attr = (Attribute)it.next();
				map.put(attr.getName(),attr.getValue());
			}
			if(!element.isRootElement())
			{
				if(element.getParent().equals(root))
				{
				  lastNodeMap = map;
				  lastNodeName = eleName;
				  lastNode = element;
				  hasPush = false;
				}
				else
				{
					if(lastNodeMap != null)
					{
						XMLData parent = null;
						if(element.getParent().equals(lastNode))
						{
							parent = lastNodeMap;
						}
						else
						{
							obj = lastNodeMap.getSubObject(element.getParent().getName());
							if(obj instanceof XMLData)
								parent = (XMLData)obj;
							else if (obj instanceof List) {
								List targetList = (List) obj;
								Object latestObj = targetList.get(targetList.size()-1);
								if (latestObj instanceof XMLData)
									parent = (XMLData) latestObj;
							}
						}
						if(parent!=null)
							parent.put(eleName,map);
					}
				}
			}
			else
			{
				data = map;
				root = element;
			}
		    /*碰到第一个叶节点,将上一个节点结构入栈*/
		    if(lastNodeMap != null && lastNode != null 
		    		&& element.elements().size() == 0 && hasPush == false)
		    {
			  data.put(lastNodeName,lastNodeMap);
			  hasPush = true;
		    }			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * visitor模式,动态加载对象并执行不同的方法体。本方法用来校验各属性
	 * @param attr 传入的xml属性对象
	 */	
	public void visit(Attribute attr)
	{

	}
	/**
	 * 获取转化后对象
	 * @return 转化后的对象
	 */
	public XMLData getXmlObj()
	{
		return data;
	}



}

