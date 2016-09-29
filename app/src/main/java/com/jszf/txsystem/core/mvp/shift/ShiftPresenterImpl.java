package com.jszf.txsystem.core.mvp.shift;

import com.jszf.txsystem.bean.PageShiftBean;
import com.jszf.txsystem.core.OnRequestListener;
import com.jszf.txsystem.core.mvp.base.BasePresenters;

import java.util.HashMap;

/**
 * Created by wpj on 16/6/14下午2:33.
 */
public class ShiftPresenterImpl extends BasePresenters<IShiftView> implements IShiftPresenter {

    private IShiftModel mShiftModel;

    public ShiftPresenterImpl(IShiftView mIShiftView) {
        super(mIShiftView);
        mShiftModel = new ShiftModelImpl();
    }
    @Override
    public void requestShiftInfo(HashMap<String, String> params) {
        mView.showLoading();
        mShiftModel.requestShiftInfo(params, new OnRequestListener<PageShiftBean>() {
            @Override
            public void onLoadCompleted(PageShiftBean t) {
                mView.hideLoading();
                mView.onShiftRequest(t);
            }

            @Override
            public void onLoadFailed(String errMsg) {
                mView.hideLoading();
                mView.showToast(errMsg);
            }
        });
    }

    @Override
    public int getStartIndex() {
        return mShiftModel.getStartIndex();
    }
}
