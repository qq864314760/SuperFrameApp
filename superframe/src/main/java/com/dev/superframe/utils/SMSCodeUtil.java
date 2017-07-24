package com.dev.superframe.utils;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * 获取验证码倒计时
 */

public class SMSCodeUtil {
    private static CountDownTimer countDownTimer;

    /**
     * Button倒计时
     *
     * @param btn
     */
    public static void startBtnTimer(final Button btn) {
        startBtnTimer(btn, 60);
    }

    /**
     * Button倒计时
     *
     * @param btn
     */
    public static void startBtnTimer(final Button btn, int second) {
        countDownTimer = new CountDownTimer(second * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                btn.setText(millisUntilFinished / 1000 + "秒");
                btn.setClickable(false);// 防止重复点击
            }

            @Override
            public void onFinish() {
                // 可以在这做一些操作,如果没有获取到验证码再去请求服务器
                btn.setClickable(true);// 防止重复点击
                btn.setText("获取验证码");
            }
        };
        countDownTimer.start();
    }

    public static void cancelTimer(final Button btn) {
        countDownTimer.cancel();
        btn.setClickable(true);// 防止重复点击
        btn.setText("获取验证码");
    }
}
