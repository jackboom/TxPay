package com.jszf.txsystem.core.mvp.base;

/**
 * @author jacking
 *         Created at 2016/8/31 13:42 .
 */
public class BasePresenters<V> {
    public V mView;
    public BasePresenters(V v) {
        mView = v;
    }

    public void onDestory() {
        mView = null;
    }
}
