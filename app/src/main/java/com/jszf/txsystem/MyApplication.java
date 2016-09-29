package com.jszf.txsystem;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;

import com.ibm.micro.client.mqttv3.MqttClient;
import com.jszf.txsystem.bean.Merchant;
import com.jszf.txsystem.service_receiver.BluetoothService;

import org.xutils.x;

import java.util.logging.Logger;


/**
 * Created by Administrator on 2016/4/27.
 */
public class MyApplication extends Application {
    private String TAG = "tag";

    private  static MyApplication instance;

    public static Context mContext;
    public static String MD5key;
    public static String merchantNo;
    public static String mUuid;
    public static MqttClient client;        //MQTT服务客户端对象
    public static String unEncryptMd5 ;
    public static Intent mIntent;
    public static String userLoginName;
    public static Merchant mMerchant;
//    public static HsBluetoothPrintDriver mHsBluetoothPrintDriver;


    //当地蓝牙适配器
    public static BluetoothAdapter mBluetoothAdapter=null;
    public static BluetoothService mService=null;
    public static ArrayAdapter<String> mPairedDevicesArrayAdapter;
    public static ArrayAdapter<String> mUPaireDevicesArrayAdapter;
    public static int printType = 3;        //1->顾客联 2->财务联 3->顾客+财务
    public static int printTime = 3;        //单位秒/s
    public static String print_outlet = "";        //收款单位
    public static String ROOT_PATH;


    public  static MyApplication getInstance(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        instance = this;
        mContext = getApplicationContext();
        Logger.getLogger(TAG);
        ROOT_PATH = getDir("tongxing", MODE_PRIVATE).getAbsolutePath();
        if (!ROOT_PATH.endsWith("/")) {
            ROOT_PATH += "/";
        }

    }


//    private void intBluetooth() {
//        if (MyApplication.getInstance().mBluetoothAdapter == null) {
//            MyApplication.getInstance().mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//            Log.d("BluetoothActivity", "---" + (MyApplication.mBluetoothAdapter == null));
//        }
//    }


    public String getRootPath() {
        return ROOT_PATH;
    }

}
