package com.jszf.txsystem.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/8/19.
 */
public class BluetoothUtils {
    public static SharedPreferences getSharedPreferences(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences("bluetooth_address",mContext.MODE_PRIVATE);
        return sp;
    }
}
