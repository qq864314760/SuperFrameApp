package com.dev.selectmultiimage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.dev.selectmultiimage.utils.BitmapUtil;
import com.dev.selectmultiimage.utils.SDFileUtil;
import com.dev.selectmultiimage.utils.compress.CompressHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PreviewActivity extends Activity {
    private ImageView ivDisplay;
    private Button btnCancel, btnSave, btnLeftRotate, btnRightRotate;

    private float degrees = 0;// 旋转角度
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ivDisplay = (ImageView) findViewById(R.id.iv_display);
        btnLeftRotate = (Button) findViewById(R.id.btn_left_rotate);
        btnRightRotate = (Button) findViewById(R.id.btn_right_rotate);

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSave = (Button) findViewById(R.id.btn_save);
        initView();

    }


    public void initView() {
        initBitmap();

        btnLeftRotate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                degrees -= 90;
                bitmap = setBitmapRotate(bitmap, degrees);
                ivDisplay.setImageBitmap(bitmap);
            }
        });
        btnRightRotate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                degrees += 90;
                bitmap = setBitmapRotate(bitmap, degrees);
                ivDisplay.setImageBitmap(bitmap);
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";//直接以当前时间戳作为文件名
                    if (bitmap != null) {
                        BitmapUtil.saveToFile(bitmap, new File(SDFileUtil.getPhotoCachePath()));
                        Log.e("本地图片保存", SDFileUtil.getPhotoCachePath() + fileName);
                    }
                    Intent intent = new Intent();
                    intent.putExtra("path", SDFileUtil.getPhotoCachePath() + fileName);

                    /*
                     * 调用setResult方法表示我将Intent对象返回给之前的那个Activity，
					 * 这样就可以在onActivityResult方法中得到Intent对象，
					 */
                    setResult(3, intent);
                    // 释放图片资源
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                    // 结束当前这个Activity对象的生命
                    finish();
                } catch (Exception e) {
                    // showToast("图片太大剪切失败，请重试");
                    e.printStackTrace();
                    finish();
                }
            }
        });
    }

    /**
     * 初始化图片资源
     */
    @SuppressWarnings("deprecation")
    private void initBitmap() {
        try {
            String path = getIntent().getStringExtra("path");
            if (TextUtils.isEmpty(path)) {
                path = SDFileUtil.getPhotoCachePath() + "temp.jpg";
            }
            bitmap = CompressHelper.getDefault(this).compressToBitmap(new File(path));
            ivDisplay.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            // showToast("图片太大剪切失败，请重试");
            finish();
        }
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private float getBitmapDegree(String path) {
        int degree = 0;
        try {
            File file = new File(path);
            if (file.exists()) {
                // 从指定路径下读取图片，并获取其EXIF信息
                ExifInterface exifInterface = new ExifInterface(path);
                // 获取图片的旋转信息
                int orientation = exifInterface.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("旋转角度", "" + degree);
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */

    public static Bitmap rotateBitmapByDegree(Bitmap bm, float degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 图片旋转
     */
    private Bitmap setBitmapRotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, false);
    }

}
