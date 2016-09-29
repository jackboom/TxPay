package com.jszf.txsystem.core.mvp.login;

import com.jszf.txsystem.bean.LoginBean;
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
public class LoginModelImpl implements ILoginModel {


    @Override
    public void requestLogin(HashMap<String, String> params,final OnRequestListener<LoginBean> listener) {
        String url = "http://api.tongxingpay.com/txpayApi/app?" +  ParaUtils.createLinkString(params);
        final ApiRequestStores apiStores = HttpHelper.getInstance().getRetrofit().create(ApiRequestStores.class);
//        NetworkCache<LoginBean> networkCache = new NetworkCache<LoginBean>() {
//            @Override
//            public Observable<LoginBean> get(String key, Class<LoginBean> cls) {
//                Observable<LoginBean> observable = apiStores.login(params)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeOn(Schedulers.io());
//                return observable;
//            }
//        };
        Observable<LoginBean> observable = apiStores.login(params)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io());
//        Observable<LoginBean> observable = CacheManager.getInstance().load(url, LoginBean.class, networkCache);
        observable.subscribe(new Observer<LoginBean>() {
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
            public void onNext(LoginBean loginBean) {
                if (null != listener) {
                    listener.onLoadCompleted(loginBean);
                }
            }
        });
    }
}
