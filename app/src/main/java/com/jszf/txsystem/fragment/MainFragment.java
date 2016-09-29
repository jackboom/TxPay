package com.jszf.txsystem.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jszf.txsystem.MyApplication;
import com.jszf.txsystem.R;
import com.jszf.txsystem.activity.AnalyseActivity;
import com.jszf.txsystem.activity.PayActivity;
import com.jszf.txsystem.bean.MainBean;
import com.jszf.txsystem.bean.Merchant;
import com.jszf.txsystem.core.mvp.MvpFragment;
import com.jszf.txsystem.core.mvp.main.IMainView;
import com.jszf.txsystem.core.mvp.main.MainPresenterImpl;
import com.jszf.txsystem.service_receiver.BluetoothService;
import com.jszf.txsystem.util.Constant;
import com.jszf.txsystem.util.GuidUtils;
import com.jszf.txsystem.util.MD5Utils;
import com.jszf.txsystem.util.ParaUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/4/27.
 */
public class MainFragment extends MvpFragment<IMainView, MainPresenterImpl> implements IMainView {
    @BindView(R.id.ibtn_home_analyse)
    ImageView mIbtnHomeAnalyse;
    @BindView(R.id.tv_home_amount)
    TextView mTvHomeAmount;
    @BindView(R.id.tv_home_weixin)
    TextView mTvHomeWeixin;
    @BindView(R.id.tv_home_alipay)
    TextView mTvHomeAlipay;
    @BindView(R.id.tv_home_yzf)
    TextView mTvHomeYzf;
    @BindView(R.id.tv_home_badiu)
    TextView mTvHomeBadiu;
    @BindView(R.id.iv_home_weixn)
    ImageView mIvHomeWeixn;
    @BindView(R.id.iv_home_alipay)
    ImageView mIvHomeAlipay;
    @BindView(R.id.iv_home_yzf)
    ImageView mIvHomeYzf;
    @BindView(R.id.iv_home_baidu)
    ImageView mIvHomeBaidu;
    @BindView(R.id.ll_home_top)
    LinearLayout mLlHomeTop;
    private View mView;
    private Merchant mMerchant;
    private HashMap<String, String> resultMap;
    private String amountAll;   //总交易额
    private String amountWixin; //微信交易额
    private String amountAlipay; //支付宝交易额
    private String amountBdqb; //百度钱包交易额
    private String amountYzf; //翼支付交易额
    private SimpleDateFormat sf;
    private String startTime;
    private String currentTime;
    private AlertDialog.Builder mBuilder;
    private int page = 1;//分页
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.frag_main, container, false);
        }

        Log.d("TAG", "oncreate");
        mBuilder = new AlertDialog.Builder(getActivity());
        ButterKnife.bind(this, mView);
        setStatus();
        init();
        if (judgeNetWord(getActivity())) {
            new Thread(() -> {
                requestInitData();
            }).start();
        }

        return mView;
    }


    private HashMap<String, String> setHashMap() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("Guid", GuidUtils.getVarUUID());
        map.put("UserName", MyApplication.userLoginName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String startTime = sdf.format(date);
        map.put("StartTime", startTime);
        return map;
    }

    @Override
    protected MainPresenterImpl createPresenter() {
        return new MainPresenterImpl(this);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * 初始化
     */
    private void init() {
        //将字体文件保存在assets/fonts/目录下，创建Typeface对象
        Typeface typeFace = Typeface.DEFAULT_BOLD;
        //使用字体
        mTvHomeAmount.setTypeface(typeFace);
        Bundle mBundle = getArguments();
//        mMerchant = (Merchant) mBundle.getSerializable("merchantInfo");
        mMerchant = MyApplication.mMerchant;
    }

    /**
     * 请求主页
     */
    private void requestInitData() {
        final HashMap<String, String> map = getRequestParams();
        Log.d("MainFragment", "mvpPresenter == null:" + (mvpPresenter == null));
        mvpPresenter.requestFirstInfo(map);
//        requestByOkhttp(map);
    }

    /**
     * 请求字段
     * @return HashMap
     */
    @NonNull
    private HashMap<String, String> getRequestParams() {
        Date mDate = new Date();
        mDate.getTime();
        sf = new SimpleDateFormat("yyyy-MM-dd");
//        startTime = AcquireTimeNode.getTimePoint(6).split(",")[0];
        currentTime = sf.format(mDate);
//        currentTime = AcquireTimeNode.getTimePoint(5).split(",")[0];
        final HashMap<String, String> map = new HashMap<>();
        map.put("service", "offBatchSearchOrder");
        map.put("version", "V1.0");
        map.put("merchantNo", MyApplication.merchantNo);
        map.put("page", "" + page);
        map.put("payType", "1,2,3,9");
        map.put("payMethod", "1");
        map.put("status", "1");
        map.put("startDate", currentTime);
        map.put("endDate", currentTime);
        map.put("sign", MD5Utils.MD5(ParaUtils.createLinkString(map) + MyApplication.MD5key));
        Log.d("MainFragment", "-------------" + ParaUtils.createLinkString(map));
        return map;
    }

    /**
     * 设置数据
     */
    private void setView() {
        mTvHomeAmount.setText(amountAll);
        Log.d("MainFragment", "----" + amountAll);
        mTvHomeWeixin.setText(amountWixin);
        mTvHomeAlipay.setText(amountAlipay);
        mTvHomeBadiu.setText(amountBdqb);
        mTvHomeYzf.setText(amountYzf);


    }

    @OnClick({R.id.ibtn_home_analyse, R.id.iv_home_weixn, R.id.iv_home_alipay,
            R.id.iv_home_yzf, R.id.iv_home_baidu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_home_analyse:
//                loadDataStyle1(mView);
                Intent mIntent5 = new Intent(MyApplication.mContext, AnalyseActivity.class);
                mIntent5.putExtra("merchantInfo", mMerchant);
                startActivity(mIntent5);
                getActivity().overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);
                break;
            case R.id.iv_home_weixn:
                Log.d("MainFragment", "----------MainFragment----------微信");
                Intent mIntent1 = new Intent(MyApplication.mContext, PayActivity.class);
                mIntent1.putExtra("payType", Constant.PAY_TYPE_WEIXIN);
                mIntent1.putExtra("merchantInfo", mMerchant);
                startActivity(mIntent1);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);
                break;
            case R.id.iv_home_alipay:
                Log.d("MainFragment", "----------MainFragment----------支付宝");

                Intent mIntent2 = new Intent(MyApplication.mContext, PayActivity.class);
                mIntent2.putExtra("payType", Constant.PAY_TYPE_ALIPAY);
                mIntent2.putExtra("merchantInfo", mMerchant);
                startActivity(mIntent2);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);

                break;
            case R.id.iv_home_yzf:
                Log.d("MainFragment", "----------MainFragment----------翼支付");

                Intent mIntent3 = new Intent(MyApplication.mContext, PayActivity.class);
                mIntent3.putExtra("payType", Constant.PAY_TYPE_YZF);
                mIntent3.putExtra("merchantInfo", mMerchant);
                startActivity(mIntent3);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);

                break;
            case R.id.iv_home_baidu:
                Log.d("MainFragment", "----------MainFragment----------百度钱包");

                Intent mIntent4 = new Intent(MyApplication.mContext, PayActivity.class);
                mIntent4.putExtra("payType", Constant.PAY_TYPE_BAIDU);
                mIntent4.putExtra("merchantInfo", mMerchant);
                startActivity(mIntent4);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);

                break;
        }
    }

    //更新标题栏右边状态和读写状态的Handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Log.d("BluetoothActivity", "已连接");
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.d("BluetoothActivity", "连接中");

                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Log.d("BluetoothActivity", "无连接");
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
                    break;
                case MESSAGE_DEVICE_NAME:
                    //保存连接设备的名字
                    String mConnectedDeviceName = msg.getData().getString("device_name");
                    Toast.makeText(getActivity(), "连接到" + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getActivity(), msg.getData().getString("toast"),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void requestFirstInfo(MainBean mainBean) {
        try {
            String dealCode = mainBean.getDealCode();
            if (!dealCode.equals(Constant.DEAL_CODE_SUCCESS)) {
                Toast.makeText(getActivity(), "系统,异常代码：" + dealCode,
                        Toast.LENGTH_SHORT).show();
                return;
            }
            MainBean.Data data = mainBean.getData();
            List<MainBean.Data.Rows> arrayString = data.getRows();
            if (arrayString.size() == 0 || arrayString == null) {
                amountAll = "0.00";
                amountWixin = "0.00";
                amountAlipay = "0.00";
                amountYzf = "0.00";
                amountBdqb = "0.00";
                setView();
                return;
            }
            for (int i = 0; i < arrayString.size(); i++) {
                String amount = arrayString.get(i).getOrderAmount();
                if (!TextUtils.isEmpty(amount)) {
                    String str = (Double.parseDouble(amount) / 100) + "";
                    amountAll = str;
                } else {
                    amountAll = "0.00";
                }
            }
            Log.d("MainFragment", "amountAll:" + amountAll);
            if (amountAll.equals("0.00")) {
                amountWixin = "0.00";
                amountAlipay = "0.00";
                amountYzf = "0.00";
                amountBdqb = "0.00";
            } else {
                MainBean.Data.SummaryInfo summaryInfo = data.getSummaryInfo();
                if (!TextUtils.isEmpty(summaryInfo.getPayType_1())) {
                    amountAlipay = (Double.parseDouble(summaryInfo.getPayType_1()) / 100) + "";
                } else {
                    amountAlipay = "0.00";
                }
                if (!TextUtils.isEmpty(summaryInfo.getPayType_2())) {
                    amountWixin = (Double.parseDouble(summaryInfo.getPayType_2()) / 100) + "";
                } else {
                    amountWixin = "0.00";
                }
                if (!TextUtils.isEmpty(summaryInfo.getPayType_3())) {
                    amountBdqb = (Double.parseDouble(summaryInfo.getPayType_3()) / 100) + "";
                } else {
                    amountBdqb = "0.00";
                }
                if (!TextUtils.isEmpty(summaryInfo.getPayType_9())) {
                    amountYzf = (Double.parseDouble(summaryInfo.getPayType_9()) / 100) + "";
                } else {
                    amountYzf = "0.00";
                }
            }
            setView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
