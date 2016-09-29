package com.jszf.txsystem.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jszf.txsystem.MyApplication;
import com.jszf.txsystem.R;
import com.jszf.txsystem.core.mvp.base.BaseActivity;
import com.jszf.txsystem.service_receiver.BluetoothService;
import com.jszf.txsystem.util.BluetoothUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BluetoothActivity extends BaseActivity {

    @BindView(R.id.iv_bluetooth_back)
    ImageView mIvBluetoothBack;
    @BindView(R.id.title_paired_devices)
    TextView mTitlePairedDevices;
    @BindView(R.id.recyle_bluetooth)
    ListView mRecyleBluetooth;
    @BindView(R.id.title_new_devices)
    TextView mTitleNewDevices;
    @BindView(R.id.un_recyle_bluetooth)
    ListView mUnRecyleBluetooth;
    @BindView(R.id.tv_bluetooth_connect)
    TextView mTvBluetoothConnect;
    @BindView(R.id.rl_connect)
    RelativeLayout mRlConnect;
    // 成员字段
    public static BluetoothAdapter mBtAdapter;
//    @BindView(R.id.print_setting)
//    TextView mPrintSetting;
    @BindView(R.id.show_survey)
    ProgressBar mShowSurvey;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private List<String> addressList = new ArrayList<>();
    private ArrayAdapter<String> mUPaireDevicesArrayAdapter;
    // 返回额外的意图
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private String address;

    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    private boolean isRegister = false;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        ButterKnife.bind(this);
        setStatusBar();
        // 本地的蓝牙适配器
        Log.d("BluetoothActivity", "MyApplication.mBluetoothAdapter == null:" + (MyApplication.getInstance().mBluetoothAdapter == null));
//        if (MyApplication.mBluetoothAdapter == null) {
//            Log.d("BluetoothActivity", "蓝牙1");
//
//            mBtAdapter = BluetoothAdapter.getDefaultAdapter();
//            MyApplication.mBluetoothAdapter = mBtAdapter;
//        }
        //蓝牙是否可用
        Log.d("BluetoothActivity", "MyApplication.mBluetoothAdapter.isEnabled():" + MyApplication.getInstance().mBluetoothAdapter.getState());
        if (!MyApplication.getInstance().mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            Log.d("BluetoothActivity", "蓝牙2");
            startActivityForResult(intent, 1);
        } else {
            if (MyApplication.getInstance().mService == null) {
                init();
                setConnect();
                Log.d("BluetoothActivity", "mBtAdapter.isEnabled():" +
                        MyApplication.getInstance().mBluetoothAdapter.isEnabled() + "," +
                        MyApplication.getInstance().mBluetoothAdapter.getState());
            } else {
                if (MyApplication.getInstance().mService.getState() == BluetoothService.STATE_CONNECTED) {
                    init();
                    mShowSurvey.setVisibility(View.GONE);
                    mTvBluetoothConnect.setVisibility(View.VISIBLE);
                    mTvBluetoothConnect.setText("已连接上" + sp.getString("name", ""));
                } else {
                    init();
//                    setConnect();
                }
            }

        }
    }

    private void connectBluetooth() {
        //得到BLuetoothDevice对象
        if (MyApplication.getInstance().mService == null) {
            MyApplication.getInstance().mService = new BluetoothService(this, mHandler);
        }
        if (TextUtils.isEmpty(sp.getString("address", ""))) {
            return;
        }

        //获取设备进行连接
        BluetoothDevice device = MyApplication.getInstance().mBluetoothAdapter.getRemoteDevice(sp.getString("address", ""));
        //尝试连接到设备
        MyApplication.getInstance().mService.connect(device);
        if (MyApplication.getInstance().mService.getState() == BluetoothService.STATE_CONNECTED) {
            mTvBluetoothConnect.setVisibility(View.VISIBLE);
            mTvBluetoothConnect.setText("已连接上" + sp.getString("name", ""));
            Log.d("BluetoothActivity", "----------设备已连接" + sp.getString("name", ""));
        }
    }

    private void init() {
        mBtAdapter = MyApplication.getInstance().mBluetoothAdapter;
        //用于已经配对设备和对新发现的设备初始化阵列适配器
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mUPaireDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mRecyleBluetooth.setAdapter(mPairedDevicesArrayAdapter);
        mRecyleBluetooth.setOnItemClickListener(mDeviceClickListener);


        mUnRecyleBluetooth.setAdapter(mPairedDevicesArrayAdapter);
        mUnRecyleBluetooth.setOnItemClickListener(mDeviceClickListener);

        sp = BluetoothUtils.getSharedPreferences(BluetoothActivity.this);

        // 当设备被发现注册广播
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        //当发现已经完成了注册广播
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);


        // 给一组目前配对设备
        Set<BluetoothDevice> pairedDevices = MyApplication.mBluetoothAdapter.getBondedDevices();
        doDiscovery();

        // 如果有配对设备,添加每一个ArrayAdapter
        int mCount = pairedDevices.size();
        Log.d("BluetoothActivity", "mCount:" + mCount);
        String mTitle = getResources().getText(R.string.title_paired_devices) + "  数量:" + String.valueOf(mCount);
        mTitlePairedDevices.setText(mTitle);
        if (mCount > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                addressList.add(device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }
    }

    private void setConnect() {
        if (!TextUtils.isEmpty(sp.getString("address", ""))) {
            for (String s : addressList) {
                if (s.equals(sp.getString("address", ""))) {
                    mShowSurvey.setVisibility(View.GONE);
                    connectBluetooth();
                    break;
                }
            }
        }
    }

    /**
     * BluetoothAdapter开始发现设备
     */
    private void doDiscovery() {
        mUPaireDevicesArrayAdapter.clear();

        //打开新设备的字幕
        mTitleNewDevices.setVisibility(View.VISIBLE);
        // 如果已经发现就停止它
        if (MyApplication.getInstance().mBluetoothAdapter.isDiscovering()) {
            Log.d("BluetoothActivity", "MyApplication.getInstance().mBluetoothAdapter.isDiscovering():" +
                    MyApplication.getInstance().mBluetoothAdapter.isDiscovering());
            MyApplication.getInstance().mBluetoothAdapter.cancelDiscovery();
            mShowSurvey.setVisibility(View.GONE);
        }

        // 请求从BluetoothAdapter发现
        MyApplication.getInstance().mBluetoothAdapter.startDiscovery();
        Log.d("BluetoothActivity", "开始查询");
    }


    // 当发现是完成了，BroadcastReceiver监听发现设备和更改标题
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            isRegister = true;
            // 当发现一个设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //得到BluetoothDevice对象的意图
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 如果它已经配对过,跳过它因为它是已经配对的
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    if (MyApplication.mUPaireDevicesArrayAdapter == null) {
                        mUPaireDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    }
                    int mCount = mUPaireDevicesArrayAdapter.getCount();
                    mUnRecyleBluetooth.setAdapter(mUPaireDevicesArrayAdapter);
                    String mTitle = getResources().getText(R.string.title_other_devices) + "  数量:" + String.valueOf(mCount);
                    Log.d("BluetoothActivity", "Count:" + mCount);
                    mTitleNewDevices.setText(mTitle);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setTitle(R.string.select_device);
                if (mUPaireDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mUPaireDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };

    // 点击的监听器ListViews的所有设备中
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // 取消发现
            Log.d("BluetoothActivity", "MyApplication.mBluetoothAdapter == null:" +
                    (MyApplication.getInstance().mBluetoothAdapter.isDiscovering()));
            if (MyApplication.getInstance().mBluetoothAdapter.isDiscovering()) {
                MyApplication.getInstance().mBluetoothAdapter.cancelDiscovery();
            }
            //获取设备的MAC地址
            String info = ((TextView) v).getText().toString();
            Log.d("BluetoothActivity", "数据：" + info.substring(0, info.length() - 17));

            if (!TextUtils.isEmpty(info) && !info.equals("没有匹配的设备")) {
                address = info.substring(info.length() - 17);
                Log.d("BluetoothActivity", "--------" + address);
//                if (!address.equals(sp.getString("address","")) || TextUtils.isEmpty(sp.getString("address",""))) {
                sp.edit().putString("address", address).commit();
                sp.edit().putString("name", info.substring(0, info.length() - 17)).commit();
//                }
                connectBluetooth();
                if (MyApplication.getInstance().mService.getState() == BluetoothService.STATE_CONNECTED) {
                    mShowSurvey.setVisibility(View.GONE);
                }
                //获取设备进行连接
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 确保没有发现
        if (MyApplication.getInstance().mBluetoothAdapter != null) {
            MyApplication.getInstance().mBluetoothAdapter.cancelDiscovery();
        }
        // 注销广播听众
        if (isRegister) {
            this.unregisterReceiver(mReceiver);
        }
    }

    private String mConnectedDeviceName;
    //更新标题栏右边状态和读写状态的Handler
    public Handler mHandler = new Handler() {
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
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    mTvBluetoothConnect.setVisibility(View.VISIBLE);

                    mTvBluetoothConnect.setText("已连接上" + mConnectedDeviceName);
//                    Toast.makeText(getApplicationContext(), "连接到" + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (MyApplication.getInstance().mBluetoothAdapter.isEnabled()) {
                    init();
                    setConnect();
                    Log.d("BluetoothActivity", "mBtAdapter.isEnabled():" +
                            MyApplication.getInstance().mBluetoothAdapter.isEnabled() + "," +
                            MyApplication.getInstance().mBluetoothAdapter.getState());
                } else {
                }
            } else {
                mTitleNewDevices.setVisibility(View.GONE);
                mRlConnect.setVisibility(View.GONE);
                mShowSurvey.setVisibility(View.GONE);

            }
        }
    }

    @OnClick({R.id.iv_bluetooth_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_bluetooth_back:
                finish();
                break;
//            case R.id.print_setting:
//                startActivity(new Intent(BluetoothActivity.this, PrintSettingActivity.class));
//                break;
        }
    }
}
