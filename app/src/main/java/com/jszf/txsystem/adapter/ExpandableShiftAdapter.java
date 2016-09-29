package com.jszf.txsystem.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.ShiftSimpleBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jacking
 *         Created at 2016/9/21 14:58 .
 */
public class ExpandableShiftAdapter extends BaseExpandableListAdapter {
    private List<ShiftSimpleBean> mGroup = new ArrayList<>();// 组名
    private LayoutInflater mInflater;
    private Context mContext;

    public ExpandableShiftAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;

    }

    @Override
    public int getGroupCount() {
        return (mGroup == null ? 0 : mGroup.size());
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (mGroup == null) ? 0 : mGroup.get(groupPosition).getChildList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroup.get(groupPosition).getChildList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupViewHolder vHolder = null;
        if (convertView == null) {
            Log.d("ExpandableShiftAdapter", "convertView==null:" + (convertView == null));
            convertView = mInflater.inflate(
                    R.layout.item_shift_view, null);
            vHolder = new GroupViewHolder();
            vHolder.mTvShiftStart = (TextView) convertView.findViewById(R.id.tv_shift_start);
            vHolder.mTvShiftEnd = (TextView) convertView.findViewById(R.id.tv_shift_end);
            vHolder.mTvShiftReceive = (TextView) convertView.findViewById(R.id.tv_shift_amount_all);
            vHolder.mTvShiftXClass = (TextView) convertView.findViewById(R.id.tv_item_shift_class);
            vHolder.mIvShiftArrow = (ImageView) convertView.findViewById(R.id.iv_item_shift_arrow);
            convertView.setTag(vHolder);
        } else {
            Log.d("ExpandableShiftAdapter", "convertView==null:" + (convertView != null));
            vHolder = (GroupViewHolder) convertView.getTag();
        }
        try {
            Log.d("ExpandableShiftAdapter", "mGroup.size():" + mGroup.size());
            ShiftSimpleBean data = mGroup.get(groupPosition);
            String str1 = data.getStartTime();
            String str2 = data.getEndTime();
            String startTime = str1.substring(5, str1.length() - 3);
            startTime = startTime.replace("/","-");
            String endTime = str2.substring(5, str2.length() - 3);
            endTime  = endTime.replace("/","-");
            Log.d("ExpandableShiftAdapter", "startTime:" + startTime);
            Log.d("ExpandableShiftAdapter", "endTime:" + endTime);
            vHolder.mTvShiftStart.setText(startTime);
            vHolder.mTvShiftEnd.setText(endTime);
            vHolder.mTvShiftReceive.setText(data.getRecive() + "");
            vHolder.mTvShiftXClass.setText(data.getClassId());


        } catch (Exception e) {
            Log.d("TAG", "group_异常：" + e.toString());
        }
        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_shift_container);
        View divider = mInflater.inflate(R.layout.divider_shift_group,null);

        if (isExpanded) {
            vHolder.mIvShiftArrow.setImageResource(R.drawable.icon_shift_arrow_up);
            convertView.setBackgroundResource(R.drawable.shape_item_shift_top);
            Log.d("ExpandableShiftAdapter", "展开");
        } else {
            vHolder.mIvShiftArrow.setImageResource(R.drawable.icon_shift_arrow);
            convertView.setBackgroundResource(R.drawable.shape_item_shift_bg);
            Log.d("ExpandableShiftAdapter", "关闭");
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        List<ShiftSimpleBean.Child> mChildren =new ArrayList<>();// 每一组对应的child
        try {
            ViewHolder vHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_shift_detail_info, null);
                vHolder = new ViewHolder();
                vHolder.mIvShiftType = (ImageView) convertView.findViewById(R.id.iv_shift_type);
                vHolder.mTvShiftAmount = (TextView) convertView.findViewById(R.id.tv_shift_amount);
                vHolder.mTvShiftCount = (TextView) convertView.findViewById(R.id.tv_shift_count);
                vHolder.mTvShiftState = (TextView) convertView.findViewById(R.id.tv_shift_state);
                vHolder.mIndicator = (TextView) convertView.findViewById(R.id.tv_head_indicator);
                vHolder.mIndicator.setVisibility(View.GONE);
                vHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_shift);
                vHolder.add = (LinearLayout) convertView.findViewById(R.id.ll_add);
                vHolder.add.setVisibility(View.GONE);
                convertView.setTag(vHolder);
            } else {
                vHolder = (ViewHolder) convertView.getTag();
            }
            mChildren = mGroup.get(groupPosition).getChildList();
            mChildren.get(childPosition).getShiftType();
            Log.d("ExpandableShiftAdapter", "mChildren.size():" + mChildren.size());
            Log.d("ExpandableShiftAdapter", "amount:" + mChildren.get(childPosition).getShiftAmount());
            vHolder.mTvShiftAmount.setText(mChildren.get(childPosition).getShiftAmount());
            vHolder.mIvShiftType.setImageResource(mChildren.get(childPosition).getShiftType());
            vHolder.mTvShiftCount.setText(mChildren.get(childPosition).getShiftCount() + "笔");
//            vHolder.mTvShiftCount.setText("2314121笔");
            vHolder.mTvShiftState.setText(mChildren.get(childPosition).getShiftState());
            Log.d("ExpandableShiftAdapter", "childPosition:" + childPosition);
            if (childPosition ==0) {
                vHolder.mIndicator.setVisibility(View.VISIBLE);
                vHolder.add.setVisibility(View.GONE);
            }else if (childPosition ==mChildren.size()-1){
                vHolder.linearLayout.setBackgroundResource(R.drawable.shape_item_shift_bottm);
                vHolder.mIndicator.setVisibility(View.GONE);
                vHolder.add.setVisibility(View.VISIBLE);

            }else {
//                linearLayout.removeView(divider);
                vHolder.add.setVisibility(View.GONE);
                vHolder.mIndicator.setVisibility(View.GONE);
            }
        }catch (Exception e){
            Log.d("TAG", "child_异常：" + e.toString());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public void addDatas(List<ShiftSimpleBean> dataList) {
        if (null == dataList) {
            return;
        }
        mGroup.addAll(dataList);
        notifyDataSetChanged();
    }
    public void setDatas(List<ShiftSimpleBean> dataList) {
        if (null == dataList) {
            return;
        }
        mGroup = dataList;
        notifyDataSetChanged();
    }

//    public void setData(List<PageShiftBean.Data> dataList, HashMap<Integer, List<ShiftItemInfo>> child) {
//        if (null == dataList) {
//            return;
//        }
//        mGroup = dataList;
//        mChildren = child;
//        notifyDataSetChanged();
//    }
//
//    public void addData(List<PageShiftBean.Data> dataList, HashMap<Integer, List<ShiftItemInfo>> child) {
//        if (null == dataList) {
//            return;
//        }
//        mGroup.addAll(dataList);
//        mChildren.putAll(child);
//        notifyDataSetChanged();
//    }

    class ViewHolder {
        public ImageView mIvShiftType;
        public TextView mTvShiftState;
        public TextView mTvShiftAmount;
        public TextView mTvShiftCount;
        public TextView mIndicator;
        public LinearLayout linearLayout;
        private LinearLayout add;
    }

    class GroupViewHolder {
        public TextView mTvShiftStart;
        public TextView mTvShiftEnd;
        public TextView mTvShiftReceive;
        public TextView mTvShiftXClass;
        public ImageView mIvShiftArrow;
    }

    public void rotateAnimDown(ImageView imageView) {
        Animation anim =new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(500); // 设置动画时间
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        imageView.startAnimation(anim);
    }
    public void rotateAnimUp(ImageView imageView) {
        Animation anim =new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(500); // 设置动画时间
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        imageView.startAnimation(anim);
    }
}



