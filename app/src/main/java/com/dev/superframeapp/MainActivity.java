package com.dev.superframeapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dev.superframe.base.BaseActivity;
import com.dev.superframe.ui.dialog.KeyBoardDailog;
import com.dev.superframe.utils.glide.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;


public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_dailog)
    Button btnDailog;
    @BindView(R.id.iv_display)
    ImageView ivDisplay;

    private  KeyBoardDailog keyBoardDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSwipeBackLayout().setEnableGesture(false);//关闭右滑返回上一级
        setContentView(R.layout.activity_main);
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
        GlideUtil.loadImageViewCircle(getActivity(), "http://www.3vsheji.com/uploads/allimg/151222/1F92594D_0.jpg", R.drawable.icon_image_err, ivDisplay);
//        GlideUtil.loadImageViewBlur(getActivity(),"http://www.3vsheji.com/uploads/allimg/151222/1F92594D_0.jpg",ivDisplay);
//        GlideUtil.loadImageViewThumbnail(getActivity(),"http://www.3vsheji.com/uploads/allimg/151222/1F92594D_0.jpg",ivDisplay);
//        GlideUtil.loadImageViewGrayscale(getActivity(),"http://www.3vsheji.com/uploads/allimg/151222/1F92594D_0.jpg",R.drawable.icon_image_err,ivDisplay);
//        GlideUtil.loadImageViewAnim(getActivity(),"http://www.3vsheji.com/uploads/allimg/151222/1F92594D_0.jpg",R.anim.progress_loading,ivDisplay);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.btn_back, R.id.btn_dailog, R.id.btn_permisson, R.id.btn_pulltorefresh,R.id.btn_selectavatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                toActivity(new Intent(getActivity(), BackActivity.class));
                break;
            case R.id.btn_dailog:
                //toActivity(new Intent(getActivity(), SampleActivity.class), -1, true);

//                new AlertStyleDialog(getActivity(), "", "确定拨打客服电话:4008940067", true, 0, new AlertStyleDialog.OnDialogButtonClickListener() {
//                    @Override
//                    public void onDialogButtonClick(int requestCode, boolean isPositive) {
//                        if (isPositive) {
//                            //IntentSystemAppUtils.toCallPhone(getActivity(), "4008940067");
//                        }
//                    }
//                }).show();
//
//                new AlertDialog(getActivity(), "", "确定拨打客服电话:4008940067", true, 0, new AlertDialog.OnDialogButtonClickListener() {
//                    @Override
//                    public void onDialogButtonClick(int requestCode, boolean isPositive) {
//                        if (isPositive) {
//                            //IntentSystemAppUtils.toCallPhone(getActivity(), "4008940067");
//                        }
//                    }
//                }).show();

//                final String[] arr = new String[]{"微信", "朋友圈", "微博"};
//                final int[] arrRes = new int[]{R.drawable.ic_wechat, R.drawable.ic_wechat2, R.drawable.ic_sina};
//                SheetFragment.build(getSupportFragmentManager()).setChoice(SheetFragment.Builder.CHOICE.GRID).setTitle("分享到").setTag("")
//                        .setItems(arr).setImages(arrRes).setOnItemClickListener(new SheetFragment.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(int position) {
//                        showShortToast(arr[position] + "分享中...");
//                    }
//                }).show();

                new KeyBoardDailog("请回复", new KeyBoardDailog.SendBackListener() {
                    @Override
                    public void sendBack(String inputText) {
                        showShortToast(inputText);
                    }
                }).show(getSupportFragmentManager(), "");

                break;
            case R.id.btn_permisson:
                List<PermissionItem> permissionItems = new ArrayList<>();
                permissionItems.add(new PermissionItem(Manifest.permission.CAMERA, "照相机", R.drawable.permission_ic_camera));
                permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "位置", R.drawable.permission_ic_location));
                permissionItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "读取内存卡", R.drawable.permission_ic_storage));
                permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "写入内存卡", R.drawable.permission_ic_storage));
                permissionItems.add(new PermissionItem(Manifest.permission.READ_CONTACTS, "联系人", R.drawable.permission_ic_contacts));
                permissionItems.add(new PermissionItem(Manifest.permission.CALL_PHONE, "拨打电话", R.drawable.permission_ic_phone));
                HiPermission.create(getActivity()).permissions(permissionItems).checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        showShortToast("取消了权限授权请求");
                    }

                    @Override
                    public void onFinish() {
                        showShortToast("授权完成的所有权限");
                    }

                    @Override
                    public void onDeny(String permission, int position) {

                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });
                break;
            case R.id.btn_pulltorefresh:
                toActivity(new Intent(getActivity(), PullToRefreshActivity.class));
                break;
            case R.id.btn_selectavatar:
                toActivity(new Intent(getActivity(), AvatarActivity.class));
                break;
        }
    }
}
