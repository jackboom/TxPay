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
import com.jszf.txsystem.bean.AccountSearchBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class AccountSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static Context mContext;
    List<AccountSearchBean.SettleInfoList> list;
    private LayoutInflater layoutInflater;
    private static final int HIGHT_TYPE = 0;
    private static final int LOW_TYPE = 1;
    public AccountSearchAdapter(Context mContext) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }
//    public AccountSearchAdapter(List<AccountSearchBean.SettleInfoList> lists,Context mContext) {
//        this.mContext = mContext;
//        list=lists;
//        layoutInflater = LayoutInflater.from(mContext);
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup mViewGroup, int i) {

            View view = layoutInflater.inflate(R.layout.reeive_item_account, null);
            ViewHolder mHolder = new ViewHolder(view);
            return mHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mViewHolder, int i) {

            // 绑定数据到ViewHolder上
            try {
                ViewHolder mHolder = (ViewHolder) mViewHolder;
                AccountSearchBean.SettleInfoList settleInfo = list.get(i);
                mHolder.itemView.setTag(settleInfo); //将数据保存在itemView的Tag中，
                String str =  settleInfo.getPayTime();
                if (!TextUtils.isEmpty(str)) {
                    str = str.substring(0,4)+"."+str.substring(4,6)+"."+str.substring(6,8)+" "+
                            str.substring(8,10)+":"+str.substring(10,12)+":"+str.substring(12,14);
                } else {
                    str = "";
                }
                mHolder.tv_receive_time.setText(str);
                mHolder.tv_receive_amount.setText(Float.parseFloat(settleInfo.getSetteleAmount())+"元");
                mHolder.tv_receive_fee.setText(Float.parseFloat(settleInfo.getAllFee())+"元");
                mHolder.tv_receive_principal.setText(Float.parseFloat(settleInfo.getInAmount())+"元");

            }
            catch (Exception e) {
                Log.d("TAG","异常："+e.toString());
            }
    }

    @Override
    public int getItemCount() {
        int count = (list == null ? 0 : list.size());
        return count;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_receive_principal;
        private TextView tv_receive_fee;
        private TextView tv_receive_amount;
        private TextView tv_receive_time;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_receive_principal = (TextView) itemView.findViewById(R.id.tv_receive_principal);
            tv_receive_fee = (TextView) itemView.findViewById(R.id.tv_receive_fee);
            tv_receive_amount = (TextView) itemView.findViewById(R.id.tv_receive_amount);
            tv_receive_time = (TextView) itemView.findViewById(R.id.tv_receive_time);
        }
    }

    public void setData(List<AccountSearchBean.SettleInfoList> settleList) {
        if (null == settleList) {
            return;
        }
        list = settleList;
        notifyDataSetChanged();
    }

    public void addData(List<AccountSearchBean.SettleInfoList> settleList) {
        if (null == settleList) {
            return;
        }
        list.addAll(settleList);
        notifyDataSetChanged();
    }
}
