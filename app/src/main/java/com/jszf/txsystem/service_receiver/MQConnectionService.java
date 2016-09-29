package com.jszf.txsystem.service_receiver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
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
import com.jszf.txsystem.activity.QrCodeProductActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * mqtt服务
 */
public class MQConnectionService extends Service {
    public static MqttClient client;        //MQTT服务客户端对象
    private static Handler mHandler;
    private static Context mContext;
//    private String host = "tcp://182.140.132.216:1883";   //服务器主机地址
    private String host = "tcp://182.147.243.123:1883";     //正式环境地址
    //    private String host = "tcp://www.tongxingpay.com:1883";   //服务器主机地址
    private String userName = "txpay";      //用户名
    private String passWord = "txpay@2016";      //用户密码
    private String myTopic = "TX_APP_PAY";      //mqtt消息系统的订阅主题
    private MqttConnectOptions options;
    private ScheduledExecutorService scheduler;
    public static List list = new ArrayList();
    private MyBind mBind = new MyBind();
    private static Intent mIntent;

    public class MyBind extends Binder {
        public MQConnectionService getMyService() {
            return MQConnectionService.this;
        }
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public MQConnectionService() {
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
//        connect();

        return super.onStartCommand(intent, flags, startId);
    }

    public MQConnectionService(Context mContext, Handler mHandler) {
        this.mHandler = mHandler;
        this.mContext = mContext;

    }

    public static void start() {
        mIntent = new Intent(mContext, MQConnectionService.class);
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
        // 设置回调
        client.setCallback(new MqttCallback() {

            @Override
            public void connectionLost(Throwable cause) {
                // 连接丢失后，一般在这里面进行重连
                Log.d("TAG", "connectionLost");
                System.out.println("connectionLost----------");
            }

//            @Override
//            public void messageArrived(String mS, MqttMessage mMqttMessage) throws Exception {
////                client.publish(myTopic,mMqttMessage.getPayload(),1,false);
//                boolean isRetained = mMqttMessage.isRetained();
//                if (!isRetained && !mMqttMessage.isDuplicate()) {
//
//                    String result = new String(mMqttMessage.getPayload());
//                    Log.d("TAG", "收到MQ消息：" + result);
//                    Message msg = Message.obtain();
//                    msg.what = 1;
//                    msg.obj = result;
//                    QrCodeProductActivity.mHandler.sendMessage(msg);
//                    client.publish(myTopic, mMqttMessage.getPayload(), 2, false);
//                    mMqttMessage.clearPayload();
//                }
//            }

//            @Override
//            public void deliveryComplete(IMqttDeliveryToken mIMqttDeliveryToken) {
//                System.out.println("deliveryComplete---------"
//                        + mIMqttDeliveryToken.isComplete());
//            }


            @Override
            public void messageArrived(MqttTopic arg0, final MqttMessage arg1)
                    throws Exception {
                boolean isRetained = arg1.isRetained();
                if (!isRetained && !arg1.isDuplicate()) {

                    String result = new String(arg1.getPayload());
                    Log.d("TAG","收到结果："+result);
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = result;
                    QrCodeProductActivity.mHandler.sendMessage(msg);
                    arg1.clearPayload();
                arg1.setRetained(false);
                }
            }

            @Override
            public void deliveryComplete(MqttDeliveryToken mMqttDeliveryToken) {
                System.out.println("deliveryComplete---------"
                        + mMqttDeliveryToken.isComplete());
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
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = 3;
//                    mHandler.sendMessage(msg);
                }
            }
        }).start();

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
}
