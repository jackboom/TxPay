package com.jszf.txsystem.core;

import com.jszf.txsystem.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

public class NetworkModule {

    public OkHttpClient provideOkHttpClient(Cache cache, MashapeKeyInterceptor mashapeKeyInterceptor) {

        OkHttpClient okHttpClient = new OkHttpClient();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? BODY : NONE);

        OkHttpClient newClient = okHttpClient.newBuilder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(mashapeKeyInterceptor)
                .addNetworkInterceptor(new CacheInterceptor())
                .cache(cache)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        return newClient;
    }
}
