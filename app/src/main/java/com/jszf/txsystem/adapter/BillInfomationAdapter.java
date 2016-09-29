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

import java.util.List;

/**
 * Created by Administrator on 2016/3/23.
 */
public class BillInfomationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static Context mContext;
    List<BillInfo> list;
    private LayoutInflater layoutInflater;
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

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public BillInfomationAdapter(Context mContext) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_layout_account, null);
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
            BillInfo mBillInfo = list.get(position);
            mHolder.itemView.setTag(mBillInfo); //将数据保存在itemView的Tag中，
            mHolder.tv_account_paytime.setText(mBillInfo.getPayTime());
            if (mBillInfo.getPayType().equals("2")) {
                mHolder.iv_item_account.setImageResource(R.drawable.product_icon_weixin);
            } else if (mBillInfo.getPayType().equals("1")) {
                mHolder.iv_item_account.setImageResource(R.drawable.product_icon_alipay);
            } else if (mBillInfo.getPayType().equals("3")) {
                mHolder.iv_item_account.setImageResource(R.drawable.product_icon_bdqb);
            } else if (mBillInfo.getPayType().equals("9")) {
                mHolder.iv_item_account.setImageResource(R.drawable.product_icon_yzf);
            } else if (mBillInfo.getPayType().equals("5")) {
                mHolder.iv_item_account.setImageResource(R.drawable.product_icon_yinlian);
            } else {

            }
            StringBuffer sb = new StringBuffer();
            if (mBillInfo.getPayState().equals("0")) {
                sb.append("订单支付中");
//                mHolder.tv_account_paystate.setText("订单支付中");
            } else if (mBillInfo.getPayState().equals("1")) {
                sb.append("订单支付成功");
//                mHolder.tv_account_paystate.setText("订单支付成功");
            } else if (mBillInfo.getPayState().equals("2")) {
                sb.append("订单支付失败");
//                mHolder.tv_account_paystate.setText("订单支付失败");
            } else if (mBillInfo.getPayState().equals("6")) {
                sb.append("订单未支付");
//                mHolder.tv_account_paystate.setText("订单未支付");
            }
            if (mBillInfo.getRefundState().equals("4")) {
                sb.append("(退款成功)");
            }else if (mBillInfo.getRefundState().equals("5")) {
                sb.append("(退款失败)");
            }else {

            }
            mHolder.tv_account_paystate.setText(sb.toString());
            String amount = mBillInfo.getPayAmount();
            if (!TextUtils.isEmpty(amount)) {
                float payAmount = (float) (Integer.parseInt(amount)) / 100;
                mHolder.tv_account_amount.setText(payAmount + "");
            }
        } catch (Exception e) {
            Log.d("TAG", "异常：" + e.toString());
        }
    }

    @Override
    public int getItemCount() {
        int count = (list == null ? 0 : list.size());
        if (mIsFooterEnable) count++;
        return count;
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

    public boolean getIsHeader() {
        return isHeader;
    }

    public boolean getIsFooter() {
        return isFooter;
    }

    public void setIsHeader(boolean b) {
        isHeader = b;
    }

    public void setIsFooter(boolean b) {
        isFooter = b;
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
    public void setData(List<BillInfo>  billList) {
        if (null == billList) {
            return;
        }
        list = billList;
        notifyDataSetChanged();
    }

    public void addData(List<BillInfo>  billList) {
        if (null == billList) {
            return;
        }
        list.addAll(billList);
        notifyDataSetChanged();
    }

    public ViewHolder getViewHolder(int position) {
        return  this.getViewHolder(position);
    }
}
