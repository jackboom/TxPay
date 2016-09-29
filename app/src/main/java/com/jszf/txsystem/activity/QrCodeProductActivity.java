package com.jszf.txsystem.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.micro.client.mqttv3.MqttClient;
import com.ibm.micro.client.mqttv3.MqttException;
import com.jszf.txsystem.MyApplication;
import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.Merchant;
import com.jszf.txsystem.bean.OrderInfo;
import com.jszf.txsystem.core.mvp.base.BaseActivity;
import com.jszf.txsystem.service_receiver.BluetoothService;
import com.jszf.txsystem.service_receiver.MQConnectionService;
import com.jszf.txsystem.ui.MyAlertDialog;
import com.jszf.txsystem.util.Constant;
import com.jszf.txsystem.util.MD5Utils;
import com.jszf.txsystem.util.ParaUtils;
import com.jszf.txsystem.util.PrintUtils;
import com.jszf.txsystem.util.QRCodeUtils;
import com.jszf.txsystem.view.CustomTextView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class QrCodeProductActivity extends BaseActivity implements Serializable {
    public static MqttClient client;        //MQTT服务客户端对象
    @BindView(R.id.iv_pay_back)
    ImageView mIvPayBack;
    @BindView(R.id.iv_icon_qrcode)
    ImageView mIvIconQrcode;
    @BindView(R.id.iv_qrcode_cancle)
    ImageView mIvQrcodeCancle;
    @BindView(R.id.tv_qrcode_amount)
    CustomTextView mTvQrcodeAmount;
    @BindView(R.id.ll_qrcode_bg)
    LinearLayout mLlQrcodeBg;
    @BindView(R.id.qrshow_bg)
    LinearLayout mQrshowBg;
    private String orderAmount;
    private String orderNo;
    private String orderTime;
    private String orderTimestamp;
    private String productName;
    private String productDesc;
    private LinearLayout ll_product_title;
    private TextView tv_product_title;
    private TextView tv_product_amount;
    private TextView tv_product_text;
    private Intent mIntent;
    private SimpleDateFormat sf = null;
    public static Handler mHandler;
    private Bitmap mLogoBmp;
    private Handler handler = new Handler();
    private String myTopic = "TX_APP_PAY";      //mqtt消息系统的订阅主题
    MQConnectionService mService = null;
    private int pay_state;      //支付状态
    private int type;   //生成二维码的方式
    private String payChannelCode;  //支付通道
    private String amount;  //订单金额
    private int count;  //提交金额，分为单位
    private AlertDialog.Builder mBuilder;
    private String mOrderNo;
    private String mTxOrderNo;
    private AlertDialog mAlertDialog;
    public static String mUuid = null;
    private Bitmap smallbitmap ;
    private String payType;
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQConnectionService.MyBind binder = (MQConnectionService.MyBind) service;
            mService = binder.getMyService();
            mService.setContext(QrCodeProductActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetworkInfo;
    private String mOrderAmount;
    private Merchant mMerchant;
    private String getMerchantNo;
    private String getOrderTime;
    private double time;
    private SharedPreferences mSp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_product2);
        ButterKnife.bind(this);
        mBuilder = new AlertDialog.Builder(this);
        initView();
        setView();
        getData();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        try {
//                          orderTime:20160523102708 订单：TX20160523102708
                            Log.d("TAG", "进入到结果");
//                            TX20160527141724 多次收到
                            String result = (String) msg.obj;
                            mSp = getSharedPreferences("Data", MODE_PRIVATE);
                            if (TextUtils.isEmpty(result)) {
                                Toast.makeText(getApplicationContext(), "请求异常!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String str = mSp.getString("result", "");
                            Log.d("TAG", "str:" + str);
                            if (str.equals(result)) {
                                Log.d("TAG", "重复去重");
                            } else {
                                mSp.edit().putString("result", result);
                                Log.d("TAG", "SP:" + mSp.getString("result", ""));
                                JSONObject mJSONObject = new JSONObject(result);
                                getMerchantNo = mJSONObject.getString("merchantNo");
                                double orderTimestamp = Double.parseDouble(orderTime);
                                Log.d("TAG", "orderTimestamp:" + orderTimestamp);
                                getOrderTime = mJSONObject.getString("dealTime");
                                double dealTime = Double.parseDouble(getOrderTime);
                                Log.d("TAG", "dealTime:" + dealTime);
                                if (orderTimestamp < dealTime && getMerchantNo.equals(MyApplication.merchantNo)) {
                                    String dealCode = mJSONObject.getString("dealCode");
                                    if (dealCode.equals(Constant.DEAL_CODE_SUCCESS)) {
                                        pay_state = 1;
                                        mOrderNo = mJSONObject.getString("orderNo");
                                        mTxOrderNo = mJSONObject.getString("txOrderNo");
                                        mOrderAmount = mJSONObject.getString("orderAmount");
//                                requestBack();
                                    } else {
                                        pay_state = 0;
                                        mOrderNo = mJSONObject.getString("orderNo");

                                    }
                                    showPayDialog(pay_state);
                                    Log.d("TAG", "返回");
                                }
                            }
                        } catch (Exception e) {
                            Log.d("TAG", "exception:" + e.toString());
                        }

                        break;
                    case 2:
                        try {
                            MyApplication.client.subscribe(myTopic, 2);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(getApplicationContext(),"成功连接",Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "连接成功!");
                        break;
                    case 3:
//                        Toast.makeText(getApplicationContext(),"重连",Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "连接失败，重连");
                        break;

                    case 4:

                        break;
                }
            }
        };
//        Intent mIntent = new Intent(this, MQConnectionService.class);
        Log.d("TAG", "开启");
        mService = new MQConnectionService(QrCodeProductActivity.this, mHandler);
//                mIntent = new Intent(this, MQConnectionService.class);
        mService.start();
        Log.d("TAG", "数据请求");
        requestProduct();

    }

    private void init() {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        mOrderNo = mSimpleDateFormat.format(new Date());
    }

    private void setView() {
        switch (type) {
            case 1:
                payType  = "微信支付";
                payChannelCode = Constant.PAY_CHANNELCODE_WEXIN;
                mQrshowBg.setBackgroundColor(getResources().getColor(R.color.qrcode_text_bg_weixin));
                mTvQrcodeAmount.setTextColor(getResources().getColor(R.color.qrcode_text_amount_weichat));
                mLlQrcodeBg.setBackgroundResource(R.drawable.qrcode_wechat_bg);
                BitmapDrawable mDrawable1 = (BitmapDrawable) getResources().getDrawable(R.drawable.product_icon_weixin);
                smallbitmap = mDrawable1.getBitmap();
                break;

            case 2:
                payType  = "支付宝支付";
                mQrshowBg.setBackgroundColor(getResources().getColor(R.color.qrcode_text_bg_alipay));
                mTvQrcodeAmount.setTextColor(getResources().getColor(R.color.qrcode_text_amount_alipay));
                payChannelCode = Constant.PAY_CHANNELCODE_ALIPAY;
                mLlQrcodeBg.setBackgroundResource(R.drawable.qrcode_alipay_bg);
                BitmapDrawable mDrawable2 = (BitmapDrawable) getResources().getDrawable(R.drawable.product_icon_alipay);
                smallbitmap = mDrawable2.getBitmap();
                break;
            case 3:
                payType  = "翼支付";
                mQrshowBg.setBackgroundColor(getResources().getColor(R.color.qrcode_text_bg_yzf));
                mTvQrcodeAmount.setTextColor(getResources().getColor(R.color.qrcode_text_amount_yzf));
                payChannelCode = Constant.PAY_CHANNELCODE_YZF;
                mLlQrcodeBg.setBackgroundResource(R.drawable.qrcode_yzf_bg);
                BitmapDrawable mDrawable3 = (BitmapDrawable) getResources().getDrawable(R.drawable.product_icon_yzf);
                smallbitmap = mDrawable3.getBitmap();

                break;
            case 4:
                payType  = "百度钱包支付";
                mQrshowBg.setBackgroundColor(getResources().getColor(R.color.qrcode_text_bg_bg));
                mTvQrcodeAmount.setTextColor(getResources().getColor(R.color.qrcode_text_amount_bd));
                payChannelCode = Constant.PAY_CHANNELCODE_BDQB;
                mLlQrcodeBg.setBackgroundResource(R.drawable.qrcode_bd_bg);
                BitmapDrawable mDrawable4 = (BitmapDrawable) getResources().getDrawable(R.drawable.product_icon_bdqb);
                smallbitmap = mDrawable4.getBitmap();
                break;
        }
        Log.d("QrCodeProductActivity", "----------QrCodeProductActivity---------类型：payType:" + payType);
        mTvQrcodeAmount.setText("¥" + amount);


    }

    private void initView() {
        Intent mIntent = getIntent();
        type = mIntent.getIntExtra("type", 0);
        mMerchant = (Merchant) mIntent.getSerializableExtra("merchantInfo");
        amount = getIntent().getStringExtra("amount");
    }

    private void requestProduct() {
        showLoadingDialog();
        if (!TextUtils.isEmpty(amount)) {
            double str = Double.parseDouble(amount);
            count = (int) (str * 100);
        }
        mConnectivityManager = (ConnectivityManager) QrCodeProductActivity.this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (!(mNetworkInfo != null && mNetworkInfo.isAvailable() && mNetworkInfo.isConnected())) {
            dismissLoading();
            setDialog("网络异常,请检查网络是否连接!");
            return;
        }
        final HashMap<String, String> map = new HashMap<>();
        map.put("service", "getCodeUrl");
        map.put("merchantNo", MyApplication.merchantNo);
        map.put("version", "V1.0");
        map.put("payChannelCode", payChannelCode);
        map.put("orderNo", orderNo);
        Log.d("TAG", "订单：" + orderNo);
        map.put("orderAmount", count + "");
        Log.d("TAG", "金额：" + count + "");
        map.put("curCode", "CNY");
        map.put("orderTime", orderTime);
        if (!TextUtils.isEmpty(MyApplication.userLoginName)) {
            map.put("ext2", MyApplication.userLoginName);
        }
        map.put("sign", MD5Utils.MD5(ParaUtils.createLinkString(map) + MyApplication.MD5key));
        Log.d("QrCodeProductActivity", "请求的数据：" + ParaUtils.createLinkString(map));
        OkHttpUtils.post()
                .url(Constant.PATH)
                .params(map)
                .build()
                .connTimeOut(5000)
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response mResponse) throws Exception {
                        return mResponse.body().string();
                    }

                    @Override
                    public void onError(Call mCall, Exception e) {
                        dismissLoading();
                        Log.d("QrCodeProductActivity", "请求异常：" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call mCall, Object o) {
                        String result = o.toString();
                        Log.d("QrCodeProductActivity", "请求的结果是:" + result);
                        dismissLoading();
                        try {
                            JSONObject mJSONObject = new JSONObject(result);
                            if (!mJSONObject.has("codeUrl")) {
                                Toast.makeText(getApplicationContext(), "请求异常", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String data = mJSONObject.getString("codeUrl");
                            String dealCode = mJSONObject.getString("dealCode");
                            Date date = new Date();//当前日期
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//格式化对象
                            String beforeTimes = sdf.format(date);
                            time = Double.parseDouble(beforeTimes);
                            Log.d("TAG", "RESULT:" + result);
                            if (!dealCode.equals(Constant.DEAL_CODE_SUCCESS)) {
                                Toast.makeText(getApplicationContext(), "结果码：" + dealCode + ",生成二维码失败", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            dismissLoading();
                            Bitmap bmp = QRCodeUtils.createQRImage(data, 400, 400, smallbitmap, "/sdcard/DCIM/m.png");
                            mIvIconQrcode.setImageBitmap(bmp);
//                            showPayDialog(1);
                            Log.d("TAG", "times:" + time);

                        } catch (JSONException e) {
                            dismissLoading();
                            Toast.makeText(getApplicationContext(), "请求异常", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "异常发生:" + e.toString());
                            return;
                        }
                    }
                });
    }

    private void getData() {
        OrderInfo mOrderInfo = OrderInfo.getInstance();
        Date mDate = new Date();
        sf = new SimpleDateFormat("yyyyMMddHHmmss");
        orderTime = sf.format(mDate);
        orderNo = orderTime;
        orderAmount = mOrderInfo.getOrderAmount();
        Log.d("TAG", "orderTime:" + orderTime);
    }

    @Override
    protected void onDestroy() {
        Log.d("TAG", "kill service");
        super.onDestroy();
        mService.stop();

    }

    private void setDialog(String message) {
        mBuilder.setMessage(message);
        mBuilder.setPositiveButton("确定", (dialog, which) -> {
            mBuilder.create().dismiss();
        });
        mBuilder.create().show();
    }

    @OnClick({R.id.iv_pay_back, R.id.iv_qrcode_cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_pay_back:
                finish();
                break;
            case R.id.iv_qrcode_cancle:
                finish();
                startActivity(new Intent(QrCodeProductActivity.this, HomeActivity.class));
                break;
        }
    }
    public void showPayDialog(int mPay_state){
        View view = LayoutInflater.from(QrCodeProductActivity.this).inflate(R.layout.layout_qrcode_dialog, null);
        final ImageView iv_dialog_state = (ImageView) view.findViewById(R.id.iv_dialog_state);
        final TextView tv_dialog_orderno = (TextView) view.findViewById(R.id.tv_dialog_orderno);
        TextView tv_dialog_amount = (TextView) view.findViewById(R.id.tv_dialog_amount);
        TextView tv_dialog_paytype = (TextView) view.findViewById(R.id.tv_dialog_paytype);
        Log.d("QrCodeProductActivity", "null:" + tv_dialog_amount.getText());

        switch (mPay_state) {
            case 0:
                iv_dialog_state.setImageResource(R.drawable.iv_icon_failed);
                break;
            case 1:
                iv_dialog_state.setImageResource(R.drawable.iv_icon_success);
                tv_dialog_orderno.setText(mOrderNo);
                Log.d("QrCodeProductActivity", "支付时间 " + mOrderNo + ",支付金额:" + amount);
                tv_dialog_amount.setText("¥" + amount);
                tv_dialog_paytype.setText("" +payType);
                break;
        }
        final MyAlertDialog dialog1 = new MyAlertDialog(QrCodeProductActivity.this)
                .builder();
        dialog1.setCancelable(false);
        dialog1.setView(view)
                .setNegativeButton("确定", v -> {
                    Intent mIntent1 = new Intent(QrCodeProductActivity.this, HomeActivity.class);
                    startActivity(mIntent1);
                    finish();
                });
       if (judgeBluetooth(dialog1)) {
           dialog1.setPositiveButton("确定并打印", v -> {
               HashMap<String, String> map = new HashMap<String, String>();
               map.put("orderNo", mOrderNo);
               map.put("orderAmount", amount);
               map.put("payType", payType);
               map.put("outlet", PrintUtils.getSharePrintData(QrCodeProductActivity.this).getString("outlet", ""));
               PrintUtils.printContentText(QrCodeProductActivity.this, map);
//                   try {
//                       Bitmap bmp = QRCodeUtils.createQRImage(orderNo, 200, 200, null, "/sdcard/DCIM/m.png");
//                       PrintUtils.sendMessage(QrCodeProductActivity.this, bmp);
//                   }catch (Exception e) {
//                       Log.d("QrCodeProductActivity", "-----------"+e.getMessage().toString());
//                   }
               Intent mIntent1 = new Intent(QrCodeProductActivity.this, HomeActivity.class);
               startActivity(mIntent1);
               finish();
           });
       }
        dialog1.show();
    }

    private boolean judgeBluetooth(MyAlertDialog mDialog1) {
        if (MyApplication.getInstance().mService == null) {
            mDialog1.btn_pos.setVisibility(View.GONE);
            return false;
        }
        if (MyApplication.getInstance().mService.getState() != BluetoothService.STATE_CONNECTED) {
            mDialog1.btn_pos.setVisibility(View.GONE);
            return false;
        }
        return true;
    }
}
