package com.jszf.txsystem.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jszf.txsystem.MyApplication;
import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.BillDetailBean;
import com.jszf.txsystem.bean.BillInfo;
import com.jszf.txsystem.bean.Merchant;
import com.jszf.txsystem.core.ApiRequestStores;
import com.jszf.txsystem.core.HttpHelper;
import com.jszf.txsystem.core.mvp.base.BaseActivity;
import com.jszf.txsystem.util.Constant;
import com.jszf.txsystem.util.MD5Utils;
import com.jszf.txsystem.util.ParaUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class BillDetailsActivity extends BaseActivity {
    @BindView(R.id.iv_billdetail_back)
    ImageView mIvBilldetailBack;
    @BindView(R.id.tv_bill_title)
    TextView mTvBillTitle;
    @BindView(R.id.tv_billdetail_refund)
    TextView mTvBilldetailRefund;
    @BindView(R.id.rl_bill_title)
    RelativeLayout mRlBillTitle;
    @BindView(R.id.tv_billdetail_merchanno)
    TextView mTvBilldetailMerchanno;
    @BindView(R.id.tv_billdetail_morderno)
    TextView mTvBilldetailMorderno;
    @BindView(R.id.tv_billdetail_txorderno)
    TextView mTvBilldetailTxorderno;
    @BindView(R.id.tv_billdetail_card)
    TextView mTvBilldetailCard;
    @BindView(R.id.tv_billdetail_paychannel)
    TextView mTvBilldetailPaychannel;
    @BindView(R.id.tv_billdetail_status)
    TextView mTvBilldetailStatus;
    @BindView(R.id.tv_billdetail_paytime)
    TextView mTvBilldetailPaytime;
    @BindView(R.id.tv_billdetail_productname)
    TextView mTvBilldetailProductname;
    @BindView(R.id.tv_billdetail_payamount)
    TextView mTvBilldetailPayamount;
    @BindView(R.id.tv_billdetail_refundAmount)
    TextView mTvBilldetailRefundAmount;
    @BindView(R.id.ll_refundAmount)
    LinearLayout mLlRefundAmount;               //退款金额
    @BindView(R.id.tv_billdetail_ext1)
    TextView mTvBilldetailExt1;
    @BindView(R.id.tv_billdetail_ext2)
    TextView mTvBilldetailExt2;
    @BindView(R.id.tv_billdetail_refundAmountCount)
    TextView mTvBilldetailRefundAmountCount;
    @BindView(R.id.ll_refundAmountCount)
    LinearLayout mLlRefundAmountCount;

    private Merchant mMerchant;
    private BillInfo mBillInfo;
    private HashMap<String, String> resultMap;
    private String orderNo; //订单号
    private String payState;     //订单支付状态
    private String payTime;     //支付时间
    private String payChannelCode;     //支付通道
    private String orderAmount;   //订单金额
    private String txOrderNo;   //同兴订单号
    private String mOrderNo;    //商户订单号
    private String txRefundNo;    //同兴退款流水号
    private String mRefundNo;   // 商户退款流水号
    private String refundTime;   // 退款申请时间
    private String txPayTime;   // 退款完成时间
    private String refundState;   // 退款状态
    private String productName;   // 商品名称
    private Handler mHandler = new Handler();
    private String extraStr1;
    private String extraStr2;
    private String refundAmount;
    private String bankOrderNo;
    private int refundCount;
    private Observable<BillDetailBean> observable;
    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        ButterKnife.bind(this);
        setStatusBar();
        initData();
        if (judgeNetWord(this)) {
            showLoadingDialog();
            requestDetailSearch();
        }
    }

    /**
     *详细订单查询
     */
    private void requestDetailSearch() {
        final HashMap<String, String> map = getRequestParams();
        try {
            ApiRequestStores apiStores = HttpHelper.getInstance().getRetrofit().create(ApiRequestStores.class);
            observable = apiStores.requestBillDetail(map)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io());
            compositeSubscription = new CompositeSubscription();
            compositeSubscription.add(observable.subscribe(new Subscriber<BillDetailBean>() {
                @Override
                public void onCompleted() {
                    dismissLoading();

                }

                @Override
                public void onError(Throwable e) {
                    dismissLoading();
                    Log.d("BillDetailsActivity", "----------" + e.getMessage().toString());
                }

                @Override
                public void onNext(BillDetailBean billDetailBean) {
                    dismissLoading();
                    String dealCode = billDetailBean.getDealCode();
                    if (!TextUtils.isEmpty(dealCode) && dealCode.equals(Constant.DEAL_CODE_SUCCESS)) {
                        txOrderNo = billDetailBean.getTxOrderNo();
                        payChannelCode = billDetailBean.getPayChannelCode();
                        payState = billDetailBean.getOrderPayStatus();
                        payTime = billDetailBean.getOrderTime();
                        refundAmount = billDetailBean.getRefundAmount();
                        productName = billDetailBean.getProductName();
                        orderAmount = billDetailBean.getOrderAmount();
                        bankOrderNo = billDetailBean.getBankOrderNo();
                        Log.d("BillDetailsActivity", "bankOrderNo:" + bankOrderNo);
                        extraStr1 = billDetailBean.getExt1();
                        extraStr2 = billDetailBean.getExt2();
                        setView();
                    }
                }
            }));
//            bservable.subscribe(new Observer<BillDetailBean>() {
//                @Override
//                public void onCompleted() {
//                    dismissLoading();
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    dismissLoading();
//                    Log.d("BillDetailsActivity", "----------" + e.getMessage().toString());
//                }
//
//                @Override
//                public void onNext(BillDetailBean billDetailBean) {
//                    dismissLoading();
//                    String dealCode = billDetailBean.getDealCode();
//                    if (!TextUtils.isEmpty(dealCode) && dealCode.equals(Constant.DEAL_CODE_SUCCESS)) {
//                        txOrderNo = billDetailBean.getTxOrderNo();
//                        payChannelCode = billDetailBean.getPayChannelCode();
//                        payState = billDetailBean.getOrderPayStatus();
//                        payTime = billDetailBean.getOrderTime();
//                        refundAmount = billDetailBean.getRefundAmount();
//                        productName = billDetailBean.getProductName();
//                        orderAmount = billDetailBean.getOrderAmount();
//                        bankOrderNo = billDetailBean.getBankOrderNo();
//                        Log.d("BillDetailsActivity", "bankOrderNo:" + bankOrderNo);
//                        extraStr1 = billDetailBean.getExt1();
//                        extraStr2 = billDetailBean.getExt2();
//                        setView();
//                    }
//                }
//            });
        } catch (Exception e) {
            Log.d("BillDetailsActivity", "-------" + e.getMessage().toString());
        }
    }

    @NonNull
    private HashMap<String, String> getRequestParams() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("service", "offSearchOrder");
        map.put("version", "V1.0");
        map.put("merchantNo", MyApplication.merchantNo);
        map.put("orderNo", orderNo);
        String str = ParaUtils.createLinkString(map);
        map.put("sign", MD5Utils.MD5(ParaUtils.createLinkString(map) + MyApplication.MD5key));
        Log.d("BillDetailsActivity", "---------" + str + "&sign=" + MD5Utils.MD5(str + MyApplication.MD5key));

        return map;
    }

    private void setView() {
        mTvBilldetailMerchanno.setText(MyApplication.merchantNo);
        mTvBilldetailMorderno.setText(orderNo);
        mTvBilldetailTxorderno.setText(txOrderNo);
        String payType = null;
        Log.d("TAG", "PAT:" + payChannelCode);
        if (payChannelCode == null) {
            payType = "";
        } else {
            if (payChannelCode.equals(Constant.PAY_CHANNELCODE_WEXIN)) {
                payType = "微信支付";
            } else if (payChannelCode.equals(Constant.PAY_CHANNELCODE_ALIPAY)) {
                payType = "支付宝支付";
            } else if (payChannelCode.equals(Constant.PAY_CHANNELCODE_BDQB)) {
                payType = "微信支付";
            } else if (payChannelCode.equals(Constant.PAY_CHANNELCODE_YZF)) {
                payType = "翼支付";
            } else if (payChannelCode.equals(Constant.PAY_CHANNELCODE_BANK)) {
                payType = "银联";
            } else {
                payType = "待确定";
            }
        }
        String refundStatus = "";
        String amount = "";
        if (!TextUtils.isEmpty(orderAmount)) {
            amount = Double.parseDouble(orderAmount) / 100 + "";
        }
        String refund = "";
        if (!TextUtils.isEmpty(refundAmount)) {
            refund = Double.parseDouble(refundAmount) / 100 + "";
        }
        if (!TextUtils.isEmpty(bankOrderNo)) {
            mTvBilldetailCard.setText(bankOrderNo);
        }
        if (!TextUtils.isEmpty(refundState)) {
            mLlRefundAmount.setVisibility(View.VISIBLE);
            if (refundState.equals("4")) {
                mLlRefundAmount.setVisibility(View.VISIBLE);
                mLlRefundAmountCount.setVisibility(View.VISIBLE);
                mTvBilldetailRefundAmount.setText(refund + "元");
                mTvBilldetailRefundAmountCount.setVisibility(View.VISIBLE);
                mTvBilldetailRefundAmountCount.setText(refundCount + "");
                if (refundAmount.equals(orderAmount)) {
                    mTvBilldetailRefund.setVisibility(View.GONE);
                }
            } else if (refundState.equals("5")) {
                refundStatus = "退款失败";
            }
        } else {
            mTvBilldetailRefund.setVisibility(View.VISIBLE);
            mLlRefundAmount.setVisibility(View.GONE);

        }
        mTvBilldetailPaychannel.setText(payType);
        String status = "";
        if (payState.equals("0")) {
            status = "订单处理中";
            mTvBilldetailRefund.setVisibility(View.GONE);
        } else if (payState.equals("1")) {
            status = "支付成功";
        } else if (payState.equals("2")) {
            status = "支付失败";
            mTvBilldetailRefund.setVisibility(View.GONE);

        } else if (payState.equals("6")) {
            status = "未支付";
            mTvBilldetailRefund.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(refundStatus)) {
            mTvBilldetailStatus.setText(status);

        } else {
            mTvBilldetailStatus.setText(status + "(" + refundStatus + ")");

        }
        if (!TextUtils.isEmpty(payTime)) {
            payTime = payTime.substring(0, 4) + "-" + payTime.substring(4, 6) + "-" + payTime.substring(6, 8) +
                    "  " + payTime.substring(8, 10) + ":" + payTime.substring(10, 12) + ":" + payTime.substring(12, 14);
        }
        mTvBilldetailPaytime.setText(payTime);

        mTvBilldetailPayamount.setText(amount + "元");
        mTvBilldetailProductname.setText(productName);
        mTvBilldetailExt1.setText(extraStr1);
        mTvBilldetailExt2.setText(extraStr2);

    }


    private void initData() {
        mBillInfo = (BillInfo) getIntent().getSerializableExtra("data");
        orderNo = mBillInfo.getOrderNo();
        refundState = mBillInfo.getRefundState();
        refundCount = mBillInfo.getRefundCount();
        Log.d("BillDetailsActivity", "orderNo:" + orderNo + "mRefundNo:" + mRefundNo + "txRefundNo:" + txRefundNo
                + "refundTime:" + refundTime);

        mMerchant = (Merchant) getIntent().getSerializableExtra("merchantInfo");
    }



    @Override
    public boolean judgeNetWord(Context mContext) {
        return super.judgeNetWord(mContext);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, BillActivity.class));
        finish();
    }

    @OnClick({R.id.iv_billdetail_back, R.id.tv_billdetail_refund})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_billdetail_back:
                finish();
                break;
            case R.id.tv_billdetail_refund:
                Intent intent = new Intent(BillDetailsActivity.this, RefundActivity.class);
                int offAmount = Integer.parseInt(orderAmount) - Integer.parseInt(refundAmount);
                intent.putExtra("amount", offAmount);
                intent.putExtra("orderNo", orderNo);
                intent.putExtra("merchantInfo", mMerchant);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeSubscription != null){
            compositeSubscription.unsubscribe();
        }
    }
}
