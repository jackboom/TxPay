package com.jszf.txsystem.core.mvp.bill;

import com.jszf.txsystem.bean.BillBean;
import com.jszf.txsystem.core.mvp.base.BaseMvpView;

/**
 * Created by wpj on 16/6/14下午2:28.
 */
public interface IBillView extends BaseMvpView {

    void onBillRequest(BillBean billBean);
}
