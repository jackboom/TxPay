package com.jszf.txsystem.core.mvp.home;

import com.jszf.txsystem.bean.HomeBean;
import com.jszf.txsystem.core.mvp.base.BaseMvpView;

/**
 * Created by wpj on 16/6/14下午2:28.
 */
public interface IHomeView extends BaseMvpView {

    void onHomeRequest(HomeBean homeBean);
}
