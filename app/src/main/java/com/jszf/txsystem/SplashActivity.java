package com.jszf.txsystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.jszf.txsystem.activity.LoginActivity;
import com.jszf.txsystem.core.mvp.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView splash = (ImageView) findViewById(R.id.splash);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
//        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
//        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        Log.d("SplashActivity", "width:" + width+",height:"+height);
        Drawable mDrawable = null;
        if (width  == 768) {
            mDrawable = getResources().getDrawable(R.drawable.splash_720_1280);
//            splash.setBackgroundResource(R.drawable.splash_768_1280);

        } else if (width == 1080) {
            mDrawable = getResources().getDrawable(R.drawable.splash_1080_1920);

//            splash.setBackgroundResource(R.drawable.splash_1080_1920);
        }else {
            mDrawable = getResources().getDrawable(R.drawable.splash_1080_1920);

//            splash.setBackgroundResource(R.drawable.splash_1080_1920);
        }
        splash.setBackground(mDrawable);
        setAnimation();
    }
    /**
     * 判断是否为平板
     *
     * @return
     */
    private boolean isPad() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        // 屏幕宽度
        float screenWidth = display.getWidth();
        // 屏幕高度
        float screenHeight = display.getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        if (x >= y){
            return true;
        }
//        // 屏幕尺寸
//        double screenInches = Math.sqrt(x + y);
//        // 大于6尺寸则为Pad
//        if (screenInches >= 8.0) {
//            return true;
//        }
        return false;
    }
    private void setAnimation() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
               startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                Intent mIntent = new Intent(SplashActivity.this,LoginActivity.class);
//                //如果启动app的Intent中带有额外的参数，表明app是从点击通知栏的动作中启动的
//                //将参数取出，传递到MainActivity中
//                if(getIntent().getBundleExtra(Constant.EXTRA_BUNDLE) != null){
//                    mIntent.putExtra(Constant.EXTRA_BUNDLE,
//                            getIntent().getBundleExtra(Constant.EXTRA_BUNDLE));
//                }
                finish();
                //两个参数分别表示进入的动画,退出的动画
                overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);
            }
        }.execute(new Void[]{});
    }


}
