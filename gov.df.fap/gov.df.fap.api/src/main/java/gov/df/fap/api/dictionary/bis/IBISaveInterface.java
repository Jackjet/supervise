package gov.df.fap.api.dictionary.bis;

import gov.df.fap.util.xml.XMLData;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IBISaveInterface {

  public Map getSaveData(HttpServletRequest request, HttpServletResponse response) throws Exception;

  public Map checkBis(HttpServletRequest request, HttpServletResponse response) throws Exception;

  public Map getEleTreeFile(HttpServletRequest request, HttpServletResponse response);

  public Map selectBis(HttpServletRequest request, HttpServletResponse response);

  public Map initPage(HttpServletRequest request, HttpServletResponse response);
  
  public Map pageChange(HttpServletRequest request, HttpServletResponse response);
  public XMLData getBisAdd() throws Exception;
  
}
