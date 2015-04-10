package com.example.xiner.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.xiner.R;
import com.example.xiner.util.DownloadPicUtil;
import com.example.xiner.util.HttpUtil;

import java.util.ArrayList;

/**
 * Created by xiner on 15-3-22.
 */
public class PictureAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> items;
    LayoutInflater layoutInflater;
    private static final String TAG="PictureAdapter";

    public PictureAdapter(Context context, ArrayList<String> items) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.gridview_picture, parent,false);
            holder = new ViewHolder();

            holder.imageView = (ImageView) convertView.findViewById(R.id.gridview_nopic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

            Log.v(TAG,"imageview"+items.get(position));
        DownloadPicUtil downloadPicUtil = new DownloadPicUtil(holder.imageView);
        downloadPicUtil.execute(HttpUtil.baseIp+items.get(position));


        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}
