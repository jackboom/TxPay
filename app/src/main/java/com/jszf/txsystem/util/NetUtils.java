package com.jszf.txsystem.util;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class NetUtils {

	/**
	 * 创建连接，上传参数和获取数据的输出流和输入流。
	 * @return
	 */
	public static InputStream getInputStream(String parament,String path) {
		InputStream dataInputStream = null;
		OutputStream dataOutputStream = null;
		try {
			byte[] data = parament.getBytes("utf-8");
			Log.i("TAG","data:"+new String(data));
			//String para = new String(b, "UTF-8");
			URL url = new URL(path);
			Log.i("URL", url.toString());
			if (null != url) {
				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();
				if (null == urlConnection) {
				}
				// urlConnection.setRequestProperty("Content-type",
				// "text/html");
				urlConnection.setRequestProperty("contentType", "utf-8");
				urlConnection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				urlConnection.setRequestProperty("Content-Length",
						String.valueOf(data.length));
				urlConnection.setConnectTimeout(5000); // 设置超时的时间
				urlConnection.setRequestMethod("POST"); // 设置请求方式
				urlConnection.setDoInput(true); // 表示从服务器获取数据
				urlConnection.setDoOutput(true); // 表示向服务器发送数据
				urlConnection.setUseCaches(false);
				Log.i("URL", path+parament);
				urlConnection.connect();

				dataOutputStream = urlConnection.getOutputStream();
				dataOutputStream.write(data);

				// 获得服务器的响应结果和状态码
				int responseCode = urlConnection.getResponseCode();
				// 请求响应完成
				if (200 == responseCode) {
					dataInputStream = urlConnection.getInputStream();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataInputStream;
	}
	public static void upLoadToServer(String path, String parament){
		InputStream dataInputStream = null;
		OutputStream dataOutputStream = null;
		try {
			byte[] data = parament.getBytes("utf-8");
			Log.i("TAG","data:"+new String(data));
			//String para = new String(b, "UTF-8");
			URL url = new URL(path);
			Log.i("URL", url.toString());
			if (null != url) {
				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();
				if (null == urlConnection) {
				}
				// urlConnection.setRequestProperty("Content-type",
				// "text/html");
				urlConnection.setRequestProperty("contentType", "utf-8");
				urlConnection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				urlConnection.setRequestProperty("Content-Length",
						String.valueOf(data.length));
				urlConnection.setConnectTimeout(5000); // 设置超时的时间
				urlConnection.setRequestMethod("POST"); // 设置请求方式
				urlConnection.setDoInput(true); // 表示从服务器获取数据
				urlConnection.setDoOutput(true); // 表示向服务器发送数据
				urlConnection.setUseCaches(false);
				Log.i("URL", path+parament);
				urlConnection.connect();


				dataOutputStream = urlConnection.getOutputStream();
				dataOutputStream.write(data);

				// 获得服务器的响应结果和状态码
				int responseCode = urlConnection.getResponseCode();
				// 请求响应完成
//				if (200 == responseCode) {
//					dataInputStream = urlConnection.getInputStream();
//				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 得到字节流 数组
	 */
	public static byte[] readStream(InputStream inStream) {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024*2];
		int len = 0;
		try {
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			outStream.close();
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outStream.toByteArray();
	}

}
