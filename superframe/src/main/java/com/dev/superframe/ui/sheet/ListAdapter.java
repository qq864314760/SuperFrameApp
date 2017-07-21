package com.dev.superframe.ui.sheet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dev.superframe.R;


/**
 * Created by Clevo on 2016/6/7.
 */
public class ListAdapter extends BaseAdapter {

    Context context;
    String title[];

    public ListAdapter(Context context, String[] title) {
        this.context = context;
        this.title = title;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_pop, parent, false);
            holder = new ViewHolder();
            holder.pop_desp = (TextView) convertView.findViewById(R.id.pop_desp);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.pop_desp.setTextColor(Color.parseColor("#ff33b5e5"));
        holder.pop_desp.setTextSize(16);
        holder.pop_desp.setText(title[position]);
        return convertView;
    }

    public static class ViewHolder {
        TextView pop_desp;
    }
}
