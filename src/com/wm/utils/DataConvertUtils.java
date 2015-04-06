package com.wm.utils;

import java.text.NumberFormat;

/**
 * 
 * �����ݽ��н��Ƶ�ת��
 * @author Like
 *
 */
public class DataConvertUtils {
	
	/**
	 * 16����ת10����
	 * @param hex ʮ�������ַ���
	 * @return ת���õ�ʮ�����ַ���
	 */
	public static String hexToDecimal(String hex) {
		return Integer.valueOf(hex, 16).toString();
	}
	
	/**
	 * 10����ת16����
	 * @param decimal ʮ�����ַ���
	 * @return ת���õ�ʮ�������ַ���
	 */
	public static String decimalToHex(String decimal) {
		int decNumber = Integer.valueOf(decimal);
		return Integer.toHexString(decNumber);
	}
	
	public static String format(double number, int save) {
		 NumberFormat nf = NumberFormat.getNumberInstance();   
         nf.setMaximumFractionDigits(save);
         return nf.format(number);
	}
	
	/**
	 * ����һλС���� ���Ҳ���������
	 * 
	 * @param value
	 * @return
	 */
	public static float formatNoRound(Double value){
		float result = (float) (Math.floor(value*10)/10);
		return result;
	}
	
}
