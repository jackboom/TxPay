package com.jszf.txsystem.util;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author jacking
 *         Created at 2016/9/1 16:09 .
 */
public class WindowLightUtils {
    /**
     * 让屏幕变暗
     */
    public static void makeWindowDark(Activity mActivity) {
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.5f;
        window.setAttributes(lp);
    }

    /**
     * 让屏幕变亮
     */
    public static void makeWindowLight(Activity mActivity) {
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 1f;
        window.setAttributes(lp);
    }
}
