package com.jszf.txsystem.core.mvp.shift;

import java.util.HashMap;

/**
 * Created by wpj on 16/6/14下午2:32.
 */
public interface IShiftPresenter {

    void requestShiftInfo(HashMap<String, String> params);
    int getStartIndex();
}
