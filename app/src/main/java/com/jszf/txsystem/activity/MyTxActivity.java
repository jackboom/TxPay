package com.jszf.txsystem.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.UpdataInfo;
import com.jszf.txsystem.core.mvp.base.BaseActivity;
import com.jszf.txsystem.service_receiver.UpdateService;
import com.jszf.txsystem.ui.MyAlertDialog;
import com.jszf.txsystem.util.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyTxActivity extends BaseActivity {
    @BindView(R.id.iv_my_back)
    ImageView mIvMyBack;
    @BindView(R.id.tv_my_title)
    TextView mTvMyTitle;
    @BindView(R.id.tv_my_mail)
    TextView mTvMyMail;
    @BindView(R.id.rl_my_phone)
    RelativeLayout mRlMyPhone;
    @BindView(R.id.tv_my_website)
    TextView mTvMyWebsite;
    @BindView(R.id.tv_my_version)
    TextView mTvMyVersion;
    @BindView(R.id.rl_my_version)
    RelativeLayout mRlMyVersion;
    private final int UPDATA_NONEED = 0;
    private final int UPDATA_CLIENT = 1;
    private final int GET_UNDATAINFO_ERROR = 2;
    private final int SDCARD_NOMOUNTED = 3;
    private final int DOWN_ERROR = 4;
    @BindView(R.id.tv_my_phone)
    TextView mTvMyPhone;
    @BindView(R.id.tv_version_arrow)
    ImageView mTvVersionArrow;
    @BindView(R.id.tv_version_state)
    TextView mTvVersionState;
    private Button getVersion;
    private UpdataInfo info;
    private int localVersion;
    private String localVersionName;
    private String url = "http://www.ycb.com/m/YcbAndroid2.0.5.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tx);
        ButterKnife.bind(this);
        setStatusBar();
        init();
    }

    private void init() {
        //获取该程序的安装包路径
        String path=getApplicationContext().getPackageResourcePath();
        Log.d("MyTxActivity", "安装路径："+path);
        localVersionName = AppUtils.getVerName(MyTxActivity.this);
        mTvMyVersion.setText(getString(R.string.my_version_name) + localVersionName);
        if (judgeVersionIsNew()) {
            mTvVersionState.setText(getString(R.string.my_version_state));
//            mRlMyVersion.setFocusable(false);
        }else {
            mTvVersionState.setText("有新版本");
        }
    }

    private String getVersionName() throws Exception {
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
                0);
        return packInfo.versionName;
    }

    @OnClick({R.id.iv_my_back, R.id.rl_my_phone, R.id.rl_my_website, R.id.rl_my_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_my_back:
                finish();
                break;
            case R.id.rl_my_phone:
                try {
                    String connectPhone = mTvMyPhone.getText().toString().trim();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + connectPhone);
                    intent.setData(data);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d("MyTxActivity", "抛出异常:" + e.getMessage().toString());
                }
                break;
            case R.id.rl_my_website:
                try {
                    String url = mTvMyWebsite.getText().toString().trim();
                    Uri uri = Uri.parse("http://" + url);
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    it.addCategory(Intent.CATEGORY_BROWSABLE);
                    startActivity(it);
                    Log.d("LoginActivity", "跳转");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rl_my_version:
                updateNotice();
                break;
        }
    }

    private boolean judgeVersionIsNew() {
        if (localVersionName.equals("1.1.0")) {
            mTvVersionArrow.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    private void updateNotice() {
        View mView = getLayoutInflater().inflate(R.layout.item_update_dialog, null);
        final ProgressBar mProgressBar = (ProgressBar) mView.findViewById(R.id.progressBar_update);
        final TextView mTextView = (TextView) mView.findViewById(R.id.tv_update);
        final MyAlertDialog dialog1 = new MyAlertDialog(MyTxActivity.this)
                .builder();
        dialog1.dialog_marBottom.setVisibility(View.VISIBLE);
        dialog1.setCancelable(false);
        dialog1.setTitle("检查更新")
                .setView(mView)
                .setPositiveButton("立即下载", v -> {
                    Intent intent = new Intent(MyTxActivity.this, UpdateService.class);
                    intent.putExtra("downUrl", url);
                    startService(intent);
                })
                .setNegativeButton("稍后再说", v -> {

                }).show();
        handler.postDelayed(() -> {
            localVersion = AppUtils.getVerCode(MyTxActivity.this);
            String localVersionName = AppUtils.getVerName(MyTxActivity.this);
            mProgressBar.setVisibility(View.GONE);
            mTextView.setText(getString(R.string.my_version_new) + ":" + localVersionName);
        }, 2000);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_NONEED:
                    Toast.makeText(getApplicationContext(), "不需要更新",
                            Toast.LENGTH_SHORT).show();
                case UPDATA_CLIENT:
                    //对话框通知用户升级程序
                    break;
                case GET_UNDATAINFO_ERROR:
                    //服务器超时
                    Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", Toast.LENGTH_SHORT).show();
                    break;
                case DOWN_ERROR:
                    //下载apk失败
                    Toast.makeText(getApplicationContext(), "下载新版本失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
