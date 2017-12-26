package gov.df.fap.util.portal;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Liurs
 *
 * 2017-4-10下午4:02:13
 */
public class StringUtils {

	/**
	 * string to date
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static java.util.Date getDate(String value,
			java.util.Date defaultValue) {
		if (null == value || value.length() < 10)
			return defaultValue;
		java.util.Date result = defaultValue;
		String v = value.replace('/', '-');

		try {
			if (v.length() == 10) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.parse(v);
			} else {
				v = v.replace('T', ' ');
				if (v.length() == 16)
					v += ":00";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return sdf.parse(v);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static boolean hasStringValue(Object o) {
		if (o == null)
			return false;
		return !"".equals(o.toString());
	}

	public static java.sql.Date getSqlDate(String value,
			java.sql.Date defaultValue) {
		if (null == value || value.length() < 10)
			return defaultValue;
		java.sql.Date result = defaultValue;
		String v = value.replace('/', '-').substring(0, 10);
		try {
			result = java.sql.Date.valueOf(v);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 
	 * @param value
	 * @param date
	 * @return
	 */
	public static String getFormatDate(Date date) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			result = sdf.format(date);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return result;
	}
}
