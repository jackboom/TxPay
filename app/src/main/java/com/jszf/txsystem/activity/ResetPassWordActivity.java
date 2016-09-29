package com.jszf.txsystem.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.ResetPasswordBean;
import com.jszf.txsystem.core.ApiRequestStores;
import com.jszf.txsystem.core.HttpHelper;
import com.jszf.txsystem.core.mvp.base.BaseActivity;
import com.jszf.txsystem.util.Constant;
import com.jszf.txsystem.util.CountDownTimerUtils;
import com.jszf.txsystem.util.ParaUtils;
import com.jszf.txsystem.util.RSAUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ResetPassWordActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.iv_reset_back)
    ImageView mIvResetBack;                 //返回
    @BindView(R.id.iv_icon_1)
    ImageView mIvIcon1;
    @BindView(R.id.tv1)
    TextView mTv1;
    @BindView(R.id.tv_reset_getCertifyCode)
    TextView mTvResetGetCertifyCode;        //验证码获取提示
    @BindView(R.id.tv_reset_time)
    TextView mTvResetTime;                  //计时
    @BindView(R.id.ll_reset_certify)
    LinearLayout mLlResetCertify;
    @BindView(R.id.edt_reset_certify)
    EditText mEdtResetCertify;              //验证码
    @BindView(R.id.iv_icon_2)
    ImageView mIvIcon2;
    @BindView(R.id.tv2)
    TextView mTv2;
    @BindView(R.id.edt_reset_newpassword1)
    EditText mEdtResetNewpassword1;         //新密码
    @BindView(R.id.iv_icon_3)
    ImageView mIvIcon3;
    @BindView(R.id.tv3)
    TextView mTv3;
    @BindView(R.id.edt_reset_newpassword2)
    EditText mEdtResetNewpassword2;         //再次输入新密码
    @BindView(R.id.btn_reset_submit)
    Button mBtnResetSubmit;                 //提交
    private CountDownTimerUtils mTimerUtils;
    private String certifyCode;
    private String newPassWord1;
    private String newPassWord2;
    private HashMap<String, String> resultMap;
    private String userName;
    private AlertDialog.Builder mBuilder;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_word);
        ButterKnife.bind(this);
        setStatusBar();
        mBuilder = new AlertDialog.Builder(this);
        initView();
        setCounTime();
    }

    private void setCounTime() {
//        mTimerUtils = new CountDownTimerUtils(tv_reset_time, tv_reset_getCertifyCode);
        mTimerUtils = new CountDownTimerUtils(mTvResetTime, mTvResetGetCertifyCode);
        mTimerUtils.start();
    }

    private void initView() {
        mEdtResetCertify.addTextChangedListener(this);
        mEdtResetNewpassword1.addTextChangedListener(this);
        mEdtResetNewpassword2.addTextChangedListener(this);
        mBtnResetSubmit.setClickable(false);
    }

    private void requestModifyPassword() {
        userName = getIntent().getStringExtra("userLoginName");
        certifyCode = mEdtResetCertify.getText().toString();
        newPassWord1 = mEdtResetNewpassword1.getText().toString();
        newPassWord2 = mEdtResetNewpassword2.getText().toString();
        showLoadingDialog();
        if (!newPassWord1.equals(newPassWord2)) {
            setDialog("两次密码不一致,请重新输入!");
            mEdtResetNewpassword1.setText("");
            mEdtResetNewpassword2.setText("");
            Toast.makeText(getApplicationContext(), "两次密码不一致,请重新输入!", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, String> map = getRequestParams();
        final ApiRequestStores apiStores = HttpHelper.getInstance().getRetrofit().create(ApiRequestStores.class);
        Observable<ResetPasswordBean> observable = apiStores.requestResetPassword(map);

        Subscription subscritiono = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResetPasswordBean>() {
                    @Override
                    public void onCompleted() {
                        dismissLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ResetPassWordActivity", "e:" + e);
                    }

                    @Override
                    public void onNext(ResetPasswordBean resetPasswordBean) {
                        String dealCode = resetPasswordBean.getDealCode();
                        if (!TextUtils.isEmpty(dealCode) && dealCode.equals(Constant.DEAL_CODE_SUCCESS)) {
                            setDialog("密码修改成功!");
                        }
                    }
                });
    }

    @NonNull
    private HashMap<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("service", "updatePassword");
        map.put("userLoginName", userName);
        map.put("message", certifyCode);
        if (!TextUtils.isEmpty(newPassWord2)) {
            try {
                String mEncyptWord = RSAUtils.encrypt(newPassWord2, Constant.txPublicKey);
                map.put("newPassWord",mEncyptWord);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        map.put("newPassWord", newPassWord2);
        map.put("version", "V1.0");
        String mPreSign = ParaUtils.createLinkString(map);
        //待签名字符串
        Log.d("TAG","Presign:"+mPreSign);
        //签名字符串
        String str = RSAUtils.sign(mPreSign, Constant.ownPrivateKey);
        try {
            String sign = URLEncoder.encode(str, "utf-8");
            map.put("sign",sign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        newPassWord1 = mEdtResetNewpassword1.getText().toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() > 16) {
            Toast.makeText(getApplicationContext(), "密码超过16位长度!", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(mEdtResetCertify.getText().toString()) ||
                TextUtils.isEmpty(mEdtResetNewpassword1.getText().toString()) ||
                TextUtils.isEmpty(mEdtResetNewpassword2.getText().toString())) {
            mBtnResetSubmit.setFocusable(false);
            mBtnResetSubmit.setClickable(false);
        } else {
            mBtnResetSubmit.setFocusable(true);
            mBtnResetSubmit.setClickable(true);
        }

    }

    private void setDialog(String message) {
        mBuilder.setMessage(message);
        mBuilder.setPositiveButton("确定", (dialog, which) -> {
            mBuilder.create().dismiss();
        });
        mBuilder.create().show();
    }

    private void setSuccessDialog(String message) {
        mBuilder.setMessage(message);
        mBuilder.setPositiveButton("确定", (dialog, which) -> {
            mBuilder.create().dismiss();
            startActivity(new Intent(ResetPassWordActivity.this, LoginActivity.class));
        });
        mBuilder.create().show();
    }

    @Override
    public boolean judgeNetWord(Context mContext) {
        return super.judgeNetWord(mContext);
    }

    @OnClick({R.id.iv_reset_back, R.id.tv_reset_getCertifyCode, R.id.btn_reset_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_reset_back:
                finish();
                break;
            case R.id.tv_reset_getCertifyCode:
                mTimerUtils.start();
                break;
            case R.id.btn_reset_submit:
                if (judgeNetWord(ResetPassWordActivity.this)) {
                    showLoadingDialog();
                    requestModifyPassword();
                } else {
                    setDialog("网络异常,请检查!");
                }
                break;
        }
    }
}
