package com.jszf.txsystem.core.mvp.home;

import com.jszf.txsystem.bean.HomeBean;
import com.jszf.txsystem.core.OnRequestListener;

import java.util.HashMap;

/**
 * Created by wpj on 16/6/14下午2:32.
 */
public interface IHomeModel {

    void requestHomeInfo(HashMap<String,String> params, OnRequestListener<HomeBean> listener);
}
