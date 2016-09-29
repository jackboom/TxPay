package com.jszf.txsystem.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.jszf.txsystem.MyApplication;
import com.jszf.txsystem.R;
import com.jszf.txsystem.adapter.AccountSearchAdapter;
import com.jszf.txsystem.bean.AccountSearchBean;
import com.jszf.txsystem.bean.SettleInfo;
import com.jszf.txsystem.core.mvp.MvpActivity;
import com.jszf.txsystem.core.mvp.accountsearch.AccountSearchPresenterImpl;
import com.jszf.txsystem.core.mvp.accountsearch.IAccountSearchView;
import com.jszf.txsystem.util.AcquireTimeNode;
import com.jszf.txsystem.util.Constant;
import com.jszf.txsystem.util.MD5Utils;
import com.jszf.txsystem.util.ParaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiveAccountActivity extends MvpActivity<IAccountSearchView, AccountSearchPresenterImpl> implements IAccountSearchView {
    @BindView(R.id.iv_receive_back)
    ImageView mIvReceiveBack;
    @BindView(R.id.recycleView_settle)
    RecyclerView mRecycleViewSettle;
    @BindView(R.id.frame_recycleView)
    PtrClassicFrameLayout mFrameRecycleView;
    private String mTotal;
    private List<SettleInfo> list = new ArrayList<>();
    private int nowPage = 1;
    private LinearLayoutManager mLinearLayoutManager;
    private Handler handler = new Handler();
    private AccountSearchAdapter mSearchAdapter;
    private boolean isAnd = false;
    private boolean noUpdate = false;
    private RecyclerAdapterWithHF mAdapterWithHF;
    private List<AccountSearchBean.SettleInfoList> settleInfoList;
    private List<AccountSearchBean.SettleInfoList> dataList = new ArrayList<>();
    public static final String PATH = "http://api.tongxingpay.com/txpayApi/app?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_account);
        ButterKnife.bind(this);
        setStatusBar();
        init();
    }
    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }


    @Override
    protected void initData() {
    }

    @Override
    protected AccountSearchPresenterImpl createPresenter() {
        return new AccountSearchPresenterImpl(this);
    }

    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleViewSettle.setLayoutManager(layoutManager);
        mSearchAdapter = new AccountSearchAdapter(this);
        mAdapterWithHF = new RecyclerAdapterWithHF(mSearchAdapter);
        mRecycleViewSettle.setAdapter(mAdapterWithHF);
        mFrameRecycleView.setLastUpdateTimeRelateObject(AcquireTimeNode.getCurrentTimeToString());
        mFrameRecycleView.postDelayed(() -> mFrameRecycleView.autoRefresh(true), 150);
        mFrameRecycleView.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                handler.postDelayed(() -> {
                    isAnd = false;
                    nowPage = mvpPresenter.getStartIndex();
                    mvpPresenter.requestAccountSearch(getRequestParams(nowPage));
                    mAdapterWithHF.notifyDataSetChanged();
                    mFrameRecycleView.refreshComplete();
                    mFrameRecycleView.setLoadMoreEnable(true);

                }, 1000);
            }
        });
        mFrameRecycleView.setOnLoadMoreListener(() -> handler.postDelayed(() -> {
            isAnd  = true;
            mvpPresenter.requestAccountSearch(getRequestParams(++nowPage));
            mAdapterWithHF.notifyDataSetChanged();
            mFrameRecycleView.loadMoreComplete(true);
        }, 1000));
    }

    @NonNull
    private HashMap<String, String> getRequestParams(int page) {
        final HashMap<String, String> map = new HashMap<>();
        map.put("service", "batchSearchSettleInfo");
        map.put("version", "V1.0");
        map.put("merchantNo", MyApplication.merchantNo);
        map.put("nowPage", page + "");
        map.put("sign", MD5Utils.MD5(ParaUtils.createLinkString(map) + MyApplication.MD5key));
        return map;
    }

    @Override
    public boolean judgeNetWord(Context mContext) {
        return super.judgeNetWord(mContext);
    }

    @OnClick(R.id.iv_receive_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onAccountRequest(AccountSearchBean accountSearchBean) {
        try {
            String dealCode = accountSearchBean.getDealCode();
            if (dealCode.equals(Constant.DEAL_CODE_SUCCESS)) {
                settleInfoList = accountSearchBean.getSettleInfoList();
                if (settleInfoList == null) {
                    mFrameRecycleView.setLoadMoreEnable(false);
                    Log.d("ReceiveAccountActivity", "settleInfoList==null:" + (settleInfoList == null));
                    if (isAnd) {
                        noUpdate = true;
                    } else {
                        return;
                    }
                }
                if (noUpdate) {
                    return;
                }
                Log.d("ReceiveAccountActivity", "settleInfoList.size():" + settleInfoList.size());
//                dataList.addAll(settleInfoList);
//                mSearchAdapter.notifyDataSetChanged();
                if (nowPage == mvpPresenter.getStartIndex()) {
                    mSearchAdapter.setData(settleInfoList);
                } else {
                    mSearchAdapter.addData(settleInfoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
