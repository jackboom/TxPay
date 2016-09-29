package com.jszf.txsystem.core.mvp.bill;

import com.jszf.txsystem.bean.BillBean;
import com.jszf.txsystem.core.OnRequestListener;

import java.util.HashMap;

/**
 * Created by wpj on 16/6/14下午2:32.
 */
public interface IBillModel {

    void requestBillInfo(HashMap<String, String> params, OnRequestListener<BillBean> listener);
    int getStartIndex();
}
