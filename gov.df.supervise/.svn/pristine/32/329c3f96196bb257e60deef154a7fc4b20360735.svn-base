package org.gov.df.supervice.util;

public class SuperviseToolUtil {
	
	/**
	 * 查询sql参数做安全校验，
	 * @param str
	 * @return
	 */
	public static boolean sql_inj(String str) {
		String inj_str = "selec,delete,update,truncate";
		String inj_stra[] = inj_str.split(",");
		for (int i = 0; i < inj_stra.length; i++) {
			if (str.indexOf(inj_stra[i]) >= 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 危险字符校验
	 * @param value
	 * @return
	 */
	public static String checkFilter(String value) {
        if (value == null) {
            return null;
        }        
        StringBuffer result = new StringBuffer(value.length());
        for (int i=0; i<value.length(); ++i) {
            switch (value.charAt(i)) {
            case '<':
                result.append("&lt;");
                break;
            case '>': 
                result.append("&gt;");
                break;
            case '"': 
                result.append("&quot;");
                break;
            case '\'': 
                result.append("&#39;");
                break;
            case '%': 
                result.append("&#37;");
                break;
            case ';': 
                result.append("&#59;");
                break;
            case '(': 
                result.append("&#40;");
                break;
            case ')': 
                result.append("&#41;");
                break;
            case '&': 
                result.append("&amp;");
                break;
            case '+':
                result.append("&#43;");
                break;
            default:
                result.append(value.charAt(i));
                break;
            }
        }
        return result.toString();
    }

}
