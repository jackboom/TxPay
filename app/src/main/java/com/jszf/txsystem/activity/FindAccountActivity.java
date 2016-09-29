package com.jszf.txsystem.activity;

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
import android.widget.Toast;

import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.MessageBean;
import com.jszf.txsystem.core.ApiRequestStores;
import com.jszf.txsystem.core.HttpHelper;
import com.jszf.txsystem.core.mvp.base.BaseActivity;
import com.jszf.txsystem.util.Constant;
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

public class FindAccountActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.iv_find_back)
    ImageView mIvFindBack;
    @BindView(R.id.edt_find_name)
    EditText mEdtFindName;
    @BindView(R.id.btn_find_getverification)
    Button mBtnFindGetverification;
    private String userName;
    private Handler mHandler = new Handler();
    private HashMap<String, String> mMap;
    private HashMap<String, String> resultMap;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);
        ButterKnife.bind(this);
        setStatusBar();
        initView();
    }

    private void initView() {
        mBtnFindGetverification.setClickable(false);
        mEdtFindName.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s != null) {
            mBtnFindGetverification.setClickable(true);
            mBtnFindGetverification.setFocusable(true);
            mBtnFindGetverification.setOnClickListener(this);
            mBtnFindGetverification.setAlpha(1f);
            mBtnFindGetverification.setBackgroundColor(getResources().getColor(R.color.home_red));
        } else {
            mBtnFindGetverification.setBackgroundColor(getResources().getColor(R.color.darker_gray));
            mBtnFindGetverification.setAlpha(0.1f);
            mBtnFindGetverification.setClickable(false);
            mBtnFindGetverification.setFocusable(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().equals("") || s == null) {
            mBtnFindGetverification.setBackgroundColor(getResources().getColor(R.color.darker_gray));
            mBtnFindGetverification.setClickable(false);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    @OnClick({R.id.iv_find_back, R.id.btn_find_getverification})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_find_back:
                finish();
                break;
            case R.id.btn_find_getverification:
                userName = mEdtFindName.getText().toString();
                showLoadingDialog();
                requestSendMessage();

//                OkHttpUtils.post()
//                        .url(Constant.PATH)
//                        .params(mMap)
//                        .build()
//                        .connTimeOut(5000)
//                        .execute(new Callback() {
//                            @Override
//                            public Object parseNetworkResponse(Response mResponse) throws Exception {
//                                return mResponse.body().string();
//                            }
//
//                            @Override
//                            public void onError(Call mCall, Exception e) {
//
//                            }
//
//                            @Override
//                            public void onResponse(Call mCall, Object o) {
//                                String result = o.toString();
//                                try {
//                                    JSONObject mJSONObject = new JSONObject(result);
//                                    String dealCode = mJSONObject.getString("dealCode");
//                                    dismissLoading();
//                                    if (dealCode.equals(Constant.DEAL_CODE_SUCCESS)) {
//
//                                        mHandler.postDelayed(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                Intent mIntent = new Intent(FindAccountActivity.this, ResetPassWordActivity.class);
//                                                mIntent.putExtra("userLoginName", userName);
//                                                startActivity(mIntent);
//                                            }
//                                        }, 1000);
//                                        Toast.makeText(getApplicationContext(), "短信验证码发送成功", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(getApplicationContext(), "登录名不正确!", Toast.LENGTH_SHORT).show();
//                                        return;
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                break;
        }
    }

    private void requestSendMessage() {
        HashMap<String, String> map = getRequestParams();
        if (!judgeNetWord(FindAccountActivity.this)) {
            dismissLoading();
            Toast.makeText(getApplicationContext(), "网络连接异常,请检查!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            final ApiRequestStores apiStores = HttpHelper.getInstance().getRetrofit().create(ApiRequestStores.class);
            Observable<MessageBean> observable = apiStores.requestSendMessage(map);
            subscription = observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<MessageBean>() {
                        @Override
                        public void onCompleted() {
                            dismissLoading();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("FindAccountActivity", "--------" + e.getMessage().toString());
                        }

                        @Override
                        public void onNext(MessageBean messageBean) {
                            String dealCode = messageBean.getDealCode();
                            if (!TextUtils.isEmpty(dealCode) && dealCode.equals(Constant.DEAL_CODE_SUCCESS)) {
                                mHandler.postDelayed(() -> {
                                    Intent mIntent = new Intent(FindAccountActivity.this, ResetPassWordActivity.class);
                                    mIntent.putExtra("userLoginName", userName);
                                    startActivity(mIntent);
                                }, 1000);
                                Toast.makeText(getApplicationContext(), "短信验证码发送成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "登录名不正确!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
            subscription.unsubscribe();
        }catch (Exception e) {
            Log.d("FindAccountActivity", e.getMessage());
        }
    }

    @NonNull
    private HashMap<String, String> getRequestParams() {
        HashMap<String,String> map = new HashMap<>();
        map.put("service", "getMessageInfo");
        map.put("userLoginName", userName);
        map.put("version", "V1.0");
        map.put("usage", "updatePassword");
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

}
