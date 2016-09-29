package com.jszf.txsystem.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class PullableExpandableListView extends ExpandableListView implements
		Pullable
{

	public PullableExpandableListView(Context context)
	{
		super(context);
	}

	public PullableExpandableListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	//指示器重新定义
	@Override
	public void setGroupIndicator(Drawable groupIndicator) {
		super.setGroupIndicator(groupIndicator);
	}

	public PullableExpandableListView(Context context, AttributeSet attrs,
									  int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public void setChildDivider(Drawable childDivider) {
		super.setChildDivider(null);
	}

	@Override
	public boolean canPullDown()
	{
		if (getCount() == 0)
		{
			// 没有item的时候也可以下拉刷新
			return true;
		} else if (getFirstVisiblePosition() == 0
				&& getChildAt(0).getTop() >= 0)
		{
			// 滑到顶部了
			return true;
		} else
			return false;
	}

	@Override
	public boolean canPullUp()
	{
		if (getCount() == 0)
		{
			// 没有item的时候也可以上拉加载
			return true;
		} else if (getLastVisiblePosition() == (getCount() - 1))
		{
			// 滑到底部了
			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
					&& getChildAt(
					getLastVisiblePosition()
							- getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
				return true;
		}
		return false;
	}

}
