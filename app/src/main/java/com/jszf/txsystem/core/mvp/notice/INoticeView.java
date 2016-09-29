package com.jszf.txsystem.core.mvp.notice;

import com.jszf.txsystem.bean.NoticeBean;
import com.jszf.txsystem.core.mvp.base.BaseMvpView;

/**
 * Created by wpj on 16/6/14下午2:28.
 */
public interface INoticeView extends BaseMvpView {

    void onNoticeRequest(NoticeBean noticeBean);
}
