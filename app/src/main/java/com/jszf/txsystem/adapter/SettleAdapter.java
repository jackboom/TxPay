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
import com.jszf.txsystem.bean.SettleInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class SettleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // 上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    // 正在加载中
    public static final int LOADING_MORE = 1;
    private static final int TYPE_ITEM = 0; // 普通Item View
    private static final int TYPE_FOOTER = 1; // 底部FootView
    private static Context mContext;
    List<SettleInfo> list;
    private LayoutInflater layoutInflater;
    // 上拉加载更多状态-默认为0
    private int load_more_status = 0;
    private int currentPage = 0;
    private int count;
    private List<SettleInfo> itemList  = new ArrayList<>();
    public static boolean mIsFooterEnable = false;//是否允许加载更多
    public static boolean mIsLoadingMore;
    /**
     * 标记加载更多的position
     */
    public int mLoadMorePosition;
    //记录是否滑动到底部
    private boolean isFooter = false;

    //记录是否滑动到顶部
    private boolean isHeader = false;

    public SettleAdapter(Context mContext, List<SettleInfo> mList) {
        this.mContext = mContext;
        list = mList;
        Log.d("TAG", "adapter:" + list.size());
        layoutInflater = LayoutInflater.from(mContext);
    }

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
                SettleInfo mSettleInfo = list.get(i);
                mHolder.itemView.setTag(mSettleInfo); //将数据保存在itemView的Tag中，
                String str =  mSettleInfo.getPayTime();
                if (!TextUtils.isEmpty(str)) {
                    str = str.substring(0,4)+"."+str.substring(4,6)+"."+str.substring(6,8)+" "+
                            str.substring(8,10)+":"+str.substring(10,12)+":"+str.substring(12,14);
                } else {
                    str = "";
                }
                Log.d("TAG","time:"+str+",settle:"+ mSettleInfo.getSetteleAmount()+",allfee:"+ mSettleInfo.getAllFee()+",inam:"+ mSettleInfo.getInAmount());
                mHolder.tv_receive_time.setText(str);
                mHolder.tv_receive_amount.setText(Float.parseFloat(mSettleInfo.getSetteleAmount())+"元");
                mHolder.tv_receive_fee.setText(Float.parseFloat(mSettleInfo.getAllFee())+"元");
                mHolder.tv_receive_principal.setText(Float.parseFloat(mSettleInfo.getInAmount())+"元");

            }
            catch (Exception e) {
                Log.d("TAG","异常："+e.toString());
            }
    }

    @Override
    public int getItemCount() {
        int count = list.size();

        if (isFooter) {
            count=count+1;
        }
        if (isHeader) {
            count=count+1;
        }
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
    public boolean getIsHeader(){
        return isHeader;
    }

    public boolean getIsFooter(){
        return isFooter;
    }

    public void setIsHeader(boolean b){
        isHeader = b;
    }

    public void setIsFooter(boolean b){
        isFooter = b;
    }
}
