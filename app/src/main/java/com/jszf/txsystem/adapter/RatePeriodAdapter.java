package com.jszf.txsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.HomeBean;
import com.jszf.txsystem.bean.RateProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/6.
 */
public class RatePeriodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // 上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    // 正在加载中
    public static final int LOADING_MORE = 1;
    private static final int TYPE_ITEM = 0; // 普通Item View
    private static final int TYPE_FOOTER = 1; // 底部FootView
    private static Context mContext;
    List<HomeBean.ProductRateList> list;
    private LayoutInflater layoutInflater;
    // 上拉加载更多状态-默认为0
    private int load_more_status = 0;
    //    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private int currentPage = 0;
    private int count;
    private List<RateProduct> itemList = new ArrayList<>();
    public static boolean mIsFooterEnable = false;//是否允许加载更多

    /**
     * 标记是否正在加载更多，防止再次调用加载更多接口
     */
    public static boolean mIsLoadingMore;
    /**
     * 标记加载更多的position
     */
    public int mLoadMorePosition;
    public static LoadMoreListener mListener; //加载更多监听


    public RatePeriodAdapter(Context mContext) {
        this.mContext = mContext;
//        list = mList;
//        Log.d("TAG", "adapter:" + list.size());
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup mViewGroup, int i) {
        if (i == TYPE_ITEM) {
            View view = layoutInflater.inflate(R.layout.rate_item_layout, null);
            ViewHolder mHolder = new ViewHolder(view);
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
//                RateProduct mRateProduct = list.get(i);
                HomeBean.ProductRateList rateProduct = list.get(i);
                mViewHolder.itemView.setTag(rateProduct); //将数据保存在itemView的Tag中，
                ((ViewHolder) mViewHolder).tv_rate_dealRate.setText(rateProduct.rate);
                String type = rateProduct.productRateType;
                String discountType = null;
                if (type.equals("1")) {
                    discountType = "折扣率";
                } else if (type.equals("2")) {
                    discountType = "笔数";
                } else if (type.equals("3")) {
                    discountType = "折扣率+上限";
                } else if (type.equals("4")) {
                    discountType = "折扣率+下限+上限";
                } else if (type.equals("5")) {
                    discountType = "折扣率+下限";
                }
                ((ViewHolder) mViewHolder).tv_rate_discountType.setText(discountType);
                String period = rateProduct.productId;
                String periodType = null;
                if (period.equals("1")) {
                    period = "周结";
                } else if (period.equals("2")) {
                    period = "月结";
                }
                ((ViewHolder) mViewHolder).tv_rate_period.setText(periodType);
                ((ViewHolder) mViewHolder).tv_rate_type.setText(rateProduct.productName);
            } catch (Exception e) {
                Log.d("TAG", "异常：" + e.toString());
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
        int count = (list == null ? 0 :list.size());
        if (mIsFooterEnable) count++;
        return count;
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_rate_type;  //方式
        private TextView tv_rate_discountType;  //折扣类型
        private TextView tv_rate_dealRate;  //交易费率
        private TextView tv_rate_period;  //结算周期

        public ViewHolder(View itemView) {
            super(itemView);
            tv_rate_dealRate = (TextView) itemView.findViewById(R.id.tv_rate_dealRate);
            tv_rate_discountType = (TextView) itemView.findViewById(R.id.tv_rate_discountType);
            tv_rate_period = (TextView) itemView.findViewById(R.id.tv_rate_period);
            tv_rate_type = (TextView) itemView.findViewById(R.id.tv_rate_type);
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

    /**
     * 设置加载更多的监听
     *
     * @param listener
     */
    public static void setLoadMoreListener(LoadMoreListener listener) {
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
    public interface LoadMoreListener {
        /**
         * 加载更多
         */
        void onLoadMore();
    }

    /**
     * 通知更多的数据已经加载
     * <p/>
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

    public void setData(List<HomeBean.ProductRateList> productList) {
        if (null == productList) {
            return;
        }
        list = productList;
        notifyDataSetChanged();
    }

    public void addData(List<HomeBean.ProductRateList> productList) {
        if (null == productList) {
            return;
        }
        list.addAll(productList);
        notifyDataSetChanged();
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
