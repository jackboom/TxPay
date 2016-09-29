package com.jszf.txsystem.core.mvp.bill;

import com.jszf.txsystem.bean.BillBean;
import com.jszf.txsystem.core.ApiRequestStores;
import com.jszf.txsystem.core.HttpHelper;
import com.jszf.txsystem.core.OnRequestListener;
import com.jszf.txsystem.util.ParaUtils;

import java.util.HashMap;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wpj on 16/6/14下午2:36.
 */
public class BillModelImpl implements IBillModel {

    private static final int PAGE_SIZE = 10;
    /**
     * 请求参数：
     * 方式一：    maxXhid:已有的最大笑话ID；minXhid:已有的最小笑话ID；size:要获取的笑话的条数
     * 方式二：    size:要获取的笑话的条数；page:分页请求的页数，从0开始
     */
    private static final String API = "http://api.1-blog.com/biz/bizserver/xiaohua/list.do?page=%s&size=%s";

    @Override
    public void requestBillInfo(final HashMap<String, String> params,final OnRequestListener<BillBean> listener) {
        String url = "http://pc.tongxingpay.com/txpayApi/app?" +  ParaUtils.createLinkString(params);
        final ApiRequestStores apiStores = HttpHelper.getInstance().getRetrofit().create(ApiRequestStores.class);
//        NetworkCache<BillBean> networkCache = new NetworkCache<BillBean>() {
//            @Override
//            public Observable<BillBean> get(String key, Class<BillBean> cls) {
//                Observable<BillBean> observable = apiStores.requestForBillinfo(params)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeOn(Schedulers.io());
//                return observable;
//            }
//        };
        Observable<BillBean> observable = apiStores.requestForBillinfo(params)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io());
        observable.subscribe(new Observer<BillBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                if (null != listener) {
                    listener.onLoadFailed(e.toString());
                }
            }

            @Override
            public void onNext(BillBean billBean) {
                if (null != listener) {
                    listener.onLoadCompleted(billBean);
                }
            }
        });
    }

    @Override
    public int getStartIndex() {
        return 1;
    }

}
