package com.jszf.txsystem.core.mvp.login;

import com.jszf.txsystem.bean.LoginBean;
import com.jszf.txsystem.core.OnRequestListener;
import com.jszf.txsystem.core.mvp.base.BasePresenters;

import java.util.HashMap;

/**
 * @author jacking
 *         Created at 2016/8/31 15:35 .
 */
public class LoginPresenterImpl extends BasePresenters<ILoginView> implements ILoginPresenter {
    private ILoginModel mLoginModel;

    public LoginPresenterImpl(ILoginView v) {
        super(v);
        mLoginModel = new LoginModelImpl();
    }


    @Override
    public void requestLogin(HashMap<String, String> params) {
        mView.showLoading();
        mLoginModel.requestLogin(params, new OnRequestListener<LoginBean>() {
            @Override
            public void onLoadCompleted(LoginBean t) {
                mView.hideLoading();
                mView.login(t);
            }

            @Override
            public void onLoadFailed(String errMsg) {
                mView.hideLoading();
                mView.showToast(errMsg);
            }
        });
    }
}
