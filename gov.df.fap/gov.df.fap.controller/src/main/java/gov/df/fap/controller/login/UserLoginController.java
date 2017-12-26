package gov.df.fap.controller.login;

import gov.df.fap.api.login.IUsersLogin;
import gov.df.fap.util.Tools;
import gov.df.fap.util.factory.ServiceFactory;
import gov.df.supervise.controller.util.Constans;
import gov.df.supervise.controller.util.CookieUtil;
import gov.df.supervise.controller.util.UUIDUtil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/login")
public class UserLoginController {

	// yy-context.xml - line 129
	private IUsersLogin userLogin = (IUsersLogin) ServiceFactory
			.getBean("df.fap.userLogin");

	@RequestMapping(value = "/userLogin.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> userLogin(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();;
		try {
			
			String code = request.getParameter("code").toUpperCase();
			// 验证码是否正确
			String picId = CookieUtil.getUid(request, "PICID");// cookie中获取验证码id
			String picCode_true1 = checkCodeget(picId);
			String picCode_true = picCode_true1;// 通过cookie中id查找缓存中验证码
			if(!StringUtils.isBlank(picCode_true1)){
				picCode_true = picCode_true1.toUpperCase();
			}
			// 清除验证码信息
			 CookieUtil.removeCookie(response, "PICID");
			 CHECKCODE.remove(picId);
			// MemcachedUtils.delete(picId);
			// 验证码失效
			if (StringUtils.isBlank(picCode_true)) {
				map.put("flag", "9");
			} else if (!picCode_true.equals(code)) {
				map.put("flag", "8");
			} else {
				map = userLogin.loginsend(request, response);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@RequestMapping(value = "/LoginMessage.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> loginMessage(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = null;
		try {
			map = userLogin.loginMessage(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@RequestMapping(value = "/loginout.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> loginout(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = null;
		try {
			map = userLogin.loginout(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, String> CHECKCODE = new HashMap<String, String>();

	// 验证码的生成
	@RequestMapping(value = "/getImage")
	public void getImage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BufferedImage img = new BufferedImage(158, 38,
				BufferedImage.TYPE_INT_RGB);
		// 得到该图片的绘图对象
		Graphics g = img.getGraphics();
		Random r = new Random();
		Color c = new Color(200, 150, 255);
		g.setColor(c);
		// 填充整个图片的颜色
		g.fillRect(0, 0, 158, 38);
		// 向图片中输出数字和字母
		StringBuffer sb = new StringBuffer();
		char[] ch = "098765321"//ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz4
				.toCharArray();
		int index, len = ch.length;
		for (int i = 0; i < 4; i++) {
			index = r.nextInt(len);
			g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt(255)));
			g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 32));
			// 输出的 字体和大小
			g.drawString("" + ch[index], 30 + (i * 22) + 6, 29);
			// 写什么数字，在图片 的什么位置画
			sb.append(ch[index]);
		}
		// 把验证码的值放入session中以便于验证
		String picCode_true = sb.toString();// 验证码真实内容
		String picId = Constans.PIC_ID + UUIDUtil.uuidRandom();// 生成对应的key
		// MemcachedUtils.add(picId, picCode_true, new Date(1000 * 60 * 10));//
		// 放入缓存，2分钟有效时间
		checkCodeset(picId, picCode_true, getDate());
		CookieUtil.addCookie(response, "PICID", picId, -1);
		ImageIO.write(img, "JPG", response.getOutputStream());
	}

	@RequestMapping(method = RequestMethod.POST, value = "/checkCode")
	public @ResponseBody
	Map<String, Object> checkCode(HttpServletRequest request,
			HttpServletResponse response, String errorMessage) {
		Map<String, Object> result = new HashMap<String, Object>();
		String code = request.getParameter("code").toUpperCase();
		// 验证码是否正确
		String picId = CookieUtil.getUid(request, "PICID");// cookie中获取验证码id
		String picCode_true1 = checkCodeget(picId);
		String picCode_true = picCode_true1;// 通过cookie中id查找缓存中验证码
		if(!StringUtils.isBlank(picCode_true1)){
			picCode_true = picCode_true1.toUpperCase();
		}
		// 清除验证码信息
		// CookieUtil.removeCookie(response, "PICID");
		// MemcachedUtils.delete(picId);
		// 验证码失效
		if (StringUtils.isBlank(picCode_true)) {
			result.put("errorCode", 0);
			result.put("errorMsg", "验证码失效！");
		} else if (!picCode_true.equals(code)) {
			result.put("errorCode", -1);
			result.put("errorMsg", "验证码输入错误！");
		} else {
			result.put("errorCode", 1);
			result.put("errorMsg", "验证码输入正确！");
		}
		return result;

	}

	public static String getDate() {
		return Tools.getCurrDate();
	}

	@SuppressWarnings("rawtypes")
	public void checkCodeset(String picId, String picCode_true, String string)
			throws ParseException {
		Format dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = format.parse(string);
		date.setTime(date.getTime() + 2 * 60 * 1000);// 在原基础上添加2分钟，作为失效时间
		String date1 = dateTimeFormat.format(date);
		CHECKCODE.put(picId, picCode_true + "@" + date1);
		if (Integer.valueOf(date1.substring(14, 16)).intValue() % 10 == 0) {
			Iterator iterator = CHECKCODE.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String value = CHECKCODE.get(key);
				String strsub = value.substring(value.length() - 19);
				if (strsub.compareTo(string) < 0) {
					iterator.remove();
					CHECKCODE.remove(key);
				}
			}
			// for (String key : CHECKCODE.keySet()) {
			// String value = CHECKCODE.get(key);
			// String strsub = value.substring(value.length() - 19);
			// if (strsub.compareTo(string) < 0) {
			// CHECKCODE.remove(key);
			// }
			// }
		}
	}

	public String checkCodeget(String picId) {
		String String = CHECKCODE.get(picId);
		String picCode;
		if (StringUtils.isBlank(String)) {
			picCode = null;
		} else {
			String picCode_true = String.split("@")[0];
			String date = String.split("@")[1];
			String dateNow = getDate();
			if (date.compareTo(dateNow) < 0) {
				CHECKCODE.remove(picId);
				picCode = null;
			} else {
				// CHECKCODE.remove(picId);
				picCode = picCode_true;
			}
		}
		return picCode;
	}

	public String loginCheckCodeget(String picId) {
		String String = CHECKCODE.get(picId);
		String picCode;
		if (StringUtils.isBlank(String)) {
			picCode = null;
		} else {
			String picCode_true = String.split("@")[0];
			String date = String.split("@")[1];
			String dateNow = getDate();
			if (date.compareTo(dateNow) < 0) {
				CHECKCODE.remove(picId);
				picCode = null;
			} else {
				CHECKCODE.remove(picId);
				picCode = picCode_true;
			}
		}
		return picCode;
	}

}
