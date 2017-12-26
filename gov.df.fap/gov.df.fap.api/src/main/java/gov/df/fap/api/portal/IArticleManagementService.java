package gov.df.fap.api.portal;

import gov.df.fap.bean.portal.ArticleRecordBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartHttpServletRequest;


/**
 * 
 * @author Liurs
 *
 * 2017-3-23下午7:16:42
 */
public interface IArticleManagementService {

	/**
	 * 将数据库Object对象转换为已录文章bean数据
	 */
	public ArticleRecordBean getArticleRecordBeanFromO();
	
	/**
	 * 获取分页请求
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public PageRequest buildPageRequest(int pageNumber,int pageSize);
	
	/**
	 * 获取分页表
	 * @param map_list
	 * @return
	 */
	public List<Map<String, String>> changePageFormat(List<Map<String, Object>> map_list);
	
	/**
	 * 通过id获取文章详细信息
	 * @param id
	 * @return
	 */
	public ArticleRecordBean getArticleDatabyId(String id);

	/**
	 * 保存article数据
	 * @param articleTittle
	 * @param articleNumber
	 * @param articleAuthor
	 * @param createTime
	 * @param picUrl
	 * @param validateTime
	 * @param expireTime
	 * @param articleLink
	 * @param articleIntroduction
	 * @param articleContent
	 * @param tokenid
	 * @param tokenid2 
	 * @return 
	 */
	public boolean saveArticleData(String articleId,String articleTittle, String articleNumber,
			String articleAuthor, String createTime, String picUrl,
			String validateTime, String expireTime, String articleLink,
			String articleIntroduction, String articleContent, String tokenid);
	/**
	 * 获取文章数据
	 * @param articleId
	 * @return
	 */
	public Map<String, String> getArticleData(String articleId);

	/**
	 * 
	 * @param sql
	 * @param pageNumber2
	 * @param pageSize
	 * @return
	 */
	public PageImpl getPageData(String sql, int pageNumber2, int pageSize);

	/**
	 * 保存文章附件
	 * @param multipartRequest
	 * @param session
	 * @return
	 */
	public boolean saveArticleAttach(
			MultipartHttpServletRequest multipartRequest, HttpSession session);
	/**
	 * 删除文章数据
	 * @param articleId
	 * @return
	 */
	public boolean deleteArticleData(String articleId);
	
	/**
	 * 获取发布portlet树
	 * @param params
	 * @return
	 */
	public Object getPortletTree(Map params);
	/**
	 * 发布文章到指定portlet
	 * @param params
	 * @return
	 */
	public Boolean PubArticleData(String pgPletId, String portletId,
			String articleId, String checkStatus);
}
