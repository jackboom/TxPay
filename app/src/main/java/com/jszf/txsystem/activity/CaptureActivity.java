package com.jszf.txsystem.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.jszf.txsystem.MyApplication;
import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.Merchant;
import com.jszf.txsystem.core.mvp.base.BaseActivity;
import com.jszf.txsystem.scan_qrcode.camera.CameraManager;
import com.jszf.txsystem.scan_qrcode.decoding.CaptureActivityHandler;
import com.jszf.txsystem.scan_qrcode.decoding.InactivityTimer;
import com.jszf.txsystem.service_receiver.BluetoothService;
import com.jszf.txsystem.ui.MyAlertDialog;
import com.jszf.txsystem.util.Constant;
import com.jszf.txsystem.util.GuidUtils;
import com.jszf.txsystem.util.MD5Utils;
import com.jszf.txsystem.util.ParaUtils;
import com.jszf.txsystem.util.PrintUtils;
import com.jszf.txsystem.util.RSAUtils;
import com.jszf.txsystem.view.ViewfinderView;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.Response;

public class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback {
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private SimpleDateFormat sf;
    private String orderTime;
    private String orderNo;
    private String dealCode;
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetworkInfo;
    private HashMap<String, String> resultMap;
    private int type;   //支付类型
    private LinearLayout rl_scan_title;
    private LinearLayout rl_scan_bottom;
    private TextView tv_capture_amount;
    private String payChannelCode;  //支付通道
    private String amount;  //订单金额
    private int count;  //提交金额，分为单位
    private Merchant mMerchant;
    private android.app.AlertDialog.Builder mBuilder;
    private String payType;

    public static final int SYSTEM_UI_FLAG_TRANSPARENT_STATUS_BAR =
            0x00000010;
    //半透明任务栏
    public static final int SYSTEM_UI_FLAG_TRANSLUCENT_STATUS_BAR =
            0x00000020;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 4:

                    break;
            }
        }
    };
    private String mIp;
    private AlertDialog mAlertDialog;
    private ImageView mButtonBack;
    private String authCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_capture);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//显示状态栏
        hideStatusBar();
        showStatusBar();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.include1);
        AutoRelativeLayout.LayoutParams lp = (AutoRelativeLayout.LayoutParams) linearLayout.getLayoutParams();
        int height = getStatusHeight(this);
        Log.d("CaptureActivity", "height:" + height);
        lp.setMargins(0, height, 0, 0);
        linearLayout.setLayoutParams(lp);
//        getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_TRANSPARENT_STATUS_BAR);
        mBuilder = new android.app.AlertDialog.Builder(this);
        initData();
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setText(amount);
        CameraManager.init(getApplication());
        mButtonBack = (ImageView) findViewById(R.id.iv_capture_back);
        mButtonBack.setOnClickListener(v -> CaptureActivity.this.finish());
//        setView();
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
//        showPayDialog2(1);
    }


    private void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    private void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    private void setView() {
//        tv_capture_amount.setText(amount);
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        orderNo = mSimpleDateFormat.format(new Date());
    }

    private void initData() {
        Intent mIntent = getIntent();
//        type = mIntent.getIntExtra("type", 0);
//        Log.d("TAG", "type:" + type);
        amount = mIntent.getStringExtra("amount");
        Log.d("CaptureActivity", "金额：" + amount);
        mMerchant = (Merchant) mIntent.getSerializableExtra("merchantInfo");
        rl_scan_title = (LinearLayout) findViewById(R.id.include1);
//        tv_capture_amount = (TextView) findViewById(R.id.tv_capture_amount);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }


    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        try {
            inactivityTimer.onActivity();
            playBeepSoundAndVibrate();
            String resultString = result.getText();
            Log.d("CaptureActivity", "扫描结果：" + resultString);
            mConnectivityManager = (ConnectivityManager) CaptureActivity.this
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
//            if (!(mNetworkInfo != null && mNetworkInfo.isAvailable() && mNetworkInfo.isConnected())) {
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        setDialog("网络异常,请检查网络是否连接!");
//                    }
//                });
//                return;
//            }
//            扫描结果：510456640251710189
            if (resultString.equals("")) {
                Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
            } else {
                authCode = result.getText().toString();
                captureQrcode();
//                final HashMap<String, String> map = setMap(authCode);
//                ApiRequestStores apiStores = HttpHelper.getInstance().getRetrofit().create(ApiRequestStores.class);
//                Observable<CaptureBean> observable = apiStores.requestCapture2(map);
//                observable.subscribe(new Subscriber<CaptureBean>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d("CaptureActivity", "-----------" + e.getMessage().toString());
//                    }
//
//                    @Override
//                    public void onNext(CaptureBean billDetailBean) {
//                        String dealCode = billDetailBean.getDealCode();
//                        if (TextUtils.isEmpty(dealCode)) {
//                            return;
//                        }
//                        if (dealCode.equals(Constant.DEAL_CODE_SUCCESS)) {
//                            showPayDialog(1);
//                        } else {
//                            setDialog(getErrorMsg(Integer.parseInt(dealCode)) + "\n支付失败,请联系同兴支付客服!");
//                        }
//                    }
//                });
            }
        } catch (Exception e) {
            final String str = e.getMessage().toString();
            Log.d("CaptureActivity", "异常:" + str);
        }
    }

    private void captureQrcode() {
        OkHttpUtils.post()
                .url(Constant.CAPTURE_URL)
                .params(getHashMap())
                .build()
                .connTimeOut(5000)
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response) throws Exception {
                        return response.body().string();
                    }

                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(Call call, Object o) {
                        String result = o.toString();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            boolean isSuccess = jsonObject.getBoolean("Success");
                            showPayDialog(1);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.d("CaptureActivity", "result:" + result);
                    }
                });
//        ApiRequestStores apiStores = HttpHelper.getInstance().getRetrofit2().create(ApiRequestStores.class);
//        Observable<CaptureBean> observable = apiStores.requestCapture(getHashMap());
//        observable.subscribe(new Subscriber<CaptureBean>() {
//            @Override
//            public void onCompleted() {
//                dismissLoading();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d("ResetPassWordActivity", "e:" + e);
//            }
//
//            @Override
//            public void onNext(CaptureBean captureBean) {
//                Log.d("CaptureActivity", "captureBean.isSuccess():" + captureBean.isSuccess());
//                boolean isSuccess = captureBean.isSuccess();
//                if (isSuccess) {
//                    orderNo = captureBean.get_OrderModel().getOrderNo();
//                    showPayDialog(1);
//                }
//            }
//        });

    }

    private HashMap<String, String> getHashMap() {
        HashMap<String,String> map = new HashMap<>();
        String type;

        if (authCode.startsWith("1")) {
            type = "1";
            payType = "微信支付";
        } else if (authCode.startsWith("2")) {
            type = "2";
            payType = "支付宝支付";
        } else if (authCode.startsWith("5")) {
            type = "3";
            payType = "翼支付";
        } else {
            type = "4";
            payType = "百度钱包支付";
        }
        map.put("PayType",type);
        map.put("Amt",amount);
        String guid = GuidUtils.getVarUUID();
        map.put("Barcode",authCode);
        map.put("Guid", guid);
        map.put("MerchantNo", MyApplication.merchantNo);
        map.put("UserName",MyApplication.userLoginName);
        try {
            map.put("MerchantKey",RSAUtils.encrypt(MyApplication.MD5key, Constant.AppPublicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String str = RSAUtils.sign(guid, Constant.AppPrivateKey);
            map.put("Sign",str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private void setDialog(String message) {
        mBuilder.setMessage(message);
        mBuilder.setPositiveButton("确定", (dialog, which) -> {
            mBuilder.create().dismiss();
        });
        mBuilder.create().show();
    }

    private HashMap<String, String> setMap(String mAuthCode) {
        HashMap<String, String> map = new HashMap<>();
        mConnectivityManager = (ConnectivityManager) CaptureActivity.this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isAvailable() && mNetworkInfo.isConnected()) {
            mIp = getIp(mNetworkInfo);
            Log.d("TAG", "IP:" + mIp);
        } else {
            setDialog("网络异常,请检查网络是否连接!");
        }
        if (!TextUtils.isEmpty(amount)) {
            double str = Double.parseDouble(amount);
            count = (int) (str * 100);
        }
        Date mDate = new Date();
        sf = new SimpleDateFormat("yyyyMMDDHHmmss");
        orderTime = sf.format(mDate);
        orderNo = orderTime;
        map.put("authCode", mAuthCode);
        map.put("service", "offPayGate");
        map.put("merchantNo", MyApplication.merchantNo);
        map.put("version", "V1.0");
        if (!TextUtils.isEmpty(MyApplication.userLoginName)) {
            map.put("ext2", MyApplication.userLoginName);

        }
        if (mAuthCode.startsWith("1")) {
            payType = "微信支付";
            map.put("payChannelCode", Constant.PAY_CHANNELCODE_WEXIN);
        } else if (mAuthCode.startsWith("2")) {
            payType = "支付宝支付";
            map.put("payChannelCode", Constant.PAY_CHANNELCODE_ALIPAY);
        } else if (mAuthCode.startsWith("5")) {
            payType = "翼支付";
            map.put("payChannelCode", Constant.PAY_CHANNELCODE_YZF);
        } else {
            payType = "百度钱包支付";
            map.put("payChannelCode", Constant.PAY_CHANNELCODE_BDQB);
        }

        map.put("orderNo", orderNo);
        map.put("orderAmount", count + "");
        map.put("payerIp", mIp);
        map.put("curCode", "CNY");
        map.put("orderTime", orderTime);
        map.put("sign", MD5Utils.MD5(ParaUtils.createLinkString(map) + MyApplication.MD5key));
        return map;
    }

    private String getIp(NetworkInfo mNetworkInfo) {
        if (mNetworkInfo.getTypeName().equals("WIFI")) {
            //获取wifi服务
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            //判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = intToIp(ipAddress);
            return ip;
        } else {
            return getLocalIpAddress();
        }
    }

    private String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }


    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.d("WifiPreference IpAddress", ex.toString());
        }
        return null;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = mediaPlayer1 -> mediaPlayer1.seekTo(0);

    public interface PayTypeListener {
        void onPayType(int type);
    }

    private PayTypeListener mPayTypeListener;

    public void setOnPaytypeListener(PayTypeListener mOnPaytypeListener) {
        this.mPayTypeListener = mOnPaytypeListener;
    }

    public void showPayDialog(int mPay_state) {
        View view = LayoutInflater.from(CaptureActivity.this).inflate(R.layout.layout_qrcode_dialog, null);
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
                tv_dialog_orderno.setText(orderNo);
                Log.d("QrCodeProductActivity", "支付时间 " + orderNo + ",支付金额:" + amount);
                tv_dialog_amount.setText("¥" + amount);
                tv_dialog_paytype.setText("" + payType);
                break;
        }
        final MyAlertDialog dialog1 = new MyAlertDialog(CaptureActivity.this)
                .builder();
        dialog1.setCancelable(false);
        dialog1.setView(view)
                .setNegativeButton("确定", v -> {
                    Intent mIntent = new Intent(CaptureActivity.this, HomeActivity.class);
                    startActivity(mIntent);
                    finish();
                });
        if (judgeBluetooth(dialog1)) {
            dialog1.setPositiveButton("确定并打印", v -> {
                HashMap<String, String> map = new HashMap<>();
                map.put("orderNo", orderNo);
                map.put("orderAmount", amount);
                map.put("payType", payType);
                map.put("outlet", PrintUtils.getSharePrintData(CaptureActivity.this).getString("outlet", ""));
                PrintUtils.printContentText(CaptureActivity.this, map);
                Intent mIntent = new Intent(CaptureActivity.this, HomeActivity.class);
                startActivity(mIntent);
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

    @Override
    public void onBackPressed() {
        if (type == 5) {
            CaptureActivity.this.finish();
            startActivity(new Intent(CaptureActivity.this, HomeActivity.class));
        }
        finish();
    }

    private String getErrorMsg(int dealCode) {
        String dealMsg = "";
        switch (dealCode) {
            case 20016:
                dealMsg = "该笔交易风险较高，拒绝此次交易";
                break;
            case 20029:
                dealMsg = "余额不足";
                break;
            case 20031:
                dealMsg = "订单已关闭";
                break;
            case 20032:
                dealMsg = "订单已撤销";
                break;
            case 20033:
                dealMsg = "银行系统异常";
                break;
            case 20037:
                dealMsg = "订单已支付";
                break;
            case 20038:
                dealMsg = "订单不存在";
                break;
            case 30001:
                dealMsg = "订单信息不存在";
                break;
        }
        return dealMsg;

    }
}
