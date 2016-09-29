package com.jszf.txsystem.cache;


import com.jszf.txsystem.bean.TextBean;

import rx.Observable;

/**
 * Created by wpj on 16/6/13下午4:32.
 */
public interface ICache {

    <T extends TextBean> Observable<T> get(String key, Class<T> cls);

    <T extends TextBean> void put(String key, T t);
}
