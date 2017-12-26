package gov.df.fap.controller.portal;

import gov.df.fap.api.portal.IArticleManagementService;
import gov.df.fap.api.portal.IPaginationService;
import gov.df.fap.bean.portal.ArticleAttachBean;
import gov.df.fap.bean.portal.ArticleRecordBean;
import gov.df.fap.util.portal.Base64;
import gov.df.fap.util.portal.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


/**
 * 
 * @author Liurs
 *
 * 2017-4-11上午9:50:47
 */
@Controller
@RequestMapping(value="/df/portal/ArticleManage")
public class GetArticleDataController {

	@Autowired
	private IPaginationService ips = null;
	@Autowired
	private IArticleManagementService iArticleManagementService = null;
	
//	private File file;

	private String fileName;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@RequestMapping(value="/GetArticleData.do", method = RequestMethod.GET)
	public @ResponseBody PageImpl GetPageJsonData(
			@RequestParam(value = "pageNumber", defaultValue = "1") String pageNumber,
			@RequestParam(value = "tokenid") String tokenid,
//		    @RequestParam(value = "pagesize", defaultValue = "5") int pageSize,
		    HttpServletRequest request, HttpServletResponse response,HttpSession session){
		
//		HttpServletRequestWrapper2 req2 = new HttpServletRequestWrapper2(request);
		String sql="select * from AP_ARTICLE";
		PageImpl pageimpl = null;
//		String tokenid = req2.getParameter("tokenid");
//		int pageNumber = Integer.parseInt(req2.getParameter("pageNumber"));
		int pageNumber2 = Integer.parseInt(Base64.decode(pageNumber));
		int pageSize  =  5;
		
		pageimpl = iArticleManagementService.getPageData(sql,pageNumber2, pageSize);
		return pageimpl;
	}
	@RequestMapping(value="/GetArticleDataById.do", method = RequestMethod.GET)
	public @ResponseBody Object getArticleData(
			@RequestParam(value = "articleId") String articleId,
		    @RequestParam(value = "tokenid") String tokenid,
		    HttpServletRequest request, HttpServletResponse response,HttpSession session){
		
		articleId = Base64.decode(articleId);
		Map<String,String> articleBean = iArticleManagementService.getArticleData(articleId);
		if(articleBean!=null){
			return articleBean;
		}
        return null;
	}
	
	@RequestMapping(value="/DeleteArticleDataById.do", method = RequestMethod.GET)
	public @ResponseBody boolean deleteArticleData(
			@RequestParam(value = "articleId") String articleId,
		    @RequestParam(value = "tokenid") String tokenid,
		    HttpServletRequest request, HttpServletResponse response,HttpSession session){
		
		articleId = Base64.decode(articleId);
		boolean isSuccess = iArticleManagementService.deleteArticleData(articleId);
        return isSuccess;
	}
	
	@RequestMapping(value="/SaveArticleData.do", method = RequestMethod.GET)
	public @ResponseBody boolean saveArticleData(
			@RequestParam(value = "articleId") String articleId,
			@RequestParam(value = "articleTittle") String articleTittle,
			@RequestParam(value = "articleNumber") String articleNumber,
			@RequestParam(value = "articleAuthor") String articleAuthor,
			@RequestParam(value = "createTime") String createTime,
			@RequestParam(value = "picUrl") String picUrl,
			@RequestParam(value = "validateTime") String validateTime,
		    @RequestParam(value = "expireTime") String expireTime,
		    @RequestParam(value = "articleLink") String articleLink,
		    @RequestParam(value = "articleIntroduction") String articleIntroduction,
		    @RequestParam(value = "articleContent") String articleContent,
		    @RequestParam(value = "tokenid") String tokenid,
		    HttpServletRequest request, HttpServletResponse response,HttpSession session){
		String articleTittle1 = Base64.decode(articleTittle);
		String articleAuthor1 =Base64.decode(articleAuthor);
		 try {

			String articleTittle2 = new String(articleTittle1.getBytes("GBK"), "UTF-8");
			articleTittle ="《"+articleTittle2+"》";
			 articleAuthor = new String(articleAuthor1.getBytes("GBK"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		articleTittle = Base64.decode(articleTittle);
		articleAuthor = Base64.decode(articleAuthor);
		articleId = Base64.decode(articleId);
		createTime = Base64.decode(createTime);
		validateTime = Base64.decode(validateTime);
		expireTime = Base64.decode(expireTime);
		boolean isSuccess = iArticleManagementService.saveArticleData(articleId,articleTittle,articleNumber,articleAuthor,
				createTime,picUrl,validateTime,expireTime,articleLink,articleIntroduction,articleContent,tokenid);
        if(isSuccess){
        	return true;
        }
        return false;                                                                                                                                                                                                                                                                                                                                                                                                                                
	}
	
	@RequestMapping(value="/UpLoadArticleAttach.do", method = RequestMethod.GET)
	public @ResponseBody boolean UpLoadArticleAttach(
		    @RequestParam(value = "tokenid") String tokenid,
		    @RequestParam(value = "file", required = false) MultipartFile file,
		    HttpServletRequest request, HttpServletResponse response,HttpSession session){
		
        MultipartHttpServletRequest multipartRequest  =  (MultipartHttpServletRequest) request;  
        
		boolean success = iArticleManagementService.saveArticleAttach(multipartRequest,session);
		
		Cookie c = new Cookie("upLoadPath", "path");
		Cookie c1 = new Cookie("attachId", "attachId");
		response.addCookie(c);
		if (success == false) {
			return false;
		}
        return true;
	}
	
	
	@RequestMapping(value="/getUpLoadArticleAttachData.do", method = RequestMethod.GET)
	public @ResponseBody HashMap<String,String> getUpLoadArticleAttachData(
		    @RequestParam(value = "tokenid") String tokenid,
		    HttpServletRequest request, HttpServletResponse response,HttpSession session){
		
		String upLoadPath = (String) session.getAttribute("upLoadPath");
		String attachId = (String) session.getAttribute("attachId");
		HashMap<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("upLoadPath", upLoadPath);
		resultMap.put("attachId", attachId);
		
        return resultMap;
	}
	
	@RequestMapping(value="/PubArticleData.do", method = RequestMethod.GET)
	public @ResponseBody Boolean pubArticleData(
		    @RequestParam(value = "tokenid") String tokenid,
		    @RequestParam(value = "articleId") String articleId,
		    HttpServletRequest request, HttpServletResponse response,HttpSession session){
		
		// 取参数
		String pgPletId = "16";
		String portletId = "article";
		articleId = Base64.decode(articleId);
		String checkStatus = "0";
		
		return iArticleManagementService.PubArticleData(pgPletId,portletId,articleId,checkStatus);
	}
}
