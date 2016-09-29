package com.jszf.txsystem.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.Merchant;
import com.jszf.txsystem.core.mvp.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayActivity extends BaseActivity {
    @BindView(R.id.tv_pay_amount)
    TextView mTvPayAmount;
    @BindView(R.id.iv_pay_one)
    ImageView mIvPayOne;
    @BindView(R.id.iv_pay_two)
    ImageView mIvPayTwo;
    @BindView(R.id.iv_pay_three)
    ImageView mIvPayThree;
    @BindView(R.id.iv_pay_four)
    ImageView mIvPayFour;
    @BindView(R.id.img_pay_five)
    ImageView mImgPayFive;
    @BindView(R.id.iv_pay_six)
    ImageView mIvPaySix;
    @BindView(R.id.iv_pay_seven)
    ImageView mIvPaySeven;
    @BindView(R.id.iv_pay_eight)
    ImageView mIvPayEight;
    @BindView(R.id.iv_pay_nine)
    ImageView mIvPayNine;
    @BindView(R.id.iv_pay_point)
    ImageView mIvPayPoint;
    @BindView(R.id.iv_pay_zero)
    ImageView mIvPayZero;
    @BindView(R.id.iv_pay_delete)
    ImageView mIvPayDelete;
    @BindView(R.id.iv_pay_submit)
    ImageView mIvPaySubmit;
    @BindView(R.id.pay_bg)
    LinearLayout mPayBg;
    @BindView(R.id.iv_pay_back)
    ImageView mIvPayBack;

    Drawable[] weichatDrawArr = new Drawable[10];
    Drawable[] alipayDrawArr = new Drawable[10];
    Drawable[] bdDrawArr = new Drawable[10];
    Drawable[] yzfDrawArr = new Drawable[10];

    ImageView[] ivArr;
    private Intent mIntent;
    private int payType;    //支付方式
    private String count;    //参与计算的数或符号
    private String amount;  //提交的订单金额
    private AlertDialog.Builder mBuilder;
    private int orderAmount;
    private Merchant mMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay2);
        ButterKnife.bind(this);
        mBuilder = new AlertDialog.Builder(this);
        init();
        setView();
    }

    private void init() {
        mIntent = getIntent();
        payType = mIntent.getIntExtra("payType", 0);
        Log.d("PayActivity", "----------PayActivity---------类型：payType:" + payType);
        mMerchant = (Merchant) mIntent.getSerializableExtra("merchantInfo");
        ivArr = new ImageView[]{mIvPayZero,mIvPayOne,mIvPayTwo,mIvPayThree,mIvPayFour,mImgPayFive,
        mIvPaySix,mIvPaySeven,mIvPayEight,mIvPayNine};
        for (int i = 0; i < 10; i++) {
            int id1 = getResources().getIdentifier("selector_pay_wechat_"+i,"drawable",getApplicationInfo().packageName);
            int id2 = getResources().getIdentifier("selector_pay_alipay_"+i,"drawable",getApplicationInfo().packageName);
            int id3 = getResources().getIdentifier("selector_pay_baidu_"+i,"drawable",getApplicationInfo().packageName);
            int id4 = getResources().getIdentifier("selector_pay_yzf_"+i,"drawable",getApplicationInfo().packageName);
            weichatDrawArr[i] = getResources().getDrawable(id1);
            alipayDrawArr[i] = getResources().getDrawable(id2);
            bdDrawArr[i] = getResources().getDrawable(id3);
            yzfDrawArr[i] = getResources().getDrawable(id4);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mTvPayAmount.setText("");
    }

    private void setView() {
        switch (payType) {
            case 1:
                for (int i = 0; i < 10; i++) {
                    ivArr[i].setImageDrawable(weichatDrawArr[i]);
                }

                mPayBg.setBackground(getResources().getDrawable(R.drawable.pay_wechat_bg));
                mIvPayPoint.setImageDrawable(getResources().getDrawable(R.drawable.selector_pay_wechat_point));
                mIvPayDelete.setImageDrawable(getResources().getDrawable(R.drawable.pay_wechat_delete));
                mIvPaySubmit.setImageDrawable(getResources().getDrawable(R.drawable.pay_wechat_submit));
                break;
            case 2:
                Log.d("PayActivity", "payType:" + payType);
                for (int i = 0; i < 10; i++) {
                    ivArr[i].setImageDrawable(alipayDrawArr[i]);
                }

                mPayBg.setBackground(getResources().getDrawable(R.drawable.pay_alipay_bg));
                mIvPayPoint.setImageDrawable(getResources().getDrawable(R.drawable.selector_pay_alipay_point));
                mIvPayDelete.setImageDrawable(getResources().getDrawable(R.drawable.pay_alipay_delete));
                mIvPaySubmit.setImageDrawable(getResources().getDrawable(R.drawable.selector_pay_alipay));

                break;
            case 3:
                for (int i = 0; i < 10; i++) {
                    ivArr[i].setImageDrawable(yzfDrawArr[i]);


                }

                mPayBg.setBackgroundResource(R.drawable.pay_yzf_bg);
                mIvPayPoint.setImageDrawable(getResources().getDrawable(R.drawable.selector_pay_yzf_point));
                mIvPayDelete.setImageDrawable(getResources().getDrawable(R.drawable.pay_yzf_delete));
                mIvPaySubmit.setImageDrawable(getResources().getDrawable(R.drawable.selector_pay_yzf));

                break;
            case 4:
                for (int i = 0; i < 10; i++) {
                    ivArr[i].setImageDrawable(bdDrawArr[i]);
                }

                mPayBg.setBackgroundResource(R.drawable.pay_bd_bg);
                mIvPayPoint.setImageDrawable(getResources().getDrawable(R.drawable.selector_pay_baidu_point));
                mIvPayDelete.setImageDrawable(getResources().getDrawable(R.drawable.pay_bd_delete));
                mIvPaySubmit.setImageDrawable(getResources().getDrawable(R.drawable.selector_pay_baidu));

                break;
            case 5:

                break;
        }
    }
    private boolean validAmount() {
//        equal();
        amount = mTvPayAmount.getText().toString().trim();
//        double str = Double.parseDouble(amount);
//        orderAmount = (int) (str *100);
        if (amount.equals("0.00") || amount.equals("0") || TextUtils.isEmpty(amount)) {
            mBuilder.setMessage("输入金额为空,请重新输入!");
            mBuilder.setPositiveButton("确定", (dialog, which) -> {
                mBuilder.create().dismiss();
            });
            mBuilder.create().show();
            return true;
        }
        return false;
    }

    private void codeProduct() {
        Intent mIntent = new Intent(PayActivity.this, QrCodeProductActivity.class);
        mIntent.putExtra("type", payType);
        mIntent.putExtra("amount", amount);
        mIntent.putExtra("merchantInfo", mMerchant);
        startActivity(mIntent);
    }

    // 获取输入等式
    public String getTextView() {
        return mTvPayAmount.getText().toString();
    }

    // 设置输入等式
    public void setTextView(String str) {
        mTvPayAmount.setText(str);
    }

    // 回退
    public void backCount() {
        // 处理字符串
        String strCount = mTvPayAmount.getText().toString().trim();
        StringBuffer sb = new StringBuffer();
        sb.append(strCount);
        Log.d("PayActivity", "strCount:"+strCount);
//        if ( strCount.equals("0") || strCount.length() ==1) {
//            mTvPayAmount.setText(getResources().getString(R.string.text_original_monty2));
//        } else {
        if (!TextUtils.isEmpty(mTvPayAmount.getText().toString())) {
            sb.deleteCharAt(sb.length()-1);
            Log.d("PayActivity", "sb.toString():" + sb.toString());
            mTvPayAmount.setText(sb.toString());
        }
//        }
    }

    // 传入R.id，获取R.id的Text
    public void setValues(String count) {
        if (mTvPayAmount.getText().toString().equals(".") && mTvPayAmount.getText().toString().startsWith(".")) {
            mTvPayAmount.setText(mTvPayAmount.getText().toString().replace(".","0."));
        }
        String setTxtView = TextView(count);
        setTextView(setTxtView);
    }

    // 返回追加R.id后的String
    public String TextView(String str) {
        String strCount = getTextView();
        StringBuffer countBuf = new StringBuffer(strCount);
        String strapp = countBuf.append(str).toString();
        return strapp;
    }

    /**
     * 设置状态栏背景状态
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = this.getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            if (color == 0) {
                window.setStatusBarColor(getResources().getColor(R.color.home_red));
            } else {
                window.setStatusBarColor(getResources().getColor(color));
            }
            ViewGroup mContentView = (ViewGroup) this.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PayActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.iv_pay_back, R.id.iv_pay_one, R.id.iv_pay_two, R.id.iv_pay_three,
            R.id.iv_pay_four, R.id.img_pay_five, R.id.iv_pay_six, R.id.iv_pay_seven,
            R.id.iv_pay_eight, R.id.iv_pay_nine, R.id.iv_pay_point, R.id.iv_pay_zero,
            R.id.iv_pay_delete, R.id.iv_pay_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_pay_back:
                Intent intent = new Intent(PayActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.iv_pay_one:
                count = "1";
                setValues(count);
                break;
            case R.id.iv_pay_two:
                count = "2";
                setValues(count);
                break;
            case R.id.iv_pay_three:
                count = "3";
                setValues(count);
                break;
            case R.id.iv_pay_four:
                count = "4";
                setValues(count);
                break;
            case R.id.img_pay_five:
                count = "5";
                setValues(count);
                break;
            case R.id.iv_pay_six:
                count = "6";
                setValues(count);
                break;
            case R.id.iv_pay_seven:
                count = "7";
                setValues(count);
                break;
            case R.id.iv_pay_eight:
                count = "8";
                setValues(count);
                break;
            case R.id.iv_pay_nine:
               count = "9";
                setValues(count);
                break;
            case R.id.iv_pay_point:
                count = ".";
                setValues(count);
                break;
            case R.id.iv_pay_zero:
                count = "0";
                setValues(count);
                break;
            case R.id.iv_pay_delete:
                backCount(); // 清空计算框内容
                Log.d("PayActivity", "回退");
                break;
            case R.id.iv_pay_submit:
                if (validAmount()) return;
                codeProduct();
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
        }
    }

}
