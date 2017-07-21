package com.dev.superframe.ui.sheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.superframe.R;


/**
 * Created by Clevo on 2016/6/7.
 */
public class GridViewAdapter extends BaseAdapter {

    Context context;
    String title[];
    int icons[];

    public GridViewAdapter(Context context, String[] title, int[] icons) {
        this.context = context;
        this.title = title;
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        return title[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_share, parent, false);
            holder = new ViewHolder();
            holder.adapter_share_image = (ImageView) convertView.findViewById(R.id.adapter_share_image);
            holder.adapter_share_text = (TextView) convertView.findViewById(R.id.adapter_share_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.adapter_share_text.setText(title[position]);
        holder.adapter_share_image.setImageResource(icons[position]);

        return convertView;
    }

    public static class ViewHolder {
        ImageView adapter_share_image;
        TextView adapter_share_text;
    }
}
