package com.dev.superframeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.dev.selectavatar.utils.SDFileUtil;
import com.dev.selectavatar.utils.SelectAvatarUtil;
import com.dev.superframe.base.BaseActivity;
import com.dev.superframe.view.CircleImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/7/21.
 */

public class AvatarActivity extends BaseActivity {

    @BindView(R.id.civ_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.btn_selectavatar)
    Button btnSelectavatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);
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

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @OnClick(R.id.btn_selectavatar)
    public void onClick() {
        SelectAvatarUtil.showSheet(getActivity(), getSupportFragmentManager());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectAvatarUtil.OpenCamera) {
            SelectAvatarUtil.startPhotoZoom(getActivity(), SDFileUtil.getPhotoCachePath() + "temp.jpg");
        } else if (requestCode == SelectAvatarUtil.OpenAlbum) {
            SelectAvatarUtil.startPhotoZoom(getActivity(), SelectAvatarUtil.getRealFilePath(getActivity(), data.getData()));
        } else if (requestCode == SelectAvatarUtil.OpenPreview) {
            if (data != null) {
                String path = SelectAvatarUtil.setPicToView(data);
                File file = new File(path);
                Glide.with(this).load(file).into(civAvatar);
                //GlideUtil.loadImageViewBySource(getActivity(), R.drawable.no_pic, GlideUtil.SOURCCE_SD, path, civAvatar);
            }
        }
    }
}
