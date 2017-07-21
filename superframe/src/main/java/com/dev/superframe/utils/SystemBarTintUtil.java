package com.dev.superframe.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.dev.superframe.manger.SystemBarTintManager;

/**
 * Created by Administrator on 2017/7/21.
 */

public class SystemBarTintUtil {
    public static void setSystemBarTint(Context context, int color) {
        // 状态栏沉浸，4.4+生效 <<<<<<<<<<<<<<<<<
        //不保留状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //Android5.0版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                ((Activity) context).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //设置状态栏颜色
                ((Activity) context).getWindow().setStatusBarColor(((Activity) context).getResources().getColor(color));
            }
            //虚拟键盘
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(((Activity) context));
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);//状态背景色，可传drawable资源
        }
        // 状态栏沉浸，4.4+生效 >>>>>>>>>>>>>>>>>
    }
}
