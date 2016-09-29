package com.jszf.txsystem.util;

import android.util.Log;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @ClassName: ParaUtils
 * @Description: 处理请求参数核心类
 * @author: taowl
 * @date: 2016年1月26日 下午7:21:43
 */

public class ParaUtils {

	/**
	 * 除去数组中的空值和签名参数
	 *
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static HashMap<String, String> paraFilter(HashMap<String, String> sArray) {
		HashMap<String, String> result = new HashMap<String, String>();
		String value = null;
		if (sArray == null || sArray.size() <= 0) {
			return result;
		}
		for (String key : sArray.keySet()) {
			value = sArray.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
				// || key.equalsIgnoreCase("signType")) { //signType
				continue;
			}
			result.put(key, sArray.get(key));
		}
		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 *
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			sb.append(key).append("=").append(value).append("&");
		}
		// 拼接时，不包括最后一个&字符
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	public static String urlEncode(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			try {
				sb.append(key).append("=").append(URLEncoder.encode(value, "utf-8")).append("&");
			} catch (Exception e) {
				Log.d("TAG","E:"+e.toString());
			}
		}
		// 拼接时，不包括最后一个&字符
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}


}
