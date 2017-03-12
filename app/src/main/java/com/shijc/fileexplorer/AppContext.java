package com.shijc.fileexplorer;

import android.app.Application;

/**
 * Class:AppContext
 * Description: 全局application
 * User: shijiacheng.
 * Date: 2017/3/12.
 */

public class AppContext extends Application {

    private static AppContext instance;

    public static AppContext getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
