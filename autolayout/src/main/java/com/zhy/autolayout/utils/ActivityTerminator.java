package com.zhy.autolayout.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/8.
 */
public class ActivityTerminator {
    private static List<Activity> activityList = new ArrayList<Activity>();

    public static void remove(Activity activity) {
        activityList.remove(activity);
    }




    public static void add(Activity activity) {
        activityList.add(activity);
    }


    public static void finishProgram() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }
}
