package gov.df.fap.util.xml;

import java.util.Map;
import java.util.StringTokenizer;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;

/**
 * 使用Visitor模式动态遍历xml各组件，根据需求解析xml成对应SQL。
 *   错误码        二进制           错误描述
 *    1      0000000000000001   未正确设置操作对应表表名
 *    1<<1   0000000000000010   查询多表时请添加外键描述
 *    1<<2   0000000000000100   插入、删除、修改只能针对单表进行
 *    1<<3   0000000000001000   未正确设置插入、修改赋值字段名 
 *    1<<4   0000000000010000   未正确设置插入、修改字段值    
 *    1<<5   0000000000100000   未正确设置过滤条件对应编码
 *    1<<6   0000000001000000   未正确设置过滤条件对应值
 *    1<<7   0000000010000000   未正确设置自定义显示字段
 *    1<<8   0000000100000000   预留
 *    1<<9   0000001000000000   预留
 *    1<<10  0000010000000000   预留
 *    1<<11  0000100000000000   预留
 * 错误机制,使用二进制位运算,使用|组合各错误,使用&解析各错误。
 * @version 1.0
 * @author justin
 * @see XMLSQLVisitor#visit(Element)
 * @see XMLSQLVisitor#visit(Attribute)
 * @since java 1.6
 */
public class XMLSQLVisitor extends VisitorSupport
{
	private StringBuffer strSQL = new StringBuffer();
	private StringBuffer condSQL = new StringBuffer();
	private StringBuffer displaySQL = new StringBuffer();
	private StringBuffer fieldStr = new StringBuffer();
	private StringBuffer fieldValueStr = new StringBuffer();
	private StringBuffer fieldAndValue = new StringBuffer();
	/**设置操作附加字段(insert or update),主要满足处理部分默认字段用*/
	private Map operaAddition = new XMLData();
	boolean hasAddAddition = false;
	
	private String sql_type = "query";	
	private int error = 0;
	private String page_index = "0";
	private String page_count = "100";
	private String table_name = "";
	private String display_type = "1";
	private boolean isMultiTable = false;
	private String fpk_desc = "";
	

	/**
	 * visitor模式,动态加载对象并执行不同的方法体。本方法用来组合成不同的SQL
	 * @param element 根据传入xml要素对象执行相应的操作。
	 */
	public void visit(Element element)
	{
		String eleName = element.getName();
		if(eleName.equalsIgnoreCase("data"))
		{
			/*校验固定条件下某些必须存在的元素标签*/
			if((sql_type.equalsIgnoreCase("insert")
			  ||sql_type.equalsIgnoreCase("modify"))
			  &&element.elements("field").size() == 0)
			{
				error |= 1<<3;
			}
			table_name = element.attributeValue("table_name");
			if(table_name == null
			  ||table_name.equalsIgnoreCase(""))
			{
				error |= 1;
			}
			else
			{
				StringTokenizer token = new StringTokenizer(table_name,",");
				if(token.countTokens()>1)isMultiTable = true;
				token = null;
			}
			fpk_desc = element.attributeValue("fpk_desc");
			if(isMultiTable)
			{
				if(sql_type.equalsIgnoreCase("query"))
				{
				  if(fpk_desc == null || fpk_desc.equalsIgnoreCase(""))
				  {
				    error |= 1<<1;
				    return;
				  }
				}
				else
				{
					error |= 1<<2;
					return;
				}
			}
			page_index = element.attributeValue("page_index");
			page_index = (page_index == null || page_index.equalsIgnoreCase(""))
													?"0":page_index.trim();
			page_count = element.attributeValue("page_count");
			page_count = (page_count ==null || page_count.equalsIgnoreCase(""))
													?"100":page_count.trim();
			display_type = element.attributeValue("display_type");
			display_type = (display_type ==null || display_type.equalsIgnoreCase(""))
													?"1":display_type.trim();												
		}
		else if(eleName.equalsIgnoreCase("field"))
		{
			String field_code = element.attributeValue("field_code");
			String field_value = element.attributeValue("field_value");
			if(field_code == null
			   ||field_code.equalsIgnoreCase(""))
			{
				error |= 1<<3;
				return;
			}
			field_code = field_code.trim();
			field_value = field_value==null?"":field_value.trim();
			fieldAndValue.append(transferField(field_code,field_value));
			fieldStr.append(field_code).append(",");
			if(field_value == null || field_value.equalsIgnoreCase(""))
			{
				fieldValueStr.append("null,");
			}
			else
			{
				fieldValueStr.append("'").append(field_value).append("',");
			}
		}
		else if(eleName.equalsIgnoreCase("par"))
		{
			String par_code = element.attributeValue("par_code");
			String par_value = element.attributeValue("par_value");
			String t_name = element.attributeValue("table_name");
			if(par_code==null
			  ||par_code.equalsIgnoreCase(""))
			{
				error |= 1<<5;
				return;
			}
			if(par_value==null
			   ||par_value.equalsIgnoreCase(""))
			{
				error |= 1<<6;
				return;
			}
			par_code = par_code.trim();
			par_value = par_value.trim();
			String compare_type = element.attributeValue("compare_type");
			compare_type = (compare_type==null||compare_type.equalsIgnoreCase(""))
			                                         ?"=":compare_type.trim();
			condSQL.append(transferPar(t_name,par_code,par_value,compare_type));
		}
		else if(eleName.equalsIgnoreCase("col")
				&&element.getParent().getName().equalsIgnoreCase("display"))
		{
			if(sql_type.equalsIgnoreCase("query"))
			{
				if(display_type.equalsIgnoreCase("2"))
				{
					String col_code = element.attributeValue("col_code");
					String col_field = element.attributeValue("col_field");
					String t_name = element.attributeValue("table_name");
					if(col_field == null
					   ||col_field.equalsIgnoreCase(""))
					{
						error |= 1<<7;
						return;
					}
					col_code = col_code==null?"":col_code.trim();
					col_field = col_field.trim();			  
				    displaySQL.append(transferCol(t_name,
					   			          	      col_code,
								          	      col_field));
				}
			}
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
	 * 根据解析,将xml组合成用户所需的SQL,如果存在错误,则返回"";
	 * @return SQL字符串
	 */
	public String getTotalSQL()
	{
		if(sql_type.equalsIgnoreCase("query"))
		{
		   return strSQL.append("select distinct ").append(getDisplaySQL())
		                .append(" from ").append(table_name).append(" where ")
				        .append(isMultiTable?fpk_desc:"1=1 ")
				        .append(condSQL).toString();
		}
		else if(sql_type.equalsIgnoreCase("delete"))
		{
		  return strSQL.append("delete from ").append(table_name)
			           .append(" where 1=1 ")
			           .append(condSQL).toString();
		}
		else if(sql_type.equalsIgnoreCase("insert"))
		{
		  if(!hasAddAddition)transferAddition();
 		  return strSQL.append("insert into ").append(table_name)
		  			   .append(" (")
		               .append(getFieldNameSQL())
		               .append(") values(")
		               .append(getFieldValueSQL())
		               .append(")").toString();
		}
		else if(sql_type.equalsIgnoreCase("modify"))
		{
		  if(!hasAddAddition)transferAddition();
		  return strSQL.append("update ").append(table_name)
		               .append(" set ")
		               .append(getFieldSetValueSQL())
			           .append(" where 1=1 ")
			           .append(condSQL).toString();
		}
		return "";
	}
	/**
	 * 获取错误编码
	 * @return 错误编码
	 */
	public int getError()
	{
		return this.error;
	}
	/**
	 * 根据错误码还原错误信息
	 * @param error 错误码
	 * @return 错误信息
	*/
	public String parseError(int error)
	{
	  StringBuffer errMsg = new StringBuffer();
	  int i=0;
	  while(i<12)
	  {
	   switch(error&(1<<i))
	   {
		  case 1:
		 	  errMsg.append("未正确设置操作对应表表名;");
			  break;
		  case 1<<1:
			  errMsg.append("查询多表时请添加外键描述;");
		      break;
		  case 1<<2:
			  errMsg.append("插入、删除、修改只能针对单表进行;");
		      break;
		  case 1<<3:
			  errMsg.append("未正确设置插入、修改赋值字段名;");
		      break;
		  case 1<<4:
			  errMsg.append("未正确设置插入、修改字段值;");
		      break;
		  case 1<<5:
			  errMsg.append("未正确设置过滤条件对应编码;");
		      break;
		  case 1<<6:
			  errMsg.append("未正确设置过滤条件对应值;");
		      break;
		  case 1<<7:
			  errMsg.append("未正确设置自定义显示字段;");
		      break;
		  case 1<<8:
			  errMsg.append("");
		      break;
		  case 1<<9:
			  errMsg.append("");
		      break;
		  case 1<<10:
			  errMsg.append("");
		      break;
		  case 1<<11:
			  errMsg.append("");
		  	  break;
		  default:
			  break;
		 }
		 i++;
	   }
	  return errMsg.toString();
	}	
	/**
	 * 设置操作附加字段(insert or update),主要满足处理部分默认字段用
	 * @param operaAddition
	 */
	public void setOperaAddition(Map operaAddition)
	{
		if(operaAddition != null)
		{
		  this.operaAddition = operaAddition;
		}
	}
	/**
	 * 获取SQL类型
	 * @return SQL类型
	 */
	public String getSqlType()
	{
		return this.sql_type;
	}
	/**
	 * 设置SQL类型
	 * @param sql_type 对应的sql类型,分为"query"、"insert"、"modify"、"delete"
	 */
	public void setSqlType(String sql_type)
	{
		this.sql_type = sql_type;
	}
	/**
	 * 获取设定好的当前页信息
	 * @return 当前页编码
	 */
	public int getPageIndex()
	{
		return Integer.parseInt(page_index);
	}
	/**
	 * 获取设定好的页面大小信息
	 * @return 页面大小
	 */
	public int getPageCount()
	{
		return Integer.parseInt(page_count);
	}
	/**
	 * 获取过滤条件部分对应的SQL
	 * @return 过滤条件部分对应的SQL
	 */
	public String getParSQL()
	{
		return condSQL.toString();
	}
	/**
	 * 获取列表显示部分对应的SQL
	 * @return 列表显示字段SQL
	 */
	public String getDisplaySQL()
	{
	   if(display_type.equalsIgnoreCase("2"))
	   {
		 return displaySQL.length()>0
		  	          ?displaySQL.deleteCharAt(displaySQL.length()-1).toString()
					   :"*";
	   }
	   else
	   {
		  return "*";
	   }
	}
	/**
	 * 获取修改时对应的赋值SQL
	 * @return 赋值SQL
	 */
	public String getFieldSetValueSQL()
	{
		return fieldAndValue.length()>0
			   ?fieldAndValue.deleteCharAt(fieldAndValue.length()-1).toString()
			   :"";
	}
	/**
	 * 获取插入时候对应的字段名列SQL
	 * @return 字段名列表SQL
	 */
	public String getFieldNameSQL()
	{
		return fieldStr.length()>0
		       ?fieldStr.deleteCharAt(fieldStr.length()-1).toString()
		       :"";
	}
	/**
	 * 获取插入时候对应的值列SQL
	 * @return 值列表SQL
	 */
	public String getFieldValueSQL()
	{
		return fieldValueStr.length()>0 
		       ?fieldValueStr.deleteCharAt(fieldValueStr.length()-1).toString()
		       :"";
	}
	/**
	 * 根据既定格式,转化field标签成为插入、修改赋值字段字符串
	 * @param field_code 字段名
	 * @param field_value 字段值
	 * @return 插入、修改赋值字段字符串
	 */
	protected String transferField(String field_code,String field_value)
	{
		StringBuffer result = new StringBuffer();
		field_code = field_code.trim();
		String first_value = field_value.trim();
		StringTokenizer token = new StringTokenizer(field_value,",");
		if(token.countTokens()>1)
		{
			first_value = token.nextToken();
		}
		token = null;
		result.append(field_code);
		if(first_value.equalsIgnoreCase(""))
		{
			result.append("=null,");
		}
		else
		{
			result.append(" = '").append(first_value).append("',");
		}
		/**判断字段是否和附加值对operaAddition对象中重复*/
		if(operaAddition.containsKey(field_code))
		{
			operaAddition.remove(field_code);
		}
		return result.toString();
	}
	/**
	 * 将附加操作值对拆分成相应的字符串
	 */
	protected void transferAddition()
	{
		Object[] object = operaAddition.keySet().toArray();
		for(int i=0;i<object.length;i++)
		{
			fieldStr.append(object[i]).append(",");
			fieldValueStr.append(operaAddition.get(object[i])==null||"".equals(operaAddition.get(object[i]))
					              ?"null,":"'"+operaAddition.get(object[i])+"',");
			fieldAndValue.append(object[i]).append("=")
			             .append(operaAddition.get(object[i])==null||"".equals(operaAddition.get(object[i]))
			            		  ?"null,":"'"+operaAddition.get(object[i])+"',");
		}
		hasAddAddition = true;
	}
	/**
	 * 根据既定格式,转化col标签成为查询自定义显示字段字符串
	 * @param table_name 表名
	 * @param col_code 显示列名
	 * @param col_field 实际查询列
	 * @return 显示字段字符串
	 */
	protected String transferCol(String table_name,String col_code,String col_field)
	{
		StringBuffer colStr = new StringBuffer();
		table_name = (table_name==null||table_name.equals(""))?"":table_name+".";
		/**如果col_field模式为(xxx)+yyy则需要进行解析*/
		if(col_field.matches("\\s*[(]\\s*[^)]*\\s*[)]\\s*[+]\\s*.*"))
		{
			if(col_field.indexOf('(')<col_field.indexOf(')')-1)
			{
				String value = col_field.substring(col_field.indexOf('(')+1,
					           col_field.indexOf(')')).trim();
				if(!value.equalsIgnoreCase(""))
				{
				  colStr.append("'('||").append(table_name).append(value).append("||')'||");
				}
			}
			if(col_field.indexOf(')')+2 < col_field.length())
			{
				String value = col_field.substring(col_field.indexOf(')')+2).trim();
				if(!value.equalsIgnoreCase(""))
				{
			      colStr.append(table_name).append(value);
				}
			}
		    colStr.append((col_code==null||col_code.equalsIgnoreCase(""))
			    	              ?"":(" as "+col_code));		
		}
		else
		{
			colStr.append(table_name).append(col_field)
			      .append((col_code==null||col_code.equalsIgnoreCase(""))
					                         ?"":(" as "+col_code));
		}
		return colStr.append(",").toString();
	}
	/**
	 * 根据既定格式,将par标签转化成过滤条件字符串
	 * @param table_name 对应表名
	 * @param par_code 条件字段
	 * @param par_value 条件值
	 * @param compare_type 比较方式
	 * @return 过滤条件字符串
	 */
	protected String transferPar(String table_name,String par_code,String par_value,String compare_type)
	{
		StringBuffer condStr = new StringBuffer();
		String max = "";
		String min = "";
		par_code = par_code.trim();
		par_value = par_value.trim();
		compare_type = compare_type.trim();
		table_name = (table_name==null || table_name.equals(""))?"":table_name+".";		
		boolean hasMoreValue = false;
		String valueStr = "";
		StringTokenizer token = new StringTokenizer(par_value,",");
		if(token.countTokens()>1)hasMoreValue = true;
		while(token.hasMoreTokens())
		{
			String temp = token.nextToken().trim();
			if(max.equalsIgnoreCase("")
			  ||min.equalsIgnoreCase(""))
			{
				max = temp;
				min = temp;
			}
			else
			{
				if(max.compareTo(temp)< 0)
				{
					max = temp;
				}
				if(min.compareTo(temp)>0)
				{
					min = temp;
				}
			}
			valueStr += "'"+temp+"',";
			
		}
		valueStr = valueStr.substring(0,valueStr.length()-1);
		token = null;
		if(compare_type.equalsIgnoreCase("/"))
		{
			if(hasMoreValue)
			{
				condStr.append(" and ").append(table_name).append(par_code)
				       .append(" > '").append(max).append("'");
			}
			else
			{
				condStr.append(" and ").append(table_name).append(par_code)
			       .append(" > ").append(valueStr);				
			}
		}
		else if(compare_type.equalsIgnoreCase("/="))
		{
			if(hasMoreValue)
			{
				condStr.append(" and ").append(table_name).append(par_code)
			       .append(" >= '").append(max).append("'");
			}
			else
			{
				condStr.append(" and ").append(table_name).append(par_code)
			       .append(" > ").append(valueStr);				
			}			
		}
		else if(compare_type.equalsIgnoreCase("="))
		{
			if(hasMoreValue)
			{
				condStr.append(" and ").append(table_name).append(par_code)
			       .append(" in (").append(valueStr).append(")");				
			}
			else
			{
				condStr.append(" and ").append(table_name).append(par_code)
			       .append(" = ").append(valueStr);
			}			
		}
		else if(compare_type.equalsIgnoreCase("\\"))
		{
			if(hasMoreValue)
			{
				condStr.append(" and ").append(table_name).append(par_code)
			       .append(" < '").append(min).append("'");				
			}
			else
			{
				condStr.append(" and ").append(table_name).append(par_code)
			       .append(" < ").append(valueStr);
			}			
		}
		else if(compare_type.equalsIgnoreCase("\\="))
		{
			if(hasMoreValue)
			{
				condStr.append(" and ").append(table_name).append(par_code)
			       .append(" <= '").append(min).append("'");
			}
			else
			{
				condStr.append(" and ").append(table_name).append(par_code)
			       .append(" <= ").append(valueStr);
			}			
		}
		else if(compare_type.equalsIgnoreCase("!="))
		{
			if(hasMoreValue)
			{
				condStr.append(" and ").append(table_name).append(par_code)
			       .append(" not in (").append(valueStr).append(")");
			}
			else
			{
				condStr.append(" and ").append(table_name).append(par_code)
			       .append(" <> ").append(valueStr);
			}			
		}
		else if(compare_type.equalsIgnoreCase("in"))
		{
			condStr.append(" and ").append(table_name).append(par_code)
		       .append(" in (").append(valueStr).append(")");
		}
		else if(compare_type.equalsIgnoreCase("not in"))
		{
			condStr.append(" and ").append(table_name).append(par_code)
		       .append(" not in (").append(valueStr).append(")");
		}
		else if(compare_type.equalsIgnoreCase("or"))
		{
			if(hasMoreValue)
			{
				condStr.append(" or ").append(table_name).append(par_code)
			       .append(" in (").append(valueStr).append(")");
			}
			else
			{
				condStr.append(" or ").append(table_name).append(par_code)
			       .append(" = ").append(valueStr);
			}
		}
		else if(compare_type.equalsIgnoreCase("like"))
		{
			if(hasMoreValue)
			{
				condStr.append(" and ").append(table_name).append(par_code)
			       .append(" in (").append(valueStr).append(")");
			}
			else
			{
				condStr.append(" and ").append(table_name).append(par_code)
			       .append(" like '").append(par_value).append("%'");
			}
			
		}
		return condStr.toString();
	}
}
