package com.mes.code.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字符校验工具类
 * 
 * @author YouWang·Peng
 * @CreateTime 2021-10-29 10:12:48
 */
public class CRC {

	private static Logger logger = LoggerFactory.getLogger(CRC.class);

	/**
	 * 一个字节包含位的数量 8
	 */
	private static final int BITS_OF_BYTE = 8;

	/**
	 * 多项式
	 */
	private static final int POLYNOMIAL = 0xA001;

	/**
	 * 初始值
	 */
	private static final int INITIAL_VALUE = 0xFFFF;

	/**
	 * 获取CRC校验码
	 */
	public static String crc16(String wContent) {
		String wResult = "";
		try {
			int[] wIntStrs = stringToIntArr(wContent);
			wResult = crc16(wIntStrs);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * CRC16 编码
	 * 
	 * @param bytes 编码内容
	 * @return 编码结果
	 * 
	 */
	private static String crc16(int[] bytes) {
		String wResult = "";
		try {
			int res = INITIAL_VALUE;

			for (int data : bytes) {
				res = res ^ data;

				for (int i = 0; i < BITS_OF_BYTE; i++) {
					res = (res & 0x0001) == 1 ? (res >> 1) ^ POLYNOMIAL : res >> 1;
				}
			}

			wResult = convertToHexString(revert(res));
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 将String字符串转换为int数组（数字范围0-9）
	 * 
	 * @param str 字符串（内只能包含数字0-9）
	 * @return 字符串内产生的数组
	 */
	private static int[] stringToIntArr(String str) {
		int[] intArr = new int[str.length()];
		try {
			char[] ch = str.toCharArray();
			for (int i = 0; i < str.length(); i++) {
				intArr[i] = (int) ch[i] - 48;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return intArr;
	}

	/**
	 * 翻转16位的高八位和低八位字节
	 * 
	 * @param src 翻转数字
	 * @return 翻转结果
	 * 
	 */
	private static int revert(int src) {
		int wResult = 0;
		try {
			int lowByte = (src & 0xFF00) >> 8;
			int highByte = (src & 0x00FF) << 8;
			wResult = lowByte | highByte;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	private static String convertToHexString(int src) {
		String wResult = "";
		try {
			wResult = Integer.toHexString(src);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
