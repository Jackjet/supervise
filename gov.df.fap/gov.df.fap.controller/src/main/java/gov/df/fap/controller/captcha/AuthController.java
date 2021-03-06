//package gov.df.fap.controller.captcha;
//
//import java.awt.image.BufferedImage;
//import java.util.Map;
//
//import javax.imageio.ImageIO;
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.util.WebUtils;
//
//@Controller
//@RequestMapping("/servlet/auth")
//public class AuthController {
//	@RequestMapping(value = "/captcha")
//	public void captcha(HttpServletRequest request, HttpServletResponse response){
//		try {
//			//把校验码转为图像
//			BufferedImage image = authService.genCaptcha(response);
//			
//			response.setContentType("image/jpeg");
//	        
//	        //输出图像
//			ServletOutputStream outStream = response.getOutputStream();
//			ImageIO.write(image, "jpeg", outStream);
//			outStream.close();
//		} catch (Exception ex) {
//			logger.error(ex.getMessage(), ex);
//		}
//	}
//
//	/**
//	 * 检查验证码是否正确
//	 * @param request
//	 * @param map
//	 * @return
//	 */
//	@RequestMapping(value = "/verifyCaptcha", method = RequestMethod.POST)
//	public JsonResult verifyCaptcha(HttpServletRequest request, @RequestBody Map<String, String> map) {
//		try {
//			return authService.verifyCaptcha(request, map);
//		} catch (Exception ex) {
//			logger.error(ex.getMessage(), ex);
//			return new JsonResult(ReturnCode.EXCEPTION, "检查验证码失败！", null);
//		}
//	}
//	
//	/**
//	 * 用户登录（web）
//	 * 
//	 * @param map（userName, password）
//	 * @return
//	 */
//	@RequestMapping(value = "/webLogin", method = RequestMethod.POST)
//	public JsonResult webLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> map) {
//		try {
//			//获取验证码的cookie id
//			String captchaCookie = WebUtils.getCookieByName(request, "YsbCaptcha");
//			map.put("captchaCookie", captchaCookie);
//			
//			return authService.webLogin(map, response, true);
//		} catch (Exception ex) {
//			logger.error(ex.getMessage(), ex);
//			return new JsonResult(ReturnCode.EXCEPTION, "用户登录失败！", null);
//		}
//	}
//}