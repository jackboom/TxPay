package com.jszf.txsystem.core.mvp.login;

import com.jszf.txsystem.bean.LoginBean;
import com.jszf.txsystem.core.mvp.base.BaseMvpView;

/**
 * @author jacking
 *         Created at 2016/8/31 15:32 .
 */
public interface ILoginView extends BaseMvpView {
    void login(LoginBean loginBean);

}
