package com.jszf.txsystem.util;

import android.content.Context;

/**
 * 作者：zengtao
 * 邮箱：1039562669@qq.com
 * 时间：2015/10/13 0013 19:18
 */
public class UnitUtils {

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
