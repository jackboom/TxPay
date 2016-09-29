package com.jszf.txsystem.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jszf.txsystem.MyApplication;
import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.HomeBean;
import com.jszf.txsystem.bean.Merchant;
import com.jszf.txsystem.core.mvp.MvpActivity;
import com.jszf.txsystem.core.mvp.home.HomePresenterImpl;
import com.jszf.txsystem.core.mvp.home.IHomeView;
import com.jszf.txsystem.fragment.MainFragment;
import com.jszf.txsystem.fragment.MessageFragment;
import com.jszf.txsystem.fragment.MyFragment;
import com.jszf.txsystem.service_receiver.BluetoothService;
import com.jszf.txsystem.util.BluetoothUtils;
import com.jszf.txsystem.util.Constant;
import com.jszf.txsystem.util.MD5Utils;
import com.jszf.txsystem.util.ParaUtils;
import com.jszf.txsystem.util.PrintUtils;
import com.jszf.txsystem.util.ProgressHUD;
import com.jszf.txsystem.util.RSAUtils;
import com.jszf.txsystem.util.UuidUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends MvpActivity<IHomeView, HomePresenterImpl> implements View.OnClickListener, IHomeView {
    @BindView(R.id.iv_home_collection)
    ImageView mIvHomeCollection;
    @BindView(R.id.iv_home_account)
    ImageView mIvHomeAccount;
    @BindView(R.id.iv_home_allScan)
    ImageView mIvHomeAllScan;
    @BindView(R.id.iv_home_message)
    ImageView mIvHomeMessage;
    @BindView(R.id.iv_home_myself)
    ImageView mIvHomeMyself;
    private Merchant mMerchant;
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetworkInfo;
    private ProgressHUD mProgressHUD;
    private int selected = 0;
    private ImageView[] ivArr = new ImageView[3];
    private int[] unSelectDrawble = new int[]{R.drawable.home_collection_normal,
            R.drawable.home_message_normal, R.drawable.home_mine_normal};
    private int[] SelectDrawble = new int[]{R.drawable.home_collection_press,
            R.drawable.home_message_press, R.drawable.home_mine_press};
    private String mUuid;
    private SharedPreferences mSp;
    private boolean[] isSelceted = new boolean[3];
    private SharedPreferences sp;

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String TOAST = "toast";
    private static final int TAB_MAIN = 0;          //主页
    private static final int TAB_MESSAGE = 1;      //消息
    private static final int TAB_MYSELF = 2;        //我的
    private MainFragment mainFragment;
    private MessageFragment messageFragment;
    private MyFragment myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setStatusBar();
        setPublicData();
        initRequest();
        initBluetooth();
        initPrintSettting();
        setSelect(TAB_MAIN);
    }

//    MD5:dea4c02423794a8ead4743b10a79126f

    @Override
    protected void initData() {

    }

    @Override
    protected HomePresenterImpl createPresenter() {
        return new HomePresenterImpl(this);
    }


    /**
     * 初始化蓝牙
     */
    private void initBluetooth() {
        if (MyApplication.getInstance().mBluetoothAdapter == null) {
            MyApplication.getInstance().mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Log.d("BluetoothActivity", "---" + (MyApplication.mBluetoothAdapter == null));
            if (MyApplication.getInstance().mBluetoothAdapter == null) {
                return;
            } else {
                connectBluetooth();
            }

        }
    }

    /**
     * 连接蓝牙
     */
    private void connectBluetooth() {
        //得到BLuetoothDevice对象
        if (!MyApplication.getInstance().mBluetoothAdapter.isEnabled()) {
            return;
        }
        if (MyApplication.getInstance().mService == null) {
            MyApplication.getInstance().mService = new BluetoothService(this, mHandler2);
        }
        sp = BluetoothUtils.getSharedPreferences(this);
        if (TextUtils.isEmpty(sp.getString("address", ""))) {
            return;
        }
        if (MyApplication.getInstance().mService.getState() == BluetoothService.STATE_CONNECTED) {
            return;
        }

        //获取设备进行连接
        BluetoothDevice device = MyApplication.getInstance().mBluetoothAdapter.getRemoteDevice(sp.getString("address", ""));
        //尝试连接到设备
        MyApplication.getInstance().mService.connect(device);
    }

    /**
     * 初始化打印设置
     */
    private void initPrintSettting() {
        SharedPreferences sp = PrintUtils.getSharePrintData(this);
        if (sp.getInt("print_type", 0) == MyApplication.printType || sp.getInt("print_type", 0) == 0) {
            sp.edit().putInt("print_type", MyApplication.printType).commit();
        }
        if (sp.getInt("time", 0) == MyApplication.printTime || sp.getInt("time", 0) == 0) {
            sp.edit().putInt("time", MyApplication.printTime).commit();
        }
        if (sp.getString("outlet", "").equals(MyApplication.print_outlet) ||
                TextUtils.isEmpty(sp.getString("outlet", ""))) {
            sp.edit().putString("outlet", MyApplication.print_outlet).commit();
        }

    }

    /**
     * 设置全局化一些公共的数据
     */
    private void setPublicData() {
        try {
            String md5Key = MyApplication.unEncryptMd5;
            MyApplication.MD5key = md5Key;
            Log.d("TAG", "md5key:" + md5Key);
            String md5 = RSAUtils.decrypt(md5Key, Constant.ownPrivateKey);
            MyApplication.MD5key = md5;
            Log.d("TAG", "md5:" + md5);
            MyApplication.mContext = HomeActivity.this;
            mUuid = UuidUtils.getMyUUID(this);
            MyApplication.mUuid = mUuid;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化请求
     */
    private void initRequest() {
        ivArr[0] = mIvHomeCollection;
        ivArr[1] = mIvHomeMessage;
        ivArr[2] = mIvHomeMyself;

        if (judgeNetWord(this) && MyApplication.mMerchant == null) {
            Log.d("HomeActivity", "----------------MyApplication.mMerchant == null:" + (MyApplication.mMerchant == null));
            mvpPresenter.requestHomeInfo(getRequestParams());
        }
    }

    private void changeButtonStatus(int position) {
        for (int i = 0; i < 3; i++) {
            if (i == position) {
                isSelceted[i] = true;
                ivArr[i].setImageResource(SelectDrawble[position]);
            } else {
                isSelceted[i] = false;
                ivArr[i].setImageResource(unSelectDrawble[i]);
            }
        }
    }

    /**
     * @return 网络请求数据
     */
    @NonNull
    private HashMap<String, String> getRequestParams() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("service", "getMerchantInfo");
        map.put("merchantNo", MyApplication.merchantNo);
        Log.d("TAG", "merchantNo:" + MyApplication.merchantNo);
        map.put("userLoginName", MyApplication.userLoginName);
        map.put("version", "V1.0");
        map.put("sign", MD5Utils.MD5(ParaUtils.createLinkString(map) + MyApplication.MD5key));
        return map;
    }

    /**
     * 判断网络状况
     *
     * @param mContext
     * @return
     */
    public boolean judgeNetWord(Context mContext) {
        mConnectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isAvailable() && mNetworkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    @OnClick({R.id.iv_home_collection, R.id.iv_home_account, R.id.iv_home_allScan,
            R.id.iv_home_message, R.id.iv_home_myself})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_home_collection:
                Log.d("HomeActivity", "1:" + 1);
                if (isSelceted[0]) {
                    return;
                }
                Log.d("HomeActivity", "isSelceted[0]:" + isSelceted[0]);
                setSelect(TAB_MAIN);
                break;
            case R.id.iv_home_account:
                Log.d("HomeActivity", "2:" + 2);
                Intent mIntent6 = new Intent(HomeActivity.this, BillActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("merchantInfo", mMerchant);
                mIntent6.putExtra("data", bundle1);
                startActivity(mIntent6);
                finish();
                break;
            case R.id.iv_home_allScan:
                Log.d("HomeActivity", "3:" + 3);
                Intent mIntent1 = new Intent(HomeActivity.this, ScanInputActivity.class);
                mIntent1.putExtra("merchantInfo", mMerchant);
                startActivity(mIntent1);
                finish();
                overridePendingTransition(R.anim.inuptodown, R.anim.outdowntoup);

                break;
            case R.id.iv_home_message:
                Log.d("HomeActivity", "4:" + 4);
                if (isSelceted[1]) {
                    return;
                }
                setSelect(TAB_MESSAGE);
                break;
            case R.id.iv_home_myself:
                Log.d("HomeActivity", "5:" + 5);
                if (isSelceted[2]) {
                    return;
                }
                setSelect(TAB_MYSELF);
                break;
        }
    }

    public Handler mHandler2 = new Handler() {
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
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public void onHomeRequest(HomeBean homeBean) {
        try {
            if (homeBean == null) {
                return;
            }
            String dealCode = homeBean.getDealCode();
            if (!dealCode.equals(Constant.DEAL_CODE_SUCCESS)) {
                Toast.makeText(HomeActivity.this, "系统,异常代码：" + dealCode,
                        Toast.LENGTH_SHORT).show();
                return;
            }
            mMerchant = new Merchant();
            mMerchant.setMerchantNo(MyApplication.merchantNo);
            mMerchant.setMerchantName(homeBean.getMerchantName());
            mMerchant.setBankName(homeBean.getBankName());
            mMerchant.setBankCompanyName(homeBean.getBankCompanyName());
            mMerchant.setBankCode(homeBean.getBankCode());
            mMerchant.setBankAddress(homeBean.getBankAddress());
            mMerchant.setSettlementPeriodType(homeBean.getSettlementPeriodType());
            mMerchant.setSettlementPeriodValue(homeBean.getSettlementPeriodValue());
            mMerchant.setAccountRetainAmount(homeBean.getAccountRetainAmount());
            mMerchant.setAccountFixAmount(homeBean.getAccountFixAmount());
            mMerchant.setAccountAllAmount(homeBean.getAccountAllAmount());
            mMerchant.setAccountUsableAmount(homeBean.getAccountUsableAmount());
            mMerchant.setTradingStatus(homeBean.getTradingStatus());
            mMerchant.setSettlementStatus(homeBean.getSettlementStatus());
            mMerchant.setTelphone(homeBean.getTelphone());
            MyApplication.mMerchant = mMerchant;

        } catch (Exception e) {
            Log.d("Home2Activity", e.getMessage().toString());
        }
    }

    /*
  * 将图片设置为亮色的；切换显示内容的fragment
  * */
    private void setSelect(int position) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();//创建一个事务
        hideFragment(transaction);//我们先把所有的Fragment隐藏了，然后下面再开始处理具体要显示的Fragment
        changeButtonStatus(position);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("merchantInfo", MyApplication.mMerchant);
        switch (position) {
            case TAB_MAIN:
                if (mainFragment == null) {
                    mainFragment = new MainFragment();
                    transaction.add(R.id.ll_home_container, mainFragment);
                }else {
                    transaction.show(mainFragment);
                }
                break;
            case TAB_MESSAGE:
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.ll_home_container, messageFragment);
                }else {
                    transaction.show(messageFragment);
                }
                break;
            case TAB_MYSELF:
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    transaction.add(R.id.ll_home_container, myFragment);
                }else {
                    transaction.show(myFragment);
                }
                break;
        }
        transaction.commit();//提交事务
    }

    /*
     * 隐藏所有的Fragment
     * */
    private void hideFragment(FragmentTransaction transaction) {
        if (mainFragment != null) {
            transaction.hide(mainFragment);
        }
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }
}

