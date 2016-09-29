package com.jszf.txsystem.core.mvp.main;

import com.jszf.txsystem.bean.MainBean;
import com.jszf.txsystem.core.OnRequestListener;
import com.jszf.txsystem.core.mvp.base.BasePresenters;

import java.util.HashMap;

/**
 * @author jacking
 *         Created at 2016/8/31 15:35 .
 */
public class MainPresenterImpl extends BasePresenters<IMainView> implements IMainPresenter {
    private IMainModel mMainModel;

    public MainPresenterImpl(IMainView v) {
        super(v);
        mMainModel = new MainModelImpl();
    }

    @Override
    public void requestFirstInfo(HashMap<String, String> params) {
        mMainModel.requestFirstInfo(params, new OnRequestListener<MainBean>() {
            @Override
            public void onLoadCompleted(MainBean t) {
                mView.requestFirstInfo(t);
            }

            @Override
            public void onLoadFailed(String errMsg) {
                mView.showToast(errMsg);
            }
        });
    }
}
