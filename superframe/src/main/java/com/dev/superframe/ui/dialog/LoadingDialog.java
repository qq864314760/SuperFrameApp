package com.dev.superframe.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.superframe.R;
import com.dev.superframe.utils.DensityUtil;


/**
 * description:弹窗浮动加载进度条
 * Created by xsf
 * on 2016.07.17:22
 */
public class LoadingDialog {
    /**
     * 加载数据对话框
     */
    public static Dialog mLoadingDialog;

    /**
     * 显示加载对话框
     *
     * @param context    上下文
     * @param msg        对话框显示内容
     * @param cancelable 对话框是否可以取消
     */
    public static Dialog showDialogForLoading(Context context, String msg, boolean cancelable) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_loading, null);
        TextView loadingText = (TextView) view.findViewById(R.id.id_tv_loading_dialog_text);
        loadingText.setText(msg);

        mLoadingDialog = new Dialog(context, R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        //mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 80), DensityUtil.dip2px(context, 80)));
        mLoadingDialog.show();
        return mLoadingDialog;
    }

    public static Dialog showDialogForLoading(Context context, boolean cancelable) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_loading, null);
        TextView loadingText = (TextView) view.findViewById(R.id.id_tv_loading_dialog_text);
        loadingText.setText("加载中...");

        mLoadingDialog = new Dialog(context, R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setCanceledOnTouchOutside(false);
//        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 80), DensityUtil.dip2px(context, 80)));
        mLoadingDialog.show();
        return mLoadingDialog;
    }

    public static Dialog showDialogForLoading(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_loading, null);
        TextView loadingText = (TextView) view.findViewById(R.id.id_tv_loading_dialog_text);
        loadingText.setText("加载中...");

        mLoadingDialog = new Dialog(context, R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setCanceledOnTouchOutside(false);
//        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 80), DensityUtil.dip2px(context, 80)));
        mLoadingDialog.show();
        return mLoadingDialog;
    }

    /**
     * 关闭加载对话框
     */
    public static void cancelDialogForLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.cancel();
        }
    }
}
