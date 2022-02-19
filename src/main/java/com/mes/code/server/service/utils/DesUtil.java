package com.mes.code.server.service.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DesUtil {

	private static Logger logger = LoggerFactory.getLogger(DesUtil.class);

	private final static String DES = "DES";

	/**
	 * Description 根据键值进行加密
	 * 
	 * @param data
	 * @param key  加密键byte数组 Decoder
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String key) {
		if(key==null||key.length()<8||key.length()>50)
			return data;
		byte[] bt;
		String strs = null;
		try {
			if(data==null||data.length()<1)
				return "";
			bt = encrypt(data.getBytes(), key.getBytes());
			strs = base64Encoder(bt, 0, 0);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return strs;
	}

	/**
	 * Description 根据键值进行解密
	 * 
	 * @param data
	 * @param key  加密键byte数组
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String decrypt(String data, String key) {
		if(key==null||key.length()<8||key.length()>50)
			return data;
		if (data == null||data.length()<5)
			return "";
		byte[] bt = new byte[0];
		byte[] buf;
		try {
			buf = base64Decoder(data.toCharArray(), 0);
			try {
				bt = decrypt(buf, key.getBytes());
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		return new String(bt);
	}

	/**
	 * Description 根据键值进行加密
	 * 
	 * @param data
	 * @param key  加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}

	/**
	 * Description 根据键值进行解密
	 * 
	 * @param data
	 * @param key  加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}

	/**
	 * Description 获取字符串MD5值
	 * 
	 * @param sourceStr
	 */
	@SuppressWarnings("unused")
	private static String MD5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
			// System.out.println("MD5(" + sourceStr + ",32) = " + result);
			// System.out.println("MD5(" + sourceStr + ",16) = " +
			// buf.toString().substring(8, 24));
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	final static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

	final static char pad = '=';

	public final static byte[] base64Decoder(char[] src, int start) throws IOException {
		if (src == null || src.length == 0)
			return null;
		char[] four = new char[4];
		int i = 0, l, aux;
		char c;
		boolean padded;
		ByteArrayOutputStream dst = new ByteArrayOutputStream(src.length >> 1);
		while (start < src.length) {
			i = 0;
			do {
				if (start >= src.length) {
					if (i > 0)
						throw new IOException("bad BASE 64 In->");
					else
						return dst.toByteArray();
				}
				c = src[start++];
				if (chars.indexOf(c) != -1 || c == pad)
					four[i++] = c;
				else if (c != '\r' && c != '\n')
					throw new IOException("bad BASE 64 In->");
			} while (i < 4);
			padded = false;
			for (i = 0; i < 4; i++) {
				if (four[i] != pad && padded)
					throw new IOException("bad BASE 64 In->");
				else if (!padded && four[i] == pad)
					padded = true;
			}
			if (four[3] == pad) {
				if (start < src.length)
					throw new IOException("bad BASE 64 In->");
				l = four[2] == pad ? 1 : 2;
			} else
				l = 3;
			for (i = 0, aux = 0; i < 4; i++)
				if (four[i] != pad)
					aux |= chars.indexOf(four[i]) << (6 * (3 - i));

			for (i = 0; i < l; i++)
				dst.write((aux >>> (8 * (2 - i))) & 0xFF);
		}
		dst.flush();
		byte[] result = dst.toByteArray();
		dst.close();
		dst = null;
		return result;
	}

	public final static String base64Encoder(byte[] src, int start, int wrapAt) {
		return base64Encoder(src, start, src.length, wrapAt);
	}

	public final static String base64Encoder(byte[] src, int start, int length, int wrapAt) {
		if (src == null || src.length == 0)
			return null;
		StringBuffer encodeDst = new StringBuffer();
		int lineCounter = 0;
		length = start + length > src.length ? src.length : start + length;
		while (start < length) {
			int buffer = 0, byteCounter;
			for (byteCounter = 0; byteCounter < 3 && start < length; byteCounter++, start++)
				buffer |= (src[start] & 0xFF) << (16 - (byteCounter << 3));
			if (wrapAt > 0 && lineCounter == wrapAt) {
				encodeDst.append("\r\n");
				lineCounter = 0;
			}
			char b1 = chars.charAt((buffer << 8) >>> 26);
			char b2 = chars.charAt((buffer << 14) >>> 26);
			char b3 = (byteCounter < 2) ? pad : chars.charAt((buffer << 20) >>> 26);
			char b4 = (byteCounter < 3) ? pad : chars.charAt((buffer << 26) >>> 26);
			encodeDst.append(b1).append(b2).append(b3).append(b4);
			lineCounter += 4;
		}
		return encodeDst.toString();
	}

	/*
	 * public static void main(String[] args) throws Exception { String data = "0";
	 * // String key = CookieContants.Key;//秘钥 String encode = encrypt(data,
	 * SessionContants.Key); System.err.println(encode); String dcode =
	 * decrypt(encode, SessionContants.Key); System.err.println(dcode); }
	 */
}