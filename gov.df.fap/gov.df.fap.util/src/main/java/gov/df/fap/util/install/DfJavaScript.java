package gov.df.fap.util.install;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;

import com.longtu.framework.install.IJavaScript;


public class DfJavaScript implements IJavaScript{

	private static DataSource datasource;
	private Connection conn;
	public void execute(DataSource datasource,String filePath) throws Exception {
		// TODO Auto-generated method stub
		this.datasource = datasource;
		conn = getConnection();
		filePath = filePath.substring("/".length(), filePath.indexOf("business"));
		List list = getFileList(filePath);
		Iterator it = list.iterator();
		while(it.hasNext()){
			File file = (File) it.next();
			readFileByLine(file,filePath);
		}
	}
	
	private void readFileByLine(File file,String filePath){
		
		System.out.println("开始了readFile");
		BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            System.out.println(reader.readLine());
            while ((tempString = reader.readLine()) != null) {
            	System.out.println(tempString);
            	if ("".equals(tempString.trim())) {
                    return;
                  }
                  int i = tempString.indexOf("#");
                  if ((i > 0) && (tempString.substring(i + 1).indexOf("#") > 0))
                  {
                    ResultSet localResultSet = null;
                    try {
						changeAutoCommit();
					} catch (SQLException e) {
						e.printStackTrace();
					}
                    try
                    {
                      String tableName = getUpdateTableName(tempString);
                      String columnName = getUpdateColumnName(tempString);
                      String condition = getUpdateCondition(tempString);
                      condition = condition.substring(0, condition.indexOf(";"));
                      String[] nameType = getFileNameAndType(tempString);
                      File localFile2;
                      for (;;)
                      {
                        File localFile1 = new File(filePath + "z_lob");
                        localFile2 = new File(localFile1, nameType[0]);
                        if (localFile2.exists()) {
                          break;
                        }
                        return;
                      }
                      if ("long".equals(nameType[1].trim().toLowerCase(Locale.US))) {
                        updateLongField(tableName, columnName, condition, localFile2);
                      } else {
                    	  System.out.println("开始了updateLobField");
                        localResultSet = updateLobField(tableName, columnName, condition, nameType, localFile2);
                      }
                      this.conn.commit();
                      this.conn.clearWarnings();
                    }
                    catch (SQLException localSQLException1)
                    {
                      localSQLException1.printStackTrace();
                    }
                    catch (Throwable localThrowable)
                    {
                    	localThrowable.printStackTrace();
                    }
                    finally
                    {
                      if (localResultSet != null) {
                        try
                        {
                          localResultSet.close();
                        }
                        catch (SQLException localSQLException2) {
                        	localSQLException2.printStackTrace();
                        }
                      }
                    }
                  }
            
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                	e1.printStackTrace();
                }
            }
        }
	}
	
	
	private void updateLongField(String str1,String str2,String str3, File file){
		String sql = "update " + str1 + " set " + str2 + "=?" + " where " + str3;
        PreparedStatement localPreparedStatement = null;
		try {
			localPreparedStatement = this.conn.prepareStatement(sql);
			String fileString = readFileToString(file);
			localPreparedStatement.setCharacterStream(1, new StringReader(fileString), fileString.length());
			localPreparedStatement.execute();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	
	private String readFileToString(File file){
		StringBuffer localStringBuffer = new StringBuffer();
	    BufferedReader localBufferedReader = null;
	    try
	    {
	      localBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
	      String str = null;
	      while ((str = localBufferedReader.readLine()) != null) {
	        localStringBuffer.append(str);
	      }
	    }
	    catch (IOException localIOException) {}finally
	    {
	      try
	      {
	        if (localBufferedReader != null) {
	          localBufferedReader.close();
	        }
	      }
	      catch (Exception localException) {}
	    }
	    return localStringBuffer.toString();
	}

	private ResultSet updateLobField(String tableName, String columnName, String condition, String[] nameType, File file){
		ResultSet localResultSet = null;
		String str = "select " + columnName + " from " + tableName + " where " + condition + " for update";
        PreparedStatement localPreparedStatement;
		try {
			localPreparedStatement = this.conn.prepareStatement(str);
			localResultSet = localPreparedStatement.executeQuery();
	        if (localResultSet.next())
	        {
	          Object localObject;
	          Method localMethod;
	          if ("CLOB".equals(nameType[1].trim().toUpperCase(Locale.US)))
	          {
	            localObject = localResultSet.getClob(1);
	            if (!localObject.getClass().getName().equals("oracle.sql.CLOB"))
	            {
	              localMethod = localObject.getClass().getMethod("getVendorObj", new Class[0]);
	              localObject = localMethod.invoke(localObject, new Object[0]);
	            }
	            localMethod = localObject.getClass().getMethod("getCharacterOutputStream", new Class[0]);
	            writeAsciiBigData(new FileInputStream(file), (Writer)localMethod.invoke(localObject, new Object[0]));
	          }
	          else if ("BLOB".equals(nameType[1].trim().toUpperCase(Locale.US)))
	          {
	            localObject = localResultSet.getBlob(1);
	            if (!localObject.getClass().getName().equals("oracle.sql.BLOB"))
	            {
	              localMethod = localObject.getClass().getMethod("getVendorObj", new Class[0]);
	              localObject = localMethod.invoke(localObject, new Object[0]);
	            }
	            localMethod = localObject.getClass().getMethod("getBinaryOutputStream", new Class[0]);
	            writeBigData(new FileInputStream(file), (OutputStream)localMethod.invoke(localObject, new Object[0]));
	            System.out.println("结束了");
	          }
	          else
	          {
	        	System.out.println("错误字段类型：" + nameType[1]);  
	          }
			
	        }
	        
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return localResultSet;
	}
	
	private void writeBigData(InputStream paramInputStream, OutputStream paramOutputStream)
		    throws IOException
		  {
		    BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramInputStream, 1024);
		    BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(paramOutputStream, 1024);
		    byte[] arrayOfByte = new byte[1024];
		    int i;
		    System.out.println("大字段数据的长度：" + localBufferedInputStream.read(arrayOfByte, 0, 1024));
		    while ((i = localBufferedInputStream.read(arrayOfByte, 0, 1024)) != -1) {
		      localBufferedOutputStream.write(arrayOfByte, 0, i);
		    }
		    close(localBufferedInputStream, localBufferedOutputStream);
		  }
	
	public static void close(InputStream paramInputStream, OutputStream paramOutputStream)
	  {
	    if (paramInputStream != null) {
	      try
	      {
	        paramInputStream.close();
	      }
	      catch (IOException localIOException1)
	      {
	        localIOException1.printStackTrace();
	      }
	    }
	    if (paramOutputStream != null) {
	      try
	      {
	        paramOutputStream.close();
	      }
	      catch (IOException localIOException2)
	      {
	        localIOException2.printStackTrace();
	      }
	    }
	  }
	
	private String getUpdateTableName(String sqlcommand){
		int i = sqlcommand.toLowerCase(Locale.US).trim().indexOf("update ");
        if (i == 0)
        {
          String str = sqlcommand.trim().substring("update ".length()).trim();
          str = str.substring(0, getFirstBlankIndex(str));
          return str;
        }
        return null;
	}
	
	private void writeAsciiBigData(InputStream paramInputStream, Writer paramWriter)
		    throws IOException
		  {
		    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream, "utf-8"), 1024);
		    BufferedWriter localBufferedWriter = new BufferedWriter(paramWriter, 1024);
		    char[] arrayOfChar = new char['?'];
		    int i;
		    while ((i = localBufferedReader.read(arrayOfChar, 0, 1024)) != -1) {
		      localBufferedWriter.write(arrayOfChar, 0, i);
		    }
		    if (localBufferedReader != null) {
		      localBufferedReader.close();
		    }
		    if (localBufferedWriter != null) {
		      localBufferedWriter.close();
		    }
		  }
	
	private int getFirstBlankIndex(String paramAnonymousString)
    {
      int i = paramAnonymousString.indexOf(" ") > -1 ? paramAnonymousString.indexOf(" ") : paramAnonymousString.length();
      int j = paramAnonymousString.indexOf("\t") > -1 ? paramAnonymousString.indexOf("\t") : paramAnonymousString.length();
      return Math.min(i, j);
    }
	
	private String getUpdateColumnName(String sqlcommand){
		
		int i = sqlcommand.toLowerCase(Locale.US).trim().indexOf(" set ");
        if (i > 0)
        {
          String str = sqlcommand.trim().substring(i + " set ".length()).trim();
          return str.substring(0, str.indexOf("=")).trim();
        }
        return null;
	}
	
	private String[] getFileNameAndType(String sqlcommand){
		
		int i = sqlcommand.trim().indexOf("#");
        String str = sqlcommand.trim().substring(i + "#".length());
        int j = str.indexOf("#");
        if (j < 0) {
          return null;
        }
        String[] arrayOfString = str.substring(0, j).trim().split(":");
        if (arrayOfString != null) {
          for (int k = 0; k < arrayOfString.length; k++) {
            arrayOfString[k] = arrayOfString[k].trim();
          }
        }
        return arrayOfString;
	}

	private String getUpdateCondition(String sqlcommand){
	
		int i = sqlcommand.toLowerCase(Locale.US).trim().indexOf(" where ");
        if (i > 0) {
          return sqlcommand.trim().substring(i + " where ".length()-";".length()).trim();
        }
        return null;
	}
	
	private void changeAutoCommit() throws SQLException
	      {
	        this.conn.setAutoCommit(false);
	      }
	
	private List getFileList(String filePath){
		List fileList = new ArrayList();
		LinkedList<File> list = null;
		File file = new File(filePath);
		if(file.exists()){
			list = new LinkedList<File>();
			File[] files = file.listFiles();
			for(File file2:files){
				if (file2.isDirectory()) {
                    list.add(file2);
                } else {
                	fileList.add(file2);
                }
			}
			File temp_file;
            while (!list.isEmpty()) {
                temp_file = list.removeFirst();
                if(temp_file.getName().equals("z_lob")||temp_file.getName().equals("business")){
                	continue;
                }
                files = temp_file.listFiles();
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        list.add(file2);
                    } else {
                        fileList.add(file2);
                    }
                }
            }
		}else {
            System.out.println("大字段文件不存在!");
        }
		return fileList;
	}
	
	
	public static Connection getConnection(){
		
		try {
			return datasource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	

}
