package com.jszf.txsystem.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jszf.txsystem.MyApplication;
import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.BillInfo;
import com.jszf.txsystem.bean.Merchant;
import com.jszf.txsystem.bean.RefundBean;
import com.jszf.txsystem.core.ApiRequestStores;
import com.jszf.txsystem.core.HttpHelper;
import com.jszf.txsystem.core.mvp.base.BaseActivity;
import com.jszf.txsystem.ui.MyAlertDialog;
import com.jszf.txsystem.util.Constant;
import com.jszf.txsystem.util.MD5Utils;
import com.jszf.txsystem.util.ParaUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RefundActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.iv_refund_back)
    ImageView mIvRefundBack;
    @BindView(R.id.tv_bill_title)
    TextView mTvBillTitle;
    @BindView(R.id.rl_bill_title)
    RelativeLayout mRlBillTitle;
    @BindView(R.id.edt_refund_refundAmount)
    EditText mEdtRefundRefundAmount;
    @BindView(R.id.edt_refund_explanation)
    EditText mEdtRefundExplanation;
    @BindView(R.id.btn_refund_submit)
    Button mBtnRefundSubmit;
    private SimpleDateFormat sf;
    private String refundTime;   //订单时间
    private HashMap<String, String> resultMap;
    private String merchantNo;  //商户号
    private String refundNo;    //退款单号
    private String orderNo;     //订单号
    private String refundAmount;    //退款金额
    private String dealCode;    //处理结果代码
    private Merchant mMerchant;
    private BillInfo mBillInfo;
    private int amount;
    private AlertDialog.Builder mBuilder;
    private int payAmount;   //付款金额
    private String description; //说明
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);
        ButterKnife.bind(this);
        setStatusBar();

        mBuilder = new AlertDialog.Builder(this);
        initView();
    }

    private void initView() {
        Intent mIntent = getIntent();
        orderNo = mIntent.getStringExtra("orderNo");
        payAmount = mIntent.getIntExtra("amount", 0);
        Log.d("TAG", "a:" + payAmount);
        mMerchant = (Merchant) mIntent.getSerializableExtra("merchantInfo");
        mEdtRefundRefundAmount.setHint("本次最多可退款金额:  " + ((double) payAmount / 100) + "元");
        mEdtRefundRefundAmount.addTextChangedListener(this);
        mBtnRefundSubmit = (Button) findViewById(R.id.btn_refund_submit);
//        btn_refund_submit.setOnClickListener(this);
    }

    private boolean validRefundAmount() {
        refundAmount = mEdtRefundRefundAmount.getText().toString().trim();
        description = mEdtRefundExplanation.getText().toString().trim();
        if (!TextUtils.isEmpty(refundAmount)) {
            double str = Double.parseDouble(refundAmount);
            amount = (int) (str * 100);
//            int maxAmount = Integer.parseInt(payAmount);
            if (amount == 0) {
                setDialog2("退款金额不能为0!");
                return true;
            } else if (amount > payAmount) {
                setDialog2("退款金额不能超过订单金额" + (double) payAmount / 100 + "元!");
                mEdtRefundRefundAmount.setText("");
                mEdtRefundExplanation.setText("");
                return true;
            }
        } else {
            setDialog2("退款金额不能为空");
            return true;
        }
        return false;
    }

    /**
     * 退款请求
     */
    private void requestRefund() {
        Date mDate = new Date();
        sf = new SimpleDateFormat("yyyyMMddHHmmss");
        refundTime = sf.format(mDate);
        refundNo = refundTime;

        final HashMap<String, String> map = new HashMap<>();
        merchantNo = MyApplication.merchantNo;

        if (!TextUtils.isEmpty(description)) {
            map.put("refundDesc", description);
        }
        map.put("service", "offRefundOrder");
        map.put("merchantNo", merchantNo);
        map.put("refundNo", refundNo);
        map.put("orderNo", orderNo);
        map.put("version", "V1.0");
        map.put("curCode", "CNY");
        map.put("refundAmount", amount + "");
        map.put("refundTime", refundTime);
        map.put("sign", MD5Utils.MD5(ParaUtils.createLinkString(map) + MyApplication.MD5key));
        try {
            final ApiRequestStores apiStores = HttpHelper.getInstance().getRetrofit().create(ApiRequestStores.class);
            Observable<RefundBean> observable = apiStores.requestRefund(map);
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<RefundBean>() {
                        @Override
                        public void onCompleted() {
                            dismissLoading();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("RefundActivity", "--------" + e.getMessage().toString());
                        }

                        @Override
                        public void onNext(RefundBean refundBean) {
                            String dealCode = refundBean.getDealCode();
                            if (TextUtils.isEmpty(dealCode)) {
                                return;
                            }
                            if (dealCode.equals(Constant.DEAL_CODE_SUCCESS)) {
                                mEdtRefundRefundAmount.setText("");
                                mEdtRefundExplanation.setText("");
                                setDialog("退款成功!");
                            } else {
                                setDialog("退款失败,失败原因:" + getErrorMsg(Integer.parseInt(dealCode)));
                            }
                        }
                    });
        }catch (Exception e) {
            Log.d("RefundActivity", "-----------"+e.getMessage().toString());
        }

    }

    private String getErrorMsg(int dealCode) {
        String dealMsg = "";
        switch (dealCode) {
            case 40001:
                dealMsg = "需要退款的订单信息不存在";
                break;
            case 40005:
                dealMsg = "订单已超过时限,不能退款";
                break;
            case 40006:
                dealMsg = "银行系统异常,退款失败";
                break;
            case 40007:
                dealMsg = "商户余额不足,不能进行退款";
                break;
            case 40009:
                dealMsg = "订单已全部退款";
                break;
            case 40010:
                dealMsg = "商户可用账户余额不足,不能进行退款";
                break;
        }
        return dealMsg;

    }


    private void setDialog(String message) {

        final MyAlertDialog dialog1 = new MyAlertDialog(RefundActivity.this)
                .builder();
        dialog1.dialog_marTop.setVisibility(View.GONE);
        dialog1.setMsg(message)
               .setPositiveButton("确定", v -> {
                    startActivity(new Intent(RefundActivity.this, BillActivity.class));
                    finish();
                });
        dialog1.show();

//        mBuilder.setMessage(message);
//        mBuilder.setPositiveButton("确定", (dialog, which) -> {
//            mBuilder.create().dismiss();
//            new Handler().post(new Runnable() {
//                @Override
//                public void run() {
//                    startActivity(new Intent(RefundActivity.this, BillActivity.class));
//                    finish();
//                }
//            });
//        });
//        mBuilder.create().show();
    }

    private void setDialog2(String message) {
        mBuilder.setMessage(message);
        mBuilder.setPositiveButton("确定", (dialog, which) -> {
            mBuilder.create().dismiss();
        });
        mBuilder.create().show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s != null) {
            mBtnRefundSubmit.setClickable(true);
            mBtnRefundSubmit.setFocusable(true);
            mBtnRefundSubmit.setOnClickListener(this);
            mBtnRefundSubmit.setBackgroundResource(R.drawable.selector_refund_submit);
        } else {
            mBtnRefundSubmit.setBackgroundColor(getResources().getColor(R.color.darker_gray));
            mBtnRefundSubmit.setAlpha(0.1f);
            mBtnRefundSubmit.setClickable(false);
            mBtnRefundSubmit.setFocusable(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().equals("") || s == null) {
            mBtnRefundSubmit.setBackgroundColor(getResources().getColor(R.color.darker_gray));
            mBtnRefundSubmit.setClickable(false);
        }
    }

    @Override
    public boolean judgeNetWord(Context mContext) {
        return super.judgeNetWord(mContext);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, BillDetailsActivity.class));
        finish();
    }

    @OnClick({R.id.iv_refund_back, R.id.btn_refund_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_refund_back:
                finish();
                break;
            case R.id.btn_refund_submit:
                if (!judgeNetWord(RefundActivity.this)) {
                    Toast.makeText(getApplicationContext(), "网络连接异常,请检查!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (validRefundAmount()) return;
                showLoadingDialog();
                requestRefund();
//                requestRefund2();
                break;
        }
    }



    private void requestRefund2() {

    }
}
