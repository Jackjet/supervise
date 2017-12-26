package gov.df.fap.controller.wf.activiti;

import gov.df.fap.api.workflow.activiti.design.IActivitiInit;
import gov.df.fap.api.workflow.activiti.design.IGetModelId;
import gov.df.fap.api.workflow.activiti.design.TreeMenu;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ibm.misc.BASE64Encoder;

@Controller
@RequestMapping(value = "/df/wf")
public class UBPMUserTreeRefController {

	@Autowired
	private IGetModelId iGetModelId;

	@Autowired
	private TreeMenu treeMenu;

	@Autowired
	private IActivitiInit iActivitiInit;

	/**
	 * 导入
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/leadin", method = RequestMethod.POST)
	public @ResponseBody
	Map leadin(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setCharacterEncoding("UTF-8");
		HashMap hashMap = new HashMap();
		String leadIn = request.getParameter("leadin");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile pictureFile = multipartRequest.getFile("imgFile");
		String originalFilename = pictureFile.getOriginalFilename();// 取到上传附件名称
		boolean endsWith = originalFilename.endsWith("wf");
		if (!endsWith) {
			hashMap.put("isFile", "false");
			return hashMap;
		}
		byte[] bytes = pictureFile.getBytes();
		try {
			RepositoryService repositoryService = iActivitiInit
					.getRepositoryService();
			Map<String, String> map = treeMenu.leadin(bytes, repositoryService,
					leadIn);
			String flag = map.get("leadin");
			if (flag.equals("true")) {
				hashMap.put("leadin", flag);
				return hashMap;
			}
			hashMap.put("success", "true");
		} catch (Exception e) {
			e.printStackTrace();
			hashMap.put("success", "false");
			return hashMap;
		}
		return hashMap;
	}

	/**
	 * 导出
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/leadout", method = RequestMethod.GET)
	public @ResponseBody
	void leadout(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String process_code = request.getParameter("wfCdode");
		String fileName = request.getParameter("wfName");
//		fileName = URLDecoder.decode(fileName, "UTF-8");
		String userAgent = request.getHeader("user-agent").toUpperCase(); 
		if (userAgent.contains("MSIE") || userAgent.contains("TRIDENT") || userAgent.contains("EDGE")) {  
		    fileName = URLEncoder.encode(fileName, "UTF-8");  
		} else {  
		    fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");  
		}
		OutputStream out = null;
		try {
			RepositoryService repositoryService = iActivitiInit
					.getRepositoryService();
			byte[] leadout = treeMenu.leadout(process_code, repositoryService);
			if (leadout != null) {
//				response.setContentType("application/octet-stream");
				request.getSession().getServletContext().getMimeType(fileName);
				response.setHeader("Content-Disposition","attachment;filename="+ fileName +".wf");
				out = new BufferedOutputStream(
						response.getOutputStream());
				out.write(leadout);
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 增加流程节点
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addProcess", method = RequestMethod.POST)
	public @ResponseBody
	Map addProcess(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String process_code = request.getParameter("code");
		String process_name = request.getParameter("name");
		String process_ptname = request.getParameter("ptname");
		String process_field = request.getParameter("field");
		HashMap hashMap = new HashMap();
		try {
			RepositoryService repositoryService = iActivitiInit
					.getRepositoryService();
			treeMenu.addProcess(process_code, process_name, process_ptname,
					process_field, repositoryService);
			hashMap.put("flag", true);
		} catch (Exception e) {
			e.printStackTrace();
			hashMap.put("flag", false);
		}
		return hashMap;
	}

	/**
	 * 删除流程节点
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delProcess", method = RequestMethod.POST)
	public @ResponseBody
	Map delProcess(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String process_code = request.getParameter("code");
		HashMap hashMap = new HashMap();
		try {
			RepositoryService repositoryService = iActivitiInit
					.getRepositoryService();
			treeMenu.delProcess(process_code, repositoryService);
			hashMap.put("flag", true);
		} catch (Exception e) {
			e.printStackTrace();
			hashMap.put("flag", true);
			return hashMap;
		}
		return hashMap;
	}

	/**
	 * 查询主表名称
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryPrimaryName", method = RequestMethod.GET)
	public @ResponseBody
	List queryPrimaryName(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = treeMenu.queryPrimaryName();
		return list;
	}

	/**
	 * 加载流程树
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/treePid", method = RequestMethod.GET)
	public @ResponseBody
	Map getZTree(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map map = treeMenu.getZtree();
		return map;
	}

	/**
	 * 获得模型id
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getModelId", method = RequestMethod.GET)
	public @ResponseBody
	Map getModelId(HttpServletRequest request, HttpServletResponse response) {

		HashMap hashMap = new HashMap();
		String treeCode = request.getParameter("code");
		String modelId = iGetModelId.getModelId(treeCode);
		hashMap.put("modelId", modelId);
		return hashMap;

	}

}
