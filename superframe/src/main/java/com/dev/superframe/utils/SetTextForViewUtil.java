package com.dev.superframe.utils;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/2/4.
 */

public class SetTextForViewUtil {

    public static void setText(View view, int resId, String text) {
        TextView textView = (TextView) view.findViewById(resId);
        textView.setText(text);
    }

    public static void setTextIsEmpty(View view, int resId, String text) {
        TextView textView = (TextView) view.findViewById(resId);
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }

    public static void setTag(View view, int resId, Object o) {
        (view.findViewById(resId)).setTag(o);
    }

    public static void setOnClick(View view, int resId, View.OnClickListener listener) {
        (view.findViewById(resId)).setOnClickListener(listener);
    }
}
