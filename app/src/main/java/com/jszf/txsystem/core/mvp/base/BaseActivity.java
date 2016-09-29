package com.jszf.txsystem.core.mvp.base;

import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jszf.txsystem.R;
import com.jszf.txsystem.util.ProgressHUD;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zhy.autolayout.AutoLayoutActivity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/4/22.
 */
public class BaseActivity extends AutoLayoutActivity implements View.OnClickListener {
    private android.app.AlertDialog.Builder mBuilder;
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetworkInfo;
    private Handler mHandler = new Handler();
    private static boolean isConnected = false;
    private ProgressHUD mProgressHUD;   //进度框对象


// Establish connection to the proxy.

    private BluetoothHeadset mBluetoothHeadset;
    private BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (profile == BluetoothProfile.HEADSET) {
                mBluetoothHeadset = (BluetoothHeadset) proxy;
                isConnected = true;
            }
            Toast.makeText(getApplicationContext(),"连接",Toast.LENGTH_LONG).show();
        }
        public void onServiceDisconnected(int profile) {
            if (profile == BluetoothProfile.HEADSET) {
                mBluetoothHeadset = null;
                isConnected = false;

            }
            Toast.makeText(getApplicationContext(),"断开连接",Toast.LENGTH_LONG).show();
        }
    };
    public void showLoadingDialog(){
        mProgressHUD.show();
    }
    public void dismissLoading(){
        mProgressHUD.dismiss();
    }
    public BroadcastReceiver mybroad = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("TAG", "退出APP");
            finish();
        }
    };

    // 当发现是完成了，BroadcastReceiver监听发现设备和更改标题
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

        }
    };

    private View mStatusBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐去标题栏（应用程序的名字）
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        ActivityTerminator.add(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("Exit APP");
        this.registerReceiver(mybroad, filter);
        //初始化loading加载对话框
        mProgressHUD  = new ProgressHUD(this);
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public boolean judgeNetWord(Context mContext) {
        mConnectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isAvailable() && mNetworkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        onUnsubscribe();
        super.onDestroy();
        unregisterReceiver(mybroad);
    }


    public void setStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint 激活导航栏
            tintManager.setNavigationBarTintEnabled(false);
            //设置系统栏设置颜色
            //tintManager.setTintColor(R.color.red);
            //给状态栏设置颜色
            tintManager.setStatusBarTintResource(R.color.statusBar);
            //Apply the specified drawable or color resource to the system navigation bar.
            //给导航栏设置资源
//            tintManager.setNavigationBarTintResource(R.color.mask_tags_1);
        }
    }

    private CompositeSubscription mCompositeSubscription;

    public void onUnsubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();//取消注册，以避免内存泄露
        }
    }

    public void addSubscription(Subscription subscription) {
//        if (mCompositeSubscription == null) {
        mCompositeSubscription = new CompositeSubscription();
//        }
        mCompositeSubscription.add(subscription);
    }
}
