package com.jszf.txsystem.core.mvp.main;

import com.jszf.txsystem.bean.MainBean;
import com.jszf.txsystem.core.ApiRequestStores;
import com.jszf.txsystem.core.OnRequestListener;
import com.jszf.txsystem.core.HttpHelper;
import com.jszf.txsystem.util.ParaUtils;

import java.util.HashMap;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author jacking
 *         Created at 2016/8/31 15:37 .
 */
public class MainModelImpl implements IMainModel {

    @Override
    public void requestFirstInfo(HashMap<String, String> params,final OnRequestListener<MainBean> listener) {
        String url = "http://api.tongxingpay.com/txpayApi/app?" +  ParaUtils.createLinkString(params);
        final ApiRequestStores apiStores = HttpHelper.getInstance().getRetrofit().create(ApiRequestStores.class);
        Observable<MainBean> observable = apiStores.requestForMain(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
        observable.subscribe(new Observer<MainBean>() {
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
            public void onNext(MainBean mainBean) {
                if (null != listener) {
                    listener.onLoadCompleted(mainBean);
                }
            }
        });
    }
}
