package com.jszf.txsystem.cache;

import com.jszf.txsystem.bean.TextBean;

import rx.Observable;

/**
 * Created by wpj on 16/6/13下午4:43.
 */
public abstract class NetworkCache<T extends TextBean> {

    public abstract Observable<T> get(String key, final Class<T> cls);
}