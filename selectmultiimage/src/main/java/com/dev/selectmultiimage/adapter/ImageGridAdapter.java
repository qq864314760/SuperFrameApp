package com.dev.selectmultiimage.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.selectmultiimage.R;
import com.dev.selectmultiimage.bean.ImageItem;
import com.dev.selectmultiimage.config.AppConstantValue;
import com.dev.selectmultiimage.utils.BitmapCacheUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ImageGridAdapter extends BaseAdapter {
    private Context context;

    private TextCallback textcallback = null;
    final String TAG = getClass().getSimpleName();

    List<ImageItem> dataList;
    public Map<String, String> map = new HashMap<String, String>();
    BitmapCacheUtil cache;
    private Handler mHandler;
    private int selectTotal = 0;
    private int listSize = 0;

    private int select_img_num = AppConstantValue.SELECT_IMG_NUM;

    BitmapCacheUtil.ImageCallback callback = new BitmapCacheUtil.ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap,
                              Object... params) {
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

    public static interface TextCallback {
        public void onListen(int count);
    }

    public void setTextCallback(TextCallback listener) {
        textcallback = listener;
    }

    public ImageGridAdapter(Context context, List<ImageItem> list, int listSize, int select_img_num,
                            Handler mHandler) {
        this.context = context;
        dataList = list;
        this.listSize = listSize;
        cache = new BitmapCacheUtil();
        this.mHandler = mHandler;
        this.select_img_num = select_img_num;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (dataList != null) {
            count = dataList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    class Holder {
        private ImageView ivDisplay;
        private ImageView ivSelected;
        private TextView tvBorder;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(context, R.layout.item_image_grid, null);
            holder.ivDisplay = (ImageView) convertView.findViewById(R.id.iv_display);
            holder.ivSelected = (ImageView) convertView.findViewById(R.id.iv_isselected);
            holder.tvBorder = (TextView) convertView.findViewById(R.id.tv_border);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final ImageItem item = dataList.get(position);

        holder.ivDisplay.setTag(item.imagePath);
        cache.displayBmp(context, holder.ivDisplay, item.thumbnailPath, item.imagePath,
                callback);
        if (item.isSelected) {
            holder.ivSelected.setImageResource(R.drawable.icon_check_pressed);
            holder.tvBorder.setBackgroundResource(R.drawable.bg_red_border);
        } else {
            holder.ivSelected.setImageResource(R.drawable.icon_check_normal);
            holder.tvBorder.setBackgroundColor(Color.TRANSPARENT);
        }
        holder.ivDisplay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String path = dataList.get(position).imagePath;

                if ((listSize + selectTotal) < select_img_num) {
                    item.isSelected = !item.isSelected;
                    if (item.isSelected) {
                        holder.ivSelected.setImageResource(R.drawable.icon_check_pressed);
                        holder.tvBorder.setBackgroundResource(R.drawable.bg_red_border);
                        selectTotal++;
                        if (textcallback != null)
                            textcallback.onListen(selectTotal);
                        map.put(path, path);

                    } else if (!item.isSelected) {
                        holder.ivSelected.setImageResource(R.drawable.icon_check_normal);
                        holder.tvBorder.setBackgroundColor(Color.TRANSPARENT);
                        selectTotal--;
                        if (textcallback != null)
                            textcallback.onListen(selectTotal);
                        map.remove(path);
                    }
                } else if ((listSize + selectTotal) >= select_img_num) {
                    if (item.isSelected == true) {
                        item.isSelected = !item.isSelected;
                        holder.ivSelected.setImageResource(R.drawable.icon_check_normal);
                        holder.tvBorder.setBackgroundColor(Color.TRANSPARENT);
                        selectTotal--;
                        map.remove(path);

                    } else {
                        Message message = Message.obtain(mHandler, 0);
                        message.sendToTarget();
                    }
                }
            }

        });

        return convertView;
    }
}
