package com.dev.selectmultiimage.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dev.selectmultiimage.R;

import java.io.File;
import java.util.List;



public class SelectGridAdapter extends BaseAdapter {
    private LayoutInflater listContainer;
    private Context mContext;
    private int selectedPosition = -1;
    private boolean shape;
    private int selectImgNum = 9;

    private List<String> pathList;
    private OnClickListener DelOnClickListener;

    public boolean isShape() {
        return shape;
    }

    public void setShape(boolean shape) {
        this.shape = shape;
    }

    public class ViewHolder {
        public ImageView ivDisplay;
        public Button btnDel;
    }

    public SelectGridAdapter(Context context, List<String> pathList) {
        this.mContext = context;
        listContainer = LayoutInflater.from(context);
        this.pathList = pathList;
    }

    public int getCount() {
        if (pathList.size() < selectImgNum) {
            return pathList.size() + 1;
        } else {
            return pathList.size();
        }
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {

        return 0;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setDelOnClickListener(OnClickListener DelOnClickListener) {
        this.DelOnClickListener = DelOnClickListener;
    }

    /**
     * ListView Item设置
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        // 自定义视图
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.item_select_grid, null);
            // 获取控件对象
            holder.ivDisplay = (ImageView) convertView.findViewById(R.id.iv_display);
            holder.btnDel = (Button) convertView.findViewById(R.id.btn_del);
            // 设置控件集到convertView
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == pathList.size()) {
            holder.ivDisplay.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.select_image));
            holder.btnDel.setVisibility(View.GONE);
            // 限定能上传几张图片
            if (position == selectImgNum) {
                holder.ivDisplay.setVisibility(View.GONE);
            }
        } else {
            if (pathList.get(position).contains("http")) {
                Log.e("网络图片绑定", pathList.get(position));
                Glide.with(mContext).load(pathList.get(position)).placeholder(R.drawable.no_image).into(holder.ivDisplay);
            } else {
                Log.e("本地图片绑定", pathList.get(position));
                Glide.with(mContext).load(new File(pathList.get(position))).placeholder(R.drawable.no_image).into(holder.ivDisplay);
            }

            holder.btnDel.setTag(position);
            if (DelOnClickListener != null) {
                holder.btnDel.setVisibility(View.VISIBLE);
                holder.btnDel.setOnClickListener(DelOnClickListener);
            } else {
                holder.btnDel.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
}
