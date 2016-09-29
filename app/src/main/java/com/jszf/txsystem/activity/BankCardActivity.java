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

public class BankCardActivity extends BaseActivity {
    @BindView(R.id.iv_bankCard_back)
    ImageView mIvBankCardBack;
    @BindView(R.id.tv_openAccount_name)
    TextView mTvOpenAccountName;
    @BindView(R.id.tv_openAccount_bank)
    TextView mTvOpenAccountBank;
    @BindView(R.id.tv_bankCard)
    TextView mTvBankCard;
    private Merchant mMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card);
        ButterKnife.bind(this);
       setStatusBar();
        initView();
        setView();
    }

    /**
     * 设置数据
     */
    private void setView() {
        if (mMerchant != null) {
            String bankCard = mMerchant.getBankCode();
            String bankName = mMerchant.getBankName();
            String openAccount_name = mMerchant.getBankCompanyName();
            mTvBankCard.setText(bankCard);
            mTvOpenAccountBank.setText(bankName);
            mTvOpenAccountName.setText(openAccount_name);
        } else {
            mTvBankCard.setText("");
            mTvOpenAccountBank.setText("");
            mTvOpenAccountName.setText("");
        }
    }

    private void initView() {
        mMerchant = (Merchant) getIntent().getSerializableExtra("merchantInfo");
    }

    @OnClick(R.id.iv_bankCard_back)
    public void onClick() {
        finish();
    }


}
