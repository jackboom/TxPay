package com.jszf.txsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.NoticeBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class NoticeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private static Context mContext;
    private List<NoticeBean.NoticeList> list;
    private LayoutInflater layoutInflater;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public NoticeAdapter(Context mContext) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup mViewGroup, int i) {
            View view = layoutInflater.inflate(R.layout.item_layout_message, null);
            ViewHolder mHolder = new ViewHolder(view);
            //将创建的View注册点击事件
            view.setOnClickListener(this);
            return mHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mViewHolder, int i) {
            ViewHolder mHolder = (ViewHolder) mViewHolder;
            // 绑定数据到ViewHolder上
            try {
                NoticeBean.NoticeList notice = list.get(i);
                mHolder.itemView.setTag(notice); //将数据保存在itemView的Tag中，
                 mHolder.tv_message_title.setText(notice.getTitle());
                String time = notice.getCreateDate();
                if (!TextUtils.isEmpty(time)) {
                    time = time.substring(0,4)+"."+time.substring(4,6)+"."+time.substring(6,8);
                }else {
                    time = "";
                }
                mHolder.tv_message_date.setText(time);
            }
            catch (Exception e) {
                Log.d("TAG","异常："+e.toString());
            }
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (NoticeBean.NoticeList) v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, NoticeBean.NoticeList data);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_message_title;
        private TextView tv_message_date;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_message_title = (TextView) itemView.findViewById(R.id.tv_message_title);
            tv_message_date = (TextView) itemView.findViewById(R.id.tv_message_date);
        }
    }

    public void setData(List<NoticeBean.NoticeList> noticeList) {
        if (null == noticeList) {
            return;
        }
        list = noticeList;
        notifyDataSetChanged();
    }

    public void addData(List<NoticeBean.NoticeList> noticeList) {
        if (null == noticeList) {
            return;
        }
        list.addAll(noticeList);
        notifyDataSetChanged();
    }
}
