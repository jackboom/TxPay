package com.jszf.txsystem.ui;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jszf.txsystem.R;


public class MyAlertDialog {
	private Context context;
	private Dialog dialog;
	private LinearLayout lLayout_bg;
	private TextView txt_title;
	private TextView txt_msg;
	private EditText edittxt_result;
	private LinearLayout dialog_Group;
	public ImageView dialog_marBottom;
	public  Button btn_neg;
	public Button btn_pos;
	private ImageView img_line;
	private Display display;
	public ImageView dialog_marTop;
	private boolean showTitle = false;
	private boolean showMsg = false;
	private boolean showEditText = false;
	private boolean showLayout = false;
	private boolean showPosBtn = false;
	private boolean showNegBtn = false;
	private boolean showSubmit = false;
	public RelativeLayout rl_submit;
	private Button btn_submit;

	public MyAlertDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int d1 = dm.widthPixels;
		int d2 = dm.heightPixels;
		Log.d("TAG", "d1:"+d1+",d2:"+d2);
	}

	public MyAlertDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.toast_view_alertdialog, null);

		// 获取自定义Dialog布局中的控件
		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_title.setVisibility(View.GONE);
		txt_msg = (TextView) view.findViewById(R.id.txt_msg);
		txt_msg.setVisibility(View.GONE);
		edittxt_result = (EditText) view.findViewById(R.id.edittxt_result);
		edittxt_result.setVisibility(View.GONE);
		dialog_Group = (LinearLayout) view.findViewById(R.id.dialog_Group);
		dialog_Group.setVisibility(View.GONE);
		dialog_marBottom = (ImageView) view.findViewById(R.id.dialog_marBottom);
		dialog_marTop = (ImageView) view.findViewById(R.id.dialog_marTop);
		btn_neg = (Button) view.findViewById(R.id.btn_neg);
		btn_pos = (Button) view.findViewById(R.id.btn_pos);
		img_line = (ImageView) view.findViewById(R.id.img_line);
		img_line.setVisibility(View.GONE);
//		rl_submit = (RelativeLayout) view.findViewById(R.id.rl_submit);
//		rl_submit.setVisibility(View.GONE);
//		btn_submit = (Button) view.findViewById(R.id.btn_submit);

		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);

		// 调整dialog背景大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));

		return this;
	}

	public MyAlertDialog setTitle(String title) {
		showTitle = true;
		if ("".equals(title)) {
			txt_title.setText("标题");
		} else {
			txt_title.setText(title);
		}
		return this;
	}


	public MyAlertDialog setEditText(String msg) {
		showEditText = true;
		if ("".equals(msg)) {
			edittxt_result.setHint("请输入收款单位名称");
		} else {
			edittxt_result.setText(msg);
		}
		return this;
	}

	public String getResult() {
		return edittxt_result.getText().toString();
	}

	public MyAlertDialog setMsg(String msg) {
		showMsg = true;
		if ("".equals(msg)) {
			txt_msg.setText("内容");
		} else {
			txt_msg.setText(msg);
		}
		return this;
	}

	public MyAlertDialog setView(View view) {
		showLayout = true;
		if (view == null) {
			showLayout = false;
		} else
			dialog_Group.addView(view,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		return this;
	}

	public MyAlertDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public MyAlertDialog setPositiveButton(String text,
										   final OnClickListener listener) {
		showPosBtn = true;
		if ("".equals(text)) {
			btn_pos.setText("确定");
		} else {
			btn_pos.setText(text);
		}
		btn_pos.setOnClickListener(v -> {
			listener.onClick(v);
			dialog.dismiss();
		});
		return this;
	}

//	public MyAlertDialog setSubmitButton(String text,final OnClickListener listener){
////		rl_submit.setVisibility(View.VISIBLE);
//		showSubmit = true;
//		if ("".equals(text)) {
//			btn_pos.setText("确定");
//		} else {
//			btn_pos.setText(text);
//		}
//		btn_submit.setOnClickListener(v -> {
//			listener.onClick(v);
//			dialog.dismiss();
//		});
//		return this;
//	}

	public MyAlertDialog setNegativeButton(String text,
										   final OnClickListener listener) {
		showNegBtn = true;
		if ("".equals(text)) {
			btn_neg.setText("取消");
		} else {
			btn_neg.setText(text);
		}
		btn_neg.setOnClickListener(v -> {
            listener.onClick(v);
            dialog.dismiss();
        });
		return this;
	}

	private void setLayout() {
		if (!showTitle && !showMsg) {
			txt_title.setText("提示");
			txt_title.setVisibility(View.VISIBLE);
		}

		if (showTitle) {
			txt_title.setVisibility(View.VISIBLE);
		}

		if (showEditText) {
			edittxt_result.setVisibility(View.VISIBLE);
		}

		if (showMsg) {
			txt_msg.setVisibility(View.VISIBLE);
		}

		if (showLayout) {
			dialog_Group.setVisibility(View.VISIBLE);
			dialog_marBottom.setVisibility(View.GONE);
		}

		if (!showPosBtn && !showNegBtn) {
			btn_pos.setText("确定");
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
			btn_pos.setOnClickListener(v -> dialog.dismiss());
		}

		if (showPosBtn && showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
			btn_neg.setVisibility(View.VISIBLE);
			btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
			img_line.setVisibility(View.VISIBLE);
		}

		if (showPosBtn && !showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}

		if (!showPosBtn && showNegBtn) {
			btn_neg.setVisibility(View.VISIBLE);
			btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}

//		if (!showSubmit) {
//			rl_submit.setVisibility(View.VISIBLE);
//		}
	}

	public void show() {
		setLayout();
		dialog.show();
	}
}
