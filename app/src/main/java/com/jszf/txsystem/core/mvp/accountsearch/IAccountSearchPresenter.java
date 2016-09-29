package com.jszf.txsystem.core.mvp.accountsearch;

import java.util.HashMap;

/**
 * Created by wpj on 16/6/14下午2:32.
 */
public interface IAccountSearchPresenter {

    void requestAccountSearch(HashMap<String, String> params);
    int getStartIndex();
}
