package com.jszf.txsystem.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jszf.txsystem.R;
import com.jszf.txsystem.core.mvp.base.BaseActivity;
import com.jszf.txsystem.ui.ActionSheetDialog;
import com.jszf.txsystem.ui.MyAlertDialog;
import com.jszf.txsystem.util.PrintUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrintSettingActivity extends BaseActivity {

    @BindView(R.id.iv_print_back)
    ImageView mIvPrintBack;
    @BindView(R.id.rl_print_offtime)
    RelativeLayout mRlPrintOfftime;
    @BindView(R.id.rl_print_outlet)
    RelativeLayout mRlPrintOutlet;
    @BindView(R.id.rl_print_num)
    RelativeLayout mRlPrintNum;
    @BindView(R.id.tv_print_time)
    TextView mTvPrintTime;
    @BindView(R.id.tv_print_outlet)
    TextView mTvPrintOutlet;
    @BindView(R.id.tv_print_num)
    TextView mTvPrintNum;

    private int time;
    private int print_type;
    private String outlet;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_setting);
        ButterKnife.bind(this);
        setStatusBar();
        initPrint();

    }

    private void initPrint() {
        sp = PrintUtils.getSharePrintData(this);
        time = sp.getInt("time",0);
        print_type = sp.getInt("print_type",0);
        outlet = sp.getString("outlet","");
        mTvPrintTime.setText(time + "s");
        String type = getStringType(print_type);
        mTvPrintNum.setText(type);
        mTvPrintOutlet.setText(outlet);
        Log.d("PrintSettingActivity", "time:" + time +",print_type:"+print_type +",outlet:"+outlet);
    }

    @NonNull
    private String getStringType(int mPrint_type) {
        String type = "";
        switch (mPrint_type) {
            case 1:
                type = "顾客联";
                break;
            case 2:
                type = "财务联";
                break;
            case 3:
                type = "顾客联+财务联";
                break;
        }
        return type;
    }

    @OnClick({R.id.iv_print_back, R.id.rl_print_offtime, R.id.rl_print_outlet, R.id.rl_print_num})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_print_back:
                finish();
                break;
            case R.id.rl_print_offtime:
                setOffTime();
                break;
            case R.id.rl_print_outlet:
                setOutLet();
                break;
            case R.id.rl_print_num:
                setPrintType();
                break;
        }
    }

    private void setPrintType() {
        new ActionSheetDialog(PrintSettingActivity.this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("顾客联", ActionSheetDialog.SheetItemColor.Status,
                        which -> {
                            print_type = 1;
                            if (print_type != sp.getInt("print_type", 0)) {
                                sp.edit().putInt("print_type",print_type).commit();
                            }
                            mTvPrintNum.setText("顾客联");
                        })
                .addSheetItem("财务联", ActionSheetDialog.SheetItemColor.Status,
                        which -> {
                            print_type =2;
                            if (print_type != sp.getInt("print_type", 0)) {
                                sp.edit().putInt("print_type",print_type).commit();
                            }
                            mTvPrintNum.setText("财务联");
                        })
                .addSheetItem("顾客联+财务联", ActionSheetDialog.SheetItemColor.Status,
                        which -> {
                            print_type = 3;
                            if (print_type != sp.getInt("print_type", 0)) {
                                sp.edit().putInt("print_type",print_type).commit();
                            }
                            mTvPrintNum.setText("顾客联+财务联");
                        }).show();
    }

    private void setOutLet() {
        final View mView = getLayoutInflater().inflate(R.layout.toast_view_alertdialog, null);
        final MyAlertDialog dialog1 = new MyAlertDialog(PrintSettingActivity.this)
                .builder();
        dialog1.setTitle("收款单位")
                .setEditText("")
                .setNegativeButton("取消", v -> {

                })
                .setPositiveButton("确定", v -> {
                    outlet = dialog1.getResult();
                    if (outlet.equals(sp.getString("outlet",""))) {
                        sp.edit().putString("outlet",outlet).commit();
                    }
                    Log.d("PrintSettingActivity", dialog1.getResult());
                    mTvPrintOutlet.setText(outlet);
                });
        dialog1.show();

    }

    private void setOffTime() {
        new ActionSheetDialog(PrintSettingActivity.this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("1s", ActionSheetDialog.SheetItemColor.Status,
                        which -> {
                            mTvPrintTime.setText("1s");
                            time = 1;
                            if (time != sp.getInt("time", 0)) {
                                sp.edit().putInt("time",time).commit();
                            }
                        })
                .addSheetItem("2s", ActionSheetDialog.SheetItemColor.Status,
                        which -> {
                            mTvPrintTime.setText("2s");
                            time = 2;
                            if (time != sp.getInt("time", 0)) {
                                sp.edit().putInt("time",time).commit();
                            }
                        })
                .addSheetItem("3s", ActionSheetDialog.SheetItemColor.Status,
                        which -> {
                            mTvPrintTime.setText("3s");
                            time = 3;
                            if (time != sp.getInt("time", 0)) {
                                sp.edit().putInt("time",time).commit();
                            }
                        })
                .addSheetItem("4s", ActionSheetDialog.SheetItemColor.Status,
                        which -> {
                            mTvPrintTime.setText("4s");
                            time = 4;
                            if (time != sp.getInt("time", 0)) {
                                sp.edit().putInt("time",time).commit();
                            }
                        })
                .addSheetItem("5s", ActionSheetDialog.SheetItemColor.Status,
                        which -> {
                            mTvPrintTime.setText("5s");
                            time = 5;
                            if (time != sp.getInt("time", 0)) {
                                sp.edit().putInt("time",time).commit();
                            }
                        }).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
}
}
