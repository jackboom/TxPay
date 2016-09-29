package com.jszf.txsystem.core.mvp.base;

/**
 * @author jacking
 *         Created at 2016/8/31 14:51 .
 */
public interface BaseMvpView {
    void showLoading();

    void hideLoading();

    void showToast(String msg);
}
