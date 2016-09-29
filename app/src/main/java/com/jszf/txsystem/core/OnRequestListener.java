package com.jszf.txsystem.core;

/**
 * Created by wpj on 16/6/8下午2:06.
 */
public interface OnRequestListener<T> {

    void onLoadCompleted(T t);

    void onLoadFailed(String errMsg);
}
