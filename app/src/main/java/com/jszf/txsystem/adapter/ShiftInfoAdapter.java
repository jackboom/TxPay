package com.jszf.txsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.BillInfo;
import com.jszf.txsystem.bean.PageShiftBean;

import java.util.List;

/**
 * Created by Administrator on 2016/3/23.
 */
public class ShiftInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static Context mContext;
    List<PageShiftBean.Data> list;

    private LayoutInflater layoutInflater;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public ShiftInfoAdapter(Context mContext) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_shift_view, null);
        ViewHolder mHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        //将创建的View注册点击事件
        return mHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        // 绑定数据到ViewHolder上
        try {
            PageShiftBean.Data data = list.get(position);
            String str1 = data.getStartTime();
            String str2 = data.getEndTime();
            String startTime = str1.substring(5,str1.length()-3);
            String endTime = str1.substring(5,str2.length()-3);
            mHolder.itemView.setTag(data);
//            mHolder.mTvShiftXClass.setText(data.getUserName());
            mHolder.mTvShiftStart.setText(startTime);
            mHolder.mTvShiftEnd.setText(endTime);
            mHolder.mTvShiftReceive.setText(data.getRecive() + "");
        } catch (Exception e) {
            Log.d("TAG", "异常：" + e.toString());
        }
    }

    @Override
    public int getItemCount() {
        int count = (list == null ? 0 : list.size());
        return count;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvShiftXClass;
        private ImageView mIvShiftArrow;
        private TextView mTvShiftStart;
        private TextView mTvShiftEnd;
        private TextView mTvShiftReceive;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvShiftStart = (TextView) itemView.findViewById(R.id.tv_shift_start);
            mTvShiftEnd = (TextView) itemView.findViewById(R.id.tv_shift_end);
            mTvShiftReceive = (TextView) itemView.findViewById(R.id.tv_shift_amount_all);
            mTvShiftXClass = (TextView) itemView.findViewById(R.id.tv_item_shift_class);
            mIvShiftArrow = (ImageView) itemView.findViewById(R.id.iv_item_shift_arrow);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (BillInfo) v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, BillInfo data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setData(List<PageShiftBean.Data> dataList) {
        if (null == dataList) {
            return;
        }
        list = dataList;
        notifyDataSetChanged();
    }

    public void addData(List<PageShiftBean.Data> dataList) {
        if (null == dataList) {
            return;
        }
        list.addAll(dataList);
        notifyDataSetChanged();
    }

}
