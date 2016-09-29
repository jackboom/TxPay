package com.jszf.txsystem.core.mvp.shift;

import com.jszf.txsystem.bean.PageShiftBean;
import com.jszf.txsystem.core.mvp.base.BaseMvpView;

/**
 * Created by wpj on 16/6/14下午2:28.
 */
public interface IShiftView extends BaseMvpView {

    void onShiftRequest(PageShiftBean pageShiftBean);
}
