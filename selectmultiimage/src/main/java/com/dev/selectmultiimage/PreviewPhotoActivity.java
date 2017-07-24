package com.dev.selectmultiimage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class PreviewPhotoActivity extends Activity {
    private ImageView ivDisplay;
    private Button btnCancel, btnSave, btnLeftRotate, btnRightRotate;

    private float degrees = 0;// 旋转角度
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_previewphoto);

        ivDisplay = (ImageView) findViewById(R.id.iv_display);
        btnLeftRotate = (Button) findViewById(R.id.btn_left_rotate);
        btnRightRotate = (Button) findViewById(R.id.btn_right_rotate);

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSave = (Button) findViewById(R.id.btn_save);

        initBitmap();

        btnLeftRotate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                degrees -= 90;
                ivDisplay.setImageBitmap(setBitmapRotate(bitmap, degrees));
            }
        });
        btnRightRotate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                degrees += 90;
                ivDisplay.setImageBitmap(setBitmapRotate(bitmap, degrees));
            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SDFileUtil.delFile(SDFileUtil.getPhotoCachePath() + "temp.jpg");
                finish();
            }
        });
        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                degrees = 0;
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
                    setResult(10001, intent);
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
