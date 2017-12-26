/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017 北京用友政务软件有限公司
 * </p>
 * <p>
 * Company: 北京用友政务软件有限公司
 * </p>
 * <p>
 * CreateData: 2017-11-22下午3:35:07
 * </p>
 * 
 * @author songlr3
 * @version 1.0
 */
package gov.df.supervise.controller.util;

import java.util.UUID;

public class UUIDUtil {

	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
			"o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };

	/**
	 * 生成短8位大小写字母数字UUID
	 */
	public static String generateShortUuid() {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();

	}

	/**
	 * 取得一个UUID的方法
	 * 
	 * @return 一个UUID
	 */
	public static String uuidRandom() {
		String id = UUID.randomUUID().toString();
		return id;
	}
	
	
	public static void main(String[] args) {
		System.out.println(uuidRandom());
	}
}
