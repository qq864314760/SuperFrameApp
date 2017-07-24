package com.dev.superframeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.dev.selectmultiimage.ImagePagerActivity;
import com.dev.selectmultiimage.utils.SDFileUtil;
import com.dev.selectmultiimage.utils.SelectMultiImageUtil;
import com.dev.superframe.base.BaseActivity;
import com.dev.superframe.config.AppConstantValue;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/21.
 */

public class MultiImageActivity extends BaseActivity {

    @BindView(R.id.gv_pics)
    GridView gvPics;
    @BindView(R.id.hsv_pics)
    HorizontalScrollView hsvPics;

    private List<String> urlList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiimage);
        ButterKnife.bind(this);

        initView();
        initData();
        initEvent();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        SelectMultiImageUtil.initSelectPictures(getActivity(), hsvPics, gvPics, urlList, AppConstantValue.SELECT_IMG_NUM, new DelClickListener());
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

        gvPics.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (position == urlList.size()) {
                        String sdcardState = Environment.getExternalStorageState();
                        if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
                            SelectMultiImageUtil.showSheet(getActivity(), getSupportFragmentManager(), urlList.size());
                        } else {
                            Toast.makeText(context, "SD卡已拔出，不能选择照片", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                        ImagePagerActivity.startImagePagerActivity(context, urlList, position, imageSize);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectMultiImageUtil.OpenCamera) {
            SelectMultiImageUtil.startPhotoZoom(getActivity(), SDFileUtil.getPhotoCachePath() + "temp.jpg");
        } else if (requestCode == SelectMultiImageUtil.OpenAlbum && data != null) {
            SelectMultiImageUtil.setAlbumToView(data, urlList);
            SelectMultiImageUtil.initSelectPictures(getActivity(), hsvPics, gvPics, urlList, AppConstantValue.SELECT_IMG_NUM, new DelClickListener());
        } else if (requestCode == SelectMultiImageUtil.OpenPreview) {
            if (data != null) {
                SelectMultiImageUtil.setPicToView(data, urlList);
                SelectMultiImageUtil.initSelectPictures(getActivity(), hsvPics, gvPics, urlList, AppConstantValue.SELECT_IMG_NUM, new DelClickListener());
            }
        }
    }

    private class DelClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            urlList.remove(Integer.parseInt(view.getTag().toString()));
            SelectMultiImageUtil.initSelectPictures(getActivity(), hsvPics, gvPics, urlList, AppConstantValue.SELECT_IMG_NUM, new DelClickListener());
        }
    }
}
