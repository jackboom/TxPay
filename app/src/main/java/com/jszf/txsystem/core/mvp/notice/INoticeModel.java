package com.jszf.txsystem.core.mvp.notice;

import com.jszf.txsystem.bean.NoticeBean;
import com.jszf.txsystem.core.OnRequestListener;

import java.util.HashMap;

/**
 * Created by wpj on 16/6/14下午2:32.
 */
public interface INoticeModel {

    void requestNoticeInfo(HashMap<String, String> params, OnRequestListener<NoticeBean> listener);
    int getStartIndex();
}
