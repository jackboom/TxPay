package com.jszf.txsystem.core.mvp.notice;

import java.util.HashMap;

/**
 * Created by wpj on 16/6/14下午2:32.
 */
public interface INoticePresenter {

    void requestNoticeInfo(HashMap<String, String> params);
    int getStartIndex();
}
