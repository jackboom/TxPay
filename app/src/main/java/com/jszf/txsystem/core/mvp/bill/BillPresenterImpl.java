package com.jszf.txsystem.core.mvp.bill;

import com.jszf.txsystem.bean.BillBean;
import com.jszf.txsystem.core.mvp.base.BasePresenters;
import com.jszf.txsystem.core.OnRequestListener;

import java.util.HashMap;

/**
 * Created by wpj on 16/6/14下午2:33.
 */
public class BillPresenterImpl extends BasePresenters<IBillView> implements IBillPresenter {

    private IBillModel mBillModel;

    public BillPresenterImpl(IBillView mIBillView) {
        super(mIBillView);
        mBillModel = new BillModelImpl();
    }

    @Override
    public void requestBillInfo(HashMap<String, String> params) {
        if (mView != null){
            mView.showLoading();
        }
        mBillModel.requestBillInfo(params, new OnRequestListener<BillBean>() {
            @Override
            public void onLoadCompleted(BillBean t) {
                if (mView != null){
                    mView.hideLoading();
                    mView.onBillRequest(t);
                }
            }

            @Override
            public void onLoadFailed(String errMsg) {
                if (mView != null){
                    mView.hideLoading();
                    mView.showToast(errMsg);
                }
            }
        });
    }

    @Override
    public int getStartIndex() {
        return mBillModel.getStartIndex();
    }
}
