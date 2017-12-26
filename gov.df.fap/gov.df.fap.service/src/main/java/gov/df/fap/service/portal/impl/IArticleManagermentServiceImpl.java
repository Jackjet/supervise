package gov.df.fap.service.portal.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.opensymphony.xwork2.util.TextUtils;


import gov.df.fap.api.portal.IArticleManagementService;
import gov.df.fap.api.portal.IPaginationService;
import gov.df.fap.bean.portal.ArticleAttachBean;
import gov.df.fap.bean.portal.ArticleRecordBean;
import gov.df.fap.service.portal.dao.BaseDao;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.portal.StringUtils;

/**
 * 
 * @author Liurs
 *
 * 2017-4-2下午1:02:20
 */
@Service
public class IArticleManagermentServiceImpl implements IArticleManagementService {

	/**
	 * 日志对象
	 */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private BaseDao baseDao; 
	
	@Autowired
	private IPaginationService ips;
	
	@Override
	public ArticleRecordBean getArticleRecordBeanFromO() {
		// TODO Auto-generated catch block
		return null;
	}

	@Override
	public PageRequest buildPageRequest(int pageNumber, int pageSize) {
		Sort sort = null;
        List<String> orders = new ArrayList<String>();
       
     //   	orders.add("VALID_DATE");
            orders.add("ID");
            sort = new Sort(Direction.DESC, orders);
        return new PageRequest(pageNumber - 1, pageSize, sort);	
	}

	/**
	 * 
	 */
	@Override
	public List<Map<String,String>> changePageFormat(
			List<Map<String, Object>> map_list) {
		List<Map<String,String>> ARList = new ArrayList<Map<String,String>>();
    	
    	try {
			for(Map<String, Object> temp : map_list){
				Map<String,String> artB = new HashMap<String,String>();
				artB.put("id",temp.get("ID").toString());
				if(StringUtil.isNull(temp.get("TITLE"))){
					
					artB.put("title","");
				}else{
					artB.put("title",temp.get("TITLE").toString());
				}
				if(StringUtil.isNull(temp.get("AUTHOR"))){
					
					artB.put("author","");
				}else{
					artB.put("author",temp.get("AUTHOR").toString());
				}
				if(StringUtil.isNull(temp.get("VALID_DATE"))){
					
					artB.put("pubStatus","未发布");
					artB.put("validateTime","");
				}else{
					artB.put("pubStatus","已发布");
					artB.put("validateTime",temp.get("VALID_DATE").toString());
				}
				if(StringUtil.isNull(temp.get("EXPIRE_DATE"))){
					
					artB.put("expiredTime","");
				}else{
					artB.put("expiredTime",temp.get("EXPIRE_DATE").toString());
				}
				if(StringUtil.isNull(temp.get("SNO"))){
					
					artB.put("articleNo","");
				}else{
					artB.put("articleNo",temp.get("SNO").toString());
				}
				if(StringUtil.isNull(temp.get("CREATOR"))){
					artB.put("creater","");
				}else{
					artB.put("creater",temp.get("CREATOR").toString());
					
				}
				if(StringUtil.isNull(temp.get("CREATE_TIME"))){
					artB.put("createTime","");
				}else{
					artB.put("createTime",temp.get("CREATE_TIME").toString());
				}
			//	Map m = new HashMap();
			//	m.put("ArticleRecordBean",artB);
				ARList.add(artB);
			//	Map m1 = new HashMap();
				
			}
		} catch (Exception e) {
			logger.error("获取文章管理列表失败！"+e.getMessage(),e);
		}
    	return ARList;
	}


	@Override
	public boolean saveArticleData(String articleId,String articleTittle, String articleNumber,
			String articleAuthor, String createTime, String picUrl,
			String validateTime, String expireTime, String articleLink,
			String articleIntroduction, String articleContent, String tokenid) {
		ArticleRecordBean params = new ArticleRecordBean();
//		params.put("id", value)
		try {
			//baseDao.insert("insertArticle", params );
			params.setCreater("sa");
			params.setTitle(articleTittle);
			params.setArticleNo(articleNumber);
			params.setAuthor(articleAuthor);
			params.setCreateTime(StringUtils.getDate(createTime, null));
			params.setImgUrl(picUrl);
			params.setValidateTime(StringUtils.getDate(validateTime, null));
			params.setExpiredTime(StringUtils.getDate(expireTime, null));
			params.setReview(articleIntroduction);
			params.setHref(articleLink);
			params.setContent(articleContent);
			params.setMendTime(StringUtils.getDate(createTime, null));
			params.setArticleType("");
			params.setMendor(articleAuthor);
			
			int id = 0;
			if("0".equals(articleId)){
				
				id = (Integer)baseDao.queryForObject("portal-article.getArticleCount");
				id = id +1 ;
				params.setId(String.valueOf(id));
				baseDao.insert("portal-article.insertArticle", params);
			}else{
				id = Integer.valueOf(articleId);
				params.setId(String.valueOf(id));
				baseDao.update("portal-article.updateByPrimaryKeyWithBLOBs", params);
			}
			//id = String.valueOf(Integer.parseInt(id)+1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	private String charsetChange(String str){
		
		String result = null;
		if(str!=null&&!"".equals(str)){
			
			try {
				byte[] b=str.getBytes("ISO-8859-1");//用tomcat的格式（iso-8859-1）方式去读。
				result = new String(b,"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}//采用utf-8去接string
		}else{
			return "";
		}
		return result;
	}
	@Override
	public Map<String,String> getArticleData(String articleId) {
		Map params = new HashMap();
		params.put("id", articleId);
		ArticleRecordBean articleBean = new ArticleRecordBean();
		Map<String,String> Result = new HashMap<String,String>();
		try {
			articleBean = (ArticleRecordBean) baseDao.queryForObject("portal-article.selectByPrimaryKey", params);
	        if(null!=articleBean){
	        	Result.put("title",articleBean.getTitle());
	        	Result.put("author",articleBean.getAuthor());
	        	Result.put("articleNo",articleBean.getArticleNo());
	        	if(articleBean.getCreateTime()!=null&&""!=articleBean.getCreateTime().toString()){
	        		
	        		Result.put("createTime",StringUtils.getFormatDate(articleBean.getCreateTime()));
	        	}else{
	        		Result.put("createTime","");
	        	}
	        	Result.put("imgUrl",articleBean.getImgUrl());
	        	if(articleBean.getValidateTime()!=null&&""!=articleBean.getValidateTime().toString()){
	        		
	        		Result.put("validateTime",StringUtils.getFormatDate(articleBean.getValidateTime()));
	        	}else{
	        		Result.put("validateTime","");
	        	}
	        	if(articleBean.getExpiredTime()!=null&&""!=articleBean.getExpiredTime().toString()){
	        		
	        		Result.put("expiredTime",StringUtils.getFormatDate(articleBean.getExpiredTime()));
	        	}else{
	        		Result.put("expiredTime","");
	        	}
	        	Result.put("href",articleBean.getHref());
	        	Result.put("review",articleBean.getReview());
	        	Result.put("content",articleBean.getContent());
	        	return Result;
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public PageImpl getPageData(String sql, int pageNumber2, int pageSize) {
		PageImpl  pageimpl = null;
		PageRequest pageRequest = buildPageRequest(pageNumber2, pageSize);
        try {
			List<Map<String, Object>> map_list = ips.getPaginationBeans(sql, pageRequest);
			int dataCount = ips.getDataCount(sql);
			List<Map<String, Object>> List = ips.getPaginationData(sql, pageRequest);
			java.util.List<Map<String, String>> PageList = changePageFormat(List);
			//建立page对象
	        pageimpl = new PageImpl(PageList, pageRequest, dataCount);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageimpl;
	}

	@Override
	public ArticleRecordBean getArticleDatabyId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveArticleAttach(MultipartHttpServletRequest multipartRequest,HttpSession session) {
        MultipartFile file = multipartRequest.getFile("file-1");
        
       // Map<String,MultipartFile> fileMap  =multipartRequest.getFileMap();
        String path = multipartRequest.getSession().getServletContext().getRealPath("upload");
        
        ArticleAttachBean attach = new ArticleAttachBean();
        InputStream is = null;
		String attachId = (new Date()).getTime() + "";
		String fileName = file.getOriginalFilename();
        if( file.isEmpty()!=true) {
        	File targetFile = new File(path, fileName);
            if(!targetFile.exists()){  
                targetFile.mkdirs();  
            } 
        	try {
        		///df/portal/download/${file.name}
				//file1.transferTo( new java.io.File( path+"/${file.name}" ) );
				file.transferTo( targetFile );
				is = new FileInputStream(targetFile);
				int len = (new Long(targetFile.length()).intValue());
				byte[] filedata = new byte[len];
				is.read(filedata);

				attach.setAttachContent(filedata);
				attach.setAttachId(attachId);
				attach.setAttachName(fileName);
				Object result =  baseDao.insert("portal-articleAttach.insert", attach);
				
				session.setAttribute("upLoadPath", path);
				session.setAttribute("attachId", attachId);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
		return true;
	}

	@Override
	public boolean deleteArticleData(String articleId) {
		// TODO Auto-generated method stub
		Map params = new HashMap();
		params.put("id", articleId);
		int result = -1;
		try {
			result = baseDao.delete("portal-article.deleteByPrimaryKey", params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(result==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public Object getPortletTree(Map params) {
		// TODO Auto-generated method stub
		boolean isRoot = true;
		try {
			if (isRoot) {
				List gen;
					gen = baseDao.queryForList("portal-common.getPrograms", params);
				List filterGen  = filterNullGen(gen);
				return toAsynTreeJson(filterGen, Boolean.FALSE);
			} else {
				List queryForList = baseDao.queryForList(
						"portal-common.getPortletTree", params);
				return toAsynTreeJson(queryForList, Boolean.TRUE);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private List filterNullGen(List gen) {
		List filterGen = new JSONArray();
		JSONObject js =null;
		Map params = new HashMap();
		List queryForList;
		for (int i = 0; i < gen.size(); i++) {
			js = JSONObject.fromObject(gen.get(i));
			params.put("pageId",js.get("PAGE_ID").toString());
			try {
				queryForList = baseDao.queryForList(
						"portal-common.getPortletTree", params);
				if(queryForList.size()>0){
					filterGen.add(gen.get(i));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return filterGen;
	}
	public static String toAsynTreeJson(List list, Boolean b) {
		JSONArray ja = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			Iterator it = ((Map) obj).keySet().iterator();
			JSONObject option = new JSONObject();
			while (it.hasNext()) {
				String key = (String) it.next();
				Object value = ((Map) obj).get(key);
				key = key.toLowerCase();
				value = value == null ? "" : value.toString();
				option.put(key, value);
			}
			option.put("leaf", b);
			option.put("isPortlet", b);
			if (option.get("page_title") != null) {
				option.put("text", option.get("page_title").toString());
			} else if (option.get("title") != null) {
				option.put("text", option.get("title").toString());

			}
			ja.add(option);
		}
		return ja.toString();
	}

	@Override
	public Boolean PubArticleData(String pgPletId, String portletId,
			String articleId, String checkStatus) {
		Map<String,Object> article = new HashMap<String,Object>();
		article.put("pgPletId", Integer.parseInt(pgPletId));
		article.put("portletId", portletId);
		article.put("articleId", articleId);
		article.put("checkStatus", checkStatus);
		article.put("validateTime", new Date());
		try {
			baseDao.insert("portal-article.insertArticlePortlet", article);
			baseDao.update("portal-article.updateArticlePubStatus", article);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	
	
}
