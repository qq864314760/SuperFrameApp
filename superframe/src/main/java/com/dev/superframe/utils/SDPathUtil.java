package com.dev.superframe.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.dev.superframe.config.AppConstantValue;

import java.io.File;
import java.io.IOException;


@SuppressLint({"DefaultLocale", "SimpleDateFormat"})
public class SDPathUtil {
    private final static String ROOT_DIR = AppConstantValue.APP_E_NAME;

    /**
     * 获取SD卡图片地址
     */
    public static String getPhotoPath() {
        String filePath = getAppSDRootPath() + "/photo/";
        File file = new File(filePath);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        file = null;
        return filePath;
    }

    /**
     * 获取SD卡崩溃地址
     */
    public static String getCrashPath() {
        String filePath = getAppSDRootPath() + "/crash/";
        File file = new File(filePath);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        file = null;
        return filePath;
    }

    /**
     * 获取SD缓存地址
     */
    public static String getCachePath() {
        String filePath = getAppSDRootPath() + "/cache/";
        File file = new File(filePath);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        file = null;
        return filePath;
    }

    /**
     * 获取到SD卡根目录
     */
    public static String getAppSDRootPath() {
        String filePath = "/" + ROOT_DIR;
        // 判断SD卡是否存在，并且是否具有读写权限
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // 获取SD卡路径
            filePath = Environment.getExternalStorageDirectory() + filePath;
        }

        File folder = new File(filePath);
        // 判断文件目录是否存在
        if (folder.exists() && folder.isDirectory()) {
            // do nothing
        } else {
            folder.mkdirs();
        }
        folder = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            File nomedia = new File(filePath + "/.nomedia");
            if (!nomedia.exists())
                try {
                    nomedia.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return filePath;
    }

    /**
     * 获取到SD卡根目录
     */
    public static String getAppRootPath(Context context) {
        String filePath = "/" + ROOT_DIR;
        // 判断SD卡是否存在，并且是否具有读写权限
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // 获取SD卡路径
            filePath = Environment.getExternalStorageDirectory() + filePath;
        } else {
            // 获取手机路径
            filePath = context.getCacheDir() + filePath;
        }

        File folder = new File(filePath);
        // 判断文件目录是否存在
        if (folder.exists() && folder.isDirectory()) {
            // do nothing
        } else {
            folder.mkdirs();
        }
        folder = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            File nomedia = new File(filePath + "/.nomedia");
            if (!nomedia.exists())
                try {
                    nomedia.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return filePath;
    }

    /**
     * 删除文件
     */
    public static boolean delFile(String filePath) {
        File file = new File(filePath);
        try {
            if (file.exists() && file.isFile()) {
                return file.delete();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isFileExist(String fileName) {
        File file = new File(getCachePath() + fileName);
        file.isFile();
        return file.exists();
    }

    public static File createSDDir(String dirName) throws IOException {
        File dir = new File(getCachePath() + dirName);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.i("createSDDir:::", "" + dir.getAbsolutePath() + "--" + dir.mkdir());
        }
        return dir;
    }
}
