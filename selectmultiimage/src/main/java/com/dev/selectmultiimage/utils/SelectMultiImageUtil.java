package com.dev.selectmultiimage.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.dev.selectmultiimage.ImageBucketActivity;
import com.dev.selectmultiimage.PreviewActivity;
import com.dev.selectmultiimage.R;
import com.dev.selectmultiimage.adapter.SelectGridAdapter;
import com.dev.selectmultiimage.view.sheet.SheetFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;


/**
 * Created by Administrator on 2017/5/5.
 */

public class SelectMultiImageUtil {
    public static final int OpenCamera = 1;//打开相机
    public static final int OpenAlbum = 2;//打开相册
    public static final int OpenPreview = 3;//跳转剪切页面

    private static SelectGridAdapter adapter;
    /**
     * 初始化多图选择
     */
    public static void initSelectPictures(final Context context, final HorizontalScrollView hsv,
                                          final GridView gv, final List<String> pathList, final int selectNum, final View.OnClickListener listener) {
        adapter = new SelectGridAdapter(context, pathList);
        adapter.setDelOnClickListener(listener);
        adapter.setSelectedPosition(0);
        int size = 0;
        if (pathList.size() < selectNum) {
            size = pathList.size() + 1;
        } else {
            size = pathList.size();
        }
        ViewGroup.LayoutParams params = gv.getLayoutParams();
        final int width = size * (int) (DensityUtil.dip2px(context, 10) * 9.4f);
        params.width = width;
        gv.setLayoutParams(params);
        gv.setColumnWidth((int) (DensityUtil.dip2px(context, 10) * 9.4f));
        gv.setStretchMode(GridView.NO_STRETCH);
        gv.setNumColumns(size);
        gv.setAdapter(adapter);

        hsv.getViewTreeObserver().addOnPreDrawListener(// 绘制完毕
                new ViewTreeObserver.OnPreDrawListener() {
                    public boolean onPreDraw() {
                        hsv.scrollTo(width, 0);
                        hsv.getViewTreeObserver().removeOnPreDrawListener(
                                this);
                        return false;
                    }
                });
    }

    /**
     * 弹出选择框
     */
    public static void showSheet(final Context context, final FragmentManager fragmentManager,final int listSize) {

        List<PermissionItem> permissionItems = new ArrayList<>();
        permissionItems.add(new PermissionItem(Manifest.permission.CAMERA, "照相机", R.drawable.permission_ic_camera));
        permissionItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "读取内存卡", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "写入内存卡", R.drawable.permission_ic_storage));
        HiPermission.create(context).permissions(permissionItems).checkMutiPermission(new PermissionCallback() {
            @Override
            public void onClose() {
                Toast.makeText(context, "取消了权限授权请求", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                final String[] arr = new String[]{"拍照", "从手机相册选择"};
                SheetFragment.build(fragmentManager)
                        .setItems(arr)
                        .setOnItemClickListener(new SheetFragment.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                if (position == 0) {
                                    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(SDFileUtil.getPhotoCachePath(), "temp.jpg")));
                                        ((Activity) context).startActivityForResult(openCameraIntent, OpenCamera);
                                    } else {
                                        Uri imageUri = FileProvider.getUriForFile(context, "com.selectmultiimage_camera_photos.fileprovider", new File(SDFileUtil.getPhotoCachePath(), "temp.jpg"));
                                        openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                        ((Activity) context).startActivityForResult(openCameraIntent, OpenCamera);
                                    }
                                } else if (position == 1) {
                                    Intent intent = new Intent(context, ImageBucketActivity.class);
                                    intent.putExtra("requestCode", OpenAlbum);
                                    intent.putExtra("listSize", listSize);
                                    ((Activity) context).startActivityForResult(intent, OpenAlbum);
                                }
                            }
                        }).show();
            }

            @Override
            public void onDeny(String permission, int position) {

            }

            @Override
            public void onGuarantee(String permission, int position) {

            }
        });
    }

    /**
     * 裁剪图片方法实现
     *
     * @param path
     */
    public static void startPhotoZoom(Context context, String path, int requestCode) {
        Intent intent = new Intent(context, PreviewActivity.class);
//        intent.setDataAndType(uri, "image/*");
        intent.putExtra("path", path);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    public static void setPicToView(Intent picdata, List<String> list) {
//        Bitmap bitmap = null;
//        byte[] bis = picdata.getByteArrayExtra("bitmap");
//        bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
//        String localImg = System.currentTimeMillis() + ".JPEG";
//
//        if (bitmap != null) {
//            SDPathUtils.saveBitmap(bitmap, localImg);
//            Log.e("保存图片", SDPathUtils.getCachePath() + localImg);
//        }
        String imgPath = picdata.getStringExtra("path");
        list.add(imgPath);
    }

    /**
     * 获取相册返回的列表
     */
    public static void setAlbumToView(Intent data, List<String> list) {
        if(data!=null) {
            List<String> pathList = (List<String>) data.getSerializableExtra("imagelist");
            //Collections.reverse(pathList);//反序
            list.addAll(pathList);
        }
    }

}
