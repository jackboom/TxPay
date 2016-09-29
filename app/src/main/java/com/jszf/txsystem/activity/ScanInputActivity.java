package com.jszf.txsystem.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jszf.txsystem.R;
import com.jszf.txsystem.core.mvp.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanInputActivity extends BaseActivity {

    @BindView(R.id.tv_scan_amount)
    TextView mTvScanAmount; //显示金额
    @BindView(R.id.img_scan_one)
    ImageView mImgScanOne;
    @BindView(R.id.img_scan_two)
    ImageView mImgScanTwo;
    @BindView(R.id.img_scan_three)
    ImageView mImgScanThree;
    @BindView(R.id.img_scan_four)
    ImageView mImgScanFour;
    @BindView(R.id.img_scan_five)
    ImageView mImgScanFive;
    @BindView(R.id.img_scan_six)
    ImageView mImgScanSix;
    @BindView(R.id.img_scan_seven)
    ImageView mImgScanSeven;
    @BindView(R.id.img_scan_eight)
    ImageView mImgScanEight;
    @BindView(R.id.img_scan_nine)
    ImageView mImgScanNine;
    @BindView(R.id.img_scan_point)
    ImageView mImgScanPoint;
    @BindView(R.id.img_scan_zero)
    ImageView mImgScanZero;
    @BindView(R.id.img_scan_back)
    ImageView mImgScanBack;     //回退
    @BindView(R.id.img_scan_xiala)
    ImageView mImgScanXiala;    //下拉
    @BindView(R.id.img_scan_submit)
    ImageView mImgScanSubmit;
    @BindView(R.id.img_scan_chongzhi)
    ImageView mImgScanChongzhi;
    private AlertDialog.Builder mBuilder;
    private ImageView[] ivArr;
    private Drawable[] mDrawables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_input);
        ButterKnife.bind(this);
        init();
//        setBar(0);
        mBuilder = new AlertDialog.Builder(this);

    }

    private void init() {
        mDrawables = new Drawable[]{mImgScanBack.getDrawable(),mImgScanChongzhi.getDrawable(),mImgScanEight.getDrawable(),
        mImgScanFive.getDrawable(),mImgScanFour.getDrawable(),mImgScanZero.getDrawable(),mImgScanXiala.getDrawable(),
        mImgScanTwo.getDrawable(),mImgScanThree.getDrawable(),mImgScanSubmit.getDrawable(),mImgScanSix.getDrawable(),
        mImgScanSeven.getDrawable(),mImgScanNine.getDrawable(),mImgScanPoint.getDrawable()};
    }

    public void setBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            try {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                ViewGroup mContentView = (ViewGroup) this.findViewById(Window.ID_ANDROID_CONTENT);
                View mChildView = mContentView.getChildAt(0);
                if (mChildView != null) {
                    //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
                    ViewCompat.setFitsSystemWindows(mChildView, true);
                }
            } catch (Exception e) {
                Log.d("TAG", "ER:" + e.toString());
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(ScanInputActivity.this, HomeActivity.class));
        for (int i = 0;i < mDrawables.length;i++){
            mDrawables[i].setCallback(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTvScanAmount.setText("");
    }

    @OnClick({ R.id.img_scan_one, R.id.img_scan_two, R.id.img_scan_three,
            R.id.img_scan_four, R.id.img_scan_five, R.id.img_scan_six, R.id.img_scan_seven,
            R.id.img_scan_eight, R.id.img_scan_nine, R.id.img_scan_point, R.id.img_scan_zero,
            R.id.img_scan_back, R.id.img_scan_xiala, R.id.img_scan_submit, R.id.img_scan_chongzhi})
    public void onClick(View view) {
        String amount = mTvScanAmount.getText().toString();
        String value = "";
        switch (view.getId()) {
            case R.id.img_scan_one:
                value = "1";
                setValues(value);
                break;
            case R.id.img_scan_two:
                value = "2";
                setValues(value);
                break;
            case R.id.img_scan_three:
                value = "3";
                setValues(value);
                break;
            case R.id.img_scan_four:
                value = "4";
                setValues(value);
                break;
            case R.id.img_scan_five:
                value = "5";
                setValues(value);
                break;
            case R.id.img_scan_six:
                value = "6";
                setValues(value);
                break;
            case R.id.img_scan_seven:
                value = "7";
                setValues(value);
                break;
            case R.id.img_scan_eight:
                value = "8";
                setValues(value);
                break;
            case R.id.img_scan_nine:
                value = "9";
                setValues(value);
                break;
            case R.id.img_scan_point:
                value = ".";
                setValues(value);
                break;
            case R.id.img_scan_zero:
                value = "0";
                setValues(value);
                break;
            case R.id.img_scan_back:
                //回退
                if (!TextUtils.isEmpty(amount)) {
                    mTvScanAmount.setText(amount.substring(0, amount.length() - 1));
                }
                break;
            case R.id.img_scan_xiala:
                startActivity(new Intent(ScanInputActivity.this,HomeActivity.class));
                finish();
                overridePendingTransition(R.anim.outdowntoup,R.anim.inuptodown);
                break;
            case R.id.img_scan_submit:
                if (amount.equals("0.00") || amount.equals("0") || TextUtils.isEmpty(amount)) {
                    mBuilder.setMessage("输入金额为空,请重新输入!");
                    mBuilder.setPositiveButton("确定", (dialog, which) -> {
                        mBuilder.create().dismiss();
                    });
                    mBuilder.create().show();
                    return;
                }
                Intent mIntent = new Intent(ScanInputActivity.this, CaptureActivity.class);
                mIntent.putExtra("type", "5");
                mIntent.putExtra("amount", amount);
                startActivity(mIntent);
                break;
            case R.id.img_scan_chongzhi:
                    mTvScanAmount.setText("");
                break;
        }
    }

    private void setValues(String mValue) {
        String preString = mTvScanAmount.getText().toString();
        if (preString.equals(".") && preString.startsWith(".")) {
            mTvScanAmount.setText(mTvScanAmount.getText().toString().replace(".","0."));
        }
        StringBuffer sb = new StringBuffer();
        sb.append(mTvScanAmount.getText().toString())
        .append(mValue);
        mTvScanAmount.setText(sb.toString());
    }

}
