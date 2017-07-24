package com.dev.selectmultiimage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.dev.selectmultiimage.adapter.ImageBucketAdapter;
import com.dev.selectmultiimage.bean.ImageBucket;
import com.dev.selectmultiimage.config.AppConstantValue;
import com.dev.selectmultiimage.utils.AlbumHelper;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class ImageBucketActivity extends Activity {
    public static final String EXTRA_IMAGE_LIST = "imagelist";

    TextView tvCancel;
    GridView gvDisplay;

    List<ImageBucket> dataList;
    ImageBucketAdapter adapter;// 自定义的适配器
    AlbumHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        setContentView(R.layout.activity_imagebucket);

        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        initData();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        dataList = helper.getImagesBucketList(false);
    }

    /**
     * 初始化view视图
     */
    private void initView() {
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        gvDisplay = (GridView) findViewById(R.id.gv_display);
        Collections.reverse(dataList);// 列表反序
        adapter = new ImageBucketAdapter(ImageBucketActivity.this, dataList);
        gvDisplay.setAdapter(adapter);

        gvDisplay.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int iSelectNum = AppConstantValue.SELECT_IMG_NUM;
                if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("SelectNum")) {
                    iSelectNum = getIntent().getIntExtra("SelectNum", AppConstantValue.SELECT_IMG_NUM);
                }
                Intent intent = new Intent(ImageBucketActivity.this,ImageGridActivity.class).putExtra("SelectNum", iSelectNum);
                intent.putExtra("requestCode", getIntent().getIntExtra("requestCode", 0));
                intent.putExtra("listSize",getIntent().getIntExtra("listSize", 0));
                intent.putExtra(ImageBucketActivity.EXTRA_IMAGE_LIST,(Serializable) dataList.get(position).imageList);
                startActivityForResult(intent, getIntent().getIntExtra("requestCode", 0));
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getIntent().getIntExtra("requestCode", 0)) {
            if (data != null) {
                Intent intent = new Intent();
                @SuppressWarnings("unchecked")
                List<String> pathList = (List<String>) data
                        .getSerializableExtra(EXTRA_IMAGE_LIST);
                intent.putExtra(EXTRA_IMAGE_LIST, (Serializable) pathList);
                setResult(getIntent().getIntExtra("requestCode", 0), intent);
                finish();
            }
        }
    }
}
