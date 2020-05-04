package com.easypre.client.util;

/**
 * @author zhoudecai
 * @version 1.0
 * @since 1.0
 */
public class StringUtil {
	/**
	 * 将对象转换为字符串，防止NPL
	 * @param object
	 * @return
	 */
	public static String convertToStr(Object object){
		return object!=null?object.toString():null;
	}

	/**
	 * 十进制转64进制
	 * @param num
	 * @return
	 */
	public static String radix10To64(long num){
		if(num<0) {
			num=Math.abs(num);
		}
		final int radix = 62;
		char[] outs = new char[64];
		int outsIndex = outs.length;
		long quotient;
		long remainder;
		char c;
		do{
			quotient = num / radix;
			remainder = num % radix;
			if(remainder>=0 && remainder <=9) {
				c = (char) ('0' + remainder);
			}else if(remainder >= 10 && remainder <= (10+26-1)) {
				c = (char) ('a' + (remainder - 10));
			}else {
				c = (char) ('A' + (remainder - 36));
			}
			outs[--outsIndex] = c;
			num = quotient;
		}while(num>0);
		return new String(outs, outsIndex, outs.length - outsIndex);
	}
}
