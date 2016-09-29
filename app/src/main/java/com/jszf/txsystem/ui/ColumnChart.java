package com.jszf.txsystem.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.ScoreBean;

import java.util.List;

/**
 * 柱状图
 * 
 * @author Administrator
 * 
 */
public class ColumnChart extends View {

	private List<ScoreBean> mScoreBean;
	private float tb;
	private float interval_left_right;
	private Paint paint_type, paint_rectf_gray, paint_rectf_red,paint_amount;

	private int fineLineColor = 0x5faaaaaa; // 灰色
	private int blueLineColor = 0xff00ffff; // 蓝色

	public ColumnChart(Context context, List<ScoreBean> mScoreBean) {
		super(context);
		init(mScoreBean);
	}

	public void init(List<ScoreBean> mScoreBean) {
		if (null == mScoreBean || mScoreBean.size() == 0)
			return;
		this.mScoreBean = mScoreBean;
		Resources res = getResources();
		tb = res.getDimension(R.dimen.historyscore_tb);
		interval_left_right = tb * 6.0f;
		Log.d("HomeColumnar", "interval_left_right:" + interval_left_right);

		paint_type = new Paint();
		paint_type.setStrokeWidth(tb * 0.1f);
		paint_type.setTextSize(tb * 1.1f);
		paint_type.setColor(getResources().getColor(R.color.analyse_month_amount));
		paint_type.setTextAlign(Align.CENTER);


		paint_amount = new Paint();
		paint_amount.setStrokeWidth(tb * 0.1f);
		paint_amount.setTextSize(tb * 1.2f);
		paint_amount.setColor(getResources().getColor(R.color.analyse_text_paytype));
		paint_amount.setTextAlign(Align.CENTER);

		paint_rectf_gray = new Paint();
		paint_rectf_gray.setStrokeWidth(tb * 0.1f);
		paint_rectf_gray.setColor(fineLineColor);
		paint_rectf_gray.setStyle(Style.FILL);
		paint_rectf_gray.setAntiAlias(true);

		paint_rectf_red = new Paint();
		paint_rectf_red.setStrokeWidth(tb * 0.1f);
		paint_rectf_red.setColor(getResources().getColor(R.color.analyse_text_paytype));
		paint_rectf_red.setStyle(Style.FILL);
		paint_rectf_red.setAntiAlias(true);

		setLayoutParams(new LayoutParams(
				(int) (this.mScoreBean.size() * interval_left_right),
				LayoutParams.MATCH_PARENT));
	}

	protected void onDraw(Canvas c) {
		if (null == mScoreBean || mScoreBean.size() == 0)
			return;
		Log.d("HomeColumnar", "开始");
//		drawDate(c);
		drawType(c);
		drawAmount(c);
		drawRectf(c);
	}

	/**
	 * 绘制矩形
	 * 
	 * @param c
	 */
	public void drawRectf(Canvas c) {
		for (int i = 0; i < mScoreBean.size(); i++) {
			Log.d("HomeColumnar", "getHeight():" + getHeight());
			RectF f = new RectF();
			f.set(tb * 0.5f + interval_left_right * i,
					getHeight() - tb * 13.0f, tb * 4.5f + interval_left_right
							* i, getHeight() - tb * 2.0f);
			c.drawRoundRect(f, tb * 0.3f, tb * 0.3f, paint_rectf_gray);

			float base = mScoreBean.get(i).score * (tb * 11.0f / 100);

			RectF f1 = new RectF();
			f1.set(tb * 0.5f + interval_left_right * i, getHeight()
							- (base + tb * 2.0f), tb * 4.5f + interval_left_right * i,
					getHeight() - tb * 2.0f);
			c.drawRoundRect(f1, tb * 0.3f, tb * 0.3f, paint_rectf_red);
		}
	}

	/**
	 * 绘制类型
	 * 
	 * @param c
	 */
	public void drawType(Canvas c) {
		for (int i = 0; i < mScoreBean.size(); i++) {
			String type = mScoreBean.get(i).pay_type;
			c.drawText(type, tb * 2.4f + interval_left_right * i,
					getHeight() - 3.0f, paint_type);
		}
	}
	/**
	 * 绘制金额
	 *
	 * @param c
	 */
	public void drawAmount(Canvas c) {
		for (int i = 0; i < mScoreBean.size(); i++) {
			String amount = mScoreBean.get(i).pay_amount +"";
			c.drawText(amount, tb * 2.4f + interval_left_right * i,
					getHeight() - tb * 14f, paint_type);
		}
	}
}
