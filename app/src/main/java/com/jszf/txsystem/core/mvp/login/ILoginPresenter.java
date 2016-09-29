package com.jszf.txsystem.core.mvp.login;

import java.util.HashMap;

/**
 * @author jacking
 *         Created at 2016/8/31 15:33 .
 */
public interface ILoginPresenter {
    void requestLogin(HashMap<String,String> params);
}
