package com.jszf.txsystem.core.mvp.accountsearch;

import com.jszf.txsystem.bean.AccountSearchBean;
import com.jszf.txsystem.core.mvp.base.BaseMvpView;

/**
 * Created by wpj on 16/6/14下午2:28.
 */
public interface IAccountSearchView extends BaseMvpView {

    void onAccountRequest(AccountSearchBean accountSearchBean);
}
