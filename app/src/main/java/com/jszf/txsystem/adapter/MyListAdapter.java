package com.jszf.txsystem.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.PageShiftBean;
import com.jszf.txsystem.bean.ShiftItemInfo;

import java.util.HashMap;
import java.util.List;

public class MyListAdapter extends BaseExpandableListAdapter {
	private List<PageShiftBean.Data> mGroup;// 组名
	private HashMap<Integer, List<ShiftItemInfo>> mChildren;// 每一组对应的child
	private LayoutInflater mInflater;
	private Context mContext;

	public MyListAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
	}


	public Object getChild(int groupPosition, int childPosition) {
		return mChildren.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return mChildren.get(groupPosition).size();
	}

	public View getChildView(int groupPosition, int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder vHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_shift_detail_info, null);
			vHolder = new ViewHolder();
			vHolder.mIvShiftType = (ImageView) convertView.findViewById(R.id.iv_shift_type);
			vHolder.mTvShiftAmount = (TextView) convertView.findViewById(R.id.tv_shift_amount);
			vHolder.mTvShiftCount = (TextView) convertView.findViewById(R.id.tv_shift_count);
			vHolder.mTvShiftState = (TextView) convertView.findViewById(R.id.tv_shift_state);
			convertView.setTag(vHolder);
		}else {
			vHolder = (ViewHolder) convertView.getTag();
		}
		vHolder.mTvShiftAmount.setText(mChildren.get(groupPosition).get(childPosition).getShiftAmount());
		vHolder.mTvShiftCount.setText(mChildren.get(groupPosition).get(childPosition).getShiftCount());
		return convertView;
	}

	class ViewHolder{
		public ImageView mIvShiftType;
		public TextView mTvShiftState;
		public TextView mTvShiftAmount;
		public TextView mTvShiftCount;
	}

	class GroupViewHolder{
		public TextView mTvShiftStart;
		public TextView mTvShiftEnd;
		public TextView mTvShiftReceive;
		public TextView mTvShiftXClass;
		public ImageView mIvShiftArrow;
	}

	public Object getGroup(int groupPosition) {
		return mGroup.get(groupPosition);
	}

	public int getGroupCount() {
		return mGroup.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/**
	 * create group view and bind data to view
	 */
	public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent) {
		GroupViewHolder vHolder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.item_shift_view, null);
			vHolder = new GroupViewHolder();
			vHolder.mTvShiftStart = (TextView) convertView.findViewById(R.id.tv_shift_start);
			vHolder.mTvShiftEnd = (TextView) convertView.findViewById(R.id.tv_shift_end);
			vHolder.mTvShiftReceive = (TextView) convertView.findViewById(R.id.tv_shift_amount_all);
			vHolder.mTvShiftXClass = (TextView) convertView.findViewById(R.id.tv_item_shift_class);
			vHolder.mIvShiftArrow = (ImageView) convertView.findViewById(R.id.iv_item_shift_arrow);
		}else {

		}
		try {
			PageShiftBean.Data data = mGroup.get(groupPosition);
			String str1 = data.getStartTime();
			String str2 = data.getEndTime();
			String startTime = str1.substring(5,str1.length()-3);
			String endTime = str1.substring(5,str2.length()-3);
			vHolder.mTvShiftStart.setText(startTime);
			vHolder.mTvShiftEnd.setText(endTime);
			vHolder.mTvShiftReceive.setText(data.getRecive() + "");
		} catch (Exception e) {
			Log.d("TAG", "异常：" + e.toString());
		}
		if (isExpanded)
			vHolder.mIvShiftArrow.setImageResource(R.drawable.icon_shift_arrow);
		else
			vHolder.mIvShiftArrow.setImageResource(R.drawable.icon_shift_arrow);
		return convertView;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}

	public void setData(List<PageShiftBean.Data> dataList,HashMap<Integer, List<ShiftItemInfo>>  child) {
		if (null == dataList) {
			return;
		}
		mGroup = dataList;
		mChildren = child;
		notifyDataSetChanged();
	}

	public void addData(List<PageShiftBean.Data> dataList,HashMap<Integer, List<ShiftItemInfo>>  child) {
		if (null == dataList) {
			return;
		}
		mGroup.addAll(dataList);
		mChildren.putAll(child);
		notifyDataSetChanged();
	}
}
