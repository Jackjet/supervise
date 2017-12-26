package gov.df.fap.util.file2html;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * txt文本转HTML文件支持GBK，UTF-8编码
 *
 */
public class Txt2HtmlUtil {

  public static Map<String, String> txt2Html(String filePath, String rootPath) throws Exception {
    Map<String, String> map = new HashMap<String, String>();
    File srcfile = new File(filePath);
    String fileName1 = srcfile.getName();
    final long timeStamp = System.currentTimeMillis();
    // *.html 目标文件,添加时间戳，防止多人同时写一个文件
    String targetFileName = rootPath + fileName1 + File.separator;
    File targetFile = new File(targetFileName);
    if (!targetFile.exists()) {
      targetFile.mkdirs();
    }
    String fileName = targetFileName + ".html";
    try {
      boolean result = isUTF8(filePath);
      InputStreamReader read = null;
      File file = new File(filePath);
      if (file.isFile() && file.exists()) { // 判断文件是否存在
        if (result) {
          read = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
        } else {
          read = new InputStreamReader(new FileInputStream(filePath), "GBK");
        }
        // 考虑到编码格式
        BufferedReader bufferedReader = new BufferedReader(read);
        // 写文件
        FileOutputStream fos = new FileOutputStream(new File(fileName));
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        BufferedWriter bw = new BufferedWriter(osw);
        String lineTxt = null;
        while ((lineTxt = bufferedReader.readLine()) != null) {
          bw.write(lineTxt + "</br>");
        }
        bw.close();
        osw.close();
        fos.close();
        read.close();
        String htmlString = Word2HtmlUtil.getString(fileName);
        map.put("htmlString", htmlString);
        map.put("fileName", fileName1);
      } else {
        throw new Exception("找不到指定的文件:" + filePath);
      }
    } catch (Exception e) {
      throw new Exception("读取文件内容出错" + e.getMessage());
    }
    return map;
  }

  /**
   * 判断txt文本是否是utf-8编码
   * @param filePath
   * @return
   * @throws Exception
   */
  public static boolean isUTF8(String filePath) throws Exception {
    boolean isUTF8 = true;
    File file = new File(filePath);
    InputStream in = new FileInputStream(file);
    byte[] b = new byte[3];
    in.read(b);
    in.close();
    //编码为UTF-8
    if (b[0] == -17 && b[1] == -69 && b[2] == -65) {
      isUTF8 = true;
    } else {
      isUTF8 = false;
    }
    return isUTF8;
  }

}
