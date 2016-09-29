package com.jszf.txsystem.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jszf.txsystem.MyApplication;
import com.jszf.txsystem.R;
import com.jszf.txsystem.activity.AccountInfoActivity;
import com.jszf.txsystem.activity.BankCardActivity;
import com.jszf.txsystem.activity.BluetoothActivity;
import com.jszf.txsystem.activity.MyTxActivity;
import com.jszf.txsystem.activity.PeriodAndRatectivity;
import com.jszf.txsystem.activity.PrintSettingActivity;
import com.jszf.txsystem.activity.ReceiveAccountActivity;
import com.jszf.txsystem.activity.ShiftActivity;
import com.jszf.txsystem.bean.HomeBean;
import com.jszf.txsystem.bean.Merchant;
import com.jszf.txsystem.core.mvp.base.BaseFragment;
import com.jszf.txsystem.ui.MyAlertDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/4/27.
 */
public class MyFragment extends BaseFragment {
    @BindView(R.id.ll_myself_title)
    LinearLayout mLlMyselfTitle;
    @BindView(R.id.btn_myself_quit)
    Button mBtnMyselfQuit;              //安全退出
    @BindView(R.id.tv_myself_merchanno)
    TextView mTvMyselfMerchanno;        //商户号
    @BindView(R.id.tv_myself_limit)
    TextView mTvMyselfLimit;             //商户权限
    @BindView(R.id.rl_myself_remain)
    RelativeLayout mRlMyselfRemain;     //账户余额
    @BindView(R.id.rl_myself_search)
    RelativeLayout mRlMyselfSearch;     //到账查询
    @BindView(R.id.rl_myself_card)
    RelativeLayout mRlMyselfCard;       //银行卡
    @BindView(R.id.rl_myself_rate)
    RelativeLayout mRlMyselfRate;       //周期与费率
    @BindView(R.id.rl_myself_bluetooth)
    RelativeLayout mRlMyselfBluetooth;  //蓝牙设置
    @BindView(R.id.tv_myself_phoneno)
    TextView mTvMyselfPhoneno;          //手机号
    @BindView(R.id.rl_mysellf_about)
    RelativeLayout mRlMysellfAbout;    //关于同兴
    @BindView(R.id.rl_myself_print)
    RelativeLayout mRlMyselfPrint;
    @BindView(R.id.rl_mysellf_shift)
    RelativeLayout mRlMysellfShift;
    private View mView;
    private String userLoginName;   //用户名
    private String merchantNo;    //商户号
    private String merchantName;    //商户名
    private String bankName;        //开户银行
    private String bankCompanyName; //银行账户名
    private String bankCode;        //卡号
    private String bankAddress;     //开户支行
    private String settlementPeriodType;    //结算周期类型
    private String settlementPeriodValue;   //结算周期
    private String accountRetainAmount;     //账户余额
    private String accountFixAmount;        //不可用金额
    private String accountAllAmount;        //待结算金额
    private String accountUsableAmount;     //可用金额
    private String tradingStatus;           //交易状态
    private String settlementStatus;        //结算状态
    private String telphone;                //手机号
    private List<HomeBean.ProductRateList> productRateList;         //产品费率列表
    private AlertDialog.Builder mBuilder;
    private Merchant mMerchant;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.frag_my, container, false);

        }
        ButterKnife.bind(this, mView);
        setStatus();
        getMerchantInfo();
        initView();
        setView();
        return mView;
    }

    @Override
    public void onDestroyView() {
        //移除当前视图，防止重复加载相同视图使得程序闪退
        ((ViewGroup) mView.getParent()).removeView(mView);
        super.onDestroyView();
    }


    private void setView() {
        try {
            mTvMyselfMerchanno.setText(merchantNo);
            StringBuffer litmitBuf = new StringBuffer();
            if (tradingStatus.equals("0")) {
                litmitBuf.append("不可交易");
            } else {
                litmitBuf.append("可交易");
            }
            if (settlementStatus.equals("0")) {
                litmitBuf.append(" 不可结算");
            } else {
                litmitBuf.append(" 可结算");
            }
            Log.d("TAG", "sb:" + litmitBuf.toString());
            mTvMyselfLimit.setText(litmitBuf.toString());
            mTvMyselfPhoneno.setText(telphone);
        } catch (Exception e) {

        }

    }

    /**
     * 获取商户信息数据
     */
    private void getMerchantInfo() {
        Bundle mBundle = getArguments();
        try {
//            mMerchant = (Merchant) mBundle.getSerializable("merchantInfo");
            mMerchant = MyApplication.mMerchant;
            Log.d("TAG", "------>" + (mMerchant == null));
            Log.d("TAG", "data:" + mMerchant.getMerchantNo());
            merchantNo = mMerchant.getMerchantNo();
            userLoginName = mMerchant.getUserLoginName();
            merchantName = mMerchant.getMerchantName();
            bankName = mMerchant.getBankName();
            bankCompanyName = mMerchant.getBankCompanyName();
            bankCode = mMerchant.getBankCode();
            bankAddress = mMerchant.getBankAddress();
            settlementPeriodType = mMerchant.getSettlementPeriodType();
            settlementPeriodValue = mMerchant.getSettlementPeriodValue();
            accountRetainAmount = mMerchant.getAccountRetainAmount();
            accountFixAmount = mMerchant.getAccountFixAmount();
            accountAllAmount = mMerchant.getAccountAllAmount();
            accountUsableAmount = mMerchant.getAccountUsableAmount();
            tradingStatus = mMerchant.getTradingStatus();
            settlementStatus = mMerchant.getSettlementStatus();
            telphone = mMerchant.getTelphone();
//            productRateList = mMerchant.getProductRateList();
        } catch (Exception e) {
            Log.d("TAG", "e:" + e.toString());
        }
    }

    private void initView() {
        mBuilder = new AlertDialog.Builder(getActivity());
    }

    private void setDialog(String message) {
        final MyAlertDialog dialog1 = new MyAlertDialog(getActivity())
                .builder();
        dialog1.dialog_marTop.setVisibility(View.GONE);
        dialog1.btn_neg.setVisibility(View.GONE);
        dialog1.btn_pos.setVisibility(View.GONE);
        dialog1.setMsg(message)
                .setNegativeButton("取消", v -> {

                })
                .setPositiveButton("确定", v -> {
                    Intent intent = new Intent();
                    intent.setAction("Exit APP");
                    getActivity().sendBroadcast(intent);
//                        ActivityTerminator.finishProgram();
                });
        dialog1.show();
    }

    @Override
    public void onDestroy() {
        getActivity().finish();
        super.onDestroy();
    }

    @OnClick({R.id.btn_myself_quit, R.id.rl_myself_remain, R.id.rl_myself_search,
            R.id.rl_myself_card, R.id.rl_myself_rate, R.id.rl_myself_bluetooth,
            R.id.rl_mysellf_about, R.id.rl_myself_print,R.id.rl_mysellf_shift})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_myself_quit:
                setDialog("是否退亏出应用?");
                break;
            case R.id.rl_myself_remain:
                Intent remainIntent = new Intent(getActivity(), AccountInfoActivity.class);
                remainIntent.putExtra("merchantInfo", mMerchant);
                startActivity(remainIntent);
                break;
            case R.id.rl_myself_search:
                Intent receiveIntent = new Intent(getActivity(), ReceiveAccountActivity.class);
                receiveIntent.putExtra("merchantInfo", mMerchant);
                startActivity(receiveIntent);
                break;
            case R.id.rl_myself_card:
                Intent cardIntent = new Intent(getActivity(), BankCardActivity.class);
                cardIntent.putExtra("merchantInfo", mMerchant);
                startActivity(cardIntent);
                break;
            case R.id.rl_myself_rate:
                Intent mIntent = new Intent(getActivity(), PeriodAndRatectivity.class);
                mIntent.putExtra("merchantInfo", mMerchant);
                startActivity(mIntent);
                break;
            case R.id.rl_myself_bluetooth:
                startActivity(new Intent(getActivity(), BluetoothActivity.class));
                break;
            case R.id.rl_myself_print:
                startActivity(new Intent(getActivity(), PrintSettingActivity.class));
                break;
            case R.id.rl_mysellf_about:
                startActivity(new Intent(getActivity(), MyTxActivity.class));
                break;
            case R.id.rl_mysellf_shift:
                startActivity(new Intent(getActivity(), ShiftActivity.class));
                break;
        }
    }
}
