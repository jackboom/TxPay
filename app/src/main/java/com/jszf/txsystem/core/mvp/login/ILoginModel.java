package com.jszf.txsystem.core.mvp.login;

import com.jszf.txsystem.bean.LoginBean;
import com.jszf.txsystem.core.OnRequestListener;

import java.util.HashMap;

/**
 * @author jacking
 *         Created at 2016/8/31 15:33 .
 */
public interface ILoginModel {
    void requestLogin(HashMap<String,String> params, OnRequestListener<LoginBean> listener);

}
