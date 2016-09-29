package com.jszf.txsystem.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.Merchant;
import com.jszf.txsystem.core.mvp.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountInfoActivity extends BaseActivity {
    @BindView(R.id.iv_accountInfo_back)
    ImageView mIvAccountInfoBack;
    @BindView(R.id.tv_accountInfo_amountAll)
    TextView mTvAccountInfoAmountAll;       //账户余额
    @BindView(R.id.tv_accountInfo_usable)
    TextView mTvAccountInfoUsable;          //可用余额
    @BindView(R.id.tv_accountInfo_retain)
    TextView mTvAccountInfoRetain;          //待结算金额
    @BindView(R.id.tv_accountInfo_fixamount)
    TextView mTvAccountInfoFixamount;       //不可用余额
    private Merchant mMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_remain);
        ButterKnife.bind(this);
        setStatusBar();
        setView();
    }

    private void setView() {
        mMerchant = (Merchant) getIntent().getSerializableExtra("merchantInfo");
        if (mMerchant != null) {
            String amountAll = mMerchant.getAccountAllAmount();
            String amountUsable = mMerchant.getAccountUsableAmount();
            String amountRetain = mMerchant.getAccountRetainAmount();
            String amountFix = mMerchant.getAccountFixAmount();
            mTvAccountInfoAmountAll.setText(amountAll);
            mTvAccountInfoUsable.setText(amountUsable);
            mTvAccountInfoRetain.setText(amountRetain);
            mTvAccountInfoFixamount.setText(amountFix);
        } else {
            mTvAccountInfoAmountAll.setText("");
            mTvAccountInfoUsable.setText("");
            mTvAccountInfoRetain.setText("");
            mTvAccountInfoFixamount.setText("");
        }
    }

    @OnClick(R.id.iv_accountInfo_back)
    public void onClick() {
        finish();
    }

}
