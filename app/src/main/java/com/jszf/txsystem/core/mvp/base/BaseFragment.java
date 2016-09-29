package com.jszf.txsystem.core.mvp.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.jszf.txsystem.R;
import com.jszf.txsystem.util.ProgressHUD;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by Administrator on 2016/4/27.
 */
public class BaseFragment<P extends BasePresenters> extends Fragment implements View.OnClickListener{
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetworkInfo;
    private ProgressHUD mProgressHUD;
    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressHUD  = new ProgressHUD(getActivity());
    }

    @Override
    public void onClick(View v) {
    }

    /**
     *toolbar 设置颜色
     */
    public void setStatus(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH){
            // 创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            // 激活导航栏设置
            tintManager.setNavigationBarTintEnabled(false);
            tintManager.setStatusBarTintResource(R.color.statusBar);
        }
    }

    /**
     * 判断网络状态
     * @param mContext
     * @return
     */
    public boolean judgeNetWord(Context mContext) {
        mConnectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isAvailable() &&mNetworkInfo.isConnected()) {
            return true;
        }else {
            return false;
        }
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
    /**
     * 设置状态栏背景状态
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            Window window = getActivity().getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            if (color == 0) {
                window.setBackgroundDrawable(getResources().getDrawable(R.drawable.zhuangtai));
            } else {
                window.setBackgroundDrawable(getResources().getDrawable(R.drawable.zhuangtai));
            }
            ViewGroup mContentView = (ViewGroup) getActivity().findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
        }
    }

    public void showLoadingDialog(){
        mProgressHUD.show();
    }
    public void dismissLoading(){
        mProgressHUD.dismiss();
    }
}
