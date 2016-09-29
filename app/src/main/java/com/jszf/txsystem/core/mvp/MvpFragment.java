package com.jszf.txsystem.core.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jszf.txsystem.core.mvp.base.BaseFragment;
import com.jszf.txsystem.core.mvp.base.BaseMvpView;
import com.jszf.txsystem.core.mvp.base.BasePresenters;
import com.jszf.txsystem.util.ProgressHUD;

/**
 * @author jacking
 *         Created at 2016/8/30 10:17 .
 */

public abstract class MvpFragment<V extends BaseMvpView, P extends BasePresenters<V>> extends BaseFragment
        implements BaseMvpView {
    protected P mvpPresenter;
    private ProgressHUD mProgressHUD;   //进度框对象

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //初始化loading加载对话框
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected abstract P createPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mvpPresenter != null) {
            mvpPresenter.onDestory();
            mvpPresenter.mView = null;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        mProgressHUD  = new ProgressHUD(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    public void showLoadingDialog(){
        mProgressHUD.show();
    }
    public void dismissLoading(){
        mProgressHUD.dismiss();
    }
}
