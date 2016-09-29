package com.jszf.txsystem.core.mvp.main;

import com.jszf.txsystem.bean.MainBean;
import com.jszf.txsystem.core.OnRequestListener;

import java.util.HashMap;

/**
 * @author jacking
 *         Created at 2016/8/31 15:33 .
 */
public interface IMainModel {
    void requestFirstInfo(HashMap<String, String> params, OnRequestListener<MainBean> listener);

}
