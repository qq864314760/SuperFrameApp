package com.dev.selectmultiimage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.selectmultiimage.adapter.ImageGridAdapter;
import com.dev.selectmultiimage.bean.ImageItem;
import com.dev.selectmultiimage.config.AppConstantValue;
import com.dev.selectmultiimage.utils.AlbumHelper;
import com.dev.selectmultiimage.utils.BitmapUtil;
import com.dev.selectmultiimage.utils.SDFileUtil;
import com.dev.selectmultiimage.utils.compress.CompressHelper;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class ImageGridActivity extends Activity {
    public static final String EXTRA_IMAGE_LIST = "imagelist";
    private int picNum = AppConstantValue.SELECT_IMG_NUM;

    GridView gvDisplay;
    Button btnOk;
    TextView tvCancel;

    List<ImageItem> dataList;
    ImageGridAdapter mAdapter;
    AlbumHelper helper;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(ImageGridActivity.this, "最多选择" + picNum + "张图片",
                            Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        setContentView(R.layout.activity_imagegrid);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("SelectNum")) {
            picNum = getIntent().getIntExtra("SelectNum", AppConstantValue.SELECT_IMG_NUM);
        }

        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        dataList = (List<ImageItem>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);

        initView();
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<String>();
                Collection<String> c = mAdapter.map.values();
                Iterator<String> it = c.iterator();
                for (; it.hasNext(); ) {
                    list.add(it.next());
                }

                List<String> pathList = new ArrayList<String>();
                for (int i = 0; i < list.size(); i++) {
                    if (pathList.size() < picNum) {
                        String path = list.get(i);
                        try {
                            Bitmap bitmap = CompressHelper.getDefault(ImageGridActivity.this).compressToBitmap(new File(path));
                            String fileName = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
                            BitmapUtil.saveToFile(bitmap, new File(SDFileUtil.getPhotoCachePath()), fileName);
                            pathList.add(SDFileUtil.getPhotoCachePath() + fileName + BitmapUtil.JPG_SUFFIX);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

                Intent intent = new Intent();
                intent.putExtra(EXTRA_IMAGE_LIST, (Serializable) pathList);
                setResult(getIntent().getIntExtra("requestCode", 0), intent);
                finish();
            }
        });
    }

    /**
     * 初始化
     */
    private void initView() {
        gvDisplay = (GridView) findViewById(R.id.gv_display);
        gvDisplay.setSelector(new ColorDrawable(Color.TRANSPARENT));
        Collections.reverse(dataList);// 列表反序
        mAdapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
                getIntent().getIntExtra("listSize", 0), picNum, mHandler);

        gvDisplay.setAdapter(mAdapter);
        mAdapter.setTextCallback(new ImageGridAdapter.TextCallback() {
            public void onListen(int count) {
                btnOk.setText("完成" + "(" + count + ")");
            }
        });

        gvDisplay.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mAdapter.notifyDataSetChanged();
            }

        });
    }
}