package com.jszf.txsystem.core.mvp.bill;

import java.util.HashMap;

/**
 * Created by wpj on 16/6/14下午2:32.
 */
public interface IBillPresenter {

    void requestBillInfo(HashMap<String, String> params);
    int getStartIndex();
}
