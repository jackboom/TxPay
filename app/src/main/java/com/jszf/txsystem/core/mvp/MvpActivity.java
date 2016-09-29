package com.jszf.txsystem.core.mvp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.jszf.txsystem.R;
import com.jszf.txsystem.core.mvp.base.BaseActivity;
import com.jszf.txsystem.core.mvp.base.BaseMvpView;
import com.jszf.txsystem.core.mvp.base.BasePresenters;

/**
 * @author jacking
 *         Created at 2016/8/29 11:52 .
 */
public abstract class MvpActivity<V extends BaseMvpView,P extends BasePresenters<V>>
        extends BaseActivity implements BaseMvpView{
    protected P mvpPresenter;
    private Activity mActivity;
//    private ProgressHUD mProgressHUD;   //进度框对象


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        super.onCreate(savedInstanceState);
        mActivity = this;
//        //初始化loading加载对话框
//        mProgressHUD  = new ProgressHUD(this);
        //网络状态监听
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.initData();
    }


    protected abstract void initData();

    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        if (null != mvpPresenter) {
            mvpPresenter.onDestory();
        }
        super.onDestroy();
        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver);
        }
    }
    public void toast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                mvpPresenter.mView.hideLoading();
                toast(getString(R.string.toast_no_network));
            }else {
            }
        }
    };

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showToast(String msg) {
        Log.d("MvpActivity", "----------------------" + msg);
        if (!msg.equals("java.net.UnknownHostException: Unable to resolve host " +
                "\"api.tongxingpay.com\": No address associated with hostname")){
            toast(msg);
        }
    }
}
