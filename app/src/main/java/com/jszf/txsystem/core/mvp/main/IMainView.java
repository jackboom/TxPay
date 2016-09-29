package com.jszf.txsystem.core.mvp.main;

import com.jszf.txsystem.bean.MainBean;
import com.jszf.txsystem.core.mvp.base.BaseMvpView;

/**
 * @author jacking
 *         Created at 2016/8/31 15:32 .
 */
public interface IMainView extends BaseMvpView {
    void requestFirstInfo(MainBean mainBean);

}
