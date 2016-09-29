package com.jszf.txsystem.core.mvp.shift;

import com.jszf.txsystem.bean.PageShiftBean;
import com.jszf.txsystem.core.OnRequestListener;

import java.util.HashMap;

/**
 * Created by wpj on 16/6/14下午2:32.
 */
public interface IShiftModel {

    void requestShiftInfo(HashMap<String, String> params, OnRequestListener<PageShiftBean> listener);
    int getStartIndex();
}
