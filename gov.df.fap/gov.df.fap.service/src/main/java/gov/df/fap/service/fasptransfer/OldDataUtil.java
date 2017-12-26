package gov.df.fap.service.fasptransfer;
 

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.longtu.framework.util.ServiceFactory;

public class OldDataUtil {
	public static List<Map> getData(String sql) throws SQLException{
		
	 DataSource ds = (DataSource)ServiceFactory.getBean("olddatasource");
	Connection conn = ds.getConnection();
    PreparedStatement ps = conn.prepareStatement(sql);
    
    ResultSet rs = ps.executeQuery();
    List<Map> result = new ArrayList<Map>();
    Map map = null;
    for (; rs.next(); result.add(map))
      map = getHashMapByResultset(rs);

    rs.close();
    ps.close();
    conn.close();
    return result;
	}
	 private static Map getHashMapByResultset(ResultSet rs) {
		    Map hm = new HashMap();
		    try {
		      ResultSetMetaData rsMeta = rs.getMetaData();
		      int i = rsMeta.getColumnCount();
		      String sColumName = null;
		      String value = null;
		      for (; i > 0; i--) {
		        int columnType = rsMeta.getColumnType(i);
		        sColumName = rsMeta.getColumnName(i).toLowerCase();
		        int columScale = rsMeta.getScale(i);
		        // number类型必须特殊处理,否则如果直接getString,对于小数将无法正确显示
		        // 如0.1将显示为.1
		        if (columnType == Types.NUMERIC) {
		          value = String.valueOf(rs.getBigDecimal(sColumName));
		          // 李文全增加，2009－03－04 解决number(xx,2)时返回"null.00" 的问题
		          if (value == null || value.equals("null") || value.equals("")) {
		            value = "";
		          } else {
		            if (columScale > 0) {
		              if (value.indexOf(".") == -1) {
		                value += ".";
		                for (int j = 0; j < columScale; j++) {
		                  value += "0";
		                }
		              } else {
		                // 补零的个数
		                int length = columScale - (value.length() - value.indexOf(".")) + 1;
		                for (int j = 0; j < length; j++) {
		                  value += "0";
		                }
		              }
		            }
		          }
		        } else if (columnType == Types.CLOB) {
		          //暂时不处理BLOB、CLOB类型
		          value = clob2String(rs.getClob(sColumName));
		        } else if (columnType == Types.BLOB) {
		          value = blob2String(rs.getBlob(sColumName));
		        } else {
		          value = rs.getString(sColumName);
		        }


		        hm.put(sColumName, value);

		      }
		    } catch (SQLException e) {
		      e.printStackTrace();
		    }
		    return hm;
		  }

		  public Map resultSet2Map(ResultSet rs) {
		    if (rs == null) {
		      return null;
		    }
		    Map map = null;
		    ResultSetMetaData meta;
		    try {
		      meta = rs.getMetaData();
		      int count = meta.getColumnCount();
		      map = new HashMap();

		      for (int i = 1; i <= count; i++) {
		        if (rs.getObject(i) != null) {
		          map.put(meta.getColumnName(i), rs.getObject(i));
		        }
		      }

		    } catch (SQLException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		    }

		    return map;
		  }
		  /**
		   * 解析数据库返回的clob字段类型
		   * @param clob
		   * @return
		   */
		  public final static String clob2String(Clob clob) {
		    if (clob == null) {
		      return null;
		    }
		    StringBuffer sb = new StringBuffer(65535);//64K 
		    Reader clobStream = null;//创建一个输入流对象 
		    try {
		      clobStream = clob.getCharacterStream();
		      char[] b = new char[60000];//每次获取60K 
		      int i = 0;
		      while ((i = clobStream.read(b)) != -1) {
		        sb.append(b, 0, i);
		      }
		    } catch (Exception ex) {
		      sb = null;
		    } finally {
		      try {
		        if (clobStream != null)
		          clobStream.close();
		      } catch (Exception e) {
		      }
		    }
		    if (sb == null)
		      return null;
		    else
		      return sb.toString();
		  }

		  /**
		   * 解析数据库返回的blob字段类型
		   * @param blob
		   * @return
		   */
		  public final static String blob2String(Blob blob) {
		    String result = "";
		    try {
		      if (blob != null) {
		        StringBuffer buffer = new StringBuffer();
		        InputStream is = null;
		        is = blob.getBinaryStream();
		        InputStreamReader isr = new InputStreamReader(is);
		        if (isr.ready()) {
		          Reader reader = new BufferedReader(isr);
		          int ch;
		          while ((ch = reader.read()) > -1) {
		            buffer.append((char) ch);
		          }
		        }
		        isr.close();
		        is.close();
		        result = buffer.toString();
		      }
		    } catch (Exception e) {
		      System.err.println("error : " + e.getMessage());
		      return result;
		    }
		    return result;
		  }
}
