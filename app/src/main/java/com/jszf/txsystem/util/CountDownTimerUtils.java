package com.jszf.txsystem.util;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.jszf.txsystem.R;

/**
 * Created by Administrator on 2016/4/29.
 */
public class CountDownTimerUtils extends CountDownTimer {
    public static final int TIME_COUNT = 60500;//时间防止从119s开始显示（以倒计时120s为例子）
    private TextView tv_certify_show;
    private TextView tv_time;
    private int endStrRid;
//    private int normalColor, timingColor;//未计时的文字颜色，计时期间的文字颜色

    /**
     * 参数 millisInFuture         倒计时总时间（如60S，120s等）
     * 参数 countDownInterval    渐变时间（每次倒计1s）
     * 参数 tv_certify_show               点击的按钮
     * 参数 tv_time               计时显示
     * 参数 endStrRid   倒计时结束后，按钮对应显示的文字
     */
    public CountDownTimerUtils (long millisInFuture, long countDownInterval, TextView tv_time,TextView mTv_certify_show, int endStrRid) {
        super(millisInFuture, countDownInterval);
        this.tv_time = tv_time;
        this.tv_certify_show = mTv_certify_show;
        this.endStrRid = endStrRid;
    }


    /**

     *参数上面有注释
     */
    public  CountDownTimerUtils (TextView tv_time, int endStrRid) {
        super(TIME_COUNT, 1000);
        this.tv_time = tv_time;
        this.endStrRid = endStrRid;
    }

    public CountDownTimerUtils (TextView tv_time,TextView mTv_certify_show) {
        super(TIME_COUNT, 1000);
        this.tv_time = tv_time;
        this.tv_certify_show = mTv_certify_show;
        this.endStrRid = R.string.text_getCerCode;
    }


    // 计时完毕时触发
    @Override
    public void onFinish() {
        tv_certify_show.setText(endStrRid);
        tv_certify_show.setEnabled(true);
        tv_time.setText("");

    }

    // 计时过程显示
    @Override
    public void onTick(long millisUntilFinished) {
        tv_certify_show.setEnabled(false);
        tv_time.setText("("+millisUntilFinished / 1000 + "s)");
    }
}
