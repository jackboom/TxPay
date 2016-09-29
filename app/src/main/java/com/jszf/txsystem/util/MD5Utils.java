package com.jszf.txsystem.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密
 * 
 * @author Administrator
 * 
 */
public class MD5Utils {
	public static final String KEY_ALGORITHM = "MD5";

	/**
	 *  数字签名 签名/验证算法
	 * */
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	public static String encryptToMD5(String info) {
		byte[] digesta = null;

		try {
			MessageDigest e = MessageDigest.getInstance("MD5");
			e.update(info.getBytes("UTF-8"));
			digesta = e.digest();
		} catch (NoSuchAlgorithmException var3) {
			var3.printStackTrace();
		} catch (UnsupportedEncodingException var4) {
			var4.printStackTrace();
		}

		return bytes2Hex(digesta);
	}

	public static String bytes2Hex(byte[] bytes) {
		StringBuffer buf = new StringBuffer("");
		String temp = "";
		int md ;
		for (int i = 0; i < bytes.length; i++) {
			md = bytes[i];
			if (md<0) {
				md+=256;
			} 
			if (md<16) {
				buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		}
		return buf.toString();
	}

	public static String sign(String prestr, String merchantMD5Key,
			String input_charset) {
		// TODO Auto-generated method stub
		byte[] digesta = null;
		try {
			MessageDigest mDigest = MessageDigest.getInstance("MD5");
//			String data = prestr + "&MD5key="+merchantMD5Key;
			String data = prestr;
			Log.d("TAG","DATA:"+data);
			mDigest.update(data.getBytes(input_charset));
			digesta = mDigest.digest();
//			StringBuffer buf = new StringBuffer("");
//			String temp = "";
//			int md ;
//			for (int i = 0; i < digesta.length; i++) {
//				md = digesta[i];
//				if (md<0) {
//					md+=256;
//				}
//				if (md<16) {
//					buf.append("0");
//					buf.append(Integer.toHexString(i));
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.d("TAG","BUF:"+bytes2Hex(digesta));
		return bytes2Hex(digesta);
	}
	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4',
				'5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			//获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			//使用指定的字节更新摘要
			mdInst.update(btInput);
			//获得密文
			byte[] md = mdInst.digest();
			//把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			Log.d("TAG","MD5:"+new String(str).toLowerCase());
			return new String(str).toLowerCase();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
