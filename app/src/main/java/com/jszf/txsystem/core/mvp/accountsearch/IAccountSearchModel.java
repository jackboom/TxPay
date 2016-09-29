package com.jszf.txsystem.core.mvp.accountsearch;

import com.jszf.txsystem.bean.AccountSearchBean;
import com.jszf.txsystem.core.OnRequestListener;

import java.util.HashMap;

/**
 * Created by wpj on 16/6/14下午2:32.
 */
public interface IAccountSearchModel {

    void requestAccountSearch(HashMap<String, String> params, OnRequestListener<AccountSearchBean> listener);
    int getStartIndex();
}
