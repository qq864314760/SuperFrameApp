package com.dev.superframe;

import android.app.Application;

import com.socks.library.KLog;

/**
 * Created by Administrator on 2017/6/22.
 */

public class BaseApp extends Application {
    private static Application instance;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        KLog.init(BuildConfig.LOG_DEBUG);
    }
}
