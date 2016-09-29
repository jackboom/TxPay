package com.jszf.txsystem.service_receiver;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ibm.micro.client.mqttv3.MqttCallback;
import com.ibm.micro.client.mqttv3.MqttClient;
import com.ibm.micro.client.mqttv3.MqttConnectOptions;
import com.ibm.micro.client.mqttv3.MqttDeliveryToken;
import com.ibm.micro.client.mqttv3.MqttException;
import com.ibm.micro.client.mqttv3.MqttMessage;
import com.ibm.micro.client.mqttv3.MqttTopic;
import com.ibm.micro.client.mqttv3.internal.MemoryPersistence;
import com.jszf.txsystem.MyApplication;
import com.jszf.txsystem.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * mqtt服务
 */
public class PushService extends Service {
    public static final String TAG = "PushService";
    private static final String NOTIF_TITLE = "同兴支付";
    private static int NOTIF_CONNECTED = 0;
    public static com.ibm.micro.client.mqttv3.MqttClient client;        //MQTT服务客户端对象
    private static Handler mHandler;
    private static Context mContext;
    public static final String PREF_STARTED = "isStarted";
    //    private String host = "tcp://192.168.1.107:1883";   //服务器主机地址
    private String host = "tcp://182.140.132.216:1883";   //服务器主机地址
    //    private String host = "tcp://www.tongxingpay.com:1883";   //服务器主机地址
//        private String host = "tcp://192.168.1.190:1883";   //服务器主机地址
    private String userName = "txpay";      //用户名
    private String passWord = "txpay@2016";      //用户密码
    private MqttConnectOptions options;
    private ScheduledExecutorService scheduler;
    public static List list = new ArrayList();
    private MyBind mBind = new MyBind();
    private static Intent mIntent;
    private NotificationManager mNotifMan;
    private SharedPreferences mPrefs;
    private ConnectivityManager mConnMan;
    private boolean mStarted;

    public class MyBind extends Binder {
        public PushService getMyService() {
            return PushService.this;
        }
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public PushService() {
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        startReconnect();
        registerReceiver(mConnectivityChanged, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));
//        connect();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        mPrefs = getSharedPreferences(TAG, MODE_PRIVATE);
        mConnMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        mNotifMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public PushService(Context mContext, Handler mHandler) {
        this.mHandler = mHandler;
        this.mContext = mContext;

    }

    public static void start() {
        mIntent = new Intent(mContext, PushService.class);
//        mIntent.setAction(ACTION_START);
        mContext.startService(mIntent);
    }

    public static void stop() {
        mContext.stopService(mIntent);
    }

    //初始化mqttclient
    private void init() {
        try {

            // host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            Log.d("TAG", "uuid-->" + MyApplication.mUuid + "\n" + MyApplication.mUuid.substring(0, 23));
//            ffffffff-b5fe-5d73-ffff-ffffe9094bb1
//            ffffffff-b6a7-68a9-27c6-53d50033c587
            client = new MqttClient(host, MyApplication.mUuid.substring(0, 23), new MemoryPersistence());
            MyApplication.client = client;
            // MQTT的连接设置
            options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(false);
            // 设置连接的用户名
            options.setUserName(userName);
            // 设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(20);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            Log.d("TAG", "2");
            callback();
            // connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callback() throws MqttException {
        Log.d("TAG", "收到");
//        showNotification("收到");
        // 设置回调
        client.setCallback(new MqttCallback() {

            @Override
            public void connectionLost(Throwable cause) {
                // 连接丢失后，一般在这里面进行重连
                Log.d("TAG", "connectionLost");
                System.out.println("connectionLost----------");
            }

            @Override
            public void deliveryComplete(MqttDeliveryToken mMqttDeliveryToken) {
                System.out.println("deliveryComplete:"+mMqttDeliveryToken.isComplete());

            }

            @Override
             public void messageArrived(MqttTopic arg0, final MqttMessage arg1)
                    throws Exception {
                arg1.setQos(1);
                arg1.setRetained(false);
                boolean isRetained = arg1.isRetained();
                if (!isRetained && !arg1.isDuplicate()) {
                    String result = new String(arg1.getPayload());
                    showNotification(result);
                    Log.d("TAG", "收到结果：" + result);
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = result;
                    arg1.clearPayload();
                }
            }
        });
    }


    //开始重新连接
    private void startReconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                if (!client.isConnected()) {
                    connect();
                }
            }
        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }

    //连接服务器
    private synchronized void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.connect(options);
                    Message msg = Message.obtain();
                    msg.what = 2;
//                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = 3;
//                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private BroadcastReceiver mConnectivityChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get network info
            NetworkInfo info = (NetworkInfo) intent
                    .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

            // Is there connectivity?
            boolean hasConnectivity = (info != null && info.isConnected()) ? true
                    : false;
//            //设置点击通知栏的动作为启动另外一个广播
//            Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
//            PendingIntent pendingIntent = PendingIntent.
//                    getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Log.d("TAG", "Connectivity changed: connected=" + hasConnectivity);

//            if (hasConnectivity) {
//                reconnectIfNecessary();
//            } else if (mConnection != null) {
//                // if there no connectivity, make sure MQTT connection is
//                // destroyed
//                mConnection.disconnect();
//                cancelReconnect();
//                mConnection = null;
//            }
        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showNotification(String text) {
//        Notification n = new Notification();
//
//        n.flags |= Notification.FLAG_SHOW_LIGHTS;
//        n.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        n.defaults = Notification.DEFAULT_ALL;
//
//        n.icon = com.tokudu.demo.R.drawable.icon;
//        n.when = System.currentTimeMillis();
//
//        // Simply open the parent activity
//        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,
//                PushActivity.class), 0);
//
//        // Change the name of the notification here
//        n.setLatestEventInfo(this, NOTIF_TITLE, text, pi);
        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setContentTitle(NOTIF_TITLE);
        mBuilder.setSmallIcon(R.drawable.ic_login_logo);
        mBuilder.setContentText(text);
        mBuilder.setShowWhen(true);
        mBuilder.setWhen(System.currentTimeMillis());
        Notification mNotification = mBuilder.build();
        mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotification.defaults = Notification.DEFAULT_ALL;

        mNotifMan.notify(NOTIF_CONNECTED++, mNotification);
    }
//    Notification.Builder mBuilder = new Notification.Builder(this);
//    mBuilder.setContentTitle(NOTIF_TITLE);
//    mBuilder.setSmallIcon(R.drawable.icon_58);
//    mBuilder.setContentText(text);
//    mBuilder.setShowWhen(true);
//    mBuilder.setWhen(System.currentTimeMillis());
//    Notification mNotification = mBuilder.build();
//    mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
//    mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
//    mNotification.defaults = Notification.DEFAULT_ALL;

    private void setStarted(boolean started) {
        mPrefs.edit().putBoolean(PREF_STARTED, started).commit();
        mStarted = started;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "destory");
        unregisterReceiver(mConnectivityChanged);
    }
}
