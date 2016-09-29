package com.jszf.txsystem.core.mvp.notice;

import com.jszf.txsystem.bean.NoticeBean;
import com.jszf.txsystem.core.OnRequestListener;
import com.jszf.txsystem.core.mvp.base.BasePresenters;

import java.util.HashMap;

/**
 * Created by wpj on 16/6/14下午2:33.
 */
public class NoticePresenterImpl extends BasePresenters<INoticeView> implements INoticePresenter {

    private INoticeModel mNoticeModel;

    public NoticePresenterImpl(INoticeView mINoticeView) {
        super(mINoticeView);
        mNoticeModel = new NoticeModelImpl();
    }

    @Override
    public void requestNoticeInfo(HashMap<String, String> params) {
        mNoticeModel.requestNoticeInfo(params, new OnRequestListener<NoticeBean>() {
            @Override
            public void onLoadCompleted(NoticeBean t) {
                mView.onNoticeRequest(t);
            }

            @Override
            public void onLoadFailed(String errMsg) {
                mView.showToast(errMsg);
            }
        });
    }

    @Override
    public int getStartIndex() {
        return mNoticeModel.getStartIndex();
    }
}
