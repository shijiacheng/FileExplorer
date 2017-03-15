package com.shijc.fileexplorer.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Class:ScreenUtils
 * Description:
 * User: shijiacheng.
 * Date: 2017/3/15.
 */

public class ScreenUtils {

    /**
     * 获得屏幕高度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        Resources resource = context.getResources();
        DisplayMetrics displayMetrics = resource.getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @return
     */
    public static int getScreenHeight(Context context) {
        Resources resource = context.getResources();
        DisplayMetrics displayMetrics = resource.getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

}
