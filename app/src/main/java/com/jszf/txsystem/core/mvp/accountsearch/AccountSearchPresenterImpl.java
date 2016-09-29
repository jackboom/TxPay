package com.jszf.txsystem.core.mvp.accountsearch;

import com.jszf.txsystem.bean.AccountSearchBean;
import com.jszf.txsystem.core.mvp.base.BasePresenters;
import com.jszf.txsystem.core.OnRequestListener;

import java.util.HashMap;

/**
 * Created by wpj on 16/6/14下午2:33.
 */
public class AccountSearchPresenterImpl extends BasePresenters<IAccountSearchView> implements IAccountSearchPresenter {

    private IAccountSearchModel mAccountSearchModel;

    public AccountSearchPresenterImpl(IAccountSearchView mIAccountSearchView) {
        super(mIAccountSearchView);
        mAccountSearchModel = new AccountSearchModelImpl();
    }

    @Override
    public void requestAccountSearch(HashMap<String, String> params) {
        mView.showLoading();
        mAccountSearchModel.requestAccountSearch(params, new OnRequestListener<AccountSearchBean>() {
            @Override
            public void onLoadCompleted(AccountSearchBean t) {
                mView.hideLoading();
                mView.onAccountRequest(t);
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
        return mAccountSearchModel.getStartIndex();
    }
}
