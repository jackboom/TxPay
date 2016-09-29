package com.jszf.txsystem.core;

import com.jszf.txsystem.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wpj on 16/6/14下午6:06.
 */
public class HttpHelper {

    private static final int TIME_OUT_LIMIT = 10;
    private RxJavaCallAdapterFactory mAdapterFactory;
    private GsonConverterFactory mGsonConverterFactory;
    private OkHttpClient.Builder mBuilder;

    private HttpHelper() {

        mGsonConverterFactory = GsonConverterFactory.create();
        mAdapterFactory = RxJavaCallAdapterFactory.create();
        //设置超时时间
        mBuilder = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT_LIMIT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT_LIMIT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT_LIMIT, TimeUnit.SECONDS);
    }

    public static final HttpHelper getInstance() {
        return new HttpHelper();
    }

    public Retrofit getRetrofit() {
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            mBuilder.addInterceptor(loggingInterceptor);
        }
        return new Retrofit.Builder()
                .baseUrl(ApiRequestStores.BASE_URL)
                .addCallAdapterFactory(mAdapterFactory)
                .addConverterFactory(mGsonConverterFactory)
                .client(mBuilder.build())
                .build();

    }
    public Retrofit getRetrofit2() {
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            mBuilder.addInterceptor(loggingInterceptor);
        }
        return new Retrofit.Builder()
                .baseUrl(ApiRequestStores.BASE_URL2)
                .addCallAdapterFactory(mAdapterFactory)
                .addConverterFactory(mGsonConverterFactory)
                .client(mBuilder.build())
                .build();
    }

    private static final class LazyHolder {
        public static final HttpHelper INSTANCE = new HttpHelper();
    }
}
