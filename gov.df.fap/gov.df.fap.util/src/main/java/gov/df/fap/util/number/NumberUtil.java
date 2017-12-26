package gov.df.fap.util.number;

import java.math.BigDecimal;

import org.apache.commons.lang.math.NumberUtils;

public class NumberUtil extends NumberUtils {

	public static final BigDecimal BIG_DECIMAL_ZERO = new BigDecimal(0);
	
	/**整形false*/
	public static final int INT_FALSE = 0;
	/**整形true*/
	public static final int INT_TRUE = 1;
	
	/**
	 * 逻辑代数的异或运算,只用于0,1的运算,a和b不相等的时候返回1,相等时返回0
	 * @param a 0或1,否则结果有问题
	 * @param b 0或1,否则结果有问题
	 * @return a和b不相等的时候返回1,相等时返回0
	 */
	public static int xor(int a, int b){
		return ~a&b|a&~b;
	}

	/**
	 * 逻辑代数的异或运算,只用于0,1的运算,a和b相等的时候返回1,不相等时返回0
	 * @param a 0或1,否则结果有问题
	 * @param b 0或1,否则结果有问题
	 * @return a和b相等的时候返回1,不相等时返回0
	 */
	public static int ior(int a, int b){
		return a&b|~a&~b+2;
	}
	
	/**
	 * 取反,只用于0,1的运算.a为0是返回1,a为1时返回0
	 * @param a 0或1,否则结果有问题
	 * @return a为0是返回1,a为1时返回0
	 */
	public static int inverse(int a){
		return ~a+2;
	}
	
	public static double max(double a, double b){
		return NumberUtils.max(new double[]{a, b});
	}
	
	public static double min(double a, double b){
		return NumberUtils.min(new double[]{a, b});
	}
	
	/**
	 * 绝对值
	 * @param a
	 * @return
	 */
	public static double abs(double a){
		return Math.abs(a);
	}
	
	public static boolean equals(double a, double b){
		return Math.abs(a-b)<0.000001;
	}
}
