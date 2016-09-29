package com.jszf.txsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.BillInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/23.
 */
public class BillInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    // 上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    // 正在加载中
    public static final int LOADING_MORE = 1;
    private static final int TYPE_ITEM = 0; // 普通Item View
    private static final int TYPE_FOOTER = 1; // 底部FootView
    private static Context mContext;
    List<BillInfo> list;
    private LayoutInflater layoutInflater;
    // 上拉加载更多状态-默认为0
    private int load_more_status = 0;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private int currentPage = 0;
    private int count;
    private List<BillInfo> itemList  = new ArrayList<>();
    public static boolean mIsFooterEnable = false;//是否允许加载更多

    //记录是否滑动到底部
    private boolean isFooter = false;

    //记录是否滑动到顶部
    private boolean isHeader = false;


    /**
     * 标记是否正在加载更多，防止再次调用加载更多接口
     */
    public static boolean mIsLoadingMore;
    /**
     * 标记加载更多的position
     */
    public int mLoadMorePosition;
    public static LoadMoreListener mListener; //加载更多监听

    public BillInfoAdapter(Context mContext, List<BillInfo> mList) {
        this.mContext = mContext;
        list = mList;
        Log.d("TAG","adapter:"+list.size());
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup mViewGroup, int i) {
        if (i == TYPE_ITEM) {
            View view = layoutInflater.inflate(R.layout.item_layout_account, null);
            ViewHolder mHolder = new ViewHolder(view);
            //将创建的View注册点击事件
            view.setOnClickListener(this);
            return mHolder;
        } else if (i == TYPE_FOOTER) {
            View foot_view = layoutInflater.inflate(
                    R.layout.item_bill_footer, mViewGroup, false);
            // 这边可以做一些属性设置，甚至事件监听绑定
            // view.setBackgroundColor(Color.RED);
            FootViewHolder footViewHolder = new FootViewHolder(foot_view);
            return footViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mViewHolder, int i) {
        if (mViewHolder instanceof ViewHolder) {
            // 绑定数据到ViewHolder上
            try {
                BillInfo mBillInfo = list.get(i);
                mViewHolder.itemView.setTag(mBillInfo); //将数据保存在itemView的Tag中，
                ((ViewHolder) mViewHolder).tv_account_paytime.setText(mBillInfo.getPayTime());
                if (mBillInfo.getPayType().equals("1")) {
                    ((ViewHolder) mViewHolder).iv_item_account.setImageResource(R.drawable.product_icon_weixin);
                } else if (mBillInfo.getPayType().equals("2")) {
                    ((ViewHolder) mViewHolder).iv_item_account.setImageResource(R.drawable.product_icon_alipay);
                } else if (mBillInfo.getPayType().equals("3")) {
                    ((ViewHolder) mViewHolder).iv_item_account.setImageResource(R.drawable.home_icon_baidupay);
                } else if (mBillInfo.getPayType().equals("4")) {
                    ((ViewHolder) mViewHolder).iv_item_account.setImageResource(R.drawable.home_icon_yizf);
                }
                if (mBillInfo.getPayState().equals("0")) {
                    ((ViewHolder) mViewHolder).tv_account_paystate.setText("订单支付中");
                } else if (mBillInfo.getPayState().equals("1")) {
                    ((ViewHolder) mViewHolder).tv_account_paystate.setText("订单支付成功");
                } else if (mBillInfo.getPayState().equals("2")) {
                    ((ViewHolder) mViewHolder).tv_account_paystate.setText("订单支付失败");
                } else if (mBillInfo.getPayState().equals("6")) {
                    ((ViewHolder) mViewHolder).tv_account_paystate.setText("订单未支付");
                }
                String amount = mBillInfo.getPayAmount();
                if (!TextUtils.isEmpty(amount)) {
                    float payAmount = (float)(Integer.parseInt(amount))/100;
                    ((ViewHolder) mViewHolder).tv_account_amount.setText(payAmount+"");
                }
            }
            catch (Exception e) {
                Log.d("TAG","异常："+e.toString());
            }
        } else if (mViewHolder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) mViewHolder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.foot_view_item_tv.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footViewHolder.foot_view_item_tv.setText("正在加载更多数据...");
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = list.size();
        if (mIsFooterEnable) count++;
        return count;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (BillInfo) v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        int footerPosition = getItemCount() - 1;
        if (footerPosition == position && mIsFooterEnable) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
//        // 最后一个item设置为footerView
//            if (position + 1 == getItemCount()) {
//                return TYPE_FOOTER;
//            } else {
//                return TYPE_ITEM;
//            }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, BillInfo data);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_item_account;
        private TextView tv_account_paystate;
        private TextView tv_account_amount;
        private TextView tv_account_paytime;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_item_account = (ImageView) itemView.findViewById(R.id.iv_item_account);
            tv_account_amount = (TextView) itemView.findViewById(R.id.tv_account_amount);
            tv_account_paystate = (TextView) itemView.findViewById(R.id.tv_account_paystate);
            tv_account_paytime = (TextView) itemView.findViewById(R.id.tv_account_paytime);
        }
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView foot_view_item_tv;

        public FootViewHolder(View view) {
            super(view);
            foot_view_item_tv = (TextView) view
                    .findViewById(R.id.foot_view_item_tv);
        }
    }
    public void addMoreItem(List<BillInfo> newData) {
        list = newData;
        notifyDataSetChanged();
    }
    /**
     * //上拉加载更多 PULLUP_LOAD_MORE=0; //正在加载中 LOADING_MORE=1; //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }
    public void setData(List<BillInfo> data)
    {
        // TODO Auto-generated method stub
        this.list = data;
    }
    /**
     * 设置加载更多的监听
     *
     * @param listener
     */
    public  void setLoadMoreListener(LoadMoreListener listener) {
        mListener = listener;
    }

    /**
     * 设置正在加载更多
     *
     * @param loadingMore
     */
    public void setLoadingMore(boolean loadingMore) {
        this.mIsLoadingMore = loadingMore;
    }

    /**
     * 加载更多监听
     */
    public  interface LoadMoreListener {
        /**
         * 加载更多
         */
        void onLoadMore();
    }

    /**
     * 通知更多的数据已经加载
     *
     * 每次加载完成之后添加了Data数据，用notifyItemRemoved来刷新列表展示，
     * 而不是用notifyDataSetChanged来刷新列表
     *
     * @param hasMore
     */
    public void notifyMoreFinish(boolean hasMore) {
        setAutoLoadMoreEnable(hasMore);
        this.notifyItemRemoved(mLoadMorePosition);
        mIsLoadingMore = false;
    }
    /**
     * 设置是否支持自动加载更多
     *
     * @param autoLoadMore
     */
    public void setAutoLoadMoreEnable(boolean autoLoadMore) {
        mIsFooterEnable = autoLoadMore;
    }
}
