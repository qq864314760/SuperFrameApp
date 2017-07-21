package com.dev.superframe.http;


import com.dev.superframe.config.AppConstantValue;
import com.socks.library.KLog;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Administrator on 2017/2/4.
 */

public class GetJsonUtil {
    private static String TAG = GetJsonUtil.class.getSimpleName();

    //++++++++++++服务器JSON解析关键字+++++++++++++++
    public static final String CODE = "status";
    public static final String ERROR = "error";
    public static final String RESULT = "result";
    public static final String DATA = "data";
    //++++++++++++服务器JSON解析关键字+++++++++++++++

    private static JSONObject getJSONObject(String resultJson) {
        try {
            return new JSONObject(resultJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param resultJson
     * @return
     */
    public static int getResponseCode(String resultJson) {
        try {
            return getJSONObject(resultJson).getInt(CODE);//TODO CODE 改为你服务器设定的key
        } catch (Exception e) {
            KLog.e(TAG, "getResponseCode No value for status");
        }
        return 0;
    }

    /**
     * @param resultJson
     * @return
     */
    public static String getResponseResult(String resultJson) {
        try {
            return getJSONObject(resultJson).getString(RESULT);//TODO DATA 改为你服务器设定的key
        } catch (Exception e) {
            KLog.e(TAG, "getResponseResult No value for result");
        }
        return null;
    }

    /**
     * @param resultJson
     * @return
     */
    public static String getResponseData(String resultJson) {
        try {
            return getJSONObject(resultJson).getString(DATA);//TODO DATA 改为你服务器设定的key
        } catch (Exception e) {
            KLog.e(TAG, "getResponseData No value for data");
        }
        return null;
    }

    /**
     * @param resultJson
     * @return
     */
    public static String getResponseResultData(String resultJson) {
        try {
            return getJSONObject(getResponseResult(resultJson)).getString(DATA);//TODO DATA 改为你服务器设定的key
        } catch (Exception e) {
            KLog.e(TAG, "getResponseData No value for result data");
        }
        return null;
    }

    /**
     * @param resultJson
     * @return
     */
    public static String getResponseError(String resultJson) {
        try {
            return getJSONObject(resultJson).getString(ERROR);//TODO DATA 改为你服务器设定的key
        } catch (Exception e) {
            KLog.e(TAG, "getResponseData No value for error");
        }
        return AppConstantValue.NETWORK_STATE;
    }
}
