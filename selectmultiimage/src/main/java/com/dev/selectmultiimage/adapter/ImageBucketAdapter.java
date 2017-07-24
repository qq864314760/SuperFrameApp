package com.dev.selectmultiimage.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.selectmultiimage.R;
import com.dev.selectmultiimage.bean.ImageBucket;
import com.dev.selectmultiimage.utils.BitmapCacheUtil;

import java.util.List;


public class ImageBucketAdapter extends BaseAdapter {
    final String TAG = getClass().getSimpleName();

    Context context;
    /**
     * 图片集列表
     */
    List<ImageBucket> dataList;
    BitmapCacheUtil cache;
    BitmapCacheUtil.ImageCallback callback = new BitmapCacheUtil.ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                if (url != null && url.equals((String) imageView.getTag())) {
                    ((ImageView) imageView).setImageBitmap(bitmap);
                } else {
                    Log.e(TAG, "callback, bmp not match");
                }
            } else {
                Log.e(TAG, "callback, bmp null");
            }
        }
    };

    public ImageBucketAdapter(Context context, List<ImageBucket> list) {
        this.context = context;
        dataList = list;
        cache = new BitmapCacheUtil();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        int count = 0;
        if (dataList != null) {
            count = dataList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    class Holder {
        private ImageView ivDisplay;
        private ImageView ivSelected;
        private TextView tvName;
        private TextView tvCount;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        Holder holder;
        if (arg1 == null) {
            holder = new Holder();
            arg1 = View.inflate(context, R.layout.item_image_bucket, null);
            holder.ivDisplay = (ImageView) arg1.findViewById(R.id.iv_display);
            holder.ivSelected = (ImageView) arg1.findViewById(R.id.iv_isselected);
            holder.tvName = (TextView) arg1.findViewById(R.id.tv_name);
            holder.tvCount = (TextView) arg1.findViewById(R.id.tv_count);
            arg1.setTag(holder);
        } else {
            holder = (Holder) arg1.getTag();
        }
        ImageBucket item = dataList.get(arg0);
        holder.tvName.setText(item.bucketName);
        holder.tvCount.setText("" + item.count);
        holder.ivSelected.setVisibility(View.GONE);
        if (item.imageList != null && item.imageList.size() > 0) {
            String thumbPath = item.imageList.get(item.imageList.size() - 1).thumbnailPath;
            String sourcePath = item.imageList.get(item.imageList.size() - 1).imagePath;
            holder.ivDisplay.setTag(sourcePath);
            cache.displayBmp(context,holder.ivDisplay, thumbPath, sourcePath, callback);
        } else {
            holder.ivDisplay.setImageBitmap(null);
            Log.e(TAG, "no images in bucket " + item.bucketName);
        }
        return arg1;
    }

}
