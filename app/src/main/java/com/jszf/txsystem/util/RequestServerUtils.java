package com.jszf.txsystem.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Administrator on 2016/4/27.
 */
public class RequestServerUtils {
    private static String mSign;
    private static String mPreSign;
    private static String mPara;
    private static String mEncyptWord;

    public static String requestNormal(final HashMap<String,String> map,String path){
        mPara = ParaUtils.createLinkString(map);
        InputStream mInputStream = NetUtils.getInputStream(mPara,path);
        if (mInputStream != null) {
            byte[] data = NetUtils.readStream(mInputStream);
            try {
                String requestString = new String(data, "utf-8"); // 登录字符串
//                JSONObject requestJson = new JSONObject(requestString);
//                Iterator mIterator = requestJson.keys();
//                HashMap<String, String> getMap = new HashMap();
//                while (mIterator.hasNext()) {
//                    String key = mIterator.next().toString();
//                    String value = requestJson.getString(key);
//                    if (value == null) {
//                        continue;
//                    } else if ("".equals(value.trim())) {
//                        continue;
//                    } else {
//                        getMap.put(key,value);
//                    }
//                }
//                HashMap signMap = ParaUtils.paraFilter(getMap);
                Log.d("TAG", "请求结果字符串：" + requestString);
                return requestString;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static HashMap<String,String> requestByMd5(final HashMap<String, String> map,String md5){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                mPreSign = ParaUtils.createLinkString(map);
                //签名字符串
                Log.d("TAG","Presign:"+mPreSign);
//                String str = RSAUtils.sign(mPreSign, Constant.ownPrivateKey);
                mPreSign = mPreSign + md5;

                String mSign = MD5Utils.MD5(mPreSign);

                Log.d("TAG","msgin:"+mSign);
                //上传服务器字符串
                mPara = ParaUtils.urlEncode(map) + "&sign=" + mSign;
                InputStream mInputStream = NetUtils.getInputStream(mPara,null);
                if (mInputStream != null) {
                    byte[] data = NetUtils.readStream(mInputStream);
                    try {
                        String requestString = new String(data, "utf-8"); // 登录字符串
                        JSONObject requestJson = new JSONObject(requestString);
                        Iterator mIterator = requestJson.keys();
                        HashMap<String, String> getMap = new HashMap();
                        while (mIterator.hasNext()) {
                            String key = mIterator.next().toString();
                            String value = requestJson.getString(key);
                            if (value == null) {
                                continue;
                            } else if ("".equals(value.trim())) {
                                continue;
                            } else {
                                getMap.put(key,value);
                            }
                        }
                        HashMap signMap = ParaUtils.paraFilter(getMap);
                        String preSign = ParaUtils.createLinkString(signMap);
                        String productSign = (MD5Utils.MD5(preSign)).toUpperCase();
//                        String productSign = MD5Utils.encryptToMD5(preSign);
                        boolean isTrue = productSign.equals(getMap.get("sign"));
                        Log.d("TAG", isTrue + ",productSign:"+productSign+"\nsign:"+getMap.get("sign"));
                        Log.d("TAG", "请求结果字符串：" + requestString);
                        return signMap;
                        //验签
//                            if (isTrue) {
//                        Message mMessage = Message.obtain();
//                        mMessage.what = flag;
//                        mMessage.obj = getMap;
//                        mHandler.handleMessage(mMessage);

//                            } else {
//                                Log.d("TAG","签名失败:   "+"preSign:"+preSign+":\nsign:"+loginJson.getString("sign"));
//                            }

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//            }
//        }).start();
        return null;
    }

    public static void BackToServer(final HashMap<String,String> map,String md5){
        InputStream dataInputStream = null;
        OutputStream dataOutputStream = null;
        try {
            mPreSign = ParaUtils.createLinkString(map);
            //签名字符串
            Log.d("TAG","Presign:"+mPreSign);
//                String str = RSAUtils.sign(mPreSign, Constant.ownPrivateKey);
            mPreSign = mPreSign + md5;

            String mSign = MD5Utils.MD5(mPreSign);

            Log.d("TAG","msgin:"+mSign);
            //上传服务器字符串
            mPara = ParaUtils.urlEncode(map) + "&sign=" + mSign;
            byte[] data = mPara.getBytes("utf-8");
            Log.i("TAG","data:"+new String(data));
            //String para = new String(b, "UTF-8");
            URL url = new URL(Constant.PATH);
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
//                urlConnection.setDoInput(true); // 表示从服务器获取数据
                urlConnection.setDoOutput(true); // 表示向服务器发送数据
                urlConnection.setUseCaches(false);
                Log.i("URL", Constant.PATH+mPara);
                urlConnection.connect();


                dataOutputStream = urlConnection.getOutputStream();
                dataOutputStream.write(data);

                // 获得服务器的响应结果和状态码
//                int responseCode = urlConnection.getResponseCode();
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
    public static HashMap<String,String> requestByRsa(final HashMap<String, String> map){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        if (map.get("newPassWord") != null) {
            try {
                mEncyptWord = RSAUtils.encrypt(map.get("newPassWord"), Constant.txPublicKey);
                map.put("newPassWord",mEncyptWord);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mPreSign = ParaUtils.createLinkString(map);
        //签名字符串
        Log.d("TAG","Presign:"+mPreSign);
//                String str = RSAUtils.sign(mPreSign, Constant.ownPrivateKey);
        //签名字符串
        String str = RSAUtils.sign(mPreSign, Constant.ownPrivateKey);
        try {
            mSign = URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //上传服务器字符串
        mPara = ParaUtils.urlEncode(map) + "&sign=" + mSign;
        InputStream mInputStream = NetUtils.getInputStream(mPara,null);
        if (mInputStream != null) {
            byte[] data = NetUtils.readStream(mInputStream);
            try {
                String requestString = new String(data, "utf-8"); // 登录字符串
                JSONObject requestJson = new JSONObject(requestString);
                Iterator mIterator = requestJson.keys();
                HashMap<String, String> getMap = new HashMap();
                while (mIterator.hasNext()) {
                    String key = mIterator.next().toString();
                    String value = requestJson.getString(key);
                    if (value == null) {
                        continue;
                    } else if ("".equals(value.trim())) {
                        continue;
                    } else {
                        getMap.put(key,value);
                    }
                }
                HashMap signMap = ParaUtils.paraFilter(getMap);
                String preSign = ParaUtils.createLinkString(signMap);
                boolean isTrue = RSAUtils.checkSign(preSign, getMap.get("sign"), Constant.txPublicKey);
                Log.d("TAG", isTrue + "");
                Log.d("TAG", "请求结果字符串：" + requestString);
                return signMap;
                //验签
//                            if (isTrue) {
//                        Message mMessage = Message.obtain();
//                        mMessage.what = flag;
//                        mMessage.obj = getMap;
//                        mHandler.handleMessage(mMessage);

//                            } else {
//                                Log.d("TAG","签名失败:   "+"preSign:"+preSign+":\nsign:"+loginJson.getString("sign"));
//                            }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
