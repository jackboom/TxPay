package com.jszf.txsystem.core;

/**
 * Created by Administrator on 2016/8/29.
 */
public interface ApiRequestBack<T> {
    void onSuccess(T model);

    void onFailure(int code, String msg);

    void onCompleted();
}
