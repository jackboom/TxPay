package com.jszf.txsystem.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.jszf.txsystem.MyApplication;
import com.jszf.txsystem.R;
import com.jszf.txsystem.activity.NoticeInfoActivity;
import com.jszf.txsystem.adapter.NoticeAdapter;
import com.jszf.txsystem.bean.NoticeBean;
import com.jszf.txsystem.core.ApiRequestStores;
import com.jszf.txsystem.core.HttpHelper;
import com.jszf.txsystem.core.mvp.base.BaseFragment;
import com.jszf.txsystem.util.Constant;
import com.jszf.txsystem.util.MD5Utils;
import com.jszf.txsystem.util.ParaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/4/27.
 */
public class MessageFragment extends BaseFragment {
    @BindView(R.id.tv_message_empty)
    TextView mTvMessageEmpty;
    @BindView(R.id.recycleView_notice)
    RecyclerView mRecycleViewNotice;
    @BindView(R.id.ptr_frame)
    PtrClassicFrameLayout mPtrFrame;
    private View mView;
    private HashMap<String, String> map;
    private HashMap<String, String> resultMap;
    private int currentPage;    //网络请求的当前页码
    private String everyPageNo = "10";    //每页条数
    private String total;   //数据总条数
    private List<NoticeBean.NoticeList> noticeList;  //数据json内容
    private List<NoticeBean> list = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private NoticeAdapter mAdapter;
    private Handler handler = new Handler();
    private Observable<NoticeBean> observable;
    private CompositeSubscription compositeSubscription;
    private boolean isRefresh = true;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.frag_message, container, false);
        }
        ButterKnife.bind(this, mView);
        setStatus();
        init();
        initListener();
        setAdapter();
        requestData(1);
//        mvpPresenter.requestNoticeInfo(getRequestParams(mvpPresenter.getStartIndex()));
        return mView;
    }

    private void requestData(int page) {
        ApiRequestStores apiStores = HttpHelper.getInstance().getRetrofit().create(ApiRequestStores.class);
        observable = apiStores.requestForNotice(getRequestParams(page))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
        compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(observable.subscribe(new Subscriber<NoticeBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d("MessageFragment", "e:" + e);
            }

            @Override
            public void onNext(NoticeBean noticeBean) {
                try {
                    String dealCode = noticeBean.getDealCode();
                    if (!dealCode.equals(Constant.DEAL_CODE_SUCCESS)) {
                        return;
                    }
                    total = noticeBean.getTotal();
                    if (TextUtils.isEmpty(total) || total.equals("0")) {
                        Log.d("MessageFragment", "-------------" + total);
                        mPtrFrame.setLoadMoreEnable(false);
                        mTvMessageEmpty.setVisibility(View.VISIBLE);
                        mPtrFrame.setVisibility(View.GONE);
                        isRefresh = false;
                        return;
                    } else {
                        noticeList = noticeBean.getNoticeList();
                        if (noticeList != null) {
                            mAdapter.setData(noticeList);
                        } else {
                            mAdapter.addData(noticeList);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));

    }

    private void init() {
        mAdapter = new NoticeAdapter(getActivity());
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleViewNotice.setLayoutManager(mLinearLayoutManager);
        final RecyclerAdapterWithHF mAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        mRecycleViewNotice.setAdapter(mAdapterWithHF);
//        mPtrFrame.setLastUpdateTimeRelateObject(AcquireTimeNode.getCurrentTimeToString());
        mPtrFrame.setPullToRefresh(false);
        mPtrFrame.postDelayed(() -> mPtrFrame.autoRefresh(true), 150);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                handler.postDelayed(() -> {
                    currentPage = 1;
                    requestData(currentPage);
                    mAdapterWithHF.notifyDataSetChanged();
                    mPtrFrame.refreshComplete();
                    mPtrFrame.setLoadMoreEnable(true);
                }, 1000);
            }
        });
        mPtrFrame.setOnLoadMoreListener(() -> handler.postDelayed(() -> {
            requestData(currentPage);
            mAdapterWithHF.notifyDataSetChanged();
            mPtrFrame.loadMoreComplete(true);
        }, 1000));
    }

    private void initListener() {
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleViewNotice.setLayoutManager(mLinearLayoutManager);
    }

//    @Override
//    protected NoticePresenterImpl createPresenter() {
//        return new NoticePresenterImpl(this);
//    }

    private void setAdapter() {
        mAdapter = new NoticeAdapter(MyApplication.mContext);
        mTvMessageEmpty.setVisibility(View.VISIBLE);
        mPtrFrame.setVisibility(View.VISIBLE);
        mRecycleViewNotice.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((view, data) -> {
            String content = data.getContent();
            Intent mIntent = new Intent(getActivity(), NoticeInfoActivity.class);
            mIntent.putExtra("content", content);
            startActivity(mIntent);
        });
    }

    private HashMap<String, String> getRequestParams(int mCurrentPage) {
        map = new HashMap<>();
        map.put("service", "searchNoticeList");
        map.put("merchantNo", MyApplication.merchantNo);
        map.put("nowPage", mCurrentPage + "");
        map.put("pageNo", "10");
        map.put("version", "V1.0");
        map.put("sign", MD5Utils.MD5(ParaUtils.createLinkString(map) + MyApplication.MD5key));
        return map;
    }

    @Override
    public void setBar(int color) {
        super.setBar(color);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }
}


