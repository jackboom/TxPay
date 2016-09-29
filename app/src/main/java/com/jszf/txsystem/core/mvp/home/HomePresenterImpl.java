package com.jszf.txsystem.core.mvp.home;

import com.jszf.txsystem.bean.HomeBean;
import com.jszf.txsystem.core.OnRequestListener;
import com.jszf.txsystem.core.mvp.base.BasePresenters;

import java.util.HashMap;

/**
 * Created by wpj on 16/6/14下午2:33.
 */
public class HomePresenterImpl extends BasePresenters<IHomeView> implements IHomePresenter {

    private IHomeModel mHomeModel;

    public HomePresenterImpl(IHomeView mIHomeView) {
        super(mIHomeView);
        mHomeModel = new HomeModelImpl();
    }

    @Override
    public void requestHomeInfo(HashMap<String, String> params) {
//        mView.showLoading();
        mHomeModel.requestHomeInfo(params, new OnRequestListener<HomeBean>() {
            @Override
            public void onLoadCompleted(HomeBean homeBean) {

                mView.onHomeRequest(homeBean);
            }

            @Override
            public void onLoadFailed(String errMsg) {
                mView.showToast(errMsg);
            }
        });
    }
}
