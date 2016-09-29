package com.jszf.txsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jszf.txsystem.MyApplication;
import com.jszf.txsystem.R;
import com.jszf.txsystem.adapter.RatePeriodAdapter;
import com.jszf.txsystem.bean.HomeBean;
import com.jszf.txsystem.bean.Merchant;
import com.jszf.txsystem.bean.RateProduct;
import com.jszf.txsystem.core.mvp.MvpActivity;
import com.jszf.txsystem.core.mvp.home.HomePresenterImpl;
import com.jszf.txsystem.core.mvp.home.IHomeView;
import com.jszf.txsystem.util.Constant;
import com.jszf.txsystem.util.MD5Utils;
import com.jszf.txsystem.util.ParaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PeriodAndRatectivity extends MvpActivity<IHomeView,HomePresenterImpl> implements IHomeView {
    @BindView(R.id.iv_rate_back)
    ImageView mIvRateBack;
    @BindView(R.id.tv_rate_title)
    TextView mTvRateTitle;
    @BindView(R.id.recycleView_rate)
    RecyclerView mRecycleViewRate;
    private ImageView iv_rate_back;
    private TextView tv_rate_type;  //方式
    private TextView tv_rate_discountType;  //折扣类型
    private TextView tv_rate_dealRate;  //交易费率
    private TextView tv_rate_period;  //结算周期
    private List<RateProduct> list = new ArrayList<>();
    private Merchant mMerchant;
    private RecyclerView recycleView_rate;
    private Intent mIntent;
    private List<HomeBean.ProductRateList> mProductRateList;
    private RatePeriodAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_and_ratectivity);
        ButterKnife.bind(this);
        setStatusBar();
        setAdapter();
        mvpPresenter.requestHomeInfo(getRequestParams());
    }

    @Override
    protected void initData() {
        mIntent = getIntent();
        mMerchant = (Merchant) mIntent.getSerializableExtra("merchantInfo");
    }

    @Override
    protected HomePresenterImpl createPresenter() {
        return new HomePresenterImpl(this);
    }

    private void setAdapter() {
        mAdapter = new RatePeriodAdapter(this);
        mRecycleViewRate.setLayoutManager(new LinearLayoutManager(this));
        mRecycleViewRate.setAdapter(mAdapter);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoading();
    }

    @OnClick(R.id.iv_rate_back)
    public void onClick() {
        finish();
    }

    /**
     * @return 网络请求数据
     */
    @NonNull
    private HashMap<String, String> getRequestParams() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("service", "getMerchantInfo");
        map.put("merchantNo", MyApplication.merchantNo);
        Log.d("TAG", "merchantNo:" + MyApplication.merchantNo);
        map.put("userLoginName", MyApplication.userLoginName);
        map.put("version", "V1.0");
        map.put("sign", MD5Utils.MD5(ParaUtils.createLinkString(map) + MyApplication.MD5key));
        Log.d("PeriodAndRatectivity","---------------------" +ParaUtils.createLinkString(map));
        return map;
    }

    @Override
    public void onHomeRequest(HomeBean homeBean) {
        try {
            if (homeBean == null) {
                return;
            }
            String dealCode = homeBean.getDealCode();
            if (!dealCode.equals(Constant.DEAL_CODE_SUCCESS)) {
                Toast.makeText(PeriodAndRatectivity.this, "系统,异常代码：" + dealCode,
                        Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("PeriodAndRatectivity", "------------"+homeBean.getProductRateList().get(0).getProductName());
            mAdapter.setData(homeBean.getProductRateList());
           mProductRateList = homeBean.getProductRateList();
        } catch (Exception e) {
            Log.d("PeriodAndRatectivity", e.getMessage().toString());
        }
    }
}
